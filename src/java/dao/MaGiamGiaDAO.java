package dao;

import model.LichSuMaGiamGia;
import model.MaGiamGia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBConnection;

public class MaGiamGiaDAO {

    private Connection getConnection() throws Exception {
        return DBConnection.getConnection();
    }

    public int getTongXuNguoiDung(int maNguoiDung) {
        String sql = "SELECT so_xu FROM nguoi_dung WHERE ma_nguoi_dung = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maNguoiDung);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("so_xu");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<MaGiamGia> getDanhSachMaSoHuu(int maNguoiDung) {
        List<MaGiamGia> list = new ArrayList<>();

        String sql = """
            SELECT 
                d.ma_doi,
                d.so_xu_da_doi,
                d.ngay_doi,
                d.trang_thai AS trang_thai_so_huu,
                m.ma_giam_gia,
                m.ma_code,
                m.ten_ma,
                m.gia_tri_giam,
                m.dieu_kien_toi_thieu,
                m.ngay_bat_dau,
                m.ngay_ket_thuc,
                m.so_luong,
                m.trang_thai,
                m.loai_giam,
                m.giam_toi_da,
                m.so_xu_can,
                m.hien_thi_doi_xu
            FROM doi_xu_ma_giam_gia d
            INNER JOIN ma_giam_gia m ON d.ma_giam_gia = m.ma_giam_gia
            WHERE d.ma_nguoi_dung = ?
            ORDER BY 
                CASE d.trang_thai
                    WHEN 'chua_dung' THEN 1
                    WHEN 'da_dung' THEN 2
                    WHEN 'het_han' THEN 3
                    ELSE 4
                END,
                d.ngay_doi DESC
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maNguoiDung);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapSoHuu(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<MaGiamGia> getDanhSachMaCoTheDoi() {
        List<MaGiamGia> list = new ArrayList<>();

        String sql = """
            SELECT 
                ma_giam_gia,
                ma_code,
                ten_ma,
                gia_tri_giam,
                dieu_kien_toi_thieu,
                ngay_bat_dau,
                ngay_ket_thuc,
                so_luong,
                trang_thai,
                loai_giam,
                giam_toi_da,
                so_xu_can,
                hien_thi_doi_xu
            FROM ma_giam_gia
            WHERE hien_thi_doi_xu = 1
              AND trang_thai = 'hoat_dong'
              AND (ngay_bat_dau IS NULL OR ngay_bat_dau <= NOW())
              AND (ngay_ket_thuc IS NULL OR ngay_ket_thuc >= NOW())
              AND so_luong > 0
            ORDER BY so_xu_can ASC, ngay_ket_thuc ASC
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapBase(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<LichSuMaGiamGia> getLichSuNhanVaDungMa(int maNguoiDung) {
    List<LichSuMaGiamGia> list = new ArrayList<>();

    String sql = """
        SELECT
            m.ma_code,
            m.ten_ma,
            d.ngay_doi,
            d.so_xu_da_doi,
            d.trang_thai
        FROM doi_xu_ma_giam_gia d
        INNER JOIN ma_giam_gia m ON d.ma_giam_gia = m.ma_giam_gia
        WHERE d.ma_nguoi_dung = ?
        ORDER BY d.ngay_doi DESC
    """;

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, maNguoiDung);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LichSuMaGiamGia item = new LichSuMaGiamGia();
                item.setLoaiLichSu("nhan_ma");
                item.setMaCode(rs.getString("ma_code"));
                item.setTenMa(rs.getString("ten_ma"));
                item.setThoiGian(rs.getTimestamp("ngay_doi"));
                item.setThayDoiXu(rs.getInt("so_xu_da_doi"));
                item.setSoTienGiam(null);
                item.setMoTa("Đổi " + rs.getInt("so_xu_da_doi") + " xu để nhận mã");
                item.setTrangThai(rs.getString("trang_thai"));

                list.add(item);
            }
        }

        System.out.println("===> LICH SU MA size = " + list.size());

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

    public String doiMaBangXu(int maNguoiDung, int maGiamGia) {
        Connection conn = null;

        String sqlUser = """
            SELECT so_xu
            FROM nguoi_dung
            WHERE ma_nguoi_dung = ?
            FOR UPDATE
        """;

        String sqlVoucher = """
            SELECT ma_giam_gia, ma_code, ten_ma, so_xu_can, so_luong, trang_thai, hien_thi_doi_xu, ngay_bat_dau, ngay_ket_thuc
            FROM ma_giam_gia
            WHERE ma_giam_gia = ?
            FOR UPDATE
        """;

        String sqlUpdateXu = """
            UPDATE nguoi_dung
            SET so_xu = so_xu - ?
            WHERE ma_nguoi_dung = ?
        """;

        String sqlUpdateStock = """
            UPDATE ma_giam_gia
            SET so_luong = so_luong - 1
            WHERE ma_giam_gia = ?
        """;

        String sqlInsertDoi = """
            INSERT INTO doi_xu_ma_giam_gia(ma_nguoi_dung, ma_giam_gia, so_xu_da_doi, ngay_doi, trang_thai)
            VALUES (?, ?, ?, CURRENT_TIMESTAMP, 'chua_dung')
        """;

        String sqlInsertLichSuXu = """
            INSERT INTO lich_su_xu(ma_nguoi_dung, loai_giao_dich, nguon_xu, so_xu, mo_ta, ngay_tao)
            VALUES (?, 'tru', 'doi_ma_giam_gia', ?, ?, CURRENT_TIMESTAMP)
        """;

        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            int soXuHienTai;
            try (PreparedStatement psUser = conn.prepareStatement(sqlUser)) {
                psUser.setInt(1, maNguoiDung);
                try (ResultSet rs = psUser.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return "not_found_user";
                    }
                    soXuHienTai = rs.getInt("so_xu");
                }
            }

            int soXuCan;
            int soLuong;
            String trangThai;
            int hienThiDoiXu;
            Timestamp ngayBatDau;
            Timestamp ngayKetThuc;
            String maCode;
            String tenMa;

            try (PreparedStatement psVoucher = conn.prepareStatement(sqlVoucher)) {
                psVoucher.setInt(1, maGiamGia);
                try (ResultSet rs = psVoucher.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return "not_found_voucher";
                    }

                    soXuCan = rs.getInt("so_xu_can");
                    soLuong = rs.getInt("so_luong");
                    trangThai = rs.getString("trang_thai");
                    hienThiDoiXu = rs.getInt("hien_thi_doi_xu");
                    ngayBatDau = rs.getTimestamp("ngay_bat_dau");
                    ngayKetThuc = rs.getTimestamp("ngay_ket_thuc");
                    maCode = rs.getString("ma_code");
                    tenMa = rs.getString("ten_ma");
                }
            }

            Timestamp now = new Timestamp(System.currentTimeMillis());

            if (hienThiDoiXu != 1) {
                conn.rollback();
                return "not_exchangeable";
            }

            if (!"hoat_dong".equals(trangThai)) {
                conn.rollback();
                return "inactive";
            }

            if (ngayBatDau != null && now.before(ngayBatDau)) {
                conn.rollback();
                return "not_started";
            }

            if (ngayKetThuc != null && now.after(ngayKetThuc)) {
                conn.rollback();
                return "expired";
            }

            if (soLuong <= 0) {
                conn.rollback();
                return "out_of_stock";
            }

            if (soXuHienTai < soXuCan) {
                conn.rollback();
                return "not_enough_xu";
            }

            try (PreparedStatement ps1 = conn.prepareStatement(sqlUpdateXu);
                 PreparedStatement ps2 = conn.prepareStatement(sqlUpdateStock);
                 PreparedStatement ps3 = conn.prepareStatement(sqlInsertDoi);
                 PreparedStatement ps4 = conn.prepareStatement(sqlInsertLichSuXu)) {

                ps1.setInt(1, soXuCan);
                ps1.setInt(2, maNguoiDung);
                ps1.executeUpdate();

                ps2.setInt(1, maGiamGia);
                ps2.executeUpdate();

                ps3.setInt(1, maNguoiDung);
                ps3.setInt(2, maGiamGia);
                ps3.setInt(3, soXuCan);
                ps3.executeUpdate();

                ps4.setInt(1, maNguoiDung);
                ps4.setInt(2, soXuCan);
                ps4.setString(3, "Đổi " + soXuCan + " xu để nhận mã " + maCode + (tenMa != null ? " - " + tenMa : ""));
                ps4.executeUpdate();
            }

            conn.commit();
            return "success";

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "error";
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception ignored) {}
        }
    }

    private MaGiamGia mapBase(ResultSet rs) throws SQLException {
        MaGiamGia item = new MaGiamGia();
        item.setMaGiamGia(rs.getInt("ma_giam_gia"));
        item.setMaCode(rs.getString("ma_code"));
        item.setTenMa(rs.getString("ten_ma"));
        item.setGiaTriGiam(rs.getBigDecimal("gia_tri_giam"));
        item.setDieuKienToiThieu(rs.getBigDecimal("dieu_kien_toi_thieu"));
        item.setNgayBatDau(rs.getTimestamp("ngay_bat_dau"));
        item.setNgayKetThuc(rs.getTimestamp("ngay_ket_thuc"));
        item.setSoLuong(rs.getInt("so_luong"));
        item.setTrangThai(rs.getString("trang_thai"));
        item.setLoaiGiam(rs.getString("loai_giam"));
        item.setGiamToiDa(rs.getBigDecimal("giam_toi_da"));
        item.setSoXuCan(rs.getInt("so_xu_can"));
        item.setHienThiDoiXu(rs.getInt("hien_thi_doi_xu") == 1);
        return item;
    }

    private MaGiamGia mapSoHuu(ResultSet rs) throws SQLException {
        MaGiamGia item = mapBase(rs);
        item.setMaDoi(rs.getInt("ma_doi"));
        item.setSoXuDaDoi(rs.getInt("so_xu_da_doi"));
        item.setNgayDoi(rs.getTimestamp("ngay_doi"));
        item.setTrangThaiSoHuu(rs.getString("trang_thai_so_huu"));
        return item;
    }
}
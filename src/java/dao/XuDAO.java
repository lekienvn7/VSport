package dao;

import model.KhoXu;
import model.LichSuXu;

import utils.DBConnection;
import java.sql.PreparedStatement;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class XuDAO {

    private static final int XU_DANG_NHAP_HANG_NGAY = 100;

    private Connection getConnection() throws Exception {
        return DBConnection.getConnection(); // sửa theo project của m
    }

    public int getTongXu(int maNguoiDung) {
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

    public Date getLanNhanXuDangNhapCuoi(int maNguoiDung) {
        String sql = "SELECT lan_nhan_xu_dang_nhap_cuoi FROM nguoi_dung WHERE ma_nguoi_dung = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maNguoiDung);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDate("lan_nhan_xu_dang_nhap_cuoi");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean daNhanXuHomNay(int maNguoiDung) {
        Date lastDate = getLanNhanXuDangNhapCuoi(maNguoiDung);
        if (lastDate == null) return false;
        return lastDate.toLocalDate().isEqual(LocalDate.now());
    }

    public List<Boolean> getTrangThaiDiemDanh7Ngay(int maNguoiDung) {
        List<Boolean> result = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            result.add(false);
        }

        String sql = """
            SELECT DISTINCT DATE(ngay_tao) AS ngay_nhan
            FROM lich_su_xu
            WHERE ma_nguoi_dung = ?
              AND nguon_xu = 'dang_nhap_hang_ngay'
              AND DATE(ngay_tao) BETWEEN DATE_SUB(CURDATE(), INTERVAL 6 DAY) AND CURDATE()
            ORDER BY ngay_nhan ASC
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maNguoiDung);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate ngayNhan = rs.getDate("ngay_nhan").toLocalDate();
                    long diff = java.time.temporal.ChronoUnit.DAYS.between(
                            LocalDate.now().minusDays(6), ngayNhan
                    );

                    if (diff >= 0 && diff <= 6) {
                        result.set((int) diff, true);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public KhoXu getKhoXuInfo(int maNguoiDung) {
        KhoXu info = new KhoXu();
        info.setTongXu(getTongXu(maNguoiDung));
        info.setDaNhanHomNay(daNhanXuHomNay(maNguoiDung));
        info.setMucThuongHomNay(XU_DANG_NHAP_HANG_NGAY);
        info.setTrangThai7Ngay(getTrangThaiDiemDanh7Ngay(maNguoiDung));

        int soNgayDaNhan = 0;
        for (Boolean b : info.getTrangThai7Ngay()) {
            if (Boolean.TRUE.equals(b)) soNgayDaNhan++;
        }

        int ngayThu = info.isDaNhanHomNay() ? soNgayDaNhan : soNgayDaNhan + 1;
        if (ngayThu > 7) ngayThu = 7;
        info.setNgayThu(ngayThu);

        return info;
    }

    public List<LichSuXu> getLichSuXu(int maNguoiDung) {
        List<LichSuXu> list = new ArrayList<>();

        String sql = """
            SELECT ma_lich_su_xu, ma_nguoi_dung, loai_giao_dich, nguon_xu, so_xu, mo_ta, ngay_tao
            FROM lich_su_xu
            WHERE ma_nguoi_dung = ?
            ORDER BY ngay_tao DESC
            LIMIT 30
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maNguoiDung);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LichSuXu item = new LichSuXu();
                    item.setMaLichSuXu(rs.getInt("ma_lich_su_xu"));
                    item.setMaNguoiDung(rs.getInt("ma_nguoi_dung"));
                    item.setLoaiGiaoDich(rs.getString("loai_giao_dich"));
                    item.setNguonXu(rs.getString("nguon_xu"));
                    item.setSoXu(rs.getInt("so_xu"));
                    item.setMoTa(rs.getString("mo_ta"));
                    item.setNgayTao(rs.getTimestamp("ngay_tao"));
                    list.add(item);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean nhanXuDangNhapHangNgay(int maNguoiDung) {
        String sqlSelect = """
            SELECT so_xu, lan_nhan_xu_dang_nhap_cuoi
            FROM nguoi_dung
            WHERE ma_nguoi_dung = ?
            FOR UPDATE
        """;

        String sqlUpdateUser = """
            UPDATE nguoi_dung
            SET so_xu = so_xu + ?,
                lan_nhan_xu_dang_nhap_cuoi = CURDATE()
            WHERE ma_nguoi_dung = ?
        """;

        String sqlInsertHistory = """
            INSERT INTO lich_su_xu (ma_nguoi_dung, loai_giao_dich, nguon_xu, so_xu, mo_ta, ngay_tao)
            VALUES (?, 'cong', 'dang_nhap_hang_ngay', ?, ?, CURRENT_TIMESTAMP)
        """;

        Connection conn = null;
        PreparedStatement psSelect = null;
        PreparedStatement psUpdate = null;
        PreparedStatement psInsert = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            psSelect = conn.prepareStatement(sqlSelect);
            psSelect.setInt(1, maNguoiDung);
            rs = psSelect.executeQuery();

            if (!rs.next()) {
                conn.rollback();
                return false;
            }

            Date lastDate = rs.getDate("lan_nhan_xu_dang_nhap_cuoi");
            if (lastDate != null && lastDate.toLocalDate().isEqual(LocalDate.now())) {
                conn.rollback();
                return false;
            }

            psUpdate = conn.prepareStatement(sqlUpdateUser);
            psUpdate.setInt(1, XU_DANG_NHAP_HANG_NGAY);
            psUpdate.setInt(2, maNguoiDung);
            int updateRow = psUpdate.executeUpdate();

            psInsert = conn.prepareStatement(sqlInsertHistory);
            psInsert.setInt(1, maNguoiDung);
            psInsert.setInt(2, XU_DANG_NHAP_HANG_NGAY);
            psInsert.setString(3, "Nhận " + XU_DANG_NHAP_HANG_NGAY + " xu đăng nhập hằng ngày");
            int insertRow = psInsert.executeUpdate();

            if (updateRow > 0 && insertRow > 0) {
                conn.commit();
                return true;
            }

            conn.rollback();
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (psSelect != null) psSelect.close(); } catch (Exception ignored) {}
            try { if (psUpdate != null) psUpdate.close(); } catch (Exception ignored) {}
            try { if (psInsert != null) psInsert.close(); } catch (Exception ignored) {}
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception ignored) {}
        }
    }
}
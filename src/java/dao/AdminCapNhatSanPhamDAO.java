package dao;

import utils.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AdminCapNhatSanPhamDAO {

    private final VonShopDAO vonShopDAO = new VonShopDAO();
    private static final BigDecimal CHENH_LECH_GIA_NHAP = new BigDecimal("100000");

    public void capNhatSanPham(
        int maSanPham,
        String tenSanPham,
        Integer maDanhMuc,
        Integer maThuongHieu,
        Integer maDoiBong,
        String moTaNgan,
        String moTaChiTiet,
        BigDecimal giaKhuyenMai,
        String trangThai,
        Integer nhomSanPham,
        Integer maBoSuuTap,
        String[] maBienTheArr,
        String[] maSizeArr,
        String[] soLuongTonArr,
        String[] giaRiengArr,
        String[] maAnhArr,
        String[] anhPhuArr
) throws Exception {

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Tự cộng vốn theo ngày nếu cần
            vonShopDAO.congVonHangNgayNeuCan(conn);

            // 2. Lấy giá niêm yết thật từ DB, không tin dữ liệu frontend
            BigDecimal giaNiemYetDB = getGiaNiemYet(conn, maSanPham);

            BigDecimal giaNhapTuDong = giaNiemYetDB.subtract(CHENH_LECH_GIA_NHAP);
            if (giaNhapTuDong.compareTo(BigDecimal.ZERO) < 0) {
                giaNhapTuDong = BigDecimal.ZERO;
            }

            // 3. Lấy tồn kho cũ theo size
            Map<Integer, Integer> oldStockBySize = getOldStockBySize(conn, maSanPham);

            // 4. Tính tổng số lượng tăng thêm
            int tongSoLuongTangThem = tinhTongSoLuongTangThem(
                    oldStockBySize,
                    maSizeArr,
                    soLuongTonArr
            );

            // 5. Nếu có nhập thêm hàng thì trừ vốn
            if (tongSoLuongTangThem > 0 && giaNhapTuDong.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal tongTienNhapThem = giaNhapTuDong.multiply(BigDecimal.valueOf(tongSoLuongTangThem));

                vonShopDAO.truVonNhapHang(
                        conn,
                        tongTienNhapThem,
                        maSanPham,
                        "Cập nhật sản phẩm, nhập thêm " + tongSoLuongTangThem + " sản phẩm vào kho"
                );

                insertPhieuNhapCapNhat(
                        conn,
                        maSanPham,
                        tongSoLuongTangThem,
                        giaNhapTuDong,
                        tongTienNhapThem
                );
            }

            // 6. Update bảng san_pham
            updateSanPham(
                    conn,
                    maSanPham,
                    tenSanPham,
                    maDanhMuc,
                    maThuongHieu,
                    maDoiBong,
                    moTaNgan,
                    moTaChiTiet,
                    giaKhuyenMai,
                    trangThai,
                    nhomSanPham,
                    maBoSuuTap
            );

            // 7. Update lại biến thể
            xoaBienTheCu(conn, maSanPham);
            insertBienTheMoi(conn, maSanPham, maSizeArr, soLuongTonArr, giaRiengArr);

            // 8. Update lại ảnh phụ
            xoaAnhPhuCu(conn, maSanPham);
            insertAnhPhuMoi(conn, maSanPham, anhPhuArr);

            conn.commit();

        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    private void updateSanPham(
            Connection conn,
            int maSanPham,
            String tenSanPham,
            Integer maDanhMuc,
            Integer maThuongHieu,
            Integer maDoiBong,
            String moTaNgan,
            String moTaChiTiet,
            BigDecimal giaKhuyenMai,
            String trangThai,
            Integer nhomSanPham,
            Integer maBoSuuTap
    ) throws SQLException {

        String sql = """
            UPDATE san_pham
            SET ten_san_pham = ?,
                ma_danh_muc = ?,
                ma_thuong_hieu = ?,
                ma_doi_bong = ?,
                mo_ta_ngan = ?,
                mo_ta_chi_tiet = ?,
                gia_khuyen_mai = ?,
                trang_thai = ?,
                nhom_san_pham = ?,
                ma_bo_suu_tap = ?
            WHERE ma_san_pham = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenSanPham);

            if (maDanhMuc != null) {
                ps.setInt(2, maDanhMuc);
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            if (maThuongHieu != null) {
                ps.setInt(3, maThuongHieu);
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            if (maDoiBong != null) {
                ps.setInt(4, maDoiBong);
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            ps.setString(5, moTaNgan);
            ps.setString(6, moTaChiTiet);

            if (giaKhuyenMai != null) {
                ps.setBigDecimal(7, giaKhuyenMai);
            } else {
                ps.setNull(7, Types.DECIMAL);
            }

            ps.setString(8, trangThai);

            if (nhomSanPham != null) {
                ps.setInt(9, nhomSanPham);
            } else {
                ps.setNull(9, Types.INTEGER);
            }

            if (maBoSuuTap != null) {
                ps.setInt(10, maBoSuuTap);
            } else {
                ps.setNull(10, Types.INTEGER);
            }

            ps.setInt(11, maSanPham);
            ps.executeUpdate();
        }
    }

    private int tinhTongSoLuongTangThem(
            Map<Integer, Integer> oldStockBySize,
            String[] maSizeArr,
            String[] soLuongTonArr
    ) {
        int tongSoLuongTangThem = 0;

        if (maSizeArr == null || soLuongTonArr == null) {
            return 0;
        }

        int length = Math.min(maSizeArr.length, soLuongTonArr.length);

        for (int i = 0; i < length; i++) {
            String maSizeStr = maSizeArr[i];
            String soLuongStr = soLuongTonArr[i];

            if (maSizeStr == null || maSizeStr.trim().isEmpty()) continue;
            if (soLuongStr == null || soLuongStr.trim().isEmpty()) continue;

            int maSize = Integer.parseInt(maSizeStr.trim());
            int soLuongMoi = Integer.parseInt(soLuongStr.trim());
            int soLuongCu = oldStockBySize.getOrDefault(maSize, 0);

            if (soLuongMoi > soLuongCu) {
                tongSoLuongTangThem += (soLuongMoi - soLuongCu);
            }
        }

        return tongSoLuongTangThem;
    }

    private void xoaBienTheCu(Connection conn, int maSanPham) throws SQLException {
        String sql = "DELETE FROM bien_the_san_pham WHERE ma_san_pham = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSanPham);
            ps.executeUpdate();
        }
    }

    private void insertBienTheMoi(
            Connection conn,
            int maSanPham,
            String[] maSizeArr,
            String[] soLuongTonArr,
            String[] giaRiengArr
    ) throws SQLException {

        if (maSizeArr == null || soLuongTonArr == null) {
            return;
        }

        String sql = """
            INSERT INTO bien_the_san_pham (ma_san_pham, ma_size, so_luong_ton, gia_rieng)
            VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int length = Math.min(maSizeArr.length, soLuongTonArr.length);

            for (int i = 0; i < length; i++) {
                String maSizeStr = maSizeArr[i];
                String soLuongStr = soLuongTonArr[i];
                String giaRiengStr = (giaRiengArr != null && i < giaRiengArr.length) ? giaRiengArr[i] : null;

                if (maSizeStr == null || maSizeStr.trim().isEmpty()) continue;
                if (soLuongStr == null || soLuongStr.trim().isEmpty()) continue;

                ps.setInt(1, maSanPham);
                ps.setInt(2, Integer.parseInt(maSizeStr.trim()));
                ps.setInt(3, Integer.parseInt(soLuongStr.trim()));

                if (giaRiengStr != null && !giaRiengStr.trim().isEmpty()) {
                    ps.setBigDecimal(4, new BigDecimal(giaRiengStr.trim()));
                } else {
                    ps.setNull(4, Types.DECIMAL);
                }

                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    private void xoaAnhPhuCu(Connection conn, int maSanPham) throws SQLException {
        String sql = """
            DELETE FROM anh_san_pham
            WHERE ma_san_pham = ?
              AND (la_anh_chinh = 0 OR la_anh_chinh IS NULL)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSanPham);
            ps.executeUpdate();
        }
    }

    private void insertAnhPhuMoi(
            Connection conn,
            int maSanPham,
            String[] anhPhuArr
    ) throws SQLException {

        if (anhPhuArr == null) {
            return;
        }

        String sql = """
            INSERT INTO anh_san_pham (ma_san_pham, duong_dan_anh, la_anh_chinh)
            VALUES (?, ?, 0)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String anh : anhPhuArr) {
                if (anh == null || anh.isBlank()) continue;

                ps.setInt(1, maSanPham);
                ps.setString(2, anh.trim());
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    private Map<Integer, Integer> getOldStockBySize(Connection conn, int maSanPham) throws SQLException {
        Map<Integer, Integer> map = new HashMap<>();

        String sql = """
            SELECT ma_size, so_luong_ton
            FROM bien_the_san_pham
            WHERE ma_san_pham = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSanPham);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getInt("ma_size"), rs.getInt("so_luong_ton"));
                }
            }
        }

        return map;
    }

    private BigDecimal getGiaNiemYet(Connection conn, int maSanPham) throws SQLException {
        String sql = """
            SELECT gia_niem_yet
            FROM san_pham
            WHERE ma_san_pham = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSanPham);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BigDecimal giaNiemYet = rs.getBigDecimal("gia_niem_yet");
                    return giaNiemYet != null ? giaNiemYet : BigDecimal.ZERO;
                }
            }
        }

        throw new SQLException("Không tìm thấy giá niêm yết của sản phẩm.");
    }

    private void insertPhieuNhapCapNhat(
            Connection conn,
            int maSanPham,
            int tongSoLuongTangThem,
            BigDecimal giaNhapThem,
            BigDecimal tongTienNhapThem
    ) throws SQLException {

        String sql = """
            INSERT INTO phieu_nhap_san_pham
            (ma_san_pham, tong_so_luong, gia_nhap_goc, phan_tram_giam, don_gia_nhap_thuc_te, tong_tien_nhap, ghi_chu)
            VALUES (?, ?, ?, 0, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSanPham);
            ps.setInt(2, tongSoLuongTangThem);
            ps.setBigDecimal(3, giaNhapThem);
            ps.setBigDecimal(4, giaNhapThem);
            ps.setBigDecimal(5, tongTienNhapThem);
            ps.setString(6, "Nhập thêm hàng khi cập nhật biến thể sản phẩm");
            ps.executeUpdate();
        }
    }
}
package dao;

import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class VonShopDAO {

    public void khoiTaoQuyVonNeuChuaCo(Connection conn) throws SQLException {
        String checkSql = "SELECT ma_quy_von FROM quy_von_shop LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(checkSql);
             ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) {
                String insertSql = """
                    INSERT INTO quy_von_shop (so_du_hien_tai, so_tien_cong_moi_ngay, ngay_cap_nhat_cong_cuoi)
                    VALUES (0, 20000000, CURDATE())
                """;
                try (PreparedStatement ips = conn.prepareStatement(insertSql)) {
                    ips.executeUpdate();
                }
            }
        }
    }

    public void congVonHangNgayNeuCan(Connection conn) throws SQLException {
        khoiTaoQuyVonNeuChuaCo(conn);

        String sql = """
            SELECT ma_quy_von, so_du_hien_tai, so_tien_cong_moi_ngay, ngay_cap_nhat_cong_cuoi
            FROM quy_von_shop
            ORDER BY ma_quy_von ASC
            LIMIT 1
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (!rs.next()) return;

            int maQuyVon = rs.getInt("ma_quy_von");
            BigDecimal soDuHienTai = rs.getBigDecimal("so_du_hien_tai");
            BigDecimal soTienCongMoiNgay = rs.getBigDecimal("so_tien_cong_moi_ngay");
            Date ngayCuoiSql = rs.getDate("ngay_cap_nhat_cong_cuoi");

            LocalDate homNay = LocalDate.now();
            LocalDate ngayCuoi = (ngayCuoiSql != null) ? ngayCuoiSql.toLocalDate() : homNay;

            long soNgayCanCong = ChronoUnit.DAYS.between(ngayCuoi, homNay);

            if (soNgayCanCong <= 0) return;

            BigDecimal tongTienCong = soTienCongMoiNgay.multiply(BigDecimal.valueOf(soNgayCanCong));
            BigDecimal soDuMoi = soDuHienTai.add(tongTienCong);

            String updateSql = """
                UPDATE quy_von_shop
                SET so_du_hien_tai = ?, ngay_cap_nhat_cong_cuoi = ?, ngay_cap_nhat = NOW()
                WHERE ma_quy_von = ?
            """;
            try (PreparedStatement ups = conn.prepareStatement(updateSql)) {
                ups.setBigDecimal(1, soDuMoi);
                ups.setDate(2, Date.valueOf(homNay));
                ups.setInt(3, maQuyVon);
                ups.executeUpdate();
            }

            String insertLog = """
                INSERT INTO lich_su_von_shop
                (ma_quy_von, loai_bien_dong, so_tien_bien_dong, so_du_truoc, so_du_sau, noi_dung)
                VALUES (?, 'cong_hoan_von', ?, ?, ?, ?)
            """;
            try (PreparedStatement ips = conn.prepareStatement(insertLog)) {
                ips.setInt(1, maQuyVon);
                ips.setBigDecimal(2, tongTienCong);
                ips.setBigDecimal(3, soDuHienTai);
                ips.setBigDecimal(4, soDuMoi);
                ips.setString(5, "Tự động cộng vốn " + soNgayCanCong + " ngày");
                ips.executeUpdate();
            }
        }
    }

    public BigDecimal getSoDuHienTai(Connection conn) throws SQLException {
        String sql = "SELECT so_du_hien_tai FROM quy_von_shop ORDER BY ma_quy_von ASC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getBigDecimal("so_du_hien_tai");
        }
        return BigDecimal.ZERO;
    }

    public int getMaQuyVon(Connection conn) throws SQLException {
        String sql = "SELECT ma_quy_von FROM quy_von_shop ORDER BY ma_quy_von ASC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("ma_quy_von");
        }
        throw new SQLException("Không tìm thấy quỹ vốn shop.");
    }

    public void truVonNhapHang(Connection conn, BigDecimal soTienTru, int maSanPham, String noiDung) throws SQLException {
        congVonHangNgayNeuCan(conn);

        int maQuyVon = getMaQuyVon(conn);
        BigDecimal soDuTruoc = getSoDuHienTai(conn);

        if (soDuTruoc.compareTo(soTienTru) < 0) {
            throw new SQLException("Vốn shop không đủ để nhập hàng. Cần: " + soTienTru + ", hiện có: " + soDuTruoc);
        }

        BigDecimal soDuSau = soDuTruoc.subtract(soTienTru);

        String updateSql = """
            UPDATE quy_von_shop
            SET so_du_hien_tai = ?, ngay_cap_nhat = NOW()
            WHERE ma_quy_von = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
            ps.setBigDecimal(1, soDuSau);
            ps.setInt(2, maQuyVon);
            ps.executeUpdate();
        }

        String logSql = """
            INSERT INTO lich_su_von_shop
            (ma_quy_von, loai_bien_dong, so_tien_bien_dong, so_du_truoc, so_du_sau, noi_dung, ma_san_pham)
            VALUES (?, 'tru_von_nhap_hang', ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(logSql)) {
            ps.setInt(1, maQuyVon);
            ps.setBigDecimal(2, soTienTru);
            ps.setBigDecimal(3, soDuTruoc);
            ps.setBigDecimal(4, soDuSau);
            ps.setString(5, noiDung);
            ps.setInt(6, maSanPham);
            ps.executeUpdate();
        }
    }
}
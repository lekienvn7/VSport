package dao;

import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AdminXoaSanPhamDAO {

    public void xoaSanPham(int maSanPham) throws Exception {
    try (Connection conn = DBConnection.getConnection()) {
        conn.setAutoCommit(false);

        try {
            xoaAnhPhu(conn, maSanPham);
            xoaBienThe(conn, maSanPham);
            xoaChiTietSanPham(conn, maSanPham);
            xoaPhieuNhapLienQuan(conn, maSanPham);
            xoaLichSuVonLienQuan(conn, maSanPham);
            xoaSanPhamChinh(conn, maSanPham);

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        }
    }
}

    private void xoaAnhPhu(Connection conn, int maSanPham) throws Exception {
        String sql = "DELETE FROM anh_san_pham WHERE ma_san_pham = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSanPham);
            ps.executeUpdate();
        }
    }

    private void xoaBienThe(Connection conn, int maSanPham) throws Exception {
        String sql = "DELETE FROM bien_the_san_pham WHERE ma_san_pham = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSanPham);
            ps.executeUpdate();
        }
    }

    private void xoaChiTietSanPham(Connection conn, int maSanPham) throws Exception {
        String sql = "DELETE FROM chi_tiet_san_pham WHERE ma_san_pham = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSanPham);
            ps.executeUpdate();
        }
    }

    private void xoaPhieuNhapLienQuan(Connection conn, int maSanPham) throws Exception {
        String sql = "DELETE FROM phieu_nhap_san_pham WHERE ma_san_pham = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSanPham);
            ps.executeUpdate();
        }
    }

    private void xoaLichSuVonLienQuan(Connection conn, int maSanPham) throws Exception {
        String sql = "DELETE FROM lich_su_von_shop WHERE ma_san_pham = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSanPham);
            ps.executeUpdate();
        }
    }

    private void xoaSanPhamChinh(Connection conn, int maSanPham) throws Exception {
        String sql = "DELETE FROM san_pham WHERE ma_san_pham = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSanPham);
            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new IllegalArgumentException("Không tìm thấy sản phẩm để xóa.");
            }
        }
    }
}
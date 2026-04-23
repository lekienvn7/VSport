package dao;

import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardStatsDAO {

    // =========================
    // 1. ĐƠN HÀNG
    // =========================

    public int getTongDonHangDaGiao() {
        String sql = """
            SELECT COUNT(*)
            FROM don_hang
            WHERE trang_thai_don_hang = 'da_giao'
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getTongDonHangDaGiaoTuanNay() {
        String sql = """
            SELECT COUNT(*)
            FROM don_hang
            WHERE trang_thai_don_hang = 'da_giao'
              AND DATE(ngay_giao_thanh_cong) >= DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY)
              AND DATE(ngay_giao_thanh_cong) < DATE_ADD(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 7 DAY)
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getTongDonHangDaGiaoTuanTruoc() {
        String sql = """
            SELECT COUNT(*)
            FROM don_hang
            WHERE trang_thai_don_hang = 'da_giao'
              AND DATE(ngay_giao_thanh_cong) >= DATE_SUB(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 7 DAY)
              AND DATE(ngay_giao_thanh_cong) < DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY)
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // =========================
    // 2. DOANH THU
    // =========================

    public double getTongDoanhThu() {
        String sql = """
            SELECT COALESCE(SUM(tong_tien_don_hang), 0)
            FROM doanh_thu_shop
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public double getDoanhThuTuanNay() {
        String sql = """
            SELECT COALESCE(SUM(tong_tien_don_hang), 0)
            FROM doanh_thu_shop
            WHERE DATE(ngay_ghi_nhan) >= DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY)
              AND DATE(ngay_ghi_nhan) < DATE_ADD(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 7 DAY)
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public double getDoanhThuTuanTruoc() {
        String sql = """
            SELECT COALESCE(SUM(tong_tien_don_hang), 0)
            FROM doanh_thu_shop
            WHERE DATE(ngay_ghi_nhan) >= DATE_SUB(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 7 DAY)
              AND DATE(ngay_ghi_nhan) < DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY)
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // =========================
    // 3. TIỀN NHẬP HÀNG
    // =========================

    public double getTongTienNhap() {
        String sql = """
            SELECT COALESCE(SUM(tong_tien_nhap), 0)
            FROM phieu_nhap_san_pham
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public double getTienNhapTuanNay() {
        String sql = """
            SELECT COALESCE(SUM(tong_tien_nhap), 0)
            FROM phieu_nhap_san_pham
            WHERE DATE(ngay_tao) >= DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY)
              AND DATE(ngay_tao) < DATE_ADD(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 7 DAY)
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public double getTienNhapTuanTruoc() {
        String sql = """
            SELECT COALESCE(SUM(tong_tien_nhap), 0)
            FROM phieu_nhap_san_pham
            WHERE DATE(ngay_tao) >= DATE_SUB(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 7 DAY)
              AND DATE(ngay_tao) < DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY)
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // =========================
    // 4. THÀNH VIÊN
    // =========================

    public int getTongThanhVien() {
        String sql = """
            SELECT COUNT(*)
            FROM nguoi_dung
            WHERE vai_tro = 'khach_hang'
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getThanhVienMoiTuanNay() {
        String sql = """
            SELECT COUNT(*)
            FROM nguoi_dung
            WHERE vai_tro = 'khach_hang'
              AND DATE(ngay_tao) >= DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY)
              AND DATE(ngay_tao) < DATE_ADD(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 7 DAY)
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getThanhVienMoiTuanTruoc() {
        String sql = """
            SELECT COUNT(*)
            FROM nguoi_dung
            WHERE vai_tro = 'khach_hang'
              AND DATE(ngay_tao) >= DATE_SUB(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 7 DAY)
              AND DATE(ngay_tao) < DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY)
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
package dao;

import model.TraHang;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TraHangDAO {

    // =========================================================
    // KHÁCH HÀNG
    // =========================================================
    public boolean taoYeuCauTraHang(TraHang traHang) {
        String sqlCheck = "SELECT COUNT(*) FROM tra_hang_hoan_tien WHERE ma_don_hang = ? AND trang_thai IN ('cho_xu_ly', 'da_hoan')";
        String sqlInsert = "INSERT INTO tra_hang_hoan_tien (ma_don_hang, ly_do, so_tien_hoan, trang_thai, ghi_chu) VALUES (?, ?, ?, 'cho_xu_ly', ?)";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sqlCheck);
            ps.setInt(1, traHang.getMaDonHang());
            rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                conn.rollback();
                return false;
            }
            rs.close();
            ps.close();

            ps = conn.prepareStatement(sqlInsert);
            ps.setInt(1, traHang.getMaDonHang());
            ps.setString(2, traHang.getLyDo());
            ps.setDouble(3, traHang.getSoTienHoan());
            ps.setString(4, traHang.getGhiChu());
            int result = ps.executeUpdate();

            if (result > 0) {
                capNhatTrangThaiDonHang(conn, traHang.getMaDonHang(), "cho_tra_hang");
                conn.commit();
                return true;
            }

            conn.rollback();
            return false;

        } catch (SQLException e) {
            rollbackQuietly(conn);
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, ps, rs);
        }
    }

    // =========================================================
    // ADMIN - DUYET / TU CHOI THU CONG
    // =========================================================
    public boolean duyetTraHang(int maTraHang) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            ps = conn.prepareStatement("SELECT * FROM tra_hang_hoan_tien WHERE ma_tra_hang = ? AND trang_thai = 'cho_xu_ly'");
            ps.setInt(1, maTraHang);
            rs = ps.executeQuery();

            if (rs.next()) {
                TraHang traHang = mapResultSet(rs);
                rs.close();
                ps.close();

                if (traHang.isHoanTonKho()) {
                    hoanTonKho(conn, traHang.getMaDonHang());
                }

                capNhatTrangThaiTraHang(conn, maTraHang, "da_hoan");
                capNhatTrangThaiDonHang(conn, traHang.getMaDonHang(), "da_tra_hang");
                conn.commit();
                return true;
            }

            conn.rollback();
            return false;

        } catch (SQLException e) {
            rollbackQuietly(conn);
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, ps, rs);
        }
    }

    public boolean tuChoiTraHang(int maTraHang) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            ps = conn.prepareStatement("SELECT ma_don_hang FROM tra_hang_hoan_tien WHERE ma_tra_hang = ?");
            ps.setInt(1, maTraHang);
            rs = ps.executeQuery();

            if (rs.next()) {
                int maDonHang = rs.getInt("ma_don_hang");
                rs.close();
                ps.close();

                capNhatTrangThaiTraHang(conn, maTraHang, "tu_choi");
                capNhatTrangThaiDonHang(conn, maDonHang, "da_giao");
                conn.commit();
                return true;
            }

            conn.rollback();
            return false;

        } catch (SQLException e) {
            rollbackQuietly(conn);
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, ps, rs);
        }
    }

    // =========================================================
    // AUTO DUYET SAU 2 PHUT
    // Chi ap dung: hang_hong, hang_that_lac
    // het_nhu_cau phai admin duyet thu cong
    // =========================================================
    public int tuDongDuyetTraHangSau2Phut() {
        String sqlSelect = "SELECT ma_tra_hang FROM tra_hang_hoan_tien WHERE trang_thai = 'cho_xu_ly' AND ly_do IN ('hang_hong', 'hang_that_lac') AND ngay_yeu_cau <= DATE_SUB(NOW(), INTERVAL 2 MINUTE)";

        Connection conn = null;
        int count = 0;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            List<Integer> dsMaTraHang = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(sqlSelect); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dsMaTraHang.add(rs.getInt("ma_tra_hang"));
                }
            }

            for (int maTraHang : dsMaTraHang) {
                if (xuLyDuyetTraHang(conn, maTraHang)) {
                    count++;
                }
            }

            conn.commit();
            return count;

        } catch (SQLException e) {
            rollbackQuietly(conn);
            e.printStackTrace();
            return 0;
        } finally {
            closeResources(conn, null, null);
        }
    }

    private boolean xuLyDuyetTraHang(Connection conn, int maTraHang) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement("SELECT * FROM tra_hang_hoan_tien WHERE ma_tra_hang = ? AND trang_thai = 'cho_xu_ly'");
            ps.setInt(1, maTraHang);
            rs = ps.executeQuery();

            if (!rs.next()) {
                return false;
            }

            TraHang traHang = mapResultSet(rs);
            rs.close();
            ps.close();
            rs = null;
            ps = null;

            if (traHang.isHoanTonKho()) {
                hoanTonKho(conn, traHang.getMaDonHang());
            }

            capNhatTrangThaiTraHang(conn, maTraHang, "da_hoan");
            capNhatTrangThaiDonHang(conn, traHang.getMaDonHang(), "da_tra_hang");
            return true;

        } finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException ignored) {
            }
            if (ps != null) try {
                ps.close();
            } catch (SQLException ignored) {
            }
        }
    }

    // =========================================================
    // ADMIN - TRUY VAN
    // =========================================================
    public List<TraHang> getTatCaYeuCau() {
        String sql = "SELECT t.*, d.tong_tien_don_hang, nd.ho_ten AS ten_khach_hang FROM tra_hang_hoan_tien t JOIN don_hang d ON t.ma_don_hang = d.ma_don_hang JOIN nguoi_dung nd ON d.ma_nguoi_dung = nd.ma_nguoi_dung ORDER BY t.ngay_yeu_cau DESC";

        List<TraHang> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetFull(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<TraHang> getYeuCauTheoTrangThai(String trangThai) {
        String sql = "SELECT t.*, d.tong_tien_don_hang, nd.ho_ten AS ten_khach_hang FROM tra_hang_hoan_tien t JOIN don_hang d ON t.ma_don_hang = d.ma_don_hang JOIN nguoi_dung nd ON d.ma_nguoi_dung = nd.ma_nguoi_dung WHERE t.trang_thai = ? ORDER BY t.ngay_yeu_cau DESC";

        List<TraHang> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetFull(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public TraHang getByMaTraHang(int maTraHang) {
        String sql = "SELECT t.*, d.tong_tien_don_hang, nd.ho_ten AS ten_khach_hang FROM tra_hang_hoan_tien t JOIN don_hang d ON t.ma_don_hang = d.ma_don_hang JOIN nguoi_dung nd ON d.ma_nguoi_dung = nd.ma_nguoi_dung WHERE t.ma_tra_hang = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maTraHang);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetFull(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public TraHang getByMaDonHang(int maDonHang) {
        String sql = "SELECT * FROM tra_hang_hoan_tien WHERE ma_don_hang = ? ORDER BY ngay_yeu_cau DESC LIMIT 1";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDonHang);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int demYeuCauChoXuLy() {
        String sql = "SELECT COUNT(*) FROM tra_hang_hoan_tien WHERE trang_thai = 'cho_xu_ly'";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // =========================================================
    // PRIVATE HELPERS
    // =========================================================
    private void capNhatTrangThaiDonHang(Connection conn, int maDonHang, String trangThai) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE don_hang SET trang_thai_don_hang = ? WHERE ma_don_hang = ?")) {
            ps.setString(1, trangThai);
            ps.setInt(2, maDonHang);
            ps.executeUpdate();
        }
    }

    private void capNhatTrangThaiTraHang(Connection conn, int maTraHang, String trangThai) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE tra_hang_hoan_tien SET trang_thai = ?, ngay_hoan = NOW() WHERE ma_tra_hang = ?")) {
            ps.setString(1, trangThai);
            ps.setInt(2, maTraHang);
            ps.executeUpdate();
        }
    }

    private void hoanTonKho(Connection conn, int maDonHang) throws SQLException {
        String sql = "UPDATE bien_the bt JOIN chi_tiet_don_hang ct ON bt.ma_bien_the = ct.ma_bien_the SET bt.so_luong = bt.so_luong + ct.so_luong WHERE ct.ma_don_hang = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDonHang);
            ps.executeUpdate();
        }
    }

    private void rollbackQuietly(Connection conn) {
        if (conn != null) try {
            conn.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        if (rs != null)   try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (ps != null)   try {
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (conn != null) try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private TraHang mapResultSet(ResultSet rs) throws SQLException {
        TraHang th = new TraHang();
        th.setMaTraHang(rs.getInt("ma_tra_hang"));
        th.setMaDonHang(rs.getInt("ma_don_hang"));
        th.setLyDo(rs.getString("ly_do"));
        th.setSoTienHoan(rs.getDouble("so_tien_hoan"));
        th.setTrangThai(rs.getString("trang_thai"));
        th.setNgayYeuCau(rs.getTimestamp("ngay_yeu_cau"));
        th.setNgayHoan(rs.getTimestamp("ngay_hoan"));
        th.setGhiChu(rs.getString("ghi_chu"));
        return th;
    }

    private TraHang mapResultSetFull(ResultSet rs) throws SQLException {
        TraHang th = mapResultSet(rs);
        try {
            th.setTenKhachHang(rs.getString("ten_khach_hang"));
        } catch (Exception ignored) {
        }
        try {
            th.setTongTienDonHang(rs.getDouble("tong_tien_don_hang"));
        } catch (Exception ignored) {
        }
        return th;
    }
}

package dao;

import model.DatSan;
import model.DatSanChiTiet;
import model.LichSan;
import utils.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatSanDAO {

    /**
     * Create booking + chi tiet in a single transaction
     */
    public int createDatSan(DatSan datSan, List<LichSan> lichSanList) throws SQLException {
        Connection c = null;
        try {
            c = DBConnection.getConnection();
            c.setAutoCommit(false);

            // Insert dat_san
            String sql = "INSERT INTO dat_san (user_id, ten_khach, so_dien_thoai, email, sub_san_id, ngay_dat, "
                    + "tong_tien, trang_thai, phuong_thuc_thanh_toan, ghi_chu, ma_giao_dich) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            int datSanId;
            try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                if (datSan.getUserId() != null) {
                    ps.setInt(1, datSan.getUserId());
                } else {
                    ps.setNull(1, Types.INTEGER);
                }
                ps.setString(2, datSan.getTenKhach());
                ps.setString(3, datSan.getSoDienThoai());
                ps.setString(4, datSan.getEmail());
                ps.setInt(5, datSan.getSubSanId());
                ps.setDate(6, datSan.getNgayDat());
                ps.setBigDecimal(7, datSan.getTongTien());
                ps.setString(8, "cho_duyet");
                ps.setString(9, datSan.getPhuongThucThanhToan());
                ps.setString(10, datSan.getGhiChu());
                ps.setString(11, datSan.getMaGiaoDich());
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                keys.next();
                datSanId = keys.getInt(1);
            }

            // Insert chi tiet
            String ctSql = "INSERT INTO dat_san_chi_tiet (dat_san_id, lich_san_id, gio_bat_dau, gio_ket_thuc, gia) VALUES (?,?,?,?,?)";
            try (PreparedStatement ps = c.prepareStatement(ctSql)) {
                for (LichSan ls : lichSanList) {
                    ps.setInt(1, datSanId);
                    ps.setInt(2, ls.getId());
                    ps.setTime(3, ls.getGioBatDau());
                    ps.setTime(4, ls.getGioKetThuc());
                    // gia per slot (will be recalculated from sub_san)
                    ps.setBigDecimal(5, datSan.getTongTien().divide(BigDecimal.valueOf(lichSanList.size()), 2, java.math.RoundingMode.HALF_UP));
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            // Lock the lich_san slots
            new LichSanDAO().lockSlots(c, lichSanList.stream().map(LichSan::getId).collect(java.util.stream.Collectors.toList()), datSanId);

            c.commit();
            return datSanId;
        } catch (SQLException e) {
            if (c != null) {
                try {
                    c.rollback();
                } catch (SQLException ignored) {
                }
            }
            throw e;
        } finally {
            DBConnection.closeQuietly(c);
        }
    }

    /**
     * Admin: approve booking
     */
    public void duyetDatSan(int datSanId) throws SQLException {
        Connection c = null;
        try {
            c = DBConnection.getConnection();
            c.setAutoCommit(false);

            // Update dat_san status
            String sql = "UPDATE dat_san SET trang_thai = 'da_duyet', updated_at = NOW() WHERE id = ?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, datSanId);
                ps.executeUpdate();
            }

            // Confirm lich_san slots
            List<Integer> lichIds = getChiTietLichIds(c, datSanId);
            new LichSanDAO().confirmSlots(c, lichIds, datSanId);

            c.commit();
        } catch (SQLException e) {
            if (c != null) {
                try {
                    c.rollback();
                } catch (SQLException ignored) {
                }
            }
            throw e;
        } finally {
            DBConnection.closeQuietly(c);
        }
    }

    /**
     * Admin: reject booking
     */
    public void tuChoiDatSan(int datSanId) throws SQLException {
        Connection c = null;
        try {
            c = DBConnection.getConnection();
            c.setAutoCommit(false);

            // Update dat_san status
            String sql = "UPDATE dat_san SET trang_thai = 'tu_choi', updated_at = NOW() WHERE id = ?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, datSanId);
                ps.executeUpdate();
            }

            // Release lich_san slots
            List<Integer> lichIds = getChiTietLichIds(c, datSanId);
            new LichSanDAO().releaseSlots(c, lichIds);

            c.commit();
        } catch (SQLException e) {
            if (c != null) {
                try {
                    c.rollback();
                } catch (SQLException ignored) {
                }
            }
            throw e;
        } finally {
            DBConnection.closeQuietly(c);
        }
    }

    public DatSan getDatSanById(int id) throws SQLException {
        String sql = "SELECT ds.*, ss.ma_sub_san, s.ten_san "
                + "FROM dat_san ds "
                + "JOIN sub_san ss ON ds.sub_san_id = ss.id "
                + "JOIN san s ON ss.san_id = s.id "
                + "WHERE ds.id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DatSan ds = mapDatSan(rs);
                    ds.setChiTietList(getChiTiet(id));
                    return ds;
                }
            }
        }
        return null;
    }

    public List<DatSan> getDatSanByUserId(int userId) throws SQLException {
        return getDatSanList("WHERE ds.user_id = ? ORDER BY ds.created_at DESC", userId);
    }

    public List<DatSan> getAllDatSan() throws SQLException {
        return getDatSanList("ORDER BY ds.created_at DESC", null);
    }

    public List<DatSan> getDatSanByTrangThai(String trangThai) throws SQLException {
        return getDatSanList("WHERE ds.trang_thai = ? ORDER BY ds.created_at DESC", trangThai);
    }

    private List<DatSan> getDatSanList(String whereClause, Object param) throws SQLException {
        List<DatSan> list = new ArrayList<>();
        String sql = "SELECT ds.*, ss.ma_sub_san, s.ten_san FROM dat_san ds "
                + "JOIN sub_san ss ON ds.sub_san_id = ss.id "
                + "JOIN san s ON ss.san_id = s.id " + whereClause;
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (param != null) {
                if (param instanceof Integer) {
                    ps.setInt(1, (Integer) param);
                } else {
                    ps.setString(1, param.toString());
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapDatSan(rs));
                }
            }
        }
        return list;
    }

    public List<DatSanChiTiet> getChiTiet(int datSanId) throws SQLException {
        List<DatSanChiTiet> list = new ArrayList<>();
        String sql = "SELECT * FROM dat_san_chi_tiet WHERE dat_san_id = ? ORDER BY gio_bat_dau";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, datSanId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DatSanChiTiet ct = new DatSanChiTiet();
                    ct.setId(rs.getInt("id"));
                    ct.setDatSanId(rs.getInt("dat_san_id"));
                    ct.setLichSanId(rs.getInt("lich_san_id"));
                    ct.setGioBatDau(rs.getTime("gio_bat_dau"));
                    ct.setGioKetThuc(rs.getTime("gio_ket_thuc"));
                    ct.setGia(rs.getBigDecimal("gia"));
                    list.add(ct);
                }
            }
        }
        return list;
    }

    private List<Integer> getChiTietLichIds(Connection c, int datSanId) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT lich_san_id FROM dat_san_chi_tiet WHERE dat_san_id = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, datSanId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("lich_san_id"));
                }
            }
        }
        return ids;
    }

    private DatSan mapDatSan(ResultSet rs) throws SQLException {
        DatSan ds = new DatSan();
        ds.setId(rs.getInt("id"));
        int uid = rs.getInt("user_id");
        ds.setUserId(rs.wasNull() ? null : uid);
        ds.setTenKhach(rs.getString("ten_khach"));
        ds.setSoDienThoai(rs.getString("so_dien_thoai"));
        ds.setEmail(rs.getString("email"));
        ds.setSubSanId(rs.getInt("sub_san_id"));
        ds.setNgayDat(rs.getDate("ngay_dat"));
        ds.setTongTien(rs.getBigDecimal("tong_tien"));
        ds.setTrangThai(rs.getString("trang_thai"));
        ds.setPhuongThucThanhToan(rs.getString("phuong_thuc_thanh_toan"));
        ds.setGhiChu(rs.getString("ghi_chu"));
        ds.setMaGiaoDich(rs.getString("ma_giao_dich"));
        ds.setCreatedAt(rs.getTimestamp("created_at"));
        ds.setUpdatedAt(rs.getTimestamp("updated_at"));
        try {
            ds.setMaSubSan(rs.getString("ma_sub_san"));
        } catch (Exception ignored) {
        }
        try {
            ds.setTenSan(rs.getString("ten_san"));
        } catch (Exception ignored) {
        }
        return ds;
    }
}

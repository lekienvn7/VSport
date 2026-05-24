package dao;

import model.KhungGio;
import model.LichSan;
import utils.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class LichSanDAO {

    /** Get schedule for a sub-field for the next 7 days */
    public Map<String, List<LichSan>> getLichSanTheoTuan(int subSanId) throws SQLException {
        Map<String, List<LichSan>> result = new LinkedHashMap<>();
        String sql = "SELECT ls.*, kg.ten_khung, kg.gio_bat_dau, kg.gio_ket_thuc " +
                     "FROM lich_san ls " +
                     "JOIN khung_gio kg ON ls.khung_gio_id = kg.id " +
                     "WHERE ls.sub_san_id = ? AND ls.ngay BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 6 DAY) " +
                     "ORDER BY ls.ngay, kg.gio_bat_dau";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, subSanId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LichSan ls = mapLichSan(rs);
                    String ngayStr = ls.getNgay().toString();
                    result.computeIfAbsent(ngayStr, k -> new ArrayList<>()).add(ls);
                }
            }
        }

        // Fill missing days (might have no slots if generate hasn't run)
        ensureWeekGenerated(subSanId);
        return result;
    }

    /** Get available (trong) slots from a given lich_san_id onwards on the same day */
    public List<LichSan> getNextTrongSlots(int subSanId, int lichSanId) throws SQLException {
        List<LichSan> list = new ArrayList<>();
        String sql = "SELECT ls.*, kg.ten_khung, kg.gio_bat_dau, kg.gio_ket_thuc " +
                     "FROM lich_san ls " +
                     "JOIN khung_gio kg ON ls.khung_gio_id = kg.id " +
                     "WHERE ls.sub_san_id = ? AND ls.ngay = (SELECT ngay FROM lich_san WHERE id = ?) " +
                     "  AND ls.trang_thai = 'trong' " +
                     "  AND kg.gio_bat_dau > (SELECT kg2.gio_ket_thuc FROM lich_san ls2 JOIN khung_gio kg2 ON ls2.khung_gio_id = kg2.id WHERE ls2.id = ?) " +
                     "ORDER BY kg.gio_bat_dau LIMIT 5";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, subSanId);
            ps.setInt(2, lichSanId);
            ps.setInt(3, lichSanId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapLichSan(rs));
            }
        }
        return list;
    }

    public LichSan getLichSanById(int id) throws SQLException {
        String sql = "SELECT ls.*, kg.ten_khung, kg.gio_bat_dau, kg.gio_ket_thuc " +
                     "FROM lich_san ls JOIN khung_gio kg ON ls.khung_gio_id = kg.id WHERE ls.id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapLichSan(rs);
            }
        }
        return null;
    }

    public List<LichSan> getLichSanByIds(List<Integer> ids) throws SQLException {
        if (ids == null || ids.isEmpty()) return new ArrayList<>();
        StringBuilder sb = new StringBuilder("SELECT ls.*, kg.ten_khung, kg.gio_bat_dau, kg.gio_ket_thuc " +
                "FROM lich_san ls JOIN khung_gio kg ON ls.khung_gio_id = kg.id WHERE ls.id IN (");
        for (int i = 0; i < ids.size(); i++) {
            sb.append(i == 0 ? "?" : ",?");
        }
        sb.append(") ORDER BY kg.gio_bat_dau");
        List<LichSan> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sb.toString())) {
            for (int i = 0; i < ids.size(); i++) ps.setInt(i + 1, ids.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapLichSan(rs));
            }
        }
        return list;
    }

    /** Mark slots as 'dang_xu_ly' when user initiates booking */
    public void lockSlots(Connection c, List<Integer> lichSanIds, int datSanId) throws SQLException {
        String sql = "UPDATE lich_san SET trang_thai = 'dang_xu_ly', dat_san_id = ? WHERE id = ? AND trang_thai = 'trong'";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            for (int id : lichSanIds) {
                ps.setInt(1, datSanId);
                ps.setInt(2, id);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    /** Mark slots as 'da_dat' after booking confirmed */
    public void confirmSlots(Connection c, List<Integer> lichSanIds, int datSanId) throws SQLException {
        String sql = "UPDATE lich_san SET trang_thai = 'da_dat', dat_san_id = ? WHERE id = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            for (int id : lichSanIds) {
                ps.setInt(1, datSanId);
                ps.setInt(2, id);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    /** Release locked slots back to 'trong' */
    public void releaseSlots(Connection c, List<Integer> lichSanIds) throws SQLException {
        String sql = "UPDATE lich_san SET trang_thai = 'trong', dat_san_id = NULL WHERE id = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            for (int id : lichSanIds) {
                ps.setInt(1, id);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    /** Auto-reset: release expired locks, generate new schedule days */
    public void resetTrangThaiTuDong() throws SQLException {
        try (Connection c = DBConnection.getConnection()) {
            // Release slots locked > 30 mins ago with no confirmed booking
            String sql = "UPDATE lich_san ls " +
                         "SET ls.trang_thai = 'trong', ls.dat_san_id = NULL " +
                         "WHERE ls.trang_thai = 'dang_xu_ly' " +
                         "AND ls.dat_san_id IN (" +
                         "  SELECT id FROM dat_san WHERE trang_thai = 'cho_duyet' " +
                         "  AND created_at < DATE_SUB(NOW(), INTERVAL 30 MINUTE)" +
                         ")";
            c.prepareStatement(sql).executeUpdate();

            // Generate schedule for next 7 days if missing
            for (int i = 0; i < 7; i++) {
                String insertSql = "INSERT IGNORE INTO lich_san (sub_san_id, ngay, khung_gio_id, trang_thai) " +
                                   "SELECT ss.id, DATE_ADD(CURDATE(), INTERVAL ? DAY), kg.id, 'trong' " +
                                   "FROM sub_san ss CROSS JOIN khung_gio kg " +
                                   "WHERE ss.trang_thai = 'hoat_dong'";
                try (PreparedStatement ps = c.prepareStatement(insertSql)) {
                    ps.setInt(1, i);
                    ps.executeUpdate();
                }
            }
        }
    }

    private void ensureWeekGenerated(int subSanId) throws SQLException {
        try (Connection c = DBConnection.getConnection()) {
            for (int i = 0; i < 7; i++) {
                String sql = "INSERT IGNORE INTO lich_san (sub_san_id, ngay, khung_gio_id, trang_thai) " +
                             "SELECT ?, DATE_ADD(CURDATE(), INTERVAL ? DAY), kg.id, 'trong' FROM khung_gio kg";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, subSanId);
                    ps.setInt(2, i);
                    ps.executeUpdate();
                }
            }
        }
    }

    public List<KhungGio> getAllKhungGio() throws SQLException {
        List<KhungGio> list = new ArrayList<>();
        String sql = "SELECT * FROM khung_gio ORDER BY gio_bat_dau";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                KhungGio kg = new KhungGio();
                kg.setId(rs.getInt("id"));
                kg.setGioBatDau(rs.getTime("gio_bat_dau"));
                kg.setGioKetThuc(rs.getTime("gio_ket_thuc"));
                kg.setTenKhung(rs.getString("ten_khung"));
                list.add(kg);
            }
        }
        return list;
    }

    private LichSan mapLichSan(ResultSet rs) throws SQLException {
        LichSan ls = new LichSan();
        ls.setId(rs.getInt("id"));
        ls.setSubSanId(rs.getInt("sub_san_id"));
        ls.setNgay(rs.getDate("ngay"));
        ls.setKhungGioId(rs.getInt("khung_gio_id"));
        ls.setTrangThai(rs.getString("trang_thai"));
        int dsId = rs.getInt("dat_san_id");
        ls.setDatSanId(rs.wasNull() ? null : dsId);
        try {
            ls.setTenKhung(rs.getString("ten_khung"));
            ls.setGioBatDau(rs.getTime("gio_bat_dau"));
            ls.setGioKetThuc(rs.getTime("gio_ket_thuc"));
        } catch (Exception ignored) {}
        return ls;
    }
}
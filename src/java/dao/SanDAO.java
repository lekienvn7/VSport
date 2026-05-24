package dao;

import model.San;
import model.SubSan;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SanDAO {

    public List<San> getAllSan() throws SQLException {
        List<San> list = new ArrayList<>();
        String sql = "SELECT * FROM san ORDER BY id";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapSan(rs));
            }
        }
        return list;
    }

    public San getSanById(int id) throws SQLException {
        String sql = "SELECT * FROM san WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapSan(rs);
                }
            }
        }
        return null;
    }

    public List<SubSan> getSubSanBySanId(int sanId) throws SQLException {
        List<SubSan> list = new ArrayList<>();
        String sql = "SELECT ss.*, s.ten_san FROM sub_san ss "
                + "JOIN san s ON ss.san_id = s.id "
                + "WHERE ss.san_id = ? AND ss.trang_thai = 'hoat_dong' "
                + "ORDER BY ss.loai_san, ss.vi_tri";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, sanId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapSubSan(rs));
                }
            }
        }
        return list;
    }

    public SubSan getSubSanById(int id) throws SQLException {
        String sql = "SELECT ss.*, s.ten_san FROM sub_san ss "
                + "JOIN san s ON ss.san_id = s.id WHERE ss.id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapSubSan(rs);
                }
            }
        }
        return null;
    }

    private San mapSan(ResultSet rs) throws SQLException {
        San s = new San();
        s.setId(rs.getInt("id"));
        s.setTenSan(rs.getString("ten_san"));
        s.setMoTa(rs.getString("mo_ta"));
        s.setDiaChi(rs.getString("dia_chi"));
        s.setHinhAnh(rs.getString("hinh_anh"));
        return s;
    }

    private SubSan mapSubSan(ResultSet rs) throws SQLException {
        SubSan ss = new SubSan();
        ss.setId(rs.getInt("id"));
        ss.setSanId(rs.getInt("san_id"));
        ss.setMaSubSan(rs.getString("ma_sub_san"));
        ss.setLoaiSan(rs.getString("loai_san"));
        ss.setViTri(rs.getString("vi_tri"));
        ss.setGiaTheoGio(rs.getBigDecimal("gia_theo_gio"));
        ss.setMoTa(rs.getString("mo_ta"));
        ss.setTrangThai(rs.getString("trang_thai"));
        try {
            ss.setTenSan(rs.getString("ten_san"));
        } catch (Exception ignored) {
        }
        return ss;
    }
}

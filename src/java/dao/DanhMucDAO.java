/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import model.DanhMuc;
import utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ltrgk
 */
public class DanhMucDAO {

    public List<DanhMuc> getAllDanhMuc() {
        List<DanhMuc> list = new ArrayList<>();

        String sql = "SELECT * FROM danh_muc ORDER BY ma_danh_muc ASC";

        try (
            Connection conn = (Connection) DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                DanhMuc dm = new DanhMuc();
                dm.setMaDanhMuc(rs.getInt("ma_danh_muc"));
                dm.setTenDanhMuc(rs.getString("ten_danh_muc"));
                dm.setSlug(rs.getString("slug"));
                list.add(dm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
    public DanhMuc getDanhMucById(int maDanhMuc) {
        String sql = """
            SELECT ma_danh_muc, ten_danh_muc, slug
            FROM danh_muc
            WHERE ma_danh_muc = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maDanhMuc);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DanhMuc dm = new DanhMuc();
                    dm.setMaDanhMuc(rs.getInt("ma_danh_muc"));
                    dm.setTenDanhMuc(rs.getString("ten_danh_muc"));
                    dm.setSlug(rs.getString("slug"));
                    return dm;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean themDanhMuc(DanhMuc danhMuc) {
        String sql = """
            INSERT INTO danh_muc (ten_danh_muc, slug)
            VALUES (?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, danhMuc.getTenDanhMuc());
            ps.setString(2, danhMuc.getSlug());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean suaDanhMuc(DanhMuc danhMuc) {
        String sql = """
            UPDATE danh_muc
            SET ten_danh_muc = ?, slug = ?
            WHERE ma_danh_muc = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, danhMuc.getTenDanhMuc());
            ps.setString(2, danhMuc.getSlug());
            ps.setInt(3, danhMuc.getMaDanhMuc());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean xoaDanhMuc(int maDanhMuc) {
        String sql = """
            DELETE FROM danh_muc
            WHERE ma_danh_muc = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maDanhMuc);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isDanhMucDangCoSanPham(int maDanhMuc) {
        String sql = """
            SELECT COUNT(*) 
            FROM san_pham 
            WHERE ma_danh_muc = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maDanhMuc);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private static class SQLIntegrityConstraintViolationException {

        public SQLIntegrityConstraintViolationException() {
        }
    }
}

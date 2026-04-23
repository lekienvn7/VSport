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
}

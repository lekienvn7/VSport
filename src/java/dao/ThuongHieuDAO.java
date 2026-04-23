/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.ThuongHieu;
import utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ThuongHieuDAO {

    public List<ThuongHieu> getAllThuongHieu() {
        List<ThuongHieu> list = new ArrayList<>();

        String sql = "SELECT * FROM thuong_hieu ORDER BY ma_thuong_hieu ASC";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                ThuongHieu th = new ThuongHieu();
                th.setMaThuongHieu(rs.getInt("ma_thuong_hieu"));
                th.setTenThuongHieu(rs.getString("ten_thuong_hieu"));
                list.add(th);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}

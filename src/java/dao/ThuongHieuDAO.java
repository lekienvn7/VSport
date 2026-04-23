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
    
    public List<ThuongHieu> getAllThuongHieuDangDungChoDanhMuc(int maDanhMuc) {
        List<ThuongHieu> list = new ArrayList<>();

        String sql = """
            SELECT DISTINCT
                th.ma_thuong_hieu,
                th.ten_thuong_hieu,
                th.slug
            FROM thuong_hieu th
            INNER JOIN san_pham sp ON sp.ma_thuong_hieu = th.ma_thuong_hieu
            WHERE sp.ma_danh_muc = ?
              AND sp.trang_thai = 'dang_ban'
            ORDER BY th.ten_thuong_hieu ASC
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, maDanhMuc);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ThuongHieu th = new ThuongHieu();
                th.setMaThuongHieu(rs.getInt("ma_thuong_hieu"));
                th.setTenThuongHieu(rs.getString("ten_thuong_hieu"));
                th.setSlug(rs.getString("slug"));
                list.add(th);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}

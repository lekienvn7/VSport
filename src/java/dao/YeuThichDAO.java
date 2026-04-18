/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import utils.DBConnection;

import model.YeuThich;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class YeuThichDAO {
    
    public boolean daYeuThich(int maNguoiDung, int maSanPham) {
        String sql = "SELECT 1 FROM yeu_thich WHERE ma_nguoi_dung = ? AND ma_san_pham = ?";
        
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, maNguoiDung);
            ps.setInt(2, maSanPham);
            
            ResultSet rs = ps.executeQuery();
            return rs.next();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean themYeuThich(int maNguoiDung, int maSanPham) {
        String sql = "INSERT INTO yeu_thich(ma_nguoi_dung, ma_san_pham) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maNguoiDung);
            ps.setInt(2, maSanPham);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean xoaYeuThich(int maNguoiDung, int maSanPham) {
        String sql = "DELETE FROM yeu_thich WHERE ma_nguoi_dung = ? AND ma_san_pham = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maNguoiDung);
            ps.setInt(2, maSanPham);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<YeuThich> getDanhSachYeuThich(int maNguoiDung) {
    List<YeuThich> list = new ArrayList<>();

    String sql = """
        SELECT 
            yt.ma_yeu_thich,
            yt.ma_nguoi_dung,
            yt.ma_san_pham,
            yt.ngay_them,
            sp.ten_san_pham,
            sp.slug,
            sp.anh_chinh,
            sp.gia_san_pham,
            sp.gia_niem_yet,
            sp.gia_khuyen_mai,
            sp.mo_ta_ngan
        FROM yeu_thich yt
        JOIN san_pham sp ON yt.ma_san_pham = sp.ma_san_pham
        WHERE yt.ma_nguoi_dung = ?
        ORDER BY yt.ngay_them DESC
    """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, maNguoiDung);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            YeuThich item = new YeuThich();

            item.setMaYeuThich(rs.getInt("ma_yeu_thich"));
            item.setMaNguoiDung(rs.getInt("ma_nguoi_dung"));
            item.setMaSanPham(rs.getInt("ma_san_pham"));
            item.setNgayThem(rs.getTimestamp("ngay_them"));
            item.setTenSanPham(rs.getString("ten_san_pham"));
            item.setGiaNiemYet(rs.getDouble("gia_niem_yet"));
            item.setGiaKhuyenMai(rs.getDouble("gia_khuyen_mai"));
            item.setSlug(rs.getString("slug"));
            item.setAnhChinh(rs.getString("anh_chinh"));
            item.setGiaSanPham(rs.getDouble("gia_san_pham"));
            item.setMoTaNgan(rs.getString("mo_ta_ngan"));

            list.add(item);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
}

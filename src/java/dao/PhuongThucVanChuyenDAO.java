package dao;

import model.PhuongThucVanChuyen;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PhuongThucVanChuyenDAO {

    public List<PhuongThucVanChuyen> getDanhSachDangHoatDong() {
        List<PhuongThucVanChuyen> list = new ArrayList<>();

        String sql = """
            SELECT ma_ptvc, ten_phuong_thuc, phi_van_chuyen, thoi_gian_du_kien, trang_thai
            FROM phuong_thuc_van_chuyen
            WHERE trang_thai = 1
            ORDER BY ma_ptvc ASC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PhuongThucVanChuyen item = new PhuongThucVanChuyen();
                item.setMaPtvc(rs.getInt("ma_ptvc"));
                item.setTenPhuongThuc(rs.getString("ten_phuong_thuc"));
                item.setPhiVanChuyen(rs.getDouble("phi_van_chuyen"));
                item.setThoiGianDuKien(rs.getString("thoi_gian_du_kien"));
                item.setTrangThai(rs.getInt("trang_thai"));
                list.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public PhuongThucVanChuyen getById(int maPtvc) {
        String sql = """
            SELECT ma_ptvc, ten_phuong_thuc, phi_van_chuyen, thoi_gian_du_kien, trang_thai
            FROM phuong_thuc_van_chuyen
            WHERE ma_ptvc = ?
            LIMIT 1
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maPtvc);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PhuongThucVanChuyen item = new PhuongThucVanChuyen();
                    item.setMaPtvc(rs.getInt("ma_ptvc"));
                    item.setTenPhuongThuc(rs.getString("ten_phuong_thuc"));
                    item.setPhiVanChuyen(rs.getDouble("phi_van_chuyen"));
                    item.setThoiGianDuKien(rs.getString("thoi_gian_du_kien"));
                    item.setTrangThai(rs.getInt("trang_thai"));
                    return item;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
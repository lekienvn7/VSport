package dao;

import model.BienTheSanPham;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BienTheSanPhamDAO {

    public List<BienTheSanPham> getByProductId(int maSanPham) {
        List<BienTheSanPham> list = new ArrayList<>();

        String sql = """
            SELECT bt.ma_bien_the,
                   bt.ma_san_pham,
                   bt.ma_size,
                   bt.so_luong_ton,
                   bt.gia_rieng,
                   s.ten_size,
                   COALESCE(NULLIF(bt.gia_rieng, 0), sp.gia_san_pham) AS gia_hien_thi
            FROM bien_the_san_pham bt
            LEFT JOIN san_pham sp ON bt.ma_san_pham = sp.ma_san_pham
            LEFT JOIN size_san_pham s ON bt.ma_size = s.ma_size
            WHERE bt.ma_san_pham = ?
            ORDER BY 
                CASE 
                    WHEN s.ten_size = 'XS' THEN 1
                    WHEN s.ten_size = 'S' THEN 2
                    WHEN s.ten_size = 'M' THEN 3
                    WHEN s.ten_size = 'L' THEN 4
                    WHEN s.ten_size = 'XL' THEN 5
                    WHEN s.ten_size = '2XL' THEN 6
                    ELSE 99
                END,
                bt.ma_bien_the ASC
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, maSanPham);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public BienTheSanPham getById(int maBienThe) {
        String sql = """
            SELECT bt.ma_bien_the,
                   bt.ma_san_pham,
                   bt.ma_size,
                   bt.so_luong_ton,
                   bt.gia_rieng,
                   s.ten_size,
                   COALESCE(NULLIF(bt.gia_rieng, 0), sp.gia_san_pham) AS gia_hien_thi
            FROM bien_the_san_pham bt
            JOIN san_pham sp ON bt.ma_san_pham = sp.ma_san_pham
            LEFT JOIN size_san_pham s ON bt.ma_size = s.ma_size
            WHERE bt.ma_bien_the = ?
            LIMIT 1
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, maBienThe);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public BienTheSanPham getByProductAndSize(int maSanPham, int maSize) {
        String sql = """
            SELECT bt.ma_bien_the,
                   bt.ma_san_pham,
                   bt.ma_size,
                   bt.so_luong_ton,
                   bt.gia_rieng,
                   s.ten_size,
                   COALESCE(NULLIF(bt.gia_rieng, 0), sp.gia_san_pham) AS gia_hien_thi
            FROM bien_the_san_pham bt
            JOIN san_pham sp ON bt.ma_san_pham = sp.ma_san_pham
            LEFT JOIN size_san_pham s ON bt.ma_size = s.ma_size
            WHERE bt.ma_san_pham = ? AND bt.ma_size = ?
            LIMIT 1
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, maSanPham);
            ps.setInt(2, maSize);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public BienTheSanPham getDefaultByProduct(int maSanPham) {
        String sql = """
            SELECT bt.ma_bien_the,
                   bt.ma_san_pham,
                   bt.ma_size,
                   bt.so_luong_ton,
                   bt.gia_rieng,
                   NULL AS ten_size,
                   COALESCE(NULLIF(bt.gia_rieng, 0), sp.gia_san_pham) AS gia_hien_thi
            FROM bien_the_san_pham bt
            JOIN san_pham sp ON bt.ma_san_pham = sp.ma_san_pham
            WHERE bt.ma_san_pham = ? AND bt.ma_size IS NULL
            ORDER BY bt.ma_bien_the ASC
            LIMIT 1
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, maSanPham);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private BienTheSanPham mapResultSet(ResultSet rs) throws Exception {
    BienTheSanPham bt = new BienTheSanPham();

    bt.setMaBienThe(rs.getInt("ma_bien_the"));
    bt.setMaSanPham(rs.getInt("ma_san_pham"));
    bt.setMaSize((Integer) rs.getObject("ma_size"));
    bt.setSoLuongTon(rs.getInt("so_luong_ton"));

    Object giaRiengObj = rs.getObject("gia_rieng");
    if (giaRiengObj != null) {
        bt.setGiaRieng(((Number) giaRiengObj).doubleValue());
    } else {
        bt.setGiaRieng(null);
    }

    String tenSize = rs.getString("ten_size");
    bt.setTenSize(tenSize != null ? tenSize : "Mặc định");

    Object giaHienThiObj = rs.getObject("gia_hien_thi");
    if (giaHienThiObj != null) {
        bt.setGiaHienThi(((Number) giaHienThiObj).doubleValue());
    } else {
        bt.setGiaHienThi(0);
    }

    return bt;
}
}
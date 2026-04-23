package dao;

import model.SizeSanPham;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SizeSanPhamDAO {

    public List<SizeSanPham> getAll() throws Exception {
        List<SizeSanPham> list = new ArrayList<>();

        String sql = """
            SELECT ma_size, ten_size, loai_size
            FROM size_san_pham
            ORDER BY
                CASE
                    WHEN loai_size = 'ao' THEN 1
                    WHEN loai_size = 'gang' THEN 2
                    WHEN loai_size = 'giay' THEN 3
                    WHEN loai_size = 'bong' THEN 4
                    ELSE 99
                END,
                CASE
                    WHEN ten_size = 'XS' THEN 1
                    WHEN ten_size = 'S' THEN 2
                    WHEN ten_size = 'M' THEN 3
                    WHEN ten_size = 'L' THEN 4
                    WHEN ten_size = 'XL' THEN 5
                    WHEN ten_size = '2XL' THEN 6
                    WHEN ten_size LIKE 'Size %'
                        THEN CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(ten_size, ' (', 1), ' ', -1) AS UNSIGNED)
                    WHEN ten_size REGEXP '^[0-9]+'
                        THEN CAST(SUBSTRING_INDEX(ten_size, ' ', 1) AS UNSIGNED)
                    ELSE 999
                END,
                ten_size ASC
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                SizeSanPham size = new SizeSanPham();
                size.setMaSize(rs.getInt("ma_size"));
                size.setTenSize(rs.getString("ten_size"));
                size.setLoaiSize(rs.getString("loai_size"));
                list.add(size);
            }
        }

        return list;
    }

    public List<SizeSanPham> getByLoaiSize(String loaiSize) throws Exception {
        List<SizeSanPham> list = new ArrayList<>();

        String sql = """
            SELECT ma_size, ten_size, loai_size
            FROM size_san_pham
            WHERE loai_size = ?
            ORDER BY
                CASE
                    WHEN ten_size = 'XS' THEN 1
                    WHEN ten_size = 'S' THEN 2
                    WHEN ten_size = 'M' THEN 3
                    WHEN ten_size = 'L' THEN 4
                    WHEN ten_size = 'XL' THEN 5
                    WHEN ten_size = '2XL' THEN 6
                    WHEN ten_size LIKE 'Size %'
                        THEN CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(ten_size, ' (', 1), ' ', -1) AS UNSIGNED)
                    WHEN ten_size REGEXP '^[0-9]+'
                        THEN CAST(SUBSTRING_INDEX(ten_size, ' ', 1) AS UNSIGNED)
                    ELSE 999
                END,
                ten_size ASC
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, loaiSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SizeSanPham size = new SizeSanPham();
                    size.setMaSize(rs.getInt("ma_size"));
                    size.setTenSize(rs.getString("ten_size"));
                    size.setLoaiSize(rs.getString("loai_size"));
                    list.add(size);
                }
            }
        }

        return list;
    }

    public SizeSanPham getById(int maSize) throws Exception {
        String sql = """
            SELECT ma_size, ten_size, loai_size
            FROM size_san_pham
            WHERE ma_size = ?
            LIMIT 1
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, maSize);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SizeSanPham size = new SizeSanPham();
                    size.setMaSize(rs.getInt("ma_size"));
                    size.setTenSize(rs.getString("ten_size"));
                    size.setLoaiSize(rs.getString("loai_size"));
                    return size;
                }
            }
        }

        return null;
    }
}
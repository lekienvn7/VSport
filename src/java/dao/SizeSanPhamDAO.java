package dao;

import model.SizeSanPham;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SizeSanPhamDAO {

    public List<SizeSanPham> getAll() throws Exception { /// cần phải try-catch khi gọi
        List<SizeSanPham> list = new ArrayList<>();
        String sql = "SELECT ma_size, ten_size FROM size_san_pham ORDER BY ma_size ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SizeSanPham size = new SizeSanPham();
                size.setMaSize(rs.getInt("ma_size"));
                size.setTenSize(rs.getString("ten_size"));
                list.add(size);
            }
        }

        return list;
    }
}
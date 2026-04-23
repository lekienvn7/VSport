package dao;

import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AdminSanPhamEditDAO {

    public String getSanPhamEditJson(int maSanPham) throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            String sqlSanPham = """
                SELECT ma_san_pham, ten_san_pham, ma_danh_muc, ma_thuong_hieu, ma_doi_bong,
                       mo_ta_ngan, mo_ta_chi_tiet, gia_niem_yet, gia_khuyen_mai, trang_thai,
                       nhom_san_pham, ma_bo_suu_tap
                FROM san_pham
                WHERE ma_san_pham = ?
            """;

            StringBuilder json = new StringBuilder();

            try (PreparedStatement ps = conn.prepareStatement(sqlSanPham)) {
                ps.setInt(1, maSanPham);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return "{\"success\":false,\"message\":\"Không tìm thấy sản phẩm.\"}";
                    }

                    json.append("{");
                    json.append("\"success\":true,");
                    json.append("\"sanPham\":{");
                    json.append("\"maSanPham\":").append(rs.getInt("ma_san_pham")).append(",");
                    json.append("\"tenSanPham\":\"").append(escapeJson(rs.getString("ten_san_pham"))).append("\",");
                    json.append("\"maDanhMuc\":").append(rs.getInt("ma_danh_muc")).append(",");
                    json.append("\"maThuongHieu\":").append(rs.getObject("ma_thuong_hieu") == null ? "null" : rs.getInt("ma_thuong_hieu")).append(",");
                    json.append("\"maDoiBong\":").append(rs.getObject("ma_doi_bong") == null ? "null" : rs.getInt("ma_doi_bong")).append(",");
                    json.append("\"moTaNgan\":\"").append(escapeJson(rs.getString("mo_ta_ngan"))).append("\",");
                    json.append("\"moTaChiTiet\":\"").append(escapeJson(rs.getString("mo_ta_chi_tiet"))).append("\",");
                    json.append("\"giaKhuyenMai\":").append(rs.getObject("gia_khuyen_mai") == null ? "null" : rs.getBigDecimal("gia_khuyen_mai")).append(",");
                    json.append("\"giaNiemYet\":")
                    .append(rs.getObject("gia_niem_yet") == null ? "null" : rs.getBigDecimal("gia_niem_yet"))
                    .append(",");
                    json.append("\"trangThai\":\"").append(escapeJson(rs.getString("trang_thai"))).append("\",");
                    json.append("\"nhomSanPham\":").append(rs.getObject("nhom_san_pham") == null ? "null" : rs.getInt("nhom_san_pham")).append(",");
                    json.append("\"maBoSuuTap\":").append(rs.getObject("ma_bo_suu_tap") == null ? "null" : rs.getInt("ma_bo_suu_tap"));
                    json.append("},");
                }
            }

            json.append("\"bienThe\":[");
            List<String> bienTheJson = new ArrayList<>();
            String sqlBienThe = """
                SELECT ma_bien_the, ma_size, so_luong_ton, gia_rieng
                FROM bien_the_san_pham
                WHERE ma_san_pham = ?
                ORDER BY ma_bien_the ASC
            """;

            try (PreparedStatement ps = conn.prepareStatement(sqlBienThe)) {
                ps.setInt(1, maSanPham);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String row = """
                            {"maBienThe":%d,"maSize":%d,"soLuongTon":%d,"giaRieng":%s}
                        """.formatted(
                                rs.getInt("ma_bien_the"),
                                rs.getInt("ma_size"),
                                rs.getInt("so_luong_ton"),
                                rs.getObject("gia_rieng") == null ? "null" : rs.getBigDecimal("gia_rieng").toPlainString()
                        );
                        bienTheJson.add(row);
                    }
                }
            }
            json.append(String.join(",", bienTheJson));
            json.append("],");

            json.append("\"anhPhu\":[");
            List<String> anhJson = new ArrayList<>();
            String sqlAnh = """
                SELECT ma_anh, duong_dan_anh
                FROM anh_san_pham
                WHERE ma_san_pham = ? AND (la_anh_chinh = 0 OR la_anh_chinh IS NULL)
                ORDER BY ma_anh ASC
            """;

            try (PreparedStatement ps = conn.prepareStatement(sqlAnh)) {
                ps.setInt(1, maSanPham);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String row = """
                            {"maAnh":%d,"duongDanAnh":"%s"}
                        """.formatted(
                                rs.getInt("ma_anh"),
                                escapeJson(rs.getString("duong_dan_anh"))
                        );
                        anhJson.add(row);
                    }
                }
            }
            json.append(String.join(",", anhJson));
            json.append("]}");

            return json.toString();
        }
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
    }
}
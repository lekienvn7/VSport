package controller.admin;

import dao.AdminCapNhatSanPhamDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

@WebServlet("/admin/san-pham/cap-nhat")
public class AdminCapNhatSanPhamServlet extends HttpServlet {

    private final AdminCapNhatSanPhamDAO dao = new AdminCapNhatSanPhamDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        try {
            System.out.println("===== UPDATE PRODUCT PARAMS =====");
            request.getParameterMap().forEach((k, v) -> {
                System.out.println(k + " = " + Arrays.toString(v));
            });
            System.out.println("=================================");

            int maSanPham = parseIntRequired(request.getParameter("maSanPham"), "maSanPham");
            String tenSanPham = parseStringRequired(request.getParameter("tenSanPham"), "tenSanPham");
            Integer maDanhMuc = parseIntRequired(request.getParameter("maDanhMuc"), "maDanhMuc");

            Integer maThuongHieu = parseInteger(request.getParameter("maThuongHieu"));
            Integer maDoiBong = parseInteger(request.getParameter("maDoiBong"));
            String moTaNgan = trimToNull(request.getParameter("moTaNgan"));
            String moTaChiTiet = trimToNull(request.getParameter("moTaChiTiet"));
            BigDecimal giaKhuyenMai = parseBigDecimal(request.getParameter("giaKhuyenMai"));
            String trangThai = parseStringRequired(request.getParameter("trangThai"), "trangThai");
            Integer nhomSanPham = parseInteger(request.getParameter("nhomSanPham"));
            Integer maBoSuuTap = parseInteger(request.getParameter("maBoSuuTap"));

            String[] maBienTheArr = request.getParameterValues("maBienThe");
            String[] maSizeArr = request.getParameterValues("maSize");
            String[] soLuongTonArr = request.getParameterValues("soLuongTon");
            String[] giaRiengArr = request.getParameterValues("giaRieng");

            String[] maAnhArr = request.getParameterValues("maAnh");
            String[] anhPhuArr = request.getParameterValues("anhPhu");

            dao.capNhatSanPham(
                    maSanPham,
                    tenSanPham,
                    maDanhMuc,
                    maThuongHieu,
                    maDoiBong,
                    moTaNgan,
                    moTaChiTiet,
                    giaKhuyenMai,
                    trangThai,
                    nhomSanPham,
                    maBoSuuTap,
                    maBienTheArr,
                    maSizeArr,
                    soLuongTonArr,
                    giaRiengArr,
                    maAnhArr,
                    anhPhuArr
            );

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String json = """
            {
                "success": true,
                "message": "Cập nhật sản phẩm thành công."
            }
            """;

            response.getWriter().write(json);

        } catch (Exception e) {
            e.printStackTrace();

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("""
                {"success":false,"message":"%s"}
            """.formatted(escapeJson(e.getMessage())));
        }
    }

    
    private String trimToNull(String s) {
        if (s == null) return null;
        String value = s.trim();
        return value.isEmpty() ? null : value;
    }

    private Integer parseInteger(String s) {
        String value = trimToNull(s);
        return value == null ? null : Integer.parseInt(value);
    }

    private Integer parseIntRequired(String s, String fieldName) {
        String value = trimToNull(s);
        if (value == null) {
            throw new IllegalArgumentException("Thiếu field: " + fieldName);
        }
        return Integer.parseInt(value);
    }

    private String parseStringRequired(String s, String fieldName) {
        String value = trimToNull(s);
        if (value == null) {
            throw new IllegalArgumentException("Thiếu field: " + fieldName);
        }
        return value;
    }

    private BigDecimal parseBigDecimal(String s) {
        String value = trimToNull(s);
        return value == null ? null : new BigDecimal(value);
    }

    private String escapeJson(String s) {
        if (s == null) return "Có lỗi xảy ra.";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
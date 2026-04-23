package controller.admin;

import dao.AdminXoaSanPhamDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/san-pham/xoa")
public class AdminXoaSanPhamServlet extends HttpServlet {

    private final AdminXoaSanPhamDAO dao = new AdminXoaSanPhamDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        try {
            String ma = request.getParameter("maSanPham");

            if (ma == null || ma.isEmpty()) {
                throw new RuntimeException("Thiếu mã sản phẩm");
            }

            int maSanPham = Integer.parseInt(ma);

            dao.xoaSanPham(maSanPham);

            response.getWriter().write("""
                {"success":true,"message":"Xóa thành công"}
            """);

        } catch (Exception e) {
            response.setStatus(400);
            response.getWriter().write("""
                {"success":false,"message":"%s"}
            """.formatted(e.getMessage()));
        }
    }
}
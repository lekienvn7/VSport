package controller.admin;

import dao.AdminSanPhamEditDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/san-pham/chi-tiet-json")
public class ChiTietSanPhamAdminServlet extends HttpServlet {

    private final AdminSanPhamEditDAO dao = new AdminSanPhamEditDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        
        

        try {
            int maSanPham = Integer.parseInt(request.getParameter("maSanPham"));
            String json = dao.getSanPhamEditJson(maSanPham);
            response.getWriter().write(json);
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("""
                {"success":false,"message":"Không lấy được dữ liệu sản phẩm."}
            """);
        }
    }
}
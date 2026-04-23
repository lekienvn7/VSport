package controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.NguoiDung;

import java.io.IOException;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/trang_chu");
            return;
        }

        Object userObj = session.getAttribute("nguoiDung");
        if (userObj == null) {
            response.sendRedirect(request.getContextPath() + "/trang_chu");
            return;
        }

        NguoiDung nguoiDung = (NguoiDung) userObj;
        String vaiTro = nguoiDung.getVaiTro() != null ? nguoiDung.getVaiTro().trim() : "";

        if (!"quan_tri".equalsIgnoreCase(vaiTro)) {
            response.sendRedirect(request.getContextPath() + "/trang_chu");
            return;
        }

        request.setAttribute("activePage", "dashboard");

        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
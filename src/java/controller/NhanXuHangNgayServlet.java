package controller;

import dao.XuDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.NguoiDung;

import java.io.IOException;

@WebServlet(name = "NhanXuHangNgayServlet", urlPatterns = {"/kho_xu/nhan_xu"})
public class NhanXuHangNgayServlet extends HttpServlet {

    private final XuDAO xuDAO = new XuDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        NguoiDung nguoiDung = null;

        if (session != null && session.getAttribute("nguoiDung") != null) {
            nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        }

        if (nguoiDung == null) {
            response.sendRedirect(request.getContextPath() + "/kho_xu?error=login");
            return;
        }

        boolean success = xuDAO.nhanXuDangNhapHangNgay(nguoiDung.getMaNguoiDung());

        if (success) {
            response.sendRedirect(request.getContextPath() + "/kho_xu?success=checkin");
        } else {
            response.sendRedirect(request.getContextPath() + "/kho_xu?error=already");
        }
    }
}
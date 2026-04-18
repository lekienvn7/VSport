package controller.auth;

import dao.NguoiDungDAO;
import model.NguoiDung;
import utils.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "DangNhapServlet", urlPatterns = {"/auth_login"})
public class DangNhapServlet extends HttpServlet {

    private final NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("nguoiDung") != null) {
            response.sendRedirect(request.getContextPath() + "/trang_chu");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/trang_chu");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String dangNhap = request.getParameter("dangNhap");
        String matKhau = request.getParameter("matKhau");
        String redirect = request.getParameter("redirect");

        dangNhap = dangNhap != null ? dangNhap.trim() : "";
        matKhau = matKhau != null ? matKhau.trim() : "";
        redirect = redirect != null ? redirect.trim() : "";

        HttpSession session = request.getSession();

        if (dangNhap.isEmpty() || matKhau.isEmpty()) {
            session.setAttribute("loginError", "Vui lòng nhập đầy đủ thông tin.");
            session.setAttribute("openLoginPopup", true);
            response.sendRedirect(request.getContextPath() + "/trang_chu");
            return;
        }

        NguoiDung nguoiDung = nguoiDungDAO.login(dangNhap);

        if (nguoiDung == null) {
            session.setAttribute("loginError", "Đăng nhập thất bại.");
            session.setAttribute("openLoginPopup", true);
            response.sendRedirect(request.getContextPath() + "/trang_chu");
            return;
        }

        boolean dungMatKhau = PasswordUtil.checkPassword(matKhau, nguoiDung.getMatKhau());

        if (!dungMatKhau) {
            session.setAttribute("loginError", "Đăng nhập thất bại.");
            session.setAttribute("openLoginPopup", true);
            response.sendRedirect(request.getContextPath() + "/trang_chu");
            return;
        }

        session.removeAttribute("loginError");
        session.removeAttribute("openLoginPopup");
        session.setAttribute("nguoiDung", nguoiDung);
        
        session.setMaxInactiveInterval(1800);
        

        if (!redirect.isEmpty()) {
            if (!redirect.startsWith("/")) {
                redirect = "/" + redirect;
            }

            if (redirect.startsWith(request.getContextPath())) {
                redirect = redirect.substring(request.getContextPath().length());
            }

            response.sendRedirect(request.getContextPath() + redirect);
        } else {
            response.sendRedirect(request.getContextPath() + "/trang_chu");
        }
    }
}
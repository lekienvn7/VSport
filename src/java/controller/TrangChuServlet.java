package controller;

import dao.YeuThichDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.NguoiDung;
import model.YeuThich;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/trang_chu")
public class TrangChuServlet extends HttpServlet {

    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    HttpSession session = request.getSession(false);

    List<YeuThich> dsYeuThich = new ArrayList<>();
    boolean loginSuccess = false;
    boolean logoutSuccess = false;

    if (session != null) {
        if (session.getAttribute("nguoiDung") != null) {
            NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
            YeuThichDAO yeuThichDAO = new YeuThichDAO();
            dsYeuThich = yeuThichDAO.getDanhSachYeuThich(nguoiDung.getMaNguoiDung());
        }

        Boolean loginSuccessSession = (Boolean) session.getAttribute("loginSuccess");
        Boolean logoutSuccessSession = (Boolean) session.getAttribute("logoutSuccess");

        if (loginSuccessSession != null) {
            loginSuccess = loginSuccessSession;
        }

        if (logoutSuccessSession != null) {
            logoutSuccess = logoutSuccessSession;
        }

        session.removeAttribute("loginSuccess");
        session.removeAttribute("logoutSuccess");
    }

    request.setAttribute("dsYeuThich", dsYeuThich);
    request.setAttribute("loginSuccess", loginSuccess);
    request.setAttribute("logoutSuccess", logoutSuccess);

    request.getRequestDispatcher("/WEB-INF/views/user/home.jsp").forward(request, response);
}
}
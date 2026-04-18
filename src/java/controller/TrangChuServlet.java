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

        if (session != null && session.getAttribute("nguoiDung") != null) {
            NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
            YeuThichDAO yeuThichDAO = new YeuThichDAO();
            dsYeuThich = yeuThichDAO.getDanhSachYeuThich(nguoiDung.getMaNguoiDung());
        }

        request.setAttribute("dsYeuThich", dsYeuThich);
        request.getRequestDispatcher("/WEB-INF/views/user/home.jsp").forward(request, response);
    }
}
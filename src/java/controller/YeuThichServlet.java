package controller;

import dao.YeuThichDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.NguoiDung;
import model.YeuThich;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "YeuThichServlet", urlPatterns = {"/yeu_thich"})
public class YeuThichServlet extends HttpServlet {

    private final YeuThichDAO yeuThichDAO = new YeuThichDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("nguoiDung") == null) {
            response.sendRedirect(request.getContextPath() + "/dang_nhap");
            return;
        }

        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        int maNguoiDung = nguoiDung.getMaNguoiDung();

        List<YeuThich> dsYeuThich = yeuThichDAO.getDanhSachYeuThich(maNguoiDung);

        request.setAttribute("dsYeuThich", dsYeuThich);
        request.getRequestDispatcher("/WEB-INF/views/pages/components/favorite-section.jsp").forward(request, response);
    }
}
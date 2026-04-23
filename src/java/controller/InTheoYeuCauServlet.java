package controller;

import dao.BoSuuTapDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/in_theo_yeu_cau")
public class InTheoYeuCauServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BoSuuTapDAO boSuuTapDAO = new BoSuuTapDAO();

        request.setAttribute("dsBoSuuTap", boSuuTapDAO.getAllBoSuuTapHienThi());
        request.setAttribute("activePage", "in_theo_yeu_cau");
        request.getRequestDispatcher("/WEB-INF/views/pages/in-theo-yeu-cau.jsp")
                .forward(request, response);
    }
}
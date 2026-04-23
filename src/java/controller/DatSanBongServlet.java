package controller;

import dao.BoSuuTapDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/dat_san_bong")
public class DatSanBongServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BoSuuTapDAO boSuuTapDAO = new BoSuuTapDAO();

        request.setAttribute("dsBoSuuTap", boSuuTapDAO.getAllBoSuuTapHienThi());
        request.setAttribute("activePage", "dat_san_bong");
        request.getRequestDispatcher("/WEB-INF/views/pages/dat-san-bong.jsp")
                .forward(request, response);
    }
}
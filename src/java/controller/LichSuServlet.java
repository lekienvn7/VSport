package controller;

import dao.DatSanDAO;
import model.NguoiDung;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class LichSuServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/auth?redirect=" + req.getContextPath() + "/lich-su");
            return;
        }
        try {
            int userId = (Integer) session.getAttribute("userId");
            DatSanDAO dao = new DatSanDAO();
            req.setAttribute("danhSachDatSan", dao.getDatSanByUserId(userId));
            req.getRequestDispatcher("/lich-su.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

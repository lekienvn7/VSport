package controller;

import dao.DatSanDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AdminDatSanServlet extends HttpServlet {

    private boolean checkAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            resp.sendRedirect(req.getContextPath() + "/auth");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!checkAdmin(req, resp)) {
            return;
        }

        String path = req.getPathInfo();
        try {
            DatSanDAO dao = new DatSanDAO();
            if ("/duyet".equals(path) || "/tu-choi".equals(path)) {
                // handled in POST
                resp.sendRedirect(req.getContextPath() + "/admin");
                return;
            }
            // Default: list all bookings
            req.setAttribute("danhSachDatSan", dao.getAllDatSan());
            req.setAttribute("choDuyet", dao.getDatSanByTrangThai("cho_duyet"));
            req.getRequestDispatcher("/admin.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!checkAdmin(req, resp)) {
            return;
        }
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        int datSanId = Integer.parseInt(req.getParameter("datSanId"));

        try {
            DatSanDAO dao = new DatSanDAO();
            if ("duyet".equals(action)) {
                dao.duyetDatSan(datSanId);
            } else if ("tu_choi".equals(action)) {
                dao.tuChoiDatSan(datSanId);
            }
            resp.sendRedirect(req.getContextPath() + "/admin");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

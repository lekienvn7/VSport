package controller;

import dao.LichSanDAO;
import dao.SanDAO;
import model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SanDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int sanId = Integer.parseInt(req.getParameter("id"));
            SanDAO sanDAO = new SanDAO();
            LichSanDAO lichSanDAO = new LichSanDAO();

            San san = sanDAO.getSanById(sanId);
            if (san == null) { resp.sendError(404); return; }

            List<SubSan> subSanList = sanDAO.getSubSanBySanId(sanId);

            // Auto reset
            lichSanDAO.resetTrangThaiTuDong();

            req.setAttribute("san", san);
            req.setAttribute("subSanList", subSanList);
            req.setAttribute("khungGioList", lichSanDAO.getAllKhungGio());
            req.getRequestDispatcher("/san-detail.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
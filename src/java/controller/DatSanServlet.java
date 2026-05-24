package controller;

import dao.DatSanDAO;
import dao.LichSanDAO;
import dao.SanDAO;
import model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatSanServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        try {
            if ("confirm".equals(action)) {
                handleConfirm(req, resp);
            } else if ("submit".equals(action)) {
                handleSubmit(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("xem".equals(action)) {
            try {
                handleXemPhieu(req, resp);
            } catch (Exception ex) {
                System.getLogger(DatSanServlet.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }

    /**
     * Step 1: Show booking confirmation modal with QR
     */
    private void handleConfirm(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String[] lichSanIds = req.getParameterValues("lichSanIds");
        int subSanId = Integer.parseInt(req.getParameter("subSanId"));

        if (lichSanIds == null || lichSanIds.length == 0) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        List<Integer> ids = new ArrayList<>();
        for (String id : lichSanIds) {
            ids.add(Integer.parseInt(id));
        }

        SanDAO sanDAO = new SanDAO();
        LichSanDAO lichSanDAO = new LichSanDAO();
        SubSan subSan = sanDAO.getSubSanById(subSanId);
        List<LichSan> selectedSlots = lichSanDAO.getLichSanByIds(ids);

        // Calculate total
        BigDecimal tongTien = subSan.getGiaTheoGio().multiply(BigDecimal.valueOf(selectedSlots.size()));

        req.setAttribute("subSan", subSan);
        req.setAttribute("selectedSlots", selectedSlots);
        req.setAttribute("tongTien", tongTien);
        req.setAttribute("lichSanIds", lichSanIds);
        req.getRequestDispatcher("/dat-san.jsp").forward(req, resp);
    }

    /**
     * Step 2: Save booking to DB
     */
    private void handleSubmit(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String[] lichSanIdStrs = req.getParameterValues("lichSanIds");
        int subSanId = Integer.parseInt(req.getParameter("subSanId"));

        List<Integer> ids = new ArrayList<>();
        for (String id : lichSanIdStrs) {
            ids.add(Integer.parseInt(id));
        }

        LichSanDAO lichSanDAO = new LichSanDAO();
        SanDAO sanDAO = new SanDAO();
        SubSan subSan = sanDAO.getSubSanById(subSanId);
        List<LichSan> selectedSlots = lichSanDAO.getLichSanByIds(ids);

        // Build booking
        DatSan datSan = new DatSan();
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            datSan.setUserId((Integer) session.getAttribute("userId"));
        }
        datSan.setTenKhach(req.getParameter("tenKhach"));
        datSan.setSoDienThoai(req.getParameter("soDienThoai"));
        datSan.setEmail(req.getParameter("email"));
        datSan.setSubSanId(subSanId);

        if (!selectedSlots.isEmpty()) {
            datSan.setNgayDat(selectedSlots.get(0).getNgay());
        } else {
            datSan.setNgayDat(new Date(System.currentTimeMillis()));
        }

        BigDecimal tongTien = subSan.getGiaTheoGio().multiply(BigDecimal.valueOf(selectedSlots.size()));
        datSan.setTongTien(tongTien);
        datSan.setPhuongThucThanhToan("chuyen_khoan");
        datSan.setGhiChu(req.getParameter("ghiChu"));
        // Generate unique transaction code
        datSan.setMaGiaoDich("DS" + System.currentTimeMillis());

        DatSanDAO datSanDAO = new DatSanDAO();
        int newId = datSanDAO.createDatSan(datSan, selectedSlots);

        resp.sendRedirect(req.getContextPath() + "/dat-san?action=xem&id=" + newId);
    }

    /**
     * View booking receipt
     */
    private void handleXemPhieu(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        int id = Integer.parseInt(req.getParameter("id"));
        DatSanDAO dao = new DatSanDAO();
        DatSan datSan = dao.getDatSanById(id);
        req.setAttribute("datSan", datSan);
        req.getRequestDispatcher("/phieu-dat.jsp").forward(req, resp);
    }
}

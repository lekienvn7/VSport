package controller;

import dao.TraHangDAO;
import model.TraHang;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/tra-hang")
public class TraHangServlet extends HttpServlet {

    private TraHangDAO traHangDAO;

    @Override
    public void init() throws ServletException {
        traHangDAO = new TraHangDAO();
    }

    // =========================================================
    // GET – Trang admin quản lý trả hàng
    // =========================================================

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String trangThai = request.getParameter("trangThai"); // cho_xu_ly / da_hoan / tu_choi

        List<TraHang> dsTraHang;
        if (trangThai != null && !trangThai.isBlank()) {
            dsTraHang = traHangDAO.getYeuCauTheoTrangThai(trangThai);
        } else {
            dsTraHang = traHangDAO.getTatCaYeuCau();
        }

        int soChoXuLy = traHangDAO.demYeuCauChoXuLy();

        request.setAttribute("dsTraHang", dsTraHang);
        request.setAttribute("soChoXuLy", soChoXuLy);
        request.setAttribute("trangThaiFilter", trangThai != null ? trangThai : "");
        request.setAttribute("activePage", "tra_hang");

        request.getRequestDispatcher("/WEB-INF/views/admin/tra-hang.jsp")
                .forward(request, response);
    }

    // =========================================================
    // POST – Các action
    // =========================================================

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if (action == null || action.isBlank()) {
            sendJson(response, false, "Thiếu action");
            return;
        }

        try {
            switch (action) {
                case "yeu_cau" -> xuLyYeuCau(request, response);
                case "duyet"   -> xuLyDuyet(request, response);
                case "tu_choi" -> xuLyTuChoi(request, response);
                default        -> sendJson(response, false, "Action không hợp lệ: " + action);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (isAjax(request)) {
                sendJson(response, false, "Lỗi hệ thống: " + e.getMessage());
            } else {
                response.sendRedirect(request.getContextPath() + "/don-hang?msg=loi_he_thong");
            }
        }
    }

    // =========================================================
    // HANDLERS
    // =========================================================

    /** Khách tạo yêu cầu trả hàng */
    private void xuLyYeuCau(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int maDonHang;
        double soTien;

        try {
            maDonHang = Integer.parseInt(request.getParameter("maDonHang"));
            soTien    = Double.parseDouble(request.getParameter("soTienHoan"));
        } catch (NumberFormatException e) {
            sendJson(response, false, "Dữ liệu số không hợp lệ");
            return;
        }

        String lyDo   = request.getParameter("lyDo");
        String ghiChu = request.getParameter("ghiChu");

        if (lyDo == null || lyDo.isBlank()) {
            sendJson(response, false, "Vui lòng chọn lý do trả hàng");
            return;
        }

        TraHang traHang = new TraHang();
        traHang.setMaDonHang(maDonHang);
        traHang.setLyDo(lyDo);
        traHang.setSoTienHoan(soTien);
        traHang.setGhiChu(ghiChu);

        boolean ok = traHangDAO.taoYeuCauTraHang(traHang);

        if (isAjax(request)) {
            sendJson(response, ok,
                ok ? "Yêu cầu trả hàng đã được gửi thành công"
                   : "Không thể tạo yêu cầu. Đơn hàng có thể đã có yêu cầu đang xử lý.");
        } else {
            response.sendRedirect(request.getContextPath() + "/don-hang?msg=" +
                (ok ? "yeu_cau_tra_thanh_cong" : "loi"));
        }
    }

    /** Admin duyệt trả hàng */
    private void xuLyDuyet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int maTraHang;
        try {
            maTraHang = Integer.parseInt(request.getParameter("maTraHang"));
        } catch (NumberFormatException e) {
            sendJson(response, false, "Mã trả hàng không hợp lệ");
            return;
        }

        boolean ok = traHangDAO.duyetTraHang(maTraHang);

        if (isAjax(request)) {
            sendJson(response, ok,
                ok ? "Duyệt trả hàng thành công" : "Duyệt thất bại");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/tra-hang?msg=" +
                (ok ? "duyet_thanh_cong" : "loi"));
        }
    }

    /** Admin từ chối trả hàng */
    private void xuLyTuChoi(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int maTraHang;
        try {
            maTraHang = Integer.parseInt(request.getParameter("maTraHang"));
        } catch (NumberFormatException e) {
            sendJson(response, false, "Mã trả hàng không hợp lệ");
            return;
        }

        boolean ok = traHangDAO.tuChoiTraHang(maTraHang);

        if (isAjax(request)) {
            sendJson(response, ok,
                ok ? "Từ chối thành công" : "Từ chối thất bại");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/tra-hang?msg=" +
                (ok ? "tu_choi_thanh_cong" : "loi"));
        }
    }

    // =========================================================
    // UTILS
    // =========================================================

    private boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    private void sendJson(HttpServletResponse response, boolean success, String message)
            throws IOException {
        if (response.isCommitted()) return;
        response.setContentType("application/json;charset=UTF-8");
        String escaped = message.replace("\\", "\\\\").replace("\"", "\\\"");
        String json = String.format("{\"success\":%b,\"message\":\"%s\"}", success, escaped);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(json);
        }
    }
}
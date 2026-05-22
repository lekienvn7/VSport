package controller.admin;

import dao.DonHangDAO;
import dao.TraHangDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AdminDuyetDonHangServlet", urlPatterns = {"/admin/don-hang/duyet"})
public class AdminDuyetDonHangServlet extends HttpServlet {

    private final DonHangDAO donHangDAO = new DonHangDAO();
    private final TraHangDAO traHangDAO = new TraHangDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String action = request.getParameter("action");

        if (action == null || action.isBlank()) {
            sendJson(response, false, "Thiếu action");
            return;
        }

        switch (action) {
            case "duyet_don" ->
                xuLyDuyetDon(request, response);
            case "xac_nhan_thanh_toan" ->
                xuLyXacNhanThanhToan(request, response);
            case "duyet_tra_hang" ->
                xuLyDuyetTraHang(request, response);
            case "tu_choi_tra_hang" ->
                xuLyTuChoiTraHang(request, response);
            default ->
                sendJson(response, false, "Action không hợp lệ: " + action);
        }
    }

    // =========================================================
    // ĐƠN HÀNG
    // =========================================================
    private void xuLyDuyetDon(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int maDonHang = parseMaDonHang(request, response);
        if (maDonHang < 0) {
            return;
        }

        boolean ok = donHangDAO.capNhatTrangThaiDaXacNhan(maDonHang);
        sendJson(response, ok, ok ? "Duyệt đơn thành công" : "Không thể duyệt đơn hoặc đơn không còn ở trạng thái chờ xác nhận");
    }

    private void xuLyXacNhanThanhToan(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int maDonHang = parseMaDonHang(request, response);
        if (maDonHang < 0) {
            return;
        }

        boolean ok = donHangDAO.xacNhanThanhToanChuyenKhoan(maDonHang);
        sendJson(response, ok, ok ? "Xác nhận thanh toán thành công" : "Không thể xác nhận thanh toán");
    }

    // =========================================================
    // TRẢ HÀNG
    // =========================================================
    private void xuLyDuyetTraHang(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int maTraHang = parseMaTraHang(request, response);
        if (maTraHang < 0) {
            return;
        }

        boolean ok = traHangDAO.duyetTraHang(maTraHang);
        sendJson(response, ok, ok ? "Duyệt trả hàng thành công" : "Không thể duyệt hoặc yêu cầu không còn ở trạng thái chờ xử lý");
    }

    private void xuLyTuChoiTraHang(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int maTraHang = parseMaTraHang(request, response);
        if (maTraHang < 0) {
            return;
        }

        boolean ok = traHangDAO.tuChoiTraHang(maTraHang);
        sendJson(response, ok, ok ? "Từ chối trả hàng thành công" : "Từ chối thất bại");
    }

    // =========================================================
    // UTILS
    // =========================================================
    private int parseMaDonHang(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String raw = request.getParameter("maDonHang");
        if (raw == null || raw.isBlank()) {
            sendJson(response, false, "Thiếu mã đơn hàng");
            return -1;
        }
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            sendJson(response, false, "Mã đơn hàng không hợp lệ");
            return -1;
        }
    }

    private int parseMaTraHang(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String raw = request.getParameter("maTraHang");
        if (raw == null || raw.isBlank()) {
            sendJson(response, false, "Thiếu mã trả hàng");
            return -1;
        }
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            sendJson(response, false, "Mã trả hàng không hợp lệ");
            return -1;
        }
    }

    private void sendJson(HttpServletResponse response, boolean success, String message)
            throws IOException {
        if (response.isCommitted()) {
            return;
        }
        String escaped = message.replace("\\", "\\\\").replace("\"", "\\\"");
        String json = String.format("{\"success\":%b,\"message\":\"%s\"}", success, escaped);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(json);
        }
    }
}

package controller;

import dao.DonHangDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.NguoiDung;

import java.io.IOException;

@WebServlet("/don-hang/huy")
public class HuyDonHangServlet extends HttpServlet {

    private final DonHangDAO donHangDAO = new DonHangDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("nguoiDung") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Vui lòng đăng nhập");
            return;
        }

        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");

        String maDonHangRaw = request.getParameter("maDonHang");
        System.out.println("=== HUY DON HANG ===");
        System.out.println("maDonHangRaw = " + maDonHangRaw);
        System.out.println("maNguoiDung = " + nguoiDung.getMaNguoiDung());

        if (maDonHangRaw == null || maDonHangRaw.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Thiếu mã đơn hàng");
            return;
        }

        int maDonHang;
        try {
            maDonHang = Integer.parseInt(maDonHangRaw.trim());
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Mã đơn hàng không hợp lệ");
            return;
        }

        boolean success = donHangDAO.huyDonHangVaHoanTonVaVoucher(
                maDonHang,
                nguoiDung.getMaNguoiDung()
        );

        System.out.println("success = " + success);

        if (success) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Hủy đơn hàng thành công");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Không thể hủy đơn hàng");
        }
    }
}
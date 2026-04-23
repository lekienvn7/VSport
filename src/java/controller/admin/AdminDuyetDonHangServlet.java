package controller.admin;

import dao.DonHangDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "AdminDuyetDonHangServlet", urlPatterns = {"/admin/don-hang/duyet"})
public class AdminDuyetDonHangServlet extends HttpServlet {

    private final DonHangDAO donHangDAO = new DonHangDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        String maDonHangRaw = request.getParameter("maDonHang");

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

        boolean ok = donHangDAO.capNhatTrangThaiChoLayHang(maDonHang);

        if (ok) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Duyệt đơn thành công");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Không thể duyệt đơn hoặc đơn không còn ở trạng thái chờ xác nhận");
        }
    }
}
package controller;

import dao.GioHangDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.GioHang;
import model.GioHangSum;
import model.NguoiDung;
import model.SanPham;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AddToCartServlet", urlPatterns = {"/gio-hang/them"})
public class GioHangServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            String maSanPhamRaw = request.getParameter("maSanPham");
            String maBienTheRaw = request.getParameter("maBienThe");
            String soLuongRaw = request.getParameter("soLuong");

            if (maSanPhamRaw == null || maBienTheRaw == null || soLuongRaw == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu dữ liệu thêm giỏ hàng");
                return;
            }
            
            

            int maSanPham = Integer.parseInt(maSanPhamRaw);
            int maBienThe = Integer.parseInt(maBienTheRaw);
            int soLuong = Integer.parseInt(soLuongRaw);

            if (soLuong <= 0) {
                soLuong = 1;
            }

            // TEST TẠM


            // BẬT LẠI KHI CÓ LOGIN
            Object userObj = request.getSession().getAttribute("nguoiDung");
            if (userObj == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vui lòng đăng nhập");
                return;
            }
            NguoiDung nguoiDung = (NguoiDung) userObj;
            int maNguoiDung = nguoiDung.getMaNguoiDung();

            GioHangDAO gioHangDAO = new GioHangDAO();

            boolean success = gioHangDAO.themHoacCongDon(maNguoiDung, maSanPham, maBienThe, soLuong);

            if (!success) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Không thể thêm vào giỏ hàng");
                return;
            }

            GioHang item = gioHangDAO.getThongTinPopupItem(maNguoiDung, maBienThe);
            GioHangSum summary = gioHangDAO.getTongQuanGioHang(maNguoiDung);
            List<SanPham> goiYList = gioHangDAO.getSanPhamGoiY(maSanPham, 4);

            request.setAttribute("popupItem", item);
            request.setAttribute("cartSummary", summary);
            request.setAttribute("goiYList", goiYList);
            
            
            

            request.getRequestDispatcher("/WEB-INF/views/cart/cart-popup.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống khi thêm giỏ hàng");
        }
    }
}
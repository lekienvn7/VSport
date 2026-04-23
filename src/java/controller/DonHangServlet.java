package controller;

import dao.DonHangDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ChiTietDonHang;
import model.DonHang;
import model.NguoiDung;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "DonHangServlet", urlPatterns = {"/don-hang"})
public class DonHangServlet extends HttpServlet {

    private final DonHangDAO donHangDAO = new DonHangDAO();

    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    // 1. Auto update đơn đã giao
    int soDonDaGiao = donHangDAO.capNhatDonHangDaGiaoVaCongDaBan();

    // 2. Auto thanh toán + cộng vốn + ghi doanh thu
    int soDonDaThanhToan = donHangDAO.capNhatThanhToanSauKhiHoanThanh();

    // DEBUG (có thể xóa sau)
    System.out.println("Đơn đã giao: " + soDonDaGiao);
    System.out.println("Đơn đã thanh toán + cộng vốn: " + soDonDaThanhToan);

    // 3. Session check
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("nguoiDung") == null) {
        response.sendRedirect(request.getContextPath() + "/trang_chu");
        return;
    }

    NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
    int maNguoiDung = nguoiDung.getMaNguoiDung();

    // 4. Load đơn hàng
    List<DonHang> dsDonHang = donHangDAO.getDanhSachDonHangTheoNguoiDung(maNguoiDung);
    Map<Integer, List<ChiTietDonHang>> mapChiTiet = new HashMap<>();

    for (DonHang donHang : dsDonHang) {
        List<ChiTietDonHang> dsChiTiet =
                donHangDAO.getChiTietDonHangTheoMaDonHang(donHang.getMaDonHang());
        mapChiTiet.put(donHang.getMaDonHang(), dsChiTiet);
    }

    request.setAttribute("dsDonHang", dsDonHang);
    request.setAttribute("mapChiTiet", mapChiTiet);
    request.setAttribute("nguoiDung", nguoiDung);

    // 5. Mapping trạng thái
    Map<String, String> mapTrangThai = new HashMap<>();
    mapTrangThai.put("cho_xac_nhan", "Chờ xác nhận");
    mapTrangThai.put("da_xac_nhan", "Đang giao");
    mapTrangThai.put("da_giao", "Hoàn thành");
    mapTrangThai.put("da_huy", "Đã hủy");
    mapTrangThai.put("tra_hang", "Trả hàng / Hoàn tiền");

    request.setAttribute("mapTrangThai", mapTrangThai);

    Map<String, String> mapThanhToan = new HashMap<>();
    mapThanhToan.put("chua_thanh_toan", "Chưa thanh toán");
    mapThanhToan.put("da_thanh_toan", "Đã thanh toán");

    request.setAttribute("mapThanhToan", mapThanhToan);

    // 👇 OPTIONAL: show số tiền vừa cộng (UI debug)
    request.setAttribute("soDonDaThanhToan", soDonDaThanhToan);

    request.getRequestDispatcher("/WEB-INF/views/pages/order-history.jsp")
            .forward(request, response);
}
}
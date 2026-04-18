package controller.auth;

import dao.NguoiDungDAO;
import model.NguoiDung;
import utils.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "DangKyServlet", urlPatterns = {"/dang_ky"})
public class DangKyServlet extends HttpServlet {

    private final NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        session.setAttribute("openLoginPopup", true);
        session.setAttribute("openRegisterPopup", true);

        response.sendRedirect(request.getContextPath() + "/trang_chu");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String hoTen = request.getParameter("hoTen");
        String email = request.getParameter("email");
        String soDienThoai = request.getParameter("soDienThoai");
        String matKhau = request.getParameter("matKhau");
        String xacNhanMatKhau = request.getParameter("xacNhanMatKhau");

        hoTen = hoTen != null ? hoTen.trim() : "";
        email = email != null ? email.trim().toLowerCase() : "";
        soDienThoai = soDienThoai != null ? soDienThoai.trim() : "";
        matKhau = matKhau != null ? matKhau.trim() : "";
        xacNhanMatKhau = xacNhanMatKhau != null ? xacNhanMatKhau.trim() : "";

        String loi = null;

        if (hoTen.isEmpty()) {
            loi = "Họ tên không được để trống.";
        } else if (hoTen.length() < 2) {
            loi = "Họ tên phải có ít nhất 2 ký tự.";
        } else if (email.isEmpty()) {
            loi = "Email không được để trống.";
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            loi = "Email không đúng định dạng.";
        } else if (soDienThoai.isEmpty()) {
            loi = "Số điện thoại không được để trống.";
        } else if (!soDienThoai.matches("^\\d{9,11}$")) {
            loi = "Số điện thoại không hợp lệ.";
        } else if (matKhau.isEmpty()) {
            loi = "Mật khẩu không được để trống.";
        } else if (matKhau.length() < 6) {
            loi = "Mật khẩu phải có ít nhất 6 ký tự.";
        } else if (xacNhanMatKhau.isEmpty()) {
            loi = "Vui lòng nhập xác nhận mật khẩu.";
        } else if (!matKhau.equals(xacNhanMatKhau)) {
            loi = "Xác nhận mật khẩu không khớp.";
        } else if (nguoiDungDAO.getByEmail(email) != null) {
            loi = "Email này đã tồn tại.";
        } else if (nguoiDungDAO.getByPhone(soDienThoai) != null) {
            loi = "Số điện thoại này đã tồn tại.";
        }

        HttpSession session = request.getSession();

        if (loi != null) {
            session.setAttribute("registerError", loi);
            session.setAttribute("openLoginPopup", true);
            session.setAttribute("openRegisterPopup", true);

            session.setAttribute("registerHoTen", hoTen);
            session.setAttribute("registerEmail", email);
            session.setAttribute("registerSoDienThoai", soDienThoai);

            response.sendRedirect(request.getContextPath() + "/trang_chu");
            return;
        }

        NguoiDung nd = new NguoiDung();
        nd.setHoTen(hoTen);
        nd.setEmail(email);
        nd.setSoDienThoai(soDienThoai);
        nd.setMatKhau(PasswordUtil.hashPassword(matKhau));
        nd.setVaiTro("khach_hang");
        nd.setTrangThai("hoat_dong");
        nd.setSoXu(0);

        boolean thanhCong = nguoiDungDAO.insert(nd);

        if (thanhCong) {
            session.setAttribute("thongBao", "Đăng ký thành công. Vui lòng đăng nhập.");
            session.setAttribute("openLoginPopup", true);
            session.removeAttribute("openRegisterPopup");

            session.removeAttribute("registerError");
            session.removeAttribute("registerHoTen");
            session.removeAttribute("registerEmail");
            session.removeAttribute("registerSoDienThoai");

            response.sendRedirect(request.getContextPath() + "/trang_chu");
        } else {
            session.setAttribute("registerError", "Đăng ký thất bại. Vui lòng thử lại.");
            session.setAttribute("openLoginPopup", true);
            session.setAttribute("openRegisterPopup", true);

            session.setAttribute("registerHoTen", hoTen);
            session.setAttribute("registerEmail", email);
            session.setAttribute("registerSoDienThoai", soDienThoai);

            response.sendRedirect(request.getContextPath() + "/trang_chu");
        }
    }
}
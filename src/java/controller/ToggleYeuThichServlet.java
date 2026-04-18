package controller;

import dao.YeuThichDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.NguoiDung;

import java.io.IOException;

@WebServlet(name = "ToggleYeuThichServlet", urlPatterns = {"/yeu_thich/toggle"})
public class ToggleYeuThichServlet extends HttpServlet {

    private final YeuThichDAO yeuThichDAO = new YeuThichDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("nguoiDung") == null) {
            response.getWriter().write("{\"success\":false,\"needLogin\":true,\"message\":\"Vui lòng đăng nhập\"}");
            return;
        }

        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        int maNguoiDung = nguoiDung.getMaNguoiDung();

        String maSanPhamStr = request.getParameter("maSanPham");
        if (maSanPhamStr == null || maSanPhamStr.trim().isEmpty()) {
            response.getWriter().write("{\"success\":false,\"message\":\"Thiếu mã sản phẩm\"}");
            return;
        }

        int maSanPham = Integer.parseInt(maSanPhamStr);

        boolean daYeuThich = yeuThichDAO.daYeuThich(maNguoiDung, maSanPham);

        boolean success;
        String action;

        if (daYeuThich) {
            success = yeuThichDAO.xoaYeuThich(maNguoiDung, maSanPham);
            action = "removed";
        } else {
            success = yeuThichDAO.themYeuThich(maNguoiDung, maSanPham);
            action = "added";
        }

        response.getWriter().write(
            "{\"success\":" + success + ",\"action\":\"" + action + "\"}"
        );
    }
}
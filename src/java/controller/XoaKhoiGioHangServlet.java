package controller;

import dao.GioHangDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import model.NguoiDung;

@WebServlet(name = "XoaKhoiGioHangServlet", urlPatterns = {"/gio_hang/xoa"})
public class XoaKhoiGioHangServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String maBienTheRaw = request.getParameter("maBienThe");
            if (maBienTheRaw == null || maBienTheRaw.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/gio_hang");
                return;
            }

            Object userObj = request.getSession().getAttribute("nguoiDung");

        if (userObj == null) {
            response.sendRedirect(request.getContextPath() + "/trang_chu");
            return;
        }

        NguoiDung nguoiDung = (NguoiDung) userObj;
        int maNguoiDung = nguoiDung.getMaNguoiDung();
            int maBienThe = Integer.parseInt(maBienTheRaw);

            GioHangDAO gioHangDAO = new GioHangDAO();
            gioHangDAO.xoaKhoiGioHang(maNguoiDung, maBienThe);
            
            

            response.sendRedirect(request.getContextPath() + "/gio_hang");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/gio_hang");
        }
    }
}
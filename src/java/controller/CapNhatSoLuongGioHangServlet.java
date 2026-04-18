package controller;

import dao.GioHangDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "CapNhatSoLuongGioHangServlet", urlPatterns = {"/gio_hang/cap_nhat_so_luong"})
public class CapNhatSoLuongGioHangServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            int maGioHang = Integer.parseInt(request.getParameter("maGioHang"));
            int soLuong = Integer.parseInt(request.getParameter("soLuong"));

            if (soLuong < 1) soLuong = 1;
            if (soLuong > 20) soLuong = 20;

            GioHangDAO gioHangDAO = new GioHangDAO();
            boolean ok = gioHangDAO.capNhatSoLuongTheoMaGioHang(maGioHang, soLuong);

            System.out.println("Cap nhat so luong: " + ok + " | maGioHang=" + maGioHang + " | soLuong=" + soLuong);

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/gio_hang");
    }
}
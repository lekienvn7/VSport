/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.admin;

import dao.TraHangDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.TraHang;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminTraHangServlet", urlPatterns = {"/admin/tra-hang"})
public class AdminTraHangServlet extends HttpServlet {

    private final TraHangDAO traHangDAO = new TraHangDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println(">>> AdminTraHangServlet được gọi");
        List<TraHang> dsYeuCauTraHang = traHangDAO.getYeuCauTheoTrangThai("cho_xu_ly");
        System.out.println(">>> Số yêu cầu tìm thấy: " + (dsYeuCauTraHang != null ? dsYeuCauTraHang.size() : 0));

        request.setAttribute("dsYeuCauTraHang", dsYeuCauTraHang); // quan trọng

        Map<String, String> mapTrangThai = new HashMap<>();
        
        mapTrangThai.put("cho_xu_ly", "Chờ duyệt");
        mapTrangThai.put("da_hoan", "Đã hoàn");
        mapTrangThai.put("tu_choi", "Từ chối");
        request.setAttribute("mapTrangThaiTraHang", mapTrangThai);

        request.getRequestDispatcher("/WEB-INF/views/admin/order/order-tra-hang.jsp")
                .forward(request, response);
    }

}

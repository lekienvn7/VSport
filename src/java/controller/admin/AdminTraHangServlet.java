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
        List<TraHang> dsYeuCauTraHang = traHangDAO.getYeuCauTheoTrangThai("cho_xu_ly"); // hoặc getYeuCauTheoTrangThai("cho_xu_ly")
        
        request.setAttribute("dsYeuCauTraHang", dsYeuCauTraHang); // quan trọng
        
        request.getRequestDispatcher("/WEB-INF/views/admin/order/order-tra-hang.jsp")
                .forward(request, response);
    }

}

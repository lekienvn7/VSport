package controller;

import dao.BienTheSanPhamDAO;
import dao.SanPhamDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.BienTheSanPham;
import model.DanhGiaSanPham;
import model.SanPham;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductDetailServlet", urlPatterns = {"/chi-tiet-san-pham/*"})
public class ProductDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        try {
            int maSanPham = Integer.parseInt(pathInfo.substring(1));

            SanPhamDAO sanPhamDAO = new SanPhamDAO();
            BienTheSanPhamDAO bienTheSanPhamDAO = new BienTheSanPhamDAO();

            SanPham sp = sanPhamDAO.getById(maSanPham);

            if (sp == null) {
                request.getRequestDispatcher("/WEB-INF/views/error/404.jsp").forward(request, response);
                return;
            }

            List<DanhGiaSanPham> dsDanhGia = sanPhamDAO.getReviewsByProductId(maSanPham);
            List<BienTheSanPham> dsBienThe = bienTheSanPhamDAO.getByProductId(maSanPham);

            request.setAttribute("sp", sp);
            request.setAttribute("spInfo", sp);
            request.setAttribute("dsDanhGia", dsDanhGia);
            request.setAttribute("dsBienThe", dsBienThe);
            request.setAttribute("activePage", "bong_da");

            request.getRequestDispatcher("/WEB-INF/views/pages/product-detail.jsp").forward(request, response);
            
            

        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.getRequestDispatcher("/WEB-INF/views/error/404.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher("/WEB-INF/views/error/404.jsp").forward(request, response);
        }
    }
}


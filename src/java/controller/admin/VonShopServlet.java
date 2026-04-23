package controller.admin;

import dao.VonShopDAO;
import dao.VonShopPageDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.LichSuVonShop;
import model.QuyVonShop;
import utils.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/admin/von-shop")
public class VonShopServlet extends HttpServlet {

    private final VonShopDAO vonShopDAO = new VonShopDAO();
    private final VonShopPageDAO vonShopPageDAO = new VonShopPageDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection conn = DBConnection.getConnection()) {
            vonShopDAO.congVonHangNgayNeuCan(conn);
        } catch (Exception e) {
            throw new ServletException("Lỗi cộng vốn hằng ngày", e);
        }

        try {
            QuyVonShop quyVon = vonShopPageDAO.getQuyVonShop();
            List<LichSuVonShop> dsLichSuVon = vonShopPageDAO.getDanhSachLichSuVon();

            request.setAttribute("quyVon", quyVon);
            request.setAttribute("dsLichSuVon", dsLichSuVon);

            request.getRequestDispatcher("/WEB-INF/views/admin/wallet.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Lỗi tải trang vốn shop", e);
        }
    }
}
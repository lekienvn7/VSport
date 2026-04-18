package controller;

import dao.MaGiamGiaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.NguoiDung;

import java.io.IOException;

@WebServlet(name = "DoiMaGiamGiaServlet", urlPatterns = {"/ma_giam_gia/doi"})
public class DoiMaGiamGiaServlet extends HttpServlet {

    private final MaGiamGiaDAO maGiamGiaDAO = new MaGiamGiaDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        NguoiDung nguoiDung = null;

        if (session != null && session.getAttribute("nguoiDung") != null) {
            nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        }

        if (nguoiDung == null) {
            response.sendRedirect(request.getContextPath() + "/trang_chu");
            return;
        }

        String maGiamGiaStr = request.getParameter("maGiamGia");

        try {
            int maGiamGia = Integer.parseInt(maGiamGiaStr);
            String result = maGiamGiaDAO.doiMaBangXu(nguoiDung.getMaNguoiDung(), maGiamGia);

            if ("success".equals(result)) {
                response.sendRedirect(request.getContextPath() + "/ma_giam_gia?success=doi_ma");
            } else {
                response.sendRedirect(request.getContextPath() + "/ma_giam_gia?error=" + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/ma_giam_gia?error=system");
        }
    }
}
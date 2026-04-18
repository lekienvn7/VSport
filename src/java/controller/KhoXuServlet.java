package controller;

import dao.XuDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.KhoXu;
import model.LichSuXu;
import model.NguoiDung;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "KhoXuServlet", urlPatterns = {"/kho_xu"})
public class KhoXuServlet extends HttpServlet {

    private final XuDAO xuDAO = new XuDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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

        int maNguoiDung = nguoiDung.getMaNguoiDung();

        KhoXu khoXuInfo = xuDAO.getKhoXuInfo(maNguoiDung);
        List<LichSuXu> dsLichSuXu = xuDAO.getLichSuXu(maNguoiDung);

        request.setAttribute("khoXuInfo", khoXuInfo);
        request.setAttribute("dsLichSuXu", dsLichSuXu);

        String success = request.getParameter("success");
        String error = request.getParameter("error");

        if ("checkin".equals(success)) {
            request.setAttribute("successMessage", "Điểm danh thành công, xu đã rơi vào ví.");
        }

        if ("already".equals(error)) {
            request.setAttribute("errorMessage", "Hôm nay bạn đã nhận xu rồi.");
        } else if ("login".equals(error)) {
            request.setAttribute("errorMessage", "Bạn cần đăng nhập để nhận xu.");
        } else if ("system".equals(error)) {
            request.setAttribute("errorMessage", "Có lỗi xảy ra, thử lại sau.");
        }

        request.getRequestDispatcher("/WEB-INF/views/pages/kho-xu.jsp").forward(request, response);
    }
}
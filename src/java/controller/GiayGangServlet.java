package controller;

import dao.BoLocSanPhamDAO;
import dao.SanPhamDAO;
import dao.ThuongHieuDAO;
import dao.YeuThichDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.NguoiDung;
import model.SanPham;
import model.ThuongHieu;
import model.YeuThich;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/giay_gang_bong_da")
public class GiayGangServlet extends HttpServlet {

    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private final BoLocSanPhamDAO boLocSanPhamDAO = new BoLocSanPhamDAO();
    private final ThuongHieuDAO thuongHieuDAO = new ThuongHieuDAO();

    private static final int MA_DANH_MUC_GIAY_BONG_DA = 2;
    private static final String DANH_MUC_SLUG = "giay-bong-da";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        request.setAttribute("activePage", "giay_gang_bong_da");

        String[] loaiList = request.getParameterValues("loai");
        String[] thuongHieuList = request.getParameterValues("thuongHieu");
        String[] sizeList = request.getParameterValues("size");

        String giaMin = trimOrNull(request.getParameter("giaMin"));
        String giaMax = trimOrNull(request.getParameter("giaMax"));
        String sort = trimOrNull(request.getParameter("sort"));

        if (sort == null || sort.isEmpty()) {
            sort = "price_asc";
        }

        List<SanPham> danhSachSanPham = sanPhamDAO.getSanPhamGiayGangDaLoc(
                MA_DANH_MUC_GIAY_BONG_DA,
                loaiList,
                thuongHieuList,
                sizeList,
                giaMin,
                giaMax,
                sort
        );

        int tongSanPhamLoc = boLocSanPhamDAO.demSanPhamGiayGangSauLoc(
                MA_DANH_MUC_GIAY_BONG_DA,
                loaiList,
                thuongHieuList,
                sizeList,
                giaMin,
                giaMax
        );

        List<ThuongHieu> dsThuongHieuNav = thuongHieuDAO.getAllThuongHieuDangDungChoDanhMuc(MA_DANH_MUC_GIAY_BONG_DA);

        HttpSession session = request.getSession(false);
        List<YeuThich> dsYeuThich = new ArrayList<>();

        if (session != null && session.getAttribute("nguoiDung") != null) {
            NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
            YeuThichDAO yeuThichDAO = new YeuThichDAO();
            dsYeuThich = yeuThichDAO.getDanhSachYeuThich(nguoiDung.getMaNguoiDung());
        }

        request.setAttribute("pageTitle", "GIÀY · GĂNG · BÓNG ĐÁ");
        request.setAttribute("danhMucSlug", DANH_MUC_SLUG);
        request.setAttribute("danhSachSanPham", danhSachSanPham);
        request.setAttribute("dsThuongHieuNav", dsThuongHieuNav);
        request.setAttribute("dsYeuThich", dsYeuThich);

        request.setAttribute("dsLoaiSanPham", boLocSanPhamDAO.getLoaiSanPhamOptionsTheoDanhMuc(MA_DANH_MUC_GIAY_BONG_DA));
        request.setAttribute("dsThuongHieu", boLocSanPhamDAO.getThuongHieuOptionsTheoDanhMuc(MA_DANH_MUC_GIAY_BONG_DA));
        request.setAttribute("dsKichCo", boLocSanPhamDAO.getSizeOptionsTheoDanhMuc(MA_DANH_MUC_GIAY_BONG_DA));
        request.setAttribute("tongSanPhamLoc", tongSanPhamLoc);

        request.setAttribute("sortDangChon", sort);
        request.setAttribute("currentFilterAction", buildCurrentFilterAction(request));

        request.getRequestDispatcher("/WEB-INF/views/pages/giay-gang-bong-da.jsp")
                .forward(request, response);
    }

    private String trimOrNull(String value) {
        if (value == null) return null;
        value = value.trim();
        return value.isEmpty() ? null : value;
    }

    private String buildCurrentFilterAction(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        return contextPath + servletPath;
    }
}
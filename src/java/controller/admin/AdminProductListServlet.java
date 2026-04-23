package controller.admin;

import dao.DanhMucDAO;
import dao.DoiBongDao;
import dao.SanPhamDAO;
import dao.SizeSanPhamDAO;
import dao.ThuongHieuDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.DanhMuc;
import model.DoiBong;
import model.SanPham;
import model.ThuongHieu;
import dao.VonShopDAO;
import java.sql.Connection;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import utils.DBConnection;

@WebServlet("/admin/san-pham")
public class AdminProductListServlet extends HttpServlet {

    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private final DanhMucDAO danhMucDAO = new DanhMucDAO();
    private final ThuongHieuDAO thuongHieuDAO = new ThuongHieuDAO();
    private final DoiBongDao doiBongDao = new DoiBongDao();
    private final SizeSanPhamDAO sizeSanPhamDAO = new SizeSanPhamDAO();
    
    
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        VonShopDAO vonShopDAO = new VonShopDAO();
        
        try (Connection conn = DBConnection.getConnection()) {
        vonShopDAO.congVonHangNgayNeuCan(conn); // nếu muốn auto cộng ngày
        BigDecimal vonHienTai = vonShopDAO.getSoDuHienTai(conn);
        request.setAttribute("vonHienTai", vonHienTai);
}       catch (SQLException ex) {
            System.getLogger(AdminProductListServlet.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        String keyword = trimOrNull(request.getParameter("keyword"));
        String createdFrom = trimOrNull(request.getParameter("createdFrom"));
        String createdTo = trimOrNull(request.getParameter("createdTo"));
        String trangThai = trimOrNull(request.getParameter("trangThai"));
        String maDanhMuc = trimOrNull(request.getParameter("maDanhMuc"));
        String maThuongHieu = trimOrNull(request.getParameter("maThuongHieu"));
        String maDoiBong = trimOrNull(request.getParameter("maDoiBong"));

        int page = parseIntOrDefault(request.getParameter("page"), 1);
        int pageSize = parseIntOrDefault(request.getParameter("size"), 10);

        if (page < 1) page = 1;
        if (pageSize <= 0) pageSize = 10;

        int offset = (page - 1) * pageSize;

        List<SanPham> dsSanPham = sanPhamDAO.getAdminProductList(
                keyword,
                createdFrom,
                createdTo,
                trangThai,
                maDanhMuc,
                maThuongHieu,
                maDoiBong,
                offset,
                pageSize
        );

        int totalRecords = sanPhamDAO.countAdminProductList(
                keyword,
                createdFrom,
                createdTo,
                trangThai,
                maDanhMuc,
                maThuongHieu,
                maDoiBong
        );

        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        if (page < 1) page = 1;
        if (offset >= totalRecords) page = 1;

        List<DanhMuc> dsDanhMuc = danhMucDAO.getAllDanhMuc();
        List<ThuongHieu> dsThuongHieu = thuongHieuDAO.getAllThuongHieu();
        List<DoiBong> dsDoiBong = doiBongDao.getAllDoiBong();

        request.setAttribute("dsSanPham", dsSanPham);
        request.setAttribute("dsDanhMuc", dsDanhMuc);
        request.setAttribute("dsThuongHieu", dsThuongHieu);
        request.setAttribute("dsDoiBong", dsDoiBong);
        try {
            request.setAttribute("dsSize", sizeSanPhamDAO.getAll());
        } catch (Exception e) {
            throw new ServletException("Lỗi khi lấy danh sách size", e);
        }

        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalRecords", totalRecords);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/WEB-INF/views/admin/product/list.jsp")
                .forward(request, response);
    }

    private String trimOrNull(String value) {
        if (value == null) return null;
        value = value.trim();
        return value.isEmpty() ? null : value;
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
package controller.admin;

import dao.AdminSanPhamVonDAO;
import dao.AdminSanPhamVonDAO.BienTheNhap;
import dao.AdminSanPhamVonDAO.ThemSanPhamRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/san-pham/them-co-von")
public class ThemSanPhamCoVonServlet extends HttpServlet {

    private final AdminSanPhamVonDAO sanPhamVonDAO = new AdminSanPhamVonDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("===== ADD PRODUCT PARAMS =====");
request.getParameterMap().forEach((k, v) -> {
    System.out.println(k + " = " + java.util.Arrays.toString(v));
});
System.out.println("=================================");

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        try {
            ThemSanPhamRequest req = buildRequestFromForm(request);

            int maSanPhamMoi = sanPhamVonDAO.themSanPhamKemTruVon(req);

            response.getWriter().write("""
                {
                  "success": true,
                  "message": "Thêm sản phẩm thành công và đã trừ vốn shop.",
                  "maSanPham": %d
                }
            """.formatted(maSanPhamMoi));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("""
                {
                  "success": false,
                  "message": "%s"
                }
            """.formatted(escapeJson(e.getMessage())));
        }
    }

    private ThemSanPhamRequest buildRequestFromForm(HttpServletRequest request) {
        ThemSanPhamRequest req = new ThemSanPhamRequest();

        req.maDanhMuc = parseIntRequired(request.getParameter("maDanhMuc"));
        req.maThuongHieu = parseInteger(request.getParameter("maThuongHieu"));
        req.maDoiBong = parseInteger(request.getParameter("maDoiBong"));
        req.maBoSuuTap = parseInteger(request.getParameter("maBoSuuTap"));
        req.nhomSanPham = parseInteger(request.getParameter("nhomSanPham"));

        req.tenSanPham = trimToNull(request.getParameter("tenSanPham"));
        req.slug = trimToNull(request.getParameter("slug"));
        req.loaiSanPham = trimToNull(request.getParameter("loaiSanPham"));
        req.moTaNgan = trimToNull(request.getParameter("moTaNgan"));
        req.moTaChiTiet = trimToNull(request.getParameter("moTaChiTiet"));
        req.anhChinh = trimToNull(request.getParameter("anhChinh"));
        req.trangThai = trimToNull(request.getParameter("trangThai"));
        req.tuKhoaPhu = trimToNull(request.getParameter("tuKhoaPhu"));

        req.giaNiemYet = parseBigDecimalRequired(request.getParameter("giaNiemYet"));
        req.giaKhuyenMai = parseBigDecimal(request.getParameter("giaKhuyenMai"));
        req.giaNhapGoc = parseBigDecimalRequired(request.getParameter("giaNhapGoc"));

        req.thuocTinh1 = trimToNull(request.getParameter("thuocTinh1"));
        req.giaTri1 = trimToNull(request.getParameter("giaTri1"));
        req.thuocTinh2 = trimToNull(request.getParameter("thuocTinh2"));
        req.giaTri2 = trimToNull(request.getParameter("giaTri2"));
        req.thuocTinh3 = trimToNull(request.getParameter("thuocTinh3"));
        req.giaTri3 = trimToNull(request.getParameter("giaTri3"));

        req.dsAnhPhu = buildDanhSachAnhPhu(request);
        req.dsBienThe = buildDanhSachBienThe(request);
        
        

        validateRequest(req);

        return req;
    }

    private List<String> buildDanhSachAnhPhu(HttpServletRequest request) {
        List<String> dsAnhPhu = new ArrayList<>();
        String[] anhPhuArr = request.getParameterValues("anhPhu");

        if (anhPhuArr != null) {
            for (String anh : anhPhuArr) {
                String value = trimToNull(anh);
                if (value != null) {
                    dsAnhPhu.add(value);
                }
            }
        }

        return dsAnhPhu;
    }

    private List<BienTheNhap> buildDanhSachBienThe(HttpServletRequest request) {
        String[] maSizeArr = request.getParameterValues("maSize");
        String[] soLuongArr = request.getParameterValues("soLuongTon");
        String[] giaRiengArr = request.getParameterValues("giaRieng");

        List<BienTheNhap> dsBienThe = new ArrayList<>();

        if (maSizeArr == null || soLuongArr == null) {
            return dsBienThe;
        }

        for (int i = 0; i < maSizeArr.length; i++) {
            Integer maSize = parseInteger(maSizeArr[i]);
            Integer soLuongTon = parseInteger(soLuongArr[i]);

            if (maSize == null || soLuongTon == null || soLuongTon <= 0) {
                continue;
            }

            BigDecimal giaRieng = null;
            if (giaRiengArr != null && i < giaRiengArr.length) {
                giaRieng = parseBigDecimal(giaRiengArr[i]);
            }

            dsBienThe.add(new BienTheNhap(maSize, soLuongTon, giaRieng));
        }

        return dsBienThe;
    }

    private void validateRequest(ThemSanPhamRequest req) {
        if (req.tenSanPham == null) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống.");
        }

        if (req.slug == null) {
            throw new IllegalArgumentException("Slug không được để trống.");
        }

        if (req.loaiSanPham == null) {
            throw new IllegalArgumentException("Loại sản phẩm không được để trống.");
        }

        if (req.giaNiemYet.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Giá niêm yết không hợp lệ.");
        }

        if (req.giaNhapGoc.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá nhập phải lớn hơn 0.");
        }

        if (req.dsBienThe == null || req.dsBienThe.isEmpty()) {
            throw new IllegalArgumentException("Phải có ít nhất 1 biến thể có số lượng lớn hơn 0.");
        }
    }

    private String trimToNull(String s) {
        if (s == null) return null;
        String value = s.trim();
        return value.isEmpty() ? null : value;
    }

    private Integer parseInteger(String s) {
        String value = trimToNull(s);
        return value == null ? null : Integer.parseInt(value);
    }

    private int parseIntRequired(String s) {
        String value = trimToNull(s);
        if (value == null) {
            throw new IllegalArgumentException("Thiếu trường số bắt buộc.");
        }
        return Integer.parseInt(value);
    }

    private BigDecimal parseBigDecimal(String s) {
        String value = trimToNull(s);
        return value == null ? null : new BigDecimal(value);
    }

    private BigDecimal parseBigDecimalRequired(String s) {
        String value = trimToNull(s);
        if (value == null) {
            throw new IllegalArgumentException("Thiếu giá trị tiền bắt buộc.");
        }
        return new BigDecimal(value);
    }

    private String escapeJson(String s) {
        if (s == null) return "Có lỗi xảy ra.";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
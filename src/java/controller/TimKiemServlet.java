package controller;

import dao.SanPhamDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.TimKiem;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@WebServlet(name = "SearchProductServlet", urlPatterns = {"/tim_kiem"})
public class TimKiemServlet extends HttpServlet {

    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String keyword = request.getParameter("keyword");
        PrintWriter out = response.getWriter();

        if (keyword == null || keyword.trim().isEmpty()) {
            out.print("""
                <div class="search-empty-state">
                    <p>Gõ tên sản phẩm, đội bóng, thương hiệu, danh mục, size...</p>
                </div>
            """);
            return;
        }

        List<TimKiem> products = sanPhamDAO.timKiemSanPham(keyword);

        if (products == null || products.isEmpty()) {
            out.print("""
                <div class="search-empty-state">
                    <p>Không tìm thấy sản phẩm nào khớp với từ khóa của bạn.</p>
                </div>
            """);
            return;
        }

        out.print("<div class='search-result-grid'>");

        for (TimKiem sp : products) {
            String productUrl = request.getContextPath() + "/chi-tiet-san-pham/" + sp.getMaSanPham();

            String imageUrl;
            if (sp.getAnhChinh() != null && !sp.getAnhChinh().trim().isEmpty()) {
                imageUrl = request.getContextPath() + "/" + sp.getAnhChinh();
            } else {
                imageUrl = request.getContextPath() + "/assets/images/no-image.png";
            }

            String tenSp = escapeHtml(sp.getTenSanPham());
            String tenDoi = escapeHtml(sp.getTenDoiBong());
            String tenThuongHieu = escapeHtml(sp.getTenThuongHieu());

            double giaNiemYet = sp.getGiaNiemYet();
            double giaKhuyenMai = sp.getGiaKhuyenMai();
            double giaSanPham = sp.getGiaSanPham();

            double giaHienThi = giaKhuyenMai > 0 ? giaKhuyenMai : giaSanPham;
            if (giaHienThi <= 0) {
                giaHienThi = giaNiemYet;
            }

            boolean dangSale = giaKhuyenMai > 0 && giaNiemYet > 0 && giaKhuyenMai < giaNiemYet;

            out.print("<a href='" + productUrl + "' class='search-product-card'>");

            out.print("<div class='search-product-thumb'>");
            out.print("<img src='" + imageUrl + "' alt='" + tenSp + "'>");
            out.print("</div>");

            out.print("<div class='search-product-info'>");

            if (!tenDoi.isBlank() || !tenThuongHieu.isBlank()) {
                out.print("<div class='search-product-meta'>");
                if (!tenDoi.isBlank()) {
                    out.print("<span>" + tenDoi + "</span>");
                }
                if (!tenThuongHieu.isBlank()) {
                    out.print("<span>" + tenThuongHieu + "</span>");
                }
                out.print("</div>");
            }

            out.print("<h3 class='search-product-name'>" + tenSp + "</h3>");

            if (dangSale) {
                int phanTramGiam = (int) Math.round((1 - giaKhuyenMai / giaNiemYet) * 100);
                out.print("<div class='search-product-badges'>");
                out.print("</div>");
            }

            out.print("<div class='search-product-price'>");

            if (dangSale) {
                out.print("<span class='old-price'>" + formatPrice(giaNiemYet) + "</span>");
                out.print("<span class='new-price'>" + formatPrice(giaKhuyenMai) + "</span>");
            } else {
                out.print("<span class='new-price'>" + formatPrice(giaHienThi) + "</span>");
            }

            out.print("</div>"); // search-product-price
            out.print("</div>"); // search-product-info
            out.print("</a>");
        }

        out.print("</div>");
    }

    private String formatPrice(double price) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        return nf.format(price) + "đ";
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}
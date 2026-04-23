    package controller;

    import dao.MaGiamGiaDAO;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.annotation.WebServlet;
    import jakarta.servlet.http.HttpServlet;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import jakarta.servlet.http.HttpSession;
    import model.LichSuMaGiamGia;
    import model.MaGiamGia;
    import model.NguoiDung;

    import java.io.IOException;
    import java.util.List;

    @WebServlet(name = "MaGiamGiaServlet", urlPatterns = {"/ma_giam_gia"})
    public class MaGiamGiaServlet extends HttpServlet {

        private final MaGiamGiaDAO maGiamGiaDAO = new MaGiamGiaDAO();

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

            int tongXu = maGiamGiaDAO.getTongXuNguoiDung(maNguoiDung);
            List<MaGiamGia> dsMaSoHuu = maGiamGiaDAO.getDanhSachMaSoHuu(maNguoiDung);
            List<MaGiamGia> dsMaCoTheDoi = maGiamGiaDAO.getDanhSachMaCoTheDoi();
            List<LichSuMaGiamGia> dsLichSuMa = maGiamGiaDAO.getLichSuNhanVaDungMa(maNguoiDung);

            System.out.println("===> maNguoiDung = " + maNguoiDung);
            System.out.println("===> dsLichSuMa size = " + dsLichSuMa.size());

            request.setAttribute("tongXu", tongXu);
            request.setAttribute("dsMaSoHuu", dsMaSoHuu);
            request.setAttribute("dsMaCoTheDoi", dsMaCoTheDoi);
            request.setAttribute("dsLichSuMa", dsLichSuMa);

            String success = request.getParameter("success");
            String error = request.getParameter("error");

            if ("doi_ma".equals(success)) {
                request.setAttribute("successMessage", "Đổi mã thành công. Xu đã bay khỏi ví, mã đã rơi vào túi.");
            }

            if ("not_enough_xu".equals(error)) {
                request.setAttribute("errorMessage", "Không đủ xu để đổi mã này.");
            } else if ("out_of_stock".equals(error)) {
                request.setAttribute("errorMessage", "Mã này đã hết lượt đổi.");
            } else if ("expired".equals(error)) {
                request.setAttribute("errorMessage", "Mã này đã hết hạn.");
            } else if ("inactive".equals(error)) {
                request.setAttribute("errorMessage", "Mã này hiện không hoạt động.");
            } else if ("not_exchangeable".equals(error)) {
                request.setAttribute("errorMessage", "Mã này không áp dụng đổi bằng xu.");
            } else if ("system".equals(error)) {
                request.setAttribute("errorMessage", "Có lỗi xảy ra trong lúc đổi mã.");
            }

            request.getRequestDispatcher("/WEB-INF/views/pages/ma-giam-gia.jsp").forward(request, response);
        }
    }
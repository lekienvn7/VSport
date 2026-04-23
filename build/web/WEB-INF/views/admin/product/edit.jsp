<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="admin-product-popup-overlay" id="editProductPopupOverlay"></div>

<div class="admin-product-popup" id="editProductPopup">
    <div class="admin-product-popup-header">
        <h2>Sửa sản phẩm</h2>
        <button type="button" class="admin-product-popup-close" id="closeEditProductPopup">×</button>
    </div>

    <form id="editProductForm"
          class="admin-product-popup-form"
          action="${pageContext.request.contextPath}/admin/san-pham/cap-nhat"
          method="post">

        <input type="hidden" name="maSanPham" id="editMaSanPham">

        <div class="admin-popup-grid">
            <div class="admin-form-group">
                <label>Tên sản phẩm</label>
                <input type="text" name="tenSanPham" id="editTenSanPham" required>
            </div>

            <div class="admin-form-group">
                <label>Danh mục</label>
                <select name="maDanhMuc" id="editMaDanhMuc" required>
                    <c:forEach var="dm" items="${dsDanhMuc}">
                        <option value="${dm.maDanhMuc}">${dm.tenDanhMuc}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="admin-form-group">
                <label>Thương hiệu</label>
                <select name="maThuongHieu" id="editMaThuongHieu">
                    <option value="">-- Chọn --</option>
                    <c:forEach var="th" items="${dsThuongHieu}">
                        <option value="${th.maThuongHieu}">${th.tenThuongHieu}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="admin-form-group">
                <label>Đội bóng</label>
                <select name="maDoiBong" id="editMaDoiBong">
                    <option value="">-- Chọn --</option>
                    <c:forEach var="db" items="${dsDoiBong}">
                        <option value="${db.maDoiBong}">${db.tenDoiBong}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="admin-form-group">
                <label>Giá khuyến mãi</label>
                <input type="number" name="giaKhuyenMai" id="editGiaKhuyenMai" min="0" step="1000">
            </div>

            <div class="admin-form-group">
                <label>Trạng thái</label>
                <select name="trangThai" id="editTrangThai">
                    <option value="dang_ban">Đang bán</option>
                    <option value="an">Ẩn</option>
                </select>
            </div>

            <div class="admin-form-group">
                <label>Nhóm sản phẩm</label>
                <input type="number" name="nhomSanPham" id="editNhomSanPham" min="1">
            </div>

            <div class="admin-form-group">
                <label>Mã bộ sưu tập</label>
                <input type="number" name="maBoSuuTap" id="editMaBoSuuTap" min="1">
            </div>

            <div class="admin-form-group admin-form-group-full">
                <label>Mô tả ngắn</label>
                <input type="text" name="moTaNgan" id="editMoTaNgan">
            </div>

            <div class="admin-form-group admin-form-group-full">
                <label>Mô tả chi tiết</label>
                <textarea name="moTaChiTiet" id="editMoTaChiTiet" rows="5"></textarea>
            </div>
            <div class="admin-form-group">
                <label>Giá niêm yết</label>
                <input type="number"
                       name="giaNiemYet"
                       id="editGiaNiemYet"
                       min="0"
                       step="1000"
                       readonly
                       class="admin-input-readonly">
            </div>
        </div>
        
        <div class="admin-subsection">
            <h3>Thông tin nhập thêm</h3>
            <div class="admin-popup-grid">
                <div class="admin-form-group">
                    <label>Giá nhập thêm / 1 sản phẩm</label>
                    <input type="number" name="giaNhapThem" id="editGiaNhapThem" min="0" step="1000" placeholder="Nhập giá vốn để trừ quỹ shop">
                </div>
            </div>
        </div>
        
        <div class="admin-capital-preview" id="editCapitalPreviewBox">
            <p>Số lượng nhập thêm: <strong id="editPreviewSoLuongNhapThem">0</strong></p>
            <p>Giá nhập thêm / 1 sản phẩm: <strong id="editPreviewGiaNhapThem">0</strong></p>
            <p>Tổng tiền sẽ trừ vốn: <strong id="editPreviewTongTienNhapThem">0</strong></p>

            <hr style="grid-column: 1 / -1; border: none; height: 1px; background: rgba(255,255,255,0.2); margin: 10px 0;">

            <p>Vốn hiện tại:
                <strong id="editPreviewVonHienTai">${vonHienTai}</strong>
            </p>
            <p>Vốn sau khi nhập:
                <strong id="editPreviewVonSau">0</strong>
            </p>
        </div>

        <div class="admin-subsection">
            <h3>Biến thể size và số lượng</h3>
            <div id="editVariantRows"></div>
            <button type="button" class="add-variant-btn" id="addEditVariantRowBtn">+ Thêm size</button>
        </div>

        <div class="admin-subsection">
            <h3>Ảnh phụ</h3>
            <div id="editSubImageRows"></div>
            <button type="button" class="add-sub-image-btn" id="addEditSubImageBtn">+ Thêm ảnh phụ</button>
        </div>

        <div class="admin-popup-actions">
            <button type="button" class="admin-btn-cancel" id="cancelEditProductPopup">Hủy</button>
            <button type="submit" class="admin-btn-submit">Cập nhật sản phẩm</button>
        </div>
    </form>
</div>

<script type="text/template" id="editVariantTemplate">
    <div class="admin-variant-row">
        <input type="hidden" name="maBienThe" value="">
        <select name="maSize" required>
            <c:forEach var="size" items="${dsSize}">
                <option value="${size.maSize}">${size.tenSize}</option>
            </c:forEach>
        </select>
        <input type="number" name="soLuongTon" min="0" placeholder="Số lượng" required>
        <input type="number" name="giaRieng" min="0" step="1000" placeholder="Giá riêng size (nếu có)">
        <button type="button" class="remove-variant-btn">Xóa</button>
    </div>
</script>

<script type="text/template" id="editSubImageTemplate">
    <div class="admin-sub-image-row">
        <input type="hidden" name="maAnh" value="">
        <input type="text" name="anhPhu" placeholder="/assets/images/...">
        <button type="button" class="remove-sub-image-btn">Xóa</button>
    </div>
</script>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="traHangModal" class="tra-hang-modal">
    <div class="tra-hang-box">

        <div class="tra-hang-header">
            <h3>Yêu cầu trả hàng / hoàn tiền</h3>
            <button type="button" class="tra-hang-close" id="closeTraHangModal" aria-label="Đóng">&times;</button>
        </div>

        <div class="tra-hang-body">
            <form id="traHangForm" novalidate>

                <%-- Hidden fields --%>
                <input type="hidden" id="maDonHangTraHang" name="maDonHang">
                <input type="hidden" id="soTienHoan"       name="soTienHoan">

                <%-- Lý do --%>
                <div class="form-group">
                    <label for="lyDoTraHang">
                        Lý do trả hàng <span class="required">*</span>
                    </label>
                    <select id="lyDoTraHang" name="lyDo" class="form-control" required>
                        <option value="">-- Chọn lý do --</option>
                        <option value="het_nhu_cau">Hết nhu cầu</option>
                        <option value="hang_hong">Hàng hỏng</option>
                        <option value="that_lac">Thất lạc</option>
                    </select>
                </div>

                <%-- Số tiền hoàn (chỉ đọc, tự động từ đơn hàng) --%>
                <div class="form-group">
                    <label for="soTienHoanDisplay">Số tiền hoàn</label>
                    <input type="text"
                           id="soTienHoanDisplay"
                           class="form-control"
                           readonly
                           placeholder="Tự động từ tổng đơn hàng">
                </div>

                <%-- Ghi chú --%>
                <div class="form-group">
                    <label for="ghiChuTraHang">Ghi chú</label>
                    <textarea id="ghiChuTraHang"
                              name="ghiChu"
                              class="form-control"
                              rows="3"
                              maxlength="500"
                              placeholder="Nhập ghi chú nếu có..."></textarea>
                </div>

                <%-- Actions --%>
                <div class="tra-hang-actions">
                    <button type="button"
                            class="tra-hang-btn tra-hang-btn-secondary"
                            id="cancelTraHang">
                        Hủy
                    </button>
                    <button type="submit"
                            class="tra-hang-btn tra-hang-btn-primary"
                            id="btnTaoYeuCau"
                            disabled>
                        Tạo yêu cầu
                    </button>
                </div>

            </form>
        </div>

    </div>
</div>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản lý trả hàng | Admin</title>
        <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/images/logos/logo-header.png">
        <jsp:include page="/WEB-INF/views/common/head.jsp" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/admin-dashboard.css">
    </head>
    <body>
        <div class="admin-shell">
            <%@ include file="/WEB-INF/views/admin/common/admin-sidebar.jsp" %>

            <main class="admin-main">
                <div class="admin-returns-page">
                    <div class="admin-returns-shell">

                        <div class="admin-returns-topbar">
                            <div>
                                <h1 class="admin-returns-title">Quản lý trả hàng</h1>
                                <p class="admin-returns-desc">
                                    Duyệt hoặc từ chối các yêu cầu trả hàng / hoàn tiền từ khách hàng.
                                </p>
                            </div>
                        </div>

                        <section class="admin-returns-section">
                            <div class="admin-returns-section-head">
                                <h2>Danh sách yêu cầu</h2>
                            </div>

                            <div class="admin-returns-list">
                                <c:choose>
                                    <c:when test="${not empty dsYeuCauTraHang}">
                                        <c:forEach var="traHang" items="${dsYeuCauTraHang}">
                                            <div class="admin-return-card" data-return-id="${traHang.maTraHang}">
                                                <div class="admin-return-card-top">
                                                    <div class="admin-return-card-meta">
                                                        <span class="admin-return-code">#${traHang.maTraHang}</span>
                                                        <span class="admin-return-user">Đơn gốc: #${traHang.maDonHang}</span>
                                                        <span class="admin-return-date">
                                                            <fmt:formatDate value="${traHang.ngayYeuCau}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                                        </span>
                                                    </div>
                                                    <div class="admin-return-badges">
                                                        <span class="admin-order-badge badge-${traHang.trangThai}">
                                                            ${mapTrangThaiTraHang[traHang.trangThai]}
                                                        </span>
                                                    </div>
                                                </div>

                                                <div class="admin-return-card-body">
                                                    <div class="admin-return-info-box">
                                                        <h3>Lý do</h3>
                                                        <p>${traHang.lyDo}</p>
                                                        <c:if test="${traHang.soTienHoan > 0}">
                                                            <p><strong>Số tiền hoàn:</strong>
                                                                <span class="admin-order-money">
                                                                    <fmt:formatNumber value="${traHang.soTienHoan}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ
                                                                </span>
                                                            </p>
                                                        </c:if>
                                                        <c:if test="${not empty traHang.ghiChu}">
                                                            <p><strong>Ghi chú:</strong> ${traHang.ghiChu}</p>
                                                        </c:if>
                                                    </div>
                                                </div>

                                                <c:if test="${traHang.trangThai == 'cho_xu_ly'}">
                                                    <div class="admin-return-actions">
                                                        <button type="button"
                                                                class="admin-order-btn admin-order-btn-primary btn-duyet-tra-hang"
                                                                data-return-id="${traHang.maTraHang}"
                                                                data-action="duyet_tra_hang">
                                                            Duyệt
                                                        </button>
                                                        <button type="button"
                                                                class="admin-order-btn admin-order-btn-dark btn-tu-choi-tra-hang"
                                                                data-return-id="${traHang.maTraHang}"
                                                                data-action="tu_choi_tra_hang">
                                                            Từ chối
                                                        </button>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <p>Không có yêu cầu trả hàng nào.</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </section>

                    </div>
                </div>
            </main>
        </div>

        <!-- Confirm Modal -->
        <div class="admin-confirm-overlay" id="adminConfirmOverlay">
            <div class="admin-confirm-modal">
                <h3 id="adminConfirmTitle">Xác nhận thao tác</h3>
                <p id="adminConfirmMessage">Bạn có chắc muốn thực hiện thao tác này?</p>
                <div class="admin-confirm-actions">
                    <button type="button" class="admin-confirm-btn cancel" id="adminConfirmCancel">Hủy</button>
                    <button type="button" class="admin-confirm-btn confirm" id="adminConfirmOk">Xác nhận</button>
                </div>
            </div>
        </div>

        <script>
    window.contextPath = "${pageContext.request.contextPath}";
        </script>
        <script src="${pageContext.request.contextPath}/assets/js/admin/admin-dashboard.js"></script>
        <script src="https://unpkg.com/lucide@latest"></script>
        <script>lucide.createIcons();</script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/admin/admin-order-tra-hang.js"></script>
    </body>
</html>
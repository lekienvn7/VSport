<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    request.setAttribute("activePage", "dashboard");
%>

<%@ include file="/WEB-INF/views/admin/common/admin-layout-start.jsp" %>
<%@ include file="/WEB-INF/views/admin/common/admin-sidebar.jsp" %>
<%@ include file="/WEB-INF/views/profile/profile-edit-popup.jsp" %>

<main class="admin-main">
    <%@ include file="/WEB-INF/views/admin/common/admin-header.jsp" %>

    <section class="admin-dashboard-content">

        <section class="admin-stats-grid">
            <div class="admin-stat-card admin-stat-card--highlight">
                <div class="admin-stat-top">
                    <span>Tổng Đơn Hàng</span>
                    <span>↗</span>
                </div>
                <h3><fmt:formatNumber value="${tongDonHang}" type="number" groupingUsed="true" maxFractionDigits="0"/></h3>
                <div class="admin-stat-change ${phanTramDonHang >= 0 ? 'positive' : 'negative'}">
                    <fmt:formatNumber value="${phanTramDonHang}" type="number" minFractionDigits="0" maxFractionDigits="1"/>%
                    <span>So Với Tháng Trước</span>
                </div>
            </div>

            <div class="admin-stat-card">
                <div class="admin-stat-top">
                    <span>Tổng Doanh Thu</span>
                    <span>↗</span>
                </div>
                <h3>
                    <fmt:formatNumber value="${tongDoanhThu}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ
                </h3>
                <div class="admin-stat-change ${phanTramDoanhThu >= 0 ? 'positive' : 'negative'}">
                    <fmt:formatNumber value="${phanTramDoanhThu}" type="number" minFractionDigits="0" maxFractionDigits="1"/>%
                    <span>So Với Tháng Trước</span>
                </div>
            </div>

            <div class="admin-stat-card">
                <div class="admin-stat-top">
                    <span>Tổng Lợi Nhuận</span>
                    <span>↗</span>
                </div>
                <h3>
                    <fmt:formatNumber value="${tongLoiNhuan}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ
                </h3>
                <div class="admin-stat-change ${phanTramLoiNhuan >= 0 ? 'positive' : 'negative'}">
                    <fmt:formatNumber value="${phanTramLoiNhuan}" type="number" minFractionDigits="0" maxFractionDigits="1"/>%
                    <span>So Với Tháng Trước</span>
                </div>
            </div>

            <div class="admin-stat-card">
                <div class="admin-stat-top">
                    <span>Số Thành Viên</span>
                    <span>↗</span>
                </div>
                <h3><fmt:formatNumber value="${tongThanhVien}" type="number" groupingUsed="true" maxFractionDigits="0"/></h3>
                <div class="admin-stat-change ${phanTramThanhVien >= 0 ? 'positive' : 'negative'}">
                    <fmt:formatNumber value="${phanTramThanhVien}" type="number" minFractionDigits="0" maxFractionDigits="1"/>%
                    <span>So Với Tháng Trước</span>
                </div>
            </div>
        </section>

        <!-- phần dưới giữ nguyên -->
        <section class="admin-chart-grid">
            <div class="admin-panel">
                <div class="admin-panel-head">
                    <div>
                        <h3>Total Sales</h3>
                        <p><fmt:formatNumber value="${tongDoanhThu}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ</p>
                    </div>
                    <div class="admin-legend">
                        <span><i class="dot current"></i>Hiện Tại</span>
                        <span><i class="dot last"></i>Tháng Trước</span>
                    </div>
                </div>

                <div class="admin-placeholder-box">
                    <div class="admin-placeholder-title">chart-sales.jsp</div>
                    <div class="admin-placeholder-sub">Khu biểu đồ line chart sẽ include sau</div>
                </div>
            </div>

            <div class="admin-panel">
                <div class="admin-panel-head">
                    <div>
                        <h3>Total Visitors</h3>
                        <p><fmt:formatNumber value="${tongThanhVien}" type="number" groupingUsed="true" maxFractionDigits="0"/></p>
                    </div>
                    <div class="admin-legend">
                        <span><i class="dot current"></i>Hiện Tại</span>
                        <span><i class="dot last"></i>Tuần Trước</span>
                    </div>
                </div>

                <div class="admin-placeholder-box">
                    <div class="admin-placeholder-title">chart-visitors.jsp</div>
                    <div class="admin-placeholder-sub">Khu biểu đồ bar chart sẽ include sau</div>
                </div>
            </div>

            <div class="admin-panel">
                <div class="admin-panel-head">
                    <div>
                        <h3>Earning Growth</h3>
                        <p><fmt:formatNumber value="${tongLoiNhuan}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ</p>
                    </div>
                    <div class="admin-legend">
                        <span><i class="dot current"></i>Hiện Tại</span>
                        <span><i class="dot last"></i>Tuần Trước</span>
                    </div>
                </div>

                <div class="admin-placeholder-box">
                    <div class="admin-placeholder-title">chart-earning.jsp</div>
                    <div class="admin-placeholder-sub">Khu biểu đồ tăng trưởng sẽ include sau</div>
                </div>
            </div>
        </section>

        <section class="admin-bottom-grid">
        <div class="admin-panel">
            <div class="admin-panel-head admin-panel-head--space">
                <h3>Đơn Hàng Gần Đây</h3>
                <a href="${pageContext.request.contextPath}/admin/don-hang" class="admin-panel-link">Xem Chi Tiết</a>
            </div>

            <%@ include file="/WEB-INF/views/admin/components/recent-transaction.jsp" %>
        </div>

        <div class="admin-panel">
            <div class="admin-panel-head admin-panel-head--space">
                <h3>Sản Phẩm Bán Chạy</h3>
                <a href="${pageContext.request.contextPath}/admin/san-pham" class="admin-panel-link">Xem Chi Tiết</a>
            </div>

            <%@ include file="/WEB-INF/views/admin/components/top-selling-product.jsp" %>
        </div>
    </section>

    </section>
</main>

</div>
<script src="${pageContext.request.contextPath}/assets/js/admin/admin-dashboard.js"></script>
<script src="https://unpkg.com/lucide@latest"></script>
<script>
    lucide.createIcons();
</script>
</body>
</html>
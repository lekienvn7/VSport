<%-- 
    Document   : dashboard
    Created on : Apr 8, 2026, 4:14:09 PM
    Author     : ltrgk
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    request.setAttribute("activePage", "dashboard");
%>

<%@ include file="/WEB-INF/views/admin/common/admin-layout-start.jsp" %>
<%@ include file="/WEB-INF/views/admin/common/admin-sidebar.jsp" %>

<%@ include file="/WEB-INF/views/profile/profile-edit-popup.jsp" %>

<main class="admin-main">
    <%@ include file="/WEB-INF/views/admin/common/admin-header.jsp" %>

    <section class="admin-dashboard-content">

        <!-- KPI cards -->
        <section class="admin-stats-grid">
            <div class="admin-stat-card admin-stat-card--highlight">
                <div class="admin-stat-top">
                    <span>Tổng Đơn Hàng</span>
                    <span>↗</span>
                </div>
                <h3>$112,789.90</h3>
                <div class="admin-stat-change positive">+10% <span>From Last Month</span></div>
            </div>

            <div class="admin-stat-card">
                <div class="admin-stat-top">
                    <span>Tổng Doanh Thu</span>
                    <span>↗</span>
                </div>
                <h3>$253,894.88</h3>
                <div class="admin-stat-change positive">+15% <span>From Last Month</span></div>
            </div>

            <div class="admin-stat-card">
                <div class="admin-stat-top">
                    <span>Tổng Lợi Nhuận</span>
                    <span>↗</span>
                </div>
                <h3>$89,992.12</h3>
                <div class="admin-stat-change negative">-12% <span>From Last Month</span></div>
            </div>

            <div class="admin-stat-card">
                <div class="admin-stat-top">
                    <span>Số Thành Viên Mới</span>
                    <span>↗</span>
                </div>
                <h3>$102,442.89</h3>
                <div class="admin-stat-change positive">+5% <span>From Last Month</span></div>
            </div>
        </section>

        <!-- chart row -->
        <section class="admin-chart-grid">
            <div class="admin-panel">
                <div class="admin-panel-head">
                    <div>
                        <h3>Total Sales</h3>
                        <p>$32,848.93</p>
                    </div>
                    <div class="admin-legend">
                        <span><i class="dot current"></i>Current</span>
                        <span><i class="dot last"></i>Last Month</span>
                    </div>
                </div>

                <div class="admin-placeholder-box">
                    <div class="admin-placeholder-title">Component chart-sales.jsp</div>
                    <div class="admin-placeholder-sub">Khu biểu đồ line chart sẽ include sau</div>
                </div>
            </div>

            <div class="admin-panel">
                <div class="admin-panel-head">
                    <div>
                        <h3>Total Visitors</h3>
                        <p>432,943</p>
                    </div>
                    <div class="admin-legend">
                        <span><i class="dot current"></i>Current</span>
                        <span><i class="dot last"></i>Last Month</span>
                    </div>
                </div>

                <div class="admin-placeholder-box">
                    <div class="admin-placeholder-title">Component chart-visitors.jsp</div>
                    <div class="admin-placeholder-sub">Khu biểu đồ bar chart sẽ include sau</div>
                </div>
            </div>

            <div class="admin-panel">
                <div class="admin-panel-head">
                    <div>
                        <h3>Earning Growth</h3>
                        <p>$25,433.89</p>
                    </div>
                    <div class="admin-legend">
                        <span><i class="dot current"></i>Current</span>
                        <span><i class="dot last"></i>Last Week</span>
                    </div>
                </div>

                <div class="admin-placeholder-box">
                    <div class="admin-placeholder-title">Component chart-earning.jsp</div>
                    <div class="admin-placeholder-sub">Khu biểu đồ tăng trưởng sẽ include sau</div>
                </div>
            </div>
        </section>

        <!-- bottom row -->
        <section class="admin-bottom-grid">
            <div class="admin-panel">
                <div class="admin-panel-head admin-panel-head--space">
                    <h3>Recent Transaction</h3>
                    <a href="javascript:void(0)" class="admin-panel-link">See All Transaction</a>
                </div>

                <div class="admin-placeholder-box admin-placeholder-box--tall">
                    <div class="admin-placeholder-title">Component recent-transaction.jsp</div>
                    <div class="admin-placeholder-sub">Danh sách giao dịch gần đây sẽ include sau</div>
                </div>
            </div>

            <div class="admin-panel">
                <div class="admin-panel-head admin-panel-head--space">
                    <h3>Top Selling Product</h3>
                    <a href="javascript:void(0)" class="admin-panel-link">See All Product</a>
                </div>

                <div class="admin-placeholder-box admin-placeholder-box--tall">
                    <div class="admin-placeholder-title">Component top-selling-product.jsp</div>
                    <div class="admin-placeholder-sub">Top sản phẩm bán chạy sẽ include sau</div>
                </div>
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

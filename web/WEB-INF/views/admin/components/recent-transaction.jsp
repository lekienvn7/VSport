<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="admin-dashboard-scroll-box">
    <c:choose>
        <c:when test="${not empty dsDonHangGanDay}">
            <div class="admin-dashboard-simple-list">
                <c:forEach var="dh" items="${dsDonHangGanDay}" begin="0" end="5">
                    <div class="admin-dashboard-order-item">
                        <div class="admin-dashboard-order-top">
                            <div class="admin-dashboard-order-code">
                                #<c:out value="${dh.maDonHang}"/>
                            </div>

                            <div class="admin-dashboard-order-status">
                                <c:out value="${dh.trangThaiDonHang}"/>
                            </div>
                        </div>

                        <div class="admin-dashboard-order-name">
                            <c:out value="${empty dh.tenNguoiDung ? dh.hoTenNguoiNhan : dh.tenNguoiDung}"/>
                        </div>

                        <div class="admin-dashboard-order-meta">
                            <span>
                                <fmt:formatNumber value="${dh.tongThanhToan}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ
                            </span>
                            <span>•</span>
                            <span>
                                <fmt:formatDate value="${dh.ngayDat}" pattern="dd/MM/yyyy HH:mm"/>
                            </span>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>

        <c:otherwise>
            <div class="admin-dashboard-empty">
                Chưa có giao dịch nào.
            </div>
        </c:otherwise>
    </c:choose>
</div>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.ThuongHieu" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String[] thuongHieuDaChon = request.getParameterValues("thuongHieu");
    String[] loaiDaChon = request.getParameterValues("loai");

    boolean isTatCa = (thuongHieuDaChon == null || thuongHieuDaChon.length == 0)
            && (loaiDaChon == null || loaiDaChon.length == 0);

    boolean isGiayNhanTao = false;
    boolean isGiayTuNhien = false;
    boolean isGang = false;

    if (loaiDaChon != null) {
        for (String loai : loaiDaChon) {
            if ("giay-bong-da".equals(loai) || "giay-san-co-nhan-tao".equals(loai)) {
                isGiayNhanTao = true;
            }
            if ("giay-bong-da".equals(loai) || "giay-san-co-tu-nhien".equals(loai)) {
                isGiayTuNhien = true;
            }
            if ("gang-tay-thu-mon".equals(loai) || "gang_tay_thu_mon".equals(loai)) {
                isGang = true;
            }
        }
    }
%>

<section class="club-filter-bar-section">
    <div class="club-container club-filter-row">

        <div class="club-links-area">
            <button class="club-scroll-btn left" id="giayGangScrollLeft" type="button">‹</button>

            <div class="club-links-scroll-wrapper" id="giayGangLinksWrapper">
                <div class="club-links-scroll">

                    <a href="${pageContext.request.contextPath}/giay_gang_bong_da"
                       class="club-filter-link <%= isTatCa ? "active" : "" %>">
                        Tất cả giày / găng bóng đá
                    </a>

                    <a href="${pageContext.request.contextPath}/giay_gang_bong_da?loai=giay-san-co-nhan-tao"
                       class="club-filter-link <%= isGiayNhanTao ? "active" : "" %>">
                        Giày sân cỏ nhân tạo
                    </a>
                        
                        
                     <a href="${pageContext.request.contextPath}/giay_gang_bong_da?loai=giay-san-co-tu-nhien"
                       class="club-filter-link <%= isGiayTuNhien ? "active" : "" %>">
                        Giày sân cỏ tự nhiên
                    </a>
                        
                        

                    <a href="${pageContext.request.contextPath}/giay_gang_bong_da?loai=gang-tay-thu-mon"
                       class="club-filter-link <%= isGang ? "active" : "" %>">
                        Găng tay thủ môn
                    </a>

                    <c:forEach var="th" items="${dsThuongHieuNav}">
                        <a href="${pageContext.request.contextPath}/giay_gang_bong_da?thuongHieu=${th.maThuongHieu}"
                           class="club-filter-link
                           <c:if test='${param.thuongHieu == th.maThuongHieu.toString()}'>active</c:if>">
                            ${th.tenThuongHieu}
                        </a>
                    </c:forEach>

                </div>
            </div>

            <button class="club-scroll-btn right" id="giayGangScrollRight" type="button">›</button>
        </div>

        <button class="club-sort-btn" id="openFilterPopup" type="button">
            Lọc &amp; Sắp xếp
        </button>
    </div>
</section>
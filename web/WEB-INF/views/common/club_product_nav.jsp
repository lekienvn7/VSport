<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<section class="club-filter-bar-section">
    <div class="club-container club-filter-row">

        <div class="club-links-area">
            <button class="club-scroll-btn left" id="clubScrollLeft" type="button">‹</button>

            <div class="club-links-scroll-wrapper" id="clubLinksWrapper">
                <div class="club-links-scroll">
                    <a href="${pageContext.request.contextPath}/bong_da" class="club-filter-link">
                        Tất cả các sản phẩm Bóng Đá
                    </a>

                    <a href="${pageContext.request.contextPath}/bong_da#football_jersey_section" class="club-filter-link">
                        Áo đấu
                    </a>

                    <c:forEach var="doi" items="${danhSachDoiBong}">
                        <a href="${pageContext.request.contextPath}/bong_da/${doi.doiSlug}?nhom=ao-dau-bong-da"
                           class="club-filter-link ${doiBongHienTai != null && doi.doiSlug eq doiBongHienTai.doiSlug ? 'active' : ''}">
                            ${doi.tenDoiBong}
                        </a>
                    </c:forEach>
                </div>
            </div>

            <button class="club-scroll-btn right" id="clubScrollRight" type="button">›</button>
        </div>

        <button class="club-sort-btn" id="openFilterPopup" type="button">
    Lọc &amp; Sắp xếp
</button>
    </div>
</section>
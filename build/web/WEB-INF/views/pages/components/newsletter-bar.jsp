<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.NguoiDung" %>

<%
    NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
%>

<% if (nguoiDung == null) { %>
<div class="newsletter-bar">
    <div class="newsletter-left">
        <img 
            src="${pageContext.request.contextPath}/assets/images/banners/banner-sign-up.jpg" 
            alt="Newsletter Banner"
        />
    </div>

    <div class="newsletter-center">
        <div class="newsletter-main-text">
            ĐĂNG KÝ THÀNH VIÊN <span style="color: var(--color-red)">V</span><span style="color: #D4AF37">$</span>PORT
        </div>
        <div class="newsletter-sub-text">
            VÀ NHẬN <span>10% GIẢM GIÁ</span> ĐƠN HÀNG ĐẦU TIÊN CỦA BẠN!
        </div>
    </div>

    <div class="newsletter-right">
        <a href="#" id="showRegisterFormBanner" class="newsletter-btn">
            ĐĂNG KÝ NGAY!
        </a>
    </div>
</div>
<% } %>
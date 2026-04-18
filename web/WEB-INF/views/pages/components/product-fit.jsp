<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="model.SanPham"%>
<%
    SanPham spFit = (SanPham) request.getAttribute("sp");
%>

<div class="pd-fit-box">
    <div class="pd-fit-item">
        <i data-lucide="shirt" class="pd-fit-icon"></i>
        <div class="pd-fit-content">
            <div class="pd-fit-label">Độ ôm</div>
            <div class="pd-fit-value">THÔNG THƯỜNG</div>
        </div>
    </div>

    <div class="pd-fit-item">
        <i data-lucide="crown" class="pd-fit-icon"></i>
        <div class="pd-fit-content">
            <div class="pd-fit-label">Loại sản phẩm</div>
            <div class="pd-fit-value"><%= spFit.getTenDanhMuc() != null ? spFit.getTenDanhMuc().toUpperCase() : "BÓNG ĐÁ" %></div>
        </div>
    </div>
</div>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="model.SanPham"%>
<%@page import="java.util.List"%>
<%
    SanPham spGallery = (SanPham) request.getAttribute("sp");
    List<String> dsAnh = spGallery.getDsAnh();

    if (dsAnh == null || dsAnh.isEmpty()) {
        dsAnh = new java.util.ArrayList<>();
        dsAnh.add(spGallery.getAnhChinh());
    }

    while (dsAnh.size() < 4) {
        dsAnh.add(dsAnh.get(dsAnh.size() - 1));
    }
%>

<div class="pd-gallery-grid">
    <% for (int i = 0; i < 4; i++) { %>
        <div class="pd-gallery-item">
            <img src="${pageContext.request.contextPath}/<%= dsAnh.get(i) %>"
                 alt="<%= spGallery.getTenSanPham() %> - ảnh <%= i + 1 %>"
                 class="pd-gallery-image">
        </div>
    <% } %>
</div>
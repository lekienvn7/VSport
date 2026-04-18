<%-- 
    Document   : club_jersey
    Created on : Apr 10, 2026, 7:02:52 PM
    Author     : ltrgk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.DoiBong" %>
<%
    DoiBong doiBongHienTai = (DoiBong) request.getAttribute("doiBongHienTai");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%= doiBongHienTai != null ? doiBongHienTai.getTenDoiBong() : "Sản phẩm bóng đá" %> | Vsport</title>
        
        
        <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/images/logos/logo.png">
        
        <jsp:include page="/WEB-INF/views/common/head.jsp" />
        
    </head>
    <body>
        <div class="header-shell">
            <%@ include file="/WEB-INF/views/common/header.jsp" %>
            <%@ include file="/WEB-INF/views/common/navbar.jsp" %>
        </div>
        <jsp:include page="/WEB-INF/views/pages/football-sections/club_products.jsp" />
        <jsp:include page="/WEB-INF/views/pages/components/filter-popup.jsp" />
        <%@ include file="/WEB-INF/views/auth/login-popup.jsp" %>
        
        <script src="https://unpkg.com/lucide@latest"></script>
        <script>
            lucide.createIcons();
        </script>
        
        
        <script>
            window.contextPath = "${pageContext.request.contextPath}";
        </script>
        <script src="${pageContext.request.contextPath}/assets/js/components/header.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/common/scroll-reveal.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/components/filter-popup.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/components/login-popup.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/components/favorite-section.js"></script>
        
        
        <script src="${pageContext.request.contextPath}/assets/js/components/search-popup.js"></script>
    </body>
</html>

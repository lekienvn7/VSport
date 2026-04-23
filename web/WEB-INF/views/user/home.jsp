<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    Boolean loginSuccessFlag = (Boolean) request.getAttribute("loginSuccess");
    Boolean logoutSuccessFlag = (Boolean) request.getAttribute("logoutSuccess");

    if (loginSuccessFlag == null) loginSuccessFlag = false;
    if (logoutSuccessFlag == null) logoutSuccessFlag = false;
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>Trang chủ | VSport</title>
        
        <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/images/logos/logo.png">

        <!-- CSS nền tảng -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base/reset.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base/variables.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base/fonts.css">

        <!-- CSS component -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components/header.css">
        <jsp:include page="/WEB-INF/views/common/head.jsp" />
        
        <script>
        (function () {
            try {
                const theme = localStorage.getItem("theme");
                if (theme === "dark") {
                    document.documentElement.classList.add("dark-mode");
                } else {
                    document.documentElement.classList.remove("dark-mode");
                }
            } catch (e) {}
        })();
        </script>
    </head>

    <body class="home-page"
        data-login-success="<%= loginSuccessFlag %>"
        data-logout-success="<%= logoutSuccessFlag %>"
    >
        
        <!-- HEADER -->
        <div class="header-shell" id="siteHeaderShell">
            <%@ include file="/WEB-INF/views/common/header.jsp" %>
            <%@ include file="/WEB-INF/views/common/navbar.jsp" %>
        </div>
        <%@ include file="/WEB-INF/views/common/banner.jsp" %>
        <%@ include file="/WEB-INF/views/auth/login-popup.jsp" %>
        <%@ include file="/WEB-INF/views/pages/components/favorite-section.jsp" %>
        <%@ include file="/WEB-INF/views/pages/components/best-seller-section.jsp" %>
        <%@ include file="/WEB-INF/views/pages/components/shop-category.jsp" %>
        <%@ include file="/WEB-INF/views/pages/components/newsletter-bar.jsp" %>

        
        <script>
            window.contextPath = "${pageContext.request.contextPath}";
        </script>
        
        <script src="${pageContext.request.contextPath}/assets/js/components/header.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/components/banner.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/components/search-popup.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/components/best-seller-section.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/components/login-popup.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/components/favorite-section.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/components/newsletter-bar.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/common/scroll-reveal.js"></script>

        <script src="https://unpkg.com/lucide@latest"></script>
        <script>
          lucide.createIcons();
        </script>

        
        <%@ include file="/WEB-INF/views/common/footer.jsp" %>
        
    </body>
</html>
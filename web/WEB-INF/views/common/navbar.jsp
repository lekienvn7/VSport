<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
String path = request.getServletPath(); // /bong_da
String lastPart = path.substring(path.lastIndexOf("/") + 1);
%>

<nav class="main-navbar">
    <div class="navbar-container">
        <ul class="navbar-menu">

            <li class="navbar-item has-mega-menu">
                <a href="${pageContext.request.contextPath}/bong_da" class="navbar-link ${activePage == "bong_da"  ? "active" : "" }">
                    BỘ KIT ĐỘI TUYỂN / CLB
                </a>

                <div class="mega-menu">
                    <div class="mega-menu-inner">

                        <div class="mega-column">
                            <a href="${pageContext.request.contextPath}/bong_da#football_jersey_section" class="mega-title">Bộ kit tuyển quốc gia</a>
                            <ul class="mega-submenu">
                                <li><a href="#">Bộ kit đội tuyển Việt Nam</a></li>
                                <li><a href="#">Bộ kit đội tuyển Pháp</a></li>
                                <li><a href="#">Bộ kit đội tuyển Đức</a></li>
                                <li><a href="#">Bộ kit đội tuyển Tây Ban Nha</a></li>
                                <li><a href="#">Bộ kit đội tuyển Nhật Bản</a></li>
                                <li><a href="${pageContext.request.contextPath}/bong_da#football_jersey_section">→ Xem tất cả</a></li>
                            </ul>
                        </div>

                        <div class="mega-column">
                            <a href="${pageContext.request.contextPath}/bong_da#football_jersey_section" class="mega-title">Bộ kit câu lạc bộ </a>
                            <ul class="mega-submenu">
                                <li><a href="${pageContext.request.contextPath}/bong_da/real-madrid">Bộ kit CLB Real Madrid</a></li>
                                <li><a href="${pageContext.request.contextPath}/bong_da/juventus">Bộ kit CLB Juventus</a></li>
                                <li><a href="${pageContext.request.contextPath}/bong_da/liverpool">Bộ kit CLB Liverpool</a></li>
                                <li><a href="${pageContext.request.contextPath}/bong_da/arsenal">Bộ kit CLB Arsenal</a></li>
                                <li><a href="${pageContext.request.contextPath}/bong_da/manchester-united">Bộ kit CLB Manchester United</a></li>
                                <li><a href="${pageContext.request.contextPath}/bong_da#football_jersey_section">→ Xem tất cả</a></li>
                            </ul>
                        </div>

                        
                    </div>
                </div>
            </li>

            <li class="navbar-item has-mega-menu">
                <a href="${pageContext.request.contextPath}/products?category=football" class="navbar-link">
                    GIÀY / GĂNG BÓNG ĐÁ 
                </a>

                <div class="mega-menu">
                    <div class="mega-menu-inner">

                        
  
                    </div>
                </div>
            </li>

            <li class="navbar-item has-mega-menu">
                <a href="${pageContext.request.contextPath}/products?category=football" class="navbar-link">
                    PHỤ KIỆN BÓNG ĐÁ 
                </a>

                <div class="mega-menu">
                    <div class="mega-menu-inner">

                        
                    </div>
                </div>
            </li>

            <li class="navbar-item has-mega-menu">
                <a href="${pageContext.request.contextPath}/products?category=football" class="navbar-link">
                    IN ÁO THEO YÊU CẦU
                </a>

                <div class="mega-menu">
                    <div class="mega-menu-inner">

                       
          
                    </div>
                </div>
            </li>

            <li class="navbar-item has-mega-menu">
                <a href="${pageContext.request.contextPath}/products?category=football" class="navbar-link">
                    ĐẶT SÂN BÓNG
                </a>

                <div class="mega-menu">
                    <div class="mega-menu-inner">

                        
                    </div>
                </div>
            </li>

            
            </li>

        </ul>
    </div>
</nav>
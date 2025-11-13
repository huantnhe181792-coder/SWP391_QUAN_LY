<%-- Document : sider Created on : Sep 22, 2025, 10:05:16 AM Author : Dell --%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css">
  <title>JSP Page</title>
</head>

<body>
<div class="sidebar-main sidebar-menu-one sidebar-expand-md sidebar-color" style="min-height: 100vh;">
  <div class="mobile-sidebar-header d-md-none">
    <div class="header-logo">
      <a href="${pageContext.request.contextPath}/home">
        <img src="${pageContext.request.contextPath}/admin/img/logo1.png" alt="logo">
      </a>
    </div>
  </div>
  <div class="sidebar-menu-content">
    <ul class="nav nav-sidebar-menu sidebar-toggle-view">
      <li class="nav-item sidebar-nav-item">
        <a href="${pageContext.request.contextPath}/account" class="nav-link"><i
                class="flaticon-dashboard"></i><span>Tài khoản</span></a>
      </li>
      <li class="nav-item sidebar-nav-item">
        <a href="${pageContext.request.contextPath}/change-password" class="nav-link"><i
                class="flaticon-classmates"></i><span>Đổi mật khẩu</span></a>
      </li>

      <c:choose>
        <c:when test="${sessionScope.account_club.role == 'admin'}">
          <li class="nav-item sidebar-nav-item">
            <a href="${pageContext.request.contextPath}/manager-account?action=list-account" class="nav-link"><i
                    class="flaticon-multiple-users-silhouette"></i><span>Danh sách tải khoản</span></a>
          </li>
<%--          <li class="nav-item sidebar-nav-item">--%>
<%--            <a href="${pageContext.request.contextPath}/manager-account?action=account-add" class="nav-link"><i--%>
<%--                    class="flaticon-technological"></i><span>Add Account</span></a>--%>
<%--          </li>--%>
        </c:when>
        <c:when test="${sessionScope.account_club.role == 'manager'}">
          <li class="nav-item sidebar-nav-item">
            <a href="${pageContext.request.contextPath}/club?action=list-club" class="nav-link"><i
                    class="flaticon-technological"></i><span>Danh sách CLB</span></a>
          </li>
          <li class="nav-item sidebar-nav-item">
            <a href="${pageContext.request.contextPath}/club?action=list-club" class="nav-link"><i
                    class="flaticon-bed"></i><span>Danh sách yêu cầu tham gia sự kiện</span></a>
          </li>
          <li class="nav-item sidebar-nav-item">
            <a href="${pageContext.request.contextPath}/manageEventByManager?action=view" class="nav-link"><i
                    class="flaticon-bed"></i><span>Danh sách sự kiện</span></a>
          </li>
<%--          <li class="nav-item sidebar-nav-item">--%>
<%--            <a href="${pageContext.request.contextPath}/changePresident?action=viewByManager" class="nav-link"><i--%>
<%--                    class="flaticon-bed"></i><span>Danh sách yêu cầu đổi trưởng CLB</span></a>--%>
<%--          </li>--%>
        </c:when>
        <c:when test="${sessionScope.account_club.role == 'president'}">
          <li class="nav-item sidebar-nav-item">
            <a href="${pageContext.request.contextPath}/managerMember?action=view" class="nav-link"><i
                    class="flaticon-technological"></i><span>Thành viên CLB</span></a>
          </li>
          <li class="nav-item sidebar-nav-item">
            <a href="${pageContext.request.contextPath}/createEvent" class="nav-link"><i class="fa-solid fa-calendar"></i>
              <span>Tạo sự kiện mới </span></a>
          </li>
          <li class="nav-item sidebar-nav-item">
            <a href="${pageContext.request.contextPath}/manageEvent?action=view" class="nav-link"><i class="fa-solid fa-calendar"></i>
              <span>Danh sách sự kiện</span></a>
          </li>
          <li class="nav-item sidebar-nav-item">
            <a href="${pageContext.request.contextPath}/changePresident" class="nav-link"><i class="fa-solid fa-calendar"></i>
              <span>Yêu cầu đổi trưởng CLB</span></a>
          </li>

<%--          <li class="nav-item sidebar-nav-item">--%>
<%--            <a href="${pageContext.request.contextPath}/changePresident?action=view" class="nav-link"><i class="fa-solid fa-calendar"></i>--%>
<%--              <span>Yêu cầu đổi trưởng CLB</span></a>--%>
<%--          </li>--%>
        </c:when>
      </c:choose>

    </ul>
  </div>
</div>
</body>

</html>
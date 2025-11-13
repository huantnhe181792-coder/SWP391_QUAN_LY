<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>AKKHOR | Class Routine</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Favicon -->
    <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/admin/img/favicon.png">
    <!-- Normalize CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/normalize.css">
    <!-- Main CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/main.css">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/bootstrap.min.css">
    <!-- Fontawesome CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/all.min.css">
    <!-- Flaticon CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/fonts/flaticon.css">
    <!-- Animate CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/animate.min.css">
    <!-- Select 2 CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/select2.min.css">
    <!-- Data Table CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/jquery.dataTables.min.css">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/style.css">
    <!-- Modernize js -->
    <script src="${pageContext.request.contextPath}/admin/js/modernizr-3.6.0.min.js"></script>
    <!--IzizToast-->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/izitoast/1.4.0/css/iziToast.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/izitoast/1.4.0/js/iziToast.min.js"></script>
</head>
<body>
<div id="wrapper" class="wrapper bg-ash">
    <!-- Giữ nguyên header và sider như trang gốc -->
    <jsp:include page="../common/header.jsp" />
    <div class="dashboard-page-one">
        <jsp:include page="../common/sider.jsp" />

        <div class="dashboard-content-one">
            <br />

            <!-- Card chứa form tạo sự kiện -->
            <div class="card height-auto">
                <div class="card-body">
                    <div class="heading-layout1">
                        <div class="item-title">
                            <h3>Tạo Sự Kiện Mới</h3>
                        </div>
                    </div>

                    <!-- Form tạo sự kiện -->
                    <form action="${pageContext.request.contextPath}/createEvent?action=create" method="post">
                        <div class="row">
                            <div class="col-md-6 form-group" style="display: block; width: 100%;">
                                <label>Tiêu đề sự kiện <span style="color: red">*</span></label>
                                <input type="text" name="title" class="form-control" placeholder="Nhập tiêu đề"
                                       value="${title}" required>
                                <c:if test="${not empty errors['title']}">
                                <small class="text-danger"><c:out value="${errors['title']}" /></small>
                                </c:if>
                            </div>

                            <div class="col-md-6 form-group">
                                <label>Ngày bắt đầu <span style="color: red">*</span></label>
                                <input type="datetime-local" name="start_at" class="form-control"
                                       value="${start_at}" required>
                                <c:if test="${not empty errors['start_at']}">
                                    <small class="text-danger"><c:out value="${errors['start_at']}" /></small>
                                </c:if>
                            </div>

                            <div class="col-md-6 form-group">
                                <label>Ngày kết thúc</label>
                                <input type="datetime-local" name="end_at" class="form-control"
                                       value="${end_at}">
                                <c:if test="${not empty errors['end_at']}">
                                    <small class="text-danger"><c:out value="${errors['end_at']}" /></small>
                                </c:if>
                            </div>

                            <div class="col-md-6 form-group">
                                <label>Địa điểm <span style="color: red">*</span></label>
                                <select name="area" class="form-control" required>
                                    <option value="">-- Chọn địa điểm --</option>
                                    <c:forEach var="a" items="${listArea}">
                                        <option value="${a.id}"
                                                <c:if test="${area == a.id}">selected</c:if>>
                                                ${a.name}
                                        </option>
                                    </c:forEach>
                                </select>
                                <c:if test="${not empty errors['area']}">
                                    <small class="text-danger"><c:out value="${errors['area']}" /></small>
                                </c:if>
                            </div>

                            <div class="col-12 form-group">
                                <label>Mô tả chi tiết</label>
                                <textarea name="description" rows="6" class="form-control"
                                          placeholder="Mô tả nội dung sự kiện, lịch trình, người liên hệ..."
                                          style="height:150px" required>${description}</textarea>
                                <c:if test="${not empty errors['description']}">
                                    <small class="text-danger"><c:out value="${errors['description']}" /></small>
                                </c:if>
                            </div>

                            <div class="col-md-6 form-group">
                                <label>Người tạo</label>
                                <input type="text" name="created_by" class="form-control"
                                       value="${sessionScope.account != null ? sessionScope.account.fullname : ''}" readonly>
                            </div>

                            <div class="col-md-12 form-group">
                                <c:if test="${not empty success}">
                                    <div class="alert alert-success">
                                        <ul>
                                            <li><c:out value="${success}"/></li>
                                        </ul>
                                    </div>
                                </c:if>
                            </div>

                            <div class="col-12 form-group text-right">
                                <button type="submit" class="btn btn-primary"
                                        style="width:140px; height:40px;text-align: center; align-content: center; font-size: 15px; margin: 10px 50px 0px 0px"
                                >Tạo sự kiện</button>
                            </div>
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>
</div>

<!-- Loading overlay shown during submit -->
<div id="loadingOverlay" style="display:none; position:fixed; inset:0; background:rgba(255,255,255,0.6); z-index:9999; align-items:center; justify-content:center;">
    <div style="display:flex; align-items:center;">
        <div class="spinner-border text-primary" role="status" style="width:3rem; height:3rem;">
            <span class="sr-only">Đang xử lý...</span>
        </div>
        <div style="margin-left:12px; font-weight:600; color:#0d6efd;">Đang tạo sự kiện...</div>
    </div>
    
</div>

<!-- JS cuối trang -->
<script src="${pageContext.request.contextPath}/admin/js/jquery-3.3.1.min.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/popper.min.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/izitoast/1.4.0/js/iziToast.min.js"></script>


<%-- client-side blocked event validation removed; validation handled on server --%>

<script>
    // Show loading overlay and disable submit button on form submit
    (function() {
        const form = document.querySelector('form[action*="/createEvent"]');
        if (!form) return;
        const submitBtn = form.querySelector('button[type="submit"]');
        const overlay = document.getElementById('loadingOverlay');
        form.addEventListener('submit', function() {
            if (submitBtn) {
                submitBtn.disabled = true;
                submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm mr-2" role="status" aria-hidden="true"></span>Đang tạo...';
            }
            if (overlay) {
                overlay.style.display = 'flex';
            }
        });
    })();
</script>
</body>
</html>

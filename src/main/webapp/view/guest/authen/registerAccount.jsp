<%--
  Created by IntelliJ IDEA.
  User: vankh
  Date: 9/24/2025
  Time: 4:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Ký</title>

    <!-- Font Awesome -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <!-- CSS -->
    <style>
        * {
            margin: 0; padding: 0; box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        body {
            display: flex; justify-content: center; align-items: center;
            min-height: 100vh;
            background: radial-gradient(1200px 600px at 10% 10%, rgba(248,87,25,0.10), transparent 60%),
                        radial-gradient(1000px 500px at 90% 90%, rgba(248,87,25,0.08), transparent 60%),
                        linear-gradient(135deg, #e3f2fd 0%, #e0f7fa 100%);
            overflow: hidden; position: relative; padding: 20px;
        }

        /* Nút Trang chủ */
        .home-button {
            position: absolute; top: 20px; left: 20px;
            background: #F85719; color: white;
            padding: 10px 16px; border-radius: 28px;
            text-decoration: none; display: flex; align-items: center; gap: 8px;
            box-shadow: 0 4px 10px rgba(248, 87, 25, 0.3);
            transition: all 0.3s ease; z-index: 100;
        }
        .home-button:hover { background: #E04F17; transform: translateY(-2px); }

        /* Card container */
        .container {
            position: relative; width: 960px; min-height: 720px;
            background: #ffffff;
            border-radius: 20px;
            box-shadow: 0 20px 50px rgba(0, 0, 0, 0.12);
            overflow: hidden;
            display: grid; grid-template-columns: 1fr 1fr;
        }

        .brand-side {
            background: linear-gradient(135deg, #F85719 0%, #E04F17 100%);
            color: #fff; padding: 40px; display: flex; flex-direction: column; justify-content: center; align-items: center;
            position: relative;
        }
        .brand-side::after {
            content: ""; position: absolute; inset: 0;
            background: radial-gradient(800px 400px at 120% -10%, rgba(255,255,255,0.15), transparent 60%),
                        radial-gradient(600px 300px at -20% 120%, rgba(255,255,255,0.08), transparent 60%);
            pointer-events: none;
        }
        .brand-side h3 { font-size: 22px; font-weight: 700; text-align: center; line-height: 1.4; margin-bottom: 10px; }
        .brand-side p { font-size: 14px; opacity: 0.9; text-align: center; }

        .form-side { padding: 36px; display: flex; justify-content: center; align-items: center; }
        .form-wrapper { width: 100%; max-width: 420px; }

        h2 { color: #E04F17; font-size: 28px; margin-bottom: 22px; text-align: center; }

        /* Inputs with icons */
        .input-group { position: relative; width: 100%; margin-bottom: 16px; }
        .input-group i { position: absolute; left: 14px; top: 50%; transform: translateY(-50%); color: #F85719; font-size: 16px; }
        .input-group input,
        .input-group select {
            width: 100%; padding: 12px 44px; border-radius: 12px;
            border: 1px solid #e3e6f0; outline: none;
            font-size: 15px; background: #fff;
            transition: all 0.25s ease;
            box-shadow: 0 4px 12px rgba(248, 87, 25, 0.10);
            appearance: none;
        }
        .input-group input:focus, .input-group select:focus { border-color: #F85719; box-shadow: 0 0 0 4px rgba(248, 87, 25, 0.15); }

        .toggle-password {
            position: absolute; right: 10px; top: 50%; transform: translateY(-50%);
            border: none; background: transparent; color: #F85719; cursor: pointer; padding: 6px; border-radius: 8px;
        }
        .toggle-password:hover { color: #E04F17; background: rgba(248,87,25,0.10); }

        .remember-forgot { display: flex; justify-content: center; align-items: center; gap: 12px; margin: 10px 0 18px; font-size: 14px; color: #666; }
        .remember-forgot a { color: #F85719; text-decoration: none; transition: 0.25s; }
        .remember-forgot a:hover { color: #E04F17; text-decoration: underline; }

        .btn {
            width: 100%; padding: 14px; border: 0; border-radius: 12px;
            background: linear-gradient(135deg, #F85719 0%, #E04F17 100%);
            color: #fff; font-size: 16px; font-weight: 700; letter-spacing: 0.2px;
            cursor: pointer; transition: transform 0.08s ease, box-shadow 0.25s ease;
            box-shadow: 0 10px 20px rgba(248, 87, 25, 0.25);
        }
        .btn:hover { transform: translateY(-2px); box-shadow: 0 14px 28px rgba(248, 87, 25, 0.35); }
        .btn:active { transform: translateY(0); box-shadow: 0 8px 16px rgba(248, 87, 25, 0.25); }

        .error-alert { margin-top: 16px; background: #ffebee; color: #c62828; border: 1px solid #ffcdd2; padding: 10px 12px; border-radius: 10px; font-size: 14px; }
        .error-alert:empty { display: none; }

        @media (max-width: 960px) {
            .container { width: 100%; grid-template-columns: 1fr; min-height: unset; }
            .brand-side { padding: 32px 28px; }
            .form-side { padding: 28px; }
        }
        @media (max-width: 480px) {
            .home-button { top: 10px; left: 10px; padding: 8px 12px; font-size: 14px; }
            .form-wrapper { max-width: 100%; }
        }
    </style>
</head>
<body>

<!-- Nút Trang chủ -->
<a href="${pageContext.request.contextPath}/view/guest/homePage.jsp" class="home-button">
    <i class="fas fa-home"></i> Trang Chủ
</a>

<!-- Container -->
<div class="container" id="container">
    <div class="brand-side">
        <h3>Tham gia cộng đồng FPTU Club</h3>
        <p>Tạo tài khoản để kết nối, đăng ký CLB và cập nhật sự kiện.</p>
    </div>
    <div class="form-side">
        <div class="form-wrapper">
            <h2>Đăng Ký</h2>
            <form id="registerForm" method="post" action="${pageContext.request.contextPath}/registerAccount">
                <div class="input-group">
                    <i class="far fa-envelope" aria-hidden="true"></i>
                    <input type="email" placeholder="Email" name="email" autocomplete="email" required>
                </div>
                <div class="input-group">
                    <i class="far fa-id-card" aria-hidden="true"></i>
                    <input type="text" placeholder="Họ và tên" name="fullname" autocomplete="name" required>
                </div>
                <div class="input-group">
                    <i class="fas fa-location-dot" aria-hidden="true"></i>
                    <input type="text" placeholder="Tỉnh/Thành phố" name="address" autocomplete="address-level1" required>
                </div>
                <div class="input-group">
                    <i class="fas fa-phone" aria-hidden="true"></i>
                    <input type="tel" placeholder="Số điện thoại" name="phone" autocomplete="tel" required>
                </div>
                <div class="input-group">
                    <i class="fas fa-id-badge" aria-hidden="true"></i>
                    <input type="text" placeholder="Mã sinh viên" name="studentId" required>
                </div>
                <div class="input-group">
                    <i class="fas fa-venus-mars" aria-hidden="true"></i>
                    <select name="gender" required>
                        <option value="" disabled selected>Giới tính</option>
                        <option value="male">Nam</option>
                        <option value="female">Nữ</option>
                        <option value="other">Khác</option>
                    </select>
                </div>
                <div class="input-group">
                    <i class="fas fa-calendar" aria-hidden="true"></i>
                    <input type="date" name="dob" required>
                </div>
                <div class="input-group">
                    <i class="fas fa-lock" aria-hidden="true"></i>
                    <input id="password" type="password" placeholder="Mật khẩu" name="password" autocomplete="new-password" required>
                    <button type="button" class="toggle-password" data-target="password" aria-label="Hiển thị/Ẩn mật khẩu"><i class="far fa-eye"></i></button>
                </div>
                <div class="input-group">
                    <i class="fas fa-lock" aria-hidden="true"></i>
                    <input id="confirmPassword" type="password" placeholder="Xác nhận mật khẩu" name="confirmPassword" autocomplete="new-password" required>
                    <button type="button" class="toggle-password" data-target="confirmPassword" aria-label="Hiển thị/Ẩn mật khẩu"><i class="far fa-eye"></i></button>
                </div>

                <div class="remember-forgot">
                    <a href="${pageContext.request.contextPath}/view/guest/authen/login.jsp">Đã có tài khoản?</a>
                    <span style="color:#bdbdbd">|</span>
                    <a href="${pageContext.request.contextPath}/view/guest/authen/forgotPassword.jsp">Quên mật khẩu?</a>
                </div>
                <button type="submit" class="btn">Đăng Ký</button>
            </form>
            <div class="error-alert">${error}</div>
        </div>
    </div>
</div>
</body>
<script>
    (function() {
        var toggles = document.querySelectorAll('.toggle-password');
        toggles.forEach(function(btn) {
            btn.addEventListener('click', function() {
                var targetId = this.getAttribute('data-target');
                var input = document.getElementById(targetId);
                if (!input) return;
                var isHidden = input.getAttribute('type') === 'password';
                input.setAttribute('type', isHidden ? 'text' : 'password');
                var icon = this.querySelector('i');
                if (icon) { icon.className = isHidden ? 'far fa-eye-slash' : 'far fa-eye'; }
            });
        });
    })();
</script>
</html>


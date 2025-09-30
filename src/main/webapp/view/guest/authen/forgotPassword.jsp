<%--
  Created by IntelliJ IDEA.
  User: vankh
  Date: 9/23/2025
  Time: 9:59 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Quên Mật Khẩu</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <style>
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    }

    body {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      background: radial-gradient(1200px 600px at 10% 10%, rgba(248,87,25,0.10), transparent 60%),
                  radial-gradient(1000px 500px at 90% 90%, rgba(248,87,25,0.08), transparent 60%),
                  linear-gradient(135deg, #e3f2fd 0%, #e0f7fa 100%);
      overflow: hidden;
      position: relative;
      padding: 20px;
    }

    .home-button {
      position: absolute;
      top: 20px;
      left: 20px;
      background: #F85719;
      color: white;
      padding: 10px 16px;
      border-radius: 28px;
      text-decoration: none;
      display: flex;
      align-items: center;
      gap: 8px;
      box-shadow: 0 4px 10px rgba(248, 87, 25, 0.3);
      transition: all 0.3s ease;
      z-index: 100;
    }

    .home-button:hover {
      background: #E04F17;
      transform: translateY(-2px);
      box-shadow: 0 6px 15px rgba(248, 87, 25, 0.35);
    }

    .home-button:active {
      transform: translateY(0);
    }

    .container {
      position: relative;
      width: 920px;
      min-height: 480px;
      background: #ffffff;
      border-radius: 20px;
      box-shadow: 0 20px 50px rgba(0, 0, 0, 0.12);
      overflow: hidden;
      display: grid;
      grid-template-columns: 1fr 1fr;
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
    .brand-side p { font-size: 14px; opacity: 0.95; text-align: center; }

    .form-side { padding: 40px; display: flex; justify-content: center; align-items: center; }
    .form-wrapper { width: 100%; max-width: 380px; }

    h2 { color: #E04F17; font-size: 28px; margin-bottom: 26px; text-align: center; }

    .input-group {
      position: relative;
      width: 100%;
      margin-bottom: 20px;
    }

    .input-group input {
      width: 100%;
      padding: 14px 44px 14px 44px;
      border: 1px solid #e3e6f0;
      outline: none;
      background: #fff;
      border-radius: 12px;
      color: #333;
      font-size: 15px;
      transition: all 0.3s ease;
      box-shadow: 0 4px 12px rgba(248, 87, 25, 0.10);
    }

    .input-group input:focus {
      border-color: #F85719;
      box-shadow: 0 0 0 4px rgba(248, 87, 25, 0.15);
    }

    .input-group input::placeholder {
      color: #aaa;
    }

    .input-group i {
      position: absolute; left: 14px; top: 50%; transform: translateY(-50%);
      color: #F85719; font-size: 16px; transition: all 0.3s ease;
    }

    .input-group input:focus + i { color: #E04F17; }

    .remember-forgot { display: flex; justify-content: center; align-items: center; gap: 12px; margin: 10px 0 0; font-size: 14px; color: #666; }
    .remember-forgot a { color: #F85719; text-decoration: none; transition: 0.25s; }
    .remember-forgot a:hover { color: #E04F17; text-decoration: underline; }

    .btn {
      width: 100%;
      padding: 14px;
      border: 0;
      border-radius: 12px;
      background: linear-gradient(135deg, #F85719 0%, #E04F17 100%);
      color: white;
      font-size: 16px;
      font-weight: 700;
      cursor: pointer;
      transition: transform 0.08s ease, box-shadow 0.25s ease;
      box-shadow: 0 10px 20px rgba(248, 87, 25, 0.25);
    }

    .btn:hover {
      transform: translateY(-2px);
      box-shadow: 0 14px 28px rgba(248, 87, 25, 0.35);
    }

    .btn:active {
      transform: translateY(0);
      box-shadow: 0 8px 16px rgba(248, 87, 25, 0.25);
    }

    .error-alert { margin-top: 16px; background: #ffebee; color: #c62828; border: 1px solid #ffcdd2; padding: 10px 12px; border-radius: 10px; font-size: 14px; }
    .error-alert:empty { display: none; }

    @media (max-width: 960px) {
      .container { width: 100%; grid-template-columns: 1fr; min-height: unset; }
      .brand-side { padding: 32px 28px; }
      .form-side { padding: 28px; }
    }

    @media (max-width: 480px) {
      .container {
        width: 90%;
        height: auto;
        padding: 20px 0;
      }

      .home-button {
        top: 10px;
        left: 10px;
        padding: 10px 15px;
        font-size: 14px;
      }
    }
  </style>
</head>
<body>
<a href="${pageContext.request.contextPath}/view/guest/homePage.jsp" class="home-button">
  <i class="fas fa-home"></i> Trang Chủ
</a>
<div class="container" id="container">
  <div class="brand-side">
    <h3>Khôi phục mật khẩu</h3>
    <p>Nhập email của bạn để nhận liên kết đặt lại mật khẩu.</p>
  </div>
  <div class="form-side">
    <div class="form-wrapper">
      <h2>Quên Mật Khẩu</h2>
      <form id="forgotPasswordForm" method="post" action="${pageContext.request.contextPath}/forgotPass">
        <div class="input-group">
          <i class="far fa-envelope" aria-hidden="true"></i>
          <input type="email" placeholder="Nhập email của bạn" name="email" autocomplete="email" required>
        </div>
        <button type="submit" class="btn">Gửi yêu cầu</button>
      </form>
      <div class="remember-forgot">
        <a href="${pageContext.request.contextPath}/view/guest/authen/login.jsp" id="backToLogin">Quay lại đăng nhập</a>
      </div>
      <div class="error-alert">${notifi}</div>
    </div>
  </div>
</div>

</body>



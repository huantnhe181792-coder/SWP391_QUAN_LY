package com.mycompany.swp391.controller.authen;

import com.mycompany.swp391.dal.implement.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/confirm")
public class ConfirmAccountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        if (token == null || token.trim().isEmpty()) {
            req.setAttribute("error", "Liên kết xác nhận không hợp lệ.");
            req.getRequestDispatcher("view/guest/authen/login.jsp").forward(req, resp);
            return;
        }

        AccountDAO dao = new AccountDAO();
        boolean ok = dao.confirmByToken(token);
        if (ok) {
            req.setAttribute("success", "Tài khoản đã được xác nhận thành công. Bạn có thể đăng nhập.");
        } else {
            req.setAttribute("error", "Mã xác nhận không hợp lệ hoặc đã được sử dụng.");
        }
        req.getRequestDispatcher("view/guest/authen/login.jsp").forward(req, resp);
    }
}



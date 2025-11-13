package com.mycompany.swp391.controller.member;


import com.mycompany.swp391.config.GlobalConfig;
import com.mycompany.swp391.dal.implement.AccountClubDAO;
import com.mycompany.swp391.entity.AccountClub;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/outClub")
public class OutClubServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        AccountClub accountClub = (session != null) ? (AccountClub) session.getAttribute(GlobalConfig.SESSION_ACCOUNT_CLUB) : null;
        AccountClubDAO accountClubDAO = new AccountClubDAO();
        Integer accountId = (accountClub != null) ? accountClub.getAccount_id() : null;
        Integer clubId = (accountClub != null) ? accountClub.getClub_id() : null;
        if(accountClubDAO.delete(accountClubDAO.findByAccountIdAndClubId(accountId, clubId))){
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}

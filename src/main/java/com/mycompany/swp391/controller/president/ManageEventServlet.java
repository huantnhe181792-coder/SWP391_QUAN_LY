package com.mycompany.swp391.controller.president;



import com.mycompany.swp391.config.GlobalConfig;
import com.mycompany.swp391.dal.implement.AccountEventDAO;
import com.mycompany.swp391.dal.implement.AreaDAO;
import com.mycompany.swp391.dal.implement.EventDAO;
import com.mycompany.swp391.dal.implement.TaskDAO;
import com.mycompany.swp391.entity.AccountClub;
import com.mycompany.swp391.entity.Area;
import com.mycompany.swp391.entity.Event;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/manageEvent")
public class ManageEventServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action){
            case "view":
                viewList(request,response);
                break;
            case "delete":
                deleteEvent(request,response);
                break;
        }
    }

    protected void viewList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Session / accountClub null-safe
        HttpSession session = request.getSession(false);
        AccountClub accountClub = (session != null) ? (AccountClub) session.getAttribute(GlobalConfig.SESSION_ACCOUNT_CLUB) : null;

        Integer clubId = (accountClub != null) ? accountClub.getClub_id() : null;
        EventDAO eventDAO = new EventDAO();
        AreaDAO areaDAO = new AreaDAO();
        Map<Integer, String> areaMap = new HashMap<>();
        List<Event> listEvent = new ArrayList<>();
        List<Area> listArea = areaDAO.findAll();
        if ("president".equals(accountClub.getRole()) || "head".equals(accountClub.getRole())) {
            listEvent = eventDAO.findByClubId(clubId);
            for (Area area : listArea) {
                areaMap.put(Integer.parseInt(area.getId()), area.getName());
            }
        }

        request.setAttribute("listEvent", listEvent);
        request.setAttribute("listArea", listArea);
        request.setAttribute("areaMap", areaMap); // Thêm areaMap vào request
        request.getRequestDispatcher("view/admin/president/listEvent.jsp").forward(request, response);
    }

    protected void deleteEvent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        boolean rl1 = new EventDAO().deleteEventByEventId(Integer.parseInt(request.getParameter("eventId")));
        boolean rl2 = new TaskDAO().deleteTaskByEventId(Integer.parseInt(request.getParameter("eventId")));
        boolean rl3 = new AccountEventDAO().deleteAccountEventByEventId(Integer.parseInt(request.getParameter("eventId")));
        if (rl1&&rl2&&rl3){
            request.setAttribute("deleteSuccess","Xóa sự kiện thành công!");
            viewList(request,response);
        }

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

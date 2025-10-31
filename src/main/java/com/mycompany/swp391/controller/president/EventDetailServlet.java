package com.mycompany.swp391.controller.president;


import com.mycompany.swp391.config.GlobalConfig;
import com.mycompany.swp391.dal.implement.*;
import com.mycompany.swp391.entity.Account;
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
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/eventDetail")
public class EventDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "viewDetail":
                viewDetail(req, resp);
                break;
            case "deleteTask":
                deleteTask(req, resp);
                break;
            default:
                req.getRequestDispatcher("view/admin/president/eventDetail.jsp").forward(req, resp);
                break;
        }

    }
    
    protected void deleteTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer taskId = Integer.parseInt(req.getParameter("taskId"));
        boolean success = new TaskDAO().deleteTaskById(taskId);
        Account account = new AccountDAO().findById(Integer.parseInt(req.getParameter("accountId")));
        if (success){
            req.setAttribute("deleteTaskSuccess", "Xóa nhiệm vụ "+account.getFullname()+" thành công!");
            viewDetail(req, resp);
        }
    }

    protected void viewDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Session / accountClub null-safe
        HttpSession session = req.getSession(false);
        AccountClub accountClub = (session != null) ? (AccountClub) session.getAttribute(GlobalConfig.SESSION_ACCOUNT_CLUB) : null;

        Integer clubId = (accountClub != null) ? accountClub.getClub_id() : null;
        System.out.println("clubId: " + clubId);
        Integer presidentId = (accountClub != null) ? accountClub.getAccount_id() : null;

        if (clubId == null || presidentId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Integer eventId = Integer.parseInt(req.getParameter("eventId"));
        EventDAO eventDAO = new EventDAO();
        AreaDAO areaDAO = new AreaDAO();

        // Lấy thông tin event và area
        Event event = eventDAO.findById(eventId);
        Area area = areaDAO.findById(event.getArea_id());

        Map<Integer, String> areaMap = new HashMap<>();
        areaMap.put(Integer.valueOf(area.getId()), area.getName());

        // Lấy danh sách thành viên CLB
        List<Account> listAccount = new ArrayList<>();
        Map<Integer, String> accountRoles = new HashMap<>(); // Map lưu role của từng account
        Map<Integer, String> accountNames = new HashMap<>(); // Map lưu tên của từng account
        AccountClubDAO accountClubDAO = new AccountClubDAO();
        AccountDAO accountDAO = new AccountDAO();

        List<AccountClub> accountClubs = accountClubDAO.findByClubId(clubId);

        if (accountClubs != null) {
            for (AccountClub ac : accountClubs) {
                Account acc = accountDAO.findById(ac.getAccount_id());
                if (acc != null) {
                    listAccount.add(acc);
                    accountRoles.put(acc.getId(), ac.getRole()); // Lưu role vào map
                    accountNames.put(acc.getId(), acc.getFullname()); // Lưu tên vào map
                }
            }
        }

        TaskDAO taskDAO = new TaskDAO();
        List<Map<String, Object>> tasks = taskDAO.findByEventId(eventId);

        for (Map<String, Object> task : tasks) {
            Integer accountId = (Integer) task.get("account_id");
            String accountName = accountNames.get(accountId);
            task.put("account_name", accountName != null ? accountName : "Unknown");
        }

        List<Area> listArea = areaDAO.findAll();
        req.setAttribute("listArea", listArea);

        // client-side blocked events are no longer used; validation is server-side

        req.setAttribute("event", event);
        req.setAttribute("areaMap", areaMap);
        req.setAttribute("listAccount", listAccount);
        req.setAttribute("accountRoles", accountRoles);
        req.setAttribute("tasks", tasks);
        req.getRequestDispatcher("view/admin/president/eventDetail.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "addParticipant":
                addTask(req, resp);
                break;
            case "updateEvent":
                updateEvent(req, resp);
                break;
            case "updateTask":
                updateTask(req, resp);
                break;
            default:
                break;
        }
    }

    protected void addTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer eventId = Integer.parseInt(req.getParameter("eventId"));
        Integer accountId = Integer.parseInt(req.getParameter("accountId"));
        String taskName = req.getParameter("taskName");
        String taskDescription = req.getParameter("taskDescription");

        TaskDAO taskDAO = new TaskDAO();
        boolean success = taskDAO.insertTaskDirect(taskName, eventId, taskDescription, accountId);
        if (success) {
            System.out.println("Task added successfully");
            viewDetail(req, resp);
        }
    }

    protected void updateEvent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer eventId = Integer.parseInt(req.getParameter("eventId"));
        String startDateStr = req.getParameter("start");
        String endDateStr = req.getParameter("end");
        Integer area = Integer.parseInt(req.getParameter("area"));
        String title = req.getParameter("title");
        String description = req.getParameter("description");

        // Parse datetime (supports datetime-local and ISO)
        LocalDateTime startLdt = tryParseDateTime(startDateStr);
        LocalDateTime endLdt = tryParseDateTime(endDateStr);
        if (startLdt == null || endLdt == null) {
            String error = "Định dạng thời gian không hợp lệ";
            req.setAttribute("error", error);
            viewDetail(req, resp);
            return;
        }
        Timestamp startTs = Timestamp.valueOf(startLdt);
        Timestamp endTs = Timestamp.valueOf(endLdt);

        EventDAO eventDAO = new EventDAO();
        Event event = eventDAO.findById(eventId);
        Timestamp startDateOld = event.getStart();
        Timestamp endDateOld = event.getEnd();

        String error = null;

        // Lấy thời gian hiện tại
        LocalDateTime now = LocalDateTime.now();

        // Validate 1: Ngày bắt đầu mới không được sớm hơn ngày cũ
        if (startLdt.isBefore(startDateOld.toLocalDateTime())) {
            error = "Ngày bắt đầu mới phải muộn hơn hoặc bằng ngày bắt đầu hiện tại";
            req.setAttribute("error", error);
            viewDetail(req, resp);
            return;
        }

        // Validate 2: Ngày bắt đầu không được sớm hơn ngày hiện tại
        if (startLdt.isBefore(now)) {
            error = "Ngày bắt đầu không được ở trong quá khứ";
            req.setAttribute("error", error);
            viewDetail(req, resp);
            return;
        }

        // Validate 3: Ngày bắt đầu phải sớm hơn ngày kết thúc
        if (!endLdt.isAfter(startLdt)) {
            error = "Ngày bắt đầu phải sớm hơn ngày kết thúc";
            req.setAttribute("error", error);
            viewDetail(req, resp);
            return;
        }

        // Validate 4: Ngày kết thúc không được sớm hơn ngày kết thúc cũ
        if (endLdt.isBefore(endDateOld.toLocalDateTime())) {
            error = "Ngày kết thúc mới phải muộn hơn hoặc bằng ngày kết thúc hiện tại";
            req.setAttribute("error", error);
            viewDetail(req, resp);
            return;
        }

        // Validate 5: Kiểm tra khoảng cách tối thiểu giữa start và end (ít nhất 1 ngày)
        long hours = Duration.between(startLdt, endLdt).toHours();
        if (hours < 24) {
            error = "Sự kiện phải kéo dài ít nhất 1 ngày";
            req.setAttribute("error", error);
            viewDetail(req, resp);
            return;
        }

        // Validate 6: Kiểm tra trùng lịch với sự kiện khác trong cùng địa điểm
        if (eventDAO.hasOverlappingEvents(area, startTs, endTs, eventId)) {
            error = "Địa điểm này đã có sự kiện khác trong khoảng thời gian này";
            req.setAttribute("error", error);
            viewDetail(req, resp);
            return;
        }

        // Cập nhật thông tin
        event.setTitle(title);
        event.setArea_id(area);
        event.setStart(startTs);
        event.setEnd(endTs);
        event.setDescription(description);

        if (eventDAO.update(event)) {
            req.setAttribute("success", "Cập nhật thành công!");
            viewDetail(req, resp);
        } else {
            error = "Cập nhật thất bại!";
            req.setAttribute("error", error);
            viewDetail(req, resp);
        }
    }

    private static LocalDateTime tryParseDateTime(String raw) {
        if (raw == null) return null;
        String s = raw.trim();
        if (s.isEmpty()) return null;

        DateTimeFormatter[] fmts = new DateTimeFormatter[] {
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
        };

        for (DateTimeFormatter f : fmts) {
            try {
                return LocalDateTime.parse(s, f);
            } catch (DateTimeParseException ignored) {}
        }
        try {
            return LocalDateTime.parse(s);
        } catch (DateTimeParseException ignored) {}
        return null;
    }

    protected void updateTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer eventId = Integer.parseInt(req.getParameter("eventId"));
        Integer taskId = Integer.parseInt(req.getParameter("taskId"));
        String taskName = req.getParameter("taskName");
        String taskDescription = req.getParameter("taskDescription");
        TaskDAO taskDAO = new TaskDAO();
        taskDAO.updateTaskWithEventCheck(taskId, eventId, taskName, taskDescription);
        viewDetail(req, resp);
    }

}

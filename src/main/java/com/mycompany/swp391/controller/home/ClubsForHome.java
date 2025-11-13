package com.mycompany.swp391.controller.home;

import com.mycompany.swp391.config.GlobalConfig;
import com.mycompany.swp391.dal.implement.*;
import com.mycompany.swp391.entity.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/clubsForHome")
public class ClubsForHome extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "viewClubs":
                viewClubs(request, response);
                break;
            case "viewDetail":
                viewDetail(request, response);
                break;
            case "joinClub":
                joinclub(request, response);
                break;
            case "joinEvent":
                joinEvent(request, response);
                break;
            default:
                break;
        }
    }

    protected void joinEvent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        AccountClub accountClub = (session != null) ? (AccountClub) session.getAttribute(GlobalConfig.SESSION_ACCOUNT_CLUB) : null;
        if (accountClub == null) {
            request.getRequestDispatcher("view/guest/authen/login.jsp").forward(request, response);
        } else {
            RequestJoinEventDAO  requestJoinEventDAO = new RequestJoinEventDAO();
            Integer eventId = Integer.parseInt(request.getParameter("eventId"));
            System.out.println(eventId);
            System.out.println(accountClub.getAccount_id());
            Integer accountId = accountClub.getAccount_id();

            // Server-side validations
            EventDAO eventDAO = new EventDAO();
            Event targetEvent = eventDAO.findById(eventId);
            if (targetEvent == null) {
                request.setAttribute("success", false);
                request.setAttribute("message", "Sự kiện không tồn tại.");
                viewDetail(request, response);
                return;
            }

            // Already requested/approved for this event?
            RequestJoinEvent existing = requestJoinEventDAO.findByAccountAndEvent(accountId, eventId);
            if (existing != null) {
                if ("pending".equalsIgnoreCase(existing.getStatus())) {
                    request.setAttribute("success", false);
                    request.setAttribute("message", "Bạn đã gửi yêu cầu tham gia sự kiện này.");
                } else if ("active".equalsIgnoreCase(existing.getStatus())) {
                    request.setAttribute("success", true);
                    request.setAttribute("message", "Bạn sẽ tham gia sự kiện này.");
                } else {
                    request.setAttribute("success", false);
                    request.setAttribute("message", "Trạng thái yêu cầu tham gia sự kiện không hợp lệ.");
                }
                viewDetail(request, response);
                return;
            }

            // Check time conflict against user's active or pending events
            boolean hasConflict = requestJoinEventDAO.hasOverlappingRequests(
                    accountId,
                    targetEvent.getStart(),
                    targetEvent.getEnd(),
                    java.util.Arrays.asList("active", "pending"),
                    eventId
            );
            if (hasConflict) {
                request.setAttribute("success", false);
                request.setAttribute("message", "Bạn bận trong thời gian này.");
                viewDetail(request, response);
                return;
            }

            // Insert pending request
            RequestJoinEvent requestJoinEvent = new RequestJoinEvent();
            requestJoinEvent.setAccountId(accountId);
            requestJoinEvent.setEventId(eventId);
            requestJoinEvent.setStatus("pending");
            requestJoinEventDAO.insert(requestJoinEvent);
            request.setAttribute("joinEvent", true);
            viewDetail(request, response);
        }
    }

    protected void viewClubs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Club> clubList = new ClubDAO().findAll().stream().filter(club -> club.getStatus().equals("active")).collect(Collectors.toList());
        System.out.println("clubList: " + clubList);
        request.setAttribute("clubList", clubList);
        request.getRequestDispatcher("view/guest/page/listClub.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse reqs) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "viewDetail":
                viewDetail(req, reqs);
                break;
            case "submitJoin":
                submitJoinClub(req, reqs);
                break;
            default:
                throw new AssertionError();
        }
    }

    protected void submitJoinClub(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String self = req.getParameter("self");
        String reason = req.getParameter("reason");
        Integer clubId = Integer.parseInt(req.getParameter("clubId"));
        Integer accountId = Integer.parseInt(req.getParameter("accountId"));
        RequestJoinClub requestJoinClub = new  RequestJoinClub();
        requestJoinClub.setClubId(clubId);
        requestJoinClub.setReason(reason);
        requestJoinClub.setAccountId(accountId);
        requestJoinClub.setSelf(self);
        requestJoinClub.setStatus("pending");
        RequestJoinClubDAO requestJoinClubDAO = new RequestJoinClubDAO();
        if(requestJoinClubDAO.insert(requestJoinClub)>0){
            req.setAttribute("joinSuccess", true);
            viewDetail(req, resp);
        }

    }

    protected void joinclub(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        AccountClub accountClub = (session != null) ? (AccountClub) session.getAttribute(GlobalConfig.SESSION_ACCOUNT_CLUB) : null;
        if (accountClub == null) {
            req.getRequestDispatcher("view/guest/authen/login.jsp").forward(req, resp);
        } else {
            Account account = new AccountDAO().findById(accountClub.getAccount_id());
            Integer accountId = account.getId();
            Integer clubId = Integer.parseInt(req.getParameter("clubId"));
            Club club = new ClubDAO().findById(clubId);
            RequestJoinClubDAO requestJoinClubDAO = new RequestJoinClubDAO();
            List<RequestJoinClub> listCheck = requestJoinClubDAO.findByAccountId(accountId).stream().filter(requestJoinClub -> requestJoinClub.getClubId() == clubId).collect(Collectors.toList());
            if (listCheck.isEmpty()) {
                req.setAttribute("club", club);
                req.setAttribute("account", account);
                req.getRequestDispatcher("view/guest/page/registerJoinClub.jsp").forward(req, resp);
            }
            else{
                req.setAttribute("joinSuccess", false);
                viewDetail(req, resp);
            }
        }
    }

    protected void viewDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer clubId = Integer.parseInt(request.getParameter("clubId"));
        Club club = new ClubDAO().findById(clubId);

        // Determine if current logged-in user already joined this club
        boolean alreadyJoined = false;
        boolean alreadyRequestedPending = false;
        HttpSession session = request.getSession(false);
        Account account = (session != null) ? (Account) session.getAttribute(GlobalConfig.SESSION_ACCOUNT) : null;
        Integer accountId = (account != null) ? account.getId() : null;
        if (account != null) {
            AccountClubDAO accountClubDAO = new AccountClubDAO();
            AccountClub membership = accountClubDAO.findByAccountIdAndClubId(account.getId(), clubId);
            alreadyJoined = (membership != null);

            if (!alreadyJoined) {
                // Check if user has a pending join request for this club
                RequestJoinClubDAO requestJoinClubDAO = new RequestJoinClubDAO();
                List<RequestJoinClub> requests = requestJoinClubDAO.findByAccountId(accountId);
                for (RequestJoinClub r : requests) {
                    if (r.getClubId() != null && r.getClubId().equals(clubId) && "pending".equalsIgnoreCase(r.getStatus())) {
                        alreadyRequestedPending = true;
                        break;
                    }
                }
            }
        }

        Date today = new Date(); // Ngày và giờ hiện tại

        List<Event> eventList = new EventDAO().findByClubId(clubId).stream()
                .filter(event -> event.getStatus().equals("active"))
                .filter(event -> {
                    Date endDate = event.getEnd();
                    // So sánh trực tiếp, sẽ bao gồm cả thời gian
                    return endDate != null && !endDate.before(today);
                })
                .collect(Collectors.toList());

        // Build per-event join status and conflict maps for current user
        Map<Integer, String> userEventStatusMap = new java.util.HashMap<>(); // eventId -> "active" | "pending" | "none"
        Map<Integer, Boolean> eventConflictMap = new java.util.HashMap<>(); // eventId -> has time conflict with approved or pending events
        if (accountId != null) {
            RequestJoinEventDAO rjeDao = new RequestJoinEventDAO();
            List<RequestJoinEvent> userRequests = rjeDao.findByAccountId(accountId);

            // Map of eventId to status for quick lookup
            Map<Integer, String> statusByEvent = new java.util.HashMap<>();
            for (RequestJoinEvent r : userRequests) {
                statusByEvent.put(r.getEventId(), r.getStatus());
            }

            // For each event on the page, compute status and conflicts
            for (Event e : eventList) {
                String status = statusByEvent.getOrDefault(e.getId(), "none");
                userEventStatusMap.put(e.getId(), status);

                boolean conflict = false;
                if ("none".equals(status) && e.getStart() != null && e.getEnd() != null) {
                    conflict = rjeDao.hasOverlappingRequests(
                        accountId,
                        e.getStart(),
                        e.getEnd(),
                        java.util.Arrays.asList("active", "pending"),
                        e.getId()
                    );
                }
                eventConflictMap.put(e.getId(), conflict);
            }
        }

        System.out.println("eventList: " + eventList);
        request.setAttribute("eventList", eventList);
        request.setAttribute("club", club);
        request.setAttribute("alreadyJoined", alreadyJoined);
        request.setAttribute("alreadyRequestedPending", alreadyRequestedPending);
        request.setAttribute("userEventStatusMap", userEventStatusMap);
        request.setAttribute("eventConflictMap", eventConflictMap);
        request.getRequestDispatcher("view/guest/page/detailClub.jsp").forward(request, response);
    }
}

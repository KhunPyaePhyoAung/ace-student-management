package me.khun.studentmanagement.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.khun.studentmanagement.application.Application;
import me.khun.studentmanagement.model.dto.UserDto;
import me.khun.studentmanagement.model.entity.User.Role;
import me.khun.studentmanagement.model.service.UserService;
import me.khun.studentmanagement.model.service.exception.InvalidFieldException;
import me.khun.studentmanagement.model.service.exception.ServiceException;
import me.khun.studentmanagement.security.LoginUser;
import me.khun.studentmanagement.view.Alert;
import me.khun.studentmanagement.view.Alert.Type;

@WebServlet(
	urlPatterns = {
		"/user/search",
		"/user/search/request",
		"/user/edit",
		"/user/save",
		"/user/delete",
		"/user/detail",
		"/user/approve"
	}
)
public class UserController extends BaseController {

    private static final long serialVersionUID = 1L;
    
    private UserService userService;
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	userService = Application.getUserService();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	switch (req.getServletPath()) {
			case "/user/search":
			case "/user/search/request":
				searchUsers(req, resp);
				break;
			case "/user/edit":
				navigateToEditUserPage(req, resp);
				break;
			case "/user/detail":
				navigateToUserDetailPage(req, resp);
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + req.getServletPath());
		}
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {    	
    	switch (req.getServletPath()) {
			case "/user/save":
				saveUser(req, resp);
				break;
			case "/user/delete":
				deleteUser(req, resp);
				break;
			case "/user/approve":
				approveUser(req, resp);
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + req.getServletPath());
		}
    }

    private void saveUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var id = req.getParameter("id");
        var name = req.getParameter("name");
        var email = req.getParameter("email");
        var password = req.getParameter("password");
        var confirmPassword = req.getParameter("confirmPassword");
        var role = req.getParameter("role");
        
        var user = new UserDto();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setConfirmPassword(confirmPassword);
        user.setRole((role == null || role.isBlank()) ? null : Role.valueOf(role));
        user.setApproved(true);
        
        var session = req.getSession();
        var loginUser = (LoginUser) session.getAttribute("loginUser");
        var loginUserId = loginUser.getUser().getId();
        
        try {
        	var saved = userService.save(user);
        	if (!saved.getId().equals(user.getId())) {
        		var alert = new Alert("Successfully created a new user.", Type.SUCCESS);
            	req.getSession(false).setAttribute("alert", alert);
        	}
        	
        	if (loginUserId.equals(saved.getId())) {
        		loginUser.setUser(userService.findById(loginUserId));
        	}
        	
        	redirect(resp, "/user/search");
        	return;
        } catch (InvalidFieldException e) {
        	session.setAttribute("invalidFieldException", e);
        } catch (ServiceException e) {
        	session.setAttribute("userFormErrorMessage", e.getMessage());
        }
        
        session.setAttribute("user", user);
        var param = id == null || id.isBlank() ? "" : "?id=".formatted(id);
    	redirect(resp, "/user/edit" + param);
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var id = req.getParameter("id");
        var approvedParam = req.getParameter("approved");
    	var approved = (approvedParam == null || !approvedParam.equals("false")) ? true : false;
    	
        var alert = new Alert();
        
        try {
        	if (getLoginInfo(req).getUser().getId().equals(id)) {
        		throw new ServiceException("You cannot delete this user.");
            }
        	
        	userService.deleteById(id);
        	alert.setMessage("Successfully %s the user.".formatted(approved ? "deleted" : "removed"));
        	alert.setType(Type.SUCCESS);
        } catch (ServiceException e) {
        	alert.setMessage(e.getMessage());
        	alert.setType(Type.ERROR);
        }
        
        req.getSession(false).setAttribute("alert", alert);
        var param = approved ? "" : "/request";
    	redirect(resp, "/user/search" + param);
    }

    private void searchUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	var keyword = req.getParameter("keyword");
    	var approved = req.getServletPath().equals("/user/search");
    	
    	req.setAttribute("users", userService.search(keyword, approved));
    	
    	var session = req.getSession(false);
    	req.setAttribute("alert", session.getAttribute("alert"));
    	session.setAttribute("alert", null);
    	
    	if (!approved) {
    		req.setAttribute("title", "User Requests");
            forward(req, resp, "/views/user-requests.jsp");
            return;
    	}
    	
    	req.setAttribute("title", "Users");
        forward(req, resp, "/views/users.jsp");
    }

    private void navigateToEditUserPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	var id = req.getParameter("id");
    	var session = req.getSession();
    	
    	req.setAttribute("userFormErrorMessage", session.getAttribute("userFormErrorMessage"));
    	session.setAttribute("userFormErrorMessage", null);
    	
    	req.setAttribute("invalidFieldException", session.getAttribute("invalidFieldException"));
    	session.setAttribute("invalidFieldException", null);
    	
    	if (session.getAttribute("user") == null) {
    		req.setAttribute("user", userService.findById(id));
    	} else {
    		req.setAttribute("user", session.getAttribute("user"));
    		session.setAttribute("user", null);
    	}
    	
    	var user = (UserDto) req.getAttribute("user");
    	req.setAttribute("title", (user == null || user.getId() == null || user.getId().isBlank()) ? "User Registration" : "Edit User");
        forward(req, resp, "/views/edit-user.jsp");
    }
    
    private void navigateToUserDetailPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	var id = req.getParameter("id");
    	var user = userService.findById(id);
    	if (user == null) {
    		resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No user with this id.");
    		return;
    	}
    	
    	var loginUser = ((LoginUser) req.getSession().getAttribute("loginUser")).getUser();
    	
    	if (loginUser.getRole() == Role.USER && !user.isApproved()) {
    		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not authorized.");
    		return;
    	}
    	
    	req.setAttribute("user", user);
    	req.setAttribute("title", "User Detail");
    	forward(req, resp, "/views/user-detail.jsp");
    }
    
    private void approveUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	var id = req.getParameter("id");
    	var user = userService.findById(id);
    	user.setApproved(true);
    	userService.save(user);
    	var alert = new Alert("User have been approved.", Type.SUCCESS);
    	req.getSession(false).setAttribute("alert", alert);
    	redirect(resp, "/user/search/request");
    }

}
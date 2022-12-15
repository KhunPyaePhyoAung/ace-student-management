package me.khun.studentmanagement.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.khun.studentmanagement.application.Application;
import me.khun.studentmanagement.model.dto.UserDto;
import me.khun.studentmanagement.model.entity.User.Role;
import me.khun.studentmanagement.model.service.AuthService;
import me.khun.studentmanagement.model.service.UserService;
import me.khun.studentmanagement.model.service.exception.InvalidFieldException;
import me.khun.studentmanagement.model.service.exception.ServiceException;
import me.khun.studentmanagement.security.LoginUser;

@WebServlet(
	urlPatterns = {
		"/login",
		"/logout",
		"/signup"
	}
)
public class AuthController extends BaseController {

    private static final long serialVersionUID = 1L;
    
    private AuthService authService;
    
    private UserService userService;
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	authService = Application.getAuthService();
    	userService = Application.getUserService();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	switch (req.getServletPath()) {
			case "/login":
				navigateToLoginPage(req, resp);
				break;
			case "/logout":
				logout(req, resp);
				break;
			case "/signup":
				navigateToSignupPage(req, resp);
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + req.getServletPath());
		}
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	switch (req.getServletPath()) {
			case "/login":
				login(req, resp);
				break;
			case "/signup":
				signup(req, resp);
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + req.getServletPath());
		}
    }
    
    private void navigateToLoginPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	var session = req.getSession();
    	req.setAttribute("loginFormErrorMessage", session.getAttribute("loginFormErrorMessage"));
    	session.setAttribute("loginFormErrorMessage", null);
    	req.setAttribute("email", session.getAttribute("email"));
    	session.setAttribute("email", null);
    	req.setAttribute("password", session.getAttribute("password"));
    	session.setAttribute("password", null);
    	forward(req, resp, "/views/login.jsp");
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	var email = req.getParameter("email");
    	var password = req.getParameter("password");
    	
    	var session = req.getSession();
    	
    	UserDto user = null;
    	
    	try {
    		user = authService.login(email, password);
            if (user == null) {
            	throw new ServiceException("Invalid Email or Password");
            }
    	} catch (ServiceException e) {
    		session.setAttribute("loginFormErrorMessage", e.getMessage());
        	session.setAttribute("email", email);
        	session.setAttribute("password", password);
        	redirect(resp, "/login");
        	return;
    	}
        
        var loginUser = new LoginUser();
        loginUser.setUser(user);
        loginUser.setLoggedInDateTime(LocalDateTime.now());
        
        session.setAttribute("loginUser", loginUser);
        
        redirect(resp, "/");
    }
    
    private void signup(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	var id = req.getParameter("id");
        var name = req.getParameter("name");
        var email = req.getParameter("email");
        var password = req.getParameter("password");
        var confirmPassword = req.getParameter("confirmPassword");
        
        var user = new UserDto();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setConfirmPassword(confirmPassword);
        user.setRole(Role.USER);
        user.setApproved(false);
        
        var session = req.getSession();
        
        try {
        	userService.save(user);
        	redirect(resp, "/user/search");
        	return;
        } catch (InvalidFieldException e) {
        	session.setAttribute("invalidFieldException", e);
        } catch (ServiceException e) {
        	session.setAttribute("signupFormErrorMessage", e.getMessage());
        }
        
        session.setAttribute("user", user);
    	redirect(resp, "/signup");
    }
    
    private void navigateToSignupPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	var session = req.getSession();
    	req.setAttribute("signupFormErrorMessage", session.getAttribute("signupFormErrorMessage"));
    	session.setAttribute("signupFormErrorMessage", null);
    	
    	req.setAttribute("invalidFieldException", session.getAttribute("invalidFieldException"));
    	session.setAttribute("invalidFieldException", null);
    	
    	req.setAttribute("user", session.getAttribute("user"));
		session.setAttribute("user", null);
    	
    	forward(req, resp, "/views/signup.jsp");
    }

    private void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getSession(false).invalidate();
        redirect(resp, "/login");
    }

}
package me.khun.studentmanagement.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.khun.studentmanagement.model.entity.User.Role;

@WebFilter(
	filterName = "AdminAuthorizationFilter",
	urlPatterns = {
			"/user/edit",
			"/user/save",
			"/user/delete",
			"/user/approve",
			"/user/search/request",
			"/course/edit",
			"/course/save",
			"/course/delete",
			"/student/edit",
			"/student/save",
			"/student/delete"
	}
)
public class AdminAuthroizationFilter extends HttpFilter implements Filter {
       
    private static final long serialVersionUID = 1L;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		var req = (HttpServletRequest) request;
		var resp = (HttpServletResponse) response;
		var session = req.getSession();
		var loginUser = (LoginUser)session.getAttribute("loginUser");
		var user = loginUser.getUser();
		
		if (user.getRole() == Role.ADMIN) {
			chain.doFilter(request, response);
			return;
		}
		
		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not authorized.");
		
	}


}

package me.khun.studentmanagement.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.khun.studentmanagement.application.Application;

@WebFilter(
	filterName = "AuthenticationFilter",
	urlPatterns = {
		"/*"
	}
)
public class AuthenticationFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		var request = (HttpServletRequest) req;
		var response = (HttpServletResponse) resp;
		var path = request.getServletPath();
		
		if (!(path.equals("/login")
				|| path.equals("/logout")
				|| path.equals("/signup"))) {
			var session = request.getSession(true);
			
			var loginUser = (LoginUser) session.getAttribute("loginUser");
			
			if (loginUser == null || loginUser.getUser() == null) {
				response.sendRedirect(request.getServletContext().getContextPath() + "/login");
				return;
			}
		}
		
		req.setAttribute("userRequestCount", Application.getUserService().getCount(false));
		
		chain.doFilter(req, resp);
		
	}

}

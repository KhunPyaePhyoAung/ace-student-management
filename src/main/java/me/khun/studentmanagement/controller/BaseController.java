package me.khun.studentmanagement.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.khun.studentmanagement.security.LoginUser;

public abstract class BaseController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void redirect(HttpServletResponse resp, String url) throws IOException {
		resp.sendRedirect(getServletContext().getContextPath() + url);
	}
	
	protected void forward(HttpServletRequest req, HttpServletResponse resp, String view) throws ServletException, IOException {
		req.getRequestDispatcher(view).forward(req, resp);
	}
	
	protected LocalDate parseDate(String dateString) {
		if (dateString == null) {
			return null;
		}
		return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	
	protected LoginUser getLoginInfo(HttpServletRequest req) {
		return (LoginUser) req.getSession().getAttribute("loginUser");
	}
}

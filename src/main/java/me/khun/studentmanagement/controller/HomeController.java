package me.khun.studentmanagement.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.khun.studentmanagement.application.Application;
import me.khun.studentmanagement.model.service.CourseService;
import me.khun.studentmanagement.model.service.StudentService;
import me.khun.studentmanagement.model.service.UserService;

@WebServlet(
	urlPatterns = {
		"/home"
	}
)
public class HomeController extends BaseController {

    private static final long serialVersionUID = 1L;
    
    private UserService userService;
    private CourseService courseService;
    private StudentService studentService;
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	userService = Application.getUserService();
    	courseService = Application.getCourseService();
    	studentService = Application.getStudentService();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	switch (req.getServletPath()){
			case "/home":
				navigateToHomePage(req, resp);
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + req.getServletPath());
		}
    }

	public void navigateToHomePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("userCount", userService.getCount(true));
		req.setAttribute("courseCount", courseService.getCount());
		req.setAttribute("studentCount", studentService.getCount());
		req.setAttribute("title", "Home");
        forward(req, resp, "/views/home.jsp");
    }
}
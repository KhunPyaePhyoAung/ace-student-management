package me.khun.studentmanagement.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.khun.studentmanagement.application.Application;
import me.khun.studentmanagement.model.dto.CourseDto;
import me.khun.studentmanagement.model.service.CourseService;
import me.khun.studentmanagement.model.service.exception.InvalidFieldException;
import me.khun.studentmanagement.model.service.exception.ServiceException;
import me.khun.studentmanagement.view.Alert;
import me.khun.studentmanagement.view.Alert.Type;

@WebServlet(
	urlPatterns = {
		"/course/search",
		"/course/edit",
		"/course/save",
		"/course/delete",
		"/course/detail"
	}
)
public class CourseController extends BaseController {

    private static final long serialVersionUID = 1L;
    
    private CourseService courseService;
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	courseService = Application.getCourseService();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	switch (req.getServletPath()) {
			case "/course/search":
				searchCourses(req, resp);
				break;
			case "/course/edit":
				navigateToEditCoursePage(req, resp);
				break;
			case "/course/detail":
				navigateToCourseDetailPage(req, resp);
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + req.getServletPath());
    	}
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	switch (req.getServletPath()) {
			case "/course/save":
				saveCourse(req, resp);
				break;
			case "/course/delete":
				deleteCourse(req, resp);
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + req.getServletPath());
    	}
    }

    private void searchCourses(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	var keyword = req.getParameter("keyword");
    	
    	var session = req.getSession(false);
    	req.setAttribute("alert", session.getAttribute("alert"));
    	session.setAttribute("alert", null);
    	
    	req.setAttribute("courses", courseService.search(keyword));
    	req.setAttribute("title", "Courses");
        forward(req, resp, "/views/courses.jsp");
    }

    private void navigateToEditCoursePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	var id = req.getParameter("id");
    	
    	var session = req.getSession();
    	
    	req.setAttribute("courseFormErrorMessage", session.getAttribute("courseFormErrorMessage"));
    	session.setAttribute("courseFormErrorMessage", null);
    	
    	req.setAttribute("invalidFieldException", session.getAttribute("invalidFieldException"));
    	session.setAttribute("invalidFieldException", null);
    	
    	
    	if (session.getAttribute("course") == null) {
    		req.setAttribute("course", courseService.findById(id));
    	} else {
    		req.setAttribute("course", session.getAttribute("course"));
    		session.setAttribute("course", null);
    	}
    	
    	var course = (CourseDto) req.getAttribute("course");
    	req.setAttribute("title", (course == null || course.getId() == null || course.getId().isBlank()) ? "Course Registration" : "Edit Course");
        forward(req, resp, "/views/edit-course.jsp");
    }

    private void saveCourse(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        var id = req.getParameter("id");
        var idPrefix = req.getParameter("idPrefi");
        var idCode = req.getParameter("idCode") == null || req.getParameter("idCode").isBlank() ? null : Integer.parseInt(req.getParameter("idCode"));
        var name = req.getParameter("name");
        var shortName = req.getParameter("shortName");
        
        var course = new CourseDto();
        course.setId(id);
        course.setIdPrefix(idPrefix);
        course.setIdCode(idCode);
        course.setName(name);
        course.setShortName(shortName);
        
        var session = req.getSession();
        
        try {
        	var saved = courseService.save(course);
        	if (!saved.getId().equals(id)) {
        		var alert = new Alert("Successfully created a new course.", Type.SUCCESS);
            	req.getSession(false).setAttribute("alert", alert);
        	}
        	redirect(resp, "/course/search");
        	return;
        } catch (InvalidFieldException e) {
        	session.setAttribute("invalidFieldException", e);
        } catch (ServiceException e) {
        	session.setAttribute("courseFormErrorMessage", e.getMessage());
        }
        
        session.setAttribute("course", course);
        
        var param = (id == null || id.isBlank()) ? "" : "?id=%s".formatted(id); 
    	redirect(resp, "/course/edit" + param);
    }

    private void deleteCourse(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var id = req.getParameter("id");
        var alert = new Alert();
        
        try {
        	courseService.deleteById(id);
        	alert.setMessage("Successfully deleted the course.");
        	alert.setType(Type.SUCCESS);
        } catch (ServiceException e) {
			alert.setMessage(e.getMessage());
			alert.setType(Type.ERROR);
		}
        
        req.getSession(false).setAttribute("alert", alert);
        redirect(resp, "/course/search");
    }
    
    private void navigateToCourseDetailPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	var id = req.getParameter("id");
    	var course = courseService.findById(id);
    	if (course == null) {
    		resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No course with this id.");
    		return;
    	}
    	
    	req.setAttribute("course", course);
    	req.setAttribute("title", "Course Detail");
    	forward(req, resp, "/views/course-detail.jsp");
    }

}
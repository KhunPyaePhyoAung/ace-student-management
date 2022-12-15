package me.khun.studentmanagement.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.khun.studentmanagement.application.Application;
import me.khun.studentmanagement.model.dto.CourseDto;
import me.khun.studentmanagement.model.dto.StudentDto;
import me.khun.studentmanagement.model.entity.Student.Gender;
import me.khun.studentmanagement.model.service.CourseService;
import me.khun.studentmanagement.model.service.StudentService;
import me.khun.studentmanagement.model.service.exception.InvalidFieldException;
import me.khun.studentmanagement.model.service.exception.ServiceException;
import me.khun.studentmanagement.view.Alert;
import me.khun.studentmanagement.view.Alert.Type;

@WebServlet(
	urlPatterns = {
		"/student/search",
		"/student/edit",
		"/student/save",
		"/student/delete",
		"/student/detail"
	}
)
public class StudentController extends BaseController {

    private static final long serialVersionUID = 1L;
    
    private StudentService studentService;
    
    private CourseService courseService;
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	studentService = Application.getStudentService();
    	courseService = Application.getCourseService();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	switch (req.getServletPath()) {
			case "/student/search":
				searchStudents(req, resp);
				break;
			case "/student/edit":
				navigateToEditStudentPage(req, resp);
				break;
			case "/student/detail":
				navigateToStudentEditPage(req, resp);
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + req.getServletPath());
		}
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	switch (req.getServletPath()) {
			case "/student/save":
				saveStudent(req, resp);
				break;
			case "/student/delete":
				deleteStudent(req, resp);
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + req.getServletPath());
		}
    }

	private void searchStudents(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		var studentKeyword = req.getParameter("studentKeyword");
		var courseKeyword = req.getParameter("courseKeyword");
		
		var session = req.getSession(false);
		req.setAttribute("alert", session.getAttribute("alert"));
		session.setAttribute("alert", null);
		
		var students = studentService.search(studentKeyword, courseKeyword);
		req.setAttribute("students", students);
		req.setAttribute("title", "Students");
        forward(req, resp, "/views/students.jsp");
    }

	private void navigateToEditStudentPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var id = req.getParameter("id");
        
        var session = req.getSession();
        
        req.setAttribute("studentFormErrorMessage", session.getAttribute("studentFormErrorMessage"));
        session.setAttribute("studentFormErrorMessage", null);
        
        req.setAttribute("invalidFieldException", session.getAttribute("invalidFieldException"));
        session.setAttribute("invalidFieldException", null);
        
        if (session.getAttribute("student") == null) {
        	var student = studentService.findById(id);
        	req.setAttribute("student", student);
        } else {
        	req.setAttribute("student", session.getAttribute("student"));
        	session.setAttribute("student", null);
        }
        
        var student = (StudentDto) req.getAttribute("student");
        req.setAttribute("title", (student == null || student.getId() == null || student.getId().isBlank()) ? "Student Registration" : "Edit Student");
        req.setAttribute("educations", getEducations());
        req.setAttribute("courses", courseService.findAll());
        forward(req, resp, "/views/edit-student.jsp");
    }
	
	private void navigateToStudentEditPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		var id = req.getParameter("id");
		var student = studentService.findById(id);
		if (student == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No student with this id.");
			return;
		}
		req.setAttribute("student", student);
		req.setAttribute("title", "Student Detail");
		forward(req, resp, "/views/student-detail.jsp");
	}

	private void saveStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        var id = req.getParameter("id");
        var idPrefix = req.getParameter("idPrefix");
        var idCodeParam = req.getParameter("idCode");
        var idCode = (idCodeParam == null || idCodeParam.isBlank()) ? null : Integer.parseInt(req.getParameter("idCode"));
        var name = req.getParameter("name");
        var dateOfBirth = (req.getParameter("dateOfBirth") == null || req.getParameter("dateOfBirth").isBlank()) ? null : parseDate(req.getParameter("dateOfBirth"));
        var gender = (req.getParameter("gender") == null || req.getParameter("gender").isBlank()) ? null : Gender.valueOf(req.getParameter("gender"));
        var phone = req.getParameter("phone");
        var education = req.getParameter("education");
        var courseIdCodes = req.getParameterValues("courses") == null ? new String[] {} : req.getParameterValues("courses");
        
        var courseList = Arrays.asList(courseIdCodes).stream().map(c -> {
        	var course = new CourseDto();
        	course.setIdCode(Integer.parseInt(c));
        	return course;
        }).toList();
        
        var student = new StudentDto();
        student.setId(id);
        student.setIdPrefix(idPrefix);
        student.setIdCode(idCode);
        student.setName(name);
        student.setDateOfBirth(dateOfBirth);
        student.setGender(gender);
        student.setEducation(education);
        student.setPhone(phone);
        student.setCourses(courseList);
        
    	var session = req.getSession();
        
        try {
        	var saved = studentService.save(student);
        	if (!saved.getId().equals(id)) {
        		var alert = new Alert("Successfully created a new student.", Type.SUCCESS);
            	req.getSession(false).setAttribute("alert", alert);
        	}
        	redirect(resp, "/student/search");
        	return;
        } catch (InvalidFieldException e) {
        	session.setAttribute("invalidFieldException", e);
        }
        catch (ServiceException e) {
        	session.setAttribute("studentFormErrorMessage", e.getMessage());
        }
        
        session.setAttribute("student", student);
        var param = (id == null || id.isBlank()) ? "" : "?id=%s".formatted(id);
    	redirect(resp, "/student/edit" + param);
    }

	private void deleteStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var id = req.getParameter("id");
        
        var alert = new Alert();
        
        try {
        	studentService.deleteById(id);
        	alert.setMessage("Successfully deleted the student.");
        	alert.setType(Type.SUCCESS);
        } catch (ServiceException e) {
        	alert.setMessage(e.getMessage());
        	alert.setType(Type.ERROR);
        }
        
        req.getSession(false).setAttribute("alert", alert);
        redirect(resp, "/student/search");
    }
	
	private List<String> getEducations() {
		return List.of(
				"Bachelor of Information Technology",
				"Diploma in IT",
				"Other"
			);
	}

}
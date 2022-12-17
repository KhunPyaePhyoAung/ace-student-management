package me.khun.studentmanagement.application;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import me.khun.studentmanagement.model.entity.Course;
import me.khun.studentmanagement.model.entity.Student;
import me.khun.studentmanagement.model.entity.Student.Gender;
import me.khun.studentmanagement.model.entity.User;
import me.khun.studentmanagement.model.entity.User.Role;
import me.khun.studentmanagement.model.repo.CourseRepo;
import me.khun.studentmanagement.model.repo.StudentRepo;
import me.khun.studentmanagement.model.repo.impl.MyBatisCourseRepoImpl;
import me.khun.studentmanagement.model.repo.impl.MyBatisStudentRepoImpl;
import me.khun.studentmanagement.model.repo.impl.MyBatisUserRepoImpl;

public class Main {
	private static CourseRepo courseRepo = new MyBatisCourseRepoImpl();
	private static StudentRepo studentRepo = new MyBatisStudentRepoImpl();
	
	public static void main(String[] args) throws SQLException, IOException {
		studentUpdate();
	}
	
	public static void studentInsert() {
		var student = new Student();
		student.setName("Mo Mo");
		student.setDateOfBirth(LocalDate.now());
		student.setGender(Gender.MALE);
		student.setPhone("09888838383");
		student.setEducation("BEEC");
		
		var courses = new ArrayList<Course>();
		var course1 = new Course();
		course1.setIdCode(10003);
		var course2 = new Course();
		course2.setIdCode(10004);
		
		courses.add(course1);
		courses.add(course2);
		
		student.setCourses(courses);
		
		var createdStudent = studentRepo.create(student);
		printStudent(createdStudent);
	}
	
	public static void studentUpdate() {
		var student = studentRepo.findById("STU10011");
		student.setName("Mo Mo Updated");
		student.setDateOfBirth(LocalDate.now());
		student.setGender(Gender.FEMALE);
		student.setPhone("095555555");
		student.setEducation("BEIT");
		
		var courses = new ArrayList<Course>();
		courses.add(courseRepo.findById("CUR10004"));
		
		student.setCourses(courses);
		
		studentRepo.update(student);
	}
	
	public static void studentSelectById() {
		printStudent(studentRepo.findById("STU10016"));
	}
	
	public static void studentSelectAll() {
		for (var s : studentRepo.findAll()) {
			printStudent(s);
		}
	}
	
	public static void studentSearch() {
		for (var s : studentRepo.search("no", "")) {
			printStudent(s);
		}
	}
	
	public static void studentGetCount() {
		System.out.println(studentRepo.getCount());
	}
	
	public static void studentDeleteById() {
		studentRepo.deleteById("STU10001");
	}
	
	public static void printStudent(Student student) {
		System.out.println(student.getIdPrefix());
		System.out.println(student.getIdCode());
		System.out.println(student.getId());
		System.out.println(student.getName());
		System.out.println(student.getDateOfBirth());
		System.out.println(student.getGender());
		System.out.println(student.getPhone());
		System.out.println(student.getEducation());
		
		for (var c : student.getCourses()) {
			printCourse(c);
		}
		
		System.out.println("-----------------------------");
	}
	
	public static void courseInsert() {
		var course = new Course();
		course.setName("JavaScript");
		course.setShortName("JS");
		
		courseRepo.create(course);
	}
	
	public static void courseUpdate() {
		var course = new Course();
		course.setId("CUR10000");
		course.setName("Programming Fundamental");
		course.setShortName("PF");
		courseRepo.update(course);
	}
	
	public static void courseFindById() {
		var course = courseRepo.findById("CUR10000");
		printCourse(course);
	}
	
	public static void courseFindAll() {
		var courses = courseRepo.findAll();
		for (var c : courses) {
			printCourse(c);
		}
	}
	
	public static void courseGetCount() {
		System.out.println(courseRepo.getCount());
	}
	
	public static void courseFindByStudentId() {
		var courses = courseRepo.findByStudentId("STU10009");
		for (var c : courses) {
			printCourse(c);
		}
	}
	
	public static void courseSearchByKeyword() {
		var courses = courseRepo.search("php");
		for(var c : courses) {
			printCourse(c);
		}
	}
	
	public static void courseDeleteById() {
		courseRepo.deleteById("CUR10002");
	}
	
	public static void printCourse(Course course) {
		System.out.println(course.getIdPrefix());
		System.out.println(course.getIdCode());
		System.out.println(course.getId());
		System.out.println(course.getName());
		System.out.println(course.getShortName());
	}
	
	public static void userInsert() throws IOException {
		var user = new User();
		user.setName("Mg Soe Hun");
		user.setRole(Role.ADMIN);
		user.setEmail("mgsoetun15@gmail.com");
		user.setPassword("mgsoetun");
		user.setApproved(true);
		
		var userRepo = new MyBatisUserRepoImpl();
		var createdUser =  userRepo.create(user);
		
		System.out.println(createdUser.getId());
		System.out.println(createdUser.getIdPrefix());
		System.out.println(createdUser.getIdCode());
		System.out.println(createdUser.getName());
		System.out.println(createdUser.getEmail());
	}
	
	public static void userUpdate() {
		var user = new User();
		user.setId("USR14");
		user.setName("Mg Soe Tun Updated");
		user.setEmail("sotunupdated@gmail.com");
		user.setRole(Role.ADMIN);
		user.setPassword("1234");
		user.setApproved(false);
		
		var userRepo = new MyBatisUserRepoImpl();
		userRepo.update(user);
	}
	
	public static void userDeleteById() {
		var userRepo = new MyBatisUserRepoImpl();
		userRepo.deleteById("USR7");
	}
	
	public static void userFindById() {
		var userRepo = new MyBatisUserRepoImpl();
		var user = userRepo.findById("USR14");
		printUser(user);
	}
	
	public static void userFindAll() {
		var userRepo = new MyBatisUserRepoImpl();
		var list = userRepo.findAll();
		for (var u : list) {
			printUser(u);
		}
	}
	
	public static void userGetCount() {
		var userRepo = new MyBatisUserRepoImpl();
		System.out.println(userRepo.getCount());
	}
	
	public static void userSearch() {
		var userRepo = new MyBatisUserRepoImpl();
		var users = userRepo.search("soe", true);
		for (var u : users) {
			printUser(u);
		}
	}
	
	public static void userSearchByEmailAndPassword() {
		var userRepo = new MyBatisUserRepoImpl();
		var users = userRepo.findByEmailAndPassword("mgsoetun9@gmail.com ", "mgsoetun");
		printUser(users);
	}
	
	public static void userGetCountByApproved() {
		var userRepo = new MyBatisUserRepoImpl();
		var count = userRepo.getCount(false);
		System.out.println(count);
	}
	
	public static void printUser(User user) {
		System.out.println(user.getIdPrefix());
		System.out.println(user.getIdCode());
		System.out.println(user.getId());
		System.out.println(user.getName());
		System.out.println(user.getEmail());
		System.out.println(user.getRole());
		System.out.println(user.getPassword());
	}
}

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cus" uri="/WEB-INF/custom-tag.tld" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${title}</title>
<jsp:include page="/views/resources.jsp"></jsp:include>
</head>
<body>
	<c:set var="role" value="${sessionScope.loginUser.getUser().getRole()}" scope="request"></c:set>
	<c:set var="menu" value="students" scope="request"></c:set>
	<jsp:include page="/views/nav-bar.jsp"></jsp:include>
	
	<div class="container my-4">
		<div class="row">
			<div class="col-6 offset-3">
				<span class="fs-4 fw-bold">${title}</span>
				<c:if test="${not empty studentFormErrorMessage}">
					<div class="mt-4">
						<label class="text-danger">${studentFormErrorMessage}</label>
					</div>
				</c:if>
				
				<c:url var="studentSaveUrl" value="/student/save"></c:url>
				<form class="mt-4" action="${studentSaveUrl}" method="post">
					<input type="hidden" name="id" value="${student.getId()}" />
					<input type="hidden" name="idPrefix" value="${student.getIdPrefix()}" />
					<input type="hidden" name="idCode" value="${student.getIdCode()}" />
					<div class="form-group">
						<label for="name" class="form-label">Name</label>
						<input id="name" class="form-control" type="text" name="name" value="${student.getName()}"/>
						<cus:invalidFieldMessage exception="${invalidFieldException}" field="name" cssClass="text-danger"/>
					</div>
					<div class="form-group mt-3">
						<label for="dob" class="form-label">Date Of Birth</label>
						<input id="dob" class="form-control" type="date" name="dateOfBirth" value="${student.getDateOfBirth()}"/>
						<cus:invalidFieldMessage exception="${invalidFieldException}" field="dateOfBirth" cssClass="text-danger"/>
					</div>
					<div class="row mt-3">
						<label class="col-auto form-label">Gender</label>
						<div class="col-auto form-check">
							<input class="form-check-input" id="male" type="radio" name="gender" value="MALE" ${student.getGender() eq 'MALE' ? 'checked' : ''}/>
							<label class="form-check-label" for="male">Male</label>
						</div>
						<div class="col-auto form-check">
							<input class="form-check-input" id="female" type="radio" name="gender" value="FEMALE" ${student.getGender() eq 'FEMALE' ? 'checked' : ''}/>
							<label class="form-check-label" for="female">Female</label>
						</div>
					</div>
					<cus:invalidFieldMessage exception="${invalidFieldException}" field="gender" cssClass="text-danger"/>
					<div class="form-group mt-3">
						<label for="phone" class="form-label">Phone</label>
						<input id="phone" class="form-control" type="tel" name="phone" value="${student.getPhone()}"/>
						<cus:invalidFieldMessage exception="${invalidFieldException}" field="phone" cssClass="text-danger"/>
					</div>
					<div class="form-group mt-3">
						<label for="education" class="form-label">Education</label>
						<select class="form-select" name="education" id="education">
							<option value="">-- select education --</option>
							<c:forEach var="edu" items="${educations}">
								<option value="${edu}" ${student.getEducation() eq edu ? 'selected' : ''}>${edu}</option>
							</c:forEach>
						</select>
						<cus:invalidFieldMessage exception="${invalidFieldException}" field="education" cssClass="text-danger"/>
					</div>
					
					<div class="mt-3">
						<label>Attend</label>
						<div class="d-flex flex-wrap align-content-between justify-content-start mt-2">
							<c:forEach var="course" items="${courses}" varStatus="status">
								<div class="form-check me-3">
									<input id="course${status.getCount()}" type="checkbox" class="form-check-input" name="courses" value="${course.getIdCode()}" ${student.getCourses().contains(course) ? 'checked' : ''}/>
									<p title="${course.getId()} : ${course.getName()}">
										<label for="course${status.getCount()}" class="form-check-label">
											${course.getShortName()}
										</label>
									</p>
								</div>
							</c:forEach>
						</div>
						<cus:invalidFieldMessage exception="${invalidFieldException}" field="courses" cssClass="text-danger"/>
					</div>
					
					<button class="form-control btn btn-primary mt-4" type="submit">
						<i class="fa-solid fa-floppy-disk me-2"></i>
						Save
					</button>
					
					<c:url var="cancelUrl" value="/student/search"></c:url>
					<a href="${cancelUrl}" class="btn btn-secondary d-block mt-2">
						<i class="fa-solid fa-xmark me-2"></i>
						Cancel
					</a>
				</form>
			</div>
		</div>
	</div>

</body>
</html>
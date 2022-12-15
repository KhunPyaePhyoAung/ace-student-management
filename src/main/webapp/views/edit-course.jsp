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
	<c:set var="menu" value="courses" scope="request"></c:set>
	<jsp:include page="/views/nav-bar.jsp"></jsp:include>
	
	<div class="container my-4">
		<div class="row">
			<div class="col-6 offset-3">
				<span class="fs-4 fw-bold">${title}</span>
				
				<c:if test="${not empty courseFormErrorMessage}">
					<div class="mt-4">
						<label class="text-danger">${courseFormErrorMessage}</label>
					</div>
				</c:if>
				
				<c:url var="saveCourseUrl" value="/course/save"></c:url>
				<form class="mt-4" action="${saveCourseUrl}" method="post">
					<input type="hidden" name="id" value="${course.getId()}" />
					<input type="hidden" name="idPrefix" value="${course.getIdPrefix()}"/>
					<input type="hidden" name="idCode" value="${course.getIdCode()}" />
					
					<div class="form-group mt-3">
						<label for="name" class="form-label">Course Name</label>
						<input id="name" class="form-control" type="text" name="name" value="${course.getName()}"/>
						<cus:invalidFieldMessage exception="${invalidFieldException}" field="name" cssClass="text-danger"/>
					</div>
					
					<div class="form-group mt-3">
						<label for="shortName" class="form-label">Short Name</label>
						<input id="shortName" class="form-control" type="text" name="shortName" value="${course.getShortName()}"/>
						<cus:invalidFieldMessage exception="${invalidFieldException}" field="shortName" cssClass="text-danger"/>
					</div>
					
					<button class="form-control btn btn-primary mt-4" type="submit">
						<i class="fa-solid fa-floppy-disk me-2"></i>
						Save
					</button>
					
					<c:url var="cancelUrl" value="/course/search"></c:url>
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
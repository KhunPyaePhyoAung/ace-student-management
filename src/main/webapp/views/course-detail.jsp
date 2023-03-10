<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
	
	<main class="container">
		<div class="row mt-4">
			<div class="col-6 offset-3 fs-4 fw-bold">
				${title}
			</div>
		</div>
		<div class="row mt-4">
			<div class="col-6 offset-3">
				<table class="table">
					<tbody>
						<tr>
							<th>ID</th>
							<td>${course.getId()}</td>
						</tr>
						<tr>
							<th>Name</th>
							<td>${course.getName()}</td>
						</tr>
						<tr>
							<th>Short Name</th>
							<td>${course.getShortName()}</td>
						</tr>
					</tbody>
				</table>
				
				<c:url var="courseEditUrl" value="/course/edit">
					<c:param name="id">${course.getId()}</c:param>
				</c:url>
				<a href="${courseEditUrl}" class="btn btn-primary w-100 mt-4 ${role eq 'ADMIN' ? '' : 'd-none'}">
					<i class="fa-solid fa-pen-to-square"></i>
					Edit
				</a>
				
				<c:url var="cancelUrl" value="/course/search"></c:url>
				<a href="${cancelUrl}" class="btn btn-secondary w-100 mt-2">
					<i class="fa-solid fa-xmark me-2"></i>
					Close
				</a>
			</div>
		</div>
	</main>

</body>
</html>
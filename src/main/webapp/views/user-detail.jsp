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
	<c:set var="menu" value="users" scope="request"></c:set>
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
							<td>${user.getId()}</td>
						</tr>
						<tr>
							<th>Name</th>
							<td>${user.getName()}</td>
						</tr>
						<tr>
							<th>Role</th>
							<td>${user.getRole()}</td>
						</tr>
						<tr>
							<th>Email</th>
							<td>${user.getEmail()}</td>
						</tr>
						<tr>
							<th>Status</th>
							<td>${user.isApproved() ? 'Approved' : 'Not approved'}</td>
						</tr>	
					</tbody>
				</table>
				
				<c:url var="userEditUrl" value="/user/edit">
					<c:param name="id">${user.getId()}</c:param>
				</c:url>
				<a href="${userEditUrl}" class="btn btn-primary w-100 mt-4 ${role eq 'ADMIN' ? '' : 'd-none'}">
					<i class="fa-solid fa-pen-to-square"></i>
					Edit
				</a>
				
				<c:url var="cancelUrl" value="/user/search"></c:url>
				<a href="${cancelUrl}" class="btn btn-secondary w-100 mt-2">
					<i class="fa-solid fa-xmark me-2"></i>
					Close
				</a>
			</div>
		</div>
	</main>

</body>
</html>
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
	
	<div class="container my-4">
		<c:url var="searchUserUrl" value="/user/search"></c:url>
		<form id="searchForm" action="${searchUserUrl}" class="row mt-3">
			<div class="col-auto">
				<div class="input-group">
					<input class="form-control" type="text" name="keyword" value="${param.keyword}" placeholder="Search User" />
					<c:url var="clearSearchUrl" value="/user/search"></c:url>
					<a href="${clearSearchUrl}" class="input-group-text">
						<i class="fa-solid fa-delete-left"></i>
					</a>
					<button class="btn btn-primary" type="submit">
						<i class="fa-solid fa-magnifying-glass"></i>
					</button>
				</div>
			</div>
			
			<div class="col-auto ${role eq 'ADMIN' ? '' : 'd-none'}">
				<c:url var="addNewUserUrl" value="/user/edit"></c:url>
				<a href="${addNewUserUrl}" class="btn btn-success">
					<i class="fa-solid fa-plus"></i>
					Add New
				</a>
			</div>
			
			<div class="col-auto ${role eq 'ADMIN' ? '' : 'd-none'}">
				<c:url var="userRequestUrl" value="/user/search/request">
				</c:url>
				<a href="${userRequestUrl}" class="btn btn-primary">
					<i class="fa-solid fa-user-plus"></i>
					Requests
					<c:if test="${userRequestCount gt 0}">
						<span class="badge bg-danger">${userRequestCount}</span>
					</c:if>
				</a>
			</div>
			
		</form>
		
		<jsp:include page="/views/alert.jsp"></jsp:include>
		
		<table class="table table-striped align-middle mt-4">
			<thead>
				<tr>
					<th>#</th>
					<th>ID</th>
					<th>Name</th>
					<th>Role</th>
					<th>Email</th>
					<th class="text-center">Actions</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="u" items="${users}" varStatus="status">
					<tr>
						<td class="text-nowrap">${status.getCount()}</td>
						<td class="text-nowrap">${u.getId()}</td>
						<td class="text-nowrap">${u.getName()}</td>
						<td class="text-nowrap">
							<c:choose>
								<c:when test="${u.getRole() eq 'ADMIN'}">
									<i title="${u.getRole()}" class="fa-solid fa-user-shield fs-5 text-success"></i>
								</c:when>
								<c:when test="${u.getRole() eq 'USER'}">
									<i title="${u.getRole()}" class="fa-solid fa-user fs-5 text-secondary"></i>
								</c:when>
							</c:choose>
						</td>
						<td class="text-nowrap">${u.getEmail()}</td>
						<td class="text-center text-nowrap">
							<c:url var="editUserUrl" value="/user/edit">
								<c:param name="id">${u.getId()}</c:param>
							</c:url>
							<a href="${editUserUrl}" class="btn btn-primary ${role eq 'ADMIN' ? '' : 'd-none'}">
								<i class="fa-solid fa-pen-to-square"></i>
							</a>
							
							<c:url var="userDetailUrl" value="/user/detail">
								<c:param name="id">${u.getId()}</c:param>
							</c:url>
							<a href="${userDetailUrl}" class="btn btn-secondary">
								<i class="fa-solid fa-circle-info"></i>
							</a>
							
							<c:url var="deleteUserUrl" value="/user/delete">
								<c:param name="id">${u.getId()}</c:param>
							</c:url>
							<form class="d-inline-block  ${role eq 'ADMIN' ? '' : 'd-none'}" action="${deleteUserUrl}" method="post">
								<button class="btn btn-danger" onclick="return confirm('Are you sure to delete this user.')">
									<i class="fa-solid fa-trash"></i>
								</button>
							</form>
						</td>
					</tr>
				</c:forEach>
				
			</tbody>
		</table>
	</div>

</body>
</html>
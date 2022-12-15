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
	
	<div class="container my-4">
		<c:url var="searchCourseUrl" value="/course/search"></c:url>
		<form action="${searchCourseUrl}"  class="row mt-3">
			<div class="col-auto">
				<div class="input-group">
					<input class="form-control" type="text" name="keyword" value="${param.keyword}" placeholder="Search Course" />
					<button class="col-auto btn btn-primary" type="submit">
						<i class="fa-solid fa-magnifying-glass"></i>
					</button>
				</div>
			</div>
			
			<div class="col-auto ${role eq 'ADMIN' ? '' : 'd-none'}">
				<c:url var="addNewCourseUrl" value="/course/edit"></c:url>
				<a href="${addNewCourseUrl}" class="btn btn-success">
					<i class="fa-solid fa-plus"></i>
					Add New
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
					<th>Short Name</th>
					<th class="text-center">Actions</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="c" items="${courses}" varStatus="status">
					<tr>
						<td class="text-nowrap">${status.getCount()}</td>
						<td class="text-nowrap">${c.getId()}</td>
						<td>${c.getName()}</td>
						<td class="text-nowrap">${c.getShortName()}</td>
						<td class="text-center text-nowrap">
							<c:url var="editCourseUrl" value="/course/edit">
								<c:param name="id">${c.getId()}</c:param>
							</c:url>
							<a href="${editCourseUrl}" class="btn btn-primary ${role eq 'ADMIN' ? '' : 'd-none'}">
								<i class="fa-solid fa-pen-to-square"></i>
							</a>
							
							<c:url var="courseDetailUrl" value="/course/detail">
								<c:param name="id">${c.getId()}</c:param>
							</c:url>
							<a href="${courseDetailUrl}" class="btn btn-secondary">
								<i class="fa-solid fa-circle-info"></i>
							</a>
							
							<c:url var="deleteCourseUrl" value="/course/delete">
								<c:param name="id">${c.getId()}</c:param>
							</c:url>
							<form class="d-inline-block ${role eq 'ADMIN' ? '' : 'd-none'}" action="${deleteCourseUrl}" method="post">
								<button type="submit" class="btn btn-danger" onclick="return confirm('Are you sure to delete this course.')">
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
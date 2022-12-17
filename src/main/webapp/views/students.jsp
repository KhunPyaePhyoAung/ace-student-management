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
	<c:set var="menu" value="students" scope="request"></c:set>
	<jsp:include page="/views/nav-bar.jsp"></jsp:include>
	
	<div class="container my-4">
		<c:url var="searchStudentUrl" value="/student/search"></c:url>
		<form action="${searchStudentUrl}" class="row mt-3">
			<div class="col-auto">
				<div class="input-group">
					<input class="form-control" type="text" name="studentKeyword" value="${param.studentKeyword}" placeholder="Search Student" />
					<input class="form-control" type="text" name="courseKeyword" value="${param.courseKeyword}" placeholder="Search Course" />
					<c:url var="clearSearchUrl" value="/student/search"></c:url>
					<a href="${clearSearchUrl}" class="input-group-text">
						<i class="fa-solid fa-delete-left"></i>
					</a>
					<button class="col-auto btn btn-primary" type="submit">
						<i class="fa-solid fa-magnifying-glass"></i>
					</button>
				</div>
			</div>
			
			<div class="col-auto ${role eq 'ADMIN' ? '' : 'd-none'}">
				<c:url var="addNewStudentUrl" value="/student/edit"></c:url>
				<a href="${addNewStudentUrl}" class="btn btn-success">
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
					<th>Gender</th>
					<th>Course</th>
					<th class="text-center">Actions</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="s" items="${students}" varStatus="status">
					<tr>
						<td class="text-nowrap">${status.getCount()}</td>
						<td class="text-nowrap">${s.getId()}</td>
						<td class="text-nowrap">${s.getName()}</td>
						<td class="text-nowrap">
							<c:choose>
								<c:when test="${s.getGender() eq 'MALE'}">
									<i title="${s.getGender()}" class="fa-solid fa-mars text-success fs-5"></i>
								</c:when>
								<c:when test="${s.getGender() eq 'FEMALE'}">
									<i title="${s.getGender()}" class="fa-solid fa-venus text-danger fs-5"></i>
								</c:when>
							</c:choose>
						</td>
						<td>
							<c:forEach var="c" items="${s.getCourses()}">
								<c:url var="courseDetailUrl" value="/course/detail">
									<c:param name="id">${c.getId()}</c:param>
								</c:url>
								<a class="badge bg-secondary text-light" href="${courseDetailUrl}" title="${c.getId()} : ${c.getName()}">${c.getShortName()}</a>
								
							</c:forEach>
						</td>
						<td class="text-center text-nowrap">
							<c:url var="editStudentUrl" value="/student/edit">
								<c:param name="id">${s.getId()}</c:param>
							</c:url>
							<a href="${editStudentUrl}" class="btn btn-primary ${role eq 'ADMIN' ? '' : 'd-none'}">
								<i class="fa-solid fa-pen-to-square"></i>
							</a>
							
							<c:url var="studentDetailUrl" value="/student/detail">
								<c:param name="id">${s.getId()}</c:param>
							</c:url>
							<a href="${studentDetailUrl}" class="btn btn-secondary">
								<i class="fa-solid fa-circle-info"></i>
							</a>
							
							<c:url var="deleteStudentUrl" value="/student/delete">
								<c:param name="id">${s.getId()}</c:param>
							</c:url>
							<form class="d-inline-block ${role eq 'ADMIN' ? '' : 'd-none'}" action="${deleteStudentUrl}" method="post">
								<button type="submit" class="btn btn-danger" onclick="return confirm('Are you sure to delete this student.')">
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
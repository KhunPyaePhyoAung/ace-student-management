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
	<c:set var="menu" value="users" scope="request"></c:set>
	<jsp:include page="/views/nav-bar.jsp"></jsp:include>
	
	<div class="container my-4">
		<div class="row">
			<div class="col-6 offset-3">
				<span class="fs-4 fw-bold">${title}</span>
				<c:if test="${not empty userFormErrorMessage}">
					<div class="mt-4">
					<label class="text-danger">${userFormErrorMessage}</label>
				</div>
				</c:if>
				
				<c:url var="userSaveUrl" value="/user/save"></c:url>
				<form class="mt-4" action="${userSaveUrl}" method="post">
					<input type="hidden" name="id" value="${user.getId()}" />
					<div class="form-group">
						<label for="name" class="form-label">Name</label>
						<input id="name" class="form-control" type="text" name="name" value="${user.getName()}"/>
						<cus:invalidFieldMessage exception="${invalidFieldException}" field="name" cssClass="text-danger"/>
					</div>
					<div class="form-group mt-3">
						<label for="role" class="form-label">Role</label>
						<select name="role" id="role" class="form-select">
							<option value="">-- select role --</option>
							<option value="ADMIN" ${user.getRole() eq 'ADMIN' ? 'selected' : ''}>Admin</option>
							<option value="USER" ${user.getRole() eq 'USER' ? 'selected' : ''}>User</option>
						</select>
						<cus:invalidFieldMessage exception="${invalidFieldException}" field="role" cssClass="text-danger"/>
					</div>
					<div class="form-group mt-3">
						<label for="email" class="form-label">Email</label>
						<input id="email" class="form-control" type="email" name="email" value="${user.getEmail()}"/>
						<cus:invalidFieldMessage exception="${invalidFieldException}" field="email" cssClass="text-danger"/>
					</div>
					<div class="form-group mt-3">
						<label for="password" class="form-label">Password</label>
						<input id="password" class="form-control" type="password" name="password" value="${user.getPassword()}"/>
						<cus:invalidFieldMessage exception="${invalidFieldException}" field="password" cssClass="text-danger"/>
					</div>
					<div class="form-group mt-3">
						<label for="confirmPassword" class="form-label">Confirm Password</label>
						<input id="confirmPassword" class="form-control" type="password" name="confirmPassword" value="${user.getConfirmPassword()}"/>
						<cus:invalidFieldMessage exception="${invalidFieldException}" field="confirmPassword" cssClass="text-danger"/>
					</div>
					
					<button class="form-control btn btn-primary mt-4" type="submit">
						<i class="fa-solid fa-floppy-disk me-2"></i>
						Save
					</button>
					
					<c:url var="cancelUrl" value="/user/search"></c:url>
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
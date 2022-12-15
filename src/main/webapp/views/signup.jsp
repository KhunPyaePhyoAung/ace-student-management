<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cus" uri="/WEB-INF/custom-tag.tld" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sign Up</title>
<jsp:include page="/views/resources.jsp"></jsp:include>
</head>
<body class="bg-primary">

	<div class="container my-5">
		<div class="row">
			<c:url var="signupUrl" value="/signup"></c:url>
			<form action="${signupUrl}" class="col-4 offset-4 card p-3" method="post">
				<span class="card-title fs-3 fw-bold text-center">Sign Up</span>
				
				<div class="card-body">
					<c:if test="${not empty signupFormErrorMessage}">
						<label class="text-danger mb-3">${signupFormErrorMessage}</label>
					</c:if>
					<div class="form-group">
						<label for="name" class="form-label">Name</label>
						<input id="name" type="text" class="form-control" name="name" value="${user.getName()}"/>
						<cus:invalidFieldMessage exception="${invalidFieldException}" field="name" cssClass="text-danger"/>
					</div>
					<div class="form-group mt-3">
						<label for="email" class="form-label">Email</label>
						<input id="email" type="email" class="form-control" name="email" value="${user.getEmail()}"/>
						<cus:invalidFieldMessage exception="${invalidFieldException}" field="email" cssClass="text-danger"/>
					</div>
					<div class="form-group mt-3">
						<label for="password" class="form-label">Password</label>
						<input id="password" type="password" class="form-control" name="password" value="${user.getPassword()}"/>
						<cus:invalidFieldMessage exception="${invalidFieldException}" field="password" cssClass="text-danger"/>
					</div>
					<div class="form-group mt-3">
						<label for="confirmPassword" class="form-label">Confirm Password</label>
						<input id="confirmPassword" type="password" class="form-control" name="confirmPassword" value="${user.getConfirmPassword()}"/>
						<cus:invalidFieldMessage exception="${invalidFieldException}" field="confirmPassword" cssClass="text-danger"/>
					</div>
					<button class="btn btn-primary form-control mt-4" type="submit">
						SIGNUP
					</button>
					<div class="text-center mt-4">
						<c:url var="loginUrl" value="/login"></c:url>
						<a href="${loginUrl}">Login</a>
					</div>
				</div>
				
			</form>
		</div>
	</div>
</body>
</html>
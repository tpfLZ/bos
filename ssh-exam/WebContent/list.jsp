<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<table border="1">
		<tr>
			<td>编号</td>
			<td>姓名</td>
			<td>密码</td>
			<td>昵称</td>
		</tr>
		<c:forEach items="${ requestScope.user }" var="user">
			<tr>
				<td>${user.uid }</td>
				<td>${user.username }</td>
				<td>${user.password }</td>
				<td>${user.nickname }</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>
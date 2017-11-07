<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">

<!-- 可选的Bootstrap主题文件（一般不用引入） -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/bootstrap/css/bootstrap-theme.min.css">

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script
	src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<title>Insert title here</title>

<style type="text/css">
body {
	padding-top: 40px;
	padding-bottom: 40px;
	background-color: #eee;
}

.bg {
	max-width: 530px;
	padding: 15px;
	margin: 0 auto;
}
</style>
<title>添加客户信息</title>
</head>
<body>

	<form role="form" class="bg"
		action="${pageContext.request.contextPath}/addCustomer"
		method="post" enctype="multipart/form-data">
		<div class="form-group">
			<label for="exampleInputEmail1">Customer Name</label> <input
				type="text" class="form-control" name="cusName"
				placeholder="Enter Customer Name">
		</div>
		<div class="form-group">
			<label for="exampleInputPassword1">Customer Phone</label> <input
				type="text" class="form-control" name="cusPhone"
				placeholder="Enter Customer Phone">
		</div>
		<div class="form-group">
			<label for="exampleInputFile">Customer Image</label> 
			<input
				type="file" name="cusImg">
		</div>

		<button type="submit" class="btn btn-default">Submit</button>
	</form>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">

<!-- 可选的Bootstrap主题文件（一般不用引入） -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/bootstrap/css/bootstrap-theme.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/jquery.pagination.css" />
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script
	src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<script
	src="${pageContext.request.contextPath}/js/jquery.pagination.min.js"></script>

<script type="text/javascript">
	function delCustomer(customerId){
		location.href = "${pageContext.request.contextPath}/delCustomer?id="+customerId;
	}
	
	var pageNum=1;
	var currentCount=5;
	
	function findOrder(customerId,pageNum){
		$.post("${pageContext.request.contextPath}/findOrder",{"customerId":customerId,"pageNum":pageNum,"currentCount":currentCount},function(data){
			var html= "";
			var dc = data.currentContent;
			for (var i = 0; i < dc.length; i++) {
				html += "<tr><td>"+dc[i].orderNum+"</td><td>"+dc[i].receiveInfo+"</td><td>"+dc[i].price+"</td><td>"+dc[i].customer.cusName+"</td><td>删除</td></tr>";
			}
			$("#msg").html(html);
			
			$("#pagination3").pagination({
				currentPage: data.pageNum,//当前页
				totalPage: data.totalPage,//一共有多少页
				isShow: true,
				count: data.totalCount ,//一共有多少条数据
				homePageText: "首页",
				endPageText: "尾页",
				prevPageText: "上一页",
				nextPageText: "下一页",
				callback: function(current) {
					findOrder(customerId,current);//点击下一页时继续调用本函数
				}
			});

		},"json");
	}
	</script>
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
</head>
<body>
	<table class="table table-hover table-bordered bg">
		<tr>
			<td colspan="5"><a
				href="${pageContext.request.contextPath}/addCustomer.jsp"
				class="btn btn-primary btn-lg active" role="button">Add Customer</a></td>
		</tr>
		<tr>
			<td>序号</td>
			<td>客户</td>
			<td>客户名称</td>
			<td>联系电话</td>
			<td>操作</td>
		</tr>
		<s:iterator value="cs" var="c" status="vs">
			<tr>
				<td><s:property value="#vs.index+1" /></td>
				<td><img src="<s:property value='#c.cusImgsrc'/>"
					class="img-circle"></td>
				<td><s:property value="#c.cusName" /></td>
				<td><s:property value="#c.cusPhone" /></td>
				<td><a href="javascript:void(0)" class="btn btn-primary btn-sm"
					onclick="delCustomer(<s:property value="#c.id"/>)">删除客户</a> <a
					href="javascript:void(0)"
					onclick="findOrder(<s:property value="#c.id"/>,pageNum)"
					class="btn
						btn-primary btn-sm" data-toggle="modal"
					data-target="#myModal">订单详情</a></td>
			</tr>
		</s:iterator>
	</table>

	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">订单详情</h4>
				</div>
				<div class="modal-body">
					<table class="table table-bordered .table-hover">
						<tr>
							<td>订单编号</td>
							<td>收货地址</td>
							<td>订单价格</td>
							<td>客户名称</td>
							<td>操作</td>
						</tr>
						<tbody id="msg">

						</tbody>
					</table>
					<div class="box">
						<div id="pagination3" class="page fl"></div>
						<div class="info fl"></div>
					</div>
				</div>

				<div class="modal-footer">
					<nav id="page"> </nav>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
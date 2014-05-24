<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>message</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
<jsp:include page="/assets/commonpage/include-dwr-script.jsp" flush="true" />


<script type="text/javascript">
$(function(){
	$("#a").click(function(){

		PushMsg.sendMessage("这是一个测试消息","<%=basePath%>/message/receive", "showMessage");
	});
});


</script> 
</head>
<body>
这是消息发送页面
<input id="a" type="button" value="点我即可发送消息">
</body>
</html>
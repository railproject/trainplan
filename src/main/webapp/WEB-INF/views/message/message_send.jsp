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
<script type="text/javascript" src="<%=basePath%>/assets/oldAssets/js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/js/trainplan/common.security.js"></script>
<script type="text/javascript" src="<%=basePath%>/dwr/util.js"></script>
<script type="text/javascript" src="<%=basePath%>/dwr/engine.js"></script>
<script type="text/javascript" src="<%=basePath%>/dwr/interface/PushMsg.js"></script>
<script type="text/javascript">
dwr.engine.setActiveReverseAjax(true);//js中开启dwr推功能
dwr.engine.setNotifyServerOnPageUnload(true);//设置在页面关闭时，通知服务器销毁session
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
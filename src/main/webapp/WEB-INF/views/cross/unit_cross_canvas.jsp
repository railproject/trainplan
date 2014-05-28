<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getContextPath();
Object jlData =  request.getAttribute("myJlData");
Object grid =  request.getAttribute("gridData");


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>交路单元</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
</head>
<body>
<!--分栏框开始-->
<div class="row">
  <div style="float:left; width:100%;">
    <!--分栏框开始-->
    <div class="panel panel-default">
      <!--panle-heading-->
      <div class="panel-body">
        <div class="table-responsive" style="overflow: auto;">
        	<canvas id="unit_cross_canvas"></canvas>
        </div>
        
      </div>
      <!--panel-body--> 
    </div>
    <!--分栏框结束--> 
    <!--分栏框开始-->
    <!--分栏框结束--> 
  </div>
  
  
</div>



<script type="text/javascript">
var canvasData = {};
canvasData.grid = <%=grid%>;
canvasData.jlData = <%=jlData%>;//交路数据
</script>



<script src="<%=basePath %>/assets/js/trainplan/util/fishcomponent.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.util.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.component.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/cross/unit_cross_canvas.js"></script>

</body>
</html>

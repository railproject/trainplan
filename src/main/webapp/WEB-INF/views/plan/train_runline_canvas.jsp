<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%

String basePath = request.getContextPath();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>列车运行线图</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
</head>
<body>
<input id="input_hidden_planTrainId" type="hidden" value="${planTrainId}"/>

<!--分栏框开始-->
<div class="row">
  <div style="float:left; width:100%;">
    <!--分栏框开始-->
    <div class="panel panel-default">
      <!--panle-heading-->
      <div class="panel-body">
    
        <div class="table-responsive" >
        	<canvas id="div_canvas"></canvas>
        </div>
        
      </div>
      <!--panel-body--> 
    </div>
    <!--分栏框结束--> 
    <!--分栏框开始-->
    <!--分栏框结束--> 
  </div>
  
  
</div>





<script src="<%=basePath %>/assets/js/trainplan/util/fishcomponent.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.util.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.component.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/plan/train_runline_canvas.js"></script>





</body>
</html>
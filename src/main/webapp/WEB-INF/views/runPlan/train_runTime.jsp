<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<% 
String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
</head>
<body class="Iframe_body">
<input id="trainRunTime_trainPlanId_hidden" type="hidden" value="${trainPlanId}">
<input id="trainRunTime_trainNbr_hidden" type="hidden" value="${trainNbr}">
<div id="div_form" class="row" style="padding-top:10px;padding-bottom:10px;">
  <form class="form-horizontal" role="form">
    <div class="form-group" style="float:left;margin-left:20px;margin-bottom:0;">
      <label for="exampleInputEmail2" class="control-label pull-left" data-bind="html: currentTrainInfoMessage()"></label>
    </div>
    <a type="button" href="#" class="btn btn-success btn-xs" data-bind="click: saveTrainTime" style="float:right;margin-right:20px;margin-bottom:0;"><i class="fa fa-search"></i>保存</a>
  </form>
</div>


<div class="row">
    <div class="panel panel-default">
      
      <div class="panel-body">
        <div class="table-responsive table-hover" style="overflow:auto">
          <table id="table_run_plan_train_times_edit" class="table table-bordered table-striped table-hover">
            <thead>
		        <tr>
		          <th style="width:40px">序号</th>
                  <th style="width:200px">站名</th>
                  <th style="width:70px">路局</th>
                  <th style="width:200px">到达时间</th>
                  <th style="width:200px">出发时间</th>
                  <th style="width:80px">停留时间</th>
                  <th style="width:80px">天数</th> 
                  <th style="width:80px" colspan="2">股道</th>  
                 </tr>
            </thead>
            <tbody data-bind="foreach: trainStns">
	           <tr data-bind="attr:{class : isChangeValue()==1? 'success':''}">
				<td style="width:40px" align="center" data-bind=" text: $index() + 1"></td>
				<td style="width:200px" data-bind="text: stnName, attr:{title: stnName}"></td>
				<td style="width:70px" align="center" data-bind="text: bureauShortName"></td>
				<td style="width:180px" align="center"><input  data-bind="value: arrTime, event:{change: onChange}"/></td>
				<td style="width:180px" align="center"><input  data-bind="value: dptTime, event:{change: onChange}"/></td>
				<td style="width:80px" align="center" data-bind="text: stepStr"></td>
				<td style="width:80px" align="center" data-bind="text: runDays"></td>
				<td style="width:80px" align="center"><input  data-bind="value: trackName, event:{change: onChange}"/></td>
	        	</tr>
	        </tbody>
          </table>
        </div>
      </div>
    </div>
	
</div>












<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/runPlan/train_runTime.js"></script>
</body>
</html>
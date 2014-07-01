<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<% 
String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>高铁乘务计划查询</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
</head>
<body class="Iframe_body">
<ol class="breadcrumb">
  <span><i class="fa fa-anchor"></i>当前位置：</span>
  <li><a href="<%=basePath %>/crew/page/sj">乘务计划->高铁乘务计划查询</a></li>
</ol>
<!--以上为必须要的--> 

<div class="row" style="padding-top:10px;padding-bottom:10px;">
  <form class="form-horizontal" role="form">
    <div class="form-group" style="float:left;margin-left:20px;margin-bottom:0;">
      <label for="exampleInputEmail2" class="control-label pull-left">计划日期:&nbsp;</label>
      <div class="pull-left">
        <input id="crew_input_rundate" type="text" class="form-control" style="width:100px;" placeholder="" data-bind="value: searchModle().runDate">
      </div>
    </div>
    <div class="form-group" style="float:left;margin-left:30px;margin-bottom:0;">
      <label for="exampleInputEmail2" class="control-label pull-left"> 车次:&nbsp;</label>
      <div class="pull-left">
        <input id="crew_input_trainNbr" type="text" class="form-control" style="width:100px;" data-bind="value: searchModle().trainNbr">
      </div>
    </div>
    <a type="button" href="#" class="btn btn-success" data-bind="click : queryList" style="float:left;margin-left:20px;margin-bottom:0;"><i class="fa fa-search"></i>查询</a>
    <button type="button" class="btn btn-success" data-bind="click : exportExcel" style="float:left;margin-left:5px;margin-bottom:0;"><i class="fa fa-sign-out"></i>导出EXCEL</button>
  </form>
</div>



<!--左右分开-->
<div class="row">
	  
  
  <!--乘务计划-->
  <div style="float:left; width:100%;">
    <!--分栏框开始-->
    <div class="panel panel-default">
      
      <div class="panel-body">
        <div class="table-responsive table-hover">
          <table class="table table-bordered table-striped table-hover">
            <thead>
              <tr>
                <th rowspan="2" class="text-center" style="vertical-align: middle;width:40px;">序号</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle">乘务交路</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle">车队组号</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle">经由铁路线</th>
                <th colspan="3" class="text-center" style="vertical-align: middle">司机1</th>
                <th colspan="3" class="text-center" style="vertical-align: middle">司机2</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle;width:80px">提交<br>状态</th>
              </tr>
              <tr>
                <th class="text-center">姓名</th>
                <th class="text-center">电话</th>
                <th class="text-center">政治面貌</th>
                <th class="text-center">姓名</th>
                <th class="text-center">电话</th>
                <th class="text-center">政治面貌</th>
              </tr>
            </thead>
            <tbody data-bind="foreach: hightLineCrewRows.rows">
              <tr>
                <td data-bind=" text: ($index() + 1)"></td>
                <td data-bind=" text: crewCross, attr:{title: crewCross}"></td>
                <td data-bind=" text: crewGroup, attr:{title: crewGroup}"></td>
                <td data-bind=" text: throughLine, attr:{title: throughLine}"></td>
                <td data-bind=" text: name1, attr:{title: name1}"></td>
                <td data-bind=" text: tel1, attr:{title: tel1}"></td>
                <td data-bind=" text: identity1, attr:{title: identity1}"></td>
                <td data-bind=" text: name2, attr:{title: name2}"></td>
                <td data-bind=" text: tel2, attr:{title: tel2}"></td>
                <td data-bind=" text: identity2, attr:{title: identity2}"></td>
                <td data-bind="html : submitTypeStr"></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <!--panel-body--> 
      
    </div>
    
    <!--分栏框结束--> 
  </div>
  <!--乘务计划end--> 




  
  
  
  
  
  
  
  

  
</div>
<!--左右分开--> 



















<script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script> 
<script type="text/javascript"  src="<%=basePath %>/assets/js/trainplan/knockout.pagemodle.js"></script> 
<script type="text/javascript"  src="<%=basePath %>/assets/js/trainplan/hightlineCrew/hightline.crew.all.js"></script> 
</body>
</html>
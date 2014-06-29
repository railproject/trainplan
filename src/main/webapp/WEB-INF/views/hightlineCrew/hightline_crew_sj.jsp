<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<% 
String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>司机乘务计划上报</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
</head>
<body class="Iframe_body">
<ol class="breadcrumb">
  <span><i class="fa fa-anchor"></i>当前位置：</span>
  <li><a href="<%=basePath %>/crew/page/sj">乘务计划->司机乘务计划</a></li>
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
    <a type="button" href="#" class="btn btn-success" data-bind="click : sendCrew" style="float:left;margin-left:5px;margin-bottom:0;"><i class="fa fa-external-link"></i>提交</a>
  </form>
</div>



<!--左右分开-->
<div class="row"> 
  <!--所有租户-->
  <div class="col-md-3 col-sm-3 col-xs-3" style="padding:0;"> 
    <!--分栏框开始-->
    <div class="panel panel-default">
      <div class="panel-heading" >
        <h3 class="panel-title" > <i class="fa fa-user-md"></i>列车开行计划</h3>
      </div>
      <!--panle-heading-->
      <div class="panel-body" style="padding:5px 5px;">
        <div class="table-responsive table-hover">
          <table class="table table-bordered table-striped table-hover">
            <thead>
              <tr>
                <th style="width:10%;">序号</th>
                <th>车次</th>
                <th>始发站</th>
                <th>始发时间</th>
                <th>终到站</th>
                <th>终到时间</th>
              </tr>
            </thead>
            <tbody data-bind="foreach: planTrainRows.rows">
              <tr class="success">
                <td data-bind=" text: ($index() + 1)"></td>
                <td data-bind=" text: trainNbr, attr:{title: trainNbr}"></td>
                <td data-bind=" text: startStn, attr:{title: startStn}"></td>
                <td data-bind=" text: startTimeStr, attr:{title: startTimeStr}"></td>
                <td data-bind=" text: endStn, attr:{title: endStn}"></td>
                <td data-bind=" text: endTimeStr, attr:{title: endTimeStr}"></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <!--panel-body--> 
      
    </div>
    
    <!--分栏框结束--> 
  </div>
  <!--所有租户--> 
  
  
  
  
  
  
  
  
  
  
  <!--乘务计划-->
  <div class="col-md-9 col-sm-9 col-xs-9 pull-right"> 
    <!--分栏框开始-->
    <div class="panel panel-default">
      <div class="panel-heading" >
        <h3 class="panel-title" > <i class="fa fa-user-md"></i>乘务计划</h3>
        <!--        <div class="col-md-8 col-sm-6 col-xs-4  pull-right" style=" width: 10%; text-align:right;">  <a  class="panel-title" href="application-Status.html" >返回</a></div>--> 
      </div>
      <!--panle-heading-->
      
      <div class="panel-body">
        <div class="row" style="margin-bottom:10px;">
          <button type="button" class="btn btn-success" data-toggle="modal" data-bind="click : onAddOpen" data-target="#createHightLineCrewModal"><i class="fa fa-plus"></i>新增</button>
          <button type="button" class="btn btn-success" data-toggle="modal" data-bind="click : onEditOpen" data-target="#updateHightLineCrewModal"><i class="fa fa-pencil-square-o"></i> 编辑</button>
          <button type="button" class="btn btn-success"  data-bind="click : deleteHightLineCrew"><i class="fa fa-minus-square"></i>移除</button>
        </div>
        <div class="table-responsive table-hover">
          <table class="table table-bordered table-striped table-hover">
            <thead>
              <tr>
                <th style="width:5%;"><input type="checkbox"></th>
                <th style="width:10%;">序号</th>
                <th>乘务交路</th>
                <th>车队组号</th>
                <th>经由铁路线</th>
                <th>司机1姓名</th>
                <th>电话</th>
                <th>政治面貌</th>
                <th>司机2姓名</th>
                <th>电话</th>
                <th>政治面貌</th>
                <th>状态</th>
              </tr>
            </thead>
            <tbody data-bind="foreach: hightLineCrewSjRows.rows">
              <tr data-bind="attr:{class : submitType==1? 'success':''}">
              	<td><input name="crew_checkbox" type="checkbox" data-bind="value : crewHighlineId"></td>
                <td data-bind=" text: ($index() + 1)"></td>
                <td data-bind=" text: crewCross"></td>
                <td data-bind=" text: crewGroup"></td>
                <td data-bind=" text: throughLine"></td>
                <td data-bind=" text: name1"></td>
                <td data-bind=" text: tel1"></td>
                <td data-bind=" text: identity1"></td>
                <td data-bind=" text: name2"></td>
                <td data-bind=" text: tel2"></td>
                <td data-bind=" text: identity2"></td>
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
  <!--用户管理--> 
  
</div>
<!--左右分开--> 













<!--新增弹出框-->
<div class="modal fade" id="createHightLineCrewModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title">新增司机乘务计划</h4>
      </div>
      
      <!--panel-heading-->
      <div class="panel-body row">
        <form id="hightLineCrewForm" class="bs-example form-horizontal" style="margin-top:10px;">
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">乘务交路：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_crewCross" type="text" class="form-control">
            </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">车队组号：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_crewGroup" type="text" class="form-control">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">经由铁路线：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_throughLine" type="text" class="form-control">
               </div>
          </div>
          
          
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">司机1姓名：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_name1" type="text" class="form-control">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">电话：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_tel1" type="text" class="form-control">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">政治面貌：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_identity1" type="text" class="form-control">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">司机2姓名：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_name2" type="text" class="form-control">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">电话：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_tel2" type="text" class="form-control">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">政治面貌：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_identity2" type="text" class="form-control">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">备注：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <textarea id="add_note" class="form-control" rows="4"></textarea>
            </div>
          </div>
        </form>
        <!--        <p class="pull-right" style="margin:0;">说明：当您申请后需要等待管理员审批才能使用。</p>
--> </div>
      <!--panel-body-->
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" data-bind="click : addHightLineCrew" data-dismiss="modal">确定</button>
        <button type="reset" class="btn btn-info">重置</button>
        <button type="button" class="btn btn-warning" data-dismiss="modal">取消</button>
      </div>
    </div>
    <!-- /.modal-content --> 
  </div>
  <!-- /.modal-dialog --> 
</div>
<!--新增弹出框 end-->




<!--修改弹出框-->
<div class="modal fade" id="updateHightLineCrewModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title">修改司机乘务计划</h4>
      </div>
      
      <!--panel-heading-->
      <div class="panel-body row">
        <form id="hightLineCrewForm" class="bs-example form-horizontal" style="margin-top:10px;" data-bind="with : hightLineCrewModel">
          <input id="update_crewHighlineId" type="hidden" data-bind="value : crewHighlineId">
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">乘务交路：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="update_crewCross" type="text" class="form-control" data-bind="value : crewCross">
            </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">车队组号：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="update_crewGroup" type="text" class="form-control" data-bind="value : crewGroup">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">经由铁路线：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="update_throughLine" type="text" class="form-control" data-bind="value : throughLine">
               </div>
          </div>
          
          
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">司机1姓名：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="update_name1" type="text" class="form-control" data-bind="value : name1">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">电话：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="update_tel1" type="text" class="form-control" data-bind="value : tel1">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">政治面貌：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="update_identity1" type="text" class="form-control" data-bind="value : identity1">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">司机2姓名：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="update_name2" type="text" class="form-control" data-bind="value : name2">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">电话：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="update_tel2" type="text" class="form-control" data-bind="value : tel2">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">政治面貌：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="update_identity2" type="text" class="form-control" data-bind="value : identity2">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">备注：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <textarea id="update_note" class="form-control" rows="4" data-bind="value : note"></textarea>
            </div>
          </div>
        </form>
        <!--        <p class="pull-right" style="margin:0;">说明：当您申请后需要等待管理员审批才能使用。</p>
--> </div>
      <!--panel-body-->
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" data-bind="click : updateHightLineCrew" data-dismiss="modal">确定</button>
        <button type="reset" class="btn btn-info">重置</button>
        <button type="button" class="btn btn-warning" data-dismiss="modal">取消</button>
      </div>
    </div>
    <!-- /.modal-content --> 
  </div>
  <!-- /.modal-dialog --> 
</div>
<!--修改弹出框 end--> 

<script src="<%=basePath %>/assets/js/trainplan/knockout.pagemodle.js"></script> 
<script src="<%=basePath %>/assets/js/trainplan/hightlineCrew/hightline.crew.sj.js"></script> 
</body>
</html>
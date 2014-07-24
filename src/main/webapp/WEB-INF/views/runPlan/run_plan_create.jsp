<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List" %>
<% 
ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();

boolean isZgsUser = false;	//当前用户是否为总公司用户
if (user!=null && user.getBureau()==null) {
	isZgsUser = true;
}
String currentUserBureau = user.getBureau();
List<String> permissionList = user.getPermissionList();
String userRolesString = "";
for(String p : permissionList){
	userRolesString += userRolesString.equals("") ? p : "," + p;
} 
String basePath = request.getContextPath();
%>
<!DOCTYPE HTML>
<html lang="en">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>生成开行计划</title>
<!-- Bootstrap core CSS -->

<!--font-awesome-->
<link href="<%=basePath %>/assets/css/datepicker.css" rel="stylesheet">
<link href="<%=basePath %>/assets/easyui/themes/default/easyui.css"
	rel="stylesheet">
<link href="<%=basePath %>/assets/easyui/themes/icon.css"
	rel="stylesheet">
<link href="<%=basePath %>/assets/css/cross/custom-bootstrap.css" rel="stylesheet">

<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/css/cross/custom-bootstrap.css">
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/css/font-awesome.min.css"/>
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/css/datepicker.css">
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/css/style.css"> 
	
<link href="<%=basePath %>/assets/easyui/themes/icon.css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="<%=basePath %>/assets/css/font-awesome.min.css" />
 
<!-- Custom styles for this template --> 
<link href="<%=basePath %>/assets/css/cross/cross.css" rel="stylesheet">  
<link href="<%=basePath %>/assets/css/style.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/css/rightmenu.css">



   
<jsp:include page="/assets/commonpage/include-dwr-script.jsp" flush="true" /> 
<script src="<%=basePath %>/assets/js/trainplan/knockout.pagemodle.js"></script> 
<script type="text/javascript">
var basePath = "<%=basePath %>";
var all_role = "<%=userRolesString %>";
var _isZgsUser = <%=isZgsUser%>;//当前用户是否为总公司用户
var currentUserBureau = "<%=currentUserBureau %>";
</script>
<!--#include virtual="assets/js/trainplan/knockout.pagefooter.tpl"-->
 <style type="text/css">
.pagination > li > a, .pagination > li > span {
	position: relative;
	float: left; 
	line-height: 1.428571429;
	text-decoration: none;
	background-color: #ffffff;
	border: 1px solid #dddddd;
	margin-left: -1px;
} 
.ckbox.disabled{
	cursor: not-allowed;
	pointer-events: none;
	opacity: 0.65;
	filter: alpha(opacity=65);
	-webkit-box-shadow: none;
	box-shadow: none;
}

 </style>

 
</head>
<body class="Iframe_body"  >
	
	<ol class="breadcrumb">
		<span><i class="fa fa-anchor"></i>当前位置:</span>
		<li><a href="#">生成开行计划</a></li>
	</ol>  
	   <div class="row" style="margin: 10px 10px 10px 10px;">  
	        <!--分栏框开始-->
		    <div class="pull-left" style="width: 100%;height:100%">
			<!--分栏框开始--> 
			      <div class="row" style="margin: 5px 10px 10px 10px;"> 
				        	<div class="panel-body">   
								<label for="exampleInputEmail3" class="control-label pull-left">
															方案:&nbsp;</label> 
								<div class="pull-left">
									<select style="width: 320px" id="input_cross_chart_id"
										class="form-control" data-bind="options:searchModle().charts, value: searchModle().chart, optionsText: 'name'">
									</select>
								</div>   	 
								<label for="exampleInputEmail3" class="control-label pull-left" >
									车辆担当局:</label>
								<div class="pull-left" style="margin-left: 5px;">
									<select style="width:55px" class="form-control" data-bind="options:searchModle().bureaus, value: searchModle().bureau, optionsText: 'shortName', optionsValue:'code', optionsCaption: '' ,event:{change: bureauChange}"></select>
								</div>
								<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 20px;">
									始发路局:</label>
								<div class="pull-left" style="margin-left: 5px; ">
									<select style="width: 50px" class="form-control" data-bind="options:searchModle().startBureaus, value: searchModle().startBureau, optionsText: 'shortName', optionsValue:'code', optionsCaption: ''"></select>
								</div>   
								    <label for="exampleInputEmail3" class="control-label pull-left">
										车次:&nbsp;</label>
									<div class="pull-left">
										<input type="text" class="form-control" style="width: 95px;"
									 		 id="input_cross_filter_trainNbr" data-bind=" value: searchModle().filterTrainNbr, event:{keyup: trainNbrChange}">
									</div>  	 
									<div class="pull-left" style="margin-left: 10px;">
											<a type="button" class="btn btn-success" data-toggle="modal"
												data-target="#" id="btn_cross_search"  data-bind="click: loadCrosses">查询</a> 
										</div>   
										
						</div>
					</div> 
				</div>
		</div>
										   
   	 	<div class="row"  style="margin: 10px 10px 10px 25px;" >  
   	 	  <div class="panel panel-default">   
	 	    <div id="learn-more-content" >
              <div class="panel-body"> 
				<!-- Tab panes --> 
	  		    <div id="plan_view_div_palnDayDetail" class="panel panel-default"> 
					      <!--panle-heading--> 
				  <div class="row" style="margin: 5px 10px 10px 10px;"> 
				    <label for="exampleInputEmail3" class="control-label pull-left" >
								开始日期:</label>
					<div class="pull-left" style="margin-left: 5px;">
						<input type="text" class="form-control" style="width:75px;" placeholder="" id="runplan_input_startDate"  name="startDate" data-bind="value: searchModle().planStartDate" />
					</div>
					<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 13px;">
						截至日期:</label>
					<div class="pull-left" style="margin-left: 5px; ">
						<input type="text" class="form-control" style="width:75px;" placeholder="" id="runplan_input_endDate"  name="endDate" data-bind="value: searchModle().planEndDate" />
					</div>
					<a  type="button" class="btn btn-success" data-toggle="modal" style="margin-left: 2px;" 
										data-target="#" id="btn_cross_createTrainLines" data-bind="click: createTrainLines">生成开行计划</a>
				</div>
				  <div class="row" style="margin: 5px 10px 10px 10px;" data-bind="html: completedMessage()">       
				  </div>
			      <div class="panel-body" style="bapadding:10px;overflow: auto">
			      	<div class="table-responsive" > 
			          <table class="table table-bordered table-striped table-hover" id="run_plan_table" data-bind="style:{width: runPlanTableWidth()}">
				      <thead>  
				      	<tr data-bind="template: { name: 'runPlanTableDateHeader', foreach: planDays }" ></tr>
				      	<tr data-bind="template: { name: 'runPlanTableWeekHeader', foreach: planDays }" ></tr> 
				      </thead> 
				      <tbody data-bind="template: { name: 'runPlanTableVlaues', foreach: trainPlans }"> 
				      </tbody> 
					  </table>
			        </div>  
			        </div>  
			      </div>
				</div>  
			</div>
		 </div>
	 </div>  
</body>  
 <script type="text/html" id="tablefooter-short-template"> 
  <table style="width:100%;height:20px;">
    <tr style="width:100%;height:20px;">
     <td style="width:60%;height:20px;">
  		<span class="pull-left">共<span data-bind="html: totalCount()"></span>条  当前<span data-bind="html: totalCount() > 0 ? (currentIndex() + 1) : '0'"></span>到<span data-bind="html: endIndex()"></span>条   共<span data-bind="text: pageCount()"></span>页</span> 								 
  	 </td>
     <td style="width:40%;height:20px;padding:0px;pading-bottom:-14">   
		<span data-bind="attr:{class:currentPage() == 0 ? 'disabed': ''}"><a style="cursor:pointer;background-color: #ffffff;border: 1px solid #dddddd;margin-right:-5px;padding:0px 5px;" data-bind="text:'<<', click: currentPage() == 0 ? null: loadPre"></a>
	    <input type="text"  style="padding-left:8px;margin-bottom:0px;padding-bottom:0;width:30px;height: 19px;background-color: #ffffff;border: 1px solid #dddddd;" data-bind="value: parseInt(currentPage())+1, event:{keyup: pageNbrChange}"/>
		<a style="cursor:pointer;background-color: #ffffff;border: 1px solid #dddddd;margin-left:-5px;padding:0px 5px;" data-bind="text:'>>', click: (currentPage() == pageCount()-1 || totalCount() == 0) ? null: loadNext"  style="padding:0px 5px;"></a>
       </ul> 
	 
     </td >
  </tr>
</table> 
</script> 


<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/html5.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/respond.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/runPlan/run_plan_create.js"></script>  
<script type="text/javascript" src="<%=basePath %>/assets/js/datepicker.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/common.js"></script> 
<script type="text/javascript" src="<%=basePath%>/assets/js/respond.min.js"></script>
<script src="<%=basePath %>/assets/js/moment.min.js"></script>
<script src="<%=basePath %>/assets/lib/fishcomponent.js"></script>
<%-- <script type="text/javascript" src="<%=basePath%>/assets/js/trainplan/common.security.js"></script> --%> 

<script src="<%=basePath %>/assets/js/trainplan/util/fishcomponent.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.util.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.component.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/runPlan/canvas_rightmenu.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/runPlan/canvas_event_getvalue.js"></script>
<script type="text/javascript">
var basePath = "<%=basePath %>";
</script>

<script  type="text/html" id="runPlanTableDateHeader"> 
    <!-- ko if: $index() == 0 -->
 	<td colspan="3" rowspan="2" style="width:80px" ><input type="checkbox" value="1" data-bind="event:{change: $root.selectCrosses}, checked: $root.crossAllcheckBox"> </td>
 	<!-- /ko --> 
 	<td align='center' data-bind="text: day" style="width:40px"></td> 
</script> 
<script  type="text/html" id="runPlanTableWeekHeader">  
 	<td align='center' data-bind="text: week"></td>
</script> 

<script  type="text/html" id="runPlanTableVlaues"> 
 <!-- ko if: trainSort == 0 --> 
 <tr data-bind="foreach: runPlans" >
    <!-- ko if: $index() == 0 --> 
    <td style="width:40px"><input type="checkbox" value="1" data-bind="event:{change: $root.selectCross.bind($data, $parent)},checked: $parent.selected"></td>
    <td data-bind="text: $parent.tokenVehBureauShowValue" style="width:40px"></td>
    <td data-bind="text: $parent.crossName + $parent.createStatusShowValue(), attr:{colspan: $parent.colspan} "></td> 
 	<!-- /ko -->   
 </tr> 
 <!-- /ko -->
 <!-- ko if: trainSort == 1 --> 
 <tr data-bind="foreach: runPlans">
    <!-- ko if: $index() == 0 --> 
    <td data-bind="text: '', attr:{rowspan: $parent.rowspan} " colspan="2"></td> 
    <td data-bind="text: $parent.trainNbr"></td>
 	<!-- /ko -->   
 	<td  align='center' data-bind="text: runFlagShowValue, style:{'color': color}"></td>
 </tr> 
 <!-- /ko -->
 <!-- ko if: trainSort > 1 --> 
 <tr data-bind="foreach: runPlans">
    <!-- ko if: $index() == 0 -->  
    <td data-bind="text: $parent.trainNbr"></td>
 	<!-- /ko -->   
 	<td  align='center' data-bind="text: runFlagShowValue, style:{'color': color}"></td>
 </tr> 
 <!-- /ko -->
</script>
</html>

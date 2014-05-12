<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>开行计划汇总</title>
<!-- Bootstrap core CSS -->
<link href="../../assets/css/custom-bootstrap.css" rel="stylesheet">
<!--font-awesome-->
<link href="../../assets/css/datepicker.css" rel="stylesheet">
<link  type="text/css" rel="stylesheet" href="../../assets/css/font-awesome.min.css"/>
<link  type="text/css" rel="stylesheet" href="../../assets/css/datepicker.css">
<!-- Custom styles for this template -->
<link href="../../assets/css/style.css" rel="stylesheet">
<script src="../../assets/js/jquery.js"></script>
<script src="../../assets/js/html5.js"></script>
<script src="../../assets/js/bootstrap.min.js"></script>
<script src="../../assets/js/respond.min.js"></script>
<script src="../../assets/js/jquery.dataTables.js"></script>
<script type="text/javascript">
$(function () {
	jQuery('#growl-success').click(function(){
		 jQuery.gritter.add({
			title: 'This is a regular notice!',
			text: 'This will fade out after a certain amount of time.',
	      class_name: 'growl-success',
	      image: '../../assets/img/screen.png',
			sticky: false,
			time: ''
		 });
		 return false;
	  });
	jQuery('#growl-warning').click(function(){
		 jQuery.gritter.add({
			title: 'This is a regular notice!',
			text: 'This will fade out after a certain amount of time.',
	      class_name: 'growl-warning',
	      image: '../../assets/img/screen.png',
			sticky: false,
			time: ''
		 });
		 return false;
	  });
	  jQuery('#growl-danger').click(function(){
		 jQuery.gritter.add({
			title: 'This is a regular notice!',
			text: 'This will fade out after a certain amount of time.',
	      class_name: 'growl-danger',
	      image: '../../assets/img/screen.png',
			sticky: false,
			time: ''
		 });
		 return false;
	  });
});


</script>

<!--<script src="../../assets/js/exporting.js"></script>-->
</head>
<body class="Iframe_body">
<!--以上为必须要的-->

<ol class="breadcrumb">
  <span><i class="fa fa-anchor"></i>当前位置：</span>
  <li><a href="#">开行计划汇总</a></li>
</ol>



<!--分栏框开始-->
<div class="row">
	<div class="panel panel-default">
      <!--panle-heading-->
        <div class="panel-body">
		    <div class="row" style="margin:-5px 0 10px 0;">
		      <form class="form-horizontal" role="form">
		        <button class="btn btn-primary" type="button" id="plan_review_all_btnQuery_ljtjxx">查询</button>
		        <div class="pull-left">
		          <div class="form-group" style="float:left;margin-left:20px;margin-bottom:0;">
		            <label for="exampleInputEmail2" class="control-label pull-left"> 日期:&nbsp;</label>
		            <div class="pull-left">
		              <input type="text" class="form-control" style="width:110px;" placeholder="" id="plan_review_all_selectdate">
		            </div>
		          </div>
		        </div>
		        <!--col-md-3 col-sm-4 col-xs-4-->
		      </form>
		    </div>
		    <div class="table-responsive">
		      <table class="table table-bordered table-striped table-hover" id="plan_review_all_table_tjxx">
		        <thead>
		          <tr>
		            <th rowspan="2"><div style="text-align:center;margin:-5px 0 10px 0;">序号</div></th>
		            <th rowspan="2"><div style="text-align:center;margin:-5px 0 10px 0;">路局简称</div></th>
		            <th rowspan="2"><div style="text-align:center;margin:-5px 0 10px 0;">始发合计</div></th>
		            <!-- <th rowspan="2"><div style="text-align:center;margin:-5px 0 10px 0;">始发线合计</div></th> -->
		            <th colspan="5"><div style="text-align:center;">始发（图定）</div></th>
		            <th colspan="5"><div style="text-align:center;">始发（临客）</div></th>
		            <th rowspan="2"><div style="text-align:center;margin:-5px 0 10px 0;">接入合计</div></th>
		            <th colspan="3"><div style="text-align:center;">接入（图定）</div></th>
		            <th colspan="3"><div style="text-align:center;">接入（临客）</div></th>
		          </tr>
		          <tr>
		            <th><div style="text-align:center;">小计</div></th>
		            <th><div style="text-align:center;">交出</div></th>
		            <th><div style="text-align:center;">交出线</div></th>
		            <th><div style="text-align:center;">终到</div></th>
		            <th><div style="text-align:center;">终到线</div></th>
		            
		            <th><div style="text-align:center;">小计</div></th>
		            <th><div style="text-align:center;">交出</div></th>
		            <th><div style="text-align:center;">交出线</div></th>
		            <th><div style="text-align:center;">终到</div></th>
		            <th><div style="text-align:center;">终到线</div></th>
		            
		            <th><div style="text-align:center;">小计</div></th>
		            <th><div style="text-align:center;">交出</div></th>
		            <th><div style="text-align:center;">终到</div></th>
		            <th><div style="text-align:center;">小计</div></th>
		            <th><div style="text-align:center;">交出</div></th>
		            <th><div style="text-align:center;">终到</div></th>
		          </tr>
		        </thead>
		        <tbody>
		        </tbody>
		      </table>
		    </div>
		  </div>
      <!--panel panel-default--> 
    </div>
</div>



<script src="../../assets/js/jquery.gritter.min.js"></script>
<script src="../../assets/js/datepicker.js"></script>
<script src="../../js/common.js"></script>
<script src="../../js/plan/plan_review_all.js"></script>
</body>
</html>
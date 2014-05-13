<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <title>审核次日客车运行线</title>
    <link type="text/css" href="assets/css/custom-bootstrap.css" rel="stylesheet"/>
    <link type="text/css" href="assets/css/font-awesome.min.css" rel="stylesheet"/>
    <link type="text/css" href="assets/css/minified/jquery-ui.min.css" rel="stylesheet"/>
    <link type="text/css" href="assets/css/style.css" rel="stylesheet"/>
    <script type="text/javascript" src="assets/js/jquery.js"></script>
    <script type="text/javascript" src="assets/js/html5.js"></script>
    <script type="text/javascript" src="assets/js/fuelUX.js"></script>
    <script type="text/javascript" src="assets/js/jquery.dataTables.js"></script>
    <script type="text/javascript" src="assets/js/jquery.knob.js"></script>
    <script type="text/javascript" src="assets/js/jquery.gritter.min.js"></script>
    <script type="text/javascript" src="assets/js/jquery.sparkline.min.js"></script>
    <script type="text/javascript" src="assets/js/jquery.freezeheader.js"></script>
    <script type="text/javascript" src="assets/js/minified/jquery-ui.min.js"></script>
    <script type="text/javascript" src="assets/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="assets/js/datepicker.js"></script>
    <script type="text/javascript" src="assets/js/purl.js"></script>
    <script type="text/javascript" src="assets/js/knockout.js"></script>
    <script type="text/javascript" src="assets/js/moment.min.js"></script>
    <script type="text/javascript" src="assets/js/trainplan/audit.js"></script>
    <style>
        #hdleft_table table tr th{
            border-color:#dfe4ee;
            text-align:center;
        }
    </style>
</head>
<body class="Iframe-body">
<ol class="breadcrumb">
    <span><i class="fa fa-anchor"></i>当前位置：</span>
    <li><a href="#">审核次日客车运行线</a></li>
</ol>
<section class="mainContent">
    <div class="row">
        <div class="col-xs-12 col-md-12 col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <i class="fa fa-home"></i>审核
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-xs-2 col-md-2 col-lg-2">
                            <form class="form-inline" role="form">
                                <div class="input-group">
                                    <input type="text" class="form-control" placeholder="请选择日期" id="date_selector"/>
                                </div>
                            </form>
                        </div>
                        <div class="col-xs-4 col-md-4 col-sm-4 col-lg-4">
                            <form class="form-inline" role="form">
                                <div class="input-group">
                                    <button href="#" class="btn btn-default margin-right-10" role="button">校验</button>
                                    <a id="compare" href="#" class="btn btn-default margin-right-10" role="button">图形对比</a>
                                    <button href="#" class="btn btn-default margin-right-10" role="button">生成运行线</button>
                                    <button href="#" class="btn btn-default margin-right-10" role="button">客调审核</button>
                                    <button href="#" class="btn btn-default margin-right-10 disabled" role="button">值班主任审核</button>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="row">
                        <div class="panel panel-default marginbottom0">
                            <div class="panel-body paddingbottom0">
                                <p>客运计划共709条，其中停运8条，热备52条，需下达709条。</p>
                                <p>已生成运行线709条，未生成0条，调整0条。</p>
                                <p>客调已审核709条，值班主任已审核12条。</p>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-6 col-md-6 col-lg-6">
                            <div class="panel panel-default">
                                <div class="panel-heading paddingbottom0">
                                    <i class="fa fa-home"></i>客运开行计划
                                </div>
                                <div class="panel-body paddingtop0 paddingleftright5">
                                    <div class="row">
                                        <div class="col-xs-12 col-md-12 col-lg-12 paddingleft0 paddingtop5">
                                            <form class="form-inline" role="form">
                                                <div class="input-group">
                                                    <label class="margin-right-10">时刻表:</label>
                                                    <input id="kyjh_time" class="checkbox-inline" style="margin: 0px 10px 0px 0px;" type="checkbox">
                                                    <label class="margin-right-10">经由:</label>
                                                    <input id="kyjh_routing" class="checkbox-inline" style="margin: 0px 10px 0px 0px;" type="checkbox">
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <dev class="col-xs-12 col-md-12 col-lg-12 paddingleft0 paddingtop5">
                                            <div class="table-responsive">
                                                <table id="left_table" class="table table-bordered table-striped table-hover tableradius">
                                                    <thead>
                                                    <tr>
                                                        <th class="text-center"><input class="checkbox-inline" type="checkbox"/></th>
                                                        <th class="text-center">序号</th>
                                                        <th class="text-center">车次</th>
                                                        <th class="text-center">始发站</th>
                                                        <th class="text-center">终到站</th>
                                                        <th class="text-center">始发日期</th>
                                                        <th class="text-center">命令号</th>
                                                        <th class="text-center">文电号</th>
                                                        <th class="text-center">上图状态</th>
                                                        <th class="text-center">上图时间</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody data-bind="foreach: kyjhTable">
                                                    <tr data-bind="value: id, click: $parent.showInPanel">
                                                        <td class="text-center"><input class="checkbox-inline" type="checkbox"/></td>
                                                        <td class="text-center" data-bind="text: $index"></td>
                                                        <td class="text-center" data-bind="text: serial"></td>
                                                        <td class="text-center" data-bind="text: startSTN">成都</td>
                                                        <td class="text-center" data-bind="text: endSTN">北京</td>
                                                        <td class="text-center" data-bind="text: runDate">5-10</td>
                                                        <td class="text-center" data-bind="text: command">123</td>
                                                        <td class="text-center" data-bind="text: tele">123</td>
                                                        <td class="text-center" data-bind="text: flag">已上图</td>
                                                        <td class="text-center" data-bind="text: generateDateTime">5-9 12:21</td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </dev>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-6 col-md-6 col-lg-6">
                            <div class="panel panel-default">
                                <div class="panel-heading paddingbottom0">
                                    <i class="fa fa-home"></i>运行线
                                </div>
                                <div class="panel-body paddingtop0 paddingleftright5">
                                    <div class="row">
                                        <div class="col-xs-12 col-md-12 col-lg-12 paddingleft0 paddingtop5">
                                            <form class="form-inline" role="form">
                                                <div class="input-group">
                                                    <label class="margin-right-10">时刻表:</label>
                                                    <input id="yxx_time" class="checkbox-inline" style="margin: 0px 10px 0px 0px;" type="checkbox">
                                                    <label class="margin-right-10">经由:</label>
                                                    <input id="yxx_routing" class="checkbox-inline" style="margin: 0px 10px 0px 0px;" type="checkbox">
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <dev class="col-xs-12 col-md-12 col-lg-12 paddingleft0 paddingtop5">
                                            <div class="table-responsive">
                                                <table id="right_table" class="table table-bordered table-striped table-hover tableradius">
                                                    <thead>
                                                    <tr>
                                                        <th class="text-center"><input class="checkbox-inline" type="checkbox"/></th>
                                                        <th class="text-center">序号</th>
                                                        <th class="text-center">车次</th>
                                                        <th class="text-center">始发站</th>
                                                        <th class="text-center">终到站</th>
                                                        <th class="text-center">始发日期</th>
                                                        <th class="text-center">客调</th>
                                                        <th class="text-center">审核日期</th>
                                                        <th class="text-center">值班主任</th>
                                                        <th class="text-center">审核时间</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <tr>
                                                        <td class="text-center"><input class="checkbox-inline" type="checkbox"/></td>
                                                        <td class="text-center">1</td>
                                                        <td class="text-center">T7</td>
                                                        <td class="text-center">成都</td>
                                                        <td class="text-center">北京</td>
                                                        <td class="text-center">5-11</td>
                                                        <td class="text-center">张三</td>
                                                        <td class="text-center">5-10 9:04</td>
                                                        <td class="text-center">李四</td>
                                                        <td class="text-center">5-10 10:01</td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </dev>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<title>Console</title>
<!-- Bootstrap core CSS -->
<link type="text/css" href="assets/css/custom-bootstrap.css" rel="stylesheet">
<!--font-awesome-->
<link type="text/css" rel="stylesheet" href="assets/css/font-awesome.min.css"/>
<!-- Custom styles for this template -->
<link type="text/css" href="assets/css/style.css" rel="stylesheet">
<script type="text/javascript" src="assets/js/jquery.js"></script>
<script type="text/javascript" src="assets/js/html5.js"></script>
<script type="text/javascript" src="assets/js/fuelUX.js"></script>
<script type="text/javascript" src="assets/js/bootstrap.min.js"></script>
<script type="text/javascript" src="assets/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="assets/js/jquery-ui-1.9.2.custom.min.js"></script>
<script type="text/javascript" src="assets/js/datepicker.js"></script>
<script type="text/javascript" src="assets/js/jquery.knob.js"></script>
<script type="text/javascript" src="assets/js/jquery.gritter.min.js"></script>
<script type="text/javascript" src="assets/js/jquery.sparkline.min.js"></script>
<!--表格中的图标-->
<script type="text/javascript">
    $(document).ready(function () {
        /*竖条*/
        var values = [1, 3, 5, 3, 8];
        $('.bar').sparkline(values, {
            type: 'bar',
            tooltipFormat: '{{value:levels}} - {{value}}',
            tooltipValueLookups: {
                levels: $.range_map({ ':2': 'Low', '3:6': 'Medium', '7:': 'High' })
            }
        });
        /*竖条*/
        /*线条*/
        $("#sparkline").sparkline([5, 6, 7, 9, 9, 5, 3, 2, 2, 4, 6, 7], {
            type: 'line'});
        /*线条*/
        $("#sparkline01").sparkline([1, 1, 2], {
            type: 'pie'});
    });
</script>
<!--表格中的图标-->
<!--日历插件-->
<script>
    $(function () {
        window.prettyPrint && prettyPrint();
        $('#dp1').datepicker();
        $('#dp2').datepicker();
    });

</script>
<!--表格排序-->
<script type="text/javascript" charset="utf-8">
    $(document).ready(function () {
        oTable = $('#table_1').dataTable({
            "sPaginationType": "full_numbers"
        });
    });
</script>
<!--进度条-->
<script>
    $(function () {
// Slider
        $('#bar01').slider({
            range: true,
            values: [17, 67]
        });
        $('#bar02').slider({
            range: true,
            values: [10, 47]
        });
        $('#bar03').slider({
            range: true,
            values: [23, 45]
        });
        $('#bar04').slider({
            range: true,
            values: [13, 33]
        });
        $('#bar05').slider({
            range: true,
            values: [19, 56]
        });

    })
</script>
<!--步骤-->
<script>
    $('#myWizard').wizard();
</script>
<!--弹出提示-->
<script>
    jQuery(document).ready(function () {

        jQuery('#growl1').click(function () {

            jQuery.gritter.add({
                // (string | mandatory) the heading of the notification
                title: 'This is a regular notice!',
                // (string | mandatory) the text inside the notification
                text: 'This will fade out after a certain amount of time.',
                // (string | optional) the image to display on the left
                image: '../assets/img/screen.png',
                // (bool | optional) if you want it to fade out on its own or just sit there
                sticky: false,
                // (int | optional) the time you want it to be alive for before fading out
                time: ''
            });

            return false;

        });

        jQuery('#growl2').click(function () {
            jQuery.gritter.add({
                title: 'This is a regular notice!',
                text: 'This will fade out after a certain amount of time.',
                sticky: false,
                time: ''
            });
            return false;
        });

        jQuery('#growl-primary').click(function () {
            jQuery.gritter.add({
                title: 'This is a regular notice!',
                text: 'This will fade out after a certain amount of time.',
                class_name: 'growl-primary',
                image: '../assets/img/screen.png',
                sticky: false,
                time: ''
            });
            return false;
        });

        jQuery('#growl-success').click(function () {
            jQuery.gritter.add({
                title: 'This is a regular notice!',
                text: 'This will fade out after a certain amount of time.',
                class_name: 'growl-success',
                image: '../assets/img/screen.png',
                sticky: false,
                time: ''
            });
            return false;
        });

        jQuery('#growl-warning').click(function () {
            jQuery.gritter.add({
                title: 'This is a regular notice!',
                text: 'This will fade out after a certain amount of time.',
                class_name: 'growl-warning',
                image: '../assets/img/screen.png',
                sticky: false,
                time: ''
            });
            return false;
        });

        jQuery('#growl-danger').click(function () {
            jQuery.gritter.add({
                title: 'This is a regular notice!',
                text: 'This will fade out after a certain amount of time.',
                class_name: 'growl-danger',
                image: '../assets/img/screen.png',
                sticky: false,
                time: ''
            });
            return false;
        });

        jQuery('#growl-info').click(function () {
            jQuery.gritter.add({
                title: 'This is a regular notice!',
                text: 'This will fade out after a certain amount of time.',
                class_name: 'growl-info',
                image: '../assets/img/screen.png',
                sticky: false,
                time: ''
            });
            return false;
        });

    });
</script>
<!--表格-->
<script type="text/javascript">
    $(function () {
        var colors = Highcharts.getOptions().colors,
                categories = ['MSIE', 'Firefox', 'Chrome', 'Safari', 'Opera'],
                name = 'Browser brands',
                data = [
                    {
                        y: 55.11,
                        color: colors[0],
                        drilldown: {
                            name: 'MSIE versions',
                            categories: ['MSIE 6.0', 'MSIE 7.0', 'MSIE 8.0', 'MSIE 9.0'],
                            data: [10.85, 7.35, 33.06, 2.81],
                            color: colors[0]
                        }
                    },
                    {
                        y: 21.63,
                        color: colors[1],
                        drilldown: {
                            name: 'Firefox versions',
                            categories: ['Firefox 2.0', 'Firefox 3.0', 'Firefox 3.5', 'Firefox 3.6', 'Firefox 4.0'],
                            data: [0.20, 0.83, 1.58, 13.12, 5.43],
                            color: colors[1]
                        }
                    },
                    {
                        y: 11.94,
                        color: colors[2],
                        drilldown: {
                            name: 'Chrome versions',
                            categories: ['Chrome 5.0', 'Chrome 6.0', 'Chrome 7.0', 'Chrome 8.0', 'Chrome 9.0',
                                'Chrome 10.0', 'Chrome 11.0', 'Chrome 12.0'],
                            data: [0.12, 0.19, 0.12, 0.36, 0.32, 9.91, 0.50, 0.22],
                            color: colors[2]
                        }
                    },
                    {
                        y: 7.15,
                        color: colors[3],
                        drilldown: {
                            name: 'Safari versions',
                            categories: ['Safari 5.0', 'Safari 4.0', 'Safari Win 5.0', 'Safari 4.1', 'Safari/Maxthon',
                                'Safari 3.1', 'Safari 4.1'],
                            data: [4.55, 1.42, 0.23, 0.21, 0.20, 0.19, 0.14],
                            color: colors[3]
                        }
                    },
                    {
                        y: 2.14,
                        color: colors[4],
                        drilldown: {
                            name: 'Opera versions',
                            categories: ['Opera 9.x', 'Opera 10.x', 'Opera 11.x'],
                            data: [ 0.12, 0.37, 1.65],
                            color: colors[4]
                        }
                    }
                ];

        function setChart(name, categories, data, color) {
            chart.xAxis[0].setCategories(categories, false);
            chart.series[0].remove(false);
            chart.addSeries({
                name: name,
                data: data,
                color: color || 'white'
            }, false);
            chart.redraw();
        }

        var chart = $('#chart01').highcharts({
            chart: {
                type: 'column'
            },
            title: {
                text: 'aaa'
            },
            subtitle: {
                text: ''
            },
            xAxis: {
                categories: categories
            },
            yAxis: {
                title: {
                    text: 'aaa'
                }
            },
            plotOptions: {
                column: {
                    cursor: 'pointer',
                    point: {
                        events: {
                            click: function () {
                                var drilldown = this.drilldown;
                                if (drilldown) { // drill down
                                    setChart(drilldown.name, drilldown.categories, drilldown.data, drilldown.color);
                                } else { // restore
                                    setChart(name, categories, data);
                                }
                            }
                        }
                    },
                    dataLabels: {
                        enabled: true,
                        color: colors[0],
                        style: {
                            fontWeight: 'bold'
                        },
                        formatter: function () {
                            return this.y + '%';
                        }
                    }
                }
            },
            tooltip: {
                formatter: function () {
                    var point = this.point,
                            s = this.x + ':<b>' + this.y + '% market share</b><br/>';
                    if (point.drilldown) {
                        s += 'Click to view ' + point.category + ' versions';
                    } else {
                        s += 'Click to return to browser brands';
                    }
                    return s;
                }
            },
            series: [
                {
                    name: name,
                    data: data,
                    color: 'white'
                }
            ],
            exporting: {
                enabled: false
            }
        })
                .highcharts(); // return chart
    });
</script>
<script type="text/javascript" src="assets/js/highcharts.js"></script>
</head>
<body class="Iframe-body">
<!--breadcrumb-->
<ol class="breadcrumb">
    <li><i class="fa fa-home"></i><a href="#">首页</a></li>
    <li><a href="#">资源管理</a></li>
    <li class="active">页面模版</li>
</ol>
<!--breadcrumb-->
<!--mainContent-->
<section class="mainContent">
<div class="row">
<div class="col-xs-12 col-sm-12 col-md-12">
    <section class="panel panel-default">
        <div class="panel-heading"><i class="fa fa-table"></i>表格</div>
        <div class="panel-body">
            <!--table-->
            <div class="table-responsive">
                <table class="table table-bordered table-striped table-hover" id="table_1">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>工单号</th>
                        <th>工单名称</th>
                        <th>工单类型</th>
                        <th>发起人</th>
                        <th>任务状态</th>
                        <th>工单状态</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>1</td>
                        <td><a href="orderDetails.html">20139261</a></td>
                        <td>新建应用申请</td>
                        <td><span id="sparkline01"></span></td>
                        <td>api</td>
                        <td><code>http://www.baidu.com</code></td>
                        <td><span class="label label-success">处理中</span></td>
                        <td><a href="orderProcess.html">处理</a></td>
                    </tr>
                    <tr>
                        <td>2</td>
                        <td><a href="orderDetails.html">20139260</a></td>
                        <td>新建应用申请</td>
                        <td><span id="sparkline"></span></td>
                        <td>api</td>
                        <td>填写基本资料</td>
                        <td><span class="label label-info">正在进行</span></td>
                        <td><a href="orderProcess.html">处理</a></td>
                    </tr>
                    <tr>
                        <td>3</td>
                        <td><a href="orderDetails.html">20139260</a></td>
                        <td>新建应用申请</td>
                        <td><span class="bar">1:4,2:3,3:2,4:1</span></td>
                        <td>api</td>
                        <td>填写基本资料</td>
                        <td><span class="label label-danger">危险</span></td>
                        <td><a href="orderProcess.html">处理</a></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <!--table-->
            <div class="pull-left">第1-5条记录/共5条记录</div>
            <ul class="pagination pull-right" style="margin:0px;">
                <li class="disabled"><a href="#">«</a></li>
                <li class="active"><a href="#">1 <span class="sr-only">(current)</span></a></li>
                <li><a href="userManagement2.html">2</a></li>
                <li><a href="#">3</a></li>
                <li><a href="#">4</a></li>
                <li><a href="#">5</a></li>
                <li><a href="#">»</a></li>
            </ul>
        </div>
    </section>
    <!--panel-->

</div>
<!--col-xs-6 col-sm-6 col-md-6-->
<div class="col-xs-4 col-sm-4 col-md-4">
    <section class="panel">
        <div class="panel-heading panel-Tab-bg">
            <ul class="nav nav-tabs nav-justified">
                <li class="active"><a href="#home" data-toggle="tab">Home</a></li>
                <li class=""><a href="#profile" data-toggle="tab">Profile</a></li>
                <li class=""><a href="#messages" data-toggle="tab">Messages</a></li>
                <li class=""><a href="#settings" data-toggle="tab">Settings</a></li>
            </ul>
        </div>
        <div class="panel-body tab-content-bg">
            <div class="tab-content">
                <div class="tab-pane active" id="home">home</div>
                <div class="tab-pane" id="profile">profile</div>
                <div class="tab-pane" id="messages">message</div>
                <div class="tab-pane" id="settings">settings</div>
            </div>
        </div>
    </section>
    <!--panel-->
</div>
<!--col-xs-6 col-sm-6 col-md-6-->
<div class="col-xs-6 col-sm-4 col-md-4">
    <section class="panel panel-default">
        <div class="panel-heading"><i class="fa fa-home"></i>Enhanced Checkboxes and Radios</div>
        <div class="panel-body">
            <div class="row">
                <div class="col-xs-6 col-sm-6 col-md-6">
                    <div class="ckbox ckbox-default">
                        <input type="checkbox" value="1" id="checkboxDefault" checked="checked">
                        <label for="checkboxDefault">Checkbox Default</label>
                    </div>
                    <div class="ckbox ckbox-primary">
                        <input type="checkbox" value="1" checked="checked">
                        <label>Checkbox Primary</label>
                    </div>
                    <div class="ckbox ckbox-warning">
                        <input type="checkbox" id="checkboxWarning" checked="checked">
                        <label for="checkboxWarning">Checkbox Warning</label>
                    </div>
                    <div class="ckbox ckbox-success">
                        <input type="checkbox" id="checkboxSuccess" checked="checked">
                        <label for="checkboxSuccess">Checkbox Success</label>
                    </div>
                    <div class="ckbox ckbox-danger">
                        <input type="checkbox" id="checkboxDanger" checked="checked">
                        <label for="checkboxDanger">Checkbox Danger</label>
                    </div>
                    <div class="ckbox ckbox-default">
                        <input type="checkbox" id="checkboxDisabled" disabled="disabled">
                        <label for="checkboxDisabled">Checkbox Disabled</label>
                    </div>
                </div>
                <!--col-xs-6 col-sm-6 col-md-6-->
                <div class="col-xs-6 col-sm-6 col-md-6">
                    <div class="rdio rdio-default">
                        <input type="radio" name="radio" id="radioDefault" value="1" checked="checked">
                        <label for="radioDefault">Radio Default</label>
                    </div>
                    <div class="rdio rdio-primary">
                        <input type="radio" name="radio" value="2" id="radioPrimary">
                        <label for="radioPrimary">Radio Primary</label>
                    </div>
                    <div class="rdio rdio-warning">
                        <input type="radio" name="radio" value="3" id="radioWarning">
                        <label for="radioWarning">Radio Warning</label>
                    </div>
                    <div class="rdio rdio-success">
                        <input type="radio" name="radio" value="4" id="radioSuccess">
                        <label for="radioSuccess">Radio Success</label>
                    </div>
                    <div class="rdio rdio-danger">
                        <input type="radio" name="radio" value="5" id="radioDanger">
                        <label for="radioDanger">Radio Danger</label>
                    </div>
                    <div class="rdio rdio-default">
                        <input type="radio" name="radio" value="6" disabled="disabled" id="radioDisabled">
                        <label for="radioDisabled">Radio Disabled</label>
                    </div>
                </div>
                <!--col-xs-6 col-sm-6 col-md-6-->
            </div>
        </div>
    </section>
    <!--panel-->
</div>
<!--col-xs-6 col-sm-6 col-md-6-->
<div class="col-xs-4 col-sm-4 col-md-4">
    <section class="panel panel-default">
        <div class="panel-heading"><i class="fa fa-home"></i>进度条</div>
        <div class="panel-body">
            <!--Ranger Slider-->
            <div id="bar01" class="slider-primary"></div>
            <div id="bar02" class="slider-success"></div>
            <div id="bar03" class="slider-warning"></div>
            <div id="bar04" class="slider-info"></div>
            <div id="bar05" class="slider-danger"></div>
            <p>&nbsp;</p>
            <!--Ranger Slider-->
        </div>
    </section>
    <!--panel-->
</div>
<!--col-xs-6 col-sm-6 col-md-6-->

<div class="col-xs-2 col-sm-2 col-md-2">
    <section class="panel panel-default">
        <div class="panel-heading"><i class="fa fa-home"></i>弹出框</div>
        <div class="panel-body">
            <button class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal"> Launch demo modal
            </button>
        </div>
    </section>
    <!--panel-->
</div>
<!--col-xs-6 col-sm-6 col-md-6-->
<div class="col-xs-2 col-sm-2 col-md-2">
    <section class="panel panel-default">
        <div class="panel-heading"><i class="fa fa-home"></i>label</div>
        <div class="panel-body">
            <p><span class="label label-default">Default</span> <span class="label label-primary">Primary</span></p>

            <p><span class="label label-success">Success</span> <span class="label label-info">Info</span></p>

            <p><span class="label label-warning">Warning</span> <span class="label label-danger">Danger</span></p>
        </div>
    </section>
    <!--panel-->
</div>
<!--col-xs-6 col-sm-6 col-md-6-->
<div class="col-xs-2 col-sm-2 col-md-2">
    <section class="panel panel-default">
        <div class="panel-heading"><i class="fa fa-home"></i>按钮</div>
        <div class="panel-body">
            <p>
                <button type="button" class="btn btn-default">Default</button>
                <button type="button" class="btn btn-primary">Primary</button>
            </p>
            <p>
                <button type="button" class="btn btn-success">Success</button>
                <button type="button" class="btn btn-info">Info</button>
            </p>
            <p>
                <button type="button" class="btn btn-warning">Warning</button>
                <button type="button" class="btn btn-danger">Danger</button>
                <button type="button" class="btn btn-link">Link</button>
        </div>
    </section>
    <!--panel-->
</div>
<!--col-xs-6 col-sm-6 col-md-6-->
<div class="col-xs-2 col-sm-2 col-md-2">
    <section class="panel panel-default">
        <div class="panel-heading"><i class="fa fa-home"></i>日历控件</div>
        <div class="panel-body">
            <form class="form-horizontal" role="form">
                <div class="input-group">
                    <input type="text" class="form-control" id="dp1">
              <span class="input-group-btn">
              <button class="btn btn-default" type="button"><i class="fa fa-calendar"></i></button>
              </span></div>
                <!-- /input-group -->
            </form>
        </div>
    </section>
    <!--panel-->
</div>
<!--col-xs-6 col-sm-6 col-md-6-->
<div class="col-xs-4 col-sm-4 col-md-4">
    <section class="panel panel-default">
        <div class="panel-heading"><i class="fa fa-home"></i>输入框</div>
        <div class="panel-body">
            <!--form-->
            <form class="form-horizontal" role="form">
                <div class="form-group">
                    <label for="inputEmail3" class="col-sm-2 control-label"><span class="red">*</span>账号</label>

                    <div class="col-sm-10">
                        <input type="email" class="form-control" id="inputEmail3" placeholder="Email">

                        <p class="help-block">输入框的说明信息。</p>
                    </div>
                </div>
                <div class="form-group">
                    <label for="inputEmail3" class="col-sm-2 control-label">com：</label>

                    <div class="col-sm-10">
                        <div class="input-group">
                            <input type="text" class="form-control">
                            <span class="input-group-addon">.com</span></div>
                    </div>
                </div>
                <div class="form-group">
                    <label for="inputPassword3" class="col-sm-2 control-label">密码：</label>

                    <div class="col-sm-10">
                        <input type="password" class="form-control" id="inputPassword3" placeholder="Password">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox">
                                记住我！ </label>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="submit" class="btn btn-default">登 录</button>
                    </div>
                </div>
            </form>
            <!--form-->
        </div>
    </section>
    <!--panel-->
</div>
<!--col-xs-6 col-sm-6 col-md-6-->

<div class="col-xs-4 col-sm-4 col-md-4">
    <section class="panel panel-default">
        <div class="panel-heading"><i class="fa fa-home"></i>图标插件</div>
        <div class="panel-body">
            <div id="chart01"></div>
        </div>
    </section>
    <!--panel-->
</div>

<!--col-xs-6 col-sm-6 col-md-6-->
<div class="col-xs-4 col-sm-4 col-md-4">
    <section class="panel panel-default">
        <div class="panel-heading"><i class="fa fa-home"></i>提示</div>
        <div class="panel-body">
            <div class="bs-callout bs-callout-primary">
                <h4>Don't mix form groups with input groups</h4>

                <p>Do not mix form groups directly with <a href="/components/#input-groups">input groups</a>. Instead,
                    nest the input group inside of the form group.</p>
            </div>
            <div class="bs-callout bs-callout-info">
                <h4>Don't mix form groups with input groups</h4>

                <p>Do not mix form groups directly with <a href="/components/#input-groups">input groups</a>. Instead,
                    nest the input group inside of the form group.</p>
            </div>
            <div class="bs-callout bs-callout-success">
                <h4>Don't mix form groups with input groups</h4>

                <p>Do not mix form groups directly with <a href="/components/#input-groups">input groups</a>. Instead,
                    nest the input group inside of the form group.</p>
            </div>
            <div class="bs-callout bs-callout-warning">
                <h4>Don't mix form groups with input groups</h4>

                <p>Do not mix form groups directly with <a href="/components/#input-groups">input groups</a>. Instead,
                    nest the input group inside of the form group.</p>
            </div>
            <div class="bs-callout bs-callout-danger">
                <h4>Don't mix form groups with input groups</h4>

                <p>Do not mix form groups directly with <a href="/components/#input-groups">input groups</a>. Instead,
                    nest the input group inside of the form group.</p>
            </div>
        </div>
    </section>
    <!--panel-->
</div>
<!--col-xs-6 col-sm-6 col-md-6-->
<div class="col-xs-4 col-sm-4 col-md-4">
    <section class="panel panel-default">
        <div class="panel-heading"><i class="fa fa-home"></i>弹出提示</div>
        <div class="panel-body">
            <p><a href="" class="btn btn-default" id="growl1">Growl With Image Message</a></p>

            <p><a href="" class="btn btn-default" id="growl2">Growl Without Image Message</a></p>

            <p><a href="" class="btn btn-primary " id="growl-primary">Primary Message</a></p>

            <p><a href="" class="btn btn-success  " id="growl-success">Success Message</a></p>

            <p><a href="" class="btn btn-warning  " id="growl-warning">Warning Message</a></p>

            <p><a href="" class="btn btn-danger  " id="growl-danger">Danger Message</a></p>

            <p><a href="" class="btn btn-info " id="growl-info">Info Message</a></p>
        </div>
    </section>
    <!--panel-->
</div>
<!--col-xs-6 col-sm-6 col-md-6-->
<div class="col-xs-4 col-sm-4 col-md-4">
    <section class="panel panel-default">
        <div class="panel-heading"><i class="fa fa-home"></i>不可拖动的进度条</div>
        <div class="panel-body">
            <div class="progress progress-striped">
                <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="40" aria-valuemin="0"
                     aria-valuemax="100" style="width: 40%"><span class="sr-only">40% Complete (success)</span></div>
            </div>
            <!--1-->
            <div class="progress progress-striped">
                <div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="20" aria-valuemin="0"
                     aria-valuemax="100" style="width: 20%"><span class="sr-only">20% Complete</span></div>
            </div>
            <!--2-->
            <div class="progress progress-striped">
                <div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="60" aria-valuemin="0"
                     aria-valuemax="100" style="width: 60%"><span class="sr-only">60% Complete (warning)</span></div>
            </div>
            <!--3-->
            <div class="progress progress-striped">
                <div class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="80" aria-valuemin="0"
                     aria-valuemax="100" style="width: 80%"><span class="sr-only">80% Complete (danger)</span></div>
            </div>
            <!--4-->
            <div class="progress progress-striped">
                <div class="progress-bar progress-bar-default" role="progressbar" aria-valuenow="80" aria-valuemin="0"
                     aria-valuemax="100" style="width: 80%"><span class="sr-only">80% Complete (danger)</span></div>
            </div>
            <!--5-->
            <div class="progress progress-striped active">
                <div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="45" aria-valuemin="0"
                     aria-valuemax="100" style="width: 45%"><span class="sr-only">45% Complete</span></div>
            </div>
            <!--6-->
            <div class="progress">
                <div class="progress-bar progress-bar-success" style="width: 35%"><span class="sr-only">35% Complete (success)</span>
                </div>
                <div class="progress-bar progress-bar-warning" style="width: 20%"><span class="sr-only">20% Complete (warning)</span>
                </div>
                <div class="progress-bar progress-bar-danger" style="width: 10%"><span class="sr-only">10% Complete (danger)</span>
                </div>
            </div>
            <!--7-->
        </div>
    </section>
    <!--panel-->
</div>
<!--col-xs-6 col-sm-6 col-md-6-->
<div class="col-xs-11 col-sm-11 col-md-11">
    <section class="panel panel-default">
        <div class="panel-heading"><i class="fa fa-home"></i>Fuel UX 插件（步骤）</div>
        <div class="panel-body fuelux">
            <!--步骤-->
            <div class="well wizard-example">
                <div id="MyWizard" class="wizard">
                    <ul class="steps">
                        <li data-target="#step1" class="active"><span class="badge badge-info">1</span>Step 1<span
                                class="chevron"></span></li>
                        <li data-target="#step2"><span class="badge">2</span>Step 2<span class="chevron"></span></li>
                        <li data-target="#step3"><span class="badge">3</span>Step 3<span class="chevron"></span></li>
                        <li data-target="#step4"><span class="badge">4</span>Step 4<span class="chevron"></span></li>
                        <li data-target="#step5"><span class="badge">5</span>Step 5<span class="chevron"></span></li>
                    </ul>
                    <div class="actions">
                        <button type="button" class="btn btn-prev btn-primary"><i class=" fa fa-arrow-left"></i>Prev
                        </button>
                        <button type="button" class="btn btn-next btn-info" data-last="Finish">Next<i
                                class="fa fa-arrow-right"></i></button>
                    </div>
                </div>
                <div class="step-content">
                    <div class="step-pane active" id="step1">This is step 1</div>
                    <div class="step-pane" id="step2">This is step 2</div>
                    <div class="step-pane" id="step3">This is step 3</div>
                    <div class="step-pane" id="step4">This is step 4</div>
                    <div class="step-pane" id="step5">This is step 5</div>
                </div>
            </div>
            <!--步骤-->
        </div>
    </section>
    <!--panel-->
</div>
<!--col-xs-6 col-sm-6 col-md-6-->

<div class="col-xs-4 col-sm-4 col-md-4">
    <section class="panel panel-default">
        <div class="panel-heading"><i class="fa fa-home"></i>段落</div>
        <div class="panel-body">
            <h1>h1. Bootstrap heading</h1>

            <h2>h2. Bootstrap heading</h2>

            <h3>h3. Bootstrap heading</h3>
            <h4>h4. Bootstrap heading</h4>
            <h5>h5. Bootstrap heading</h5>
            <h6>h6. Bootstrap heading</h6>
        </div>
    </section>
    <!--panel-->
</div>
<!--col-xs-6 col-sm-6 col-md-6-->
<div class="col-xs-4 col-sm-4 col-md-4">
    <section class="panel panel-default">
        <div class="panel-heading"><i class="fa fa-home"></i>Treeview</div>
        <div class="panel-body">
            <ul id="browser" class="filetree">
                <li><span class="folder">所有租户</span>
                    <ul>
                        <li><span class="folder"><i class="fa fa-user"></i>租户1</span>
                            <ul id="folder21">
                                <li><a data-toggle="modal" data-target="#myModal2"><i class="fa fa-user"></i>租户2</a>
                                    <ul id="folder21">
                                        <li><a data-toggle="modal" data-target="#myModal2"><i class="fa fa-user"></i>租户2</a>
                                        </li>
                                        <li><a data-toggle="modal" data-target="#myModal2"><i class="fa fa-user"></i>租户3</a>
                                            <ul id="folder21">
                                                <li><a data-toggle="modal" data-target="#myModal2"><i
                                                        class="fa fa-user"></i>租户2</a></li>
                                                <li><a data-toggle="modal" data-target="#myModal2"><i
                                                        class="fa fa-user"></i>租户3</a></li>
                                            </ul>
                                        </li>
                                    </ul>
                                </li>
                                <li><a data-toggle="modal" data-target="#myModal2"><i class="fa fa-user"></i>租户3</a>
                                </li>
                            </ul>
                        </li>
                        <li><span class="file"><i class="fa fa-user"></i>租户4</span></li>
                    </ul>
                </li>
            </ul>
        </div>
    </section>
    <!--panel-->
</div>
<!--col-xs-6 col-sm-6 col-md-6-->
<div class="col-xs-4 col-sm-4 col-md-4">
    <section class="panel panel-default">
        <div class="panel-heading"><i class="fa fa-home"></i>list</div>
        <div class="panel-body">
            <ul class="list-group">
                <li class="list-group-item"><span class="badge">14</span> Cras justo odio</li>
                <li class="list-group-item"><span class="badge">2</span> Dapibus ac facilisis in</li>
                <li class="list-group-item"><span class="badge">1</span> Morbi leo risus</li>
            </ul>
            <div class="list-group"><a href="#" class="list-group-item active"> Cras justo odio </a> <a href="#"
                                                                                                        class="list-group-item">Dapibus
                ac facilisis in</a> <a href="#" class="list-group-item">Morbi leo risus</a> <a href="#"
                                                                                               class="list-group-item">Porta
                ac consectetur ac</a> <a href="#" class="list-group-item">Vestibulum at eros</a></div>
        </div>
    </section>
    <!--panel-->
</div>
<!--col-xs-6 col-sm-6 col-md-6-->
<div class="col-xs-6 col-sm-6 col-md-6">
    <section class="panel panel-default">
        <div class="panel-heading"><i class="fa fa-home"></i>Tooltips</div>
        <div class="panel-body">
            <div class="bs-example-tooltips">
                <button type="button" class="btn btn-default tooltip01" id="tooltip02" data-toggle="tooltip"
                        data-placement="left" title="" data-original-title="Tooltip on left">Tooltip on left
                </button>
                <button type="button" class="btn btn-default tooltip01" data-toggle="tooltip" data-placement="top"
                        title="" data-original-title="Tooltip on top">Tooltip on top
                </button>
                <button type="button" class="btn btn-default tooltip01" data-toggle="tooltip" data-placement="bottom"
                        title="" data-original-title="Tooltip on bottom">Tooltip on bottom
                </button>
                <button type="button" class="btn btn-default tooltip01" data-toggle="tooltip" data-placement="right"
                        title="" data-original-title="Tooltip on right">Tooltip on right
                </button>
            </div>
        </div>
    </section>
    <!--panel-->
</div>
<!--col-xs-6 col-sm-6 col-md-6-->
<div class="col-xs-6 col-sm-6 col-md-6">
    <section class="panel panel-default">
        <div class="panel-heading"><i class="fa fa-home"></i>Popover</div>
        <div class="panel-body">
            <div class="bs-example-tooltips">
                <button type="button" class="btn btn-default popover01" data-container="body" data-toggle="popover"
                        data-placement="left" data-content="Vivamus sagittis lacus vel augue laoreet rutrum faucibus."
                        data-original-title="" title=""> Popover on left
                </button>
                <button type="button" class="btn btn-default popover01" data-container="body" data-toggle="popover"
                        data-placement="top" data-content="Vivamus sagittis lacus vel augue laoreet rutrum faucibus."
                        data-original-title="" title=""> Popover on top
                </button>
                <button type="button" class="btn btn-default popover01" data-container="body" data-toggle="popover"
                        data-placement="bottom" data-content="Vivamus sagittis lacus vel augue laoreet rutrum faucibus."
                        data-original-title="" title=""> Popover on bottom
                </button>
                <button type="button" class="btn btn-default popover01" data-container="body" data-toggle="popover"
                        data-placement="right" data-content="Vivamus sagittis lacus vel augue laoreet rutrum faucibus."
                        data-original-title="" title=""> Popover on right
                </button>
            </div>
        </div>
    </section>
    <!--panel-->
</div>
<!--col-xs-6 col-sm-6 col-md-6-->
<div class="col-xs-11 col-sm-11 col-md-11">
    <section class="panel panel-default">
        <div class="panel-heading"><i class="fa fa-home"></i>页面警告</div>
        <div class="panel-body">
            <div class="alert alert-success"><strong>Well done!</strong> You successfully read this important alert
                message.
            </div>
            <!--1-->
            <div class="alert alert-info"><strong>Heads up!</strong> This alert needs your attention, but it's not super
                important.
            </div>
            <!--2-->
            <div class="alert alert-warning"><strong>Warning!</strong> Better check yourself, you're not looking too
                good.
            </div>
            <!--3-->
            <div class="alert alert-danger"><strong>Oh snap!</strong> Change a few things up and try submitting again.
            </div>
            <!--4-->
        </div>
    </section>
    <!--panel-->
</div>
<!--col-xs-6 col-sm-6 col-md-6-->
<div class="col-xs-4 col-sm-4 col-md-4">
    <section class="panel panel-default">
        <div class="panel-heading"><i class="fa fa-home"></i>步骤提示</div>
        <div class="panel-body">aaaa</div>
    </section>
    <!--panel-->
</div>
<!--col-xs-6 col-sm-6 col-md-6-->
</div>
<!--row-->

</section>
<!--mainContent-->

<!--modal-->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">Modal title</h4>
            </div>
            <div class="modal-body"> ...</div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary">Save changes</button>
            </div>
        </div>
    </div>
</div>
<!--modal-->
<script src="assets/js/bootstrap.min.js"></script>
<script>
    $(".tooltip01").tooltip("toggle");
    $(".tooltip01").tooltip("hide");
    $(".popover01").popover("toggle");
    $(".popover01").popover("hide");
</script>
<script src="assets/js/jquery.treeview.js"></script>
<script src="assets/js/jquery.treeview.demo.js"></script>
</body>
</html>

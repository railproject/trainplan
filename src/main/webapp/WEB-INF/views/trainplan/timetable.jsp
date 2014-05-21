<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <title>时刻表</title>
    <link type="text/css" href="${ctx}/assets/css/custom-bootstrap.css" rel="stylesheet"/>
    <link type="text/css" href="${ctx}/assets/css/font-awesome.min.css" rel="stylesheet"/>
    <link type="text/css" href="${ctx}/assets/css/style.css" rel="stylesheet"/>
    <script type="text/javascript" src="${ctx}/assets/js/jquery.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/html5.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/fuelUX.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/jquery.dataTables.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/jquery-ui-1.9.2.custom.min.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/datepicker.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/jquery.knob.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/jquery.gritter.min.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/jquery.sparkline.min.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/jquery.freezeheader.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/purl.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/knockout.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/trainplan/timetable.js"></script>
    <style>
        #hdtimetable table tr th{
            border-color:#dfe4ee;
            text-align:center;
        }
    </style>
</head>
<body>
<div class="container padding0 margin0" style="width: 100%;">
<div class="row padding0 margin0">
    <div class="col-md-12 col-xs-12 col-lg-12 padding0 margin0">
        <div class="panel-body padding0 margin0">
            <div class="table-responsive padding0 margin0">
                <table id="timetable" class="table table-bordered table-striped table-hover">
                    <thead>
                    <tr>
                        <th class="text-center">序号</th>
                        <th class="text-center">站名</th>
                        <th class="text-center">到达</th>
                        <th class="text-center">出发</th>
                        <th class="text-center">股道</th>
                        <th class="text-center">营业</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="row" items="${list}" varStatus="idx">
                        <tr>
                            <td class="text-center">${idx.index + 1}</td>
                            <td class="text-center">${row.name}</td>
                            <td class="text-center">${row.arrDateTime}</td>
                            <td class="text-center">${row.dptDateTime}</td>
                            <td class="text-center">${row.trackName}</td>
                            <td class="text-center">${row.psg}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</div>
</body>
</html>
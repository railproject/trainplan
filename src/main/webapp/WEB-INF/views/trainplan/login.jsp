<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Login</title>
    <!-- Bootstrap core CSS -->
    <link href="assets/css/custom-bootstrap.css" rel="stylesheet">
    <!--font-awesome-->
    <link type="text/css" rel="stylesheet" href="assets/css/font-awesome.min.css"/>
    <!-- Custom styles for this template -->
    <link href="assets/css/style.css" rel="stylesheet">
</head>
<body class="sign_body">
<div class="sign">
    <h2 style="text-align:center;" class="sign-logo">电科华云综合管理云平台</h2>

    <form role="form">
        <div class="form-group">
            <input type="email" class="form-control" id="exampleInputEmail1" placeholder="输入账号"></input>
        </div>
        <div class="form-group">
            <input type="password" class="form-control" id="exampleInputPassword1" placeholder="输入密码">
        </div>
        <div class="checkbox">
            <label>
                <input type="checkbox">
                记住我！</label>
            <label class=" pull-right"></label>
        </div>
        <hr>
        <div class="sign_a"><span><a href="#">忘记密码？</a></span><span class=" pull-right"><a href="#">注册账号</a></span>
        </div>
        <a class="btn btn-success btn-lg pull-right" href="index.jsp">登 录</a>
    </form>
</div>
</body>
</html>

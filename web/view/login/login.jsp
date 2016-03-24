<%--
  Created by IntelliJ IDEA.
  User: chenpengjiang
  Date: 2016/3/22
  Time: 14:55
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="../../resources/jsp/_inc.jsp"%>
    <title>私有云平台登陆</title>

</head>
<body class="login-body">

<div class="container">

    <form class="form-signin" method="post" action="<%=request.getContextPath()%>/login.do">
        <h2 class="form-signin-heading">私有云平台</h2>
        <div class="login-wrap">
            <span style="padding-bottom: 1px;color: red">${errmsg}</span>
            <input name="userName" type="text" class="form-control" required placeholder="用户名" autofocus>
            <input name="password" type="password" class="form-control"required placeholder="密码">
            <label class="checkbox">
                <input type="checkbox" value="remember-me"> 记住密码
                <span class="pull-right"> <a href="#"> 忘记密码?</a></span>
            </label>
            <button class="btn btn-lg btn-login btn-block" type="submit">登陆</button>

        </div>

    </form>

</div>
</body>
</html>

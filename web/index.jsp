<%--
  Created by IntelliJ IDEA.
  User: chenpengjiang
  Date: 2016/3/7
  Time: 9:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>登陆</title>
  </head>
  <body>
  <form method="post" action="/login.do">
    <input type="text" placeholder="账号" name="userName">
    <input type="text" placeholder="密码" name="password">

    <input type="submit" value="登陆">
  </form>
  </body>
</html>

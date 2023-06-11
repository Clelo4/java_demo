<%--
  Created by IntelliJ IDEA.
  User: jack
  Date: 2023/6/9
  Time: 下午10:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.servletlearn.bean.*"%>
<%@ page import="com.example.servletlearn.basic.bean.User" %>
<%
    User user = (User) request.getAttribute("user");
%>
<html>
<head>
    <title>Hello World - JSP</title>
</head>
<body>
<%-- JSP Comment --%>
<h1>Hello <%= user.getName() %>!</h1>
<p>School Name:
    <span style="color:red">
        <%= user.getSchool().getName() %>
    </span>
</p>
<p>School Address:
    <span style="color:red">
        <%= user.getSchool().getAddress() %>
    </span>
</p>
<p>
    <%
        out.println("Your IP address is ");
    %>
    <span style="color:red">
        <%= request.getRemoteAddr() %>
    </span>
</p>
</body>
</html>
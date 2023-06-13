<%--
   Created by IntelliJ IDEA.
   User: lj兴灬
   Date: 2022/11/17
   Time: 15:50
   To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>
</head>
<body>

$.ajax({
    url:"",
    data:{
    },
    type:"",
    dataType:"",
    success:function(data){

    }
})








</body>
</html>


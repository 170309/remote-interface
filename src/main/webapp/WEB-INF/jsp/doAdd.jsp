
<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title></title>
    <style type="text/css">
    	fieldset{
		    display: block;
		    width: 60%;
		    height: auto;
		    margin: 0 auto;
		    font-size: 1.01em;
		    padding: 20px;
		    background: #eee;
		    border: 1px solid #ddd;
		}
		a{

		    text-decoration:none;
		}
		legend{
		    margin-top: 5px;
		    padding-top: 5px
		}
    </style>
</head>
<body>
    <fieldset>
        <legend>添加结果</legend>
        <%
            String userExist = (String) request.getSession().getAttribute("userExist");
            String text = (String) request.getSession().getAttribute("text");
            String data = (String) request.getSession().getAttribute("data");
            if(userExist != null){
        %>
        <p><%= userExist%></p>
        <%}else{%>
            <p> <%= text%> </p>
        <%}%>

        <hr>
        <a href="?method=list&data=<%=data%>">返回上页</a>
    </fieldset>
</body>
</html>

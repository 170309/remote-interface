
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
    <%
        String interfaceName = (String) request.getSession().getAttribute("interfaceName");
        String exeMessage = (String) request.getSession().getAttribute("exeMessage");
        String data = (String) request.getSession().getAttribute("data");
    %>
    <legend><%= interfaceName%>执行结果</legend>
    <p>
        <%= exeMessage%>
    </p>
    <a href="?method=list&data=<%= data%>"></a>
</fieldset>
</body>
</html>


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
<form action="?method=t2" method="post">
    <fieldset>
        <legend>操作接口结果</legend>
        <%
            String text = (String) request.getSession().getAttribute("text");
            String interfaceName = (String) request.getSession().getAttribute("interfaceName");
            if(interfaceName == null){
                %>
                    <p><%= text%></p>
                <%
            }else{
                %>
                <p><%= text%>了接口:<%= interfaceName%></p>
        <%
            }
        %>
        <hr>
        <a href="?method=outCheck">返回上页</a>
    </fieldset>
</form>
</body>
</html>

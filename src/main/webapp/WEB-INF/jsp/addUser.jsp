
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
<form action="?method=doAdd" method="post">
    <fieldset>
        <legend>添加用户</legend>
        接口:<select name="interfaceId" id="">
        <%
            List<Map<String,Object>> result =
                    (List<Map<String, Object>>) request.getSession().getAttribute("result");
            for (Map<String, Object> map : result) {
        %>
            <option value=<%= map.get("INTERFACE_ID")%>> <%= map.get("INTERFACE_DESCRIPTION") %> </option>
        <%}%>
        </select>
        用户ID: <input type="text" name="userId" value=""/>
        <input type="submit" value="添加">
    </fieldset>
</form>
</body>
</html>

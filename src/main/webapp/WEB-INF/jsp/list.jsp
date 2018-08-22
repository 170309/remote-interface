
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
        String userIsAdmin = (String) request.getSession().getAttribute("userIsAdmin");
        List<Map<String,Object>> result =
                (List<Map<String, Object>>) request.getSession().getAttribute("result");
        String data = (String) request.getSession().getAttribute("data");
       
        
       	
    %>
    <legend>用户接口映射列表<% if(userIsAdmin!=null&&userIsAdmin.equals("YES")){%> <a href="?method=adduser">添加</a>  <%}%></legend>
    <table>
        <tr>
            <th>用户</th>
            <th>接口</th>
            <th>操作</th>
        </tr>
        <% 
        System.out.println(result);
        for(Map<String, Object> map : result){
        	String userName =  (String)map.get("USERNAME");
        	String description =  (String)map.get("IDESCRIPTION");
        %>
        <tr>
            <td><%= userName%></td>
            <td><%= description%></td>
            <td>
                <%
                    if(userIsAdmin!=null&&userIsAdmin.equals("YES")){
                        String delUrl = "?method=delete&interface_id=" + map.get("INTERFACEID") + "&userId=" + map.get("USERID")+ "&data="+data;
                        String exeUrl ="?method=executeInput&interface_id=" + map.get("INTERFACEID")+ "&data="+data;
                %>
                <a href=<%= delUrl%> >删除</a>
                <a href= <%= exeUrl %>>执行</a>
                <%}else{
                    String exeUrl ="?method=executeInput&interface_id=" + map.get("INTERFACEID")+ "&data="+data;
                %>
                <a href= <%= exeUrl %>>执行</a>
                <%}%>
            </td>
        </tr>
        <%}%>
    </table>
    <hr>
    <a href="?method=list&data=<%= data %>">首页</a>
</fieldset>
</body>
</html>

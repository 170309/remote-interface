
<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
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
    <h1>维护接口执行</h1>
    <form action="?method=t2" method="post">
        <fieldset>
            <legend>操作接口</legend>
            <table>
                <tr>
                    <th>接口</th>
                    <th>当前状态</th>
                    <th>操作</th>
                </tr>
                <%
                    List<Map<String,Object>> result =
                            (List<Map<String, Object>>) request.getSession().getAttribute("result");
                    String data = (String) request.getSession().getAttribute("data");
                    for(Map<String,Object> map:result){
                        String text = "true".equals(map.get("EXECUTE_FLAG")) ? "禁用" : "启用";
                        String currentStatus = "true".equals(map.get("EXECUTE_FLAG")) ? "可执行" : "不可执行";
                        String url = "?method=updateIfAuth&interface_id="+
                                map.get("INTERFACE_ID")+"&prev_flag="+map.get("PREV_FLAG")+"&data="+data;
                %>
                    <tr>
                    <td><%= map.get("INTERFACE_DESCRIPTION") %></td>
                    <td><%= currentStatus %></td>
                    <td>
                        <a href=<%= url%>>
                            <%= text %>
                        </a>
                    </td>
                    </tr>
                <%}%>
            </table>
            <hr>
            <%String url = "?method=list&data="+data; %>
            <a href=<%= url%> >首页</a>
        </fieldset>
    </form>
</body>
</html>

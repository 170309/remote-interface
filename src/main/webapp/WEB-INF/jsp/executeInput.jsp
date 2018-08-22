<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page import="java.util.*" %>
<%@ page import="top.miaojun.j" %>
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
<%
    String[] versions = {"Ver_Submit", "Ver_Adj_Submit", "Ver_CTRL"};
    String[] verNames = {"上报版", "调整预算上报版", "执行版"};
    String[] perName = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月",
            "九月", "十月", "十一月", "十二月"};
    List<Map<String, Object>> list = (List<Map<String, Object>>) request.getSession().getAttribute("result");
    String exeFlag = (String) list.get(0).get("EXECUTE_FLAG");
    String interfaceName = (String) list.get(0).get("INTERFACE_NAME");
    if ("true".equals(exeFlag)) {
        String param = (String) list.get(0).get("PARAM_LIST");
        String[] params = param.split(",");
        String data = (String) request.getSession().getAttribute("data");%>
<form action="?method=executeIF" method="post">
    <fieldset>
        <legend>接口参数填写</legend>

        <%
            int len = params.length;
            if (param.length() == 0) {
                String url = "?method=executeIF&interface_name=" + interfaceName;%>
        <p><%= interfaceName%>没有参数</p>
        <a href=<%= url%>>执行</a>

        <%
        } else {
            if (len == 1) {
                String o = params[0];
                if ("year".equals(o)) {%>
        年份:<select name='pYear'>
        <%
            for (int l = 16; l < 99; l++) {
                String value = "20" + l;%>
        <option value=<%= value%>><%=value%>年</option>
        <%}%>
    </select><br>
        <%
            }
        } else {
            String o = params[0];
            String t = params[1];

            if ("year".equals(o) || "year".equals(t)) {
        %>
        年份:<select name="pYear">
        <%
            for (int l = 16; l < 99; l++) {
                String value = "20" + l;
        %>
        <option value=<%=value%>><%=value%>年</option>
        <%}%>
    </select><br>
        <%
            }
            if ("version".equals(o) || "version".equals(t)) {
        %>版本:<select name='pVersion'>";
        <%for (int l = 0; l < versions.length; l++) {%>
        <option value=<%= verNames[l] %>><%= verNames[l] %>
        </option>
        <%}%>
    </select><br>
        <%
        } else if ("period".equals(o) || "period".equals(t)) {
        %>期间:<select name='pPeriod'>";
        <%
            for (int l = 0; l < perName.length; l++) {
        %>
        <option value=<%=(l + 1)%>><%= perName[l]%>
        </option>
        <%}%>
    </select><br>
        <%}%>

        <%}%>
        <input type='hidden' name='interface_name' value=<%=interfaceName%>>
        <input type='hidden' name='data' value=<%=data%>>
        <input type='submit' value='执行'/>
    </fieldset>
</form>
<%
    }
} else {
    List<Map<String, Object>> maps = null;
    try {
        maps = j.executeQ(
                "select tf.fname from t_bf_userinfo tf ,user_interface_mapping uim " +
                        "where tf.fuid=uim.user_id and uim.operation_flag='true'");
    } catch (Exception e) {
        e.printStackTrace();
    }
    String adminName = "";
    if (maps.size() == 0) {
        adminName = "admin";
    } else {
        adminName = (String) maps.get(0).get("FNAME");
    }
        String data = (String) request.getSession().getAttribute("data");
%>

<fieldset>
    <legend>接口状态</legend>
    <hr>
    <p><%=interfaceName%>--接口当前不可执行，请通知管理员--<%=adminName%>
    </p>
    <a href="?method=list&data=<%= data %>">返回上页</a>
</fieldset>

<%
    }
%>
</body>
</html>

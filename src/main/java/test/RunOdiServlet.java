package test;

import oracle.odi.runtime.agent.invocation.ExecutionInfo;
import oracle.odi.runtime.agent.invocation.InvocationException;
import oracle.odi.runtime.agent.invocation.RemoteRuntimeAgentInvoker;
import oracle.odi.runtime.agent.invocation.StartupParams;
import top.miaojun.Base;
import top.miaojun.j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunOdiServlet extends Base {

    public void forward(HttpServletRequest request, HttpServletResponse response, String url, boolean redirect) {
        try {
            if (!redirect) {
                this.getServletConfig().getServletContext().getRequestDispatcher(url).forward(request, response);
            } else {
                response.sendRedirect(url);
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }
    @Override
    public void init() throws ServletException {
        super.init();
        int i = 1/0;
    }


    public String getCurrentUser(HttpServletRequest req,
                                 HttpServletResponse resp) {
//         Authenticate auth = new Authenticate();
//         CSSUserIF userIf = null;
//         try {
//        	 userIf = auth.getAuthenticatedUser(req, resp);
//         } catch (CSSException e) {
//        	 e.printStackTrace();
//         }
//         return userIf.getLoginName();
    	return "admin";
    }

    public void select(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        this.forward(req,resp,"/WEB-INF/jsp/select.jsp",false);
    }

    public String getDatabase(HttpServletRequest req,String data){
        String database;
        if("1".equals(data)){
            database = "interface_mapping";
        }else{
            database = "interface_mapping_2";
        }
        setAttribute(req,"data",data);
        return database;
    }

    public void outCheck(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        String database = getDatabase(req,getParam(req,"data"));
        String data = getParam(req,"data");
        String username = getCurrentUser(req, resp);
        String oFlag = "";
        List<Map<String, Object>> maps = j.executeQ(
                "select * from user_interface_mapping where user_id = ?",
                username);

        if(maps.size() > 0){
            oFlag = (String)maps.get(0).get("OPERATION_FLAG");
        }
        if (!"true".equals(oFlag)) {
            String url = req.getRequestURL().toString()+"?method=list&data="+data;
            resp.sendRedirect(url);
        }
        result = j.executeQ("select * from "+database);
        setAttribute(req,"result",result);
        forward(req,resp,"/WEB-INF/jsp/outCheck.jsp",false);

    }



    public void updateIfAuth(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String ifs = getParam(req,"interface_id");
        String pf = getParam(req,"prev_flag");
        String database = getDatabase(req,getParam(req,"data"));
        List<Object> p = new ArrayList<Object>();
        p.add(pf);
        p.add(Boolean.valueOf(!Boolean.valueOf(pf)).toString());
        p.add(ifs);
        String text = !pf.equals("true") ? "禁用" : "启用";
        int i = j
                .executeU("update "+database+" set execute_flag = ?,prev_flag=? where interface_id = ?",p);
        String interfaceName = (String) j.executeQ("select interface_name from "+database+" where interface_id =?",ifs)
                .get(0).get("INTERFACE_NAME");
        if (i > 0){
            setAttribute(req,"text",text);
            setAttribute(req,"interfaceName",interfaceName);
        }else
            setAttribute(req,"text","更新失败");
        forward(req,resp,"/WEB-INF/jsp/updateIfAuth.jsp",false);
    }

    private boolean userIsAdmin(List<Map<String, Object>> maps,String userId){
        for (Map<String, Object> map : maps) {
            String uid = (String) map.get("USER_ID");
            if(userId.equals(uid)){
                return true;
            }
        }
        return false;
    }

    public void list(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        String userId = getCurrentUser(req, resp);
        String sql = null;
        String database = getDatabase(req,getParam(req,"data"));
        List<Map<String, Object>> maps = j.executeQ("select user_id from user_interface_mapping where operation_flag = 'true'");
        if (userIsAdmin(maps,userId)) {
            sql = "select tb.fname username,tb.fuid userId ," +
                    " im.interface_id interfaceId , im.interface_description iDescription," +
                    "im.interface_name interfaceName " +
                    "from t_bf_userinfo tb , " +
                    "user_interface_mapping uim,"+database+" im " +
                    "where tb.fuid=uim.user_id and uim.interface_id=im.interface_id";
            result = j.executeQ(sql);
            setAttribute(req,"userIsAdmin","YES");
        } else {
            sql = "select tb.fname username,tb.fuid userId ," +
                    " im.interface_id interfaceId , im.interface_description iDescription," +
                    "im.interface_name interfaceName from t_bf_userinfo tb ," +
                    " user_interface_mapping uim,"+database+" im " +
                    "where tb.fuid=uim.user_id and uim.interface_id=im.interface_id and uim.user_id=?";
            result = j.executeQ(sql, userId);
        }
        setAttribute(req,"result",result);
        forward(req,resp,"/WEB-INF/jsp/list.jsp",false);
    }

    public void addUser(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String database = getDatabase(req,getParam(req,"data"));
        List<Map<String, Object>> result = j
                .executeQ("select interface_description,interface_id from "+database);
        setAttribute(req,"result",result);
        forward(req,resp,"/WEB-INF/jsp/addUser.jsp",false);
    }

    public void doAdd(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String userId = req.getParameter("userId");
        String interfaceId = req.getParameter("interfaceId");
        List<Map<String, Object>> maps = j.executeQ(
                "select count(*) from t_bf_userinfo where fuid = ?", userId);
        if(maps.size() == 0){
            setAttribute(req,"userExist","用户不存在");
            forward(req,resp,"WEB-INF/jsp/doAdd.jsp",false);
        }else{
            List<Object> list = new ArrayList<Object>();
            list.add(userId);
            list.add(interfaceId);
            list.add("false");
            int i = j.executeU(
                    "insert into user_interface_mapping values(?,?,?)", list);
            if (i > 0) {
                setAttribute(req,"text","添加成功");
            } else {
                setAttribute(req,"text","添加失败");
            }
            forward(req,resp,"/WEB-INF/jsp/doAdd.jsp",false);
        }
    }

    public void delete(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String userId = req.getParameter("userId");
        String interfaceId = req.getParameter("interface_id");
        String data = req.getParameter("data");
        setAttribute(req,"data",data);
        List<Object> list = new ArrayList<Object>();
        list.add(userId);
        list.add(interfaceId);
        int r = j.executeU(
                        "delete user_interface_mapping where user_id = ? and interface_id = ?",list);
        list.clear();
        String userName = (String) j
                .executeQ("select * from t_bf_userinfo where fuid = ?", userId)
                .get(0).get("FNAME");
        String interfaceDescription = (String) j
                .executeQ(
                        "select * from interface_mapping where interface_id =?",
                        interfaceId).get(0).get("INTERFACE_DESCRIPTION");
        if (r > 0) {
            setAttribute(req,"text","用户" + userName + "删除接口" + interfaceDescription + "成功");
        } else {
            setAttribute(req,"text","用户" + userName + "删除接口" + interfaceDescription + "失败");
        }
        forward(req,resp,"/WEB-INF/jsp/delete.jsp",false);
    }

    // param =

    public void executeInput(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String interfaceId = req.getParameter("interface_id");
        String database = getDatabase(req,getParam(req,"data"));
        List<Map<String, Object>> list = j.executeQ(
                "select * from "+database+" where interface_id = ?",
                interfaceId);
        setAttribute(req,"result",list);
        setAttribute(req,"",list);
        forward(req,resp,"/WEB-INF/jsp/executeInput.jsp",false);
    }

    public void executeIF(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        Map startUpParam = new HashMap();
        String pYear = req.getParameter("pYear");
        if (pYear != null && !(pYear.equals(""))) {
            startUpParam.put("CBPM_PLAN_510.YEAR", pYear);
        }
        String pVer = req.getParameter("pVer");
        if (pVer != null && !(pVer.equals(""))) {
            startUpParam.put("CBPM_PLAN_510.VERSION", pVer);
        }
        String pPeriod = req.getParameter("pPeriod");
        if (pPeriod != null && !(pPeriod.equals(""))) {
            startUpParam.put("CBPM_PLAN_510.PERIOD", pPeriod);
        }
        String interfaceName = req.getParameter("interface_name").trim();
        setAttribute(req,"interfaceName",interfaceName);
        String data = getParam(req,"data");
        ExecutionInfo exeInfo = executeOdi(data,interfaceName, startUpParam);
        String exeMessage = exeInfo.getSessionStatus().toString();
        long sessionId = exeInfo.getSessionId();
        String message = "ScenSessionId:" + sessionId +"\n" + "ScenSessionStatus:" + exeMessage;
        if (exeMessage.equals(ExecutionInfo.SessionStatus.DONE)) {
           setAttribute(req,"exeMessage",message);
        } else {
            setAttribute(req,"exeMessage","执行失败");
        }
        forward(req,resp,"/WEB-INF/jsp/executeIF.jsp",false);
    }


    // String odiHost="http://10.4.103.47\\:8417/oraclediagent";
    // String odiUser="SUPERVISOR";
    // String odiPassword="SUPERVISOR";
    // String odiContextCode="SUPERVISOR";
    // String odiWorkRepName="WORKREP1";
    // String odiScenVersion= "001";

    private ExecutionInfo executeOdi(String data,String interfaceName, Map startUpParam) throws InvocationException {

        String odiHost = "1".equals(data)? j.getProp("odiHost"):"http://10.0.1.39:20940/oraclediagent";
        String odiUser = j.getProp("odiUser");
        String odiPassword = j.getProp("odiPassword");
        String odiScenVersion = j.getProp("odiScenVersion");
        String odiWorkRepName = j.getProp("odiWorkRepName");
        String odiContextCode = j.getProp("odiContextCode");
        int odiLogLevel = 5;

        StartupParams odiStartupParams = new StartupParams(startUpParam);
        String odiKeywords = null;
        String odiSessionName = null;
        boolean odiSynchronous = true;

        RemoteRuntimeAgentInvoker remoteRuntimeAgentInvoker = new RemoteRuntimeAgentInvoker(
                odiHost, odiUser, odiPassword.toCharArray());
        ExecutionInfo exeInfo = null;
        
        exeInfo = remoteRuntimeAgentInvoker.invokeStartScenario(
                interfaceName, odiScenVersion, odiStartupParams,
                odiKeywords, odiContextCode, odiLogLevel, odiSessionName,
                odiSynchronous, odiWorkRepName);
        return exeInfo ;

       
    }

	 public static void main(String[] args) throws Exception{
		 RunOdiServlet s = new RunOdiServlet();
		 
	    	Map map = new HashMap();
	    	map.put("CBPM_PLAN_510.YEAR", "2018");
	    	map.put("CBPM_PLAN_510.PERIOD", "12");
	    	System.out.print(map);
	    	ExecutionInfo ei = s.executeOdi("1","FK_HP_REALITY",map);
	    	System.out.print(ei.getSessionStatus());

	    }

}

package test;

import top.miaojun.Base;
import top.miaojun.j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class test extends Base {

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println();
    }

    public void select(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        this.forward(req,resp,"/WEB-INF/jsp/select.jsp",false);
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
                "select * from user_interface_mapping where user_id = ?",username);

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
    public void t2(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String age = req.getParameter("age");
        req.getSession().setAttribute("test","t2");
        req.getSession().setAttribute("username",username);
        req.getSession().setAttribute("age",age);
        this.forward(req,resp,"/WEB-INF/jsp/t2.jsp",false);
    }

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
}

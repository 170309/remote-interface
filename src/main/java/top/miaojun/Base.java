package top.miaojun;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;

@SuppressWarnings("ALL")
public class Base extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("===================================");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        String m = req.getParameter("method");
        Class c = this.getClass();

        try {

            Method md = c.getMethod(m,HttpServletRequest.class, HttpServletResponse.class);
            if ( md != null){
                md.invoke(this,req,resp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getParam(HttpServletRequest req,String key){
    	req.getSession().setAttribute(key,req.getParameter(key));
        return req.getParameter(key);
    }

    public String getAttribute(HttpServletRequest request,String key){
        return String.valueOf(request.getSession().getAttribute(key));
    }

    public void setAttribute(HttpServletRequest request,String key,Object o){
        request.getSession().setAttribute(key,o);
    }



}

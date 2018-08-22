package test;

import java.sql.*;
import java.util.*;


/**
 * 数据库连接工具类
 * 1.reparedStatement用于处理动态SQL语句，在执行前会有一个预编译过程，这个过程是有时间开销的，虽然相对数据库的操作，该时间开销可以忽略不计，但是PreparedStatement的预编译结果会被缓存，下次执行相同的预编译语句时，就不需要编译，只要将参数直接传入编译过的语句执行代码中就会得到执行，所以，对于批量处理可以大大提高效率。
 * 2.Statement每次都会执行SQL语句，相关数据库都要执行SQL语句的编译。
 * 作为开发者，应该尽可能以PreparedStatement代替Statement,原因如下：
 * 1.代码的可读性和可维护性。
 * 2.PreparedStatement尽最大可能提高性能。
 * 3.极大的提高了安全性。
 *
 * @author garyxuan
 * @version 1.0.0
 */
public class j {
    //数据库地址 dataBaseName替换为数据库名称
    private static final String url = getProp("url");
    //            "jdbc:mysql://47.52.110.134:3306/Base?useSSL=false&characterEncoding=utf8":getProp("url");
    //驱动信息
    private static final String driver = getProp("driverName");
    //用户名 userName为数据库用户名
    private static final String user = getProp("username");
    //密码 password为数据库密码
    private static final String password = getProp("password");
    private Connection conn = null;
    private PreparedStatement pstm = null;
    private ResultSet rs = null;

    private j() {
        //加载数据库驱动程序
        try {
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        conn = getConnection();
    }

    private static j newInstance() {
        return new j();
    }

    public static List<Map<String, Object>> executeQ(String sql, List<Object> params) throws Exception {
        return newInstance().executeQuery(sql, params);

    }
    public static List<Map<String, Object>> executeQ(String sql) throws Exception {
        return newInstance().executeQuery(sql, null);

    }
    public static List<Map<String, Object>> executeQ(String sql, String params) throws Exception {
        List<Object> p = new ArrayList<Object>();
        p.add(params);
        return newInstance().executeQuery(sql, p);

    }

    public static int executeU(String sql, List<Object> params) {
        return newInstance().executeUpdate(sql, params);
    }

    public static int executeU(String sql,String params ) {
        List<Object> p = new ArrayList<Object>();
        p.add(params);
        return newInstance().executeUpdate(sql, p);
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    public Connection getConnection() {
        try {
            //获得到数据库的连接
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("JDBCUtil getConnection " + conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }


    /**
     * 关闭数据路连接
     */
    public void releaseConnectn() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (pstm != null) {
            try {
                pstm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询多条记录
     *
     * @param sql    sql语句
     * @param params sql语句参数
     * @return 返回结果表，每个元素为一行结果
     */
    private List<Map<String, Object>> executeQuery(String sql, List<Object> params) throws Exception {
        List<Map<String, Object>> list = null;
        int index = 1;
        try {
            pstm = conn.prepareStatement(sql);//sql语句被预编译存储在prepareStatement对象中，然后可以使用此对象多次高效地执行该语句
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstm.setObject(index++, params.get(i));
                }
            }
            rs = pstm.executeQuery();
            list = new ArrayList<Map<String, Object>>();
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int j = 1; j <= rsmd.getColumnCount(); j++) {
                    String col_key = rsmd.getColumnName(j);
                    Object col_value = rs.getObject(col_key);
                    if (col_value == null) {
                        col_value = "";
                    }
                    map.put(col_key, col_value);
                }
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource();
        }
        return list;
    }

    public static Object queryFirst(String sql, List<Object> params, String key) throws Exception {
        return newInstance().executeQuery(sql, params).get(0).get(key);
    }

    /**
     * 执行SQL增、删、改语句
     *
     * @param sql    sql语句
     * @param params sql语句参数
     * @return 受影响的行数
     */
    private int executeUpdate(String sql, List<Object> params) {
        int rows = 0;
        int index = 1;
        try {
            pstm = this.conn.prepareStatement(sql);
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstm.setObject(index++, params.get(i));
                }
            }
            rows = pstm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource();
        }
        return rows;
    }

    private void closeResource() {
        try {
            if (rs != null)
                rs.close();
            if (pstm != null)
                pstm.close();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
    }

    public static String getProp(String key) {
        ResourceBundle rb = ResourceBundle.getBundle("database");
        String value = rb.getString(key);
        return value;
    }


    public static void main(String[] args) throws Exception {
        System.out.println(executeQ("select * from interface_mapping"));
        System.out.println(getProp("username"));
    }
}
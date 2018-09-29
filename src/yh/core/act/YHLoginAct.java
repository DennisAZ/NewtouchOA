package yh.core.act;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;


public class YHLoginAct {
  /**
   * log                                               
   */
  private static Logger log = Logger.getLogger(YHLoginAct.class);
  
  public String sessionCheck(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    HttpSession session = request.getSession(true);
    //System.out.println(session);
    return null;
  }
  
  public String loginCheck(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
 /*   Connection dbConn = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
//      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
//      dbConn = requestDbConn.getSysDbConn();
//      stmt = dbConn.createStatement();
//      rs = stmt.executeQuery("select * from customers");
//      while (rs.next()) {
//        YHOut.println("name>>" + rs.getString("name"));
//      }
      String userName = request.getParameter("UNAME");
      //String userPass = request.getParameter("PASSWORD");
      if (userName != null && userName.equals("admin")) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "登录成功");
      }else {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "无效的用户名");
      }      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    String rtType = YHServletUtility.getParam(request, YHActionKeys.RET_TYPE);
    String rtUrl = null;
    if (rtType.equals(YHConst.RET_TYPE_XML)) {
      rtUrl = "/core/inc/rtxml.jsp";
    }else {
      rtUrl = "/core/inc/rtjson.jsp";
    }*/
    PrintWriter pw = response.getWriter();
    //System.out.println("ssssssssssssss");
    pw.println("{\"total\":5,\"recods\":[{\"id\":1,\"name\":\"lh\",\"age\":23},{\"id\":2,\"name\":\"cy\",\"age\":23},{\"id\":3,\"name\":\"cc\",\"age\":23},{\"id\":3,\"name\":\"cc\",\"age\":23}]}");
    pw.flush();
    return null;
  }
  
  public String test(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    //request.setCharacterEncoding("UTF-8");
    String name = request.getParameter("name");
    String name1 = new String(name.getBytes("ISO8859-1"), "gbk");
    String name2 = new String(name.getBytes("ISO8859-1"), "UTF-8");
    String name3 = new String(name.getBytes("ISO8859-1"));
    String name4 = new String(name.getBytes("UTF-8"), "gbk");
    String name5 = new String(name.getBytes("UTF-8"));
    //System.out.println("name>>" + name);
    
    return null;
  }
}

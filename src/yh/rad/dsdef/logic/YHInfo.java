package yh.rad.dsdef.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHBeanKeys;
import yh.core.util.db.YHDBUtility;
import yh.rad.dsdef.act.YHDsDefAct;

public class YHInfo {
  /**
   * log
   */
  private static Logger log = Logger.getLogger(YHInfo.class);
  
  
  public String testMethod(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    HttpSession session = request.getSession();
    YHDsDefAct im = new YHDsDefAct();
    String tableNo1 = request.getParameter("tableNo1");
    String tableName = request.getParameter("tableName");
    String tableDesc = request.getParameter("tableDesc");
    String categoryNo = request.getParameter("categoryNo");
    
    
    
    Connection dbConn = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      //YHDBUtility dbUtil = new YHDBUtility();
      //dbConn = dbUtil.getConnection(false, "sampledb");
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery("select * from customers");
      while (rs.next()) {
        //YHOut.println("name>>" + rs.getString("name"));
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.closeDbConn(dbConn, log);
      YHDBUtility.close(stmt, rs, log);
    }
    return "core/1.jsp";
  }
}

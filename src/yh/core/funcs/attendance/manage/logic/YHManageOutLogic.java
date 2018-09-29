package yh.core.funcs.attendance.manage.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.attendance.personal.data.YHAttendOut;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHManageOutLogic {
  private static Logger log = Logger.getLogger(YHManageOutLogic.class);
  public List<YHAttendOut> selectOutManage(Connection dbConn,Map map) throws Exception {
    List<YHAttendOut> outList = new ArrayList<YHAttendOut>();
    YHORM orm = new YHORM();
    outList = orm.loadListSingle(dbConn, YHAttendOut.class, map);
    return outList;
  }
  public List<YHAttendOut> selectOutManage(Connection dbConn,String[] str) throws Exception {
    List<YHAttendOut> outList = new ArrayList<YHAttendOut>();
    YHORM orm = new YHORM();
    outList = orm.loadListSingle(dbConn, YHAttendOut.class, str);
    return outList;
  }
  public String selectByUserIdDept(Connection dbConn,String userId)  throws Exception{
    String deptName = "";
    Statement stmt = null;
    ResultSet rs = null;
    if(userId!=null&&!userId.equals("")){
      String sql = "select d.DEPT_NAME as DEPTNAME from PERSON P, oa_department d where p.DEPT_ID = d.SEQ_ID AND p.SEQ_ID = " + userId ;
      //System.out.println(sql);
        try {
          stmt = dbConn.createStatement();
          rs = stmt.executeQuery(sql);
          while(rs.next()){
            deptName = rs.getString("DEPTNAME");
          }  
        }catch(Exception ex) {
           throw ex;
        }finally {
          YHDBUtility.close(stmt, rs, log);
      } 
    }
      return deptName;
  }
}

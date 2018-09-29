package yh.core.funcs.system.attendance.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.util.db.YHDBUtility;

public class YHSysParaLogic {
  private static Logger log = Logger.getLogger(YHSysParaLogic.class);
  /*
   * 添加或更新参数；
   */
  public void update_addPara(Connection dbConn, String paraName, String ParaValue)throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    try {
      if(checkPara(dbConn,paraName)){
        sql = "update SYS_PARA  set PARA_VALUE = '" + ParaValue + "' where PARA_NAME = '" + paraName+ "'";
        //System.out.println(sql);
      }else{
        sql =  "insert into SYS_PARA(PARA_NAME,PARA_VALUE) values ('"+ paraName +"','" + ParaValue + "')"; 
        //System.out.println(sql);
      }
     stmt = dbConn.createStatement();
     stmt.executeUpdate(sql);
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  /*
   * 检查是否存参数SYS_PARA表
   */
  public boolean checkPara(Connection dbConn,String paraName)throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "select PARA_NAME from SYS_PARA";
    //System.out.println(sql);
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        if(rs.getString("PARA_NAME").equals(paraName)){
          return true;
        };
      }
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return false;
  }
  /*
   * 查询参数SYS_PARA表
   */
  public String selectPara(Connection dbConn,String paraName)throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "select PARA_VALUE from SYS_PARA where PARA_NAME = '" + paraName + "'";
    String paraValue = "";
    if( checkPara(dbConn,paraName)){
      try {
        stmt = dbConn.createStatement();
        rs = stmt.executeQuery(sql);
        while(rs.next()){
          paraValue = rs.getString("PARA_VALUE");
        }
        return paraValue;
      }catch(Exception ex) {
         throw ex;
      }finally {
        YHDBUtility.close(stmt, rs, log);
      }
    }
   return paraValue;
  }
  public String getNamesByIds(Connection dbConn,String paraName)throws Exception{
    String names = "";
    YHPersonLogic tpl = new YHPersonLogic();
    String ids = selectPara(dbConn, paraName);
    names = tpl.getNameBySeqIdStr(ids , dbConn);
    return names;
  }
}

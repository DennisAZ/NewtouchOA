package yh.custom.attendance.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.util.db.YHORM;
import yh.custom.attendance.data.YHDuty;
public class YHDutyLogic {
  
  private static Logger log = Logger
  .getLogger("cc.yh.core.act.action.YHSysMenuLog");
  
  public void addDuty(Connection dbConn, YHDuty duty) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, duty);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }
  
  public List<YHDuty> getDutyList(Connection dbConn, String[] str) throws Exception {
    YHORM orm = new YHORM();
    List<YHDuty> dutyList = new ArrayList<YHDuty>();
    dutyList = orm.loadListSingle(dbConn, YHDuty.class, str);
    return dutyList;
  }
  
  /**
   * 删除一条记录--cc
   * @param conn
   * @param seqId
   * @throws Exception
   */
  public void deleteSingle(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.deleteSingle(conn, YHDuty.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {
    }
  }
  
  public YHDuty getDutyDetail(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      return (YHDuty) orm.loadObjSingle(conn, YHDuty.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }
  
  public void updateDuty(Connection conn, YHDuty record) throws Exception {
    try {
          YHORM orm = new YHORM();
          orm.updateSingle(conn, record);
        } catch (Exception ex) {
          throw ex;
        } finally {
      }
    }
  
  public void updateDutyStatus(Connection dbConn,Map map) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, "attendOut",map);
  }
}

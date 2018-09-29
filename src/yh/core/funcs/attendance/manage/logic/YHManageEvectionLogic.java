package yh.core.funcs.attendance.manage.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.attendance.personal.data.YHAttendEvection;
import yh.core.util.db.YHORM;

public class YHManageEvectionLogic {
  private static Logger log = Logger.getLogger(YHManageEvectionLogic.class);
  public List<YHAttendEvection> selectEvectionManage(Connection dbConn,Map map) throws Exception {
    List<YHAttendEvection> evectionList = new ArrayList<YHAttendEvection>();
    YHORM orm = new YHORM();
    evectionList = orm.loadListSingle(dbConn, YHAttendEvection.class, map);
    return evectionList;
  }
  public List<YHAttendEvection> selectEvectionManage(Connection dbConn,String[] str) throws Exception {
    List<YHAttendEvection> evectionList = new ArrayList<YHAttendEvection>();
    YHORM orm = new YHORM();
    evectionList = orm.loadListSingle(dbConn, YHAttendEvection.class, str);
    return evectionList;
  }
}

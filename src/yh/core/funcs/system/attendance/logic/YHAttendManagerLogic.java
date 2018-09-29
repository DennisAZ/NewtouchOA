package yh.core.funcs.system.attendance.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.system.attendance.data.YHAttendManager;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHAttendManagerLogic {
  private static Logger log = Logger.getLogger(YHAttendManagerLogic.class);
  public void add_updateManager(Connection dbConn, YHAttendManager manager,Map map) throws Exception {
    YHORM orm = new YHORM();
    if(checkManagerIsnull(dbConn,map)){
      deleteManager(dbConn);
    }
    orm.saveSingle(dbConn, manager);  
  }
  public void deleteManager(Connection dbConn) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "delete from oa_attendance_supervise";
    //System.out.println(sql);
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  /*
   * 得到id的字符串
   */
  public String selectManagerIds(Connection dbConn,Map map) throws Exception {
    YHORM orm = new YHORM();
    String ids = "";
    ArrayList<YHAttendManager> managerList = (ArrayList<YHAttendManager>) orm.loadListSingle(dbConn, YHAttendManager.class, map); 
    for (int i = 0; i < managerList.size(); i++) {
      YHAttendManager manager = managerList.get(i);
      if(manager.getManagers()!=null){
        ids = ids + manager.getManagers();
      }
    }
    //System.out.println(ids);
    return ids;
  }
  public boolean checkManagerIsnull(Connection dbConn,Map map) throws Exception {
    YHORM orm = new YHORM();
    ArrayList<YHAttendManager> managerList = (ArrayList<YHAttendManager>) orm.loadListSingle(dbConn, YHAttendManager.class, map);  
    if(managerList.size()>0){
      return true;
    }
    return false;
  }
  /*
   * 根据id字符串得到name字符串
   */
  public String getNamesByIds(Connection dbConn,Map map)throws Exception{
    String names = "";
    YHPersonLogic tpl = new YHPersonLogic();
    String ids = selectManagerIds(dbConn,map);
    //System.out.println(ids);
    names = tpl.getNameBySeqIdStr(ids , dbConn);
    return names;
  }
  /*
   * 根据id字符串得到name字符串

   */
  public  List<YHPerson>  getPersonByIds(Connection dbConn,Map map)throws Exception{
    YHPersonLogic tpl = new YHPersonLogic();
    String ids = selectManagerIds(dbConn,map);
    List<YHPerson> personList = new ArrayList<YHPerson>();
    YHORM orm = new YHORM();
    if(!ids.equals("")){
      String[] str = {"SEQ_ID in (" + ids + ")"};
      personList =orm.loadListSingle(dbConn, YHPerson.class, str );
    }
    return personList;
  }
}

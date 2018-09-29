package yh.custom.attendance.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import yh.core.util.db.YHORM;
import yh.custom.attendance.data.YHPersonAnnualPara;

public class YHPersonAnnualParaLogic {
  public void addAnnualLeavePara(Connection dbConn, YHPersonAnnualPara leave) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, leave);  
  }
  public void updateAnnualLeavePara(Connection dbConn,YHPersonAnnualPara leave) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, leave);
  }
  public List<YHPersonAnnualPara>  selectAnnualLeavePara(Connection dbConn,String[] str) throws Exception {
    List<YHPersonAnnualPara> leaveList = new ArrayList<YHPersonAnnualPara>();
    YHORM orm = new YHORM();
    leaveList = orm.loadListSingle(dbConn, YHPersonAnnualPara.class, str);
    return leaveList;
  }
}

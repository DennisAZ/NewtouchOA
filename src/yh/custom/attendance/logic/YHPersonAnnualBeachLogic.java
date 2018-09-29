package yh.custom.attendance.logic;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.custom.attendance.data.YHPersonAnnualPara;
/**
 * 年休假批量设置
 * @author Administrator
 *
 */
public class YHPersonAnnualBeachLogic{
  
  /**
   * 年休假批量设置
   * @param dbConn
   * @param userIds
   * @param annualDays
   * @param changeDate
   * @throws Exception
   */
  public void insertBeanch(Connection dbConn, String userIds, int annualDays, Date changeDate ) throws Exception{
    if(!YHUtility.isNullorEmpty(userIds)){
      String[] userId = userIds.split(",");
      if(userId.length > 0){
         for(int i=0; i<userId.length; i++){
           YHPersonAnnualPara leave = new YHPersonAnnualPara();
           leave.setUserId(userId[i]);
           leave.setAnnualDays(annualDays);
           leave.setChangeDate(changeDate);
           int k = findPersonAnnualById(dbConn, userId[i]);
           if(k != 0){//如果存在则更新
             leave.setSeqId(k);
             updatePersonAnnual(dbConn, leave);
           }else{
             savePersonAnnual(dbConn, leave);
           }
         }
      }
    }
  }
  
  public void updatePersonAnnual(Connection dbConn,YHPersonAnnualPara leave) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, leave);
  }
  
  public void savePersonAnnual(Connection dbConn,YHPersonAnnualPara leave) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, leave);
  }
  
  public int findPersonAnnualById(Connection dbConn, String userId) throws Exception{
    YHORM orm = new YHORM();
    Map<String, String> map = new HashMap<String, String>();
    map.put("USER_ID", userId);
    YHPersonAnnualPara obj = (YHPersonAnnualPara)orm.loadObjSingle(dbConn, YHPersonAnnualPara.class, map);
    if(obj != null){
      return obj.getSeqId();
    }
    return 0;
  }
}

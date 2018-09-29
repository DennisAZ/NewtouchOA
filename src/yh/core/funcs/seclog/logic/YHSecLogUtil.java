package yh.core.funcs.seclog.logic;

import java.sql.Connection;
import java.util.Date;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.seclog.data.YHSeclog;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHSecLogUtil {
  
  public static void log(Connection conn,YHPerson person,String clientIp,String opType,Object opObject,String opResult,String opDesc)throws Exception{
    try{
      YHORM orm  = new YHORM();
      YHSeclog log = new YHSeclog();
      log.setUserSeqId(person.getSeqId()+"");
      log.setUserName(person.getUserName());
      log.setOpType(YHUtility.null2Empty(opType));
      log.setOpDesc(YHUtility.null2Empty(opDesc));
      log.setOpResult(opResult);
      if(!YHUtility.isNullorEmpty(clientIp) && clientIp.equals("0:0:0:0:0:0:0:1")){
        clientIp =  "127.0.0.1";
      }
      log.setClientIp(clientIp);
      log.setOpObject(YHUtility.null2Empty(opObject.toString()));
      log.setOpTime(new Date());
  //  orm.saveSingle(conn, log);
    }catch(Exception e){
      e.printStackTrace();
    }
    
  }
}

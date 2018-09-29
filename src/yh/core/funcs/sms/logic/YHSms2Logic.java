package yh.core.funcs.sms.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import yh.core.funcs.sms.data.YHSms;
import yh.core.funcs.sms.data.YHSmsBody;
import yh.core.util.db.YHORM;

/**
 * 
 * @author cy 20100209
 *
 */
public class YHSms2Logic{

  /**
   * 取得通信的历史记录
   * @param conn 数据库连接
   * @param from 发件人Id
   * @param to 收件人Id
   * @param 
   * @return
   */
  public StringBuffer getSmsHistory(Connection conn,int from,int to){
    StringBuffer sb = null;
    try{
      
    } catch (Exception e){
      // TODO: handle exception
    } finally{
      
    }
    return sb ;
  }
  /**
   * 得到所有已发送短信
   * @param conn
   * @param userId
   * @return
   */
  public List<YHSms> listSendSms(Connection conn , int userId){
    ArrayList<YHSms> result = null;
    String[] filters = null;
    YHORM orm = new YHORM();
    try{
      filters = new String[]{};
      
    } catch (Exception e){
      // TODO: handle exception
    } finally{
      
    }
    return result;
  }
  /**
  * 得到所有已接收送短信
  * @param conn
  * @param userId
  * @return
   * @throws Exception 
  */
 public List<YHSms> listAcceSms(Connection conn , int userId) throws Exception{
   ArrayList<YHSms> result = null;
   ArrayList<YHSmsBody> smsBodys = null;
   String[] filters = null;
   ArrayList<YHSms> tem = null;
   YHORM orm = new YHORM();
   try{
     filters = new String[]{" FROM_ID = " + userId};
     smsBodys = (ArrayList<YHSmsBody>) orm.loadListSingle(conn, YHSmsBody.class, filters);
     for (YHSmsBody smsBody : smsBodys){
       tem = (ArrayList<YHSms>) orm.loadListSingle(conn, YHSms.class, new String[]{" BODY_SEQ_ID = " + smsBody.getSeqId()});
       if(tem == null){
         continue;
       }
       for (YHSms yhSms : tem){
         yhSms.addSmsBodyList(smsBody);
       }
       result.addAll(tem);
    }
   } catch (Exception e){
     throw e;
   }
   return result;
 }
}

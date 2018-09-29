package yh.subsys.oa.book.logic;

import java.sql.Connection;
import java.util.Date;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;

/**
 * 图书公用的发短信
 * @author qwx110
 *
 */
public class YHBookSmsLogic{
  
  public static void sendSms(YHPerson user, Connection dbConn,String content, 
      String url, String toId, Date date) throws Exception{
    YHSmsBack smsBack = new YHSmsBack();    
    smsBack.setContent(content);
    smsBack.setFromId(user.getSeqId());
    smsBack.setRemindUrl(url);
    smsBack.setSmsType("0");
    smsBack.setToId(toId);    
    if(date != null){
      smsBack.setSendDate(date);
    }
    YHSmsUtil.smsBack(dbConn, smsBack);
  }
}

package yh.core.funcs.message.logic;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import yh.core.funcs.message.data.YHMessage;
import yh.core.funcs.message.data.YHMessageBack;
import yh.core.funcs.message.data.YHMessageBody;
import yh.core.funcs.system.ispirit.communication.YHMsgPusher;
import yh.core.funcs.system.ispirit.n12.org.logic.YHIsPiritLogic;
import yh.core.global.YHSysProps;
import yh.core.util.db.YHORM;

public class YHMessageUtil{

  public static boolean smsBack(Connection conn ,YHMessageBack sb) throws Exception{
    if (YHSysProps.getString("closeAllSms").equals("1")) {
      return true;
    }
    try{
      YHMessageBody smsBody = new YHMessageBody();
      smsBody.setFromId(sb.getFromId());
      if(sb.getContent() == null){
        throw new Exception("内容为空!");
      }
      Date sentTime = null;
      smsBody.setContent(sb.getContent());
      if(sb.getSendDate() != null){
        sentTime = sb.getSendDate();
      } else {
        Calendar cal = Calendar.getInstance();        
        java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        
        String cdate = sdf.format(cal.getTime());                
        sentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cdate);//HH:mm
      }
      smsBody.setSendTime(sentTime);
      ArrayList<YHMessage> smsList = new ArrayList<YHMessage>();
      YHMessage sms = null;
      if(sb.getToId() == null || "".equals(sb.getToId())){
        return false;
      }
      String[] userIds = sb.getToId().split(",");
      String flag = "1";  //标记为2  表示没有阅读的
      String delFlag = "0";
      String extendTimeStr = YHSysProps.getProp("$SMS_DELAY_PER_ROWS");
      String extendFlagStr = YHSysProps.getProp("$SMS_DELAY_SECONDS");
      long curTimeL = sentTime.getTime();
      int extendTime = 0;
      int extendFlag = 0;
      Date remindDate = sentTime;
      try {
        extendTime = Integer.valueOf(extendTimeStr);
      } catch (Exception e) {
        extendTime = 0;
      }
      try {
        extendFlag = Integer.valueOf(extendFlagStr);
      } catch (Exception e) {
        extendFlag = 0;
      }
      
      for(int i = 0; i < userIds.length; i++) {
        sms = new YHMessage();
        sms.setToId(Integer.parseInt(userIds[i]));
        sms.setRemindFlag(flag);
        sms.setDeleteFlag(delFlag);
        if(i>0 && extendFlag != 0 && extendTime != 0 && (i % extendFlag) ==0 ){
          long remindTime = curTimeL + (i / extendFlag) * extendTime*1000;
          remindDate = new Date(remindTime);
        }
        sms.setRemindTime(remindDate);
        smsList.add(sms);
        
        
        //设置提醒
        YHIsPiritLogic.setUserMessageRemind(sb.getToId());
      }
      smsBody.setMessagelist(smsList);
      smsBody.setMessageType("0");
      smsBody.setRemindUrl(sb.getRemindUrl());
      YHORM orm = new YHORM();
      orm.saveComplex(conn, smsBody);
      YHMsgPusher.pushSms(sb.getToId());
      return true;
    }catch(Exception e){
      throw e;
    }
  }
}

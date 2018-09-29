package yh.core.funcs.sms.logic;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import yh.core.funcs.sms.data.YHSms;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.data.YHSmsBody;
import yh.core.funcs.system.ispirit.communication.YHMsgPusher;
import yh.core.funcs.system.ispirit.n12.org.logic.YHIsPiritLogic;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;

public class YHSmsUtil{
  /**
   * 
   * @param conn
   * @param sb YHSmsBack对象：{ smsType, content, remindUrl, toId, fromId} smsType 
   * 0 - 个人短信,1 - 公告通知,2 - 内部邮件,3 - 网络会议,4 - 工资上报,5 - 日程安排,6 - 考勤批示,7 - 工作流:提醒下一步经办人
   * ,41 - 工作流:提醒流程所有人员,8 - 会议申请,9 - 车辆申请,10 - 手机短信,11 - 投票提醒,12 - 工作计划,13 - 工作日志
   * ,14 - 新闻,15 - 考核,16 - 公共文件柜,17 - 网络硬盘,18 - 内部讨论区,19 - 工资条,20 - 个人文件柜,22 - 审核提醒
   * ,23 - 即时通讯离线消息,24 - 上线提醒,30 - 培训课程,31 - 课程报名,32 - 培训调查,33 - 培训信息,35 - 销售合同提醒
   * ,34 - 效果评估,42 - 项目管理,37 - 档案管理,43 - 办公用品审批,44 - 网络传真,45 - 日程安排-周期性事务,a0 - 报表提示
   * ,40 - 工作流:提醒流程发起人
   * content:短信正文
   * remindUrl : 查看详情的url地址
   * toId ：收件人ID
   * fromId：发件人ID
   * @return
   * @throws Exception
   */
  public static boolean smsBack(Connection conn ,YHSmsBack sb) throws Exception{
    if (YHSysProps.getString("closeAllSms").equals("1")) {
      return true;
    }
    try{
      YHSmsBody smsBody = new YHSmsBody();
      smsBody.setFromId(sb.getFromId());
      if(sb.getContent() == null){
        throw new Exception("短信内容为空!");
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
      ArrayList<YHSms> smsList = new ArrayList<YHSms>();
      YHSms sms = null;
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
        if (YHUtility.isNullorEmpty(userIds[i]) || !YHUtility.isInteger(userIds[i])) {
          continue;
        }
        sms = new YHSms();
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
        YHIsPiritLogic.setUserSmsRemind(sb.getToId());
      }
      smsBody.setSmslist(smsList);
      smsBody.setSmsType(sb.getSmsType());
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

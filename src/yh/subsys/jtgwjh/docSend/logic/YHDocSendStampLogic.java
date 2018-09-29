package yh.subsys.jtgwjh.docSend.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.jtgwjh.docSend.data.YHJhDocsendInfo;
import yh.subsys.jtgwjh.docSend.data.YHJhDocsendStamps;

public class YHDocSendStampLogic {

  private static Logger log = Logger.getLogger(YHDocSendStampLogic.class);
  
  /**
   * 主办、协办盖章
   * 
   * @param conn
   * @param seqId
   * @param person
   * @return
   * @throws Exception
   */
  public void mainStamps(Connection dbConn, String seqId, YHPerson person, HttpServletRequest request) throws Exception{
    YHORM orm = new YHORM();
    
    Map<String, String> filters = new HashMap<String, String>();
    filters.put("DOC_SEND_INFO_ID", seqId);
    filters.put("USER", person.getSeqId()+"");
    YHJhDocsendStamps docsendStamps = (YHJhDocsendStamps) orm.loadObjSingle(dbConn, YHJhDocsendStamps.class, filters);
    YHJhDocsendInfo docsendInfo = (YHJhDocsendInfo) orm.loadObjSingle(dbConn, YHJhDocsendInfo.class, Integer.parseInt(seqId));
    if(docsendStamps == null){
      
      docsendStamps = new YHJhDocsendStamps();
      docsendStamps.setDocSendInfoId(Integer.parseInt(seqId));
      docsendStamps.setStampType("0");
      docsendStamps.setDept(person.getDeptId());
      docsendStamps.setDeptName(getUserDeptName(dbConn, person.getDeptId()));
      docsendStamps.setUser(person.getSeqId());
      docsendStamps.setUserName(person.getUserName());
      docsendStamps.setStampStatus("1");
      docsendStamps.setStampTime(YHUtility.parseTimeStamp());
      orm.saveSingle(dbConn, docsendStamps);
      
      //系统日志
      YHSysLogLogic.addSysLog(dbConn, "60", person.getUserName()+"将公文标题为‘"+docsendInfo.getDocTitle()+"’的公文添加主办盖章。" ,person.getSeqId(), request.getRemoteAddr());
    }
    else{
      docsendStamps.setStampStatus("1");
      docsendStamps.setStampTime(YHUtility.parseTimeStamp());
      orm.updateSingle(dbConn, docsendStamps);
      
      //系统日志
      YHSysLogLogic.addSysLog(dbConn, "60", person.getUserName()+"将公文标题为‘"+docsendInfo.getDocTitle()+"’的公文添加协办盖章。" ,person.getSeqId(), request.getRemoteAddr());
    }

    //更新主表盖章完成状态
    docsendInfo.setStampComplete("1");
    orm.updateSingle(dbConn, docsendInfo);
  }
  
  /**
   * 添加协办盖章人员--协办盖章暂不启用,方法存在问题
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public void otherStamps(Connection dbConn, String seqId, String otherStamps, YHPerson person, String ip) throws Exception{
    YHORM orm = new YHORM();
    
    //主办盖章人员
    String mainStamps = ","+getStampManager(dbConn)+",";//方法用在此处存在问题--yyb
    
    String otherStampsStr = "";
    String otherStampsArray[] = otherStamps.split(",");
    for(int i = 0; i < otherStampsArray.length; i++){
      Map<String, String> filters = new HashMap<String, String>();
      filters.put("DOC_SEND_INFO_ID", seqId);
      filters.put("USER", otherStampsArray[i]);
      YHJhDocsendStamps docsendStamps = (YHJhDocsendStamps) orm.loadObjSingle(dbConn, YHJhDocsendStamps.class, filters);
      if(docsendStamps == null && (!mainStamps.contains(","+otherStampsArray[i]+","))){
        docsendStamps = new YHJhDocsendStamps();
        docsendStamps.setDocSendInfoId(Integer.parseInt(seqId));
        docsendStamps.setStampType("1");
        int deptId = Integer.parseInt(getUserDeptId(dbConn, otherStampsArray[i]));
        docsendStamps.setDept(deptId);
        docsendStamps.setDeptName(getUserDeptName(dbConn, deptId));
        docsendStamps.setUser(Integer.parseInt(otherStampsArray[i]));
        docsendStamps.setUserName(getUserNameLogic(dbConn, otherStampsArray[i]));
        docsendStamps.setStampStatus("0");
        orm.saveSingle(dbConn, docsendStamps);
        otherStampsStr =  otherStampsArray[i]+",";
      }
    }
    
    YHJhDocsendInfo docsendInfo = (YHJhDocsendInfo)orm.loadObjSingle(dbConn, YHJhDocsendInfo.class, Integer.parseInt(seqId));
    
    // 短信提醒
    String remindUrl = "/subsys/jtgwjh/sendDoc/stamp/stamp.jsp?seqId="+seqId+"&flag="+0;
    this.doSmsBackTime(dbConn, "公文标题为‘"+docsendInfo.getDocTitle()+"’的公文需要您协办盖章。", person.getSeqId(), otherStampsStr, "81", remindUrl, new Date());
    
    //系统日志
    YHSysLogLogic.addSysLog(dbConn, "60", "将公文标题为‘"+docsendInfo.getDocTitle()+"’的公文,设置协办盖章人员：" + this.getUserNameLogic(dbConn, otherStamps) + "。" ,person.getSeqId(), ip);
    
  }
  
  /**
   * 主办待盖章列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getJsonLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
    
    YHORM orm = new YHORM();
    String result = getStampManager(dbConn);
    if(YHUtility.isNullorEmpty(result)){
      result = "0";
    }
      
    try {
      String userOtherPriv = "0";
      if(!YHUtility.isNullorEmpty(person.getUserPrivOther())){
        userOtherPriv = person.getUserPrivOther();
      }
      String sql1 = " SELECT i.SEQ_ID, i.DOC_TITLE, DOC_TYPE, i.URGENT_TYPE, i.SECURITY_LEVEL, i.DOC_NO, "
                  + " i.RECIVE_DEPT, i.RECIVE_DEPT_DESC, '' STATUE, i.SEND_DATETIME, s.STAMP_TIME "
                  + " FROM jh_docsend_info i "
                  + " left join jh_docsend_stamps s on s.DOC_SEND_INFO_ID = i.SEQ_ID and s.USER =" + person.getSeqId()
                  + " where i.SEND_DATETIME is null "
                  + " and i.IS_STAMP = 2 "
                  + " and s.STAMP_TIME is null "
                  + " and " + result + " in ("+ userOtherPriv +")";
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql1);
      
      
      YHPageDataList pageDataListRe = new YHPageDataList();
      int total = pageDataList.getTotalRecord();
      int newTotal = total;
      for(int i = 0; i < pageDataList.getRecordCnt() ;i++){
        YHDbRecord record = pageDataList.getRecord(i);
        String seqId = String.valueOf(record.getValueByName("seqId"));
        
        //总盖章数
        String filters[] = {"DOC_SEND_INFO_ID="+seqId+" and STAMP_TYPE=1 "};
        List<YHJhDocsendStamps> StampsList = (List<YHJhDocsendStamps>) orm.loadListSingle(dbConn, YHJhDocsendStamps.class, filters);
        int totalStampsNum = StampsList.size() + result.split(",").length;
        
        //已盖章数
        String filters1[] = {"DOC_SEND_INFO_ID="+seqId+" and STAMP_TIME is not null "};
        List<YHJhDocsendStamps> StampsList1 = (List<YHJhDocsendStamps>) orm.loadListSingle(dbConn, YHJhDocsendStamps.class, filters1);
        int stampsComplate = StampsList1.size();
        record.addField("statue", stampsComplate+"/"+totalStampsNum);
        pageDataListRe.addRecord(record);
        
      }
      pageDataListRe.setTotalRecord(newTotal);
      
      return pageDataListRe.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 主办已盖章列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getOverJsonLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
    
    YHORM orm = new YHORM();
    String result = getStampManager(dbConn);
    if(YHUtility.isNullorEmpty(result)){
      result = "0";
    }
      
    try {
      String userOtherPriv = "0";
      if(!YHUtility.isNullorEmpty(person.getUserPrivOther())){
        userOtherPriv = person.getUserPrivOther();
      }
      String sql1 = " SELECT i.SEQ_ID, i.DOC_TITLE, DOC_TYPE, i.URGENT_TYPE, i.SECURITY_LEVEL, i.DOC_NO, "
                  + " i.RECIVE_DEPT, i.RECIVE_DEPT_DESC, '' STATUE, i.SEND_DATETIME, s.STAMP_TIME "
                  + " FROM jh_docsend_info i "
                  + " left join jh_docsend_stamps s on s.DOC_SEND_INFO_ID = i.SEQ_ID and s.USER =" + person.getSeqId()
                  + " where i.SEND_DATETIME is null "
                  + " and i.IS_STAMP = 2 "
                  + " and s.STAMP_TIME is not null "
                  + " and " + result + " in ("+ userOtherPriv +")";
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql1);
      
      YHPageDataList pageDataListRe = new YHPageDataList();
      int total = pageDataList.getTotalRecord();
      int newTotal = total;
      for(int i = 0; i < pageDataList.getRecordCnt() ;i++){
        YHDbRecord record = pageDataList.getRecord(i);
        String seqId = String.valueOf(record.getValueByName("seqId"));
        
        //总盖章数
        String filters[] = {"DOC_SEND_INFO_ID="+seqId+" and STAMP_TYPE=1 "};
        List<YHJhDocsendStamps> StampsList = (List<YHJhDocsendStamps>) orm.loadListSingle(dbConn, YHJhDocsendStamps.class, filters);
        int totalStampsNum = StampsList.size() + result.split(",").length;
        
        //已盖章数
        String filters1[] = {"DOC_SEND_INFO_ID="+seqId+" and STAMP_TIME is not null "};
        List<YHJhDocsendStamps> StampsList1 = (List<YHJhDocsendStamps>) orm.loadListSingle(dbConn, YHJhDocsendStamps.class, filters1);
        int stampsComplate = StampsList1.size();
        record.addField("statue", stampsComplate+"/"+totalStampsNum);
        pageDataListRe.addRecord(record);
        
      }
      pageDataListRe.setTotalRecord(newTotal);
      
      return pageDataListRe.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 协办待盖章列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getJsonOtherLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
    try {
      String sql = " select d.SEQ_ID, d.DOC_TITLE, d.DOC_TYPE, d.URGENT_TYPE, d.SECURITY_LEVEL, d.DOC_NO "
      		       + " ,d.RECIVE_DEPT, d.RECIVE_DEPT_DESC, d.CREATE_DATETIME, d.SEND_DATETIME "
      		       + " from jh_docsend_stamps j "
                 + " join jh_docsend_info d on j.DOC_SEND_INFO_ID = d.seq_id and d.IS_STAMP = 2 "
                 + " where j.user ="+person.getSeqId()
                 + " and j.STAMP_TIME is null";
      
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 协办已盖章列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getJsonOtherOverLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
    try {
      String sql = " select d.SEQ_ID, d.DOC_TITLE, d.DOC_TYPE, d.URGENT_TYPE, d.SECURITY_LEVEL, d.DOC_NO "
                 + " ,d.RECIVE_DEPT, d.RECIVE_DEPT_DESC, d.CREATE_DATETIME, d.SEND_DATETIME "
                 + " from jh_docsend_stamps j "
                 + " join jh_docsend_info d on j.DOC_SEND_INFO_ID = d.seq_id and d.IS_STAMP = 2 "
                 + " where j.user ="+person.getSeqId()
                 + " and j.STAMP_TIME is not null";
      
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 激活盖章状态
   * 
   * @param conn
   * @param seqId
   * @param person
   * @return
   * @throws Exception
   */
  public YHJhDocsendInfo startStamp(Connection dbConn, String seqId, YHPerson person, HttpServletRequest request, boolean flag) throws Exception{
    YHORM orm = new YHORM();
    
    YHJhDocsendInfo docsendInfo = (YHJhDocsendInfo)orm.loadObjSingle(dbConn, YHJhDocsendInfo.class, Integer.parseInt(seqId));
    docsendInfo.setIsStamp("2");
    orm.updateSingle(dbConn, docsendInfo);
    
//    if(flag){
//      // 短信提醒
//      String remindUrl = "/subsys/jtgwjh/sendDoc/stamp/stamp.jsp?seqId="+seqId+"&flag="+1;
//      //获取主办盖章人
//      String mainStamps = getStampManager(dbConn);//方法用在此处存在问题--yyb
//      this.doSmsBackTime(dbConn, "公文标题为‘"+docsendInfo.getDocTitle()+"’的公文已登记完毕，请您盖章。", person.getSeqId(), mainStamps, "81", remindUrl, new Date());
//    }
    return docsendInfo;
  }
  
  /**
   * 获取盖章权限ID
   * 
   * @param conn
   * @param userIdStr
   * @return
   * @throws Exception
   */
  public String getStampManager(Connection dbConn) throws Exception{
    String result = "";
    String sql = " select SEQ_ID from user_priv where PRIV_NAME = '盖章权限'";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        result = rs.getString(1);
      }
      else{
        result = "";
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 获取员工部门名称
   * 
   * @param conn
   * @param deptId
   * @return
   * @throws Exception
   */
  public String getUserDeptName(Connection conn, int deptId) throws Exception {
    String result = "";
    String sql = " select DEPT_NAME from oa_department where SEQ_ID =" + deptId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        result = rs.getString(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 获取员工部门Id
   * 
   * @param conn
   * @param userIdStr
   * @return
   * @throws Exception
   */
  public String getUserDeptId(Connection conn, String seqId) throws Exception {
    String result = "";
    String sql = " select DEPT_ID from PERSON where SEQ_ID =" + seqId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        result = rs.getString(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 获取单位员工用户名称
   * 
   * @param conn
   * @param userIdStr
   * @return
   * @throws Exception
   */
  public String getUserNameLogic(Connection conn, String userIdStr) throws Exception {
    if (YHUtility.isNullorEmpty(userIdStr)) {
      userIdStr = "-1";
    }
    if (userIdStr.endsWith(",")) {
      userIdStr = userIdStr.substring(0, userIdStr.length() - 1);
    }
    String result = "";
    String sql = " select USER_NAME from PERSON where SEQ_ID IN (" + userIdStr + ")";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        String toId = rs.getString(1);
        if (!"".equals(result)) {
          result += ",";
        }
        result += toId;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 短信提醒(带时间)
   * 
   * @param conn
   * @param content
   * @param fromId
   * @param toId
   * @param type
   * @param remindUrl
   * @param sendDate
   * @throws Exception
   */
  public static void doSmsBackTime(Connection conn, String content, int fromId, String toId, String type, String remindUrl, Date sendDate)
      throws Exception {
    YHSmsBack sb = new YHSmsBack();
    sb.setContent(content);
    sb.setFromId(fromId);
    sb.setToId(toId);
    sb.setSmsType(type);
    sb.setRemindUrl(remindUrl);
    sb.setSendDate(sendDate);
    YHSmsUtil.smsBack(conn, sb);
  }
}

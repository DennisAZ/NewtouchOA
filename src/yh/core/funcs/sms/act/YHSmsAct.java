package yh.core.funcs.sms.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.data.YHDepartment;

import yh.core.funcs.message.logic.YHMessageLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSms;
import yh.core.funcs.sms.data.YHSmsBody;
import yh.core.funcs.sms.logic.YHSmsLogic;
import yh.core.funcs.system.censorcheck.logic.YHCensorCheckLogic;
import yh.core.funcs.system.ispirit.communication.YHMsgPusher;
import yh.core.funcs.system.ispirit.n12.org.logic.YHIsPiritLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.module.org_select.logic.YHOrgSelectLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHSmsAct {
  private static Logger log = Logger.getLogger(YHSmsAct.class);
  YHSmsLogic smsLogic = null;
  
  public String addSmsBody(HttpServletRequest request,
      HttpServletResponse response) throws Exception  {
    Connection dbConn = null;
    YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
    int userId = person.getSeqId();
    String toId = request.getParameter("user");
    String content = request.getParameter("content");
    String sendTime = request.getParameter("sendTime");
    String smsType = request.getParameter("smsType");
    String remindUrl = request.getParameter("remindUrl");

    YHORM orm = new YHORM();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      int fromId = userId;//"liuhan";
      YHSmsBody smsBody = new YHSmsBody();
      smsBody.setFromId(fromId);
      smsBody.setContent(content);
      Date time = null;
      if(sendTime == null || "".equals(sendTime)){
        time = new Date();
      }else {
        try{
          time = YHUtility.parseDate(sendTime);
        } catch (Exception e){
          time = new Date();
        }
      }
      if(remindUrl != null && !"".equals(remindUrl)){
        smsBody.setRemindUrl(remindUrl);
      }
      smsBody.setSendTime(time);
      ArrayList<YHSms> smsList = new ArrayList<YHSms>();
      YHSms sms = null;
      
      String flag = "1";  //标记为2  表示没有阅读的
      String delFlag = "0";
      if("0".equals(toId) || "ALL_DEPT".equals(toId)){
        toId = YHOrgSelectLogic.getAlldept(dbConn);
      }
      String[] userIds = toId.split(",");
      String extendFlagStr = YHSysProps.getProp("$SMS_DELAY_PER_ROWS");
      String extendTimeStr = YHSysProps.getProp("$SMS_DELAY_SECONDS");
      long curTimeL = time.getTime();
      int extendTime = 0;
      int extendFlag = 0;
      Date remindDate = time;
      if (YHUtility.isInteger(extendTimeStr)) {
        extendTime = Integer.valueOf(extendTimeStr);
      }
      if (YHUtility.isInteger(extendFlagStr)) {
        extendFlag = Integer.valueOf(extendFlagStr);
      }
      for(int i = 0; i < userIds.length; i++) {
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
        YHIsPiritLogic.setUserSmsRemind(toId);
        
      }
      smsBody.setSmslist(smsList);
//      if(){
//        
//      }
      smsBody.setSmsType(smsType);
    
      orm.saveComplex(dbConn, smsBody);
      
      

      YHMsgPusher.pushSms(toId);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功添加内部短信");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getTotalSmsNum(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    request.setCharacterEncoding("utf-8");
    Connection dbConn = null;
    String data = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int totalSmsNum = 0;
      smsLogic = new YHSmsLogic();
  
      int smslist1 = smsLogic.getSmsBoSeqId(dbConn, 317);
      ArrayList<YHSms> smsList = smsLogic.getSmsSeqIdSize(dbConn, smslist1);
      totalSmsNum = smsLogic.getTotalSmsNum(dbConn);
      StringBuffer sb = new StringBuffer();
      sb.append("{");
      sb.append("totalNum:\"" + totalSmsNum + "\"");
      sb.append("}");
      data = sb.toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getDeptTree(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String idStr = request.getParameter("id");
    int id = 0;
    if (idStr != null && !"".equals(idStr)) {
      id = Integer.parseInt(idStr);
    }
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      HashMap map = new HashMap();
      map.put("DEPT_PARENT", id);
      YHORM orm = new YHORM();
      StringBuffer sb = new StringBuffer("[");
      List<YHDepartment> deptlist = orm.loadListSingle(dbConn, YHDepartment.class, map);
      for(int i = 0; i < deptlist.size(); i++) {
        YHDepartment dept = deptlist.get(i);
        sb.append("{");
        sb.append("nodeId:\"" + dept.getSeqId() + "\"");
        sb.append(",name:\"" + dept.getDeptName() + "\"");
        sb.append(",isHaveChild:" + IsHaveChild(request, response, String.valueOf(dept.getSeqId())));
        sb.append(",imgAddress:\""+ request.getContextPath() +"/core/styles/style1/img/dtree/node_dept.gif\"");       
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());       
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public int IsHaveChild(HttpServletRequest request,
      HttpServletResponse response,String id) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      Map map = new HashMap();
      map.put("DEPT_PARENT", id);
      List<YHDepartment> list = orm.loadListSingle(dbConn, YHDepartment.class, map);
      if(list.size() > 0){
        return 1;
      }else{
        return 0;
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
  }
 /**
  * 数据结构：Map<YHSmsBody,List<YHsms>>
  * @param request
  * @param response
  * @return
  * @throws Exception
 */
  public String listAllSms(HttpServletRequest request,
      HttpServletResponse response) throws Exception { 
    String pageNoStr = request.getParameter("pageNo");
    String pageSizeStr = request.getParameter("pageSize");
    int sizeNo = 0;
    int pageNo = 0;
    int pageSize = 0;
    Connection dbConn = null;
    try {
      pageNo = Integer.parseInt(pageNoStr);
      pageSize = Integer.parseInt(pageSizeStr);
      HashMap<YHSmsBody,List<YHSms>> smsData = new HashMap<YHSmsBody, List<YHSms>>(); 
      List<YHSms> smsList = null;
      YHORM orm = new YHORM();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      YHSmsLogic sl = new YHSmsLogic();
      ArrayList<YHSmsBody>  allList = (ArrayList<YHSmsBody>) orm.loadListSingle(dbConn, YHSmsBody.class, new String[]{" FROM_ID=" + seqId + " order by SEND_TIME DESC" });
      //sizeSum = sbList.size();
      ArrayList<YHSmsBody> sbList = new ArrayList<YHSmsBody>();

      int length = allList.size() >= ((pageNo + 1) * pageSize) ? ((pageNo + 1) * pageSize) : allList.size();
      Iterator iter = allList.iterator();
      while(iter.hasNext()){
        YHSmsBody yhSmsBody = (YHSmsBody) iter.next();
        smsList = orm.loadListSingle(dbConn, YHSms.class,new String[]{" BODY_SEQ_ID=" + yhSmsBody.getSeqId() + " and DELETE_FLAG IN(0,1)"} );
        if(smsList.size() > 0){
          if(smsData.size() < length){
            smsData.put(yhSmsBody, smsList);
           // sbList.add(yhSmsBody);
          }
        }else{
          iter.remove();
        }
      }
      sizeNo = allList.size();
      length = allList.size() >= ((pageNo + 1) * pageSize) ? ((pageNo + 1) * pageSize) : allList.size();
      if(length > 0){
        sbList.addAll(allList.subList(pageNo*pageSize, length));
      }
      //request.setAttribute("pageSum", smsList.size());
      request.setAttribute("sbList", sbList);
      request.setAttribute("DataMap", smsData);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取2出数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/sms/sentsms.jsp?sizeNo="+sizeNo + "&pageNo=" + pageNo + "&pageSize=" + pageSize ;//sentsms
  }
  
  public String acceptedSms(HttpServletRequest request,
      HttpServletResponse response) throws Exception { 
    Connection dbConn = null;
    YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
    int toId = person.getSeqId();  //用户seqId
    String pageNoStr = request.getParameter("pageNo");
    String pageSizeStr = request.getParameter("pageSize");
    int sizeNo = 0;
    int pageNo = 0;
    int pageSize = 0;
    try {
      pageNo = Integer.parseInt(pageNoStr);
      pageSize = Integer.parseInt(pageSizeStr);
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
      dbConn = requestDbConn.getSysDbConn();
      ArrayList<YHSms> allList = new ArrayList<YHSms>();
      ArrayList<YHSms> contentList = new ArrayList<YHSms>();
      smsLogic = new YHSmsLogic();
      allList = smsLogic.getSmsByUserId(dbConn, toId,1);
      int length = allList.size() >= ((pageNo + 1) * pageSize) ? ((pageNo + 1) * pageSize) : allList.size();
      contentList.addAll(allList.subList(pageNo*pageSize, length));
      sizeNo = allList.size();
      request.setAttribute("pageNo", pageNo);
      request.setAttribute("contentList", contentList);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/sms/acceptedsms.jsp?sizeNo="+sizeNo + "&pageNo=" + pageNo + "&pageSize=" + pageSize ;
  }
  public String getStatusByBodyId(HttpServletRequest request,
      HttpServletResponse response) throws Exception { 
    Connection dbConn = null;
    YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
    String bodyIdStr = request.getParameter("bodyId");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      ArrayList<YHSms> contentList = (ArrayList<YHSms>) orm.loadListSingle(dbConn, YHSms.class, new String[]{" BODY_SEQ_ID=" + bodyIdStr + " and (DELETE_FLAG = '0' or DELETE_FLAG = '1') "});
      request.setAttribute("contentList", contentList);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/sms/test.jsp";
  }
  /*
  public String getTotalSmsContentNum(HttpServletRequest request,
      HttpServletResponse response) throws Exception { 
    YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
    int seqIdPerson = person.getSeqId();
    String userName = person.getUserName();
    Connection dbConn = null;  
    String bodySeqIds = null;
    YHSmsContent smsContent= null;
    StringBuffer sb = new StringBuffer();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
      dbConn = requestDbConn.getSysDbConn();
      String toId = String.valueOf(seqIdPerson);//"208";
      ArrayList<YHSmsContent> contentList = new ArrayList<YHSmsContent>();
      smsLogic = new YHSmsLogic();
      bodySeqIds = smsLogic.getBodySeqIds(dbConn, toId);
      String[] seqIds = bodySeqIds.split(",");
      for(int i = 0; i < seqIds.length; i++) {
        String seqId = seqIds[i];
        smsContent = smsLogic.getSmsContent(dbConn, seqId);
        contentList.add(smsContent);
      }
      sb.append("{");
      sb.append("num:\"" + contentList.size() + "\"");
      sb.append("}");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  */
  public String listDeptUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception { 
    String id = request.getParameter("id");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
      dbConn = requestDbConn.getSysDbConn();
      ArrayList<YHPerson> personList = null;
      String data = null;
      StringBuffer sb = new StringBuffer();
      Map map = new HashMap();
      map.put("DEPT_ID", id);
      YHORM orm = new YHORM();
      sb.append("[");
      personList = (ArrayList<YHPerson>)orm.loadListSingle(dbConn, YHPerson.class, map);
      if(personList.size() > 0 ) {
        for(int i = 0; i < personList.size(); i++) {
          YHPerson person = personList.get(i);
          sb.append("{");
          sb.append("seqId:\"" + person.getSeqId() + "\"");
          sb.append(",userId:\"" + person.getUserId() + "\"");
          sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1);
      }    
      sb.append("]");
      data = sb.toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功存入数据！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String deleteSms(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");  
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);   
      dbConn = requestDbConn.getSysDbConn();
      Map map = new HashMap();
      map.put("seqId", seqId);
      YHORM orm = new YHORM();
      orm.deleteSingle(dbConn, "sms", map);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功删除内部短信！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String deleteAllSms(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String idStrs = request.getParameter("idStrs");  
    int smsSeqId = Integer.parseInt(request.getParameter("idStrs"));
    Connection dbConn = null;
    smsLogic = new YHSmsLogic();
    YHORM orm = new YHORM();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);   
      dbConn = requestDbConn.getSysDbConn();
      String delFlag = smsLogic.getdeleteFlagSeqId(dbConn, smsSeqId);
      String remindFlag = smsLogic.getremindFlagSeqId(dbConn, smsSeqId);
      int smsId = smsLogic.getSmsBoSeqId(dbConn, smsSeqId);
      //System.out.println(smsId);
      ArrayList<YHSms> smsList = smsLogic.getSmsSeqIdSize(dbConn, smsId);
      //System.out.println(smsList.size()+"TTTTTTTTXXXXXXXX");
      if(remindFlag.equals("1")){
        YHSmsBody sms = (YHSmsBody) orm.loadObjSingle(dbConn, YHSmsBody.class, smsId);
        sms.setSeqId(smsId);
        orm.deleteSingle(dbConn, sms);
        YHSms sms1 = (YHSms) orm.loadObjSingle(dbConn, YHSms.class, smsSeqId);
        sms1.setSeqId(smsSeqId);
        orm.deleteSingle(dbConn, sms1);
      }else{
        if(delFlag.equals("1")){
          if(smsList.size() <= 1){
            YHSmsBody sms = (YHSmsBody) orm.loadObjSingle(dbConn, YHSmsBody.class, smsId);
            sms.setSeqId(smsId);
            orm.deleteSingle(dbConn, sms);
            YHSms sms1 = (YHSms) orm.loadObjSingle(dbConn, YHSms.class, smsSeqId);
            sms1.setSeqId(smsSeqId);
            orm.deleteSingle(dbConn, sms1);
          }else{
            YHSms sms1 = (YHSms) orm.loadObjSingle(dbConn, YHSms.class, smsSeqId);
            sms1.setSeqId(smsSeqId);
            orm.deleteSingle(dbConn, sms1);
          }
        }else{
          smsLogic.deleteAllSms(dbConn, idStrs);
        }
      }
      
//      int seq = sl.getSmsSeqId(dbConn, toId, bodySeqId);
//      System.out.println(seq+":PPPPPPPP");
//      m.put("SEQ_ID", seq);
//      orm.deleteSingle(dbConn, "sms", m);
      //ArrayList<YHSms> smslist = smsLogic.getSmsSeqIdSize2(dbConn, smsSeqId);
      //System.out.println(smslist.size()+"TTTTTTTT");
//      smsLogic.deleteAllSms(dbConn, idStrs);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功删除所有内部短信！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String showSmsBodyContent(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    request.setCharacterEncoding("utf-8");
    String seqId = request.getParameter("seqId");
    //String userId = request.getParameter("userId");
    Connection dbConn = null;
    String contentDetail = null;
    Date sendTime = null;
    try {
      smsLogic = new YHSmsLogic();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);   
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHSmsBody smsBody = null;
      YHORM orm = new YHORM();
      smsBody = (YHSmsBody)orm.loadObjSingle(dbConn, YHSmsBody.class, Integer.parseInt(seqId));
      smsLogic.updateFalg(dbConn, person.getSeqId(), Integer.parseInt(seqId));
      if(smsBody == null) {
        smsBody = new YHSmsBody();
      }
      contentDetail = smsBody.getContent();
      contentDetail = contentDetail.replaceAll("[\\n\\r]", "<br>");
      contentDetail = YHUtility.encodeSpecial(contentDetail);
      sendTime = smsBody.getSendTime();
      request.setAttribute("sendTime", new SimpleDateFormat("MM-dd HH:mm").format(sendTime));
      request.setAttribute("contentDetail", contentDetail);
      request.setAttribute("userId", String.valueOf(person.getSeqId()));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出短信内容！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/sms/showcontent.jsp";
  }
  public String showSmsBody(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    request.setCharacterEncoding("utf-8");
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      smsLogic = new YHSmsLogic();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);   
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHSmsBody smsBody = null;
      YHORM orm = new YHORM();
      smsBody = (YHSmsBody)orm.loadObjSingle(dbConn, YHSmsBody.class, Integer.parseInt(seqId));
      smsLogic.updateFalg(dbConn, person.getSeqId(), Integer.parseInt(seqId));
      StringBuffer data = YHFOM.toJson(smsBody);
      //System.out.println(data.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出短信内容！");
      request.setAttribute(YHActionKeys.RET_DATA,data.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getSmsBodyContent(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    YHSmsLogic smsLogic = null;
    String contentDetail = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);   
      dbConn = requestDbConn.getSysDbConn();
      String data = null;
      StringBuffer sb = new StringBuffer();
      smsLogic = new YHSmsLogic();
      contentDetail = smsLogic.getContent(dbConn, seqId);
      contentDetail = YHUtility.encodeSpecial(contentDetail);
      sb.append("{");
      sb.append("content:\"" + contentDetail + "\"");
      sb.append("}");
      data = sb.toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出短信内容！");
      request.setAttribute(YHActionKeys.RET_DATA, data);   
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String bodySeqIdTest(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqIdPerson = person.getSeqId();
      String userName = person.getUserName();
     // int seqId = Integer.parseInt(request.getParameter("seqId"));
      String formId = request.getParameter("formId");
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      ArrayList<YHPerson> perList = null;
      List<Map> list = new ArrayList();
        String[] filterPer = new String[]{"USER_ID='"+formId+"'" };//"AND TO_ID="+seqIdPerson
        List funcList = new ArrayList();
        funcList.add("person");
        map = (HashMap)orm.loadDataSingle(dbConn, funcList, filterPer);
        list.addAll((List<Map>) map.get("PERSON"));
      if(list.size() > 1){
        for(Map m : list){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",userId:\"" + m.get("userId") + "\"");
          sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1); 
      }else{
        for(Map m : list){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",userId:\"" + m.get("userId") + "\"");
          sb.append("}");
        }
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String search(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String toId = request.getParameter("toId");
      YHORM orm = new YHORM();
      String bodyId = "";
      HashMap map = null;
      List<Map> list = new ArrayList();
      List<Map> listBody = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"TO_ID=" + toId};
      List funcList = new ArrayList();
      YHSmsLogic sl = new YHSmsLogic();
      funcList.add("sms");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("SMS"));
      for(Map m : list){
        m.get("bodySeqId");
        String[] bodyStr = new String[]{"SEQ_ID="+m.get("bodySeqId")};
        List bodylist = new ArrayList();
        bodylist.add("smsBody");
        map = (HashMap)orm.loadDataSingle(dbConn, bodylist, bodyStr);
        listBody.addAll((List<Map>) map.get("OA_MSG_BODY"));
      }       
      if(listBody.size() > 1){
        for(Map m : listBody){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",fromId:\"" + m.get("fromId") + "\"");
          sb.append(",content:\"" + m.get("content") + "\"");
          sb.append(",sendTime:\"" + m.get("sendTime") + "\"");
          sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1); 
      }else{
        for(Map m : listBody){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",fromId:\"" + m.get("fromId") + "\"");
          sb.append(",content:\"" + m.get("content") + "\"");
          sb.append(",sendTime:\"" + m.get("sendTime") + "\"");
          sb.append("}");
        }
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String searchList(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String toId = request.getParameter("toId");
      YHORM orm = new YHORM();
      HashMap map = null;
      List<Map> list = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"TO_ID=" + toId};
      List funcList = new ArrayList();
      YHSmsLogic sl = new YHSmsLogic();
      funcList.add("sms");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("SMS"));
      ArrayList<YHSmsBody> bodyList = null;
      for(Map m : list){
        m.get("bodySeqId");
        bodyList = (ArrayList<YHSmsBody>) sl.getSearchlist(dbConn, m.get("bodySeqId"));
      }       
      request.setAttribute("bodySearchList", bodyList);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/sms/search.jsp";
    //return "/core/inc/rtjson.jsp";
  }
  
  public String search2(HttpServletRequest request,
      HttpServletResponse response) throws Exception { 
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     
      dbConn = requestDbConn.getSysDbConn();
      ArrayList<YHSms> personList = null;
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String toId = request.getParameter("toId");
      YHORM orm = new YHORM();
      HashMap map = null;
      List<Map> list = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"TO_ID=" + toId};
      List funcList = new ArrayList();
      YHSmsLogic sl = new YHSmsLogic();
      funcList.add("sms");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("SMS"));
      for(Map m : list){
        m.get("bodySeqId");
      }       
      Map m = null;
      personList = (ArrayList<YHSms>)orm.loadListSingle(dbConn, YHSms.class, m);
      request.setAttribute("personList", personList);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/person/offdutypersonlist.jsp";
  }
  /**
   * 新短信提示
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String remindFlag(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int toId = person.getSeqId();
      YHSmsLogic sl = new YHSmsLogic();
      String sb = sl.getRemindInBox(dbConn, toId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String notConfirm(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    String pageNoStr = request.getParameter("pageNo");
    String pageSizeStr = request.getParameter("pageSize");
    int sizeNo = 0;
    int pageNo = 0;
    int pageSize = 0;
    try{
      pageNo = Integer.parseInt(pageNoStr);
      pageSize = Integer.parseInt(pageSizeStr);
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int toId = person.getSeqId();
      YHSmsLogic smsLogic = new YHSmsLogic();
      HashMap map = null;
      List<YHSms> list = new ArrayList();
      ArrayList<YHSms> contentList = new ArrayList<YHSms>();
      list = smsLogic.getSmsByUserId(dbConn, toId,2);
      sizeNo = list.size();
      int length = list.size() >= ((pageNo + 1) * pageSize) ? ((pageNo + 1) * pageSize) : list.size();
      contentList.addAll(list.subList(pageNo*pageSize, length));
      request.setAttribute("contentList", contentList);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/sms/notconfirmsms.jsp?sizeNo="+sizeNo + "&pageNo=" + pageNo + "&pageSize=" + pageSize ;
  }
  /**
   * 标记为读
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String resetFlag(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int toId = person.getSeqId();
      String bodySeqIds = request.getParameter("seqId");
      YHORM orm = new YHORM();
     // HashMap map = null;
     // List<Map> list = new ArrayList();
     // StringBuffer sb = new StringBuffer("[");
     // String[] filters = new String[]{"BODY_SEQ_ID=" + bodySeqId +" AND TO_ID='"+toId+"'"};
      //List funcList = new ArrayList();
      YHSmsLogic sl = new YHSmsLogic();
     // funcList.add("sms");
     // map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      //list.addAll((List<Map>) map.get("SMS"));
      String[] bodyIds = bodySeqIds.split(",");
      for (String bodyId : bodyIds) {
        if("".equals(bodyId.trim()) ||bodyId.indexOf("r")!=-1){
          continue;
        }
        
        //解决sms表 body_seq_id和to_id索引出数据不唯一的情况
        List<Integer> list = sl.getSmsSeqIds(dbConn, toId, Integer.parseInt(bodyId.trim()));
        for (int id : list) {
          YHSms sms = (YHSms) orm.loadObjSingle(dbConn, YHSms.class, id);
          sms.setSeqId(id);
          sms.setToId(toId);
          sms.setRemindFlag("0");
          orm.updateSingle(dbConn, sms);
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 标记为读
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String resetFlagAll(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    ArrayList<YHSms> smslist = null;
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int toId = person.getSeqId();
      YHORM orm = new YHORM();
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{" REMIND_FLAG IN(1,2) AND TO_ID=" + toId };
      List funcList = new ArrayList();
      YHSmsLogic sl = new YHSmsLogic();
      funcList.add("sms");
      
      smslist = (ArrayList<YHSms>) orm.loadListSingle(dbConn,  YHSms.class, filters);
      for (YHSms sms : smslist){
        sms.setRemindFlag("0");
        orm.updateSingle(dbConn, sms);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String deleteAcceptSms(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int toId = person.getSeqId();
      int bodySeqId = Integer.parseInt(request.getParameter("seqId"));
      //System.out.println(bodySeqId);
      YHORM orm = new YHORM();
      HashMap map = null;
      Map m =new HashMap();
      YHSmsLogic sl = new YHSmsLogic();
      String deleteFlag = sl.getdeleteFlag(dbConn, toId, bodySeqId);
      if(deleteFlag.equals("2")){
        //System.out.println("JJJJJJJJJJ1");
        ArrayList<YHSms> smsList = sl.getSmsSeqIdSize(dbConn, bodySeqId);
        //System.out.println(smsList.size()+":RRRRRRRRRR");
        if(smsList.size() <= 1){
          YHSmsBody sms = (YHSmsBody) orm.loadObjSingle(dbConn, YHSmsBody.class, bodySeqId);
          sms.setSeqId(bodySeqId);
          orm.deleteSingle(dbConn, sms);
          //int seq = sl.getSmsSeqId(dbConn, toId, bodySeqId);
          List<Integer> ids = sl.getSmsSeqIds(dbConn, toId, bodySeqId);
          for (int id : ids) {
            m.put("SEQ_ID", id);
            orm.deleteSingle(dbConn, "sms", m);
          }
          //System.out.println(seq+":PPPPPPPP");
        }else{
          List<Integer> ids = sl.getSmsSeqIds(dbConn, toId, bodySeqId);
          for (int id : ids) {
            m.put("SEQ_ID", id);
            orm.deleteSingle(dbConn, "sms", m);
          }
        }
      }else{
        //System.out.println("JJJJJJJJJJ22222");
        List<Map> list = new ArrayList();
        String[] filters = new String[]{"BODY_SEQ_ID=" + bodySeqId +"AND TO_ID='"+toId+"'"};
        List funcList = new ArrayList();
        funcList.add("sms");
        map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
        list.addAll((List<Map>) map.get("SMS"));
        int seqIdd = sl.getSmsSeqId(dbConn, toId, bodySeqId);
        YHSms sms = (YHSms) orm.loadObjSingle(dbConn, YHSms.class, seqIdd);
        sms.setSeqId(seqIdd);
        sms.setToId(toId);
        sms.setDeleteFlag("1");
        orm.updateSingle(dbConn, sms);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String changeName(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String toId = request.getParameter("toId");
      YHORM orm = new YHORM();
      HashMap map = null;
      List<Map> list = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"USER_ID='" + toId+"'"};
      List funcList = new ArrayList();
      funcList.add("person");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("PERSON"));
      if(list.size() > 1){
        for(Map m : list){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1); 
      }else{
        for(Map m : list){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append("}");
        }
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getFlag(HttpServletRequest request,
      HttpServletResponse response) throws Exception { 
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     
      dbConn = requestDbConn.getSysDbConn();
      ArrayList<YHSms> personList = null;
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String toId = request.getParameter("toId");
      YHORM orm = new YHORM();
      HashMap map = null;
      List<Map> list = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"TO_ID=" + toId};
      List funcList = new ArrayList();
      YHSmsLogic sl = new YHSmsLogic();
      funcList.add("sms");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("SMS"));
      for(Map m : list){
        m.get("bodySeqId");
      }       
      Map m = null;
      personList = (ArrayList<YHSms>)orm.loadListSingle(dbConn, YHSms.class, m);
      request.setAttribute("personList", personList);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/person/offdutypersonlist.jsp";
  }
  
  public String getDialogDetail(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int toId = person.getSeqId();
      
      String seqIdSms = request.getParameter("seqId");
      String flag = "2";
      YHORM orm = new YHORM();
      String userIdToId = "";
      HashMap map = null;
      HashMap map1 = null;
      HashMap map2 = null;
      List<Map> list = new ArrayList();
      List<Map> listBody = new ArrayList();
      List<Map> listBodys = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"SEQ_ID="+seqIdSms};
      List funcList = new ArrayList();
      YHSmsLogic sl = new YHSmsLogic();
      funcList.add("sms");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("SMS"));
      for(Map m : list){
        m.get("bodySeqId");
        m.get("seqId");
        m.get("toId");
        userIdToId = sl.getUserIdDialog(dbConn, m.get("toId"));
        String[] bodyStr = new String[]{"SEQ_ID="+m.get("bodySeqId")};
        List bodylist = new ArrayList();
        bodylist.add("smsBody");
        map = (HashMap)orm.loadDataSingle(dbConn, bodylist, bodyStr);
        listBody.addAll((List<Map>) map.get("OA_MSG_BODY"));
        for(Map ms : listBody){
          ms.get("fromId");
          //System.out.println(userIdToId+":formIDdddd");
          ms.get("sendTime");
          String str = String.valueOf(ms.get("sendTime"));
          String strSub = str.substring(0, 19);
          sl.testGetConnection(dbConn, ms.get("fromId"), userIdToId, strSub);
        }
      }       
      if(listBodys.size() > 1){
        for(Map m : listBodys){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",fromId:\"" + m.get("fromId") + "\"");
          sb.append(",content:\"" + m.get("content") + "\"");
          sb.append(",sendTime:\"" + m.get("sendTime") + "\"");
          sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1); 
      }else{
        for(Map m : listBodys){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",fromId:\"" + m.get("fromId") + "\"");
          sb.append(",content:\"" + m.get("content") + "\"");
          sb.append(",sendTime:\"" + m.get("sendTime") + "\"");
          sb.append("}");
        }
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   *  ckeck sms remind for im
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String remindCheck(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection conn = null;
    int data = 0;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      if(person != null){
        int personId = person.getSeqId();
        smsLogic = new YHSmsLogic();
        data = smsLogic.isRemindNew(personId);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, data+"");
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      return "/core/inc/rtjson.jsp";
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   *  ckeck message remind for im
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String remindCheck1(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection conn = null;
    int data = 0;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      if(person != null){
        int personId = person.getSeqId();
        smsLogic = new YHSmsLogic();
        data = smsLogic.isRemindNew1(personId);
      }
      response.setCharacterEncoding("UTF-8");
      response.setContentType("text/html;charset=UTF-8");
      response.setHeader("Cache-Control","private");
      PrintWriter out = response.getWriter();
      out.print(data);
      out.flush();
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      
    }
    return null;
  }
  
  /**
   * ckeck  both  remind for index
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String remindCheck2(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection conn = null;
    int data = 0;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      if(person != null){
        int personId = person.getSeqId();
        smsLogic = new YHSmsLogic();
        data = smsLogic.isRemindNew2(personId);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, data+"");
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      return "/core/inc/rtjson.jsp";
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 得到历史记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getHistory(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection conn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      String toUserIdStr = request.getParameter("userId");
      String dateStr = request.getParameter("date");
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int personId = person.getSeqId();
      int toUserId = Integer.valueOf(toUserIdStr.trim());
      //System.out.println();
      smsLogic = new YHSmsLogic();
      String bodyIds = smsLogic.getBodyIdsForHis(conn, personId, toUserId,dateStr);
      YHORM orm = new YHORM();
      if("".equals(bodyIds)){
        bodyIds = "0";
      }
      String[] filters = new String[]{" SEQ_ID IN("+ bodyIds + ") ORDER BY SEND_TIME Desc"};
      ArrayList<YHSmsBody> sblist = (ArrayList<YHSmsBody>) orm.loadListSingle(conn, YHSmsBody.class, filters);
      StringBuffer sb = smsLogic.toHistoryData(sblist, personId);
      //System.out.println(sb.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 短信删除逻辑
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delSms(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deType = request.getParameter("deType");
      String bodyId = request.getParameter("bodyId");
      smsLogic = new YHSmsLogic();
      if(bodyId != null){
        String[] bIds = bodyId.split(",");
        //分离提醒与微讯
        String messageIds="";
        String smsId="";
        for(int i=0;i<bIds.length;i++){
          String idS=bIds[i];
          if(!YHUtility.isNullorEmpty(idS)){
            if(idS.indexOf("r")!=-1){
              
              messageIds+=idS.replace("r", "");
              messageIds+=",";
            }else{
              smsId+=idS;
              smsId+=",";
            }
         }
        }
        
        
        //微讯删除
        YHMessageLogic Logic = new YHMessageLogic();
        for (String bIdstr : messageIds.split(",")){
          if("".equals(bIdstr.trim())){
            continue;
          }
          int bId = Integer.parseInt(bIdstr.trim());
          Logic.doDelSms(dbConn, bId, deType, person.getSeqId());
        }
        
        
        
        //提醒删除
        for (String bIdstr : smsId.split(",")){
          if("".equals(bIdstr.trim())){
            continue;
          }
          int bId = Integer.parseInt(bIdstr.trim());
          smsLogic.doDelSms(dbConn, bId, deType, person.getSeqId());
        }
        
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功删除微讯！");
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "微讯删除失败！可能原因：" + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 删除邮件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delSmsByUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String bodyId = request.getParameter("bodyId");
      String userIdStr = request.getParameter("userId");
      smsLogic = new YHSmsLogic();
      if(bodyId != null){
        String[] bIds = bodyId.split(",");
        String[] uIds = userIdStr.split(",");
        for (int i = 0 ; i < bIds.length; i ++){
          String  bIdstr  = bIds[i];
          String  uIdstr  = uIds[i];
          if("".equals(bIdstr.trim())){
            continue;
          }
          int bId = Integer.parseInt(bIdstr.trim());
          int userId = Integer.parseInt(uIdstr);
          smsLogic.doDelSmsByUser(dbConn, bId, "2",person.getSeqId(), userId);
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功删除内部短信！");
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "内部短信删除失败！可能原因：" + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 短信删除逻辑
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delSmsByShortcut (HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deType = request.getParameter("deType");
      String shortCutType = request.getParameter("shortCutType");
      String bodyIdS = "";
      smsLogic = new YHSmsLogic();
      String bodyId = smsLogic.getBodyIdFShortCut(dbConn, person.getSeqId(), Integer.valueOf(shortCutType), Integer.valueOf(deType));
      if(bodyId != null){
        String[] bIds = bodyId.split(",");
        for (int i=0 ;i <  bIds.length ; i ++ ){
          String bIdstr = bIds[i];
          if("".equals(bIdstr.trim())){
            continue;
          }
          int bId = Integer.parseInt(bIdstr.trim());
          if(smsLogic.doDelSms(dbConn, bId, deType, person.getSeqId())){
            if(!"".equals(bodyIdS)){
              bodyIdS += ",";
            }
            bodyIdS += bId;
          }
          if(i%400 == 0){
            if(!"".equals(bodyIdS)){
              smsLogic.deleteAll(dbConn, bodyIdS, "OA_MSG_BODY");
              bodyIdS = "";
            }
          }
        }
        if(!"".equals(bodyIdS)){
          smsLogic.deleteAll(dbConn, bodyIdS, "OA_MSG_BODY");
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功删除内部短信！");
    }catch(Exception e){
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "内部短信删除失败！可能原因：" + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   *查询收信箱邮件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String searchForIn(HttpServletRequest request,
      HttpServletResponse response) throws Exception { 
    Connection dbConn = null;
    YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
    int toId = person.getSeqId();  //用户seqId
    String pageNoStr = request.getParameter("pageNo");
    String pageSizeStr = request.getParameter("pageSize");
    int sizeNo = 0;
    int pageNo = 0;
    int pageSize = 0;
    try {
      pageNo = Integer.parseInt(pageNoStr);
      pageSize = Integer.parseInt(pageSizeStr);
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
      dbConn = requestDbConn.getSysDbConn();
      ArrayList<YHSms> allList = new ArrayList<YHSms>();
      ArrayList<YHSms> contentList = new ArrayList<YHSms>();
      smsLogic = new YHSmsLogic();
      allList = smsLogic.getSmsBySearchIn(dbConn, toId, 1, request.getParameterMap());
      int length = allList.size() >= ((pageNo + 1) * pageSize) ? ((pageNo + 1) * pageSize) : allList.size();
      contentList.addAll(allList.subList(pageNo*pageSize, length));
      sizeNo = allList.size();
      request.setAttribute("pageNo", pageNo);
      request.setAttribute("contentList", contentList);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/sms/searchForIn.jsp?sizeNo="+sizeNo + "&pageNo=" + pageNo + "&pageSize=" + pageSize ;
  }
  /**
   * 查询发信箱邮件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String searchForOut(HttpServletRequest request,
      HttpServletResponse response) throws Exception { 
    String pageNoStr = request.getParameter("pageNo");
    String pageSizeStr = request.getParameter("pageSize");
    String userId = request.getParameter("userId");

    int sizeNo = 0;
    int pageNo = 0;
    int pageSize = 0;
    Connection dbConn = null;
    try {
      pageNo = Integer.parseInt(pageNoStr);
      pageSize = Integer.parseInt(pageSizeStr);
      if(userId != null && !"".equals(userId)){
        if(userId.trim().endsWith(",")){
          userId = userId.trim().substring(0, userId.trim().length() - 1);
        }
      }
      HashMap<YHSmsBody,List<YHSms>> smsData = new HashMap<YHSmsBody, List<YHSms>>(); 
      List<YHSms> smsList = null;
      YHORM orm = new YHORM();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      YHSmsLogic sl = new YHSmsLogic();
      
      ArrayList<YHSmsBody>  allList = sl.getSmsBodyBySearchOut(dbConn, seqId, 2, request.getParameterMap());
      //sizeSum = sbList.size();
      ArrayList<YHSmsBody> sbList = new ArrayList<YHSmsBody>();

      int length = allList.size() >= ((pageNo + 1) * pageSize) ? ((pageNo + 1) * pageSize) : allList.size();
      Iterator iter = allList.iterator();
      while(iter.hasNext()){
        YHSmsBody yhSmsBody = (YHSmsBody) iter.next();
        if(userId != null && !"".equals(userId)){
          smsList = orm.loadListSingle(dbConn, YHSms.class,new String[]{" BODY_SEQ_ID=" + yhSmsBody.getSeqId() + " and DELETE_FLAG IN(0,1) AND TO_ID IN(" + userId + ")"} );
        }else{
          smsList = orm.loadListSingle(dbConn, YHSms.class,new String[]{" BODY_SEQ_ID=" + yhSmsBody.getSeqId() + " and DELETE_FLAG IN(0,1)"} );
        }
        if(smsList.size() > 0){
          if(smsData.size() < length){
            smsData.put(yhSmsBody, smsList);
           // sbList.add(yhSmsBody);
          }
        }else{
          iter.remove();
        }
      }
      sizeNo = allList.size();
      length = allList.size() >= ((pageNo + 1) * pageSize) ? ((pageNo + 1) * pageSize) : allList.size();
      if(length > 0){
        sbList.addAll(allList.subList(pageNo*pageSize, length));
      }
      //request.setAttribute("pageSum", smsList.size());
      request.setAttribute("sbList", sbList);
      request.setAttribute("DataMap", smsData);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/sms/searchForOut.jsp?sizeNo="+sizeNo + "&pageNo=" + pageNo + "&pageSize=" + pageSize ;//sentsms
  }
  /**
   * 已收短信
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String smsInbox(HttpServletRequest request,
      HttpServletResponse response) throws Exception { 
    Connection dbConn = null;
    YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
    int toId = person.getSeqId();  //用户seqId
    String pageNoStr = request.getParameter("pageNo");
    String pageSizeStr = request.getParameter("pageSize");
    int sizeNo = 0;
    int pageNo = 0;
    int pageSize = 0;
    try {
      pageNo = Integer.parseInt(pageNoStr);
      pageSize = Integer.parseInt(pageSizeStr);
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);  
      dbConn = requestDbConn.getSysDbConn();
      smsLogic = new YHSmsLogic();
      String data = smsLogic.getPanelInBox(dbConn,request.getParameterMap(), toId, pageNo, pageSize);
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 数据结构：Map<YHSmsBody,List<YHsms>>
   * @param request
   * @param response
   * @return
   * @throws Exception
  */
   public String smsSentbox(HttpServletRequest request,
       HttpServletResponse response) throws Exception { 
     String pageNoStr = request.getParameter("pageNo");
     String pageSizeStr = request.getParameter("pageSize");
     int sizeNo = 0;
     int pageNo = 0;
     int pageSize = 0;
     Connection dbConn = null;
     try {
       pageNo = Integer.parseInt(pageNoStr);
       pageSize = Integer.parseInt(pageSizeStr);
       YHORM orm = new YHORM();
       YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
       dbConn = requestDbConn.getSysDbConn();
       YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
       int userId = person.getSeqId();
       YHSmsLogic sl = new YHSmsLogic();
       StringBuffer data = sl.getPanelSentBox(dbConn, request.getParameterMap(),userId, pageNo, pageSize);
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
       request.setAttribute(YHActionKeys.RET_DATA,data.toString());
     }catch(Exception ex) {
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
       request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
       throw ex;
     }
     return "/core/inc/rtjson.jsp";
   }
   
   public String getSmsToId(HttpServletRequest request,
       HttpServletResponse response) throws Exception { 
     String bodyId = request.getParameter("bodyId");
     Connection dbConn = null;
     try {
       YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
       YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
       dbConn = requestDbConn.getSysDbConn();
       YHSmsLogic sl = new YHSmsLogic();
       String data = sl.getToIdByBodyId(dbConn, Integer.parseInt(bodyId));
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
       request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
     }catch(Exception ex) {
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
       request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
       throw ex;
     }
     return "/core/inc/rtjson.jsp";
   }
   
   public String getStauts(HttpServletRequest request,
       HttpServletResponse response)throws Exception{
     String bodyId = request.getParameter("bodyId");
     Connection dbConn = null;
     try {
       YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
       YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
       dbConn = requestDbConn.getSysDbConn();
       YHSmsLogic sl = new YHSmsLogic();
       String data = sl.getStatusByBodyId(dbConn, Integer.parseInt(bodyId));
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
       request.setAttribute(YHActionKeys.RET_DATA,data);
     }catch(Exception ex) {
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
       request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
       throw ex;
     }
     return "/core/inc/rtjson.jsp";
   }
   public String getSmsTypeDesc(HttpServletRequest request,
       HttpServletResponse response)throws Exception{
     String smsType = request.getParameter("smsType");
     String code = request.getParameter("code");
     Connection dbConn = null;
     try {
       YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
       YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
       dbConn = requestDbConn.getSysDbConn();
       YHSmsLogic sl = new YHSmsLogic();
       String data = sl.getSmsTypeDesc(dbConn, Integer.parseInt(smsType.trim()), code);
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
       request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
     }catch(Exception ex) {
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
       request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
       throw ex;
     }
     return "/core/inc/rtjson.jsp";
   }
   
   public String getPersonInfo(HttpServletRequest request,
       HttpServletResponse response)throws Exception{
     String userIdStr = request.getParameter("userId");
     Connection dbConn = null;
     try {
       YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
       YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
       dbConn = requestDbConn.getSysDbConn();
       YHSmsLogic sl = new YHSmsLogic();
       String data="";
       
       if(!YHUtility.isNullorEmpty(userIdStr)){
         if(userIdStr.indexOf(".")!=-1)
         userIdStr=userIdStr.substring(0,userIdStr.indexOf("."));
         data = sl.getPersonInfo(dbConn, Integer.valueOf(userIdStr.trim()));
       }else{
         data = sl.getPersonInfo(dbConn, person.getSeqId());
       }
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
       request.setAttribute(YHActionKeys.RET_DATA, data);
     }catch(Exception ex) {
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
       request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
       throw ex;
     }
     return "/core/inc/rtjson.jsp";
   }
   
   public String censor(HttpServletRequest request,
       HttpServletResponse response)throws Exception{
     Connection dbConn = null;
     try {
       YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
       YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
       dbConn = requestDbConn.getSysDbConn();
       int userId = person.getSeqId();
       String toId = request.getParameter("user");
       String content = request.getParameter("content");
       String sendTime = request.getParameter("sendTime");
       String smsType = request.getParameter("smsType");
       if(sendTime == null || "".equals(sendTime.trim())){
         sendTime = YHUtility.getDateTimeStr(new Date());
       }
       String jsonStr = "{FROM_ID:" + userId + ", TO_ID:\"" + toId + "\", SEND_TIME:\"" + sendTime + "\", CONTENT:\"" + YHUtility.encodeSpecial(content) + "\", SMS_TYPE:\"" + smsType + "\", REMIND_URL:\"\"}";
       YHCensorCheckLogic.addJsonContent(dbConn, "1", jsonStr,userId);
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     }catch(Exception ex) {
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
       request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
       throw ex;
     }
     return "/core/inc/rtjson.jsp";
   }
}

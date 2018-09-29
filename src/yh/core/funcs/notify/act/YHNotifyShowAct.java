package yh.core.funcs.notify.act;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oa.spring.util.StaticData;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.news.data.YHNews;
import yh.core.funcs.notify.data.YHNotify;
import yh.core.funcs.notify.logic.YHNotifyManageLogic;
import yh.core.funcs.notify.logic.YHNotifyShowLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHNotifyShowAct {
  private static Logger log = Logger.getLogger(YHNotifyShowAct.class);
  private static final int MAX = 10;
  private YHNotifyShowLogic notifyShowLogic = new YHNotifyShowLogic();
  public boolean findToId(String object,String object2){
    boolean temp = false;
    if(object != null && !"".equals(object)){
      String[] toIds = object.split(",");
      for(int j = 0 ;j < toIds.length ; j++){
        String toIdTemp =  toIds[j];
        if(toIdTemp.equals(object2)){
          temp = true;
          break;
        }
      }
    }
    return temp;
  }
  
  public String getnotifyNoReadList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    YHNotifyShowLogic notifyShowLogic = new YHNotifyShowLogic();
    Connection dbConn = null;
    Statement stmt = null;
    ResultSet rs = null;
    String format = "";
    String anonymityYn = "";
    String type = request.getParameter("type");//下拉框中类型
    String ascDesc = request.getParameter("ascDesc");//升序还是降序
    String field = request.getParameter("field");//排序的字段
    
    String showLenStr = request.getParameter("showLength");//每页显示长度
    String pageIndexStr = request.getParameter("pageIndex");//页码数
    if(pageIndexStr == null || pageIndexStr.trim() == ""|| pageIndexStr.replaceAll(" ", "").length() <1) {
      pageIndexStr = "1";
    }

//  String loginUserId = request.getParameter("loginUserId");
    YHPerson loginUser = null;
    loginUser = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    try {
      String data = "";
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
        data = notifyShowLogic.getnotifyNoReadList(dbConn, loginUser, Integer.parseInt(showLenStr),type,ascDesc,field, 
            Integer.parseInt(pageIndexStr));
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
        request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    //return "/core/funcs/dept/deptinput.jsp";
    //?deptParentDesc=+deptParentDesc
    return "/core/inc/rtjson.jsp";
  }
  
  //全部公告
  public String getnotifyShowList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    YHNotifyShowLogic notifyShowLogic = new YHNotifyShowLogic();
    Connection dbConn = null;
    Statement stmt = null;
    ResultSet rs = null;
    String format = "";
    String anonymityYn = "";
    String type = request.getParameter("type");//下拉框中类型
    String ascDesc = request.getParameter("ascDesc");//升序还是降序
    String field = request.getParameter("field");//排序的字段

    String sendTime = request.getParameter("sendTime");
    String showLenStr = request.getParameter("showLength");//每页显示长度
    String pageIndexStr = request.getParameter("pageIndex");//页码数
    if(pageIndexStr == null || pageIndexStr.trim() == ""|| pageIndexStr.replaceAll(" ", "").length() <1) {
      pageIndexStr = "1";
    }

//  String loginUserId = request.getParameter("loginUserId");
    YHPerson loginUser = null;
    loginUser = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    List<YHNotify> list = new ArrayList<YHNotify>();
    try {
      String data = "";
      int temp = 0;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int loginDeptId = loginUser.getDeptId();
      String userPriv = loginUser.getUserPriv();
      int seqUserId = loginUser.getSeqId();
        data = notifyShowLogic.getnotifyShowList(dbConn, loginUser, Integer.parseInt(showLenStr),type,ascDesc,field, 
            Integer.parseInt(pageIndexStr),sendTime);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
        request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    //return "/core/funcs/dept/deptinput.jsp";
    //?deptParentDesc=+deptParentDesc
    return "/core/inc/rtjson.jsp";
  }
  
  public String showObject(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    YHORM orm = new YHORM();
  
    YHNotifyShowLogic notifyShowLogic = new YHNotifyShowLogic();
    String seqId = request.getParameter("seqId");
    String isManage = request.getParameter("isManage");
  
    String data = "";
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHNotify notify = (YHNotify)orm.loadObjSingle(dbConn, YHNotify.class, Integer.parseInt(seqId));
      Date endDate = null;
      String fromId = "";
      if (notify != null) {
        endDate = notify.getEndDate();
        fromId = notify.getFromId();
        if (!YHUtility.null2Empty(notify.getFormat()).equals("2")) {
          byte[] byteContent = notify.getCompressContent();
          if (byteContent == null) {
            notify.setContent("");
          }else {
            notify.setContent(new String(byteContent, "UTF-8"));
          }
        }
      }else{
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "该公告已删除");
        return "/core/inc/rtjson.jsp";
      }
      
      
      
      if((!"".equals(endDate)&&endDate!=null)&&!"1".equals(isManage)&&endDate.compareTo(new Date())<0) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "该公告通知已终止");
        return "/core/inc/rtjson.jsp";
      }
//      if(!"1".equals(notify.getPublish())) {
//        if(!fromId.equals(Integer.toString(person.getSeqId()))) {
//          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
//          request.setAttribute(YHActionKeys.RET_MSRG, "该公告通知未发布。");
//          return "/core/inc/rtjson.jsp";
//        }
//      }
      
      if(!YHUtility.isNullorEmpty(fromId)&&!"1".equals(person.getUserPriv()) && !"1".equals(person.getPostPriv()) && !fromId.equals(String.valueOf(person.getSeqId()))
          &&!"0".equals(notify.getToId())&&findToId(notify.getToId(),Integer.toString(person.getDeptId()))==false
          &&findToId(notify.getPrivId(),person.getUserPriv())==false
          &&findToId(notify.getUserId(),Integer.toString(person.getSeqId()))==false && !notify.getAuditer().equalsIgnoreCase(person.getSeqId()+"")) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "您无权限查看该公告。");
        return "/core/inc/rtjson.jsp";
      }
      data = notifyShowLogic.showObject(dbConn,person,Integer.parseInt(seqId),isManage,notify);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "该公告已删除。");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  //我的办公桌公告查询
  public String queryNotify(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    Statement stmt = null;
    ResultSet rs = null;
    String data = "";
    int pageIndex = 1;
    int showLen = 10;
    YHNotify notify = (YHNotify) YHFOM.build(request.getParameterMap());
//    String pageIndexStr = request.getParameter("pageIndex");
//    String showLengthStr = request.getParameter("showLength");
    String ascDesc = request.getParameter("ascDesc");//升序还是降序
    String field = request.getParameter("field");//排序的字段
    String beginDate = request.getParameter("beginDate");
    String endDate = request.getParameter("endDate");
    YHPerson loginUser = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
     
      YHNotifyShowLogic notifyShowLogic = new YHNotifyShowLogic();

      data = notifyShowLogic.queryNotify(dbConn,notify,loginUser,beginDate,endDate,ascDesc,field);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage().substring(ex.getMessage().lastIndexOf(":")+1,ex.getMessage().length()),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    //return "/core/funcs/dept/deptinput.jsp";
    //?deptParentDesc=+deptParentDesc
    return "/core/inc/rtjson.jsp";
  }
  
  public String showReader(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    YHNotify notify = null;
    String seqId = request.getParameter("seqId");
    String displayAll = request.getParameter("displayAll");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHNotifyShowLogic notifyShowLogic = new YHNotifyShowLogic();
      String data = notifyShowLogic.showReader(dbConn, seqId,displayAll);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    //return "/core/funcs/dept/deptinput.jsp";
    //?deptParentDesc=+deptParentDesc
    return "/core/inc/rtjson.jsp";
  }
  
  public String test(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    YHNews news = null;
    YHDeptLogic deptLogic = new YHDeptLogic();
    String seqId = request.getParameter("seqId");
    String displayAll = request.getParameter("displayAll");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = deptLogic.getDeptTreeJson(0, dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
      
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    //return "/core/funcs/dept/deptinput.jsp";
    //?deptParentDesc=+deptParentDesc
    return "/core/inc/rtjson.jsp";
  }
  
  public String deleteReader(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    YHNotifyManageLogic notifyManageLogic = new YHNotifyManageLogic();
    YHPerson loginUser = null;
    Connection dbConn = null;
    String seqId = request.getParameter("seqId");    
     loginUser = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
     Statement st = null;
     ResultSet rs = null;
     try {
       YHRequestDbConn requestDbConn = (YHRequestDbConn) request
           .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       dbConn = requestDbConn.getSysDbConn();
       String updateSql = "update oa_notify set READERS='' where SEQ_ID='"+seqId+"'";
       if(!"1".equals(loginUser.getUserPriv())){
         updateSql  =  updateSql + " and FROM_ID ='"+loginUser.getSeqId()+"'";
       }
       st = dbConn.createStatement();
      st.executeUpdate(updateSql);
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG,"终止生效状态已修改");
  } catch (Exception ex) {
    String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, message);
    throw ex;
  }
  //return "/core/funcs/dept/deptinput.jsp";
  //?deptParentDesc=+deptParentDesc
  return "/core/funcs/notify/show/showReader.jsp";
  }
  
  /**
   * 标记所有为已读
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String changeNoReadAll(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    Statement stmt = null;
    ResultSet rs = null;
    YHPerson loginUser = null;
    loginUser = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String changeSql = "update oa_notify set READERS ="+dbLikeLast("READERS",String.valueOf(loginUser.getSeqId()))+" where PUBLISH='1' and "+ dbLikePre("READERS","','")+" not like '%"+","+loginUser.getSeqId()+",%' or READERS is null";
      //YHOut.println(changeSql);
      stmt = dbConn.createStatement();
      stmt.executeUpdate(changeSql);
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    String foward = request.getParameter(YHActionKeys.RET_METHOD_FORWARD);
    if (YHUtility.null2Empty(foward).equalsIgnoreCase("all")) {
      return "/core/funcs/notify/show/notifyAll.jsp";
    }else {
      return "/core/funcs/notify/show/notifyNoRead.jsp";
    }
  }
  
  public static String dbLikeLast(String fieldName, String value) throws SQLException {
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
      if (dbms.equals("sqlserver")) {
          return "ISNULL("+fieldName+",'')" + "+ '" + value+",' ";
        }else if (dbms.equals("mysql")) {
          return "concat(IFNULL("+fieldName+",''),'" + value + ",')";
        }else if (dbms.equals("oracle")) {
          return fieldName + "||'" + value+",'";
        }else {
          throw new SQLException("not accepted dbms");
        } 
    }
    
    public static String dbLikePre(String fieldName, String fix) throws SQLException {
      String dbms = YHSysProps.getProp("db.jdbc.dbms");
      if (dbms.equals("sqlserver")) {
          return fix +"+" +" ISNULL("+ fieldName +",'')";
        }else if (dbms.equals("mysql")) {
          return "concat("+fix+",IFNULL("+fieldName+",''))";
        }else if (dbms.equals("oracle")) {
          return fix + "||" + fieldName;
        }else {
          throw new SQLException("not accepted dbms");
        } 
    }
  
  /**
   * 公告通知桌面模块
   * @param request
   * @param response
   * @return
   * @throws Exception
   */

  public String getdeskNotifyAllList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    Statement stmt = null;
    ResultSet rs = null;
    StringBuffer sb = new StringBuffer("[");
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    int count = 0;
    int len = 0;
    String queryNotifySql = "";
//  String loginUserId = request.getParameter("loginUserId");
    YHPerson loginUser = null;
    loginUser = (YHPerson)request.getSession().getAttribute("LOGIN_USER");

    List<YHNews> list = new ArrayList<YHNews>();
  
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int loginDeptId = loginUser.getDeptId();
      String userPriv = loginUser.getUserPriv();
      int seqUserId = loginUser.getSeqId();
      String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
      if(loginUser.isAdmin()){
        if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
          queryNotifySql= "SELECT SEQ_ID,FROM_ID,TO_ID,SUBJECT,[TOP],TOP_DAYS,PRIV_ID,USER_ID,READERS,"
            +"TYPE_ID,SEND_TIME,BEGIN_DATE,FORMAT from oa_notify  where  BEGIN_DATE<="+ YHDBUtility.currDateTime()+" and (END_DATE>" + YHDBUtility.currDateTime()
            +" or END_DATE is null) and PUBLISH='1' order by SEND_TIME desc";
        }else if(dbms.equals(YHConst.DBMS_MYSQL)){
          queryNotifySql= "SELECT SEQ_ID,FROM_ID,TO_ID,SUBJECT,TOP,TOP_DAYS,PRIV_ID,USER_ID,READERS,"
            +"TYPE_ID,SEND_TIME,BEGIN_DATE,FORMAT from oa_notify  where  BEGIN_DATE<="+ YHDBUtility.currDateTime()+" and (END_DATE>" + YHDBUtility.currDateTime()
            +" or END_DATE is null) and PUBLISH='1' order by SEND_TIME desc limit 10";
        }else if(dbms.equals(YHConst.DBMS_ORACLE)){
        	queryNotifySql= "select * from(SELECT /*+index(oa_notify)*/ SEQ_ID,FROM_ID,TO_ID,SUBJECT,TOP,TOP_DAYS,PRIV_ID,USER_ID,READERS,"
                    +"TYPE_ID,SEND_TIME,BEGIN_DATE,FORMAT from oa_notify  where  BEGIN_DATE<="+ YHDBUtility.currDateTime()+" and (END_DATE>" + YHDBUtility.currDateTime()
                    +" or END_DATE is null) and PUBLISH='1' order by SEND_TIME desc) where rownum<=10";
        }
      }else{      
        if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
          queryNotifySql = "SELECT SEQ_ID,FROM_ID,TO_ID,SUBJECT,[TOP],TOP_DAYS,PRIV_ID,USER_ID,READERS,"
            +"TYPE_ID,SEND_TIME,BEGIN_DATE,FORMAT from oa_notify  where  BEGIN_DATE<="+ YHDBUtility.currDateTime() +" and (END_DATE>"+YHDBUtility.currDateTime() +
            " or END_DATE is null) and PUBLISH='1' and ("+ YHDBUtility.findInSet(Integer.toString(loginDeptId), "TO_ID")
            + " or " + YHDBUtility.findInSet(userPriv,"PRIV_ID") +" or " 
            + YHDBUtility.findInSet(Integer.toString(seqUserId),"USER_ID" ) + " or "+YHDBUtility.findInSet("0", "TO_ID")+") ";
          queryNotifySql = queryNotifySql + " order by SEND_TIME desc";
        }else if(dbms.equals(YHConst.DBMS_MYSQL)){
          queryNotifySql = "SELECT SEQ_ID,FROM_ID,TO_ID,SUBJECT,TOP,TOP_DAYS,PRIV_ID,USER_ID,READERS,"
            +"TYPE_ID,SEND_TIME,BEGIN_DATE,FORMAT from oa_notify  where  BEGIN_DATE<="+ YHDBUtility.currDateTime() +" and (END_DATE>"+YHDBUtility.currDateTime() +
            " or END_DATE is null) and PUBLISH='1' and ("+ YHDBUtility.findInSet(Integer.toString(loginDeptId), "TO_ID")
            + " or " + YHDBUtility.findInSet(userPriv,"PRIV_ID") +" or " 
            + YHDBUtility.findInSet(Integer.toString(seqUserId),"USER_ID" ) + " or "+YHDBUtility.findInSet("0", "TO_ID")+") ";
          queryNotifySql = queryNotifySql + " order by SEND_TIME desc  limit 10";
        }else if(dbms.equals(YHConst.DBMS_ORACLE)){
        	queryNotifySql = "select * from (SELECT /*+index(oa_notify)*/ SEQ_ID,FROM_ID,TO_ID,SUBJECT,TOP,TOP_DAYS,PRIV_ID,USER_ID,READERS,"
                    +"TYPE_ID,SEND_TIME,BEGIN_DATE,FORMAT from oa_notify  where  BEGIN_DATE<="+ YHDBUtility.currDateTime() +" and (END_DATE>"+YHDBUtility.currDateTime() +
                    " or END_DATE is null) and PUBLISH='1' and ("+ YHDBUtility.findInSet(Integer.toString(loginDeptId), "TO_ID")
                    + " or " + YHDBUtility.findInSet(userPriv,"PRIV_ID") +" or " 
                    + YHDBUtility.findInSet(Integer.toString(seqUserId),"USER_ID" ) + " or "+YHDBUtility.findInSet("0", "TO_ID")+") ";
                  queryNotifySql = queryNotifySql + " order by SEND_TIME desc ) where rownum<=10";
        }
      }
      //YHOut.println(queryNotifySql);
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryNotifySql);
      
      while(rs.next() && ((++len) < MAX)) {
/*        if("ALL_DEPT".equals(rs.getString("TO_ID"))||
            "0".equals(rs.getString("TO_ID"))||
            findToId(rs.getString("TO_ID"),Integer.toString(loginDeptId))==true||
            findToId(rs.getString("PRIV_ID"),userPriv)==true||
            findToId(rs.getString("USER_ID"),Integer.toString(seqUserId))==true){*/
          count ++;
          int readerCount = 0;
          String readers = rs.getString("READERS");
          if(!"".equals(readers)&&readers!=null){
            readerCount = readers.split(",").length;
          }
          sb.append("{");
          sb.append("seqId:" + rs.getInt("SEQ_ID"));
          sb.append(",subject:\"" + YHUtility.encodeSpecial(rs.getString("SUBJECT")) + "\"");
          sb.append(",readerCount:\"" + readerCount + "\"");
          sb.append(",sendTime:\"" + rs.getDate("SEND_TIME") + "\"");
          sb.append(",iread:\"" + notifyShowLogic.haveRead(dbConn, loginUser.getSeqId(), rs.getInt("SEQ_ID")) + "\"");
          sb.append("},");
       /* }*/
      }
      if(count>0) {
        sb.deleteCharAt(sb.length() - 1); 
        }
      sb.append("]");
      //YHOut.println(sb.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    //return "/core/funcs/dept/deptinput.jsp";
    //?deptParentDesc=+deptParentDesc
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 查询所有仓库需要交货的提醒
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryAllPurchaseRemind(HttpServletRequest request,
	      HttpServletResponse response) throws Exception {
	    Connection dbConn = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    StringBuffer sb = new StringBuffer("[");
	    Date currentDate = new Date();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    int count = 0;
	    int len = 0;
	    String queryNotifySql = "";
	    YHPerson loginUser = null;
	    loginUser = (YHPerson)request.getSession().getAttribute("LOGIN_USER");

	    try {
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
	          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      int seqUserId = loginUser.getSeqId();
	      
	      queryNotifySql= "SELECT purOut.id AS purOutId, " +
	      		"pc.id as id, " +
	      		"pc.code,pc.title,pc.person," +
	      		"pc.sign_date,pc.status," +
	      		"pur_pro.date as deliveryDate FROM erp_purchase pc " +
	      		"LEFT JOIN erp_purchase_product_out " +
	      		"purOut ON purOut.purchase_id=pc.id LEFT JOIN " +
	      		"erp_ppo_pro pur_pro ON pur_pro.ppo_id=purOut.id " +
	      		"WHERE pc.person_id="+ seqUserId +" ORDER BY pur_pro.date desc";

	      stmt = dbConn.createStatement();
	      rs = stmt.executeQuery(queryNotifySql);
	      int showMAXRecode = 10; //桌面显示的最大记录条数
	      while(rs.next() && ((++len) < showMAXRecode)) {
	    	  String deliveryDate = rs.getString("deliveryDate"); //交货日期
	    	  String currentDateStr = sdf.format(currentDate);
	    	  Date deliveryDate2 = sdf.parse(deliveryDate);
	    	  if((deliveryDate2.getTime() - currentDate.getTime() > 0) || deliveryDate.equals(currentDateStr)){
		          count ++;
		          sb.append("{");
		          sb.append("seqId:\"" + rs.getString("id") + "\"");
		          sb.append(",code:\"" + rs.getString("code") + "\"");
		          sb.append(",title:\"" + YHUtility.encodeSpecial(rs.getString("title")) + "\"");
		          sb.append(",person:\"" + rs.getString("person") + "\"");
		          sb.append(",deliveryDate:\"" + rs.getString("deliveryDate") + "\"");
		          sb.append(",status:\"" + rs.getString("status") + "\"");
		          sb.append("},");
	    	  }
	      }
	      if(count>0) {
	        sb.deleteCharAt(sb.length() - 1); 
	        }
	      sb.append("]");
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
	      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
	    } catch (Exception ex) {
	      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, message);
	      throw ex;
	    }
	    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 查询所有销售订单需要发货的的提醒
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryAllsendFormRemind(HttpServletRequest request,
		  HttpServletResponse response) throws Exception {
	  Connection dbConn = null;
	  Statement stmt = null;
	  ResultSet rs = null;
	  StringBuffer sb = new StringBuffer("[");
	  Date currentDate = new Date();
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	  int count = 0;
	  int len = 0;
	  String queryNotifySql = "";
	  YHPerson loginUser = null;
	  loginUser = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
	  
	  try {
		  YHRequestDbConn requestDbConn = (YHRequestDbConn) request
		  .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		  dbConn = requestDbConn.getSysDbConn();
		  int seqUserId = loginUser.getSeqId();
		  
		  queryNotifySql = "SELECT po.id,o.order_code," +
		  		"po.po_code,po.po_title," +
		  		"po.order_id,po.po_status,pod.pod_date, pod.pod_sender_id " +
		  		"FROM erp_order_product_out po," +
		  		"erp_sale_order o,erp_order_product_out_detail pod " +
		  		"WHERE o.id = po.order_id AND " +
		  		"po.id=pod.po_id AND o.id=pod.order_id "+
		  		"AND pod.pod_sender_id="+ seqUserId +" ORDER BY pod.pod_date desc";
		  
		  stmt = dbConn.createStatement();
		  rs = stmt.executeQuery(queryNotifySql);
		  int showMAXRecode = 10; //桌面显示的最大记录条数
		  while(rs.next() && ((++len) < showMAXRecode)) {
			  String pod_date = rs.getString("pod_date"); //交货日期
			  String currentDateStr = sdf.format(currentDate);
			  Date pod_date2 = sdf.parse(pod_date);
//			  if((pod_date2.getTime() - currentDate.getTime() > 0) || pod_date.equals(currentDateStr)){
				  count ++;
				  sb.append("{");
				  sb.append("seqId:\"" + rs.getString("id") + "\"");
				  sb.append(",order_code:\"" + rs.getString("order_code") + "\"");
				  sb.append(",po_code:\"" + rs.getString("po_code") + "\"");
				  sb.append(",po_title:\"" + YHUtility.encodeSpecial(rs.getString("po_title")) + "\"");
				  sb.append(",po_status:\"" + rs.getString("po_status") + "\"");
				  sb.append(",pod_sender_id:\"" + rs.getString("pod_sender_id") + "\"");
				  sb.append(",pod_date:\"" + rs.getString("pod_date") + "\"");
				  sb.append("},");
//			  }
		  }
		  if(count>0) {
			  sb.deleteCharAt(sb.length() - 1); 
		  }
		  sb.append("]");
		  request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		  request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
		  request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
	  } catch (Exception ex) {
		  String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
		  request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		  request.setAttribute(YHActionKeys.RET_MSRG, message);
		  throw ex;
	  }
	  return "/core/inc/rtjson.jsp";
  }
}

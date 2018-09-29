package yh.core.funcs.notify.act;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.notify.data.YHNotify;
import yh.core.funcs.notify.logic.YHNotifyAuditingLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHORM;

public class YHNotifyAuditingAct {
  private static Logger log = Logger.getLogger(YHNotifyAuditingAct.class);
  /**
   * 待审批的公告
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getUnAuditedList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    Statement st = null;
    ResultSet rs = null;
    YHNotify notify = null;
    String data = "";
    int notifyAuditingSingle = 0;
    String type = request.getParameter("type");//下拉框中类型
    String ascDesc = request.getParameter("ascDesc");//升序还是降序
    String field = request.getParameter("field");//排序的字段

    String showLenStr = request.getParameter("showLength");//每页显示长度
    String pageIndexStr = request.getParameter("pageIndex");//页码数

    if("".equals(ascDesc)) {
      ascDesc = "1";
    }
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String queryPapaSql = "select PARA_VALUE from SYS_PARA where PARA_NAME='NOTIFY_AUDITING_SINGLE'";
      st = dbConn.createStatement();
      rs = st.executeQuery(queryPapaSql);
      if(rs.next()) {
        String papaValue =  rs.getString("PARA_VALUE");
        if(!"1".equals(papaValue)){
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
          request.setAttribute(YHActionKeys.RET_MSRG,"请在系统管理中设置审批参数!");
          return "/core/inc/rtjson.jsp";
        }else{
          String queryAuditerSql = "select PARA_VALUE from SYS_PARA where PARA_NAME='NOTIFY_AUDITING_ALL'";
          Statement stmt = null;
          ResultSet rss = null;
          stmt = dbConn.createStatement();
          rss = stmt.executeQuery(queryAuditerSql);
          if(rss.next()) {
            String papaValues = rss.getString(1);
            if(papaValues != null && !"".equals(papaValues)){
              String[] papaValuess = papaValues.split(",");
              for(int j = 0 ;j < papaValuess.length ; j++){
                String papaValuetemp =  papaValuess[j];
                if(papaValuetemp.equals(Integer.toString(person.getSeqId()))){
                   notifyAuditingSingle = 1;
                   break;
                }
              }
            }

          }
        }
      }
      
      if(notifyAuditingSingle==1){
        YHNotifyAuditingLogic notifyAuditingLogic = new YHNotifyAuditingLogic();
        data = notifyAuditingLogic.getUnAuditedList(dbConn,person,type,ascDesc,field,Integer.parseInt(showLenStr),Integer.parseInt(pageIndexStr));
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
        request.setAttribute(YHActionKeys.RET_DATA, data);
      }else {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG,"您没有审批权限！");
        return "/core/inc/rtjson.jsp";
      }
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  //批准
  public String beforeAuditingPass(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String seqId = request.getParameter("seqId");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = "";
      YHNotifyAuditingLogic notifyAuditingLogic = new YHNotifyAuditingLogic();
      data = notifyAuditingLogic.beforeAuditingPass(dbConn, seqId);
      //YHOut.println("&&&&&&&"+data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
    }
  
  public String operation(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    YHNotifyAuditingLogic notifyAuditingLogic = new YHNotifyAuditingLogic();
    String seqId = request.getParameter("seqId");//seqId
    String reason = request.getParameter("reason");//
    String top = request.getParameter("top");
    String topDays = request.getParameter("topDays");
    String sendTime = request.getParameter("sendTime");
    String beginDate = request.getParameter("beginDate");
    String endDate = request.getParameter("endDate");
    String operation = request.getParameter("operation");//操作
    String mailRemind = request.getParameter("mailRemind");//操作
    //YHOut.println(mailRemind);
    
    YHPerson loginUser = null;
    Connection dbConn = null;
    YHORM orm = new YHORM();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     loginUser = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
     try {
       YHRequestDbConn requestDbConn = (YHRequestDbConn) request
           .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       dbConn = requestDbConn.getSysDbConn();
       YHNotify notify = (YHNotify)orm.loadObjSingle(dbConn, YHNotify.class, Integer.parseInt(seqId));
       if(!"".equals(reason)&&reason!=null){
         notify.setReason(reason);
       }
       if(!"".equals(top)&&top!=null){
         notify.setTop(top);
       }
       if(!"".equals(topDays)&&topDays!=null){
         notify.setTopDays(Integer.parseInt(topDays));
       }
       if(!"".equals(sendTime)&&sendTime!=null){
         notify.setSendTime(sdf1.parse(sendTime));
       }
       if(!"".equals(beginDate)&&beginDate!=null){
         notify.setBeginDate(sdf.parse(beginDate));
       }
       if(!"".equals(endDate)&&endDate!=null){
         notify.setEndDate(sdf.parse(endDate));
       }
    notifyAuditingLogic.operation(dbConn, loginUser,notify,operation, mailRemind);
    
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
  return "/core/funcs/notify/auditing/unaudited.jsp";
  }
  
  //已审批公告列表
  public String getAuditedList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    Statement st = null;
    ResultSet rs = null;
    YHNotify notify = null;
    String data = "";
    int notifyAuditingSingle = 0;
    String type = request.getParameter("type");//下拉框中类型
    String ascDesc = request.getParameter("ascDesc");//升序还是降序
    String field = request.getParameter("field");//排序的字段

    String showLenStr = request.getParameter("showLength");//每页显示长度
    String pageIndexStr = request.getParameter("pageIndex");//页码数

    if("".equals(ascDesc)) {
      ascDesc = "1";
    }
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
        YHNotifyAuditingLogic notifyAuditingLogic = new YHNotifyAuditingLogic();
        data = notifyAuditingLogic.getAuditedList(dbConn,person,type,ascDesc,field,Integer.parseInt(showLenStr),Integer.parseInt(pageIndexStr));
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
        request.setAttribute(YHActionKeys.RET_DATA, data);
     
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}

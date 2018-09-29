package yh.core.funcs.doc.receive.act;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.doc.receive.data.YHDocReceive;
import yh.core.funcs.doc.receive.logic.YHDocReceiveRegLogic;
import yh.core.funcs.doc.util.YHDocUtility;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;

/**
 * 收文登记
 * @author lh
 *
 */
public class YHDocReceiveRegAct{
  /**
   * 登记收文
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String updateReceive(HttpServletRequest request, HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHDocReceive doc = new YHDocReceive();
      String docNo = request.getParameter("docNo");
      String fromUnits = request.getParameter("fromUnits");
      String oppDocNo = request.getParameter("oppDocNo");
      String title = request.getParameter("title");
      String copies = request.getParameter("copies");
      String confLevel = request.getParameter("confLevel");
      String instruct = request.getParameter("instruct");
      String docType = request.getParameter("docType");
      String sponsor = request.getParameter("deptId");
      String personId = request.getParameter("user");
      String alarm = request.getParameter("alarm");
      String attachName = request.getParameter("attachmentName");
      String attachId = request.getParameter("attachmentId");
      String seqId = request.getParameter("seqId");

      doc.setSeq_id(Integer.parseInt(seqId));
      int userId = Integer.parseInt(personId);
      doc.setDocNo(docNo);
      doc.setFromUnits(fromUnits);
      doc.setOppdocNo(oppDocNo);
      doc.setTitle(title);
      doc.setCopies(Integer.parseInt(copies));
      doc.setConfLevel(Integer.parseInt(confLevel));
      doc.setInstruct(instruct);
      doc.setDocType(Integer.parseInt(docType));
      doc.setSponsor(sponsor);
      doc.setUserId(userId);
      doc.setAttachNames(attachName);
      doc.setAttachIds(attachId);
      doc.setSendStauts(0);
      YHDocReceiveRegLogic logic = new YHDocReceiveRegLogic();
      logic.updateDocReceive(dbConn, doc);
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      String content = user.getUserName() + "提醒：请签收您的收文!收文文号:" + docNo;  
      String url =  "/core/funcs/doc/receive/readdocindex.jsp";
//      if(!YHUtility.isNullorEmpty(alarm)){
//        YHDocSmsLogic.sendSms(user, dbConn, content, url, recipient, null);
//      }
      request.setAttribute("msg", "登记成功！");
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e; 
    }
    return "/core/funcs/doc/receive/msgBox2.jsp";
  }
  /**
   * 取得未登记的记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getNReg(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    YHPerson loginUser = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      loginUser = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      YHDocReceiveRegLogic logic = new YHDocReceiveRegLogic();
      StringBuffer result = logic.getRegList(dbConn, request.getParameterMap(), loginUser, "2" , request.getRealPath("/"));
      PrintWriter pw = response.getWriter();
      pw.println( result.toString());
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  /**
   * 根据seq_id取得未登记的记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRecReg(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String webroot = request.getRealPath("/");
      YHDocReceiveRegLogic logic = new YHDocReceiveRegLogic();
      String str = logic.getRecReg(dbConn, Integer.parseInt(seqId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 根据用户有登记权限的部门

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectDept(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHDocUtility util = new YHDocUtility();
      YHDeptLogic logic = new YHDeptLogic();
      String data = "";
      if (!util.haveAllRight(user, dbConn)) {
        String dept = util.deptRight(userId, dbConn);
        data = "[";
        if (!YHUtility.isNullorEmpty(dept)) {
          String name = logic.getNameByIdStr(dept, dbConn);
          String[] depts = dept.split(",");
          String[] names = name.split(",");
          
          for (int i = 0 ;i < depts.length ; i++) {
            String tmp = depts[i];
            String tmp2 = names[i];
            data += "{";
            data += "value:\"" + tmp + "\"";
            data += ",text:\"" + YHUtility.encodeSpecial(tmp2) + "\"";
            data += "},";
          }
          if (data.endsWith(",")) {
            data = YHWorkFlowUtility.getOutOfTail(data);
          }
        }
        data += "]";
      } else {
        //查询所有的部门
        data = logic.getDeptTreeJson(0, dbConn);
      }
      if (YHUtility.isNullorEmpty(data)) {
        data = "[]";
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
}

package yh.core.funcs.doc.act;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHUserPriv;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.person.logic.YHUserPrivLogic;
import yh.core.funcs.doc.data.YHDocFlowFormItem;
import yh.core.funcs.doc.data.YHDocFlowProcess;
import yh.core.funcs.doc.data.YHDocFlowType;
import yh.core.funcs.doc.logic.YHFlowFormLogic;
import yh.core.funcs.doc.logic.YHFlowProcessLogic;
import yh.core.funcs.doc.logic.YHFlowTypeLogic;
import yh.core.funcs.doc.logic.YHFlowUserSelectLogic;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.funcs.doc.util.sort.YHFlowProcessComparator;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.module.org_select.logic.YHOrgSelectLogic;
public class YHProcessUserSelectAct {
  private static Logger log = Logger
      .getLogger("yh.core.funcs.doc.act.YHProcessUserSelectAct");

  public String getUsers(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPersonLogic logic = new YHPersonLogic();
      YHFlowProcessLogic fp = new YHFlowProcessLogic();
      YHDocFlowProcess proc = fp.getFlowProcessById(Integer.parseInt(seqId) , dbConn);

      String ids = proc.getPrcsUser();

      String data = logic.getPersonSimpleJson(ids , dbConn);

      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "get Success");
      request.setAttribute(YHActionKeys.RET_DATA, "[" + data + "]");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public String getPrivUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String sPrcsId = request.getParameter("prcsId");
    String sFlowId = request.getParameter("flowId");
    String sSeqId  = request.getParameter("seqId");
    
    String sDeptId = request.getParameter("deptId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowProcessLogic logic = new YHFlowProcessLogic();
      YHDocFlowProcess fp  = null; 
      
      if(sSeqId == null || "".equals(sSeqId) || "null".equals(sSeqId)){
        int flowId =  Integer.parseInt(sFlowId);
        fp = logic.getFlowProcessById(flowId, sPrcsId , dbConn);
      }else{
        int seqId = Integer.parseInt(sSeqId);
        fp = logic.getFlowProcessById(seqId, dbConn);
      }
      String data = "";
      if (fp != null) {
        String user = fp.getPrcsUser();//人员
        String dept = sDeptId;
        String priv = fp.getPrcsPriv();//角色
        YHFlowUserSelectLogic select = new YHFlowUserSelectLogic();
        
        dept = YHOrgSelectLogic.changeDept(dbConn, fp.getPrcsDept()); ///部门
        data = select.getPersonInDept(user, dept, priv, dbConn, sDeptId);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "取得成功！");
      request.setAttribute(YHActionKeys.RET_DATA, "[" + data + "]");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getUserByRole(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String sPrcsId = request.getParameter("prcsId");
    String sFlowId = request.getParameter("flowId");
    String sSeqId  = request.getParameter("seqId");
    String sRoleId = request.getParameter("roleId");
    int roleId = Integer.parseInt(sRoleId);
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHFlowProcessLogic logic = new YHFlowProcessLogic();
      YHDocFlowProcess fp;
      if(sSeqId == null || "".equals(sSeqId) || "null".equals(sSeqId)){
        int flowId =  Integer.parseInt(sFlowId);
        fp = logic.getFlowProcessById(flowId, sPrcsId , dbConn);
      }else{
        int seqId = Integer.parseInt(sSeqId);
        fp = logic.getFlowProcessById(seqId, dbConn);
      }
      String deptStr = YHOrgSelectLogic.changeDept(dbConn, fp.getPrcsDept()); 
      String userStr =  fp.getPrcsUser() == null ? "" : fp.getPrcsUser();
      String roleStr = fp.getPrcsPriv()  == null ? "" : fp.getPrcsPriv();
      deptStr = deptStr  == null ? "" : deptStr;
      YHFlowUserSelectLogic select = new YHFlowUserSelectLogic();
      
      //取转交相关数据
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "");
      request.setAttribute(YHActionKeys.RET_DATA, "{principalRole:[" + select.getUserByRoleP(roleId, fp, dbConn , user) 
          + "],supplementRole:["
          + select.getUserBySupplementRoleP(roleId, fp, dbConn , user) + "]}");
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}

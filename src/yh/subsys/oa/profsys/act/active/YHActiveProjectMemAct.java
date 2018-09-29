package yh.subsys.oa.profsys.act.active;

import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.subsys.oa.profsys.data.YHProjectMem;
import yh.subsys.oa.profsys.logic.active.YHActiveProjectMemLogic;

public class YHActiveProjectMemAct {

  /**
   * 来访项目人员By ProjId
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryActiveMemByProjId(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId(); 
      String projId = request.getParameter("projId");
      if(YHUtility.isNullorEmpty(projId)){
        projId = "0";
      }
      YHActiveProjectMemLogic tbal = new YHActiveProjectMemLogic();
      String data = tbal.toSearchData(dbConn, request.getParameterMap(),projId,"0");
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  /**
   * 查询大型活动项目
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String profsysSelectActiveMem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHProjectMem mem = new YHProjectMem();
      String memNum = request.getParameter("memNum");
      String memPosition = request.getParameter("memPosition");
      String memName = request.getParameter("memName");
      String memSex = request.getParameter("memSex");
      String unitNum = request.getParameter("unitNum");
      String unitName = request.getParameter("unitName");
      String projMemType = request.getParameter("projMemType");
      mem.setMemNum(memNum);
      mem.setMemPosition(memPosition);
      mem.setMemName(memName);
      mem.setMemSex(memSex);
      mem.setUnitNum(unitNum);
      mem.setUnitName(unitName);
      mem.setProjMemType(projMemType);
      String projId = YHActiveProjectMemLogic.memSeqId(dbConn, mem);
      String data = YHActiveProjectMemLogic.profsysSelectActiveMem(dbConn,request.getParameterMap(),projId,projMemType);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
}

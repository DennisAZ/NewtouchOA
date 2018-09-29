package yh.cms.permissions.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.cms.permissions.logic.YHPermissionsLogic;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;

public class YHPermissionsAct {

  /**
   * 获取cms人员权限

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getPersonIdStr(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn;
    String userType = request.getParameter("userType");
    String seqId = request.getParameter("seqId");
    if(YHUtility.isNullorEmpty(seqId)){
      seqId = "0";
    }
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPermissionsLogic logic = new YHPermissionsLogic();
      String data = logic.getPermissions(dbConn, userType, seqId);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 设置cms人员权限

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String setVisitById(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn;
    String stringInfo = request.getParameter("stringInfo");
    String userType = request.getParameter("userType");
    String seqId = request.getParameter("seqId");
    String override = request.getParameter("override");
    if(YHUtility.isNullorEmpty(seqId)){
      seqId = "0";
    }
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPermissionsLogic logic = new YHPermissionsLogic();
      logic.setPermissions(dbConn, userType, seqId, stringInfo);
      if(!YHUtility.isNullorEmpty(override)){
        logic.setPermissionsChild(dbConn, userType, seqId, stringInfo);
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取cms人员权限-栏目

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getPersonIdStrColumn(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn;
    String userType = request.getParameter("userType");
    String seqId = request.getParameter("seqId");
    if(YHUtility.isNullorEmpty(seqId)){
      seqId = "0";
    }
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPermissionsLogic logic = new YHPermissionsLogic();
      String data = logic.getPermissionsColumn(dbConn, userType, seqId);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 设置cms人员权限-栏目

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String setVisitByIdColumn(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn;
    String stringInfo = request.getParameter("stringInfo");
    String userType = request.getParameter("userType");
    String seqId = request.getParameter("seqId");
    String override = request.getParameter("override");
    if(YHUtility.isNullorEmpty(seqId)){
      seqId = "0";
    }
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPermissionsLogic logic = new YHPermissionsLogic();
      logic.setPermissionsColumn(dbConn, userType, seqId, stringInfo);
      if(!YHUtility.isNullorEmpty(override)){
        logic.setPermissionsChildColumn(dbConn, userType, seqId, stringInfo);
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取CMS树型结构-无权限

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getColumnTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String id = request.getParameter("id");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      dbConn = requestDbConn.getSysDbConn();
      YHPermissionsLogic logic = new YHPermissionsLogic();
      String data = "";
      if (!YHUtility.isNullorEmpty(id) && !id.equals("0")) {
        String idArry[] = id.split(",");
        if (idArry != null && idArry.length > 0) {
          data = logic.getColumnTree(dbConn, idArry[0], idArry[1], person);
        }
      } else {
        data = logic.getStationTree(dbConn, person);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 设置cms人员权限-栏目

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String setContentPermissions(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn;
    String stringInfo = request.getParameter("stringInfo");
    String userType = request.getParameter("userType");
    String seqId = request.getParameter("seqId");
    String override = request.getParameter("override");
    if(YHUtility.isNullorEmpty(seqId)){
      seqId = "0";
    }
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPermissionsLogic logic = new YHPermissionsLogic();
      logic.setPermissionsColumn(dbConn, userType, seqId, stringInfo);
      if(!YHUtility.isNullorEmpty(override)){
        logic.setPermissionsChildColumn(dbConn, userType, seqId, stringInfo);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  } 
}

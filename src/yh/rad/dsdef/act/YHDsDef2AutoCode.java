package yh.rad.dsdef.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHStringFormat;
import yh.rad.dsdef.logic.YHDsDef2AutoCodeLogic;
import yh.rad.velocity.YHCodeUtil;
import yh.rad.velocity.metadata.YHField;
import yh.rad.velocity.metadata.YHGridField;

/**
 * 用于生成数据字典的物理结构
 * @author Think
 *
 */
public class YHDsDef2AutoCode {

  /**
   *生成物理结构
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String parserTemp(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      YHDsDef2AutoCodeLogic dda = new YHDsDef2AutoCodeLogic();
      String data = dda.fileTemp2Json();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String parserTempXml(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      YHDsDef2AutoCodeLogic dda = new YHDsDef2AutoCodeLogic();
      String xmlName = request.getParameter("tempName");
      String data = dda.xmlTemp2Json(xmlName);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String autoCode(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      YHDsDef2AutoCodeLogic dda = new YHDsDef2AutoCodeLogic();
      String xmlName = request.getParameter("tempName");
      String data = dda.xmlTemp2Json(xmlName);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String code2java(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String tableNo = request.getParameter("tableNo").trim();
      String pojectName = request.getParameter("pojectName").trim();
      String tempPoj = request.getParameter("tempPoj").trim();

      YHDsDef2AutoCodeLogic dsac = new YHDsDef2AutoCodeLogic();
      dsac.autoCode(dbConn, tableNo, pojectName, tempPoj);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "代码生成成功！");
    } catch(Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "代码生成失败,请检查数据字典是否配置正确！");
      e.printStackTrace();
    }
    return "/core/inc/rtjson.jsp";
  }
}

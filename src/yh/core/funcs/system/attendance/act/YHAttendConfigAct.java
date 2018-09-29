package yh.core.funcs.system.attendance.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.system.attendance.data.YHAttendConfig;
import yh.core.funcs.system.attendance.logic.YHAttendConfigLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.form.YHFOM;

public class YHAttendConfigAct {
  /**
   * 
   * 添加排版定义类型
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addConfig(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAttendConfig config = new YHAttendConfig();
      YHAttendConfigLogic yhacl = new YHAttendConfigLogic();
      YHFOM fom = new YHFOM();
      config =  (YHAttendConfig)fom.build(request.getParameterMap());
      yhacl.addConfig(dbConn, config);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, "data");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询所有排班类型
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectConfig(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAttendConfig config = new YHAttendConfig();
      YHAttendConfigLogic yhacl = new YHAttendConfigLogic();
      String data = "[";
      Map map = new HashMap();
      List<YHAttendConfig> configList = yhacl.selectConfig(dbConn, map);
      for (int i = 0; i < configList.size(); i++) {
        data = data+(YHFOM.toJson(configList.get(i))).toString() + ",";
      }
      if(configList.size() > 0 ){
        data = data.substring(0, data.length() - 1);
      }
      data = data + "]";
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 查询单个排班类型
   * ById
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectConfigById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String data = "";
      YHAttendConfig config = new YHAttendConfig();
      YHAttendConfigLogic yhacl = new YHAttendConfigLogic();
      if(!seqId.equals("")){
        config = yhacl.selectConfigById(dbConn, seqId);
        data = data+(YHFOM.toJson(config)).toString() + "";
      }else{
        data = "[]";
      } 
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, " 查询成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 更新排班类型ById
   * 
   */
  public String updateConfigById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      if(!seqId.equals("") && seqId != null){
        YHAttendConfig config = new YHAttendConfig();
        config = (YHAttendConfig) YHFOM.build(request.getParameterMap());
        config.setSeqId(Integer.parseInt(seqId));
        YHAttendConfigLogic yhacl = new YHAttendConfigLogic();
        yhacl.updateConfig(dbConn, config);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "更新成功！");
      }else{
        addConfig(request,response);
      }  
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /*
   * 
   * 单个删除排版 ById
   */
  public String deleteConfigById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHAttendConfigLogic yhacl = new YHAttendConfigLogic();
      yhacl.deleteConfig(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/system/attendance/attendtype.jsp";
  }
  /*
   * 
   * 更新单个排班类型的公休日 ById
   */
  public String  updateConfigGenaralById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String week1 = request.getParameter("week1");
      String week2 = request.getParameter("week2");
      String week3 = request.getParameter("week3");
      String week4 = request.getParameter("week4");
      String week5 = request.getParameter("week5");
      String week6 = request.getParameter("week6");
      String week7 = request.getParameter("week7");
      String general = "";
      if(week1 != null){
        general = general + week1 + ",";
      }
      if(week2 != null){
        general = general + week2 + ",";
      }
      if(week3 != null){
        general = general + week3 + ",";
      }
      if(week4 != null){
        general = general + week4 + ",";
      }
      if(week5 != null){
        general = general + week5 + ",";
      }
      if(week6 != null){
        general = general + week6 + ",";
      }
      if(week7 != null){
        general = general + week7 + ",";
      }
      if(!general.equals("")){
        general = general.substring(0,general.length()-1);
      }
      YHAttendConfig config = new YHAttendConfig();
      YHAttendConfigLogic yhacl = new YHAttendConfigLogic();
      yhacl.updateConfigGenaralById(dbConn, seqId, general);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！"); 
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}

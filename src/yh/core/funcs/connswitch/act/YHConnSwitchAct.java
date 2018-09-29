package yh.core.funcs.connswitch.act;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.funcs.connswitch.logic.YHConnSwitchLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHConst;
import yh.setup.util.YHERPSetupUitl;

public class YHConnSwitchAct {
  private YHConnSwitchLogic logic=new YHConnSwitchLogic();
  public String getConnectingDbms(HttpServletRequest request
      ,HttpServletResponse response) throws Exception{
    try {
      String data="";
      data=logic.getConnectingDbms();
      data="{dbms:'"+data+"'}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,  ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getSwitchDbms(HttpServletRequest request
      ,HttpServletResponse response) throws Exception{
    try {
      String dbms=request.getParameter("dbms");
      String rootPath = request.getRealPath("/");
      logic.getSwitchDbms(dbms,rootPath);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,  ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getOraConnInfo(HttpServletRequest request
      ,HttpServletResponse response) throws Exception{
    try {
      String data="";
      data=logic.getOraConnInfo();
     
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,  ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getSqlConnInfo(HttpServletRequest request
      ,HttpServletResponse response) throws Exception{
    try {
      String data="";
      data=logic.getSqlConnInfo();
     
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,  ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getMysqlConnInfo(HttpServletRequest request
      ,HttpServletResponse response) throws Exception{
    try {
      String data="";
      data=logic.getMysqlConnInfo();
     
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,  ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String testOraConnect(HttpServletRequest request
      ,HttpServletResponse response) throws Exception{
    try {
      String data="";
      data=logic.testOraConnect(request.getParameterMap());
     data="{test:'"+data+"'}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,  ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  public String testSqlConnect(HttpServletRequest request
      ,HttpServletResponse response) throws Exception{
    try {
      String data="";
      data=logic.testSqlConnect(request.getParameterMap());
     data="{test:'"+data+"'}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,  ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String testMysqlConnect(HttpServletRequest request
      ,HttpServletResponse response) throws Exception{
    try {
      String data="";
      data=logic.testMysqlConnect(request.getParameterMap());
     data="{test:'"+data+"'}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,  ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  
  public String saveOraConnect(HttpServletRequest request
      ,HttpServletResponse response) throws Exception{
    try {
      logic.saveOraConnect(request.getParameterMap());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);

    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,  ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  public String saveSqlConnect(HttpServletRequest request
      ,HttpServletResponse response) throws Exception{
    try {

      logic.saveSqlConnect(request.getParameterMap());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,  ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String saveMysqlConnect(HttpServletRequest request
      ,HttpServletResponse response) throws Exception{
    try {

     logic.saveMysqlConnect(request.getParameterMap());

      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,  ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
}

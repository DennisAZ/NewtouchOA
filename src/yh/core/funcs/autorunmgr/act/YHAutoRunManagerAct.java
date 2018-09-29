package yh.core.funcs.autorunmgr.act;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.autorun.YHAutoRunConfig;
import yh.core.autorun.YHAutoRunThread;
import yh.core.funcs.autorunmgr.logic.YHAutoRunManager;
import yh.core.global.YHActionKeys;
import yh.core.global.YHConst;
import yh.core.util.form.YHFOM;

/**
 * 后台服务进程
 * @author tulaike
 *
 */
public class YHAutoRunManagerAct {
  /**
   * 取得所有后台服务的配置
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getAutoRunCfgs(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      YHAutoRunManager arm  =  new YHAutoRunManager();
      String data  = arm.getAutoRunCfgList2Json().toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      ex.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 根据名称取得后台服务的配置
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getAutoRunCfg(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      String cfgname = request.getParameter("cfgname");
      YHAutoRunManager arm  =  new YHAutoRunManager();
      String data  = "";
      data = YHFOM.toJson(arm.getAutoRunCfgByName(cfgname)).toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 根据名称取得后台服务的配置
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteAutoRunCfg(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      String cfgname = request.getParameter("cfgname");
      YHAutoRunManager arm  =  new YHAutoRunManager();
      arm.deletePropertiesByName(cfgname);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除后台服务成功!");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 根据名称修改一个配置
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateAutoRunCfg(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      String cfgname = request.getParameter("cfgname");
      YHAutoRunManager arm  =  new YHAutoRunManager();
      YHAutoRunConfig arc = (YHAutoRunConfig) YHFOM.build(request.getParameterMap(), YHAutoRunConfig.class, null);
      arm.updatePropertiesByName(cfgname, arc);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改后台服务成功!");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 怎加一个后台服务配置
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addAutoRunCfg(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      YHAutoRunManager arm  =  new YHAutoRunManager();
      YHAutoRunConfig arc = (YHAutoRunConfig) YHFOM.build(request.getParameterMap(), YHAutoRunConfig.class, null);
      String cfgname = "autoRunTask" + arm.getAutoRunName();
      arm.updatePropertiesByName(cfgname, arc);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "注册服务添加成功!");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 得到主线程的服务
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getMainThreadStatus(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      YHAutoRunManager arm  =  new YHAutoRunManager();
      String data = arm.getMainThreadStatus();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 执行主线程
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String execMainThread(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      String type = request.getParameter("type");
      YHAutoRunManager arm  =  new YHAutoRunManager();
      arm.execMainThreadStatus(Integer.parseInt(type));
      String msrg = "服务启动成功!";
      if("1".equals(type)){
        msrg = "服务停止成功!";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, msrg);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 执行子线程
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String execSubThread(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      String id = request.getParameter("subid");
      YHAutoRunManager arm  =  new YHAutoRunManager();
      int flag = arm.execSubThreadStatus(id);
      String msrg = "";
      if(flag == 0){
        msrg = "后台子服务执行成功!";
      }else if(flag == 2){
        msrg = "该服务正在运行!";
      }else if(flag == 1){
        msrg = "没有找到该服务!";
      }else if(flag == 4){
        msrg = "主服务已停止,请启动主服务后再执行此操作!";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, msrg);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
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
  public String checkClassIsInvalidity(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      String cls = request.getParameter("cls");
      YHAutoRunManager arm  =  new YHAutoRunManager();
      int flag = arm.checkClassIsInvalidity(cls);
      String data = "{isIncalidity:\"" + flag + "\"}";
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

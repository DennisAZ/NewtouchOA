package yh.setup.erp.act;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.global.YHActionKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.setup.erp.logic.YHERPSetupLogic;
import yh.setup.util.YHERPSetupUitl;

public class YHERPSetupAct {
  /**
   * YHEA安装包
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String setup(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      String erpcontextPath = "yherp";
      String setupContentPath = "setup";
      YHERPSetupLogic easu = new YHERPSetupLogic(request.getParameterMap(), "sqlserver", YHSysProps.getProp(YHSysPropKeys.JSP_ROOT_DIR),erpcontextPath);
      easu.createSysDb(erpcontextPath);
      String installPath = YHSysProps.getRootPath();
      YHERPSetupUitl.updateInstallInfo(installPath, setupContentPath, "erp", "1");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,  ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}

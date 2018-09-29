package yh.setup.fis.act;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.global.YHActionKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.setup.fis.logic.YHFISSetupLogic;
import yh.setup.util.YHERPSetupUitl;

public class YHFisSetupAct {
  /**
   * YHFis安装包
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

      YHFISSetupLogic fsl = new YHFISSetupLogic(request.getParameterMap(), "sqlserver", YHSysProps.getProp(YHSysPropKeys.JSP_ROOT_DIR));
      fsl.createFisDb(erpcontextPath, "TDSYS"); //创建账套库
      fsl.insertErpMeun2YH(erpcontextPath); //注册YH菜单
      String installPath = YHSysProps.getRootPath();
      YHERPSetupUitl.updateInstallInfo(installPath, setupContentPath, "fis", "1");
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

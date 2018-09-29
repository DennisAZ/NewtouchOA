package yh.setup.act;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.global.YHActionKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.setup.util.YHERPSetupUitl;

public class YHSetupUtilAct {
  /**
   * 查找还没有安装的系统
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String findNotStalledSys(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      String erpcontextPath = "yherp";
      String setupContentPath = "setup";
      String installPath = YHSysProps.getRootPath();
      YHERPSetupUitl easu = new YHERPSetupUitl();
      String data = easu.getErpInstallInfo(installPath, setupContentPath);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,  ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 测试数据库连接
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String testDbConn(HttpServletRequest request
      ,HttpServletResponse response) throws Exception{
    try {
      boolean isActive = YHERPSetupUitl.testDbConn(request.getParameterMap(), "sqlserver");
      String testRt = isActive ? "1" : "0";
      String data = "{testRt:\"" + testRt + "\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,  ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}

package yh.core.funcs.system.act.filters;

import java.io.File;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.act.common.YHLoginErrorConst;
import yh.core.funcs.system.act.imp.YHLoginValidator;
import yh.core.funcs.system.logic.YHSystemLogic;
import yh.core.global.YHRegistProps;
import yh.core.global.YHSysProps;
import yh.core.util.auth.YHRegistUtility;

public class YHSoftwareExpiredValidator implements YHLoginValidator {

  public void addSysLog(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    // TODO Auto-generated method stub
    
  }

  public int getValidatorCode() {
    // TODO Auto-generated method stub
    return YHLoginErrorConst.SOFTWARE_EXPIRED_CODE;
  }

  public String getValidatorMsg() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getValidatorType() {
    // TODO Auto-generated method stub
    return YHLoginErrorConst.SOFTWARE_EXPIRED;
  }

  public boolean isValid(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    try {
      if (YHRegistProps.isEmpty()) {
        List<String> registInfo = YHSystemLogic.loadRegistRequires(conn);
        if (registInfo.size() > 1) {
          String webInfoPath = YHSysProps.getWebInfPath();
          Map registMap = YHRegistUtility.loadRegistFromPath(webInfoPath + File.separator + "config" + File.separator + "regist", webInfoPath.substring(0, 3), registInfo.get(0), registInfo.get(1));
          if (registMap != null && registMap.size() > 0) {
            YHRegistProps.setProps(registMap);
          }
        }
      }
    }catch(Exception ex) {
      log.debug(ex);
    }
    
    //如果软件过期提示用户注册
    if (!YHRegistUtility.hasRegisted() && YHRegistUtility.isExpired()) {
      return false;
    }
    else {
      return true;
    }
  }

}

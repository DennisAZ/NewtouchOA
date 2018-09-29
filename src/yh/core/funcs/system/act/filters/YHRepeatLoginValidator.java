package yh.core.funcs.system.act.filters;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import yh.core.data.YHProps;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.act.common.YHLoginErrorConst;
import yh.core.funcs.system.act.imp.YHLoginValidator;
import yh.core.global.YHSysProps;

public class YHRepeatLoginValidator implements YHLoginValidator {

  public void addSysLog(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    // TODO Auto-generated method stub

  }

  public int getValidatorCode() {
    // TODO Auto-generated method stub
    return YHLoginErrorConst.REPEAT_LOGIN_ERROR_CODE;
  }

  public String getValidatorMsg() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getValidatorType() {
    // TODO Auto-generated method stub
    return YHLoginErrorConst.REPEAT_LOGIN_ERROR;
  }

  public boolean isValid(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    // TODO Auto-generated method stub
    String mulLogin = YHSysProps.getString("$ONE_USER_MUL_LOGIN");
    
    if (mulLogin == null) {
      mulLogin = "1";
    }
    
    if ("0".equals(mulLogin)) {
      return !this.logic.isLogin(conn, person.getSeqId());
    }
    return true;
  }

}

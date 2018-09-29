package yh.core.funcs.system.act.filters;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.act.common.YHLoginErrorConst;
import yh.core.funcs.system.act.imp.YHLoginValidator;

public class YHForbidLoginValidator implements YHLoginValidator{

 
  public boolean isValid(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    return forbidLogin(person);
  }

  /**
   * 判断是否禁止登录
   * @param person
   * @return
   */
  private boolean forbidLogin(YHPerson person){
    String notLogin = person.getNotLogin();
    if (notLogin == null) {
      notLogin = "0";
    }
    return !"1".equals(notLogin.trim());
  }
 
  public int getValidatorCode() {
    return YHLoginErrorConst.LOGIN_FORBID_LOGIN_CODE;
  }

  
  public String getValidatorType() {
    return YHLoginErrorConst.LOGIN_FORBID_LOGIN;
  }

  
  public void addSysLog(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    
  }

  
  public String getValidatorMsg() {
    return null;
  }
}

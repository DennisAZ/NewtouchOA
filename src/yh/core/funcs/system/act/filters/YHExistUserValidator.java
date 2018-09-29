package yh.core.funcs.system.act.filters;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.act.common.YHLoginErrorConst;
import yh.core.funcs.system.act.imp.YHLoginValidator;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHLogConst;

public class YHExistUserValidator  implements YHLoginValidator {


  public boolean isValid(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    return person != null;
  }



 
  public String getValidatorType() {
    return YHLoginErrorConst.LOGIN_NOTEXIST_USER;
  }

 
  public void addSysLog(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    //系统日志-用户名错误
    YHSysLogLogic.addSysLog(conn, YHLogConst.INVALID_USER, "用户名错误",
        0,request.getRemoteAddr());
    
  }



  public int getValidatorCode() {
    return YHLoginErrorConst.LOGIN_NOTEXIST_USER_CODE;
  }




  public String getValidatorMsg() {
    return null;
  }

}

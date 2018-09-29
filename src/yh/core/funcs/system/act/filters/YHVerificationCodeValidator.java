package yh.core.funcs.system.act.filters;

import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHSecureKey;
import yh.core.funcs.person.logic.YHSecureCardLogic;
import yh.core.funcs.system.act.common.YHLoginErrorConst;
import yh.core.funcs.system.act.common.YHValidatorHelper;
import yh.core.funcs.system.act.imp.YHLoginValidator;
import yh.core.funcs.system.security.data.YHSecurity;
import yh.core.funcs.system.security.logic.YHSecurityLogic;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHLogConst;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHPassEncrypt;
import yh.core.util.db.YHORM;
import seamoonotp.seamoonapi;

public class YHVerificationCodeValidator implements YHLoginValidator {
  String isSecureCard = "0";
  String returnValue = "0";

  public boolean isValid(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    String verificationCode = request.getParameter("verificationCode");
    
    Map<String,String> map = logic.getSysPara(conn);
    
    String vc = map.get("VERIFICATION_CODE");
    
    if (!"1".equals(vc)){
      return true;
    }
    HttpSession session = request.getSession(false);
    String value = YHUtility.null2Empty((String)session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY));
    return value.equalsIgnoreCase(verificationCode);
  }

  
  public int getValidatorCode() {
    return YHLoginErrorConst.VERIFICATION_CODE_CODE;
  }

  
  public String getValidatorType() {
    return YHLoginErrorConst.VERIFICATION_CODE_ERROR;
  }

  
  public void addSysLog(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    //系统日志-登陆密码错误
    YHSysLogLogic.addSysLog(conn, YHLogConst.LOGIN_PASSWORD_ERROR, "验证码错误",
      person.getSeqId(), request.getRemoteAddr());
  }


  
  public String getValidatorMsg() {
    // TODO Auto-generated method stub
    return null;
  }

}

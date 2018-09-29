package yh.core.funcs.system.act.filters;

import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import yh.core.util.auth.YHPassEncrypt;
import yh.core.util.db.YHORM;
import seamoonotp.seamoonapi;

public class YHPasswordValidator implements YHLoginValidator {
  String isSecureCard = "0";
  String returnValue = "0";

  public boolean isValid(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    String pwd = "";
    
    //是否开启动态密码卡验证
    YHSecurityLogic orgLogic = new YHSecurityLogic();
    YHSecurity security  = orgLogic.getSecritySecureKey(conn);
    
    //该用户是否绑定动态密码卡
    YHSecureCardLogic secureCardLogic = new YHSecureCardLogic();
    YHSecureKey secureKey = secureCardLogic.getKeyInfo(conn , person);
    
    if("1".equals(security.getParaValue()) && secureKey != null){
      isSecureCard = security.getParaValue();
      if(request.getParameter("pwd").length() < 6){
        return false;
      }
      pwd = request.getParameter("pwd").substring(0, request.getParameter("pwd").length() - 6);
    }
    else{
      pwd = request.getParameter("pwd");
    }
    
//    if (pwd == null) {
//      pwd = null;
//    }
     
    if (person.getPassword() == null){
      person.setPassword("");
    }
    
    if (person != null && YHPassEncrypt.isValidPas(pwd, person.getPassword().trim())){
      if("1".equals(security.getParaValue()) && secureKey != null){
        
        seamoonapi sc = new seamoonapi();
        String newSninfo = sc.checkpassword(secureKey.getKeyInfo(), request.getParameter("pwd").substring(request.getParameter("pwd").length()-6 , request.getParameter("pwd").length()));
        returnValue = newSninfo;
        if(newSninfo.length() > 3){
          secureKey.setKeyInfo(newSninfo);
          YHORM orm = new YHORM();
          orm.updateSingle(conn, secureKey);
          return true;
        }
        else
          return false;
      }
      else
        return true;
    }
    else {
      return false;
    }
  }

  
  public int getValidatorCode() {
    // TODO Auto-generated method stub
    if("1".equals(isSecureCard)){
      if("-1".equals(returnValue)){
        return YHLoginErrorConst.LOGIN_PASSWORD_ERROR_CODE_SECURE_CARD_1;
      }
      else if("-2".equals(returnValue)){
        return YHLoginErrorConst.LOGIN_PASSWORD_ERROR_CODE_SECURE_CARD_2;
      }
      else if("0".equals(returnValue)){
        return YHLoginErrorConst.LOGIN_PASSWORD_ERROR_CODE_SECURE_CARD_3;
      }
    }
    return YHLoginErrorConst.LOGIN_PASSWORD_ERROR_CODE;
  }

  
  public String getValidatorType() {
    // TODO Auto-generated method stub
    if("1".equals(isSecureCard)){
      if("-1".equals(returnValue)){
        return YHLoginErrorConst.LOGIN_PASSWORD_ERROR_SECURE_CARD_1;
      }
      else if("-2".equals(returnValue)){
        return YHLoginErrorConst.LOGIN_PASSWORD_ERROR_SECURE_CARD_2;
      }
      else if("0".equals(returnValue)){
        return YHLoginErrorConst.LOGIN_PASSWORD_ERROR_SECURE_CARD_3;
      }
    }
    return YHLoginErrorConst.LOGIN_PASSWORD_ERROR;
  }

  
  public void addSysLog(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    //系统日志-登陆密码错误
    YHSysLogLogic.addSysLog(conn, YHLogConst.LOGIN_PASSWORD_ERROR, "登录密码错误",
      person.getSeqId(), request.getRemoteAddr());
  }


  
  public String getValidatorMsg() {
    // TODO Auto-generated method stub
    return null;
  }

}

package yh.core.funcs.system.act.filters;

import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.act.common.YHLoginErrorConst;
import yh.core.funcs.system.act.common.YHValidatorHelper;
import yh.core.funcs.system.act.imp.YHLoginValidator;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHLogConst;
import yh.core.util.auth.YHPassEncrypt;

public class YHRetryLoginValidator implements YHLoginValidator {

  private int times;
  private int minutes;
  
  public boolean isValid(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    
    Map<String,String> map = logic.getSysPara(conn);
    
    String retry = map.get("SEC_RETRY_BAN");
    
    //当不限制错误登陆次数时,返回验证成功
    if (!"1".equals(retry)){
      return true;
    }
    
    try {
      this.times = Integer.parseInt(map.get("SEC_RETRY_TIMES"));
      this.minutes = Integer.parseInt(map.get("SEC_BAN_TIME"));
    } catch (NumberFormatException e) {
      //默认设置10分钟3次登陆错误      this.times = 3;
      this.minutes = 10;
    }
    
    if (logic.retryLogin(conn, this.times, this.minutes, person.getSeqId(), request.getRemoteAddr())){
      return true;
    }
    else{
      return false;
    }
  }
  
  public int getValidatorCode() {
    // TODO Auto-generated method stub
    return YHLoginErrorConst.LOGIN_RETRY_ERROR_CODE;
  }

  
  public String getValidatorType() {
    // TODO Auto-generated method stub
    return YHLoginErrorConst.LOGIN_RETRY_ERROR;
  }

  
  public void addSysLog(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    
  }

/**
 * 返回验证的具体信息
 */
  
  public String getValidatorMsg() {
    // TODO Auto-generated method stub
    return "{\"times\":" + times + ",\"minutes\":" + minutes + "}";
  }

}

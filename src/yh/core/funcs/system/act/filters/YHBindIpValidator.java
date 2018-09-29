package yh.core.funcs.system.act.filters;

import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.act.common.YHLoginErrorConst;
import yh.core.funcs.system.act.common.YHValidatorHelper;
import yh.core.funcs.system.act.imp.YHLoginValidator;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHLogConst;

public class YHBindIpValidator implements YHLoginValidator {

  /**
   * 绑定ip
   * @param person
   * @param ip
   * @return
   */
  private boolean bindIp(YHPerson person, String ip){
    String bindIp = person.getBindIp();
    if (bindIp != null && !"".equals(bindIp.trim())){
      
      boolean result = false;
      
      for (String s: bindIp.split(",")){
        
        if (s.contains("-")){
          String[] segment = s.split("-");
          if (segment.length == 2) {
            result = YHValidatorHelper.betweenIP(ip, s.split("-")[0], s.split("-")[1]);
          }
        }
        
        else{
          result = s.equals(ip);
        }
        
        if (result){
          break;
        }
      }
      
      return !result;
    }
    else{
      return false;
    }
  }


  public boolean isValid(HttpServletRequest request, YHPerson person,
      Connection conn) {
    return !this.bindIp(person, request.getRemoteAddr());
  }


  public String getValidatorType() {
    return YHLoginErrorConst.LOGIN_BIND_IP;
  }


  public int getValidatorCode() {
    return YHLoginErrorConst.LOGIN_BIND_IP_CODE;
  }

 
  public void addSysLog(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    //系统日志-非法ip登陆
    YHSysLogLogic.addSysLog(conn, YHLogConst.ILLEGAL_IP_LOGIN, "非法ip登录", 
        person.getSeqId(),request.getRemoteAddr());
  }


  public String getValidatorMsg() {
    return null;
  }
}

package yh.core.funcs.system.act.filters;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.accesscontrol.data.YHIpRule;
import yh.core.funcs.system.act.common.YHLoginErrorConst;
import yh.core.funcs.system.act.common.YHValidatorHelper;
import yh.core.funcs.system.act.imp.YHLoginValidator;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHLogConst;

public class YHIpRuleValidator implements YHLoginValidator{

  private boolean isIpRuleLimit(Connection conn, YHPerson person,String ip) throws Exception{
    
    Map<String,String> map = logic.getSysPara(conn);
    String unlimted = map.get("IP_UNLIMITED_USER");
    unlimted = unlimted == null ? "" : "," + unlimted + ",";
    
    String seqId = "," + person.getSeqId() + ",";
    
    if (unlimted.contains(seqId)){
      return false;
    }
    else{
      List<YHIpRule> list  = logic.getIpRule(conn);
      if (list.size() == 0){
        return false;
      }
      else{
        for(YHIpRule ir : list){
          if (YHValidatorHelper.betweenIP(ip, ir.getBeginIp(), ir.getEndIp())){
            return false;
          }
        }
        return true;
      }
    }
  }

  
  public boolean isValid(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    
    return !this.isIpRuleLimit(conn, person, request.getRemoteAddr());
  }

 
  public int getValidatorCode() {
    // TODO Auto-generated method stub
    return YHLoginErrorConst.LOGIN_IP_RULE_LIMIT_CODE;
  }

 
  public String getValidatorType() {
    // TODO Auto-generated method stub
    return YHLoginErrorConst.LOGIN_IP_RULE_LIMIT;
  }

  
  public void addSysLog(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    //系统日志-非法ip登陆
    YHSysLogLogic.addSysLog(conn, YHLogConst.ILLEGAL_IP_LOGIN, "非法ip登录", 
        person.getSeqId(),request.getRemoteAddr());
  }


  
  public String getValidatorMsg() {
    // TODO Auto-generated method stub
    return null;
  } 
}

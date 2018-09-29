package yh.core.funcs.system.act.filters;

import java.sql.Connection;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.act.common.YHLoginErrorConst;
import yh.core.funcs.system.act.imp.YHLoginValidator;

public class YHPwExpiredValidator implements YHLoginValidator {

  
  public boolean isValid(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    // TODO Auto-generated method stub
    return !isPwExpired(conn, person);
  }
  
  /**
   * 判断密码是否过期
   * @param map
   * @param person
   * @return
   * @throws Exception 
   */
  private boolean isPwExpired(Connection conn, YHPerson person) throws Exception{
    Map<String,String> map = logic.getSysPara(conn);
    
    String flag = map.get("SEC_PASS_FLAG").trim();
    
    if ("1".equals(flag)){
      Date date = new Date();
      
      Date passDate = person.getLastPassTime();

      if (passDate == null){
        return true;
      }
      
      long seconds = date.getTime() - passDate.getTime();
      
      long days = seconds / (3600000 * 24);
      
      int passDays = 0;
      
      try {
        passDays = Integer.parseInt(map.get("SEC_PASS_TIME").trim());
      }catch(NumberFormatException e){
        passDays = 0;
      }
      
      return days >= passDays;
    }
    else{
      return false;
    }
    
  }

  
  public int getValidatorCode() {
    // TODO Auto-generated method stub
    return YHLoginErrorConst.LOGIN_PW_EXPIRED_CODE;
  }

  
  public String getValidatorType() {
    // TODO Auto-generated method stub
    return YHLoginErrorConst.LOGIN_PW_EXPIRED;
  }

  
  public void addSysLog(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    // TODO Auto-generated method stub
    
  }

  
  public String getValidatorMsg() {
    // TODO Auto-generated method stub
    return null;
  }
}

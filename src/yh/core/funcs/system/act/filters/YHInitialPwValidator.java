package yh.core.funcs.system.act.filters;

import java.sql.Connection;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.act.common.YHLoginErrorConst;
import yh.core.funcs.system.act.imp.YHLoginValidator;
import yh.core.util.auth.YHPassEncrypt;
public class YHInitialPwValidator implements YHLoginValidator {

 
  public int getValidatorCode() {
    return YHLoginErrorConst.LOGIN_INITIAL_PW_CODE;
  }

  
  public String getValidatorType() {
    return YHLoginErrorConst.LOGIN_INITIAL_PW;
  }
  public boolean isValid(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    return !isInitialPw(conn, person);
  }
  
  /**
   * 判断是否为初始密码
   * @param person
   * @return
   * @throws Exception 
   */
  private boolean isInitialPw(Connection conn, YHPerson person) throws Exception{
    
    Map<String,String> map = logic.getSysPara(conn);
    String flag = map.get("SEC_INIT_PASS");
    
    if ("1".equals(flag)){
      return (person.getPassword() == null || YHPassEncrypt.isValidPas("", person.getPassword().trim()));
    }
    else{
      return false;
    }
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

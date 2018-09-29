package yh.core.funcs.system.act.filters;

import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.act.common.YHLoginErrorConst;
import yh.core.funcs.system.act.imp.YHLoginValidator;
import yh.core.global.YHActionKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHUsbKey;

public class YHUsbkeyValidator implements YHLoginValidator {

  private String msg;
  
  public void addSysLog(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    // TODO Auto-generated method stub

  }

  
  public int getValidatorCode() {
    // TODO Auto-generated method stub
    return YHLoginErrorConst.LOGIN_USBKEY_ERROR_CODE;
  }
  
  public String getValidatorType() {
    // TODO Auto-generated method stub
    return YHLoginErrorConst.LOGIN_USBKEY_ERROR;
  }
  
  public boolean isValid(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception {
    // TODO Auto-generated method stub
    return isUsbkey(request, person, conn);
  }
  
  private boolean isUsbkey(HttpServletRequest request, YHPerson person,
      Connection conn) throws Exception{
    
    //Map<String,String> map = logic.getSysPara(conn);
    //String secUserName = map.get("SEC_KEY_USER");
    
    String keyUser = YHUtility.null2Empty(request.getParameter("KEY_USER"));
    int randomNum = 123456; 
    Integer randomInt = (Integer)request.getSession().getAttribute("RANDOM_NUMBER");
    if (randomInt != null) {
      randomNum = randomInt;
    }
    //tVHbkPWW57Hw.
    //使用UsbKey 登录
    String useingKey = person.getUseingKey();
    if (useingKey == null) {
      useingKey = "";
    }
    else {
      useingKey = useingKey.trim();
    }
    String keySn = YHUtility.null2Empty(request.getParameter("KEY_SN"));
    String keyDigest = YHUtility.null2Empty(request.getParameter("KEY_DIGEST"));
    String userKey = "";
    String userKeyStr = "";
    
    if (!"".equals(keySn) && !"".equals(keyUser) && !"".equals(keyDigest) && "1".equals(useingKey)) {
      userKey = keySn;
      userKeyStr = userKey.substring(0, 8).toUpperCase();
      boolean isValid = YHUsbKey.digestComp(keyDigest, String.valueOf(randomNum), YHUsbKey.md5Hex(person.getPassword()));
      
      if (!userKeyStr.equals(YHConst.KEY_TD_SIGN) || !keySn.equals(userKey) || !isValid) {
        return false;
      }
      else{
        return true;
      }
    }
    
    return false;
  }


  
  public String getValidatorMsg() {
    // TODO Auto-generated method stub
    return null;
  }

}

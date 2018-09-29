package yh.core.funcs.system.act.adapter;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.act.imp.YHLoginValidator;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHLoginAdapter {
  
  private HttpServletRequest request;
  private Connection dbConn;
  private YHPerson person;
  
  public YHLoginAdapter(HttpServletRequest request, YHPerson person) throws Exception{
    this.request = request;
    
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request
    .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    
    this.dbConn = requestDbConn.getSysDbConn();
    
    this.person = person;
  }
  
  public boolean isValid(YHLoginValidator lv) throws Exception{
    return lv.isValid(this.request, this.person, this.dbConn);
  }
  
  public boolean validate(YHLoginValidator lv) throws Exception{
    if (lv.isValid(this.request, this.person, this.dbConn)){
      return true;
    }
    else{
      
      //写系统日志
      lv.addSysLog(this.request, this.person, this.dbConn);
      String msg = lv.getValidatorMsg();
      if (msg == null || "".equals(msg.trim())){
        msg = "{}";
      }
      
      //返回到页面上的信息
      String retData = "{\"code\":" + lv.getValidatorCode() + ",\"msg\":" + msg + "}";
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, lv.getValidatorType());
      request.setAttribute(YHActionKeys.RET_DATA, retData);
      
      return false;
    }
  }
}

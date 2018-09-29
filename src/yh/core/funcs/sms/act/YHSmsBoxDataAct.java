package yh.core.funcs.sms.act;

import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.logic.YHSmsLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

/**
 * 组织各个短信箱的数据
 * @author Think
 *
 */
public class YHSmsBoxDataAct {
  private static Logger log = Logger.getLogger(YHSmsBoxDataAct.class);
  /**
   * 列出所有已发送的短信
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listAllSms(HttpServletRequest request,
      HttpServletResponse response) throws Exception { 
    
    String toIdStr = request.getParameter("toId");
    StringBuffer data = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      YHSmsLogic sl = new YHSmsLogic();
      
      PrintWriter pw = response.getWriter();
      pw.println(data.toString());
      pw.flush();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;//sentsms
  }
  
}

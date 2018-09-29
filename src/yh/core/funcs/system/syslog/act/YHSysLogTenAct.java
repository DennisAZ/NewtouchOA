package yh.core.funcs.system.syslog.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.syslog.logic.YHSysLogSearchLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHSysLogTenAct {
  private static Logger log = Logger.getLogger(YHSysLogTenAct.class);
  
  public String getMySysLog(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
        String str = "";
        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHPerson personLogin = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        YHSysLogSearchLogic syslog =new YHSysLogSearchLogic();
        str = syslog.getMySysTenLog(dbConn, 233);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"成功取出日志");
        request.setAttribute(YHActionKeys.RET_DATA, str);
    }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}

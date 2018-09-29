package yh.core.funcs.system.syslog.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHSysLogAct {
  private static Logger log = Logger.getLogger(YHSysLogAct.class);
  public String addLog(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
        String type = request.getParameter("type");
        String remark = request.getParameter("remark");
        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        YHSysLogLogic.addSysLog(dbConn, type, remark, person.getSeqId(), request.getRemoteAddr());
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"日志添加成功：" + person.getUserName() + " 执行 ：" + remark);
    }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}

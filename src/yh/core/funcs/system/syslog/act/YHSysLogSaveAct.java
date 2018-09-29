package yh.core.funcs.system.syslog.act;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.syslog.logic.YHSysLogSaveLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHSysLogSaveAct {
  private static Logger log = Logger.getLogger(YHSysLogSaveAct.class);
  
  public String SaveLog(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        //YHPerson personLogin = (YHPerson)request.getSession().getAttribute("233");
        YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        YHSysLogSaveLogic save = new YHSysLogSaveLogic();
        String OkandSory="";
        if(person.isAdmin()){
            OkandSory =  save.getSaveLog(dbConn, person);
        }
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute("data", OkandSory);
        request.setAttribute(YHActionKeys.RET_DATA,"'"+ OkandSory+"'");
    }catch(SQLException ex) {
        String no="同一天不能结转两次";
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG,no);
        return "/core/inc/rtjson.jsp";
    }
    catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getsysradio(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        YHSysLogSaveLogic save = new YHSysLogSaveLogic();
        //List list =  save.getOkSaveLog(dbConn, person);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        //request.setAttribute("data", list);
    }
    catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
    }
    return "/core/funcs/system/syslog/manager.jsp";
  }
}

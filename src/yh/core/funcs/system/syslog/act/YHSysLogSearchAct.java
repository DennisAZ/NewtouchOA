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

public class YHSysLogSearchAct {
  private static Logger log = Logger.getLogger(YHSysLogSearchAct.class);
  /**
   * 以下方法统计分别是:
   *str2 今年访问量 方法，str3本月访问量，str4今日访问量，str5平均每日访问量
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getMySysLog(HttpServletRequest request,
   HttpServletResponse response) throws Exception {
   Connection dbConn = null;
   try {
       YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       dbConn = requestDbConn.getSysDbConn();
       YHPerson personLogin = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
       YHSysLogSearchLogic syslog =new YHSysLogSearchLogic();
       String  str = syslog.getMySysLog(dbConn,233);
       String str1= syslog.getMySysLogAll(dbConn, 233);   
       String str2 =  syslog.getMySysYearLog(dbConn, 233);//今年访问量 方法
       String str3 =  syslog.getMySysMonthLog(dbConn, 233);//本月访问量
       String str4 = syslog.getMySysDayLog(dbConn, 233); //今日访问量
       String str5 = syslog.getMySysAveLog(dbConn, 233);//平均每日访问量
      /*
       String str1=""
       StringBuffer sb = new StringBuffer("{str1:");
       sb.append(str);
       sb.append(",str2:");
       //System.out.println(str);
       if(str != null){
       request.setAttribute("countDay",str);
          return "/core/funcs/system/syslog/logsituation.jsp";
        }
       request.setAttribute(YHActionKeys.RET_MSRG,"日志添加成功：" + person.getUserName() + " 执行 ：" + remark);
       */
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"成功取出日志");
        request.setAttribute(YHActionKeys.RET_DATA, "{str:" + str + ",str1:" + str1 +",str2:"+ str2 +",str3:"+str3+",str4:"+str4+",str5:"+str5+"}");
    }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}

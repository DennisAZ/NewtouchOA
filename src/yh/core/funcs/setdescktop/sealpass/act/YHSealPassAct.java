package yh.core.funcs.setdescktop.sealpass.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.util.form.YHFOM;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.setdescktop.mypriv.logic.YHMyprivLogic;
import yh.core.funcs.setdescktop.pass.logic.YHPassLogic;
import yh.core.funcs.setdescktop.sealpass.logic.YHSealPassLogic;
import yh.core.funcs.setdescktop.setports.data.YHPort;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.system.sealmanage.data.YHSeal;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.load.YHPageLoader;
import yh.core.menu.data.YHSysMenu;

public class YHSealPassAct {
  private YHSealPassLogic logic = new YHSealPassLogic();
  public final static String SEAL_LOG_TYPE = "makeseal";
  
  /**
   * 获取印章信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getSealInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception{

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      List<YHSeal> list = this.logic.getSealInfo(dbConn, user.getSeqId());
      
      
      StringBuffer sb = new StringBuffer("{\"total\":");
      sb.append(list.size());
      sb.append(",\"records\":[");
      
      for(YHSeal s : list){
        String uerStr = this.logic.getSealUserStr(dbConn, "(" + s.getUserStr() + ")");
        s.setUserStr(uerStr == null ? "" : uerStr);
        
        sb.append(YHFOM.toJson(s));
        sb.append(",");
      }
        
      if(list.size()>0){
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]}");
      PrintWriter pw = response.getWriter();
      pw.println(sb.toString().trim());
      pw.flush();
      return "";
      
    }catch(Exception ex) {
      throw ex;
    }
  }
  
  /**
   * 更新印章信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateSealData(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    String data = request.getParameter("data");
    String sealId = request.getParameter("seqId");
    String pass = request.getParameter("pass");
    String sealName = request.getParameter("sealName");
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      if(sealId != null && !"".equals(sealId)){
        this.logic.updateSealData(dbConn, data, sealId);
        this.logic.updateSealLog(dbConn, sealId, YHSealPassAct.SEAL_LOG_TYPE, new YHSysLogLogic().getIpAddr(request), user.getSeqId());
        
        String userList = this.logic.getUserList(dbConn, sealId);
        
        for(String s : userList.split(",")){
          if(Integer.parseInt(s) == user.getSeqId())
            continue;
          YHSmsBack sb = new YHSmsBack();
          sb.setFromId(user.getSeqId());
          sb.setContent("印章" + sealName + "密码已经修改，新的密码为:" + pass);
          sb.setSendDate(new Date());
          sb.setSmsType("0");
          sb.setToId(s);
          YHSmsUtil.smsBack(dbConn,sb);
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
}
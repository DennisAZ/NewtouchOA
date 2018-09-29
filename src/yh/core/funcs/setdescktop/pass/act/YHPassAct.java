package yh.core.funcs.setdescktop.pass.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.util.YHUtility;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.setdescktop.pass.logic.YHPassLogic;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.auth.YHPassEncrypt;

public class YHPassAct {
  public final static String SYS_LOG_PASS = "14";
  private YHPassLogic logic = new YHPassLogic();
  
  /**
   * 获取系统参数
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getSysPara(HttpServletRequest request,
      HttpServletResponse response) throws Exception{

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      List<String> list = this.logic.getSysPara(dbConn);
      String time = this.logic.getLastPassTime(dbConn, user.getSeqId());
      StringBuffer sb = new StringBuffer("{");
      for(String s : list){
        sb.append(s);
        sb.append(",");
      }
      sb.append("LAST_PASS_TIME:\"");
      if (time == null) {
        time = "";
      }
      
      if (time.length() > 19) {
        time = time.substring(0, 19);
      }
      sb.append(time);
      //演示版禁止更改密码

      String isDemoStr = YHSysProps.getString("IS_ONLINE_EVAL");
      if (!YHUtility.isNullorEmpty(isDemoStr) && "1".equals(isDemoStr)) {
        sb.append("\",\"isDemo\": \"1");
      }
      sb.append("\"}");
      
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询属性");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取修改密码的历史记录(最后10条)
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getSysLog(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      List<Map> list = this.logic.getSysLog(dbConn, user.getSeqId(),YHPassAct.SYS_LOG_PASS);
      StringBuffer sb = new StringBuffer("{\"total\":");
      sb.append(list.size());
      sb.append(",\"records\":[");
      
      for(Map<String,String> map : list){
        
        String time = map.get("TIME");
        if (time != null) {
          time = time.substring(0, time.length() > 19 ? 19 : time.length());
        }
        map.put("TIME", time);
        
        sb.append(this.toJson(map));
        sb.append(",");
      }
        
      if(list.size()>0){
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]}");
      PrintWriter pw = response.getWriter();
      
      pw.println(sb.toString().trim());
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询属性");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    
    return "";
  }
  
  /**
   * 修改密码
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updatePassWord(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    String pw = request.getParameter("PASS1");
    String formPw = request.getParameter("PASS0");
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      boolean result = false;
      if(YHPassEncrypt.isValidPas(formPw, user.getPassword())){
        
        //加密
        pw = YHPassEncrypt.encryptPass(pw);
        result = this.logic.updatePassWord(dbConn, user.getSeqId(), pw);
      }
      
      if(result){
        user.setPassword(pw);
        yh.core.funcs.system.syslog.logic.YHSysLogLogic.addSysLog(
                  dbConn, YHPassAct.SYS_LOG_PASS, "用户修改密码", user.getSeqId(), YHSysLogLogic.getIpAddr(request));
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"成功设置密码");
      }
      else{
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG,"设置密码失败");
      }
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 检查原密码是否正确
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String checkPassWord(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String pw = request.getParameter("PASSWORD");
    try {
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      if(pw != null && !"".equals(pw)){
        if(!YHPassEncrypt.isValidPas(pw, user.getPassword())){
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
          request.setAttribute(YHActionKeys.RET_MSRG, "输入的原密码错误");
          return "/core/inc/rtjson.jsp";
        }
      }
      else{
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "");
        return "/core/inc/rtjson.jsp";
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
  
  private String toJson(Map<String,String> m) throws Exception{
    StringBuffer sb = new StringBuffer("{");
    for (Iterator<Entry<String,String>> it = m.entrySet().iterator(); it.hasNext();){
      Entry<String,String> e = it.next();
      sb.append(e.getKey());
      sb.append(":\"");
      sb.append(e.getValue());
      sb.append("\",");
    }
    if (sb.charAt(sb.length() - 1) == ','){
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("}");
    return sb.toString();
  }
}
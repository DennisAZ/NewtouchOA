package yh.core.funcs.seclog.act;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.seclog.logic.YHSecLogUtil;
import yh.core.funcs.seclog.logic.YHSeclogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.setting.data.YHHrCode;
import yh.subsys.oa.hr.setting.logic.YHHrCodeLogic;

public class YHSeclogAct {
  YHSeclogLogic logic = new YHSeclogLogic(); 
  
  /**
   * 通用列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getLogListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.getLogLogic(dbConn, request.getParameterMap(), person);
      PrintWriter pw = response.getWriter();
      //System.out.println(data);
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  
  
  /**
   * 通用列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String doExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      ArrayList<YHDbRecord> data = this.logic.doExportLogic(dbConn, request.getParameterMap(), person);
      String fileName = URLEncoder.encode("安全日志.xls","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      
      YHJExcelUtil.writeExc(response.getOutputStream(), data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  
  
  /**
   * 获取日志类型
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getSecLogType(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String type=request.getParameter("sec_type");
      String data = this.logic.getSecLogType(dbConn, type);
      data=YHUtility.null2Empty(data);
      data="{logName:'"+data+"'}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取日志类型
   * */
  public String selectChildCode(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String parentNo = request.getParameter("parentNo");
      if(YHUtility.isNullorEmpty(parentNo)){
        parentNo = "";
      }
      String seqId = request.getParameter("seqId");
      YHSeclogLogic codeLogic = new YHSeclogLogic();
      String data = "[";
      List<YHHrCode> codeList = new ArrayList<YHHrCode>();
      codeList = codeLogic.getChildCode(dbConn, parentNo);
      for (int i = 0; i < codeList.size(); i++) {
        YHHrCode code = codeList.get(i);
        data = data + YHFOM.toJson(code) + ",";
      }
      if(codeList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 日志归档 
   * 
   **/
  public String doArchive(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data="1";
      try{
        Statement stmt=null;
      //  ResultSet rs=null;
        String dbms=YHSysProps.getProp("db.jdbc.dbms");
        String dataStr=YHUtility.getCurDateTimeStr("yyyyMMdd");
        if(dbms.equals("mysql")){
          String sql=""
              +"CREATE TABLE `seclog"+dataStr+"` ("
              +"  `SEQ_ID` int(10) unsigned NOT NULL auto_increment,"
              +"  `USER_SEQ_ID` varchar(200) default NULL,"
              +"  `OP_TIME` datetime default NULL,"
              +"  `CLIENT_IP` varchar(20) default NULL,"
              +"  `OP_TYPE` varchar(10) default NULL,"
              +"  `OP_OBJECT` text,"
              +"  `OP_DESC` text,"
              +"  `user_name` varchar(200) default NULL,"
              +"  `op_result` varchar(45) default NULL,"
              +"  PRIMARY KEY  (`SEQ_ID`)"
              +") ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;";
          stmt=dbConn.createStatement();
          stmt.execute(sql);//新建表
          sql=" insert into seclog"+dataStr+"  select * from seclog  ";
          stmt.execute(sql);//归档
          sql=" truncate table seclog ";
          stmt.execute(sql);
        }
        
        // add  seclog
        try{
           
           YHSecLogUtil.log(dbConn, person,request.getRemoteAddr(), "220","执行日志归档","1", "归档日志");
        }catch(Exception ex){
          
        }
        
      }catch(Exception e){
        e.printStackTrace();
        // add  seclog
        try{
           
           YHSecLogUtil.log(dbConn, person,request.getRemoteAddr(), "220","执行日志归档","0", "归档日志");
        }catch(Exception ex){
          
        }
        data="0";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
}

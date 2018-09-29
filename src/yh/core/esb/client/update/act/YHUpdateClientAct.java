package yh.core.esb.client.update.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.util.db.YHORM;
import yh.core.data.YHRequestDbConn;
import yh.core.esb.client.data.YHEsbClientConfig;
import yh.core.esb.client.data.YHEsbConst;
import yh.core.esb.client.data.YHExtDept;
import yh.core.esb.client.logic.YHDeptTreeLogic;
import yh.core.esb.client.update.data.YHUpdateClientLog;
import yh.core.esb.client.update.logic.YHUpdateClientLogic;
import yh.core.esb.server.update.data.YHUpdateLogDetl;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;


public class YHUpdateClientAct{
	 public String getUpdateClientList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		    Connection dbConn = null;
		    try {
		      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		      dbConn = requestDbConn.getSysDbConn();
		      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		      YHUpdateClientLogic logic = new YHUpdateClientLogic();
		      String data = logic.getJsonLogic(dbConn, request.getParameterMap(), person, request);
		      PrintWriter pw = response.getWriter();
		      pw.println(data);
		      pw.flush();
		    } catch (Exception ex) {
		      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		      throw ex;
		    }
		    return null;
		  }
	 
	  //删除公告信息
	  public String deleteFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		    String seqIdStr = request.getParameter("seqId");
		    Connection dbConn = null;
		    try {
		      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		      dbConn = requestDbConn.getSysDbConn();
		      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);

		      YHUpdateClientLogic logic = new YHUpdateClientLogic();
		      String temp = logic.deleteFileLogic(dbConn, seqIdStr);
		      
		      //系统日志
		      YHSysLogLogic.addSysLog(dbConn, "60", "删除升级信息。"  ,person.getSeqId(), request.getRemoteAddr());
		      
		      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		    } catch (Exception e) {
		      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
		      throw e;
		    }
		    return "/core/inc/rtjson.jsp";
		  }
	  
	  public String updateClient(HttpServletRequest request, HttpServletResponse response)throws Exception{
		  Connection dbConn = null;
		  try{
			  YHORM orm= new YHORM();
			   int seqId = Integer.parseInt(request.getParameter("seqId"));
		      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		      dbConn = requestDbConn.getSysDbConn();
		      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		      String updateUser = person.getUserName();
		      String filePath=YHSysProps.getRootPath() + "\\update\\deploy";
		      YHUpdateClientLogic logic=new YHUpdateClientLogic();
			  YHEsbClientConfig config = YHEsbClientConfig.builder(request.getRealPath("/") + YHEsbConst.CONFIG_PATH) ;
	          YHDeptTreeLogic dtl = new YHDeptTreeLogic();
	          YHExtDept ed = dtl.getDeptByEsbUser(dbConn,(config.getUserId()));//根据发送部门，查询组织机构
	          String fromDept = "";
	          String fromDeptName = "";
	          if(ed != null){
	            fromDept = ed.getDeptId()+"";//发送部门GUID
	            fromDeptName = ed.getDeptName();//发送部门名称
	          }
		      boolean flag= logic.updateSystem(filePath);
		      if(flag==true){
		    	 logic.updateClientStatus(dbConn, seqId,updateUser);
		      }else{
		    	  return null;
		      }
		      YHUpdateClientLog clientLog=(YHUpdateClientLog) orm.loadObjSingle(dbConn, YHUpdateClientLog.class, seqId);
		      String status=logic.getStatus(dbConn, seqId);
		      if("2".equals(status)){         //返回系统升级成功的回执   
		    	  logic.backSuccessInfo(clientLog,request.getRealPath("/"),dbConn);
		      }
		  	}catch(Exception e){
		  		e.printStackTrace();
		  }
		  return "/core/inc/rtjson.jsp";
	  }
	  
	  public String getUser(HttpServletRequest request, HttpServletResponse response)throws Exception{
		    Connection dbConn = null;
		    try {
		      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		      dbConn = requestDbConn.getSysDbConn();
		      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		      return person.getUserName();
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
		    return null;
	  }
	  
	  public void restartSystem(HttpServletRequest request, HttpServletResponse response)throws Exception{
		  try{
			  Runtime.getRuntime().exec("shutdown -r -f -t 5");
		  }catch(Exception e){
			e.printStackTrace();  
		  }
	  }
}
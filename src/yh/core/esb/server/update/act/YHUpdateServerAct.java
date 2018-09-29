package yh.core.esb.server.update.act;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.esb.client.data.YHEsbClientConfig;
import yh.core.esb.client.data.YHEsbConst;
import yh.core.esb.client.data.YHExtDept;
import yh.core.esb.client.logic.YHDeptTreeLogic;
import yh.core.esb.client.service.YHWSCaller;
import yh.core.esb.frontend.YHEsbFrontend;
import yh.core.esb.server.act.YHRangeUploadAct;
import yh.core.esb.server.update.data.YHUpdateLogDetl;
import yh.core.esb.server.update.logic.YHUpdateServerLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.jtgwjh.notifyManage.logic.YHJhNotifyInfoLogic;
import yh.subsys.jtgwjh.task.data.YHJhTaskLog;


public class YHUpdateServerAct{
	  public YHWSCaller caller = new YHWSCaller();
	  String path = YHRangeUploadAct.UPLOAD_PATH + File.separator + "ESB-CACHE";
	  String recePath = "d:\\ESB-CACHE";
	  //上传文件
	  public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		  Connection dbConn = null;
		  try {
		      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		      dbConn = requestDbConn.getSysDbConn();
		      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			  YHFileUploadForm fileForm = new YHFileUploadForm();
		      fileForm.parseUploadRequest(request);
		      if(false == YHEsbFrontend.isOnline()){
		        if(YHEsbFrontend.login() != 0){
		          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		          request.setAttribute(YHActionKeys.RET_MSRG, "连接esb服务器失败！");
		        }
		      }
		      Iterator<String> iKeys = fileForm.iterateFileFields();
		      YHUpdateServerLogic logic = new YHUpdateServerLogic();
		      int logSeqId=logic.addServerLog(dbConn,user);
		      String savePath = "";
		      if (iKeys.hasNext()) {
		        String fieldName = iKeys.next();
		        String fileName ="ServerUpdate_" + YHGuid.getRawGuid()+"_"+fileForm.getFileName(fieldName);
		        savePath = path + File.separator + fileName;
		        File parentFile = new File(path);
		        if (!parentFile.exists()) {
		          parentFile.mkdir();
		        }
		        fileForm.saveFile(savePath);
		        YHEsbClientConfig config = YHEsbClientConfig.builder(request.getRealPath("/") + YHEsbConst.CONFIG_PATH) ;
		        caller.setWS_PATH(config.getWS_PATH());
		        String ret = caller.broadcast(savePath, config.getToken());
		        System.out.println(ret);
		        System.out.println(YHGuid.getGuid());
		       String str2= caller.send(savePath, "client", "JHupdate",config.getToken());
		        Map map1=YHFOM.json2Map(str2);
		        String code1="";
		        String guId1="";
	            if(map1 != null){
		               code1 = (String) map1.get("code");
		               guId1 = ((String)map1.get("guid"));
		            }
		        Map map = YHFOM.json2Map(ret);
		          YHDeptTreeLogic dtl = new YHDeptTreeLogic();
		          YHExtDept ed = dtl.getDeptByEsbUser(dbConn,(config.getUserId()));//根据发送部门，查询组织机构
		          String fromDept = "";
		          String fromDeptName = "";
		          if(ed != null){
		            fromDept = ed.getDeptId()+"";//发送部门GUID
		            fromDeptName = ed.getDeptName();//发送部门名称
		          }
		          
		          /*
		           * 获取所有接收单位，从组织机构上查询
		           */
		          String[] str = {"ESB_USER <> ''" ,"ESB_USER is not null"};
		          List<YHExtDept>  listDept = YHDeptTreeLogic.select(dbConn, str);
		          String sendDept =  "client";
		          if(!YHUtility.isNullorEmpty(sendDept)){
		            String[] sendDeptA = sendDept.split(",");
		            String code  = "";
		            String guId = "";
		            if(map != null){
		               code = (String) map.get("code");
		               guId = ((String)map.get("guid"));
		            }
		            Date date = new Date();
		            for (int i = 0; i < listDept.size(); i++) {
		              YHUpdateLogDetl task = new  YHUpdateLogDetl();
		              YHExtDept toDeptObj  = listDept.get(i);
		              String toDept = "";
		              String toDeptName = "";
		              if(toDeptObj != null){
		                toDept = toDeptObj.getDeptId()+"";//部门GUID
		                toDeptName = toDeptObj.getDeptName();//部门名称
		              }
		              if(fromDept.equals(toDept)){//如果是本单位不需要发送
		            	  task.setClientGuid(guId1);
		              }else{
		            	  
		            	  task.setClientGuid(guId);
		              }
		              task.setClientName(toDeptName);
		              task.setUpdateStatus("1");
		              task.setLogSeqId(logSeqId);
		              task.setDoneTime(date);
		              YHUpdateServerLogic.add(dbConn, task);
		            }
		      }
		      }
		      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		      request.setAttribute(YHActionKeys.RET_MSRG, "加入任务队列成功！");
		  }catch (Exception ex) {
		      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		      throw ex;
		    }
		    return "/core/esb/server/update/success.jsp";
		  }
	  
	  /*
	   * 获取系统更新日志列表
	   */
	  
	 public String getUpdateServerList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		    Connection dbConn = null;
		    try {
		      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		      dbConn = requestDbConn.getSysDbConn();
		      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		      YHUpdateServerLogic logic = new YHUpdateServerLogic();
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
	 
	  //删除更新信息
	  public String deleteFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		    String seqIdStr = request.getParameter("seqId");
		    Connection dbConn = null;
		    try {
		      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		      dbConn = requestDbConn.getSysDbConn();
		      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);

		      YHUpdateServerLogic logic = new YHUpdateServerLogic();
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
	  
	  public String getUpdateDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		    Connection dbConn = null;
		    try {
		      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		      dbConn = requestDbConn.getSysDbConn();
		      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		      String logSeqId = request.getParameter("logSeqId") == null ? "" : request.getParameter("logSeqId");
		      String[]  fileter = {"LOG_SEQ_ID='" + logSeqId + "'"};
		      YHUpdateServerLogic logic=new YHUpdateServerLogic();
		      List<YHUpdateLogDetl>  list = logic.select(dbConn, fileter);
		      String data = "[";
		      for (int i = 0; i < list.size(); i++) {
		        data = data + YHFOM.toJson(list.get(i)) + ",";
		      }
		      if(list.size()>0){
		        data = data.substring(0, data.length()-1);
		      }
		      data = data +"]";
		      request.setAttribute(YHActionKeys.RET_DATA, data);
		      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
		    } catch (Exception e) {
		      e.printStackTrace();
		      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
		    }
		    return "/core/inc/rtjson.jsp";
		  }
}
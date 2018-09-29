package yh.subsys.jtgwjh.notifyManage.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.esb.client.data.YHEsbClientConfig;
import yh.core.esb.client.data.YHEsbConst;
import yh.core.esb.client.data.YHExtDept;
import yh.core.esb.client.logic.YHDeptTreeLogic;
import yh.core.esb.frontend.services.YHEsbService;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.file.YHFileUtility;
import yh.core.util.file.YHZipFileTool;
import yh.core.util.form.YHFOM;
import yh.subsys.jtgwjh.docSend.logic.YHDocSendLogic;
import yh.subsys.jtgwjh.notifyManage.act.YHJhNotifyInfoAct;
import yh.subsys.jtgwjh.notifyManage.data.YHJhNotify; 
import yh.subsys.jtgwjh.task.data.YHJhTaskLog;
import yh.subsys.jtgwjh.util.YHDocUtil;

public class YHJhNotifyInfoLogic{
	  private static Logger log = Logger.getLogger(YHJhNotifyInfoLogic.class);
	  public static String filePath = YHSysProps.getAttachPath() + File.separator + "notify";
	  private static final String attachmentFolder = "notify";
	 public Map<String, String> fileUploadLogic1(YHFileUploadForm fileForm) throws Exception {
		    Map<String, String> result = new HashMap<String, String>();
		    String filePath = YHSysProps.getAttachPath() ;
		    Calendar cld = Calendar.getInstance();
		    int year = cld.get(Calendar.YEAR) % 100;
		    int month = cld.get(Calendar.MONTH) + 1;
		    String mon = month >= 10 ? month + "" : "0" + month;
		    String hard = year + mon;
		    Iterator<String> iKeys = fileForm.iterateFileFields();
		    while (iKeys.hasNext()) {
		      String fieldName = iKeys.next();
		      String fileName = fileForm.getFileName(fieldName);
		      String trusFileName = "";
		      if (YHUtility.isNullorEmpty(fileName)) {
		        continue;
		      }
		      String rand =   YHGuid.getRawGuid();
		      trusFileName = rand + "_" + fileName;
		      result.put(hard + "_" + rand, fileName);
		      fileForm.saveFile(fieldName, filePath + File.separator + YHJhNotifyInfoAct.attachmentFolder + File.separator
		          + hard + File.separator + trusFileName);
		    }
		    YHSelAttachUtil selA = new YHSelAttachUtil(fileForm, YHJhNotifyInfoAct.attachmentFolder);
		    result.putAll(selA.getAttachInFo());
		    return result;
		  }
	 
	  public StringBuffer uploadMsrg2Json(YHFileUploadForm fileForm)
		      throws Exception {
		    StringBuffer sb = new StringBuffer();
		    Map<String, String> attr = null;
		    String attachmentId = "";
		    String attachmentName = "";
		    try {
		      attr = fileUploadLogic1(fileForm);
		      Set<String> attrKeys = attr.keySet();
		      for (String key : attrKeys) {
		        String fileName = attr.get(key);
		        attachmentId += key + ",";
		        attachmentName += fileName + "*";
		      }
		      sb.append("{");
		      sb.append("'attachmentId':").append("\"").append(attachmentId).append(
		          "\",");
		      sb.append("'attachmentName':").append("\"").append(attachmentName)
		          .append("\"");
		      sb.append("}");
		    } catch (Exception e) {
		      e.printStackTrace();
		      throw e;
		    }
		    return sb;
		  }
	
	  public void setNotifyValueLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
		  YHORM orm = new YHORM();
		  String notifyTitle=fileForm.getParameter("notifyTitle");
		  String receiveDept = fileForm.getParameter("reciveDept");
		  String content=fileForm.getParameter("content");
		  String attachmentId = fileForm.getParameter("attachmentId");
		  String attachmentName = fileForm.getParameter("attachmentName");
		  String receiveDeptName=fileForm.getParameter("reciveDeptDesc");
		  String createDate = YHUtility.getCurDateTimeStr();
		  String userId=String.valueOf(person.getSeqId());
		  String userName=person.getUserName();
		  String sendDeptId=String.valueOf(person.getDeptId());
		  String sendDeptName=getDeptNameById(sendDeptId,dbConn);
		  String seqId = fileForm.getParameter("seqId");
		  try{
		  YHJhNotify notifyInfo=new YHJhNotify();
		  notifyInfo.setUserId(userId);
		  notifyInfo.setUserName(userName);
		  notifyInfo.setAttachmentId(attachmentId);
		  notifyInfo.setAttachmentName(attachmentName);
		  notifyInfo.setContent(content);
		  notifyInfo.setTitle(notifyTitle);
		  notifyInfo.setReceiveDept(receiveDept);
		  notifyInfo.setSendDept(sendDeptId);
		  notifyInfo.setSendDeptName(sendDeptName);
		  notifyInfo.setCreateDate(YHUtility.parseDate(createDate));
		  notifyInfo.setContent(content);
	          notifyInfo.setReceiveDeptName(receiveDeptName);
		  notifyInfo.setPublish("0");
		  orm.saveSingle(dbConn,notifyInfo	);
		  }catch (Exception e) {
		      throw e;
		    }
		   
	  }
	  public String getDeptNameById(String  receiveDept, Connection dbConn )  throws Exception{
		  String result="";
		  String sql="";
		  PreparedStatement ps = null;
		  ResultSet rs = null;
		  try{
			  	sql="select  DEPT_NAME from oa_department where SEQ_ID = "+receiveDept;
			  	ps=dbConn.prepareStatement(sql);
			      rs = ps.executeQuery();
			      if (rs.next()) {
			        result = rs.getString(1);
			      }
		  }catch (Exception e) {
		      throw e;
		    } finally {
		        YHDBUtility.close(ps, rs, log);
		    }
	      return result; 
	  }
	  public boolean delFloatFile(Connection dbConn, String attId, String attName, int seqId) throws Exception {
		    boolean updateFlag = false;
		    if (seqId != 0) {
		      YHORM orm = new YHORM();
		      YHJhNotify notify = (YHJhNotify)orm.loadObjSingle(dbConn, YHJhNotify.class, seqId);
		      String[] attIdArray = {};
		      String[] attNameArray = {};
		      String attachmentId = notify.getAttachmentId();
		      String attachmentName = notify.getAttachmentName();
		      //YHOut.println("attachmentId"+attachmentId+"--------attachmentName"+attachmentName);
		      if (!"".equals(attachmentId.trim()) && attachmentId != null && attachmentName != null) {
		        attIdArray = attachmentId.trim().split(",");
		        attNameArray = attachmentName.trim().split("\\*");
		      }
		      String attaId = "";
		      String attaName = "";
		  
		      for (int i = 0; i < attIdArray.length; i++) {
		        if (attId.equals(attIdArray[i])) {
		          continue;
		        }
		        attaId += attIdArray[i] + ",";
		        attaName += attNameArray[i] + "*";
		      }
		      //YHOut.println("attaId=="+attaId+"--------attaName=="+attaName);
		      notify.setAttachmentId(attaId.trim());
		      notify.setAttachmentName(attaName.trim());
		      orm.updateSingle(dbConn, notify);
		    }
		  //处理文件
		    String[] tmp = attId.split("_");
		    String path = filePath + File.separator  + tmp[0] + File.separator + tmp[1] + "_" + attName;
		    File file = new File(path);
		    if(file.exists()){
		      file.delete();
		    } else {
		      //兼容老的数据
		      String path2 = filePath + File.separator  + tmp[0] + File.separator + tmp[1] + "." + attName;
		      File file2 = new File(path2);
		      if(file2.exists()){
		        file2.delete();
		      }
		    }
		    updateFlag=true;
		    return updateFlag;
		  }
	  
	  //获取公告列表信息
	  public String getJsonLogic(Connection dbConn, Map request1, YHPerson person,HttpServletRequest request,String publish) throws Exception {
		    try {
		      String sql = "select SEQ_ID,GUID,TITLE,RECEIVE_DEPT,RECEIVE_DEPT_NAME,CREATE_DATE,PUBLISH from jh_notify where PUBLISH !='2'  order by create_date desc "; 
		      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request1);
		      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
		      return pageDataList.toJson();
		    } catch (Exception e) {
		      throw e;
		    }
		  }
	  
	  /**
	   * 删除文件
	   * 
	   * @param dbConn
	   * @param seqIdStr
	   * @throws Exception
	   */
	  public String deleteFileLogic(Connection dbConn, String seqIdStr, String filePath) throws Exception {
	    YHORM orm = new YHORM();
	    if (YHUtility.isNullorEmpty(seqIdStr)) {
	      seqIdStr = "";
	    }
	    String tempReturn = "";
	    try {
	      String seqIdArry[] = seqIdStr.split(",");
	      if (!"".equals(seqIdArry) && seqIdArry.length > 0) {
	        for (String seqId : seqIdArry) {
	          StringBuffer attIdBuffer = new StringBuffer();
	          StringBuffer attNameBuffer = new StringBuffer();
	          YHJhNotify notifyInfo = (YHJhNotify) orm.loadObjSingle(dbConn, YHJhNotify.class, Integer.parseInt(seqId));
	          String attachmentId = YHUtility.null2Empty(notifyInfo.getAttachmentId());
	          String attachmentName = YHUtility.null2Empty(notifyInfo.getAttachmentName());
	          attIdBuffer.append(attachmentId.trim());
	          attNameBuffer.append(attachmentName.trim());
	          String[] attIdArray = {};
	          String[] attNameArray = {};
	          if (!YHUtility.isNullorEmpty(attIdBuffer.toString()) && !YHUtility.isNullorEmpty(attNameBuffer.toString()) && attIdBuffer.length() > 0) {
	            attIdArray = attIdBuffer.toString().trim().split(",");
	            attNameArray = attNameBuffer.toString().trim().split("\\*");
	          }
	          if (attIdArray != null && attIdArray.length > 0) {
	            for (int i = 0; i < attIdArray.length; i++) {
	              Map<String, String> map = this.getFileName(attIdArray[i], attNameArray[i]);
	              if (map.size() != 0) {
	                Set<String> set = map.keySet();
	                // 遍历Set集合
	                for (String keySet : set) {
	                  String key = keySet;
	                  String keyValue = map.get(keySet);
	                  String attaIdStr = this.getAttaId(keySet);
	                  String fileNameValue = attaIdStr + "_" + keyValue;
	                  String fileFolder = this.getFilePathFolder(key);
	                  String oldFileNameValue = attaIdStr + "." + keyValue;
	                  File file = new File(filePath + File.separator + fileFolder + File.separator + fileNameValue);
	                  File oldFile = new File(filePath + File.separator + fileFolder + File.separator + oldFileNameValue);
	                  if (file.exists()) {
	                    YHFileUtility.deleteAll(file.getAbsoluteFile());
	                  } else if (oldFile.exists()) {
	                    YHFileUtility.deleteAll(oldFile.getAbsoluteFile());
	                  }
	                }
	              }
	            }
	          }     
	          // 删除数据库信息
	          orm.deleteSingle(dbConn, notifyInfo);
	          tempReturn += notifyInfo.getTitle()+",";
	        }
	      }
	      if(tempReturn.length() > 1 && tempReturn.endsWith(",")){
	        tempReturn = tempReturn.substring(0, tempReturn.length() - 1);
	      }
	      return tempReturn;
	    } catch (Exception e) {
	      throw e;
	    }
	  }
	  /**
	   * 拼接附件Id与附件名
	   * 
	   * @param attachmentId
	   * @param attachmentName
	   * @return
	   */
	  public Map<String, String> getFileName(String attachmentId, String attachmentName) {
	    Map<String, String> map = new HashMap<String, String>();
	    if (YHUtility.isNullorEmpty(attachmentId) || YHUtility.isNullorEmpty(attachmentName)) {
	      return map;
	    }
	    if (!"".equals(attachmentId.trim()) && !"".equals(attachmentName.trim())) {
	      String attachmentIds[] = attachmentId.split(",");
	      String attachmentNames[] = attachmentName.split("\\*");
	      if (attachmentIds.length != 0 && attachmentNames.length != 0) {
	        for (int i = 0; i < attachmentIds.length; i++) {
	          map.put(attachmentIds[i], attachmentNames[i]);
	        }
	      }
	    }
	    return map;
	  }
	  
	  /**
	   * 得到附件的Id 兼老数据
	   * 
	   * @param keyId
	   * @return
	   */
	  public String getAttaId(String keyId) {
	    String attaId = "";
	    if (keyId != null && !"".equals(keyId)) {
	      if (keyId.indexOf('_') != -1) {
	        String[] ids = keyId.split("_");
	        if (ids.length > 0) {
	          attaId = ids[1];
	        }

	      } else {
	        attaId = keyId;
	      }
	    }
	    return attaId;
	  }
	  
	  /**
	   * 更新发文登记
	   * 
	   * @param dbConn
	   * @param fileForm
	   * @param person
	   * @throws Exception
	   */
	  public YHJhNotify updateNotifyInfo(Connection dbConn, YHFileUploadForm fileForm, YHPerson person, int seqId) throws Exception {
		  YHORM orm = new YHORM();
		  String notifyTitle=fileForm.getParameter("title");
		  String receiveDept = fileForm.getParameter("reciveDept");
		  String content=fileForm.getParameter("content");
		  String attachmentId = fileForm.getParameter("attachmentId");
		  String attachmentName = fileForm.getParameter("attachmentName");
		  String receiveDeptName=fileForm.getParameter("reciveDeptDesc");
		  String createDate = YHUtility.getCurDateTimeStr();
		  String userId=String.valueOf(person.getSeqId());
		  String userName=person.getUserName();
		  String sendDeptId=String.valueOf(person.getDeptId());
		  String sendDeptName=getDeptNameById(sendDeptId,dbConn);
		  try{
		  //YHJhNotify notifyInfo=new YHJhNotify();
		  YHJhNotify notifyInfo = (YHJhNotify) orm.loadObjSingle(dbConn, YHJhNotify.class, seqId);
		  notifyInfo.setUserId(userId);
		  notifyInfo.setUserName(userName);
		  notifyInfo.setAttachmentId(attachmentId);
		  notifyInfo.setAttachmentName(attachmentName);
		  notifyInfo.setContent(content);
		  notifyInfo.setTitle(notifyTitle);
		  notifyInfo.setReceiveDept(receiveDept);
		  notifyInfo.setSendDept(sendDeptId);
		  notifyInfo.setSendDeptName(sendDeptName);
		  notifyInfo.setCreateDate(YHUtility.parseDate(createDate));
		  notifyInfo.setContent(content);
	    	  notifyInfo.setReceiveDeptName(receiveDeptName);
		  notifyInfo.setPublish("0");
		  orm.updateSingle(dbConn,notifyInfo);
		  return notifyInfo;
		  }catch (Exception e) {
		      throw e;
		    }
	  }

	  /**
	   * 得到该文件的文件夹名
	   * 
	   * @param key
	   * @return
	   */
	  public String getFilePathFolder(String key) {
	    String folder = "";
	    if (key != null && !"".equals(key)) {
	      if (key.indexOf('_') != -1) {
	        String[] str = key.split("_");
	        for (int i = 0; i < str.length; i++) {
	          folder = str[0];
	        }
	      } else {
	        folder = "all";
	      }
	    }
	    return folder;
	  }
	  
	  
	  public void sendNotify(Connection dbConn, int jhNotifyId, HttpServletRequest request, String forward) throws Exception{
		    
		    YHORM orm = new YHORM();
		    YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		    
		    //循环发送单位
		    YHJhNotify notifysendInfo = (YHJhNotify) orm.loadObjSingle(dbConn, YHJhNotify.class, jhNotifyId);
		   notifysendInfo.setSendDateTime(YHUtility.parseTimeStamp());
		   notifysendInfo.setPublish("1");
		    orm.updateSingle(dbConn, notifysendInfo);
		    //此次打包任务名称
		    String taskName ="JHNOTIFY_" + YHGuid.getRawGuid();
		    //生成xml文件
		    Map<String, String> filters = new HashMap<String, String>();

		    
		    String xml = notifysendInfo.toXML();
		    String path =YHSysProps.getAttachPath() + File.separator + "uploadJTGW" + File.separator + taskName;
		    String FileName = "JHNOTIFY_" + YHGuid.getRawGuid() + ".xml";
		    while (YHDocUtil.getExist(path, FileName)) {
		      FileName = "JHNOTIFY_" + YHGuid.getRawGuid() + ".xml";
		    }
		    File file = new File(path + File.separator + FileName);
		    if(!file.exists()){
		      File file2 = new File(path);
		      if(!file2.exists()){
		        file2.mkdirs();
		      }
		      file.createNewFile();
		    }
		    FileOutputStream out = new FileOutputStream(file);
		    out.write(xml.getBytes("utf-8"));
		    out.close();
		    
		    //复制附件到指定目录
		    String attachmentId = notifysendInfo.getAttachmentId();
		    String attachmentName = notifysendInfo.getAttachmentName();
		    String attachmentIdArray[] = attachmentId.split(",");
		    String attachmentNameArray[] = attachmentName.split("\\*");
		    for(int j = 0; j < attachmentIdArray.length; j++){
		      String attachPath = getAttachPath(attachmentIdArray[j], attachmentNameArray[j], "1".equals(forward) ? "docReceive" : attachmentFolder);
		      File fileTemp = new File(attachPath);
		      YHFileUtility.copyFile(attachPath, path + File.separator + fileTemp.getName());
		    }
		    //生成zip压缩包
		    YHZipFileTool.doZip(path, path + ".zip");
		    //循环连接esb服务器发送任务
		    String reciveDeptName = notifysendInfo.getReceiveDept();
		    
	        YHDocSendLogic dsl = new YHDocSendLogic();
            String reciveDeptDescClient = dsl.getEsbUser(dbConn, reciveDeptName);
		    YHEsbClientConfig config = YHEsbClientConfig.builder(request.getRealPath("/") + YHEsbConst.CONFIG_PATH) ;
		    YHEsbService esbService = new YHEsbService();
		    
		    String ret = esbService.send(path + ".zip", reciveDeptDescClient, config.getToken(), "JGnotify", "");
		    //ret json串中code值为0时 改为发送中
		    Map map = YHFOM.json2Map(ret);
		    String code = (String) map.get("code");
		    if("0".equals(code)){
			   notifysendInfo.setGuid((String)map.get("guid"));
			   orm.updateSingle(dbConn, notifysendInfo);
			   YHJhNotify notify=(YHJhNotify)orm.loadObjSingle(dbConn, YHJhNotify.class, jhNotifyId);
			    String recDeptIdArray[] = notify.getReceiveDept().split(",");
			     String recDeptNameArray[] =notify.getReceiveDeptName().split(",");
			      for(int i=0;i<recDeptIdArray.length;i++)
			      {
			    	if(!YHUtility.isNullorEmpty(recDeptIdArray[i])){
			    		YHJhTaskLog tasklog= new YHJhTaskLog();
			    		tasklog.setStatus("0");
			    		tasklog.setFromDept(notify.getSendDept());
			    		tasklog.setFromDeptName(notify.getSendDeptName());
			    		String reciveDeptIdTemp = "";
			    		String reciveDeptNameTemp = "";
			    		if("0".equals(recDeptIdArray[i])){
			    			reciveDeptIdTemp = notify.getReceiveDept();
			    			reciveDeptNameTemp = notify.getReceiveDeptName();
			    		}
			    		else{
			    			reciveDeptIdTemp = recDeptIdArray[i];
			    			reciveDeptNameTemp = recDeptNameArray[i];;
			    		}
			    		tasklog.setToDept(reciveDeptIdTemp);
			    		tasklog.setToDeptName(reciveDeptNameTemp);
			    		tasklog.setType("3");
			    		tasklog.setUserId(notify.getUserId());
			    		tasklog.setOptTime(YHUtility.parseDate(YHUtility.getCurDateTimeStr()));
			    		tasklog.setUserName(notify.getUserName());
			    		tasklog.setGuid((String)map.get("guid"));
			    		orm.saveSingle(dbConn, tasklog);
			    	}else
			    	{
			    		return ;
			    	}
			    	
			      }
		      //系统日志
		      YHSysLogLogic.addSysLog(dbConn, "60", "发送发文成功：" + notifysendInfo.toString() ,person.getSeqId(), request.getRemoteAddr());
		    }
		    else{
		      //修改task任务为本地发送失败状态
			    filters.put("GUID",  notifysendInfo.getGuid()+"");
			    List<YHJhTaskLog>  jhTaskLogList= orm.loadListSingle(dbConn, YHJhTaskLog.class, filters);
		       for(YHJhTaskLog docsendTasks : jhTaskLogList){
		        docsendTasks.setStatus("-2");//本地失败
		        orm.updateSingle(dbConn, docsendTasks);
		      }
		      //系统日志
		      YHSysLogLogic.addSysLog(dbConn, "60", "发送发文失败：" + notifysendInfo.toString() ,person.getSeqId(), request.getRemoteAddr());
		    }
		  }
	  
	  
	  public static String getAttachPath(String aId , String aName  , String moduleDesc) throws Exception {
		    String pathPx = YHSysProps.getAttachPath();
		    String filePath = pathPx + File.separator +  moduleDesc;
		    int index = aId.indexOf("_");
		    String hard = "";
		    String str = "";
		    if (index > 0) {
		      hard = aId.substring(0, index);
		      str = aId.substring(index + 1);
		    } else {
		      hard = "all";
		      str = aId;
		    }
		    String path = filePath + File.separator +  hard + File.separator +  str + "_" + aName;
		    return path;
		  }
	  /**
	   * 获取详情
	   * 
	   * @param conn
	   * @param seqId
	   * @return
	   * @throws Exception
	   */
	  public YHJhNotify getDetailLogic(Connection conn, int seqId) throws Exception {
	    try {
	      YHORM orm = new YHORM();
	      return (YHJhNotify) orm.loadObjSingle(conn, YHJhNotify.class, seqId);
	    } catch (Exception ex) {
	      throw ex;
	    }
	  }
	  
	  /*
	   * 文件签收
	   */
	  public static String toSearchData(Connection conn, Map request,HttpServletRequest request1) throws Exception {
		    String publish = request1.getParameter("status") == null ? "" : request1 .getParameter("status");
		    String sql = "select seq_id,title,send_dept_name,send_date_time from jh_notify where guid != '' and publish ='2' ";
		 //   sql = sql + " where  publish='" + publish + "' ";
		    String title = request1.getParameter("title");
		    String sendDate = request1.getParameter("sendDate");
		    String sendDate2 = request1.getParameter("sendDate2");
		    if (!YHUtility.isNullorEmpty(title)) {
		    	title = title.replace("'", "''");
		      sql = sql + "and  TITLE like '%" + YHDBUtility.escapeLike(title)
		          + "%' " + YHDBUtility.escapeLike();
		    }
		    if (!YHUtility.isNullorEmpty(sendDate)) {
		      sql = sql + " and "
		          + YHDBUtility.getDateFilter("SEND_DATE_TIME", sendDate, ">=");
		    }
		    if (!YHUtility.isNullorEmpty(sendDate2)) {
		      sql = sql
		          + " and "
		          + YHDBUtility.getDateFilter("SEND_DATE_TIME", sendDate2 + " 23:59:59",
		              "<=");
		    }
		    sql = sql + " order by send_date_time desc";
	
		    // 下面是判断是否登录人部门是计划的接收部门，且团组号也要在这条计划中

		    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
		    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn, queryParam,sql);
		    return pageDataList.toJson();
		  }
	  
	  /**
	   * 查询ById
	   * 
	   * @param dbConn
	   * @return
	   * @throws Exception
	   */
	  public static YHJhNotify getById(Connection dbConn, String seqId)
	      throws Exception {
	    YHORM orm = new YHORM();
	    YHJhNotify code = (YHJhNotify) orm.loadObjSingle(dbConn,YHJhNotify.class, Integer.parseInt(seqId));
	    return code;
	  }
	  
	  public static void updateNotify(Connection dbConn, YHJhNotify notify)  throws Exception {
		    YHORM orm = new YHORM();
		    orm.updateSingle(dbConn, notify);
		  }
	  
	  public static void add(Connection dbConn, YHJhNotify code)
		      throws Exception {
		    YHORM orm = new YHORM();
		    orm.saveSingle(dbConn, code);
		    dbConn.commit();
		    //return YHCalendarLogic.getMaSeqId(dbConn, "JH_NOTIFY");
		  }

	  
	  /**
	   * 获取最大的SeqId值
	   * 
	   * @param dbConn
	   * @return
	   */
	  public int getMaxSeqId(Connection dbConn) {
	    String sql = "select MAX(SEQ_ID) SEQ_ID from jh_notify";
	    int seqId = 0;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	      ps = dbConn.prepareStatement(sql);
	      rs = ps.executeQuery();
	      if (rs.next()) {
	        seqId = rs.getInt("SEQ_ID");
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	      YHDBUtility.close(ps, rs, log);
	    }
	    return seqId;
	  }

	public static String updateNotifyStatus(Connection dbConn, String guid,int state, String to) throws Exception{
		   YHORM orm = new YHORM();
		    Map<String, String> filters = new HashMap<String, String>();
		    filters.put("GUID", guid);
		    YHJhTaskLog notifyTask = null;
		    List<YHJhTaskLog> notifyTaskList = null;
		    if(YHUtility.isNullorEmpty(to)){
		    	notifyTaskList =  orm.loadListSingle(dbConn, YHJhTaskLog.class, filters);
		    }
		    else{
		      YHDeptTreeLogic dtl = new YHDeptTreeLogic();
		      YHExtDept ed = dtl.getDeptByEsbUser(dbConn, to);//根据发送部门，查询外部组织机构
		      filters.put("RECIVE_DEPT", ed.getDeptId()+"");
		      notifyTask = (YHJhTaskLog) orm.loadObjSingle(dbConn, YHJhTaskLog.class, filters);
		    }
		    
		    if(notifyTask != null || notifyTaskList !=null){
		      switch(state){
		        case 1 : {
		          //修改task任务为发送完毕
		          for(YHJhTaskLog notifyTaskTemp : notifyTaskList){
		        	  notifyTaskTemp.setStatus("3");//发送完毕
		            orm.updateSingle(dbConn, notifyTaskTemp);
		          }
		          break;
		        }
		        case 2 : {
		          //修改task任务为对方已接收
		        	notifyTask.setStatus("4");//对方已接收
		           orm.updateSingle(dbConn, notifyTask);
		          break;
		        }
		        case -3 : {
		          //修改task任务为对方接收失败
		          notifyTask.setStatus("-3");//对方已接收
		          orm.updateSingle(dbConn, notifyTask);
		          break;
		        }
		        default : {
		          //修改task任务为发送失败
		        	notifyTask.setStatus("-1");//发送失败
		          orm.updateSingle(dbConn, notifyTask);
		          break;
		        }
		     }
		      return "1";
		    }
		    else{
		      return "";
		    }
	}
	  /**
	   * 根据任务的GUID查询是否存在
	   * 
	   * @param dbConn
	   * @param item
	   * @return
	   * @throws Exception
	   */
	  public static boolean checkTask(Connection dbConn, String guId) throws Exception {
	    Statement stmt = null;
	    ResultSet rs = null;
	    if (YHUtility.isNullorEmpty(guId)) {
	      guId = "";
	    }
	    String sql = "SELECT * from JH_TASK_LOG where guId='" + guId+ "'";
	    try {
	      stmt = dbConn.createStatement();
	      rs = stmt.executeQuery(sql);
	      if (rs.next()) {
	        return true;
	      }
	    } catch (Exception ex) {
	      throw ex;
	    } finally {
	      YHDBUtility.close(stmt, rs, log);
	    }
	    return false;
	  }
}


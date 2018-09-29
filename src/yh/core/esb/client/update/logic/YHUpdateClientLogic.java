package yh.core.esb.client.update.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.esb.client.data.YHEsbClientConfig;
import yh.core.esb.client.data.YHEsbConst;
import yh.core.esb.client.data.YHExtDept;
import yh.core.esb.client.logic.YHDeptTreeLogic;
import yh.core.esb.client.service.YHWSCaller;
import yh.core.esb.client.update.data.YHUpdateClientLog;
import yh.core.esb.frontend.services.YHEsbService;
import yh.core.esb.server.update.data.YHUpdateLogDetl;
import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHSysProps;
import yh.core.install.YHInstallConfig;
import yh.core.load.YHPageLoader;
import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUtility;
import yh.core.util.file.YHZipFileUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.jtgwjh.docSend.data.YHJhDocsendTasks;
import yh.subsys.jtgwjh.docSend.logic.YHDocSendLogic;
import yh.subsys.jtgwjh.util.YHDocUtil;
import yh.user.api.core.db.YHDbconnWrap;

import com.agile.zip.CZipInputStream;
import com.agile.zip.ZipEntry;



public class YHUpdateClientLogic{
	 private static Logger log = Logger .getLogger("yh.core.esb.client.update.logic.YHUpdateClientLogic");
	public static void unZipFileXml(String filePath, String fromUnit,String guid) throws Exception {
	    String savePath = YHSysProps.getRootPath() + "/update/deploy";//接收附件存放路径
	    Connection dbConn = null;
	    YHDbconnWrap dbUtil = new YHDbconnWrap();   
	    dbConn = dbUtil.getSysDbConn();
	    File file = new File(filePath);
	    InputStream rarFile = new FileInputStream(file);
	    if (rarFile == null) {
	      return ;
	    }
	    CZipInputStream zip = new CZipInputStream(rarFile);// 支持中文目录
	    ZipEntry entry;
	    String xmlFile = "";//系统更新描述文件
	    String upXMLPath = filePath;
	    while ((entry = zip.getNextEntry()) != null) {// 循环zip下的所有文件和目录
	      String fileName = entry.getName();
	      if (YHUtility.isNullorEmpty(fileName)) {
	        continue;
	      }
	      File outFile = new File(savePath + "/" + fileName);
	      File outPath = outFile.getParentFile();
	      if (!outPath.exists()) {
	        outPath.mkdirs();
	      }
          if(fileName.endsWith("desc.xml")){
        	  xmlFile = fileName;
        	  upXMLPath = savePath + File.separator   + fileName;
          }
	      if (!entry.isDirectory()) {
	        try {
	          outFile = new File(savePath + File.separator   + fileName);
	          outFile.createNewFile();
	          FileOutputStream out = new FileOutputStream(outFile);
	          int len = 0;
	          byte[] buff = new byte[4096];
	          while ((len = zip.read(buff)) != -1) {
	            out.write(buff, 0, len);
	          }
	          out.close();
	        } catch (IOException e) {
	          e.printStackTrace();
	        }
	      }
	      if(entry.isDirectory()){
	    	  File zipFolder = new File(savePath + File.separator   + fileName);  
	    	   if (!zipFolder.exists()) {  
	               zipFolder.mkdirs();  
	    		  }  
	      }
	    }
        zip.closeEntry();
	    if (!YHUtility.isNullorEmpty(xmlFile)) {//解析xml文件
	       parseXML(upXMLPath, dbConn, fromUnit, guid);
	    }
	    System.out.println(xmlFile);
	  }

	  /**
	   * 只支持ZIP附件
	   * 
	   * @param filePath:公文发送附件ZIP
	   * @param savePath ： 将解压的附件保存路径
	   *          文件元素名称
	        fromUnit:发送单位 guid:ESB数据交互平台发送的唯一表示
	   * @param attachStrArrys ： 公文XML返回的数据数组
	   *         guid ： ESB 唯一标识
	   * @throws Exception
	   */
	  public static void unZipFile(String filePath, String savePath, Connection dbConn,String fromUnit,String guid) throws Exception{
	    File file = new File(filePath);   
	    InputStream rarFile = new FileInputStream(file);
	    String upXMLPath = filePath;
	    if(rarFile == null){//如果没有文件跳出
	      return ;
	    }
	    CZipInputStream zip=new CZipInputStream(rarFile);//支持中文目录
	    ZipEntry entry;
	    while ((entry = zip.getNextEntry()) != null) {//循环zip下的所有文件和目录
	      String fileName=entry.getName(); 
	      if(YHUtility.isNullorEmpty(fileName)){
	        continue;
	      }
	      File outFile = new File(savePath + "/" + fileName);
	      if(entry.isDirectory()){
	    	  File zipFolder = new File(savePath + File.separator   + fileName);  
	    	   if (!zipFolder.exists()) {  
	               zipFolder.mkdirs();  
	    		  }  
	      } else{
		        try {
			          outFile = new File(savePath + File.separator   + fileName);
			          outFile.createNewFile();
			          FileOutputStream out = new FileOutputStream(outFile);
			          int len = 0;
			          byte[] buff = new byte[4096];
			          while ((len = zip.read(buff)) != -1) {
			            out.write(buff, 0, len);
			          }
			          zip.closeEntry();
			          out.close();
			        } catch (IOException e) {
			          e.printStackTrace();
			        }
			      }
	    }
	}

	  /**
	   * 
	   *  解析公告XML、并新建收文记录
	   * upXMLPath: 临时公文XML存放路径
	   * @param savePath
	   *          :保存文件路径
	   * @param fromUnit
	   *          ：发送单位
	   * @param dbConn
	   *          ：数据库 fromUnit：发送单位 * guid:ESB数据交互平台发送的唯一标识
	   * @throws Exception
	   */
	  public static void parseXML(String upXMLPath, Connection dbConn, String fromUnit, String guid) throws Exception {
		//String updateUser = YHUpdateServerLogic.getUser(); 
		SAXReader saxReader = new SAXReader();
	    File XMLFile = new File(upXMLPath);
	    Document document = saxReader.read(XMLFile);
	    Element root = document.getRootElement();
	    YHUpdateClientLog  clientLog = null;
	    if (!YHUtility.isNullorEmpty(root.getName()) && root.getName().equals("body")) {
	    	clientLog = new YHUpdateClientLog();
	      List<Element> elements = root.elements();
	      for (Element el : elements) {
	        String elName = el.getName();
	        String elData = (String) el.getData() == null ? "" : (String) el.getData();
	        elData = elData.trim();
	        if (elName.equalsIgnoreCase("desc")) {
	        	clientLog.setUpdateDesc(elData);
	        }
	        else if (elName.equalsIgnoreCase("toVersion")) {
	        	clientLog.setToVersion(Integer.parseInt(elData));
	          }
	      }
	    }
	    if (clientLog != null) {
	    	clientLog.setUpdateStatus("1");
	    	clientLog.setReseiveTime(YHUtility.parseDate(YHUtility.getCurDateTimeStr()));
	    	YHPerson person = new YHPerson();
	    	clientLog.setGuid(guid);
	      //新建系统升级
	       add(dbConn, clientLog);
	       //获取request
	       HttpServletRequest request = getWebserviceHttp();
	       String IP = "";
	       if(request != null ){
	         IP = request.getRemoteAddr();
	       }
	       YHPerson user = new YHPerson();
	       user.setSeqId(0);
	       user.setUserName("系统");
	       //系统日志
	       YHSysLogLogic.addSysLog(dbConn, "62", "系统成功接收日志：" + clientLog.toString() ,0, IP);
	    }
	  }
	  
	  //获取客户端更新信息
		public String getJsonLogic(Connection dbConn, Map parameterMap,YHPerson person, HttpServletRequest request)throws Exception {
		    try {
			      String sql = "select SEQ_ID,UPDATE_DESC,TO_VERSION,UPDATE_USER,DONE_TIME,UPDATE_STATUS  from update_client_log  order by DONE_TIME desc "; 
			      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(parameterMap);
			      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
			      return pageDataList.toJson();
			    } catch (Exception e) {
			      throw e;
			    }
		}
	  public String deleteFileLogic(Connection dbConn, String seqIdStr)throws Exception {
			YHORM orm = new YHORM();
		    if (YHUtility.isNullorEmpty(seqIdStr)) {
		      seqIdStr = "";
		    }
		    String tempReturn = "";
		    try {
		      String seqIdArry[] = seqIdStr.split(",");
		      if (!"".equals(seqIdArry) && seqIdArry.length > 0) {
		        for (String seqId : seqIdArry) {
		          YHUpdateClientLog clientLog = (YHUpdateClientLog) orm.loadObjSingle(dbConn, YHUpdateClientLog.class, Integer.parseInt(seqId));   
		          // 删除数据库信息
		          orm.deleteSingle(dbConn, clientLog);
		          tempReturn += clientLog.getUpdateDesc()+",";
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
	  
	public static void add(Connection conn, YHUpdateClientLog clientLog) throws Exception{
	    YHORM orm = new YHORM();
	    orm.saveSingle(conn, clientLog);
	    conn.commit();
	}
	  public static HttpServletRequest getWebserviceHttp(){
		    MessageContext mc =  (MessageContext) org.apache.axis.MessageContext.getCurrentContext();;  
		    HttpServletRequest request = (HttpServletRequest) mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);  
		    return request;
		  }
	  /*
	   * 更新系统
	   * 属性文件，数据库，一般源文件
	   */
	  
	  public  boolean  updateSystem(String filePath) throws Exception {  //更新系统
		  YHInstallConfig config = new YHInstallConfig();
		  String updateDir=YHSysProps.getRootPath() + "\\update\\deploy\\";
		  String historyDir = YHSysProps.getRootPath() + "\\update\\history";
		  try{
			  String installPath=YHSysProps.getRootPath();
			  String contextPath="yh";
			  String dest="";
			  File file= new File(filePath);
			  if (!file.isDirectory()) {
				  System.out.println(file.getName());
			   } else if (file.isDirectory()) {
				  String[] filelist = file.list();
				  for (int i = 0; i < filelist.length; i++) {
                      File readfile = new File(filePath + "\\" + filelist[i]);
                      if (!readfile.isDirectory()) {
                    	 String extName = YHFileUtility.getFileExtName(readfile.getName());
                    	 String abspath=readfile.getPath();
                        String  abspath2= abspath.substring(0, abspath.length()-readfile.getName().length()-1);
                    	  if("sql".equals(extName)){
                    		  try{
                    			  config.exeSql(abspath);
                    		  }catch(Exception e){
                    			  e.printStackTrace();
                    		  }
                    	  }else if("properties".equals(extName)){
                    		  try{
                    			  if("update".equals(readfile.getParent().substring(49))){
                    			  config.updateSysConfByFileName(installPath, contextPath, abspath,readfile.getName());
                    			  }else if("delete".equals(readfile.getParent().substring(49))){
                    				  config.deleteSysConfByFileName(installPath, contextPath, abspath,readfile.getName());
                    			  }
                    		  }catch(Exception e){
                    			  e.printStackTrace();
                    		  }
                    	  }else if("desc.xml".equals(readfile.getName())){
                    		  continue;
                    	  }
                    	  else {
                    		  try{
                    			  String destPath = installPath +"\\webroot\\" + contextPath + abspath2.substring(46) ;
                    			  YHFileUtility.copyDir(abspath2, destPath);
                    		  }catch(Exception e){
                    			  e.printStackTrace();
                    		  }
                    	  }
                     } else if (readfile.isDirectory()) {
                              updateSystem(filePath + "\\" + filelist[i]);
                      }
				  }
			   }
			  YHFileUtility.copyDir(updateDir,historyDir);
			  YHFileUtility.deleteAll(filePath);
			  return true;
			  } catch (FileNotFoundException e) {
			  System.out.println("readfile()   Exception:" + e.getMessage());
			  }
		  return false;
	  }
	  /*
	   * 更新客户端更新状态
	   * 
	   */
	  public void updateClientStatus(Connection dbConn,int seqId,String updateUser) throws Exception{
		    int versionNum=this.getVersionNum(dbConn, seqId);
		    String doneTime=YHUtility.getCurDateTimeStr();
		    Date time=YHUtility.parseDate(doneTime);
		    YHUpdateClientLog clientLog=new YHUpdateClientLog();
		    YHORM orm = new YHORM();
		    try {
		    	  	clientLog.setSeqId(seqId);
				    clientLog.setDoneTime(time);
				    clientLog.setUpdateStatus("2");
				    clientLog.setUpdateUser(updateUser);
				    clientLog.setToVersion(versionNum);
				    orm.updateSingle(dbConn, clientLog);
	        } catch (Exception ex) {
	          throw ex;
	        } 
	      this.updateVersion(dbConn, versionNum);
	  }
	  /*
	   * 
	   * 获取版本号
	   */
	  
	  public int getVersionNum(Connection dbConn,int seqId)throws Exception{ //获取版本号
		    Statement stmt = null;
		    ResultSet rs = null;
		    int versionNum=0;
		    String sql="select TO_VERSION from update_client_log where SEQ_ID = "+seqId;
		    try{
		    	stmt = dbConn.createStatement();
		    	rs=stmt.executeQuery(sql);
		    	while(rs.next()){
		    		versionNum=rs.getInt(1);
		    	}
		    	return versionNum;
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
		    return 0;
	  }
	  
	  /*
	   * 
	   * 更新版本号
	   */
	  public void updateVersion(Connection dbConn,int versionNum)throws Exception{
		    Statement stmt = null;
		    ResultSet rs = null;
		    String sql="update version set VERSION_NUM="+versionNum+" where VER='2.2.110412.1'";
		    try{
		    	stmt=dbConn.createStatement();
		    	stmt.executeUpdate(sql);
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
	  }
	  /*
	   * 
	   * 获取客户端更新状态
	   */
	  public String getStatus(Connection dbConn,int seqId)throws Exception{
		  Statement stmt=null;
		  ResultSet rs = null;
		  String status=null;
		  String sql="select UPDATE_STATUS from update_client_log where SEQ_ID = "+seqId;
		  try{
			  stmt=dbConn.createStatement();
			  rs=stmt.executeQuery(sql);
			  while(rs.next()){
				  status=rs.getString(1);
			  }
			  return status;
		  }catch(Exception e){
			  e.printStackTrace();
		  }
		  return null;
	  }

	  /*
	   * 升级成功，生成XML文件
	   */
	  public static String toXML(String clientGuid, int logSeqId, String updateStatus,String clientName) {

	    String str = "<?xml version='1.0' encoding='UTF-8'?>" + "<body>" + "<guid>"
	        + clientGuid + "</guid>" + "<logSeqId>" + logSeqId + "</logSeqId>"
	        + "<updateStatus>" + updateStatus + "</updateStatus>"
	        + "<clientName>" + clientName + "</clientName>"
	        + "</body>";
	    return str;
	  }
	  
	  /*
	   * 成功升级，返回信息
	   */
	public void backSuccessInfo(YHUpdateClientLog clientLog,String webroot , Connection dbConn)throws Exception {
		 if(!YHUtility.isNullorEmpty(clientLog.getUpdateUser())){
		        String xml = toXML(clientLog.getGuid(),clientLog.getLogSeqId(),clientLog.getUpdateStatus(),clientLog.getUpdateUser());
		        //此次打包任务名称
		        String taskName = YHGuid.getRawGuid();
		        String path = YHSysProps.getAttachPath() + File.separator + "uploadJTGW"
		            + File.separator + taskName ;
		        String FileName = "JH_UPDATE_" + YHGuid.getRawGuid() + ".xml";
		        while (YHDocUtil.getExist(path, FileName)) {
		          FileName = "JH_UPDATE_" + YHGuid.getRawGuid() + ".xml";
		        }
		        YHEsbClientConfig config = YHEsbClientConfig.builder(webroot + YHEsbConst.CONFIG_PATH) ;
		        YHEsbService esbService = new YHEsbService();
		        String fileName  =path + File.separator + FileName;
		        YHFileUtility.storeString2File(fileName, xml);
		        esbService.send (fileName, "client", config.getToken(),"JHupdate", "");
		      }
		
	}
	
}
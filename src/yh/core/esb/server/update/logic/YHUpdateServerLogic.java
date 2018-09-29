package yh.core.esb.server.update.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.agile.zip.CZipInputStream;
import com.agile.zip.ZipEntry;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.esb.client.data.YHExtDept;
import yh.core.esb.client.logic.YHDeptTreeLogic;
import yh.core.esb.server.update.data.YHUpdateLogDetl;
import yh.core.esb.server.update.data.YHUpdateServerLog;
import yh.core.funcs.calendar.logic.YHCalendarLogic;
import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.jtgwjh.docSend.data.YHJhDocsendTasks;
import yh.user.api.core.db.YHDbconnWrap;


public class YHUpdateServerLogic{

	public String getJsonLogic(Connection dbConn, Map parameterMap,YHPerson person, HttpServletRequest request)throws Exception {
	    try {
		      String sql = "select SEQ_ID,UPDATE_DESC,TO_VERSION,DEPLOY_USER,DEPLOY_TIME  from update_server_log  order by DEPLOY_TIME desc "; 
		      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(parameterMap);
		      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
		      return pageDataList.toJson();
		    } catch (Exception e) {
		      throw e;
		    }
	}
	
	  /**
	   * 新建
	   * 
	   * @param dbConn
	   * @param code
	   * @return
	   * @throws Exception
	   */
	  public static int add(Connection dbConn, YHUpdateLogDetl code) throws Exception {
	    YHORM orm = new YHORM();
	    orm.saveSingle(dbConn, code);
	    return YHCalendarLogic.getMaSeqId(dbConn, "UPDATE_LOG_DETL");
	  }

	public int addServerLog(Connection dbConn,YHPerson user) throws Exception {
		 YHORM orm = new YHORM();
		try{
			YHUpdateServerLog serverLog= new YHUpdateServerLog();
			String deployTime = YHUtility.getCurDateTimeStr();
			serverLog.setDeployUser(user.getUserName());
			int toVersion=this.getVersion(dbConn);
			serverLog.setToVersion(toVersion);
			serverLog.setDeployTime(YHUtility.parseDate(deployTime));
			serverLog.setUpdateDesc("系统升级");
			orm.saveSingle(dbConn, serverLog);
			int seqId =this.getMaxServerId(dbConn);
			return seqId;
		}catch(Exception e ){
			e.printStackTrace();
		}
		return 0;
	}

public int getVersion(Connection dbConn) throws Exception{
	int  result =  0 ;
    String sql = " select VERSION_NUM from version";
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        int versionNum = rs.getInt(1);
        if(versionNum != 0){
          result = versionNum;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
}

public int getMaxServerId(Connection conn)throws Exception{
	int  result =  0 ;
    String sql = " select max(SEQ_ID) from update_server_log";
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        int maxId = rs.getInt(1);
        if(maxId != 0){
          result = maxId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
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
          StringBuffer attIdBuffer = new StringBuffer();
          StringBuffer attNameBuffer = new StringBuffer();
          YHUpdateServerLog serverLog = (YHUpdateServerLog) orm.loadObjSingle(dbConn, YHUpdateServerLog.class, Integer.parseInt(seqId));   
          // 删除数据库信息
          this.deleteLogDetl(dbConn, Integer.parseInt(seqId));
          orm.deleteSingle(dbConn, serverLog);
          tempReturn += serverLog.getUpdateDesc()+",";
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

public void deleteLogDetl(Connection conn,int seqId)throws Exception{
    Statement stmt = null;
    int rs=0;
    String sql="delete from update_log_detl where LOG_SEQ_ID = "+seqId;
	try{
		stmt=conn.createStatement();
		rs=stmt.executeUpdate(sql);
	}catch(Exception e){
		e.printStackTrace();
	}
}
public static List<YHUpdateLogDetl> select(Connection dbConn, String[] str)
	      throws Exception {
	    YHORM orm = new YHORM();
	    List<YHUpdateLogDetl> codeList = new ArrayList<YHUpdateLogDetl>();
	    codeList = orm.loadListSingle(dbConn, YHUpdateLogDetl.class, str);
	    return codeList;
	  }

public static void paseXML(String filePath, Connection dbConn, String from,String guid) throws DocumentException {
    SAXReader saxReader = new SAXReader();
    File XMLFile = new File(filePath);
    Document document = saxReader.read(XMLFile);
    Element root = document.getRootElement();
    if(root.getName().equals("body")){
      YHUpdateLogDetl logDetail = new YHUpdateLogDetl();
      List<Element> elements = root.elements();
      for (Element el : elements) {
        String elName = el.getName();
        String elData = (String) el.getData() == null ? "" : (String) el.getData();
        elData = elData.trim();
        
        if (elName.equalsIgnoreCase("guid")) {
        	logDetail.setClientGuid(elData);
        } 
        else if (elName.equalsIgnoreCase("clientName")) {
        	logDetail.setClientName(elData);
        }
      }
      try {
        YHDbconnWrap dbUtil = new YHDbconnWrap();
        dbConn = dbUtil.getSysDbConn();
        YHORM orm = new YHORM();
        Map<String, String> filters = new HashMap<String, String>();
        filters.put("CLIENT_GUID", logDetail.getClientGuid());
        YHDeptTreeLogic dtl = new YHDeptTreeLogic();
        YHExtDept ed = dtl.getDeptByEsbUser(dbConn, from);//根据发送部门，查询组织机构
        if(ed != null){
          filters.put("CLIENT_NAME", ed.getDeptName());
          YHUpdateLogDetl logDetailTemp = (YHUpdateLogDetl) orm.loadObjSingle(dbConn, YHUpdateLogDetl.class, filters);
          if(logDetailTemp != null){
        	  logDetailTemp.setUpdateStatus("3");//对方已经升级成功
        	  orm.updateSingle(dbConn, logDetailTemp);
        	  dbConn.commit();
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        YHDBUtility.closeDbConn(dbConn, null);
      }
    }
	
	}
public static String updateUpdateStatus(Connection dbConn, String guid, int state, String to) throws Exception{
    YHORM orm = new YHORM();
    Map<String, String> filters = new HashMap<String, String>();
    filters.put("CLIENT_GUID", guid);
    YHUpdateLogDetl logDetl = null;
    List<YHUpdateLogDetl> logDetlList = null;
    if(YHUtility.isNullorEmpty(to)){
    	logDetlList =  orm.loadListSingle(dbConn, YHUpdateLogDetl.class, filters);
    }
    else{
      
      YHDeptTreeLogic dtl = new YHDeptTreeLogic();
      YHExtDept ed = dtl.getDeptByEsbUser(dbConn, to);//根据发送部门，查询组织机构
      if(ed != null){
        filters.put("CLIENT_NAME",ed.getDeptName());
        logDetl = (YHUpdateLogDetl) orm.loadObjSingle(dbConn, YHUpdateLogDetl.class, filters);
      }
    }
    
    if(logDetl != null || logDetlList !=null){
      switch(state){
        case 1 : {
          //修改task任务为发送完毕
          for(YHUpdateLogDetl logDetlTemp : logDetlList){
        	  logDetlTemp.setUpdateStatus("3");//文件已发送到服务器
              orm.updateSingle(dbConn, logDetlTemp);
          }
          break;
        }
        case 2 : {
          //系统更新状态为对方已接收
          logDetl.setUpdateStatus("2");//对方已接收
          orm.updateSingle(dbConn, logDetl);
          dbConn.commit();
          break;
        }
        case -3 : {
          //修改task任务为对方接收失败
          logDetl.setUpdateStatus("-3");//对方接收失败
          orm.updateSingle(dbConn, logDetl);
          dbConn.commit();
          break;
        }
        default : {
          if(logDetl != null){
            //修改task任务为发送失败
            logDetl.setUpdateStatus("-1");//发送失败
            orm.updateSingle(dbConn, logDetl);
          }
          else{
            for(YHUpdateLogDetl logDetlTemp : logDetlList){
            	logDetlTemp.setUpdateStatus("-1");//发送失败
              orm.updateSingle(dbConn, logDetlTemp);
            }
          }
          break;
        }
      }
      return "1";
    }
    else{
      return "";
    }
  }
}
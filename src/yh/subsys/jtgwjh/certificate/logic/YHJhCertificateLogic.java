package yh.subsys.jtgwjh.certificate.logic;

import java.io.File;
import java.io.FileInputStream;
import java.security.cert.CertificateFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.security.*;
import java.io.*;
import java.util.*;
import java.security.*;
import java.security.cert.*;
import sun.security.x509.*;
import java.security.cert.Certificate;
import javax.servlet.http.HttpServletRequest;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.esb.client.data.YHExtDept;
import yh.core.esb.client.logic.YHDeptTreeLogic;
import yh.core.esb.server.update.data.YHUpdateServerLog;
import yh.core.funcs.person.data.YHPerson;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.jtgwjh.certificate.data.YHJhCertificate;




public class YHJhCertificateLogic{

		public String getJsonLogic(Connection dbConn, Map parameterMap,YHPerson person, HttpServletRequest request)throws Exception {
		    try {
			      String sql = "select SEQ_ID,ORG_GUID,ORG_NAME,CERT,STOP_USE,BUILD_TIME  from jh_certificate  order by BUILD_TIME desc "; 
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
		          YHJhCertificate certInfo = (YHJhCertificate) orm.loadObjSingle(dbConn, YHJhCertificate.class, Integer.parseInt(seqId));   
		          // 删除数据库信息
		          orm.deleteSingle(dbConn, certInfo);
		          tempReturn += certInfo.getCert()+",";
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


		public boolean parseFile(String certPath, Connection dbConn)throws Exception  {
			YHJhCertificate cert=new YHJhCertificate();
			YHORM orm = new YHORM();
			try{
			File certFile= new File(certPath);
			String fileName=certFile.getName();
			CertificateFactory cf=CertificateFactory.getInstance("X.509");//从文件读取证书
			FileInputStream in=new FileInputStream(certPath);
			Certificate certificate=cf.generateCertificate(in);
			String certContent=certificate.toString();
			String extName= YHFileUtility.getFileExtName(fileName);
			String orgName=fileName.substring(0,fileName.length()-extName.length()-1);
			//根据单位名称查询外部组织结构
			String curTime=YHUtility.getCurDateTimeStr();
			YHDeptTreeLogic dtl = new YHDeptTreeLogic();
	        String orgGuid = dtl.getDeptGuidByName(dbConn,orgName);
	        
	        int seqId=this.getMaxId(dbConn);
	        if(seqId==0){
		        cert.setOrgGuid(orgGuid);
		        cert.setOrgName(orgName);
		        cert.setBuildTime(YHUtility.parseDate(curTime));
		        cert.setCert(certContent);
		        cert.setStopUse("0");
				orm.saveSingle(dbConn, cert);
	        }else{
	        	this.updateCert(dbConn,seqId);
		        cert.setOrgGuid(orgGuid);
		        cert.setOrgName(orgName);
		        cert.setBuildTime(YHUtility.parseDate(curTime));
		        cert.setCert(certContent);
		        cert.setStopUse("0");
				orm.saveSingle(dbConn, cert);
	        }
	    	return true;
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
		}
		
		
		public int getMaxId(Connection dbConn)throws Exception{
			int result=0;
			String sql="select max(SEQ_ID) from jh_certificate";
			PreparedStatement ps = null;
		    ResultSet rs = null ;
		    try {
		      ps = dbConn.prepareStatement(sql);
		      rs = ps.executeQuery();
		      if(rs.next()){
		        result = rs.getInt(1);
		      }
		    } catch (Exception e) {
		      throw e;
		    } finally {
		      YHDBUtility.close(ps, rs, null);
		    }
		    return result;
			
		}
		
		public void updateCert(Connection dbConn, int seqId)throws Exception{
			YHJhCertificate cert=new YHJhCertificate();
			cert.setSeqId(seqId);
			cert.setStopUse("1");
			YHORM orm= new YHORM();
			try{
				orm.updateSingle(dbConn, cert);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
}
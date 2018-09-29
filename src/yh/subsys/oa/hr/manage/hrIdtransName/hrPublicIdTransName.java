package yh.subsys.oa.hr.manage.hrIdtransName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class hrPublicIdTransName {
	 //id 转化为name
	 public static String getUserName(Connection dbConn, int seqId) throws Exception{ 
		 String sql = "select USER_NAME from person dr where dr.SEQ_ID=" + seqId; 
		 PreparedStatement ps = null; 
		 ResultSet rs = null; 
		 try{ 
				 ps = dbConn.prepareStatement(sql); 
				 rs = ps.executeQuery(); 
			 if(rs.next()){ 
			   return rs.getString("USER_NAME"); 
			 } 
		 } catch (Exception e){ 
		     throw e; 
		 }finally{ 
		     YHDBUtility.close(ps, null, null); 
		 } 
		     return null; 
	 }
	/**
	 * 获得码表id 对应的名称
	 * @param dbConn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	 public static String getCodeUserName(Connection dbConn, String seqId) throws Exception{
	   if (YHUtility.isNullorEmpty(seqId)) {
	     return "";
	   }
		 String sql = "select CODE_NAME from oa_pm_code where SEQ_ID='" + seqId + "'"; 
		 PreparedStatement ps = null; 
		 ResultSet rs = null; 
		 try{ 
				 ps = dbConn.prepareStatement(sql); 
				 rs = ps.executeQuery(); 
			 if(rs.next()){ 
			   return rs.getString("CODE_NAME"); 
			 } 
		 } catch (Exception e){ 
		     throw e; 
		 }finally{ 
		     YHDBUtility.close(ps, null, null); 
		 } 
		     return null; 
	 }
	
	 
}

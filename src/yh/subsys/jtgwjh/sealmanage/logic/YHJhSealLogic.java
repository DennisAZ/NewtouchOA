package yh.subsys.jtgwjh.sealmanage.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.subsys.jtgwjh.sealmanage.data.YHJhSeal;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHJhSealLogic {
  private static Logger log = Logger.getLogger(YHJhSealLogic.class);

  public String getSealList(Connection conn,Map request) throws Exception{
    String sql =  "select SEQ_ID" +
                  ",SEAL_ID" +
                  ",SEAL_NAME" +
                  ",CERT_STR" +
                  ",USER_STR" +
                  ",IS_FLAG" +
                  ",DEPT_STR" +
                  ",CREATE_TIME from JH_SEAL";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    return pageDataList.toJson();
  }

  
  public String getSearchList(Connection conn,Map request, String sealId, String sealName, String beginTime, String endTime) throws Exception{
    
    String sql = "select SEQ_ID" +
                 ",SEAL_ID" +
                 ",SEAL_NAME" +
                 ",CERT_STR" +
                 ",USER_STR" +
                 ",IS_FLAG" +
                 ",DEPT_STR" +
                 ",CREATE_TIME from JH_SEAL where 1=1";
    if(!YHUtility.isNullorEmpty(sealId)){ 
      sql = sql + " and SEAL_ID like '%" + sealId + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(sealName)){ 
      sql = sql + " and SEAL_NAME like '%" + sealName + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(beginTime)){
      beginTime = YHDBUtility.getDateFilter("CREATE_TIME", beginTime, ">=");
      sql = sql+" and " + beginTime; 
    }else if(!YHUtility.isNullorEmpty(endTime)){
      endTime = YHDBUtility.getDateFilter("CREATE_TIME", endTime, "<=");
      sql = sql + " and " + endTime; 
    }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    return pageDataList.toJson();
  }
  
  /**
   * 取得用户名称
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  
  public String getUserNameLogic(Connection conn , String userId) throws Exception{
    String result = "";
    String sql = " select USER_NAME from PERSON where SEQ_ID in (" + userId +")";
    PreparedStatement ps = null;
    ResultSet rs = null ;
    String toId = "";
    try {
      ps = conn.prepareStatement(sql);
      //System.out.println(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        toId += rs.getString("USER_NAME") + ",";
        if(toId != null){
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 删除印章权限管理数据
   * @param conn
   * @param seqIds   PERSON表中的SEQ_ID串（以逗号为分隔）
   * @throws Exception
   */
  
  public void deleteSeal(Connection conn, String seqIds) throws Exception {
    String sql = "DELETE FROM JH_SEAL WHERE SEQ_ID IN(" + seqIds + ")";
    PreparedStatement pstmt = null;
    try {
      pstmt = conn.prepareStatement(sql);
      pstmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }
  }
  /**
   * 删除印章权限管理数据
   * @param conn
   * @param seqIds   PERSON表中的SEQ_ID串（以逗号为分隔）
   * @throws Exception
   */
  
  public String[]  selectSeal(Connection conn, String seqIds) throws Exception {
    String sql = "select seal_id,seal_name FROM JH_SEAL WHERE SEQ_ID IN(" + seqIds + ")";
    PreparedStatement pstmt = null;
    ResultSet rs = null ;
    String[] seal = new String[2];
    String sId = "";
    String sealName = "";
    try {
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while(rs.next()){
        sId = sId + rs.getString(1) + ",";
        sealName = sealName + rs.getString(2) + ",";
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }
    if(sId.endsWith(",")){
      sId = sId.substring(0, sId.length() -1);
    }
    if(sealName.endsWith(",")){
      sealName = sealName.substring(0, sealName.length() -1);
    }
    seal[0] = sId;
    seal[1] = sealName;
    return seal;
  }
  
  /**
   * 删除印章权限管理数据
   * @param conn
   * @throws Exception
   */
  
  public static void deleteAllSeal(Connection conn) throws Exception {
    String sql = "DELETE FROM JH_SEAL ";
    PreparedStatement pstmt = null;
    try {
      pstmt = conn.prepareStatement(sql);
      pstmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      conn.commit();
      YHDBUtility.close(pstmt, null, null);
     
    }
  }
  /**
   * 查询
   * @param conn
   * @param seqIds 
   * @throws Exception
   */
  
  public  static List<YHJhSeal>  selectSeal(Connection dbConn, String[] fileter) throws Exception {
    List<YHJhSeal> list = new ArrayList<YHJhSeal>();
    try {
     YHORM orm = new YHORM();
     list =  orm.loadListSingle(dbConn, YHJhSeal.class, fileter);
     return list;
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(null, null, null);
    }
  }
  
  public String getCounterLogic(Connection conn , String SEAL_ID) throws Exception{
    String result = "";
    String sql = " select max(SEAL_ID) from JH_SEAL where SEAL_ID like '" + SEAL_ID + "%'";
    //System.out.println(sql);
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      //System.out.println(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        String toId = rs.getString(1);
        //System.out.println(toId);
        if(!YHUtility.isNullorEmpty(toId)){
          int maxNum = Integer.parseInt(toId.substring(toId.length()-4, toId.length()))+1;
          String maxStr = String.valueOf(maxNum);
          //System.out.println(maxStr.length());
          if(maxStr.length() == 1){
            result = "000" + maxStr;
            //System.out.println(result+"KLKLK");
          }else if(maxStr.length() == 2){
            result = "00" + maxStr;
            //System.out.println(result+"CC");
          }else if(maxStr.length() == 3){
            result = "0" + maxStr;
          }else if(maxStr.length() == 4){
            result = maxStr;
          }
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    if(YHUtility.isNullorEmpty(result)){
      result = "0001";
    }
    return result;
  }
  
  public String getShowInfo(Connection conn , int seqId) throws Exception{
    String result = "";
    String sql = " select SEAL_DATA from JH_SEAL where SEQ_ID ="+seqId;
    PreparedStatement pstmt = null;
    ResultSet rs = null ;
    String toId = "";
    try {
      
      pstmt = conn.prepareStatement(sql);
      rs  = pstmt.executeQuery();
      
      while(rs.next()){
        result = rs.getString(1);
    
      }
     
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, log);
    }
    return result;
  }
  
  public String getSealId(Connection conn , int seqId) throws Exception{
    String result = "";
    String sql = " select SEAL_ID from JH_SEAL where SEQ_ID = " + seqId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }

  
  
  /**
   * 
   *  解析公文XML、并删除之前的印章，新增数据
   * upXMLPath: 临时公文XML存放路径
   * @param savePath
   *          :保存文件路径
   * @param fromUnit
   *          ：发送单位
   * @param dbConn
   *          ：数据库 fromUnit：发送单位 * guid:ESB数据交互平台发送的唯一标识
   * @throws Exception
   */
  public static void parseXML(String upXMLPath,Connection dbConn, String fromUnit, String guid) throws Exception {
    SAXReader saxReader = new SAXReader();
    File XMLFile = new File(upXMLPath);
    Document document = saxReader.read(XMLFile);
    Element root = document.getRootElement();
    if (!YHUtility.isNullorEmpty(root.getName()) && root.getName().equals("tasks")) {
      //先删除本地所有印章
      deleteAllSeal(dbConn);
      
      List<Element> elements = root.elements();//task对象
      for (Element node : elements) {
        YHJhSeal seal = new YHJhSeal();
        String seqIdName = node.element("seqId").getName() == null ? "" : node.element("seqId").getName();
        String seqIdValue = node.element("seqId").getText() == null ? "" : node.element("seqId").getText();
        String sealId = YHUtility.empty2Default(node.element("sealId").getText(), "");
        String deptId = YHUtility.empty2Default(node.element("deptId") .getText(), "");
        String sealName = YHUtility.empty2Default(node.element("sealName") .getText(), "");
        String certStr = YHUtility.empty2Default(node.element("certStr").getText(), "");

        String userStr = YHUtility.empty2Default(node.element("userStr").getText(), "");
        String sealData = YHUtility.empty2Default(node.element("sealData") .getText(), "");
        String createTime = YHUtility.empty2Default(node.element("createTime").getText(), "");
        String deptStr = YHUtility.empty2Default(node.element("deptStr") .getText(), "");
        String isFlag = YHUtility.empty2Default(node.element("isFlag") .getText(), "");
        
        String outDeptId = YHUtility.empty2Default(node.element("outDeptId") .getText(), "");
        String outDeptName = YHUtility.empty2Default(node.element("outDeptName") .getText(), "");
      seal.setSealId(sealId);
      seal.setDeptId(deptId);
      seal.setDeptStr(deptStr);
      seal.setSealData(sealData);
      seal.setSealName(sealName);
      seal.setUserStr(userStr);
      seal.setIsFlag(isFlag);
      seal.setCreateTime(new Date());
      seal.setOutDeptId(outDeptId);
      seal.setOutDeptName(outDeptName);
      seal.setCertStr(certStr);
      addSeal(dbConn,seal);
      }
    }

  }
  
  /**
   * 新增
   * @param dbConn
   * @param seal
   * @throws Exception
   */
  public static void addSeal(Connection dbConn , YHJhSeal seal) throws Exception{
    try {
     YHORM orm = new YHORM();
     orm.saveSingle(dbConn, seal);
    } catch (Exception e) {
      throw e;
    } finally {
      dbConn.commit();
      YHDBUtility.close(null, null, null);
    }
  }
  
  /**
   * 启用或停用
   * @param conn
   * @throws Exception
   */
  
  public static void updateIsFlag(Connection conn,String seqId, String isFlag) throws Exception {
    String sql = "update  JH_SEAL set IS_FLAG = '" + isFlag + "' where seq_id =" + seqId;
    PreparedStatement pstmt = null;
    try {
      pstmt = conn.prepareStatement(sql);
      pstmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }
  }
  
  public String getSealName(Connection dbConn , String seqId) throws Exception{
	    String result = "";
	    String sql = " select SEAL_NAME from JH_SEAL where SEQ_ID = '" + seqId + "'";
	    PreparedStatement ps = null;
	    ResultSet rs = null ;
	    try {
	      ps = dbConn.prepareStatement(sql);
	      rs = ps.executeQuery();
	      if(rs.next()){
	        String toId = rs.getString(1);
	        if(toId != null){
	          result = toId;
	        }
	      }
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      YHDBUtility.close(ps, rs, log);
	    }
	    return result;
	  }


public YHJhSeal getSeal(Connection dbConn ,String seqId) throws Exception{
	YHJhSeal seal = new YHJhSeal();
	 String sql = " select * from JH_SEAL where SEQ_ID = '" + seqId + "'";
	    PreparedStatement ps = null;
	    ResultSet rs = null ;
	    try {
	      ps = dbConn.prepareStatement(sql);
	      rs = ps.executeQuery();
	      if(rs.next()){
	    	  seal.setSealId(rs.getString("SEAL_ID"));
	    	  seal.setSealName(rs.getString("SEAL_NAME"));
	    	  seal.setDeptStr(rs.getString("DEPT_STR"));
	      }
	    }catch (Exception e) {
		      throw e;
		    } finally {
		      YHDBUtility.close(ps, rs, log);
		    }
	return seal;
}
}

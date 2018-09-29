package yh.subsys.inforesouce.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.subsys.inforesouce.data.YHKengine;
import yh.subsys.inforesouce.util.YHFileMateConstUtil;

/**
 * 查找文件的主题词，组织机构名，人名，地名等
 * @author qwx110
 *
 */
public class YHAFileMateLogic{
  
  
  /**
   * 把相关的人名,地名，组织机构存放到集合中。
   * @param seqId
   * @param userName
   * @param areaName
   * @param org
   * @param subJect
   * @param keyWord
   * @param dbConn
   * @return
   * @throws Exception
   */
  public YHKengine findString(int seqId,String userName,String areaName,String org,String subJect,String keyWord, Connection dbConn)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;    
    YHKengine ki = new YHKengine();
    try{
         // 查询出所有的人名，地名..... 组织好串 返回到页面 ok
      if(! YHUtility.isNullorEmpty(userName)){
        String nameString = findMate(seqId,userName,dbConn);
        ki.setUserName(nameString);
      }if(! YHUtility.isNullorEmpty(areaName)){
        String areaString = findMate(seqId,areaName,dbConn);
        ki.setAreaName(areaString);
      }if(! YHUtility.isNullorEmpty(org)){
        String orgString = findMate(seqId,org,dbConn);
        ki.setOrgName(orgString);
      }if(! YHUtility.isNullorEmpty(subJect)){
        String subjectString = findMate(seqId,subJect,dbConn);
        ki.setSubJect(subjectString);
      }if(! YHUtility.isNullorEmpty(keyWord)){
        String keyWordString = findMate(seqId,keyWord,dbConn);
        ki.setKeyWord(keyWordString);
      }
       
    }catch(SQLException ex){
      throw ex;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return ki;
  }
  
  
  /**
   * 查询相关的人名，地名，组织机构名...
   * @param seqId
   * @param fileString
   * @param dbConn
   * @return
   * @throws Exception
   */
  public String findMate(int seqId, String fileString,Connection dbConn) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String attrString = null;
    String findSql = null;
    try{
      int number = YHFileMateConstUtil.checkString(fileString);
      String nums = null;
      String fileTable = null;
      if(number>100){ // 从file_attrs02 表查询 
        nums = "" + number;
        fileTable = "oa_file_attrs02";
      }else {         //从file_attrs01 表查询 
        fileTable = "oa_file_attrs01";
        if(number<10){
          nums = "00"+number;
        } else{
          nums = "0"+number;
        }
      }
      findSql = "select seq_id, attr_"+nums+" from "+ fileTable +" where file_seq_id ="+seqId;
      ps = dbConn.prepareStatement(findSql);
      rs = ps.executeQuery();
      while(rs.next()){
        int seqIds = rs.getInt("seq_id");
        attrString = rs.getString("attr_"+nums+"");
      }
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
     return attrString;
   }
  
  /**
   * 返回文件名，和moudle值
   * @param dbConn
   * @param fileId
   * @return
   * @throws Exception
   */
  public  String findFileNameAndMoudle(Connection dbConn, String fileId)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql ="select FILE_PATH,MODULE_NO from oa_seal_attach where FILE_ID ='"+ fileId +"'";
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    StringBuffer sb = new StringBuffer();
    sb.append("{");
    if(rs.next()){
      sb.append("\"fileName\":").append("\"").append(YHUtility.encodeSpecial(paraFilePath(rs.getString("FILE_PATH"), fileId))).append("\"").append(",");
      sb.append("\"moudle\":").append("\"").append(rs.getString("MODULE_NO")).append("\"");
    }
    sb.append("}");   
    return sb.toString();
  }
 
  /**
   * 从文件路径中摘除文件名
   * @param filePath
   * @param fileId
   * @return
   */
  public static String paraFilePath(String filePath, String fileId){
    fileId = fileId.replace("_", "\\");
    if(!YHUtility.isNullorEmpty(filePath)){
      filePath = filePath.substring(filePath.indexOf(fileId)+fileId.length()+1, filePath.length());
      return filePath;
    }else{
      return "";
    }
  }
  /**
   * 返回人名，地名，组织机构名
   * @param dbConn
   * @param pName
   * @param addrName
   * @param orgName
   * @return
   * @throws Exception
   */
  public Map<String, String> findAFileMate(Connection dbConn, String pName, String addrName, String orgName)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql ="select  NUMBER_ID, CHNAME from oa_mate_kind where CHNAME = '"+ pName +"' or CHNAME = '"+ addrName +"' or CHNAME = '"+ orgName +"'";
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    Map<String, String> name_numberId = new HashMap<String, String>();
    while(rs.next()){
      name_numberId.put(rs.getString("chname"), rs.getString("number_id"));
    }
    return name_numberId;
  }
  
  /**
   * 返回人名、地名，组织机构名（按照顺序）
   * @param dbConn
   * @param map
   * @param file_seq_id
   * @return
   * @throws Exception
   */
  public String mateJaon(Connection dbConn, Map<String, String> map, String attachmentId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = "select " + getColum(map) +" from oa_file_attrs01 a join ( select  SEQ_ID from oa_seal_attach where FILE_ID='"+ attachmentId +"') b on a.FILE_SEQ_ID=b.SEQ_ID";
    StringBuffer sb = new StringBuffer();
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    sb.append("{");
    if(rs.next()){
       sb.append("name").append(":").append("'").append(YHUtility.encodeSpecial(subAString(rs.getString(1)))).append("',");
       sb.append("area").append(":").append("'").append(YHUtility.encodeSpecial(subAString(rs.getString(2)))).append("',");
       sb.append("orge").append(":").append("'").append(YHUtility.encodeSpecial(subAString(rs.getString(3)))).append("'");
    }
    sb.append("}");
    return sb.toString();
  }
  /**
   * 主调用方法 返回人名、地名，组织机构名（按照顺序）
   * @param dbConn
   * @param file_seq_id
   * @return
   * @throws Exception
   */
  public String mateJson(Connection dbConn, String attrmentId)throws Exception{
    Map<String, String> map = findAFileMate(dbConn, YHFileMateConstUtil.userName, YHFileMateConstUtil.areaName, YHFileMateConstUtil.Org);
    String jsons = mateJaon(dbConn, map, attrmentId);
    return jsons;
  }
  
  
  /**
   * 返回人名、地名，机构名的sql串
   * @param map
   * @return
   */
  public static String  getColum(Map<String, String> map){
    String colums = "";
       colums += "a.attr_" + paramNumber(map.get(YHFileMateConstUtil.userName));
       colums += "," + "a.attr_" + paramNumber(map.get(YHFileMateConstUtil.areaName));
       colums += "," + "a.attr_" + paramNumber(map.get(YHFileMateConstUtil.Org));
    return colums;
  }
  
  
  public static String paramNumber(String num){
    num = num.replace("MEX", "").replace("M", "");
    String tk = "";
    int attr = Integer.parseInt(num);
    if(attr < 10){
      tk += "00"+attr;
    }else if(attr < 100 && attr >= 10){
      tk += "0"+attr;
    }
    return tk;
  }
  
  public String subAString(String befor){
    if(!YHUtility.isNullorEmpty(befor)){
      String[] after = befor.split(",");
      if(after.length < 3){
        return befor;
      }else{
        for(int i=0; i<3; i++){
          String temp= "";
          if(i < 2){
            temp += after[i] + ",";
          }else{
            temp += after[i];
          }
          return temp;
        }
      }
    }
    return null;
  }
  
  public static void main(String[] args) throws Exception{
   // String filePath= "D:\\yh\\attach\\email\\1005\\31545a4aaf1d6b274826a2a392146fd5_文档 9765.docx";
   // String fileId = "1005_31545a4aaf1d6b274826a2a392146fd5";
   // String name = paraFilePath(filePath, fileId);
   // System.out.println(name);
    
//    Connection dbConn = TestDbUtil.getConnection(false, "TD_OA2");
//    YHAFileMateLogic aLogic = new YHAFileMateLogic();
//    String attrId = "0902_ea7f31ab6a3ff3b029167161acb2580e";
//    String tt = aLogic.mateJson(dbConn, attrId);
//    System.out.println(tt);
  }
}

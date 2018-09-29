package yh.subsys.inforesouce.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import yh.core.util.YHReflectUtility;
import yh.subsys.inforesouce.data.YHImageManage;
import yh.subsys.inforesouce.data.YHKengine;
import yh.subsys.inforesouce.util.YHPageUtil;

/**
 * 图片管理中心
 * @author qwx110
 *
 */
public class YHManageImgLogic{
  
  /**
   * 获得所有的图片
   * @param dbConn
   * @param fileId
   * @param pu
   * @return
   * @throws Exception
   */
  public List<YHImageManage> findAllImage(Connection dbConn,  YHPageUtil page,  HttpServletRequest request) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql ="select  DISTINCT sf.RECORD_ID, sf.ABSTRACT, n.SUBJECT  from oa_seal_attach sf, oa_news n where sf.FILE_TYPE = 2 and sf.RECORD_ID = n.SEQ_ID order by sf.RECORD_ID desc";
    List<YHImageManage> imges = null;
    try{
      ps = dbConn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
      ps.setMaxRows(page.getCurrentPage() * page.getPageSize());
      rs = ps.executeQuery();
      rs.first();   
      rs.relative((page.getCurrentPage()-1) * page.getPageSize() -1); 
      
      imges = new ArrayList<YHImageManage>();
      while(rs.next()){
        YHImageManage img = new YHImageManage();
        img.setNewsId(Integer.parseInt(rs.getString("RECORD_ID")));
        img.setDescription(rs.getString("ABSTRACT"));
        img.setSubject(rs.getString("SUBJECT"));
        Map<String, String> map = getImagePath(dbConn, rs.getString("RECORD_ID"));
        String attachName = map.get("attachmentName");
        String attachId = map.get("attachmentId");
        img.setAttachmentId(attachId);
        img.setAttachmentName(attachName);
        img.setBaseContext(request.getContextPath());
        imges.add(img);
      }
    } catch (SQLException e){
      throw e;      
    }
    return imges;    
  }
  
  /**
   * 获取所有的图片的数量
   * @param dbConn
   * @return
   * @throws Exception
   */
  public int findImageAmount(Connection dbConn) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql ="select  count(distinct sf.record_id)  from oa_seal_attach sf, oa_news n where sf.file_type = 2 and sf.record_id = n.seq_id";
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        return rs.getInt(1);
      }
    } catch (SQLException e){
      throw e;      
    }
    return 0;    
  }
  
  /**
   * 返回文件的附件和附件id
   * @param newSeqId
   * @return
   * @throws Exception
   */
  public Map<String, String> getImagePath(Connection dbConn,String newSeqId)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql ="select ATTACHMENT_NAME, ATTACHMENT_ID from oa_news where SEQ_ID=" + newSeqId;
   
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        Map<String, String> paths = new HashMap<String, String>();
        paths.put("attachmentName", rs.getString("ATTACHMENT_NAME"));
        paths.put("attachmentId", rs.getString("ATTACHMENT_ID"));
        return paths;
      }
    } catch (SQLException e){
      throw e;      
    }
   return null;
  }
  
  private static String[] imageType = {"gif","jpg","jpeg","png","bmp","iff","jp2","jpx","jb2","jpc","xbm","wbmp"};
  public static boolean isImageType(String ext) {
    for (int i = 0;i < imageType.length ;i++ ) {
      String imageExt = imageType[i];
      if (imageExt.equals(ext)) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * 返回第一张图片
   * @param newSeqId
   * @return
   * @throws Exception 
   */
  public String findFirstPic(Connection dbConn,String newSeqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = "select SEQ_ID from oa_seal_attach where RECORD_ID=" + newSeqId;
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        return rs.getString("SEQ_ID");
      }
    } catch (SQLException e){
      throw e;
    }   
    return null;
  }

/**
 * 返回人名，地名，组织机构名
 * @param dbConn
 * @param newSeqId
 * @param pName
 * @param area
 * @param orgName
 * @return
 * @throws Exception
 */
public YHKengine findMate(Connection dbConn,String newSeqId, String pName, String area, String orgName) throws Exception {
      String className = "yh.subsys.inforesouce.logic.YHAFileMateLogic";
      String method = "findString";
      String seqId = findFirstPic(dbConn, newSeqId);
      int id = Integer.parseInt(seqId);
      String[] paramTypeNameArray = {"int", "java.lang.String", "java.lang.String","java.lang.String", "java.lang.String", "java.lang.String", "java.sql.Connection"};
      Object[] params = {id,pName,area,orgName,"","",dbConn};
      return (YHKengine) YHReflectUtility.exeMethod(className, method, paramTypeNameArray, params);
}


/**
 * 获得10张热点图片
 * @param dbConn
 * @param fileId
 * @param pu
 * @return
 * @throws Exception
 */
public List<YHImageManage> findTenImage(Connection dbConn, HttpServletRequest request) throws Exception{
  PreparedStatement ps = null;
  ResultSet rs = null;
  String sql ="select  DISTINCT sf.RECORD_ID, sf.ABSTRACT, n.SUBJECT  from oa_seal_attach sf, oa_news n where sf.FILE_TYPE = 2 and sf.RECORD_ID = n.SEQ_ID order by sf.RECORD_ID desc";
  List<YHImageManage> imges = null;
  try{
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    imges = new ArrayList<YHImageManage>();
    int cnt = 0;
    while(rs.next() && ++ cnt <= 10){
      YHImageManage img = new YHImageManage();
      img.setNewsId(Integer.parseInt(rs.getString("RECORD_ID")));
      img.setDescription(rs.getString("ABSTRACT"));
      img.setSubject(rs.getString("SUBJECT"));
      Map<String, String> map = getImagePath(dbConn, rs.getString("RECORD_ID"));
      String attachName = map.get("attachmentName");
      String attachId = map.get("attachmentId");
      img.setAttachmentId(attachId);
      img.setAttachmentName(attachName);
      img.setBaseContext(request.getContextPath());
      imges.add(img);
    }
  } catch (SQLException e){
    throw e;      
  }
  return imges;    
}
}

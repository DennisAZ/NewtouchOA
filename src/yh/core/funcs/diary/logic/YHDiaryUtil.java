package yh.core.funcs.diary.logic;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import org.apache.log4j.Logger;

import yh.core.funcs.diary.data.YHDiary;
import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

/**
 * 工作日志模块的工具类
 * @author TTlang
 *
 */
public class YHDiaryUtil{
  private static Logger log = Logger
  .getLogger("yh.core.funcs.diary.logic.YHDiaryUtil");
  /**
   * 去掉所有html标签
   * @param html
   * @return
   */
  public static String  cutHtml(String html){
    String result = "";
    if (!YHUtility.isNullorEmpty(html)) {
      result = html.replaceAll("(<[^/\\s][\\w]*)[\\s]*([^>]*)(>)", "$1$3").replaceAll("<[^>]*>", "");
    }
    return result;
  }
  
  public static String getRondom() throws NoSuchAlgorithmException{
    String result = "";
    //Random rand = new Random();
    //result = rand.nextLong();
    //System.out.println(result);
    result = YHGuid.getRawGuid();
    return result;
  }
  /**
   * 判断 文件是否存在
   * @param savePath
   * @param fileExtName
   * @return
   * @throws IOException
   */
  public static boolean getExist(String savePath,String fileExtName) throws IOException {
      String filePath = savePath + File.separator + fileExtName;
      if (new File(filePath).exists()) {
        return true;
      }
    return false;
  }
  /**
   * 取得用户名称
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
    public static String getUserNameLogic(Connection conn , int userId) throws Exception{
      String result = "";
      String sql = " select USER_NAME from PERSON where SEQ_ID = " + userId ;
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
        YHDBUtility.close(ps, rs, log);
      }
      return result;
    }
  
  /**
   * 取得用户名称
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
    public static Integer getDeptIdByUserId(Connection conn , int userId) throws Exception{
      Integer result = -1;
      String sql = " select DEPT_ID from PERSON where SEQ_ID = " + userId ;
      PreparedStatement ps = null;
      ResultSet rs = null ;
      try {
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();
        if(rs.next()){
          Integer toId = rs.getInt(1);
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
 * 取得部门名称
 * @param conn
 * @param deptId
 * @return
 * @throws Exception
 */
  public static String getDeptNameLogic(Connection conn , int deptId) throws Exception{
    String result = "";
    String sql = " select DEPT_NAME from oa_department where SEQ_ID = " + deptId ;
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
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  public static int writerDiary(Connection conn,int userId,String subject,String content) throws Exception{
    YHDiary dia = new YHDiary();
    dia.setSubject(subject);
    dia.setUserId(userId);
    dia.setContent(content);
    dia.setDiaDate(new Date());
    dia.setDiaTime(new Date());
    dia.setDiaType("1");
    YHORM orm = new YHORM();
    orm.saveSingle(conn, dia);
    int result = 0;
    result = getMaxSeqId(conn, "oa_journal");
    return result;
  }
  public static void updateDiary(Connection conn,int seqId,int userId,String subject,String content) throws Exception{
    YHDiary dia = new YHDiary();
    dia.setSeqId(seqId);
    dia.setSubject(subject);
    dia.setUserId(userId);
    dia.setContent(content);
    dia.setDiaTime(new Date());
    dia.setDiaType("1");
    YHORM orm = new YHORM();
    orm.updateSingle(conn, dia);
  }
  public static YHDiary getDiary(Connection conn,int seqId) throws Exception{
    YHDiary dia = null;
    YHORM orm = new YHORM();
    dia = (YHDiary) orm.loadObjSingle(conn, YHDiary.class, seqId);
    return dia;
  }
  /**
   * 取得最大的seqId
   * @param conn
   * @param tableName
   * @return
   * @throws Exception
   */
  public static int getMaxSeqId(Connection conn,String tableName) throws Exception{
    int result = 0;
    String sql = "select max(SEQ_ID) from " + tableName;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        result = rs.getInt(1);
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
}

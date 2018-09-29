package yh.subsys.infomgr.bilingual.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.infomgr.bilingual.data.YHBilingualCorrection;

public class YHCorrectionLogic {
        
  private static Logger log = Logger.getLogger(YHCorrectionLogic.class);
 
  /**
   * 增加一条记录
   * @param conn
   * @return
   * @throws Exception
   */
  public void addCorrection(Connection conn,YHBilingualCorrection bi) throws Exception{
    
    try{
      YHORM orm = new YHORM();
      orm.saveSingle(conn, bi);
    }catch(Exception ex) {
      throw ex;
    }finally {
    }
  }
  
  public void modifyCorrection(Connection conn,YHBilingualCorrection bi) throws Exception{
    
    try{
      YHORM orm = new YHORM();
      orm.updateSingle(conn, bi);
    }catch(Exception ex) {
      throw ex;
    }finally {
    }
  }
  
  /**
   * 删除一条记录
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public void deleteRecord(Connection conn, int seqId) throws Exception{
    try{
      YHORM orm = new YHORM();
      orm.deleteSingle(conn, YHBilingualCorrection.class, seqId);
    }catch(Exception ex) {
      throw ex;
    }finally {
    }
  }
  
  /**
   * 确认一条记录
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public void confirmRecord(Connection conn, int seqId) throws Exception{
    PreparedStatement ps = null;
    try{
      String sql = "update BILINGUAL_CORRECTION" +
      		" set FLAG = '1'" +
      		" where SEQ_ID = ?";
      
      ps = conn.prepareStatement(sql);
      ps.setInt(1, seqId);
      ps.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
    }
  }
  
  public YHBilingualCorrection queryRecord(Connection conn,int seqId) throws Exception{
    try{
      YHORM orm = new YHORM();
      return (YHBilingualCorrection)orm.loadObjSingle(conn, YHBilingualCorrection.class, seqId);
    }catch(Exception ex) {
      throw ex;
    }finally {
    }
  }
  
  public List<YHBilingualCorrection> getNotConfirmSqServer(Connection conn)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      
      String sql = "select SEQ_ID, LOCATION, PICTURE, CONTENT" +
                         " from BILINGUAL_CORRECTION" +
                         " where FLAG = '0'" +
                         " order by CORRECT_DATE desc ";
      //YHOut.println(sql);
      List<YHBilingualCorrection> correntionList = new ArrayList<YHBilingualCorrection>();
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      int cnt = 0;
      while(rs.next() && ++cnt <= 10){
        YHBilingualCorrection corr = new YHBilingualCorrection();
        corr.setSeqId(rs.getInt(1));
        corr.setLocation(rs.getString(2));
        corr.setPicture(rs.getString(3));
        corr.setContent(subString(30, rs.getString(4)));
        correntionList.add(corr);
      }
      return correntionList;
    } catch (Exception e){
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  public static String subString(int length, String str){
    if(!isEmpty(str)){
      if(str.length() > length){
      String strNew = str.substring(0, length);
      strNew = strNew +"....";
      return strNew;
      }else{
        return str;
     }
    }else{
      return "";
    }    
  } 
  
  /**
   * 判读字符串为空

   * @param flag
   * @return
   */
  public static boolean isEmpty(String flag){
    //YHOut.println(flag == " "+"*******");
    if(flag == null || flag.length()< 1){
      return true;
    }
    return false;
  }
  
}

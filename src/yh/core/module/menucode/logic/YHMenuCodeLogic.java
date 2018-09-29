package yh.core.module.menucode.logic;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import yh.core.funcs.email.data.YHEmailBox;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHMenuCodeLogic{

  /**
   * 取得FLOW_NAME
   * @param conn
   * @param seqId
   * @return
   * @throws Exception 
   */
  public String getFlowName(Connection dbConn ,int seqId) throws Exception{
    //SELECT  SEQ_ID FROM EMAIL_BODY WHERE SEQ_ID IN( SELECT BODY_ID FROM EMAIL WHERE BOX_ID = 0)
    String sql = " SELECT FLOW_NAME FROM oa_fl_type WHERE SEQ_ID=" + seqId;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String result = null;
    try{
      pstmt = dbConn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while(rs.next()){
        result = rs.getString(1);
      }
    } catch (Exception e){
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    return result;
  }
  
  public String getSortName(Connection dbConn ,int seqId) throws Exception{
    String sql = " SELECT SORT_NAME FROM oa_file_sort WHERE SEQ_ID=" + seqId;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String result = null;
    try{
      pstmt = dbConn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while(rs.next()){
        result = rs.getString(1);
      }
    } catch (Exception e){
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    return result;
  }
  
  public String getDiskName(Connection dbConn ,int seqId) throws Exception{
    String sql = " SELECT DISK_NAME FROM oa_net_storage WHERE SEQ_ID=" + seqId;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String result = null;
    try{
      pstmt = dbConn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while(rs.next()){
        result = rs.getString(1);
      }
    } catch (Exception e){
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    return result;
  }
  
  public String getPicName(Connection dbConn ,int seqId) throws Exception{
    String sql = " SELECT PIC_NAME FROM oa_picture WHERE SEQ_ID=" + seqId;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String result = null;
    try{
      pstmt = dbConn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while(rs.next()){
        result = rs.getString(1);
      }
    } catch (Exception e){
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    return result;
  }
  
  public String getConfidentialFile(Connection dbConn ,int seqId) throws Exception{
    String sql = " SELECT SORT_NAME FROM oa_secret_sort WHERE SEQ_ID=" + seqId;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String result = null;
    try{
      pstmt = dbConn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while(rs.next()){
        result = rs.getString(1);
      }
    } catch (Exception e){
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    return result;
  }
}

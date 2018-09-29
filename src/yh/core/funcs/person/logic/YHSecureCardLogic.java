package yh.core.funcs.person.logic;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHSecureKey;
import yh.core.global.YHConst;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;

public class YHSecureCardLogic {

  
  public boolean isExist(Connection conn , String keySn) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    try {
      String queryStr = "select 1 from oa_secure_key where KEY_SN = '"+keySn+"'"; 
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      if(rs.next()){      
        return true;
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return false;
  }
  
  /**
   * 动态密保卡 通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getSecureCard(Connection dbConn, Map request, YHPerson person) throws Exception {
    try {
      String sql = " SELECT sk.SEQ_ID, sk.KEY_SN, p.USER_ID, p.USER_NAME, d.DEPT_NAME, sk.KEY_INFO "
                 + " FROM oa_secure_key sk "
                 + " LEFT JOIN person p ON p.KEY_SN = sk.KEY_SN "
                 + " LEFT JOIN oa_department d ON d.SEQ_ID = p.DEPT_ID ";
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  public void bindUser(Connection conn, String userId, String keySn) throws Exception{
    Statement stmt = null;
    try {
      String queryStr = " update person set KEY_SN = '"+keySn+"' where SEQ_ID ="+userId; 
      stmt = conn.createStatement();
      stmt.executeUpdate(queryStr);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, null);
    }
  }
  
  public void deleteSecureCard(Connection conn, String seqIdStrs) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    if (YHUtility.isNullorEmpty(seqIdStrs)) {
      seqIdStrs = "";
    }
    try {
      String seqIdArry[] = seqIdStrs.split(",");
      if (!"".equals(seqIdArry) && seqIdArry.length > 0) {
        for (String seqId : seqIdArry) {
          String userId = "0";
          String queryStr = " SELECT SEQ_ID FROM person p where KEY_SN = (SELECT KEY_SN FROM oa_secure_key s WHERE s.SEQ_ID = "+seqId+") "; 
          stmt = conn.createStatement();
          rs = stmt.executeQuery(queryStr);
          if(rs.next()){
            userId = rs.getString("SEQ_ID");
          }
          queryStr = " update person set KEY_SN = '' where SEQ_ID ="+userId; 
          stmt = conn.createStatement();
          stmt.executeUpdate(queryStr);
          
          queryStr = " delete from oa_secure_key where SEQ_ID ="+seqId; 
          stmt = conn.createStatement();
          stmt.executeUpdate(queryStr);
        }
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, null);
    }
  }
  
  
  public void unBindSecureCard(Connection conn, String seqIdStrs) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    if (YHUtility.isNullorEmpty(seqIdStrs)) {
      seqIdStrs = "";
    }
    try {
      String seqIdArry[] = seqIdStrs.split(",");
      if (!"".equals(seqIdArry) && seqIdArry.length > 0) {
        for (String seqId : seqIdArry) {
          String userId = "0";
          String queryStr = " SELECT SEQ_ID FROM person p where KEY_SN = (SELECT KEY_SN FROM oa_secure_key s WHERE s.SEQ_ID = "+seqId+") "; 
          stmt = conn.createStatement();
          rs = stmt.executeQuery(queryStr);
          if(rs.next()){
            userId = rs.getString("SEQ_ID");
          }
          queryStr = " update person set KEY_SN = '' where SEQ_ID ="+userId; 
          stmt = conn.createStatement();
          stmt.executeUpdate(queryStr);
        }
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, null);
    }
  }
  
  public String getKeySn(Connection conn , String seqId) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    try {
      String queryStr = "select KEY_INFO from oa_secure_key where SEQ_ID = '"+seqId+"'"; 
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      if(rs.next()){      
        return rs.getString("KEY_INFO");
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return "";
  }
  
  public YHSecureKey getKeyInfo(Connection conn, YHPerson person) throws Exception{
    
    Statement stmt = null;
    ResultSet rs = null;
    int seqId = 0;
    if(person != null){
      seqId = person.getSeqId();
    }
    try {
      String queryStr = "select SEQ_ID , KEY_SN , KEY_INFO from oa_secure_key where KEY_SN = (select KEY_SN from person where seq_id = "+seqId+")"; 
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      if(rs.next()){  
        YHSecureKey secureKey = new YHSecureKey();
        secureKey.setSeqId(rs.getInt("SEQ_ID"));
        secureKey.setKeySn(rs.getString("KEY_SN"));
        secureKey.setKeyInfo(rs.getString("KEY_INFO"));
        return secureKey;
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return null; 
  }
  
  public void setKeyInfoByKeySn(Connection conn, YHSecureKey secureCard) throws Exception{
    Statement stmt = null;
    try {
      String queryStr = " update oa_secure_key set KEY_INFO = '"+secureCard.getKeyInfo()+"' where KEY_SN ="+secureCard.getKeySn(); 
      stmt = conn.createStatement();
      stmt.executeUpdate(queryStr);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, null);
    }
  }
  
  /**
   * 导入CSV批量绑定密码卡
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @param buffer
   * @return
   * @throws Exception
   */
  public int impSecureCardInfoToCsv(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
    Map<Object, Object> returnMap = new HashMap<Object, Object>();
    PreparedStatement stmt = null;
    String sql = "  ";
    int count = 0;
    try {
      InputStream is = fileForm.getInputStream();
      ArrayList<YHDbRecord> dbRecords = YHCSVUtil.CVSReader(is, YHConst.CSV_FILE_CODE);
      String userName = "";
      String keySn = "";
      for (YHDbRecord record : dbRecords) {
        int fieldCount = record.getFieldCnt();
        if (fieldCount > 0) {
          for (int i = 0; i < fieldCount; i++) {

            String keyName = record.getNameByIndex(i);
            String value = (String) record.getValueByIndex(i);
            if ("用户名".equals(keyName.trim())) {
              userName = value;
            }
            if("卡号".equals(keyName.trim())){
              keySn = value;
            }
            
            if(i%2 == 1){
              sql = " update person set KEY_SN = '"+keySn+"' where USER_NAME = '"+userName+"' ";
              stmt = dbConn.prepareStatement(sql);
              count = count + stmt.executeUpdate();
            }
          }
        }
      }
      return count;
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, null, null);
    }
  }
}

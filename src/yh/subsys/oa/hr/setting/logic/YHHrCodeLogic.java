package yh.subsys.oa.hr.setting.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.hr.setting.data.YHHrCode;

public class YHHrCodeLogic {
  private static Logger log = Logger
      .getLogger("yh.subsys.oa.hr.setting.logic.YHHrCodeLogic");

  /**
   * 新建
   * 
   * @param dbConn
   * @param code
   * @return
   * @throws Exception
   */
  public static int addCode(Connection dbConn, YHHrCode code) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, code);
    return 0;
  }

  /**
   * 查询ById
   * 
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static YHHrCode getCodeById(Connection dbConn, String seqId)
      throws Exception {
    try {
      YHORM orm = new YHORM();
      YHHrCode code = (YHHrCode) orm.loadObjSingle(dbConn, YHHrCode.class,
          Integer.parseInt(seqId));
      return code;
    } catch (NumberFormatException e) {
      return null;
    } catch (Exception e) {
      throw e;
    } finally {
      
    }
  }

  /**
   * 查询父级
   * 
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static List<YHHrCode> getCode(Connection dbConn, String[] str)
      throws Exception {
    YHORM orm = new YHORM();
    List<YHHrCode> codeList = new ArrayList<YHHrCode>();
    codeList = orm.loadListSingle(dbConn, YHHrCode.class, str);
    return codeList;
  }

  /**
   * 查询下一级
   * 
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static List<YHHrCode> getChildCode(Connection dbConn, String parentNo)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    List<YHHrCode> codeList = new ArrayList<YHHrCode>();
    if (YHUtility.isNullorEmpty(parentNo)) {
      parentNo = "";
    }
    parentNo = parentNo.replaceAll("'", "''");
    String sql = "SELECT * from oa_pm_code where parent_no in (select code_no from oa_pm_code where code_no = '"+ parentNo + "' and  (parent_no is null or parent_no ='')) order by CODE_ORDER ASC";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        YHHrCode code = new YHHrCode();
        code.setSeqId(rs.getInt("SEQ_ID"));
        code.setCodeNo(rs.getString("CODE_NO"));
        code.setCodeName(rs.getString("CODE_NAME"));
        code.setCodeOrder(rs.getString("code_order"));
        code.setCodeFlag(rs.getString("code_flag"));
        codeList.add(code);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return codeList;
  }

  /**
   * 删除ById
   * 
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void delCodeById(Connection dbConn, String seqId)
      throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHHrCode.class, Integer.parseInt(seqId));
  }

  /**
   * 检查代码编号有没有重复--父级
   * 
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static boolean checkCodeNo(Connection dbConn, String codeNo,
      String seqId) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    if (YHUtility.isNullorEmpty(codeNo)) {
      codeNo = "";
    }
    codeNo = codeNo.replaceAll("'", "''");
    String sql = "SELECT * from oa_pm_code where CODE_NO='" + codeNo
        + "' and (PARENT_NO='' or PARENT_NO is null)";
    if (YHUtility.isInteger(seqId)) {
      sql = sql + " and SEQ_ID <> " + seqId;
    }
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        return true;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return false;
  }

  /**
   * 检查代码编号有没有重复--子级
   * 
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static boolean checkCodeNo(Connection dbConn, String codeNo,
      String parentNo, String seqId) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    if (YHUtility.isNullorEmpty(codeNo)) {
      codeNo = "";
    }
    codeNo = codeNo.replaceAll("'", "''");
    if (YHUtility.isNullorEmpty(parentNo)) {
      parentNo = "";
    }
    parentNo = parentNo.replaceAll("'", "''");
    String sql = "SELECT * from oa_pm_code where CODE_NO='" + codeNo
        + "' and PARENT_NO='" + parentNo + "'";
    if (YHUtility.isInteger(seqId)) {
      sql = sql + " and SEQ_ID <> " + seqId;
    }
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        return true;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return false;
  }

  /**
   * 更新
   * 
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void updateCode(Connection dbConn, String seqId, String codeNo,
      String codeName, String codeOrder, String codeFlag) throws Exception {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    boolean b = false;
    String sql = "update oa_pm_code set CODE_NO = ?, CODE_NAME = ?,CODE_ORDER=?,CODE_FLAG = ? where SEQ_ID = "
        + seqId;
    try {
      pstmt = dbConn.prepareStatement(sql);
      pstmt.setString(1, codeNo);
      pstmt.setString(2, codeName);
      pstmt.setString(3, codeOrder);
      pstmt.setString(4, codeFlag);
      pstmt.executeUpdate();

    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(pstmt, rs, log);
    }
  }
  
  /**
   * 更新
   * 
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void updateChildCode(Connection dbConn, String oldCodeNo,String codeNo) throws Exception {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    boolean b = false;
    String sql = "update oa_pm_code set PARENT_NO = ? where PARENT_NO = ? ";
    try {
      pstmt = dbConn.prepareStatement(sql);
      pstmt.setString(1, codeNo);
      pstmt.setString(2, oldCodeNo);
      pstmt.executeUpdate();

    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(pstmt, rs, log);
    }
  }
}

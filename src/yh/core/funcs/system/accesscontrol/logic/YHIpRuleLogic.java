package yh.core.funcs.system.accesscontrol.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import yh.core.funcs.system.accesscontrol.data.YHIpRule;
import yh.core.util.db.YHDBUtility;

public class YHIpRuleLogic {
  private static Logger log = Logger.getLogger(YHIpRuleLogic.class);

  public ArrayList<YHIpRule> getIpRule(Connection dbConn) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHIpRule rule = null;
    List list = new ArrayList();
    ArrayList<YHIpRule> ruleList = new ArrayList<YHIpRule>();
    try {
      stmt = dbConn.createStatement();
      String sql = "select SEQ_ID, BEGIN_IP, END_IP, TYPE, REMARK from oa_iprule";
      rs = stmt.executeQuery(sql);
      while (rs.next()) { 
        rule = new YHIpRule();
        rule.setSeqId(rs.getInt("SEQ_ID"));
        rule.setBeginIp(rs.getString("BEGIN_IP"));
        rule.setEndIp(rs.getString("END_IP"));
        rule.setType(rs.getString("TYPE"));
        rule.setRemark(rs.getString("REMARK"));
        ruleList.add(rule);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return ruleList;
  }
  public ArrayList<YHIpRule> getIpRule(Connection dbConn,String type) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHIpRule rule = null;
    List list = new ArrayList();
    ArrayList<YHIpRule> ruleList = new ArrayList<YHIpRule>();
    try {
      stmt = dbConn.createStatement();
      String sql = "select SEQ_ID, BEGIN_IP, END_IP, TYPE, REMARK from oa_iprule where TYPE = " + type;
      rs = stmt.executeQuery(sql);
      while (rs.next()) { 
        rule = new YHIpRule();
        rule.setSeqId(rs.getInt("SEQ_ID"));
        rule.setBeginIp(rs.getString("BEGIN_IP"));
        rule.setEndIp(rs.getString("END_IP"));
        rule.setType(rs.getString("TYPE"));
        rule.setRemark(rs.getString("REMARK"));
        ruleList.add(rule);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return ruleList;
  }
  
  /**
   * 删除所有IP规则
   * @param conn
   * @param seqIds
   * @throws Exception
   */
  
  public void deleteAll(Connection conn, String seqIds) throws Exception {
    String seqIdStr = seqIds.substring(0, seqIds.length() - 1);
    String sql = "DELETE FROM oa_iprule WHERE SEQ_ID IN(" + seqIdStr + ")";
    PreparedStatement pstmt = null;
    try {
      //System.out.println(sql);
      pstmt = conn.prepareStatement(sql);
      pstmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }
  }
}

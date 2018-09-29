package yh.core.funcs.org.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import yh.core.funcs.org.data.YHOrganization;
import yh.core.util.db.YHDBUtility;

public class YHOrgLogic {
  private static Logger log = Logger.getLogger(YHOrgLogic.class);
  
  public YHOrganization get(Connection conn)throws Exception {
    Statement stmt = null;
    ResultSet rs = null; 
    YHOrganization org = null;
    try {
      String queryStr = "select SEQ_ID, UNIT_NAME, TELEPHONE, MAX, POSTCODE," +
          " ADDRESS, WEBSITE, EMAIL, SIGN_IN_USER, ACCOUNT from oa_organization";
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      //System.out.println(queryStr);
      
      if (rs.next()) {
        org =  new YHOrganization();
        org.setSeqId(rs.getInt("SEQ_ID"));
        org.setUnitName(rs.getString("UNIT_NAME"));
        org.setTelephone(rs.getString("TELEPHONE"));
        org.setMax(rs.getString("MAX"));
        org.setPostcode(rs.getString("POSTCODE"));
        org.setAddress(rs.getString("ADDRESS"));
        org.setWebsite(rs.getString("WEBSITE"));
        org.setEmail(rs.getString("EMAIL"));
        org.setSignInUser(rs.getString("SIGN_IN_USER"));
        org.setAccount(rs.getString("ACCOUNT"));
      }     
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return org;
  }
  
  public void update(Connection conn, YHOrganization org)throws Exception {
    PreparedStatement pstmt = null;
    try{   
      String queryStr = "update oa_organization set UNIT_NAME = ?, TELEPHONE = ?, MAX = ?, POSTCODE = ?," +
          " ADDRESS = ?, WEBSITE = ?, EMAIL = ?, SIGN_IN_USER = ?, ACCOUNT = ? where SEQ_ID = ?";
      pstmt = conn.prepareStatement(queryStr);
      pstmt.setString(1, org.getUnitName());
      pstmt.setString(2, org.getTelephone());
      pstmt.setString(3, org.getMax());
      pstmt.setString(4, org.getPostcode());
      pstmt.setString(5, org.getAddress());
      pstmt.setString(6, org.getWebsite());
      pstmt.setString(7, org.getEmail());
      pstmt.setString(8, org.getSignInUser());
      pstmt.setString(9, org.getAccount());
      pstmt.setInt(10, org.getSeqId());
      pstmt.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(pstmt, null, log);
    }
  }
  
  public void add(Connection conn, YHOrganization org)throws Exception {
    PreparedStatement pstmt = null;
    try{
      String queryStr = "insert into oa_organization (UNIT_NAME, TELEPHONE, MAX, POSTCODE" +
          ",ADDRESS, WEBSITE, EMAIL, SIGN_IN_USER, ACCOUNT) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
      pstmt = conn.prepareStatement(queryStr);
      pstmt.setString(1, org.getUnitName());
      pstmt.setString(2, org.getTelephone());
      pstmt.setString(3, org.getMax());
      pstmt.setString(4, org.getPostcode());
      pstmt.setString(5, org.getAddress());
      pstmt.setString(6, org.getWebsite());
      pstmt.setString(7, org.getEmail());
      pstmt.setString(8, org.getSignInUser());
      pstmt.setString(9, org.getAccount());
      pstmt.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(pstmt, null, log);
    }
  }
  
}

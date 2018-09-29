package yh.core.funcs.workplan.logic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import yh.core.funcs.workplan.data.YHPlanType;
import yh.core.util.db.YHDBUtility;

public class YHPlanTypeLogic {
  private static Logger log = Logger.getLogger(YHPlanTypeLogic.class);
  /***
   * 查询数据
   * @return
   * @throws Exception 
   */
  public List<YHPlanType> selectType(Connection dbConn) throws Exception {
    ResultSet rs = null;
    PreparedStatement stmt = null ; 
    YHPlanType planType = null;
    List<YHPlanType> list = new ArrayList<YHPlanType>();
    String sql = "select SEQ_ID,TYPE_NAME,TYPE_NO from oa_plan_kind order by TYPE_NO";
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        planType = new YHPlanType(); 
        planType.setSeqId(rs.getInt("SEQ_ID"));
        planType.setTypeName(rs.getString("TYPE_NAME"));
        planType.setTypeNO(rs.getInt("TYPE_NO"));
        list.add(planType);
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return list;  
  }
  /***
   * 增加数据
   * @return
   * @throws Exception 
   */
  public void addType(Connection dbConn,YHPlanType type) throws Exception {
    PreparedStatement stmt = null ; 
    String sql = "insert into oa_plan_kind(TYPE_NAME,TYPE_NO) values(?,?)";
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setString(1,type.getTypeName().replaceAll("\"","'"));
      stmt.setInt(2,type.getTypeNO());
      stmt.executeUpdate();
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  /***
   * 删除数据
   * @return
   * @throws Exception 
   */
  public void deleteType(Connection dbConn,int seqId) throws Exception {
    PreparedStatement stmt = null ; 
    String sql = "delete from oa_plan_kind where seq_id=?";
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setInt(1,seqId);
      stmt.executeUpdate();
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  /***
   * 删除所有数据
   * @return
   * @throws Exception 
   */
  public void deleteTypeAll(Connection dbConn) throws Exception {
    PreparedStatement stmt = null ; 
    String sql = "delete from oa_plan_kind";
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.executeUpdate();
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  /***
   *修改数据
   * @return
   * @throws Exception 
   */
  public void updateType(Connection dbConn,YHPlanType type) throws Exception {
    PreparedStatement stmt = null ; 
    String sql = "update oa_plan_kind set TYPE_NAME=?,TYPE_NO=? where seq_id=?";
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setString(1,type.getTypeName().replaceAll("\"","'"));
      stmt.setInt(2,type.getTypeNO());
      stmt.setInt(3,type.getSeqId());
      stmt.executeUpdate();
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  /***
   *根据ID查询数据
   * @return
   * @throws Exception 
   */
  public YHPlanType selectId(Connection dbConn,int seqId) throws Exception {
    ResultSet rs = null;
    PreparedStatement stmt = null ; 
    YHPlanType planType = null;
    String sql = "select SEQ_ID,TYPE_NAME,TYPE_NO from oa_plan_kind where seq_id=?";
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setInt(1,seqId);
      rs = stmt.executeQuery();
      if (rs.next()) {
        planType = new YHPlanType(); 
        planType.setSeqId(rs.getInt("SEQ_ID"));
        planType.setTypeName(rs.getString("TYPE_NAME"));
        planType.setTypeNO(rs.getInt("TYPE_NO"));
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return planType;  
  }
  /***
   *根据ID查询数据
   * @return
   * @throws Exception 
   */
  public YHPlanType selectTypeName(Connection dbConn,String TypeName) throws Exception {
    ResultSet rs = null;
    PreparedStatement stmt = null ; 
    YHPlanType planType = null;
    String sql = "select TYPE_NAME from oa_plan_kind where TYPE_NAME=?";
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setString(1,TypeName);
      rs = stmt.executeQuery();
      if (rs.next()) {
        planType = new YHPlanType(); 
        planType.setTypeName(rs.getString("TYPE_NAME"));
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return planType;  
  }
}

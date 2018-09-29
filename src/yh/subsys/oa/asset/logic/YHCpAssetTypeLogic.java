package yh.subsys.oa.asset.logic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import yh.core.util.db.YHDBUtility;
import yh.subsys.oa.asset.data.YHCpAssetType;
import yh.subsys.oa.asset.data.YHCpCptlInfo;

public class YHCpAssetTypeLogic {
  private static Logger log = Logger.getLogger(YHCpAssetTypeLogic.class);


  public static List<YHCpAssetType> specList(Connection dbConn) {
    ResultSet rs = null;
    PreparedStatement stmt = null ;
    YHCpAssetType cp = null;
    List<YHCpAssetType> list = new ArrayList<YHCpAssetType>();
    String sql = "SELECT SEQ_ID,TYPE_NAME,TYPE_NO from oa_asset_type order by TYPE_NO";
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        cp =  new YHCpAssetType();
        cp.setSeqId(rs.getInt("SEQ_ID"));
        cp.setTypeName(rs.getString("TYPE_NAME"));
        cp.setTypeNo(rs.getInt("TYPE_NO"));
        list.add(cp);
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally {
      YHDBUtility.close(stmt,null,log);
    } 
    System.out.println("size:"+list.size());
    return list;
  }

  public static YHCpAssetType cptlSpec(Connection dbConn,String typeName) {
    ResultSet rs = null;
    PreparedStatement stmt = null ;
    YHCpAssetType cp = null;
    String sql = "SELECT SEQ_ID,TYPE_NAME from oa_asset_type where TYPE_NAME=?";
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setString(1,typeName);
      rs = stmt.executeQuery();
      if (rs.next()) {
        cp =  new YHCpAssetType();
        cp.setSeqId(rs.getInt("SEQ_ID"));
        cp.setTypeName(rs.getString("TYPE_NAME"));
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally {
      YHDBUtility.close(stmt,null,log);
    } 
    return cp;
  }

  /***
   * 增加数据
   * @return
   * @throws Exception 
   */
  public static void addType(Connection dbConn,YHCpAssetType type) throws Exception {
    PreparedStatement stmt = null ; 
    String sql = "insert into oa_asset_type(TYPE_NAME,TYPE_NO) values(?,?)";
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setString(1,type.getTypeName());
      stmt.setInt(2,type.getTypeNo());
      stmt.executeUpdate();
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  /**
   * 取得最大的seqId
   * @param conn
   * @param tableName
   * @return
   * @throws Exception
   */
  public static int getMaxSeqId(Connection conn) throws Exception{
    int result = 0;
    String sql = "select max(SEQ_ID) from oa_asset_type";
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

  /***
   *根据ID查询数据
   * @return
   * @throws Exception 
   */
  public YHCpAssetType selectTypeName(Connection dbConn,String TypeName) throws Exception {
    ResultSet rs = null;
    PreparedStatement stmt = null ; 
    YHCpAssetType planType = null;
    String sql = "select TYPE_NAME from oa_asset_type where TYPE_NAME=?";
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setString(1,TypeName);
      rs = stmt.executeQuery();
      if (rs.next()) {
        planType = new YHCpAssetType(); 
        planType.setTypeName(rs.getString("TYPE_NAME"));
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return planType;  
  }
  
  //删除所有
  public static void deleteAll (Connection dbConn) throws SQLException {
    PreparedStatement stmt = null ; 
    String sql = "delete from oa_asset_type";
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt,null, log);
    }
  }
  
  //查询,故意存放
  public static YHCpCptlInfo getAsset (Connection dbConn,int seqId) throws SQLException {
    PreparedStatement stmt = null ;
    ResultSet rs = null;
    YHCpCptlInfo type = null;

    String sql = "select cp.cptl_no as cptlNo,cp.cptl_name as cptlName,"
      + " cp.cptl_spec as cptlSpec,re.dept_id as deptId,re.cpre_qty as cpreQty,"
      + " re.cpre_place as cprePlace,re.cpre_memo as cpreMemo,"
      + " re.cpre_reason as cpreReason,re.cpre_flag as cpreFlag from oa_asset_record re "
      + " left outer join oa_asset_info cp on cp.SEQ_ID=re.cptl_id where re.seq_id=?";
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setInt(1,seqId);
      rs = stmt.executeQuery();
      if (rs.next()) {
        type = new YHCpCptlInfo();
        type.setSeqId(seqId);
        type.setCptlNo(rs.getString("cptlNo"));
        type.setCptlName(rs.getString("cptlName"));
        type.setCptlSpec(rs.getString("cptlSpec"));
        type.setUseDept(rs.getString("deptId"));//领用部门
        type.setCptlQty(rs.getInt("cpreQty"));//数量
        type.setRemark(rs.getString("cpreMemo"));//备注
        type.setUseFor(rs.getString("cpreFlag"));//领用单，返库单
        type.setSafekeeping(rs.getString("cprePlace"));//地点
        type.setKeeper(rs.getString("cpreReason"));//原因
      }
    } catch (SQLException e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt,null, log);
    }
    return type;
  }
}

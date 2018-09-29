package yh.subsys.oa.asset.logic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.util.db.YHDBUtility;

public class YHProductInfoLogic {
  private static Logger log = Logger.getLogger(YHProductInfoLogic.class);
  /***
   * 根据计量单位名称查询是否存在，存在则读取id
   * @return
   * @throws Exception 
   */
  public String selectUnit(Connection dbConn,String unitName) throws Exception {
    ResultSet rs = null;
    String unitId = "";
    PreparedStatement stmt = null ;
    try {
      String sql = "select unit_id from erp_product_unit where unit_name=?";
      stmt = dbConn.prepareStatement(sql);
      stmt.setString(1,unitName);
      rs = stmt.executeQuery();
      if (rs.next()) {
        unitId = rs.getString("unit_id");
      }
    } catch (Exception e) {
      //System.out.println(e.getMessage());
    } finally {
      YHDBUtility.close(stmt,rs,log);
    }
    return unitId;
  }
  /***
   * 根据所属大类名称查询是否存在，存在则读取id
   * @return
   * @throws Exception 
   */
  public String selectStyle(Connection dbConn,String styleName) throws Exception {
	  ResultSet rs = null;
	  String styleId = "";
	  PreparedStatement stmt = null ;
	  try {
		  String sql = "select id from erp_product_style where name=?";
		  stmt = dbConn.prepareStatement(sql);
		  stmt.setString(1,styleName);
		  rs = stmt.executeQuery();
		  if (rs.next()) {
			  styleId = rs.getString("id");
		  }
	  } catch (Exception e) {
		  //System.out.println(e.getMessage());
	  } finally {
		  YHDBUtility.close(stmt,rs,log);
	  }
	  return styleId;
  }
  /***
   * 根据类别名称查询是否存在，存在则读取id
   * @return
   * @throws Exception 
   */
  public String selectTypeName(Connection dbConn,String typeName) throws Exception {
	  ResultSet rs = null;
	  String typeId = "";
	  PreparedStatement stmt = null ;
	  try {
		  String sql = "select id from erp_product_type where name=?";
		  stmt = dbConn.prepareStatement(sql);
		  stmt.setString(1,typeName);
		  rs = stmt.executeQuery();
		  if (rs.next()) {
			  typeId = rs.getString("id");
		  }
	  } catch (Exception e) {
		  //System.out.println(e.getMessage());
	  } finally {
		  YHDBUtility.close(stmt,rs,log);
	  }
	  return typeId;
  }
  /***
   * 根据类别父节点名称查询是否存在，存在则读取id
   * @return
   * @throws Exception 
   */
  public Map<String,Object> selectType(Connection dbConn,String styleName) throws Exception {
	  ResultSet rs = null;
	  String typeId = "";
	  PreparedStatement stmt = null ;
	  Map<String,Object> typeMap=new HashMap<String,Object>();
	  try {
		  String sql = "select id,tree_code,tree_name from erp_product_type where name=?";
		  stmt = dbConn.prepareStatement(sql);
		  stmt.setString(1,styleName);
		  rs = stmt.executeQuery();
		  if (rs.next()) {
			 typeMap.put("typeId", rs.getString("id"));
			 typeMap.put("treeCode", rs.getString("tree_code"));
			 typeMap.put("treeName", rs.getString("tree_name"));
		  }
	  } catch (Exception e) {
		  //System.out.println(e.getMessage());
	  } finally {
		  YHDBUtility.close(stmt,rs,log);
	  }
	  return typeMap;
  }
  
 
}

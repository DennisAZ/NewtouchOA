package yh.subsys.oa.asset.logic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.util.db.YHDBUtility;

public class YHDbInfoLogic {
  private static Logger log = Logger.getLogger(YHDbInfoLogic.class);
  /***
   * 根据仓库名称查询是否存在，存在则读取id
   * @return
   * @throws Exception 
   */
  public String selectWhName(Connection dbConn,String whName) throws Exception {
    ResultSet rs = null;
    String whId = "";
    PreparedStatement stmt = null ;
    try {
      String sql = "select id as whId from erp_warehouse where name=?";
      stmt = dbConn.prepareStatement(sql);
      stmt.setString(1,whName);
      rs = stmt.executeQuery();
      if (rs.next()) {
    	  whId = rs.getString("whId");
      }
    } catch (Exception e) {
      //System.out.println(e.getMessage());
    } finally {
      YHDBUtility.close(stmt,rs,log);
    }
    return whId;
  }
  /***
   * 根据产品名称查询是否存在，存在则读取id
   * @return
   * @throws Exception 
   */
  public String selectProduct(Connection dbConn,String proName) throws Exception {
	  ResultSet rs = null;
	  String proId = "";
	  PreparedStatement stmt = null ;
	  try {
		  String sql = "select id as proId from erp_product where pro_name=?";
		  stmt = dbConn.prepareStatement(sql);
		  stmt.setString(1,proName);
		  rs = stmt.executeQuery();
		  if (rs.next()) {
			  proId = rs.getString("proId");
		  }
	  } catch (Exception e) {
		  //System.out.println(e.getMessage());
	  } finally {
		  YHDBUtility.close(stmt,rs,log);
	  }
	  return proId;
  }
  /***
   * 根据所属大类查询是否存在，存在则读取id
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

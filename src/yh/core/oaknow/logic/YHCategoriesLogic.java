package yh.core.oaknow.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import yh.core.oaknow.data.YHCategoriesType;
import yh.core.oaknow.util.YHStringUtil;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHCategoriesLogic{
  
  /**
   * 增加新的类型
   * @param dbConn
   * @param type
   * @return
   * @throws Exception
   */
  public int saveCategoty(Connection dbConn, YHCategoriesType type) throws Exception{
    PreparedStatement ps = null;
    int id = 0;
    String sql = "insert into oa_category_kind(CATEGORIE_NAME,PEARENT_ID,ORDER_ID,MANAGER) values(?,?,?,?)";    
    try{
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, YHStringUtil.replaceSQ(type.getName()));
      ps.setInt(2, type.getPearentId());
      ps.setInt(3, type.getOrderId());
      ps.setString(4, type.getManagers());
      id = ps.executeUpdate();
    } catch (SQLException e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return id;
  }
 /**
  * 更新类型 
  * @param dbConn
  * @param type
  * @return
  * @throws Exception
  */
  public int updateCategoty(Connection dbConn, YHCategoriesType type) throws Exception{
    PreparedStatement ps = null;
    int id =0;
    String sql = "update oa_category_kind set CATEGORIE_NAME =?,PEARENT_ID =?, ORDER_ID=?, MANAGER=? where SEQ_ID = ?";
    try{
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, YHStringUtil.replaceSQ(type.getName()));
      ps.setInt(2, type.getPearentId());
      ps.setInt(3, type.getOrderId());
      ps.setString(4, type.getManagers());
      ps.setInt(5, type.getSeqId());
      id = ps.executeUpdate();
    } catch (SQLException e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return id;
  }
  
  public int saveOrUpdateCategoty(Connection dbConn, YHCategoriesType type) throws Exception{
    if(type.getSeqId()==0){
      return saveCategoty(dbConn,type);
    }else{
      return updateCategoty(dbConn, type);
    }
  }
  
  /**
   * 根据seqId找类型
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public YHCategoriesType findATypeById(Connection dbConn, int seqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = "select CATEGORIE_NAME" 
    		              +  ",PEARENT_ID" 
    		               + ",ORDER_ID" 
    		               + ",MANAGER"
    		               +", seq_id"
    		           + " from oa_category_kind"
    		           +" where seq_id = "+ seqId;
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    if(rs.next()){
      YHCategoriesType type = new YHCategoriesType();
      type.setName(rs.getString(1));
      type.setPearentId(rs.getInt(2));
      type.setOrderId(rs.getInt(3));
      type.setManagers(rs.getString(4));
      type.setSeqId(rs.getInt(5));
      if(YHStringUtil.isNotEmpty(type.getManagers())){
        String names = listUtil(findPersonNames(dbConn, type.getManagers()));
        //YHOut.println(names);
        type.setManagerNames(names);
      }
      return type;
    }
    return null;
  }
  
  
  public List<String> findPersonNames(Connection dbConn, String flag)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = "select user_name from person where seq_id in (" + flag +")";
    //YHOut.println(sql);
    List<String> names = new ArrayList<String>();
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        names.add(rs.getString(1));
      }
      return names;
    } catch (Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
  }
  /**
   * 删除某个分类
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public int deleteTypeById(Connection dbConn, int seqId) throws Exception{
    PreparedStatement ps = null;
    String sql = "delete from oa_category_kind where SEQ_ID = "+ seqId;
    try{
      ps = dbConn.prepareStatement(sql);
      int id = ps.executeUpdate();
      return id;
    } catch (Exception e){
     throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
  }
  /**
   * 删除某个分类的子类
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public int deleteChildTypeById(Connection dbConn, int seqId)throws Exception{
    PreparedStatement ps = null;
    String sql = "delete from oa_category_kind where PEARENT_ID = "+ seqId;
    try{
      ps = dbConn.prepareStatement(sql);
      int id = ps.executeUpdate();
      return id;
    } catch (Exception e){
     throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
  }
 /**
  * 删除分类 
  * @param dbConn
  * @param seqId
  * @return
  * @throws Exception
  */
  public int deleteType(Connection dbConn, int seqId)throws Exception{
    int one = deleteTypeById(dbConn, seqId);
    if(one != 0){
      int two = deleteChildTypeById(dbConn, seqId);
    }
    return one;
  }
  
  public String listUtil(List<String> list){
    String temp ="";
    if(list !=null && list.size()>0){
      for(int i=0; i< list.size(); i++){
        if(i< list.size()-1){
         temp += list.get(i) +",";
        }else{
          temp += list.get(list.size()-1);
        }
      }      
    }
    return temp;
  }
}


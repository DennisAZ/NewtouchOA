package yh.subsys.inforesouce.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import yh.core.util.YHOut;
import yh.core.util.db.YHDBUtility;

/**
 * 值域操作
 * @author qwx110
 *
 */
public class YHMateValueLogic{
  /**
   * 删除值域
   * @param dbConn
   * @param seqIda 值域表id
   * @param seqIdb 元数据类型表id
   * @throws Exception 
   */
  public int deleteMateVale(Connection dbConn,  int seqIda ) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    int flag =0;
    String sql = "delete from oa_mate_value where seq_id="+seqIda;
    //YHOut.println(sql);
    try{
      ps = dbConn.prepareStatement(sql);
      flag = ps.executeUpdate();
    } catch (Exception e){
     throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return flag;
  }
 
  /**
   * 更新值域
   * @param dbConn
   * @param seqIda
   * @param seqIdb
   * @return
   * @throws Exception
   */
  public int updateMate(Connection dbConn, int seqIda, int seqIdb)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    int flag =0;
    String sql = "update oa_mate_kind set value_range='"+updateRange(dbConn,seqIda+"",seqIdb)+"' where seq_id="+seqIdb;
    //YHOut.println(sql);
    try{
      ps = dbConn.prepareStatement(sql);
      flag = ps.executeUpdate();
      deleteMateVale(dbConn,seqIda);
    } catch (Exception e){
     throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return flag;
  }
  /**
   * 查找值域
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public String findValueRange(Connection dbConn, int seqId)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;  
    String sql = "select value_range from oa_mate_kind where seq_id =" + seqId;
    //YHOut.println(sql);
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        return rs.getString(1);
      }
    } catch (Exception e){
     throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return null;
  }
  
  /**
   * 用来更新值域
   * @param dbConn
   * @param aId
   * @param bId
   * @return
   * @throws Exception
   */
  public String updateRange(Connection dbConn,String aId, int bId) throws Exception{
    String vids = findValueRange(dbConn,bId);
    String newId ="";
    if(vids!=null && vids.length() >0){
      String[] ids = vids.split(",");    
      for(int i=0; i<ids.length; i++){
        if(!aId.trim().equalsIgnoreCase(ids[i])){//删除值域前，先把类型表mate_type的值域查询出来，如果把值域mate_value表的id和类型表的id不相同，就保存起来。在更新表就能去掉删除的id
          newId += ids[i]+",";
        }
      }
    }
    return newId.substring(0,newId.lastIndexOf(",")==-1?0:newId.lastIndexOf(","));
  }
}

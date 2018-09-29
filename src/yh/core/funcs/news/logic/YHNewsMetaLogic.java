package yh.core.funcs.news.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import yh.core.util.YHReflectUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;


/**
 * 与图片新闻的元数据有关
 * @author qwx110
 *
 */
public class YHNewsMetaLogic{
  
  /**
   * 将文件插入文件中心
   * @param dbConn
   * @param guid  文件上传id
   * @param filePath  文件的绝对路径
   * @param zhaiYao   摘要
   * @param fileType  1。文本类型， 2。图片类型， 3视频类型
   * @param seqId   新闻的seqId
   * @throws Exception
   * 
   */
  public void insertMainData(Connection dbConn, String guid, String filePath, Map<String, String> dataMap, 
                             String zhaiYao, String fileType, int newsSeqId) throws Exception {
    String className = "yh.subsys.inforesouce.db.YHMetaDbHelper";
    String method = "updateMetadata";
    String[] paramTypeNameArray = {"java.sql.Connection", "java.lang.String", "java.lang.String","java.util.Map", "java.lang.String", "java.lang.String", "int"};
    Object[] params = {dbConn, guid, filePath, dataMap, zhaiYao, fileType, newsSeqId};
    YHReflectUtility.exeMethod(className, method, paramTypeNameArray, params);
  }
  
  /**
   * 查找人名， 地名，机构名的编号
   * @param dbConn
   * @param name
   * @return
   * @throws SQLException
   */
  public String findNumber(Connection dbConn, String name)throws SQLException{
    PreparedStatement ps = null;
    ResultSet rs = null;
    int seqIds =0;
    String numberId = null;
    String chName = null;
    String findPersonSql = "select seq_id, number_id, chname from oa_mate_kind where chname ='"+ name +"'";
     try{
      ps = dbConn.prepareStatement(findPersonSql);
      rs = ps.executeQuery();
      if(rs.next()){
         seqIds = rs.getInt("seq_id");
         numberId = rs.getString("number_id");
         chName = rs.getString("chname");
      }
      
    }catch(SQLException ex){
      throw ex;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return numberId;
  }
}

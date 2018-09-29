package yh.core.funcs.notify.logic;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;

import yh.core.util.db.YHDBUtility;

public class YHNotifyManageUtilLogic {
  private static Logger log = Logger.getLogger(YHNotifyManageUtilLogic.class);
  
  /**
   * 产生一个随机数
   * @return
   */
  public long getRandom(){
    long result = 0l;
    //Random rand = new Random();
    //result = rand.nextLong();
    result = (long)(Math.random()*(new Date()).getTime());
    //System.out.println(result);
    return result;
  }
  /**
   * 判断 文件是否存在
   * @param savePath
   * @param fileExtName
   * @return
   * @throws IOException
   */
  public boolean getExist(String savePath,String fileExtName) throws IOException {
      String filePath = savePath + File.separator + fileExtName;
      if (new File(filePath).exists()) {
        return true;
      }
    return false;
  }
  
  /**
   * 得到email表的SEQ_ID
   * @param conn
   * @return
   * @throws SQLException 
   */
  public int getBodyId(Connection conn) throws Exception{
    String sql = "select Max(SEQ_ID) FROM OA_NOTIFY";
    PreparedStatement pstmt =null;
    ResultSet rs  = null;
    try{
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while(rs.next()){
        return rs.getInt(1);
      }
        return 0;
    } catch (Exception e){
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
  }
}

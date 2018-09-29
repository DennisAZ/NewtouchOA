package yh.subsys.portal.guoyan.leaderact.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import com.sun.jmx.snmp.Timestamp;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHLeaderactLogic {
 /**
  * 加载 显示
  * @param conn
  * @param newsType
  * @param limit 显示条数
  * @return
 * @throws Exception 
 */
  public StringBuffer loadNew(Connection conn,int limit) throws Exception{
    StringBuffer result = new StringBuffer();
    result = loadNew(conn, limit, 0);
    return result;
  }
  /**
   * 分页加载
   * @param conn
   * @param newsType
   * @param pageSize
   * @param pageNum
   * @return
   * @throws Exception 
   */
  public StringBuffer loadNew(Connection conn,int pageSize,int pageIndex) throws Exception{
    StringBuffer result = new StringBuffer();
    String sql = "SELECT " +
    		" NEWS_ID" +
    		",SUBJECT" +
    		",NEWS_TIME" +
    		" from " +
    		" oa_news " +
    		" where " +
    		" PUBLISH='1' and TO_ID='ALL_DEPT' order by NEWS_TIME desc";
    YHPageQueryParam queryParam = new YHPageQueryParam();
    String nameStr = "newId,subject,newsTime";
    queryParam.setNameStr(nameStr);
    queryParam.setPageIndex(pageIndex);
    queryParam.setPageSize(pageSize);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    result.append(pageDataList.toJson());
    return result;
  }
  public StringBuffer loadNew2(Connection conn,int newId) throws Exception{
    StringBuffer result = new StringBuffer();
    StringBuffer field = new StringBuffer();
    String sql = "SELECT " +
    " NEWS_ID" +
    ",SUBJECT" +
    ",NEWS_TIME" +
    " from " +
    " oa_news " +
    " where " +
    " PUBLISH='1' and TO_ID='ALL_DEPT' order by NEWS_TIME desc";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      
      while(rs.next()){
        int newsId = rs.getInt(1);
        String subject = rs.getString(2);
        Date newsTime = rs.getDate(3);
        if(!"".equals(field.toString())){
          field.append(",");
        }
        field.append("{")
          .append("newsId:").append(newsId).append(",")
          .append("subject:\"").append(YHUtility.encodeSpecial(subject)).append("\",")
          .append("newsTime:\"").append(YHUtility.getDateTimeStr(newsTime)).append("\"")
          .append("}");
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    result.append("[").append(field).append("]");
    return result;
  }
  
  /**
   * 分页加载
   * @param conn
   * @param newsType
   * @param pageSize
   * @param pageNum
   * @return
   * @throws Exception 
   */
  public StringBuffer loadOneNew(Connection conn,int newId) throws Exception{
    StringBuffer result = new StringBuffer();
    String sql = "SELECT " +
        " NEWS_ID" +
        ",SUBJECT" +
        ",CONTENT" +
        ",NEWS_TIME" +
        " from " +
        " oa_news " +
        " where " +
        " NEWS_ID = " + newId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String subject = rs.getString(2);
        String content = rs.getString(3);
        Date newsTime = rs.getDate(4);
        result.append("{")
          .append("subject:\"").append(YHUtility.encodeSpecial(subject)).append("\",")
          .append("content:\"").append(YHUtility.encodeSpecial(content)).append("\",")
          .append("newsTime:\"").append(YHUtility.getDateTimeStr(newsTime)).append("\"")
          .append("}");
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
}

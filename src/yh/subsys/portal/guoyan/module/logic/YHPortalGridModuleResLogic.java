package yh.subsys.portal.guoyan.module.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Map;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
/**
 * 
 * @author Think
 *
 */
public class YHPortalGridModuleResLogic {
  /**
   * 加载数据
   * @param conn
   * @param params
   * @return
   * @throws Exception
   */
  public StringBuffer loadGridDataLogic(Connection conn,Map params) throws Exception{
    StringBuffer result = new StringBuffer();
    Map<String ,String > paramsMap = YHPortalUtil.praserParams(params);
    String orderByField = paramsMap.get("orderBy");
    String orderBySort = paramsMap.get("sort");
    String newsType = paramsMap.get("type");
    String limit = paramsMap.get("limit");
    String start = paramsMap.get("start");
    String mappingName = paramsMap.get("paramName");
    String[] orderByFields = orderByField.split(",");
    String[] orderBySorts = orderBySort.split(",");
    String orderByStr = "";
    for (int i = 0; i < orderByFields.length; i++) {
      String field = orderByFields[i];
      String sort = orderBySorts[i];
      if(!"".equals(orderByStr)){
        orderByStr += ",";
      }
      orderByStr += field + " "  + sort;
    }
    if(!"".equals(orderByStr.trim())){
      orderByStr = " order by " + orderByStr;
    }
    String sql = "SELECT " +
    " NEWS_ID" +
    ",SUBJECT" +
    ",NEWS_TIME" +
    " from " +
    " drc_research_doc  " +
    " where " +
    " PUBLISH='1' and TYPE_ID='" + newsType + "' ";
    
    sql += orderByStr;
    
    YHPageQueryParam queryParam = new YHPageQueryParam();
    queryParam.setNameStr(mappingName);
    queryParam.setPageIndex(Integer.valueOf(start));
    queryParam.setPageSize(Integer.valueOf(limit));
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    result = YHPortalUtil.toJson(pageDataList);
    return result;
  }

  public StringBuffer loadOneData(Connection conn,int newId) throws Exception{
    StringBuffer result = new StringBuffer();
    String sql = "SELECT " +
        " NEWS_ID" +
        ",SUBJECT" +
        ",CONTENT" +
        ",NEWS_TIME" +
        ",PROVIDER " +
        " from " +
        " drc_research_doc " +
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
        String userId = rs.getString(5);
        String userName = YHPortalUtil.getUserNameById(conn, userId);
        result.append("{")
          .append("subject:\"").append(YHUtility.encodeSpecial(subject)).append("\",")
          .append("content:\"").append(YHUtility.encodeSpecial(content)).append("\",")
          .append("publish:\"").append(YHUtility.encodeSpecial(userName)).append("\",")
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
  /**
   * 
   * @param conn
   * @param pageSize
   * @param pageIndex
   * @return
   * @throws Exception
   */
  public StringBuffer loadDataPage(Connection conn,int pageSize,int pageIndex,String newsType) throws Exception{
    StringBuffer result = new StringBuffer();
    String orderByStr = "";
    orderByStr = " order by NEWS_TIME desc " ;
    String sql = "SELECT " +
    " NEWS_ID" +
    ",SUBJECT" +
    ",NEWS_TIME" +
    " from " +
    " drc_research_doc " +
    " where " +
    " PUBLISH='1' and TYPE_ID='" + newsType + "' ";
    
    sql += orderByStr;
    YHPageQueryParam queryParam = new YHPageQueryParam();
    String nameStr = "newId,subject,newsTime";
    queryParam.setNameStr(nameStr);
    queryParam.setPageIndex(pageIndex);
    queryParam.setPageSize(pageSize);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    result.append(pageDataList.toJson());
    return result;
  }
}

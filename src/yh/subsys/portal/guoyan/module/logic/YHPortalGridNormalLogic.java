package yh.subsys.portal.guoyan.module.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.subsys.portal.guoyan.module.data.YHPortalDataRule;
/**
 * 
 * @author Think
 *
 */
public class YHPortalGridNormalLogic {
  private static Map<String,YHPortalDataRule> dataRuleMap = null;
  static{
    String fileName = YHPortalUtil.getConfigFileName();
    try {
      dataRuleMap = YHPortalUtil.loadDataRules(fileName);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
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
    String start = paramsMap.get("start");
    String ruleName = paramsMap.get("ruleName");
    YHPortalDataRule dataRule = dataRuleMap.get(ruleName);
    String orderByField = dataRule.getOrderBy();
    String orderBySort = dataRule.getOrderBySort();
    String tableName = dataRule.getTableName();
    String mappingName = dataRule.getNames();
    String dbFieldName = dataRule.getDbFieldNames().trim();
    int limit = dataRule.getLimit();
    String filter = dataRule.getFilter() == null ? "" : dataRule.getFilter();
    filter = setFilterValue(filter, paramsMap);
    if(dbFieldName == null || "".equals(dbFieldName.trim())){
      return result.append("[]");
    }
    if(dbFieldName.endsWith(",")){
      dbFieldName = dbFieldName.substring(0,dbFieldName.length() - 1 );
    }
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
   
    String sql = "SELECT " + dbFieldName +
    " from " +
    tableName +
    " where 1=1 " +
    " and " + filter ;
    
    sql += orderByStr;
    
    YHPageQueryParam queryParam = new YHPageQueryParam();
    queryParam.setNameStr(mappingName);
    queryParam.setPageIndex(Integer.valueOf(start));
    queryParam.setPageSize(Integer.valueOf(limit));
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    result = YHPortalUtil.toJson(pageDataList);
    return result;
  }

  public StringBuffer loadOneData(Connection conn,int newId,String ruleName) throws Exception{
    StringBuffer result = new StringBuffer();
    YHPortalDataRule dataRule = dataRuleMap.get(ruleName);
    String dbFieldName = dataRule.getDbFieldNames().trim();
    String seqFieldName = dataRule.getSeqFieldName();
    if(dbFieldName == null || "".equals(dbFieldName.trim())){
      return result;
    }
    if(seqFieldName == null || "".equals(seqFieldName.trim())){
      seqFieldName = " NEWS_ID ";
    }
    if(dbFieldName.endsWith(",")){
      dbFieldName = dbFieldName.substring(0,dbFieldName.length() - 1 );
    }
    String tableName = dataRule.getTableName();
    String mappingName = dataRule.getNames();

    String sql = "SELECT " +
        dbFieldName +
        " from " +
        tableName +
        " where " +
        seqFieldName + "= " + newId;
    YHPageQueryParam queryParam = new YHPageQueryParam();
    queryParam.setNameStr(mappingName);
    queryParam.setPageIndex(Integer.valueOf(0));
    queryParam.setPageSize(Integer.valueOf(1));
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    result = YHPortalUtil.oneDatatoJson(pageDataList);
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
  public StringBuffer loadDataPage(Connection conn,int pageIndex,String ruleName,Map params) throws Exception{
    StringBuffer result = new StringBuffer();
    Map<String ,String > paramsMap = YHPortalUtil.praserParams(params);
    YHPortalDataRule dataRule = dataRuleMap.get(ruleName);
    String limitStr = paramsMap.get("pageSize");
    String dbFieldName = dataRule.getDbFieldNames().trim();
    if(dbFieldName == null || "".equals(dbFieldName.trim())){
      return result;
    }
    if(dbFieldName.endsWith(",")){
      dbFieldName = dbFieldName.substring(0,dbFieldName.length() - 1 );
    }
    String tableName = dataRule.getTableName();
    String mappingName = dataRule.getNames();
    int limit = 0;
    if(limitStr == null || "".equals(limitStr)){
      limit = dataRule.getPagingLimit();
    }else{
      limit = Integer.valueOf(limitStr);
    }
    String filter = dataRule.getFilterPaging() == null ? "" : dataRule.getFilterPaging();
    filter = setFilterValue(filter, paramsMap);
    String filterstr = "";
    if(!"".equals(filter)){
      filterstr = " and " + filter;
    }
    String serchFlag = paramsMap.get("searchFlag");
    if(serchFlag != null && "1".equals(serchFlag)){
      String serachstr = dataRule.getSerach() == null ? "" : dataRule.getSerach();
      serachstr = setFilterValue(serachstr, paramsMap);
      filterstr += " and " + serachstr;
    }
    String orderByStr = "";
    String orderByField = dataRule.getOrderBy();
    String orderBySort = dataRule.getOrderBySort();
    String[] orderByFields = orderByField.split(",");
    String[] orderBySorts = orderBySort.split(",");
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
      dbFieldName +
      " from " +
      tableName +
      " where 1=1 " +
      filterstr ;
    
    sql += orderByStr;
    YHPageQueryParam queryParam = new YHPageQueryParam();
    queryParam.setNameStr(mappingName);
    queryParam.setPageIndex(pageIndex);
    queryParam.setPageSize(limit);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    result.append(pageDataList.toJson());
    return result;
  }
  
  public String setFilterValue(String filter, Map<String ,String > paramsMap){
    String result = "";
    String  patternStrs="#\\{([a-zA-Z]+)\\}";
    Pattern p = Pattern.compile(patternStrs,Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher(filter);
    StringBuffer sb = new StringBuffer();
    while(m.find()){
      String key =  m.group(1);
      String repkey =  m.group(0);
      String filterKey = "filter_" + key;
      String filterValue = paramsMap.get(filterKey);
      m.appendReplacement(sb, filterValue);
    }
    m.appendTail(sb);
    return sb.toString();
  }
}

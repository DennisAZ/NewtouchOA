package yh.core.funcs.portal.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yh.core.funcs.portal.util.rules.YHLinkRule;
import yh.core.funcs.portal.util.rules.YHModulesRule;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHPortalProducer{
  private List<Object> data;
  private List<YHModulesRule> rules;
  private String link = "";
  private int amount;
  private int start;
  private int limit;
  
  public YHPortalProducer() {
    data = new ArrayList<Object>();
    rules = new ArrayList<YHModulesRule>();
  }
  public void setData(List<Object> list) {
    this.data = list;
  }
  
  public void setData(Connection dbConn, String sql) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      this.data = this.resultSet2List(rs);
    }//如果sql语句有问题的话，不返回异常,即this.data为空
    catch (Exception e) {
      throw e;
    }
    finally {
      YHDBUtility.close(ps, rs, null);
    }
  }
  
  public void setMoreLink(String link) {
    this.link = link;
  }
  
  public void setTotalRecords(int amount) {
    this.amount = amount;
  }
  
  public void setStart(int start) {
    this.start = start;
  }
  
  public void setLimit(int limit) {
    this.limit = limit;
  }
  
  public void addRule(YHModulesRule rule) {
    this.rules.add(rule);
  }
  
  public String toJson() throws Exception {
    StringBuffer sb = new StringBuffer();
    sb.append(String.format("{moreLink:\"%s\",totalRecords:%d, start: %d, limit: %d, records:[", link, amount, start, limit));
    for (Object o : data) {
      sb.append("{cells:[");
      for (YHModulesRule rule : rules) {
        sb.append(rule.toJson(o));
        sb.append(",");
      }
      
      if (sb.charAt(sb.length() - 1) == ',') {
        sb.deleteCharAt(sb.length() - 1);
      }
      
      sb.append("]},");
    }
    if (sb.charAt(sb.length() - 1) == ',') {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]}");
    return sb.toString();
  }
  
  private List<Object> resultSet2List(ResultSet rs) throws SQLException{
    List<Object> list = new ArrayList<Object>();
    ResultSetMetaData rsMeta = rs.getMetaData();
    while(rs.next()){
      Map<String, String> map = new HashMap<String, String>();
      for(int i = 0; i < rsMeta.getColumnCount(); ++i){   
        String columnName = rsMeta.getColumnName(i+1);   
        map.put(rsMeta.getColumnName(i+1), null == rs.getString(columnName)?"":rs.getString(columnName)); 
      }
      list.add(map);
    }
    return list;
  }
  
  public static String[] convert2Array(String str) {
    Pattern pattern = Pattern.compile("\\$\\{\\w+\\}",Pattern.CASE_INSENSITIVE  );  
    Matcher matcher = pattern.matcher(str);
    int startIndex = 0;
    List<String> list = new ArrayList<String>();
    while(matcher.find()) {
      int start = matcher.start();
      int end = matcher.end();
      String str1  = str.substring(startIndex , start);
      String str2 = str.substring(start , end);
      startIndex = end ;
      if (!YHUtility.isNullorEmpty(str1)) {
        list.add(YHModulesRule.DISPLAY_PREFIX + str1);
      } 
      String s= str2.substring(2, str2.length() - 1);
      list.add(s);
    }
    String[] strs = new String[list.size()];
    for(int i = 0 ;i < list.size() ; i++) {
      strs[i] = list.get(i);
    }
    return strs;
  }
  public static void main(String[] args) throws Exception {
    YHPortalProducer rule = new YHPortalProducer();
    String[] s = rule.convert2Array("${liudhan}a刘ddd ${liudhan}涵木要fddd${liudhan}aa,ddaadd${aaaadd}");
    for (String ss : s) {
      System.out.println(ss);
    }
//    List<Object> list = new ArrayList<Object>();
//    for (int i = 0; i < 5; i++) {
//      Map<String, String> map = new HashMap<String, String>();
//      map.put("seqId", "第" + i + "条");
//      map.put("B", "bbbbb");
//      map.put("C", "cccc");
//      map.put("D", "ddddddddddddddd");
//      list.add(map);
//    }
//    rule.setData(list);
//    
//    YHLinkRule r = new YHLinkRule(new String[]{"seqId", YHModulesRule.DISPLAY_PREFIX + "--", "B"},
//        new String[]{YHModulesRule.DISPLAY_PREFIX + "/yh/somefuncs.act?id=", "B", YHModulesRule.DISPLAY_PREFIX + "&type=", "seqId"});
//    r.setAttribute("target", "_blank");
//    rule.addRule(r);
//    
//    System.out.println(rule.toJson());
  }
}
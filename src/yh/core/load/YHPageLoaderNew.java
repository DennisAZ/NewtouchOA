package yh.core.load;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import yh.core.data.YHDbRecord;
import yh.core.data.YHDsType;
import yh.core.data.YHPageDataListNew;
import yh.core.data.YHPageQueryParamNew;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.generics.YHSQLParamHepler;

public class YHPageLoaderNew {
  /**
   * log                                               
   */
  private static Logger log = Logger.getLogger(YHPageLoaderNew.class);
  /**
   * 加载分页数据
   * @param dbConn
   * @param pageSize
   * @param pageIndex
   * @param sql
   * @return
   * @throws Exception
   */
  public static YHPageDataListNew loadPageList(Connection dbConn,
      YHPageQueryParamNew queryParam,
      String sql) throws Exception {
    
    int pageSize = queryParam.getPageSize();
    int pageIndex = queryParam.getPageIndex();
    String sortColumn = queryParam.getSortColumn();
    String direct = queryParam.getDirect();
    
    
    String[] nameList = queryParam.getNameStr().split(",");
    
    //设置排序字段
    if (!YHUtility.isNullorEmpty(sortColumn)) {
      StringBuilder sb = new StringBuilder();
      for (char c : sortColumn.toCharArray()) {
        if (Character.isUpperCase(c)) {
          sb.append('_');
        }
        sb.append(c);
      }
      
      		
      
      String dbms = YHSysProps.getProp("db.jdbc.dbms");
      if (dbms.equals("sqlserver")) {
        Pattern p = Pattern.compile("order {1,}by.{1,}(desc|asc)");
        Matcher m = p.matcher(sql);
        if (m.find()) {
          String sel = sql.substring(0, m.start());
          String ord = sql.substring(m.start(), m.end());
          String other = sql.substring(m.end(), sql.length());
          
          Pattern op = Pattern.compile("order {1,}by ", Pattern.CASE_INSENSITIVE);
          Matcher om = p.matcher(ord);
          String order = om.replaceAll("order by " + sb + " ");
          
          Pattern op2 = Pattern.compile("(desc|asc)", Pattern.CASE_INSENSITIVE);
          Matcher om2 = p.matcher(ord);
          order += om.replaceAll(direct);
          
          sql = sel + order + other;
        }
        else {
          sql += " order by " + sb + " " + direct;
        }
        
        
      } else if (dbms.equals("mysql")) {
        //按照gbk编码的排序
        sql = "select *" +
        " from (" + sql + ")" + " PAGE_TEMP_TABLE";
        sql += " order by CONVERT(" + sb + " USING gbk) COLLATE gbk_chinese_ci " + direct;
      } else if (dbms.equals("oracle")) {
        sql = "select *" +
        " from (" + sql + ")" + " PAGE_TEMP_TABLE";
        sql += " order by " + sb + " " + direct;
      } else {
        throw new SQLException("not accepted dbms");
      }
    }
    
    YHPageDataListNew rtList = new YHPageDataListNew();

    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      rs = stmt.executeQuery(sql);
      //总记录数
      rs.last();
      int recordCnt = rs.getRow();
      if (recordCnt == 0) {
        return rtList;
      }
      rtList.setTotalRecord(recordCnt);
      //总页数
      int pageCnt = recordCnt / pageSize;
      if (recordCnt % pageSize != 0) {
        pageCnt++;
      }
      if (pageIndex < 0) {
        pageIndex = 0;
      }
      if (pageIndex > pageCnt - 1) {
        pageIndex = pageCnt - 1;
      }
      
      rtList.setPage(queryParam.getPage());
      rtList.setTotal(pageCnt);
      rs.absolute(pageIndex * pageSize + 1);
      
      int fieldCnt = nameList.length;
      
      ResultSetMetaData meta = rs.getMetaData();
      int[] typeArray = new int[fieldCnt];
      int[] scale = new int[fieldCnt];
      for (int i = 0; i < fieldCnt; i++) {
        typeArray[i] = meta.getColumnType(i + 1);
        scale[i] = meta.getScale(i + 1);
      }
      //记录取出记录的条数
      for (int i = 0; i < pageSize && !rs.isAfterLast(); i++) {
        YHDbRecord record = new YHDbRecord();
        rtList.addRecord(record);
        for (int j = 0; j < fieldCnt; j++) {
          String name = nameList[j];
          Object value = null;
          int typeInt = typeArray[j];
          if (YHDsType.isDecimalType(typeInt)) {
            if (scale[j] == 0) {
              value = new Integer(rs.getInt(j + 1));
            }else {
              value = new Double(rs.getDouble(j + 1));
            }
          }else if (YHDsType.isIntType(typeInt)) {
            value = new Integer(rs.getInt(j + 1));
          }else if (YHDsType.isLongType(typeInt)) {
            value = new Long(rs.getLong(j + 1));
          }else if (YHDsType.isDateType(typeInt)) {
            value = rs.getTimestamp(j + 1);
          }else if (typeInt == Types.CLOB) {
            value = YHSQLParamHepler.clobToString(rs.getClob(j + 1));
          }else {
            value = rs.getString(j + 1);
          }
          record.addField(name, value);
        }
        rs.next();
      }
      return rtList;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  public static void main(String[] args) {
    String sql = "select * from person where s = fd order by dqe dec asc";
    Pattern p = Pattern.compile("order {1,}by.{1,}(desc|asc)");
    Matcher m = p.matcher(sql);
    if (m.find()) {
      String sel = sql.substring(0, m.start());
      String ord = sql.substring(m.start(), m.end());
      String other = sql.substring(m.end(), sql.length());
    }
  }
}

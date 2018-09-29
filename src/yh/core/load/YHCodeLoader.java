package yh.core.load;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import yh.core.data.YHDBMapExcelReader;
import yh.core.dto.YHCodeLoadParam;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHCodeLoader {
  /**
   * log                                               
   */
  private static Logger log = Logger.getLogger(YHCodeLoader.class);
  
  /**
   * 加载编码
   * @param dbConn
   * @param param
   * @return
   * @throws Exception
   */
  public static List<String[]> loadData(Connection dbConn, YHCodeLoadParam param) throws Exception {
    
    List rtList = new ArrayList<String[]>();
    Statement stmt = null;
    ResultSet rs = null;
    try {
      String codeField = param.getCodeField();
      String nameField = param.getNameField();
      String tableName = (String)YHDBMapExcelReader.GDBMap.get(param.getTableName());
      	if(tableName==null){
       tableName=param.getTableName();
      	}
      String filterField = param.getFilterField();
      String filterValue = param.getFilterValue();
      String filter = null;
      if (!YHUtility.isNullorEmpty(filterField) && !YHUtility.isNullorEmpty(filterValue)) {
        if (filterValue.equalsIgnoreCase("[null]")) {
          filter = filterField + " is null";
        } else if(filterValue.equalsIgnoreCase("[no]")){
          filter = filterField + " is not null";
        } else {
          filter = filterField + "='" + filterValue + "'";
        }
      }
      String sql = "select " + codeField + ", " + nameField + " from " + tableName;
      if (!YHUtility.isNullorEmpty(filter)) {
        sql += " where " + filter;
      }
      String orderFiled = param.getOrder();
      if (!YHUtility.isNullorEmpty(orderFiled)) {
        sql += " order by " + orderFiled;
      }
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String code = rs.getString(1);
        String name = rs.getString(2);
        rtList.add(new String[]{code, name});
      }
      return rtList;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
}

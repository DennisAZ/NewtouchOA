package yh.core.act;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHDBMapUtil;
import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHCode2DescAct {

  private static Logger log = Logger.getLogger(YHCode2DescAct.class);

  /**
   * 代码转换成名称描述   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String code2Desc(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    Connection dbConn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String queryParam = request.getParameter("queryParam");
      queryParam = queryParam.trim();
      String[] paramGroup = queryParam.split("/");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      StringBuffer codeData = new StringBuffer("[");
      StringBuffer field = new StringBuffer();
      for (String param : paramGroup) {
        String queryIds = param.trim().split(";")[0];
        String[] querys = param.split(";")[1].split(",");
        String tableName = querys[0].trim();
        tableName=YHDBMapUtil.getMapDBName(tableName);
        String codeName = querys[1].trim();
        String descName = querys[2].trim();
        String codeTpye = "";
        if(querys.length == 4){
          codeTpye = querys[3].trim();
        }
        if (queryIds.endsWith(",")) {
          queryIds = queryIds.substring(0, queryIds.length() - 1);
        }
        if (queryIds.indexOf(",,") > 0) {
          queryIds = queryIds.replaceAll(",,", ",0,");
        }
        if (YHUtility.isNullorEmpty(queryIds)) {
          queryIds = "0";
        }
        String sql = null;
        if( "oa_kind_dict_item".equalsIgnoreCase(tableName)){
          sql = "select " + codeName + "," + descName + " from "
          + tableName + " where " + codeName + " = ('" + queryIds + "')";
        }else if (queryIds.equalsIgnoreCase("all_dept")
            || queryIds.equalsIgnoreCase("0")) {
          sql = "select " + codeName + "," + descName + " from " + tableName;
        }else {
          if (queryIds.startsWith(",")) {
            queryIds = "0" + queryIds;
          }
          if (queryIds.endsWith(",")) {
            queryIds = queryIds + "0";
          }
          sql = "select " + codeName + "," + descName + " from "
            + tableName + " where " + codeName + " in (" + queryIds + ")";
        }
        //System.out.println(tableName + "查询语句  ： " + sql);
        if(!"".equals(codeTpye)){
          sql += " and CLASS_NO = '" + codeTpye + "'";
        }
        ps = dbConn.prepareStatement(sql);
        rs = ps.executeQuery();
        if (!"".equals(field.toString())) {
          field.append(",");
        }
        List<String> list = new LinkedList<String>();
        // list.
        for (String str : queryIds.split(",")) {
          str = str.replace("\'", "");
          list.add(str);
        }
        field.append(toJson(rs, list));
      }
      codeData.append(field).append("]");
      //System.out.println("codeData2Json 数据为 ： " + codeData.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "加载代码成功");
      request.setAttribute(YHActionKeys.RET_DATA, codeData.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "加载代码失败" + ex.getMessage());
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * @param rs
   * @param filterName
   * @param querys
   * @return
   * @throws Exception
   */
  private StringBuffer toJson(ResultSet rs, List querys)
      throws Exception {
    StringBuffer result = new StringBuffer("{");
    StringBuffer field = new StringBuffer();
    
    Map<String,String> rsMap = new HashMap<String,String>();
    while (rs.next()) {
      String value = null;
      String key = null;
      key = rs.getString(1);
      value = "\"" + rs.getString(2) + "\"";
      rsMap.put(key, value);
    }
    for (Object obj : querys) {
      if (!"".equals(field.toString())) {
        field.append(",");
      }
      String key = (String)obj;
      String value = rsMap.get(key);
      String code = "\"" + key + "\"";
      if (value == null) {
        value = "\"\"";
      }
      field.append(code).append(":").append(value);
    }
    result.append(field).append("}");
    return result;
  }
}

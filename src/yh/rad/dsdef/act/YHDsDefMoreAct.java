package yh.rad.dsdef.act;

import java.sql.Connection;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataListNew;
import yh.core.data.YHPageQueryParamNew;
import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.load.YHPageLoaderNew;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.rad.dsdef.logic.YHDsDefJsonlogic;
import yh.rad.dsdef.logic.YHDsDefLogic;

public class YHDsDefMoreAct {
  private static Logger log = Logger.getLogger(YHDsDefMoreAct.class);

  public String testMethod(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String tableNo = request.getParameter("tableNo");
      YHDsDefJsonlogic json = new YHDsDefJsonlogic();
      YHDsDefLogic ddl = new YHDsDefLogic();
      List list1 = ddl.selectTableField(dbConn, tableNo);
      Object ob = null;
      StringBuffer jsons = new StringBuffer(" { \"total\":"+100+","+"\"records\":[");        
       for (Iterator its = list1.iterator(); its.hasNext();) {
        ob = its.next();
        String s = (json.toJson(ob)).toString();
        jsons.append(s);
        if(its.hasNext()){
          jsons.append(",");
        }
      }
      jsons.append("]}");
      PrintWriter out = response.getWriter();
      out.println(jsons.toString().trim());                           
      out.flush();
      out.close();
     //System.out.println(jsons.toString());
     /*// out.flush();
      String classTable = (String) request.getParameter("YHDsTable");
      YHDsDefFormAct ds = new YHDsDefFormAct();
      Object obj = ds.build(request, classTable);
      String classField = (String) request.getParameter("YHDsField");
      int idName = Integer.parseInt(request.getParameter("id"));
      YHDsDefFormMoreAct dm = new YHDsDefFormMoreAct();
      dm.build(request, classField, idName);
      String tableNo = request.getParameter("tableNo");
      int fieldNo = Integer.parseInt(request.getParameter("fieldNo"));
      YHDsDefLogic dsdef = new YHDsDefLogic();
      dsdef.delete(fieldNo, dbConn);
      dsdef.selectTable(dbConn);
      dsdef.selectTableField(dbConn, tableNo);
*/
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "submit failed");

      throw ex;
    }
  /*  return "/rad/dsdef/jsp/success.jsp";
*/
    // fieldNo, fieldName, fieldDesc,
    // fkTableNo, fkTableNo2, fkRelaFieldNo, fkNameFieldNo, fkFilter,
    // codeClass, defaultValue, formatMode, formatRule, errorMsrg,
    // fieldPrecision, fieldScale, dataType, isPrimKey, isIdentity,
    // displayLen, isMustFill

    // YHDBUtility dbUtil = new YHDBUtility();
    // dbConn = dbUtil.getConnection(false, "sampledb");
    return null;
  }
  
  public String testMethod2(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String tableNo = request.getParameter("tableNo");
      String sql = " SELECT SEQ_ID, TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO"
      		       + " , FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE"
      		       + " , FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY"
      		       + " , DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2 "
      		       + " FROM oa_field_dicts d "
      		       + " WHERE d.TABLE_NO = '" + tableNo + "'"
      		       + " ORDER BY FIELD_NO asc ";
      YHPageQueryParamNew queryParam = (YHPageQueryParamNew) YHFOM.build(request.getParameterMap());
      YHPageDataListNew pageDataList = YHPageLoaderNew.loadPageList(dbConn, queryParam, sql);
      String d = pageDataList.toJson();
      PrintWriter pw = response.getWriter();
      pw.println(d);
      pw.flush();
      pw.close();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "submit failed");
      throw ex;
    }
    return null;
  }
  
  public String editDsField(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);   
      dbConn = requestDbConn.getSysDbConn();
      Object obj = YHFOM.build(request.getParameterMap());
      YHORM orm = new YHORM();
      orm.updateSingle(dbConn, obj);        
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"编辑数据成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";  
  }
}

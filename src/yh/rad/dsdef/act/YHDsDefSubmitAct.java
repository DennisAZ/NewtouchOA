package yh.rad.dsdef.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHDsDefSubmitAct {
  private static Logger log = Logger.getLogger(YHDsDefSubmitAct.class);
 
  public String testMethod(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    try {

      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
     String isPrimKey = request.getParameter("isPrimKey_0");
     //System.out.println(isPrimKey+"sssssssssssssssssssss555");
      String tableNo = request.getParameter("tableNo");
      String classTable = (String) request.getParameter("YHDsTable");
      
      YHDsDefFormAct ds = new YHDsDefFormAct();
      Object obj = ds.build(request, classTable, tableNo);
      String classField = (String) request.getParameter("YHDsField");
      int idName = Integer.parseInt(request.getParameter("id"));
      //System.out.println(idName+"sssssssssssssssssssss");
      YHDsDefFormMoreAct dm = new YHDsDefFormMoreAct();
      dm.build(request, classField, idName);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "新增加成功");
//      int fieldNo = Integer.parseInt(request.getParameter("fieldNo"));
//
//      YHDsDefLogic dsdef = new YHDsDefLogic();
//      dsdef.delete(fieldNo, dbConn);
//      dsdef.selectTable(dbConn);
//      dsdef.selectTableField(dbConn, tableNo);
     
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "submit failed");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
    //return "/rad/dsdef/jsp/success.jsp";
    // fieldNo, fieldName, fieldDesc,
    // fkTableNo, fkTableNo2, fkRelaFieldNo, fkNameFieldNo, fkFilter,
    // codeClass, defaultValue, formatMode, formatRule, errorMsrg,
    // fieldPrecision, fieldScale, dataType, isPrimKey, isIdentity,
    // displayLen, isMustFill
    // YHDBUtility dbUtil = new YHDBUtility();
    // dbConn = dbUtil.getConnection(false, "sampledb");
    //return null;
  }
}

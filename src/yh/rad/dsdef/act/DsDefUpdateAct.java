package yh.rad.dsdef.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.rad.dsdef.logic.YHDsDefLogic;

public class DsDefUpdateAct {
  private static Logger log = Logger.getLogger(DsDefUpdateAct.class);


 
  public String testMethod(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    try {

      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      
    
      String tableNo = request.getParameter("tableNo");
      String fieldNo = request.getParameter("fieldNo");
      String id = request.getParameter("id");
     
      String classTable = (String) request.getParameter("YHDsTable");
      
      //System.out.println(classTable);
      
      int idName = Integer.parseInt(request.getParameter("id"));
      YHDsDefFormUpdateAct ds = new YHDsDefFormUpdateAct();
      Object obj = ds.build(request, classTable);
      
      String classField = (String) request.getParameter("YHDsField");
      
      
      //System.out.println(idName+":"+"xxxxxxxxxxxxxxxx");
      
      //////////////////////////////////////////////
      YHDsDefLogic df = new YHDsDefLogic();
      //df.delete(fieldNo, dbConn);
      
//      YHDsDefFormMoreUpdateAct dm = new YHDsDefFormMoreUpdateAct();
//      dm.build(request, classField, idName);

     
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "submit failed");
      throw ex;
    }
    return "/rad/dsdef/jsp/success.jsp";

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

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

public class YHDsDefDeleteFieldAct {
  private static Logger log = Logger.getLogger(YHDsDefDeleteFieldAct.class);
 
  public String testMethod(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    try {

      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String tableNoField = request.getParameter("tableNoField");
      String seqId = request.getParameter("seqId");
      YHDsDefLogic dsdef = new YHDsDefLogic();
      dsdef.delete(seqId, dbConn);
      //YHORM orm = new YHORM();
      //orm.delete(dbConn, obj);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "submit failed");
      throw ex;
    }
    return "/raw/cy/gridDebug.html";
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

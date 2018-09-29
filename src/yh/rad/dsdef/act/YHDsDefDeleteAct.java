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

public class YHDsDefDeleteAct {
  private static Logger log = Logger.getLogger(YHDsDefDeleteAct.class);
 
  public String testMethod(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    try {

      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String tableSeqId = request.getParameter("seqId");
      String tableNoF = request.getParameter("tableNoF");
      //System.out.println(tableSeqId+":"+"xxxxxxxxxxxxxxxxxx");
      int obj = Integer.parseInt(request.getParameter("seqId"));
      //System.out.println(obj+":"+"cccccccccccccc");

      YHDsDefLogic dsdef = new YHDsDefLogic();
      dsdef.deleteUpdate(tableSeqId, dbConn, tableNoF);
    //  YHORM orm = new YHORM();
      //orm.delete(dbConn, obj);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "submit failed");
      throw ex;
    }
    //return "/raw/cy/gridDebug.html";
    return "/core/inc/rtjson.jsp";
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

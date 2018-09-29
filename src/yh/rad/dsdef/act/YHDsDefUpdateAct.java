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

public class YHDsDefUpdateAct {
  private static Logger log = Logger.getLogger(YHDsDefUpdateAct.class);
 
  public String testMethod(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    try {

      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
     
      String tableNo = request.getParameter("tableNoDiv");
      String tableNoField = request.getParameter("tableNo");
      //System.out.println(tableNoField+"5656565656565");
      
      String classTable = (String) request.getParameter("YHDsTable");
      //System.out.println(classTable);
      YHDsDefFormUpdateAct ds = new YHDsDefFormUpdateAct();
    //删除子表
      String tableNoDiv = request.getParameter("tableNoDiv");
      //System.out.println(tableNoDiv+"5656565656565222222222222222222");
      YHDsDefLogic td = new YHDsDefLogic();
      //td.delete(tableNoDiv, dbConn);
      td.delete(tableNoDiv, dbConn);
      
      
      //修改主表
      Object obj = ds.build(request, classTable);
      //System.out.println("gggggggggggggggggggggggggggggggggggggg");
      
      
      int idN = Integer.parseInt(request.getParameter("id"));
      //System.out.println(idN+"zzzzzzzzzzzzz");
      String tableNo1  = request.getParameter("tableNo");
      YHDsDefFormMoreAct dm = new YHDsDefFormMoreAct();
      String classField = (String) request.getParameter("YHDsField");
      //System.out.println(classField+"yyyyyyyyyyyyyyyyyyyy");
      dm.build(request, classField, idN);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改成功");
      //String classField = (String) request.getParameter("YHDsField");
      //YHDsDefFormMoreUpdateAct dm = new YHDsDefFormMoreUpdateAct();
      //dm.build(request, classField, idName);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "submit failed");
      throw ex;
    }
    //return "/rad/dsdef/jsp/success.jsp";
    return "/core/inc/rtjson.jsp";
    //return "/raw/cy/gridDebug.html";
  }
}

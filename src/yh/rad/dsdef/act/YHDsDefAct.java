package yh.rad.dsdef.act;

import java.sql.Connection;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHDsTable;
import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHDTJ;
import yh.core.util.db.YHORM;
import yh.rad.dsdef.logic.YHDsDefJsonlogic;
import yh.rad.dsdef.logic.YHDsDefLogic;

public class YHDsDefAct {
  private static Logger log = Logger.getLogger(YHDsDefAct.class);

  public String testMethod(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      response.setCharacterEncoding(YHConst.DEFAULT_CODE);
      PrintWriter out = null;
      try {
        out = response.getWriter();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      int total = 0;
      String tableNo = request.getParameter("tableNo");
      YHDsTable st = new YHDsTable();
      YHDsDefJsonlogic json = new YHDsDefJsonlogic();
      YHDsDefLogic ddl = new YHDsDefLogic();
      List list = ddl.selectTable(dbConn, total);
      total = ddl.getTotles();
      // List list1 = ddl.selectTableField(dbConn, tableNo);
      Object ob = null;
      StringBuffer jsons = new StringBuffer(" { \"total\":" + total + ","
          + "\"records\":[");
      for (Iterator its = list.iterator(); its.hasNext();) {
        ob = its.next();
        try {
          String s = (json.toJson(ob)).toString();
          jsons.append(s);
          if (its.hasNext()) {
            jsons.append(",");
          }
        } catch (Exception e) {
          // TODO Auto-generated catch block
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
          request.setAttribute(YHActionKeys.RET_MSRG, "submit failed");
          throw e;
        }
      }
      jsons.append("]}");
      //System.out.println(jsons.toString().trim());
      out.println(jsons.toString().trim());
      out.flush();
      out.close();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "submit failed");
      throw ex;
    }
    return null;
  }

  public String insertDsDef(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String tableNo = request.getParameter("tableNo");
      YHDsDefLogic ddl = new YHDsDefLogic();
      if (ddl.existsTableNo(dbConn, tableNo, "0")) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "表编码不能重复");
        return "/core/inc/rtjson.jsp";
      } else {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "添加成功");
      }
      String classTable = (String) request.getParameter("YHDsTable");
      YHDsDefFormAct ds = new YHDsDefFormAct();
      Object obj = ds.build(request, classTable, tableNo);
      String classField = (String) request.getParameter("YHDsField");
      int idName = Integer.parseInt(request.getParameter("id"));
      YHDsDefFormMoreAct dm = new YHDsDefFormMoreAct();
      dm.build(request, classField, idName);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "新增加成功");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "submit failed");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String existsTableNo(HttpServletRequest request, HttpServletResponse response) throws Exception{
    try {
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String tableNo = request.getParameter("tableNo");
      String seqId = request.getParameter("seqId");
      YHDsDefLogic ddl = new YHDsDefLogic();
      boolean isExists = ddl.existsTableNo(dbConn, tableNo, seqId);
      String isExistStr = "0";
      if(isExists){
        isExistStr = "1";
      }
      String data = "{\"isExistsTableNo\":\"" + isExistStr + "\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "submit failed");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteDsDef(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String tableSeqId = request.getParameter("seqId");
      String tableNoF = request.getParameter("tableNoF");
      int obj = Integer.parseInt(request.getParameter("seqId"));
      YHDsDefLogic dsdef = new YHDsDefLogic();
      dsdef.deleteDsDef(tableSeqId, dbConn, tableNoF);
      YHORM orm = new YHORM();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "submit failed");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public String updateDsDef(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String classTable = (String) request.getParameter("YHDsTable");
      
      YHDsDefFormUpdateAct ds = new YHDsDefFormUpdateAct();
      // 删除子表
      String tableNoDiv = request.getParameter("tableNoDiv");
      YHDsDefLogic td = new YHDsDefLogic();
      td.delete(tableNoDiv, dbConn);
      // 修改主表
      Object obj = ds.build(request, classTable);
      int idN = Integer.parseInt(request.getParameter("id"));
      YHDsDefFormMoreAct dm = new YHDsDefFormMoreAct();
      String classField = (String) request.getParameter("YHDsField");
      // 插入子表
      dm.build(request, classField, idN);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改成功");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "submit failed");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public String listDsDef(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    // 1.得到tabName,pageNum,pageRows
    response.setCharacterEncoding("UTF-8");
    // 2.通过tabName,pageNum,pageRows得到json数据
    String tabNo = request.getParameter("tabNo");
    String pageNumStr = request.getParameter("pageNum");
    String pageRowsStr = request.getParameter("pageRows");
    int pageNum = Integer.parseInt(pageNumStr);
    int pageRows = Integer.parseInt(pageRowsStr);
    //System.out.println(pageNum);
    YHDTJ dtj = new YHDTJ();
    try {
      YHORM t = new YHORM();
      //System.out.println("ddddd");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      String[] filters = new String[] { "TABLE_NO = '" + tabNo + "'" };
//      StringBuffer d = dtj.dataToJson(dbConn, tabNo, pageNum, pageRows);
  //    YHDsTable dsTable = (YHDsTable) t.loadObj(dbConn, YHDsTable.class,
   //       filters);
      dbConn.close();
      PrintWriter pw = response.getWriter();
  //    pw.println(d.toString());
      pw.flush();
      pw.close();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // 3.将json数据输出到前端    return null;
  }
}

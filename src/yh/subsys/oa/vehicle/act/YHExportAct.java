package yh.subsys.oa.vehicle.act;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.global.YHBeanKeys;
import yh.core.util.YHUtility;
import yh.subsys.oa.vehicle.data.YHVehicleUsage;
import yh.subsys.oa.vehicle.logic.YHExportLogic;

public class YHExportAct {
  /**
   * lz
   * 
   * */
  public String exportXls(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    OutputStream ops = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String fileName = URLEncoder.encode("车辆使用记录.xls","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      ops = response.getOutputStream();
      //接受参数
      String vuStatus = request.getParameter("vuStatus");
      String vId = request.getParameter("vId");
      String vuDriver = request.getParameter("vuDriver");
      String vuRequestDateMin = request.getParameter("vuRequestDateMin");
      String vuRequestDateMax = request.getParameter("vuRequestDateMax");
      String vuUser = request.getParameter("vuUser");
      String vuDept = request.getParameter("vuDept");
      String vuStartMin = request.getParameter("vuStartMin");
      String vuStartMax = request.getParameter("vuStartMax");
      String vuEndMin = request.getParameter("vuEndMin");
      String vuEndMax = request.getParameter("vuEndMax");
      String vuProposer = request.getParameter("vuProposer");
      String vuReason = request.getParameter("vuReason");
      String vuRemark = request.getParameter("vuRemark");
      String vuOperator = request.getParameter("vuOperator");
      
      YHVehicleUsage usage = new YHVehicleUsage();
      usage.setVuStatus(vuStatus);
      usage.setVId(vId);
      usage.setVuDriver(vuDriver);
      usage.setVuUser(vuUser);
      usage.setVuDept(vuDept);
      usage.setVuProposer(vuProposer);
      usage.setVuReason(vuReason);
      usage.setVuRemark(vuRemark);
      usage.setVuOperator(vuOperator);
      if (!YHUtility.isNullorEmpty(vuRequestDateMin)) {
        usage.setVuRequestDate(YHUtility.parseDate(vuRequestDateMin));
      }
      if (!YHUtility.isNullorEmpty(vuStartMin)) {
        usage.setVuStart(YHUtility.parseDate(vuStartMin));
      }
      if (!YHUtility.isNullorEmpty(vuEndMin)) {
        usage.setVuEnd(YHUtility.parseDate(vuEndMin));
      }
      //返回LIST集合
      List<YHVehicleUsage> usageList = YHExportLogic.vehicleAll(dbConn, usage,vuRequestDateMax,vuStartMax,vuEndMax);
      ArrayList<YHDbRecord > dbL = YHExportLogic.getDbRecord(usageList,dbConn);
      YHJExcelUtil.writeExc(ops, dbL);
      //YHCSVUtil.CVSWrite(ops, dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
      ops.close();
    }
    return null;
  }
}

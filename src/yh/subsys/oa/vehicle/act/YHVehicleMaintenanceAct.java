package yh.subsys.oa.vehicle.act;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.vehicle.data.YHVehicleMaintenance;
import yh.subsys.oa.vehicle.logic.YHVehicleMaintenanceLogic;

public class YHVehicleMaintenanceAct {
  /**
   * 
   * 添加车辆维护
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addVehicleMainten(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVehicleMaintenance vcMaintenance = new YHVehicleMaintenance();
      if(YHUtility.isInteger(request.getParameter("vId"))){
        vcMaintenance.setVId(Integer.parseInt(request.getParameter("vId")));
        if(YHUtility.isNumber(request.getParameter("vmFee"))){
          vcMaintenance.setVmFee(Double.parseDouble(request.getParameter("vmFee")));
        }
  
        vcMaintenance.setVmPerson(request.getParameter("vmPerson"));
        vcMaintenance.setVmReason(request.getParameter("vmReason"));
        vcMaintenance.setVmRemark(request.getParameter("vmRemark"));
        vcMaintenance.setVmType(request.getParameter("vmType"));
        if(!YHUtility.isNullorEmpty(request.getParameter("vmRequestDate"))){
          vcMaintenance.setVmRequestDate(YHUtility.parseDate("yyyy-MM-dd",request.getParameter("vmRequestDate")));
        }
        YHVehicleMaintenanceLogic tvml = new YHVehicleMaintenanceLogic();
        tvml.addMaintenance(dbConn, vcMaintenance);
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, "data");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询车辆维护情况
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectVehicleMaintenance(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVehicleMaintenance vcMaintenance = new YHVehicleMaintenance();
      YHORM orm = new YHORM();
      String vId = request.getParameter("vId");
      String vmBeginDate = request.getParameter("vmBeginDate");
      String vmEndDate = request.getParameter("vmEndDate");
      String vmReason = request.getParameter("vmReason");
      String vmType = request.getParameter("vmType");
      String vmPerson = request.getParameter("vmPerson");
      String vmFeeMin = request.getParameter("vmFeeMin");
      String vmFeeMax = request.getParameter("vmFeeMax");
      String vmRemark = request.getParameter("vmRemark");
      String orderType = request.getParameter("orderType");
      if(YHUtility.isNullorEmpty(orderType)){
        orderType = "";
      }
      YHVehicleMaintenanceLogic tvml = new YHVehicleMaintenanceLogic();
      ArrayList<Map<String,String>>  vehicleMaintenList = (ArrayList)tvml.selectMaintenance(dbConn,vId,vmBeginDate,vmEndDate,vmReason,vmType,vmPerson,vmFeeMin,vmFeeMax,vmRemark,orderType );
      String feeTotals = "";
      Map<String,String> totalMap = vehicleMaintenList.get(vehicleMaintenList.size()-1);
      feeTotals = totalMap.get("total");
      
      YHVehicleAct vehicleAct = new YHVehicleAct();
      vehicleMaintenList.remove(vehicleMaintenList.size()-1);
      String data = vehicleAct.getJson(vehicleMaintenList);
      
      //request.setAttribute("feeTotal", feeTotals);
     // request.setAttribute("vehicleMaintenList", vehicleMaintenList);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, feeTotals);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
    //return "/subsys/oa/vehicle/maintenance/selectvehiclemaintenance.jsp";
  }
  /**
   * 查询车辆维护情况BY VEHICLE(ID)
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectVehicleMaintenanceByVId(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVehicleMaintenance vcMaintenance = new YHVehicleMaintenance();
      YHORM orm = new YHORM();
      String vId = request.getParameter("vId");
      String orderType = request.getParameter("orderType");
      if(YHUtility.isNullorEmpty(orderType)){
        orderType = "";
      }
      String feeTotals = "";
      String data = "";
      YHVehicleMaintenanceLogic tvml = new YHVehicleMaintenanceLogic();
      if(!YHUtility.isNullorEmpty(vId)){
        ArrayList<Map<String,String>>  vehicleMaintenList = (ArrayList)tvml.selectMaintenanceByVId(dbConn,vId,orderType );
        Map<String,String> totalMap = vehicleMaintenList.get(vehicleMaintenList.size()-1);
        feeTotals = totalMap.get("total");
        YHVehicleAct vehicleAct = new YHVehicleAct();
        vehicleMaintenList.remove(vehicleMaintenList.size()-1);
        data = vehicleAct.getJson(vehicleMaintenList);
      }else{
        data = "[]";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, feeTotals);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 
   * 删除车辆维护  根据SEQ_ID
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteVehicleMiantenById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVehicleMaintenance vcMaintenance = new YHVehicleMaintenance();
      String seqId = request.getParameter("seqId");
      if(!YHUtility.isNullorEmpty(seqId)){
        YHORM orm = new YHORM();
        orm.deleteSingle(dbConn, YHVehicleMaintenance.class, Integer.parseInt(seqId));
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, "");
      }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询车辆维护情况ById
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectVehicleMaintenanceById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVehicleMaintenance vcMaintenance = new YHVehicleMaintenance();
      YHORM orm = new YHORM();
      String seqId = request.getParameter("seqId");
      String data = "";
      if(YHUtility.isInteger(seqId)){
        YHVehicleMaintenanceLogic tvml = new YHVehicleMaintenanceLogic();
        YHVehicleMaintenance mainten = tvml.selectMaintenanceById(dbConn, Integer.parseInt(seqId));
        data = YHFOM.toJson(mainten).toString();
      }
      if(data.equals("")){
        data = "{}";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
      }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * 更新车辆维护 根据SEQ_ID
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateVehicleMaimtenById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVehicleMaintenance vcMaintenance = new YHVehicleMaintenance();
      if(YHUtility.isInteger(request.getParameter("seqId"))){
        vcMaintenance.setSeqId(Integer.parseInt(request.getParameter("seqId")));
        vcMaintenance.setVId(Integer.parseInt(request.getParameter("vId")));
        if(YHUtility.isNumber(request.getParameter("vmFee"))){
          vcMaintenance.setVmFee(Double.parseDouble(request.getParameter("vmFee")));
        }
        vcMaintenance.setVmPerson(request.getParameter("vmPerson"));
        vcMaintenance.setVmReason(request.getParameter("vmReason"));
        vcMaintenance.setVmRemark(request.getParameter("vmRemark"));
        vcMaintenance.setVmType(request.getParameter("vmType"));
        if(!YHUtility.isNullorEmpty(request.getParameter("vmRequestDate"))){
          vcMaintenance.setVmRequestDate(YHUtility.parseDate("yyyy-MM-dd",request.getParameter("vmRequestDate")));
        }
        YHVehicleMaintenanceLogic tvml = new YHVehicleMaintenanceLogic();
        tvml.updateMaintenance(dbConn, vcMaintenance);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "更新成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, "data");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * csv导出
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String exportCSV(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    response.setCharacterEncoding(YHConst.CSV_FILE_CODE);
    InputStream is = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);  
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHVehicleMaintenance vcMaintenance = new YHVehicleMaintenance();
      YHORM orm = new YHORM();
      String vId = request.getParameter("vId");
      String vmBeginDate = request.getParameter("vmBeginDate");
      String vmEndDate = request.getParameter("vmEndDate");
      String vmReason = request.getParameter("vmReason");
      String vmType = request.getParameter("vmType");
      String vmPerson = request.getParameter("vmPerson");
      String vmFeeMin = request.getParameter("vmFeeMin");
      String vmFeeMax = request.getParameter("vmFeeMax");
      String vmRemark = request.getParameter("vmRemark");
      String orderType = request.getParameter("orderType");
      if(YHUtility.isNullorEmpty(orderType)){
        orderType = "";
      }
      String fileName  = URLEncoder.encode("车辆维护记录.csv","utf-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control", "private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges", "bytes");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
      YHVehicleMaintenanceLogic tvml = new YHVehicleMaintenanceLogic();
      ArrayList<YHDbRecord> dbL = tvml.selectMaintenanceCvs(dbConn, vId, vmBeginDate, vmEndDate, vmReason, vmType, vmPerson, vmFeeMin, vmFeeMax, vmRemark, "");

      YHCSVUtil.CVSWrite(response.getWriter(), dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
    return null;
  }
  /**
   * csv导出
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String exportCSVByVId(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    response.setCharacterEncoding(YHConst.CSV_FILE_CODE);
    InputStream is = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);  
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHVehicleMaintenance vcMaintenance = new YHVehicleMaintenance();
      YHORM orm = new YHORM();
      String vId = request.getParameter("vId");
      String fileName  = URLEncoder.encode("车辆维护记录.csv","utf-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control", "private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges", "bytes");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
      ArrayList<YHDbRecord> dbL = new ArrayList<YHDbRecord>();
      if(YHUtility.isInteger(vId)){
        YHVehicleMaintenanceLogic tvml = new YHVehicleMaintenanceLogic();
        dbL = tvml.selectMaintenanceCvsByVId(dbConn, vId);
      }
      YHCSVUtil.CVSWrite(response.getWriter(), dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
    return null;
  }
}

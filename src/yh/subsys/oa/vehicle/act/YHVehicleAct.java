package yh.subsys.oa.vehicle.act;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.codeclass.data.YHCodeItem;
import yh.core.codeclass.logic.YHCodeClassLogic;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.vehicle.data.YHVehicle;
import yh.subsys.oa.vehicle.data.YHVehicleMaintenance;
import yh.subsys.oa.vehicle.data.YHVehicleUsage;
import yh.subsys.oa.vehicle.logic.YHVehicleLogic;
import yh.subsys.oa.vehicle.logic.YHVehicleMaintenanceLogic;
import yh.subsys.oa.vehicle.logic.YHVehicleUsageLogic;

public class YHVehicleAct {
  /**
   * 
   * 添加车辆
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addVehicle(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFileUploadForm fileForm = new YHFileUploadForm();
      SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyMM");
      fileForm.parseUploadRequest(request);
      YHVehicle vehicle = new YHVehicle();
      vehicle.setVNum(fileForm.getParameter("vNum"));
      vehicle.setVModel(fileForm.getParameter("vModel"));
      vehicle.setVEngineNum(fileForm.getParameter("vEngineNum"));
      vehicle.setVDriver(fileForm.getParameter("vDriver"));
      vehicle.setVType(fileForm.getParameter("vType"));
      if(!YHUtility.isNullorEmpty(fileForm.getParameter("vDate"))){
        vehicle.setVDate(YHUtility.parseDate("yyyy-MM-dd",fileForm.getParameter("vDate")));
      }
      if(!YHUtility.isNullorEmpty(fileForm.getParameter("vPrice"))){
        vehicle.setVPrice(fileForm.getParameter("vPrice"));
      }
      vehicle.setVStatus(fileForm.getParameter("vStatus"));
      vehicle.setVRemark(fileForm.getParameter("vRemark"));
      vehicle.setUseingFlag("0");
      if(!YHUtility.isNullorEmpty(fileForm.getParameter("insuranceDate"))){
        vehicle.setInsuranceDate(YHUtility.parseDate("yyyy-MM-dd",fileForm.getParameter("insuranceDate")));
      }
      vehicle.setBeforeDay(Integer.parseInt(fileForm.getParameter("beforeDay")));
      vehicle.setInsuranceFlag(0);

      String attachmentId = "";
      String attachmentName = "";
      Iterator<String> iKeys = fileForm.iterateFileFields();
      String filePath = YHSysProps.getAttachPath()   + File.separator + "vehicle"   + File.separator   + dateFormat2.format(new Date()); // YHSysProps.getAttachPath()
      while (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        String fileName = fileForm.getFileName(fieldName);
        String regName = fileName;
        if (YHUtility.isNullorEmpty(fileName)) {
          continue;
        }
        YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
        String rand = emul.getRandom();
        attachmentId =  dateFormat2.format(new Date()) + "_" + attachmentId + rand;
        attachmentName = attachmentName + fileName;
        fileName = rand + "_" + fileName;
        fileForm.saveFile(fieldName, filePath   + File.separator +fileName);
      }
      vehicle.setAttachmentId(attachmentId);
      vehicle.setAttachmentName(attachmentName);
      YHVehicleLogic tsoval = new YHVehicleLogic();
      tsoval.addVehicle(dbConn, vehicle);
      String path = request.getContextPath();
      response.sendRedirect(path + "/subsys/oa/vehicle/manage/addVehicle.jsp?type=1");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, "{}");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "";
  }
  /**
   * 查询所有车辆
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectVehicle(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String data =  "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVehicleLogic vehicleLogic = new YHVehicleLogic();
      ArrayList<Map<String,String>> vehicleList = vehicleLogic.selectVehicle(dbConn,"");
      data = data + getJson(vehicleList);
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出主菜单和子菜单项的数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询所有可用车辆 V_STATUS = 0

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectUseVehicle(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVehicleLogic vehicleLogic = new YHVehicleLogic();
      String[] str = {"V_STATUS = 0"};
      ArrayList<YHVehicle> vehicleList = vehicleLogic.selectVehicle(dbConn,str);
      String data = "[";
      for (int i = 0; i < vehicleList.size(); i++) {
        data = data + YHFOM.toJson(vehicleList.get(i)) + ",";
      }
      if(vehicleList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出主菜单和子菜单项的数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询所有未使用的车辆 SHOW_FLAG = 0  

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectUseingVehicle(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVehicleLogic vehicleLogic = new YHVehicleLogic();
      ArrayList<Map<String,String>> vehicleList = vehicleLogic.selectVehicle(dbConn,"0");
      String data =  getJson(vehicleList);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出主菜单和子菜单项的数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 事务分页查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectVehiclePage(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHVehicleLogic dl = new YHVehicleLogic();
      
      String seqId = request.getParameter("seqId");
      String data = dl.toSearchData(dbConn,request.getParameterMap(),seqId);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  /**
   * 查询车辆  根据SEQ_ID
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectVehicleById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String data =  "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      String seqId = request.getParameter("seqId");
      if(!YHUtility.isNullorEmpty(seqId)){
        YHVehicle vehicle = (YHVehicle) orm.loadObjSingle(dbConn, YHVehicle.class, Integer.parseInt(seqId));
        if(vehicle!=null){
          YHPersonLogic personLogic = new YHPersonLogic();
          String vDriverName = "";
          if(!YHUtility.isNullorEmpty(vehicle.getVDriver())&&YHUtility.isInteger(vehicle.getVDriver())){
            vDriverName = personLogic.getNameBySeqIdStr(vehicle.getVDriver(), dbConn);
          }
          String carUserName = "";
          if(YHUtility.isInteger(vehicle.getCarUser())){
            carUserName = personLogic.getNameBySeqIdStr(vehicle.getCarUser(), dbConn);
          }
          data = data+YHFOM.toJson(vehicle).toString().substring(0, YHFOM.toJson(vehicle).toString().length()-1) + ",vDriverName:\"" + vDriverName + "\",carUserName:\"" + carUserName +"\"}" ;
        }

      }
      if(YHUtility.isNullorEmpty(data)){
        data = "{}";
      }
      String filePath = YHSysProps.getAttachPath() ;
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,filePath.replace("\\", "/"));
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询车辆  根据SEQ_ID
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryVehicleById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String data =  "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHVehicleLogic vehicleLogic = new YHVehicleLogic();
      if(!YHUtility.isNullorEmpty(seqId)){
        Map<String,String> map = vehicleLogic.selectVehicleById(dbConn,Integer.parseInt(seqId));
        data = data + getJson(map);
      }else{
        data = "{}";
      }
      String filePath = YHSysProps.getAttachPath() ;
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,filePath.replace("\\", "/"));
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
   * 删除车辆  根据SEQ_ID
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteVehicleById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVehicle vehicle = new YHVehicle();
      String seqId = request.getParameter("seqId");
      YHORM orm = new YHORM();
      if(YHUtility.isInteger(seqId)){
        orm.deleteSingle(dbConn, YHVehicle.class, Integer.parseInt(seqId));
        //删除方法,删除车辆维护的记录
        YHVehicleMaintenanceLogic.deleteMaintenanceByVId(dbConn, seqId);
        //lz-删除方法,删除车辆申请的记录
        YHVehicleUsageLogic.deleteVID(dbConn,seqId);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功！");
      request.setAttribute(YHActionKeys.RET_DATA, "{}");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * 删除车辆  根据SEQ_ID的字符串以逗号分隔
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteVehicle(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVehicle vehicle = new YHVehicle();
      String seqId = request.getParameter("seqIds");
      YHVehicleLogic vehicleLogic = new YHVehicleLogic();
      if(!YHUtility.isNullorEmpty(seqId)){
        vehicleLogic.deleteVehicle(dbConn, seqId);
        //删除维修记录
        if(seqId.endsWith(",")){
          seqId = seqId.substring(0, seqId.length()-1);
        }
        YHVehicleMaintenanceLogic.deleteMaintenanceByVIds(dbConn, seqId);
        
        //lz-删除方法
        YHVehicleUsageLogic.deleteVID(dbConn,seqId);
        //System.out.println(seqId);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功！");
      request.setAttribute(YHActionKeys.RET_DATA, "{}");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 
   * 更新车辆  根据SEQ_ID
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateVehicle(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFileUploadForm fileForm = new YHFileUploadForm();
      SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyMM");
      fileForm.parseUploadRequest(request);
      YHVehicleLogic tsoval = new YHVehicleLogic();
      if(YHUtility.isInteger(fileForm.getParameter("seqId"))){
        YHVehicle vehicle = tsoval.selectVehicleById(dbConn, fileForm.getParameter("seqId"));
        if(vehicle!=null){
          vehicle.setSeqId(Integer.parseInt(fileForm.getParameter("seqId")));
          vehicle.setVNum(fileForm.getParameter("vNum"));
          vehicle.setVModel(fileForm.getParameter("vModel"));
          vehicle.setVEngineNum(fileForm.getParameter("vEngineNum"));
          vehicle.setVDriver(fileForm.getParameter("vDriver"));
          vehicle.setVType(fileForm.getParameter("vType"));
          if(!YHUtility.isNullorEmpty(fileForm.getParameter("vDate"))){
            vehicle.setVDate(YHUtility.parseDate("yyyy-MM-dd",fileForm.getParameter("vDate")));
          }
          if(!YHUtility.isNullorEmpty(fileForm.getParameter("vPrice"))){
            vehicle.setVPrice(fileForm.getParameter("vPrice"));
          }
          vehicle.setVStatus(fileForm.getParameter("vStatus"));
          vehicle.setVRemark(fileForm.getParameter("vRemark"));
          if(!YHUtility.isNullorEmpty(fileForm.getParameter("insuranceDate"))){
            vehicle.setInsuranceDate(YHUtility.parseDate("yyyy-MM-dd",fileForm.getParameter("insuranceDate")));
          }
          vehicle.setBeforeDay(Integer.parseInt(fileForm.getParameter("beforeDay")));
          String attachmentId = "";
          String attachmentName = "";
          Iterator<String> iKeys = fileForm.iterateFileFields();
          String filePath = YHSysProps.getAttachPath()    + File.separator + "vehicle"    + File.separator  + dateFormat2.format(new Date()); // YHSysProps.getAttachPath()
          while (iKeys.hasNext()) {
            String fieldName = iKeys.next();
            String fileName = fileForm.getFileName(fieldName);
            String regName = fileName;
            if (YHUtility.isNullorEmpty(fileName)) {
              continue;
            }
            YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
            String rand = emul.getRandom();
            attachmentId =  dateFormat2.format(new Date()) + "_" + attachmentId + rand;
            attachmentName = attachmentName + fileName;
            fileName = rand + "_" + fileName;
            fileForm.saveFile(fieldName, filePath   + File.separator + fileName);
          }
          vehicle.setAttachmentId(attachmentId);
          vehicle.setAttachmentName(attachmentName);
          tsoval.updateVehicle(dbConn, vehicle);
        }
      }
      String path = request.getContextPath();
      response.sendRedirect(path + "/subsys/oa/vehicle/manage/updateVehicle.jsp?type=1");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, "data");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "";
  }
  public String getJson(List<Map<String,String>> mapList){
    StringBuffer buffer=new StringBuffer("["); 
    for(Map<String, String> equipmentsMap:mapList){ 
      buffer.append("{"); 
      Set<String>keySet=equipmentsMap.keySet(); 
      for(String mapStr:keySet){ 
        //System.out.println(mapStr + ":>>>>>>>>>>>>" + equipmentsMap.get(mapStr)); 
        String name=equipmentsMap.get(mapStr); 
        if(name!=null){
          name =name.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
        }
        /* if(mapStr!=null&&mapStr.equals("seqId")){

      }*/
        buffer.append( mapStr+":\"" + (name==null? "":name) + "\","); 
      } 
      buffer.deleteCharAt(buffer.length()-1); 
      buffer.append("},"); 
    }
    buffer.deleteCharAt(buffer.length()-1); 
    if (mapList.size()>0) { 
      buffer.append("]"); 
    }else { 
      buffer.append("[]"); 
    }
    String data = buffer.toString();
    return data;
  }
  public String getJson(Map<String,String> map){
    StringBuffer buffer=new StringBuffer("{"); 
    Set<String>keySet=map.keySet(); 
    for(String mapStr:keySet){ 
      String name=map.get(mapStr); 
      if(name!=null){
        name = YHUtility.encodeSpecial(name);
      }
      /* if(mapStr!=null&&mapStr.equals("seqId")){

      }*/
      buffer.append( mapStr+":\"" + (name==null? "":name) + "\","); 
    } 
    buffer.deleteCharAt(buffer.length()-1); 
    buffer.append("}"); 
    String data = buffer.toString();
    return data;
  }
  /**
   * 得到车辆的所有类型
   * 根据seqId（codeClass） 得到所有的codeItem
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCodeItem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHCodeClassLogic codeLogic = new YHCodeClassLogic();
      String data = "[";
      List<YHCodeItem> itemList = new ArrayList<YHCodeItem>();
      itemList = codeLogic.getCodeItem(dbConn, "VEHICLE_TYPE");
      for (int i = 0; i < itemList.size(); i++) {
        YHCodeItem item = itemList.get(i);
        data = data + YHFOM.toJson(item) + ",";
      }
      if (itemList.size() > 0) {
        data = data.substring(0, data.length() - 1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 得到车辆的所有类型

   * 根据seqId（codeClass） 得到所有的codeItem
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCodeItem2(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHCodeClassLogic codeLogic = new YHCodeClassLogic();
      String data = "[";
      List<YHCodeItem> itemList = new ArrayList<YHCodeItem>();
      itemList = codeLogic.getCodeItem(dbConn, "VEHICLE_REPAIR_TYPE");
      for (int i = 0; i < itemList.size(); i++) {
        YHCodeItem item = itemList.get(i);
        data = data + YHFOM.toJson(item) + ",";
      }
      if (itemList.size() > 0) {
        data = data.substring(0, data.length() - 1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 得到所有车辆在未来7天使用情况
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getVehicleUsageInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVehicleLogic vehicleLogic = new YHVehicleLogic();
      YHVehicleUsageLogic usageLogic = new YHVehicleUsageLogic();
      //得到所有车辆
      String[] str = {};
      ArrayList<YHVehicle> vehicleList = vehicleLogic.selectVehicle(dbConn,str);

      //得到未来7天
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM-dd");
      SimpleDateFormat dateFormat3 = new SimpleDateFormat("MM-dd HH:mm");
      SimpleDateFormat dateFormat4 = new SimpleDateFormat("HH:mm");
      Date curDate = new Date();

      List daysList = new ArrayList();
      String days = "";
      String[] weekDays = {"周天","周一","周二","周三","周四","周五","周六"};
      Calendar calendar = new GregorianCalendar();
      String severDayTr = "<tr class='TableHeader'><td width='10%' align='center' nowrap>未来7天</td>";
      for(int i = 0;i<7;i++){
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE,+i) ;
        Date dateTemp = calendar.getTime();
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        String dateTempStr = dateFormat2.format(dateTemp);
        daysList.add(dateFormat.format(dateTemp));
        severDayTr = severDayTr + "<td  width='10%' align='center' nowrap>" + dateTempStr + "(" + weekDays[week-1] + ")</td>"; 
      }
      severDayTr = severDayTr + "</tr>";
      String trStr = "";
      for (int i = 0; i < vehicleList.size(); i++) {//循环所有车辆
        YHVehicle vehicle = vehicleList.get(i);
        int vId = vehicle.getSeqId();
        String vModel =  vehicle.getVModel();
        if(YHUtility.isNullorEmpty( vehicle.getVModel())){
          vModel = "";
        }
        trStr  = trStr + "<tr ><td width='20%'  class='TableData'>" +YHUtility.encodeSpecial(vModel)+"</td>" ;//循环TR
        for (int j = 0; j < daysList.size(); j++) {//循环未来7天
          String date =  (String) daysList.get(j);

          //查询出这辆车在这天出车情况
          String[] str2 = {"VU_STATUS <> '3'", "VU_STATUS <> '4'", "V_ID='" + vId + "'","((" + YHDBUtility.getDateFilter("VU_START", date, ">") + " and " + YHDBUtility.getDateFilter("VU_START", date + " 23:59:59", "<") + ")"
              + " or (" + YHDBUtility.getDateFilter("VU_END", date, ">") + " and " + YHDBUtility.getDateFilter("VU_END", date + " 23:59:59", "<") + ")"
              + " or (" + YHDBUtility.getDateFilter("VU_START", date, "<") + " and " + YHDBUtility.getDateFilter("VU_END", date + " 23:59:59", ">") + ")) order by VU_START"};
          List<YHVehicleUsage>  usageList = usageLogic.selectVUByVuStatus(dbConn, str2);
          if(usageList.size()>0){
            trStr = trStr + "<td width='10%' nowrap >";//循环TD
            trStr = trStr + "<table style='border:1px #7b7b7b solid; border-collapse:collapse;'  width=100% height=100%><tr>";
            for (int k = 0; k < usageList.size(); k++) {//循环查询出来的车辆使用记录
              YHVehicleUsage usage = usageList.get(k);
              String vuStatusColorType = "";
              String vuStatus = usage.getVuStatus();
              if(!YHUtility.isNullorEmpty(vuStatus)){
                if(vuStatus.equals("0")){
                  vuStatusColorType =  "#ff33ff";
                }
                if(vuStatus.equals("1")){
                  vuStatusColorType =  "#00ff00";
                }
                if(vuStatus.equals("2")){
                  vuStatusColorType =  "#ff0000";
                }
              }
              String vuStart = "";//MM-dd
              String vuEnd = "";
              String vuStartMMdd = "";//HH:mm
              String vuEndMMdd = "";

              String curDateStr = dateFormat.format(curDate);//yyyy-MM-dd
              String vuStartY = "";
              String vuEndY = "";
              if(usage.getVuStart()!=null){
                vuStart = dateFormat3.format(usage.getVuStart());
                vuStartMMdd = dateFormat4.format(usage.getVuStart());
                vuStartY = dateFormat.format(usage.getVuStart());
              }
              if(usage.getVuEnd()!=null){
                vuEnd = dateFormat3.format(usage.getVuEnd());
                vuEndMMdd = dateFormat4.format(usage.getVuEnd());
                vuEndY = dateFormat.format(usage.getVuEnd());
              }
              //得到开始时间HH：mm
              if(!date.equals(vuStartY)){//||vuStartMMdd.compareTo("08:00")<0
                vuStartMMdd = "08:00";
              }
              //得到结束时间HH：mm
              if(!date.equals(vuEndY)){//||vuEndMMdd.compareTo("17:00")>0
                vuEndMMdd = "17:00";
              }
              trStr = trStr+ "<td title='" + vuStart + " 至 "  + vuEnd + "' bgColor='" + vuStatusColorType + "' width='20%'> "
              + vuStartMMdd + "-<BR>" + vuEndMMdd + "</td>";
            }
            trStr = trStr +"</tr></table></td>";//
          }else{
            trStr = trStr + "<td width='20%'  bgColor='#378CD9' ></td>";//循环TD
          }

        }
        trStr = trStr + "</tr>";
      }
      String trsStr = severDayTr + trStr ;

      String data = "{AllTr:\"" + trsStr + "\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 自动使用和收回---
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getAutoUsageBack(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHVehicleLogic vehicleLogic = new YHVehicleLogic();
      YHVehicleUsageLogic usageLogic = new YHVehicleUsageLogic();
      String curDateStr = YHUtility.getCurDateTimeStr();
      
      /**
       * 1、状态为1（审批通过）的、并且已经开始使用的（申请的开始时间小于当前时间）申请表记录，状态更新为2（使用中）
       * 2、同时更新车辆基本信息表中的车辆记录，状态改为1（使用中）
       */
      List<YHVehicleUsage> usageList = usageLogic.selectVUByVuStatus(dbConn, "1", "VU_START", curDateStr, "<");
      for (int i = 0; i < usageList.size(); i++) {
        YHVehicleUsage usage = usageList.get(i);
        if(YHUtility.isInteger(usage.getVId())){
          YHORM orm = new YHORM();
          YHVehicle vehicle = (YHVehicle)orm.loadObjSingle(dbConn, YHVehicle.class, Integer.parseInt(usage.getVId()));
          if("0".equals(vehicle.getUseingFlag())){
            usageLogic.updateVuStatus(dbConn, usage.getSeqId(), "2");//更新为使用中
            vehicleLogic.updateVStatus(dbConn, Integer.parseInt(usage.getVId()), "1");//车辆在使用中
          }
        }
      }
        
      //是否自动回收
      String autoReturnVehicle = YHSysProps.getString("autoReturnVehicle");
      if("1".equals(autoReturnVehicle)){
  
        /**
         * 1、状态为2（使用中）的、并且已经开始使用的（申请的结束时间小于当前时间）申请表记录，状态更新为4（已结束）  
         * 2、同时更新车辆基本信息表中的车辆记录，状态改为0（未使用/正常）  
         */
        List<YHVehicleUsage> usageList2 = usageLogic.selectVUByVuStatus(dbConn, "2", "VU_END", curDateStr, "<");
        for (int i = 0; i < usageList2.size(); i++) {
          YHVehicleUsage usage = usageList2.get(i);
          usageLogic.updateVuStatus(dbConn, usage.getSeqId(), "4");//更改为结束状态  
          if(YHUtility.isInteger(usage.getVId())){
            vehicleLogic.updateVStatus(dbConn, Integer.parseInt(usage.getVId()), "0");//车辆未使用中
          }
        }
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
        //request.setAttribute(YHActionKeys.RET_DATA, data);
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /***
   * 删除车辆的附件ById

   * @return
   * @throws Exception 
   */
  public String deleleFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String attachId = request.getParameter("attachId");
      String attachName = request.getParameter("attachName");
      if(seqId==null){
        seqId = "";
      }
      if(attachId==null){
        attachId = "";
      }
      if(attachName==null){
        attachName = "";
      }
      YHVehicle vehicle = null;
      YHVehicleLogic vehicleLogic = new YHVehicleLogic();
      String updateFlag = "0";
      if(seqId!=null&&!seqId.equals("")){
        vehicle = vehicleLogic.selectVehicleById(dbConn, seqId);
        if(vehicle!=null){
          String attachmentId = vehicle.getAttachId();
          String attachmentName = vehicle.getAttachName();
          if(attachmentId==null){
            attachmentId = "";
          }
          if(attachmentName==null){
            attachmentName = "";
          }
          String[] attachmentIdArray = attachmentId.split(",");
          String[] attachmentNameArray = attachmentName.split("\\*");
          String newAttachmentId = "";
          String newAttachmentName = "";
          for (int i = 0; i < attachmentIdArray.length; i++) {
            if(!attachmentIdArray[i].equals(attachId)){
              newAttachmentId = newAttachmentId +attachmentIdArray[i] + ",";
            }
          }
          for (int i = 0; i < attachmentNameArray.length; i++) {
            if(!attachmentNameArray[i].equals(attachName)){
              newAttachmentName = newAttachmentName +attachmentNameArray[i] + "*";
            }
          }

          vehicleLogic.updateFile(dbConn, newAttachmentId, newAttachmentName, seqId);
          updateFlag = "1";
        }
      }
      String data = "{updateFlag:"+updateFlag+"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 更新车辆的附件ATTACH_ID
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyMM");
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      // 保存从文件柜、网络硬盘选择附件
      YHSelAttachUtil sel = new YHSelAttachUtil(fileForm, "vehicle");
      String attIdStr = sel.getAttachIdToString(",");
      String attNameStr = sel.getAttachNameToString("*");

      String seqId = fileForm.getParameter("seqId");
      String history = fileForm.getParameter("history");
      String carUser = fileForm.getParameter("carUser");
      String attachId = fileForm.getParameter("attachId");
      String attachName = fileForm.getParameter("attachName");
      if(YHUtility.isNullorEmpty(carUser)){
        carUser = "";
      }
      YHVehicleLogic vehicleLogic = new YHVehicleLogic();
      if(YHUtility.isInteger(seqId)){

        //先查出数据库的附件，然后加上
        String attIdStrTemp = "";
        String attNameStrTemp = "";
        YHVehicle vehicleTemp = vehicleLogic.selectVehicleById(dbConn, seqId); 
        if(vehicleTemp!=null){
          attIdStrTemp  = vehicleTemp.getAttachId();
          attNameStrTemp = vehicleTemp.getAttachName();
        }
        
        Iterator<String> iKeys = fileForm.iterateFileFields();
        String filePath = YHSysProps.getAttachPath()    + File.separator + "vehicle"   + File.separator +dateFormat2.format(new Date()); // YHSysProps.getAttachPath()
        String attachmentId = "";
        String attachmentName = "";
        while (iKeys.hasNext()) {
          String fieldName = iKeys.next();
          String fileName = fileForm.getFileName(fieldName);
          String regName = fileName;

          if (YHUtility.isNullorEmpty(fileName)) {
            continue;
          }
          YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
          String rand = emul.getRandom();
          attachmentId =  dateFormat2.format(new Date()) + "_" + attachmentId + rand+",";
          attachmentName = attachmentName + fileName+"*";
          fileName = rand + "_" + fileName;
          fileForm.saveFile(fieldName, filePath   + File.separator +fileName);
        }
        attachmentId = attachmentId + attIdStr;
        attachmentName = attachmentName + attNameStr;
        if(attIdStrTemp!=null&&!attIdStrTemp.equals("")){
          attachmentId = attIdStrTemp  + "," + attachmentId;
        }
        if(attNameStrTemp!=null&&!attNameStrTemp.equals("")){
          attachmentName = attNameStrTemp  + "*" + attachmentName;
        }
        vehicleLogic.updateFile(dbConn, attachmentId, attachmentName, carUser, history, seqId);


      }

      String path = request.getContextPath();
      response.sendRedirect(path + "/subsys/oa/vehicle/manage/vehicleFile.jsp?seqId="+seqId);
      /*
       * //短信smsType, content, remindUrl, toId, fromId YHSmsBack sb = new
       * YHSmsBack(); sb.setSmsType("5"); sb.setContent("请查看日程安排！内容："+content);
       * sb.setRemindUrl("/yh/core/funcs/calendar/mynote.jsp?seqId="+maxSeqId+
       * "&openFlag=1&openWidth=300&openHeight=250");
       * sb.setToId(String.valueOf(userId)); sb.setFromId(userId);
       * YHSmsUtil.smsBack(dbConn, sb);
       */
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, "");

    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "";
  }
  public String remiend(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String smsRemind = request.getParameter("smsRemind");
      String moblieSmsRemind = request.getParameter("moblieSmsRemind");
      String beforeDay = request.getParameter("beforeDay");
      String beforeHour = request.getParameter("beforeHour");
      String beforeMin = request.getParameter("beforeMin");
      String vmRequestDate = request.getParameter("vmRequestDate");
      String vmReason = request.getParameter("vmReason");
      String vuOperator = request.getParameter("vuOperator");
      String vmType = request.getParameter("vmType");
      String vId = request.getParameter("vId");
      if(YHUtility.isNullorEmpty(vmType)){
        vmType = "";
      }
      if(YHUtility.isInteger(vId)){
        YHVehicleLogic vehicleLogic = new YHVehicleLogic();
        Map<String,String> map = vehicleLogic.selectVehicleById(dbConn,Integer.parseInt(vId));
        int beforeDayInt = 0;
        int beforeHourInt = 0;
        int beforeMinInt = 0;
        if (YHUtility.isInteger(beforeDay)) {
          beforeDayInt = Integer.parseInt(beforeDay);
        }
        if (YHUtility.isInteger(beforeHour)) {
          beforeHourInt = Integer.parseInt(beforeHour);
        }
        if (YHUtility.isInteger(beforeMin)) {
          beforeMinInt = Integer.parseInt(beforeMin);
        }    
        Calendar c = Calendar.getInstance();
        if(YHUtility.isDayTime(vmRequestDate)){
          c.setTime(dateFormat.parse(vmRequestDate));
        }
        c.add(Calendar.DATE, -beforeDayInt);
        c.add(Calendar.HOUR, -beforeHourInt);
        c.add(Calendar.MINUTE, -beforeMinInt);
        Date newDate = c.getTime();
        Date curDate = new Date();
        String vmTypeDesc = "";
        if(vmType.equals("0")){
          vmTypeDesc = "维修";
        }
        if(vmType.equals("1")){
          vmTypeDesc = "加油";
        }
        if(vmType.equals("2")){
          vmTypeDesc = "洗车";
        }
        if(vmType.equals("3")){
          vmTypeDesc = "年检";
        }
        if(vmType.equals("4")){
          vmTypeDesc = "其他";
        }
        String classDesc = "";
        String vModel = "";
        if(!YHUtility.isNullorEmpty(map.get("classDesc"))){
          classDesc = map.get("classDesc");
        }
        if(!YHUtility.isNullorEmpty(map.get("vModel"))){
          vModel = map.get("vModel");
        }
        String content = "车辆维护：" + classDesc + vModel + "需要在" + vmRequestDate +"进行维护，维护类型：" + vmTypeDesc + " （维护）原因： " +vmReason;

        if (smsRemind != null) {
          // 短信smsType, content, remindUrl, toId, fromId
          YHSmsBack sb = new YHSmsBack();
          if (curDate.compareTo(newDate) < 0) {
            sb.setSendDate(newDate);
          }
          sb.setSmsType("9");
          sb.setContent(content);
          sb.setToId(vuOperator);
          sb.setFromId(userId);
          YHSmsUtil.smsBack(dbConn, sb);
        }
        if (moblieSmsRemind != null) {
          YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
          sms2Logic.remindByMobileSms(dbConn, vuOperator, userId,content, new Date());
        }
      }
      String path = request.getContextPath();
      response.sendRedirect(path + "/subsys/oa/vehicle/repairremind.jsp?type=1");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, "");

    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "";
  }
  /**
   * 车辆ATTACHMENT不是图片可下载 
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String exportAttachment(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    OutputStream ops = null;
    InputStream is = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);  
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHVehicleMaintenance vcMaintenance = new YHVehicleMaintenance();
      YHORM orm = new YHORM();
      String seqId = request.getParameter("seqId");
      String attachmentId = request.getParameter("attachmentId");
      String attachmentName = request.getParameter("attachmentName");
      String filePath = request.getParameter("filePath");

      String fileName  = attachmentName;
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control", "private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges", "bytes");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Content-disposition", "attachment; filename="
          + new String(fileName.getBytes("GBK"), "iso8859-1"));
      ops = response.getOutputStream();


      //ops.write(br.read());
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
      ops.close();
    }
    return null;
  }
  
  /**
   * 
   * 删除车辆  根据SEQ_ID
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String stopInsurance(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      
      YHVehicleLogic vehicleLogic = new YHVehicleLogic();
      vehicleLogic.stopInsurance(dbConn, seqId);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功！");
      request.setAttribute(YHActionKeys.RET_DATA, "{}");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

}

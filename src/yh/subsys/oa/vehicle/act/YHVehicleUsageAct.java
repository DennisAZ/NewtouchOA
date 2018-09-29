package yh.subsys.oa.vehicle.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.workflow.util.YHFlowHookUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.vehicle.data.YHVehicleUsage;
import yh.subsys.oa.vehicle.logic.YHVehicleLogic;
import yh.subsys.oa.vehicle.logic.YHVehicleUsageLogic;

public class YHVehicleUsageAct {
 
  /**
   * 车辆申请-lz
   * @param request
   * @param response
   * @throws Exception
   */
  public String add(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    String result="";
  try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();     
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);

      YHORM orm = new YHORM();
      YHVehicleUsage ve = (YHVehicleUsage)YHFOM.build(request.getParameterMap());
      String checkType = "2";//不能申请
      if(checkUsage(request, response, ve)){//检查是否可以申请，检查申请时间
        checkType = "1";
        orm.saveSingle(dbConn,ve);
        
        YHFlowHookUtility ut = new YHFlowHookUtility();
        int vehicleUsageId = ut.getMax(dbConn, "select max(SEQ_ID) FROM oa_vehicle_usage");
        Map dataArray = new HashMap();
        dataArray.put("KEY", vehicleUsageId + "");
        dataArray.put("FIELD", "VU_ID");
        dataArray.put("VU_PROPOSER", ve.getVuProposer()+"");
        dataArray.put("VU_DESTINATION", ve.getVuDestination()+"");
        dataArray.put("VU_MILEAGE", ve.getVuMileage()+"");
        dataArray.put("V_ID", ve.getvId()+"");
        String userName = "";
        if (YHUtility.isInteger(ve.getVuUser())) {
          YHPersonLogic p = new YHPersonLogic();
          userName = p.getUserNameLogic(dbConn, Integer.parseInt(ve.getVuUser()));
        }
        dataArray.put("VU_USER", userName);
        dataArray.put("VU_REASON", ve.getVuReason()+"");
        dataArray.put("VU_DRIVER", ve.getVuDriver());
        dataArray.put("VU_REMARK", ve.getVuRemark()+"");
        dataArray.put("VU_START", YHUtility.getDateTimeStrCn(ve.getVuStart()));
        dataArray.put("VU_END", YHUtility.getDateTimeStrCn(ve.getVuEnd()));
        YHVehicleUsageLogic gift = new YHVehicleUsageLogic();
        String deptName=gift.getDeptName(dbConn,ve.getVuDept());
        dataArray.put("VU_DEPT", deptName);
        dataArray.put("ATTACHMENT_ID","");
        dataArray.put("ATTACHMENT_NAME","");
        dataArray.put("MODULE_SRC","meeting");
        dataArray.put("MODULE_DESC","workflow");

        String url = ut.runHook(dbConn, person, dataArray, "vehicle_apply");
       
        if (!"".equals(url)) {
          String path = request.getContextPath();
          result=path+ url;
          checkType="4";
        }
 

        //内部消息提醒
        String smsflag = request.getParameter("smsflag");
        String smsSJ = request.getParameter("smsSJ");
        if (!YHUtility.isNullorEmpty(ve.getDeptManager())) {
          if (smsflag.equals("1")) {
            YHSmsBack sb = new YHSmsBack();
            sb.setSmsType("9");
            sb.setContent("请求车辆申请部门审批，请批示:");
            sb.setSendDate(new java.util.Date());
            sb.setFromId(person.getSeqId());
            sb.setToId(ve.getDeptManager());
            sb.setRemindUrl("/subsys/oa/vehicle/deptManage/index.jsp?openFlag=1&openWidth=860&openHeight=650");
            //sb.setRemindUrl("/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/selectDetail.act?seqId=" + getMaxSeqId + "&openFlag=1&openWidth=600&openHeight=600");
            YHSmsUtil.smsBack(dbConn,sb);
          }
          //手机消息提醒
          if (smsSJ.equals("1")) {
            YHMobileSms2Logic sb = new YHMobileSms2Logic();
            sb.remindByMobileSms(dbConn,ve.getDeptManager(),person.getSeqId(),"请求车辆申请部门审批，请批示:",new java.util.Date());
          }
        }

        String seqStr = YHVehicleUsageLogic.getSeqIdStr(dbConn);
        if (YHUtility.isNullorEmpty(ve.getDeptManager())) {
          if (smsflag.equals("1")) {
            YHSmsBack sb = new YHSmsBack();
            sb.setSmsType("9");
            sb.setContent("向您提交车辆申请，请批示！");
            sb.setSendDate(new java.util.Date());
            sb.setFromId(person.getSeqId());
            sb.setToId(seqStr);
            sb.setRemindUrl("/subsys/oa/vehicle/deptManage/index.jsp?openFlag=1&openWidth=860&openHeight=650");
            //sb.setRemindUrl("/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/selectDetail.act?seqId=" + getMaxSeqId + "&openFlag=1&openWidth=700&openHeight=650");
            YHSmsUtil.smsBack(dbConn,sb);
          }
          //手机消息提醒
          if (smsSJ.equals("1")) {
            YHMobileSms2Logic sb = new YHMobileSms2Logic();
            sb.remindByMobileSms(dbConn,seqStr,person.getSeqId(),"向您提交车辆申请，请批示！:",new java.util.Date());
          }
        }
      }
      result="{url:'"+result+"'}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, checkType);
      request.setAttribute(YHActionKeys.RET_DATA, result);
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加失败");
      throw e;
    }
   
       return "/core/inc/rtjson.jsp";
  }
  /**
   *查询所有(分页)通用列表显示数据-lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String vehicleSelect(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHVehicleUsageLogic gift = new YHVehicleUsageLogic();
      String status = request.getParameter("status");
      String data = gift.vehicleSelect(dbConn,request.getParameterMap(),status,String.valueOf(person.getSeqId()));

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
   *查询所有(分页)通用列表显示数据-lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String vehicleQuery(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVehicleUsageLogic gift = new YHVehicleUsageLogic();
      String status = request.getParameter("status");
      String data = gift.vehicleQuery(dbConn,request.getParameterMap(),status);
   
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
   *查询所有(分页)通用列表显示数据-lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String vehicleAll(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVehicleUsageLogic gift = new YHVehicleUsageLogic();
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

      String data = gift.vehicleAll(dbConn,request.getParameterMap(),usage,vuRequestDateMax,vuStartMax,vuEndMax);
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
   *删除，根据seqId -lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteVehicle(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId2 = request.getParameter("seqId");
      int seqId = 0;
      if (!YHUtility.isNullorEmpty(seqId2)) {
        seqId = Integer.parseInt(seqId2);
      }    
      
      YHVehicleUsageLogic logic = new YHVehicleUsageLogic();
      logic.deleteVehicleUse(dbConn, seqId);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  } 
  /**
   *详细信息，根据seqId -lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectDetail(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId2 = request.getParameter("seqId");
      int seqId = 0;
      if (!YHUtility.isNullorEmpty(seqId2)) {
        seqId = Integer.parseInt(seqId2);
      }
      YHORM orm = new YHORM();
      YHVehicleUsage usage = (YHVehicleUsage)orm.loadObjSingle(dbConn,YHVehicleUsage.class,seqId);
      request.setAttribute("usage", usage);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/subsys/oa/vehicle/usageDetail.jsp";
  }
  /**
   *查询信息，根据seqId -lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectId(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId2 = request.getParameter("seqId");
      int seqId = 0;
      if (!YHUtility.isNullorEmpty(seqId2)) {
        seqId = Integer.parseInt(seqId2);
      }
      YHORM orm = new YHORM();
      YHVehicleUsage usage = (YHVehicleUsage)orm.loadObjSingle(dbConn,YHVehicleUsage.class,seqId);
      request.setAttribute("usage", usage);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/subsys/oa/vehicle/updateVehicle.jsp";
  }
  /**
   * 根据ID修改数据 -lz
   * @param request
   * @param response
   * @throws Exception
   */
  public String editVehicle(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      dbConn = requestDbConn.getSysDbConn();
      //将表单form1映射到YHTest实体类
      YHORM orm = new YHORM();//orm映射数据库
      YHVehicleUsage usage = (YHVehicleUsage)YHFOM.build(request.getParameterMap());
      orm.updateComplex(dbConn,usage);
      int getMaxSeqId = YHVehicleUsageLogic.getMaxSeqId(dbConn);
      //内部消息提醒

      String smsflag = request.getParameter("smsflag");
      String smsSJ = request.getParameter("smsSJ");
      if (!YHUtility.isNullorEmpty(usage.getDeptManager()) && !usage.getDeptManager().equals("null")) {
        if (smsflag.equals("1")) {
          YHSmsBack sb = new YHSmsBack();
          sb.setSmsType("9");
          sb.setContent("请求车辆申请部门审批，请批示:");
          sb.setSendDate(new java.util.Date());
          sb.setFromId(person.getSeqId());
          sb.setToId(usage.getDeptManager());
          sb.setRemindUrl("/subsys/oa/vehicle/deptManage/index.jsp?openFlag=1&openWidth=860&openHeight=650");
          //sb.setRemindUrl("/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/selectDetail.act?seqId=" + getMaxSeqId + "&openFlag=1&openWidth=600&openHeight=600");
          YHSmsUtil.smsBack(dbConn,sb);
        }
        //手机消息提醒
        if (smsSJ.equals("1")) {
          YHMobileSms2Logic sb = new YHMobileSms2Logic();
          sb.remindByMobileSms(dbConn,usage.getDeptManager(),person.getSeqId(),"请求车辆申请部门审批，请批示:",new java.util.Date());
        }
      }

      String seqStr = YHVehicleUsageLogic.getSeqIdStr(dbConn);
      if (YHUtility.isNullorEmpty(usage.getDeptManager()) || usage.getDeptManager().equals("null")) {
        if (smsflag.equals("1")) {
          YHSmsBack sb = new YHSmsBack();
          sb.setSmsType("9");
          sb.setContent("向您提交车辆申请，请批示！");
          sb.setSendDate(new java.util.Date());
          sb.setFromId(person.getSeqId());
          sb.setToId(seqStr);
          sb.setRemindUrl("/subsys/oa/vehicle/deptManage/index.jsp?openFlag=1&openWidth=860&openHeight=650");
          //sb.setRemindUrl("/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/selectDetail.act?seqId=" + getMaxSeqId + "&openFlag=1&openWidth=700&openHeight=650");
          YHSmsUtil.smsBack(dbConn,sb);
        }
        //手机消息提醒
        if (smsSJ.equals("1")) {
          YHMobileSms2Logic sb = new YHMobileSms2Logic();
          sb.remindByMobileSms(dbConn,seqStr,person.getSeqId(),"向您提交车辆申请，请批示！:",new java.util.Date());
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据失败");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 根据ID修改数据 -lz
   * @param request
   * @param response
   * @throws Exception
   */
  public String editVehicle2(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //      String vuEnd = request.getParameter("vuEnd");
      //      String vuDestination = request.getParameter("vuDestination");
      //      String vuMileage = request.getParameter("vuMileage");
      //      String vuRemark = request.getParameter("vuRemark");
      //      int seqId = Integer.parseInt(request.getParameter("seqId"));
      //      YHVehicleUsage usage = new YHVehicleUsage();
      //      usage.setVuDestination(vuDestination);
      //      usage.setVuRemark(vuRemark);
      //      usage.setSeqId(seqId);
      //      if (!YHUtility.isNullorEmpty(vuMileage)) {
      //        usage.setVuMileage(Integer.parseInt(vuMileage));
      //      }
      //      if (!YHUtility.isNullorEmpty(vuEnd)) {
      //        usage.setVuEnd(java.sql.Date.valueOf(vuEnd));
      //      }
      YHORM orm = new YHORM();//orm映射数据库
      YHVehicleUsage usage = (YHVehicleUsage)YHFOM.build(request.getParameterMap());
      orm.updateComplex(dbConn,usage);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据失败");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   *结束，根据seqId -lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateStatus(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId2 = request.getParameter("seqId");
      int seqId = 0;
      if (!YHUtility.isNullorEmpty(seqId2)) {
        seqId = Integer.parseInt(seqId2);
      }
      YHVehicleUsageLogic.updateStatus(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   *查询所有(分页)通用列表显示数据 -lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String vehicleDept(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHVehicleUsageLogic gift = new YHVehicleUsageLogic();
      String dmerStatus = request.getParameter("dmerStatus");
      String data = gift.vehicleDept(dbConn,request.getParameterMap(),dmerStatus,String.valueOf(person.getSeqId()));
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
   *批准,撤销,不批准，根据seqId -lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateDmerStatus(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId2 = request.getParameter("seqId");
      int seqId = 0;
      if (!YHUtility.isNullorEmpty(seqId2)) {
        seqId = Integer.parseInt(seqId2);
      }
      String dmerStatus = request.getParameter("dmerStatus");
      //发送短消息
      if (!dmerStatus.equals("0")) {
        String name = "已批";
        String url = "/subsys/oa/vehicle/query/index.jsp&openFlag=1&openWidth=860&openHeight=720";
        if (dmerStatus.equals("3")) {
          name = "未批";
          url = "/subsys/oa/vehicle/index.jsp&openFlag=1&openWidth=860&openHeight=720";
        }
        YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
        YHORM orm = new YHORM();
        YHVehicleUsage usage = (YHVehicleUsage)orm.loadObjSingle(dbConn,YHVehicleUsage.class,seqId);   
        //批准通过发送短信
        if (usage.getSmsRemind().equals("1")) {
          YHSmsBack sb = new YHSmsBack();
          sb.setSmsType("9");
          sb.setContent("部门领导" + person.getUserName() + name + "您的车辆申请!");
          sb.setSendDate(new java.util.Date());
          sb.setFromId(person.getSeqId());
          sb.setToId(usage.getVuProposer());
          sb.setRemindUrl(url);
          //sb.setRemindUrl("/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/selectDetail.act?seqId=" + usage.getSeqId() + "&openFlag=1&openWidth=700&openHeight=650");
          YHSmsUtil.smsBack(dbConn,sb);      
        }
        //批准通过手机短信
        if (usage.getSms2Remind().equals("1")) {
          YHMobileSms2Logic sb = new YHMobileSms2Logic();
          sb.remindByMobileSms(dbConn,usage.getVuProposer(),person.getSeqId(),"部门领导系统管理员" + person.getUserName() + name + "您的车辆申请!:",new java.util.Date());
        }
      }

      YHVehicleUsageLogic.updateDmerStatus(dbConn,seqId,dmerStatus);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   *查询，根据seqId -lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectDeptReason(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId2 = request.getParameter("seqId");
      int seqId = 0;
      if (!YHUtility.isNullorEmpty(seqId2)) {
        seqId = Integer.parseInt(seqId2);
      }
      String data = YHVehicleUsageLogic.selectDeptReason(dbConn,seqId);
      data = "{deptReason:\"" + YHUtility.encodeSpecial(data) + "\"}";
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   *修改原因，根据seqId -lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateDeptReason(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId2 = request.getParameter("seqId");
      int seqId = 0;
      if (!YHUtility.isNullorEmpty(seqId2)) {
        seqId = Integer.parseInt(seqId2);
      }
      String deptReason = request.getParameter("deptReason");
      YHVehicleUsageLogic.updateDeptReason(dbConn,seqId,deptReason);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   *查询信息，根据seqId -lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectId2(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId2 = request.getParameter("seqId");
      int seqId = 0;
      if (!YHUtility.isNullorEmpty(seqId2)) {
        seqId = Integer.parseInt(seqId2);
      }
      YHORM orm = new YHORM();
      YHVehicleUsage usage = (YHVehicleUsage)orm.loadObjSingle(dbConn,YHVehicleUsage.class,seqId);
      request.setAttribute("usage", usage);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/subsys/oa/vehicle/query/modify.jsp";
  }
  /**
   * 检查是否可以车辆申请
   * @param request
   * @param response
   * @param tvu
   * @return
   * @throws Exception
   */
  public boolean checkUsage(HttpServletRequest request,
      HttpServletResponse response,YHVehicleUsage tvu) throws Exception {
    Connection dbConn = null;
    boolean b = true;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVehicleUsageLogic usageLogic = new YHVehicleUsageLogic();
      String vId = tvu.getvId();
      if(YHUtility.isNullorEmpty(vId)){
        vId = "";
      }
      String[] str = {"(VU_STATUS = '1' or VU_STATUS = '2')","V_ID='" + vId + "'"};
      List<YHVehicleUsage> usageList = usageLogic.selectVUByVuStatus(dbConn, str);
      for (int i = 0; i < usageList.size(); i++) {
        YHVehicleUsage usage =  usageList.get(i);
        if((tvu.getVuStart().compareTo(usage.getVuStart())>=0&&tvu.getVuStart().compareTo(usage.getVuEnd())<=0)
            || (tvu.getVuEnd().compareTo(usage.getVuStart())>=0&&tvu.getVuEnd().compareTo(usage.getVuEnd())<=0)   
            ||(tvu.getVuStart().compareTo(usage.getVuStart())<=0&&tvu.getVuEnd().compareTo(usage.getVuEnd())>=0)){
          b =  false;
          break;
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, "");

    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return b;
  }
  /**
   *查询信息，根据V_ID -lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String showDetailAll(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVehicleUsageLogic gift = new YHVehicleUsageLogic();
      String data = gift.showDetailAll(dbConn,request.getParameterMap());
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
   *查询信息，根据V_ID -lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String showDetail(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId2 = request.getParameter("seqId");
      int seqId = 0;
      if (!YHUtility.isNullorEmpty(seqId2)) {
        seqId = Integer.parseInt(seqId2);
      }
      String[] str = {"VU_STATUS <> '4' "," V_ID='" + seqId + "'"};
      List<YHVehicleUsage> usageList = YHVehicleUsageLogic.showDetailSize(dbConn,str);

      String data = "[";
      YHVehicleUsage usage = new YHVehicleUsage(); 
      for (int i = 0; i < usageList.size(); i++) {
        usage = usageList.get(i);
        data = data + YHFOM.toJson(usage).toString()+",";
      }
      if(usageList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data.replaceAll("\\n", "");
      data = data.replaceAll("\\r", "");
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   *查询所有(分页)通用列表显示数据 -lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectVID(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVehicleUsageLogic gift = new YHVehicleUsageLogic();
      String vId2 = request.getParameter("vId");
      int vId = 0;
      if (!YHUtility.isNullorEmpty(vId2)) {
        vId = Integer.parseInt(vId2);
      }
      String data = gift.selectVID(dbConn,request.getParameterMap(),vId);
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
   *查询所有(分页)通用列表显示数据 -lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String useManage(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHVehicleUsageLogic gift = new YHVehicleUsageLogic();
      String vuStatus = request.getParameter("vuStatus");
      String data = gift.useManage(dbConn,request.getParameterMap(),vuStatus,person.getUserPriv(),String.valueOf(person.getSeqId()));
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
   *批准,收回,不批准，根据seqId -lz
   * @param request
   * @param response
   * @return 
   * @throws Exception
   */
  public String updateStatusId(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String seqId2 = request.getParameter("seqId");
      int seqId = 0;
      if (!YHUtility.isNullorEmpty(seqId2)) {
        seqId = Integer.parseInt(seqId2);
      }
      String status = request.getParameter("status");
      
      YHORM orm = new YHORM();
      YHVehicleUsage usage = (YHVehicleUsage)orm.loadObjSingle(dbConn,YHVehicleUsage.class,seqId);
      if("1".equals(status) && !checkUsage(request, response, usage)){
        String data = "{flag:\"1\"}";
        request.setAttribute(YHActionKeys.RET_DATA,data);
      }
      else{
        YHVehicleUsageLogic.updateStatus(dbConn,seqId,status);
        //短消息  
        if (!status.equals("0")) {
          String name = "已被批准!";
          if (status.equals("3")) {
            name = "未被批准!";
          }
          if (status.equals("4")) {
            name = "被收回了!";
            YHVehicleLogic vehicleLogic = new YHVehicleLogic();
            vehicleLogic.updateVStatus(dbConn, Integer.parseInt(usage.getVId()), "0");//车辆未使用中
          }
          //批准通过发送短信  
          if (usage.getSmsRemind().equals("1")) {
            YHSmsBack sb = new YHSmsBack();
            sb.setSmsType("9");
            sb.setContent("您的车辆申请" + name);
            sb.setSendDate(new java.util.Date());
            sb.setFromId(person.getSeqId());
            sb.setToId(usage.getVuProposer());
            sb.setRemindUrl("/subsys/oa/vehicle/index.jsp&openFlag=1&openWidth=860&openHeight=720");
            //sb.setRemindUrl("/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/selectDetail.act?seqId=" + usage.getSeqId() + "&openFlag=1&openWidth=700&openHeight=650");
            YHSmsUtil.smsBack(dbConn,sb);      
          }
          //批准通过手机短信
          if (usage.getSms2Remind().equals("1")) {
            YHMobileSms2Logic sb = new YHMobileSms2Logic();
            sb.remindByMobileSms(dbConn,usage.getVuProposer(),person.getSeqId(),"您的车辆申请" + name + ":",new java.util.Date());
          }
        }
        String data = "{flag:\"0\"}";
        request.setAttribute(YHActionKeys.RET_DATA,data);
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   *查询，根据seqId -lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String operatorReason(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId2 = request.getParameter("seqId");
      int seqId = 0;
      if (!YHUtility.isNullorEmpty(seqId2)) {
        seqId = Integer.parseInt(seqId2);
      }
      String data = YHVehicleUsageLogic.operatorReason(dbConn,seqId);
      data = "{operatorReason:\"" + YHUtility.encodeSpecial(data) + "\"}";
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   *修改原因，根据seqId -lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateoPeratorReason(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId2 = request.getParameter("seqId");
      int seqId = 0;
      if (!YHUtility.isNullorEmpty(seqId2)) {
        seqId = Integer.parseInt(seqId2);
      }
      String operatorReason = request.getParameter("operatorReason");
      YHVehicleUsageLogic.updateoPeratorReason(dbConn,seqId,operatorReason);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   *查询，根据seqId -lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectNotes(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId2 = request.getParameter("seqId");
      int seqId = 0;
      if (!YHUtility.isNullorEmpty(seqId2)) {
        seqId = Integer.parseInt(seqId2);
      }
      YHVehicleUsage usage = YHVehicleUsageLogic.selectNotes(dbConn,seqId);
      String data = "{vuMileageTrue:\"" + usage.getVuMileageTrue() + "\",vuParkingFees:\"" + usage.getVuParkingFees() + "\"}";
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);     
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 修改出车情况 -lz
   * @throws Exception 
   * 
   * 
   * */
  public String updateNotes(HttpServletRequest request,HttpServletResponse
      response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVehicleUsage usage = (YHVehicleUsage)YHFOM.build(request.getParameterMap());
      YHVehicleUsageLogic.updateNotes(dbConn, usage);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);     
      request.setAttribute(YHActionKeys.RET_MSRG, "修改成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}


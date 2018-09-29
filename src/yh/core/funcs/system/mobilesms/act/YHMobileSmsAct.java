package yh.core.funcs.system.mobilesms.act;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.address.data.YHAddress;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.system.mobilesms.data.YHSms2;
import yh.core.funcs.system.mobilesms.data.YHSms2Priv;
import yh.core.funcs.system.mobilesms.data.YHSms3;
import yh.core.funcs.system.mobilesms.logic.YHMobileSmsLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHMobileSmsAct {
  private static Logger log = Logger.getLogger(YHMobileSmsAct.class);
  
  /**
   * 被提醒权限
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getRemindPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      List<Map> list = new ArrayList();
      YHORM orm = new YHORM();
      Map filters = new HashMap();
      filters = null;
      String data = null;
      List< YHSms2Priv> intface = orm.loadListSingle(dbConn, YHSms2Priv.class, filters);
      if(intface.size() == 0){
        data = YHFOM.toJson(new YHSms2Priv()).toString();
      }else{
        data = YHFOM.toJson(intface.get(0)).toString();
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 编辑被提醒权限
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String updateRemindPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHMobileSmsLogic orgLogic = new YHMobileSmsLogic();
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      String remindPriv = request.getParameter("remindPriv");
      Map m =new HashMap();
      
      if(seqId != 0){
        m.put("seqId", seqId);
      }
      m.put("remindPriv", remindPriv);
      YHORM orm = new YHORM();
      if(seqId != 0){
        orm.updateSingle(dbConn, "sms2Priv", m);
      }else{
        orm.saveSingle(dbConn, "sms2Priv", m);
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"被提醒权限已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 外发权限
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getOutPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      List<Map> list = new ArrayList();
      YHORM orm = new YHORM();
      Map filters = new HashMap();
      filters = null;
      String data = null;
      List< YHSms2Priv> intface = orm.loadListSingle(dbConn, YHSms2Priv.class, filters);
      if(intface.size() == 0){
        data = YHFOM.toJson(new YHSms2Priv()).toString();
      }else{
        data = YHFOM.toJson(intface.get(0)).toString();
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 编辑外发权限
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String updateOutPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHMobileSmsLogic orgLogic = new YHMobileSmsLogic();
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      String outPriv = request.getParameter("outPriv");
      String outToSelf = request.getParameter("outToSelf");
      if(outToSelf.trim().equals("true")){
        outToSelf = "1";
      }else{
        outToSelf = "0";
      }
      Map m =new HashMap();
      if(seqId != 0){
        m.put("seqId", seqId);
      }
      m.put("outPriv", outPriv);
      m.put("outToSelf", outToSelf);
      YHORM orm = new YHORM();
      if(seqId != 0){
        orm.updateSingle(dbConn, "sms2Priv", m);
      }else{
        orm.saveSingle(dbConn, "sms2Priv", m);
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"外发权限已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 提醒权限
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getSms2RemindPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      List<Map> list = new ArrayList();
      YHORM orm = new YHORM();
      Map filters = new HashMap();
      filters = null;
      String data = null;
      List< YHSms2Priv> intface = orm.loadListSingle(dbConn, YHSms2Priv.class, filters);
      if(intface.size() == 0){
        data = YHFOM.toJson(new YHSms2Priv()).toString();
      }else{
        data = YHFOM.toJson(intface.get(0)).toString();
      }
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 编辑提醒权限
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String updateSms2RemindPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHMobileSmsLogic orgLogic = new YHMobileSmsLogic();
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      String sms2RemindPriv = request.getParameter("sms2RemindPriv");
      Map m =new HashMap();
      if(seqId != 0){
        m.put("seqId", seqId);
      }
      m.put("sms2RemindPriv", sms2RemindPriv);
      YHORM orm = new YHORM();
      if(seqId != 0){
        orm.updateSingle(dbConn, "sms2Priv", m);
      }else{
        orm.saveSingle(dbConn, "sms2Priv", m);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"提醒权限已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getTypePriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      List<Map> list = new ArrayList();
      YHORM orm = new YHORM();
      Map filters = new HashMap();
      filters = null;
      String data = null;
      List< YHSms2Priv> intface = orm.loadListSingle(dbConn, YHSms2Priv.class, filters);
      
      if(intface.size() == 0){
        data = YHFOM.toJson(new YHSms2Priv()).toString();
      }else{
        data = YHFOM.toJson(intface.get(0)).toString();
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取模块权限
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getTypePrivConfig(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String pos = request.getParameter("pos");
    YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      List<Map> list = new ArrayList();
      List<Map> listd = new ArrayList();
      YHORM orm = new YHORM();
      HashMap map = null;
      HashMap mapd = null;
      String seqIdStr = "";
      String typePrivStr = request.getParameter("typePriv");
      StringBuffer selectedSb = new StringBuffer("selected:[");
      StringBuffer disselectedSb = new StringBuffer("{disselected:[");
      
      YHMobileSmsLogic msl = new YHMobileSmsLogic();
      String seqIds = msl.getSysCodeSeqId(dbConn, typePrivStr);
      if(!YHUtility.isNullorEmpty(seqIds)){
        seqIdStr = seqIds.substring(0, seqIds.length()-1);
      }else{
        seqIdStr = "-1";
      }
      
      String[] filters = new String[]{"CLASS_NO = 'SMS_REMIND' and SEQ_ID IN (" + seqIdStr + ") "};
      List funcList = new ArrayList();
      funcList.add("codeItem");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("OA_KIND_DICT_ITEM"));
      for(Map ms : list){
        selectedSb.append("{");
        selectedSb.append("value:\"" + ms.get("classCode") + "\"");
        selectedSb.append(",text:\"" + ms.get("classDesc") + "\"");
        selectedSb.append("},");
      }
      
      String[] filtersd = new String[]{"CLASS_NO = 'SMS_REMIND' and (not SEQ_ID IN (" + seqIdStr + ")) "};
      List funcListd = new ArrayList();
      funcListd.add("oaKindDictItem");
      mapd = (HashMap)orm.loadDataSingle(dbConn, funcListd, filtersd);
      listd.addAll((List<Map>) mapd.get("OA_KIND_DICT_ITEM"));
      for(Map msd : listd){
        disselectedSb.append("{");
        disselectedSb.append("value:\"" + msd.get("classCode") + "\"");
        disselectedSb.append(",text:\"" + msd.get("classDesc") + "\"");
        disselectedSb.append("},");
      }
      if (selectedSb.charAt(selectedSb.length() - 1) == ',') {
        selectedSb.deleteCharAt(selectedSb.length() - 1);
      }
      if (disselectedSb.charAt(disselectedSb.length() - 1) == ',') {
        disselectedSb.deleteCharAt(disselectedSb.length() - 1);
      }
      disselectedSb.append("],");
      selectedSb.append("]}");
      StringBuffer sb = new StringBuffer(disselectedSb);
      sb.append(selectedSb);
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功设置桌面属性");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 设置模块权限
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String setTypePrivConfig(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    int seqId = Integer.parseInt(request.getParameter("seqId"));
    String selectValue = request.getParameter("selectValue");
    //System.out.println(selectValue+"HKJH");
    YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      // selectValue属性为cancel时,代表用户未设置任何信息      YHORM t = new YHORM();
      Map m =new HashMap();
      if(!"cancel".equals(selectValue.trim())){
        if(seqId != 0){
          m.put("seqId", seqId);
        }
        m.put("typePriv", selectValue);
        if(seqId != 0){
          t.updateSingle(dbConn, "sms2Priv", m);
        }else{
          t.saveSingle(dbConn, "sms2Priv", m);
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功设置桌面属性");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 短信接收管理列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getReceiveSearchList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String phone = request.getParameter("phone");
      phone = YHDBUtility.escapeLike(phone);
      String content = request.getParameter("content");
      content = YHDBUtility.escapeLike(content);
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      YHMobileSmsLogic dl = new YHMobileSmsLogic();
      String data = dl.getManagePersonList(dbConn,request.getParameterMap(), phone, content, beginDate, endDate);
      
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
   * 获取PERSON表中用户姓名和手机号是否公开的标记
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getMobilNo3(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      List<Map> list = new ArrayList();
      YHORM orm = new YHORM();
      HashMap map = null;
      String mobilNo = request.getParameter("mobilNo");
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"MOBIL_NO like '%" + mobilNo + "%'" + YHDBUtility.escapeLike()};
      List funcList = new ArrayList();
      funcList.add("person");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("PERSON"));
      for(Map ms : list){
        sb.append("{");
        sb.append("userName:\"" + ms.get("userName") + "\"");
        sb.append(",mobilNoHidden:\"" + ms.get("mobilNoHidden") + "\"");
        sb.append("},");
      }
      if (sb.charAt(sb.length() - 1) == ',') {
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取PERSON(人员)表中MOBIL_NO
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getPersonPhone(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String mobilNo = request.getParameter("mobilNo");
      List<Map> list = new ArrayList();
      YHORM orm = new YHORM();
      //String[] filters = new String[]{"MOBIL_NO like '%" + mobilNo + "%'"};
      String data = null;
      //List< YHPerson> mobileNoList = orm.loadListSingle(dbConn, YHPerson.class, filters);
      YHMobileSmsLogic msLogic = new YHMobileSmsLogic();
      List<YHPerson> mobileNoList = msLogic.getMobileSmsFunc(dbConn, mobilNo);
      if(mobileNoList.size() == 0){
        data = "{userName:''}";
      }else{
        data = YHFOM.toJson(mobileNoList.get(0)).toString();
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获得Address(通讯簿)表中的MOBIL_NO
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getAddressPhone(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String mobilNo = request.getParameter("mobilNo");
      List<Map> list = new ArrayList();
      YHORM orm = new YHORM();
      String data = null;
      //String[] filterAddress = new String[]{"MOBIL_NO like '%" + mobilNo + "%'"};
      //List< YHAddress> addressList = orm.loadListSingle(dbConn, YHAddress.class, filterAddress);
      YHMobileSmsLogic msLogic = new YHMobileSmsLogic();
      List<YHAddress> addressList = msLogic.getAddressPsnName(dbConn, mobilNo);
      if (addressList.size() == 0) {
        data = "{psnName:''}";
      } else {
        data = YHFOM.toJson(addressList.get(0)).toString();
      }
      //System.out.println(data+"RTYUIdd");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取SMS3表中的phone
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getSms3MobilNo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      List<Map> list = new ArrayList();
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHORM orm = new YHORM();
      String data = null;
      String[] sms3Str = new String[]{"SEQ_ID="+seqId};
      List< YHSms3> sms3List = orm.loadListSingle(dbConn, YHSms3.class, sms3Str);
      if (sms3List.size() == 0) {
        data = "{phone:''}";
      } else {
        data = YHFOM.toJson(sms3List.get(0)).toString();
      }
      //System.out.println(data+"RTYUIdd");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 短信接收管理批量删除
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String deleteSelect(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String sumStrs = request.getParameter("sumStrs");
      YHMobileSmsLogic msl = new YHMobileSmsLogic();
      msl.deleteAll(dbConn, sumStrs);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功删除数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 短信接收管理当前页面删除
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String deleteReceiveManage(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String phone = request.getParameter("phone");
      phone = YHDBUtility.escapeLike(phone);
      String content = request.getParameter("content");
      content = YHDBUtility.escapeLike(content);
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      YHMobileSmsLogic dl = new YHMobileSmsLogic();
      dl.deleteReceiveManage(dbConn,request.getParameterMap(), phone, content, beginDate, endDate);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功删除数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 短信接收管理列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getSendSearchList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String sendFlag = request.getParameter("sendFlag");
      String phone = request.getParameter("phone");
      phone = YHDBUtility.escapeLike(phone);
      String user = request.getParameter("user");
      String content = request.getParameter("content");
      content = YHDBUtility.escapeLike(content);
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      YHMobileSmsLogic dl = new YHMobileSmsLogic();
      String data = dl.getSendSearchList(dbConn,request.getParameterMap(), sendFlag, phone, content, beginDate, endDate, user);
      //System.out.println(data+"KLKLKLKLKL");
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
   * 获取发信人名称
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getUserName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userIdStr = request.getParameter("userId");
      int userId = Integer.parseInt(userIdStr);
      YHPersonLogic dl = new YHPersonLogic();
      String data = dl.getUserNameLogic(dbConn, userId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取SMS2表中的phone
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getSms2MobilNo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      List<Map> list = new ArrayList();
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHORM orm = new YHORM();
      String data = null;
      //List< YHSms2> sms3List = orm.loadListSingle(dbConn, YHSms2.class, sms3Str);
      YHMobileSmsLogic msLogic = new YHMobileSmsLogic();
      List< YHSms2> sms2List = msLogic.getSms2Phone(dbConn, seqId);
      if (sms2List.size() == 0) {
        data = "{phone:''}";
      } else {
        data = YHFOM.toJson(sms2List.get(0)).toString();
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 短信发送管理删除
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String deleteSendManage(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String user = request.getParameter("user");
      String phone = request.getParameter("phone");
      phone = YHDBUtility.escapeLike(phone);
      String content = request.getParameter("content");
      content = YHDBUtility.escapeLike(content);
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      YHMobileSmsLogic dl = new YHMobileSmsLogic();
      dl.deleteSendManage(dbConn,request.getParameterMap(), phone, content, beginDate, endDate, user);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功删除数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 手机短信查询结果批量删除
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String deleteSelectSms2(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String sumStrs = request.getParameter("sumStrs");
      YHMobileSmsLogic msl = new YHMobileSmsLogic();
      msl.deleteSelectSms2(dbConn, sumStrs);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功删除数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 手机短信发送统计列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getReportSearchList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      YHMobileSmsLogic dl = new YHMobileSmsLogic();
      String data = dl.getReportSearchList(dbConn,request.getParameterMap(), beginDate, endDate);
      
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
  
  public String getReportDeptSearchList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      //System.out.println(beginDate);
      YHMobileSmsLogic dl = new YHMobileSmsLogic();
      String data = dl.getReportDeptSearchList1(dbConn, beginDate, endDate);
      //System.out.println(data+"NBHYGUHJKJKMN");
      //PrintWriter pw = response.getWriter();
      //pw.println(data);
      //pw.flush();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取发送成功记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getSendSuccess(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String fromId = request.getParameter("seqId");
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      YHMobileSmsLogic msl = new YHMobileSmsLogic();
      String data = msl.getSendSuccess(dbConn, fromId, beginDate, endDate);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取未发送记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getSendNo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String fromId = request.getParameter("seqId");
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      YHMobileSmsLogic msl = new YHMobileSmsLogic();
      String data = msl.getSendNo(dbConn, fromId, beginDate, endDate);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取发送超时记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getsendTimeOut(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String fromId = request.getParameter("seqId");
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      YHMobileSmsLogic msl = new YHMobileSmsLogic();
      String data = msl.getsendTimeOut(dbConn, fromId, beginDate, endDate);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 删除该用户的发送记录 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String deleteSendSign(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String sumStrs = request.getParameter("sumStrs");
      YHMobileSmsLogic msl = new YHMobileSmsLogic();
      msl.deleteSendSign(dbConn, sumStrs);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功删除数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getDeptName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIdStr = request.getParameter("seqId");
      int seqId = Integer.parseInt(seqIdStr);
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      YHMobileSmsLogic msl = new YHMobileSmsLogic();
      String data = msl.getDeptNameLogic(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 导出到EXCEL表格中

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String exportToExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{
    OutputStream ops = null;
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String fileName = URLEncoder.encode("手机短信接收记录.xls","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      ops = response.getOutputStream();
      YHMobileSmsLogic ieml = new YHMobileSmsLogic();
      ArrayList<YHDbRecord > dbL = ieml.toExportDeptData(conn);
      YHJExcelUtil.writeExc(ops, dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
      ops.close();
    }
    return null;
  }
  
  /**
   * 导出到EXCEL表格中   手机短信发送统计(按人员)


   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String exportToExcelSmsPerson(HttpServletRequest request,HttpServletResponse response) throws Exception{
    OutputStream ops = null;
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String fileName = URLEncoder.encode("手机短信发送统计(按人员).xls","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      ops = response.getOutputStream();
      YHMobileSmsLogic ieml = new YHMobileSmsLogic();
      ArrayList<YHDbRecord > dbL = ieml.exportToExcelSmsPerson(conn, beginDate, endDate);
      
      YHJExcelUtil.writeExc(ops, dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
      ops.close();
    }
    return null;
  }
  
  /**
   * 导出到EXCEL表格中   手机短信发送统计(按部门)


   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String exportToExcelSmsDept(HttpServletRequest request,HttpServletResponse response) throws Exception{
    OutputStream ops = null;
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String fileName = URLEncoder.encode("手机短信发送统计(按部门).xls","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      ops = response.getOutputStream();
      YHMobileSmsLogic ieml = new YHMobileSmsLogic();
      ArrayList<YHDbRecord > dbL = ieml.exportToExcelSmsDept(conn, beginDate, endDate);
      
      YHJExcelUtil.writeExc(ops, dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
      ops.close();
    }
    return null;
  }
}

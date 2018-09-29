package yh.core.funcs.system.remind.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.diary.data.YHDiary;
import yh.core.funcs.system.diary.logic.YHDiaryLogic;
import yh.core.funcs.system.mobilesms.data.YHSms2Priv;
import yh.core.funcs.system.remind.data.YHRemind;
import yh.core.funcs.system.remind.logic.YHRemindLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHRemindAct {
  private static Logger log = Logger.getLogger(YHRemindAct.class);
  public String getRemind(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHRemindLogic orgLogic = new YHRemindLogic();
      YHRemind org = null;
      String data = null;
      org = orgLogic.get(dbConn);
      if (org == null) {
        org = new YHRemind();
      }
      data = YHFOM.toJson(org).toString();
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
  
  public String getRemindCode(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      HashMap map = null;
      List<Map> list = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"CLASS_NO='SMS_REMIND' AND CLASS_CODE!='0' order by SORT_NO asc"};
      List funcList = new ArrayList();
      funcList.add("codeItem");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("OA_KIND_DICT_ITEM"));
      if(list.size() > 1){
        for(Map m : list){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",codeNo:\"" + m.get("classCode") + "\"");
          sb.append(",codeName:\"" + m.get("classDesc") + "\"");
          sb.append(",codeOrder:\"" + m.get("sortNo") + "\"");
          sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1); 
      }else{
        for(Map m : list){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",codeNo:\"" + m.get("classCode") + "\"");
          sb.append(",codeName:\"" + m.get("classDesc") + "\"");
          sb.append(",codeOrder:\"" + m.get("sortNo") + "\"");
          sb.append("}");
        }
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
  
  public String updateRemind(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHRemindLogic orgLogic = new YHRemindLogic();
      String remindSum = request.getParameter("remindSum");
      int seqId = Integer.parseInt(request.getParameter("seqIdRemind"));
      //System.out.println(remindSum+":::"+seqId);
      orgLogic.updateRemind(dbConn, seqId, remindSum);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"工作日志设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String addDiary(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHDiaryLogic orgLogic = new YHDiaryLogic();
      String statrTime = request.getParameter("statrTime");
      String endTime = request.getParameter("endTime");
      //Date d =new SimpleDateFormat("yyyy-MM-dd").parse(endTime);
      String days = request.getParameter("days");
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      String sumStr = statrTime.substring(0,10)+","+endTime.substring(0,10)+","+days;
      //System.out.println(sumStr);
      orgLogic.add(dbConn, sumStr);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"工作日志设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getNotify(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHDiaryLogic orgLogic = new YHDiaryLogic();
      YHDiary org = null;
      String data = null;
      org = orgLogic.getNotify(dbConn);
      if (org == null) {
        org = new YHDiary();
      }
      data = YHFOM.toJson(org).toString();
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
  
  public String getRemindPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String loginUserId = String.valueOf(person.getSeqId());
      YHORM orm = new YHORM();
      String[] moduleFalg = null;
      String privFlag = "0";
      ArrayList<YHSms2Priv> priv = (ArrayList<YHSms2Priv>) orm.loadListSingle(dbConn, YHSms2Priv.class, moduleFalg);
      if(priv.size() == 0){
        privFlag = "0";
      }else{
        String[] privStr = YHUtility.null2Empty(priv.get(0).getRemindPriv()).split(",");
        for(int i = 0; i < privStr.length; i++){
          if(privStr[i].equals(loginUserId)){
            privFlag = "1";
          }
        }
      }
      //System.out.println(privFlag+"JHYUIKJHYUIKJ");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, privFlag);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}

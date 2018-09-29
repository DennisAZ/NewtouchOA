package yh.core.funcs.system.diary.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.system.diary.data.YHDiary;
import yh.core.funcs.system.diary.logic.YHDiaryLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.form.YHFOM;

public class YHDiaryAct {
  private static Logger log = Logger.getLogger(YHDiaryAct.class);
  public String getDiary(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHDiaryLogic orgLogic = new YHDiaryLogic();
      YHDiary org = null;
      String data = null;
      org = orgLogic.get(dbConn);
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
  
  public String updateDiary(HttpServletRequest request,
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
      if("".equals(statrTime)){
        statrTime = "";
      }
      if("".equals(endTime)){
        endTime = "";
      }
      if("".equals(days)){
        days = "0";
      }
      String sumStr = statrTime+","+endTime+","+days;
      
      orgLogic.updateDiary(dbConn, seqId, sumStr);
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
}

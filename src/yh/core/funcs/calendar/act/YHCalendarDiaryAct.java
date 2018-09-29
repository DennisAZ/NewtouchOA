package yh.core.funcs.calendar.act;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.calendar.data.YHCalendar;
import yh.core.funcs.calendar.logic.YHCalendarDiaryLogic;
import yh.core.funcs.diary.data.YHDiary;
import yh.core.funcs.diary.logic.YHDiaryLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.form.YHFOM;

public class YHCalendarDiaryAct {
  /**
   * 新建日程安排
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addCalendarDiary(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn(); 
      YHCalendar calendar = new YHCalendar();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String calendarId = request.getParameter("calendarId");
      String diaryId = request.getParameter("diaryId");

      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, "data");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
 /**
  * 查询此日程 相关联的所有日志
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
  public String selectDiary(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn(); 
      String seqId = request.getParameter("seqId");
      String data = "[";
      YHCalendarDiaryLogic cdLogic = new YHCalendarDiaryLogic();
      YHDiaryLogic dl = new YHDiaryLogic();
      if(seqId!=null&&!seqId.equals("")){
        String diaryIds = cdLogic.selectDiaryId(dbConn, seqId);
        if(diaryIds!=null&&!diaryIds.equals("")){
          ArrayList<YHDiary> diaryList = dl.getDiaryListById(dbConn, diaryIds);
          for (int i = 0; i < diaryList.size(); i++) {
           data = data + YHFOM.toJson(diaryList.get(i))+",";
          }
          if(diaryList.size()>0){
            data = data.substring(0, data.length()-1);
          }
        }
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
/**
 * 判断今天是否写过日程日志，如果写了则查出来
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
 public String selectDiaryByDate(HttpServletRequest request,
     HttpServletResponse response) throws Exception {
   Connection dbConn = null;
   try {
     YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     dbConn = requestDbConn.getSysDbConn(); 
     String data = "[";
     String date = request.getParameter("date");
     YHCalendarDiaryLogic cdLogic = new YHCalendarDiaryLogic();
     YHDiaryLogic dl = new YHDiaryLogic();
     if(date!=null&&!date.equals("")){
       String diaryIds = cdLogic.selectDiaryIdByDate(dbConn,date);
       if(diaryIds!=null&&!diaryIds.equals("")){
         ArrayList<YHDiary> diaryList = dl.getDiaryListById(dbConn, diaryIds);
         for (int i = 0; i < diaryList.size(); i++) {
          data = data + YHFOM.toJson(diaryList.get(i))+",";
         }
         if(diaryList.size()>0){
           data = data.substring(0, data.length()-1);
         }
       }
      
     }
     data = data + "]";
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
     request.setAttribute(YHActionKeys.RET_DATA, data);
   }catch(Exception ex) {
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
     throw ex;
   }
   return "/core/inc/rtjson.jsp";
 }
}

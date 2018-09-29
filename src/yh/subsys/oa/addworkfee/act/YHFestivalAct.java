package yh.subsys.oa.addworkfee.act;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.subsys.oa.addworkfee.data.YHFestival;
import yh.subsys.oa.addworkfee.logic.YHFestivalLogic;

/**
 * 节假日
 * @author Administrator
 *
 */
public class YHFestivalAct{
  /**
   * 增加节假日
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addFestival(HttpServletRequest request, HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    
    try{
      dbConn = requestDbConn.getSysDbConn();
      String year = request.getParameter("year");
      String name = request.getParameter("festName");
      String beginDate = request.getParameter("startTime");
      String endDate = request.getParameter("endTime");
      YHFestival  fest = new YHFestival();
      fest.setYear(Integer.parseInt(year));
      fest.setFeName(name);
      Date begin = YHUtility.parseDate(beginDate);
      fest.setBeginDate(begin);
      Date end = YHUtility.parseDate(endDate);
      fest.setEndDate(end);
      YHFestivalLogic logic = new YHFestivalLogic();
      int ok = logic.addYHFestival(dbConn, fest);
      if(ok == 0){
        request.setAttribute("msg", "插入失败");
        return "/subsys/inforesource/docmgr/docreceve/msgBox.jsp";
      }
      int dateYear = 1970;
      if(YHUtility.isNullorEmpty(year)){
        dateYear = new Date().getYear()+1900;
      }else{
        dateYear = Integer.parseInt(year);
      }
      List<YHFestival> fets = logic.findFestival(dbConn, dateYear+"");
      List<Integer> fYears = logic.findYearList(dbConn);
      List<Integer> ints = logic.findYearList(dbConn);
      String yearList = list2String(ints);
      request.setAttribute("yearList", yearList);
      request.setAttribute("yearArray", fYears);
      request.setAttribute("fets", fets);
      request.setAttribute("year", dateYear);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    //request.setAttribute(YHActionKeys.RET_METHOD, YHActionKeys.RET_METHOD_REDIRECT);
    return "/subsys/oa/addworkfee/addfestival.jsp";
  }
  
  /**
   * 查询节日列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @SuppressWarnings("deprecation")
  public String findFestvialList(HttpServletRequest request, HttpServletResponse response)throws Exception{
    try{
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int dateYear = 1970;
      YHFestivalLogic logic = new YHFestivalLogic();
      String years = request.getParameter("year");
      if(YHUtility.isNullorEmpty(years)){
        dateYear = new Date().getYear()+1900;
      }else{
        dateYear = Integer.parseInt(years);
      }
      List<YHFestival> fets = logic.findFestival(dbConn, dateYear+"");
      List<Integer> ints = logic.findYearList(dbConn);
      String yearList = list2String(ints);
      request.setAttribute("yearList", yearList);
      request.setAttribute("fets", fets);
      request.setAttribute("year", dateYear);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/subsys/oa/addworkfee/addfestival.jsp";
  }
  
  /**
   * 删除节日列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delFestvial(HttpServletRequest request, HttpServletResponse response)throws Exception{
    int year = 0;
    try{
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFestivalLogic logic = new YHFestivalLogic();
      String seq_id = request.getParameter("seqId");
      String years = request.getParameter("year");
      int seqId = 0;
      if(!YHUtility.isNullorEmpty(seq_id)){
        seqId = Integer.parseInt(seq_id);
      }
      year = Integer.parseInt(years);
      int k = logic.delFestival(dbConn, seqId);
      if(k == 0){
        request.setAttribute("msg", "删除失败");
        return "/subsys/inforesource/docmgr/docreceve/msgBox.jsp";
      }
      List<Integer> ints = logic.findYearList(dbConn);
      String yearList = list2String(ints);
      request.setAttribute("yearList", yearList);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return request.getContextPath() + "/subsys/oa/addworkfee/act/YHFestivalAct/findFestvialList.act?year=" + year;
  }
  
  /**
   * 编辑某一项
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String editFestival(HttpServletRequest request, HttpServletResponse response)throws Exception{
    YHFestivalLogic logic = new YHFestivalLogic();
    String seq_id = request.getParameter("seqId");
    int seqId = 0;
    if(!YHUtility.isNullorEmpty(seq_id)){
      seqId = Integer.parseInt(seq_id);
    }
    try{
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFestival fest = logic.findFestival(dbConn, seqId);
      List<YHFestival> fets = logic.findFestival(dbConn, fest.getYear()+"");
      List<Integer> ints = logic.findYearList(dbConn);
      String yearList = list2String(ints);
      request.setAttribute("yearList", yearList);
      request.setAttribute("fets", fets);
      request.setAttribute("year", fest.getYear());
      request.setAttribute("fest", fest);
      request.setAttribute("edit", "_edit");
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/subsys/oa/addworkfee/addfestival.jsp";
  }
  
  /**
   * 更新节假日
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateFestival(HttpServletRequest request, HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    
    try{
      dbConn = requestDbConn.getSysDbConn();
      String year = request.getParameter("year");
      String name = request.getParameter("festName");
      String beginDate = request.getParameter("startTime");
      String endDate = request.getParameter("endTime");
      String seqId = request.getParameter("seqId");
      YHFestival  fest = new YHFestival();
      fest.setYear(Integer.parseInt(year));
      fest.setFeName(name);
      Date begin = YHUtility.parseDate(beginDate);
      fest.setBeginDate(begin);
      Date end = YHUtility.parseDate(endDate);
      fest.setEndDate(end);
      fest.setSeqId(Integer.parseInt(seqId));
      YHFestivalLogic logic = new YHFestivalLogic();
      int ok = logic.updateFestival(dbConn, fest);
      if(ok == 0){
        request.setAttribute("msg", "更新成功失败");
        return "/subsys/inforesource/docmgr/docreceve/msgBox.jsp";
      }
      int dateYear = 1970;
      if(YHUtility.isNullorEmpty(year)){
        dateYear = new Date().getYear()+1900;
      }else{
        dateYear = Integer.parseInt(year);
      }
      List<YHFestival> fets = logic.findFestival(dbConn, dateYear+"");
      List<Integer> fYears = logic.findYearList(dbConn);
      List<Integer> ints = logic.findYearList(dbConn);
      String yearList = list2String(ints);
      request.setAttribute("yearList", yearList);
      request.setAttribute("yearArray", fYears);
      request.setAttribute("fets", fets);
      request.setAttribute("year", dateYear);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    //request.setAttribute(YHActionKeys.RET_METHOD, YHActionKeys.RET_METHOD_REDIRECT);
    return "/subsys/oa/addworkfee/addfestival.jsp";
  }
  
  private String list2String(List list){
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    if(list != null && list.size() >0){
      for(int i=0; i<list.size(); i++){
        sb.append(list.get(i));
        if(i < list.size() -1){
          sb.append(",");
        }
      }
    }
    sb.append("]");
    //YHOut.println(sb.toString());
    return sb.toString();
  }
}

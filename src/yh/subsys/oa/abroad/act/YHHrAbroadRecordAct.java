package yh.subsys.oa.abroad.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.abroad.data.YHHrAbroadRecord;
import yh.subsys.oa.abroad.logic.YHHrAbroadRecordLogic;
import yh.subsys.oa.fillRegister.logic.YHAttendFillLogic;

public class YHHrAbroadRecordAct {
  public static final String attachmentFolder = "abroad";
  private YHHrAbroadRecordLogic logic = new YHHrAbroadRecordLogic();

  /**
   * 新建出国记录--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addRecord(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
     
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      Map<String,String[]> map = request.getParameterMap();
      YHAttendFillLogic afl = new YHAttendFillLogic();
      YHHrAbroadRecord record = (YHHrAbroadRecord) YHFOM.build(map, YHHrAbroadRecord.class, "");
      String stUserId = record.getAbroadUserId();
      String[] staffUserIdStr = stUserId.split(",");
      for(int i = 0; i < staffUserIdStr.length; i++){
        record.setCreateUserId(String.valueOf(person.getSeqId()));
        record.setCreateDeptId(person.getDeptId());
        record.setAbroadUserId(staffUserIdStr[i]);
        this.logic.add(dbConn, record);
        afl.autoAttendFill(dbConn, person, request.getParameter("beginDate"), request.getParameter("endDate"), staffUserIdStr[i], "");
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加"); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取出国记录管理--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getAbroadRecordListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.getAbroadRecordListJson(dbConn, request.getParameterMap(), person);
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
  
  public String getUserName(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userIdStr = request.getParameter("userIdStr");
      String data = this.logic.getUserNameLogic(dbConn, userIdStr);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 删除一条记录--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteSingle(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      this.logic.deleteSingle(dbConn, Integer.parseInt(seqId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功"); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 批量删除--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteAll(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
    
      String seqIdStr = request.getParameter("sumStrs");
      this.logic.deleteAll(dbConn, seqIdStr);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取出国记录信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRecordInfoDetail(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      if(YHUtility.isNullorEmpty(seqId)){
        seqId = "0";
      }
      YHHrAbroadRecord record = (YHHrAbroadRecord)this.logic.getRecordDetail(dbConn, Integer.parseInt(seqId));
      if (record == null){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
        request.setAttribute(YHActionKeys.RET_MSRG, "培训记录不存在");
        return "/core/inc/rtjson.jsp";
      }
      StringBuffer data = YHFOM.toJson(record);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功"); 
      request.setAttribute(YHActionKeys.RET_DATA, data.toString()); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 编辑出国记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateRecord(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      Map<String,String[]> map = request.getParameterMap();
      YHHrAbroadRecord record = (YHHrAbroadRecord) YHFOM.build(map, YHHrAbroadRecord.class, "");
      this.logic.updateRecord(dbConn, record);
      YHAttendFillLogic afl = new YHAttendFillLogic();
      String stUserId = record.getAbroadUserId();
      afl.autoAttendFill(dbConn, person, request.getParameter("beginDate"), request.getParameter("endDate"), stUserId, "");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加"); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
	/**
	 * 出国记录查询 --wyw
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String queryAbroadRecordListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("abroadUserId", YHDBUtility.escapeLike(request.getParameter("abroadUserId")));
			map.put("abroadName", YHDBUtility.escapeLike(request.getParameter("abroadName")));
			map.put("beginDate", YHDBUtility.escapeLike(request.getParameter("beginDate")));
			map.put("beginDate1", YHDBUtility.escapeLike(request.getParameter("beginDate1")));
			map.put("endDate", YHDBUtility.escapeLike(request.getParameter("endDate")));
			map.put("endDate1", YHDBUtility.escapeLike(request.getParameter("endDate1")));
			map.put("remark", YHDBUtility.escapeLike(request.getParameter("remark")));
			String data = "";
			data = this.logic.queryAbroadRecordListJsonLogic(dbConn, request.getParameterMap(), map, person);
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
  
	public String showTime(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String startDateStr = request.getParameter("beginDate");
      String endDateStr = request.getParameter("endDate");
      String dayList = this.logic.getDayList(dbConn, startDateStr, endDateStr);
      String data = "";
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加"); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
  
	public String showMonth(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      String data = "";
      YHAttendFillLogic afl = new YHAttendFillLogic();
      data = afl.showTimeStr(dbConn, beginDate, endDate);
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加"); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
  
}

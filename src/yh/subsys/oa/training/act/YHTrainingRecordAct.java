package yh.subsys.oa.training.act;


import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.address.data.YHAddress;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.training.data.YHHrTrainingRecord;
import yh.subsys.oa.training.logic.YHTrainingRecordLogic;

public class YHTrainingRecordAct {
	
	public static final String attachmentFolder = "training";
	private YHTrainingRecordLogic logic = new YHTrainingRecordLogic();
	
	/**
	 * 新建培训记录--cc
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
	     YHHrTrainingRecord record = (YHHrTrainingRecord) YHFOM.build(map, YHHrTrainingRecord.class, "");
	     int countFlag = 0;
	     String stUserId = record.getStaffUserId();
	     String[] staffUserIdStr = stUserId.split(",");
	     for(int i = 0; i < staffUserIdStr.length; i++){
	       if(!this.logic.existsTrainingRecord(dbConn, staffUserIdStr[i], record.getTPlanNo())){
	         record.setCreateUserId(String.valueOf(person.getSeqId()));
	         record.setCreateDeptId(person.getDeptId());
	         record.setStaffUserId(staffUserIdStr[i]);
	         this.logic.add(dbConn, record);
	       }else{
	         countFlag++;
	       }
	     }
	     String data = String.valueOf(countFlag);
	     request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
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
	 * 培训记录信息 列表--cc
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getTrainingRecordListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.getTrainingRecordListJson(dbConn, request.getParameterMap(), person);
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
   * 获取用户名称 -cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getUserName(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userId = request.getParameter("userId");
      String data = this.logic.getUserNameLogic(dbConn, Integer.parseInt(userId));
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
   * 培训记录详细信息--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRecordDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {

    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHHrTrainingRecord meeting = (YHHrTrainingRecord) this.logic.getRecordDetail(dbConn, Integer.parseInt(seqId));
      if (meeting == null) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "未找到相应记录");
        return "/core/inc/rtjson.jsp";
      }
      StringBuffer data = YHFOM.toJson(meeting);
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
   * 获取培训记录管理列表--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getTrainingRecordSearchList(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    ArrayList<YHAddress> addressList = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String userId = YHDBUtility.escapeLike(request.getParameter("userId"));
      String tPlanNo = YHDBUtility.escapeLike(request.getParameter("tPlanNo"));
      String tInstitutionName = YHDBUtility.escapeLike(request.getParameter("tInstitutionName"));
      String trainningCost = YHDBUtility.escapeLike(request.getParameter("trainningCost"));
      String data = "";
      data = this.logic.getTrainingRecordSearchList(dbConn, request.getParameterMap(), person, userId, tPlanNo, tInstitutionName, trainningCost);
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
   * 获取培训记录信息(编辑)--cc
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
      YHHrTrainingRecord record = (YHHrTrainingRecord)this.logic.getRecordDetail(dbConn, Integer.parseInt(seqId));
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
   * 编辑培训记录--cc
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
      Map<String,String[]> map = request.getParameterMap();
      YHHrTrainingRecord record = (YHHrTrainingRecord) YHFOM.build(map, YHHrTrainingRecord.class, "");
      this.logic.updateRecord(dbConn, record);
      
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

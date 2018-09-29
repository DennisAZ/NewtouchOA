package yh.subsys.oa.hr.recruit.filter.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.recruit.filter.data.YHHrRecruitFilter;
import yh.subsys.oa.hr.recruit.filter.logic.YHHrRecruitFilterLogic;

public class YHHrRecruitFilterAct {
	private YHHrRecruitFilterLogic logic = new YHHrRecruitFilterLogic();
	public static final String attachmentFolder = "hr";
	public static final String headPicFolder = "recruit_pic";
   /**
  *查找条数�?
  */
  public String show(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    request.setCharacterEncoding("utf-8");
    Connection dbConn = null;
    String seqId = request.getParameter("seqId");
    String data = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Object obj = null;
      YHORM orm = new YHORM();
      obj = orm.loadObjSingle(dbConn, YHHrRecruitFilter.class, Integer.parseInt(seqId));
      if(obj == null) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "不存在此条数据！");
        return "/core/inc/rtjson.jsp";
      }
      StringBuffer sb = YHFOM.toJson(obj);
      data = sb.toString();
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
  *列表加载
  */
  public String list(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    request.setCharacterEncoding("utf-8");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      StringBuffer data = getListJson(dbConn,request.getParameterMap());
      PrintWriter pw = response.getWriter();
      pw.println(data.toString());
      pw.flush();
    }catch(Exception ex) {
      throw ex;
    }
    return null;
  }
 /**
 * 删除数据
 */
  public String deleteField(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");   
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);   
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      orm.deleteSingle(dbConn, YHHrRecruitFilter.class, Integer.parseInt(seqId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"删除成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
  *添加数据
  */
  public String addField(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn 
                      = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      Object obj = YHFOM.build(request.getParameterMap());
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn,obj);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"添加数据成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
  *修改数据
  */  
  public String updateField(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);   
      dbConn = requestDbConn.getSysDbConn();
      Object obj = YHFOM.build(request.getParameterMap());
      YHORM orm = new YHORM();
      orm.updateSingle(dbConn,obj);           
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"编辑数据成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";    
  }
  
  /**
 * @param conn
 * @param request
 * @param userId
 * @return
 * @throws Exception
 */
  public StringBuffer getListJson(Connection conn,Map request) throws Exception{
    StringBuffer sub = new StringBuffer();
    String sql =  "select " +
    		"SEQ_ID" +
    		",CREATE_USER_ID" +
        ",CREATE_DEPT_ID" +
        ",EXPERT_ID" +
        ",EMPLOYEE_NAME" +
        ",PLAN_NO" +
        ",POSITION" +
        ",EMPLOYEE_MAJOR" +
        ",EMPLOYEE_PHONE" +
        ",TRANSACTOR_STEP" +
        ",STEP_FLAG" +
        ",END_FLAG" +
        ",NEXT_DATE_TIME" +
        ",NEXT_TRANSA_STEP" +
        ",FILTER_METHOD1" +
        ",FILTER_DATE_TIME1" +
        ",FIRST_CONTENT1" +
        ",FIRST_VIEW1" +
        ",TRANSACTOR_STEP1" +
        ",PASS_OR_NOT1" +
        ",NEXT_TRANSA_STEP1" +
        ",NEXT_DATE_TIME1" +
        ",FILTER_METHOD2" +
        ",FILTER_DATE_TIME2" +
        ",FIRST_CONTENT2" +
        ",FIRST_VIEW2" +
        ",TRANSACTOR_STEP2" +
        ",PASS_OR_NOT2" +
        ",NEXT_TRANSA_STEP2" +
        ",NEXT_DATE_TIME2" +
        ",FILTER_METHOD3" +
        ",FILTER_DATE_TIME3" +
        ",FIRST_CONTENT3" +
        ",FIRST_VIEW3" +
        ",TRANSACTOR_STEP3" +
        ",PASS_OR_NOT3" +
        ",NEXT_TRANSA_STEP3" +
        ",NEXT_DATE_TIME3" +
        ",FILTER_METHOD4" +
        ",FILTER_DATE_TIME4" +
        ",FIRST_CONTENT4" +
        ",FIRST_VIEW4" +
        ",TRANSACTOR_STEP4" +
        ",PASS_OR_NOT4" +
        " from " +
    		"oa_pm_enroll_filter";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request,YHPageQueryParam.class,null);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    sub.append(pageDataList.toJson());
    return sub;
  }
  
  /**
	 *招聘筛选 列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getHrFilterListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.getHrFilterListLogic(dbConn, request.getParameterMap(), person);
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
	 * 招聘筛选-模糊查找
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getHrFilterList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String keyWord = request.getParameter("condition");
		keyWord = YHUtility.decodeURL(keyWord); //解码
	if(YHUtility.isNullorEmpty(keyWord)){
		keyWord = "";
	}
	String userId = request.getParameter("userId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			StringBuffer sb = new StringBuffer("[");
			YHPerson person = null;
			if(!YHUtility.isNullorEmpty(userId)){
				person = new YHPerson();
				person.setSeqId(Integer.parseInt(userId));//从页面中传过来的用户信息
			}else{
				person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
			}
			List<YHHrRecruitFilter> filters = this.logic.getHrFilterListLogic(dbConn,person,keyWord);
			boolean isHave = false;
			if (filters!=null && filters.size()>0) {
				for(YHHrRecruitFilter filter:filters){
					sb.append("{");
					sb.append("expertId:\"" +  filter.getSeqId() + "\"");
					sb.append(",employeeName:\"" +  YHUtility.encodeSpecial(YHUtility.null2Empty(filter.getEmployeeName()))+ "\"");
					sb.append("},");
					isHave = true;
				}
				if (isHave) {
					sb.deleteCharAt(sb.length()-1);
				}
				sb.append("]");
				
			}else {
				sb.append("]");
			}
			request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	/**
	 * 新建筛选信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addHrFilterInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String expertId = request.getParameter("expertId");
		String employeeName = request.getParameter("employeeName");
		String planName = request.getParameter("planName");
		String planNo = request.getParameter("planNo");
		String position = request.getParameter("position");
		String employeeMajor = request.getParameter("employeeMajor");
		String employeePhone = request.getParameter("employeePhone");
		String transactorStepName = request.getParameter("transactorStepName");
		String transactorStep = request.getParameter("transactorStep");
		String nextTransaStep = request.getParameter("nextTransaStep");
		String nextDateTime = request.getParameter("nextDateTime");
		String smsRemind = request.getParameter("smsRemind");
		String sms2Remind = request.getParameter("sms2Remind");
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("expertId", expertId);
		map.put("employeeName", employeeName);
		map.put("planName", planName);
		map.put("planNo", planNo);
		map.put("position", position);
		map.put("employeeMajor", employeeMajor);
		map.put("employeePhone", employeePhone);
		map.put("transactorStepName", transactorStepName);
		map.put("transactorStep", transactorStep);
		map.put("nextTransaStep", nextTransaStep);
		map.put("nextDateTime", nextDateTime);
		map.put("smsRemind", smsRemind);
		map.put("sms2Remind", sms2Remind);
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
			this.logic.addHrFilterInfoLogic(dbConn,person,map);
			
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "新建成功");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	/**
	 * 删除筛选信息 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String deleteHrFilter(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			this.logic.deleteHrFilterLogic(dbConn, seqIdStr);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}
	/**
	 * 获取人员名称 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getPersonName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String userName = this.logic.getUserNameLogic(dbConn, seqIdStr);
			String data = "{userName:\"" + YHUtility.encodeSpecial(userName) + "\"}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功获取数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	/**
   * 招聘筛选详情 
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getHrFilterDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		int seqId = 0;
		if (YHUtility.isNumber(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
    Connection dbConn = null;
    YHORM orm = new YHORM();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHHrRecruitFilter filter = (YHHrRecruitFilter) orm.loadObjSingle(dbConn, YHHrRecruitFilter.class, seqId);
      if (filter == null) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "未找到相应记录");
        return "/core/inc/rtjson.jsp";
      }
      StringBuffer data = YHFOM.toJson(filter);
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
	 * 招聘计划名称 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getPlanNameByPlanNo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String planNoStr = request.getParameter("planNo");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String planName = this.logic.getPlanNameByPlanNoLogic(dbConn, planNoStr);
			String data = "{planName:\"" + YHUtility.encodeSpecial(planName) + "\"}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功获取数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	/**
	 * 编辑筛选信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateHrFilterInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String seqId = request.getParameter("seqId");
		String expertId = request.getParameter("expertId");
		String employeeName = request.getParameter("employeeName");
		String planName = request.getParameter("planName");
		String planNo = request.getParameter("planNo");
		String position = request.getParameter("position");
		String employeeMajor = request.getParameter("employeeMajor");
		String employeePhone = request.getParameter("employeePhone");
		String transactorStepName = request.getParameter("transactorStepName");
		String transactorStep = request.getParameter("transactorStep");
		String nextTransaStep = request.getParameter("nextTransaStep");
		String nextDateTime = request.getParameter("nextDateTime");
		String smsRemind = request.getParameter("smsRemind");
		String sms2Remind = request.getParameter("sms2Remind");
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("seqId", seqId);
		map.put("expertId", expertId);
		map.put("employeeName", employeeName);
		map.put("planName", planName);
		map.put("planNo", planNo);
		map.put("position", position);
		map.put("employeeMajor", employeeMajor);
		map.put("employeePhone", employeePhone);
		map.put("transactorStepName", transactorStepName);
		map.put("transactorStep", transactorStep);
		map.put("nextTransaStep", nextTransaStep);
		map.put("nextDateTime", nextDateTime);
		map.put("smsRemind", smsRemind);
		map.put("sms2Remind", sms2Remind);
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
			this.logic.updateHrFilterInfoLogic(dbConn,person,map);
			
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "新建成功");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	/**
	 * 处理筛选信息1
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String dealWithFilter1(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String seqId = request.getParameter("seqId");
		String stepFlag = request.getParameter("stepFlag");
		String isFinish = request.getParameter("IS_FINISH");
		
		String passOrNot1 = request.getParameter("PASS_OR_NOT1");
		String filterMethod1 = request.getParameter("filterMethod1");
		String filterDateTime1 = request.getParameter("filterDateTime1");
		String firstContent1 = request.getParameter("firstContent1");
		String firstView1 = request.getParameter("firstView1");
		String transactorStep1 = request.getParameter("fTransactorStep1");
		String nextTransaStep1 = request.getParameter("nextTransaStep1");
		String nextDateTime1 = request.getParameter("nextDateTime1");
		String smsRemind1 = request.getParameter("smsRemind1");
		String sms2Remind1 = request.getParameter("sms2Remind1");
		
		
		String passOrNot2 = request.getParameter("PASS_OR_NOT2");
		String dbFilterMethod2 = request.getParameter("dbFilterMethod2");
		String dbFilterDateTime2 = request.getParameter("dbFilterDateTime2");
		String dbFirstContent2 = request.getParameter("dbFirstContent2");
		String dbFirstView2 = request.getParameter("dbFirstView2");
		String dbTransactorStep2 = request.getParameter("dbTransactorStep2");
		String dbNextTransaStep2 = request.getParameter("dbNextTransaStep2");
		String dbNextDateTime2 = request.getParameter("dbNextDateTime2");
		String smsRemind2 = request.getParameter("smsRemind2");
		String sms2Remind2 = request.getParameter("sms2Remind2");
		
		
		String passOrNot3 = request.getParameter("PASS_OR_NOT3");
		String dbFilterMethod3 = request.getParameter("dbFilterMethod3");
		String dbFilterDateTime3 = request.getParameter("dbFilterDateTime3");
		String dbFirstContent3 = request.getParameter("dbFirstContent3");
		String dbFirstView3 = request.getParameter("dbFirstView3");
		String dbTransactorStep3 = request.getParameter("dbTransactorStep3");
		String dbNextTransaStep3 = request.getParameter("dbNextTransaStep3");
		String dbNextDateTime3 = request.getParameter("dbNextDateTime3");
		String smsRemind3 = request.getParameter("smsRemind3");
		String sms2Remind3 = request.getParameter("sms2Remind3");
		
		String passOrNot4 = request.getParameter("PASS_OR_NOT4");
		String dbFilterMethod4 = request.getParameter("dbFilterMethod4");
		String dbFilterDateTime4 = request.getParameter("dbFilterDateTime4");
		String dbFirstContent4 = request.getParameter("dbFirstContent4");
		String dbFirstView4 = request.getParameter("dbFirstView4");
		String dbTransactorStep4 = request.getParameter("dbTransactorStep4");
//		String dbNextTransaStep4 = request.getParameter("dbNextTransaStep4");
//		String dbNextDateTime4 = request.getParameter("dbNextDateTime4");
		String smsRemind4 = request.getParameter("smsRemind4");
		String sms2Remind4 = request.getParameter("sms2Remind4");
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("seqId", seqId);
		map.put("stepFlag", stepFlag);
		map.put("isFinish", isFinish);
		
		YHORM orm = new YHORM();
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
			
			if ("1".equals(stepFlag)) {
				map.put("passOrNot1", passOrNot1);
				map.put("filterMethod1", filterMethod1);
				map.put("filterDateTime1", filterDateTime1);
				map.put("firstContent1", firstContent1);
				map.put("firstView1", firstView1);
				map.put("transactorStep1", transactorStep1);
				map.put("nextTransaStep1", nextTransaStep1);
				map.put("nextDateTime1", nextDateTime1);
				map.put("smsRemind1", smsRemind1);
				map.put("sms2Remind1", sms2Remind1);
				
				this.logic.dealWithFilter1Logic(dbConn,person,map);
			}else if ("2".equals(stepFlag)) {
				map.put("passOrNot2", passOrNot2);
				map.put("dbFilterMethod2", dbFilterMethod2);
				map.put("dbFilterDateTime2", dbFilterDateTime2);
				map.put("dbFirstContent2", dbFirstContent2);
				map.put("dbFirstView2", dbFirstView2);
				map.put("dbTransactorStep2", dbTransactorStep2);
				map.put("dbNextTransaStep2", dbNextTransaStep2);
				map.put("dbNextDateTime2", dbNextDateTime2);
				map.put("smsRemind2", smsRemind2);
				map.put("sms2Remind2", sms2Remind2);
				this.logic.dealWithFilter2Logic(dbConn,person,map);
			}else if ("3".equals(stepFlag)) {
				map.put("passOrNot3", passOrNot3);
				map.put("dbFilterMethod3", dbFilterMethod3);
				map.put("dbFilterDateTime3", dbFilterDateTime3);
				map.put("dbFirstContent3", dbFirstContent3);
				map.put("dbFirstView3", dbFirstView3);
				map.put("dbTransactorStep3", dbTransactorStep3);
				map.put("dbNextTransaStep3", dbNextTransaStep3);
				map.put("dbNextDateTime3", dbNextDateTime3);
				map.put("smsRemind3", smsRemind3);
				map.put("sms2Remind3", sms2Remind3);
				this.logic.dealWithFilter3Logic(dbConn,person,map);
			}else if ("4".equals(stepFlag)) {
				map.put("passOrNot4", passOrNot4);
				map.put("dbFilterMethod4", dbFilterMethod4);
				map.put("dbFilterDateTime4", dbFilterDateTime4);
				map.put("dbFirstContent4", dbFirstContent4);
				map.put("dbFirstView4", dbFirstView4);
				map.put("dbTransactorStep4", dbTransactorStep4);
//				map.put("dbNextTransaStep4", dbNextTransaStep4);
//				map.put("dbNextDateTime4", dbNextDateTime4);
				map.put("smsRemind4", smsRemind4);
				map.put("sms2Remind4", sms2Remind4);
				this.logic.dealWithFilter4Logic(dbConn,person,map);
			}
			
			int dbSeqId = 0;
			if (YHUtility.isNumber(seqId)) {
				dbSeqId = Integer.parseInt(seqId);
			}
			
			boolean passOrNotFlag = false;
			boolean finishFlag = false;
			if ("1".equalsIgnoreCase(passOrNot1) || "1".equalsIgnoreCase(passOrNot2)|| "1".equalsIgnoreCase(passOrNot3)|| "1".equalsIgnoreCase(passOrNot4)) {
				passOrNotFlag = true;
			}
			
			if ("1".equalsIgnoreCase(isFinish) && passOrNotFlag) {
				finishFlag = true;
			}
			
			if (("4".equals(stepFlag) && "1".equals(passOrNot4 )) || finishFlag) {
				this.logic.updateHrcecruitPool(dbConn, dbSeqId);
				YHHrRecruitFilter filter = (YHHrRecruitFilter) orm.loadObjSingle(dbConn, YHHrRecruitFilter.class, dbSeqId);
				if (filter!=null) {
					filter.setEndFlag(2);
					orm.updateSingle(dbConn, filter);
				}
			}
			
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "新建成功");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	/**
	 *招聘筛选查询 列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String queryFilterListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("employeeName", YHUtility.null2Empty(request.getParameter("employeeName")));
		map.put("planName", YHUtility.null2Empty(request.getParameter("planName")));
		map.put("planNo", YHUtility.null2Empty(request.getParameter("planNo")));
		map.put("position", YHUtility.null2Empty(request.getParameter("position")));
		map.put("employeeMajor", YHUtility.null2Empty(request.getParameter("employeeMajor")));
		
		map.put("employeePhone", YHUtility.null2Empty(request.getParameter("employeePhone")));
		map.put("transactorStep", YHUtility.null2Empty(request.getParameter("transactorStep")));
		map.put("nextTransaStep1", YHUtility.null2Empty(request.getParameter("nextTransaStep1")));
		map.put("nextTransaStep2", YHUtility.null2Empty(request.getParameter("nextTransaStep2")));
		map.put("nextTransaStep3", YHUtility.null2Empty(request.getParameter("nextTransaStep3")));
		map.put("nextTransaStep4", YHUtility.null2Empty(request.getParameter("nextTransaStep4")));
		map.put("status", YHUtility.null2Empty(request.getParameter("status")));
		
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.queryFilterListLogic(dbConn, request.getParameterMap(), person,map);
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
	 * 获取处理状态
	 * 2011-3-28
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String passORNot(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		int seqId = 0;
		if (YHUtility.isNumber(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String titleStatus = this.logic.passORNotLogic(dbConn,seqId);
			String data = "{titleStatus:\"" + YHUtility.encodeSpecial(titleStatus) + "\"}";
			request.setAttribute(YHActionKeys.RET_DATA, data);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "ok");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
		}
		return "/core/inc/rtjson.jsp";
	}
	
	
	
	

}

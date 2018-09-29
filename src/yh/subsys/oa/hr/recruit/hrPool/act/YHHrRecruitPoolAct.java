package yh.subsys.oa.hr.recruit.hrPool.act;

import java.io.File;
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
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.recruit.hrPool.data.YHHrRecruitPool;
import yh.subsys.oa.hr.recruit.hrPool.logic.YHHrRecruitPoolLogic;
import yh.subsys.oa.hr.recruit.plan.data.YHHrRecruitPlan;

public class YHHrRecruitPoolAct {
	private YHHrRecruitPoolLogic logic = new YHHrRecruitPoolLogic();
	public static final String attachmentFolder = "hr";
	public static final String headPicFolder = "recruit_pic";// 头像图片>>D:\MYOA\webroot\attachment\hrms_pic

  /**
	 *人才档案管理列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getHrPoolListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.getHrPoolListJsonLogic(dbConn, request.getParameterMap(), person);
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
	 * 招聘计划名称-模糊查找
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getHrRecruitPlanList(HttpServletRequest request, HttpServletResponse response) throws Exception{
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
			List<YHHrRecruitPlan> plans = this.logic.getHrRecruitPlanListLogic(dbConn,person,keyWord);
			boolean isHave = false;
			if (plans!=null && plans.size()>0) {
				for(YHHrRecruitPlan plan:plans){
					sb.append("{");
					sb.append("seqId:\"" +  plan.getSeqId() + "\"");
					sb.append(",planNo:\"" +  YHUtility.encodeSpecial(YHUtility.null2Empty(plan.getPlanNo()))+ "\"");
					sb.append(",planName:\"" +  YHUtility.encodeSpecial(YHUtility.null2Empty(plan.getPlanName()))+ "\"");
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
	 * 新建人才档案
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addHrPoolInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		String contexPath = request.getContextPath();
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			dbConn = requestDbConn.getSysDbConn();
			this.logic.setNewHrPoolInfoValueLogic(dbConn, fileForm, person);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		response.sendRedirect(contexPath + "/subsys/oa/hr/recruit/hrPool/newWarn.jsp");
		return null;
	}
	
	 /**
	 *人才档案查询列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String queryHrPoolListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("planNo", YHUtility.null2Empty(request.getParameter("planNo")));
		map.put("employeeName", YHUtility.null2Empty(request.getParameter("employeeName")));
		map.put("employeeSex", YHUtility.null2Empty(request.getParameter("employeeSex")));
		map.put("employeeNativePlace", YHUtility.null2Empty(request.getParameter("employeeNativePlace")));
		map.put("employeePoliticalStatus", YHUtility.null2Empty(request.getParameter("employeePoliticalStatus")));
		map.put("position", YHUtility.null2Empty(request.getParameter("position")));
		map.put("jobCategory", YHUtility.null2Empty(request.getParameter("jobCategory")));
		map.put("jobIntension", YHUtility.null2Empty(request.getParameter("jobIntension")));
		map.put("workCity", YHUtility.null2Empty(request.getParameter("workCity")));
		map.put("expectedSalary", YHUtility.null2Empty(request.getParameter("expectedSalary")));
		
		map.put("startWorking", YHUtility.null2Empty(request.getParameter("startWorking")));
		map.put("employeeMajor", YHUtility.null2Empty(request.getParameter("employeeMajor")));
		map.put("employeeHighestSchool", YHUtility.null2Empty(request.getParameter("employeeHighestSchool")));
		map.put("residencePlace", YHUtility.null2Empty(request.getParameter("residencePlace")));
		map.put("employeeNationality", YHUtility.null2Empty(request.getParameter("employeeNationality")));
		map.put("employeeHealth", YHUtility.null2Empty(request.getParameter("employeeHealth")));
		map.put("employeeMaritalStatus", YHUtility.null2Empty(request.getParameter("employeeMaritalStatus")));
		map.put("employeeDomicilePlace", YHUtility.null2Empty(request.getParameter("employeeDomicilePlace")));
		map.put("graduationSchool", YHUtility.null2Empty(request.getParameter("graduationSchool")));
		map.put("computerLevel", YHUtility.null2Empty(request.getParameter("computerLevel")));
		
		map.put("foreignLanguage1", YHUtility.null2Empty(request.getParameter("foreignLanguage1")));
		map.put("foreignLanguage2", YHUtility.null2Empty(request.getParameter("foreignLanguage2")));
		map.put("foreignLevel1", YHUtility.null2Empty(request.getParameter("foreignLevel1")));
		map.put("foreignLevel2", YHUtility.null2Empty(request.getParameter("foreignLevel2")));
		
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.queryHrPoolListJsonLogic(dbConn, request.getParameterMap(), person,map);
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
	 * 删除人才档案 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String deleteHrPool(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String filePath = YHSysProps.getAttachPath() + File.separator + this.attachmentFolder + File.separator;
			this.logic.deleteHrPoolLogic(dbConn, seqIdStr,filePath);
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
   * 人才档案详情 
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getHrPoolnDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
      YHHrRecruitPool recruitPool = (YHHrRecruitPool) orm.loadObjSingle(dbConn, YHHrRecruitPool.class, seqId);;
      if (recruitPool == null) {
//        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
//        request.setAttribute(YHActionKeys.RET_MSRG, "未找到相应记录");
        return "/core/inc/rtjson.jsp";
      }
      StringBuffer data = YHFOM.toJson(recruitPool);
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
	 * 编辑人才档案
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateHrPoolInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		String contexPath = request.getContextPath();
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			dbConn = requestDbConn.getSysDbConn();
			this.logic.setUpdateHrPoolInfoValueLogic(dbConn, fileForm, person,seqIdStr);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		response.sendRedirect(contexPath + "/subsys/oa/hr/recruit/hrPool/newWarn.jsp?updateFlag=1");
		return null;
	}
	
	/**
	 * 浮动菜单文件删除
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delFloatFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		String attachId = request.getParameter("delAttachId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			boolean updateFlag = this.logic.updateFloadFile(dbConn, seqIdStr, YHUtility.null2Empty(attachId));
			int returnFlag = 0;
			if (updateFlag) {
				returnFlag = 1;
			}
			String data = "{updateFlag:\"" + returnFlag + "\"}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 删除头像图片
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String deletePhoto(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			this.logic.deletePhotoLogic(dbConn, seqIdStr);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	
}




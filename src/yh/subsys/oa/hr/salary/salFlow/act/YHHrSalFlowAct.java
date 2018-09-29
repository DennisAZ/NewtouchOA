package yh.subsys.oa.hr.salary.salFlow.act;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.salary.salFlow.data.YHSalFlow;
import yh.subsys.oa.hr.salary.salFlow.logic.YHHrSalFlowLogic;

public class YHHrSalFlowAct {
	private YHHrSalFlowLogic logic = new YHHrSalFlowLogic();
	public static final String attachmentFolder = "hr";

	/**
	 * 新建工资流程
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addSalFlowInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		String contexPath = request.getContextPath();
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			dbConn = requestDbConn.getSysDbConn();
			this.logic.setNewSalFlowValueLogic(dbConn, fileForm, person);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		response.sendRedirect(contexPath + "/subsys/oa/hr/salary/manage/salFlow/manage.jsp");
		return null;
	}

	/**
	 * 工资流程 通用列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getSalFlowListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.getSalFlowJsonLogic(dbConn, request.getParameterMap(), person);
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
	 * 更改工资流程状态
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String doIssend(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		String seqId = request.getParameter("seqId");
		String flag = request.getParameter("flag");
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			this.logic.doIssend(dbConn, seqId, flag);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 获取详情
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getSalFlowDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqId = request.getParameter("seqId");
		if (YHUtility.isNullorEmpty(seqId)) {
			seqId = "0";
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHSalFlow salFlow = (YHSalFlow) this.logic.getSalFlowDetailLogic(dbConn, Integer.parseInt(seqId));
			if (salFlow == null) {
				request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
				request.setAttribute(YHActionKeys.RET_MSRG, "未找到相应记录");
				return "/core/inc/rtjson.jsp";
			}
			StringBuffer data = YHFOM.toJson(salFlow);
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
	 * 编辑工资流程
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateSalFlowInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		String contexPath = request.getContextPath();
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			this.logic.updateSalFlowInfoLogic(dbConn, fileForm, person);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		response.sendRedirect(contexPath + "/subsys/oa/hr/salary/manage/salFlow/manage.jsp");
		return null;
	}

	/**
	 * 删除工资流程
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String deleteFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			this.logic.deleteFileLogic(dbConn, seqIdStr);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 下载CSV模板
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String downCSVTemplet(HttpServletRequest request, HttpServletResponse response) throws Exception {
	  response.setCharacterEncoding(YHConst.CSV_FILE_CODE);
	  Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			String fileName = URLEncoder.encode("工资模板.csv", "UTF-8");
			fileName = fileName.replaceAll("\\+", "%20");
			response.setHeader("Cache-control", "private");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Accept-Ranges", "bytes");
			response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
			response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
			ArrayList<YHDbRecord> dbL = this.logic.downCSVTempletLogic(dbConn);
			YHCSVUtil.CVSWrite(response.getWriter(), dbL);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return null;
	}

	/**
	 * 导入CSV工资数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String impSalDataInfoToCsv(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute("LOGIN_USER");
			StringBuffer buffer = new StringBuffer("[");
			Map<Object, Object> returnMap = this.logic.impTransInfoToCsvLogic(dbConn, fileForm, person, buffer);
			int isCount = (Integer) returnMap.get("isCount");
			int updateCount = (Integer) returnMap.get("updateCount");
			if (buffer.charAt(buffer.length() - 1) == ',') {
				buffer.deleteCharAt(buffer.length() - 1);
			}
			buffer.append("]");
			String data = buffer.toString();
			request.setAttribute("isCount", isCount);
			request.setAttribute("updateCount", updateCount);
			request.setAttribute("contentList", data);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "导入数据成功！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, "导入数据失败");
			throw e;
		}
		return "/subsys/oa/hr/salary/manage/salFlow/import.jsp";
	}

	/**
	 * 获取总数
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getSalDataCountByFlowId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String flowIdStr = request.getParameter("flowId");
		int flowId = 0;
		if (YHUtility.isNumber(flowIdStr)) {
			flowId = Integer.parseInt(flowIdStr);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			int count = this.logic.getSalDataCountByFlowIdLogic(dbConn, flowId);
			if (count > 0) {
				count = 1;
			}
			String data = "{count:\"" + count + "\"}";
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
	 * 获取导出可选字段
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getSalItemNames(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String flowIdStr = request.getParameter("flowId");
		int flowId = 0;
		if (YHUtility.isNumber(flowIdStr)) {
			flowId = Integer.parseInt(flowIdStr);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String data = this.logic.getSalItemNamesLogic(dbConn, flowId);
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
	 * 获取工资报表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getSalItemTable(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String flowId = request.getParameter("flowId");
		String userId = request.getParameter("userId");
		String deptId = request.getParameter("deptId");
		String fldStr = request.getParameter("fldStr");
		String deptFlag = request.getParameter("deptFlag");

		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("flowId", flowId);
		map.put("userId", userId);
		map.put("deptId", deptId);
		map.put("fldStr", fldStr);
		map.put("deptFlag", deptFlag);
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String data = this.logic.getSalItemTableLogic(dbConn, map);
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
	 * 根据条件导出数据到Excel文件
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String exportToExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("flowId", YHUtility.null2Empty(request.getParameter("flowId")));
		map.put("userId", YHUtility.null2Empty(request.getParameter("user")));
		map.put("deptId", YHUtility.null2Empty(request.getParameter("deptStr")));
		map.put("fldStr", YHUtility.null2Empty(request.getParameter("fieldName")));
		map.put("deptFlag", YHUtility.null2Empty(request.getParameter("deptFlag")));
		OutputStream ops = null;
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String fileName = URLEncoder.encode("工资报表.xls", "UTF-8");
			fileName = fileName.replaceAll("\\+", "%20");
			response.setHeader("Cache-control", "private");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Accept-Ranges", "bytes");
			response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
			response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
			ops = response.getOutputStream();
			ArrayList<YHDbRecord> dbL = this.logic.exportToExcelLogic(dbConn, map, person);
//			YHCSVUtil.CVSWrite(ops, dbL);
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
   * 发送email工资条
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String sendEMail(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String flowId = request.getParameter("flowId");
    String content = request.getParameter("content");
    String userIdStr = request.getParameter("userIdStr");
    String fldStr = request.getParameter("fldStr");
    String isZero = request.getParameter("isZero");
    
    Map<Object, Object> map = new HashMap<Object, Object>();
    map.put("flowId", flowId);
    map.put("content",content );
    map.put("userIdStr", userIdStr);
    map.put("fldStr", fldStr);
    map.put("isZero",isZero);
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute("LOGIN_USER");
      String data = this.logic.sendEMail(dbConn,map,person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
      
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 发送手机工资条
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String sendMobile(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String flowId = request.getParameter("flowId");
    String content = request.getParameter("content");
    String userIdStr = request.getParameter("userIdStr");
    String fldStr = request.getParameter("fldStr");
    
    
    Map<Object, Object> map = new HashMap<Object, Object>();
    map.put("flowId", flowId);
    map.put("content",content );
    map.put("userIdStr", userIdStr);
    map.put("fldStr", fldStr);
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute("LOGIN_USER");
      String data = this.logic.sendMobile(dbConn,map,person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
      
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }

}

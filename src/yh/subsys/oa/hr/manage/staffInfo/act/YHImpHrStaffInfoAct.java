package yh.subsys.oa.hr.manage.staffInfo.act;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.hr.manage.staffInfo.logic.YHImpHrStaffInfoLogic;

public class YHImpHrStaffInfoAct {

	private YHImpHrStaffInfoLogic logic = new YHImpHrStaffInfoLogic();

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
	  try {
			String fileName = URLEncoder.encode("人事档案模板.csv", "UTF-8");
			fileName = fileName.replaceAll("\\+", "%20");
			response.setHeader("Cache-control", "private");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Accept-Ranges", "bytes");
			response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
			response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
			ArrayList<YHDbRecord> dbL = this.logic.downCSVTempletLogic();
			YHCSVUtil.CVSWrite(response.getWriter(), dbL);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return null;
	}

	/**
	 * 导入人员档案信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String impStaffInfoToCsv(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute("LOGIN_USER");
			StringBuffer buffer = new StringBuffer("[");
			Map<Object, Object> returnMap =  this.logic.impStaffInfoToCsvLogic(dbConn, fileForm, person, buffer);
			
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
		return "/subsys/oa/hr/manage/staffInfo/import.jsp";

	}

}

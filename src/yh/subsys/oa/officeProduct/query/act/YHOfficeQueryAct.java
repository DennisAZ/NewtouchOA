package yh.subsys.oa.officeProduct.query.act;

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
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.subsys.oa.officeProduct.query.logic.YHOfficeQueryLogic;

public class YHOfficeQueryAct {
	private YHOfficeQueryLogic logic = new YHOfficeQueryLogic();

	/**
	 * 办公用品目录树获取(办公用品信息查询) 2011-3-29
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getQueryTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = "";
			if (!YHUtility.isNullorEmpty(id) && !id.equals("0")) {
				String idArry[] = id.split(",");
				if (idArry != null && idArry.length > 0) {
					if (idArry.length > 1 && idArry.length < 3) {
						data = this.logic.getTypeTreeLogic(dbConn, person, idArry[0]);
					} else if (idArry.length > 2 && idArry.length < 4) {
						data = this.logic.getProductsTreeLogic(dbConn, person, idArry[0]);
					}
				}
			} else {
				data = this.logic.getQueryTreeLogic(dbConn, person);
			}
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "返回数据成功！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 办公用品目录树获取((办公用品信息管理) 2011-3-29
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getManageTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = "";
			if (!YHUtility.isNullorEmpty(id) && !id.equals("0")) {
				String idArry[] = id.split(",");
				if (idArry != null && idArry.length > 0) {
					if ("undefindType".equalsIgnoreCase(idArry[0])) {
						data = this.logic.getUndefindTypePro(dbConn);
					} else {
						if (idArry.length > 1 && idArry.length < 3) {
							data = this.logic.getTypeTreeLogic(dbConn, person, idArry[0]);
						} else if (idArry.length > 2 && idArry.length < 4) {
							data = this.logic.getProductsTreeLogic(dbConn, person, idArry[0]);
						}

					}

				}
			} else {
				data = this.logic.getManageTreeLogic(dbConn, person);
			}
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "返回数据成功！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 查询单个办公用品信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getOneOfficeProductInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String storeId = request.getParameter("storeId");
		String typeId = request.getParameter("typeId");
		String proSeqId = request.getParameter("proSeqId");
		// "storeId="+ <%=storeId%>
		// +"&typeId="+<%=typeId%>+"&proSeqId="+<%=proSeqId%>;
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = "";
			if (!YHUtility.isNullorEmpty(proSeqId)) {
				data = this.logic.getOneOfficeProductInfoLogic(dbConn, person, proSeqId);
			}
			// data = this.logic.getProductsTreeLogic(dbConn, person, idArry[0]);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "返回数据成功！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 获得办公用品信息结果（带分页）
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getOfficeProductsListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("proName", YHDBUtility.escapeLike(request.getParameter("proName")));
			map.put("proDesc", YHDBUtility.escapeLike(request.getParameter("proDesc")));
			map.put("proCode", YHDBUtility.escapeLike(request.getParameter("proCode")));
			map.put("officeDepository", YHDBUtility.escapeLike(request.getParameter("officeDepository")));
			map.put("officeProtype", YHDBUtility.escapeLike(request.getParameter("officeProtype")));
			String data = "";
			data = this.logic.queryOfficeProductsJsonLogic(dbConn, request.getParameterMap(), map, person);
//			System.out.println(data);
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
	 * 办公用品查询 第二个标签页
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String queryOfficeProductsListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("proName", YHDBUtility.escapeLike(request.getParameter("proName")));
			map.put("proDesc", YHDBUtility.escapeLike(request.getParameter("proDesc")));
			map.put("proCode", YHDBUtility.escapeLike(request.getParameter("proCode")));
			map.put("officeDepository", YHDBUtility.escapeLike(request.getParameter("officeDepository")));
			map.put("officeProtype", YHDBUtility.escapeLike(request.getParameter("officeProtype")));
			String data = "";
			data = this.logic.queryOfficeProductsJsonLogic1(dbConn, request.getParameterMap(), map, person);
//			System.out.println(data);
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
	 * 办公用品导出 第二个标签页
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String queryOfficeProductsExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		OutputStream ops = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("proName", YHDBUtility.escapeLike(request.getParameter("proName")));
			map.put("proDesc", YHDBUtility.escapeLike(request.getParameter("proDesc")));
			map.put("proCode", YHDBUtility.escapeLike(request.getParameter("proCode")));
			map.put("officeDepository", YHDBUtility.escapeLike(request.getParameter("officeDepository")));
			map.put("officeProtype", YHDBUtility.escapeLike(request.getParameter("officeProtype")));
			String data = "";
			String fileName = URLEncoder.encode("办公用品.xls", "UTF-8");
			fileName = fileName.replaceAll("\\+", "%20");
			response.setHeader("Cache-control", "private");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Accept-Ranges", "bytes");
			response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
			response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
			ops = response.getOutputStream();
			ArrayList<YHDbRecord> dbL = this.logic.queryOfficeProductsExport(dbConn, request.getParameterMap(), map, person);
			YHJExcelUtil.writeExc(ops, dbL);
		} catch (Exception ex) {
			throw ex;
		} finally {
			ops.close();
		}
		return null;
	}

	/**
	 * 获取单位员工用户名称
	 * 
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
	 * 删除办公用品信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String deleteOfficeProducts(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			if (!YHUtility.isNullorEmpty(seqIdStr)) {
				this.logic.deleteOfficeProductsLogic(dbConn, seqIdStr);
				this.logic.delOfficeProductsByIdLogic(dbConn, seqIdStr);
			}
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "返回数据成功！");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
}

package yh.subsys.oa.officeProduct.product.act;

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
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.officeProduct.product.data.YHOfficeProducts;
import yh.subsys.oa.officeProduct.product.logic.YHOfficeProductsLogic;
import yh.subsys.oa.officeProduct.query.data.YHOfficeType;

public class YHOfficeProductsAct {
	private YHOfficeProductsLogic logic = new YHOfficeProductsLogic();

	/**
	 * 获取办公用品库(返回类别seq_id)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getOfficeDepositoryNames(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.getOfficeDepositoryNamesLogic(dbConn, person);
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
	 * 获取办公用品库(返回类别seq_id)产品编辑页面,如与extData值相等则选中
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getproEditDepositoryNames(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String extData = request.getParameter("extData");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.getproEditDepositoryNamesLogic(dbConn, person,extData);
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
	 * 获取办公用品库(返回库seq_id)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getOfficeDepositoryName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.getOfficeDepositoryNameLogic(dbConn, person);
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
	 * 获取办公用品类别
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getOfficeTypeNamesById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String typeIdStr = request.getParameter("typeId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String data = this.logic.getOfficeTypeNamesByIdLogic(dbConn, typeIdStr);
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
	 * 增加办公用品
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addOfficeProducts(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String proName = request.getParameter("proName");
		String officeProtype = request.getParameter("officeProtype");

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			Map<String, String[]> map = request.getParameterMap();
			YHOfficeProducts products = (YHOfficeProducts) YHFOM.build(map, YHOfficeProducts.class, "");
			int isHave = 0;
			int maxSeqId = 0;
			int counter = this.logic.isHaveValue(dbConn, officeProtype, proName,"");
			if (counter > 0) {
				isHave = 1;
			} else {
				maxSeqId = this.logic.addOfficeProductsLogic(dbConn, products);
			}
			String proNameStr = "";
			if (!YHUtility.isNullorEmpty(products.getProName())) {
				proNameStr = YHUtility.encodeSpecial(products.getProName());
			}
			String data = "{isHave:\"" + isHave + "\",maxSeqId:\"" + maxSeqId + "\",proName:\"" + proNameStr + "\"}";
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
	 * 获取办公用品信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getOfficeProductsById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String proSeqIdStr = request.getParameter("proSeqId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHOfficeProducts products = this.logic.getOfficeProductsById(dbConn, proSeqIdStr);
			if (products == null) {
				request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
				request.setAttribute(YHActionKeys.RET_MSRG, "0");
				return "/core/inc/rtjson.jsp";
			}
			StringBuffer data = YHFOM.toJson(products);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "返回数据成功！");
			request.setAttribute(YHActionKeys.RET_DATA, data.toString());
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 更新办公用品信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateOfficeProductsById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String proSeqIdStr = request.getParameter("proSeqId");
		int proSeqId = 0;
		if (YHUtility.isNumber(proSeqIdStr)) {
			proSeqId = Integer.parseInt(proSeqIdStr);
		}

		String proName = request.getParameter("proName");
		String proDesc = request.getParameter("proDesc");
//		String officeDepository = request.getParameter("officeDepository");
		String officeProtype = request.getParameter("officeProtype");
		String proCode = request.getParameter("proCode");
		String proUnit = request.getParameter("proUnit");
		String proPriceStr = request.getParameter("proPrice");
		String proSupplier = request.getParameter("proSupplier");
		String proLowstockStr = request.getParameter("proLowstock");
		String proMaxstockStr = request.getParameter("proMaxstock");
		String proStockStr = request.getParameter("proStock");
		String proCreator = request.getParameter("proCreator");
		String proAuditer = request.getParameter("proAuditer");
		String proManager = request.getParameter("proManager");
		String proDept = request.getParameter("proDept");
		
		
		double proPrice = 0;
		if (YHUtility.isNumber(proPriceStr)) {
			proPrice = Double.parseDouble(proPriceStr);
		}
		int proLowstock = 0;
		if (YHUtility.isNumber(proLowstockStr)) {
			proLowstock = Integer.parseInt(proLowstockStr);
		}
		int proMaxstock = 0;
		if (YHUtility.isNumber(proMaxstockStr)) {
			proMaxstock = Integer.parseInt(proMaxstockStr);
		}
		int proStock = 0;
		if (YHUtility.isNumber(proStockStr)) {
			proStock = Integer.parseInt(proStockStr);
		}

		Connection dbConn = null;
		YHORM orm = new YHORM();
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			int isHave = 0;
			String proNameStr = "";
			int counter = this.logic.isHaveValue(dbConn, officeProtype, proName,proSeqIdStr);
			if (counter > 0) {
				isHave = 1;
			}else {
				YHOfficeProducts products = this.logic.getOfficeProductsById(dbConn, proSeqIdStr);
				if (products != null) {
					products.setProName(proName);
					products.setProDesc(proDesc);
					products.setProUnit(proUnit);
					products.setProCode(proCode);
					products.setProSupplier(proSupplier);
					products.setProPrice(proPrice);
					products.setProLowstock(proLowstock);
					products.setProMaxstock(proMaxstock);
					products.setProStock(proStock);
					products.setProDept(proDept);
					products.setProManager(proManager);
					products.setProCreator(proCreator);
					products.setOfficeProtype(officeProtype);
					products.setProAuditer(proAuditer);
					orm.updateSingle(dbConn, products);
					
					if (!YHUtility.isNullorEmpty(products.getProName())) {
						proNameStr = YHUtility.encodeSpecial(products.getProName());
					}
				}
			} 
			String data = "{isHave:\"" + isHave + "\",maxSeqId:\"" + proSeqId + "\",proName:\"" + proNameStr + "\"}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "返回数据成功！");
			 request.setAttribute(YHActionKeys.RET_DATA, data.toString());
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
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
	public String delOfficeProductsById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String proSeqIdStr = request.getParameter("proSeqId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			if (!YHUtility.isNullorEmpty(proSeqIdStr)) {
				this.logic.delTranshistoryLogic(dbConn,proSeqIdStr);
				this.logic.delOfficeProductsByIdLogic(dbConn,proSeqIdStr);
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
	/**
	 * 新增办公用品类型定义
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addOfficeType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String depository = request.getParameter("depository");
		String typeNameStr = request.getParameter("typeName");
		String typeOrder = request.getParameter("typeOrder");
		
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			boolean isHaveFlag = this.logic.isHaveOfficeTypeLogic(dbConn,depository,typeNameStr);
			int isHave = 0;
			if (isHaveFlag) {
				isHave = 1;
			}else {
				this.logic.addOfficeTypeLogic(dbConn,depository,typeNameStr,typeOrder);
			}
			String data = "{isHave:\"" + isHave + "\",typeName:\"" + YHUtility.encodeSpecial(typeNameStr) + "\"}";
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
			String fileName = URLEncoder.encode("办公用品信息模板.csv", "UTF-8");
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
	 * 导入办公用品信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String impOfficeProToCsv(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute("LOGIN_USER");
			StringBuffer buffer = new StringBuffer("[");
			Map<Object, Object> returnMap =  this.logic.impOfficeProToCsvLogic(dbConn, fileForm, person,buffer );
			if (buffer.charAt(buffer.length() - 1) == ',') {
				buffer.deleteCharAt(buffer.length() - 1);
			}
			buffer.append("]");
			String data = buffer.toString();
			
			int isCount = (Integer) returnMap.get("isCount");
			request.setAttribute("isCount", isCount);
			request.setAttribute("contentList", data);
			
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "导入数据成功！");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/subsys/oa/officeProduct/product/proImport.jsp";
	}
	/**
	 * 获取办公用品类别
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getOfficeType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String data = this.logic.getOfficeTypeLogic(dbConn);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "导入数据成功！");
			request.setAttribute(YHActionKeys.RET_DATA,data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, "导入数据失败");
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	public String getOfficeTypeById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String typeIdStr = request.getParameter("typeId");
		int typeId = 0;
		if (YHUtility.isNumber(typeIdStr)) {
			typeId = Integer.parseInt(typeIdStr);
		}
		YHORM orm = new YHORM();
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHOfficeType officeType = (YHOfficeType) orm.loadObjSingle(dbConn, YHOfficeType.class, typeId);
			StringBuffer data = YHFOM.toJson(officeType);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "取出数据成功！");
			request.setAttribute(YHActionKeys.RET_DATA,data.toString());
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, "取出数据失败");
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	/**
	 * 更新类别
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateTypeName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String typeIdStr = request.getParameter("codeId");
		String depositoryIdStr = request.getParameter("depository");
		String codeNameStr = request.getParameter("codeName");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			boolean isHaveFlag = this.logic.isHaveOfficeType2Logic(dbConn, depositoryIdStr, codeNameStr);
			int isHave = 0;
			if (isHaveFlag) {
				isHave = 1;
			}else {
				this.logic.updateOfficeTypeLogic(dbConn,depositoryIdStr,codeNameStr,typeIdStr);
			}
			String data = "{isHave:\"" + isHave + "\",typeName:\"" + YHUtility.encodeSpecial(codeNameStr) + "\"}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "取出数据成功！");
			request.setAttribute(YHActionKeys.RET_DATA,data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, "取出数据失败");
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	/**
	 * 删除类别
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delTypeName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String typeIdStr = request.getParameter("typeId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			this.logic.delTypeNameLogic(dbConn,typeIdStr);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "取出数据成功！");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, "取出数据失败");
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	/**
	 * 获取办公用品类别(根据库的id)
	 * 2011-3-29
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	public String getTypeNamesByStoreId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String storeIdStr = request.getParameter("storeId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String data = this.logic.getTypeNamesByStoreIdLogic(dbConn, storeIdStr);
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
	
	
	

}

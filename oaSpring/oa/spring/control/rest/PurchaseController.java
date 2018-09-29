package oa.spring.control.rest;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oa.spring.service.PurchaseService;
import oa.spring.service.SaleOrderService;
import oa.spring.util.StaticData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {

	@Autowired
	private PurchaseService purchaseService;
	@Autowired
	private SaleOrderService saleOrderService;
	private static final Logger logger = Logger.getLogger(PurchaseController.class);

	/**
	 * 
	 * @param tableName
	 *            表名称
	 * 
	 * @return 要展示的对象的集合
	 */

	// 查询出采购详情
	@RequestMapping(value = "getPurchaseById", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String, Object>> getPurchaseById(HttpServletRequest request) {
		String purchaseId = request.getParameter("purchaseId");
		List<Map<String, Object>> list = purchaseService
				.getPurchaseById(purchaseId);
		return list;
	}
	// 查询出采购详情
	@RequestMapping(value = "getSupplierByProId", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String, Object>> getSupplierByProId(HttpServletRequest request) {
		String supId = request.getParameter("supId");
		String reqId = request.getParameter("reqId");
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("supId", supId);
		map.put("reqId", reqId);
		List<Map<String, Object>> list = purchaseService
		.getSupplierByProId(map);
		return list;
	}
 
	// 根据商品名称分页查询出匹配的供货商信息
	@RequestMapping(value = "supplierList", method = RequestMethod.POST)
	public @ResponseBody
	String supplierList(HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		String proId = request.getParameter("proId");
		String str = "";
		String sql = "SELECT supp.sup_id , sup_name,sprod.sp_other_name,sup_phone,sup_mob,sp_price,sp_upd_date,sp_has_tax FROM  crm_sup_prod sprod LEFT JOIN crm_supplier supp ON supp.sup_id=sprod.sp_sup_id  WHERE sprod.sp_wpr_id ='"
			+ proId + "'";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(purchaseService.yhRequestDbConn(dbConn, sql,
					request.getParameterMap()).getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	// 根据商品名称分页查询出匹配的供货商信息
	@RequestMapping(value = "querySuppPro", method = RequestMethod.POST)
	public @ResponseBody
	String querySuppPro(HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		String supId = request.getParameter("supId");
		String reqId = request.getParameter("reqId");
		String str = "";
		String sql = "SELECT rs.pro_id,pro.pro_code AS proCode,pro.pro_name AS"
			+ "proName,pro.pro_type AS proType,pro.pro_price AS"
			+ "proPrice,un.unit_name,ps.name AS psName,pt.name AS ptName FROM erp_purchase_request pr LEFT JOIN"
			+ "erp_request_supplier rs ON rs.request_id=pr.id LEFT JOIN"
			+ "erp_product pro ON pro.id=rs.pro_id LEFT JOIN erp_product_unit"
			+ "un ON un.unit_id=pro.unit_id LEFT JOIN crm_supplier supplier ON"
			+ "supplier.sup_id=rs.sup_id 	LEFT JOIN erp_product_style ps ON ps.id=pro.ps_id LEFT JOIN erp_product_type pt ON pt.id=pro.pt_id WHERE supplier.sup_id='"
			+ supId + "' AND" + "rs.request_id='" + reqId + "'";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
			.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(purchaseService.yhRequestDbConn(dbConn,sql,
					request.getParameterMap()).getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	// 查询出采购人员是当前登录系统人员的采购信息
	@RequestMapping(value = "getPurchaseListByPerson", method = RequestMethod.POST)
	public @ResponseBody
	String getPurchaseListByPerson(HttpServletRequest request,
			HttpServletResponse response) {
		Connection dbConn = null;
		String status=request.getParameter("status");
		String beginTime=request.getParameter("beginTime");
		String endTime=request.getParameter("endTime");
		 YHPerson loginUser = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
		 String seqUserId = String.valueOf(loginUser.getSeqId());
		 String sql="";
			if("0".equals(status)){
				sql= "SELECT purOut.id AS purOutId, pc.id as id, pc.code,pc.title,pc.person,pc.sign_date,pc.status,pc.remark FROM erp_purchase pc LEFT JOIN erp_purchase_product_out purOut ON purOut.purchase_id=pc.id where pc.person_id="+ seqUserId +" and (pc.status='"+StaticData.NEW_CREATE+"' or pc.status ='"+StaticData.RUNNING+"')";
			}else if("all".equals(status)){
				sql= "SELECT purOut.id AS purOutId, pc.id as id, pc.code,pc.title,pc.person,pc.sign_date,pc.status,pc.remark FROM erp_purchase pc LEFT JOIN erp_purchase_product_out purOut ON purOut.purchase_id=pc.id where 1=1 and pc.person_id="+ seqUserId +"";
			}else{
				sql= "SELECT purOut.id AS purOutId, pc.id as id, pc.code,pc.title,pc.person,pc.sign_date,pc.status,pc.remark FROM erp_purchase pc LEFT JOIN erp_purchase_product_out purOut ON purOut.purchase_id=pc.id  where pc.status="+status+" and pc.person_id="+ seqUserId +"";
					
			}
			if(!"".equals(beginTime)){
				sql+= " and date_format(sign_date,'%Y-%m-%d') >= '"+beginTime+"'";
			}
				if(!"".equals(endTime)){
				sql+= " and date_format(sign_date,'%Y-%m-%d') <= '"+endTime+"'";
			}
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(purchaseService.yhRequestDbConn(dbConn,sql,
					request.getParameterMap()).getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	// 查询出所有的采购信息
	@RequestMapping(value = "purchaseList", method = RequestMethod.POST)
	public @ResponseBody
	String getPurchaseList(HttpServletRequest request,
			HttpServletResponse response) {
		Connection dbConn = null;
		String status=request.getParameter("status");
		String beginTime=request.getParameter("beginTime");
		String endTime=request.getParameter("endTime");
		String str = "";
		String sql="";
		if("0".equals(status)){
			sql= "SELECT purOut.id AS purOutId, pc.id as id, pc.code,pc.title,pc.person,pc.sign_date,pc.status,pc.remark FROM erp_purchase pc LEFT JOIN erp_purchase_product_out purOut ON purOut.purchase_id=pc.id where status='"+StaticData.NEW_CREATE+"' or status ='"+StaticData.RUNNING+"'";
		}else if("all".equals(status)){
			sql= "SELECT purOut.id AS purOutId, pc.id as id, pc.code,pc.title,pc.person,pc.sign_date,pc.status,pc.remark FROM erp_purchase pc LEFT JOIN erp_purchase_product_out purOut ON purOut.purchase_id=pc.id where 1=1";
		}else{
			sql= "SELECT purOut.id AS purOutId, pc.id as id, pc.code,pc.title,pc.person,pc.sign_date,pc.status,pc.remark FROM erp_purchase pc LEFT JOIN erp_purchase_product_out purOut ON purOut.purchase_id=pc.id  where status="+status;
			
		}
		if(!"".equals(beginTime)){
			sql+= " and date_format(sign_date,'%Y-%m-%d') >= '"+beginTime+"'";
		}
		if(!"".equals(endTime)){
			sql+= " and date_format(sign_date,'%Y-%m-%d') <= '"+endTime+"'";
		}
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
			.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(purchaseService.yhRequestDbConn(dbConn,sql,
					request.getParameterMap()).getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	// 查询出所有的申请单信息
	@RequestMapping(value = "requestList/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody
	String getRequestList(HttpServletRequest request,
			HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		String sql = "SELECT req.id AS reqId,req.code AS reqCode,req.name AS reqName,req.person AS reqPerson,req.date AS reqDate,req.status AS reqStatus,req.remark AS reqRemark FROM erp_purchase_request req";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(purchaseService.yhRequestDbConn(dbConn,sql,
					request.getParameterMap()).getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	// 查询出所有的供货商信息
	@RequestMapping(value = "suppList", method = RequestMethod.POST)
	public @ResponseBody
	String getSuppList(HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		String reqId=request.getParameter("reqId");
		String sql = "";
		if ("0".equals(reqId)||"null".equals(reqId)) {
			sql = "SELECT rsup.sup_id AS id ,rsup.sup_code,rsup.sup_contact_man,rsup.sup_name,rsup.sup_phone,rsup.sup_mob,rsup.sup_fex,rsup.sup_email,rsup.sup_net,rsup.sup_qq,rsup.sup_add,rsup.sup_zip_code,rsup.sup_prod,rsup.sup_bank,rsup.sup_bank_name,rsup.sup_bank_code,rsup.sup_remark FROM  crm_supplier  rsup  ";
		} else {
			sql = "SELECT csup.sup_id AS supId, rsup.sup_id AS id ,rsup.sup_code,rsup.sup_contact_man,rsup.sup_name,rsup.sup_phone,rsup.sup_mob,rsup.sup_fex,rsup.sup_email,rsup.sup_net,rsup.sup_qq,rsup.sup_add,rsup.sup_zip_code,rsup.sup_prod,rsup.sup_bank,rsup.sup_bank_name,rsup.sup_bank_code,rsup.sup_remark FROM  erp_request_supplier csup LEFT JOIN crm_supplier  rsup ON rsup.sup_id=csup.sup_id where csup.request_id='"
					+ reqId + "' GROUP BY supId ";
		}
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(purchaseService.yhRequestDbConn(dbConn,sql,
					request.getParameterMap()).getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	// 查询出所有的供货商信息
	@RequestMapping(value = "suppAllList/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody
	String getSuppAllList(HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		String sql = "";
		sql = "SELECT rsup.sup_id AS id ,rsup.sup_code,rsup.sup_contact_man,rsup.sup_name,rsup.sup_phone,rsup.sup_mob,rsup.sup_fex,rsup.sup_email,rsup.sup_net,rsup.sup_qq,rsup.sup_add,rsup.sup_zip_code,rsup.sup_prod,rsup.sup_bank,rsup.sup_bank_name,rsup.sup_bank_code,rsup.sup_remark FROM  crm_supplier  rsup  ";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
			.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(purchaseService.yhRequestDbConn(dbConn,sql,
					request.getParameterMap()).getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	// 添加采购申请单
	@RequestMapping(value = "addRequest", method = RequestMethod.POST)
	public @ResponseBody
	String addRequest(HttpServletRequest request, HttpServletResponse response) {
		String str = "";

		String reqId = UUID.randomUUID().toString();// 申请单Id
		String reqName = request.getParameter("reqName");// 申请单名称
		String reqCode = request.getParameter("reqCode");// 申请单编号
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String reqDate =sdf.format(new Date()).toString();//申请日期
		String reqRemark = request.getParameter("reqRemark");// 采购申请单备注
		String supArryChild = request.getParameter("str");// 供货商名称
		YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
		
		//添加erp_purchase_request -- start
		Map<String,Object>  reqMap = new HashMap<String,Object>();
		reqMap.put("id", reqId);
		reqMap.put("code", reqCode);
		reqMap.put("name", reqName);
		reqMap.put("status", StaticData.NEW_CREATE);
		reqMap.put("date", reqDate);
		reqMap.put("remark", reqRemark);
		reqMap.put("person", user.getUserName());
		reqMap.put("person_id", user.getSeqId());
		//添加erp_purchase_request -- end
		
		//添加erp_request_supplier --start
		List<Map<String,Object>> supList = new ArrayList<Map<String,Object>>();
		String []supArry=supArryChild.split(";");
		String []supArrys=null;
		for (int i = 0; i < supArry.length; i++) {
			supArrys=supArry[i].split(",");
				String reqSupId = UUID.randomUUID().toString();// 供货商记录Id
				Map<String,Object>  supMap = new HashMap<String,Object>();
				supMap.put("id", reqSupId);
				supMap.put("pro_id", supArrys[0]);
				supMap.put("sup_id", supArrys[1]);
				supMap.put("request_id", reqId);
				supMap.put("status", StaticData.NEW_CREATE);
				supMap.put("remark", "");
				supList.add(supMap);
		}
		//添加erp_request_supplier --end
		try {
			str = purchaseService.addRequest(reqMap, supList);//添加erp_purchase_request和erp_request_supplier
		} catch (Exception e) {
			str = "1";
			e.printStackTrace();
		}
		return str;
	}

	// 编辑采购申请单
	@RequestMapping(value = "updateRequest", method = RequestMethod.POST)
	public @ResponseBody
	String updateRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String str = "";
		String reqId = request.getParameter("reqId");// 申请单Id
		String reqName = request.getParameter("reqName");// 申请单名称
		String reqCode = request.getParameter("reqCode");// 申请单编号
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String reqDate =sdf.format(new Date()).toString();//申请日期
		String reqRemark = request.getParameter("reqRemark");// 采购申请单备注
		String supArryChild = request.getParameter("str");// 供货商名称
		YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
		
		//添加erp_purchase_request -- start
		Map<String,Object>  reqMap = new HashMap<String,Object>();
		reqMap.put("id", reqId);
		reqMap.put("code", reqCode);
		reqMap.put("name", reqName);
		reqMap.put("status", StaticData.NEW_CREATE);
		reqMap.put("date", reqDate);
		reqMap.put("remark", reqRemark);
		reqMap.put("person", user.getUserName());
		reqMap.put("person_id", user.getSeqId());
		//添加erp_purchase_request -- end
		
		//添加erp_request_supplier --start
		List<Map<String,Object>> supList = new ArrayList<Map<String,Object>>();
		String []supArry=supArryChild.split(";");
		String []supArrys=null;
		for (int i = 0; i < supArry.length; i++) {
			supArrys=supArry[i].split(",");
				String reqSupId = UUID.randomUUID().toString();// 供货商记录Id
				Map<String,Object>  supMap = new HashMap<String,Object>();
				supMap.put("id", reqSupId);
				supMap.put("pro_id", supArrys[0]);
				supMap.put("sup_id", supArrys[1]);
				supMap.put("request_id", reqId);
				supMap.put("status", StaticData.NEW_CREATE);
				supMap.put("remark", "");
				supList.add(supMap);
		}
		//添加erp_request_supplier --end
		try {
			str = purchaseService.updateRequest(reqMap, supList);//操作删除erp_request_supplier，erp_purchase_request,重新添加申请单erp_purchase_request，erp_request_supplier
		} catch (Exception e) {
			str = "1";
			e.printStackTrace();
		}
		return str;
	}

	// 增加采购信息
	@RequestMapping(value = "addPurchase", method = RequestMethod.POST)
	public @ResponseBody
	String addPurchase(HttpServletRequest request, HttpServletResponse resposne) {
		String str = "";
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> listPro = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> listPpop = new ArrayList<Map<String,Object>>();
	
		// 采购单信息
		String purchaseId = UUID.randomUUID().toString();// 采购订单id
		String purCode = request.getParameter("purCode");// 采购订单编号
		String purTitle = request.getParameter("purTitle");// 采购订单标题
		String userDesc = request.getParameter("userDesc");// 采购员名称
		String userId = request.getParameter("userId");// 采购员id
		String purSignDate = request.getParameter("purSignDate");// 采购日期
		String purContractId = request.getParameter("purContractId");// 合同
		String purRemark = request.getParameter("purRemark");// 订单备注
		
		//添加采购订单信息erp_purchase--start
		map.put("purchaseId", purchaseId);
		map.put("purRemark", purRemark);
		map.put("purCode", purCode);
		map.put("purTitle", purTitle);
		map.put("purPerson", userDesc);
		map.put("purStatus", StaticData.NEW_CREATE);
		map.put("purSignDate", purSignDate);
		map.put("purContractId", purContractId);
		map.put("personId", userId);
		//添加采购订单信息erp_purchase--end
		// 供货商信息
		String supId = UUID.randomUUID().toString();// 缓存供货商id
		String sup_id = request.getParameter("supId");// 供货商名称
		String supName = request.getParameter("supName");// 供货商名称
		String supCode = request.getParameter("supCode");// 供货商编号
		String supContactName = request.getParameter("supContactMan");// 联系人
		String supPhone = request.getParameter("supPhone");// 电话
		String supMobile = request.getParameter("supMobile");// 手机
		String supFex = request.getParameter("supFex");// 传真
		String supEmail = request.getParameter("supEmail");// 电子邮件
		String supAddress = request.getParameter("supAddress");// 地址
		String supZipCode = request.getParameter("supZipCode");// 邮编
		String supWebsite = request.getParameter("supWebsite");// 网址
		String supQq = request.getParameter("supQq");// qq
		String supProducts = request.getParameter("supProducts");// 产品
		String supBank = request.getParameter("supBank");// 开户行
		String supBankName = request.getParameter("supBankName");// 开户行名称
		String supBankAccount = request.getParameter("supBankAccount");// 开户行账户
		String supRemark = request.getParameter("supRemark");// 开户行账户
		
		//添加采购单对应供货商缓存信息erp_purchase_supplier--start
		map.put("supId", supId);
		map.put("sup_id", sup_id);
		map.put("supName", supName);
		map.put("supCode", supCode);
		map.put("supContactName", supContactName);
		map.put("supPhone", supPhone);
		map.put("supMobile", supMobile);
		map.put("supFex", supFex);
		map.put("supEmail", supEmail);
		map.put("supAddress", supAddress);
		map.put("supZipCode", supZipCode);
		map.put("supWebsite", supWebsite);
		map.put("supQq", supQq);
		map.put("supProducts", supProducts);
		map.put("supBank", supBank);
		map.put("supBankName", supBankName);
		map.put("supBankAccount", supBankAccount);
		map.put("supRemark", supRemark);
		//添加采购单对应供货商缓存信息erp_purchase_supplier--end（引用采购订单的purchaseId）
		// 采购货单信息
		String pppoId = UUID.randomUUID().toString();// 出货单id
		String ppoTitle = request.getParameter("opoTitle");// 采购货单标题
		String ppoCode = request.getParameter("opoCode");// 采购货单编号
		String ppoRemark = request.getParameter("opoRemark");// 采购货单备注
		String productIds = request.getParameter("productIds");// 采购商品Id
		String sendDate = request.getParameter("sendDate");// 商品采购日期
		String prices = request.getParameter("prices");// 商品单价
		String numbers = request.getParameter("numbers");// 商品数量
		String totals = request.getParameter("totals");// 商品总额
		String[] arrPro = productIds.split(",");
		//添加采购货单信息erp_purchase_product_out--start
		map.put("ppoTitle", ppoTitle);
		map.put("ppoCode", ppoCode);
		map.put("ppoRemark", ppoRemark);
		map.put("ppoStatus", StaticData.NEW_CREATE);
		map.put("pppoId", pppoId);
		//添加采购货单信息erp_purchase_product_out--end（引用采购订单的purchaseId）
		String[] arrDate = sendDate.split(",");
		String[] arrPrices = prices.split(",");
		String[] arrNumbers = numbers.split(",");
		String[] arrTotals = totals.split(",");
		//添加erp_ppo_pro 采购单产品关联表信息--start
		for (int i = 0; i < arrPro.length; i++) {
			String ppopId = UUID.randomUUID().toString();// 产品关联id
			Map<String,Object> pproMap = new HashMap<String,Object>();
			pproMap.put("ppopId", ppopId);
			pproMap.put("proUuId",arrPro[i]);
			pproMap.put("pppoId", pppoId);
			pproMap.put("ppopoNum", arrNumbers[i]);
			pproMap.put("ppopStatus", StaticData.NEW_CREATE);
			pproMap.put("ppopPrice", arrPrices[i]);
			pproMap.put("ppopTotal", arrTotals[i]);
			pproMap.put("newDate", arrDate[i]);
			pproMap.put("ppopRemark", ppoRemark);
			listPpop.add(pproMap);
		}
		//添加erp_ppo_pro 采购单产品关联表信息--end
	

		// 出款单
		String pppId = UUID.randomUUID().toString();// 出款单id
		String paidTitle = request.getParameter("paidTitle");// 出款单主题
		String paidCode = request.getParameter("paidCode");// 出款单主题
		String paidTotal = request.getParameter("paidTotal");// 出款单应付总额
		String alreadyTotal = request.getParameter("alreadyTotal");// 出款单已经付了
		String paidRemark = request.getParameter("paidRemark");// 出款单备注
		//添加采购单出款单信息erp_purchase_paid_plan--start(同时添加财务应付单信息)
		map.put("outStatus", StaticData.HAND_CREATE);
		map.put("outType", "采购支付");
		map.put("paidTitle", paidTitle);
		map.put("paidCode", paidCode);
		map.put("paidStatus", StaticData.NEW_CREATE);
		map.put("paidTotal", paidTotal);
		map.put("alreadyTotal", alreadyTotal);
		map.put("paidRemark", paidRemark);
		map.put("pppId", pppId);
		//添加采购单出款单信息erp_purchase_paid_plan--end

		try {
			str = purchaseService.addResult(map, listPro, listPpop);
		} catch (Exception e) {
			str = "1";
			e.printStackTrace();
		}
		return str;
	}

	// 编辑采购信息
	@RequestMapping(value = "updatePurchase", method = RequestMethod.POST)
	public @ResponseBody
	String updatePurchase(HttpServletRequest request,
			HttpServletResponse resposne) {
		String str = "";
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> listPro = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> listPpop = new ArrayList<Map<String,Object>>();
		String purId = request.getParameter("purId");// 采购订单id
		String purOutId = request.getParameter("purOutId");// 出货单id
		// 采购单信息
		String purCode = request.getParameter("purCode");// 采购订单编号
		String userDesc = request.getParameter("userDesc");// 采购员
		String userId = request.getParameter("userId");// 采购员Id
		String purTitle = request.getParameter("purTitle");// 采购订单标题
		String purSignDate = request.getParameter("purSignDate");// 采购日期
		String purContractId = request.getParameter("purContractId");// 合同
		String purRemark = request.getParameter("purRemark");// 订单备注
		// 供货商信息
		String sup_id = request.getParameter("supId");// 供货商名称
		String supName = request.getParameter("supName");// 供货商名称
		String supCode = request.getParameter("supCode");// 供货商编号
		String supContactName = request.getParameter("supContactMan");// 联系人
		String supPhone = request.getParameter("supPhone");// 电话
		String supMobile = request.getParameter("supMobile");// 手机
		String supFex = request.getParameter("supFex");// 传真
		String supEmail = request.getParameter("supEmail");// 电子邮件
		String supAddress = request.getParameter("supAddress");// 地址
		String supZipCode = request.getParameter("supZipCode");// 邮编
		String supWebsite = request.getParameter("supWebsite");// 网址
		String supQq = request.getParameter("supQq");// qq
		String supProducts = request.getParameter("supProducts");// 产品
		String supBank = request.getParameter("supBank");// 开户行
		String supBankName = request.getParameter("supBankName");// 开户行名称
		String supBankAccount = request.getParameter("supBankAccount");// 开户行账户
		String supRemark = request.getParameter("supRemark");// 开户行账户
		// 采购货单信息
		String ppoTitle = request.getParameter("opoTitle");// 采购货单标题
		String ppoCode = request.getParameter("opoCode");// 采购货单编号
		String ppoRemark = request.getParameter("opoRemark");// 采购货单备注
		// 更新采购订单信息erp_purchase--start
		map.put("purchaseId", purId);
		map.put("purRemark", purRemark);
		map.put("purCode", purCode);
		map.put("purTitle", purTitle);
		map.put("purStatus", StaticData.NEW_CREATE);
		map.put("outStatus", StaticData.HAND_CREATE);
		map.put("purPerson", userDesc);
		map.put("personId", userId);
		map.put("purSignDate", purSignDate);
		map.put("purContractId", purContractId);
		// 更新采购订单信息erp_purchase--end
		
		//添加采购产品中间表erp_ppo_pro--start
		String productIds = request.getParameter("productIds");// 采购商品Id
		String sendDate = request.getParameter("sendDate");// 商品采购日期
		String prices = request.getParameter("prices");// 商品单价
		String numbers = request.getParameter("numbers");// 商品数量
		String totals = request.getParameter("totals");// 商品总额
		String[] arrPro = productIds.split(",");
		String[] arrDate = sendDate.split(",");
		String[] arrPrices = prices.split(",");
		String[] arrNumbers = numbers.split(",");
		String[] arrTotals = totals.split(",");
		if (productIds == null || productIds.equals("")) {
		} else {
			for (int i = 0; i < arrPro.length; i++) {
				String ppopId = UUID.randomUUID().toString();// 产品关联
				Map<String,Object> map2 = new HashMap<String,Object>();
				map2.put("ppopId", ppopId);
				map2.put("pppoId", purOutId);
				map2.put("proUuId", arrPro[i]);
				map2.put("ppopoNum", arrNumbers[i]);
				map2.put("ppopStatus", StaticData.NEW_CREATE);
				map2.put("ppopPrice", arrPrices[i]);
				map2.put("ppopTotal", arrTotals[i]);
				map2.put("newDate", arrDate[i]);
				map2.put("ppopRemark", ppoRemark);
				listPpop.add(map2);
			}
		}
		//添加采购产品中间表erp_ppo_pro--end
		
		//更新供货商信息erp_purchase_supplier--start
		map.put("sup_id", sup_id);
		map.put("supName", supName);
		map.put("supCode", supCode);
		map.put("supContactName", supContactName);
		map.put("supPhone", supPhone);
		map.put("supMobile", supMobile);
		map.put("supFex", supFex);
		map.put("supEmail", supEmail);
		map.put("supAddress", supAddress);
		map.put("supZipCode", supZipCode);
		map.put("supWebsite", supWebsite);
		map.put("supQq", supQq);
		map.put("supProducts", supProducts);
		map.put("supBank", supBank);
		map.put("supBankName", supBankName);
		map.put("supBankAccount", supBankAccount);
		map.put("supRemark", supRemark);
		//更新供货商信息erp_purchase_supplier--end
		
		// 更新出款单erp_purchase_paid_plan和财务应付单erp_finance_out-start（引用采购单id）
		String paidTitle = request.getParameter("paidTitle");// 出款单主题
		String paidCode = request.getParameter("paidCode");// 出款单主题
		String paidTotal = request.getParameter("paidTotal");// 出款单应付总额
		String alreadyTotal = request.getParameter("alreadyTotal");// 出款单已经付了
		String paidRemark = request.getParameter("paidRemark");// 出款单备注
		map.put("paidTitle", paidTitle);
		map.put("paidCode", paidCode);
		map.put("paidStatus", StaticData.NEW_CREATE);
		map.put("paidTotal", paidTotal);
		map.put("alreadyTotal", alreadyTotal);
		map.put("paidRemark", paidRemark);
		// 更新出款单erp_purchase_paid_plan-end
		
		// 更新出货单erp_purchase_product_out--start（引用采购单id）
		map.put("ppoTitle", ppoTitle);
		map.put("ppoCode", ppoCode);
		map.put("ppoRemark", ppoRemark);
		// 更新出货单erp_purchase_product_out--end
		try {
			str = purchaseService.updateResult(map, purId, purOutId, listPro,
					listPpop);
		} catch (Exception e) {
			str = "1";
			e.printStackTrace();
		}
		return str;
	}

	// 删除采购订单相关信息
	@RequestMapping(value = "deletePurchase", method = RequestMethod.POST)
	public @ResponseBody
	String deletePurchase(HttpServletRequest request,
			HttpServletResponse resposne) {
		String str = "";
		String purchaseId = request.getParameter("purchaseId");
		String purOutId = request.getParameter("purOutId");
		try {
			str = purchaseService.deleteResult(purchaseId, purOutId);
		} catch (Exception e) {
			str = "1";
			e.printStackTrace();
		}
		return str;
	}

	// 删除采购订单相关信息
	@RequestMapping(value = "deletePResult", method = RequestMethod.POST)
	public @ResponseBody
	String deletePResult(HttpServletRequest request,
			HttpServletResponse resposne) {
		String str = "";
		String reqId = request.getParameter("reqId");
		try {
			str = purchaseService.deletePResult(reqId);
		} catch (Exception e) {
			str = "1";
			e.printStackTrace();
		}
		return str;
	}

	// 查看采购订单相关信息
	@RequestMapping(value = "getRequestById", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String,Object>> getRequestById(HttpServletRequest request, HttpServletResponse resposne) {
		String reqId = request.getParameter("reqId");
		List<Map<String,Object>>  list = purchaseService.getRequestById(reqId);
		return list;
	}
	@RequestMapping(value = "getRequestSupById", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String,Object>>  getRequestSupById(HttpServletRequest request, HttpServletResponse resposne) {
		String proId = request.getParameter("proId");
		String reqId = request.getParameter("reqId");
		Map<String,Object> map =new HashMap <String,Object>();
		map.put("proId", proId);
		map.put("reqId", reqId);
		List<Map<String,Object>> list = purchaseService.getRequestSupById(map);
		return list;
	}

	// 查看采购订单相关信息
	@RequestMapping(value = "getRequestByProId", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String,Object>> getRequestByProId(HttpServletRequest request,
			HttpServletResponse resposne) {
		String proName = request.getParameter("proName");
		String reqId = request.getParameter("reqId");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("proName", proName);
		map.put("reqId", reqId);
		List<Map<String,Object>>  list = purchaseService.getRequestByProId(map);
		return list;
	}
	@RequestMapping(value = "openAccount", method = RequestMethod.POST)
	public @ResponseBody String openAccount(HttpServletRequest request,HttpServletResponse resposne) {
		String str = "0";
		String reqId = request.getParameter("reqId");
		try {
			purchaseService.openAccount(reqId);
		} catch (Exception e) {
			e.printStackTrace();
			str = "1";
		}
		return str;
	}
	@RequestMapping(value = "openAccountPurchase", method = RequestMethod.POST)
	public @ResponseBody String openAccountPurchase(HttpServletRequest request,HttpServletResponse resposne) {
		String str = "0";
		String purId = request.getParameter("purId");
		try {
			purchaseService.openAccountPurchase(purId);
		} catch (Exception e) {
			e.printStackTrace();
			str = "1";
		}
		return str;
	}
	
	@RequestMapping(value = "queryAllPurchaseRemind", method = RequestMethod.POST)
	public @ResponseBody String queryAllPurchaseRemind(HttpServletRequest request,HttpServletResponse resposne) throws Exception {
		    Connection dbConn = null;
		    Statement stmt = null;
		    ResultSet rs = null;
		    StringBuffer sb = new StringBuffer("[");
		    int count = 0;
		    int len = 0;
		    String queryNotifySql = "";
		    YHPerson loginUser = null;
		    loginUser = (YHPerson)request.getSession().getAttribute("LOGIN_USER");

		  
		    try {
		      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
		          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		      dbConn = requestDbConn.getSysDbConn();
		      int seqUserId = loginUser.getSeqId();
		      
		      queryNotifySql= "SELECT purOut.id AS purOutId, " +
		      		"pc.id as id, " +
		      		"pc.code,pc.title,pc.person," +
		      		"pc.sign_date,pc.status," +
		      		"pc.remark FROM erp_purchase pc " +
		      		"LEFT JOIN erp_purchase_product_out " +
		      		"purOut ON purOut.purchase_id=pc.id LEFT JOIN " +
		      		"erp_ppo_pro pur_pro ON pur_pro.ppo_id=purOut.id " +
		      		"WHERE pc.person_id="+ seqUserId +" ORDER BY pur_pro.date desc";

		      stmt = dbConn.createStatement();
		      rs = stmt.executeQuery(queryNotifySql);
		      int showMAXRecode = 10; //桌面显示的最大记录条数
		      while(rs.next() && ((++len) < showMAXRecode)) {
		          count ++;
		          sb.append("{");
		          sb.append("seqId:" + rs.getString("id"));
		          sb.append(",code:" + rs.getString("code"));
		          sb.append(",title:\"" + YHUtility.encodeSpecial(rs.getString("title")) + "\"");
		          sb.append(",person:" + rs.getString("person"));
		          sb.append(",sign_date:\"" + rs.getString("sign_date") + "\"");
		          sb.append(",status:\"" + rs.getString("status") + "\"");
		          sb.append("},");
		      }
		      if(count>0) {
		        sb.deleteCharAt(sb.length() - 1); 
		        }
		      sb.append("]");
		      //YHOut.println(sb.toString());
		      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
		      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
		    } catch (Exception ex) {
		      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
		      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		      request.setAttribute(YHActionKeys.RET_MSRG, message);
		      throw ex;
		    }
		    return "/core/inc/rtjson.jsp";
	}
	
	
}

package oa.spring.control.rest;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oa.spring.service.FinanceService;
import oa.spring.util.StaticData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHBeanKeys;

@Controller
@RequestMapping("/finance")
public class FinanceController {
	@Autowired
	private FinanceService financeService;
	
	private static final Logger logger = Logger.getLogger(FinanceController.class);
	
	@RequestMapping(value = "moneyInManage/{timestamp}", method = RequestMethod.POST)
    public @ResponseBody String moneyInManage(HttpServletRequest request,HttpServletResponse response) {
		 Connection dbConn = null;
    	 String str = "";
        try {
        	YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        	dbConn = requestDbConn.getSysDbConn();
        	String sql="SELECT pp.id,pp.order_id,o.order_code,pp.paid_code,pp.paid_title,pp.paid_status FROM erp_paid_plan pp,erp_sale_order o WHERE pp.order_id=o.id AND (pp.paid_status = '"+StaticData.RUNNING+"' or pp.paid_status = '"+StaticData.OVER+"')";
        	logger.debug(sql);
        	str = new String(financeService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return str;
    }
	//出款单
	@RequestMapping(value = "moneyOutManage", method = RequestMethod.POST)
    public @ResponseBody String moneyOutManage(HttpServletRequest request,HttpServletResponse response) {
		 Connection dbConn = null;
    	 String str = "";
    	 String status=request.getParameter("status");
        try {
        	YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        	dbConn = requestDbConn.getSysDbConn();
        	String sql="";
        	if("0".equals(status)){	
        	sql="SELECT fout.id AS fId,fout.type_id AS fTypeId , fout.type AS fType,fout.code AS fCode,fout.payee AS fPayee ,fout.status AS fStatus FROM erp_finance_out fout LEFT JOIN erp_purchase pur ON pur.id=fout.type_id LEFT JOIN erp_purchase_paid_plan ppp ON ppp.purchase_id=fout.type_id WHERE fout.status='"+StaticData.NEW_CREATE+"'";
        	}else if("all".equals(status)){
        		sql="SELECT fout.id AS fId,fout.type_id AS fTypeId , fout.type AS fType,fout.code AS fCode,fout.payee AS fPayee ,fout.status AS fStatus FROM erp_finance_out fout LEFT JOIN erp_purchase pur ON pur.id=fout.type_id LEFT JOIN erp_purchase_paid_plan ppp ON ppp.purchase_id=fout.type_id WHERE fout.status<>'"+StaticData.HAND_CREATE+"'";
        		
        		
        	}else{
        		sql="SELECT fout.id AS fId,fout.type_id AS fTypeId , fout.type AS fType,fout.code AS fCode,fout.payee AS fPayee ,fout.status AS fStatus FROM erp_finance_out fout LEFT JOIN erp_purchase pur ON pur.id=fout.type_id LEFT JOIN erp_purchase_paid_plan ppp ON ppp.purchase_id=fout.type_id WHERE fout.status='"+status+"'";
        		
        	}
        	logger.debug(sql);
        	str = new String(financeService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return str;
    }
	//现金银行
	@RequestMapping(value = "bank/{timestamp}", method = RequestMethod.POST)
    public @ResponseBody String bank(HttpServletRequest request,HttpServletResponse response) {
		 Connection dbConn = null;
    	 String str = "";
        try {
        	YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        	dbConn = requestDbConn.getSysDbConn();
        	String sql="SELECT id,NAME,open_name,account,money,remark FROM erp_company_bank";
        	logger.debug(sql);
        	str = new String(financeService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return str;
    }
	//添加现金银行
	@RequestMapping(value = "addBank", method = RequestMethod.POST)
    public @ResponseBody String  addBank(HttpServletRequest request,HttpServletResponse response) {
		String str="0";
		String bankName=request.getParameter("bankName");//公司银行名称 
		String bankAccount=request.getParameter("bankAccount");//公司银行卡号 
		String bankMoney=request.getParameter("bankMoney");//公司银行资产 
		String bankRemark=request.getParameter("bankRemark");//公司银行备注
		String openName=request.getParameter("openName");//开户名称
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("bankName", bankName);
		map.put("bankAccount", bankAccount);
		map.put("bankMoney", bankMoney);
		map.put("bankRemark", bankRemark);
		map.put("openName", openName);
		try{
			financeService.addBank(map);
		}catch(Exception e){
			str="1";
			e.printStackTrace();
		} 
		return str;
		
	}
	//更新现金银行
	@RequestMapping(value = "updateBank", method = RequestMethod.POST)
    public @ResponseBody String  updateBank(HttpServletRequest request,HttpServletResponse response) {
		String str="0";
		String bankName=request.getParameter("bankName");//公司银行名称 
		String bankId=request.getParameter("bankId");//公司银行名称 
		String bankAccount=request.getParameter("bankAccount");//公司银行卡号 
		String bankMoney=request.getParameter("bankMoney");//公司银行资产 
		String bankRemark=request.getParameter("bankRemark");//公司银行备注
		String openName=request.getParameter("openName");//开户名称
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("bankName", bankName);
		map.put("bankAccount", bankAccount);
		map.put("bankMoney", bankMoney);
		map.put("bankRemark", bankRemark);
		map.put("openName", openName);
		map.put("bankId", bankId);
		try{
			financeService.updateBank(map);
		}catch(Exception e){
			str="1";
			e.printStackTrace();
		} 
		return str;
		
	}
	//删除现金银行
	@RequestMapping(value = " deleteBank", method = RequestMethod.POST)
    public @ResponseBody String  deleteBank(HttpServletRequest request,HttpServletResponse response) {
		String str="0";
		String bankId=request.getParameter("bankId");
		try{
			financeService.deleteBank(bankId);
		}catch(Exception e){
			str="1";
			e.printStackTrace();
		}
		return str;
	}
	//现金银行明细
	@RequestMapping(value = " detailBank", method = RequestMethod.POST)
    public @ResponseBody List<Map<String,Object>>  detailBank(HttpServletRequest request,HttpServletResponse response) {
		String bankId=request.getParameter("bankId");
		List<Map<String,Object>> list=financeService.getBankById(bankId);
		return list;
	}
	@RequestMapping(value = "showDetial", method = RequestMethod.POST)
	public @ResponseBody String showDetial(HttpServletRequest request,HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String order_id = request.getParameter("order_id");
			String sql="SELECT o.id AS order_id,o.order_code,pp.id AS pp_id,pp.paid_code,fi.id AS fi_id,fi.code,fi.status FROM erp_sale_order o,erp_paid_plan pp,erp_finance_in fi WHERE pp.order_id = fi.type_id AND pp.order_id = o.id AND fi.type_id='"+order_id+"'";
			logger.debug(sql);
			str = new String(financeService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	@RequestMapping(value = "ppdShowDetial", method = RequestMethod.POST)
	public @ResponseBody String ppdShowDetial(HttpServletRequest request,HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String sql="SELECT o.id AS order_id,pp.id AS pp_id,fi.id AS ppd_id,o.order_code,pp.paid_code,fi.code AS ppd_code,fi.status AS ppd_status FROM erp_finance_in fi,erp_paid_plan pp,erp_sale_order o WHERE fi.type_id = o.id AND o.id = pp.order_id";
			logger.debug(sql);
			str = new String(financeService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	@RequestMapping(value = "fiShowDetial", method = RequestMethod.POST)
	public @ResponseBody String fiShowDetial(HttpServletRequest request,HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		String status=request.getParameter("status");
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String sql="";
				if ("0".equals(status)) {
					sql="SELECT id,TYPE,customer,CODE,STATUS,type_id FROM erp_finance_in WHERE  STATUS='"+StaticData.NEW_CREATE+"'";
					
				} else if ("all".equals(status)) {
					sql="SELECT id,TYPE,customer,CODE,STATUS,type_id FROM erp_finance_in WHERE  1=1";
				} else {
					sql="SELECT id,TYPE,customer,CODE,STATUS,type_id FROM erp_finance_in WHERE  STATUS='"+status+"'";


				}
				logger.debug(sql);
			str = new String(financeService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	@RequestMapping(value = "fidShowDetial", method = RequestMethod.POST)
	public @ResponseBody String fidShowDetial(HttpServletRequest request,HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		String status=request.getParameter("status");
		String beginTime=request.getParameter("beginTime");
		String endTime=request.getParameter("endTime");
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String fi_id = request.getParameter("fi_id");
			String sql = "";
			if ("-2".equals(status)) {
				sql="SELECT fid.id,fi.code as fi_code,fid.code,fid.status FROM erp_finance_in fi,erp_finance_in_detial fid WHERE fi.id = fid.fi_id  and fi.id='"+fi_id+"' and fid.status='"+StaticData.NEW_CREATE+"' or fid.status='"+StaticData.RUNNING+"'";
			} else if ("-3".equals(status)) {
				sql="SELECT fid.id,fi.code as fi_code,fid.code,fid.status FROM erp_finance_in fi,erp_finance_in_detial fid WHERE fi.id = fid.fi_id  and fi.id='"+fi_id+"' ";
			} else {
				sql="SELECT fid.id,fi.code as fi_code,fid.code,fid.status FROM erp_finance_in fi,erp_finance_in_detial fid WHERE fi.id = fid.fi_id  and fi.id='"+fi_id+"' and fid.status='"+status+"'";


			}
			if (!"".equals(beginTime)) {
				sql += " and date_format(paid_date,'%Y-%m-%d') >= '" + beginTime
						+ "'";
			}
			if (!"".equals(endTime)) {
				sql += " and date_format(paid_date,'%Y-%m-%d') <= '" + endTime
						+ "'";
			}
			logger.debug(sql);
			str = new String(financeService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	@RequestMapping(value = "outShowDetail", method = RequestMethod.POST)
	public @ResponseBody String outShowDetail(HttpServletRequest request,HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		String status=request.getParameter("status");
		String beginTime=request.getParameter("beginTime");
		String endTime=request.getParameter("endTime");
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String fId = request.getParameter("fId");
			String sql = "";
			if("0".equals(fId)){
			if ("0".equals(status)) {
				sql="SELECT  fod.id AS fodId,fod.fo_id,fod.code AS fodCode,fout.code AS foutCode,fod.status AS fodStatus  FROM erp_finance_out_detial fod LEFT JOIN erp_finance_out fout ON fout.id=fod.fo_id where fod.status ='"+StaticData.NEW_CREATE+"'";
			} else if ("all".equals(status)) {
				sql="SELECT  fod.id AS fodId,fod.fo_id,fod.code AS fodCode,fout.code AS foutCode,fod.status AS fodStatus  FROM erp_finance_out_detial fod LEFT JOIN erp_finance_out fout ON fout.id=fod.fo_id where 1=1";
			} else {
				sql="SELECT  fod.id AS fodId,fod.fo_id,fod.code AS fodCode,fout.code AS foutCode,fod.status AS fodStatus  FROM erp_finance_out_detial fod LEFT JOIN erp_finance_out fout ON fout.id=fod.fo_id where fod.status ='"+status+"'";
			}
			if (!"".equals(beginTime)) {
				sql += " and date_format(paid_date,'%Y-%m-%d') >= '" + beginTime
						+ "'";
			}
			if (!"".equals(endTime)) {
				sql += " and date_format(paid_date,'%Y-%m-%d') <= '" + endTime
						+ "'";
			}
			}else{
				if ("-2".equals(status)) {
					sql="SELECT  fod.id AS fodId,fod.fo_id,fod.code AS fodCode,fout.code AS foutCode,fod.status AS fodStatus  FROM erp_finance_out_detial fod LEFT JOIN erp_finance_out fout ON fout.id=fod.fo_id where (fod.status ='"+StaticData.NEW_CREATE+"' or fod.status ='"+StaticData.RUNNING+"') and fod.fo_id='"+fId+"'";
				} else if ("-3".equals(status)) {
					sql="SELECT  fod.id AS fodId,fod.fo_id,fod.code AS fodCode,fout.code AS foutCode,fod.status AS fodStatus  FROM erp_finance_out_detial fod LEFT JOIN erp_finance_out fout ON fout.id=fod.fo_id where  fod.fo_id='"+fId+"'";
				} else {
					sql="SELECT  fod.id AS fodId,fod.fo_id,fod.code AS fodCode,fout.code AS foutCode,fod.status AS fodStatus  FROM erp_finance_out_detial fod LEFT JOIN erp_finance_out fout ON fout.id=fod.fo_id where fod.status ='"+status+"'  and fod.fo_id='"+fId+"'";
				}
				if (!"".equals(beginTime)) {
					sql += " and date_format(paid_date,'%Y-%m-%d') >= '" + beginTime
							+ "'";
				}
				if (!"".equals(endTime)) {
					sql += " and date_format(paid_date,'%Y-%m-%d') <= '" + endTime
							+ "'";
				}
			}
			logger.debug(sql);
			str = new String(financeService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	@RequestMapping(value = "fidManage", method = RequestMethod.POST)
	public @ResponseBody String fidManage(HttpServletRequest request,HttpServletResponse response) {
		Connection dbConn = null;
		String status=request.getParameter("status");
		String beginTime=request.getParameter("beginTime");
		String endTime=request.getParameter("endTime");
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String sql = "";
			if ("0".equals(status)) {
				sql="SELECT fid.id,fid.fi_id,fi.code as fi_code,fid.code,fid.status FROM erp_finance_in fi,erp_finance_in_detial fid WHERE fi.id = fid.fi_id   and fid.status='"+StaticData.NEW_CREATE+"'";
			} else if ("all".equals(status)) {
				sql="SELECT fid.id,fid.fi_id,fi.code as fi_code,fid.code,fid.status FROM erp_finance_in fi,erp_finance_in_detial fid WHERE fi.id = fid.fi_id ";
			} else {
				sql="SELECT fid.id,fid.fi_id,fi.code as fi_code,fid.code,fid.status FROM erp_finance_in fi,erp_finance_in_detial fid WHERE fi.id = fid.fi_id   and fid.status='"+status+"'";


			}
			if (!"".equals(beginTime)) {
				sql += " and date_format(paid_date,'%Y-%m-%d') >= '" + beginTime
						+ "'";
			}
			if (!"".equals(endTime)) {
				sql += " and date_format(paid_date,'%Y-%m-%d') <= '" + endTime
						+ "'";
			}
			logger.debug(sql);
			str = new String(financeService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	@RequestMapping(value = "bankRecord", method = RequestMethod.POST)
	public @ResponseBody String bankRecord(HttpServletRequest request,HttpServletResponse response) {
		Connection dbConn = null;
		String status=request.getParameter("status");
		String beginTime=request.getParameter("beginTime");
		String endTime=request.getParameter("endTime");
		String name=request.getParameter("name");
		String bankId=request.getParameter("bankId");
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String sql = "";
			if ("in".equals(status)) {
				sql="SELECT fid.code,bank.name,bank.account,fi.customer,fid.money,fid.paid_date,fid.person FROM erp_finance_in fi,erp_finance_in_detial fid,erp_company_bank bank WHERE fi.id = fid.fi_id  AND bank.id=fid.bank_id and fid.status='"+StaticData.OVER+"' ";
				if(!"".equals(name)){
					sql+=" and fi.customer like '%"+name+"%'";
					
				}
				if(!"".equals(bankId)){
					sql+=" and fid.bank_id = '"+bankId+"'";
					
				}
			} else if ("out".equals(status)) {
				sql="SELECT  fot.code,bank.name,bank.account,fout.payee,fot.money,fot.paid_date,fot.person FROM erp_finance_out_detial  fot LEFT JOIN erp_finance_out fout   ON fot.fo_id=fout.id LEFT JOIN erp_company_bank bank ON bank.id=fot.bank_id WHERE fot.status='"+StaticData.OVER+"' ";
				if(!"".equals(name)){
					sql+=" and and payee like'%"+name+"%'";
					
				}
				if(!"".equals(bankId)){
					sql+=" and and fot.bank_id ='"+bankId+"'";
					
				}
			} 
			if (!"".equals(beginTime)) {
				sql += " and date_format(paid_date,'%Y-%m-%d') >= '" + beginTime
				+ "'";
			}
			if (!"".equals(endTime)) {
				sql += " and date_format(paid_date,'%Y-%m-%d') <= '" + endTime
				+ "'";
			}
			logger.debug(sql);
			str = new String(financeService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	@RequestMapping(value = "getPPDMsg", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getPPDMsg(HttpServletRequest request) {
		String fi_id = request.getParameter("fi_id");
		Map<String,Object> map = financeService.getPPDMsg(fi_id);
		return map;
	}
	@RequestMapping(value = "getFIMsg", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getFIMsg(HttpServletRequest request) {
		String fi_id = request.getParameter("fi_id");
		Map<String,Object> map = financeService.getFIMsg(fi_id);
		return map;
	}
	@RequestMapping(value = "getBankMsg", method = RequestMethod.POST)
	public @ResponseBody List<Map<String,Object>> getBankMsg(HttpServletRequest request) {
		List<Map<String,Object>> list = financeService.getBankMsg();
		return list;
	}
	@RequestMapping(value = "getFIDMsg", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getFIDMsg(HttpServletRequest request) {
		String fid_id = request.getParameter("fid_id");
		Map<String,Object> map = financeService.getFIDMsg(fid_id);
		return map;
	}
	
	@RequestMapping(value = "getOut", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getOut(HttpServletRequest request) {
		String fId = request.getParameter("fId");
		Map<String,Object> map = financeService.getOut(fId);
		return map;
	}
	@RequestMapping(value = "getOutDetail", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getOutDetail(HttpServletRequest request) {
		String fdId = request.getParameter("fdId");
		Map<String,Object> map = financeService.getOutDetail(fdId);
		return map;
	}
	@RequestMapping(value = "getCusMsg", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getCusMsg(HttpServletRequest request) {
		String order_id = request.getParameter("order_id");
		Map<String,Object> map = financeService.getCusMsg(order_id);
		return map;
	}
	@RequestMapping(value = "getFinanceInsMsg", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getFinanceInMsg(HttpServletRequest request) {
		String fi_id = request.getParameter("fi_id");
		Map<String,Object> map = financeService.getFinanceInMsg(fi_id);
		return map;
	}
	
	@RequestMapping(value = "addPPD", method = RequestMethod.POST)
    public @ResponseBody String addPPD(HttpServletRequest request) {
		 String rtStr = "0";
		 
		 String ppd_code = request.getParameter("ppd_code");
//		 String ppd_status = request.getParameter("ppd_status");
		 String ppd_status = StaticData.NEW_CREATE;
		 String order_id = request.getParameter("order_id");
//		 String pp_id = request.getParameter("pp_id");
//		 String cus_id = request.getParameter("cus_id");
		 String cus_name = request.getParameter("cus_name");
		 String bank_id = request.getParameter("bank_id");
		 double total = Double.parseDouble(request.getParameter("total"));
		 String paid_way = request.getParameter("paid_way");
		 String paid_date = request.getParameter("paid_date");
		 String person = request.getParameter("person");
		 String invoice_id = request.getParameter("invoice_id");
		 String remark = request.getParameter("remark");
		 Map<String,Object> map = new HashMap<String,Object>();
		 map.put("code", ppd_code);
		 map.put("status", ppd_status);
		 map.put("type", "销售");
		 map.put("type_id", order_id);
		 map.put("customer", cus_name);
		 map.put("total", total);
		 map.put("paid_way", paid_way);
		 map.put("paid_date", paid_date);
		 map.put("person", person);
		 map.put("invoice_id", invoice_id);
		 map.put("bank_id", bank_id);
		 map.put("remark", remark);
		try {
			financeService.addPPD(map);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
    }
	//支付单明细
	@RequestMapping(value = "addOutDetail", method = RequestMethod.POST)
    public @ResponseBody String addOutDetail(HttpServletRequest request) {
		 String rtStr = "0";
		 String outId=UUID.randomUUID().toString();
		 String outCode = request.getParameter("ppd_code");
		 String outStatus = StaticData.NEW_CREATE;
		 String outTypeId = request.getParameter("fCode");
		 String fId = request.getParameter("fId");
		 String outPayee = request.getParameter("supName");
		 String bank_id = request.getParameter("bank_id");
		 String bank = request.getParameter("bank");
		 String outBankName = request.getParameter("bankName");
			YHPerson user = (YHPerson) request.getSession().getAttribute(
			"LOGIN_USER");// 获得登陆用户
		 String outAccount = request.getParameter("account");
		 double outPaidTotal = Double.parseDouble(request.getParameter("total"));
		 double outAlreadyPaidTotal = Double.parseDouble(request.getParameter("outAlreadyTotal"));
		 double outTotal = Double.parseDouble(request.getParameter("outTotal"));
		 String paid_way = request.getParameter("paid_way");
		 String paid_date = request.getParameter("paid_date");
		 String invoice_id = request.getParameter("invoice_id");
		 String outRemark = request.getParameter("remark");
		 Map<String,Object> map = new HashMap<String,Object>();
		 map.put("outCode", outCode);
		 map.put("outId", outId);
		 map.put("fId", fId);
		 map.put("outStatus", outStatus);
		 map.put("outType", "采购");
		 map.put("outTypeId", outTypeId);
		 map.put("outPayee", outPayee);
		 map.put("outPaidTotal", outPaidTotal);
		 map.put("outAlreadyPaidTotal", outAlreadyPaidTotal);
		 map.put("outTotal", outTotal);
		 map.put("bank", bank);
		 map.put("outAccount", outAccount);
		 map.put("outBankName", outBankName);
		 map.put("paid_way", paid_way);
		 map.put("paid_date", paid_date);
		 map.put("person", user.getUserName());
		 map.put("personId", user.getSeqId());
		 map.put("invoice_id", invoice_id);
		 map.put("bank_id", bank_id);
		 map.put("outRemark", outRemark);
		try {
			financeService.addOutDetail(map);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
    }

	//支付单明细
	@RequestMapping(value = "updateOutDetail", method = RequestMethod.POST)
    public @ResponseBody String updateOutDetail(HttpServletRequest request) {
		 String rtStr = "0";
		 String fdId=request.getParameter("fdId");
		 String outCode = request.getParameter("ppd_code");
		 String outStatus = StaticData.NEW_CREATE;
		 String outTypeId = request.getParameter("fCode");
		 String outPayee = request.getParameter("supName");
		 String bank_id = request.getParameter("bank_id");
		 String bank = request.getParameter("bank");
		 String outBankName = request.getParameter("bankName");
			YHPerson user = (YHPerson) request.getSession().getAttribute(
			"LOGIN_USER");// 获得登陆用户
		 String outAccount = request.getParameter("account");
		 double outPaidTotal = Double.parseDouble(request.getParameter("total"));
		 double outAlreadyPaidTotal = Double.parseDouble(request.getParameter("outAlreadyTotal"));
		 double outTotal = Double.parseDouble(request.getParameter("outTotal"));
		 String paid_way = request.getParameter("paid_way");
		 String paid_date = request.getParameter("paid_date");
		 String invoice_id = request.getParameter("invoice_id");
		 String outRemark = request.getParameter("remark");
		 Map<String,Object> map = new HashMap<String,Object>();
		 map.put("outCode", outCode);
		 map.put("fdId", fdId);
		 map.put("outStatus", outStatus);
		 map.put("outType", "采购");
		 map.put("outTypeId", outTypeId);
		 map.put("outPayee", outPayee);
		 map.put("outPaidTotal", outPaidTotal);
		 map.put("outAlreadyPaidTotal", outAlreadyPaidTotal);
		 map.put("outTotal", outTotal);
		 map.put("bank", bank);
		 map.put("outAccount", outAccount);
		 map.put("outBankName", outBankName);
		 map.put("personId", user.getSeqId());
		 map.put("paid_way", paid_way);
		 map.put("paid_date", paid_date);
		 map.put("person", user.getUserName());
		 map.put("invoice_id", invoice_id);
		 map.put("bank_id", bank_id);
		 map.put("outRemark", outRemark);
		try {
			financeService.updateOutDetail(map);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
    }
	//应付单添加
	@RequestMapping(value = "addOut", method = RequestMethod.POST)
    public @ResponseBody String addOut(HttpServletRequest request) {
		 String rtStr = "0";
		 String outId=UUID.randomUUID().toString();
		 String outStatus = StaticData.NEW_CREATE;
		 String type = request.getParameter("type");
		 String outCode = request.getParameter("outCode");
		 String outPayee = request.getParameter("supName");
		 String bank = request.getParameter("bank");
		 String outBankName = request.getParameter("bankName");
		 String outAccount = request.getParameter("account");
		 double outPaidTotal = Double.parseDouble(request.getParameter("total"));
		 double outAlreadyPaidTotal = Double.parseDouble(request.getParameter("outAlreadyTotal"));
			YHPerson user = (YHPerson) request.getSession().getAttribute(
			"LOGIN_USER");// 获得登陆用户
		 String outRemark = request.getParameter("remark");
		 Map<String,Object> map = new HashMap<String,Object>();
		 map.put("outCode", outCode);
		 map.put("outId", outId);
		 map.put("outStatus", outStatus);
		 map.put("outType", type);
		 map.put("outPayee", outPayee);
		 map.put("outPaidTotal", outPaidTotal);
		 map.put("outAlreadyPaidTotal", outAlreadyPaidTotal);
		 map.put("bank", bank);
		 map.put("outAccount", outAccount);
		 map.put("outTypeId", "0");
		 map.put("outBankName", outBankName);
		 map.put("person", user.getUserName());
		 map.put("outRemark", outRemark);
		try {
			financeService.addOut(map);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
    }
	//应付单更新
	@RequestMapping(value = "updateOut", method = RequestMethod.POST)
	public @ResponseBody String updateOut(HttpServletRequest request) {
		String rtStr = "0";
		String fId=request.getParameter("fId");
//		String outStatus = StaticData.NEW_CREATE;
		String type = request.getParameter("type");
		String fCode = request.getParameter("fCode");
		String outPayee = request.getParameter("supName");
		String bank = request.getParameter("bank");
		String outBankName = request.getParameter("bankName");
		String outAccount = request.getParameter("account");
		double outPaidTotal = Double.parseDouble(request.getParameter("total"));
		double outAlreadyPaidTotal = Double.parseDouble(request.getParameter("outAlreadyTotal"));
		YHPerson user = (YHPerson) request.getSession().getAttribute(
		"LOGIN_USER");// 获得登陆用户
		String outRemark = request.getParameter("remark");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("fCode", fCode);
		map.put("fId", fId);
//		map.put("outStatus", outStatus);
		map.put("outType", type);
		map.put("outPayee", outPayee);
		map.put("outPaidTotal", outPaidTotal);
		map.put("outAlreadyPaidTotal", outAlreadyPaidTotal);
		map.put("bank", bank);
		map.put("outAccount", outAccount);
		map.put("outTypeId", "0");
		map.put("outBankName", outBankName);
		map.put("person", user.getUserName());
		map.put("outRemark", outRemark);
		try {
			financeService.updateOut(map);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "addFinanceIn", method = RequestMethod.POST)
	public @ResponseBody String addFinanceIn(HttpServletRequest request) {
		String rtStr = "0";
		
		String code = request.getParameter("code");
		String status = StaticData.NEW_CREATE;
		String type = request.getParameter("type");
		String customer = request.getParameter("customer");
		double total = Double.parseDouble(request.getParameter("total"));
		double already_total = Double.parseDouble(request.getParameter("already_total"));
		String remark = request.getParameter("remark");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("code", code);
		map.put("status", status);
		map.put("type", type);
		map.put("customer", customer);
		map.put("total", total);
		map.put("already_total", already_total);
		map.put("remark", remark);
		try {
			financeService.addFinanceIn(map);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "addFID", method = RequestMethod.POST)
	public @ResponseBody String addFID(HttpServletRequest request) {
		String rtStr = "0";
		
		String fi_id = request.getParameter("fi_id");
		String code = request.getParameter("code");
		String bank_id = request.getParameter("bank_id");
		String status = StaticData.NEW_CREATE;
		double money = Double.parseDouble(request.getParameter("money"));
		String paid_way = request.getParameter("paid_way");
		String paid_date = request.getParameter("paid_date");
		String person = request.getParameter("person");
		String invoice_id = request.getParameter("invoice_id");
		String remark = request.getParameter("remark");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("fi_id", fi_id);
		map.put("code", code);
		map.put("status", status);
		map.put("bank_id", bank_id);
		map.put("money", money);
		map.put("paid_way", paid_way);
		map.put("paid_date", paid_date);
		map.put("person", person);
		map.put("invoice_id", invoice_id);
		map.put("remark", remark);
		try {
			financeService.addFID(map);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "updateFinanceIn", method = RequestMethod.POST)
	public @ResponseBody String updateFinanceIn(HttpServletRequest request) {
		String rtStr = "0";
		
		String fi_id = request.getParameter("fi_id");
		String code = request.getParameter("code");
		String type = request.getParameter("type");
		String customer = request.getParameter("customer");
		double total = Double.parseDouble(request.getParameter("total"));
		double already_total = Double.parseDouble(request.getParameter("already_total"));
		String remark = request.getParameter("remark");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("fi_id", fi_id);
		map.put("code", code);
		map.put("type", type);
		map.put("customer", customer);
		map.put("total", total);
		map.put("already_total", already_total);
		map.put("remark", remark);
		try {
			financeService.updateFinanceIn(map);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "updatePPD", method = RequestMethod.POST)
	public @ResponseBody String updatePPD(HttpServletRequest request) {
		String rtStr = "0";
		
		String ppd_id = request.getParameter("ppd_id");
		String ppd_code = request.getParameter("ppd_code");
		double total = Double.parseDouble(request.getParameter("total"));
		String bank_id = request.getParameter("bank_id");
		String paid_way = request.getParameter("paid_way");
		String paid_date = request.getParameter("paid_date");
		String person = request.getParameter("person");
		String invoice_id = request.getParameter("invoice_id");
		String remark = request.getParameter("remark");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("ppd_id", ppd_id);
		map.put("ppd_code", ppd_code);
		map.put("total", total);
		map.put("bank_id", bank_id);
		map.put("paid_way", paid_way);
		map.put("paid_date", paid_date);
		map.put("person", person);
		map.put("invoice_id", invoice_id);
		map.put("remark", remark);
		try {
			financeService.updatePPD(map);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "updateFID", method = RequestMethod.POST)
	public @ResponseBody String updateFID(HttpServletRequest request) {
		String rtStr = "0";
		
		String fid_id = request.getParameter("fid_id");
		String code = request.getParameter("code");
		String bank_id = request.getParameter("bank_id");
	    String money = request.getParameter("money");
	    String paid_way = request.getParameter("paid_way");
	    String paid_date = request.getParameter("paid_date");
	    String person = request.getParameter("person");
	    String invoice_id = request.getParameter("invoice_id");
	    String remark = request.getParameter("remark");
	  
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("fid_id", fid_id);
		map.put("code", code);
		map.put("bank_id", bank_id);
		map.put("money", money);
		map.put("paid_way", paid_way);
		map.put("paid_date", paid_date);
		map.put("person", person);
		map.put("invoice_id", invoice_id);
		map.put("remark", remark);
		try {
			financeService.updateFID(map);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "deletePPD", method = RequestMethod.POST)
	public @ResponseBody String deletePPD(HttpServletRequest request) {
		String rtStr = "0";
		String ppd_id = request.getParameter("ppd_id");
		try {
			financeService.deletePPD(ppd_id);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "deleteFID", method = RequestMethod.POST)
	public @ResponseBody String deleteFID(HttpServletRequest request) {
		String rtStr = "0";
		String fid_id = request.getParameter("fid_id");
		try {
			financeService.deleteFID(fid_id);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "deleteOut", method = RequestMethod.POST)
	public @ResponseBody String deleteOut(HttpServletRequest request) {
		String rtStr = "0";
		String fId = request.getParameter("fId");
		try {
			financeService.deleteOut(fId);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "deleteOutDetail", method = RequestMethod.POST)
	public @ResponseBody String deleteOutDetail(HttpServletRequest request) {
		String rtStr = "0";
		String fdId = request.getParameter("fdId");
		try {
			financeService.deleteOutDetail(fdId);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "openAccount", method = RequestMethod.POST)
	public @ResponseBody String openAccount(HttpServletRequest request) {
		String rtStr = "0";
		String fi_id = request.getParameter("fi_id");
		try {
			financeService.openAccount(fi_id);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "openAccountFID", method = RequestMethod.POST)
	public @ResponseBody String openAccountFID(HttpServletRequest request) {
		String rtStr = "0";
		String fid_id = request.getParameter("fid_id");
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			Connection dbConn = requestDbConn.getSysDbConn();
			financeService.openAccountFID(fid_id,dbConn);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	
	@RequestMapping(value = "openAccountFinanceOut", method = RequestMethod.POST)
	public @ResponseBody String openAccountFinanceOut(HttpServletRequest request) {
		String rtStr = "0";
		String fId = request.getParameter("fId");
		try {
			financeService.openAccountFinanceOut(fId);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "openAccountFinanceOutDetial", method = RequestMethod.POST)
	public @ResponseBody String openAccountFinanceOutDetial(HttpServletRequest request) {
		String rtStr = "0";
		String fdId = request.getParameter("fdId");
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			Connection dbConn = requestDbConn.getSysDbConn();
			financeService.openAccountFinanceOutDetial(fdId,dbConn);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
}

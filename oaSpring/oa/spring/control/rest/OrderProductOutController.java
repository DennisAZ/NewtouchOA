package oa.spring.control.rest;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oa.spring.po.OrderProductOut;
import oa.spring.po.SaleOrder;
import oa.spring.service.OrderProductOutService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHBeanKeys;

@Controller
@RequestMapping("/opo")
public class OrderProductOutController {

	@Autowired
	private OrderProductOutService orderProductOutService;

	private static final Logger logger = Logger
			.getLogger(OrderProductOutController.class);

	/**
	 * 
	 * @param tableName
	 *            表名称
	 * 
	 * @return 要展示的对象的集合
	 */
	// 查询出所有的产品所属大类信息
	@RequestMapping(value = "custList/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody
	String custList(HttpServletRequest request,
			HttpServletResponse response) {
		 Connection dbConn = null;
    	 String str = "";
        try {
        	YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        	dbConn = requestDbConn.getSysDbConn();
            str = new String(orderProductOutService.queryProduct(dbConn,request.getParameterMap()).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return str;
	}
	@RequestMapping(value = "custContact", method = RequestMethod.POST)
	public @ResponseBody
	String custContact(HttpServletRequest request,
			HttpServletResponse response) {
		 Connection dbConn = null;
		 String cusId=request.getParameter("cusId");
    	 String str = "";
        try {
        	YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        	dbConn = requestDbConn.getSysDbConn();
            str = new String(orderProductOutService.queryContact(dbConn,cusId,request.getParameterMap()).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return str;
	}
	@RequestMapping(value = "getProductById", method = RequestMethod.POST)
	public @ResponseBody
	List getProductByIds(HttpServletRequest request) {
		String id = request.getParameter("id");
		List list = orderProductOutService.getSaleOrderByIds(id);
		System.out.println(list.size());
		return list;
	}

	@RequestMapping(value = "productDelete", method = RequestMethod.POST)
	public @ResponseBody
	String updateDelete(HttpServletRequest request) {
		String rtStr = "2";
		String id = request.getParameter("id");
		try {
			orderProductOutService.saleOrderDelete(id);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}

}

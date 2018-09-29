package oa.spring.control.rest;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oa.spring.po.ProductUnit;
import oa.spring.service.ProductUnitService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHBeanKeys;

@Controller
@RequestMapping("/productUnit")
public class ProductUnitController {

	@Autowired 
	private ProductUnitService productUnitService; 
	
	private static final Logger logger = Logger.getLogger(ProductUnitController.class);
	
	/**
	 * 
	 * @param tableName 表名称
	 * 
	 * @return 要展示的对象的集合
	 */
	@RequestMapping(value = "getList/{timestamp}", method = RequestMethod.POST)
    public @ResponseBody List getList(HttpServletRequest request,HttpServletResponse response) {
		
        return productUnitService.getProduct();
    }
	//查询出所有的产品所属大类信息
	@RequestMapping(value = "productList/{timestamp}", method = RequestMethod.POST)
    public @ResponseBody String getProductList(HttpServletRequest request,HttpServletResponse response) {
		 Connection dbConn = null;
    	 String str = "";
        try {
        	YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        	dbConn = requestDbConn.getSysDbConn();
            str = new String(productUnitService.queryProduct(dbConn,request.getParameterMap()).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return str;
    }
	@RequestMapping(value = "productSave", method = RequestMethod.POST)
    public @ResponseBody String addWareHouse(HttpServletRequest request) {
		String rtStr = "0";
		String unitName = request.getParameter("unitName");
		String unitId = request.getParameter("unitId");
		ProductUnit ps = new ProductUnit(unitId,unitName);
		try {
			productUnitService.addProduct(ps);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
    }
	
	@RequestMapping(value = "getProductById", method = RequestMethod.POST)
	public @ResponseBody List getProductByIds(HttpServletRequest request) {
		String id = request.getParameter("id");
		List list = productUnitService.getProductByIds(id);
		System.out.println(list.size());
		return list;
	}
	@RequestMapping(value = "productUpdate", method = RequestMethod.POST)
	public @ResponseBody String productUpdate(HttpServletRequest request) {
		String rtStr = "2";
		String unitName = request.getParameter("unitName");
		String unitId = request.getParameter("unitId");
		ProductUnit ps = new ProductUnit(unitId,unitName);
		try {
			productUnitService.productUpdate(ps);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "productDelete", method = RequestMethod.POST)
	public @ResponseBody String updateDelete(HttpServletRequest request) {
		String rtStr = "2";
		String id = request.getParameter("id");
		try {
			productUnitService.productDelete(id);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	
}

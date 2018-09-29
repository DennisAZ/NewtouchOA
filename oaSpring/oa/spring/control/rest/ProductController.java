package oa.spring.control.rest;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oa.spring.po.Product;
import oa.spring.po.ProductStyle;
import oa.spring.po.ProductType;
import oa.spring.po.ProductUnit;
import oa.spring.service.ProductService;
import oa.spring.util.HypyUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHBeanKeys;

@Controller
@RequestMapping("/product")
public class ProductController {

	@Autowired 
	private ProductService productService; 
	
	private static final Logger logger = Logger.getLogger(ProductController.class);
	
	/**
	 * 
	 * @param tableName 表名称
	 * 
	 * @return 要展示的对象的集合
	 */
	//查询出所有的产品所属大类信息
	@RequestMapping(value = "productList", method = RequestMethod.POST)
    public @ResponseBody String productList(HttpServletRequest request,HttpServletResponse response) {
		 Connection dbConn = null;
    	 String str = "";
    	 String param=request.getParameter("param");
        try {
        	YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        	dbConn = requestDbConn.getSysDbConn();
            str = new String(productService.queryProduct(dbConn,param,request.getParameterMap()).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return str;
    }
	@RequestMapping(value = "getCpProduct/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody String getCpProduct(HttpServletRequest request,HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(productService.getCpProduct(dbConn,request.getParameterMap()).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	/**
	 * 
	 * @param tableName 表名称
	 * 
	 * @return 要展示的对象的集合
	 */
	//查询工艺所属产品信息
	@RequestMapping(value = "selectProduct", method = RequestMethod.POST)
    public @ResponseBody String selectProduct(HttpServletRequest request,HttpServletResponse response) {
		 Connection dbConn = null;
    	 String str = "";
    	 String param=request.getParameter("param");
        try {
        	YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        	dbConn = requestDbConn.getSysDbConn();
            str = new String(productService.selectProduct(dbConn,param,request.getParameterMap()).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return str;
    }
	@RequestMapping(value = "getProduct/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody String getProduct(HttpServletRequest request,HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(productService.getProduct(dbConn,request.getParameterMap()).getBytes("UTF-8"),"ISO-8859-1");
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
		String proName = request.getParameter("proName");
		String proCode = request.getParameter("proCode");
		String proType = request.getParameter("proType");
		String proPrice = request.getParameter("proPrice");
		Double price=Double.parseDouble(proPrice);
		String psId = request.getParameter("psId");
		String ptId = request.getParameter("ptId");
		String unitId = request.getParameter("unitId");
		HypyUtil hu=new HypyUtil();
		String shortName=hu.cn2py(proName);
		String id = request.getParameter("id");
		ProductUnit pUnit=new ProductUnit();
		ProductStyle pStyle=new ProductStyle();
		ProductType pType=new ProductType();
		pUnit.setUnitId(unitId);
		pStyle.setId(psId);
		pType.setId(ptId);
		String remark = request.getParameter("remark");
		Product ps = new Product(proCode,proName,proType,price,pUnit,pType,pStyle,remark,shortName);
		rtStr = shortName+","+ptId;
		try {
			productService.addProduct(ps);
		} catch(Exception e){
			e.printStackTrace();
			rtStr="-1";
		}
		return rtStr;
    }
	@RequestMapping(value = "checkIsUsing", method = RequestMethod.POST)
	public @ResponseBody String checkIsUsing(HttpServletRequest request) {
		String rtStr = "0";
		String pro_id = request.getParameter("pro_id");
		String craftsId = request.getParameter("craftsId");
		try {
			int count = productService.checkIsUsing(pro_id,craftsId);
			if(count > 0){
				rtStr = "1";
			}
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "2";
		}
		return rtStr;
	}
	@RequestMapping(value = "checkDrawIsUsing", method = RequestMethod.POST)
	public @ResponseBody String checkDrawIsUsing(HttpServletRequest request) {
		String rtStr = "0";
		String pro_id = request.getParameter("pro_id");
		String drawingId = request.getParameter("drawingId");
		try {
			int count = productService.checkDrawIsUsing(pro_id,drawingId);
			if(count > 0){
				rtStr = "1";
			}
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "2";
		}
		return rtStr;
	}
	@RequestMapping(value = "setUsing", method = RequestMethod.POST)
	public @ResponseBody String setUsing(HttpServletRequest request) {
		String rtStr = "0";
		String craftsId = request.getParameter("craftsId");
		try {
			productService.setUsing(craftsId);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "getProType", method = RequestMethod.POST)
	public @ResponseBody List<Map<String,Object>> getProType(HttpServletRequest request) {
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		try {
			list=productService.getProType();
		} catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	@RequestMapping(value = "setDrawingUsing", method = RequestMethod.POST)
	public @ResponseBody String setDrawingUsing(HttpServletRequest request) {
		String rtStr = "0";
		String drawingId = request.getParameter("drawingId");
		try {      
			productService.setDrawingUsing(drawingId);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "changeUsing", method = RequestMethod.POST)
	public @ResponseBody String changeUsing(HttpServletRequest request) {
		String rtStr = "0";
		String pro_id = request.getParameter("pro_id");
		try {
			productService.changeUsing(pro_id);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "changeDrawUsing", method = RequestMethod.POST)
	public @ResponseBody String changeDrawUsing(HttpServletRequest request) {
		String rtStr = "0";
		String pro_id = request.getParameter("pro_id");
		try {
			productService.changeDrawUsing(pro_id);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	
	@RequestMapping(value = "getProductById", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> getProductByIds(HttpServletRequest request) {
		String id = request.getParameter("id");
		List<Map<String, Object>>list = productService.getProductByIds(id);
		return list;
	}
	@RequestMapping(value = "productUpdate", method = RequestMethod.POST)
	public @ResponseBody String productUpdate(HttpServletRequest request) {
		String rtStr = "2";
		String proName = request.getParameter("proName");
		String proCode = request.getParameter("proCode");
		String proType = request.getParameter("proType");
		String proPrice = request.getParameter("proPrice");
		Double price=Double.parseDouble(proPrice);
		String psId = request.getParameter("psId");
		String ptId = request.getParameter("ptId");
		HypyUtil hu=new HypyUtil();
		String shortName=hu.cn2py(proName);
		String unitId = request.getParameter("unitId");
		String id = request.getParameter("id");
		String remark = request.getParameter("remark");
		ProductUnit pUnit=new ProductUnit();
		ProductStyle pStyle=new ProductStyle();
		ProductType pType=new ProductType();
		pUnit.setUnitId(unitId);
		pStyle.setId(psId);
		pType.setId(ptId);
		Product ps = new Product(proCode,proName,proType,price,pUnit,pType,pStyle,remark,shortName);
		
		ps.setId(id);
		try {
			productService.productUpdate(ps);
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
			productService.productDelete(id);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value="getType/{timestamp}",method=RequestMethod.POST)
	public @ResponseBody List getType(HttpServletRequest request){
		
		List list=productService.getType();
		return list;
	}
}

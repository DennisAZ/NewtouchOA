package oa.spring.control.rest;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oa.spring.po.ProductType;
import oa.spring.service.ProductTypeService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHBeanKeys;

@Controller
@RequestMapping("/productType")
public class ProductTypeController {

	@Autowired
	private ProductTypeService productTypeService;

	private static final Logger logger = Logger
			.getLogger(ProductTypeController.class);

	/**
	 * 
	 * @param tableName
	 *            表名称
	 * 
	 * @return 要展示的对象的集合
	 */
	@RequestMapping(value = "showWHTree/{timestamp}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	List getProduct() {
		List list = productTypeService.getProduct();
		return list;
	}

	// 查询出所有的产品所属大类信息
	@RequestMapping(value = "productList/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody
	String getProductList(HttpServletRequest request,
			HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(productTypeService.queryProduct(dbConn,
					request.getParameterMap()).getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	@RequestMapping(value = "productSave", method = RequestMethod.POST)
	public @ResponseBody
	String addWareHouse(HttpServletRequest request) {
		String rtStr = "0";
		String name = request.getParameter("name");
		String id = request.getParameter("id");
		String parentId = request.getParameter("parentId");
		String remark = request.getParameter("remark");
		String parentName = request.getParameter("parentName");
		String treeCode = null;
		String treeName = null;
		if (parentName == null || parentName.equals("")) {
			id = "-1";
			parentName = "-1";
		}
		ProductType ptpe = new ProductType();
		List<ProductType> list = new ArrayList<ProductType>();
		if (parentId.equals("-1")) {
			list = productTypeService.getProductByIds(id);
		} else {
			list = productTypeService.getAllParent(parentId);
		}
		for (int i = 0; i < list.size(); i++) {
			ptpe = list.get(i);
		}
		treeCode = ptpe.getTreeCode();
		treeName = ptpe.getTreeName();
		if (treeName == null) {
			treeName = name;
		} else {
			treeName = treeName + "," + name;
		}
		ProductType pt = new ProductType(name, id, treeCode, treeName, remark,
				parentName);
		try {
			productTypeService.addProduct(pt);
			List<ProductType> listId = productTypeService.getByName(name);
			for (int i = 0; i < listId.size(); i++) {
				ptpe = listId.get(i);

			}
			if (treeCode == null) {
				treeCode = ptpe.getId();
			} else {
				treeCode = treeCode + "," + ptpe.getId();
			}
			ptpe.setTreeCode(treeCode);
			try {
				productTypeService.treeCodeUpdate(ptpe);
			} catch (Exception e) {
				e.printStackTrace();
				rtStr = "1";
			}
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}

	@RequestMapping(value = "getProductById", method = RequestMethod.POST)
	public @ResponseBody
	List getProductByIds(HttpServletRequest request) {
		String id = request.getParameter("id");
		List list = productTypeService.getProductByIds(id);
		System.out.println(list.size());
		return list;
	}

	@RequestMapping(value = "productUpdate", method = RequestMethod.POST)
	public @ResponseBody
	String updateWareHouse(HttpServletRequest request) {
		String rtStr = "2";
		String name = request.getParameter("name");
		String id = request.getParameter("id");
		String parentId = request.getParameter("parentId");
		String remark = request.getParameter("remark");
		String parentName = request.getParameter("parentName");
		String treeName = "";
		String treeCode = "";
		ProductType ptpe = new ProductType();
		if ("-1".equals(parentId)) {
			treeCode = id;
			treeName = name;

		} else {
			List<ProductType> list = productTypeService.getAllParent(parentId);
			ptpe.setParentId(id);
			if(list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				ptpe = list.get(i);
				if (!"".equals(treeCode)) {
					treeCode += ",";
					treeName += ",";
				}
				treeCode += ptpe.getTreeCode();
				treeName += ptpe.getTreeName();
			}
			}else{
				treeCode +=parentId+","+id;
				treeName += parentName+","+name;
				
			}
			String[] arrTree = treeName.split(",");
			treeName=treeName.replace(arrTree[1].toString(), name);
		}
		ProductType pt = new ProductType(name, parentId, treeCode, treeName,
				remark, parentName);
		pt.setId(id);

		try {
			productTypeService.productUpdate(pt);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}

	@RequestMapping(value = "productDelete", method = RequestMethod.POST)
	public @ResponseBody
	String updateDelete(HttpServletRequest request) {
		String rtStr = "2";
		String id = request.getParameter("id");
		try {
			productTypeService.productDelete(id);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
}

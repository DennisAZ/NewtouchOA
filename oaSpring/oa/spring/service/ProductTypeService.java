package oa.spring.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import oa.spring.dao.batis.ProductStyleMapper;
import oa.spring.dao.batis.ProductTypeMapper;
import oa.spring.po.ProductStyle;
import oa.spring.po.ProductType;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.form.YHFOM;

public class ProductTypeService {
	private ProductTypeMapper productTypeMapper;
	public ProductTypeMapper getProductTypeMapper() {
		return productTypeMapper;
	}

	public void setProductTypeMapper(ProductTypeMapper productTypeMapper) {
		this.productTypeMapper = productTypeMapper;
	}

	public String queryProduct(Connection dbConn, Map map) {
		try {
			String sql = "select id,name,parent_id,remark from erp_product_type where is_del='0'";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(map);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,
					queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<ProductType> getProduct() {

		return productTypeMapper.getProduct();
	}

	public void addProduct(ProductType pt) {
		productTypeMapper.addProduct(pt);
	}
	
	public List getProductByIds(String id){
		return productTypeMapper.getProductByIds(id);
		
	}
	public void productUpdate(ProductType pt){
		productTypeMapper.productUpdate(pt);
	}
	public List getAllParent(String parentId){
		return productTypeMapper.getAllParent(parentId);
		
	}
	public void treeCodeUpdate(ProductType pt){
		productTypeMapper.treeCodeUpdate(pt);
	}
	public List<ProductType>getByName(String name){
		return productTypeMapper.getByName(name);
	}
	public void productDelete(String id){
		
		
		
		List<Map<String,String>> list=productTypeMapper.getTypeId(id);
		for(int i=0;i<list.size();i++){
			productTypeMapper.productDelete(list.get(i).get("id"));
		}
	}
}

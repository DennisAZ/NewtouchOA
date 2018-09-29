package oa.spring.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import oa.spring.dao.batis.ProductStyleMapper;
import oa.spring.dao.batis.WareHouseMapper;
import oa.spring.po.ProductStyle;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.form.YHFOM;

public class ProductStyleService {
	private ProductStyleMapper productStyleMapper;

	public ProductStyleMapper getProductStyleMapper() {
		return productStyleMapper;
	}

	public void setProductStyleMapper(ProductStyleMapper productStyleMapper) {
		this.productStyleMapper = productStyleMapper;
	}

	public String queryProduct(Connection dbConn, Map map) {
		try {
			String sql = "select id,name,remark from erp_product_style where is_del='0'";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(map);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,
					queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<ProductStyle> getProduct() {

		return productStyleMapper.getProduct();
	}

	public void addProduct(ProductStyle ps) {
		productStyleMapper.addProduct(ps);
	}
	
	public List getProductByIds(String id){
		return productStyleMapper.getProductByIds(id);
		
	}
	public void productUpdate(ProductStyle ps){
		 productStyleMapper.productUpdate(ps);
	}
	public void productDelete(String id){
		productStyleMapper.productDelete(id);
	}
}

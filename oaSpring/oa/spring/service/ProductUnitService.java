package oa.spring.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import oa.spring.dao.batis.ProductUnitMapper;
import oa.spring.po.ProductUnit;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.form.YHFOM;

public class ProductUnitService {
	private ProductUnitMapper productUnitMapper;

	

	public ProductUnitMapper getProductUnitMapper() {
		return productUnitMapper;
	}

	public void setProductUnitMapper(ProductUnitMapper productUnitMapper) {
		this.productUnitMapper = productUnitMapper;
	}

	public String queryProduct(Connection dbConn, Map map) {
		try {
			String sql = "select unit_id,unit_name from erp_product_unit  where is_del='0'";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(map);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,
					queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<ProductUnit> getProduct() {

		return productUnitMapper.getProduct();
	}

	public void addProduct(ProductUnit ps) {
		productUnitMapper.addProduct(ps);
	}
	
	public List getProductByIds(String id){
		return productUnitMapper.getProductByIds(id);
		
	}
	public void productUpdate(ProductUnit ps){
		productUnitMapper.productUpdate(ps);
	}
	public void productDelete(String id){
		productUnitMapper.productDelete(id);
	}
}

package oa.spring.dao.batis;

import java.util.List;

import oa.spring.po.ProductStyle;
import oa.spring.po.ProductUnit;

public interface ProductUnitMapper {
	
	public List<ProductUnit> getProduct();

	public void addProduct(ProductUnit unit);
	
	public List getProductByIds(String id);
	
	public void productUpdate(ProductUnit unit);
	
	public void productDelete(String id);
}

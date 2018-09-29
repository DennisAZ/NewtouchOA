package oa.spring.dao.batis;

import java.util.List;

import oa.spring.po.ProductStyle;

public interface ProductStyleMapper {
	
	public List<ProductStyle> getProduct();

	public void addProduct(ProductStyle ps);
	
	public List getProductByIds(String id);
	
	public void productUpdate(ProductStyle ps);
	
	public void productDelete(String id);
}

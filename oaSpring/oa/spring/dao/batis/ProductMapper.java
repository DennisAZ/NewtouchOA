package oa.spring.dao.batis;

import java.util.List;
import java.util.Map;

import oa.spring.po.Product;

public interface ProductMapper {
	
	public List<Product> productList();

	public void addProduct(Product pro);
	public void setUsing(String craftsId);
	public void setDrawingUsing(String drawingId);
	public void changeUsing(String pro_id);
	public void changeDrawUsing(String pro_id);
	public int checkIsUsing(String pro_id);
	public int checkIsUsing1(Map<String,String> map);
	public int checkDrawIsUsing1(Map<String,String> map);
	public int checkDrawIsUsing(String pro_id);
	public List<Map<String, Object>> getProductByIds(String id);
	public List<Map<String, Object>> getPro(String id);
	
	public void productUpdate(Product pro);
	
	public void productDelete(String id);
	
	public List<Map<String,Object>>getType();
	public List<Map<String,Object>>getProType();
}

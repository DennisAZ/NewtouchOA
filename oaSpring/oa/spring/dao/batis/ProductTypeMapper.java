package oa.spring.dao.batis;

import java.util.List;
import java.util.Map;

import oa.spring.po.ProductStyle;
import oa.spring.po.ProductType;

public interface ProductTypeMapper {
	//查询所有产品类别
	public List<ProductType> getProduct();
	//添加产品类别
	public void addProduct(ProductType pt);
	//根据id查询产品类别
	public List getProductByIds(String id);
	//更新产品类别
	public void productUpdate(ProductType pt);
	//查询出当前产品类别的所有上级节点
	public List<ProductType>getAllParent(String parentId);
	//更新treeCode
	public void treeCodeUpdate(ProductType pt);
	//根据类别名字查询出id
	public List<ProductType> getByName(String name);
	public List<Map<String,String>> getTypeId(String id);
	//删除
	public void productDelete(String id);
}

package oa.spring.service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oa.spring.dao.batis.ProductMapper;
import oa.spring.po.Product;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.form.YHFOM;

public class ProductService {
	private ProductMapper productMapper;

	public ProductMapper getProductMapper() {
		return productMapper;
	}

	public void setProductMapper(ProductMapper productMapper) {
		this.productMapper = productMapper;
	}

	public String selectProduct(Connection dbConn, String param, Map map) {
		try {
			String[] arrParm=param.split(",");
			String sql = "";
			
			if ("-1".equals(param)) {
				//查询产品列表
				sql = "SELECT   p.id  AS pId,  pro_code,  pro_name,  p.shortName,  pro_type,  pro_price,  u.unit_name           AS uName,  ps.name               AS psName,  pt.name               AS ptName,  p.remark              AS pRemark  FROM erp_product p  LEFT JOIN erp_product_style ps    ON ps.id = p.ps_id  LEFT JOIN erp_product_type pt    ON pt.id = p.pt_id  LEFT JOIN erp_product_unit u    ON u.unit_id = p.unit_id WHERE p.is_del = '0' ";
			} else if("-3".equals(param)){
				//销售发货单过来的生产工艺查询产品列表
				sql= "SELECT  cp.id AS cpId,cp.pro_code,cp.pro_name,pro.shortName,pro.pro_type,cp.pro_price,cp.unit_name,cp.pt_name,cp.ps_name,cp.remark    AS pRemark,crafts.id,crafts.crafts_version FROM erp_po_pro ppro LEFT JOIN erp_cache_product cp  ON cp.id = ppro.pro_id  LEFT JOIN erp_product pro ON pro.id=cp.pro_id LEFT JOIN erp_produce_crafts crafts ON crafts.pro_id=pro.id LEFT JOIN erp_produce_drawing  draw ON draw.pro_id=cp.pro_id GROUP BY pro.id";
			}else if("-4".equals(param)){
				//工艺查询产品列表	
				sql = "SELECT p.id AS pId, pro_code,pro_name,p.shortName,pro_type,pro_price,u.unit_name AS uName,ps.name AS psName,pt.name AS ptName,p.remark AS pRemark,crafts.id,crafts.crafts_version,crafts.is_using,draw.id,draw.drawing_version,draw.is_using FROM erp_product p LEFT JOIN erp_product_style ps ON ps.id=p.ps_id LEFT JOIN erp_product_type pt ON pt.id=p.pt_id LEFT JOIN erp_product_unit u ON u.unit_id=p.unit_id LEFT JOIN erp_produce_crafts crafts ON crafts.pro_id=p.id LEFT JOIN erp_produce_drawing draw ON draw.pro_id =p.id WHERE p.is_del='0' AND crafts.is_using = '1' AND draw.is_using = '1'";
			}else {
				
				sql = "SELECT p.id AS pId, pro_code,pro_name,p.shortName,pro_type,pro_price,u.unit_name AS uName,ps.name AS psName,pt.name AS ptName,p.remark AS pRemark,crafts.id,crafts.crafts_version,crafts.is_using,draw.id,draw.drawing_version,draw.is_using FROM erp_product p LEFT JOIN erp_product_style ps ON ps.id=p.ps_id LEFT JOIN erp_product_type pt ON pt.id=p.pt_id LEFT JOIN erp_product_unit u ON u.unit_id=p.unit_id LEFT JOIN erp_produce_crafts crafts ON crafts.pro_id=p.id LEFT JOIN erp_produce_drawing draw ON draw.pro_id =p.id WHERE p.is_del='0' AND crafts.is_using = '1' AND draw.is_using = '1'";
					 if(!"".equals(arrParm[0])){
						sql+="  and shortName LIKE '%"+arrParm[0]+"%'";
					} if(!"".equals(arrParm[1])){
						sql+="  or  pro_name LIKE '%"+arrParm[1]+"%'";
						
					} if(!"".equals(arrParm[2])&&!"0".equals(arrParm[2])){
						sql+=" and p.pt_id ='"+arrParm[2]+"'";
					}
			}
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(map);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,
					queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public String queryProduct(Connection dbConn, String param, Map map) {
		try {
			String[] arrParm=param.split(",");
			String sql = "";
			
			if ("-1".equals(param)) {
				//查询产品列表
				sql = "SELECT   p.id  AS pId,  pro_code,  pro_name,  p.shortName,  pro_type,  pro_price,  u.unit_name           AS uName,  ps.name               AS psName,  pt.name               AS ptName,  p.remark              AS pRemark  FROM erp_product p  LEFT JOIN erp_product_style ps    ON ps.id = p.ps_id  LEFT JOIN erp_product_type pt    ON pt.id = p.pt_id  LEFT JOIN erp_product_unit u    ON u.unit_id = p.unit_id WHERE p.is_del = '0' ";
			} else {
				sql = "SELECT   p.id  AS pId,  pro_code,  pro_name,  p.shortName,  pro_type,  pro_price,  u.unit_name           AS uName,  ps.name               AS psName,  pt.name               AS ptName,  p.remark              AS pRemark  FROM erp_product p  LEFT JOIN erp_product_style ps    ON ps.id = p.ps_id  LEFT JOIN erp_product_type pt    ON pt.id = p.pt_id  LEFT JOIN erp_product_unit u    ON u.unit_id = p.unit_id WHERE p.is_del = '0' ";
				
				if(!"".equals(arrParm[0])){
					sql+="  and shortName LIKE '%"+arrParm[0]+"%'";
				}
				//当为-2的时候查询产品名称不执行
				if(!"-2".equals(arrParm[1])){
					sql+="  and pro_name LIKE '%"+arrParm[1]+"%'";
				}
				if(!"".equals(arrParm[2])&&!"0".equals(arrParm[2])){
					sql+=" and p.pt_id ='"+arrParm[2]+"'";
				}
			}
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(map);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,
					queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getProduct(Connection dbConn, Map map) {
		try {
		
			String sql = "SELECT  cp.id AS cpId,  p.id AS pId,  p.pro_code, p.pro_name,p.pro_price, p.pro_type, u.unit_name ";
						sql+=" AS uName,ps.name AS psName,pt.name AS ptName,p.remark AS pRemark FROM erp_db db";
						sql+=" LEFT JOIN erp_product p ON p.id=db.pro_id";
						sql+=" LEFT JOIN erp_product_style ps ON ps.id=p.ps_id LEFT JOIN erp_product_type pt ON pt.id=p.pt_id";
						sql+=" LEFT JOIN erp_product_unit u ON u.unit_id=p.unit_id  LEFT JOIN erp_cache_product cp ON cp.pro_id=p.id"; 
						sql+=" GROUP BY p.id";
						
		
			
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(map);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,
					queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public String getCpProduct(Connection dbConn, Map map) {
		try {
			
			String sql = "SELECT cp.id AS cpId,cp.pro_id,cp.pro_code,cp.pro_name,cp.pro_price,cp.unit_name,cp.pt_name,cp.ps_name FROM erp_cache_product cp  group by cp.pro_id";
			
			
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(map);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,
					queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Product> productList() {

		return productMapper.productList();
	}

	public void addProduct(Product pro) {
		productMapper.addProduct(pro);
	}
	public void setUsing(String craftsId) {
		productMapper.setUsing(craftsId);
	}
	public void setDrawingUsing(String drawingId) {
		productMapper.setDrawingUsing(drawingId);
	}
	public void changeUsing(String pro_id) {
		productMapper.changeUsing(pro_id);
	}
	public void changeDrawUsing(String pro_id) {
		productMapper.changeDrawUsing(pro_id);
	}
	
	public int checkIsUsing(String pro_id,String craftsId) {
		if(craftsId != null && !"".equals(craftsId)){
			Map<String,String> map = new HashMap<String,String>();
			map.put("pro_id", pro_id);
			map.put("craftsId", craftsId);
			return productMapper.checkIsUsing1(map);
		}
		return productMapper.checkIsUsing(pro_id);
	}
	public int checkDrawIsUsing(String pro_id,String drawingId) {
		if(drawingId != null && !"".equals(drawingId)){
			Map<String,String> map = new HashMap<String,String>();
			map.put("pro_id", pro_id);
			map.put("drawingId", drawingId);
			return productMapper.checkDrawIsUsing1(map);
		}
		return productMapper.checkDrawIsUsing(pro_id);
	}

	public List<Map<String, Object>> getProductByIds(String id) {
		return productMapper.getProductByIds(id);

	}

	public void productUpdate(Product pro) {
		productMapper.productUpdate(pro);
	}

	public void productDelete(String id) {
		productMapper.productDelete(id);
	}
	public List<Map<String,Object>>getType(){
		return productMapper.getType();
	}
	public List<Map<String,Object>>getProType(){
		return productMapper.getProType();
	}
}

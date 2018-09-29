package com.psit.struts.BIZ.impl;

import java.util.List;

import com.psit.struts.BIZ.TypeListBIZ;
import com.psit.struts.DAO.CusAreaDAO;
import com.psit.struts.DAO.CusCityDAO;
import com.psit.struts.DAO.CusProvinceDAO;
import com.psit.struts.DAO.TypeListDAO;
import com.psit.struts.entity.CusArea;
import com.psit.struts.entity.CusCity;
import com.psit.struts.entity.CusProvince;
import com.psit.struts.entity.TypeList;
import com.psit.struts.util.DAO.ModifyTypeDAO;

public class TypeListBIZImpl implements TypeListBIZ{
	TypeListDAO TypeListDao=null;
	ModifyTypeDAO modType=null;
	CusAreaDAO provDAO=null;
	CusProvinceDAO cityDAO=null;
	CusCityDAO districtDAO=null;
	
	/**
	 * 
	 * 根据类别名称和类型得到类别 <br>
	 * @param typeName 类别名称
	 * @param typType 类型
	 * @return TypeList 返回类别实体
	 */
	public TypeList getTypeByName(String typeName,String typType){
		return TypeListDao.getTypeByName(typeName, typType);
	}
	/**
	 * 
	 * 获得某个类型下的所有已启用的类别 <br>
	 * @param type  类型
	 * @return List<TypeList> 返回类别列表
	 */
	public List<TypeList> getEnabledType(String type){
		List<TypeList> list= TypeListDao.getEnabledType(type);
		return list;
	}
	/**
	 * 
	 * 保存类别<br>
	 * @param typeList 类别实体
	 */
	public void save(TypeList typeList){
		TypeListDao.save(typeList);
	}
	/**
	 * 
	 * 查询某个类型下的所有类别 <br>
	 * @param type 类型
	 * @return List<TypeList> 返回类别列表
	 */
	public List<TypeList> findAll(String type){
		return TypeListDao.findAll(type);
	}
	/**
	 * 
	 * 修改某个类型的类别 <br>
	 * @param tableName 表名
	 * @param prefix 前缀
	 * @param typeNames 类别名称
	 * @param typeIds 类别id
	 * @param enabledIds 是否启用
	 * @param type 类型
	 */
	public void modType(String tableName, String prefix, String[] typeNames,
			String[] typeIds, String[] enabledIds, String type) {
		modType.modType(tableName, prefix, typeNames, typeIds, enabledIds, type);
	}
	
	/**
	 * 获得所有类别列表 <br>
	 * @return List 类别实体列表<br>
	 */
	public List<TypeList> findAll(){
		return TypeListDao.findAll();
	}
	
	/**
	 * 根据id查找类别<br>
	 * @param id  类别Id
	 * @return  类别实体
	 */
	public TypeList findById(Long id) {
		return TypeListDao.findById(id);
	}
	
	public List getProv() {
		return provDAO.getCusArea();
	}
	public List getCity(String provId) {
		return cityDAO.getCusProvince(Long.parseLong(provId));
	}
	public List getDistrict(String cityId) {
		return districtDAO.getCusCity(Long.parseLong(cityId));
	}
	
	public CusArea findProvince(long id){
		return provDAO.showCountry(id);
	}
	public CusProvince findCity(long id){
		return cityDAO.showProvince(id);
	}
	public CusCity findDistrict(long id){
		return districtDAO.showCity(id);
	}
	
	public void setTypeListDao(TypeListDAO typeListDao) {
		TypeListDao = typeListDao;
	}
	public void setModType(ModifyTypeDAO modType) {
		this.modType = modType;
	}
	public void setProvDAO(CusAreaDAO provDAO) {
		this.provDAO = provDAO;
	}
	public void setCityDAO(CusProvinceDAO cityDAO) {
		this.cityDAO = cityDAO;
	}
	public void setDistrictDAO(CusCityDAO districtDAO) {
		this.districtDAO = districtDAO;
	}
}

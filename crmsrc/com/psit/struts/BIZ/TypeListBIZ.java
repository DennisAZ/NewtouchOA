package com.psit.struts.BIZ;

import java.util.List;

import com.psit.struts.entity.CusArea;
import com.psit.struts.entity.CusCity;
import com.psit.struts.entity.CusProvince;
import com.psit.struts.entity.TypeList;

public interface TypeListBIZ {
	/**
	 * 根据类别名称和类型得到类别 <br>
	 * @param typeName 类别名称
	 * @param typType 类型
	 * @return TypeList 返回类别实体
	 */
	public TypeList getTypeByName(String typeName,String typType);
	/**
	 * 获得某个类型下的所有已启用的类别 <br>
	 * @param type  类型
	 * @return List<TypeList> 返回类别列表
	 */
	public List<TypeList> getEnabledType(String type);
	/**
	 * 保存类别<br>
	 * @param transientInstance 类别实体
	 */
	public void save(TypeList transientInstance);
	/**
	 * 查询某个类型下的所有类别 <br>
	 * @param type 类型
	 * @return List<TypeList> 返回类别列表
	 */
	public List<TypeList> findAll(String type);
	/**
	 * 修改某个类型的类别 <br>
	 * @param tableName 表名
	 * @param prefix 前缀
	 * @param typeNames 类别名称
	 * @param typeIds 类别id
	 * @param enabledIds 是否启用
	 * @param type 类型
	 */
	public void modType(String tableName,String prefix,String[] typeNames,String[] typeIds,String[] enabledIds,String type);
	
	/**
	 * 获得所有类别列表 <br>
	 * @return List 类别实体列表<br>
	 */
	public List<TypeList> findAll();
	
	/**
	 * 根据Id获得类型实体<br>
	 * @param id  类型id
	 */
	public TypeList findById(java.lang.Long id);
	
	public List getProv();
	public List getCity(String provId);
	public List getDistrict(String cityId);
	
	public CusArea findProvince(long id);
	public CusProvince findCity(long id);
	public CusCity findDistrict(long id);
}

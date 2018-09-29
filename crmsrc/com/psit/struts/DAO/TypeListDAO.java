package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.TypeList;
/**
 * 
 * 类别管理DAO <br>
 *
 * create_date: Aug 18, 2010,2:43:15 PM<br>
 * @author zjr
 */
public interface TypeListDAO {
	/**
	 * 
	 * 根据类别名称和类型得到类别 <br>
	 * create_date: Aug 11, 2010,10:37:45 AM <br>
	 * @param typeName 类别名称
	 * @param typType 类型
	 * @return TypeList 返回类别实体
	 */
	public TypeList getTypeByName(String typeName,String typType);
	/**
	 * 
	 * 获得某个类型下的所有已启用的类别 <br>
	 * create_date: Aug 11, 2010,10:41:43 AM <br>
	 * @param type  类型
	 * @return List<TypeList> 返回类别列表
	 */
	public List<TypeList> getEnabledType(String type);
	/**
	 * 
	 * 保存类别<br>
	 * create_date: Aug 11, 2010,10:43:21 AM <br>
	 * @param transientInstance 类别实体
	 */
	public void save(TypeList transientInstance);
	/**
	 * 
	 * 查询某个类型下的所有类别 <br>
	 * create_date: Aug 11, 2010,10:45:20 AM <br>
	 * @param type 类型
	 * @return List<TypeList> 返回类别列表
	 */
	public List<TypeList> findAll(String type);
	
	/**
	 * 获得所有类别列表 <br>
	 * create_date: Dec 9, 2010,12:40:18 AM <br>
	 * @return List 类别实体列表<br>
	 */
	public List<TypeList> findAll();
	
	/**
	 * 根据Id获得类型实体<br>
	 * create_date: Dec 9, 2010,9:38:26 AM <br>
	 * @param id  类型id
	 */
	public TypeList findById(java.lang.Long id);
}

package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.CusArea;
/**
 * 
 * 国家DAO <br>
 *
 * create_date: Aug 12, 2010,5:54:59 PM<br>
 * @author zjr
 */
public interface CusAreaDAO 
{   
     /**
      * 
      * 获得已启用的国家 <br>
      * create_date: Aug 12, 2010,5:57:56 PM <br>
      * @return List 返回国家列表
      */
	 public List getCusArea();
	 /**
	  * 
	  * 保存国家 <br>
	  * create_date: Aug 13, 2010,9:30:14 AM <br>
	  * @param cusArea 国家实体
	  */
	 public void saveCountry(CusArea cusArea);
	 /**
	  * 
	  * 获得所有国家 <br>
	  * create_date: Aug 13, 2010,9:36:05 AM <br>
	  * @return List 返回国家列表
	  */
	 public List getAllCusArea();
	 /**
	  * 
	  * 根据国家id获得国家实体 <br>
	  * create_date: Aug 13, 2010,9:40:06 AM <br>
	  * @param id 国家id
	  * @return CusArea 返回国家实体
	  */
	 public CusArea showCountry(long id);
}
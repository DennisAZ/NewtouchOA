package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.CrmManagementAreaRange;
import com.psit.struts.entity.CusProvince;
/**
 * 
 * 省份DAO <br>
 *
 * create_date: Aug 13, 2010,4:32:26 PM<br>
 * @author zjr
 */
public interface CusProvinceDAO 
{
	/**
	 * 
	 * 获得自已启用的省 <br>
	 * @param id 省份id
	 * @return List 返回省份列表 
	 */
	public List<java.lang.Long> getMyManageCusProvince(long id);
	/**
	 * 
	 * 获得已启用的省 <br>
	 * create_date: Aug 6, 2010,4:28:42 PM <br>
	 * @param id 省份id
	 * @return List 返回省份列表 
	 */
	 public List getCusProvince(long id);
	 
	 /**
	 * 获取我自己管辖范围内的省份名称
	 * @param priv_id 省份Id
	 * @return
	 */
	 public List<CusProvince> getmyManageProvinceName(long priv_id);
	 
	/**
	 * 
	 * 保存省份信息 <br>
	 * create_date: Aug 6, 2010,5:25:34 PM <br>
	 * @param cusProvince 省份实体
	 */
	 public void saveProvince(CusProvince cusProvince);
	/**
	 * 
	 * 获取所有省份 <br>
	 * create_date: Aug 6, 2010,5:26:04 PM <br>
	 * @return List 返回省份列表 
	 */
	 public List getAllProvince();
	/**
	 * 
	 * 得到省份实体 <br>
	 * create_date: Aug 6, 2010,4:29:12 PM <br>
	 * @param id 省份id
	 * @return CusProvince 返回省份实体 
	 */
	 public CusProvince showProvince(long id);
}
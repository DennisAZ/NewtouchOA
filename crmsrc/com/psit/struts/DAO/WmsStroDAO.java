package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.WmsStro;
/**
 * 
 * 仓库DAO <br>
 *
 * create_date: Aug 19, 2010,11:59:57 AM<br>
 * @author zjr
 */
public interface WmsStroDAO {
	/**
	 * 
	 * 保存仓库 <br>
	 * create_date: Aug 11, 2010,2:16:14 PM <br>
	 * @param wmsStro
	 */
	public void save(WmsStro wmsStro);
	/**
	 * 
	 * 按仓库名称查询仓库列表数量 <br>
	 * create_date: Aug 11, 2010,2:17:18 PM <br>
	 * @param wmsName 仓库名称
	 * @return int 返回仓库列表数量
	 */
	public int getWmsCount(String wmsName);
	/**
	 * 
	 * 按仓库名称查询仓库列表 <br>
	 * create_date: Aug 11, 2010,2:18:09 PM <br>
	 * @param wmsName 仓库名称
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回仓库列表
	 */
	public List<WmsStro> WmsSearch(String wmsName,int currentPage,int pageSize);
	/**
	 * 
	 * 查询仓库列表 <br>
	 * create_date: Aug 11, 2010,2:32:30 PM <br>
	 * @return List 返回仓库列表
	 */
	public List findAll();
	/**
	 * 
	 * 查看仓库详情 <br>
	 * create_date: Aug 11, 2010,2:20:52 PM <br>
	 * @param wmsCode 仓库编号
	 * @return WmsStro 仓库实体
	 */
	public WmsStro findWmsStro(String wmsCode);
	/**
	 * 
	 * 更新仓库 <br>
	 * create_date: Aug 11, 2010,2:21:58 PM <br>
	 * @param wmsStro 仓库实体
	 */
	public void updateWmsStro(WmsStro wmsStro);
	/**
	 * 
	 * 删除仓库 <br>
	 * create_date: Aug 11, 2010,2:22:21 PM <br>
	 * @param wmsStro 仓库实体
	 */
	public void delete(WmsStro wmsStro);
	/**
	 * 
	 * 判断仓库名称是否重复 <br>
	 * create_date: Aug 11, 2010,3:38:34 PM <br>
	 * @param wmsName 仓库名称
	 * @return List 返回仓库列表
	 */
	public List checkWmsName(String wmsName);
}
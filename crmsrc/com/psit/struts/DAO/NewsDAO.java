package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.News;
/**
 * 
 * 新闻公告DAO <br>
 *
 * create_date: Aug 16, 2010,3:32:05 PM<br>
 * @author zjr
 */
public interface NewsDAO  {
	/**
	 * 
	 * 保存新闻公告 <br>
	 * create_date: Aug 6, 2010,1:46:05 PM <br>
	 * @param news 新闻公告实体
	 */
	public void saveNews(News news);
	/**
	 * 
	 * 获得所有已收新闻公告（无分页） <br>
	 * create_date: Aug 6, 2010,1:47:24 PM <br>
	 * @param empId 员工id
	 * @param type 类型
	 * @return List 返回新闻公告列表 
	 */
	public List getAllNews(Long empId,String type);
	/**
	 * 
	 * 获得所有已收新闻公告（有分页） <br>
	 * create_date: Aug 6, 2010,1:49:55 PM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @param empId 员工id
	 * @return List 返回新闻公告列表 
	 */
	public List getAllNews(int pageCurrentNo, int pageSize,String orderItem,String isDe, Long empId);
	/**
	 * 
	 * 获得所有已收新闻公告条数 <br>
	 * create_date: Aug 6, 2010,1:51:16 PM <br>
	 * @param empId 员工id
	 * @return int 返回新闻公告列表数量 
	 */
	public int getAllNewsCount(Long empId);
	/**
	 * 
	 * 根据新闻编号获得新闻公告 <br>
	 * create_date: Aug 6, 2010,1:52:21 PM <br>
	 * @param newCode 新闻编号
	 * @return News 返回新闻实体 
	 */
	public News showNewsInfo(Long newCode);
	/**
	 * 
	 * 获得特定账号已发的新闻公告 <br>
	 * create_date: Aug 6, 2010,1:52:52 PM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @param empId 员工id
	 * @return List 返回新闻公告列表 
	 */
	public List getHaveSenNews(String orderItem, String isDe,int pageCurrentNo, int pageSize);
	/**
	 * 
	 * 获得特定账号已发的新闻公告的数量 <br>
	 * create_date: Aug 6, 2010,1:53:44 PM <br>
	 * @param empId 员工id
	 * @return int 返回新闻公告列表数量 
	 */
	public int getHaveSenNewsCount();
	/**
	 * 
	 * 更新新闻公告信息 <br>
	 * create_date: Aug 6, 2010,1:54:50 PM <br>
	 * @param news 新闻公告实体
	 */
	public void update(News news);
	/**
	 * 
	 * 删除新闻公告 <br>
	 * create_date: Aug 6, 2010,1:56:10 PM <br>
	 * @param news 新闻公告实体
	 */
	public void delNews(News news);
	
	
	/**
	 * 根据新闻Id查找所有接收人 <br>
	 * create_date: Feb 17, 2011,10:15:59 AM <br>
	 * @param newId 新闻Id
	 * @return List 返回接收人列表<br>
	 */
	public List findAllMsgMan(Long newId);
	
	/**
	 * 清空新闻的接收人 <br>
	 * create_date: Feb 17, 2011,11:05:29 AM <br>
	 * @param newId void 新闻Id<br>
	 */
	public void delRnewLim(Long newId);
}
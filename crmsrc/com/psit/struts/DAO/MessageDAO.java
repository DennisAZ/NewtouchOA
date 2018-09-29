package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.Message;
/**
 * 
 * 消息报告DAO <br>
 *
 * create_date: Aug 16, 2010,2:43:45 PM<br>
 * @author zjr
 */
public interface MessageDAO  
{
	/**
	 * 
	 * 保存消息内容 <br>
	 * create_date: Aug 6, 2010,12:03:07 PM <br>
	 * @param message 消息实体
	 */
	public void save(Message message);
	/**
	 * 
	 * 获得特定账号已提交的报告<br>
	 * create_date: Aug 6, 2010,2:08:04 PM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @param empId 员工id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @return List 返回报告列表 
	 */
	public List getHaveSenMess(int pageCurrentNo, int pageSize,Long empId,String orderItem, String isDe);
	/**
	 * 
	 * 获得特定账号已提交的报告的数量 <br>
	 * create_date: Aug 6, 2010,12:05:00 PM <br>
	 * @param empId 员工id
	 * @return  int 返回报告列表数量 
	 */
	public int getHaveSenMessCount(Long empId);
	/**
	 * 
	 * 获得特定账号待发的消息 <br>
	 * create_date: Aug 6, 2010,12:05:49 PM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @param empId 员工id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @return List 返回消息列表 
	 */
	public List getReadySenMess(int pageCurrentNo, int pageSize,Long empId, String orderItem, String isDe);
	/**
	 * 
	 * 获得特定账号待发的消息的数量 <br>
	 * create_date: Aug 6, 2010,12:06:18 PM <br>
	 * @param empId 员工id
	 * @return int 返回消息数量 
	 */
	public int getReadySenMessCount(Long empId);
	/**
	 * 
	 * 获得消息详情 <br>
	 * create_date: Aug 6, 2010,12:07:09 PM <br>
	 * @param meCode 消息id
	 * @return Message 返回消息实体 
	 */
	public Message showMessInfo(Long meCode);
	/**
	 * 
	 * 获得所有已收消息 <br>
	 * create_date: Aug 6, 2010,12:10:05 PM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @param empId 员工id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @return List 返回消息列表 
	 */
	public List getAllMess(int pageCurrentNo, int pageSize,Long empId, String orderItem, String isDe);
	/**
	 * 
	 * 获得所有已收消息条数 <br>
	 * create_date: Aug 6, 2010,12:10:43 PM <br>
	 * @param empId 员工id
	 * @return int 返回消息数量 
	 */
	public int getAllMessCount(Long empId);
	/**
	 * 
	 * 获得已读或未读消息列表 <br>
	 * create_date: Aug 6, 2010,12:11:26 PM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @param empId 员工id
	 * @param isRead 是否已读
	 * @return List 返回消息列表 
	 */
	public List getIsReadMess(int pageCurrentNo, int pageSize, 
			Long empId,String isRead);
	/**
	 * 
	 * 获得已读或未读消息的条数 <br>
	 * create_date: Aug 6, 2010,12:11:58 PM <br>
	 * @param empId 员工id
	 * @param isRead 是否已读
	 * @return int 返回消息列表数量 
	 */
	public int getIsReadMessCount(Long empId,String isRead);
	/**
	 * 
	 * 获得已回或未回消息列表 <br>
	 * create_date: Aug 6, 2010,12:12:29 PM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @param empId 员工id
	 * @param isRep 是否回复
	 * @return List 返回消息列表 
	 */
	public List getIsReplyMes(int pageCurrentNo, int pageSize, 
			Long empId, String isRep);
	/**
	 * 
	 * 获得已回或未回消息条数 <br>
	 * create_date: Aug 6, 2010,12:13:08 PM <br>
	 * @param empId 员工id
	 * @param isRep 是否回复
	 * @return int 返回消息列表数量 
	 */
	public int getIsReplyCount(Long empId,String isRep);
	/**
	 * 
	 * 更新消息 <br>
	 * create_date: Aug 6, 2010,12:13:37 PM <br>
	 * @param message 消息实体
	 */
	public void update(Message message);
	/**
	 * 
	 * 删除消息 <br>
	 * create_date: Aug 6, 2010,12:14:05 PM <br>
	 * @param message 消息实体
	 */
	public void delMess(Message message);
	/**
	 * 
	 * 得到无用的消息 <br>
	 * create_date: Aug 6, 2010,12:14:28 PM <br>
	 * @param empId 账号
	 * @return List 返回消息列表 
	 */
	public List getNoUseMess(Long empId);
}
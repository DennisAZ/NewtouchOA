package com.psit.struts.BIZ;

import java.util.Date;
import java.util.List;

import com.psit.struts.entity.Message;
import com.psit.struts.entity.News;
import com.psit.struts.entity.RMessLim;
import com.psit.struts.entity.RNewLim;
import com.psit.struts.entity.RRepLim;
import com.psit.struts.entity.Report;
import com.psit.struts.entity.Schedule;
/**
 * 
 * 协调办公BIZ <br>
 *
 * create_date: Aug 6, 2010,10:48:07 AM<br>
 * @author zjr
 */
public interface MessageBIZ 
{
	/**
	 * 
	 * 保存报告 <br>
	 * create_date: Aug 6, 2010,11:02:13 AM <br>
	 * @param report 保存实体
	 */
	public void saveReport(Report report);
	/**
	 * 
	 * 获得特定账号已提交的报告 <br>
	 * create_date: Aug 6, 2010,11:09:24 AM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @param empId 员工id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @return List 返回报告列表
	 */
	public List getHaveSenRep(int pageCurrentNo, int pageSize,Long empId, String orderItem, String isDe);
	/**
	 * 
	 * 获得特定账号已提交的报告的数量 <br>
	 * create_date: Aug 6, 2010,11:24:29 AM <br>
	 * @param empId 员工id
	 * @return int 返回报告列表数量
	 */
	public int getHaveSenRepCount(Long empId);
	/**
	 * 
	 * 获得特定账号待发的报告 <br>
	 * create_date: Aug 6, 2010,11:27:00 AM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @param empId 员工id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @return List 返回报告列表
	 */
	public List<Report> getReadySenRep(int pageCurrentNo, int pageSize,Long empId, String orderItem, String isDe);
	/**
	 * 
	 * 获得特定账号待发的报告的数量 <br>
	 * create_date: Aug 6, 2010,11:27:46 AM <br>
	 * @param empId 员工id
	 * @return int 返回报告列表数量
	 */
	public int getReadySenRepCount(Long empId);
	/**
	 * 
	 * 根据报告的Id获得报告内容 <br>
	 * create_date: Aug 6, 2010,11:30:37 AM <br>
	 * @param repCode 报告id
	 * @return Report 返回报告实体
	 */
	public Report showReportInfo(Long repCode);
	/**
	 * 
	 * 保存报告接收人 <br>
	 * create_date: Aug 6, 2010,11:31:10 AM <br>
	 * @param rrepLim 报告接收人实体
	 */
	public void save(RRepLim rrepLim);
	/**
	 * 
	 * 查看所有已收到的报告 <br>
	 * create_date: Aug 6, 2010,11:35:40 AM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @param empId 员工id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @return List 返回报告列表
	 */
	public List getAllReport(int pageCurrentNo, int pageSize,Long empId, String orderItem, String isDe);
	/**
	 * 
	 * 获得所有已收到的报告的数量 <br>
	 * create_date: Aug 6, 2010,11:37:00 AM <br>
	 * @param empId 员工id
	 * @return int 返回报告列表数量
	 */
	public int getAllReportCount(Long empId);
	/**
	 * 
	 * 更新报告 <br>
	 * create_date: Aug 6, 2010,11:38:01 AM <br>
	 * @param report 报告实体
	 */
	public void update(Report report);
	/**
	 * 
	 * 报告接受人实体 <br>
	 * create_date: Aug 6, 2010,11:38:34 AM <br>
	 * @param id 接受人id
	 * @return RRepLim 返回报告接受人实体
	 */
	public RRepLim getRRepLim(Long id);
	/**
	 * 
	 * 查看某一报告是否存在下一审批步骤 <br>
	 * @param appOrder 审批步骤
	 * @param repCode 报告id
	 * @return List 返回报告列表
	 */
	public List getRRepLim(Integer appOrder,String repCode);
	/**
	 * 
	 * 查看某一报告是否审批结束 <br>
	 * create_date: Aug 6, 2010,11:41:25 AM <br>
	 * @param repCode 报告id
	 * @return List 返回报告列表
	 */
	public List getRRepLim(String repCode);
	/**
	 * 
	 * 判断某个报告同一步骤中的所有人是否都审批结束 <br>
	 * create_date: Aug 6, 2010,11:44:46 AM <br>
	 * @param n 流程步骤
	 * @param repCode 报告id
	 * @return boolean 是返回true,否返回false
	 */
	public boolean isAllAppro(int n,String repCode);
	/**
	 * 
	 * 判断某个报告是否全部审批完 <br>
	 * create_date: Aug 6, 2010,11:45:53 AM <br>
	 * @param repCode 报告id
	 * @return boolean 是返回true,否返回false
	 */
	public boolean isAllAppro(String repCode);
	/**
	 * 
	 * 更新审批内容 <br>
	 * create_date: Aug 6, 2010,11:47:30 AM <br>
	 * @param rrepLim 报告接受人实体
	 */
	public void update(RRepLim rrepLim);
	/**
	 * 
	 * 删除报告 <br>
	 * create_date: Aug 6, 2010,11:47:56 AM <br>
	 * @param report 报告实体
	 */
	public void delReport(Report report);
	/**
	 * 
	 * 得到无用报告 <br>
	 * create_date: Aug 6, 2010,11:48:35 AM <br>
	 * @param empId 员工id
	 * @return List 返回报告列表 
	 */
	public List getNoUseRep(Long empId);
	/**
	 * 
	 * 得到已审批的报告 <br>
	 * create_date: Aug 6, 2010,11:49:13 AM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @param empId 员工id
	 * @return List 返回报告列表 
	 */
	public List getAppReport(int pageCurrentNo, int pageSize, Long empId);
	/**
	 * 
	 * 得到已审批的报告数量 <br>
	 * create_date: Aug 6, 2010,11:50:55 AM <br>
	 * @param empId 员工id
	 * @return int 返回报告列表数量 
	 */
	public int getAppReportCount(Long empId);
	/**
	 * 
	 * 得到未审批的报告 <br>
	 * create_date: Aug 6, 2010,11:51:41 AM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @param empId 员工id
	 * @return List 返回报告列表 
	 */
	public List getNoAppReport(int pageCurrentNo, int pageSize, Long empId);
	/**
	 * 
	 * 得到未审批的报告数量 <br>
	 * create_date: Aug 6, 2010,11:52:58 AM <br>
	 * @param empId 员工id
	 * @return int 返回报告列表数量 
	 */
	public int getNoAppReportCount(Long empId);
	/**
	 * 
	 * 获得待删除的所有已发报告 <br>
	 * create_date: Aug 6, 2010,11:55:57 AM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回报告列表 
	 */
	public List findDelReport(int pageCurrentNo, int pageSize);
	/**
	 * 
	 * 获得待删除的所有已发报告数量 <br>
	 * create_date: Aug 6, 2010,11:56:33 AM <br>
	 * @return int 返回报告数量 
	 */
	public int findDelReportCount();
	/**
	 * 
	 * 获得所有下级账号 <br>
	 * create_date: Aug 6, 2010,11:57:13 AM <br>
	 * @param userNum 用户码
	 * @param userCode 账号
	 * @return List 返回用户列表 
	 */
	public List getAllLowerUser(String userNum,String userCode);
	/**
	 * 
	 * 保存消息内容 <br>
	 * create_date: Aug 6, 2010,12:03:07 PM <br>
	 * @param message 消息实体
	 */
	public void save(Message message);
	/**
	 * 
	 * 保存接收人 <br>
	 * create_date: Aug 6, 2010,12:03:40 PM <br>
	 * @param rmessLim 接收人实体
	 */
	public void save(RMessLim rmessLim);
	/**
	 * 
	 * 获得特定账号已提交的报告 <br>
	 * create_date: Aug 6, 2010,12:04:15 PM <br>
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
	public List getReadySenMess(int pageCurrentNo, int pageSize,Long empId,String orderItem, String isDe);
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
	public List getIsReadMess(int pageCurrentNo, int pageSize, Long empId,String isRead);
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
	public List getIsReplyMes(int pageCurrentNo, int pageSize, Long empId, String isRep);
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
	 * @param empId 员工id
	 * @return List 返回消息列表 
	 */
	public List getNoUseMess(Long empId);
	/**
	 * 
	 * 得到接收人实体 <br>
	 * create_date: Aug 6, 2010,12:15:48 PM <br>
	 * @param id 接收人id
	 * @return RMessLim 返回接收人实体  
	 */
	public RMessLim getRMessLim(Long id);
	/**
	 * 
	 * 更新接收人消息 <br>
	 * create_date: Aug 6, 2010,12:16:20 PM <br>
	 * @param rmessLim 接收人实体
	 */
	public void update(RMessLim rmessLim);
	/**
	 * 
	 * 获得待删除的所有已收消息 <br>
	 * create_date: Aug 6, 2010,12:17:00 PM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回消息列表 
	 */
	public List findDelMail(int pageCurrentNo, int pageSize);
	/**
	 * 
	 * 获得待删除的所有已收消息数量 <br>
	 * create_date: Aug 6, 2010,12:17:32 PM <br>
	 * @return int 返回消息列表数量 
	 */
	public int findDelMailCount();
	/**
	 * 
	 * 删除已收消息 <br>
	 * create_date: Aug 6, 2010,12:17:59 PM <br>
	 * @param rmessLim 接收人实体
	 */
	public void delRMessLim(RMessLim rmessLim);
	/**
	 * 
	 * 保存日程内容 <br>
	 * create_date: Aug 6, 2010,12:18:28 PM <br>
	 * @param schedule 日程实体
	 */
	public void save(Schedule schedule);
	/**
	 * 
	 * 获得最近的日程安排 <br>
	 * create_date: Aug 6, 2010,12:56:18 PM <br>
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @param empId 员工id
	 * @return List 返回日程列表
	 */
	public List getSchByDate(Date startDate,Date endDate,Long empId);
	/**
	 * 
	 * 获得最近的日程安排数量 <br>
	 * create_date: Aug 6, 2010,1:40:42 PM <br>
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @param empId 员工id
	 * @return int 返回日程数量 
	 */
	public int getSchByDateCount(Date startDate,Date endDate,Long empId);
	/**
	 * 
	 * 根据Id获得日程安排 <br>
	 * create_date: Aug 6, 2010,1:42:18 PM <br>
	 * @param id 日程id
	 * @return Schedule 返回日程实体 
	 */
	public Schedule showSchedule(Long id);
	/**
	 * 
	 * 更新日程安排信息 <br>
	 * create_date: Aug 6, 2010,1:43:52 PM <br>
	 * @param schedule 日程实体
	 */
	public void update(Schedule schedule);
	/**
	 * 
	 * 删除日程安排 <br>
	 * create_date: Aug 6, 2010,1:44:16 PM <br>
	 * @param schedule 日程实体
	 */
	public void delSchedule(Schedule schedule);
	/**
	 * 
	 * 根据日期查询日程 <br>
	 * create_date: Aug 6, 2010,1:44:43 PM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @param args[0] 开始日期
	 * @param args[1] 结束日期
	 * @param args[2] 员工id
	 * @param args[3] 是否全部
	 * @return List 返回日程列表 
	 */
	public List getSchList(String [] args, String orderItem, String isDe,int pageCurrentNo, int pageSize);
	/**
	 * 
	 * 根据日期查询日程数量 <br>
	 * create_date: Aug 6, 2010,1:45:28 PM <br>
	 * @param args[0]:开始日期
	 * @param args[1]:结束日期
	 * @param args[2]: 员工id
	 * @param args[3]:是否全部
	 * @return int 返回日程列表数量 
	 */
	public int getSchListCount(String [] args);
	/**
	 * 
	 * 保存新闻公告 <br>
	 * create_date: Aug 6, 2010,1:46:05 PM <br>
	 * @param news 新闻公告实体
	 */
	public void saveNews(News news);
	/**
	 * 
	 * 保存新闻公告接收人 <br>
	 * create_date: Aug 6, 2010,1:46:29 PM <br>
	 * @param rnewLim 新闻接收人实体
	 */
	public void save(RNewLim rnewLim);
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
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @return List 返回新闻公告列表 
	 */
	public List getAllNews(int pageCurrentNo, int pageSize,String orderItem,String isDe,Long empId);
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

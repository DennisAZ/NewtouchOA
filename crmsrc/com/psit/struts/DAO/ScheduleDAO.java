package com.psit.struts.DAO;

import java.util.Date;
import java.util.List;

import com.psit.struts.entity.SalOpp;
import com.psit.struts.entity.Schedule;

/**
 * 日程安排数据库表操作的接口类 <br>
 * create_date: Aug 12, 2010,3:54:38 PM<br>
 * @author 朱皖宁
 */
public interface ScheduleDAO  
{
	/**
	 * 保存日程内容<br>
	 * create_date: Aug 12, 2010,3:55:10 PM<br>
	 * @param schedule 日程安排对象<br>
	 */
	public void save(Schedule schedule);
	/**
	 * 获得当天，七天内的日程安排<br>
	 * create_date: Aug 12, 2010,3:55:36 PM<br>
	 * @param startDate 日程日期开始
	 * @param endDate 日程日期结束
	 * @param empId 用户id
	 * @return List 日程安排列表<br>
	 */
	public List getSchByDate(Date startDate,Date endDate,Long empId);
	/**
	 * 获得当天，七天内,三十天内的日程安排数量<br>
	 * create_date: Aug 12, 2010,3:57:26 PM<br>
	 * @param startDate 日程日期开始
	 * @param endDate 日程日期结束
	 * @param empId 用户id
	 * @return int 日程安排数量<br>
	 */
	public int getSchByDateCount(Date startDate,Date endDate,Long empId);
	/**
	 * 根据Id获得日程安排<br>
	 * create_date: Aug 12, 2010,3:58:03 PM<br>
	 * @param id 日程安排id
	 * @return Schedule 日程安排对象<br>
	 */
	public Schedule showSchedule(Long id);
	/**
	 * 更新日程安排信息<br>
	 * create_date: Aug 12, 2010,3:58:40 PM<br>
	 * @param schedule 日程安排对象<br>
	 */
	public void update(Schedule schedule);
	/**
	 * 删除日程安排<br>
	 * create_date: Aug 12, 2010,3:58:57 PM<br>
	 * @param schedule 日程安排对象<br>
	 */
	public void delSchedule(Schedule schedule);
	/**
	 * 查询日程<br>
	 * create_date: Aug 12, 2010,3:59:12 PM<br>
	 * @param pageCurrentNo 当前页码
	 * @param pageSize 每页显示的记录数
	 * @param startDate 日程日期开始
	 * @param endDate 日程日期结束
	 * @param empId 用户id
	 * @param isAll 日程状态（1：未完成）
	 * @return List (如果是action方法需注明跳转的一个或多个映射名)<br>
	 */
	public List getSchList(String [] args, String orderItem, String isDe,int pageCurrentNo, int pageSize);
	/**
	 * 查询日程数量<br>
	 * create_date: Aug 12, 2010,4:00:44 PM<br>
	 * @param args[0] 日程日期开始
	 * @param args[1] 日程日期结束
	 * @param args[2] 用户id
	 * @param args[3] 日程状态（1：未完成）
	 * @return int 日程数量<br>
	 */
	public int getSchListCount(String [] args);
}
package com.psit.struts.DAO;



import java.util.Date;
import java.util.List;

import com.psit.struts.entity.CusServ;
import com.psit.struts.entity.SalPra;
/**
 * 
 * 客户服务DAO <br>
 *
 * create_date: Aug 13, 2010,4:48:37 PM<br>
 * @author zjr
 */
public interface CusServDAO 
{
	/**
	 * 
	 * 保存客户服务信息 <br>
	 * create_date: Aug 9, 2010,10:40:30 AM <br>
	 * @param cusServ 客户服务实体
	 */
	public void save(CusServ cusServ);
	/**
	 * 
	 * 根据客户服务编号获取客户服务 <br>
	 * create_date: Aug 9, 2010,10:41:49 AM <br>
	 * @param serCode 客户服务id
	 * @return CusServ  返回客户服务实体 
	 */
	public CusServ showCusServ(Long serCode);
	/**
	 * 更新客户服务信息
	 * method_name update
	 * create_date Aug 20, 20099:59:35 AM
	 * @param cusServ
	 */
	public void update(CusServ cusServ);
	 /**
	  * 
	  * 删除客户服务 <br>
	  * create_date: Aug 9, 2010,10:55:33 AM <br>
	  * @param cusServ 客户服务实体
	  */
	 public void delServ(CusServ cusServ);
	 /**
	  * 
	  * 获得待删除的所有客户服务 <br>
	  * create_date: Aug 9, 2010,11:14:08 AM <br>
	  * @param pageCurrentNo 当前页数
	  * @param pageSize 每页数量
	  * @return List 返回客户服务列表 
	  */
	 public List findDelServ(int pageCurrentNo, int pageSize);
	 /**
	  * 
	  * 获得待删除的所有客户服务数量 <br>
	  * create_date: Aug 9, 2010,11:14:37 AM <br>
	  * @return int 返回客户服务列表数量
	  */
	 public int findDelServCount();
	 /**
	  * 
	  * 获得某客户下的所有客户服务 <br>
	  * create_date: Aug 9, 2010,11:33:42 AM <br>
	  * @param cusCode 客户id
	  * @param orderItem 排序字段
	  * @param isDe 是否逆序
	  * @param currentPage 当前页数
	  * @param pageSize 每页数量
	  * @return List 返回客户服务列表
	  */
	 public List getCusServ(Long cusCode,String orderItem, String isDe,int currentPage,int pageSize);
	 /**
	  * 
	  * 获得某客户下的所有客户服务数量 <br>
	  * create_date: Aug 9, 2010,11:34:13 AM <br>
	  * @param cusCode 客户id
	  * @return int 返回客户服务列表数量
	  */
	 public int getCusServCount(Long cusCode); 
	 
	 /**
	  * 获得客户数量<br>
	  * create_date: Jan 29, 2011,4:28:10 PM <br>
	  * @param args
	  *			    args[0]: 是否删除
	  * 			args[1]: 0:待处理 2：已处理
	  * 			args[2]: 主题
	  * 			args[3]: 客户Id
	  * 			args[4]: 状态
	  * 			args[5]: 关怀方式
	  * 			args[6]: 开始执行日期
	  * 			args[7]: 终止执行日期
	  * @return int 客户数量<br>
	  * 		attribute > (attributeName:注释)（没有setAttribute删除此条注释）
	  */
	 public int getServCount(String [] args);
	 
	 /**
	  * 获得客户<br>
	  * create_date: Jan 29, 2011,5:56:29 PM <br>
	  * @param args
	  *			    args[0]: 是否删除
	  * 			args[1]: 0:待处理 2：已处理
	  * 			args[2]: 主题
	  * 			args[3]: 客户Id
	  * 			args[4]: 状态
	  * 			args[5]: 关怀方式
	  * 			args[6]: 开始执行日期
	  * 			args[7]: 终止执行日期
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	  * @return List<CusServ> (如果是action方法需注明跳转的一个或多个映射名)<br>
	  * 		attribute > (attributeName:注释)（没有setAttribute删除此条注释）
	  */
	 public List<CusServ> getServ(String[]args, String orderItem,String isDe, int currentPage, int pageSize);
}
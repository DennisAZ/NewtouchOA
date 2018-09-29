package com.psit.struts.BIZ;

import java.util.Date;
import java.util.List;

import com.psit.struts.entity.CusServ;
import com.psit.struts.entity.Quote;
import com.psit.struts.entity.RQuoPro;
import com.psit.struts.entity.SalOpp;
import com.psit.struts.entity.SalPra;
/**
 * 客户管理BIZ <br>
 */
public interface CusServBIZ {
//销售机会
	/**
	 * 销售机会列表 <br>
	 * @param args 	[0]是否删除(1未删除,0已删除);  [1]范围(0自己,1全部);  [2]用户码(userNum);  
	 * 				[3]机会主题;  [4]客户ID;  [5]机会热度;  [6]机会状态;  [7]负责人;  
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	 * @return List<SalOpp> 机会列表
	 */
	public List<SalOpp> listSalOpps(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listSalOppsCount(String[]args);
	/**
	 * 获得最近销售机会<br>
	 * @param pageCurrentNo 当前页码
	 * @param pageSize 每页显示的记录数
	 * @param startDate 销售机会提醒开始时间
	 * @param endDate 销售机会提醒结束时间
	 * @param range 标识查询范围0表示查询我的销售机会1表示查询我的下属销售机会2表示查询全部销售机会
	 * @param userCode 账号
	 * @param userNum 用户id及其下属
	 * @return List 销售机会记录列表<br>
	 */
	public List getOppByExeDate(int pageCurrentNo, int pageSize,Date startDate,Date endDate,String range,String seNo);
	public int getOppByExeDateCount(Date startDate,Date endDate,String range,String seNo);
	/**
	  * 获得待删除的所有销售机会 <br>
	  * @param pageCurrentNo 当前页数
	  * @param pageSize 每页数量
	  * @return List 返回销售机会列表 <br> 
	  */
	 public List findDelOpp(int pageCurrentNo, int pageSize);
	 public int findDelOppCount();
	 /**
	 * 获得客户下的所有销售机会 <br>
	 * @param cusCode 客户id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回销售机会列表
	 */
	 public List getCusOpp(Long cusCode,String orderItem, String isDe, int currentPage,int pageSize);
	 public int getCusOppCount(Long cusCode); 
	 /**
	  * 下拉列表加载客户下的销售机会（无分页）
	  * @param cusCode 客户id
	  * @return List 返回销售机会列表
	  */
	 public List getOppByCusCode(Long cusCode);
	/**
	 * 保存销售机会信息 <br>
	 * @param salOpp 销售机会实体 <br>
	 */
	public void save(SalOpp salOpp);
	/**
	 * 根据Id获取销售机会(带状态) <br>
	 * @param id 销售机会id
	 * @return SalOpp 返回销售机会实体 <br>
	 */
	public SalOpp showSalOpp(Long id);
	 /**
	  * 根据id获得销售机会 <br>
	  * @param id 销售机会id
	  * @return SalOpp 销售机会实体 <br>
	  */
	 public SalOpp getSalOpp(Long id);
	/**
	 * 更新销售机会信息 <br>
	 * @param salOpp 销售机会实体 <br>
	 */
	public void update(SalOpp salOpp);
	 /**
	  * 删除销售机会 <br>
	  * @param salOpp 销售机会实体 <br>
	  */
	 public void delOpp(SalOpp salOpp);
//机会报价(未使用)
	 /**
	  * 获得待删除的所有报价记录 <br>
	  * @param pageCurrentNo 当前页数
	  * @param pageSize 每页数量
	  * @return List 返回销售机会列表数量 <br> 
	  */
	 public List findDelQuote(int pageCurrentNo, int pageSize);
	 public int findDelQuoteCount();
	 /**
	  * 保存报价信息 <br>
	  * @param quote 报价实体 <br>
	  */
	 public void saveQuote(Quote quote);
	 /**
	  * 根据Id获得报价 <br>
	  * @param id 报价id
	  * @return Quote 返回报价实体 <br> 
	  */
	 public Quote showQuote(Long id);
	 /**
	  * 更新报价信息 <br>
	  * @param quote 报价实体 <br>
	  */
	 public void updateQuo(Quote quote);
	 /**
	  * 删除机会报价 <br>
	  * @param quote 报价实体 <br>
	  */
	 public void delQuote(Quote quote);
	/**
	 * 根据报价记录删除报价明细 <br>
	 * @param quoId 报价id <br>
	 */
	public void delByQuo(Long quoId);
	/**
	 * 保存报价明细 <br>
	 * @param rup 报价明细实体 <br>
	 */
	public void saveRup(RQuoPro rup);
	
	
//来往记录
	/**
	 * 来往记录列表<br>
	 * @param args
	  *            0：是否删除
	  *            1：0:我的客户 2：全部客户
	  *            2：负责人Id
	  *            3：客户Id
	  *            4：摘要
	  *            5：状态
	  *            6：方式
	  *            7：执行日期（起始）
	  *            8：执行日期（终止）
	  *            9：负责账号
	  *            10：过滤器
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	 * @return List<SalOpp> 机会列表
	 */
	public List<SalPra> listSalPras(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listSalPrasCount(String[]args);
	 /**
	  * 获得最近来往记录<br>
	  * @param startDate 执行日期开始
	  * @param endDate 执行日期结束
	  * @param range 标识查询范围0表示查询我的来往记录1表示查询我的下属来往记录2表示查询全部来往记录
	  * @param userCode 账号
	  * @param userNum 用户id及其下属
	  * @return List 来往记录列表<br>
	  */
	 public List getPraByExeDate(Date startDate,Date endDate,String range,String seNo);
	 public int getPraByExeDateCount(Date startDate,Date endDate,String range,String seNo);
	 /**
	  * 获得待删除的所有来往记录 <br>
	  * @param pageCurrentNo 当前页数
	  * @param pageSize 每页数量
	  * @return List 返回来往记录列表 <br> 
	  */
	 public List findDelPra(int pageCurrentNo, int pageSize);
	 public int findDelPraCount();
	 /**
	  * 获得客户下的所有来往记录 <br>
	  * @param cusCode 客户id 
	  * @param orderItem 排序字段
	  * @param isDe 是否逆序
	  * @param currentPage 当前页数
	  * @param pageSize 每页数量
	  * @return List 返回来往记录列表 <br>
	  */
	 public List getCusPra(Long cusCode,String orderItem, String isDe,int currentPage,int pageSize);
	 public int getCusPraCount(Long cusCode); 
	/**
	 * 保存来往记录信息 <br>
	 * @param salPra 来往记录实体 <br>
	 */
	public void save(SalPra salPra);
	/**
	 * 根据Id获取来往记录 <br>
	 * @param id 来往记录id
	 * @return SalPra 返回来往记录实体  <br>
	 */
	public SalPra showSalPra(Long id);
	/**
	 * 更新来往记录信息 <br>
	 * @param salPra 来往记录实体 <br>
	 */
	public void update(SalPra salPra);
	/**
	 * 删除来往记录 <br>
	 * @param salPra 来往记录实体 <br>
	 */
	public void delPra(SalPra salPra);
	
//客户服务
	/**
	  * 客服列表<br>
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
	 public int getServCount(String [] args);
	 /**
	  * 获得待删除的所有客户服务 <br>
	  * @param pageCurrentNo 当前页数
	  * @param pageSize 每页数量
	  * @return List 返回客户服务列表 <br> 
	  */
	 public List findDelServ(int pageCurrentNo, int pageSize);
	 public int findDelServCount();
	 /**
	  * 获得客户下的所有客户服务 <br>
	  * @param cusCode 客户id
	  * @param orderItem 排序字段
	  * @param isDe 是否逆序
	  * @param currentPage 当前页数
	  * @param pageSize 每页数量
	  * @return List 返回客户服务列表 <br>
	  */
	 public List getCusServ(Long cusCode,String orderItem, String isDe,int currentPage,int pageSize);
	 public int getCusServCount(Long cusCode); 
	/**
	 * 保存客户服务信息 <br>
	 * @param cusServ 客户服务实体 <br>
	 */
	public void save(CusServ cusServ);
	/**
	 * 根据客户服务编号获取客户服务 <br>
	 * @param serCode 客户服务id
	 * @return CusServ  返回客户服务实体 <br> 
	 */
	public CusServ showCusServ(Long serCode);
	/**
	 * 更新客户服务信息 <br>
	 * @param cusServ 客户服务实体 <br>
	 */
	public void update(CusServ cusServ);
	 /**
	  * 删除客户服务 <br>
	  * @param cusServ 客户服务实体 <br>
	  */
	 public void delServ(CusServ cusServ);
}
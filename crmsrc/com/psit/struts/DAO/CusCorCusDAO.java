package com.psit.struts.DAO;

import java.util.Date;
import java.util.List;

import com.psit.struts.entity.CrmManagementAreaRange;
import com.psit.struts.entity.CusCorCus;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.StaTable;
import com.psit.struts.form.RecvAmtForm;
import com.psit.struts.form.StatSalMForm;
/**
 * 客户资料DAO <br>
 */
public interface CusCorCusDAO 
{
	
	/**
	 * 获取管辖区域下的所有客户信息
	 * 
	 * @return
	 */
	public List<CrmManagementAreaRange> getmyManageCrmManagementArea(long person_id);
	/**
	 * 客户资料列表 <br>
	 */
	public List<CusCorCus> listCustomers(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listCustomersCount(String[]args);
	
	/**
	 * 客户资料列表 <br>
	 */
	public List<CusCorCus> mylistCustomers(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	/**
	 * 根据区域获取客户资料列表 <br>
	 */
	public List<CusCorCus> getCustomersByArea(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int mylistCustomersCount(String[]args);

	 /**
	  * 保存客户信息 <br>
	  */
	 public void save(CusCorCus cusCorCus);

	/**
	 * 根据客户Id获得客户信息(带未删除状态) <br>
	 */
	 public CusCorCus getCusCorCusInfo(String corCode);

	/**
	 * 获得客户实体 <br>
	 */
	 public CusCorCus findCus(Long id);
	/**
	 * 更新客户信息 <br>
	 */
	 public void update(CusCorCus cusCorCus);
	/**
	 * 判断客户是否重复 <br>
	 */
	 public List<String> getCusNames(String cusName);
	/**
     * 高级搜索获得符合条件的客户(带分页) <br>
     */
	 public List<CusCorCus> supSearCus(String range,CusCorCus cusCorCus, Long indId,Date startDateCon,Date endDateCon,Date lastDateCon,
             Date startDateUpd,Date endDateUpd,Date lastDateUpd,String mark,
             String startTime,String endTime,String uCode,String orderItem,String isDe,int currentPage,int pageSize);
	public int supSearCusCount(String range,CusCorCus cusCorCus, Long indId,Date startDateCon,Date endDateCon,Date lastDateCon,
            Date startDateUpd,Date endDateUpd,Date lastDateUpd,String mark,
            String startTime,String endTime,String uCode);
	/**
	 * 获得删除状态为0的所有客户 <br>
	 */
	public List<CusCorCus> listDelCus(int pageCurrentNo, int pageSize);
	public int listDelCusCount();
	/**
     * 删除客户 <br>
     */
	public void delCusCorCus(CusCorCus cusCorCus);
	
	/**
	 * 批量分配<br>
	 */
	public void batchAssign(String ids,String seNo, String seName, String cusState, LimUser curUser);
	
	/**
	 * 导出客户<br>
	 */
	public List<CusCorCus> getOutCus(String [] args);
	
	/**
	 * 通过Id集合获得客户列表<br>
	 */
	public List<CusCorCus> getCusByIds(String ids);
	
	/**
	 * 获得高级搜索的客户列表（无分页）<br>
	 */
	public List<CusCorCus> getSupSearCus(String range,CusCorCus cusCorCus, Long indId,Date startDateCon,Date endDateCon,Date lastDateCon,
            Date startDateUpd,Date endDateUpd,Date lastDateUpd,String mark,
            String startTime,String endTime,String uCode);
	
	/**
	 * 批量导入客户<br>
	 */
	public void importCus(List[] dataList)throws Exception;
	
//----- 统计 --------
	/**
	 * 统计表格中客户列表 <br>
	 */
	public List<CusCorCus> listStatCus(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listStatCusCount(String[]args);
	
	/**
	 * 客户类型统计 <br>
	 */
	public StaTable statCusTypeByEmp(String[] args);
	/**
	 * 客户来源统计 <br>
	 */
	public StaTable statEmpCusSou(String[] args);
	/**
	 * 客户行业统计 <br>
	 */
	public StaTable statEmpCusInd(String[] args);
	
	/**
	 * 批量标色<br>
	 */
	public void batchColor(String ids, String color);
	
	/**
	 * 获得客户实体list <br>
	 */
	 public List<CusCorCus> getOnTopCusList(String state,String seNo);
	 
	/**
	 * 获得应收款信息list <br>
	 */
	 public List<RecvAmtForm> recvAmtList(String recDate);

	 /**
	  * 到期客户<br>
	  */
	 public List<StatSalMForm> onDateCusList(String[] args);
	 
	/**
	 * 销售提成分析 <br>
	 * @param args 0:发货日期开始  1 ：发货日期结束
	 * @return StaTable 统计结果
	 */
	public StaTable statSaleAnalyse(String[] args);
		
	
	/**
	 * 获得所有客户 <br>
	 * @return List 返回客户列表
	 */
	public List<CusCorCus> listAllCus();
	
	/**
	 * 应收款分析<br>
	 * @param args args[0]:到期日期 ; args[1]:时间范围
	 * @param orderItem 排序字段
	 * @param isDe 是否降序
	 * @return List<CusCorCus> (如果是action方法需注明跳转的一个或多个映射名)<br>

	 */
	public List <CusCorCus> statRecvAnalyse(String[] args, String orderItem, String isDe);
}
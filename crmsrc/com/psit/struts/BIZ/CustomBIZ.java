package com.psit.struts.BIZ;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.psit.struts.entity.CrmManagementAreaRange;
import com.psit.struts.entity.CusAddr;
import com.psit.struts.entity.CusArea;
import com.psit.struts.entity.CusCity;
import com.psit.struts.entity.CusContact;
import com.psit.struts.entity.CusCorCus;
import com.psit.struts.entity.CusProd;
import com.psit.struts.entity.CusProvince;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.MemDate;
import com.psit.struts.entity.StaTable;
import com.psit.struts.form.RecvAmtForm;
import com.psit.struts.form.StatSalMForm;
/**
 * 客户管理BIZ <br>
 */
public interface CustomBIZ {
	public Double getCusToPaid(String cusId);
	/**
	 * 客户资料列表 <br>
	 * @param args 	[0]是否删除(1未删除,0已删除);  [1]范围(0自己,1全部);  [2]员工Id(seNo);  [3]客户名称;  
	 * 				[4]客户编号;  [5]客户类型;  [6][7]创建时间;  [8]负责人; [9]地址; [10]行业; 
	 * 				[11]:filter:列表筛选类型;	[12]:标色;	[13]客户状态;
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	 * @return List<CusCorCus> 客户资料列表
	 */
	public List<CusCorCus> listCustomers(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listCustomersCount(String[]args);
	/**
	 * 客户资料列表 <br>
	 * @param args 	[0]是否删除(1未删除,0已删除);  [1]范围(0自己,1全部);  [2]员工Id(seNo);  [3]客户名称;  
	 * 				[4]客户编号;  [5]客户类型;  [6][7]创建时间;  [8]负责人; [9]地址; [10]行业; 
	 * 				[11]:filter:列表筛选类型;	[12]:标色;	[13]客户状态;
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	 * @return List<CusCorCus> 客户资料列表
	 */
	public List<CusCorCus> mylistCustomers(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	/**
	 * 根据区域获取客户资料列表 <br>
	 * @param args 	[0]是否删除(1未删除,0已删除);  [1]范围(0自己,1全部);  [2]员工Id(seNo);  [3]客户名称;  
	 * 				[4]客户编号;  [5]客户类型;  [6][7]创建时间;  [8]负责人; [9]地址; [10]行业; 
	 * 				[11]:filter:列表筛选类型;	[12]:标色;	[13]客户状态;
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	 * @return List<CusCorCus> 客户资料列表
	 */
	public List<CusCorCus> getCustomersByArea(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int mylistCustomersCount(String[]args);
	/**
	 * 获得删除状态为0的所有客户 <br>
	 * @param pageSize 每页数量
	 * @return List 返回客户列表
	 */
	public List<CusCorCus> listDelCus(int pageCurrentNo, int pageSize);
	public int listDelCusCount();
	public void uploadCus();
    /**
     * 高级搜索获得符合条件的客户(带分页) <br>
     * @param range 0:我的客户  1：全部客户
     * @param cusCorCus 客户实体
	 * @param indId 行业id
	 * @param startDateCon 联系日期(开始)
	 * @param endDateCon 联系日期(结束)
	 * @param lastDateCon 联系日期
	 * @param startDateUpd 更新日期(开始)
	 * @param endDateUpd 更新日期(结束)
	 * @param lastDateUpd 最近更新日期
	 * @param mark 联系标记
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
     * @param uCode 负责账号
     * @param orderItem 排序字段
     * @param isDe 是否逆序
     * @param currentPage 当前页数
     * @param pageSize 每页数量
     * @return List 返回客户列表
     */
	 public List<CusCorCus> supSearCus(String range,CusCorCus cusCorCus, Long indId,Date startDateCon,Date endDateCon,Date lastDateCon,
            Date startDateUpd,Date endDateUpd,Date lastDateUpd,String mark,
            String startTime,String endTime,String uCode,String orderItem, String isDe,int currentPage,int pageSize);
	public int supSearCusCount(String range,CusCorCus cusCorCus, Long indId,Date startDateCon,Date endDateCon,Date lastDateCon,
           Date startDateUpd,Date endDateUpd,Date lastDateUpd,String mark,
           String startTime,String endTime,String uCode);
	/**
	 * 通过Id集合获得客户列表<br>
	 * @param ids 客户Id
	 * @return
	 */
	public List<CusCorCus> getCusByIds(String ids);
	
	/**
	 * 获得高级查询的客户列表（无分页）<br>
     * @param range 0：我的客户 1：全部客户
     * @param cusCorCus 客户实体
	 * @param indId 行业id
	 * @param startDateCon 联系日期(开始)
	 * @param endDateCon 联系日期(结束)
	 * @param lastDateCon 联系日期
	 * @param startDateUpd 更新日期(开始)
	 * @param endDateUpd 更新日期(结束)
	 * @param lastDateUpd 最近更新日期
	 * @param mark 联系标记
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
     * @param uCode 负责账号
     * @return List 返回客户列表
	 */
	public List<CusCorCus> getSupSearCus(String range,CusCorCus cusCorCus, Long indId,Date startDateCon,Date endDateCon,Date lastDateCon,
            Date startDateUpd,Date endDateUpd,Date lastDateUpd,String mark,
            String startTime,String endTime,String uCode);
	 
	/**
	 * 判断客户是否重复 <br>
	 * @param corName1 客户名称
	 * @return boolean 重复返回true,不重复返回false
	 */
	public boolean checkCus(String corName1);
	/**
	 * 保存客户信息 <br>
	 * @param cusCorCus 客户实体
	 */
	public void save(CusCorCus cusCorCus);
	/**
	 * 根据客户Id获得客户信息(带未删除状态) <br>
	 * @param corCode 客户id
	 * @return CusCorCus 返回客户实体 
	 */
	public CusCorCus getCusCorCusInfo(String corCode);
	/**
	 * 获得客户实体 <br>
	 * @param id 客户id
	 * @return CusCorCus 返回客户实体 
	 */
	public CusCorCus findCus(Long id);
	/**
	 * 更新客户信息 <br>
	 * @param cusCorCus 客户实体
	 */
	public void update(CusCorCus cusCorCus);
	/**
     * 删除客户 <br>
     * @param cusCorCus 客户实体
     */
	public void delCusCorCus(CusCorCus cusCorCus);
	
	
	/**
	 * 联系人列表 <br>
	 * @param args 	[0]是否删除(1未删除,0已删除);  [1]范围(0自己,1全部);  [2]负责人(seNo);  
	 * 				[3]联系人名称;  [4]客户ID;  [5]联系人分类;  [6]负责人;  
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	 * @return List<CusContact> 联系人列表
	 */
	public List<CusContact> listContacts(String [] args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listContactsCount(String[]args);
	
	/**
	 * 客户联系人列表 <br>
	 * @param args 	[0]客户Id;  [1]姓名;   
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	 * @return List<CusContact> 联系人列表
	 */
	public List<CusContact> listCusContacts(String [] args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listCusContactsCount(String[]args);
	
	/**
	 * 获得最近关注的联系人 <br>
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @param range 标识查询范围0表示查询我的客户1表示查询我的下属客户
	 * @param userCode 账号
	 * @param userNum 用户码
	 * @return List 返回联系人列表
	 */
	 public List getContactByBirth(Date startDate,Date endDate,String range,String seNo);
	 public int getContactByBirthCount(Date startDate,Date endDate,String range,String seNo);
	 
	 public List<CusContact> getAllConByMark(String cusId);
	 /**
     * 获得某客户下的所有联系人 <br>
     * @param cusCode 客户id
     * @param orderItem 排序字段
     * @param isDe 是否逆序
     * @param currentPage 当前页数
     * @param pageSize 每页数量
     * @return List 返回联系人列表 
     */
	 public List getCusCon(String cusCode,String orderItem, String isDe, int currentPage,int pageSize);
	 public int getCusConCount(String cusCode);
	/**
	 * 通过Id获得联系人列表<br>
	 * @param ids 联系人Id
	 * @return List<CusContact> 联系人列表<br>
	 */
	public List<CusContact> getContByIds(String ids);
	 /**
	 * 获得删除状态为0的所有联系人 <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回联系人列表
	 */
//	public List findDelContact(int pageCurrentNo, int pageSize);
//	public int findDelContactCount();
	/**
	 * 保存客户联系人信息 <br>
	 * @param cusContact 联系人实体
	 */
	public void save(CusContact cusContact);
	/**
	 * 根据联系人Id获取联系人 <br>
	 * @param id 联系人Id
	 * @return CusContact 返回联系人实体 
	 */
	public CusContact showContact(Long id);
	public CusProd showCusProd(Long id);
	/**
	 * 更新联系人信息 <br>
	 * @param cusContact 联系人实体
	 */
	public void updateContact(CusContact cusContact);

	/**
	 * 删除联系人 <br>
	 * @param cusContact 联系人实体
	 */
	public void delContact(CusContact cusContact);
	
	/**
	 * 保存纪念日 <br>
	 */
	public void saveMemDate(MemDate memDate);
	
	/**
	 * 更新纪念日
	 */
	public void updMemDate(MemDate memDate);
	
	/**
	 * 删除纪念日
	 */
	public void delMemDate(String mdId);
	
	/**
	 * 纪念日详情 <br>
	 * @param meId
	 * @return MemDate
	 */
	public MemDate getMemDate(String mdId);
	
	/**
	 * 纪念日列表 <br>
	 * @param conId	联系人ID
	 * @param orderItem
	 * @param isDe
	 * @param currentPage
	 * @param pageSize
	 * @return List<MemDate>
	 */
	public List<MemDate> listMemDateByCon(String conId, String orderItem, String isDe, int currentPage, int pageSize);
	public int listMemDateByConCount(String conId);

	/**
	 * 获得已启用的国家 <br>
	 * @return List 返回国家列表 
	 */
	public List getCusArea();
	/**
	 * 得到国家实体 <br>
	 * @param id 国家id
	 * @return CusArea 返回国家实体 
	 */
	public CusArea showCountry(long id);
	/**
	 * 获得已启用的省 <br>
	 * @param id 省份id
	 * @return List 返回省份列表 
	 */
	public List getCusProvince(long id);
	
	/**
	 * 获取我自己管辖范围内的省份
	 * @param id
	 * @return
	 */
	public List<java.lang.Long> getmyManageProvince(long id);
	/**
	 * 获取我自己管辖范围内的省份名称
	 * @param priv_id 省份Id
	 * @return
	 */
	public List<CusProvince> getmyManageProvinceName(long priv_id);
	
	/**
	 * 获取我自己管辖省份范围内的城市
	 * @param id
	 * @return
	 */
	public List<java.lang.Long> getmyManageCity(long id);
	
	/**
	 * 获取我自己管辖范围内的城市名称
	 * @param city_id 城市Id
	 * @return
	 */
	public List<CusCity> getmyManageCityName(long city_id);
	
	/**
	 * 获取管辖区域下的所有客户信息
	 * 
	 * @return
	 */
	public List<CrmManagementAreaRange> getmyManageCrmManagementArea(long person_id);
	/**
	 * 得到省份实体 <br>
	 * @param id 省份id
	 * @return CusProvince 返回省份实体 
	 */
	public CusProvince showProvince(long id);
	
	/**
	 * 得到城市实体 <br>
	 * @param id 城市id
	 * @return CusCity 返回城市实体<br>
	 */
	public CusCity showCity(long id);
	/**
	 * 获得已启用的城市 <br>
	 * @param id 城市id
	 * @return List 返回城市列表 
	 */
	public List getCusCity(long id);
	
	/**
	 * 保存国家信息 <br>
	 * @param cusArea 国家实体
	 */
	public void saveCountry(CusArea cusArea);
	/**
	 * 获得所有国家 <br>
	 * @return List 返回国家列表
	 */
	public List getAllCusArea();
	/**
	 * 保存省份信息 <br>
	 * @param cusProvince1 省份实体
	 */
	public void saveProvince(CusProvince cusProvince1);
	/**
	 * 获取所有省份 <br>
	 * @return List 返回省份列表 
	 */
	public List getAllProvince();
	/**
	 * 保存城市 <br>
	 * @param cusCity1 城市实体
	 */
	public void saveCity(CusCity cusCity1);
	/**
	 * 获得所有城市 <br>
	 * @return List 返回城市列表
	 */
	public List getAllCity();

	/**
	 * 从excel导入客户<br>
	 */
	public String importCus(HttpServletRequest request);

	/**
	 * 批量分配客户 <br>
	 * @param ids		客户ID集合
	 * @param cusState	客户状态
	 * @param seNo		负责人ID
	 * @param curUser	当前帐号
	 */
	public void batchAssignCus(String ids, String cusState, String seNo, LimUser curUesr);
	
	/**
	 * 批量分配联系人 <br>
	 * @param ids	联系人ID集合
	 * @param seNo	负责人ID
	 */
	public void batchAssignCon(String ids, String seNo);
	
	/**
	 * 批量标色<br>
	 * @param ids 客户Ids
	 * @param color 颜色
	 */
	public void batchColor(String ids, String color);
	
	public List<CusProd> listCusProd(String cusId);
	
	/**
	 * 获得客户产品列表<br>
	 * @param args
	 * 			args[0]:是否删除
	 * 			args[1]:客户Id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return  客户产品列表
	 */
	public List<CusProd> listCusProd(String[] args, String orderItem,
			String isDe, int currentPage, int pageSize);
	
	/**
	 * 获得客户产品数量<br>
	 * @param args
	 * 			args[0]:是否删除
	 * 			args[1]:客户Id
	 * @return count: 户产品数量
	 */
	public int listCusProdCount(String[] args);
	
	/**
	 * 
	 * 保存客户产品<br> 
	 */
	public void saveCusProd(CusProd cusProd);
	
	/**
	 * 通过Id查找客户产品<br>
	 * @param cpId 客户产品Id
	 * @return CusProd 客户产品<br>
	 */
	public CusProd findCusProdById(Long cpId);
	
	/**
	 * 更新客户产品<br>
	 * @param cusProd 客户产品<br>
	 */
	public void updateCusProd(CusProd cusProd);
	
	/**
	 * 删除客户产品<br>
	 * @param persistentInstance 客户产品<br>
	 */
	public void delete(CusProd persistentInstance);
	
	public List<CusAddr> listCusAddr(String cusId);
	
	/**
	 * 获得客户地址列表<br>
	 * @param cusId 客户Id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return  客户地址列表
	 */
	public List<CusAddr> listCusAddr(String cusId, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listCusAddrCount(String cusId);
	
	/**
	 * 保存客户地址<br> 
	 */
	public void saveCusAddr(CusAddr cusAddr);
	
	/**
	 * 更新客户地址<br>
	 * @param cusAddr 客户地址<br>
	 */
	public void updateCusAddr(CusAddr cusAddr);
	/**
	 * 通过Id查找客户地址<br>
	 * @param cadId 客户地址Id
	 * @return CusAddr 客户地址<br>
	 */
	public CusAddr findCusAddrById(Long cadId);
	
	/**
	 * 删除客户地址<br>
	 * @param cusAddr 客户地址<br>
	 */
	public void deleteCusAddr(CusAddr cusAddr);
	
//-------- 统计 ----------
	/**
	 * 统计表格中客户列表 <br>
	 * @param args 	[0]是否删除(1未删除,0已删除);  [1]员工ID;  [2]统计类型; [3]统计对象; [4]员工ID集合;
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	 * @return List<CusCorCus> 客户资料列表
	 */
	public List<CusCorCus> listStatCus(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listStatCusCount(String[]args);
		
	/**
	 * 客户类型统计 <br>
	 * @param args 0:用户ID集合
	 * @return StaTable 统计结果
	 */
	public StaTable statCusTypeByEmp(String[] args);
	/**
	 * 客户来源统计 <br>
	 * @param args 0:用户ID集合
	 * @return StaTable 统计结果
	 */
	public StaTable statEmpCusSou(String[] args);
	/**
	 * 客户行业统计 <br>
	 * @param args 0:用户ID集合
	 * @return StaTable 统计结果
	 */
	public StaTable statEmpCusInd(String[] args);
	
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
	 * 获得所有客户 <br>
	 * @return List 返回客户列表
	 */
	public List<CusCorCus> listAllCus();
		
	/**
	 * 销售提成分析 <br>
	 * @param args 0:发货日期开始  1 ：发货日期结束
	 * @return StaTable 统计结果
	 */
	public StaTable statSaleAnalyse(String[] args);
	 
	/**
	 * 应收款分析<br>
	 * @param args args[0]:到期日期 ; args[1]:时间范围
	 * @param orderItem 排序字段
	 * @param isDe 是否降序
	 * @return List<CusCorCus> (如果是action方法需注明跳转的一个或多个映射名)<br>

	 */
	public List <CusCorCus> statRecvAnalyse(String[] args, String orderItem, String isDe);
	
	/**
	 * 工作台提醒 <br>
	 */
	public List<CusProd> getDeskTip();
	 
//-------- 导出 ----------
	public String[][][] getOutCus(String type, String cusIds, String dataCols, String colNames, String[] args);
	public String[][][] getOutCon(String type, String cusIds, String dataCols, String colNames, String[] args);
}
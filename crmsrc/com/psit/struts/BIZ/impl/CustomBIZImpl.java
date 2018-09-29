package com.psit.struts.BIZ.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.psit.struts.util.CrmCustExcelReader;
import com.psit.struts.util.format.GetDate;
import com.psit.struts.util.format.StringFormat;
import com.psit.struts.util.file.importer.ExcelToCus;
import com.psit.struts.util.file.importer.ImporterManager;

import com.psit.struts.BIZ.CustomBIZ;
import com.psit.struts.DAO.CusAddrDAO;
import com.psit.struts.DAO.CusAreaDAO;
import com.psit.struts.DAO.CusCityDAO;
import com.psit.struts.DAO.CusContactDAO;
import com.psit.struts.DAO.CusCorCusDAO;
import com.psit.struts.DAO.CusProdDAO;
import com.psit.struts.DAO.CusProvinceDAO;
import com.psit.struts.DAO.LimUserDAO;
import com.psit.struts.DAO.MemDateDAO;
import com.psit.struts.DAO.ProdOutDAO;
import com.psit.struts.DAO.ProdShipmentDAO;
import com.psit.struts.DAO.SalEmpDAO;
import com.psit.struts.DAO.SalOrderDAO;
import com.psit.struts.DAO.SalOrgDAO;
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
import com.psit.struts.entity.ProdStore;
import com.psit.struts.entity.StaTable;
import com.psit.struts.entity.YHPerson;
import com.psit.struts.form.RecvAmtForm;
import com.psit.struts.form.StatSalMForm;

/**
 * 客户管理BIZ实现类 <br>
 */
public class CustomBIZImpl implements CustomBIZ {
	CusCorCusDAO cusCorCusDAO=null;
	CusContactDAO cusContactDAO=null;
	CusAreaDAO cusAreaDAO=null;
	CusProvinceDAO cusProvince=null;
	CusCityDAO cusCity=null;
	LimUserDAO limUserDAO=null;
	SalOrgDAO salOrgDAO=null;
	CusProdDAO cusProdDAO = null;
	MemDateDAO memDateDAO = null;
	SalEmpDAO empDAO = null;
	CusAddrDAO cusAddrDAO = null;
	SalOrderDAO salOrderDAO = null;
	ProdShipmentDAO prodShipmentDAO = null;
	ProdOutDAO prodOutDAO = null;
	

	
	public List<CrmManagementAreaRange> getmyManageCrmManagementArea(long person_id) {
		return cusCorCusDAO.getmyManageCrmManagementArea(person_id);
	}
	
	public Double getCusToPaid(String cusId){
		Double paidSum = 0.0, shipAmtSum = 0.0, toPaid = 0.0;
		paidSum = salOrderDAO.getOrdPaidByCus(cusId);
		shipAmtSum = prodShipmentDAO.getShipAmtByCus(cusId);
		toPaid = (shipAmtSum!=null?shipAmtSum:0)-(paidSum!=null?paidSum:0);
		if(toPaid<0){ toPaid=0.0; }
		return toPaid;
	}
	
	/**
	 * 客户资料列表 <br>
	 */
	public List<CusCorCus> listCustomers(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		return cusCorCusDAO.listCustomers(args, orderItem, isDe, currentPage, pageSize);
	}
	public int listCustomersCount(String[]args){
		return cusCorCusDAO.listCustomersCount(args);
	}
	
	public List<CusCorCus> mylistCustomers(String[] args, String orderItem,
			String isDe, int currentPage, int pageSize) {
		return cusCorCusDAO.mylistCustomers(args, orderItem, isDe, currentPage, pageSize);
	}
	
	public List<CusCorCus> getCustomersByArea(String[] args, String orderItem,
			String isDe, int currentPage, int pageSize) {
		return cusCorCusDAO.getCustomersByArea(args, orderItem, isDe, currentPage, pageSize);
	}

	public int mylistCustomersCount(String[] args) {
		return cusCorCusDAO.mylistCustomersCount(args);
	}
	/**
	 * 通过Id集合获得客户列表<br>
	 */
	public List<CusCorCus> getCusByIds(String ids){
		return cusCorCusDAO.getCusByIds(ids);
	}
	/**
	 * 获得高级查询的客户列表（无分页）<br>
	 */
	public List<CusCorCus> getSupSearCus(String range,CusCorCus cusCorCus, Long indId,Date startDateCon,Date endDateCon,Date lastDateCon,
            Date startDateUpd,Date endDateUpd,Date lastDateUpd,String mark,
            String startTime,String endTime,String uCode){
		return cusCorCusDAO.getSupSearCus(range, cusCorCus, indId, startDateCon, endDateCon, lastDateCon, startDateUpd, endDateUpd, lastDateUpd, mark, startTime, endTime, uCode);
	}
	/**
	 * 获得删除状态为0的所有客户 <br>
	 */
	public List<CusCorCus> listDelCus(int pageCurrentNo, int pageSize) {
		return cusCorCusDAO.listDelCus(pageCurrentNo, pageSize);
	}
	public int listDelCusCount() {
		return cusCorCusDAO.listDelCusCount();
	}
	 /**
     * 高级搜索获得符合条件的客户(带分页) <br>
     */
	public List<CusCorCus> supSearCus(String range,CusCorCus cusCorCus, Long indId, Date startDateCon,
			Date endDateCon, Date lastDateCon, Date startDateUpd,
			Date endDateUpd, Date lastDateUpd, String mark, String startTime,
			String endTime, String uCode, String orderItem,String isDe, int currentPage,
			int pageSize) {
		return cusCorCusDAO.supSearCus(range,cusCorCus, indId, startDateCon, endDateCon, lastDateCon, startDateUpd, endDateUpd, lastDateUpd, mark, startTime, endTime, uCode, orderItem,isDe, currentPage, pageSize);
	}
	public int supSearCusCount(String range,CusCorCus cusCorCus, Long indId,
			Date startDateCon, Date endDateCon, Date lastDateCon,
			Date startDateUpd, Date endDateUpd, Date lastDateUpd, String mark,
			String startTime, String endTime, String uCode) {
		return cusCorCusDAO.supSearCusCount(range,cusCorCus, indId, startDateCon, endDateCon, lastDateCon, startDateUpd, endDateUpd, lastDateUpd, mark, startTime, endTime, uCode);
	}
	/**
	 * 判断客户是否重复 <br>
	 */
	public boolean checkCus(String cusName) {
		List<String> list = cusCorCusDAO.getCusNames(cusName);
		if (list.size() > 0) {
			return true;
		} else
			return false;
	}
	/**
	 * 批量分配客户<br>
	 */
	public void batchAssignCus(String ids, String cusState, String seNo, LimUser curUser){
		String seName = (StringFormat.isEmpty(seNo)?"":empDAO.findById(Long.parseLong(seNo)).getSeName());
		cusCorCusDAO.batchAssign(ids, seNo, seName, cusState, curUser);
	}
	
	public void batchAssignCon(String ids, String seNo){
		cusContactDAO.batchAssign(ids, seNo);
	}
		
	/**
	  * 保存客户信息 <br>
	  */
	 public void save(CusCorCus cusCorCus) {
		cusCorCusDAO.save(cusCorCus);
	 }
	 /**
	 * 根据客户Id获得客户信息(带未删除状态) <br>
	 */
	public CusCorCus getCusCorCusInfo(String corCode) {
		return cusCorCusDAO.getCusCorCusInfo(corCode);
	}
	/**
	 * 获得客户实体 <br>
	 */
	public CusCorCus findCus(Long id){
		return cusCorCusDAO.findCus(id);
	}
	/**
	 * 更新客户信息 <br>
	 */
	public void update(CusCorCus cusCorCus) {
		cusCorCusDAO.update(cusCorCus);
	}
	 /**
     * 删除客户 <br>
     */
	public void delCusCorCus(CusCorCus cusCorCus) {
		cusCorCusDAO.delCusCorCus(cusCorCus);
	}
	
	/**
	 * 联系人列表 <br>
	 */
	public List<CusContact> listContacts(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		return cusContactDAO.listContacts(args, orderItem, isDe, currentPage, pageSize);
	}
	public int listContactsCount(String[]args){
		return cusContactDAO.listContactsCount(args);
	}
	
	/**
	 * 客户联系人列表 <br>
	 */
	public List<CusContact> listCusContacts(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		return cusContactDAO.listCusContacts(args, orderItem, isDe, currentPage, pageSize);
	}
	public int listCusContactsCount(String[]args){
		return cusContactDAO.listCusContactsCount(args);
	}
	/**
	 * 获得最近关注的联系人 <br>
	 */
	public List getContactByBirth(Date startDate, Date endDate,String range,String seNo) {
		return cusContactDAO.getContactByBirth(startDate,endDate,range,seNo);
	}
	public int getContactByBirthCount(Date startDate, Date endDate,
			String range,String seNo) {
		return cusContactDAO.getContactByBirthCount(startDate,endDate,range,seNo);
	}
	
	public List<CusContact> getAllConByMark(String cusId){
		return cusContactDAO.getAllConByMark(cusId);
	}
	
	/**
     * 获得某客户下的所有联系人 <br>
     */
	 public List getCusCon(String cusCode,String orderItem, String isDe, int currentPage,int pageSize){
		 return cusContactDAO.getCusCon(cusCode, orderItem, isDe, currentPage, pageSize);
	 }
	 public int getCusConCount(String cusCode){
		 return cusContactDAO.getCusConCount(cusCode);
	 }
	 /**
	 * 获得删除状态为0的所有联系人 <br>
	 */
	/*public List findDelContact(int pageCurrentNo, int pageSize) {
		return cusContactDAO.findDelContact(pageCurrentNo, pageSize);
	}
	public int findDelContactCount() {
		return cusContactDAO.findDelContactCount();
	}*/
	/**
	 * 通过Id获得联系人列表<br>
	 */
	public List<CusContact> getContByIds(String ids){
		return cusContactDAO.getContByIds(ids);
	}
	
	/**
	 * 保存客户联系人信息 <br>
	 */
	public void save(CusContact cusContact) {
		cusContactDAO.save(cusContact);
	}
	/**
	 * 根据联系人Id获取联系人 <br>
	 */
	public CusContact showContact(Long id) {
		return cusContactDAO.showContact(id);
	}
	/**
	 * 更新联系人信息 <br>
	 */
	public void updateContact(CusContact cusContact) {
		cusContactDAO.updateContact(cusContact);
	}
	/**
	 * 删除联系人 <br>
	 */
	public void delContact(CusContact cusContact) {
		cusContactDAO.delContact(cusContact);
	}
	
	/**
	 * 保存纪念日 <br>
	 */
	public void saveMemDate(MemDate memDate){
		memDateDAO.save(memDate);
	}
	
	/**
	 * 更新纪念日
	 */
	public void updMemDate(MemDate memDate){
		memDateDAO.update(memDate);
	}
	
	/**
	 * 删除纪念日
	 */
	public void delMemDate(String mdId){
		MemDate memDate = memDateDAO.findById(Long.parseLong(mdId));
		if(memDate!=null){
			memDateDAO.delete(memDate);
		}
	}
	
	/**
	 * 纪念日详情 <br>
	 * @param meId
	 * @return MemDate
	 */
	public MemDate getMemDate(String mdId){
		return memDateDAO.findById(Long.parseLong(mdId));
	}
	
	/**
	 * 纪念日列表 <br>
	 * @param conId	联系人ID
	 * @param orderItem
	 * @param isDe
	 * @param currentPage
	 * @param pageSize
	 * @return List<MemDate>
	 */
	public List<MemDate> listMemDateByCon(String conId, String orderItem, String isDe, int currentPage, int pageSize){
		return memDateDAO.listByCon(conId,orderItem,isDe,currentPage,pageSize);
	}
	public int listMemDateByConCount(String conId){
		return memDateDAO.listByConCount(conId);
	}
	
	/**
	 * 获得已启用的国家 <br>
	 */
	public List getCusArea() {
		return cusAreaDAO.getCusArea();
	}
	/**
	 * 获得已启用的城市 <br>
	 */
	public List getCusCity(long id) {
		return cusCity.getCusCity(id);
	}
	/**
	 * 获得已启用的省 <br>
	 */
	public List getCusProvince(long id) {
		return cusProvince.getCusProvince(id);
	}
	
	/**
	 * 获取自己管辖的省份
	 */
	public List<java.lang.Long> getmyManageProvince(long id) {
		return cusProvince.getMyManageCusProvince(id);
	}
	
	public List<CusProvince> getmyManageProvinceName(long privId) {
		return cusProvince.getmyManageProvinceName(privId);
	}
	
	public List<Long> getmyManageCity(long id) {
		return cusCity.getmyManageCity(id);
	}
	
	public List<CusCity> getmyManageCityName(long cityId) {
		return cusCity.getmyManageCityName(cityId);
	}
	/**
	 * 得到省份实体 <br>
	 */
	public CusProvince showProvince(long id){
		return cusProvince.showProvince(id);
	}
	/**
	 *得到城市实体 <br>
	 */
	public CusCity showCity(long id){
		return cusCity.showCity(id);
	}
	/**
	 * 得到国家实体 <br>
	 */
	public CusArea showCountry(long id){
		return cusAreaDAO.showCountry(id);
	}
	/**
	 * 获得所有国家 <br>
	 */
    public List getAllCusArea() {
		return cusAreaDAO.getAllCusArea();
	}
	/**
	 * 保存国家信息 <br>
	 */
	public void saveCountry(CusArea cusArea) {
		cusAreaDAO.saveCountry(cusArea);
		
	}
	/**
	 * 获取所有省份 <br>
	 */
   public List getAllProvince() {
		return cusProvince.getAllProvince();
	}
	/**
	 * 保存省份信息 <br>
	 */
	public void saveProvince(CusProvince cusProvince1) {
		cusProvince.saveProvince(cusProvince1);
	}
	/**
	 * 获得所有城市 <br>
	 */
	public List getAllCity() {
		return cusCity.getAllCity();
	}
	/**
	 * 保存城市 <br>
	 */
	public void saveCity(CusCity cusCity1) {
		cusCity.saveCity(cusCity1);
	}
	
	/**
	 * 批量导入银行客户 <br>
	 */
	public String importCus(HttpServletRequest request)  {
		LimUser limUser = (LimUser) request.getSession().getAttribute("limUser");
		ExcelToCus etLoanCus = new ExcelToCus(limUser,this);
		String errMsg = "";
		try {
			errMsg = ImporterManager.importExcel(request,etLoanCus);
			if(errMsg.equals("")){
				cusCorCusDAO.importCus(etLoanCus.getDataList());//保存数据
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg = e.toString();
		}
		return errMsg;
	}
	
	/**
	 * 批量标色<br>
	 * @param ids 客户Ids
	 * @param color 颜色
	 */
	public void batchColor(String ids, String color){
		cusCorCusDAO.batchColor(ids, color);
	}
	
//---------- 统计 -----------
	/**
	 * 统计表格中客户列表 <br>
	 */
	public List<CusCorCus> listStatCus(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		return cusCorCusDAO.listStatCus(args, orderItem, isDe, currentPage, pageSize);
	}
	public int listStatCusCount(String[]args){
		return cusCorCusDAO.listStatCusCount(args);
	}
	
	/**
	 * 客户类型统计 <br>
	 */
	public StaTable statCusTypeByEmp(String[] args){
		return cusCorCusDAO.statCusTypeByEmp(args);
	}
	/**
	 * 客户来源统计 <br>
	 */
	public StaTable statEmpCusSou(String[] args){
		return cusCorCusDAO.statEmpCusSou(args);
	}
	/**
	 * 客户行业统计 <br>
	 */
	public StaTable statEmpCusInd(String[] args){
		return cusCorCusDAO.statEmpCusInd(args);
	}
	
	 /**
	  * 到期客户<br>
	  */
	 public List<StatSalMForm> onDateCusList(String[] args){
		 return cusCorCusDAO.onDateCusList(args);
	 }
	 
	/**
	 * 销售提成分析 <br>
	 * @param args 0:发货日期开始  1 ：发货日期结束
	 * @return StaTable 统计结果
	 */
	public StaTable statSaleAnalyse(String[] args){
		 return cusCorCusDAO.statSaleAnalyse(args);
	}
	 
	/**
	 * 获得所有客户 <br>
	 * @return List 返回客户列表
	 */
	public List<CusCorCus> listAllCus(){
		return cusCorCusDAO.listAllCus();
	}
	/**
	 * 应收款分析<br>
	 * @param args args[0]:到期日期 ; args[1]:时间范围
	 * @param orderItem 排序字段
	 * @param isDe 是否降序
	 * @return List<CusCorCus> (如果是action方法需注明跳转的一个或多个映射名)<br>

	 */
	public List <CusCorCus> statRecvAnalyse(String[] args, String orderItem, String isDe){
		return cusCorCusDAO.statRecvAnalyse(args, orderItem, isDe);
	}
//---------- 导出 -----------	
	/**
	 * 导出客户 <br>
	 */
	public String[][][] getOutCus(String type,String casIds,String dataCols,String colNames,String[] args){
		List<CusCorCus> list = null;
		String [] colsArr = dataCols.split(",");
 		String [] colsHead = colNames.split(",");
		if(type.equals("0")){
			list = cusCorCusDAO.getOutCus(args);//批次下案件
 		}
 		else{
 			list = cusCorCusDAO.getCusByIds(casIds);//选中记录
 		}
 		
		String [] header = new String [colsArr.length];
		for(int i=0;i<colsArr.length;i++){
			header[i] = colsHead[Integer.parseInt(colsArr[i])];//生成表格表头
		}
		String [][] datas = new String [list.size()][header.length];
		for(int i=0;i<datas.length;i++){
			datas[i] = getCusRow(colsArr,list.get(i));
		}
		return new String[][][]{new String[][]{header},datas};
	}
	/**
	 * 生成表格行 <br>
	 * @param colsArr	选中导出的列序号
	 * @param cusCorCus	客户实体
	 * @return String[]
	 */
	private String[] getCusRow(String[]colsArr, CusCorCus cusCorCus){
		String[] datas = new String[colsArr.length];
		String cardType="", area="";
		if(!StringFormat.isEmpty(cusCorCus.getCorCardType())){
			switch(Integer.parseInt(cusCorCus.getCorCardType())){
			case 1: cardType = "身份证"; break;
			case 2: cardType = "营业执照"; break;
			case 3: cardType = "组织结构代码"; break;
			}
		}
		if(cusCorCus.getCusArea()==null || cusCorCus.getCusArea().getAreId()==1){
			area = "";
		 }else{
			 area = cusCorCus.getCusArea().getAreName();
		 }
		 if(cusCorCus.getCusProvince()==null || cusCorCus.getCusProvince().getPrvId()==1){
			 area += "";
		 }else{
			 area +=cusCorCus.getCusProvince().getPrvName();
		 }
		 if(cusCorCus.getCusCity()==null || cusCorCus.getCusCity().getCityId()==1){
			 area += "";
		 }else{
			 area+=cusCorCus.getCusCity().getCityName();
		 }
		for(int i=0;i<colsArr.length;i++){
			switch(Integer.parseInt(colsArr[i])){
			case  0: datas[i] = cusCorCus.getCorNum(); break;
			case  1: datas[i] = cusCorCus.getCorName();break;
			case  2: datas[i] = cusCorCus.getCorMne();break;
			case  3: datas[i] = cusCorCus.getCusIndustry()!=null?cusCorCus.getCusIndustry().getTypName():"";break;
			case  4: datas[i] = cusCorCus.getCorState()!=null?CusCorCus.getCorStateTxt(cusCorCus.getCorState()):""; break;
			case  5: datas[i] = cusCorCus.getCorSou()!=null?cusCorCus.getCorSou().getTypName():""; break;
			case  6: datas[i] = cusCorCus.getCusType()!=null?cusCorCus.getCusType().getTypName():""; break;
			case  7: datas[i] = cusCorCus.getCorHot().length()>0?(cusCorCus.getCorHot()+"星"):"";break;
			case  8: datas[i] = cusCorCus.getCorRelation();break;
			case  9: datas[i] = cusCorCus.getPerson()!= null?cusCorCus.getPerson().getUserName():"";break;
			case 10: datas[i] = cusCorCus.getCorLastDate()!=null?GetDate.parseStrDate(cusCorCus.getCorLastDate()):""; break;
			case 11: datas[i] = cardType;break;		
			case 12: datas[i] = cusCorCus.getCorCardNum();break;		
			case 13: datas[i] = cusCorCus.getCorPerSize();break;
			case 14: datas[i] = cusCorCus.getCorComInf();break;
			case 15: datas[i] = area; break;
			case 16: datas[i] = cusCorCus.getCorAddress();break;
			case 17: datas[i] = cusCorCus.getCorPhone();break;
			case 18: datas[i] = cusCorCus.getCorCellPhone();break;
			case 19: datas[i] = cusCorCus.getCorFex();break;
			case 20: datas[i] = cusCorCus.getCorZipCode();break;
			case 21: datas[i] = cusCorCus.getCorNet();break;
			case 22: datas[i] = cusCorCus.getCorEmail();break;
			case 23: datas[i] = cusCorCus.getCorQq();break;
			case 24: datas[i] = cusCorCus.getCorRemark();break;
			}
		}
		return datas;
	}
	
	/**
	 * 导出联系人 <br>
	 */
	public String[][][] getOutCon(String type,String conIds,String dataCols,String colNames,String[] args){
		List<CusContact> list = null;
		String [] colsArr = dataCols.split(",");
 		String [] colsHead = colNames.split(",");
		if(type.equals("0")){
			list = cusContactDAO.getOutCont(args);
 		}
 		else{
 			list = cusContactDAO.getContByIds(conIds);
 		}
 		
		String [] header = new String [colsArr.length];
		for(int i=0;i<colsArr.length;i++){
			header[i] = colsHead[Integer.parseInt(colsArr[i])];//生成表格表头
		}
		String [][] datas = new String [list.size()][header.length];
		for(int i=0;i<datas.length;i++){
			datas[i] = getConRow(colsArr,list.get(i));
		}
		return new String[][][]{new String[][]{header},datas};
	}
	/**
	 * 生成表格行 <br>
	 * @param colsArr	选中导出的列序号
	 * @param cusContact联系人实体
	 * @return String[]
	 */
	private String[] getConRow(String[]colsArr, CusContact cont){
		String[] datas = new String[colsArr.length];
		for(int i=0;i<colsArr.length;i++){
			switch(Integer.parseInt(colsArr[i])){
			case  0: datas[i] = cont.getConName();break;
			case  1: datas[i] = cont.getCusCorCus()!=null?cont.getCusCorCus().getCorName():"";break;
			case  2: datas[i] = cont.getPerson()!=null?cont.getPerson().getUserName():""; break;
			case  3: datas[i] = cont.getConSex();break;
			case  4: datas[i] = cont.getConDep();break;
			case  5: datas[i] = cont.getConPos();break;
			case  6: datas[i] = cont.getConLev();break;
			//case  7: datas[i] = cont.getConType();break;
			//case  8: datas[i] = cont.getConBir()!=null?GetDate.parseStrDate(cont.getConBir()):""; break;
			case  7: datas[i] = cont.getConPhone();break;
			case 8: datas[i] = cont.getConEmail();break;
			case 9: datas[i] = cont.getConQq();break;
			case 10: datas[i] = cont.getConMsn();break;
			case 11: datas[i] = cont.getConZipCode();break;
			case 12: datas[i] = cont.getConHomePho();break;
			case 13: datas[i] = cont.getConAdd();break;
			case 14: datas[i] = cont.getConOthLink();break;
			case 15: datas[i] = cont.getConWorkPho();break;
			case 16: datas[i] = cont.getConFex();break;
			case 17: datas[i] = cont.getConHob();break;
			case 18: datas[i] = cont.getConTaboo();break;
			case 19: datas[i] = cont.getConEdu();break;
			case 20: datas[i] = cont.getConRemark();break;
			}
		}
		return datas;
	}
	
	public List<CusProd> listCusProd(String cusId){
		return cusProdDAO.listByCusId(cusId);
	}
	
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
			String isDe, int currentPage, int pageSize) {
		return cusProdDAO.listCusProd(args, orderItem, isDe, currentPage, pageSize);
	}
	/**
	 * 获得客户产品数量<br>
	 * @param args
	 * 			args[0]:是否删除
	 * 			args[1]:客户Id
	 * @return count: 户产品数量
	 */
	public int listCusProdCount(String[] args) {
		return cusProdDAO.listCusProdCount(args);
	}
	
	/**
	 * 保存客户产品 <br>
	 * @param cusProd 客户产品
	 */
	public void saveCusProd(CusProd cusProd) {
		cusProdDAO.save(cusProd);
	}
	
	/**
	 * 通过客户产品Id查找<br>
	 * @param cpId ：客户产品Id
	 * @return 客户产品
	 */
	public CusProd findCusProdById(Long cpId) {
		return cusProdDAO.findById(cpId);
	}
	
	/**
	 * 更新客户产品<br>
	 * @param cusProd 客户产品
	 */
	public void updateCusProd(CusProd cusProd) {
		cusProdDAO.update(cusProd);
	}
	/**
	 * 删除客户产品<br>
	 * @param persistentInstance 客户产品<br>
	 */
	public void delete(CusProd persistentInstance){
		cusProdDAO.delete(persistentInstance);
	}
	
	public List<CusAddr> listCusAddr(String cusId){
		return cusAddrDAO.listByCusId(cusId);
	}
	
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
			String isDe, int currentPage, int pageSize){
		return cusAddrDAO.list(cusId,orderItem,isDe,currentPage,pageSize);
	}
	public int listCusAddrCount(String cusId){
		return cusAddrDAO.listCount(cusId);
	}
	
	/**
	 * 保存客户地址<br> 
	 */
	public void saveCusAddr(CusAddr cusAddr){
		cusAddrDAO.save(cusAddr);
	}
	
	/**
	 * 更新客户地址<br>
	 * @param cusAddr 客户地址<br>
	 */
	public void updateCusAddr(CusAddr cusAddr){
		cusAddrDAO.merge(cusAddr);
	}
	
	/**
	 * 通过Id查找客户地址<br>
	 * @param cadId 客户地址Id
	 * @return CusAddr 客户地址<br>
	 */
	public CusAddr findCusAddrById(Long cadId){
		return cusAddrDAO.findById(cadId);
	}
	
	/**
	 * 删除客户地址<br>
	 * @param cusAddr 客户地址<br>
	 */
	public void deleteCusAddr(CusAddr cusAddr){
		cusAddrDAO.delete(cusAddr);
	}
	
	/**
	 * 获得客户实体list <br>
	 */
	 public List<CusCorCus> getOnTopCusList(String state,String seNo){
		return cusCorCusDAO.getOnTopCusList(state, seNo);
	 }
	 
	/**
	 * 获得应收款信息list <br>
	 */
	 public List<RecvAmtForm> recvAmtList(String recDate){
		 return cusCorCusDAO.recvAmtList(recDate);
	 };
	 
	/**
	 * 工作台提醒 <br>
	 */
	public List<CusProd> getDeskTip(){
		List<CusProd> list = cusProdDAO.findAll();
		List<CusProd> reList = new ArrayList();
		CusProd cusProd = null;
		for(int i=0;i<list.size();i++){
			cusProd = list.get(i);
			if(cusProd.getCpSentDate() !=null && cusProd.getCpWarnDay() !=null ){
				Date d = GetDate.getDate(GetDate.parseStrDate(cusProd.getCpSentDate()), Integer.parseInt(String.valueOf(30-cusProd.getCpWarnDay())));
				if(d.after(GetDate.getDate(GetDate.parseStrDate(GetDate.getCurDate()),-1))){
					reList.add(cusProd);
				}
			}
		}
		
		return reList;
	}
	 
	public void setCusCorCusDAO(CusCorCusDAO cusCorCusDAO) {
		this.cusCorCusDAO = cusCorCusDAO;
	}
	public void setCusAreaDAO(CusAreaDAO cusAreaDAO) {
		this.cusAreaDAO = cusAreaDAO;
	}
   public void setCusProvince(CusProvinceDAO cusProvince) {
		this.cusProvince = cusProvince;
	}
	public void setCusCity(CusCityDAO cusCity) {
		this.cusCity = cusCity;
	}
	public void setCusContactDAO(CusContactDAO cusContactDAO) {
		this.cusContactDAO = cusContactDAO;
	}
	public void setLimUserDAO(LimUserDAO limUserDAO) {
		this.limUserDAO = limUserDAO;
	}
	public void setSalOrgDAO(SalOrgDAO salOrgDAO) {
		this.salOrgDAO = salOrgDAO;
	}
	public void setCusProdDAO(CusProdDAO cusProdDAO) {
		this.cusProdDAO = cusProdDAO;
	}
	public void setMemDateDAO(MemDateDAO memDateDAO) {
		this.memDateDAO = memDateDAO;
	}
	public void setEmpDAO(SalEmpDAO empDAO) {
		this.empDAO = empDAO;
	}
	public void setCusAddrDAO(CusAddrDAO cusAddrDAO) {
		this.cusAddrDAO = cusAddrDAO;
	}

	public void setSalOrderDAO(SalOrderDAO salOrderDAO) {
		this.salOrderDAO = salOrderDAO;
	}

	public void setProdShipmentDAO(ProdShipmentDAO prodShipmentDAO) {
		this.prodShipmentDAO = prodShipmentDAO;
	}

	public void setProdOutDAO(ProdOutDAO prodOutDAO) {
		this.prodOutDAO = prodOutDAO;
	}

	@Override
	public void uploadCus() {
		// TODO Auto-generated method stub
		List c=new CrmCustExcelReader().initYHDBMapExcelReader();
		Iterator it=c.iterator();
  YHPerson p=new YHPerson();
  p.setSeqId(1168);
		while(it.hasNext()){
			CusCorCus key=(CusCorCus)it.next();
			key.setCorState(1);
			key.setCorRecvAmt(0.0);
			key.setCorRecvMax(0.0);
			key.setCorBeginBal(0.0);
			key.setCorAging("0");
			key.setCorIsdelete("1");
			key.setPerson(p);
			cusCorCusDAO.save(key);
			
		}
		
		
	}

	@Override
	public CusProd showCusProd(Long id) {
		// TODO Auto-generated method stub
		return cusProdDAO.findById(id);
	}
}
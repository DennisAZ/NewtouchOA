package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.psit.struts.util.format.StringFormat;

/**
 * 员工资料实体 <br>
 */

public class SalEmp implements java.io.Serializable {

	// Fields

	private Long seNo;
	private String seCode;
	private SalOrg salOrg;
	private String seUserCode;
	private String seName;
	private String seIdeCode;
	private String sePos;
	private String seSex;
	private String seProb;
	private String seBirPlace;
	private String seAccPlace;
	private String seBirth;
	private String seMarry;
	private String seType;
	private String seJobLev;
	private String seJobCate;
	private String seJobTitle;
	private Date seStartDay;
	private String seYearPay;
	private String seCostCenter;
	private String seEmail;
	private String seNation;
	private String sePoliStatus;
	private String seEdu;
	private String seTel;
	private String sePhone;
	private String seQq;
	private String seMsn;
	private String seRecSource;
	private String seProvFund;
	private Date seJobDate;
	private String seHouReg;
	private String seSocialCode;
	private String seRap;
	private String seAddress;
	private String seRemark;
	private String seEdcBac;
	private String seWorkEx;
	private String seBankName;
	private String seBankCard;
	private String seWealAddress;
	private String seWealPos;
	private String seIsovertime;
	private String seAttendance;
	private String seCardNum;
	private String sePic;
	private String seIsenabled;
	private Date seInserDate;
	private LimRole limRole;
	private String seLog;
	private Date seAltDate;
	private String seInserUser;
	private String seAltUser;
	private Date seEndDate;
	private Set salPras = new HashSet(0);//来往记录
	private Set cusServs = new HashSet(0);//客户服务
	private Set quotes = new HashSet(0);//报价
	private Set salOrdCons = new HashSet(0);//订单
	private Set messages = new HashSet(0);//消息
	private Set rmessLims = new HashSet(0);//消息用户关系
	private Set reports = new HashSet(0);//报告
	private Set rrepLims = new HashSet(0);//报告用户关系
	private Set news = new HashSet(0);//公告新闻
	private Set rnewLims = new HashSet(0);//公告用户关系
	private Set schedules = new HashSet(0);//日程安排
	private Set salTasks = new HashSet(0);//工作任务
	private Set taLims = new HashSet(0);//执行人
	private Set salPaidPasts = new HashSet(0);//回款记录
	private Set cusCorCus = new HashSet(0);//客户
	private Set cusContacts = new HashSet(0);//联系人
	private Set salOpps = new HashSet(0);//销售机会
	private Set salPaidPlans = new HashSet(0);//回款计划
	private Set attachments = new HashSet(0);
	
	public Set getAttachments() {
		return attachments;
	}
	public void setAttachments(Set attachments) {
		this.attachments = attachments;
	}
	// Constructors
	/** default constructor */
	public SalEmp() {
	}
    public SalEmp(Long seNo)
    {
    	this.seNo=seNo;
    }
	/** full constructor */
	public SalEmp(SalOrg salOrg, String seUserCode,
			String seName, String seIdeCode,Long seNo,String seCode,
			String sePos, String seSex, String seProb, String seBirPlace,
			String seAccPlace, String seBirth, String seMarry, String seType,
			String seJobLev, String seJobCate, String seJobTitle,
			Date seStartDay, String seYearPay, String seCostCenter,
			String seEmail, String seNation, String sePoliStatus, String seEdu,
			String seTel, String sePhone, String seQq, String seMsn,
			String seRecSource, String seProvFund, Date seJobDate,
			String seHouReg, String seSocialCode, String seRap,
			String seAddress, String seRemark,String seEdcBac, String seWorkEx, String seBankName,
			String seBankCard, String seWealAddress, String seWealPos,
			String seIsovertime, String seAttendance, String seCardNum,String sePic,
			String seIsenabled, Date seInserDate, Date seEndDate, Set salPras, Set cusServs,
			Set quotes, Set salOrdCons, Set messages, Set rmessLims, Set reports,
			Set rrepLims, Set news, Set rnewLims, Set schedules, Set salTasks, Set taLims,
			Set salPaidPasts, Set cusCorCus, Set cusContacts, Set salOpps, Set salPaidPlans) {
		this.salOrg=salOrg;
		this.seUserCode = seUserCode;
		this.seName = seName;
		this.seCode=seCode;
		this.seNo=seNo;
		this.seIdeCode = seIdeCode;
		this.sePos = sePos;
		this.seSex = seSex;
		this.seProb = seProb;
		this.seBirPlace = seBirPlace;
		this.seAccPlace = seAccPlace;
		this.seBirth = seBirth;
		this.seMarry = seMarry;
		this.seType = seType;
		this.seJobLev = seJobLev;
		this.seJobCate = seJobCate;
		this.seJobTitle = seJobTitle;
		this.seStartDay = seStartDay;
		this.seYearPay = seYearPay;
		this.seCostCenter = seCostCenter;
		this.seEmail = seEmail;
		this.seNation = seNation;
		this.sePoliStatus = sePoliStatus;
		this.seEdu = seEdu;
		this.seTel = seTel;
		this.sePhone = sePhone;
		this.seQq = seQq;
		this.seMsn = seMsn;
		this.seRecSource = seRecSource;
		this.seProvFund = seProvFund;
		this.seJobDate = seJobDate;
		this.seHouReg = seHouReg;
		this.seSocialCode = seSocialCode;
		this.seRap = seRap;
		this.seAddress = seAddress;
		this.seRemark = seRemark;
		this.seEdcBac = seEdcBac;
		this.seWorkEx = seWorkEx;
		this.seBankName = seBankName;
		this.seBankCard = seBankCard;
		this.seWealAddress = seWealAddress;
		this.seWealPos = seWealPos;
		this.seIsovertime = seIsovertime;
		this.seAttendance = seAttendance;
		this.seCardNum = seCardNum;
		this.sePic=sePic;
		this.seIsenabled = seIsenabled;
		this.seInserDate = seInserDate;
		this.seEndDate = seEndDate;
		this.salPras = salPras;
		this.cusServs = cusServs;
		this.quotes = quotes;
		this.salOrdCons = salOrdCons;
		this.messages = messages;
		this.rmessLims = rmessLims;
		this.reports = reports;
		this.rrepLims = rrepLims;
		this.news = news;
		this.rnewLims = rnewLims;
		this.schedules = schedules;
		this.salTasks = salTasks;
		this.taLims = taLims;
		this.salPaidPasts = salPaidPasts;
		this.cusCorCus = cusCorCus;
		this.cusContacts = cusContacts;
		this.salOpps = salOpps;
		this.salPaidPlans = salPaidPlans;
	}

	// Property accessors

	public Long getSeNo() {
		return this.seNo;
	}

	public void setSeNo(Long seNo) {
		this.seNo = seNo;
	}
	
	public String getSeName() {
		return this.seName;
	}

	public void setSeName(String seName) {
		this.seName = seName;
	}

	public String getSeIdeCode() {
		return this.seIdeCode;
	}

	public void setSeIdeCode(String seIdeCode) {
		this.seIdeCode = seIdeCode;
	}

	public String getSePos() {
		return this.sePos;
	}

	public void setSePos(String sePos) {
		this.sePos = sePos;
	}

	public String getSeSex() {
		return this.seSex;
	}

	public void setSeSex(String seSex) {
		this.seSex = seSex;
	}

	public String getSeProb() {
		return this.seProb;
	}

	public void setSeProb(String seProb) {
		this.seProb = seProb;
	}

	public String getSeBirPlace() {
		return this.seBirPlace;
	}

	public void setSeBirPlace(String seBirPlace) {
		this.seBirPlace = seBirPlace;
	}

	public String getSeAccPlace() {
		return this.seAccPlace;
	}

	public void setSeAccPlace(String seAccPlace) {
		this.seAccPlace = seAccPlace;
	}

	public String getSeBirth() {
		return this.seBirth;
	}

	public void setSeBirth(String seBirth) {
		this.seBirth = seBirth;
	}

	public String getSeMarry() {
		return this.seMarry;
	}

	public void setSeMarry(String seMarry) {
		this.seMarry = seMarry;
	}

	public String getSeJobLev() {
		return this.seJobLev;
	}

	public void setSeJobLev(String seJobLev) {
		this.seJobLev = seJobLev;
	}

	public String getSeJobCate() {
		return this.seJobCate;
	}

	public void setSeJobCate(String seJobCate) {
		this.seJobCate = seJobCate;
	}

	public String getSeJobTitle() {
		return this.seJobTitle;
	}

	public void setSeJobTitle(String seJobTitle) {
		this.seJobTitle = seJobTitle;
	}

	public Date getSeStartDay() {
		return this.seStartDay;
	}

	public void setSeStartDay(Date seStartDay) {
		this.seStartDay = seStartDay;
	}

	public String getSeYearPay() {
		return this.seYearPay;
	}

	public void setSeYearPay(String seYearPay) {
		this.seYearPay = seYearPay;
	}

	public String getSeCostCenter() {
		return this.seCostCenter;
	}

	public void setSeCostCenter(String seCostCenter) {
		this.seCostCenter = seCostCenter;
	}

	public String getSeEmail() {
		return this.seEmail;
	}

	public void setSeEmail(String seEmail) {
		this.seEmail = seEmail;
	}

	public String getSeNation() {
		return this.seNation;
	}

	public void setSeNation(String seNation) {
		this.seNation = seNation;
	}

	public String getSePoliStatus() {
		return this.sePoliStatus;
	}

	public void setSePoliStatus(String sePoliStatus) {
		this.sePoliStatus = sePoliStatus;
	}

	public String getSeEdu() {
		return this.seEdu;
	}

	public void setSeEdu(String seEdu) {
		this.seEdu = seEdu;
	}

	public String getSeTel() {
		return this.seTel;
	}

	public void setSeTel(String seTel) {
		this.seTel = seTel;
	}

	public String getSePhone() {
		return this.sePhone;
	}

	public void setSePhone(String sePhone) {
		this.sePhone = sePhone;
	}

	public String getSeQq() {
		return this.seQq;
	}

	public void setSeQq(String seQq) {
		this.seQq = seQq;
	}

	public String getSeMsn() {
		return this.seMsn;
	}

	public void setSeMsn(String seMsn) {
		this.seMsn = seMsn;
	}

	public String getSeRecSource() {
		return this.seRecSource;
	}

	public void setSeRecSource(String seRecSource) {
		this.seRecSource = seRecSource;
	}

	public String getSeProvFund() {
		return this.seProvFund;
	}

	public void setSeProvFund(String seProvFund) {
		this.seProvFund = seProvFund;
	}

	public Date getSeJobDate() {
		return this.seJobDate;
	}

	public void setSeJobDate(Date seJobDate) {
		this.seJobDate = seJobDate;
	}

	public String getSeHouReg() {
		return this.seHouReg;
	}

	public void setSeHouReg(String seHouReg) {
		this.seHouReg = seHouReg;
	}

	public String getSeSocialCode() {
		return this.seSocialCode;
	}

	public void setSeSocialCode(String seSocialCode) {
		this.seSocialCode = seSocialCode;
	}

	public String getSeRap() {
		return this.seRap;
	}

	public void setSeRap(String seRap) {
		this.seRap = seRap;
	}

	public String getSeAddress() {
		return this.seAddress;
	}

	public void setSeAddress(String seAddress) {
		this.seAddress = seAddress;
	}

	public String getSeRemark() {
		return StringFormat.toBlank(this.seRemark);
	}

	public void setSeRemark(String seRemark) {
		this.seRemark = seRemark;
	}

	public String getSeBankName() {
		return this.seBankName;
	}

	public void setSeBankName(String seBankName) {
		this.seBankName = seBankName;
	}

	public String getSeBankCard() {
		return this.seBankCard;
	}

	public void setSeBankCard(String seBankCard) {
		this.seBankCard = seBankCard;
	}

	public String getSeWealAddress() {
		return this.seWealAddress;
	}

	public void setSeWealAddress(String seWealAddress) {
		this.seWealAddress = seWealAddress;
	}

	public String getSeWealPos() {
		return this.seWealPos;
	}

	public void setSeWealPos(String seWealPos) {
		this.seWealPos = seWealPos;
	}

	public String getSeIsovertime() {
		return this.seIsovertime;
	}

	public void setSeIsovertime(String seIsovertime) {
		this.seIsovertime = seIsovertime;
	}

	public String getSeAttendance() {
		return this.seAttendance;
	}

	public void setSeAttendance(String seAttendance) {
		this.seAttendance = seAttendance;
	}

	public String getSeCardNum() {
		return this.seCardNum;
	}

	public void setSeCardNum(String seCardNum) {
		this.seCardNum = seCardNum;
	}

	public String getSeIsenabled() {
		return this.seIsenabled;
	}

	public void setSeIsenabled(String seIsenabled) {
		this.seIsenabled = seIsenabled;
	}

	public Date getSeInserDate() {
		return this.seInserDate;
	}

	public void setSeInserDate(Date seInserDate) {
		this.seInserDate = seInserDate;
	}

	public SalOrg getSalOrg() {
		return salOrg;
	}

	public void setSalOrg(SalOrg salOrg) {
		this.salOrg = salOrg;
	}

	public String getSePic() {
		return sePic;
	}

	public void setSePic(String sePic) {
		this.sePic = sePic;
	}

	public String getSeType() {
		return seType;
	}

	public void setSeType(String seType) {
		this.seType = seType;
	}

	public String getSeCode() {
		return seCode;
	}

	public void setSeCode(String seCode) {
		this.seCode = seCode;
	}

	public LimRole getLimRole() {
		return limRole;
	}
	public void setLimRole(LimRole limRole) {
		this.limRole = limRole;
	}
	public String getSeLog() {
		return seLog;
	}
	public void setSeLog(String seLog) {
		this.seLog = seLog;
	}
	public Date getSeAltDate() {
		return seAltDate;
	}
	public void setSeAltDate(Date seAltDate) {
		this.seAltDate = seAltDate;
	}
	public String getSeInserUser() {
		return seInserUser;
	}
	public void setSeInserUser(String seInserUser) {
		this.seInserUser = seInserUser;
	}
	public String getSeAltUser() {
		return seAltUser;
	}
	public void setSeAltUser(String seAltUser) {
		this.seAltUser = seAltUser;
	}
	public Date getSeEndDate() {
		return seEndDate;
	}
	public void setSeEndDate(Date seEndDate) {
		this.seEndDate = seEndDate;
	}
	public Set getSalPras() {
		return salPras;
	}
	public void setSalPras(Set salPras) {
		this.salPras = salPras;
	}
	public Set getCusServs() {
		return cusServs;
	}
	public void setCusServs(Set cusServs) {
		this.cusServs = cusServs;
	}
	public Set getQuotes() {
		return quotes;
	}
	public void setQuotes(Set quotes) {
		this.quotes = quotes;
	}
	public Set getSalOrdCons() {
		return salOrdCons;
	}
	public void setSalOrdCons(Set salOrdCons) {
		this.salOrdCons = salOrdCons;
	}
	public Set getMessages() {
		return messages;
	}
	public void setMessages(Set messages) {
		this.messages = messages;
	}
	public Set getRmessLims() {
		return rmessLims;
	}
	public void setRmessLims(Set rmessLims) {
		this.rmessLims = rmessLims;
	}
	public Set getReports() {
		return reports;
	}
	public void setReports(Set reports) {
		this.reports = reports;
	}
	public Set getRrepLims() {
		return rrepLims;
	}
	public void setRrepLims(Set rrepLims) {
		this.rrepLims = rrepLims;
	}
	public Set getNews() {
		return news;
	}
	public void setNews(Set news) {
		this.news = news;
	}
	public Set getRnewLims() {
		return rnewLims;
	}
	public void setRnewLims(Set newLims) {
		rnewLims = newLims;
	}
	public Set getSchedules() {
		return schedules;
	}
	public void setSchedules(Set schedules) {
		this.schedules = schedules;
	}
	public Set getSalTasks() {
		return salTasks;
	}
	public void setSalTasks(Set salTasks) {
		this.salTasks = salTasks;
	}
	public Set getTaLims() {
		return taLims;
	}
	public void setTaLims(Set taLims) {
		this.taLims = taLims;
	}
	public Set getSalPaidPasts() {
		return salPaidPasts;
	}
	public void setSalPaidPasts(Set salPaidPasts) {
		this.salPaidPasts = salPaidPasts;
	}
	public String getSeEdcBac() {
		return StringFormat.toBlank(seEdcBac);
	}
	public void setSeEdcBac(String seEdcBac) {
		this.seEdcBac = seEdcBac;
	}
	public String getSeWorkEx() {
		return StringFormat.toBlank(seWorkEx);
	}
	public void setSeWorkEx(String seWorkEx) {
		this.seWorkEx = seWorkEx;
	}
	public Set getCusCorCus() {
		return cusCorCus;
	}
	public void setCusCorCus(Set cusCorCus) {
		this.cusCorCus = cusCorCus;
	}
	public Set getCusContacts() {
		return cusContacts;
	}
	public void setCusContacts(Set cusContacts) {
		this.cusContacts = cusContacts;
	}
	public Set getSalOpps() {
		return salOpps;
	}
	public void setSalOpps(Set salOpps) {
		this.salOpps = salOpps;
	}
	public Set getSalPaidPlans() {
		return salPaidPlans;
	}
	public void setSalPaidPlans(Set salPaidPlans) {
		this.salPaidPlans = salPaidPlans;
	}
	public String getSeUserCode() {
		return seUserCode;
	}
	public void setSeUserCode(String seUserCode) {
		this.seUserCode = seUserCode;
	}

}
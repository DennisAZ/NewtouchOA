package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.psit.struts.util.format.StringFormat;

/**
 * 客户类 <br>
 */

public class CusCorCus implements java.io.Serializable {

	// Fields
	private Long corCode;  //id
	private String corNum; //客户编号
	private TypeList cusIndustry;//行业
	private TypeList corSou; //来源
	private CusProvince cusProvince;//省份
	private YHPerson person;//负责人

	private String userName;
	private CusCity cusCity;//城市
	private CusArea cusArea;//地区
	private TypeList cusType;//客户类型
	private String corName;//客户名称
	private String corHot;//客户级别
	private String corMne;//助记简称
	private String corCardType;//证件类型
	private String corCardNum;//证件号码
	private Integer corState;//客户状态
	private String corColor;//标色
	private String corRelation;//关系等级
	private String corPerSize;//人员规模
	private String corEmail;//电子邮件
	private String corQq;//QQ
	private String corComInf;//客户简介
	private String corPhone;//电话
	private String corFex;//传真
	private String corNet;//网址
	private String corZipCode;//邮编
	private String corAddress;//地址
	private String corRemark;//备注
	private Date corCreatDate;
	private Date corUpdDate;
	private String corIssuc;
	private Date corLastDate;//最近联系时间
	private String corTempTag;
	private String corIsdelete;//判断记录是否被删除
	private String corSpeWrite;
	private String corUpdUser;
	private String corInsUser;
	private String corBankCode;//开户行及账号
	private String corTaxNum;//税号
	private String corSendType;//常用发货方式
	private String corStateRecord;//修改记录
	private Date corOnDate;//到期日期
	private Date corRecDate;//收款日期
	private Set salOrdCons = new HashSet(0);//
	private Set cusContacts = new HashSet(0);//联系人
	private Set salPras = new HashSet(0);
	private Set salOpps = new HashSet(0);
	private Set cusServs = new HashSet(0);
	private Set salPaids = new HashSet(0);
	private Set attachments = new HashSet(0);//附件
	private String corCellPhone;//手机
	private Date corAssignDate;//最后分配日期
	private String corAssignCont;//分配情况
	private Double corSaleNum;//最低月销售额
	private Double corRecvAmt;
	private Double corRecvMax;
	private Double corBeginBal;//发货期初余额
	private Double corTicketNum;//应收开票余额
	private String corAging;//账龄（天）
	private Double corTicketBal;//开票期初余额
	private String corBeginRecord;
	private String corTicketRecord;
	public static final int S_UNDEV=0,S_DEVING=1,S_DEVED=2; 
	public static final String D_DELED="0", D_UNDEL="1";
	public static final String C_NONE="0", C_RED="1", C_GREEN="2", C_BLUE="3";
	public static final String IS_SUCED="1", IS_UNSUC="0";
	public static final String M_MARKED="1", M_UNMARK="0";
	private String cusUuId;//自定义uuid
	// Constructors

	/** default constructor */
	public CusCorCus() {
	}

	/** minimal constructor */
	public CusCorCus(Long corCode) {
		this.corCode = corCode;
	}
	/** full constructor */
	public CusCorCus(Long corCode, String corNum, TypeList cusIndustry,
			TypeList cusType, CusProvince cusProvince, CusCity cusCity,
			SalEmp salEmp, CusArea cusArea, String corName, String corHot,
			String corMne, String corCardType, String corCardNum, Integer corState,
			String corColor, String corRelation, String corPerSize,
			String corEmail, String corQq, TypeList corSou, String corComInf,
			String corPhone, String corFex, String corNet, String corZipCode,
			String corAddress, String corRemark, Date corCreatDate,
			Date corUpdDate, String corIssuc, Date corLastDate,
			String corTempTag, String corIsdelete, String corSpeWrite,
			String corUpdUser, String corInsUser, Set salOrdCons,
			Set cusContacts, Set salPras, Set salOpps, Set cusServs,
			Set salPaids, Set attachments, String corCellPhone,
			String corBankCode, String corTaxNum, String corSendType,
			Date corAssignDate, String corAssignCont, Double corSaleNum, 
			Double corRecvAmt, Double corRecvMax, Double corBeginBal, String corAging,
			String corStateRecord,Date corOnDate,Date corRecDate,Double corTicketNum,Double corTicketBal,
			String corBeginRecord, String corTicketRecord) {
		this.corCode = corCode;
		this.cusIndustry = cusIndustry;
		this.cusProvince = cusProvince;
		this.person = person;
		this.cusCity = cusCity;
		this.cusArea = cusArea;
		this.cusType = cusType;
		this.corName = corName;
		this.corHot = corHot;
		this.corMne = corMne;
		this.corCardType = corCardType;
		this.corCardNum = corCardNum;
		this.corColor = corColor;
		this.corRelation = corRelation;
		this.corPerSize = corPerSize;
		this.corEmail = corEmail;
		this.corQq = corQq;
		this.corSou = corSou;
		this.corComInf = corComInf;
		this.corPhone = corPhone;
		this.corFex = corFex;
		this.corNet = corNet;
		this.corZipCode = corZipCode;
		this.corAddress = corAddress;
		this.corRemark = corRemark;
		this.corCreatDate = corCreatDate;
		this.corUpdDate = corUpdDate;
		this.corIssuc = corIssuc;
		this.corLastDate = corLastDate;
		this.corTempTag = corTempTag;
		this.corIsdelete = corIsdelete;
		this.corSpeWrite = corSpeWrite;
		this.corInsUser=corInsUser;
		this.corUpdUser = corUpdUser;
		this.salOrdCons = salOrdCons;
		this.cusContacts = cusContacts;
		this.salPras = salPras;
		this.salOpps = salOpps;
		this.cusServs = cusServs;
		this.salPaids = salPaids;
		this.attachments = attachments;
		this.corCellPhone = corCellPhone;
		this.corBankCode = corBankCode;
		this.corTaxNum = corTaxNum;
		this.corSendType = corSendType;
		this.corAssignDate = corAssignDate;
		this.corAssignCont = corAssignCont;
		this.corSaleNum = corSaleNum;
		this.corRecvAmt = corRecvAmt;
		this.corRecvMax = corRecvMax;
		this.corBeginBal = corBeginBal;
		this.corAging = corAging;
		this.corOnDate = corOnDate;
		this.corStateRecord = corStateRecord;
		this.corRecDate = corRecDate;
		this.corTicketNum = corTicketNum;
		this.corTicketBal = corTicketBal;
		this.corBeginRecord = corBeginRecord;
		this.corTicketRecord = corTicketRecord;
	}

	// Property accessors

	public String getCusUuId() {
		return cusUuId;
	}

	public void setCusUuId(String cusUuId) {
		this.cusUuId = cusUuId;
	}

	public String getCorBeginRecord() {
		return corBeginRecord;
	}

	public void setCorBeginRecord(String corBeginRecord) {
		this.corBeginRecord = corBeginRecord;
	}

	public String getCorTicketRecord() {
		return corTicketRecord;
	}

	public void setCorTicketRecord(String corTicketRecord) {
		this.corTicketRecord = corTicketRecord;
	}

	public Double getCorTicketBal() {
		return corTicketBal;
	}

	public void setCorTicketBal(Double corTicketBal) {
		this.corTicketBal = corTicketBal;
	}

	public Double getCorTicketNum() {
		return corTicketNum;
	}

	public void setCorTicketNum(Double corTicketNum) {
		this.corTicketNum = corTicketNum;
	}

	public String getCorCellPhone() {
		return corCellPhone;
	}

	public void setCorCellPhone(String corCellPhone) {
		this.corCellPhone = corCellPhone;
	}
	
	public Long getCorCode() {
		return this.corCode;
	}

	public void setCorCode(Long corCode) {
		this.corCode = corCode;
	}

	public TypeList getCusIndustry() {
		return this.cusIndustry;
	}

	public void setCusIndustry(TypeList cusIndustry) {
		this.cusIndustry = cusIndustry;
	}

	public CusProvince getCusProvince() {
		return this.cusProvince;
	}

	public void setCusProvince(CusProvince cusProvince) {
		this.cusProvince = cusProvince;
	}

	public CusCity getCusCity() {
		return this.cusCity;
	}

	public void setCusCity(CusCity cusCity) {
		this.cusCity = cusCity;
	}

	public CusArea getCusArea() {
		return this.cusArea;
	}

	public void setCusArea(CusArea cusArea) {
		this.cusArea = cusArea;
	}

	public String getCorName() {
		return this.corName;
	}

	public void setCorName(String corName) {
		this.corName = corName;
	}

	public String getCorHot() {
		return this.corHot;
	}

	public void setCorHot(String corHot) {
		this.corHot = corHot;
	}

	public String getCorMne() {
		return this.corMne;
	}

	public void setCorMne(String corMne) {
		this.corMne = corMne;
	}

	public String getCorCardType() {
		return this.corCardType;
	}

	public void setCorCardType(String corCardType) {
		this.corCardType = corCardType;
	}

	public String getCorCardNum() {
		return this.corCardNum;
	}

	public void setCorCardNum(String corCardNum) {
		this.corCardNum = corCardNum;
	}

	public String getCorColor() {
		return this.corColor;
	}

	public void setCorColor(String corColor) {
		this.corColor = corColor;
	}

	public String getCorRelation() {
		return this.corRelation;
	}

	public void setCorRelation(String corRelation) {
		this.corRelation = corRelation;
	}

	public String getCorPerSize() {
		return this.corPerSize;
	}

	public void setCorPerSize(String corPerSize) {
		this.corPerSize = corPerSize;
	}

	public String getCorEmail() {
		return this.corEmail;
	}

	public void setCorEmail(String corEmail) {
		this.corEmail = corEmail;
	}

	public String getCorQq() {
		return this.corQq;
	}

	public void setCorQq(String corQq) {
		this.corQq = corQq;
	}

	public TypeList getCorSou() {
		return this.corSou;
	}

	public void setCorSou(TypeList corSou) {
		this.corSou = corSou;
	}

	public String getCorComInf() {
		return StringFormat.toBlank(this.corComInf);
	}

	public void setCorComInf(String corComInf) {
		this.corComInf = corComInf;
	}

	public String getCorPhone() {
		return this.corPhone;
	}

	public void setCorPhone(String corPhone) {
		this.corPhone = corPhone;
	}

	public String getCorFex() {
		return this.corFex;
	}

	public void setCorFex(String corFex) {
		this.corFex = corFex;
	}

	public String getCorNet() {
		return this.corNet;
	}

	public void setCorNet(String corNet) {
		this.corNet = corNet;
	}

	public String getCorZipCode() {
		return this.corZipCode;
	}

	public void setCorZipCode(String corZipCode) {
		this.corZipCode = corZipCode;
	}

	public String getCorAddress() {
		return StringFormat.toBlank(this.corAddress);
	}

	public void setCorAddress(String corAddress) {
		this.corAddress = corAddress;
	}

	public String getCorRemark() {
		return StringFormat.toBlank(this.corRemark);
	}

	public void setCorRemark(String corRemark) {
		this.corRemark = corRemark;
	}

	public Date getCorCreatDate() {
		return this.corCreatDate;
	}

	public void setCorCreatDate(Date corCreatDate) {
		this.corCreatDate = corCreatDate;
	}

	public Date getCorUpdDate() {
		return this.corUpdDate;
	}

	public void setCorUpdDate(Date corUpdDate) {
		this.corUpdDate = corUpdDate;
	}

	public String getCorIssuc() {
		return this.corIssuc;
	}

	public void setCorIssuc(String corIssuc) {
		this.corIssuc = corIssuc;
	}

	public Date getCorLastDate() {
		return this.corLastDate;
	}

	public void setCorLastDate(Date corLastDate) {
		this.corLastDate = corLastDate;
	}

	public String getCorTempTag() {
		return this.corTempTag;
	}

	public void setCorTempTag(String corTempTag) {
		this.corTempTag = corTempTag;
	}

	public String getCorIsdelete() {
		return this.corIsdelete;
	}

	public void setCorIsdelete(String corIsdelete) {
		this.corIsdelete = corIsdelete;
	}

	public Set getSalOrdCons() {
		return this.salOrdCons;
	}

	public void setSalOrdCons(Set salOrdCons) {
		this.salOrdCons = salOrdCons;
	}

	public Set getCusContacts() {
		return this.cusContacts;
	}

	public void setCusContacts(Set cusContacts) {
		this.cusContacts = cusContacts;
	}

	public Set getSalPras() {
		return this.salPras;
	}

	public void setSalPras(Set salPras) {
		this.salPras = salPras;
	}

	public Set getSalOpps() {
		return this.salOpps;
	}

	public void setSalOpps(Set salOpps) {
		this.salOpps = salOpps;
	}
	//��ӵĸ���ʵ��
	public Set getAttachments(){
		return this.attachments;
	}
	
	public void setAttachments(Set attachments){
		this.attachments = attachments;
	}

	public Set getCusServs() {
		return this.cusServs;
	}

	public void setCusServs(Set cusServs) {
		this.cusServs = cusServs;
	}

	public Set getSalPaids() {
		return this.salPaids;
	}

	public void setSalPaids(Set salPaids) {
		this.salPaids = salPaids;
	}

	public String getCorSpeWrite() {
		return StringFormat.toBlank(corSpeWrite);
	}

	public void setCorSpeWrite(String corSpeWrite) {
		this.corSpeWrite = corSpeWrite;
	}

	public String getCorUpdUser() {
		return corUpdUser;
	}

	public void setCorUpdUser(String corUpdUser) {
		this.corUpdUser = corUpdUser;
	}

	public TypeList getCusType() {
		return cusType;
	}

	public void setCusType(TypeList cusType) {
		this.cusType = cusType;
	}

	public String getCorNum() {
		return corNum;
	}

	public void setCorNum(String corNum) {
		this.corNum = corNum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCorInsUser() {
		return corInsUser;
	}

	public void setCorInsUser(String corInsUser) {
		this.corInsUser = corInsUser;
	}

	public String getCorBankCode() {
		return corBankCode;
	}

	public void setCorBankCode(String corBankCode) {
		this.corBankCode = corBankCode;
	}

	public String getCorTaxNum() {
		return corTaxNum;
	}

	public void setCorTaxNum(String corTaxNum) {
		this.corTaxNum = corTaxNum;
	}

	public String getCorSendType() {
		return corSendType;
	}

	public void setCorSendType(String corSendType) {
		this.corSendType = corSendType;
	}

	public Date getCorAssignDate() {
		return corAssignDate;
	}

	public void setCorAssignDate(Date corAssignDate) {
		this.corAssignDate = corAssignDate;
	}

	public String getCorAssignCont() {
		return corAssignCont;
	}

	public void setCorAssignCont(String corAssignCont) {
		this.corAssignCont = corAssignCont;
	}

	public Double getCorSaleNum() {
		return corSaleNum;
	}

	public void setCorSaleNum(Double corSaleNum) {
		this.corSaleNum = corSaleNum;
	}

	public Integer getCorState() {
		return corState;
	}

	public void setCorState(Integer corState) {
		this.corState = corState;
	}
	
	public static String getCorStateTxt(int corStateValue){
		String stateTxt = "";
		switch(corStateValue){
		case S_UNDEV: 
			stateTxt="待开发";
			break;
		case S_DEVING:
			stateTxt="开发中";
			break;
		case S_DEVED:
			stateTxt="已归属";
			break;
		}
		return stateTxt;
	}

	public Double getCorRecvAmt() {
		return corRecvAmt;
	}

	public void setCorRecvAmt(Double corRecvAmt) {
		this.corRecvAmt = corRecvAmt;
	}

	public Double getCorRecvMax() {
		return corRecvMax;
	}

	public void setCorRecvMax(Double corRecvMax) {
		this.corRecvMax = corRecvMax;
	}

	public Double getCorBeginBal() {
		return corBeginBal;
	}

	public void setCorBeginBal(Double corBeginBal) {
		this.corBeginBal = corBeginBal;
	}

	public String getCorAging() {
		return corAging;
	}

	public void setCorAging(String corAging) {
		this.corAging = corAging;
	}

	public String getCorStateRecord() {
		return corStateRecord;
	}

	public void setCorStateRecord(String corStateRecord) {
		this.corStateRecord = corStateRecord;
	}

	public Date getCorOnDate() {
		return corOnDate;
	}

	public void setCorOnDate(Date corOnDate) {
		this.corOnDate = corOnDate;
	}

	public Date getCorRecDate() {
		return corRecDate;
	}

	public void setCorRecDate(Date corRecDate) {
		this.corRecDate = corRecDate;
	}
	public YHPerson getPerson() {
		return person;
	}

	public void setPerson(YHPerson person) {
		this.person = person;
	}

}
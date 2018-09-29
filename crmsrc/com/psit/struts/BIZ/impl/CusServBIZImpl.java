package com.psit.struts.BIZ.impl;

import java.util.Date;
import java.util.List;

import com.psit.struts.BIZ.CusServBIZ;
import com.psit.struts.DAO.CusServDAO;
import com.psit.struts.DAO.QuoteDAO;
import com.psit.struts.DAO.RQuoProDAO;
import com.psit.struts.DAO.SalOppDAO;
import com.psit.struts.DAO.SalPraDAO;
import com.psit.struts.entity.CusServ;
import com.psit.struts.entity.Quote;
import com.psit.struts.entity.RQuoPro;
import com.psit.struts.entity.SalOpp;
import com.psit.struts.entity.SalPra;
/**
 * 客户管理BIZ实现类 <br>
 */
public class CusServBIZImpl implements CusServBIZ{
	SalOppDAO salOppDAO=null;
	SalPraDAO salPraDAO=null;
	CusServDAO cusServDao=null;
	QuoteDAO quoteDao=null;
	RQuoProDAO rquoProDao=null;
	
	/**
	 * 销售机会列表 <br>
	 */
	public List<SalOpp> listSalOpps(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		return salOppDAO.listSalOpps(args, orderItem, isDe, currentPage, pageSize);
	}
	public int listSalOppsCount(String[]args){
		return salOppDAO.listSalOppsCount(args);
	}
	/**
	  * 获得最近销售机会<br>
	  */
	public List getOppByExeDate(int pageCurrentNo, int pageSize, Date startDate,
			Date endDate,String range,String seNo) {
		return salOppDAO.getOppByExeDate(pageCurrentNo, pageSize, startDate, endDate,range,seNo);
	}
	public int getOppByExeDateCount(Date startDate, Date endDate,String range,String seNo) {
		return salOppDAO.getOppByExeDateCount(startDate, endDate,range,seNo);
	}
	/**
	  * 根据客户编号获得销售机会(无分页) <br>
	  */
	public List getOppByCusCode(Long cusCode) {
		return salOppDAO.getOppByCusCode(cusCode);
	}
	 /**
	  * 获得待删除的所有销售机会 <br>
	  */
	public List findDelOpp(int pageCurrentNo, int pageSize) {
		return salOppDAO.findDelOpp(pageCurrentNo, pageSize);
	}
	public int findDelOppCount() {
		return salOppDAO.findDelOppCount();
	}
	/**
	 * 获得某客户下的所有销售机会 <br>
	 */
	public List getCusOpp(Long cusCode,String orderItem, String isDe, int currentPage,int pageSize){
		 return salOppDAO.getCusOpp(cusCode,orderItem, isDe, currentPage, pageSize);
	}
	public int getCusOppCount(Long cusCode){
		 return salOppDAO.getCusOppCount(cusCode);
	}
	/**
	 * 保存销售机会信息 <br>
	 */
	public void save(SalOpp salOpp) {
		salOppDAO.save(salOpp);
	}
	/**
	 * 根据Id获取销售机会(带状态) <br>
	 */
	public SalOpp showSalOpp(Long id) {
		return salOppDAO.showSalOpp(id);
	}
	/**
	 * 更新销售机会 <br>
	 */
	public void update(SalOpp salOpp) {
		salOppDAO.update(salOpp);
	}
	/**
	  * 删除销售机会 <br>
	  */
	public void delOpp(SalOpp salOpp) {
		salOppDAO.delOpp(salOpp);
	}
	
	 /**
	  * 获得待删除的所有报价记录 <br>
	  */
	public List findDelQuote(int pageCurrentNo, int pageSize) {
		return quoteDao.findDelQuote(pageCurrentNo, pageSize);
	}
	public int findDelQuoteCount() {
		return quoteDao.findDelQuoteCount();
	}
	/**
	  * 保存报价信息 <br>
	  */
	public void saveQuote(Quote quote) {
		quoteDao.saveQuote(quote);
	}
	 /**
	  * 根据Id获得报价 <br>
	  */
	public Quote showQuote(Long id) {
		return quoteDao.showQuote(id);
	}
	 /**
	  * 更新报价信息 <br>
	  */
	public void updateQuo(Quote quote) {
		quoteDao.updateQuo(quote);
	}
	 /**
	  * 删除机会报价 <br>
	  */
	public void delQuote(Quote quote) {
		quoteDao.delQuote(quote);
	}
	 /**
	  * 根据id获得销售机会 <br>
	  */
	public SalOpp getSalOpp(Long id) {
		return salOppDAO.getSalOpp(id);
	}
	/**
	 * 根据报价记录删除报价明细 <br>
	 */
	public void delByQuo(Long quoId){
		rquoProDao.delByQuo(quoId);
	}
	/**
	 * 保存报价明细 <br>
	 */
	public void saveRup(RQuoPro rup){
		rquoProDao.save(rup);
	}
	
	/**
	 * 来往记录列表<br>
	 */
	public List<SalPra> listSalPras(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		return salPraDAO.listSalPras(args, orderItem, isDe, currentPage, pageSize);
	}
	public int listSalPrasCount(String[]args){
		return salPraDAO.listSalPrasCount(args);
	}
	 /**
	  * 获得某客户下的所有来往记录 <br>
	  */
	public List getCusPra(Long cusCode,String orderItem, String isDe,int currentPage,int pageSize){
		 return salPraDAO.getCusPra(cusCode, orderItem, isDe,currentPage, pageSize);
	}
	public int getCusPraCount(Long cusCode){
		 return salPraDAO.getCusPraCount(cusCode);
	}
	 /**
	  * 获得最近来往记录 <br>
	  */
	public List getPraByExeDate(Date startDate, Date endDate,String range,String seNo) {
		return salPraDAO.getPraByExeDate(startDate, endDate,range,seNo);
	}
	public int getPraByExeDateCount(Date startDate, Date endDate,String range,String seNo) {
		return salPraDAO.getPraByExeDateCount(startDate, endDate,range,seNo);
	}
	 /**
	  * 获得待删除的所有来往记录 <br>
	  */
	public List findDelPra(int pageCurrentNo, int pageSize) {
		return salPraDAO.findDelPra(pageCurrentNo, pageSize);
	}
	public int findDelPraCount() {
		return salPraDAO.findDelPraCount();
	}
	/**
	 * 保存来往记录信息 <br>
	 */
	public void save(SalPra salPra) {
		salPraDAO.save(salPra);
	}
	/**
	 * 根据Id获取来往记录 <br>
	 */
	public SalPra showSalPra(Long id) {
		return salPraDAO.showSalPra(id);
	}
	/**
	 * 更新来往记录信息 <br>
	 */
	public void update(SalPra salPra) {
		salPraDAO.update(salPra);
	}
	 /**
	  * 删除来往记录 <br>
	  */
	public void delPra(SalPra salPra) {
		salPraDAO.delPra(salPra);
	}
	
	 /**
	  * 获得客户服务<br>
	  */
	 public List<CusServ> getServ(String[]args, String orderItem,String isDe, int currentPage, int pageSize){
		 return cusServDao.getServ(args, orderItem, isDe, currentPage, pageSize);
	 }
	 public int getServCount(String [] args){
		 return cusServDao.getServCount(args);
	 }
	 /**
	  * 获得某客户下的所有客户关怀 <br>
	  */
	public List getCusServ(Long cusCode,String orderItem, String isDe, int currentPage,int pageSize){
		 return cusServDao.getCusServ(cusCode, orderItem, isDe, currentPage, pageSize);
	}
	public int getCusServCount(Long cusCode){
		 return cusServDao.getCusServCount(cusCode);
	}
	 /**
	  * 获得待删除的所有客户关怀 <br>
	  */
	public List findDelServ(int pageCurrentNo, int pageSize) {
		return cusServDao.findDelServ(pageCurrentNo, pageSize);
	}
	public int findDelServCount() {
		return cusServDao.findDelServCount();
	}
	/**
	 * 保存客户关怀信息 <br>
	 */
	public void save(CusServ cusServ) {
		cusServDao.save(cusServ);
	}
	/**
	 * 根据客户关怀编号获取客户关怀 <br>
	 */
	public CusServ showCusServ(Long serCode) {
		return cusServDao.showCusServ(serCode);
	}
	/**
	 * 更新客户关怀信息 <br>
	 */
	public void update(CusServ cusServ) {
		cusServDao.update(cusServ);
	}
	 /**
	  * 删除客户关怀 <br>
	  */
	public void delServ(CusServ cusServ) {
		cusServDao.delServ(cusServ);
	}
	
	public void setSalOppDAO(SalOppDAO salOppDAO) {
		this.salOppDAO = salOppDAO;
	}
	public void setSalPraDAO(SalPraDAO salPraDAO) {
		this.salPraDAO = salPraDAO;
	}
	public void setCusServDao(CusServDAO cusServDao) {
		this.cusServDao = cusServDao;
	}
	public void setRquoProDao(RQuoProDAO rquoProDao) {
		this.rquoProDao = rquoProDao;
	}
	public void setQuoteDao(QuoteDAO quoteDao) {
		this.quoteDao = quoteDao;
	}
}
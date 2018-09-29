package com.psit.struts.BIZ.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.psit.struts.BIZ.SalInvoiceBIZ;
import com.psit.struts.DAO.CusCorCusDAO;
import com.psit.struts.DAO.SalInvoiceDAO;
import com.psit.struts.entity.CusCorCus;
import com.psit.struts.entity.SalInvoice;
import com.psit.struts.util.format.GetDate;
import com.psit.struts.util.format.StringFormat;

/**
 * 订单回款BIZ实现类 <br>
 */
public class SalInvoiceBIZImpl implements SalInvoiceBIZ {
	SalInvoiceDAO salInvoiceDao = null;
	CusCorCusDAO cusDao = null;
	
	/**
	 * 保存回款记录 <br>
	 */
	public void save(SalInvoice invoice,String cusId){
		CusCorCus cusCorCus = cusDao.findCus(Long.parseLong(cusId));
		Date d;
		if(cusCorCus.getCorAging() != null && !cusCorCus.getCorAging().equals("")){
			d = GetDate.getDate(GetDate.parseStrDate(invoice.getSinDate()),Integer.parseInt(cusCorCus.getCorAging()));
		}else{
			d = GetDate.getDate(GetDate.parseStrDate(invoice.getSinDate()),0);
		}
		
		cusCorCus.setCorRecDate(d);
		cusDao.update(cusCorCus);
		salInvoiceDao.save(invoice);
	}
	/**
	 * 更新回款记录 <br>
	 */
	public void update(SalInvoice invoice,Double oldSin){
		salInvoiceDao.update(invoice,oldSin);
	}


	/**
	 * 彻底删除 <br>
	 */
	public void delete(String id){
		if(!StringFormat.isEmpty(id)){
			SalInvoice invoice=salInvoiceDao.findById(Long.parseLong(id));
			salInvoiceDao.delete(invoice);
		}
	}

	/**
	 * 回款记录详情 <br>
	 */
	public SalInvoice getById(Long id)
	{
		return salInvoiceDao.getById(id);
	}
	
	public SalInvoice findById(Long id) {
		// TODO Auto-generated method stub
		return salInvoiceDao.getById(id);
	}
	
	/**
	 * 票据记录列表 <br>
	 */
	public List<SalInvoice> listSalInvoices(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		return salInvoiceDao.listSalInvoices(args, orderItem, isDe, currentPage, pageSize);
	};
	public int listSalInvoicesCount(String[]args){
		return salInvoiceDao.listSalInvoicesCount(args);
	}
	
	
	public void setSalInvoiceDao(SalInvoiceDAO salInvoiceDao) {
		this.salInvoiceDao = salInvoiceDao;
	}
	public void setCusDao(CusCorCusDAO cusDao) {
		this.cusDao = cusDao;
	}

}
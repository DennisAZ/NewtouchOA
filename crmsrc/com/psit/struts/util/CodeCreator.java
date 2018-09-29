package com.psit.struts.util;

import com.psit.struts.util.DAO.CodeCreatorDAO;

/**
 * 
 * 编号生成器DAO <br>
 *
 * create_date: Aug 20, 2010,1:56:03 PM<br>
 * @author zjr
 */
public class CodeCreator {
	/**
	 * lock_table: 生成编号加锁的表
	 *  cus_cor_cus	 客户表
	 *	sal_org	 部门表
	 *	wms_change	库存调拨表
	 *	wms_check	库存盘点表
	 *	wms_stro 仓库表
	 *	wms_war_in	入库表
	 *	wms_war_out	 出来表
	 *	acc_in 入账表
	 */
	private static final String LOCK_TABS[]={"lock_table"};
	/**
	 * 
	 * 生成编号 <br>
	 * create_date: Aug 20, 2010,1:59:30 PM <br>
	 * @param prefix 前缀
	 * @param type 类型编码
	 * @param i 账号数字编号
	 * @return String 返回编号<br>
	 */
	public String createCode(String prefix, String type,int i) {
		CodeCreatorDAO codeCreatorDao = new CodeCreatorDAO();
		String max = codeCreatorDao.getMax(type,prefix,LOCK_TABS[i]);
		String code = prefix+"-"+max;
		return code;
	}

}

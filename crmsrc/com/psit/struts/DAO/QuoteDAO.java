package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.Quote;
/**
 * 报价表的数据库操作接口类 <br>
 * create_date: Aug 9, 2010,10:11:51 AM<br>
 * @author 朱皖宁
 */
public interface QuoteDAO  
{
	/**
	 * 保存报价信息<br>
	 * create_date: Aug 9, 2010,10:20:18 AM<br>
	 * @param quote 报价记录对象<br>
	 */
	public void saveQuote(Quote quote);
	/**
	 * 根据Id获得报价<br>
	 * create_date: Aug 9, 2010,10:22:56 AM<br>
	 * @param id 报价记录id
	 * @return Quote 返回报价实体<br>
	 */
	public Quote showQuote(Long id);
	/**
	 * 更新报价信息<br>
	 * create_date: Aug 9, 2010,10:23:17 AM<br>
	 * @param quote 报价记录对象 <br>
	 */
	public void updateQuo(Quote quote);
	/**
	 * 删除机会报价<br>
	 * create_date: Aug 9, 2010,10:23:33 AM<br>
	 * @param quote 报价记录对象 <br>
	 */
	public void delQuote(Quote quote);
    /**
     * 获得待删除的所有报价记录<br>
     * create_date: Aug 9, 2010,10:23:46 AM<br>
     * @param pageCurrentNo 当前页码
     * @param pageSize 每页显示记录数
     * @return List 返回报价记录列表<br>
     */
	public List findDelQuote(int pageCurrentNo, int pageSize);
	/**
	 * 获得待删除的所有报价记录数量<br>
	 * create_date: Aug 9, 2010,10:23:57 AM<br>
	 * @return int 返回报价记录数量<br>
	 */
	public int findDelQuoteCount();
}
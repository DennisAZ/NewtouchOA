package com.psit.struts.DAO;

import com.psit.struts.entity.RNewLim;
/**
 * 
 * 新闻公告接收人DAO <br>
 *
 * create_date: Aug 17, 2010,1:49:01 PM<br>
 * @author zjr
 */
public interface RNewLimDAO  {
	/**
	 * 
	 * 保存新闻公告接收人 <br>
	 * create_date: Aug 6, 2010,1:46:29 PM <br>
	 * @param rnewLim 新闻接收人实体
	 */
	public void save(RNewLim rnewLim);
}
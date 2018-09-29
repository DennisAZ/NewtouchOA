package com.psit.struts.DAO;

import com.psit.struts.entity.RQuoPro;
/**
 * 
 * 报价明细DAO <br>
 *
 * create_date: Aug 17, 2010,2:20:31 PM<br>
 * @author zjr
 */
public interface RQuoProDAO {
	/**
	 * 
	 * 根据报价记录删除报价明细 <br>
	 * create_date: Aug 9, 2010,11:30:16 AM <br>
	 * @param quoId 报价id
	 */
	public void delByQuo(Long quoId);
	/**
	 * 
	 * 保存报价明细 <br>
	 * create_date: Aug 9, 2010,11:30:38 AM <br>
	 * @param rup 报价明细实体
	 */
	public void save(RQuoPro rup);
}

package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.RMessLim;
/**
 * 
 * 消息接收人DAO <br>
 *
 * create_date: Aug 17, 2010,11:52:52 AM<br>
 * @author zjr
 */
public interface RMessLimDAO 
{
	/**
	 * 
	 * 保存接收人 <br>
	 * create_date: Aug 6, 2010,12:03:40 PM <br>
	 * @param rmessLim 接收人实体
	 */
	public void save(RMessLim rmessLim);
	/**
	 * 
	 * 得到接收人实体 <br>
	 * create_date: Aug 6, 2010,12:15:48 PM <br>
	 * @param id 接收人id
	 * @return RMessLim 返回接收人实体  
	 */
	public RMessLim getRMessLim(Long id);
	/**
	 * 
	 * 更新接收人消息 <br>
	 * create_date: Aug 6, 2010,12:16:20 PM <br>
	 * @param rmessLim 接收人实体
	 */
	public void update(RMessLim rmessLim);
	/**
	 * 
	 * 删除已收消息 <br>
	 * create_date: Aug 6, 2010,12:17:59 PM <br>
	 * @param rmessLim 接收人实体
	 */
	public void delRMessLim(RMessLim rmessLim);
	/**
	 * 
	 * 获得待删除的所有已收消息 <br>
	 * create_date: Aug 6, 2010,12:17:00 PM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回消息列表 
	 */
	public List findDelMail(int pageCurrentNo, int pageSize);
	/**
	 * 
	 * 获得待删除的所有已收消息数量 <br>
	 * create_date: Aug 6, 2010,12:17:32 PM <br>
	 * @return int 返回消息列表数量 
	 */
	public int findDelMailCount();
}

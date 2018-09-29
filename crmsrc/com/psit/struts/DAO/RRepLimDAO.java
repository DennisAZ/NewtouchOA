package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.RRepLim;
/**
 * 
 * 报告接收人DAO <br>
 *
 * create_date: Aug 17, 2010,2:27:46 PM<br>
 * @author zjr
 */
public interface RRepLimDAO 
{
	/**
	 * 
	 * 保存报告接收人 <br>
	 * create_date: Aug 6, 2010,11:31:10 AM <br>
	 * @param rrepLim 报告接收人实体
	 */
	public void save(RRepLim rrepLim);
	/**
	 * 
	 * 报告接受人实体 <br>
	 * create_date: Aug 6, 2010,11:38:34 AM <br>
	 * @param id 接受人id
	 * @return RRepLim 返回报告接受人实体
	 */
	public RRepLim getRRepLim(Long id);
	/**
	 * 
	 * 查看某一报告是否存在下一审批步骤 <br>
	 * @param appOrder 审批步骤
	 * @param repCode 报告id
	 * @return List 返回报告列表
	 */
	public List getRRepLim(Integer appOrder,String repCode);
	/**
	 * 
	 * 查看某一报告是否审批结束 <br>
	 * create_date: Aug 6, 2010,11:41:25 AM <br>
	 * @param repCode 报告id
	 * @return List 返回报告列表
	 */
	public List getRRepLim(String repCode);
	/**
	 * 
	 * 更新审批内容 <br>
	 * create_date: Aug 6, 2010,11:47:30 AM <br>
	 * @param rrepLim 报告接受人实体
	 */
	public void update(RRepLim rrepLim);
//    public void delRRepLim(RRepLim rrepLim);//删除已收报告
     
	
}

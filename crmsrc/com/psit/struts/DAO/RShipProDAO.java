package com.psit.struts.DAO;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import com.psit.struts.entity.ProdShipment;
import com.psit.struts.entity.RShipPro;
import com.psit.struts.form.RopShipForm;
import com.psit.struts.form.StatSalMForm;

public interface RShipProDAO {
	public void save(RShipPro transientInstance);
	
	/**
	 * 产品销售记录列表 <br>
	 * @param prodId 产品ID(=)
	 * @param orderItem
	 * @param isDe
	 * @param currentPage
	 * @param pageSize
	 * @return List<RShipPro>
	 */
	public List<RShipPro> listSalHistory(String prodId, int currentPage, int pageSize);
	public int listSalHistoryCount(String prodId);
	
	/**
	 * 统计产品提成分析列表 <br>
	 * @param args [0]:员工Id,[1]:产品Id,[2][3]:发货日期
	 * @param orderItem
	 * @param isDe
	 * @param currentPage
	 * @param pageSize
	 * @return List<RShipPro>
	 */
	public List<RShipPro> listShipPro(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);

	public int listShipProCount(String[]args);
	
	public List<RShipPro> list(String pshId);
	
	public List<RopShipForm> getRopShip(String ordId);
	
	public RShipPro findById(Long id);
	
	public RShipPro merge(RShipPro detachedInstance);
	
	public void delete(RShipPro persistentInstance);
	/**
	 * 产品提成分析<br>
	 * @param startDate	开始日期
	 * @param endDate	结束日期
	 * @param wprId 产品Id
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statPsalBack(String startDate, String endDate, String wprId);
	
	/**
	 * 产品分析<br>
	 * @param startDate,endDate	签单日期 
	 * @param startDate1,endDate1	发货日期 
	 * @param wprId 产品Id
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statProAnalyse(String startDate, String endDate, String wprId,String startDate1, String endDate1);
	
}
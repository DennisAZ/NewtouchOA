package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.ProdShipment;
import com.psit.struts.entity.RShipPro;
import com.psit.struts.form.StatSalMForm;

public interface ProdShipmentDAO {
	public Double getShipAmtByCus(String cusId);
	
	public void saveRsps(List<RShipPro> rspList,ProdShipment psh);
	public void updShipNums(List<RShipPro> toUpdRspList,List<RShipPro> toDelRspList,ProdShipment psh,double oldProdAmt);
	
	public void save(ProdShipment transientInstance);
	public void update(ProdShipment transientInstance);
	public void delete(ProdShipment persistentInstance);
	public ProdShipment findById(Long id);
	
	public List<ProdShipment> listProdShipment(String expName,String cusName,String ordNum, String corName,String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listProdShipmentCount(String expName,String cusName, String ordNum,String corName);
	
	public List<ProdShipment> listByOrdId(String ordId,String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listByOrdIdCount(String ordId);
	
	
	public List<ProdShipment> listByCusId(String[] args,String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listByCusIdCount(String[] args);
	/**
	 * 查询订单下发货金额和提成 <br>
	 * @param ordId
	 * @return Double[]
	 */
	public Double[] getProdAmtSum(String ordId);
	
	/**
	 * (月平均发货额)未达最低销售金额客户 <br>
	 * @param empIds	员工ID集合
	 * @param cusIds	客户ID集合
	 * @param statMonth	统计月
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statLowestSals(String empIds,String cusIds,String startDate, String endDate);
	
	/**
	 * 销售额在上月末未达到规定金额的单位 <br>
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> getLTsals();
	/**
	 * 提成金额统计 <br>
	 * @param empIds	员工ID集合
	 * @param cusIds	客户ID集合
	 * @param statMonth	统计月
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statSalBack(String empIds,String cusIds,String startDate, String endDate);
}
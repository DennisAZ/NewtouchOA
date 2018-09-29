package com.psit.struts.BIZ.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.psit.struts.BIZ.OrderBIZ;
import com.psit.struts.DAO.CusCorCusDAO;
import com.psit.struts.DAO.CusProdDAO;
import com.psit.struts.DAO.ProdSalBackDAO;
import com.psit.struts.DAO.ProdShipmentDAO;
import com.psit.struts.DAO.ROrdProDAO;
import com.psit.struts.DAO.RShipProDAO;
import com.psit.struts.DAO.SalOrderDAO;
import com.psit.struts.entity.CusCorCus;
import com.psit.struts.entity.CusProd;
import com.psit.struts.entity.OrdStatistic;
import com.psit.struts.entity.ProdSalBack;
import com.psit.struts.entity.ProdShipment;
import com.psit.struts.entity.ROrdPro;
import com.psit.struts.entity.RShipPro;
import com.psit.struts.entity.SalOrdCon;
import com.psit.struts.entity.StaTable;
import com.psit.struts.entity.StatOrd;
import com.psit.struts.entity.WmsProduct;
import com.psit.struts.form.RopShipForm;
import com.psit.struts.form.StatSalMForm;
import com.psit.struts.util.format.GetDate;
import com.psit.struts.util.format.StringFormat;

public class OrderBIZImpl implements OrderBIZ {
	SalOrderDAO orderDao = null;
	ROrdProDAO rordProDao=null;
	ProdShipmentDAO prodShipmentDAO = null;
	RShipProDAO rshipProDAO = null;
	ProdSalBackDAO prodSalBackDAO = null; 
	CusCorCusDAO cusDao = null;
	CusProdDAO cusProdDAO = null;
	


	/**
	 * 保存订单 <br>
	 */
	public void saveOrd(SalOrdCon order) {
		orderDao.save(order);
	}
	/**
	 * 修改订单 <br>
	 */
	public void modifyOrd(SalOrdCon order) {
		orderDao.update(order);
	}
	/**
	 * 删除订单（移至回收站）<br>
	 */
	public void invalidOrd(SalOrdCon order) {
		orderDao.invalid(order);
	}
	/**
	 * 恢复订单，更新客户已成单状态<br>
	 */
	public void recovery(SalOrdCon order){
		orderDao.recovery(order);
	}
	
	/**
	 * 订单合同列表<br>
	 */
	public List<SalOrdCon> listOrders(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		return orderDao.listOrders(args, orderItem, isDe, currentPage, pageSize);
	}
	public int listOrdersCount(String[]args){
		return orderDao.listOrdersCount(args);
	}
	
	/**
	 * 得到订单应收款 <br>
	 */
	public String getLeftMon(){
		return orderDao.getLeftMon();
	}

	/**
	 * 相应客户下订单列表<br>
	 * @param args 	[0]是否删除(1已删除,0未删除);  [1]客户Id;  [2]订单主题;  [3]订单号;  
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	 * @return List<SalOrdCon> 订单合同列表
	 */
	public List<SalOrdCon> listOrdersByCusId(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		return orderDao.listOrdersByCusId(args, orderItem, isDe, currentPage, pageSize);
	}
	public int listOrdersByCusId(String[]args){
		return orderDao.listOrdersByCusId(args);
	}
	
	/**
	 * 检查订单编号是否重复 <br>
	 */
	public boolean checkOrdCode(String code){
		List list = orderDao.searchByCode(code);
		if(list.size()>0){
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * 得到订单详情 <br>
	 */
	public SalOrdCon getOrdCon(String code){
		return orderDao.getOrdCon(code);
	}
	
	/**
	 * 订单产品明细列表 <br>
	 * @param ordId 	订单Id
	 * @return List<ROrdPro>
	 */
	public List<ROrdPro> listOrdPro(String ordId){
		return rordProDao.listOrdPro(ordId);
	}
	
	/**
	 * 得到订单总额 <br>
	 */
	public String getOrdMonSum(String userCode){
		return orderDao.getOrdMonSum(userCode);
	}
	/**
	 * 保存订单明细 <br>
	 */
	public void saveRop(List<ROrdPro> ropList, String sodId){
		List<ROrdPro> saveList = new ArrayList();
		Iterator<ROrdPro> toSaveIt = ropList.iterator();
		List<ROrdPro> oldList = rordProDao.findByOrd(sodId);
		Iterator<ROrdPro> oldIt = null;
		ROrdPro rordPro = null;
		ROrdPro oldRordPro = null;
		while(toSaveIt.hasNext()){
			rordPro = toSaveIt.next();
			oldIt = oldList.iterator();
			while(oldIt.hasNext()){
				oldRordPro = oldIt.next();
				if(oldRordPro.getWmsProduct().getWprId().equals(rordPro.getWmsProduct().getWprId())){
					rordPro.setRopShipNum(oldRordPro.getRopShipNum());
					rordPro.setRopReturnNum(oldRordPro.getRopReturnNum());
					oldList.remove(rordPro);
					break;
				}
			}
			saveList.add(rordPro);
		}
		
//		orderBiz.delByWCode(sodCode);//批量删除
		
		rordProDao.batSave(saveList,sodId);
	}
	/**
	 * 根据订单号查询订单明细 <br>
	 */
	public List findByOrd(String sodCode){
		return rordProDao.findByOrd(sodCode);
	}
	/**
	 * 根据订单明细Id查询订单明细 <br>
	 */
	public ROrdPro getRop(Long ropId){
		return rordProDao.getRop(ropId);
	}
	/**
	 * 更新订单明细 <br>
	 */
	public void updateRop(ROrdPro rordPro){
		rordProDao.updateRop(rordPro);
	}
	/**
	 * 删除订单明细 <br>
	 */
	public void deleteRop(ROrdPro rordPro){
		rordProDao.delete(rordPro);
	}
	/**
	 * 根据商品id查询订单 <br>
	 */
	public List<SalOrdCon> getOrdersByProd(String prodId){
		return rordProDao.getOrdersByProd(prodId);
	}
	/**
	 * 根据订单id和商品id查询订单明细 <br>
	 */
	public List findByWpr(String sodCode,Long wprCode){
		return rordProDao.findByWpr(sodCode, wprCode);
	}
	/**
	 * 根据订单id查询订单明细仓储信息 <br>
	 */
	public List findByStro(String sodCode){
		return rordProDao.findByStro(sodCode);
	}
	/**
	 * 根据商品id查询订单明细 <br>
	 */
	public List findByWpr(Long wprCode){
		return rordProDao.findByWpr(wprCode);
	}
	/**
	 * 得到当月订单总额 <br>
	 */
	public String relOrdMonSum(String empId){
		return orderDao.relOrdMonSum(empId);
	}
	
	/**
	 * 得到某人的订单总额或回款总额<br>
	 */
	public String getOrdSumWithUNum(String userCode,String isAll,String type){
		return orderDao.getOrdSumWithUNum(userCode,isAll,type);
	}
	/**
	 * 查询某人当月已成客户数量 <br>
	 */
	public int relCusCount(String empId){
		return orderDao.relCusCount(empId);
	}
	/**
	 * 订单高级查询列表数量 <br>
	 */
	public int getSupCount(String hql,String isAll,String userNum,String sodAppIsok){
		return orderDao.getSupCount(hql,isAll,userNum,sodAppIsok);
	}
	/**
	 * 订单高级查询列表 <br>
	 */
	public List<SalOrdCon> ordSupSearch(String hql,String isAll,String userNum,String sodAppIsok,int pageCurNo,int pageSize){
		return orderDao.ordSupSearch(hql,isAll,userNum,sodAppIsok,pageCurNo, pageSize);
	}
	/**
	 * 年度20大客户排行 <br>
	 */
	public List<OrdStatistic>  topSalCus(String type){
		return orderDao.topSalCus(type);
	}
	
	/**
	 * 统计表格中订单列表 <br>
	 */
	public List<SalOrdCon> listStatOrd(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		return orderDao.listStatOrd(args, orderItem, isDe, currentPage, pageSize);
	}
	public int listStatOrdCount(String[]args){
		return orderDao.listStatOrdCount(args);
	}
	
	/**
	 * 销售金额统计 <br>
	 * @param empIds	员工ID集合
	 * @param cusIds	客户ID集合
	 * @param startDate	统计日期
	 * @param endDate
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statSalM(String empIds,String cusIds,String startDate,String endDate){
		return orderDao.statSalM(StringFormat.removeLastSplitWord(empIds, ","),cusIds,startDate,endDate);
	}
	
	/**
	 * (月平均发货额)未达最低销售金额客户 <br>
	 * @param empIds	员工ID集合
	 * @param cusIds	客户ID集合
	 * @param statMonth	统计月
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statLowestSals(String empIds,String cusIds,String startDate,String endDate){
		return prodShipmentDAO.statLowestSals(StringFormat.removeLastSplitWord(empIds, ","),cusIds,startDate,endDate);
	}
	
	
	/**
	 * 销售额在上月末未达到规定金额的单位 <br>
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> getLTsals(){
		return prodShipmentDAO.getLTsals();
	}
	/**
	 * 业务提成金额统计 <br>
	 * @param empIds	员工ID集合
	 * @param cusIds	客户ID集合
	 * @param statMonth	统计月
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statSalBack(String empIds,String cusIds,String startDate,String endDate){
		return prodShipmentDAO.statSalBack(StringFormat.removeLastSplitWord(empIds, ","),cusIds,startDate,endDate);
	}
	
	/**
	 * 订单类别统计 <br>
	 */
	public List<StatOrd> statOrdType(String[]args){
		return orderDao.statOrdType(args);
	}
	/**
	 * 订单来源统计 <br>
	 */
	public List<StatOrd> statOrdSou(String[]args){
		return orderDao.statOrdSou(args);
	}
	/**
	 * 订单月度分布统计 <br>
	 */
	public StaTable statOrdEmpMon(String[]args){
		return orderDao.statOrdEmpMon(GetDate.getMonths(args[0], args[1]),new String[]{args[2]});
	}
	/**
	 * 订单月度统计 <br>
	 */
	public List<StatOrd> statOrdMon(String[]args){
		List<StatOrd> statResult = new ArrayList<StatOrd>() ;
		String[] months = GetDate.getMonths(args[0], args[1]);
		List<StatOrd> statList = orderDao.statOrdMon(args);
		Iterator<StatOrd> statIt = statList.iterator();
		StatOrd ordStatObj = null;
		if(statIt.hasNext()){
			ordStatObj = statIt.next();
		}
		for(int i = 0; i<months.length; i++){
			String month = months[i];
			Long ordCount = 0l;
			Double ordSum = 0.0;
			if(ordStatObj!=null&&ordStatObj.getTypName().equals(month)){
				ordCount = ordStatObj.getCount();
				ordSum = ordStatObj.getSum();
				if(statIt.hasNext()){
					ordStatObj = statIt.next();
				}
				else{
					ordStatObj = null;
				}
			}
			statResult.add(new StatOrd(month,ordCount,ordSum));
		}
		return statResult;
	}
	
	/**
	 * 订单金额月份统计 <br>
	 */
	public List sumMonByDate(String hql){
		return orderDao.sumMonByDate(hql);
	}
	
	/**
	 * 工作台发货提醒 <br>
	 */
	public List<SalOrdCon> getProdTipList(String filter){
		return orderDao.getProdTipList(filter);
	}
	/**
	 * 订单统计数据挖掘查询列表数量 <br>
	 */
	public int searchByStatCount(String statType,String orderType,String ordConDate,String user,String queryString){
		return orderDao.searchByStatCount(statType,orderType, ordConDate,user,queryString);
	}
	/**
	 * 订单统计数据挖掘查询列表 <br>
	 */
	public List searchByStat(String statType,String orderType,String ordConDate,String user,String queryString,int currentPage, int pageSize){
		return orderDao.searchByStat(statType,orderType, ordConDate,user,queryString, currentPage, pageSize);
	}
	/**
	 * 工作台销售业绩图表hql语句 <br>
	 */
	public String lastSalStat(Long empId,String date1,String date2){
		return orderDao.lastSalStat(empId, date1, date2);
	}

	/**
	 * 查看某商品销售历史列表数量 <br>
	 */
	public int getCountByWpr(Long wprCode){
		return rordProDao.getCountByWpr(wprCode);
	}
	/**
	 * 根据商品id查询订单明细 <br>
	 */
	public List<ROrdPro> findByWpr(Long wprCode,int currentPage,int pageSize){
		return rordProDao.findByWpr(wprCode, currentPage, pageSize);
	}
	/**
	 * 实际销售合计（按每年每人） <br>
	 */
	public List<OrdStatistic> allOrdSumByFct(String uCode,String tarName,String tarYear){
		return orderDao.allOrdSumByFct(uCode, tarName, tarYear);
	}
	/**
	 * 实际销售合计（按年） <br>
	 */
	public List<OrdStatistic> allOrdSumByFct2(String uCode,String tarName,String tarYear){
		return orderDao.allOrdSumByFct2(uCode,tarName, tarYear);
	}
	/**
	 * 获得待删除的所有订单列表数量 <br>
	 */
	public List findDelOrder(String orderItem, String isDe, int pageCurrentNo, int pageSize) {
		return orderDao.findDelOrder(orderItem, isDe, pageCurrentNo, pageSize);
	}
	public int findDelOrderCount() {
		return orderDao.findDelOrderCount();
	}
	/**
	 * 根据订单Id得到订单 <br>
	 */
	public SalOrdCon getSalOrder(Long id) {
		return orderDao.getSalOrder(id);
	}
	/**
	 * 删除订单 <br>
	 */
	public void delete(SalOrdCon persistentInstance) {
		orderDao.delete(persistentInstance);
	}
	/**
	 * 修改订单 <br>
	 */
	public void update(SalOrdCon instance) {
		orderDao.update(instance);
	}

	/**
	 * 通过客户Id查找订单明细<br>
	 * @param cusId 客户Id
	 * @return List<ROrdPro> 订单明细列表<br>
	 */
	public List<ROrdPro> findByCusId(String cusId, String orderItem,
			String isDe, int currentPage, int pageSize){
		return rordProDao.findByCusId(cusId, orderItem, isDe, currentPage, pageSize);
	}
	
	/**
	 *  通过客户Id查找订单明细的数量<br>
	 * @param cusId 客户Id
	 * @return 订单明细的数量
	 */
	public int findByCusIdCount(String cusId){
		return rordProDao.findByCusIdCount(cusId);
	}
	
	public List<ProdShipment> listProdShipment(String expName,String cusName,String ordNum,String corName, String orderItem,
			String isDe, int currentPage, int pageSize){
		return prodShipmentDAO.listProdShipment(expName, cusName, ordNum, corName, orderItem, isDe, currentPage, pageSize);
	}
	public int listProdShipmentCount(String expName,String cusName,String ordNum,String corName){
		return prodShipmentDAO.listProdShipmentCount(expName, cusName, ordNum,corName);
	}
	
	public List<ProdShipment> listByCusId(String[] args,String orderItem,
			String isDe, int currentPage, int pageSize){
		return prodShipmentDAO.listByCusId(args, orderItem, isDe, currentPage, pageSize);
	}
	public int listByCusIdCount(String[] args){
		return prodShipmentDAO.listByCusIdCount(args);
	}
	
	public List<ProdShipment> listPShipmentByOrd(String ordId, String orderItem,
			String isDe, int currentPage, int pageSize){
		return prodShipmentDAO.listByOrdId(ordId, orderItem, isDe, currentPage, pageSize);
	}
	public int listPShipmentByOrdCount(String ordId){
		return prodShipmentDAO.listByOrdIdCount(ordId);
	}
	
	public List<RShipPro> listRShipProByPsh(String pshId){
		return rshipProDAO.list(pshId);
	}
	public List<RopShipForm> getRopShip(String ordId){
		return rshipProDAO.getRopShip(ordId);
	}
	
	public ProdShipment findProdShipment(String pshId){
		return prodShipmentDAO.findById(Long.parseLong(pshId));
	}
	public void saveProdShipment(ProdShipment prodShipment,String ordId){
		/**SalOrdCon salOrdCon = getOrdCon(ordId);
		CusCorCus cusCorCus = salOrdCon.getCusCorCus();
		if(cusCorCus.getCorRecDate() ==null && prodShipment.getPshDeliveryDate() !=null){
			Date d = GetDate.getDate(GetDate.parseStrDate(prodShipment.getPshDeliveryDate()),Integer.parseInt(cusCorCus.getCorAging()));
			cusCorCus.setCorRecDate(d);
			cusDao.update(cusCorCus);
		}*/
		prodShipmentDAO.save(prodShipment);
	}
	public void updProdShipment(ProdShipment prodShipment){
		prodShipmentDAO.update(prodShipment);
	}
	
	
	
	public void deleteProdShipment(String pshId){
		prodShipmentDAO.delete(prodShipmentDAO.findById(Long.parseLong(pshId)));
//		prodShipmentDAO.deleteById(pshId);
	}
	
	public void savePshProd(String pshId, String[] prodIdsArr,
			String[] transNums, String[] transNum1s, String[] transNum2s,
			String[] transAmt1s, String[] transAmt2s, String[] transUnit1s,
			String[] transUnit2s, String[] prodAmts, String totalAmt,String[] outNums) {
		Double totalProdAmt = 0.0, totalSalBack=0.0, ropAmt = 0.0, salBack = 0.0;
		ProdShipment psh = prodShipmentDAO.findById(Long.parseLong(pshId));
		if(psh!=null){
			List<ROrdPro> rOrdProList = rordProDao.findByOrd(String.valueOf(psh.getPshOrder().getSodCode()));
			List<RShipPro> rShipProList = new ArrayList<RShipPro>();
//			List<ROrdPro> saveRopList = new ArrayList<ROrdPro>();
			ROrdPro rOrdPro = null;
			RShipPro rShipPro = null;
			for(int i=0; i<prodIdsArr.length; i++){
				if(!StringFormat.isEmpty(transNums[i])){
					List<CusProd> prodList = cusProdDAO.listByWprId(prodIdsArr[i]);
					for(int n=0;n<prodList.size();n++){
						if(psh.getPshDeliveryDate() !=null){
							prodList.get(n).setCpSentDate(psh.getPshDeliveryDate());
						}else{
							prodList.get(n).setCpSentDate(null);
						}
					}

					
					rShipPro = new RShipPro();
					rShipPro.setRshpShipment(new ProdShipment(Long.parseLong(pshId)));
					rShipPro.setRshpCount(Double.valueOf(transNums[i]));
					if(outNums[i] !=null && !outNums[i].equals("")){
						rShipPro.setRshpOutCount(Double.valueOf(outNums[i]));
					}else{
						rShipPro.setRshpOutCount(0.0);
					}
					if(!StringFormat.isEmpty(prodIdsArr[i])){
						rShipPro.setRshpProd(new WmsProduct(prodIdsArr[i]));
					}
					if(!StringFormat.isEmpty(transNum1s[i])){
						rShipPro.setRshpPackCount1(Double.valueOf(transNum1s[i]));
					}
					else{
						rShipPro.setRshpPackCount1(0.0);
					}
					if(!StringFormat.isEmpty(transNum2s[i])){
						rShipPro.setRshpPackCount2(Double.valueOf(transNum2s[i]));
					}
					else{
						rShipPro.setRshpPackCount2(0.0);
					}
					if(!StringFormat.isEmpty(transAmt1s[i])){
						rShipPro.setRshpAmt1(Double.valueOf(transAmt1s[i]));
					}
					else{
						rShipPro.setRshpAmt1(0.0);
					}
					if(!StringFormat.isEmpty(transAmt2s[i])){
						rShipPro.setRshpAmt2(Double.valueOf(transAmt2s[i]));
					}
					else{
						rShipPro.setRshpAmt2(0.0);
					}
					if(!StringFormat.isEmpty(transUnit1s[i])){
						rShipPro.setRshpUnit1(transUnit1s[i]);
					}
					if(!StringFormat.isEmpty(transUnit2s[i])){
						rShipPro.setRshpUnit2(transUnit2s[i]);
					}
					for(int j=0;j<prodIdsArr.length;j++){
						rOrdPro = rOrdProList.get(j);
						if(rOrdPro.getWmsProduct()!=null && rOrdPro.getWmsProduct().getWprId().toString().equals(prodIdsArr[i])){
							ropAmt = Double.parseDouble(prodAmts[i]);
							rShipPro.setRshpPrice(rOrdPro.getRopPrice());
							rShipPro.setRshpTax(rOrdPro.getRopZk());
							salBack = getSalBack(rOrdPro.getWmsProduct(),rOrdPro.getRopPrice(),rOrdPro.getRopZk(),Double.valueOf(transNums[i]));
							if(salBack>0){
								rShipPro.setRshpSalBack(salBack);
								totalSalBack += salBack;
							}
							if(ropAmt>0){
								rShipPro.setRshpProdAmt(ropAmt);
								totalProdAmt += ropAmt;
							}
//							rOrdPro.setRopShipNum((rOrdPro.getRopShipNum()!=null?rOrdPro.getRopShipNum():0)+Double.valueOf(transNums[i]));
//							saveRopList.add(rOrdPro);
							rOrdProList.remove(j);
							break;
						}
					}
					rShipProList.add(rShipPro);
				}
			}
			psh.setPshAmt((!StringFormat.isEmpty(totalAmt)?Double.valueOf(totalAmt):0));
			psh.setPshProdAmt(totalProdAmt);
			psh.setPshSalBack(totalSalBack);
			
			prodShipmentDAO.saveRsps(rShipProList,psh);
		}
	}
	//提成计算
	private Double getSalBack(WmsProduct prod,Double ropPrc,String ropTax,Double prodNum){
		Double prodPrc = prod.getWprSalePrc()>0?prod.getWprSalePrc():0;//标准价格
		Double salBack = 0.0;
		if(ropPrc>0&&prodNum>0){
			Double taxPrc = ropPrc*(1+(!StringFormat.isEmpty(ropTax)&&Double.parseDouble(ropTax)>0?Double.parseDouble(ropTax):0));//不含税需转成含税价
			if(taxPrc>=prodPrc){
				salBack=prodPrc*prodNum*((prod.getWprNormalPer()!=null&&prod.getWprNormalPer()>0)?prod.getWprNormalPer():0)
						+(taxPrc-prodPrc)*prodNum*((prod.getWprOverPer()!=null&&prod.getWprOverPer()>0)?prod.getWprOverPer():0);
			}
			else{
				List<ProdSalBack> psbList = prodSalBackDAO.listByProdId(prod.getWprId().toString());
				Iterator<ProdSalBack> psbIt = psbList.iterator();
				ProdSalBack psb = null;
				float lowRate = 0;
				while(psbIt.hasNext()){
					psb = psbIt.next();
					if(psb.getPsbPrice()!=null&&taxPrc>=psb.getPsbPrice()){
						lowRate = psb.getPsbRate();
					}
				}
				salBack=taxPrc*prodNum*lowRate;
			}
		}
		return salBack;
	}
	
	/**
	 * 查询订单下发货明细发货金额和提成 <br>
	 * @param ordId
	 * @return Double[]
	 */
	public Double[] getProdAmtSum(String ordId){
		return prodShipmentDAO.getProdAmtSum(ordId);
	}
	
	public void updShipNums(String[] rspIdsArr,String[] prodNums,String[] prodOutNums,String[] shipNum1s,String[] shipNum2s){
		RShipPro rsp = null;
		double oldProdAmt = 0;
		double prodNum = 0.0, oldProdNum = 0.0, shipNum1 = 0.0, shipNum2 = 0.0 ,prodOutNum = 0.0;
		double totalPshProdAmt = 0.0, totalPshAmt = 0.0, totalPshSalBack = 0.0;
		ProdShipment psh = null;
		List<RShipPro> toUpdRspList = new ArrayList<RShipPro>(), toDelRspList = new ArrayList<RShipPro>();
		for(int i=0; i<rspIdsArr.length; i++){
			rsp = rshipProDAO.findById(Long.parseLong(rspIdsArr[i]));
			if(psh == null){ 
				psh = rsp.getRshpShipment();
				oldProdAmt = psh.getPshProdAmt();
			}
			if(!StringFormat.isEmpty(prodNums[i])&&Double.valueOf(prodNums[i])>0){
				if(!StringFormat.isEmpty(prodNums[i])){ prodNum = Double.valueOf(prodNums[i]); }
				if(!StringFormat.isEmpty(prodOutNums[i])){ prodOutNum = Double.valueOf(prodOutNums[i]); }
				if(!StringFormat.isEmpty(shipNum1s[i])){ shipNum1 = Double.valueOf(shipNum1s[i]); }
				if(!StringFormat.isEmpty(shipNum2s[i])){ shipNum2 = Double.valueOf(shipNum2s[i]); }
				oldProdNum = rsp.getRshpCount();
				rsp.setRshpCount(Double.valueOf(prodNums[i]));
				if(prodOutNums[i] !=null && !prodOutNums[i].equals("")){
					rsp.setRshpOutCount(Double.valueOf(prodOutNums[i]));
				}else{
					rsp.setRshpOutCount(0.0);
				}
				
				rsp.setRshpProdAmt(((rsp.getRshpProdAmt()!=null?rsp.getRshpProdAmt():0)/oldProdNum)*prodNum);
				rsp.setRshpSalBack(((rsp.getRshpSalBack()!=null?rsp.getRshpSalBack():0)/oldProdNum)*prodNum);
				rsp.setRshpPackCount1(shipNum1);
				rsp.setRshpPackCount2(shipNum2);
				totalPshProdAmt += rsp.getRshpProdAmt();
				totalPshSalBack += rsp.getRshpSalBack();
				totalPshAmt += shipNum1*(rsp.getRshpAmt1()!=null?rsp.getRshpAmt1():0)+shipNum2*(rsp.getRshpAmt2()!=null?rsp.getRshpAmt2():0);
				toUpdRspList.add(rsp);
			}
			else {
				toDelRspList.add(rsp);
			}
		}
		if(psh!=null){
			psh.setPshProdAmt(totalPshProdAmt);
			psh.setPshAmt(totalPshAmt);
			psh.setPshSalBack(totalPshSalBack);
		}
		prodShipmentDAO.updShipNums(toUpdRspList, toDelRspList, psh, oldProdAmt);
	}
	
	/**
	 * 产品销售记录列表 <br>
	 * @param prodId 产品ID(=)
	 * @param orderItem
	 * @param isDe
	 * @param currentPage
	 * @param pageSize
	 * @return List<RShipPro>
	 */
	public List<RShipPro> listSalHistory(String prodId, int currentPage, int pageSize){
		return rshipProDAO.listSalHistory(prodId, currentPage, pageSize);
	}
	
	
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
			String isDe, int currentPage, int pageSize){
		return rshipProDAO.listShipPro(args, orderItem, isDe, currentPage, pageSize);
	}

	public int listShipProCount(String[]args){
		return rshipProDAO.listShipProCount(args);
	}
	/**
	 * 产品提成分析<br>
	 * @param startDate	开始日期
	 * @param endDate	结束日期
	 * @param wprId 产品Id
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statPsalBack(String startDate, String endDate, String wprId){
		return rshipProDAO.statPsalBack(startDate, endDate, wprId);
	}
	
	/**
	 * 产品分析<br>
	 * @param startDate,endDate	签单日期 
	 * @param startDate1,endDate1	发货日期 
	 * @param wprId 产品Id
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statProAnalyse(String startDate, String endDate, String wprId,String startDate1, String endDate1){
		return rshipProDAO.statProAnalyse(startDate, endDate, wprId, startDate1, endDate1);
	}
	public int listSalHistoryCount(String prodId){
		return rshipProDAO.listSalHistoryCount(prodId);
	}
	
	public void setOrderDao(SalOrderDAO orderDao) {
		this.orderDao = orderDao;
	}
	public void setRordProDao(ROrdProDAO rordProDao) {
		this.rordProDao = rordProDao;
	}
	public void setProdShipmentDAO(ProdShipmentDAO prodShipmentDAO) {
		this.prodShipmentDAO = prodShipmentDAO;
	}
	public void setRshipProDAO(RShipProDAO rshipProDAO) {
		this.rshipProDAO = rshipProDAO;
	}
	public void setProdSalBackDAO(ProdSalBackDAO prodSalBackDAO) {
		this.prodSalBackDAO = prodSalBackDAO;
	}
	
	public void setCusDao(CusCorCusDAO cusDao) {
		this.cusDao = cusDao;
	}
	
	public void setCusProdDAO(CusProdDAO cusProdDAO) {
		this.cusProdDAO = cusProdDAO;
	}
}
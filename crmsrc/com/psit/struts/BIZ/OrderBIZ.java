package com.psit.struts.BIZ;

import java.util.List;

import com.psit.struts.entity.OrdStatistic;
import com.psit.struts.entity.ProdShipment;
import com.psit.struts.entity.ROrdPro;
import com.psit.struts.entity.RShipPro;
import com.psit.struts.entity.SalOrdCon;
import com.psit.struts.entity.StaTable;
import com.psit.struts.entity.StatOrd;
import com.psit.struts.form.RopShipForm;
import com.psit.struts.form.StatSalMForm;

public interface OrderBIZ {
	/**
	 * 保存订单 <br>
	 * @param order 订单实体
	 */
	public void saveOrd(SalOrdCon order);
	/**
	 * 修改订单 <br>
	 * @param order 订单实体
	 */
	public void modifyOrd(SalOrdCon order);
	/**
	 * 删除订单（移至回收站），更新客户已成单状态 <br>
	 * @param order 订单实体
	 */
	public void invalidOrd(SalOrdCon order);
	/**
	 * 恢复订单，更新客户已成单状态<br>
	 * @param order 订单实体
	 */
	public void recovery(SalOrdCon order);
	
	/**
	 * 订单合同列表<br>
	 * @param args 	[0]是否删除(1已删除,0未删除);  [1]审核状态(0待审,1已审);  [2]订单主题;  [3]订单号;  
	 * 				[4]客户ID;  [5]签订日期(开始);  [6]签订日期(结束);  [7]列表筛选类型;[8]签单人
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	 * @return List<SalOrdCon> 订单合同列表
	 */
	public List<SalOrdCon> listOrders(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listOrdersCount(String[]args);
	/**
	 * 得到订单应收款 <br>
	 * @return String 应收款
	 */
	public String getLeftMon();
	
	/**
	 * 检查订单编号是否重复 <br>
	 * @param code 订单编号
	 * @return boolean 重复返回true,不重复返回false
	 */
	public boolean checkOrdCode(String code);
	/**
	 * 得到订单详情 <br>
	 * @param code 订单id
	 * @return SalOrdCon 返回订单实体
	 */
	public SalOrdCon getOrdCon(String code);
	
	/**
	 * 订单产品明细列表 <br>
	 * @param ordId 	订单Id
	 * @return List<ROrdPro>
	 */
	public List<ROrdPro> listOrdPro(String ordId);
	
	/**
	 * 订单高级查询列表数量 <br>
	 * @param hql 查询语句
	 * @param isAll 标识查询范围0表示查询我的订单1表示查询我的下属订单2表示全部订单
	 * @param userNum 用户码
	 * @param sodAppIsok 是否审核
	 * @return int 返回订单列表数量
	 */
	public int getSupCount(String hql,String isAll,String userNum,String sodAppIsok);
	/**
	 * 订单高级查询列表 <br>
	 * @param hql 查询语句
	 * @param isAll 标识查询范围0表示查询我的订单1表示查询我的下属订单2表示全部订单
	 * @param userNum 用户码
	 * @param sodAppIsok 是否审核
	 * @param pageCurNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回订单列表
	 */
	public List<SalOrdCon> ordSupSearch(String hql,String isAll,String userNum,String sodAppIsok,int pageCurNo,int pageSize);
	/**
	 * 得到订单总额 <br>
	 * @param userCode 账号
	 * @return String 返回订单总额
	 */
	public String getOrdMonSum(String userCode);
	
	/**
	 * 保存订单明细 <br>
	 * @param ropList 待保存明细列表
	 * @param sodId	订单ID
	 */
	public void saveRop(List<ROrdPro> ropList,String sodId);
	/**
	 * 根据订单号查询订单明细 <br>
	 * @param sodCode 订单id 
	 * @return List 返回订单明细列表
	 */
	public List findByOrd(String sodCode);
	/**
	 * 根据订单明细Id查询订单明细 <br>
	 * @param ropId 订单明细Id
	 * @return ROrdPro 返回订单明细实体
	 */
	public ROrdPro getRop(Long ropId);
	/**
	 * 更新订单明细 <br>
	 * @param rordPro 订单明细实体
	 */
	public void updateRop(ROrdPro rordPro);
	/**
	 * 删除订单明细 <br>
	 * @param rordPro 订单明细实体
	 */
	public void deleteRop(ROrdPro rordPro);
	/**
	 * 根据商品id查询订单 <br>
	 * @param prodId 商品ID
	 * @return List<Long> 订单ID集合
	 */
	public List<SalOrdCon> getOrdersByProd(String prodId);
	/**
	 * 根据订单id和商品id查询订单明细 <br>
	 * @param sodCode 订单id
	 * @param wprCode 产品id
	 * @return List 返回订单明细列表
	 */
	public List findByWpr(String sodCode,Long wprCode);
	/**
	 * 根据订单id查询订单明细仓储信息 <br>
	 * @param sodCode 订单id
	 * @return List 返回订单明细仓储列表
	 */
	public List findByStro(String sodCode);
	/**
	 * 根据商品id查询订单明细 <br>
	 * @param wprCode 商品id
	 * @return List 返回订单明细列表
	 */
	public List findByWpr(Long wprCode);
	/**
	 * 获得待删除的所有订单列表 <br>
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回订单列表
	 */
	public List<SalOrdCon> findDelOrder(String orderItem, String isDe, int pageCurrentNo, int pageSize);
	public int findDelOrderCount();
	/**
	 * 根据订单Id得到订单 <br>
	 * @param id 订单id
	 * @return SalOrdCon 返回订单实体
	 */
	public SalOrdCon getSalOrder(Long id);
	/**
	 * 删除订单 <br>
	 * @param persistentInstance 订单实体
	 */
	public void delete(SalOrdCon persistentInstance);
	/**
	 * 修改订单 <br>
	 * @param instance 订单实体
	 */
	public void update(SalOrdCon instance);
	/**
	 * 得到当月订单总额 <br>
	 * @param empId 员工id
	 * @return String 返回当月订单总额
	 */
	public String relOrdMonSum(String empId);
	
	/**
	 * 得到某人的订单总额或回款总额<br>
	 * @param userCode 用户Id或用户码
	 * @param isAll 是否是全部
	 * @param type 查询类别(0为查询订单总额，1为查询回款总额)
	 * @return String 订单或回款总额
	 */
	public String getOrdSumWithUNum(String userCode,String isAll,String type);
	/**
	 * 查询某人当月已成客户数量 <br>
	 * @param empId 员工id
	 * @return int 返回已成客户数量
	 */
	public int relCusCount(String empId);
	
	/**
	 * 统计表格中订单列表 <br>
	 * @param args 	[0]是否删除(1未删除,0已删除);  [2]统计类型; [3]统计对象; [4]员工ID集合;  [5][6]签订日期;
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	 * @return List<SalOrdCon> 订单列表列表
	 */
	public List<SalOrdCon> listStatOrd(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listStatOrdCount(String[]args);
	
	/**
	 * 销售金额统计 <br>
	 * @param empIds	员工ID集合
	 * @param cusIds	客户ID集合
	 * @param startDate	统计日期
	 * @param endDate
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statSalM(String empIds,String cusIds,String startDate,String endDate);
	
	/**
	 * (月平均发货额)未达最低销售金额客户 <br>
	 * @param empIds	员工ID集合
	 * @param cusIds	客户ID集合
	 * @param statMonth	统计月
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statLowestSals(String empIds,String cusIds,String startDate,String endDate);
	
	/**
	 * 业务提成金额统计 <br>
	 * @param empIds	员工ID集合
	 * @param cusIds	客户ID集合
	 * @param statMonth	统计月
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statSalBack(String empIds,String cusIds,String startDate,String endDate);
	
	/**
	 * 订单类别统计 <br>
	 * @param args [0][1]签订日期
	 * @return List<StatOrd> 统计结果
	 */
	public List<StatOrd> statOrdType(String[]args);	
	/**
	 * 订单来源统计 <br>
	 * @param args [0][1]签订日期
	 * @return List<StatOrd> 统计结果
	 */
	public List<StatOrd> statOrdSou(String[]args);
	/**
	 * 订单人员月度分布统计 <br>
	 * @param args [0][1]签订月份, [2]员工ID集合
	 * @return StaTable 统计结果
	 */
	public StaTable statOrdEmpMon(String[]args);
	/**
	 * 订单月度统计 <br>
	 * @param args [0][1]签订日期月份区间; [2]员工ID集合;
	 * @return List<StatOrd> 统计结果
	 */
	public List<StatOrd> statOrdMon(String[]args);
	
	/**
	 * 订单金额月份统计 <br>
	 * @param hql 查询语句
	 * @return List 返回订单金额月份统计列表
	 */
	public List sumMonByDate(String hql);
	/**
	 * 订单统计数据挖掘查询列表数量 <br>
	 * @param statType 统计类型
	 * @param orderType 订单类别
	 * @param ordConDate 签单日期
	 * @param user 负责账号
	 * @param queryString 查询语句
	 * @return int 返回订单统计数据挖掘查询列表数量
	 */
	public int searchByStatCount(String statType,String orderType,String ordConDate,String user,String queryString);
	/**
	 * 订单统计数据挖掘查询列表 <br>
	 * @param statType 统计类型
	 * @param orderType 订单类别
	 * @param ordConDate 签单日期
	 * @param user 负责账号
	 * @param queryString 查询语句
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回订单统计数据挖掘查询列表
	 */
	public List<SalOrdCon> searchByStat(String statType,String orderType,String ordConDate,String user,String queryString,int currentPage, int pageSize);
	/**
	 * 年度20大客户排行 <br>
	 * @param type 金额类型(回款额，销售额)
	 * @return List 返回订单统计列表
	 */
	public List<OrdStatistic>  topSalCus(String type);
	/**
	 * 工作台销售业绩图表hql语句 <br>
	 * @param empId 员工id
	 * @param date1 签单日期(开始)
	 * @param date2 签单日期(结束)
	 * @return String 返回查询语句
	 */
	public String lastSalStat(Long empId,String date1,String date2);
	/**
	 * 查看某商品销售历史列表数量 <br>
	 * @param wprCode 商品Id
	 * @return int返回 销售历史列表数量
	 */
	public int getCountByWpr(Long wprCode);
	/**
	 * 查看某商品销售历史列表 <br>
	 * @param wprCode 商品Id
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回销售历史列表
	 */
	public List<ROrdPro> findByWpr(Long wprCode,int currentPage,int pageSize);

	/**
	 * 实际销售合计（按每年每人） <br>
	 * @param uCode 员工id
	 * @param tarName 统计金额类型
	 * @param tarYear 年份
	 * @return List 返回订单合计列表
	 */
	public List<OrdStatistic> allOrdSumByFct(String uCode,String tarName,String tarYear);
	/**
	 * 实际销售合计（按年） <br>
	 * @param uCode 员工id
	 * @param tarName 统计金额类型
	 * @param tarYear 年份
	 * @return List 返回订单合计列表
	 */
	public List<OrdStatistic> allOrdSumByFct2(String uCode,String tarName,String tarYear);
	
	/**
	 * 通过客户Id查找订单明细<br>
	 * @param cusId 客户Id
	 * @return List<ROrdPro> 订单明细列表<br>
	 */
	public List<ROrdPro> findByCusId(String cusId, String orderItem,
			String isDe, int currentPage, int pageSize);
	
	/**
	 *  通过客户Id查找订单明细的数量<br>
	 * @param cusId 客户Id
	 * @return 订单明细的数量
	 */
	public int findByCusIdCount(String cusId);
	
	public List<ProdShipment> listProdShipment(String expName,String cusName,String ordNum, String corName, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listProdShipmentCount(String expName,String cusName,String ordNum, String corName);
	
	public List<ProdShipment> listPShipmentByOrd(String ordId, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listPShipmentByOrdCount(String ordId);
	
	public List<RShipPro> listRShipProByPsh(String pshId);
	public List<RopShipForm> getRopShip(String ordId);
	
	public ProdShipment findProdShipment(String pshId);
	public void saveProdShipment(ProdShipment prodShipment,String ordId);
	public void updProdShipment(ProdShipment prodShipment);
	public void deleteProdShipment(String pshId);
	
	
	public void savePshProd(String pshId, String[] prodIdsArr,
			String[] transNums, String[] transNum1s, String[] transNum2s,
			String[] transAmt1s, String[] transAmt2s, String[] transUnit1s,
			String[] transUnit2s, String[] prodAmts, String totalAmt,String[] outNums);
	
	/**
	 * 查询订单下发货明细发货金额和提成 <br>
	 * @param ordId
	 * @return Double[]
	 */
	public Double[] getProdAmtSum(String ordId);
	
	public void updShipNums(String[] rspIdsArr,String[] prodNums,String[] prodOutNums,String[] shipNum1s,String[] shipNum2s);
	
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
	 * 工作台发货提醒 <br>
	 */
	public List<SalOrdCon> getProdTipList(String filter);
	
	/**
	 * 销售额在上月末未达到规定金额的单位 <br>
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> getLTsals();
	
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
			String isDe, int currentPage, int pageSize);
	public int listOrdersByCusId(String[]args);
	
	/**
	 * 产品提成分析<br>
	 * @param startDate	开始日期
	 * @param endDate	结束日期
	 * @param wprId 产品Id
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statPsalBack(String startDate, String endDate, String wprId);
	
	public List<ProdShipment> listByCusId(String[] args,String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listByCusIdCount(String[] args);
	
	
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
	/**
	 * 产品分析<br>
	 * @param startDate,endDate	签单日期 
	 * @param startDate1,endDate1	发货日期 
	 * @param wprId 产品Id
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statProAnalyse(String startDate, String endDate, String wprId,String startDate1, String endDate1);
}
package com.psit.struts.BIZ;

import java.util.List;

import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.RStroPro;
import com.psit.struts.entity.RWmsChange;
import com.psit.struts.entity.RWmsWms;
import com.psit.struts.entity.RWoutPro;
import com.psit.struts.entity.WmsChange;
import com.psit.struts.entity.WmsCheck;
import com.psit.struts.entity.WmsLine;
import com.psit.struts.entity.WmsProType;
import com.psit.struts.entity.WmsWarIn;
import com.psit.struts.entity.WmsWarOut;

/**
 * 
 * 库存管理BIZ(2) <br>
 *
 * create_date: Aug 11, 2010,4:41:38 PM<br>
 * @author zjr
 */
public interface WwoBIZ {
	/**
	 * 
	 * 保存出库单 <br>
	 * create_date: Aug 11, 2010,4:45:13 PM <br>
	 * @param wmsWarOut 出库单实体
	 */
	public void saveWwo(WmsWarOut wmsWarOut);
	/**
	 * 
	 * 出库单查询列表数量(按主题,仓库编号,审核状态,出库状态,创建日期) <br>
	 * create_date: Aug 11, 2010,4:50:47 PM <br>
	 * @param wwoTitle 主题
	 * @param wmsCode 出库编号
	 * @param wwoAppIsok 审核状态
	 * @param wwoState 出库状态
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @return int 返回出库单列表数量
	 */
	public int getWwoCount(String wwoTitle,String wmsCode,String wwoAppIsok,String wwoState
			,String startTime,String endTime);
	/**
	 * 
	 * 出库单查询列表(按主题,仓库编号,审核状态,出库状态,创建日期) <br>
	 * create_date: Aug 11, 2010,4:55:05 PM <br>
	 * @param wwoTitle 主题
	 * @param wmsCode 出库编号
	 * @param wwoAppIsok 审核状态
	 * @param wwoState 出库状态
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回出库单列表
	 */
	public List WwoSearch(String wwoTitle,String wmsCode,String wwoAppIsok,String wwoState
			,String startTime,String endTime,int currentPage,int pageSize);
	/**
	 * 
	 * 按出库单id查询出库单实体<br>
	 * create_date: Aug 11, 2010,4:59:17 PM <br>
	 * @param wwoId 库单id
	 * @return WmsWarOut 返回出库单实体
	 */
	public WmsWarOut WwoSearchByCode(Long wwoId);
	/**
	 * 
	 * 保存出库明细 <br>
	 * create_date: Aug 11, 2010,5:03:56 PM <br>
	 * @param rwoutPro 出库明细实体
	 */
	public void saveRwp(RWoutPro rwoutPro);
	/**
	 * 
	 * 根据出库单号查询出库明细 <br>
	 * create_date: Aug 11, 2010,5:04:23 PM <br>
	 * @param wwoCode 出库单号
	 * @return List 返回出库明细列表
	 */
	public List getRwoutPro(String wwoCode);
	/**
	 * 
	 * 更新出库明细 <br>
	 * create_date: Aug 11, 2010,5:08:03 PM <br>
	 * @param rwoutPro 出库明细实体
	 */
	public void updateRwp(RWoutPro rwoutPro);
	/**
	 * 
	 * 根据出库明细id查询实体 <br>
	 * create_date: Aug 11, 2010,5:08:28 PM <br>
	 * @param rwoId 出库明细id
	 * @return RWoutPro 返回出库明细实体
	 */
	public RWoutPro getRWoutPro(Long rwoId);
	/**
	 * 
	 * 删除出库明细 <br>
	 * create_date: Aug 11, 2010,5:17:33 PM <br>
	 * @param rwoutPro 出库明细实体
	 */
	public void deleteRwp(RWoutPro rwoutPro);
	/**
	 * 
	 * 查询仓库所有商品按仓库排序 <br>
	 * create_date: Aug 11, 2010,5:19:38 PM <br>
	 * @param wmsCode 仓库编号
	 * @return List 返回出库明细列表
	 */
	public List getAllRsp(String wmsCode);
	/**
	 * 
	 * 更新出库单 <br>
	 * create_date: Aug 11, 2010,5:21:47 PM <br>
	 * @param wmsWarOut 出库单实体
	 */
	public void updateWwo(WmsWarOut wmsWarOut);
	/**
	 * 
	 * 删除出库单 <br>
	 * create_date: Aug 11, 2010,5:22:07 PM <br>
	 * @param wmsWarOut 出库单实体
	 */
	public void deleteWwo(WmsWarOut wmsWarOut);
	/**
	 * 
	 * 保存仓库调拨 <br>
	 * create_date: Aug 11, 2010,5:23:16 PM <br>
	 * @param wmsChange 仓库调拨实体
	 */
	public void saveWch(WmsChange wmsChange);
	/**
	 * 
	 * 仓库调拨查询列表数量(按主题,仓库编号,审核状态,调拨状态,创建日期) <br>
	 * create_date: Aug 11, 2010,5:24:24 PM <br>
	 * @param wchTitle 调拨主题
	 * @param wmsCode 出库编号
	 * @param wchAppIsok 审核状态
	 * @param wchState 调拨状态
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @return int 返回调拨查询列表数量
	 */
	public int getWchCount(String wchTitle,String wmsCode,String wchAppIsok,String wchState,
			String startTime,String endTime);
	/**
	 * 
	 * 仓库调拨查询列表(按主题,仓库编号,审核状态,调拨状态,创建日期) <br>
	 * create_date: Aug 11, 2010,5:27:35 PM <br>
	 * @param wchTitle 调拨主题
	 * @param wmsCode 出库编号
	 * @param wchAppIsok 审核状态
	 * @param wchState 调拨状态
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回调拨查询列表
	 */
	public List wchSearch(String wchTitle,String wmsCode,String wchAppIsok,String wchState,
			String startTime,String endTime,int currentPage,int pageSize);
	/**
	 * 
	 * 根据id查询仓库调拨 <br>
	 * create_date: Aug 11, 2010,5:33:40 PM <br>
	 * @param wchId 仓库调拨id
	 * @return WmsChange 返回仓库调拨实体
	 */
	public WmsChange wchDesc(Long wchId);
	/**
	 * 
	 * 保存调拨明细 <br>
	 * create_date: Aug 11, 2010,5:36:06 PM <br>
	 * @param rwmsWms 调拨明细实体
	 */
	public void saveRww(RWmsWms rwmsWms);
	/**
	 * 
	 * 根据仓库调拨单id查询调拨明细 <br>
	 * create_date: Aug 11, 2010,5:38:21 PM <br>
	 * @param wchId 仓库调拨单id
	 * @return List 返回调拨明细列表
	 */
	public List getRww(Long wchId);
	/**
	 * 
	 * 更新仓库调拨商品 <br>
	 * create_date: Aug 11, 2010,5:39:12 PM <br>
	 * @param rwmsWms 调拨明细实体
	 */
	public void updateRww(RWmsWms rwmsWms);
	/**
	 * 
	 * 根据Id查询调拨明细 <br>
	 * create_date: Aug 11, 2010,5:39:46 PM <br>
	 * @param rwwId 调拨明细id
	 * @return RWmsWms 返回调拨明细实体
	 */
	public RWmsWms searchRww(Long rwwId);
	/**
	 * 
	 * 按商品名称和仓库编号查询仓库明细列表数量 <br>
	 * create_date: Aug 11, 2010,5:44:14 PM <br>
	 * @param wmsCode 仓库编号
	 * @param wprName 商品名称
	 * @return int 返回仓库明细列表数量
	 */
	public int getCountByWms(String wmsCode,String wprName);
	/**
	 * 
	 * 按商品名称和仓库编号查询仓库明细列表 <br>
	 * create_date: Aug 11, 2010,5:49:43 PM <br>
	 * @param wmsCode 仓库编号
	 * @param wprName 商品名称
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回仓库明细列表
	 */
	public List findProByWms(String wmsCode,String wprName,int currentPage,int pageSize);
	/**
	 * 
	 * 删除仓库调拨明细 <br>
	 * create_date: Aug 11, 2010,5:53:46 PM <br>
	 * @param rwmsWms 调拨明细实体
	 */
	public void delRww(RWmsWms rwmsWms);
	/**
	 * 
	 * 更新仓库调拨 <br>
	 * create_date: Aug 11, 2010,5:55:07 PM <br>
	 * @param wmsChange 仓库调拨实体
	 */
	public void updateWch(WmsChange wmsChange);
	/**
	 * 
	 * 删除仓库调拨单 <br>
	 * create_date: Aug 11, 2010,5:56:11 PM <br>
	 * @param wmsChange 仓库调拨实体
	 */
	public void delWch(WmsChange wmsChange);
	/**
	 * 
	 * 查询所有商品类别 <br>
	 * create_date: Aug 12, 2010,9:38:30 AM <br>
	 * @return List 返回商品类别列表
	 */
	public List findAllWpt();
	/**
	 * 
	 * 添加商品类别 <br>
	 * create_date: Aug 12, 2010,9:40:34 AM <br>
	 * @param wmsProType 商品类别实体
	 */
	public void saveWpt(WmsProType wmsProType);
	/**
	 * 
	 * 查询某仓库商品的库存量 <br>
	 * create_date: Aug 12, 2010,9:49:04 AM <br>
	 * @param wmsCode 仓库编号
	 * @param wprCode 商品id
	 * @return List<RStroPro> 返回库存明细列表
	 */
	public List<RStroPro> findProNum(String wmsCode,Long wprCode);
	/**
	 * 
	 * 查询指定仓库的出库单 <br>
	 * create_date: Aug 12, 2010,9:51:19 AM <br>
	 * @param wmsCode 仓库编号
	 * @return List 返回出库单列表
	 */
	public List WwoSearch(String wmsCode);
	/**
	 * 
	 * 根据入库仓库查询调拨单 <br>
	 * create_date: Aug 12, 2010,9:58:18 AM <br>
	 * @param wmsCode 仓库编号
	 * @return List 返回调拨单列表
	 */
	public List wchSearch(String wmsCode);
	/**
	 * 
	 * 根据出库仓库查询调拨单 <br>
	 * create_date: Aug 12, 2010,10:10:01 AM <br>
	 * @param wmsCode 仓库编号
	 * @return List 返回调拨单列表
	 */
	public List wchSearch2(String wmsCode);
	/**
	 * 
	 * 查询订单下的出库单 <br>
	 * create_date: Aug 12, 2010,10:13:32 AM <br>
	 * @param sodCode 订单id
	 * @return List 返回订单列表
	 */
	public List findByOrd(String sodCode); 
	/**
	 * 
	 * 清除库存为0的商品数据 <br>
	 * create_date: Aug 12, 2010,10:20:05 AM <br>
	 * @param wmsCode 仓库编号
	 */
	public void delData(String wmsCode);
	/**
	 * 
	 * 库存盘点查询列表数量(按主题,仓库编号,审核状态,盘点状态,创建日期) <br>
	 * create_date: Aug 12, 2010,10:20:27 AM <br>
	 * @param wmcTitle 主题
	 * @param wmsCode 仓库编号
	 * @param wmcAppIsok 审核状态
	 * @param wmcState 盘点状态
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @return int 返回库存盘点列表数量
	 */
	public int getWmcCount(String wmcTitle,String wmsCode,String wmcAppIsok,
			String wmcState,String startTime,String endTime);
	/**
	 * 
	 * 库存盘点查询列表(按主题,仓库编号,审核状态,盘点状态,创建日期) <br>
	 * create_date: Aug 12, 2010,10:29:10 AM <br>
	 * @param wmcTitle 主题
	 * @param wmsCode 仓库编号
	 * @param wmcAppIsok 审核状态
	 * @param wmcState 盘点状态
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回库存盘点列表
	 */
	public List wmcSearch(String wmcTitle,String wmsCode,String wmcAppIsok,String wmcState,
			String startTime,String endTime,int currentPage,int pageSize);
	/**
	 * 
	 * 保存库存盘点 <br>
	 * create_date: Aug 12, 2010,10:30:44 AM <br>
	 * @param wmsCheck 库存盘点实体
	 */
	public void saveWmc(WmsCheck wmsCheck);
	/**
	 * 
	 * 根据Id查看盘点单据实体 <br>
	 * create_date: Aug 12, 2010,10:32:14 AM <br>
	 * @param wmcId 盘点单id
	 * @return WmsCheck 返回盘点单据实体
	 */
	public WmsCheck findById(Long wmcId);
	/**
	 * 
	 * 保存盘点明细 <br>
	 * create_date: Aug 12, 2010,10:35:49 AM <br>
	 * @param rwmsChange 盘点明细实体
	 */
	public void save(RWmsChange rwmsChange);
	/**
	 * 
	 * 根据Id得到盘点明细实体 <br>
	 * create_date: Aug 12, 2010,10:36:16 AM <br>
	 * @param id 盘点明细id
	 * @return RWmsChange 返回盘点明细实体
	 */
	public RWmsChange findByIdRmc(Long id);
	/**
	 * 
	 * 更新盘点明细 <br>
	 * create_date: Aug 12, 2010,10:39:00 AM <br>
	 * @param rwmsChange 盘点明细实体
	 */
	public void updateRwmc(RWmsChange rwmsChange);
	/**
	 * 
	 * 删除盘点明细<br>
	 * create_date: Aug 12, 2010,10:39:25 AM <br>
	 * @param rwmsChange 盘点明细实体
	 */
	public void deleteRwmc(RWmsChange rwmsChange);
	/**
	 * 
	 * 根据盘点单id查询盘点明细 <br>
	 * create_date: Aug 12, 2010,10:39:47 AM <br>
	 * @param wmcId 盘点单id
	 * @return List 返回盘点明细列表
	 */
	public List findByWmc(Long wmcId);
	/**
	 * 
	 * 更新盘点单据 <br>
	 * create_date: Aug 12, 2010,10:41:52 AM <br>
	 * @param wmsCheck 盘点单id
	 */
	public void updateWmc(WmsCheck wmsCheck);
	/**
	 * 
	 * 删除盘点单据 <br>
	 * create_date: Aug 12, 2010,10:42:39 AM <br>
	 * @param persistentInstance 盘点单据实体
	 */
	public void delWmsCheck(WmsCheck persistentInstance);
	/**
	 * 
	 * 保存库存流水 <br>
	 * create_date: Aug 12, 2010,10:43:10 AM <br>
	 * @param wmsLine 库存流水实体
	 */
	public void saveWli(WmsLine wmsLine);
	/**
	 * 
	 * 根据单据类型,明细编号查询其流水 <br>
	 * create_date: Aug 12, 2010,10:43:26 AM <br>
	 * @param wliType 流水类型
	 * @param wliWmsId 库存明细id
	 * @return List 返回库存流水列表
	 */
	public List findByWmsId( String wliType,Long wliWmsId);
	/**
	 * 
	 * 库存流水查询列表数量(按单据类型,仓库编号,产品名称,创建日期) <br>
	 * create_date: Aug 12, 2010,10:50:45 AM <br>
	 * @param wliType 单据类型
	 * @param wmsCode 仓库编号
	 * @param wprName 产品名称
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @return int 返回库存流水列表数量
	 */
	public int getWliCount(String wliType,String wmsCode,String wprName,String startTime,String endTime);
	/**
	 * 
	 * 库存流水查询列表(按单据类型,仓库编号,产品名称,创建日期) <br>
	 * create_date: Aug 12, 2010,10:54:36 AM <br>
	 * @param wliType 单据类型
	 * @param wmsCode 仓库编号
	 * @param wprName 产品名称
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回库存流水列表
	 */
	public List WliSearch(String wliType,String wmsCode,String wprName,String startTime,String endTime,int currentPage,int pageSize);
	/**
	 * 
	 * 更新库存流水 <br>
	 * create_date: Aug 12, 2010,10:56:11 AM <br>
	 * @param wmsLine 库存流水实体
	 */
	public void updateWli(WmsLine wmsLine);
	/**
	 * 
	 * 删除库存流水 <br>
	 * create_date: Aug 12, 2010,10:56:27 AM <br>
	 * @param wmsLine 库存流水实体
	 */
	public void deleteWli(WmsLine wmsLine);
	/**
	 * 
	 * 根据Id获得库存流水 <br>
	 * create_date: Aug 12, 2010,10:56:46 AM <br>
	 * @param id 库存流水id
	 * @return WmsLine 返回库存流水实体
	 */
	public WmsLine findWmsLineById(Long id);
	/**
	 * 
	 * 根据单据编号查询其流水 <br>
	 * create_date: Aug 12, 2010,10:59:39 AM <br>
	 * @param wliType 单据类型
	 * @param wliTypeCode 单据编号
	 * @return List 返回库存流水列表
	 */
	public List findByTypeCode( String wliType,String wliTypeCode);
	/**
	 * 
	 * 查看商品的库存流水列表数量 <br>
	 * create_date: Aug 12, 2010,11:02:06 AM <br>
	 * @param wprCode 商品编号
	 * @param isAll 状态标识
	 * @return int 返回库存流水列表数量
	 */
	public int getWliCountByWpr(Long wprCode,String isAll);
	/**
	 * 
	 * 查看商品的库存流水列表 <br>
	 * create_date: Aug 12, 2010,11:06:50 AM <br>
	 * @param wprCode 商品编号
	 * @param isAll 状态标识
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回库存流水列表
	 */
	public List findByWpr(Long wprCode,String isAll,int currentPage,int pageSize);
	/**
	 * 
	 * 生成商品的库存流水的XML <br>
	 * create_date: Aug 12, 2010,11:08:37 AM <br>
	 * @param wprCode 商品id
	 * @param isAll 状态标识
	 * @return List 返回库存流水列表
	 */
	public List findByWpr(Long wprCode,String isAll);
	/**
	 * 
	 * 查询指定仓库的盘点 <br>
	 * create_date: Aug 12, 2010,11:13:49 AM <br>
	 * @param wmsCode 仓库编号
	 * @return List 返回盘点列表
	 */
	public List wmcSearch(String wmsCode);
	/**
	 * 
	 * 查询某个仓库的流水 <br>
	 * create_date: Aug 12, 2010,11:24:28 AM <br>
	 * @param wmsCode 仓库编号
	 * @return List 返回流水列表
	 */
	public List wliSearch(String wmsCode);
	/**
	 * 
	 * 根据单据类型删除指定的库存流水 <br>
	 * create_date: Aug 12, 2010,11:26:12 AM <br>
	 * @param wliType 单据类型
	 * @param wliTypeCode 单据id
	 */
	public void updateByCode(String wliType,String wliTypeCode);
	/**
	 * 
	 * 根据商品类别Id查询类别实体 <br>
	 * create_date: Aug 12, 2010,11:27:56 AM <br>
	 * @param id 商品类别Id
	 * @return WmsProType 返回商品类别实体
	 */
	public WmsProType wptFindById(Long id);
	/**
	 * 
	 * 更新商品类别 <br>
	 * create_date: Aug 12, 2010,11:29:59 AM <br>
	 * @param instance 商品类别实体
	 */
	public void updateProType(WmsProType instance);
	/**
	 * 
	 * 删除商品类别 <br>
	 * create_date: Aug 12, 2010,11:30:18 AM <br>
	 * @param instance 商品类别实体
	 */
	public void deleteProType(WmsProType instance);
	/**
	 * 
	 * 查询商品类别的下级类别 <br>
	 * create_date: Aug 12, 2010,11:30:37 AM <br>
	 * @param wptId 商品类别id
	 * @return List 返回商品类别列表
	 */
	public List findByUpId(Long wptId);
	/**
	 * 
	 * 根据仓库编号查询需要审核的出库单数量 <br>
	 * create_date: Aug 12, 2010,11:34:23 AM <br>
	 * @param wmsCode 仓库编号
	 * @param isok 审核状态
	 * @return int 返回出库单列表数量
	 */
	public int findOutApp(String wmsCode,String isok);
	/**
	 * 
	 * 根据仓库编号查询需要出库的单据数量 <br>
	 * create_date: Aug 12, 2010,11:37:02 AM <br>
	 * @param wmsCode 仓库编号
	 * @return int 返回出库单列表数量
	 */
	public int findOutWms(String wmsCode);
	/**
	 * 
	 * 根据仓库编号查询需要审核的调拨单数量 <br>
	 * create_date: Aug 12, 2010,11:38:40 AM <br>
	 * @param wmsCode 仓库编号
	 * @param isok 审核状态
	 * @return int 返回调拨单列表数量
	 */
	public int findWchApp(String wmsCode,String isok);
	/**
	 * 
	 * 根据仓库编号查询要审核的调拨调入数量 <br>
	 * create_date: Aug 12, 2010,11:40:11 AM <br>
	 * @param wmsCode 仓库编号
	 * @param isok 审核状态
	 * @return int 返回调拨单列表数量
	 */
	public int findApp2(String wmsCode,String isok);
	/**
	 * 
	 * 根据仓库编号查询需要调拨的单据数量 <br>
	 * create_date: Aug 12, 2010,11:41:58 AM <br>
	 * @param wmsCode 仓库编号
	 * @return int 返回调拨单列表数量
	 */
	public int findWchWms(String wmsCode);
	/**
	 * 
	 * 根据仓库编号查询需要审核的盘点单数量 <br>
	 * create_date: Aug 12, 2010,11:43:50 AM <br>
	 * @param wmsCode 仓库编号
	 * @param isok 审核状态
	 * @return int 返回盘点单列表数量
	 */
	public int findWmcApp(String wmsCode,String isok);
	/**
	 * 
	 * 根据仓库编号查询雪要盘点的单据数量 <br>
	 * create_date: Aug 12, 2010,11:44:39 AM <br>
	 * @param wmsCode 仓库编号
	 * @return int 返回盘点单列表数量
	 */
	public int findWmcWms(String wmsCode);
	/**
	 * 
	 * 获得待删除的所有出库单列表 <br>
	 * create_date: Aug 12, 2010,11:45:40 AM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回出库单列表
	 */
	public List findDelWarOut(int pageCurrentNo, int pageSize);
	/**
	 * 
	 * 获得待删除的所有出库单列表数量 <br>
	 * create_date: Aug 12, 2010,11:47:23 AM <br>
	 * @return int 返回出库单列表数量
	 */
	public int findDelWarOutCount();
	/**
	 * 
	 * 获得待删除的所有调拨单列表 <br>
	 * create_date: Aug 12, 2010,11:47:50 AM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回调拨单列表
	 */
	public List findDelWmsChange(int pageCurrentNo, int pageSize);
	/**
	 * 
	 * 获得待删除的所有调拨单数量 <br>
	 * create_date: Aug 12, 2010,11:49:22 AM <br>
	 * @return int 返回调拨单列表数量
	 */
	public int findDelWmsChange();
	/**
	 * 
	 * 获得待删除的所有盘点记录列表 <br>
	 * create_date: Aug 12, 2010,11:49:50 AM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回盘点记录列表
	 */
	public List findDelWmsCheck(int pageCurrentNo, int pageSize);
	/**
	 * 
	 * 获得待删除的所有盘点记录数量 <br>
	 * create_date: Aug 12, 2010,11:50:28 AM <br>
	 * @return int 返回盘点记录列表数量
	 */
	public int findDelWmsCheckCount();
	/**
	 * 
	 * 获得待删除的所有库存流水 <br>
	 * create_date: Aug 12, 2010,11:52:33 AM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回库存流水列表
	 */
	public List findDelWmsLine(int pageCurrentNo, int pageSize);
	/**
	 * 
	 * 获得待删除的所有库存流水数量 <br>
	 * create_date: Aug 12, 2010,11:53:09 AM <br>
	 * @return int 返回库存流水列表数量
	 */
	public int findDelWmsLineCount();
	/**
	 * 
	 * 带删除状态查出出库单实体 <br>
	 * create_date: Aug 12, 2010,11:54:19 AM <br>
	 * @param wwoId 出库单id
	 * @return WmsWarOut 返回出库单实体
	 */
	public WmsWarOut findWwo(Long wwoId);
	/**
	 * 
	 * 带删除状态查出调拨单实体 <br>
	 * create_date: Aug 12, 2010,11:56:07 AM <br>
	 * @param wchId 调拨单id
	 * @return WmsChange 返回调拨单实体
	 */
	public WmsChange findWch(Long wchId);
	/**
	 * 
	 * 带删除状态查出盘点实体 <br>
	 * create_date: Aug 12, 2010,11:57:48 AM <br>
	 * @param wmcId 盘点记录id
	 * @return WmsCheck 返回盘点记录实体
	 */
	public WmsCheck findWmc(Long wmcId);
	/**
	 * 
	 * 根据出库单号查询出库单 <br>
	 * create_date: Aug 12, 2010,11:58:35 AM <br>
	 * @param wwoCode 出库单号
	 * @return List 返回出库单列表
	 */
	public List findByWwoCode(String wwoCode);
	/**
	 * 
	 * 根据调拨单号查询调拨单 <br>
	 * create_date: Aug 12, 2010,11:59:34 AM <br>
	 * @param wchCode 调拨单号
	 * @return List 返回调拨单列表
	 */
	public List findBywchCode(String wchCode);
	/**
	 * 
	 * 根据盘点单号查询 <br>
	 * create_date: Aug 12, 2010,12:00:15 PM <br>
	 * @param wmcCode 盘点单号
	 * @return List 返回盘点记录列表
	 */
	public List findByWmcCode(String wmcCode);
	/**
	 * 
	 * 根据商品id查询出库明细 <br>
	 * create_date: Aug 12, 2010,12:00:58 PM <br>
	 * @param wprId 商品id
	 * @return List 返回出库明细列表
	 */
	public List rwoByWpr(Long wprId);
	/**
	 * 
	 * 根据商品id查询调拨明细 <br>
	 * create_date: Aug 12, 2010,12:02:30 PM <br>
	 * @param wprId 商品id
	 * @return List 返回调拨明细列表
	 */
	public List rwwByWpr(Long wprId);
	/**
	 * 
	 *  根据商品id查询盘点明细<br>
	 * create_date: Aug 12, 2010,12:03:40 PM <br>
	 * @param wprId 商品id
	 * @return List 返回盘点明细列表
	 */
	public List rmcByWpr(Long wprId);
	/**
	 * 
	 * 根据商品id查询库存流水 <br>
	 * create_date: Aug 12, 2010,12:04:08 PM <br>
	 * @param wprId 商品id
	 * @return List 返回流水列表
	 */
	public List wliByWpr(Long wprId);
	/**
	 * 
	 * 根据出库单id和商品id查询出库明细 <br>
	 * create_date: Aug 12, 2010,12:04:55 PM <br>
	 * @param wprId 商品id
	 * @param wwoId 出库单id
	 * @return List 返回出库明细列表
	 */
	public List findByWwo(Long wprId,Long wwoId);
	/**
	 * 
	 * 根据调拨单id和商品id查询调拨明细 <br>
	 * create_date: Aug 12, 2010,12:05:59 PM <br>
	 * @param wprId 商品id
	 * @param wchId 调拨单id
	 * @return List 返回出调拨明细列表
	 */
	public List findByWch(Long wprId,Long wchId);
	/**
	 * 
	 * 根据盘点记录id和商品id查询盘点明细 <br>
	 * create_date: Aug 12, 2010,12:06:56 PM <br>
	 * @param wprId 商品id
	 * @param wmcId 盘点记录id
	 * @return List 返回出盘点明细列表
	 */
	public List findByWmc(Long wprId,Long wmcId);
}
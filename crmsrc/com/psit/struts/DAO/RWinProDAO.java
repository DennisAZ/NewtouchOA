package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.RWinPro;
/**
 * 
 * 入库明细DAO <br>
 *
 * create_date: Aug 17, 2010,4:04:48 PM<br>
 * @author zjr
 */
public interface RWinProDAO  {
	/**
	 * 
	 * 保存入库明细 <br>
	 * create_date: Aug 11, 2010,2:43:37 PM <br>
	 * @param rwinPro 入库明细实体
	 */
	public void save(RWinPro rwinPro);
	/**
	 * 
	 * 查询所有入库商品 <br>
	 * create_date: Aug 11, 2010,2:44:10 PM <br>
	 * @param wwiId 入库明细id
	 * @return List<RWinPro> 返回入库明细列表
	 */
	public List<RWinPro> getRwinPro(Long wwiId);
	/**
	 * 
	 * 根据id查询入库明细实体 <br>
	 * create_date: Aug 11, 2010,2:50:18 PM <br>
	 * @param rwiId 入库明细id
	 * @return RWinPro 返回入库明细实体
	 */
	public RWinPro getRWinPro(Long rwiId);
	/**
	 * 
	 * 更新入库商品 <br>
	 * create_date: Aug 11, 2010,2:51:24 PM <br>
	 * @param rwinPro 入库明细实体
	 */
	public void updateRwp(RWinPro rwinPro);
	/**
	 * 
	 * 删除入库商品 <br>
	 * create_date: Aug 11, 2010,2:53:45 PM <br>
	 * @param rwinPro 入库明细实体
	 */
	public void delRwp(RWinPro rwinPro);
	/**
	 * 
	 * 根据入库单号删除入库明细 <br>
	 * create_date: Aug 11, 2010,3:00:06 PM <br>
	 * @param wwiCode 入库单号
	 */
	public void delRwinPro(Long wwiCode);
	/**
	 * 
	 * 查询入库明细中的商品 <br>
	 * create_date: Aug 11, 2010,3:23:32 PM <br>
	 * @param wprId 商品id
	 * @return List<RWinPro> 返回入库明细列表
	 */
	public List<RWinPro> findByWpr(Long wprId);
	/**
	 * 
	 * 按入库单号和商品id查询入库明细 <br>
	 * create_date: Aug 11, 2010,3:25:55 PM <br>
	 * @param wprId 商品id
	 * @param wwiCode 入库单号
	 * @return List<RWinPro> 返回入库明细列表
	 */
	public List<RWinPro> findByWpr(Long wprId,String wwiCode);
	//查询所有入库商品
//	public List<RWinPro> findAll();
}
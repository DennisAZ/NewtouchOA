package com.psit.struts.util;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import com.psit.struts.entity.OrdStatistic;
import com.psit.struts.entity.SalOrdCon;
import com.psit.struts.entity.StaTable;
/**
 * List操作类<br>
 */
public class OperateList 
{
	/**
	 * 返回动态统计表格实体(存储过程) <br>
	 * @param procStr  存储过程
	 * @param params sql查询条件
	 * @return StaTable 返回统计列表实体<br>
	 */
	public static StaTable getStaTab(String procStr,String params){
		StaTable staTab = null;
		JdbcCon jd=new JdbcCon();
		ResultSet rs;
		try {
			if(params!=null && !params.equals("")){
				procStr = "{call "+procStr+"(?)}";
			}
			rs = jd.callProcedure(procStr,params);
			staTab = parseRsToTab(rs,true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		jd.closeAll();
		return staTab;
	}
	/**
	 * 返回动态统计表格实体(SQL) <br>
	 * @param sql sql语句
	 * @return StaTable 返回统计列表实体<br>
	 */
	public static StaTable getStaTab(String sql,boolean isSum){
		StaTable staTab = null;
		JdbcCon jd=new JdbcCon();
		ResultSet rs;
		try {
			rs = jd.querySql(sql);
			staTab = parseRsToTab(rs,isSum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		jd.closeAll();
		return staTab;
	}
	/**
	 * 将结果集转换为封装类 <br>
	 * @param rs 结果集
	 * @param hasSum 是否包含合计行（是否使用rollup）
	 * @return StaTable 
	 */
	public static StaTable parseRsToTab(ResultSet rs,boolean hasSum){
		ResultSetMetaData colData;
		String[] headArr ;
		List dataList = new ArrayList();
		int columnCount;
		int headCol = 0;
		if(rs!=null){
			try {
				colData = rs.getMetaData();
				columnCount = colData.getColumnCount();
				headArr = new String[columnCount];
				for(int i = 1;i<= columnCount;i++){ 
					String colName = colData.getColumnName(i);//获得指定列的列名 
					if(headCol>0){
						headArr[i-1] = colName;
//						headLi.add(colName);
					}
					if(colName!=null&&colName.equals("head")){
						headCol = i;
					}
				}
				if(rs.next()){
					do { 
						/*LinkedHashMap rowData = new LinkedHashMap();
						for (int i = 1; i <= columnCount; i++) {
							if(rs.isLast() && i==headCol && hasSum){
								rowData.put(i, "合计");
							}
							else{
								rowData.put(i, rs.getString(i));
							}
						}*/
						String[] rowData = new String[columnCount];
						for(int i=1; i<=columnCount; i++){
							if(rs.isLast() && i==headCol && hasSum){
								rowData[i-1] = "合计";
							}
							else{
								rowData[i-1] = rs.getString(i);
							}
						}
						dataList.add(rowData);
					}while(rs.next());
					return new StaTable(columnCount,dataList.size(),headArr,dataList);
				}
				else{
					return null;
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		else{
			return null;
		}
	}
	/**
	 * 返回动态统计表格实体 <br>
	 * @param sql sql查询语句
	 * @param procStr  存储过程
	 * @return StaTable 返回统计列表实体<br>
	 */
	public static StaTable getStaTable(String sql,String procStr){
		StaTable staTab = null;
		JdbcCon jd=new JdbcCon();
		ResultSet rs;
		ResultSetMetaData data;
		try {
			rs = jd.callProcedure(procStr,sql);
			int columnCount;
			data = rs.getMetaData();
			columnCount = data.getColumnCount();
			String[] colArr = new String[columnCount-2];
			for(int i = 3;i<= columnCount;i++){ 
				//获得指定列的列名 
//				li.add(data.getColumnName(i));
				colArr[i-3] = data.getColumnName(i);
			}
			
			List dataList = new ArrayList();
			if(rs.next()){
				do { 
					/*LinkedHashMap rowData = new LinkedHashMap();
					for (int i = 1; i <= columnCount; i++) {
						if(rs.isLast()&&i==2){
							rowData.put(i, "合计");
						}
						else{
							rowData.put(i, rs.getString(i));
						}
					}*/
					String[] rowData = new String[columnCount];
					for (int i = 1; i <= columnCount; i++) {
						if(rs.isLast()&&i==2){
							rowData[i-1] = "合计";
						}
						else{
							rowData[i-1] = rs.getString(i);
						}
					}
					dataList.add(rowData);
				}while(rs.next());
			}
			staTab = new StaTable(columnCount,dataList.size(),colArr,dataList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		jd.closeAll();
		return staTab;
	}
	
	
	/**
	 * 计算列表中值的和 <br>
	 * @param list 需操作的列表
	 * @return String 返回值的和<br>
	 */
	public static String getTotal(List<OrdStatistic> list)
	{
		double dtotal=0.0;
		long ltotal=0;
		String total="";
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				OrdStatistic ord=(OrdStatistic)list.get(i);
				if(ord.getType().equals("1")||ord.getType().equals("4")){
					ltotal+=ord.getLnum();//long类型
				}
				else if(ord.getType().equals("2")||ord.getType().equals("3")){
					dtotal+=ord.getDnum();//double类型
				}
			}
		}
		if(ltotal!=0)
			total=String.valueOf(ltotal);
		else if(dtotal!=0.0)
			total=String.valueOf(dtotal);
		return total;
	}
	/**
	 * 计算某客户下订单总金额，已回款金额 <br>
	 * @param list 订单列表
	 * @return double[] 返回订单总金额<br>
	 */
	public static double[] getOrderTotal(List<SalOrdCon> list)
	{
		double sumMon=0.00;
		double paidMon=0.00;
		double[] total=new double[2];
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				SalOrdCon ord=(SalOrdCon)list.get(i);
				if(ord.getSodSumMon()!=null)
					sumMon+=ord.getSodSumMon();
				if(ord.getSodPaidMon()!=null)
					paidMon+=ord.getSodPaidMon();
			}
		}
		total[0]=sumMon;
		total[1]=paidMon;
		return total;
	}
}

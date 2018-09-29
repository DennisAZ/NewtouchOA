package yh.subsys.oa.hr.salary.welfare_manager.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.subsys.oa.hr.salary.insurancePara.salItem.data.YHSalItem;

public class YHSalaryAnalysisLogic {
	 public  static String querySale(Connection conn, String beginTime , String endTime) throws Exception {
		 String query2="";
 	    		query2 = "SELECT  sale.salesperson,SUM(total) as total  FROM erp_sale_order sale LEFT JOIN erp_finance_in fin ON fin.type_id=sale.id  where 1=1 ";
 	    	if (!"".equals(beginTime)) {
		    	query2 += " and date_format(sign_date,'%Y-%m-%d') >= '"
						+ beginTime + "'";
			}
			if (!"".equals(endTime)) {
				query2 += " and date_format(sign_date,'%Y-%m-%d') <= '" + endTime
						+ "'";
			}
		    query2+="  GROUP BY person_id";
		    PreparedStatement ps2 = null;
		    ResultSet rs2 = null;
		    List<Map<String,Object>> itemList = new ArrayList<Map<String,Object>>();
		    String queryStr = "";
		    String tableHeader = "";
		    StringBuffer sb = new StringBuffer();
		    sb.append("{tableHeader:'销售人员,金额', tableData:[");
		    String queryCount =query2;
		    ps2 = conn.prepareStatement(query2);
		      rs2 = ps2.executeQuery();
		      int count2 = 0 ;
		     while(rs2.next()) {
		    	  Map<String,Object>saleOrder = new HashMap<String,Object>();
		    	  saleOrder.put("saleSperson", rs2.getString("saleSperson"));
		    	  saleOrder.put("total", rs2.getDouble("total"));
		    	  sb.append("['"+rs2.getString("saleSperson")+"'");
		    	  sb.append(","+rs2.getDouble("total")+"],");
		    	  count2++;
		    	  itemList.add(saleOrder);
		      }
		    PreparedStatement ps3 = null;
		    ResultSet rs3 = null;
		   
		    int dataCount = 0 ;
		    dataCount=itemList.size();
		    if (count2 > 0 ) {
		      sb.deleteCharAt(sb.length() - 1);
		    }
		    sb.append("], dataCount : "+count2+"}");
		    return sb.toString();
		  }
	 public  static String queryCust(Connection conn, String beginTime , String endTime) throws Exception {
		 String query2="";
			 query2="SELECT cus.cus_name, SUM(total) AS total  FROM erp_sale_order sale LEFT JOIN erp_finance_in fin ON fin.type_id=sale.id LEFT JOIN erp_order_cus cus ON cus.order_id=sale.id  where 1=1 ";
			 
		 
		 if (!"".equals(beginTime)) {
			 query2 += " and date_format(sign_date,'%Y-%m-%d') >= '"
				 + beginTime + "'";
		 }
		 if (!"".equals(endTime)) {
			 query2 += " and date_format(sign_date,'%Y-%m-%d') <= '" + endTime
			 + "'";
		 }
			 query2+=" GROUP BY cus.cus_id";
		 PreparedStatement ps2 = null;
		 ResultSet rs2 = null;
		 List<Map<String,Object>> itemList = new ArrayList<Map<String,Object>>();
		 String queryStr = "";
		 String tableHeader = "";
		 StringBuffer sb = new StringBuffer();
		 sb.append("{tableHeader:'客户名称,金额', tableData:[");
		 String queryCount =query2;
		 ps2 = conn.prepareStatement(query2);
		 rs2 = ps2.executeQuery();
		 int count2 = 0 ;
		 while(rs2.next()) {
			 Map<String,Object>saleOrder = new HashMap<String,Object>();
			 saleOrder.put("cus_name", rs2.getString("cus_name"));
			 saleOrder.put("total", rs2.getDouble("total"));
			 sb.append("['"+rs2.getString("cus_name")+"'");
			 sb.append(","+rs2.getDouble("total")+"],");
			 count2++;
			 itemList.add(saleOrder);
		 }
		 PreparedStatement ps3 = null;
		 ResultSet rs3 = null;
		 
		 int dataCount = 0 ;
		 dataCount=itemList.size();
		 if (count2 > 0 ) {
			 sb.deleteCharAt(sb.length() - 1);
		 }
		 sb.append("], dataCount : "+count2+"}");
		 return sb.toString();
	 }
	 public  static String queryPurchase(Connection conn, String beginTime , String endTime) throws Exception {
		 String query2="";
		 query2="SELECT pur.person,SUM(fin.paid_total) as total FROM erp_purchase pur LEFT JOIN erp_finance_out fin ON fin.type_id=pur.id where 1=1 ";
		 
		 
		 if (!"".equals(beginTime)) {
			 query2 += " and date_format(sign_date,'%Y-%m-%d') >= '"
				 + beginTime + "'";
		 }
		 if (!"".equals(endTime)) {
			 query2 += " and date_format(sign_date,'%Y-%m-%d') <= '" + endTime
			 + "'";
		 }
		 query2+=" GROUP BY pur.person_id";
		 PreparedStatement ps2 = null;
		 ResultSet rs2 = null;
		 List<Map<String,Object>> itemList = new ArrayList<Map<String,Object>>();
		 String queryStr = "";
		 String tableHeader = "";
		 StringBuffer sb = new StringBuffer();
		 sb.append("{tableHeader:'采购员,金额', tableData:[");
		 String queryCount =query2;
		 ps2 = conn.prepareStatement(query2);
		 rs2 = ps2.executeQuery();
		 int count2 = 0 ;
		 while(rs2.next()) {
			 Map<String,Object>saleOrder = new HashMap<String,Object>();
			 saleOrder.put("person", rs2.getString("person"));
			 saleOrder.put("total", rs2.getDouble("total"));
			 sb.append("['"+rs2.getString("person")+"'");
			 sb.append(","+rs2.getDouble("total")+"],");
			 count2++;
			 itemList.add(saleOrder);
		 }
		 PreparedStatement ps3 = null;
		 ResultSet rs3 = null;
		 
		 int dataCount = 0 ;
		 dataCount=itemList.size();
		 if (count2 > 0 ) {
			 sb.deleteCharAt(sb.length() - 1);
		 }
		 sb.append("], dataCount : "+count2+"}");
		 return sb.toString();
	 }
	 public  static String querySupplier(Connection conn, String beginTime , String endTime) throws Exception {
		 String query2="";
		 query2="SELECT sup.sup_name,SUM(fin.paid_total) as total FROM erp_purchase pur LEFT JOIN erp_finance_out fin ON fin.type_id=pur.id LEFT JOIN erp_purchase_supplier sup ON sup.purchase_id=pur.id  where 1=1";
		 
		 
		 if (!"".equals(beginTime)) {
			 query2 += " and date_format(sign_date,'%Y-%m-%d') >= '"
				 + beginTime + "'";
		 }
		 if (!"".equals(endTime)) {
			 query2 += " and date_format(sign_date,'%Y-%m-%d') <= '" + endTime
			 + "'";
		 }
		 query2+=" GROUP BY sup_id";
		 PreparedStatement ps2 = null;
		 ResultSet rs2 = null;
		 List<Map<String,Object>> itemList = new ArrayList<Map<String,Object>>();
		 String queryStr = "";
		 String tableHeader = "";
		 StringBuffer sb = new StringBuffer();
		 sb.append("{tableHeader:'供货商名称,金额', tableData:[");
		 String queryCount =query2;
		 ps2 = conn.prepareStatement(query2);
		 rs2 = ps2.executeQuery();
		 int count2 = 0 ;
		 while(rs2.next()) {
			 Map<String,Object>saleOrder = new HashMap<String,Object>();
			 saleOrder.put("sup_name", rs2.getString("sup_name"));
			 saleOrder.put("total", rs2.getDouble("total"));
			 sb.append("['"+rs2.getString("sup_name")+"'");
			 sb.append(","+rs2.getDouble("total")+"],");
			 count2++;
			 itemList.add(saleOrder);
		 }
		 PreparedStatement ps3 = null;
		 ResultSet rs3 = null;
		 
		 int dataCount = 0 ;
		 dataCount=itemList.size();
		 if (count2 > 0 ) {
			 sb.deleteCharAt(sb.length() - 1);
		 }
		 sb.append("], dataCount :"+count2+"}");
		 return sb.toString();
	 }
	 public  static String queryProduct(Connection conn, String beginTime , String endTime) throws Exception {
		 String query2="";
		 query2="SELECT cp.pro_name,SUM(fin.paid_total) AS total FROM erp_purchase pur LEFT JOIN erp_finance_out fin ON fin.type_id=pur.id LEFT JOIN erp_cache_product cp ON cp.useful_id=pur.id  where 1=1";
		 
		 
		 if (!"".equals(beginTime)) {
			 query2 += " and date_format(sign_date,'%Y-%m-%d') >= '"
				 + beginTime + "'";
		 }
		 if (!"".equals(endTime)) {
			 query2 += " and date_format(sign_date,'%Y-%m-%d') <= '" + endTime
			 + "'";
		 }
		 query2+=" GROUP BY cp.pro_id";
		 PreparedStatement ps2 = null;
		 ResultSet rs2 = null;
		 List<Map<String,Object>> itemList = new ArrayList<Map<String,Object>>();
		 String queryStr = "";
		 String tableHeader = "";
		 StringBuffer sb = new StringBuffer();
		 sb.append("{tableHeader:'产品名称,金额', tableData:[");
		 String queryCount =query2;
		 ps2 = conn.prepareStatement(query2);
		 rs2 = ps2.executeQuery();
		 int count2 = 0 ;
		 while(rs2.next()) {
			 Map<String,Object>saleOrder = new HashMap<String,Object>();
			 saleOrder.put("pro_name", rs2.getString("pro_name"));
			 saleOrder.put("total", rs2.getDouble("total"));
			 sb.append("['"+rs2.getString("pro_name")+"'");
			 sb.append(","+rs2.getDouble("total")+"],");
			 count2++;
			 itemList.add(saleOrder);
		 }
		 PreparedStatement ps3 = null;
		 ResultSet rs3 = null;
		 
		 int dataCount = 0 ;
		 dataCount=itemList.size();
		 if (count2 > 0 ) {
			 sb.deleteCharAt(sb.length() - 1);
		 }
		 sb.append("], dataCount : "+count2+"}");
		 return sb.toString();
	 }
	 public  static String queryPro(Connection conn, String beginTime , String endTime,String proId) throws Exception {
		 String query2="";
		 query2="SELECT pur.sign_date,ppro.pur_price FROM erp_purchase pur LEFT JOIN erp_purchase_product_out  pout ON pout.purchase_id=pur.id LEFT JOIN erp_ppo_pro ppro  ON ppro.ppo_id=pout.id LEFT JOIN erp_cache_product cp ON cp.useful_id=pur.id WHERE cp.pro_id='"+proId+"'";
		 
		 
		 if (!"".equals(beginTime)) {
			 query2 += " and date_format(sign_date,'%Y-%m-%d') >= '"
				 + beginTime + "'";
		 }
		 if (!"".equals(endTime)) {
			 query2 += " and date_format(sign_date,'%Y-%m-%d') <= '" + endTime
			 + "'";
		 }
		 PreparedStatement ps2 = null;
		 ResultSet rs2 = null;
		 List<Map<String,Object>> itemList = new ArrayList<Map<String,Object>>();
		 String queryStr = "";
		 String tableHeader = "";
		 StringBuffer sb = new StringBuffer();
		 sb.append("{tableHeader:'日期,金额', tableData:[");
		 String queryCount =query2;
		 ps2 = conn.prepareStatement(query2);
		 rs2 = ps2.executeQuery();
		 int count2 = 0 ;
		 while(rs2.next()) {
			 Map<String,Object>saleOrder = new HashMap<String,Object>();
			 saleOrder.put("sign_date", rs2.getString("sign_date"));
			 saleOrder.put("pro_price", rs2.getDouble("pur_price"));
			 sb.append("['"+rs2.getString("sign_date")+"'");
			 sb.append(","+rs2.getDouble("pur_price")+"],");
			 count2++;
			 itemList.add(saleOrder);
		 }
		 PreparedStatement ps3 = null;
		 ResultSet rs3 = null;
		 
		 int dataCount = 0 ;
		 dataCount=itemList.size();
		 if (count2 > 0 ) {
			 sb.deleteCharAt(sb.length() - 1);
		 }
		 sb.append("], dataCount :"+count2+"}");
		 return sb.toString();
	 }
	public String querySalary(Connection conn , String dept , String salYear , String salMonth) throws Exception {
	    String query = "SELECT SEQ_ID FROM oa_sal_flow where " 
	      + " sal_year = '" + salYear + "'"
	      + " and sal_month = '" + salMonth + "'"
	      + " and sal_year = '" + salYear + "'"
	      + " and ISSEND = '1'";
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    int seqId = 0;
	    try {
	      ps = conn.prepareStatement(query);
	      rs = ps.executeQuery();
	      if (rs.next()) {
	        seqId = rs.getInt("SEQ_ID");
	      } else {
	        return null;
	      }
	    } catch (SQLException e) {
	      throw e;
	    } finally {
	      YHDBUtility.close(ps, rs, null);
	    }
	    String str = "";
	    if (!YHUtility.isNullorEmpty(dept)
	        && !"0".equals(dept)) {
	      if (dept.endsWith(",")) {
	        dept = YHWorkFlowUtility.getOutOfTail(dept);
	      }
	      str = " and D.SEQ_ID IN (" + dept + ") ";
	    } 
	    String query2 = "SELECT ITEM_NAME ,ISREPORT , SLAITEM_ID  FROM oa_sal_item";
	    PreparedStatement ps2 = null;
	    ResultSet rs2 = null;
	    List<YHSalItem> itemList = new ArrayList();
	    String queryStr = "";
	    String tableHeader = "";
	    try {
	      ps2 = conn.prepareStatement(query2);
	      rs2 = ps2.executeQuery();
	      while (rs2.next()) {
	        YHSalItem salItem = new YHSalItem();
	        String itemName = rs2.getString("ITEM_NAME");
	        String isReport = rs2.getString("ISREPORT");
	        int slaitemId = rs2.getInt("SLAITEM_ID");
	        salItem.setSlaitemId(slaitemId);
	        salItem.setItemName(itemName);
	        salItem.setIsreport(isReport);
	        itemList.add(salItem);
	        tableHeader += itemName + ",";
	        queryStr += " count(S"+slaitemId+") AS S"+slaitemId + " ,";
	      }
	    } catch (SQLException e) {
	      throw e;
	    } finally {
	      YHDBUtility.close(ps2, rs2, null);
	    }
	    StringBuffer sb = new StringBuffer();
	    sb.append("{tableHeader:'" + tableHeader + "', tableData:[");
	    String queryCount = "SELECT " + queryStr + "" +
	    		" D.SEQ_ID , DEPT_NAME "
	      + " FROM oa_department D,oa_sal_data S, PERSON P" +
	      		" WHERE FLOW_ID = '"+seqId+"' "
	      + " AND P.SEQ_ID = S.USER_ID "
	      + " AND P.DEPT_ID = D.SEQ_ID "
	      + str
	      + " GROUP BY D.SEQ_ID, D.DEPT_NAME";
	    PreparedStatement ps3 = null;
	    ResultSet rs3 = null;
	    int count2 = 0 ;
	    int dataCount = 0 ;
	    try {
	      ps3 = conn.prepareStatement(queryCount);
	      rs3 = ps3.executeQuery();
	      while (rs3.next()) {
	        //int seqId2 = rs3.getInt("SEQ_ID");
	        String deptName = rs3.getString("DEPT_NAME");
	        sb.append("[");
	        sb.append("'"  + deptName + "',");
	        int count3 = 0 ;
                                       	        for (YHSalItem si : itemList) {
	          String ss = "S" + si.getSlaitemId();
	          int count = rs3.getInt(ss);
	          sb.append(count + ",");
	          count3 += count;
	        }
           	        dataCount += count3;
	        sb.append(count3 + "],");
	        count2++;
	      }
	    } catch (SQLException e) {
	      throw e;
	    } finally {
	      YHDBUtility.close(ps3, rs3, null);
	    }
	    if (count2 > 0 ) {
	      sb.deleteCharAt(sb.length() - 1);
	    }
	    sb.append("], dataCount : "+dataCount +"}");
	    return sb.toString();
	  }
	}

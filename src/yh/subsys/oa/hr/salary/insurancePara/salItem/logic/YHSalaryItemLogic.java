package yh.subsys.oa.hr.salary.insurancePara.salItem.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.hr.salary.insurancePara.salItem.data.YHHrInsurancePara;
import yh.subsys.oa.hr.salary.insurancePara.salItem.data.YHSalItem;

public class YHSalaryItemLogic {
	public void setSalaryItemLogic(Connection dbConn,
			YHFileUploadForm fileForm, YHPerson person) throws Exception {
		YHORM orm = new YHORM();
		String itemName = fileForm.getParameter("ITEM_NAME");//薪酬项目名称
		String itemType = fileForm.getParameter("ITEM_TYPE");//项目类型
		String formula = fileForm.getParameter("FORMULA");
		String formulaname = fileForm.getParameter("FORMULANAME"); // 计算公式名称
		int seqId = getSalaryItemCountLogic(dbConn,person);
	    /*int id =	seqId+1;
	    System.out.println("++++::"+id);*/
		String isprint="1";
		String iscomputer = "";
		String isreport ="";
		
		  if(!YHUtility.isNullorEmpty(itemType)&&itemType.equals("1")){
			  isreport = "1";
			    iscomputer = "0";
		}else{
			isreport = "0";
			if(!YHUtility.isNullorEmpty(itemType)&&itemType.equals("2")){
				iscomputer = "1";
			}else{ 
			  iscomputer = "0";
			}
		}
		try {
			YHSalItem salItem = new YHSalItem();
			salItem.setItemName(itemName);
			salItem.setIsprint(isprint);
			salItem.setIscomputer(iscomputer);
			salItem.setFormula(formula);
			salItem.setFormulaname(formulaname);
			salItem.setIsreport(isreport);
			salItem.setSlaitemId(seqId+1);  
			if(seqId<51){
		 	  orm.saveSingle(dbConn, salItem);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 查询薪酬项
	 * @param dbConn
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<YHSalItem> getSalaryItemJsonLogic(Connection dbConn, YHPerson user) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT SEQ_ID,ITEM_NAME,ISPRINT,ISCOMPUTER,FORMULA,FORMULANAME,ISREPORT,SLAITEM_ID from oa_sal_item order by SEQ_ID";
		List<YHSalItem> salItems = new ArrayList<YHSalItem>();
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
				while (rs.next()) {
					YHSalItem salItem = new YHSalItem();
					salItem.setSeqId(rs.getInt("SEQ_ID"));
					salItem.setSlaitemId(rs.getInt("SLAITEM_ID"));
					salItem.setItemName(rs.getString("ITEM_NAME"));
					salItem.setIsprint(rs.getString("ISPRINT"));
					salItem.setIscomputer(rs.getString("ISCOMPUTER"));
					salItem.setFormula(rs.getString("FORMULA"));
					salItem.setFormulaname(rs.getString("FORMULANAME"));
					salItem.setIsreport(rs.getString("ISREPORT"));
					salItems.add(salItem);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return salItems;
	}
	/**
	 * 查找薪酬项的总数
	 * @param dbConn
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int getSalaryItemCountLogic(Connection dbConn, YHPerson user) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT count(*) as num from oa_sal_item";
		List<YHSalItem> salItems = new ArrayList<YHSalItem>();
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
			  return rs.getInt("num");
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return 0;
	}
	/**
	 * 删除单个薪酬项
	 * @param conn
	 * @param person
	 * @param noHiddenId
	 * @return
	 * @throws Exception
	 */
	public static int delSalItemInfoLogic(Connection conn, YHPerson person,
			String noHiddenId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int ok = 0;
		if(YHUtility.isNullorEmpty(noHiddenId)){
			noHiddenId ="0";
		}
		String deleteSql = "delete from oa_sal_item where slaitem_id ="
				+Integer.valueOf(noHiddenId);
		try {
			ps = conn.prepareStatement(deleteSql);
			ok = ps.executeUpdate();
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}

		return ok;
	}
	/**
	 * 删除所有薪酬项
	 * @param conn
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public static int delAllSalItemInfoLogic(Connection conn, YHPerson person) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int ok = 0;
		String deleteSql = "delete from oa_sal_item";
		try {
			ps = conn.prepareStatement(deleteSql);
			ok = ps.executeUpdate();
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}

		return ok;
	}
	
	
	/**
	 * 在修改薪酬项目时，先查询
	 * @param dbConn
	 * @param user
	 * @param slaId
	 * @return
	 * @throws Exception
	 */
	public YHSalItem findSlaItemInfoLogic(Connection dbConn, YHPerson user,
			String slaId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		YHSalItem sle = null;
		YHORM orm =new YHORM();
		try {
			if(YHUtility.isNullorEmpty(slaId)){
				slaId="0";
			}
			int slaitemId = Integer.valueOf(slaId);
			Map map = new HashMap();
			map.put("SLAITEM_ID", slaitemId);
			sle= (YHSalItem) orm.loadObjSingle(dbConn, YHSalItem.class, map);
			
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return sle;
	}
	/**
	 * 修改薪酬项目
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @throws Exception
	 */
	public void setUpSalaryItemLogic(Connection dbConn,
			YHFileUploadForm fileForm, YHPerson person) throws Exception {
	    YHORM orm = new YHORM();
	    String seqId = fileForm.getParameter("seqId");//自增的Id
		String itemId = fileForm.getParameter("ITEM_ID");//薪酬有意义Id
		String itemName = fileForm.getParameter("ITEM_NAME");//薪酬项目名称
		String itemType = fileForm.getParameter("ITEM_TYPE");//项目类型
		String formula = fileForm.getParameter("FORMULA");
		String formulaname = fileForm.getParameter("FORMULANAME"); // 计算公式名称
		try {
			YHSalItem salItem = new YHSalItem();
			salItem.setSeqId(Integer.valueOf(seqId));
			if (!YHUtility.isNullorEmpty(itemId)) {
				salItem.setSlaitemId(Integer.valueOf(itemId));
			}
			salItem.setItemName(itemName);
			String isprint="1";
			String iscomputer = "";
			String isreport ="";
			if(!YHUtility.isNullorEmpty(itemType)&&itemType.equals("1")){
				  isreport = "1";
				    iscomputer = "0";
			}else{
				isreport = "0";
				if(!YHUtility.isNullorEmpty(itemType)&&itemType.equals("2")){
					iscomputer = "1";
				}else{ 
				  iscomputer = "0";
				}
			}
		    salItem.setIsprint(isprint);
			salItem.setIscomputer(iscomputer);
			salItem.setIsreport(isreport);
			salItem.setFormula(formula);
			salItem.setFormulaname(formulaname);	    
		    orm.updateSingle(dbConn, salItem);
     
		} catch (Exception e) {
			throw e;
		}
	}
	
	public int upSaveSalaryItemLogic(Connection dbConn, YHPerson user,String seqId,
			String slaItemId,String formulaId,String textFormula ) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		YHSalItem sle = null;
		int ok =0;
		if(YHUtility.isNullorEmpty(slaItemId)){
			slaItemId="0";
		}
		if(YHUtility.isNullorEmpty(seqId)){
			seqId="0";
		}
		int seqID =	Integer.valueOf(seqId);
		int slaitemId = Integer.valueOf(slaItemId);
		String sql = "update oa_sal_item set FORMULA='"+formulaId+"',FORMULANAME='"+textFormula+"' where SLAITEM_ID ="+slaitemId;
		//System.out.println(sql);
		try {
			ps = dbConn.prepareStatement(sql);
		    ok = ps.executeUpdate();
			/*Map map = new HashMap();
			map.put("SEQ_ID", seqID);
			map.put("SLAITEM_ID", slaitemId);
			map.put("FORMULA", formulaId);
			map.put("FORMULANAME", textFormula);
			orm.updateSingle(dbConn, "oa_sal_item", map);*/
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return ok;
	}
	
	public static String getSalItemNameLogic(Connection conn,YHPerson user) throws Exception{
	    StringBuffer sb = new StringBuffer();
	    PreparedStatement ps = null;
		ResultSet rs = null;
	    String findSql = "SELECT SEQ_ID,SLAITEM_ID,ITEM_NAME from oa_sal_item";
	    try{
	        sb.append("{");
	        sb.append("listData:[");
	        ps = conn.prepareStatement(findSql);
	        rs = ps.executeQuery();
	        List<Map> list = new ArrayList();
	        while (rs.next()) {
	            String seqId = rs.getString("SEQ_ID");
	            String slaitemId = rs.getString("SLAITEM_ID");
	            String itemName = rs.getString("ITEM_NAME");
	            Map mapTmp = new HashMap();
	            mapTmp.put("seqId", seqId);
	            mapTmp.put("slaitemId", slaitemId);
	            mapTmp.put("itemName", itemName);
	            list.add(mapTmp);
	        }
	       for(int j = 0; j < list.size();  j++){
	          Map tmpMap = list.get(j);
	          sb.append("{");
	          sb.append("seqId:" + tmpMap.get("seqId")); 
	          sb.append(",slaitemId:\"" + tmpMap.get("slaitemId") + "\"");
	          sb.append(",itemName:\"" + tmpMap.get("itemName") + "\"");
	          sb.append("},");       
	       }
	       if(list.size() > 0)
	          sb.deleteCharAt(sb.length() - 1);
	          sb.append("]");
	          sb.append("}");
	     }catch(Exception ex){
	         throw ex;
	    }finally{
	         YHDBUtility.close(ps, rs, null);
	    }
	     return sb.toString();
	  }
	/**
	 * 查询保险系数设置
	 * @param dbConn
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<YHHrInsurancePara> findInsureBaseSetLogic(Connection dbConn, YHPerson user) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<YHHrInsurancePara> sle = null;
		YHORM orm =new YHORM();
		try {
			sle= (List<YHHrInsurancePara>) orm.loadListSingle(dbConn, YHHrInsurancePara.class, new HashMap());
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return sle;
	}
	/**
	 * 查询保险参数的总数 如果为0增加一列保险参数数据
	 * 如果大于 >0 修改保险参数数据
	 * @param dbConn
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int findInsureBaseCountLogic(Connection dbConn, YHPerson user) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT SEQ_ID from oa_pm_insurance";
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
			  return rs.getInt("SEQ_ID");
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return 0;
	}
	/**
	 * 增加一列保险参数数据
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @throws Exception
	 */
	public void addInsureBaseLogic(Connection dbConn,
			YHFileUploadForm fileForm, YHPerson person) throws Exception {
		YHORM orm = new YHORM();
		String yesOther = fileForm.getParameter("YES_OTHER");//
		String pensionPPay = fileForm.getParameter("PENSION_P_PAY");
		String pensionPPayAdd = fileForm.getParameter("PENSION_P_PAY_ADD");
		String pensionUPay = fileForm.getParameter("PENSION_U_PAY");
		String pensionUPayAdd = fileForm.getParameter("PENSION_U_PAY_ADD");
		String healthPPay = fileForm.getParameter("HEALTH_P_PAY");
		String healthPPayAdd = fileForm.getParameter("HEALTH_P_PAY_ADD");
		String healthUPay = fileForm.getParameter("HEALTH_U_PAY");
		String healthUPayAdd = fileForm.getParameter("HEALTH_U_PAY_ADD");
		String unemploymentPPay = fileForm.getParameter("UNEMPLOYMENT_P_PAY");
		String unemploymentPPayAdd = fileForm.getParameter("UNEMPLOYMENT_P_PAY_ADD");
		String unemploymentUPay = fileForm.getParameter("UNEMPLOYMENT_U_PAY");
		String unemploymentUPayAdd = fileForm.getParameter("UNEMPLOYMENT_U_PAY_ADD");
		String housingPPay = fileForm.getParameter("HOUSING_P_PAY");
		String housingPPayAdd = fileForm.getParameter("HOUSING_P_PAY_ADD");
		String housingUPay = fileForm.getParameter("HOUSING_U_PAY");
		String housingUPayAdd = fileForm.getParameter("HOUSING_U_PAY_ADD");
		String injuryUPay = fileForm.getParameter("INJURY_U_PAY");
		String injuryUPayAdd = fileForm.getParameter("INJURY_U_PAY_ADD");
		String maternityUPay = fileForm.getParameter("MATERNITY_U_PAY");
		String maternityUPayAdd = fileForm.getParameter("MATERNITY_U_PAY_ADD");
	    String deptId="";
	    int deptID = person.getDeptId();
	    if(deptID!=0){
	    	deptId = String.valueOf(deptID);
	    }
	    if(!YHUtility.isNullorEmpty(yesOther) && yesOther.equals("on")){
	    	yesOther="1";
	    }else{
	    	yesOther="0";
	    }
		try {
			YHHrInsurancePara insurPara = new YHHrInsurancePara();
			insurPara.setCreateUserId(person.getUserId());
			insurPara.setCreateDeptId(deptId); 
			insurPara.setYesOther(yesOther);
			insurPara.setPensionPPay(Double.valueOf(pensionPPay));
			insurPara.setPensionPPayAdd(Double.valueOf(pensionPPayAdd));
			insurPara.setPensionUPay(Double.valueOf(pensionUPay));
			insurPara.setPensionUPayAdd(Double.valueOf(pensionUPayAdd));
			insurPara.setHealthPPay(Double.valueOf(healthPPay));
			insurPara.setHealthPPayAdd(Double.valueOf(healthPPayAdd));
			insurPara.setHealthUPay(Double.valueOf(healthUPay));
			insurPara.setHealthUPayAdd(Double.valueOf(healthUPayAdd));
			insurPara.setUnemploymentPPay(Double.valueOf(unemploymentPPay));
			insurPara.setUnemploymentPPayAdd(Double.valueOf(unemploymentPPayAdd));
			insurPara.setUnemploymentUPay(Double.valueOf(unemploymentUPay));
			insurPara.setUnemploymentUPayAdd(Double.valueOf(unemploymentUPayAdd));
			insurPara.setHousingPPay(Double.valueOf(housingPPay));
			insurPara.setHousingPPayAdd(Double.valueOf(housingPPayAdd));
			insurPara.setHousingUPay(Double.valueOf(housingUPay));
			insurPara.setHousingUPayAdd(Double.valueOf(housingUPayAdd));
			insurPara.setInjuryUPay(Double.valueOf(injuryUPay));
			insurPara.setInjuryUPayAdd(Double.valueOf(injuryUPayAdd));
			insurPara.setMaternityUPay(Double.valueOf(maternityUPay));
			insurPara.setMaternityUPayAdd(Double.valueOf(maternityUPayAdd));
		 	  orm.saveSingle(dbConn, insurPara);
			
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 修改一列保险参数数据
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @throws Exception
	 */
	public int  upInsureBaseLogic(Connection dbConn,
			YHFileUploadForm fileForm, YHPerson person,int seqId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int OK =0;
		YHORM orm = new YHORM();
		String yesOther = fileForm.getParameter("YES_OTHER");//
		String pensionPPay = fileForm.getParameter("PENSION_P_PAY");
		String pensionPPayAdd = fileForm.getParameter("PENSION_P_PAY_ADD");
		String pensionUPay = fileForm.getParameter("PENSION_U_PAY");
		String pensionUPayAdd = fileForm.getParameter("PENSION_U_PAY_ADD");
		String healthPPay = fileForm.getParameter("HEALTH_P_PAY");
		String healthPPayAdd = fileForm.getParameter("HEALTH_P_PAY_ADD");
		String healthUPay = fileForm.getParameter("HEALTH_U_PAY");
		String healthUPayAdd = fileForm.getParameter("HEALTH_U_PAY_ADD");
		String unemploymentPPay = fileForm.getParameter("UNEMPLOYMENT_P_PAY");
		String unemploymentPPayAdd = fileForm.getParameter("UNEMPLOYMENT_P_PAY_ADD");
		String unemploymentUPay = fileForm.getParameter("UNEMPLOYMENT_U_PAY");
		String unemploymentUPayAdd = fileForm.getParameter("UNEMPLOYMENT_U_PAY_ADD");
		String housingPPay = fileForm.getParameter("HOUSING_P_PAY");
		String housingPPayAdd = fileForm.getParameter("HOUSING_P_PAY_ADD");
		String housingUPay = fileForm.getParameter("HOUSING_U_PAY");
		String housingUPayAdd = fileForm.getParameter("HOUSING_U_PAY_ADD");
		String injuryUPay = fileForm.getParameter("INJURY_U_PAY");
		String injuryUPayAdd = fileForm.getParameter("INJURY_U_PAY_ADD");
		String maternityUPay = fileForm.getParameter("MATERNITY_U_PAY");
		String maternityUPayAdd = fileForm.getParameter("MATERNITY_U_PAY_ADD");
	    String deptId="";
	    int deptID = person.getDeptId();
	    if(deptID!=0){
	    	deptId = String.valueOf(deptID);
	    }
	    if(!YHUtility.isNullorEmpty(yesOther) && yesOther.equals("on")){
	    	yesOther="1";
	    }else{
	    	yesOther="0";
	    }
	    String sql ="update oa_pm_insurance set" +
	    		" CREATE_USER_ID ="+person.getSeqId() +
	    		" ,CREATE_DEPT_ID ="+ deptId +
	    		" ,YES_OTHER ="+ yesOther +
	    		" ,PENSION_P_PAY ="+ pensionPPay +
	    		" ,PENSION_P_PAY_ADD ="+ pensionPPayAdd +
	    		" ,PENSION_U_PAY ="+ pensionUPay +
	    		" ,PENSION_U_PAY_ADD ="+ pensionUPayAdd +
	    		" ,HEALTH_P_PAY ="+ healthPPay +
	    		" ,HEALTH_P_PAY_ADD ="+ healthPPayAdd +
	    		" ,HEALTH_U_PAY ="+ healthUPay +
	    		" ,HEALTH_U_PAY_ADD ="+ healthUPayAdd +
	    		" ,UNEMPLOYMENT_P_PAY ="+ unemploymentPPay +
	    		" ,UNEMPLOYMENT_P_PAY_ADD ="+ unemploymentPPayAdd +
	    		" ,UNEMPLOYMENT_U_PAY ="+ unemploymentUPay +
	    		" ,UNEMPLOYMENT_U_PAY_ADD ="+ unemploymentUPayAdd +
	    		" ,HOUSING_P_PAY ="+ housingPPay +
	    		" ,HOUSING_P_PAY_ADD ="+ housingPPayAdd +
	    		" ,HOUSING_U_PAY="+ housingUPay +
	    		" ,HOUSING_U_PAY_ADD ="+ housingUPayAdd +
	    		" ,INJURY_U_PAY="+ injuryUPay +
	    		" ,INJURY_U_PAY_ADD ="+ injuryUPayAdd +
	    		" ,MATERNITY_U_PAY="+ maternityUPay +
	    		" ,MATERNITY_U_PAY_ADD ="+ maternityUPayAdd+" where SEQ_ID ="+seqId;
			try {
				ps = dbConn.prepareStatement(sql);
				OK = ps.executeUpdate();
			/*YHHrInsurancePara insurPara = new YHHrInsurancePara();
			insurPara.setCreateUserId(person.getUserId());
			insurPara.setCreateDeptId(deptId); 
			insurPara.setYesOther(yesOther);
			insurPara.setPensionPPay(Double.valueOf(pensionPPay));
			insurPara.setPensionPPayAdd(Double.valueOf(pensionPPayAdd));
			insurPara.setPensionUPay(Double.valueOf(pensionUPay));
			insurPara.setPensionUPayAdd(Double.valueOf(pensionUPayAdd));
			insurPara.setHealthPPay(Double.valueOf(healthPPay));
			insurPara.setHealthPPayAdd(Double.valueOf(healthPPayAdd));
			insurPara.setHealthUPay(Double.valueOf(healthUPay));
			insurPara.setHealthUPayAdd(Double.valueOf(healthUPayAdd));
			insurPara.setUnemploymentPPay(Double.valueOf(unemploymentPPay));
			insurPara.setUnemploymentPPayAdd(Double.valueOf(unemploymentPPayAdd));
			insurPara.setUnemploymentUPay(Double.valueOf(unemploymentUPay));
			insurPara.setUnemploymentUPayAdd(Double.valueOf(unemploymentUPayAdd));
			insurPara.setHousingPPay(Double.valueOf(housingPPay));
			insurPara.setHousingPPayAdd(Double.valueOf(housingPPayAdd));
			insurPara.setHousingUPay(Double.valueOf(housingUPay));
			insurPara.setHousingUPayAdd(Double.valueOf(housingUPayAdd));
			insurPara.setInjuryUPay(Double.valueOf(injuryUPay));
			insurPara.setInjuryUPayAdd(Double.valueOf(injuryUPayAdd));
			insurPara.setMaternityUPay(Double.valueOf(maternityUPay));
			insurPara.setMaternityUPayAdd(Double.valueOf(maternityUPayAdd));	*/		
			
		} catch (Exception e) {
			throw e;
		}finally {
			YHDBUtility.close(ps, rs, null);
		}
		return OK;
	}
	/* public String getSalaryItemJsonLogic1(Connection dbConn, Map request, YHPerson person) throws Exception {
		    String conditionStr = "";
		    String sql = "";
		    try {
		       sql = "SELECT * from oa_sal_item order by ITEM_ID";
		      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
		      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
		      //System.out.println(pageDataList.toJson());
		      return pageDataList.toJson();
		    } catch (Exception e) {
		      throw e;
		    }
		}*/
}

package yh.subsys.oa.hr.salary.staffInsurance.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.hr.salary.insurancePara.salItem.data.YHHrInsurancePara;
import yh.subsys.oa.hr.salary.insurancePara.salItem.data.YHHrSalData;
import yh.subsys.oa.hr.salary.insurancePara.salItem.data.YHSalItem;
import yh.subsys.oa.hr.salary.insurancePara.salItem.data.YHTwoPerson;
import yh.subsys.oa.hr.salary.submit.data.YHSalPerson;
import yh.subsys.oa.officeProduct.officeType.data.YHOfficeDepository;

public class YHWageBaseSetLogic {
	commentSalary com = new commentSalary();
	public static String getUserName(Connection dbConn, int seqId) throws Exception{ 
		//select count(user_id) from person where DEPT_ID=762 
		 String sql = "select USER_NAME from person dr where dr.SEQ_ID=" + seqId; 
		 PreparedStatement ps = null; 
		 ResultSet rs = null; 
		 try{ 
				 ps = dbConn.prepareStatement(sql); 
				 rs = ps.executeQuery(); 
			 if(rs.next()){ 
			   return rs.getString("USER_NAME"); 
			 } 
		 } catch (Exception e){ 
		     throw e; 
		 }finally{ 
		     YHDBUtility.close(ps, null, null); 
		 } 
		     return null; 
	 }
	/**
	 * 获取本部门共有多少人
	 * @param dbConn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public String getWageBaseSetLogic(Connection dbConn, int seqId) throws Exception {
		String sql = "select count(user_id) as numperson from person where DEPT_ID="+seqId;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String numPerson = "0";
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
            if(rs.next()){
            	numPerson =	rs.getString("numperson");
            }
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, null);
		}
		return numPerson;
	}
	/**
	 * 求部门id属于哪个部门的
	 * @param dbConn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public String getWhereDeptIdLogic(Connection dbConn, int seqId) throws Exception {
		String sql = "select dept_name from oa_department where seq_id="+seqId;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String deptName = "";
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
            if(rs.next()){
            	deptName =	rs.getString("dept_name");
            }
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, null);
		}
		return deptName;
	}	
	/**
	 * 获取工资项的seq_id 并且ISCOMPUTER !=1的
	 * @param dbConn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public String getSalItemIdLogic(Connection dbConn, YHPerson user) throws Exception {
		String sql = "SELECT SEQ_ID,SLAITEM_ID from oa_sal_item where ISCOMPUTER !=1 order by seq_id";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String seqIds = "";
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
            while(rs.next()){
             seqIds  += rs.getString("SLAITEM_ID")+",";
            }
           // System.out.println(seqIds);
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, null);
		}
		return seqIds;
	}	
	/**
	 * 获得薪酬项目中的Id和Name
	 * @param dbConn
	 * @param user
	 * @param seqID
	 * @return
	 * @throws Exception
	 */
	public YHSalItem getSalItemIdAndNameLogic(Connection dbConn, YHPerson user,int seqID) throws Exception {
		//select ITEM_ID,ITEM_NAME from oa_sal_item where ITEM_ID='$STYLE_ARRAY[$I]'
		String sql = "select SEQ_ID,SLAITEM_ID,ITEM_NAME,ISCOMPUTER,ISREPORT from oa_sal_item where SLAITEM_ID ="+seqID;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String seqIds = "";
		YHSalItem salItem = new YHSalItem();
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
            if(rs.next()){
            	//deptName =	rs.getString("dept_name");
              salItem.setSeqId(rs.getInt("SEQ_ID"));
              salItem.setSlaitemId(rs.getInt("SLAITEM_ID"));
              salItem.setItemName(rs.getString("ITEM_NAME"));
              salItem.setIscomputer(rs.getString("ISCOMPUTER"));
              salItem.setIsreport(rs.getString("ISREPORT")); 
            }
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, null);
		}
		return salItem;
	}
	/**
	 * 查询HR_INSURANCE_PARA中的yesOther 是否为1 为1显示保险系数
	 * @param dbConn
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public String getYesOtherLogic(Connection dbConn, YHPerson user) throws Exception {
		String sql = "SELECT YES_OTHER from oa_pm_insurance";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String seqIds = "";
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
            if(rs.next()){
             return rs.getString("YES_OTHER");
            }
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, null);
		}
		return null;
	}
	public List<YHHrInsurancePara> getYesOtherLogic1(Connection dbConn, YHPerson user) throws Exception {
		YHORM orm =new YHORM();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<YHHrInsurancePara> insurancePara = null;
		try {
			//sle= (YHSalItem) orm.loadObjSingle(dbConn, YHSalItem.class, map);
			//orm.loadListSingle(dbConn, cls, filters);
			insurancePara = (List<YHHrInsurancePara>)orm.loadListSingle(dbConn, YHHrInsurancePara.class,new HashMap());
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, null);
		}
		return insurancePara;
	}
	
	/**
	 * 根据部门id查找所有属于这个部门的人员
	 * @param dbConn
	 * @param user
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public List<YHTwoPerson> getDeptPersonNameLogic(Connection dbConn, YHPerson user,String seqId) throws Exception {
		
		String sql = "SELECT SEQ_ID,USER_ID,USER_NAME from  person where DEPT_ID="+seqId+" order by seq_id";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String seqIds = "";
		List<YHTwoPerson> listPerson = new ArrayList<YHTwoPerson>();
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
            while(rs.next()){
            	YHTwoPerson person = new YHTwoPerson();
            	person.setSeqId(rs.getInt("SEQ_ID"));
                person.setUserId(rs.getString("USER_ID"));
                person.setUserName(rs.getString("USER_NAME"));
                person.setSalData(getAllHrSalDataLogicLists(dbConn,user,rs.getString("SEQ_ID")));
                listPerson.add(person);
            }
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, null);
		}
		return listPerson;
	}
	
	 public List<YHHrSalData> getAllHrSalDataLogicLists(Connection dbConn, YHPerson user,String userId) throws Exception{
	    	String sql = "select * from oa_pm_salary_data where USER_ID ="+ userId;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String seqIds = "";
			String STR="";
			YHHrSalData salData = new YHHrSalData();
			List<YHHrSalData> dates = new ArrayList<YHHrSalData>();
			try {
				stmt = dbConn.prepareStatement(sql);
				rs = stmt.executeQuery();
	            while(rs.next()){
	            	for(int i=0; i<=50; i++){
	            		STR+="S"+i+",";
	            		//list.add(STR);
	            	}
	            	salData.setAllBase(rs.getDouble(("ALL_BASE")));
	            	salData.setPensionBase(rs.getDouble("PENSION_BASE"));
	            	salData.setPensionU(rs.getDouble("PENSION_U"));
	            	salData.setPensionP(rs.getDouble("PENSION_P"));
	            	salData.setMedicalBase(rs.getDouble("MEDICAL_BASE"));
	            	salData.setMedicalU(rs.getDouble("MEDICAL_U"));
	            	salData.setMedicalP(rs.getDouble("MEDICAL_P"));
	            	salData.setFertilityBase(rs.getDouble("FERTILITY_BASE"));
	            	salData.setFertilityU(rs.getDouble("FERTILITY_U"));
	            	salData.setUnemploymentBase(rs.getDouble("UNEMPLOYMENT_BASE"));
	            	salData.setUnemploymentU(rs.getDouble("UNEMPLOYMENT_U"));
	            	salData.setUnemploymentP(rs.getDouble("UNEMPLOYMENT_P"));
	            	salData.setInjuriesBase(rs.getDouble("INJURIES_BASE"));
	            	salData.setInjuriesU(rs.getDouble("INJURIES_U"));
	            	salData.setHousingBase(rs.getDouble("HOUSING_BASE"));
	            	salData.setHousingU(rs.getDouble("HOUSING_U"));
	            	salData.setHousingP(rs.getDouble("HOUSING_P"));
	            	//salData.setPersonId(user.getSeqId());
	            	//salData.setUserName(user.getUserName());
	            	dates.add(salData);
	            }
			} catch (Exception e) {
				throw e;
			} finally {
				YHDBUtility.close(stmt, rs, null);
			}
			return dates;
	    }
	
	 public Map getHrSalDateLogic(Connection dbConn, YHPerson user,int userId,String sid) throws Exception {
		     String sqls ="";
			 String[] sids = sid.split(",");
			 sqls = "select user_id"; 
			       for(int i=0; sids.length>0 && i<sids.length; i++){
			    	  if(!YHUtility.isNullorEmpty(sids[i])){
			    	  sqls += ","+sids[i]+" ";
			    	  }
			       }
			       sqls += " from oa_pm_salary_data where user_id="+userId+"order by seq_id"; 
			       
			      // System.out.println(sqls);
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String seqIds = "";
			Map map = new TreeMap();
			YHHrSalData salData = new YHHrSalData();
			try {
				stmt = dbConn.prepareStatement(sqls);
				rs = stmt.executeQuery();
	
	            if(rs.next()){
	            	//salData.setUserId(rs.getString("user_id"));
	            	map.put("USER_ID",rs.getString(1));
	            	for (int i = 2; i < sids.length + 2; i++) {
	            		//rs.next();
	            		map.put("S" + i,rs.getString(i));
					}
	           
	            }
			} catch (Exception e) {
				throw e;
			} finally {
				YHDBUtility.close(stmt, rs, null);
			}
			return map;
		}
	 
	/**
	 * 根据部门id查找所有属于这个部门的人员
	 * @param dbConn
	 * @param user
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public List<YHPerson> getDeptPersonIdsList(Connection dbConn,String deptId) throws Exception {
		
		String sql = "SELECT SEQ_ID,USER_ID,USER_NAME from  person where DEPT_ID="+deptId+" order by seq_id";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String seqIds = "";
		List<YHPerson> listPerson = new ArrayList<YHPerson>();
		 YHPerson user = new YHPerson();
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
            while(rs.next()){
            	YHPerson person = new YHPerson();
                person.setSeqId(rs.getInt("SEQ_ID"));
                person.setUserId(rs.getString("USER_ID"));
                person.setUserName(rs.getString("USER_NAME"));
                listPerson.add(person);
            }
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, null);
		}
		return listPerson;
	}
	
	
	 public List<YHHrSalData> getAllHrSalList(Connection dbConn, String deptId) throws Exception{
		 List<YHPerson>  users = getDeptPersonIdsList(dbConn, deptId);
		 List<YHHrSalData> datas = new ArrayList<YHHrSalData>();
		 for(int i=0;  i<users.size(); i++){
			 List<YHHrSalData>list =  getAllHrSalDataLogicList(dbConn, users.get(i));
			 if(list != null && list.size() > 0){
				 datas.add(list.get(0));
			 }
		 }
		 return datas;
	 }
	
	
    public List<YHHrSalData> getAllHrSalDataLogicList(Connection dbConn, YHPerson user) throws Exception{
    	String sql = "select * from oa_pm_salary_data where USER_ID ="+ user.getSeqId();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String seqIds = "";
		String str="";
		YHHrSalData salData = new YHHrSalData();
		List<YHHrSalData> dates = new ArrayList<YHHrSalData>();
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
            while(rs.next()){
            	for(int i=0; i<=50; i++){
            		str+="S"+i+",";
            	}
            	salData.setAllBase(rs.getDouble(("ALL_BASE")));
            	salData.setPensionBase(rs.getDouble("PENSION_BASE"));
            	salData.setPensionU(rs.getDouble("PENSION_U"));
            	salData.setPensionP(rs.getDouble("PENSION_P"));
            	salData.setMedicalBase(rs.getDouble("MEDICAL_BASE"));
            	salData.setMedicalU(rs.getDouble("MEDICAL_U"));
            	salData.setMedicalP(rs.getDouble("MEDICAL_P"));
            	salData.setFertilityBase(rs.getDouble("FERTILITY_BASE"));
            	salData.setFertilityU(rs.getDouble("FERTILITY_U"));
            	salData.setUnemploymentBase(rs.getDouble("UNEMPLOYMENT_BASE"));
            	salData.setUnemploymentU(rs.getDouble("UNEMPLOYMENT_U"));
            	salData.setUnemploymentP(rs.getDouble("UNEMPLOYMENT_P"));
            	salData.setInjuriesBase(rs.getDouble("INJURIES_BASE"));
            	salData.setInjuriesU(rs.getDouble("INJURIES_U"));
            	salData.setHousingBase(rs.getDouble("HOUSING_BASE"));
            	salData.setHousingU(rs.getDouble("HOUSING_U"));
            	salData.setHousingP(rs.getDouble("HOUSING_P"));
            	//salData.setPersonId(user.getSeqId());
            	salData.setUserName(user.getUserName());
            	dates.add(salData);
            }
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, null);
		}
		return dates;
    }
	/**
	 * 通过用户名id 获取HR_SAL_DATA信息
	 * @param dbConn
	 * @param user
	 * @param seqID
	 * @return
	 * @throws Exception
	 */
	public YHHrSalData getAllHrSalDataLogic(Connection dbConn, YHPerson user,int seqID) throws Exception {
		String sql = "select * from oa_pm_salary_data where USER_ID ="+seqID;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String seqIds = "";
		String str="";
		YHHrSalData salData = new YHHrSalData();
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
            if(rs.next()){
            	for(int i=0; i<=50; i++){
            		str+="S"+i+",";
            	}
            	salData.setAllBase(rs.getDouble(("ALL_BASE")));
            	salData.setPensionBase(rs.getDouble("PENSION_BASE"));
            	salData.setPensionU(rs.getDouble("PENSION_U"));
            	salData.setPensionP(rs.getDouble("PENSION_P"));
            	salData.setMedicalBase(rs.getDouble("MEDICAL_BASE"));
            	salData.setMedicalU(rs.getDouble("MEDICAL_U"));
            	salData.setMedicalP(rs.getDouble("MEDICAL_P"));
            	salData.setFertilityBase(rs.getDouble("FERTILITY_BASE"));
            	salData.setFertilityU(rs.getDouble("FERTILITY_U"));
            	salData.setUnemploymentBase(rs.getDouble("UNEMPLOYMENT_BASE"));
            	salData.setUnemploymentU(rs.getDouble("UNEMPLOYMENT_U"));
            	salData.setUnemploymentP(rs.getDouble("UNEMPLOYMENT_P"));
            	salData.setInjuriesBase(rs.getDouble("INJURIES_BASE"));
            	salData.setInjuriesU(rs.getDouble("INJURIES_U"));
            	salData.setHousingBase(rs.getDouble("HOUSING_BASE"));
            	salData.setHousingU(rs.getDouble("HOUSING_U"));
            	salData.setHousingP(rs.getDouble("HOUSING_P"));
            }
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, null);
		}
		return salData;
	}
	/**
	 * 获得薪酬项目中的Id和Name的数据
	 * @param dbConn
	 * @param user
	 * @param seqID
	 * @return
	 * @throws Exception
	 */
	public YHSalItem getSalItemIdAndNameData(Connection dbConn, YHPerson user,int seqID) throws Exception {
		//select ITEM_ID,ITEM_NAME from oa_sal_item where ITEM_ID='$STYLE_ARRAY[$I]'
		String sql = "select SEQ_ID,ITEM_NAME from oa_sal_item where SEQ_ID ="+seqID;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String seqIds = "";
		YHSalItem salItem = new YHSalItem();
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
            if(rs.next()){
            	//deptName =	rs.getString("dept_name");
              salItem.setSeqId(rs.getInt("SEQ_ID"));
              salItem.setItemName(rs.getString("ITEM_NAME"));
              
              
            }
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, null);
		}
		return salItem;
	}
	/**
	 * 批量 获取 所有员工的 薪酬项目和保险系数
	 * @param dbConn
	 * @param user
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public List<YHSalPerson> getDeptPersonNameLogic1(Connection dbConn, YHPerson user,String seqId) throws Exception {
	    
	    String sql = " select SLAITEM_ID SID from oa_sal_item "
	    	       + " where ISCOMPUTER !=1 "
	               + " order by SLAITEM_ID ";
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    List<String> sid = new ArrayList<String>();
	    List<YHSalPerson> listPerson = new ArrayList<YHSalPerson>();
	    try{
	      stmt = dbConn.prepareStatement(sql);
	      rs = stmt.executeQuery();
	      while(rs.next()){
	        sid.add(rs.getString("SID"));
	      } 
	      
	      StringBuffer sb = new StringBuffer();
	      for(int i = 0; i < sid.size(); i++){
	    	  sb.append("(select " + "S"+sid.get(i) + " from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) " + "S"+sid.get(i) + " ,");
	      }
	      sb.append("(select ALL_BASE from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) ALL_BASE ,");
	      sb.append("(select PENSION_BASE from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) PENSION_BASE ,");
	      sb.append("(select PENSION_U from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) PENSION_U ,");
	      sb.append("(select PENSION_P from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) PENSION_P ,");
	      sb.append("(select MEDICAL_BASE from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) MEDICAL_BASE ,");
	      sb.append("(select MEDICAL_U from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) MEDICAL_U ,");
	      sb.append("(select MEDICAL_P from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) MEDICAL_P ,");
	      sb.append("(select FERTILITY_BASE from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) FERTILITY_BASE ,");
	      sb.append("(select FERTILITY_U from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) FERTILITY_U ,");
	      sb.append("(select UNEMPLOYMENT_BASE from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) UNEMPLOYMENT_BASE ,");
	      sb.append("(select UNEMPLOYMENT_U from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) UNEMPLOYMENT_U ,");
	      sb.append("(select UNEMPLOYMENT_P from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) UNEMPLOYMENT_P ,");
	      sb.append("(select INJURIES_BASE from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) INJURIES_BASE ,");
	      sb.append("(select INJURIES_U from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) INJURIES_U ,");
	      sb.append("(select HOUSING_BASE from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) HOUSING_BASE ,");
	      sb.append("(select HOUSING_U from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) HOUSING_U ,");
	      sb.append("(select HOUSING_P from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) HOUSING_P ,");
	      sql = " select p1.SEQ_ID USER_ID, p1.USER_NAME ,(select SEQ_ID from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) HSD_ID ," + sb.substring(0, sb.length()-1)
	          + " from PERSON p1 "
	          + " join USER_PRIV u1 on u1.SEQ_ID = p1.USER_PRIV "
	          + " where  DEPT_ID = " + seqId + " or "+ YHDBUtility.findInSet(String.valueOf(seqId), "DEPT_ID_OTHER")
	          + " order by u1.PRIV_NO ";
	     // System.out.println(sql);
	      stmt = dbConn.prepareStatement(sql);
	      rs = stmt.executeQuery();      
	      
	      while(rs.next()){
	        YHSalPerson salPerson = new YHSalPerson();
	        
	        salPerson.setHsdId(rs.getInt("HSD_ID"));
	        salPerson.setUserId(rs.getInt("USER_ID"));
	        salPerson.setUserName(rs.getString("USER_NAME"));
	        salPerson.setAllBase(rs.getDouble("ALL_BASE"));
	        salPerson.setPensionBase(rs.getDouble("PENSION_BASE"));
	        salPerson.setPensionU(rs.getDouble("PENSION_U"));
	        salPerson.setPensionP(rs.getDouble("PENSION_P"));
	        salPerson.setMedicalBase(rs.getDouble("MEDICAL_BASE"));
	        salPerson.setMedicalU(rs.getDouble("MEDICAL_U"));
	        salPerson.setMedicalP(rs.getDouble("MEDICAL_P"));
	        salPerson.setFertilityBase(rs.getDouble("FERTILITY_BASE"));
	        salPerson.setFertilityU(rs.getDouble("FERTILITY_U"));
	        salPerson.setUnemploymentBase(rs.getDouble("UNEMPLOYMENT_BASE"));
	        salPerson.setUnemploymentU(rs.getDouble("UNEMPLOYMENT_U"));
	        salPerson.setUnemploymentP(rs.getDouble("UNEMPLOYMENT_P"));
	        salPerson.setInjuriesBase(rs.getDouble("INJURIES_BASE"));
	        salPerson.setInjuriesU(rs.getDouble("INJURIES_U"));
	        salPerson.setHousingBase(rs.getDouble("HOUSING_BASE"));
	        salPerson.setHousingU(rs.getDouble("HOUSING_U"));
	        salPerson.setHousingP(rs.getDouble("HOUSING_P"));
	        Map<String,Double> smap = new HashMap<String,Double>();
	        List<String> slist = new ArrayList<String>();
	        for(int i = 0; i < sid.size(); i++){
	          smap.put("S"+sid.get(i), rs.getDouble("S"+sid.get(i)));//smap中获取动态Si-Si rs.getDouble(sid.get(i))的值
	          slist.add(i, (String)"S"+sid.get(i)); //获取动态的s1-s50(记得动态的不确定有几个值)
	          
	        }
	        salPerson.setSmap(smap);
	        salPerson.setSlist(slist);
	        listPerson.add(salPerson);
	      }
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      YHDBUtility.close(stmt, rs, null);
	    }
	    return listPerson;
	  }	
	/**
	 * 增加薪酬基数
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @throws Exception
	 */
	public void addWageBaseLogic(Connection dbConn,
			YHFileUploadForm fileForm, YHPerson person) throws Exception {
		 PreparedStatement ps = null;
		    ResultSet rs = null;
		    PreparedStatement ps1 = null;
		    ResultSet rs1 = null;
		YHORM orm = new YHORM();
		String allBase = fileForm.getParameter("allBase");
		String pensionBase = fileForm.getParameter("pensionBase");
		String pensionU = fileForm.getParameter("pensionU");
		String pensionP = fileForm.getParameter("pensionP"); 
		String medicalBase = fileForm.getParameter("medicalBase");
		String medicalU = fileForm.getParameter("medicalU");
		String medicalP = fileForm.getParameter("medicalP");
		String fertilityBase = fileForm.getParameter("fertilityBase"); 
		String fertilityU = fileForm.getParameter("fertilityU");
		String unemploymentBase = fileForm.getParameter("unemploymentBase");
		String unemploymentU = fileForm.getParameter("unemploymentU");
		String unemploymentP = fileForm.getParameter("unemploymentP"); 
		String injuriesBase = fileForm.getParameter("injuriesBase");
		String injuriesU = fileForm.getParameter("injuriesU");
		String housingBase = fileForm.getParameter("housingBase");
		String housingU = fileForm.getParameter("housingU");
		String housingP = fileForm.getParameter("housingP");
		String userNames = fileForm.getParameter("userNames");
		String[] allBaseLen = allBase.split(",");
		String[] pensionBaseLen = pensionBase.split(",");
		String[] pensionUlen = pensionU.split(",");
		String[] pensionPlen = pensionP.split(",");
		String[] medicalBaselen = medicalBase.split(",");
		String[] medicalUlen = medicalU.split(",");
		String[] medicalPlen = medicalP.split(",");
		String[] fertilityBaseLen = fertilityBase.split(",");
		String[] fertilityUlen = fertilityU.split(",");
		String[] unemploymentBaseLen = unemploymentBase.split(",");
		String[] unemploymentUlen = unemploymentU.split(",");
		String[] unemploymentPlen = unemploymentP.split(",");
		String[] injuriesBaseLen = injuriesBase.split(",");
		String[] injuriesUlen = injuriesU.split(",");
		String[] housingBaselen = housingBase.split(",");
		String[] housingUlen = housingU.split(",");
		String[] housingPlen = housingP.split(",");
		String[] userNamesLen = userNames.split(",");//取得用户名ID
		String[] par1 = null;
		String slaItemId = getSalItemIdLogic(dbConn,person);//取得动态的薪酬项的名字
		String [] ids = slaItemId.split(",");
		par1 = new String[ids.length];
		for(int m=0; m<ids.length; m++){
			par1[m]= fileForm.getParameter("params_"+ids[m]); //取得动态的薪酬项数据			
		}
		try {
			for(int i = 0; i < userNamesLen.length; i++){
		        String sql = " select * from oa_pm_salary_data where USER_ID="+userNamesLen[i];
		        ps = dbConn.prepareStatement(sql);
		        rs = ps.executeQuery();
		        if(rs.next()){
		          sql = " update oa_pm_salary_data set ALL_BASE="+ allBaseLen[i]+"" +
		          		" ,PENSION_BASE="+pensionBaseLen[i]+"" +
		          		" ,PENSION_U="+pensionUlen[i]+"" +
		          		", PENSION_P="+pensionPlen[i]+"" +
		          		" ,MEDICAL_BASE="+medicalBaselen[i]+"" +
		          		" ,MEDICAL_U="+medicalUlen[i]+"" +
		          		", MEDICAL_P="+medicalPlen[i]+"" +		
		          		" ,FERTILITY_BASE="+fertilityBaseLen[i]+"" +
		          		" ,FERTILITY_U="+fertilityUlen[i]+"" +
		          		", UNEMPLOYMENT_BASE="+unemploymentBaseLen[i]+"" +
		          		" ,UNEMPLOYMENT_U="+unemploymentUlen[i]+"" +
		          		" ,UNEMPLOYMENT_P="+unemploymentPlen[i]+"" +
		          		", INJURIES_BASE="+injuriesBaseLen[i]+"" +    		
		          		" ,INJURIES_U="+injuriesUlen[i]+"" +
		          		" ,HOUSING_BASE="+housingBaselen[i]+"" +
		          		", HOUSING_U ="+housingUlen[i]+"" +
		          		", HOUSING_P ="+housingPlen[i];
		          for(int m=0; m<ids.length; m++){
		  			par1[m]= fileForm.getParameter("params_"+ids[m]); //取得动态的薪酬项数据
		  			String[] sqlPara = par1[m].split(",");
		  				sql += ",S"+ids[m] + "=" + sqlPara[i];
		  		  } 
		          sql += " where user_id = " + userNamesLen[i];
		        }
	           else{
	        	   sql = " insert into oa_pm_salary_data(USER_ID, ALL_BASE"
	          		+ " ,PENSION_BASE"
	          		+ " ,PENSION_U"
	          		+ ", PENSION_P"
	          		+ " ,MEDICAL_BASE"
	          		+ " ,MEDICAL_U"
	          		+ ", MEDICAL_P"
	          		+ " ,FERTILITY_BASE"
	          		+ " ,FERTILITY_U"
	          		+ ", UNEMPLOYMENT_BASE"
	          		+ " ,UNEMPLOYMENT_U"
	          		+ " ,UNEMPLOYMENT_P"
	          		+ ", INJURIES_BASE	"
	          		+ " ,INJURIES_U"
	          		+ " ,HOUSING_BASE"
	          		+ ", HOUSING_U "
	          		+ ", HOUSING_P ";   
	          for(int m=0; m<ids.length; m++){
	  			par1[m]= fileForm.getParameter("params_"+ids[m]); //取得动态的薪酬项数据
	  			String[] sqlPara = par1[m].split(",");
	  				sql += ",S"+ids[m];
	  		  } 
	          sql += ") values (";
	          sql += userNamesLen[i];
	          sql +=","+allBaseLen[i];	
	          sql += ","+ pensionBaseLen[i] ;	
	          sql += ","+ pensionUlen[i] ;	
	          sql += ","+ pensionPlen[i] ;	
	          sql += ","+ medicalBaselen[i] ;	
	          sql += ","+ medicalUlen[i] ;	
	          sql += ","+ medicalPlen[i] ;	
	          sql += ","+ fertilityBaseLen[i] ;	
	          sql += ","+ fertilityUlen[i] ;	
	          sql += ","+ unemploymentBaseLen[i] ;	
	          sql += ","+ unemploymentUlen[i] ;	
	          sql += ","+ unemploymentPlen[i] ;	
	          sql += ","+ injuriesBaseLen[i] ;	
	          sql += ","+ injuriesUlen[i] ;	
	          sql += ","+ housingBaselen[i] ;	
	          sql += ","+ housingUlen[i] ;	
	          sql += ","+ housingPlen[i];
	          for(int m=0; m<ids.length; m++){
		  			par1[m]= fileForm.getParameter("params_"+ids[m]); //取得动态的薪酬项数据
		  			String[] sqlPara = par1[m].split(",");
		  			sql += ","+sqlPara[i];
		  	  } 
	          sql += ")";
		     }
		        //System.out.println(sql);
		        ps1 = dbConn.prepareStatement(sql);
		        ps1.executeUpdate();     
			}
		} catch (Exception e) {
			throw e;
		}		
	}
	/**
	 * 批量增加或更新 部门下所有员工的 薪酬项目和保险系数
	 * @param dbConn
	 * @param request
	 * @param person
	 * @throws Exception
	 */
	 @SuppressWarnings("unused")
	  public void setSubmitInfo(Connection dbConn, Map<String,String> request, YHPerson person) throws Exception {
	    String flowId = request.get("flowId");
	    String totalStr = request.get("total");
	    String titleStr = request.get("title");
	    if (!YHUtility.isNullorEmpty(titleStr)) {
	      titleStr = titleStr.substring(0, titleStr.length() - 1 );
	    }
	    String titleList[] = titleStr.split(",");//获取动态的薪酬项目S1-S50 区分长度
	    int total = 0;
	    if (!YHUtility.isNullorEmpty(totalStr)) {
	      total = Integer.parseInt(totalStr);
	    }
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	      for(int i = 0; i < total; i++){
	    	  String userID ="";
	    	  String userIdStr = request.get("userId_" + i);
	    	  if (!YHUtility.isNullorEmpty(userIdStr)) {
	    		  userID = userIdStr;
		      }
	    	  String sqls = " select * from oa_pm_salary_data where USER_ID="+userID;
		        ps = dbConn.prepareStatement(sqls);
		        rs = ps.executeQuery();	        
	        List<String> Slist = new ArrayList<String>();
	        for(int j = 0; j < titleList.length; j++){
	          Slist.add(request.get(titleList[j]+"_"+i));//和页面对应titleList[j]获取动态的薪酬项目S1-S50  在加上 '_i' 就是对应页面中获取项目中的名称
	        }
	          String userId = request.get("userId_" + i);
	          String userName = request.get("userName_" + i);
	          String allBase = request.get(userName+"_ALL_BASE");
	          String pensionBase = request.get(userName+"_PENSION_BASE");
	          String pensionU = request.get(userName+"_PENSION_U");
	          String pensionP = request.get(userName+"_PENSION_P");
	          String medicalBase = request.get(userName+"_MEDICAL_BASE");
	          String medicalU = request.get(userName+"_MEDICAL_U");
	          String medicalP = request.get(userName+"_MEDICAL_P");
	          String fertilityBase = request.get(userName+"_FERTILITY_BASE");
	          String fertilityU = request.get(userName+"_FERTILITY_U");
	          String unemploymentBase = request.get(userName+"_UNEMPLOYMENT_BASE");
	          String unemploymentU = request.get(userName+"_UNEMPLOYMENT_U");
	          String unemploymentP = request.get(userName+"_UNEMPLOYMENT_P");
	          String injuriesBase = request.get(userName+"_INJURIES_BASE");
	          String injuriesU = request.get(userName+"_INJURIES_U");
	          String housingBase = request.get(userName+"_HOUSING_BASE");
	          String housingU = request.get(userName+"_HOUSING_U");
	          String housingP = request.get(userName+"_HOUSING_P");
	        if(rs.next()){
	          sqls = " update oa_pm_salary_data set ALL_BASE="+allBase+"" +
	          		", PENSION_BASE="+pensionBase+"" +
	          		", PENSION_U="+pensionU+"" +
	          		", PENSION_P="+pensionP+"" +
	          		" ,MEDICAL_BASE="+medicalBase+""+
	          	  	" ,MEDICAL_U="+medicalU+""+
	          		", MEDICAL_P="+medicalP+""+
	          		" ,FERTILITY_BASE="+fertilityBase+""+
	          	    " ,FERTILITY_U="+fertilityU+""+
	          		", UNEMPLOYMENT_BASE="+unemploymentBase+""+
	          		" ,UNEMPLOYMENT_U="+unemploymentU+""+
	          		" ,UNEMPLOYMENT_P="+unemploymentP+""+
	          		", INJURIES_BASE="+injuriesBase+""+
	          		" ,INJURIES_U="+injuriesU+""+
	          		" ,HOUSING_BASE="+housingBase+""+
	          		", HOUSING_U ="+housingU+""+
	          		", HOUSING_P ="+housingP+"" ;
	          		for(int n=0; n<titleList.length;n++){
	          		  if (!YHUtility.isNullorEmpty(titleList[n]) 
	          		      && !YHUtility.isNullorEmpty(Slist.get(n)))  {
	          		    sqls+=","+titleList[n]+"="+Slist.get(n);
	          		  }
	          		}
	          		sqls +=" where USER_ID="+userId;
	          		//System.out.println(sqls);
	          	 ps = dbConn.prepareStatement(sqls);
	 		     ps.executeUpdate(); 
	        }else{   
	          String str = "";
	          String titleStrTemp = "";
            if (!YHUtility.isNullorEmpty(titleStr)) {
              titleStrTemp = ","+titleStr;
              str = ","+Slist.toString().substring(1, Slist.toString().length()-1);
            }
	           sqls = " insert into oa_pm_salary_data(USER_ID, ALL_BASE"
	          		+ " ,PENSION_BASE"
	          		+ " ,PENSION_U"
	          		+ ", PENSION_P"
	          		+ " ,MEDICAL_BASE"
	          		+ " ,MEDICAL_U"
	          		+ ", MEDICAL_P"
	          		+ " ,FERTILITY_BASE"
	          		+ " ,FERTILITY_U"
	          		+ ", UNEMPLOYMENT_BASE"
	          		+ " ,UNEMPLOYMENT_U"
	          		+ " ,UNEMPLOYMENT_P"
	          		+ ", INJURIES_BASE	"
	          		+ " ,INJURIES_U"
	          		+ " ,HOUSING_BASE"
	          		+ ", HOUSING_U "
	          		+ ", HOUSING_P " 
	          		+ titleStrTemp+" ) values ("+userId+","+allBase+","+pensionBase+","+pensionU+","+pensionP+","+medicalBase+"" +
	          				" ,"+medicalU+","+medicalP+","+fertilityBase+","+fertilityU+","+unemploymentBase+","+unemploymentU+"" +
	          				" ,"+unemploymentP+","+injuriesBase+","+injuriesU+","+housingBase+","+housingU+","+housingP
	          				+ str +" )";
	          ps = dbConn.prepareStatement(sqls);
	          ps.executeUpdate();
	          YHDBUtility.close(ps, rs, null);
	        }    
	      }
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      YHDBUtility.close(ps, rs, null);
	    }
	 }
	 
	 
	/**
	 * 设置单个员工的 薪酬项目和保险系数
	 * @param dbConn
	 * @param request
	 * @param person
	 * @throws Exception
	 */
	 @SuppressWarnings("unused")
	  public void setOnePersonSubmitLogic(Connection dbConn, Map<String,String> request, YHPerson person) throws Exception {
	    String totalStr = request.get("total");//人员个数
	    String titleStr = request.get("title");//设置的薪酬项目名称个数
		String titleList[] = new String[0];//获取动态的薪酬项目S1-S50 区分长度

        if(!YHUtility.isNullorEmpty(titleStr)){
        	titleStr = titleStr.substring(0, titleStr.length()-1);
        	titleList= titleStr.split(",");
        }
	    String userId = request.get("userId");//用户 id
	    
	   // String titleList[] = titleStr.split(",");//获取动态的薪酬项目S1-S50 区分长度
	    int total = 0;
	    if (!YHUtility.isNullorEmpty(totalStr)) {
	      total = Integer.parseInt(totalStr);
	    }
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	      for(int i = 0; i < total; i++){
	    	  String sqls = " select * from oa_pm_salary_data where USER_ID="+userId;
		        ps = dbConn.prepareStatement(sqls);
		        rs = ps.executeQuery();	        
	        List<String> Slist = new ArrayList<String>();
	        for(int j = 0; j < titleList.length; j++){
	          Slist.add(request.get(titleList[j]));//和页面对应titleList[j]获取动态的薪酬项目S1-S50  在加上 '_i' 就是对应页面中获取项目中的名称
	        }
	          //String userName = request.get("userName_" + i);
	          String allBase = request.get("allBase");
	          String pensionBase = request.get("pensionBase");
	          String pensionU = request.get("pensionU");
	          String pensionP = request.get("pensionP");
	          String medicalBase = request.get("medicalBase");
	          String medicalU = request.get("medicalU");
	          String medicalP = request.get("medicalP");
	          String fertilityBase = request.get("fertilityBase");
	          String fertilityU = request.get("fertilityU");
	          String unemploymentBase = request.get("unemploymentBase");
	          String unemploymentU = request.get("unemploymentU");
	          String unemploymentP = request.get("unemploymentP");
	          String injuriesBase = request.get("injuriesBase");
	          String injuriesU = request.get("injuriesU");
	          String housingBase = request.get("housingBase");
	          String housingU = request.get("housingU");
	          String housingP = request.get("housingP");
	         if(rs.next()){
	          sqls = " update oa_pm_salary_data set ALL_BASE="+allBase+"" +
	          		", PENSION_BASE="+pensionBase+"" +
	          		", PENSION_U="+pensionU+"" +
	          		", PENSION_P="+pensionP+"" +
	          		" ,MEDICAL_BASE="+medicalBase+""+
	          	  	" ,MEDICAL_U="+medicalU+""+
	          		", MEDICAL_P="+medicalP+""+
	          		" ,FERTILITY_BASE="+fertilityBase+""+
	          	    " ,FERTILITY_U="+fertilityU+""+
	          		", UNEMPLOYMENT_BASE="+unemploymentBase+""+
	          		" ,UNEMPLOYMENT_U="+unemploymentU+""+
	          		" ,UNEMPLOYMENT_P="+unemploymentP+""+
	          		", INJURIES_BASE="+injuriesBase+""+
	          		" ,INJURIES_U="+injuriesU+""+
	          		" ,HOUSING_BASE="+housingBase+""+
	          		", HOUSING_U ="+housingU+""+
	          		", HOUSING_P ="+housingP+"" ;
	          		for(int n=0; n<titleList.length;n++){
	          		 sqls+=","+titleList[n]+"="+Slist.get(n);
	          		}
	          		sqls +=" where USER_ID="+userId;
	          		//System.out.println(sqls);
	          	 ps = dbConn.prepareStatement(sqls);
	 		     ps.executeUpdate(); 
	        }else{  
	           String str = "";
	           if (!"".equals(titleStr)) {
	             titleStr = ", "+titleStr;
	             str = ","+Slist.toString().substring(1, Slist.toString().length()-1);
	           }
	           sqls = " insert into oa_pm_salary_data(USER_ID, ALL_BASE"
	          		+ " ,PENSION_BASE"
	          		+ " ,PENSION_U"
	          		+ ", PENSION_P"
	          		+ " ,MEDICAL_BASE"
	          		+ " ,MEDICAL_U"
	          		+ ", MEDICAL_P"
	          		+ " ,FERTILITY_BASE"
	          		+ " ,FERTILITY_U"
	          		+ ", UNEMPLOYMENT_BASE"
	          		+ " ,UNEMPLOYMENT_U"
	          		+ " ,UNEMPLOYMENT_P"
	          		+ ", INJURIES_BASE	"
	          		+ " ,INJURIES_U"
	          		+ " ,HOUSING_BASE"
	          		+ ", HOUSING_U "
	          		+ ", HOUSING_P " 
	          		 +titleStr +") values ("+userId+","+allBase+","+pensionBase+","+pensionU+","+pensionP+","+medicalBase+"" +
	          				" ,"+medicalU+","+medicalP+","+fertilityBase+","+fertilityU+","+unemploymentBase+","+unemploymentU+"" +
	          				" ,"+unemploymentP+","+injuriesBase+","+injuriesU+","+housingBase+","+housingU+","+housingP 
	          				+ str
	          				+" )";
             // System.out.println(sqls);
              ps = dbConn.prepareStatement(sqls);
              ps.executeUpdate();
	        } 
	      }
	    } catch (Exception e) {
	      throw e;
	    }
	 }
	 
	 /**
	  * 获取单个人员的薪酬项目和保险基数
	  * @param dbConn
	  * @param user
	  * @param seqId
	  * @return
	  * @throws Exception
	  */
  public YHSalPerson getPersonNameLogic(Connection dbConn, YHPerson user,String seqId) throws Exception {
		    YHSalPerson salPerson = new YHSalPerson();
		    String sql = " select SLAITEM_ID SID from oa_sal_item "
		    	       + " where ISCOMPUTER !=1 "
		               + " order by SLAITEM_ID ";
		    //System.out.println(sql);
		    PreparedStatement stmt = null;
		    ResultSet rs = null;
		    ResultSet rsTemp = null;
		    List<String> sid = new ArrayList<String>();
		    List<YHSalPerson> listPerson = new ArrayList<YHSalPerson>();
		    try{
		      stmt = dbConn.prepareStatement(sql);
		      rs = stmt.executeQuery();
		      while(rs.next()){
		        sid.add(rs.getString("SID"));
		      }   
		      StringBuffer sb = new StringBuffer();
		      for(int i = 0; i < sid.size(); i++){
		        sb.append("(select " +"S"+ sid.get(i) + " from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) " +"S"+ sid.get(i) + " ,");
		      }
		      sb.append("(select ALL_BASE from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) ALL_BASE ,");
		      sb.append("(select PENSION_BASE from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) PENSION_BASE ,");
		      sb.append("(select PENSION_U from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) PENSION_U ,");
		      sb.append("(select PENSION_P from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) PENSION_P ,");
		      sb.append("(select MEDICAL_BASE from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) MEDICAL_BASE ,");
		      sb.append("(select MEDICAL_U from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) MEDICAL_U ,");
		      sb.append("(select MEDICAL_P from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) MEDICAL_P ,");
		      sb.append("(select FERTILITY_BASE from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) FERTILITY_BASE ,");
		      sb.append("(select FERTILITY_U from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) FERTILITY_U ,");
		      sb.append("(select UNEMPLOYMENT_BASE from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) UNEMPLOYMENT_BASE ,");
		      sb.append("(select UNEMPLOYMENT_U from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) UNEMPLOYMENT_U ,");
		      sb.append("(select UNEMPLOYMENT_P from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) UNEMPLOYMENT_P ,");
		      sb.append("(select INJURIES_BASE from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) INJURIES_BASE ,");
		      sb.append("(select INJURIES_U from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) INJURIES_U ,");
		      sb.append("(select HOUSING_BASE from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) HOUSING_BASE ,");
		      sb.append("(select HOUSING_U from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) HOUSING_U ,");
		      sb.append("(select HOUSING_P from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) HOUSING_P ,");
		      sql = " select p1.SEQ_ID USER_ID, p1.USER_NAME ,(select SEQ_ID from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) SD_ID ," + sb.substring(0, sb.length()-1)
		          + " from PERSON p1 "
		          + " join USER_PRIV u1 on u1.SEQ_ID = p1.USER_PRIV "
		          + " where  p1.SEQ_ID = " + seqId 
		          + " order by u1.PRIV_NO ";
		      //System.out.println(sql);
		      stmt = dbConn.prepareStatement(sql);
		      rs = stmt.executeQuery();      
		     
		      if(rs.next()){
		        salPerson.setSdId(rs.getInt("SD_ID"));
		        salPerson.setUserId(rs.getInt("USER_ID"));
		        salPerson.setUserName(rs.getString("USER_NAME"));
		        salPerson.setAllBase(rs.getDouble("ALL_BASE"));
		        salPerson.setPensionBase(rs.getDouble("PENSION_BASE"));
		        salPerson.setPensionU(rs.getDouble("PENSION_U"));
		        salPerson.setPensionP(rs.getDouble("PENSION_P"));
		        salPerson.setMedicalBase(rs.getDouble("MEDICAL_BASE"));
		        salPerson.setMedicalU(rs.getDouble("MEDICAL_U"));
		        salPerson.setMedicalP(rs.getDouble("MEDICAL_P"));
		        salPerson.setFertilityBase(rs.getDouble("FERTILITY_BASE"));
		        salPerson.setFertilityU(rs.getDouble("FERTILITY_U"));
		        salPerson.setUnemploymentBase(rs.getDouble("UNEMPLOYMENT_BASE"));
		        salPerson.setUnemploymentU(rs.getDouble("UNEMPLOYMENT_U"));
		        salPerson.setUnemploymentP(rs.getDouble("UNEMPLOYMENT_P"));
		        salPerson.setInjuriesBase(rs.getDouble("INJURIES_BASE"));
		        salPerson.setInjuriesU(rs.getDouble("INJURIES_U"));
		        salPerson.setHousingBase(rs.getDouble("HOUSING_BASE"));
		        salPerson.setHousingU(rs.getDouble("HOUSING_U"));
		        salPerson.setHousingP(rs.getDouble("HOUSING_P"));
		        /*if(salPerson.getSdId() == 0){
		          String sqlTemp = " select (select SEQ_ID from HR_oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID) HSD_ID ," + sb.substring(0, sb.length()-1).replace("SAL_DATA", "HR_SAL_DATA")
		                         + " from PERSON p1 "
		                         + " join USER_PRIV u1 on u1.SEQ_ID = p1.USER_PRIV "
		                         + " where p1.SEQ_ID = " + seqId 
		                         + " order by u1.PRIV_NO ";
		          stmt = dbConn.prepareStatement(sqlTemp);
		          rsTemp = stmt.executeQuery();   
		          if(rsTemp.next()){
		            salPerson.setHsdId(rsTemp.getInt("HSD_ID"));
		          }
		        }*/
		        Map<String,Double> smap = new HashMap<String,Double>();
		        List<String> slist = new ArrayList<String>();
		        for(int i = 0; i < sid.size(); i++){
		          smap.put("S"+sid.get(i), rs.getDouble("S"+sid.get(i)));
		          slist.add(i, (String)"S"+sid.get(i));
		        }
		        salPerson.setSmap(smap);
		        salPerson.setSlist(slist);
		      }
		    } catch (Exception e) {
		      throw e;
		    } finally {
		      YHDBUtility.close(stmt, rs, null);
		    }
		    return salPerson;
		  }
  /**
   * 员工薪酬基数批量设置
   * @param dbConn
   * @param user
   * @param seqId
   * @return
   * @throws Exception
   */
  public List<YHSalPerson> setPLPersonNameLogic(Connection dbConn, YHPerson user) throws Exception {
	  List<YHSalPerson> salPersons = new ArrayList<YHSalPerson>();
	    String sql = " select SLAITEM_ID SID from oa_sal_item "
	    	       + " where ISCOMPUTER !=1 "
	               + " order by SLAITEM_ID ";
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    ResultSet rsTemp = null;
	    List<String> sid = new ArrayList<String>();
	    try{
	      stmt = dbConn.prepareStatement(sql);
	      rs = stmt.executeQuery();
	      while(rs.next()){
	        sid.add(rs.getString("SID"));
	      }   
	      StringBuffer sb = new StringBuffer();
	      for(int i = 0; i < sid.size(); i++){
	        sb.append("(select " + "S"+sid.get(i) + " from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) " +"S"+sid.get(i) + " ,");
	      }
	      String str = "";
	      if(sid.size() != 0){
	        str = "," + sb.substring(0, sb.length()-1);
	      }
	     // sql = " select p1.SEQ_ID USER_ID, p1.USER_NAME ,(select SEQ_ID from HR_oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID) SD_ID ," + sb.substring(0, sb.length()-1)
	      sql = " select p1.SEQ_ID USER_ID, p1.USER_NAME ,(select SEQ_ID from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) SD_ID " + str
              + " from PERSON p1 "
	          + " join USER_PRIV u1 on u1.SEQ_ID = p1.USER_PRIV "
	          + " order by u1.PRIV_NO ";
	      stmt = dbConn.prepareStatement(sql);
	      rs = stmt.executeQuery();      
	     
	      while(rs.next()){
	        YHSalPerson salPerson = new YHSalPerson();
	        salPerson.setSdId(rs.getInt("SD_ID"));
	        salPerson.setUserId(rs.getInt("USER_ID"));
	        salPerson.setUserName(rs.getString("USER_NAME"));

	        Map<String,Double> smap = new HashMap<String,Double>();
	        List<String> slist = new ArrayList<String>();
	        for(int i = 0; i < sid.size(); i++){
	          smap.put("S"+sid.get(i), rs.getDouble("S"+sid.get(i)));
	          slist.add(i, (String)"S"+sid.get(i));
	        }
	        salPerson.setSmap(smap);
	        salPerson.setSlist(slist);
	        salPersons.add(salPerson);
	      }
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      YHDBUtility.close(stmt, rs, null);
	    }
	    return salPersons;
	  }
  
  public void setPLPersonSubmitLogic(Connection dbConn,
		  Map<String,String> request, YHPerson person) throws Exception {
	  PreparedStatement ps = null;
	  PreparedStatement ps1 = null;
	  ResultSet rs = null;
	  String userIdStr="";
	  String allBase = YHUtility.isNullorEmpty(request.get("allBase"))? "0.00" :request.get("allBase");
      String pensionBase = YHUtility.isNullorEmpty(request.get("pensionBase"))? "0.00" :request.get("pensionBase");
      String pensionU = YHUtility.isNullorEmpty(request.get("pensionU"))? "0.00" :request.get("pensionU");
      String pensionP = YHUtility.isNullorEmpty(request.get("pensionP")) ? "0.00" : request.get("pensionP");
      String medicalBase = YHUtility.isNullorEmpty(request.get("medicalBase"))? "0.00" :request.get("medicalBase");
      String medicalU = YHUtility.isNullorEmpty(request.get("medicalU"))? "0.00" :request.get("medicalU");
      String medicalP = YHUtility.isNullorEmpty(request.get("medicalP"))? "0.00" :request.get("medicalP");
      String fertilityBase = YHUtility.isNullorEmpty(request.get("fertilityBase"))? "0.00" :request.get("fertilityBase");
      String fertilityU = YHUtility.isNullorEmpty(request.get("fertilityU"))? "0.00" :request.get("fertilityU");
      String unemploymentBase = YHUtility.isNullorEmpty(request.get("unemploymentBase"))? "0.00" :request.get("unemploymentBase");
      String unemploymentU = YHUtility.isNullorEmpty(request.get("unemploymentU"))? "0.00" :request.get("unemploymentU");
      String unemploymentP = YHUtility.isNullorEmpty(request.get("unemploymentP"))? "0.00" :request.get("unemploymentP");
      String injuriesBase = YHUtility.isNullorEmpty(request.get("injuriesBase"))? "0.00" :request.get("injuriesBase");
      String injuriesU = YHUtility.isNullorEmpty(request.get("injuriesU"))? "0.00" :request.get("injuriesU");
      String housingBase = YHUtility.isNullorEmpty(request.get("housingBase"))? "0.00" :request.get("housingBase");
      String housingU = YHUtility.isNullorEmpty(request.get("housingU"))? "0.00" :request.get("housingU");
      String housingP = YHUtility.isNullorEmpty(request.get("housingP"))? "0.00" :request.get("housingP");
	  
	  
	  String deptS = request.get("dept"); // 库Id
	  String userS = request.get("user1");// 人员
	  String roleS = request.get("role"); //按角色设置	  String titleStr="";
	  titleStr = request.get("title");//薪酬设置项目名称
	  String titleList[] = new String[0];
	  if(!YHUtility.isNullorEmpty(titleStr)){
	     titleStr = titleStr.substring(0, titleStr.length()-1);
	     titleList = titleStr.split(",");
	  }

	  
	  List<String> Slist = new ArrayList<String>();
      for(int j = 0; j < titleList.length; j++){
        Slist.add(request.get(titleList[j]));
      }
		try {
			if(!YHUtility.isNullorEmpty(deptS)){
        if(deptS.equals("0")){
      	  userIdStr = com.findDeptPerson(dbConn, person);
        }else{
      	  userIdStr = com.findDeptPerson1(dbConn, person, deptS);
        }
			}
			if(!YHUtility.isNullorEmpty(userS)){
				userIdStr = com.idqueryPerson(dbConn, person, userS,userIdStr);
			}
			if(!YHUtility.isNullorEmpty(roleS)){
				userIdStr = com.roleQueryPerson(dbConn, person, roleS, userIdStr);
			}    
			
		if(!YHUtility.isNullorEmpty(userIdStr)){	
		   String[] userIdStrNum = userIdStr.split(",");
		    int ArrayNum =0;
		    ArrayNum = userIdStrNum.length;
		    if( userIdStrNum[ArrayNum-1]==""){
			  ArrayNum --;
		    }
		    for(int i = 0; i < ArrayNum; i++){

		    	  String sqls = " select * from oa_pm_salary_data where USER_ID="+userIdStrNum[i];
			      ps = dbConn.prepareStatement(sqls);
			      rs = ps.executeQuery();	        
		          if(rs.next()){
		          sqls = " update oa_pm_salary_data set ALL_BASE="+allBase+"" +
		          		", PENSION_BASE="+pensionBase+"" +
		          		", PENSION_U="+pensionU+"" +
		          		", PENSION_P="+pensionP+"" +
		          		", MEDICAL_BASE="+medicalBase+""+
		          	  ", MEDICAL_U="+medicalU+""+
		          		", MEDICAL_P="+medicalP+""+
		          		", FERTILITY_BASE="+fertilityBase+""+
		          	  ", FERTILITY_U="+fertilityU+""+
		          		", UNEMPLOYMENT_BASE="+unemploymentBase+""+
		          		", UNEMPLOYMENT_U="+unemploymentU+""+
		          		", UNEMPLOYMENT_P="+unemploymentP+""+
		          		", INJURIES_BASE="+injuriesBase+""+
		          		", INJURIES_U="+injuriesU+""+
		          		", HOUSING_BASE="+housingBase+""+
		          		", HOUSING_U ="+housingU+""+
		          		", HOUSING_P ="+housingP+"" ;
		          		for(int n=0; n<titleList.length;n++){
		          		 sqls += " , " + titleList[n] + " = " ;
		          		 sqls += Slist.get(n) == "" ? "0" : Slist.get(n);
		          		}
		          		sqls +=" where USER_ID="+userIdStrNum[i];
		          		//System.out.println(sqls);
		          		try{
  		          		ps1 = dbConn.prepareStatement(sqls);
  		          		ps1.executeUpdate(); 
                  }
                  catch(Exception e){
                    e.printStackTrace();
                  }
                  finally{
                    YHDBUtility.close(ps1, null, null);
                    YHDBUtility.close(ps, rs, null);
                  }
		        }else{   
		        	if("".equals(titleStr)){
		        		sqls = " insert into oa_pm_salary_data(USER_ID, ALL_BASE"
			          		+ " ,PENSION_BASE"
			          		+ " ,PENSION_U"
			          		+ ", PENSION_P"
			          		+ " ,MEDICAL_BASE"
			          		+ " ,MEDICAL_U"
			          		+ ", MEDICAL_P"
			          		+ " ,FERTILITY_BASE"
			          		+ " ,FERTILITY_U"
			          		+ ", UNEMPLOYMENT_BASE"
			          		+ " ,UNEMPLOYMENT_U"
			          		+ " ,UNEMPLOYMENT_P"
			          		+ ", INJURIES_BASE"
			          		+ " ,INJURIES_U"
			          		+ " ,HOUSING_BASE"
			          		+ ", HOUSING_U"
			          		+ ", HOUSING_P" 
			          		+") values("+userIdStrNum[i]+","+allBase+","+pensionBase+","+pensionU+","+pensionP+","+medicalBase+" " +
			          				" ,"+medicalU+","+medicalP+","+fertilityBase+","+fertilityU+","+unemploymentBase+","+unemploymentU+" " +
			          				" ,"+unemploymentP+","+injuriesBase+","+injuriesU+","+housingBase+","+housingU+","+housingP+")";
		        	}
		        	else{
		        		sqls = " insert into oa_pm_salary_data(USER_ID, ALL_BASE"
		        			+ " ,PENSION_BASE"
		        			+ " ,PENSION_U"
		        			+ ", PENSION_P"
		        			+ " ,MEDICAL_BASE"
		        			+ " ,MEDICAL_U"
		        			+ ", MEDICAL_P"
		        			+ " ,FERTILITY_BASE"
		        			+ " ,FERTILITY_U"
		        			+ ", UNEMPLOYMENT_BASE"
		        			+ " ,UNEMPLOYMENT_U"
		        			+ " ,UNEMPLOYMENT_P"
		        			+ ", INJURIES_BASE"
		        			+ " ,INJURIES_U"
		        			+ " ,HOUSING_BASE"
		        			+ ", HOUSING_U"
		        			+ ", HOUSING_P" 
		        			+ ", "+titleStr+") values("+userIdStrNum[i]+","+allBase+","+pensionBase+","+pensionU+","+pensionP+","+medicalBase+" " +
		        			" ,"+medicalU+","+medicalP+","+fertilityBase+","+fertilityU+","+unemploymentBase+","+unemploymentU+" " +
		        			" ,"+unemploymentP+","+injuriesBase+","+injuriesU+","+housingBase+","+housingU+","+housingP+","+Slist.toString().substring(1, Slist.toString().length()-1)+" )";
		        	}
		           try{
		             ps1 = dbConn.prepareStatement(sqls);
		             ps1.executeUpdate();
		           }
		           catch(Exception e){
		             e.printStackTrace();
		           }
		           finally{
		             YHDBUtility.close(ps1, null, null);
		             YHDBUtility.close(ps, rs, null);
		           }
		        }    
		      }
		}
		} catch (Exception e) {
			throw e;
		} finally {
		  YHDBUtility.close(ps, rs, null);
		}
	}
      
}

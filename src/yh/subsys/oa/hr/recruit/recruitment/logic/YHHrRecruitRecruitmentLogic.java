package yh.subsys.oa.hr.recruit.recruitment.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHUserPriv;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.recruit.filter.data.YHHrRecruitFilter;
import yh.subsys.oa.hr.recruit.hrPool.logic.YHHrRecruitPoolLogic;
import yh.subsys.oa.hr.recruit.recruitment.data.YHHrRecruitRecruitment;



public class YHHrRecruitRecruitmentLogic {
	private static Logger log = Logger.getLogger(YHHrRecruitRecruitmentLogic.class);
	
public String getHrRecruitJsonLogic(Connection dbConn, Map request, YHPerson person) throws Exception{
		
	YHHrRecruitPoolLogic poolLogic = new YHHrRecruitPoolLogic();
		try {
			String sql =  "select " +
   		"SEQ_ID" +
   		",PLAN_NO" +
   		",JOB_STATUS" +
   		",APPLYER_NAME" +
   		",ASSESSING_OFFICER" +
   		",ASS_PASS_TIME" +
   		
   		",OA_NAME" +
   		",EXPERT_ID" +
       " from " +
   		"oa_pm_enroll where " + poolLogic.getHrPriv(dbConn, person, "") + " order by SEQ_ID desc"; 
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 新建招聘录用信息 
	 * @param dbConn
	 * @param person
	 * @param map
	 * @throws Exception
	 */
	public int addHrRecruitInfoLogic(Connection dbConn,YHPerson person,Map map) throws Exception{
		String planName =(String) map.get("planName");
		String planNo =(String) map.get("planNo");
		String applyerName =(String) map.get("applyerName");
		String expertIdStr =(String) map.get("expertId");
		String jobStatus =(String) map.get("jobStatus");
		String oaName =(String) map.get("oaName");
		String assessingOfficer =(String) map.get("assessingOfficer");
		String assPassTime =(String) map.get("assPassTime");
		String department =(String) map.get("department");
		String type =(String) map.get("type");
		String administrationLevel =(String) map.get("administrationLevel");
		String jobPosition =(String) map.get("jobPosition");
		String presentPosition =(String) map.get("presentPosition");
		String onBoardingTime =(String) map.get("onBoardingTime");
		String startingSalaryTime =(String) map.get("startingSalaryTime");
		String remark =(String) map.get("remark");
		
		int expertId = 0;
		if (YHUtility.isNumber(expertIdStr)) {
			expertId = Integer.parseInt(expertIdStr);
		}
		int count = 0;
		YHORM orm = new YHORM();
		try {
//			Map filters = new HashMap();
//			filters.put("OA_NAME", YHUtility.null2Empty(oaName));
//			YHHrRecruitRecruitment recruitment = (YHHrRecruitRecruitment) orm.loadObjSingle(dbConn, YHHrRecruitRecruitment.class, filters);
//			if (recruitment!=null) {
//				recruitment.setPlanNo(planNo);
//				recruitment.setApplyerName(applyerName);
//				recruitment.setJobStatus(jobStatus);
//				recruitment.setAssessingOfficer(assessingOfficer);
//				if(!YHUtility.isNullorEmpty(assPassTime)){
//					recruitment.setAssPassTime(YHUtility.parseTimeStamp(assPassTime));
//				}
//				
//				recruitment.setDepartment(department);
//				recruitment.setType(type);
//				recruitment.setAdministrationLevel(administrationLevel);
//				recruitment.setJobPosition(jobPosition);
//				recruitment.setPresentPosition(presentPosition);
//				if (!YHUtility.isNullorEmpty(onBoardingTime)) {
//					recruitment.setOnBoardingTime(YHUtility.parseTimeStamp(onBoardingTime));
//				}
//				if (!YHUtility.isNullorEmpty(startingSalaryTime)) {
//					recruitment.setStartingSalaryTime(YHUtility.parseTimeStamp(startingSalaryTime));
//				}
//				recruitment.setRemark(remark);
//				orm.updateSingle(dbConn, recruitment);
//			}else {
//				recruitment = new YHHrRecruitRecruitment();
//				recruitment.setCreateUserId(String.valueOf(person.getSeqId()));
//				recruitment.setCreateDeptId(person.getDeptId());
//				recruitment.setPlanNo(planNo);
//				recruitment.setAssessingOfficer(assessingOfficer);
//				if (!YHUtility.isNullorEmpty(assPassTime)) {
//					recruitment.setAssPassTime(YHUtility.parseTimeStamp(assPassTime));
//				}
//				recruitment.setDepartment(department);
//				recruitment.setType(type);
//				recruitment.setAdministrationLevel(administrationLevel);
//				recruitment.setJobPosition(jobPosition);
//				recruitment.setPresentPosition(presentPosition);
//				if (!YHUtility.isNullorEmpty(onBoardingTime)) {
//					recruitment.setOnBoardingTime(YHUtility.parseTimeStamp(onBoardingTime));
//				}
//				if (!YHUtility.isNullorEmpty(startingSalaryTime)) {
//					recruitment.setStartingSalaryTime(YHUtility.parseTimeStamp(startingSalaryTime));
//				}
//				recruitment.setRemark(remark);
//				recruitment.setJobStatus(jobStatus);
//				recruitment.setApplyerName(applyerName);
//				recruitment.setOaName(oaName);
//				recruitment.setExpertId(expertId);
//				orm.saveSingle(dbConn, recruitment);
//			}
			
			YHHrRecruitRecruitment recruitment = new YHHrRecruitRecruitment();
			recruitment.setCreateUserId(String.valueOf(person.getSeqId()));
			recruitment.setCreateDeptId(person.getDeptId());
			recruitment.setPlanNo(planNo);
			recruitment.setAssessingOfficer(assessingOfficer);
			if (!YHUtility.isNullorEmpty(assPassTime)) {
				recruitment.setAssPassTime(YHUtility.parseTimeStamp(assPassTime));
			}
			recruitment.setDepartment(department);
			recruitment.setType(type);
			recruitment.setAdministrationLevel(administrationLevel);
			recruitment.setJobPosition(jobPosition);
			recruitment.setPresentPosition(presentPosition);
			if (!YHUtility.isNullorEmpty(onBoardingTime)) {
				recruitment.setOnBoardingTime(YHUtility.parseTimeStamp(onBoardingTime));
			}
			if (!YHUtility.isNullorEmpty(startingSalaryTime)) {
				recruitment.setStartingSalaryTime(YHUtility.parseTimeStamp(startingSalaryTime));
			}
			recruitment.setRemark(remark);
			recruitment.setJobStatus(jobStatus);
			recruitment.setApplyerName(applyerName);
			recruitment.setOaName(oaName);
			recruitment.setExpertId(expertId);
			orm.saveSingle(dbConn, recruitment);
			count = this.getHrStaffInfoCount(dbConn, oaName);
			
		} catch (Exception e) {
			throw e;
		}
		return count;
	}
	
	
	/**
	 * 编辑招聘录用信息
	 * @param dbConn
	 * @param person
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void updateHrRecruitInfoLogic(Connection dbConn,YHPerson person,Map map) throws Exception{
		String seqIdStr =(String) map.get("seqId");
		String planNo =(String) map.get("planNo");
		String applyerName =(String) map.get("applyerName");
		String jobStatus =(String) map.get("jobStatus");
		String oaName =(String) map.get("oaName");
		String assessingOfficer =(String) map.get("assessingOfficer");
		String assPassTime =(String) map.get("assPassTime");
		String department =(String) map.get("department");
		String type =(String) map.get("type");
		String administrationLevel =(String) map.get("administrationLevel");
		String jobPosition =(String) map.get("jobPosition");
		String presentPosition =(String) map.get("presentPosition");
		String onBoardingTime =(String) map.get("onBoardingTime");
		String startingSalaryTime =(String) map.get("startingSalaryTime");
		String remark =(String) map.get("remark");
		
		int seqId = 0;
		if (YHUtility.isNumber(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		YHORM orm = new YHORM();
		try {
			YHHrRecruitRecruitment recruitment = (YHHrRecruitRecruitment) orm.loadObjSingle(dbConn, YHHrRecruitRecruitment.class, seqId);
			if (recruitment!=null) {
				recruitment.setPlanNo(planNo);
				recruitment.setApplyerName(applyerName);
				recruitment.setJobStatus(jobStatus);
				recruitment.setAssessingOfficer(assessingOfficer);
				if(!YHUtility.isNullorEmpty(assPassTime)){
					recruitment.setAssPassTime(YHUtility.parseTimeStamp(assPassTime));
				}
				
				recruitment.setDepartment(department);
				recruitment.setType(type);
				recruitment.setAdministrationLevel(administrationLevel);
				recruitment.setJobPosition(jobPosition);
				recruitment.setPresentPosition(presentPosition);
				if (!YHUtility.isNullorEmpty(onBoardingTime)) {
					recruitment.setOnBoardingTime(YHUtility.parseTimeStamp(onBoardingTime));
				}
				if (!YHUtility.isNullorEmpty(startingSalaryTime)) {
					recruitment.setStartingSalaryTime(YHUtility.parseTimeStamp(startingSalaryTime));
				}
				recruitment.setRemark(remark);
				recruitment.setOaName(oaName);
				orm.updateSingle(dbConn, recruitment);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public int getHrStaffInfoCount(Connection dbConn,String userId) throws Exception{
		int count = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT count(SEQ_ID) from oa_pm_employee_info WHERE USER_ID =?";
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, YHUtility.null2Empty(userId));
			rs = stmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(stmt, rs, log);
		}
		return count;
	}
	/**
	 * 取得人员名称
	 * 
	 * @param conn
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String getUserNameLogic(Connection dbConn, String seqIdStr) throws Exception {
		String result = "";
		if (YHUtility.isNullorEmpty(seqIdStr)) {
			return result;
		}
		if (seqIdStr.endsWith(",")) {
			seqIdStr = seqIdStr.substring(0, seqIdStr.length() - 1);
		}
		String sql = " select USER_NAME from PERSON where SEQ_ID in(" + seqIdStr + ")";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				String toId = rs.getString(1);
				if (toId != null) {
					result += toId + ",";
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, log);
		}
		if (result.endsWith(",")) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}
	
	public void deleteHrRecruitLogic(Connection dbConn,String seqIdStr) throws Exception{
		if (YHUtility.isNullorEmpty(seqIdStr)) {
			seqIdStr = "0";
		}
		if (seqIdStr.endsWith(",")) {
			seqIdStr = seqIdStr.substring(0, seqIdStr.length()-1);
		}
		PreparedStatement stmt = null;
		String sql = "delete from oa_pm_enroll where SEQ_ID in (" +seqIdStr + ")";		
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.executeUpdate();
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(stmt, null, log);
		}
	}
	
	/**
   * 招聘录用查询列表
   * @param dbConn
   * @param request
   * @param person
   * @param map
   * @return
   * @throws Exception
   */
  public String queryRecruitmentListJsonLogic(Connection dbConn, Map request, YHPerson person,Map map) throws Exception{
    String PLAN_NO = (String) map.get("PLAN_NO");
    String APPLYER_NAME = (String) map.get("APPLYER_NAME");
    String OA_NAME = (String) map.get("OA_NAME");
    String JOB_STATUS = (String) map.get("JOB_STATUS");
    String ASSESSING_OFFICER = (String) map.get("ASSESSING_OFFICER");//
    String ASS_PASS_TIME_START = (String) map.get("ASS_PASS_TIME_START");
    String ASS_PASS_TIME_END = (String) map.get("ASS_PASS_TIME_END");//
    String department = (String) map.get("department");
    String TYPE = (String) map.get("TYPE");
    String ADMINISTRATION_LEVEL = (String) map.get("ADMINISTRATION_LEVEL");
    
    String JOB_POSITION = (String) map.get("JOB_POSITION");
    String PRESENT_POSITION = (String) map.get("PRESENT_POSITION");
    String REMARK = (String) map.get("REMARK");
   
    String conditionStr = "";
    try {
      if (!YHUtility.isNullorEmpty(PLAN_NO)) {
        conditionStr += " and PLAN_NO ='" + YHDBUtility.escapeLike(PLAN_NO) + "'";
      }
      if (!YHUtility.isNullorEmpty(APPLYER_NAME)) {
        conditionStr += " and APPLYER_NAME ='" + YHDBUtility.escapeLike(APPLYER_NAME) + "'";
      }
      if (!YHUtility.isNullorEmpty(OA_NAME)) {
        conditionStr += " and OA_NAME ='" + YHDBUtility.escapeLike(OA_NAME) + "'";
      }
      if (!YHUtility.isNullorEmpty(JOB_STATUS)) {
        conditionStr += " and JOB_STATUS ='" + YHDBUtility.escapeLike(JOB_STATUS) + "'";
      }
      
      if (!YHUtility.isNullorEmpty(ASSESSING_OFFICER)) {
        conditionStr += " and " + YHDBUtility.findInSet(YHDBUtility.escapeLike(ASSESSING_OFFICER), "ASSESSING_OFFICER") ;
      }
      if (!YHUtility.isNullorEmpty(ASS_PASS_TIME_START)) {
        conditionStr += " and " + YHDBUtility.getDateFilter("ASS_PASS_TIME", ASS_PASS_TIME_START, ">=") ;
      }
      if (!YHUtility.isNullorEmpty(ASS_PASS_TIME_END)) {
        conditionStr += " and "+YHDBUtility.getDateFilter("ASS_PASS_TIME", ASS_PASS_TIME_END, "<=");
      }
      if (!YHUtility.isNullorEmpty(department)) {
        conditionStr += " and "+YHDBUtility.findInSet(YHDBUtility.escapeLike(department), "DEPARTMENT");
      }
    
      if (!YHUtility.isNullorEmpty(TYPE)) {
        conditionStr += " and "+YHDBUtility.findInSet(TYPE,"TYPE");
      }
      if (!YHUtility.isNullorEmpty(ADMINISTRATION_LEVEL)) {
        conditionStr += " and " + YHDBUtility.findInSet(YHDBUtility.escapeLike(ADMINISTRATION_LEVEL), "ADMINISTRATION_LEVEL");
      }
      if (!YHUtility.isNullorEmpty(JOB_POSITION)) {
        conditionStr += " and " + YHDBUtility.findInSet(YHDBUtility.escapeLike(JOB_POSITION), "JOB_POSITION");
      }
      if (!YHUtility.isNullorEmpty(PRESENT_POSITION)) {
        conditionStr += " and "+YHDBUtility.findInSet(YHDBUtility.escapeLike(PRESENT_POSITION), "PRESENT_POSITION");
      }
      if (!YHUtility.isNullorEmpty(REMARK)) {
        conditionStr += " and REMARK like '%" + YHDBUtility.escapeLike(REMARK) + "%'" + YHDBUtility.escapeLike();
      }
      
      String sql =  "select " +
      "SEQ_ID" +
      ",PLAN_NO" +
      ",JOB_STATUS" +
      ",APPLYER_NAME" +
      ",ASSESSING_OFFICER" +
      ",ASS_PASS_TIME " +

      "from oa_pm_enroll where 1=1 " +
      conditionStr +
      " order by ASS_PASS_TIME desc"; 
   
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
	 * 判断名称是否已存在（Staff_Info表）
	 * 
	 * @param dbConn
	 * @param userIdStr
	 * @return
	 * @throws Exception
	 */
	public String checkUserLogic(Connection dbConn, String userIdStr) throws Exception {
		String sql = "SELECT count(SEQ_ID) from oa_pm_employee_info where USER_ID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int counter = 0;
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, userIdStr);
			rs = stmt.executeQuery();
			if (rs.next()) {
				counter = rs.getInt(1);
			}
			int isHave = 0;
			if (counter > 0) {
				isHave = 1;
			}
			String data = "{isHave:\"" + isHave + "\"}";
			return data;
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
	}
	
	/**
	 * 获取角色名称
	 * 
	 * @param dbConn
	 * @return
	 * @throws Exception
	 */
	public String getUserPrivLogic(Connection dbConn) throws Exception {
		YHORM orm = new YHORM();
		StringBuffer buffer = new StringBuffer();
		try {
			boolean isHave = false;
			List<YHUserPriv> userPrivs = (List<YHUserPriv>) orm.loadListSingle(dbConn, YHUserPriv.class, new HashMap());
			if (userPrivs != null && userPrivs.size() > 0) {
				buffer.append("[");
				for (YHUserPriv userPriv : userPrivs) {
					String privName = YHUtility.null2Empty(userPriv.getPrivName());
					buffer.append("{");
					buffer.append("seqId:\"" + userPriv.getSeqId() + "\"");
					buffer.append(",privName:\"" + YHUtility.encodeSpecial(privName) + "\"");
					buffer.append("},");
					isHave = true;
				}
				if (isHave) {
					buffer.deleteCharAt(buffer.length() - 1);
				}
				buffer.append("]");
			} else {
				buffer.append("[]");
			}
		} catch (Exception e) {
			throw e;
		}
		return buffer.toString();
	}	
	public String getHrSetUserLogin(Connection conn) throws Exception {
		String result = "";
		String sql = " select PARA_VALUE from SYS_PARA where PARA_NAME = 'HR_SET_USER_LOGIN'";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				String toId = rs.getString(1);
				if (toId != null) {
					result = toId;
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, log);
		}
		return result;
	}
	
	/**
	 * 招聘筛选-模糊查找
	 * @param dbConn
	 * @param person
	 * @param keyWord
	 * @return
	 * @throws Exception
	 */
	public List<YHHrRecruitFilter> getHrFilterListLogic(Connection dbConn,YHPerson person,String keyWord) throws Exception{
		List<YHHrRecruitFilter> list = new ArrayList<YHHrRecruitFilter>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		YHHrRecruitPoolLogic poolLogic = new YHHrRecruitPoolLogic();
		try {
			String sql = "SELECT SEQ_ID,EMPLOYEE_NAME from oa_pm_enroll_set where ( SEQ_ID like '%"  + YHDBUtility.escapeLike(keyWord) + "%' or EMPLOYEE_NAME like '%" 
						+ YHDBUtility.escapeLike(keyWord) + "%' ) and"
						+ poolLogic.getHrPriv(dbConn, person, "") 
						+ " and WHETHER_BY_SCREENING = '1'";
			sql += " order by SEQ_ID";
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			int counter = 0;
			while(rs.next() && ++counter<50){
				
				int dbSeqId = rs.getInt("SEQ_ID");
				int count = this.getHrRecruitmentCount(dbConn, dbSeqId);
				if (count!=0) {
					continue;
				}
				YHHrRecruitFilter filter = new YHHrRecruitFilter();
				filter.setSeqId(dbSeqId);
				filter.setEmployeeName(rs.getString("EMPLOYEE_NAME"));
				list.add(filter);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(stmt, rs, log);
		}
		return list;
	}
	public int getHrRecruitmentCount(Connection dbConn,int expertId) throws Exception{
		int count = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT count(SEQ_ID) from oa_pm_enroll WHERE EXPERT_ID =?";
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setInt(1, expertId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(stmt, rs, log);
		}
		return count;
	}
	
	
	
}

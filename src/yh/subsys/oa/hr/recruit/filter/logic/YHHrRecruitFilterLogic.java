package yh.subsys.oa.hr.recruit.filter.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.recruit.filter.data.YHHrRecruitFilter;
import yh.subsys.oa.hr.recruit.hrPool.data.YHHrRecruitPool;
import yh.subsys.oa.hr.recruit.hrPool.logic.YHHrRecruitPoolLogic;

public class YHHrRecruitFilterLogic {
	private static Logger log = Logger.getLogger(YHHrRecruitFilterLogic.class);
	
	public String getHrFilterListLogic(Connection dbConn, Map request, YHPerson person) throws Exception{
		try {
			String conditionStr = " TRANSACTOR_STEP='" + person.getSeqId()
					+ "' or NEXT_TRANSA_STEP='" + person.getSeqId() 
					+ "' or NEXT_TRANSA_STEP1='" + person.getSeqId() 
					+ "' or NEXT_TRANSA_STEP2='" + person.getSeqId() 
					+ "' or NEXT_TRANSA_STEP3='" + person.getSeqId() + "'" ;
			String sql =  "select " +
   		"SEQ_ID" +
   		",EMPLOYEE_NAME" +
   		",POSITION" +
   		",EMPLOYEE_MAJOR" +
   		",EMPLOYEE_PHONE" +
   		",TRANSACTOR_STEP" +
   		",END_FLAG" +
   		",STEP_FLAG,NEXT_TRANSA_STEP,NEXT_TRANSA_STEP1,NEXT_TRANSA_STEP2,NEXT_TRANSA_STEP3 " +
      " from " +
   		" oa_pm_enroll_filter where " +conditionStr + " order by NEW_TIME desc"; 
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<YHHrRecruitFilter> getHrFilterListLogic(Connection dbConn,YHPerson person,String keyWord) throws Exception{
		List<YHHrRecruitFilter> list = new ArrayList<YHHrRecruitFilter>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT SEQ_ID,EMPLOYEE_NAME from oa_pm_enroll_set  where ( SEQ_ID like '%"
			+ YHDBUtility.escapeLike(YHUtility.null2Empty(keyWord)) + "%' or EMPLOYEE_NAME like '%" 
			+ YHDBUtility.escapeLike(YHUtility.null2Empty(keyWord)) + "%' ) and WHETHER_BY_SCREENING = '0' "
			+ " order by SEQ_ID";
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			int counter = 0;
			while(rs.next() && ++counter<50){
				YHHrRecruitFilter filter = new YHHrRecruitFilter();
				filter.setSeqId(rs.getInt("SEQ_ID"));
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
	public void addHrFilterInfoLogic(Connection dbConn,YHPerson person,Map map) throws Exception{
		
		String expertIdStr =(String) map.get("expertId");
		String employeeName =(String) map.get("employeeName");
		String planNo =(String) map.get("planNo");
		String position =(String) map.get("position");
		String employeeMajor =(String) map.get("employeeMajor");
		String employeePhone =(String) map.get("employeePhone");
		String transactorStep =(String) map.get("transactorStep");
		String nextTransaStep =(String) map.get("nextTransaStep");
		String nextDateTime =(String) map.get("nextDateTime");
		String smsRemind =(String) map.get("smsRemind");
		String sms2Remind =(String) map.get("sms2Remind");
		
		int expertId = 0;
		if (YHUtility.isNumber(expertIdStr)) {
			expertId = Integer.parseInt(expertIdStr);
		}
		
		YHORM orm = new YHORM();
		try {
			YHHrRecruitFilter filter = new YHHrRecruitFilter();
			filter.setCreateUserId(String.valueOf(person.getSeqId()));
			filter.setCreateDeptId(person.getDeptId());
			filter.setExpertId(expertId);
			filter.setEmployeeName(employeeName);
			filter.setPlanNo(planNo);
			filter.setPosition(position);
			filter.setEmployeeMajor(employeeMajor);
			filter.setEmployeePhone(employeePhone);
			filter.setTransactorStep(transactorStep);
			filter.setNextTransaStep(nextTransaStep);
			if (!YHUtility.isNullorEmpty(nextDateTime)) {
				filter.setNextDateTime(YHUtility.parseTimeStamp(nextDateTime));
			}
			filter.setStepFlag(1);
			filter.setNewTime(YHUtility.parseTimeStamp());
			orm.saveSingle(dbConn, filter);
			
			String smsContent = person.getUserName() + " 向您提交招聘筛选，请办理！";
			String remindUrl = "/subsys/oa/hr/recruit/filter/hrFilterManage.jsp";
			if ("1".equals(smsRemind) && !YHUtility.isNullorEmpty(nextTransaStep)) {
				this.doSmsBackTime(dbConn, smsContent, person.getSeqId(), nextTransaStep, "65", remindUrl, new Date());
			}
			if ("1".equals(sms2Remind) && !YHUtility.isNullorEmpty(nextTransaStep)) {
				YHMobileSms2Logic sbl = new YHMobileSms2Logic();
				sbl.remindByMobileSms(dbConn, nextTransaStep , person.getSeqId() , smsContent , new Date());
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
  /**
   * 短信提醒(带时间)
   * @param conn
   * @param content
   * @param fromId
   * @param toId
   * @param type
   * @param remindUrl
   * @param sendDate
   * @throws Exception
   */
	public void doSmsBackTime(Connection dbConn, String content, int fromId, String toId, String type, String remindUrl, Date sendDate)throws Exception {
		try {
			YHSmsBack sb = new YHSmsBack();
			sb.setContent(content);
			sb.setFromId(fromId);
			sb.setToId(toId);
			sb.setSmsType(type);
			sb.setRemindUrl(remindUrl);
			sb.setSendDate(sendDate);
			YHSmsUtil.smsBack(dbConn, sb);
		} catch (Exception e) {
			throw e;
		}
	}
	public void deleteHrFilterLogic(Connection dbConn,String seqIdStr) throws Exception{
		if (YHUtility.isNullorEmpty(seqIdStr)) {
			seqIdStr = "0";
		}
		if (seqIdStr.endsWith(",")) {
			seqIdStr = seqIdStr.substring(0, seqIdStr.length()-1);
		}
		PreparedStatement stmt = null;
		String sql = "delete from oa_pm_enroll_filter where SEQ_ID in (" +seqIdStr + ")";		
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
	/**
	 * 招聘计划名称 
	 * 
	 * @param conn
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String getPlanNameByPlanNoLogic(Connection dbConn, String planNoStr) throws Exception {
		String result = "";
		if (YHUtility.isNullorEmpty(planNoStr)) {
			planNoStr = "";
		}
		String sql = "SELECT PLAN_NAME from oa_pm_enroll_plan where PLAN_NO=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = dbConn.prepareStatement(sql);
			ps.setString(1, planNoStr);
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
	 * 编辑筛选信息
	 * @param dbConn
	 * @param person
	 * @param map
	 * @throws Exception
	 */
	public void updateHrFilterInfoLogic(Connection dbConn,YHPerson person,Map map) throws Exception{
		
		String seqIdStr =(String) map.get("seqId");
		String expertIdStr =(String) map.get("expertId");
		String employeeName =(String) map.get("employeeName");
		String planNo =(String) map.get("planNo");
		String position =(String) map.get("position");
		String employeeMajor =(String) map.get("employeeMajor");
		String employeePhone =(String) map.get("employeePhone");
		String transactorStep =(String) map.get("transactorStep");
		String nextTransaStep =(String) map.get("nextTransaStep");
		String nextDateTime =(String) map.get("nextDateTime");
		String smsRemind =(String) map.get("smsRemind");
		String sms2Remind =(String) map.get("sms2Remind");
		
		int expertId = 0;
		int seqId = 0;
		if (YHUtility.isNumber(expertIdStr)) {
			expertId = Integer.parseInt(expertIdStr);
		}
		if (YHUtility.isNumber(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		
		YHORM orm = new YHORM();
		try {
			YHHrRecruitFilter filter = (YHHrRecruitFilter) orm.loadObjSingle(dbConn, YHHrRecruitFilter.class, seqId);
			if (filter!=null) {
				filter.setExpertId(expertId);
				filter.setEmployeeName(employeeName);
				filter.setPlanNo(planNo);
				filter.setPosition(position);
				filter.setEmployeeMajor(employeeMajor);
				filter.setEmployeePhone(employeePhone);
				filter.setNextTransaStep(nextTransaStep);
				
//				filter.setCreateUserId(String.valueOf(person.getSeqId()));
//				filter.setCreateDeptId(person.getDeptId());
//				filter.setTransactorStep(transactorStep);
//				if (!YHUtility.isNullorEmpty(nextDateTime)) {
//					filter.setNextDateTime(YHUtility.parseTimeStamp(nextDateTime));
//				}
//				filter.setStepFlag(1);
//				filter.setNewTime(YHUtility.parseTimeStamp());
				orm.updateSingle(dbConn, filter);
				
				String smsContent = person.getUserName() + " 向您提交招聘筛选，请办理！";
				String remindUrl = "";
				if ("1".equals(smsRemind) && !YHUtility.isNullorEmpty(nextTransaStep)) {
					this.doSmsBackTime(dbConn, smsContent, person.getSeqId(), nextTransaStep, "65", remindUrl, new Date());
				}
				if ("1".equals(sms2Remind) && !YHUtility.isNullorEmpty(nextTransaStep)) {
					YHMobileSms2Logic sbl = new YHMobileSms2Logic();
					sbl.remindByMobileSms(dbConn, nextTransaStep , person.getSeqId() , smsContent , new Date());
				}
			}
			
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 处理筛选信息1
	 * @param dbConn
	 * @param person
	 * @param map
	 * @throws Exception
	 */
	public void dealWithFilter1Logic(Connection dbConn,YHPerson person,Map map) throws Exception{
		String seqIdStr =(String) map.get("seqId");
		String stepFlag =(String) map.get("stepFlag");
		String passOrNot1 =(String) map.get("passOrNot1");
		String filterMethod1 =(String) map.get("filterMethod1");
		String filterDateTime1 =(String) map.get("filterDateTime1");
		String firstContent1 =(String) map.get("firstContent1");
		String firstView1 =(String) map.get("firstView1");
		String transactorStep1 =(String) map.get("transactorStep1");
		String nextTransaStep1 =(String) map.get("nextTransaStep1");
		String nextDateTime1 =(String) map.get("nextDateTime1");
		String smsRemind =(String) map.get("smsRemind");
		String sms2Remind =(String) map.get("sms2Remind");
		
		int seqId = 0;
		if (YHUtility.isNumber(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		YHORM orm = new YHORM();
		try {
			YHHrRecruitFilter filter = (YHHrRecruitFilter) orm.loadObjSingle(dbConn, YHHrRecruitFilter.class, seqId);
			if (filter!=null) {
				
				if ("1".equals(stepFlag)) {
					if ("1".equals(passOrNot1)) {
						filter.setFilterMethod1(filterMethod1);
						if (!YHUtility.isNullorEmpty(filterDateTime1)) {
							filter.setFilterDateTime1(YHUtility.parseTimeStamp(filterDateTime1));
						}
						filter.setFirstContent1(firstContent1);
						filter.setFirstView1(firstView1);
						filter.setTransactorStep1(transactorStep1);
						filter.setPassOrNot1(Integer.parseInt(passOrNot1));
						filter.setNextTransaStep1(nextTransaStep1);
						if (!YHUtility.isNullorEmpty(nextDateTime1)) {
							filter.setNextDateTime1(YHUtility.parseTimeStamp(nextDateTime1));
						}
						filter.setStepFlag(2);
						orm.updateSingle(dbConn, filter);
						String smsContent = person.getUserName() + " 向您提交招聘筛选，请办理！";
						String remindUrl = "";
						if ("1".equals(smsRemind) && !YHUtility.isNullorEmpty(nextTransaStep1)) {
							this.doSmsBackTime(dbConn, smsContent, person.getSeqId(), nextTransaStep1, "65", remindUrl, new Date());
						}
						if ("1".equals(sms2Remind) && !YHUtility.isNullorEmpty(nextTransaStep1)) {
							YHMobileSms2Logic sbl = new YHMobileSms2Logic();
							sbl.remindByMobileSms(dbConn, nextTransaStep1 , person.getSeqId() , smsContent , new Date());
						}
					}else {
						
						filter.setFilterMethod1(filterMethod1);
						if (!YHUtility.isNullorEmpty(filterDateTime1)) {
							filter.setFilterDateTime1(YHUtility.parseTimeStamp(filterDateTime1));
						}
						filter.setFirstContent1(firstContent1);
						filter.setFirstView1(firstView1);
						filter.setTransactorStep1(transactorStep1);
						filter.setPassOrNot1(Integer.parseInt(passOrNot1));
						filter.setNextTransaStep1(nextTransaStep1);
						if (!YHUtility.isNullorEmpty(nextDateTime1)) {
							filter.setNextDateTime1(YHUtility.parseTimeStamp(nextDateTime1));
						}
						filter.setEndFlag(1);
						filter.setStepFlag(2);
						orm.updateSingle(dbConn, filter);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 处理筛选信息2
	 * @param dbConn
	 * @param person
	 * @param map
	 * @throws Exception
	 */
	public void dealWithFilter2Logic(Connection dbConn,YHPerson person,Map map) throws Exception{
		
		String seqIdStr =(String) map.get("seqId");
		String stepFlag =(String) map.get("stepFlag");
		String passOrNot2 =(String) map.get("passOrNot2");
		String dbFilterMethod2 =(String) map.get("dbFilterMethod2");
		String dbFilterDateTime2 =(String) map.get("dbFilterDateTime2");
		
		String dbFirstContent2 =(String) map.get("dbFirstContent2");
		String dbFirstView2 =(String) map.get("dbFirstView2");
		String dbTransactorStep2 =(String) map.get("dbTransactorStep2");
		String dbNextTransaStep2 =(String) map.get("dbNextTransaStep2");
		String dbNextDateTime2 =(String) map.get("dbNextDateTime2");
		String smsRemind2 =(String) map.get("smsRemind2");
		String sms2Remind2 =(String) map.get("sms2Remind2");
		int seqId = 0;
		if (YHUtility.isNumber(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		
		YHORM orm = new YHORM();
		try {
			YHHrRecruitFilter filter = (YHHrRecruitFilter) orm.loadObjSingle(dbConn, YHHrRecruitFilter.class, seqId);
			if (filter!=null) {
				if ("2".equals(stepFlag)) {
					if ("1".equals(passOrNot2)) {
						filter.setFilterMethod2(dbFilterMethod2);
						if (!YHUtility.isNullorEmpty(dbFilterDateTime2)) {
							filter.setFilterDateTime2(YHUtility.parseTimeStamp(dbFilterDateTime2));
						}
						filter.setFirstContent2(dbFirstContent2);
						filter.setFirstView2(dbFirstView2);
						filter.setTransactorStep2(dbTransactorStep2);
						filter.setPassOrNot2(Integer.parseInt(passOrNot2));
						filter.setNextTransaStep2(dbNextTransaStep2);
						if (!YHUtility.isNullorEmpty(dbNextDateTime2)) {
							filter.setNextDateTime2(YHUtility.parseTimeStamp(dbNextDateTime2));
						}
						filter.setStepFlag(3);
						orm.updateSingle(dbConn, filter);
						String smsContent = person.getUserName() + " 向您提交招聘筛选，请办理！";
						String remindUrl = "";
						if ("1".equals(smsRemind2) && !YHUtility.isNullorEmpty(dbNextTransaStep2)) {
							this.doSmsBackTime(dbConn, smsContent, person.getSeqId(), dbNextTransaStep2, "65", remindUrl, new Date());
						}
						if ("1".equals(sms2Remind2) && !YHUtility.isNullorEmpty(dbNextTransaStep2)) {
							YHMobileSms2Logic sbl = new YHMobileSms2Logic();
							sbl.remindByMobileSms(dbConn, dbNextTransaStep2 , person.getSeqId() , smsContent , new Date());
						}
					}else {
						
						filter.setFilterMethod2(dbFilterMethod2);
						if (!YHUtility.isNullorEmpty(dbFilterDateTime2)) {
							filter.setFilterDateTime2(YHUtility.parseTimeStamp(dbFilterDateTime2));
						}
						filter.setFirstContent2(dbFirstContent2);
						filter.setFirstView2(dbFirstView2);
						filter.setTransactorStep2(dbTransactorStep2);
						filter.setPassOrNot2(Integer.parseInt(passOrNot2));
						filter.setNextTransaStep2(dbNextTransaStep2);
						if (!YHUtility.isNullorEmpty(dbNextDateTime2)) {
							filter.setNextDateTime2(YHUtility.parseTimeStamp(dbNextDateTime2));
						}
						filter.setEndFlag(1);
						filter.setStepFlag(3);
						orm.updateSingle(dbConn, filter);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 处理筛选信息3
	 * @param dbConn
	 * @param person
	 * @param map
	 * @throws Exception
	 */
	public void dealWithFilter3Logic(Connection dbConn,YHPerson person,Map map) throws Exception{
		
		String seqIdStr =(String) map.get("seqId");
		String stepFlag =(String) map.get("stepFlag");
		
		String passOrNot3 =(String) map.get("passOrNot3");
		String dbFilterMethod3 =(String) map.get("dbFilterMethod3");
		String dbFilterDateTime3 =(String) map.get("dbFilterDateTime3");		
		String dbFirstContent3 =(String) map.get("dbFirstContent3");
		String dbFirstView3 =(String) map.get("dbFirstView3");
		String dbTransactorStep3 =(String) map.get("dbTransactorStep3");
		String dbNextTransaStep3 =(String) map.get("dbNextTransaStep3");
		String dbNextDateTime3 =(String) map.get("dbNextDateTime3");
		String smsRemind3 =(String) map.get("smsRemind3");
		String sms2Remind3 =(String) map.get("sms2Remind3");
		int seqId = 0;
		if (YHUtility.isNumber(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		
		YHORM orm = new YHORM();
		try {
			YHHrRecruitFilter filter = (YHHrRecruitFilter) orm.loadObjSingle(dbConn, YHHrRecruitFilter.class, seqId);
			if (filter!=null) {
				if ("3".equals(stepFlag)) {
					if ("1".equals(passOrNot3)) {
						filter.setFilterMethod3(dbFilterMethod3);
						if (!YHUtility.isNullorEmpty(dbFilterDateTime3)) {
							filter.setFilterDateTime3(YHUtility.parseTimeStamp(dbFilterDateTime3));
						}
						filter.setFirstContent3(dbFirstContent3);
						filter.setFirstView3(dbFirstView3);
						filter.setTransactorStep3(dbTransactorStep3);
						filter.setPassOrNot3(Integer.parseInt(passOrNot3));
						filter.setNextTransaStep3(dbNextTransaStep3);
						if (!YHUtility.isNullorEmpty(dbNextDateTime3)) {
							filter.setNextDateTime3(YHUtility.parseTimeStamp(dbNextDateTime3));
						}
						filter.setStepFlag(4);
						orm.updateSingle(dbConn, filter);
						String smsContent = person.getUserName() + " 向您提交招聘筛选，请办理！";
						String remindUrl = "";
						if ("1".equals(smsRemind3) && !YHUtility.isNullorEmpty(dbNextTransaStep3)) {
							this.doSmsBackTime(dbConn, smsContent, person.getSeqId(), dbNextTransaStep3, "65", remindUrl, new Date());
						}
						if ("1".equals(sms2Remind3) && !YHUtility.isNullorEmpty(dbNextTransaStep3)) {
							YHMobileSms2Logic sbl = new YHMobileSms2Logic();
							sbl.remindByMobileSms(dbConn, dbNextTransaStep3 , person.getSeqId() , smsContent , new Date());
						}
					}else {
						
						filter.setFilterMethod3(dbFilterMethod3);
						if (!YHUtility.isNullorEmpty(dbFilterDateTime3)) {
							filter.setFilterDateTime3(YHUtility.parseTimeStamp(dbFilterDateTime3));
						}
						filter.setFirstContent3(dbFirstContent3);
						filter.setFirstView3(dbFirstView3);
						filter.setTransactorStep3(dbTransactorStep3);
						filter.setPassOrNot3(Integer.parseInt(passOrNot3));
						filter.setNextTransaStep3(dbNextTransaStep3);
						if (!YHUtility.isNullorEmpty(dbNextDateTime3)) {
							filter.setNextDateTime3(YHUtility.parseTimeStamp(dbNextDateTime3));
						}
						filter.setEndFlag(1);
						filter.setStepFlag(4);
						orm.updateSingle(dbConn, filter);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 处理筛选信息4
	 * @param dbConn
	 * @param person
	 * @param map
	 * @throws Exception
	 */
	public void dealWithFilter4Logic(Connection dbConn,YHPerson person,Map map) throws Exception{
		
		String seqIdStr =(String) map.get("seqId");
		String stepFlag =(String) map.get("stepFlag");
		String isFinish =(String) map.get("isFinish");
		
		String passOrNot4=(String) map.get("passOrNot4");
		String dbFilterMethod4 =(String) map.get("dbFilterMethod4");
		String dbFilterDateTime4 =(String) map.get("dbFilterDateTime4");		
		String dbFirstContent4 =(String) map.get("dbFirstContent4");
		String dbFirstView4 =(String) map.get("dbFirstView3");
		String dbTransactorStep4 =(String) map.get("dbTransactorStep4");
//		String dbNextTransaStep4 =(String) map.get("dbNextTransaStep4");
		String smsRemind4 =(String) map.get("smsRemind4");
		String sms2Remind4 =(String) map.get("sms2Remind4");
		int seqId = 0;
		if (YHUtility.isNumber(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		
		YHORM orm = new YHORM();
		try {
			YHHrRecruitFilter filter = (YHHrRecruitFilter) orm.loadObjSingle(dbConn, YHHrRecruitFilter.class, seqId);
			if (filter!=null) {
				String ccString ="";
				String nextTransaStep = YHUtility.null2Empty(filter.getNextTransaStep());
				if ("4".equals(stepFlag)) {
					if ("1".equals(passOrNot4)) {
						filter.setFilterMethod4(dbFilterMethod4);
						if (!YHUtility.isNullorEmpty(dbFilterDateTime4)) {
							filter.setFilterDateTime4(YHUtility.parseTimeStamp(dbFilterDateTime4));
						}
						filter.setFirstContent4(dbFirstContent4);
						filter.setFirstView4(dbFirstView4);
						filter.setTransactorStep4(dbTransactorStep4);
						filter.setPassOrNot4(Integer.parseInt(passOrNot4));
						filter.setStepFlag(5);
						orm.updateSingle(dbConn, filter);
						String smsContent = person.getUserName() + " 向您提交招聘筛选，请办理！";
						String remindUrl = "";
						if ("1".equals(smsRemind4) && !YHUtility.isNullorEmpty(nextTransaStep)) {
							this.doSmsBackTime(dbConn, smsContent, person.getSeqId(), nextTransaStep, "65", remindUrl, new Date());
						}
						if ("1".equals(sms2Remind4) && !YHUtility.isNullorEmpty(nextTransaStep)) {
							YHMobileSms2Logic sbl = new YHMobileSms2Logic();
							sbl.remindByMobileSms(dbConn, nextTransaStep , person.getSeqId() , smsContent , new Date());
						}
					}else {
						
						filter.setFilterMethod4(dbFilterMethod4);
						if (!YHUtility.isNullorEmpty(dbFilterDateTime4)) {
							filter.setFilterDateTime4(YHUtility.parseTimeStamp(dbFilterDateTime4));
						}
						filter.setFirstContent4(dbFirstContent4);
						filter.setFirstView4(dbFirstView4);
						filter.setTransactorStep4(dbTransactorStep4);
						filter.setPassOrNot4(Integer.parseInt(passOrNot4));
						filter.setEndFlag(1);
						filter.setStepFlag(5);
						orm.updateSingle(dbConn, filter);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void updateHrcecruitPool(Connection dbConn,int filterId ) throws Exception{
		PreparedStatement stmt = null;
		String sql = "update oa_pm_enroll_set set WHETHER_BY_SCREENING=?  where SEQ_ID=(select EXPERT_ID from oa_pm_enroll_filter where SEQ_ID= ?)";
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setInt(1, 1);
			stmt.setInt(2, filterId);
			stmt.executeUpdate();
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(stmt, null, log);
		}
		
	}
	
	/**
	 * 招聘筛选查询 列表
	 * @param dbConn
	 * @param request
	 * @param person
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String queryFilterListLogic(Connection dbConn, Map request, YHPerson person,Map map) throws Exception{
		String employeeName = (String) map.get("employeeName");
		String planName = (String) map.get("planName");
		String planNo = (String) map.get("planNo");
		String position = (String) map.get("position");
		String employeeMajor = (String) map.get("employeeMajor");
		
		String employeePhone = (String) map.get("employeePhone");
		String transactorStep = (String) map.get("transactorStep");
		String nextTransaStep1 = (String) map.get("nextTransaStep1");
		String nextTransaStep2 = (String) map.get("nextTransaStep2");
		String nextTransaStep3 = (String) map.get("nextTransaStep3");
		String nextTransaStep4 = (String) map.get("nextTransaStep4");
		String status = (String) map.get("status");
		
		String conditionStr = "";		
		
		try {
			
			if (!YHUtility.isNullorEmpty(employeeName)) {
				conditionStr += " and EMPLOYEE_NAME like '%" + YHDBUtility.escapeLike(employeeName) + "%'" + YHDBUtility.escapeLike();
			}
//			if (!YHUtility.isNullorEmpty(planName)) {
//				conditionStr += " and PLAN_NAME like '%" + YHDBUtility.escapeLike(planName) + "%'" + YHDBUtility.escapeLike();
//			}
			if (!YHUtility.isNullorEmpty(position)) {
				conditionStr += " and POSITION like '%" + YHDBUtility.escapeLike(position) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(employeeMajor)) {
				conditionStr += " and EMPLOYEE_MAJOR like '%" + YHDBUtility.escapeLike(employeeMajor) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(employeePhone)) {
				conditionStr += " and EMPLOYEE_PHONE like '%" + YHDBUtility.escapeLike(employeePhone) + "%'" + YHDBUtility.escapeLike();
			}
			
			if (!YHUtility.isNullorEmpty(transactorStep)) {
				conditionStr += " and TRANSACTOR_STEP ='" + YHDBUtility.escapeLike(transactorStep) + "'";
			}
			if (!YHUtility.isNullorEmpty(nextTransaStep1)) {
				conditionStr += " and NEXT_TRANSA_STEP1 ='" + YHDBUtility.escapeLike(nextTransaStep1) + "'";
			}
			if (!YHUtility.isNullorEmpty(nextTransaStep2)) {
				conditionStr += " and NEXT_TRANSA_STEP2 ='" + YHDBUtility.escapeLike(nextTransaStep2) + "'";
			}
			if (!YHUtility.isNullorEmpty(nextTransaStep3)) {
				conditionStr += " and NEXT_TRANSA_STEP3 ='" + YHDBUtility.escapeLike(nextTransaStep3) + "'";
			}
			if (!YHUtility.isNullorEmpty(nextTransaStep4)) {
				conditionStr += " and TRANSACTOR_STEP4 ='" + YHDBUtility.escapeLike(nextTransaStep4) + "'";
			}
			if ("0".equals(status)) {
				conditionStr += " and (STEP_FLAG='1' or STEP_FLAG='2' or STEP_FLAG='3' or STEP_FLAG='4') and END_FLAG='0'";
			}
			if ("1".equals(status)) {
				conditionStr += " and END_FLAG='1'";
			}
			if ("2".equals(status)) {
				conditionStr += " and END_FLAG='2'";
			}
			
			
			YHHrRecruitPoolLogic poolLogic = new YHHrRecruitPoolLogic();
			
			
			
//			String conditionStr = " TRANSACTOR_STEP='" + person.getSeqId()
//					+ "' or NEXT_TRANSA_STEP='" + person.getSeqId() 
//					+ "' or NEXT_TRANSA_STEP1='" + person.getSeqId() 
//					+ "' or NEXT_TRANSA_STEP2='" + person.getSeqId() 
//					+ "' or NEXT_TRANSA_STEP3='" + person.getSeqId() + "'" ;
			String sql =  "select " +
   		"SEQ_ID" +
   		",EMPLOYEE_NAME" +
   		",POSITION" +
   		",EMPLOYEE_MAJOR" +
   		",EMPLOYEE_PHONE" +
   		",TRANSACTOR_STEP" +
   		
   		",END_FLAG" +
       " from " +
   		" oa_pm_enroll_filter where " + poolLogic.getHrPriv(dbConn, person, "") + conditionStr + " order by SEQ_ID desc"; 
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 获取处理状态
	 * 2011-3-28
	 * @param dbConn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public String passORNotLogic(Connection dbConn,int seqId) throws Exception{
		String titleStatus = "";
		YHORM orm = new YHORM();
		try {
			YHHrRecruitFilter filter = (YHHrRecruitFilter) orm.loadObjSingle(dbConn, YHHrRecruitFilter.class, seqId);
			if (filter!=null) {
				int endFlag = filter.getEndFlag();
				int exptId = filter.getExpertId();
				YHHrRecruitPool recruitPool = (YHHrRecruitPool) orm.loadObjSingle(dbConn, YHHrRecruitPool.class, exptId);
				int whetherByScreening = 0;
				if (recruitPool!=null) {
					whetherByScreening = recruitPool.getWhetherByScreening();
				}
				
				if (endFlag ==0) {
					titleStatus = "待筛选";
				}else if(endFlag == 2) {
					if (whetherByScreening ==0) {
						titleStatus = "未通过";
					}else {
						titleStatus = "已通过";
					}
				}else {
					int stepFlag = filter.getStepFlag();
					if (stepFlag ==2) {
						if (filter.getPassOrNot1() == 1 ) {
							titleStatus = "已通过";
						}else {
							titleStatus = "未通过";
						}
					}else if (stepFlag == 3) {
						if (filter.getPassOrNot2() == 1 ) {
							titleStatus = "已通过";
						}else {
							titleStatus = "未通过";
						}
					}else if (stepFlag == 4) {
						if (filter.getPassOrNot3() == 1 ) {
							titleStatus = "已通过";
						}else {
							titleStatus = "未通过";
						}
					}else if (stepFlag == 5) {
						if (filter.getPassOrNot4() == 1 ) {
							titleStatus = "已通过";
						}else {
							titleStatus = "未通过";
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return  titleStatus;
	}
	

}

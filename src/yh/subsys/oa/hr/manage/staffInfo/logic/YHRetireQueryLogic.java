package yh.subsys.oa.hr.manage.staffInfo.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;

public class YHRetireQueryLogic {
	private static Logger log = Logger.getLogger(YHRetireQueryLogic.class);

	/**
	 * 本月退休人员查询列表
	 * 
	 * @param dbConn
	 * @param request
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getThisMonthInfoListLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
		// String ageArry[] = this.getHrRetireAge(dbConn);
		// String manAgeStr = ageArry[0];
		// String womenAgeStr = ageArry[1];
		// int manAge = 0;
		// int womenAge = 0;
		// if (YHUtility.isNumber(manAgeStr)) {
		// manAge = Integer.parseInt(manAgeStr);
		// }
		// if (YHUtility.isNumber(womenAgeStr)) {
		// womenAge = Integer.parseInt(womenAgeStr);
		// }
		//
		// Calendar now = Calendar.getInstance();
		// now.setTime(new Date());
		// int thisYear = now.get(Calendar.YEAR);
		// int yearMin = thisYear - manAge;
		// // int thisMonth = now.get(Calendar.MONTH) + 1;
		// int thisMaxDay = now.getActualMaximum(Calendar.DAY_OF_MONTH);
		// now.set(Calendar.YEAR, yearMin);
		// now.set(Calendar.DATE, 1);
		// Date minDate = now.getTime();
		//
		// now.set(Calendar.DATE, thisMaxDay);
		// Date maxDate = now.getTime();
		try {
			// String sql = "select "
			// + " a.SEQ_ID"
			// + ", a.DEPT_ID"
			// + ", a.STAFF_NAME"
			// + ", a.STAFF_SEX"
			// + ", a.STAFF_BIRTH"
			// + " from HR_STAFF_INFO a, PERSON p,DEPARTMENT d "
			// + " where a.USER_ID=p.USER_ID AND p.DEPT_ID=d.SEQ_ID";
			// sql = sql + " and " + YHDBUtility.getDateFilter("a.STAFF_BIRTH",
			// YHUtility.getDateTimeStr(minDate).substring(0, 10), ">=");
			// sql = sql + " and " + YHDBUtility.getDateFilter("a.STAFF_BIRTH",
			// YHUtility.getDateTimeStr(maxDate).substring(0, 10), "<=");
			
			String seqIdStr = this.getSeqIdStr(dbConn);
			String sql = "select " + " a.SEQ_ID" + ", a.DEPT_ID" + ", a.STAFF_NAME" + ", a.STAFF_SEX" + ", a.STAFF_BIRTH" + " from oa_pm_employee_info a"
					+ " where a.SEQ_ID IN(" + seqIdStr + ")";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 按条日期退休人员
	 * 
	 * @param dbConn
	 * @param request
	 * @param queryDate1
	 * @param queryDate2
	 * @return
	 * @throws Exception
	 */
	public String queryRetirListJsonLogic(Connection dbConn, Map request, String queryDate1, String queryDate2) throws Exception {
		try {
			String seqIdStr = this.getRetireSeqIdStr(dbConn, queryDate1, queryDate2);
			String sql = "select " 
				+ " a.SEQ_ID" 
				+ ", a.DEPT_ID" 
				+ ", a.STAFF_NAME" 
				+ ", a.STAFF_SEX" 
				+ ", a.STAFF_BIRTH" 
				+ " from oa_pm_employee_info a"
				+ " where a.SEQ_ID IN(" + seqIdStr + ")";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			throw e;
		}
	}

	public String getRetireSeqIdStr(Connection dbConn, String queryDate1, String queryDate2) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "select a.SEQ_ID,a.STAFF_SEX,a.STAFF_BIRTH from oa_pm_employee_info a  LEFT OUTER JOIN PERSON b ON a.USER_ID = b.USER_ID  LEFT OUTER JOIN oa_department f ON b.DEPT_ID=f.SEQ_ID ";
		String seqIdStr = "";
		try {
			String ageArry[] = this.getHrRetireAge(dbConn);
			String manAgeStr = ageArry[0];
			String womenAgeStr = ageArry[1];
			int manAge = 0;
			int womenAge = 0;
			if (YHUtility.isNumber(manAgeStr)) {
				manAge = Integer.parseInt(manAgeStr);
			}
			if (YHUtility.isNumber(womenAgeStr)) {
				womenAge = Integer.parseInt(womenAgeStr);
			}
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String staffSex = YHUtility.null2Empty(rs.getString(2));
				Date staffBirth = rs.getTimestamp(3);
				if ("0000-00-00".equals(staffBirth) || "1900-01-01".equals(staffBirth) || staffBirth == null) {
					continue;
				}
				if ("0".equals(staffSex)) {
					boolean isDateFlag = this.checkRetireDate(dbConn, manAge, staffBirth, queryDate1, queryDate2);
					if (isDateFlag) {
						seqIdStr += rs.getString(1) + ",";
					}
				} else {
					boolean isDateFlag = this.checkRetireDate(dbConn, womenAge, staffBirth, queryDate1, queryDate2);
					if (isDateFlag) {
						seqIdStr += rs.getString(1) + ",";
					}
				}
			}
			if (!YHUtility.isNullorEmpty(seqIdStr)) {
				if (seqIdStr.endsWith(",")) {
					seqIdStr = seqIdStr.substring(0, seqIdStr.length() - 1);
				}
			} else {
				seqIdStr = "0";
			}
		} catch (Exception e) {
			throw e;
		}
		return seqIdStr;
	}

	public String getSeqIdStr(Connection dbConn) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "select h.SEQ_ID,h.STAFF_SEX,h.STAFF_BIRTH from oa_pm_employee_info h,PERSON p,oa_department d where h.USER_ID=p.USER_ID AND p.DEPT_ID=d.SEQ_ID";
		String seqIdStr = "";
		try {
			String ageArry[] = this.getHrRetireAge(dbConn);
			String manAgeStr = ageArry[0];
			String womenAgeStr = ageArry[1];
			int manAge = 0;
			int womenAge = 0;
			if (YHUtility.isNumber(manAgeStr)) {
				manAge = Integer.parseInt(manAgeStr);
			}
			if (YHUtility.isNumber(womenAgeStr)) {
				womenAge = Integer.parseInt(womenAgeStr);
			}
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String staffSex = YHUtility.null2Empty(rs.getString(2));
				Date staffBirth = rs.getTimestamp(3);
				if ("0000-00-00".equals(staffBirth) || "1900-01-01".equals(staffBirth) || staffBirth == null) {
					continue;
				}
				if ("0".equals(staffSex)) {
					boolean isDateFlag = this.checkDate(dbConn, manAge, staffBirth);
					if (isDateFlag) {
						seqIdStr += rs.getString(1) + ",";
					}
				} else {
					boolean isDateFlag = this.checkDate(dbConn, womenAge, staffBirth);
					if (isDateFlag) {
						seqIdStr += rs.getString(1) + ",";
					}
				}
			}
			if (!YHUtility.isNullorEmpty(seqIdStr)) {
				if (seqIdStr.endsWith(",")) {
					seqIdStr = seqIdStr.substring(0, seqIdStr.length() - 1);
				}
			} else {
				seqIdStr = "0";
			}
		} catch (Exception e) {
			throw e;
		}
		return seqIdStr;
	}

	public boolean checkRetireDate(Connection dbConn, int retireAge, Date staffBirth, String queryDate1, String queryDate2) throws Exception {
		boolean flag = false;
		try {
			boolean date1Flag = false;
			boolean date2Flag = false;
			Calendar now = Calendar.getInstance();

			if (!YHUtility.isNullorEmpty(queryDate1)) {
				now.setTime(YHUtility.parseDate(queryDate1));
				int thisYear = now.get(Calendar.YEAR);
				int yearMin = thisYear - retireAge;
				now.set(Calendar.YEAR, yearMin);
				Date minDate = now.getTime();
        if (minDate.compareTo(staffBirth) <= 0) {// maxDate比staffBirth小

          date1Flag = true;
        }
			}
			else{
	      now.setTime(YHUtility.parseDate("1920-01-01"));
        int thisYear = now.get(Calendar.YEAR);
        int yearMin = thisYear - retireAge;
        now.set(Calendar.YEAR, yearMin);
        Date minDate = now.getTime();
        if (minDate.compareTo(staffBirth) <= 0) {// maxDate比staffBirth小

          date1Flag = true;
        }
			}
			if (!YHUtility.isNullorEmpty(queryDate2)) {
				now.setTime(YHUtility.parseDate(queryDate2));
				int thisYear = now.get(Calendar.YEAR);
				int yearMin = thisYear - retireAge;
				now.set(Calendar.YEAR, yearMin);
				Date maxDate = now.getTime();
				if (maxDate.compareTo(staffBirth) >= 0) {// maxDate比staffBirth大
					date2Flag = true;
				}
			}
			else {
        now.setTime(new Date());
        int thisYear = now.get(Calendar.YEAR);
        int yearMin = thisYear - retireAge;
        now.set(Calendar.YEAR, yearMin);
        Date maxDate = now.getTime();
        if (maxDate.compareTo(staffBirth) >= 0) {// maxDate比staffBirth大

          date2Flag = true;
        }
      }
			if (date1Flag && date2Flag) {
				flag = true;
			}
		} catch (Exception e) {
			throw e;
		}
		return flag;
	}

	public boolean checkDate(Connection dbConn, int retireAge, Date staffBirth) throws Exception {
		boolean flag = false;
		try {
			Calendar now = Calendar.getInstance();
			now.setTime(new Date());
			int thisYear = now.get(Calendar.YEAR);
			int yearMin = thisYear - retireAge;
			int thisMaxDay = now.getActualMaximum(Calendar.DAY_OF_MONTH);
			now.set(Calendar.YEAR, yearMin);
			now.set(Calendar.DATE, 1);
			Date minDate = now.getTime();

			now.set(Calendar.DATE, thisMaxDay);
			Date maxDate = now.getTime();

			if (minDate.compareTo(staffBirth) <= 0 && staffBirth.compareTo(maxDate) <= 0) {
				flag = true;
			}
		} catch (Exception e) {
			throw e;
		}
		return flag;
	}

	public String[] getHrRetireAge(Connection conn) throws Exception {
		String result = "";
		String sql = " select PARA_VALUE from SYS_PARA where PARA_NAME = 'RETIRE_AGE'";
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
			String manAge = "";
			String womenAge = "";
			if (!YHUtility.isNullorEmpty(result)) {
				String resultArry[] = result.split(",");
				if (resultArry.length > 1) {
					manAge = resultArry[0];
					womenAge = resultArry[1];
				} else if (resultArry.length == 1) {
					manAge = resultArry[0];
				}

			}
			String[] ageArry = { manAge, womenAge };
			return ageArry;
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, log);
		}
	}

}

package yh.subsys.oa.hr.manage.analysis.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHHrStaffAnalysisLogic {
  private static Logger log = Logger.getLogger(YHHrStaffAnalysisLogic.class);

  public String getAnalysis(Connection dbConn, String module, String sumField, String deptId, String ageRange, YHPerson person) throws Exception{
    
    PreparedStatement ps = null;
    ResultSet rs = null; 
    String data = getTitle(module, sumField);
    String sumName = "";
    String totalCount = "";
    try{
      String sql = getSql(module,sumField,deptId);

      ps = dbConn.prepareStatement(sql);
      if(module.equals("HR_INFO") && Integer.parseInt(sumField) == 2){
        String ageList[] = ageRange.split(",");
        for(int i = 0; i < ageList.length; i++){
          String temp = ageList[i];
          String arr[] = temp.split("-");
          ps.setString(1, arr[0]);
          ps.setString(2, arr[1]);
          rs = ps.executeQuery();
          while(rs.next()){
            totalCount = rs.getString("total_count");
            if(!totalCount.equals("0")) {
              data = data +  "<set name='" + arr[0] + "-" + arr[1] + "' value='" + totalCount + "'/>";
            }
          }
        }
      }
        
      else{
        rs = ps.executeQuery();
  
        while(rs.next()){
          sumName = rs.getString("sum_name");
          if(YHUtility.isNullorEmpty(sumName)){
            sumName = "其他";
          }
          if(module.equals("HR_JC")){
            if(sumName.equals("1")) sumName = "奖励";
            else if(sumName.equals("2")) sumName = "罚款";
          }
          if(module.equals("HR_HT")){
            if(Integer.parseInt(sumField) == 4 && !sumName.equals("其他")){
              sumName = sumName.substring(0, 10);
              String temp = sumName.substring(9,10);
              if(temp.equals(".")){
                sumName = sumName.substring(0,8)+"0"+sumName.substring(8,9);
              }
            }
          } 
          if(module.equals("HR_INFO")){
            if(Integer.parseInt(sumField) == 3){
              if(sumName.equals("0")) sumName = "男";
              else if(sumName.equals("1")) sumName = "女";
            }
            if(Integer.parseInt(sumField) == 9 && !sumName.equals("其他")){
              sumName = sumName.substring(0, 10);
              String temp = sumName.substring(9,10);
              if(temp.equals(".")){
                sumName = sumName.substring(0,8)+"0"+sumName.substring(8,9);
              }
            }
          }
          totalCount = rs.getString("total_count");
          data = data +  "<set name='" + sumName + "' value='" + totalCount + "'/>";
        }
      }
      return data + "</graph>";
    }catch(Exception ex) {
      throw ex;
    }
  }
  
 public String getAnalysisTwo(Connection dbConn, String module,  String deptId,String startDate, String endDate ) throws Exception{
    
    PreparedStatement ps = null;
    ResultSet rs = null; 
    String data = getTitle(module,"");
    String sumName = "";
    String totalCount = "";
    try{
      String sql = getSqlTwo(module,deptId,startDate,endDate);

      ps = dbConn.prepareStatement(sql);
        rs = ps.executeQuery(); 
        while(rs.next()){
          sumName = rs.getString("sum_name");
          sumName=this.getDeptName(dbConn, sumName);       
        
          totalCount = rs.getString("total_count");
          data = data +  "<set name='" + sumName + "' value='" + totalCount + "'/>";
        }
      return data + "</graph>";
    }catch(Exception ex) {
      throw ex;
    }
  }
  
  public String getAnalysisList(Connection dbConn, String module, String deptId, String startDate, String endDate) throws Exception{
    
    Statement stmt = null;
    ResultSet rs = null; 
    String sql="";
    String data="";
    try{
      deptId=deptId.replaceAll(",","','");
      if("HR_LZ1".equals(module)){
         String dept=" leave_dept in ('"+deptId+"') and ";
         if("0".equals(deptId)){
           dept="";
         }
         sql="select leave_dept,count(seq_id) from oa_pm_employee_leave where "+dept+"  "+YHDBUtility.getDateFilter("QUIT_TIME_FACT", startDate, ">=")+" and "+YHDBUtility.getDateFilter("QUIT_TIME_FACT", endDate, "<=")+" group by leave_dept ";
      }
      else if("HR_DD1".equals(module)){
        String dept=" TRAN_DEPT_BEFORE in ('"+deptId+"') and ";
        if("0".equals(deptId)){
          dept="";
        }
         sql="select TRAN_DEPT_BEFORE,count(seq_id) from oa_pm_employee_transfer where "+dept+" "+YHDBUtility.getDateFilter("TRANSFER_DATE", startDate, ">=")+" and "+YHDBUtility.getDateFilter("TRANSFER_DATE", endDate, "<=")+" group by TRAN_DEPT_BEFORE ";        
      }
      stmt=dbConn.createStatement();
      rs=stmt.executeQuery(sql);
      String deptName="";
      String count="";
      while(rs.next()){
        deptName=getDeptName(dbConn,rs.getString(1));
        count=rs.getString(2);
        data+="{dept:'"+deptName+"',count:'"+count+"'}";
        data+=",";
      }
      
      if(data.endsWith(",")){
        data=data.substring(0,data.length()-1);
      }
      return data;
    }catch(Exception ex) {
      throw ex;
    }
  }
  public String getDeptName(Connection conn,String deptId) throws SQLException{
    Statement stmt = null;
    ResultSet rs = null; 
    String deptName="";
    String sql="select dept_name from oa_department where seq_id="+deptId;
    stmt=conn.createStatement();
    rs=stmt.executeQuery(sql);
    if(rs.next()){
      deptName=rs.getString("dept_name");
    }
   return deptName;
  }
  private String getSqlTwo(String module,String deptId,String startDate,String endDate) throws Exception{
    String sql="";
    deptId=deptId.replaceAll(",", "','");
    if(module.equals("HR_LZ1")){
      String dept=" leave_dept in ('"+deptId+"') and ";
      if("0".equals(deptId)){
        dept="";
      }
      sql="select leave_dept SUM_NAME,count(seq_id) TOTAL_COUNT from oa_pm_employee_leave where "+dept+"  "+YHDBUtility.getDateFilter("QUIT_TIME_FACT", startDate, ">=")+" and "+YHDBUtility.getDateFilter("QUIT_TIME_FACT", endDate, "<=")+" group by leave_dept ";
    }else if("HR_DD1".equals(module)){
      String dept=" TRAN_DEPT_BEFORE in ('"+deptId+"') and ";
      if("0".equals(deptId)){
        dept="";
      }
      sql="select TRAN_DEPT_BEFORE SUM_NAME,count(seq_id) TOTAL_COUNT from oa_pm_employee_transfer where "+dept+" "+YHDBUtility.getDateFilter("TRANSFER_DATE", startDate, ">=")+" and "+YHDBUtility.getDateFilter("TRANSFER_DATE", endDate, "<=")+" group by TRAN_DEPT_BEFORE ";        
      
    }
    return sql;
  }
  private String getSql(String module, String sumField, String deptId){
    String sql = "";
    //人事档案
    if(module.equals("HR_INFO")){
      //人事档案--学历
      if(Integer.parseInt(sumField) == 1){
        sql = " select C1.CODE_NAME SUM_NAME , COUNT(1) TOTAL_COUNT "
            + " from  oa_pm_employee_info l1 "
            + " left join oa_pm_code c1 on l1.STAFF_HIGHEST_SCHOOL = c1.SEQ_ID ";
        if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
          sql = sql + " where  l1.DEPT_ID in (" + deptId + ") ";
        }
        sql = sql  + " GROUP BY c1.CODE_NAME ";
      }
      //人事档案--年龄*************************
      else if(Integer.parseInt(sumField) == 2){
        sql = " select  COUNT(1) TOTAL_COUNT "
            + " from  oa_pm_employee_info l1 "
            + " where l1.STAFF_AGE between ? and ? ";
          if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
            sql = sql + " and l1.DEPT_ID in (" + deptId + ") ";
          }
      }
      //人事档案--性别
      else if(Integer.parseInt(sumField) == 3){
        sql = " select l1.STAFF_SEX SUM_NAME , COUNT(1) TOTAL_COUNT "
          + " from  oa_pm_employee_info l1 ";
          if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
            sql = sql + " where l1.DEPT_ID in (" + deptId + ") ";
          }
          sql = sql + " GROUP BY l1.STAFF_SEX ";
      }
      //人事档案--政治面貌
      else if(Integer.parseInt(sumField) == 4){
        sql = " select c1.CODE_NAME SUM_NAME , COUNT(1) TOTAL_COUNT "
           + " from  oa_pm_employee_info l1 "
           + " left join oa_pm_code c1 on l1.STAFF_POLITICAL_STATUS = c1.SEQ_ID ";
          if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
            sql = sql + " where l1.DEPT_ID in (" + deptId + ") ";
          }
          sql = sql+ " GROUP BY c1.CODE_NAME ";
      }
      //人事档案--在职状态
      else if(Integer.parseInt(sumField) == 5){
        sql = " select c1.CODE_NAME SUM_NAME , COUNT(1) TOTAL_COUNT "
          + " from  oa_pm_employee_info l1 "
          + " left join oa_pm_code c1 on l1.WORK_STATUS = c1.SEQ_ID ";
          if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
            sql = sql + " where l1.DEPT_ID in (" + deptId + ") ";
          }
          sql = sql + " GROUP BY c1.CODE_NAME ";
      }
      //人事档案--籍贯
      else if(Integer.parseInt(sumField) == 6){
        sql = " select c1.CODE_NAME SUM_NAME , COUNT(1) TOTAL_COUNT "
          + " from  oa_pm_employee_info l1 "
          + " left join oa_pm_code c1 on l1.STAFF_NATIVE_PLACE = c1.SEQ_ID ";
          if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
            sql = sql + " where l1.DEPT_ID in (" + deptId + ") ";
          }
          sql = sql + " GROUP BY c1.CODE_NAME ";
      }
      //人事档案--职称
      else if(Integer.parseInt(sumField) == 7){
        sql = " select c1.CODE_NAME SUM_NAME , COUNT(1) TOTAL_COUNT "
           + " from  oa_pm_employee_info l1 "
           + " left join oa_pm_code c1 on l1.PRESENT_POSITION = c1.SEQ_ID ";
        if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
          sql = sql + " where l1.DEPT_ID in (" + deptId + ") ";
        }
        sql = sql + " GROUP BY c1.CODE_NAME ";
      }
      //人事档案--员工类型
      else if(Integer.parseInt(sumField) == 8){
        sql = " select c1.CODE_NAME SUM_NAME , COUNT(1) TOTAL_COUNT "
          + " from  oa_pm_employee_info l1 "
          + " left join oa_pm_code c1 on l1.STAFF_OCCUPATION = c1.SEQ_ID ";
        if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
          sql = sql + " where l1.DEPT_ID in (" + deptId + ") ";
        }
        sql = sql + " GROUP BY c1.CODE_NAME ";
      }
      //人事档案--加入本单位时间
      else if(Integer.parseInt(sumField) == 9){
        sql = " select l1.DATES_EMPLOYED SUM_NAME , COUNT(1) TOTAL_COUNT "
          + " from  oa_pm_employee_info l1 ";
          if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
            sql = sql + " where l1.DEPT_ID in (" + deptId + ") ";
          }
          sql = sql + " GROUP BY l1.DATES_EMPLOYED ";
      }
    } 
    //合同管理
    else if(module.equals("HR_HT")){
      //合同管理--合同类型
      if(Integer.parseInt(sumField) == 1){
        sql = " select C1.CODE_NAME SUM_NAME , COUNT(1) TOTAL_COUNT "
            + " from  oa_pm_employee_contract l1 ";
        if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
          sql = sql + " join person p1 on l1.STAFF_NAME = p1.seq_id and p1.DEPT_ID in (" + deptId + ") ";
        }
        sql = sql + " left join oa_pm_code c1 on l1.CONTRACT_TYPE = c1.SEQ_ID "
                  + " GROUP BY c1.CODE_NAME ";
      }
      //合同管理--合同状态
      else if(Integer.parseInt(sumField) == 2){
        sql = " select C1.CODE_NAME SUM_NAME , COUNT(1) TOTAL_COUNT "
          + " from  oa_pm_employee_contract l1 ";
          if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
            sql = sql + " join person p1 on l1.STAFF_NAME = p1.seq_id and p1.DEPT_ID in (" + deptId + ") ";
          }
          sql = sql + " left join oa_pm_code c1 on l1.STATUS = c1.SEQ_ID "
                    + " GROUP BY c1.CODE_NAME ";
      }
      //合同管理--合同属性
      else if(Integer.parseInt(sumField) == 3){
        sql = " select l1.CONTRACT_SPECIALIZATION SUM_NAME , COUNT(1) TOTAL_COUNT "
          + " from  oa_pm_employee_contract l1 ";
          if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
            sql = sql + " join person p1 on l1.STAFF_NAME = p1.seq_id and p1.DEPT_ID in (" + deptId + ") ";
          }
          sql = sql + " GROUP BY l1.CONTRACT_SPECIALIZATION ";
      }
      //合同管理--签订日期
      else if(Integer.parseInt(sumField) == 4){
        sql = " select l1.MAKE_CONTRACT SUM_NAME , COUNT(1) TOTAL_COUNT "
          + " from  oa_pm_employee_contract l1 ";
          if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
            sql = sql + " join person p1 on l1.STAFF_NAME = p1.seq_id and p1.DEPT_ID in (" + deptId + ") ";
          }
          sql = sql + " GROUP BY l1.MAKE_CONTRACT ";
      }
    } 
    //奖惩管理
    else if(module.equals("HR_JC")){
      //奖惩管理--奖惩项目
      if(Integer.parseInt(sumField) == 1){
        sql = " select C1.CODE_NAME SUM_NAME , COUNT(1) TOTAL_COUNT "
            + " from  oa_pm_employee_encouragement l1 ";
        if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
          sql = sql + " join person p1 on l1.STAFF_NAME = p1.seq_id and p1.DEPT_ID in (" + deptId + ") ";
        }
        sql = sql + " left join oa_pm_code c1 on l1.INCENTIVE_ITEM = c1.SEQ_ID "
                  + " GROUP BY c1.CODE_NAME ";
      }
      //奖惩管理--奖惩属性
      else if(Integer.parseInt(sumField) == 2){
        sql = " select l1.INCENTIVE_TYPE SUM_NAME , COUNT(1) TOTAL_COUNT "
          + " from  oa_pm_employee_encouragement l1 ";
          if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
            sql = sql + " join person p1 on l1.STAFF_NAME = p1.seq_id and p1.DEPT_ID in (" + deptId + ") ";
          }
          sql = sql + " GROUP BY l1.INCENTIVE_TYPE ";
      }
    } 
    //证照管理
    else if(module.equals("HR_ZZ")){
      //证照管理--证照类型
      if(Integer.parseInt(sumField) == 1){
        sql = " select C1.CODE_NAME SUM_NAME , COUNT(1) TOTAL_COUNT "
            + " from  oa_pm_employee_certificate l1 ";
        if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
          sql = sql + " join person p1 on l1.STAFF_NAME = p1.seq_id and p1.DEPT_ID in (" + deptId + ") ";
        }
        sql = sql + " left join oa_pm_code c1 on l1.LICENSE_TYPE = c1.SEQ_ID "
                  + " GROUP BY c1.CODE_NAME ";
      }
      //证照管理--证照状态
      else if(Integer.parseInt(sumField) == 2){
        sql = " select C1.CODE_NAME SUM_NAME , COUNT(1) TOTAL_COUNT "
          + " from  oa_pm_employee_certificate l1 ";
          if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
            sql = sql + " join person p1 on l1.STAFF_NAME = p1.seq_id and p1.DEPT_ID in (" + deptId + ") ";
          }
          sql = sql + " left join oa_pm_code c1 on l1.STATUS = c1.SEQ_ID "
                    + " GROUP BY c1.CODE_NAME ";
      }
    } 
    //学习经历--所获学位
    else if(module.equals("HR_XX")){
      sql = " select l1.EXPIRATION_PERIOD SUM_NAME , COUNT(1) TOTAL_COUNT "
          + " from  oa_pm_employee_learn l1 ";
      if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
        sql = sql + " join person p1 on l1.STAFF_NAME = p1.seq_id and p1.DEPT_ID in (" + deptId + ") ";
      }
      sql = sql + " GROUP BY l1.EXPIRATION_PERIOD ";     
    }
    //工作经历
    else if(module.equals("HR_JL")){
      //工作经历--所在部门
      if(Integer.parseInt(sumField) == 1){
        sql = " select l1.WORK_BRANCH SUM_NAME , COUNT(1) TOTAL_COUNT "
            + " from  oa_pm_employee_experience l1 ";
        if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
          sql = sql + " join person p1 on l1.STAFF_NAME = p1.seq_id and p1.DEPT_ID in (" + deptId + ") ";
        }
        sql = sql + " GROUP BY l1.WORK_BRANCH ";
      }
      //工作经历--担任职务
      else if(Integer.parseInt(sumField) == 2){
        sql = " select l1.POST_OF_JOB SUM_NAME , COUNT(1) TOTAL_COUNT "
          + " from  oa_pm_employee_experience l1 ";
          if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
            sql = sql + " join person p1 on l1.STAFF_NAME = p1.seq_id and p1.DEPT_ID in (" + deptId + ") ";
          }
          sql = sql + " GROUP BY l1.POST_OF_JOB ";
      }
    }   
    //劳动技能
    else if(module.equals("HR_JN")){
      //劳动技能--技能名称
      if(Integer.parseInt(sumField) == 1){
        sql = " select l1.ABILITY_NAME SUM_NAME , COUNT(1) TOTAL_COUNT "
            + " from  oa_pm_employee_skills l1 ";
        if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
          sql = sql + " join person p1 on l1.STAFF_NAME = p1.seq_id and p1.DEPT_ID in (" + deptId + ") ";
        }
        sql = sql + " GROUP BY l1.ABILITY_NAME ";
      }
      //劳动技能--技能级别
      else if(Integer.parseInt(sumField) == 2){
        sql = " select l1.SKILLS_LEVEL SUM_NAME , COUNT(1) TOTAL_COUNT "
          + " from  oa_pm_employee_skills l1 ";
          if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
            sql = sql + " join person p1 on l1.STAFF_NAME = p1.seq_id and p1.DEPT_ID in (" + deptId + ") ";
          }
          sql = sql + " GROUP BY l1.SKILLS_LEVEL ";
      }
    }
    //社会关系
    else if(module.equals("HR_GX")){
      //社会关系--与本人关系
      if(Integer.parseInt(sumField) == 1){
        sql = " select C1.CODE_NAME SUM_NAME , COUNT(1) TOTAL_COUNT "
            + " from  oa_pm_employee_correlation r1 ";
        if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
          sql = sql + " join person p1 on r1.STAFF_NAME = p1.seq_id and p1.DEPT_ID in (" + deptId + ") ";
        }
        sql = sql + " left join oa_pm_code c1 on r1.RELATIONSHIP = c1.SEQ_ID "
                  + " GROUP BY c1.CODE_NAME ";
      }
      //社会关系--政治面貌
      else if(Integer.parseInt(sumField) == 2){
        sql = " select C1.CODE_NAME SUM_NAME , COUNT(1) TOTAL_COUNT "
          + " from  oa_pm_employee_correlation r1 ";
          if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
            sql = sql + " join person p1 on r1.STAFF_NAME = p1.seq_id and p1.DEPT_ID in (" + deptId + ") ";
          }
          sql = sql + " left join oa_pm_code c1 on r1.POLITICS = c1.SEQ_ID "
                    + " GROUP BY c1.CODE_NAME ";
      }
    }
    //人事调动--调动类型
    else if(module.equals("HR_DD")){
      sql = " select C1.CODE_NAME SUM_NAME , COUNT(1) TOTAL_COUNT "
          + " from  oa_pm_employee_transfer t1 ";
      if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
        sql = sql + " join person p1 on T1.TRANSFER_PERSON = p1.seq_id and p1.DEPT_ID in (" + deptId + ") ";
      }
      sql = sql + " left join oa_pm_code c1 on t1.TRANSFER_TYPE = c1.SEQ_ID "
                + " GROUP BY c1.CODE_NAME ";     
    }
    //离职管理--离职类型
    else if(module.equals("HR_LZ")){
      sql = " select C1.CODE_NAME SUM_NAME , COUNT(1) TOTAL_COUNT "
          + " from  oa_pm_employee_leave l1 ";
      if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
        sql = sql + " join person p1 on l1.LEAVE_PERSON = p1.seq_id and p1.DEPT_ID in (" + deptId + ") ";
      }
      sql = sql + " left join oa_pm_code c1 on l1.QUIT_TYPE = c1.SEQ_ID "
                + " GROUP BY c1.CODE_NAME ";     
    }
    //复职管理--复职类型
    else if(module.equals("HR_FZ")){
      sql = " select C1.CODE_NAME SUM_NAME , COUNT(1) TOTAL_COUNT "
          + " from  oa_pm_employee_reinstatement r1 ";
      if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
        sql = sql + " join person p1 on r1.REINSTATEMENT_PERSON = p1.seq_id and p1.DEPT_ID in (" + deptId + ") ";
      }
      sql = sql + " left join oa_pm_code c1 on r1.REAPPOINTMENT_TYPE = c1.SEQ_ID "
                + " GROUP BY c1.CODE_NAME ";     
    }
    //职称评定
    else if(module.equals("HR_ZC")){
      //职称评定--获取方式
      if(Integer.parseInt(sumField) == 1){
        sql = " select C1.CODE_NAME SUM_NAME , COUNT(1) TOTAL_COUNT "
          + " from  oa_pm_employee_title t1 ";
          if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
            sql = sql + " join person p1 on t1.APPROVE_PERSON = p1.seq_id and p1.DEPT_ID in (" + deptId + ") ";
          }
          sql = sql + " left join oa_pm_code c1 on t1.GET_METHOD = c1.SEQ_ID "
                    + " GROUP BY c1.CODE_NAME ";
      } 
      //职称评定--获取职称
      else if(Integer.parseInt(sumField) == 2){
        sql = " select t1.POST_NAME SUM_NAME , COUNT(1) TOTAL_COUNT "
          + " from  oa_pm_employee_title t1 ";
          if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
            sql = sql + " join person p1 on t1.APPROVE_PERSON = p1.seq_id and p1.DEPT_ID in (" + deptId + ") ";
          }
          sql = sql + " GROUP BY t1.POST_NAME ";
      } 
    }
    //员工关怀--关怀类型
    else if(module.equals("HR_GH")){
      sql = " select C1.CODE_NAME SUM_NAME , COUNT(1) TOTAL_COUNT "
          + " from  oa_pm_employee_care r1 ";
      if(!(YHUtility.isNullorEmpty(deptId) || deptId.equals("0"))){
        sql = sql + " join person p1 on r1.BY_CARE_STAFFS = p1.seq_id and p1.DEPT_ID in (" + deptId + ") ";
      }
      sql = sql + " left join oa_pm_code c1 on r1.CARE_TYPE = c1.SEQ_ID "
                + " GROUP BY c1.CODE_NAME ";     
    }
    return sql;
  }
  
  private String getTitle(String module, String sumField){
    String title = "";
    if(module.equals("HR_INFO")){
      if(Integer.parseInt(sumField) == 1){
        title = "<graph caption='按学历统计' showNames='1'>";
      }
      else if(Integer.parseInt(sumField) == 2){
        title = "<graph caption='按年龄统计' showNames='1'>";
      }
      else if(Integer.parseInt(sumField) == 3){
        title = "<graph caption='按性别统计' showNames='1'>";
      }
      else if(Integer.parseInt(sumField) == 4){
        title = "<graph caption='按政治面貌统计' showNames='1'>";
      }
      else if(Integer.parseInt(sumField) == 5){
        title = "<graph caption='按在职状态统计' showNames='1'>";
      }
      else if(Integer.parseInt(sumField) == 6){
        title = "<graph caption='按籍贯统计' showNames='1'>";
      }
      else if(Integer.parseInt(sumField) == 7){
        title = "<graph caption='按职称统计' showNames='1'>";
      }
      else if(Integer.parseInt(sumField) == 8){
        title = "<graph caption='按员工类型统计' showNames='1'>";
      }
      else if(Integer.parseInt(sumField) == 9){
        title = "<graph caption='按加入本单位时间统计' showNames='1'>";
      }
    } 
    else if(module.equals("HR_HT")){
      if(Integer.parseInt(sumField) == 1){
        title = "<graph caption='按合同类型统计' showNames='1'>";
      }
      else if(Integer.parseInt(sumField) == 2){
        title = "<graph caption='按合同状态统计' showNames='1'>";
      }
      else if(Integer.parseInt(sumField) == 3){
        title = "<graph caption='按合同属性统计' showNames='1'>";
      }
      else if(Integer.parseInt(sumField) == 4){
        title = "<graph caption='按签订日期统计' showNames='1'>";
      }
    }  
    else if(module.equals("HR_JC")){
      if(Integer.parseInt(sumField) == 1){
        title = "<graph caption='按奖惩项目统计' showNames='1'>";
      }
      else if(Integer.parseInt(sumField) == 2){
        title = "<graph caption='按奖惩属性统计' showNames='1'>";
      }
    }  
    if(module.equals("HR_ZZ")){
      if(Integer.parseInt(sumField) == 1){
        title = "<graph caption='按证照类型统计' showNames='1'>";
      }
      else if(Integer.parseInt(sumField) == 2){
        title = "<graph caption='按证照状态统计' showNames='1'>";
      }
    }      
    else if(module.equals("HR_XX")){
      title = "<graph caption='按所获学位统计' showNames='1'>";
    }
    else if(module.equals("HR_JL")){
      if(Integer.parseInt(sumField) == 1){
        title = "<graph caption='按所在部门统计' showNames='1'>";
      }
      else if(Integer.parseInt(sumField) == 2){
        title = "<graph caption='按担任职位统计' showNames='1'>";
      }
    }    
    else if(module.equals("HR_JN")){
      if(Integer.parseInt(sumField) == 1){
        title = "<graph caption='按技能名称统计' showNames='1'>";
      }
      else if(Integer.parseInt(sumField) == 2){
        title = "<graph caption='按技能级别统计' showNames='1'>";
      }
    }
    else if(module.equals("HR_GX")){
      if(Integer.parseInt(sumField) == 1){
        title = "<graph caption='按与本人关系统计' showNames='1'>";
      }
      else if(Integer.parseInt(sumField) == 2){
        title = "<graph caption='按政治面貌统计' showNames='1'>";
      }
    }
    else if(module.equals("HR_DD")){
      title = "<graph caption='按调动类型统计' showNames='1'>";
    }
    else if(module.equals("HR_LZ")){
      title = "<graph caption='按离职类型统计' showNames='1'>";
    }
    else if(module.equals("HR_FZ")){
      title = "<graph caption='按复职类型统计' showNames='1'>";
    }
    else if(module.equals("HR_ZC")){
      if(Integer.parseInt(sumField) == 1){
        title = "<graph caption='按获取方式统计' showNames='1'>";
      }
      else if(Integer.parseInt(sumField) == 2){
        title = "<graph caption='按获取职称统计' showNames='1'>";
      }
    }
    else if(module.equals("HR_GH")){
      title = "<graph caption='按关怀类型统计' showNames='1'>";
    }
    else if(module.equals("HR_LZ1")){
      title="<graph caption='按部门离职统计分析' showNames='1'>";
    }
    else if(module.equals("HR_DD1")){
      title="<graph caption='按部门人事调动统计分析' showNames='1'>";
    }
    return title;
  }
}

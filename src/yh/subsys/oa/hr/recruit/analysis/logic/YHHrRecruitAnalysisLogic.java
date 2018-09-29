package yh.subsys.oa.hr.recruit.analysis.logic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHHrRecruitAnalysisLogic {
 
  /**获取分析数据
    * @param dbConn;
    * @param sumField;
    * @param position
    * @param ageRange
    * @param person;
    * */
public String getAnalysisLogic(Connection dbConn, String sumField, String position, String ageRange, YHPerson person) throws Exception{
    
    PreparedStatement ps = null;
    ResultSet rs = null; 
    String data = getTitle(sumField);
    String sumName = "";
    String totalCount = "";
    try{
      String sql = getSql(sumField,position);
     
      ps = dbConn.prepareStatement(sql);
      if(Integer.parseInt(sumField) == 2){

        String ageList[] = ageRange.split(",");
        for(int i = 0; i < ageList.length; i++){
          String temp = ageList[i];
          String arr[] = temp.split("-");
          ps.setInt(1, Integer.parseInt(arr[0]));
          ps.setInt(2, Integer.parseInt(arr[1]));
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
        if(Integer.parseInt(sumField)!= 3){
         sumName=this.getCode(dbConn,sumName);
           }
        if(Integer.parseInt(sumField) == 3){
          
             if(Integer.parseInt(sumName.trim())==0) sumName="女";
             else if(Integer.parseInt(sumName.trim())==1) sumName="男";
  
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
  
  
private String getSql(String sumField, String position){
  String sql = "";
    //--学历
    if(Integer.parseInt(sumField) == 1){
      sql = " select EMPLOYEE_HIGHEST_SCHOOL SUM_NAME , COUNT(1) TOTAL_COUNT "
          + " from  oa_pm_enroll_set c1  where 1=1 ";
      if(!(YHUtility.isNullorEmpty(position) || position.equals("0"))){        
        sql += " and c1.POSITION='"+position+"' ";
      }
        sql = sql + " GROUP BY c1.EMPLOYEE_HIGHEST_SCHOOL ";
    }
    //--年龄
    else if(Integer.parseInt(sumField) == 2){
      sql = " select  COUNT(1) TOTAL_COUNT "
        + " from  oa_pm_enroll_set c1 "
          + " where c1.EMPLOYEE_AGE between ? and ? ";
      if(!(YHUtility.isNullorEmpty(position) || position.equals("0"))){
        sql += " and c1.POSITION='"+position+"' ";
      }
    }
    //人--性别
    else if(Integer.parseInt(sumField) == 3){
      sql = " select c1.EMPLOYEE_SEX SUM_NAME , COUNT(1) TOTAL_COUNT "
        + " from  oa_pm_enroll_set c1  where 1=1 ";
      if(!(YHUtility.isNullorEmpty(position) || position.equals("0"))){
        sql += " and c1.POSITION='"+position+"' ";
      }
        sql = sql + " GROUP BY c1.EMPLOYEE_SEX ";
    }
    //专业
    else if(Integer.parseInt(sumField) == 4){
      sql = " select c1.EMPLOYEE_MAJOR SUM_NAME , COUNT(1) TOTAL_COUNT "
        + " from  oa_pm_enroll_set c1  where 1=1 ";
      if(!(YHUtility.isNullorEmpty(position) || position.equals("0"))){
        sql += " and c1.POSITION='"+position+"' ";
      }
        sql =sql+ " GROUP BY c1.EMPLOYEE_MAJOR ";
               
    }
    //--籍贯
    else if(Integer.parseInt(sumField) == 5){
      sql = " select c1.EMPLOYEE_NATIVE_PLACE SUM_NAME , COUNT(1) TOTAL_COUNT "
        + " from  oa_pm_enroll_set c1  where 1=1 ";
      if(!(YHUtility.isNullorEmpty(position) || position.equals("0"))){
        sql += " and c1.POSITION='"+position+"' ";
      }
        sql = sql  + " GROUP BY  c1.EMPLOYEE_NATIVE_PLACE ";
                 
    }
    //期望工作性质
    else if(Integer.parseInt(sumField) == 6){
      sql = " select c1.JOB_CATEGORY SUM_NAME , COUNT(1) TOTAL_COUNT "
        + " from  oa_pm_enroll_set c1  where 1=1 ";
      if(!(YHUtility.isNullorEmpty(position) || position.equals("0"))){
        sql += " and c1.POSITION='"+position+"' ";
      }
        sql = sql + " GROUP BY c1.JOB_CATEGORY ";                 
      }
      return sql;
}

private String getTitle(String sumField){
  
  String title = "";
 
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
        title = "<graph caption='按专业统计' showNames='1'>";
      }
      else if(Integer.parseInt(sumField) == 5){
        title = "<graph caption='按籍贯统计' showNames='1'>";
      }
      else if(Integer.parseInt(sumField) == 6){
        title = "<graph caption='按期望工作性质统计' showNames='1'>";
      }

  
  return title;
} 
  

public String getCode( Connection dbConn,String codeId) throws SQLException{
  PreparedStatement ps= null;
  ResultSet rs = null; 
  String name="";
  String sql="";
  sql=" select CODE_NAME from oa_pm_code where SEQ_ID='"+codeId+"'";
  ps = dbConn.prepareStatement(sql);
  rs = ps.executeQuery();
  if(rs.next()){
    name=rs.getString("CODE_NAME");
  }
  if(name.equals("")){
     name=codeId;
  }
  return name;
}




}

package yh.core.funcs.system.syslog.logic;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import yh.core.data.YHDbRecord;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
  /**
   * 系统日志导出
   * @author Administrator
   *
   */
  public class YHExportlogLogic {
  public ArrayList<YHDbRecord> sysexportlog(String type, String users, String statrtime, String endtime,String ip, String remark, String copytime,Connection conn, YHPerson user)
    throws Exception {
    StringBuffer sb = new StringBuffer();
    Statement stmt = null;
    ResultSet rs = null;
    String startdate = "";
    String enddate = "";
    if(!YHUtility.isNullorEmpty(statrtime)){
       startdate= YHDBUtility.getDateFilter("TIME", statrtime, ">=");
    }
    if(!YHUtility.isNullorEmpty(endtime)){
       enddate = YHDBUtility.getDateFilter("TIME", endtime, "<=");
       //YHOut.println("endtime:::dc"+enddate);
    }
    try{
         stmt = conn.createStatement();
         String queryGllogSql="";
         if(copytime.equals("on")){
             if(YHUtility.isNullorEmpty(statrtime) && !YHUtility.isNullorEmpty(endtime)){
                 queryGllogSql = "select oa_sys_log.seq_id, oa_sys_log.user_id, oa_sys_log.time, oa_sys_log.ip, oa_sys_log.type, oa_sys_log.remark, PERSON.USER_NAME from oa_sys_log,person where "+enddate+" and person.seq_id=oa_sys_log.user_id";
             }
             else if(YHUtility.isNullorEmpty(endtime) && !YHUtility.isNullorEmpty(statrtime)){
                 queryGllogSql = "select oa_sys_log.seq_id, oa_sys_log.user_id, oa_sys_log.time, oa_sys_log.ip, oa_sys_log.type, oa_sys_log.remark, PERSON.USER_NAME from oa_sys_log,person where "+startdate+" and person.seq_id=oa_sys_log.user_id ";
             }else{
                //queryGllogSql = "select SYS_LOG.seq_id, SYS_LOG.user_id, SYS_LOG.time, SYS_LOG.ip, SYS_LOG.type, SYS_LOG.remark, PERSON.USER_NAME from sys_log,person where time >= to_date('" + statrtime + "','yyyy-mm-dd hh24:mi:ss')" +
             		//"and time <= to_date('" + endtime + "','yyyy-mm-dd hh24:mi:ss') and person.seq_id=sys_log.user_id order by time desc";
                  queryGllogSql = "select oa_sys_log.seq_id, oa_sys_log.user_id, oa_sys_log.time, oa_sys_log.ip, oa_sys_log.type, oa_sys_log.remark, PERSON.USER_NAME from oa_sys_log,person where "+startdate+"" +
                  " and "+enddate+" and person.seq_id=oa_sys_log.user_id";
             }
         }else{
             if (YHUtility.isNullorEmpty(statrtime) && !YHUtility.isNullorEmpty(endtime)){
                 queryGllogSql = "select seq_id, user_id, time, ip, type, remark from sys_log_"+copytime+" where "+enddate+"";
              }
              else if (YHUtility.isNullorEmpty(endtime) && !YHUtility.isNullorEmpty(statrtime)){
                 //queryGllogSql = "select * from (select seq_id, user_id, time, ip, type, remark from sys_log_"+copytime+" where time >= to_date('" + statrtime + "','yyyy-mm-dd hh24:mi:ss') order by time desc)where rownum <= 300";
                  queryGllogSql = "select seq_id, user_id, time, ip, type, remark from sys_log_"+copytime+" where "+startdate+"";
              }else{
                  queryGllogSql = "select seq_id, user_id, time, ip, type, remark from sys_log_"+copytime+" where "+startdate+"" +
                  " and "+enddate+" ";
              }
         }
         if (!"".equals(users) && users != null){
            queryGllogSql = queryGllogSql + " and oa_sys_log.user_id in ("
            + users + ")";
         }
        if (!"".equals(type) && type != null){
            queryGllogSql = queryGllogSql + " and oa_sys_log.type='"
            + type + "'";
        }
        if (!"".equals(ip) && ip != null){
            queryGllogSql = queryGllogSql + " and oa_sys_log.ip='"
            + ip + "'";
        }
        if(!"".equals(remark) && remark != null){
            queryGllogSql = queryGllogSql + " and oa_sys_log.remark like "
            + "'%" + YHDBUtility.escapeLike(remark) + "%'" +YHDBUtility.escapeLike();
        }
        //System.out.println(queryGllogSql);
        queryGllogSql= queryGllogSql+" order by time desc";
        rs = stmt.executeQuery(queryGllogSql);
        ArrayList<YHDbRecord>  dbl = new ArrayList<YHDbRecord>();
        String seqIdStr = "";
        String state = "";
        int num = 0;
        while (rs.next() && ++num<=300) {
             int seqId = rs.getInt("seq_id");
             int userId = rs.getInt("user_id");
             String user_name = rs.getString("USER_NAME");
             Timestamp date = rs.getTimestamp("time");
             String dates= YHUtility.getDateTimeStr(date);
             String Ip = rs.getString("ip");
             String types = rs.getString("type");
             //对23种类型进行转换    Map<Integer, String>
             String value = getValue(Integer.parseInt(types));
             
             String remarks = rs.getString("remark");
             YHDbRecord dbr = new YHDbRecord();
             dbr.addField("用户名",user_name);
             dbr.addField("日期", dates);
             dbr.addField("Ip地址", Ip);
             dbr.addField("日志类型", value);
             dbr.addField("备注", remarks);
             dbl.add(dbr);
       }
           return dbl;
   }catch(Exception ex){
          throw ex;
   }finally {
           YHDBUtility.close(stmt, rs, null);
    }  
  }
  /**
   * 导入 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public void sysimportlog(int nameValue, String dates, String ip, String type,String rmark, Connection conn, YHPerson user)
    throws Exception {
    StringBuffer sb = new StringBuffer();
    Statement stmt = null;
    ResultSet rs = null;
    String startdate= YHDBUtility.getDateFilter("TIME", dates, "<=");
    try{
       stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
       ResultSet.CONCUR_READ_ONLY);
       //String queryGllogSql="insert into sys_log(user_id,time,ip,type,remark)values("+nameValue+",to_date('" + dates + "','yyyy-mm-dd hh24:mi'),'"+ip+"','"+type+"','"+rmark+"')";
       String queryGllogSql="insert into oa_sys_log(user_id,time,ip,type,remark)values("+nameValue+","+startdate+",'"+ip+"','"+type+"','"+rmark+"')";
       rs = stmt.executeQuery(queryGllogSql);
    }catch(Exception ex){
           throw ex;
    }finally {
          YHDBUtility.close(stmt, rs, null);
    }  
  }
  /**
   * 转换名称
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public int insertimport(String nameValue, Connection conn, YHPerson user)
    throws Exception {
    StringBuffer sb = new StringBuffer();
    Statement stmt = null;
    ResultSet rs = null;
    try{
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_READ_ONLY);
        String queryGllogSql="select distinct oa_sys_log.user_id, PERSON.USER_NAME from oa_sys_log,person where person.seq_id=oa_sys_log.user_id and person.user_name='"+nameValue+"'";
        rs = stmt.executeQuery(queryGllogSql);
        int  userId=0;
        if(rs.next()){
          userId = rs.getInt("user_id");
        }
        return userId;
    }catch(Exception ex){
          throw ex;
    }finally {
        YHDBUtility.close(stmt, rs, null);
    }  
  }

  /**
   * 23种小码表转换
   * @param dbConn
   * @param netdisk
   * @throws Exception
   */
  public static List getnumber(){
    List  list =new ArrayList();
    list.add("登陆日志");
    list.add("登陆密码错误");
    list.add("添加部门");
    list.add("编辑部门");
    list.add("删除部门");
    list.add("添加用户");
    list.add("编辑用户");
    list.add("删除用户");
    list.add("非法IP登录");
    list.add("错误用户名");
    list.add("admin清空密码");
    list.add("系统资源回收");
    list.add("考勤数据管理");
    list.add("修改登录密码");
    list.add("公告通知管理");
    list.add("公共文件柜");
    list.add("网络硬盘");
    list.add("软件注册");
    list.add("用户批量设置");
    list.add("培训课程管理");
    list.add("用户KEY验证失败");
    list.add("退出系统");
    list.add("员工离职");
    return list;
  }  
  
 public static Map<Integer,String> getNumber(){
     // List <Map> list =new ArrayList();
     Map map = new HashMap();       
     map.put(1, "登陆日志");
     map.put(2, "登陆密码错误");
     map.put(3, "添加部门");
     map.put(4, "编辑部门");
     map.put(5, "删除部门");
     map.put(6, "添加用户");
     map.put(7, "编辑用户");
     map.put(8, "删除用户");
     map.put(9, "非法IP登录");
     map.put(10, "错误用户名");
     map.put(11, "admin清空密码");
     map.put(12, "系统资源回收");
     map.put(13, "考勤数据管理");
     map.put(14, "修改登录密码");
     map.put(15, "公告通知管理");
     map.put(16, "公共文件柜");
     map.put(17, "网络硬盘");
     map.put(18, "软件注册");
     map.put(19, "用户批量设置");
     map.put(20, "培训课程管理");
     map.put(21, "用户KEY验证失败");
     map.put(22, "退出系统");
     map.put(23, "员工离职");
     //list.add(map);
     return map;
 } 
 /*
  * 第二种 转换23中类型
   Map<Integer,String> total = new HashMap();
   total  = getNumber();
   //System.out.println(total);
   String va =  total.get(Integer.parseInt(types));
   //System.out.println(va);
                   第三种 转换23中类型  list 存放是有顺序的 ，存放的是0-22 种， type等于几 就获得集合中数据
   List  list2 = getnumber();
   list2.get(Integer.parseInt(types)-1);
   //System.out.println(list2);      
 */    
  public static Map<Integer, String> getMap(){
     Map map = new HashMap();       
     map.put(1, "登陆日志");
     map.put(2, "登陆密码错误");
     map.put(3, "添加部门");
     map.put(4, "编辑部门");
     map.put(5, "删除部门");
     map.put(6, "添加用户");
     map.put(7, "编辑用户");
     map.put(8, "删除用户");
     map.put(9, "非法IP登录");
     map.put(10, "错误用户名");
     map.put(11, "admin清空密码");
     map.put(12, "系统资源回收");
     map.put(13, "考勤数据管理");
     map.put(14, "修改登录密码");
     map.put(15, "公告通知管理");
     map.put(16, "公共文件柜");
     map.put(17, "网络硬盘");
     map.put(18, "软件注册");
     map.put(19, "用户批量设置");
     map.put(20, "培训课程管理");
     map.put(21, "用户KEY验证失败");
     map.put(22, "退出系统");
     map.put(23, "员工离职");
     return map;
 }     
  /**
   * 获得Map 中的Key
   * @param dbConn
   * @param netdisk
   * @throws Exception
   */
  public String getValue(int key){
    return getMap().get(key);
  }
     
  public static void main(String[] args){
    YHExportlogLogic logic = new YHExportlogLogic();
    String v =  logic.getValue(21);
    //System.out.print(v);
  }
}

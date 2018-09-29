package yh.core.funcs.calendar.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import yh.core.data.YHDbRecord;
import yh.core.funcs.calendar.data.YHAffair;
import yh.core.funcs.calendar.data.YHCalendar;
import yh.core.funcs.calendar.data.YHTask;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHCalExpImpLogic {
  private static Logger log = Logger.getLogger(YHCalExpImpLogic.class);
  public  ArrayList<YHDbRecord>  getCVS(String sql,ArrayList<YHDbRecord> cvs,Connection dbConn,String type) throws Exception {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      pstmt = dbConn.prepareStatement(sql);
      ResultSetMetaData rsmd = null;
      rs = pstmt.executeQuery();
      //rsmd = pstmt.getMetaData();
      //rsmd.getColumnCount();//得到多少列
      if(type!=null&&type.equals("CALENDAR")){
        while(rs.next()){
          YHDbRecord rd = new YHDbRecord();
          rd.addField("主题", rs.getString("CONTENT"));
          if(!YHUtility.isNullorEmpty(rs.getString("CAL_TIME"))){
            rd.addField("开始日期", rs.getString("CAL_TIME").substring(0, 19));
          }else{
            rd.addField("开始日期", "");
          }
          if(!YHUtility.isNullorEmpty(rs.getString("END_TIME"))){
            rd.addField("结束日期", rs.getString("END_TIME").substring(0, 19));
          }else{
            rd.addField("结束日期","");
          }

          rd.addField("类型", rs.getString("CAL_TYPE"));
          rd.addField("优先级", rs.getString("CAL_LEVEL"));
          rd.addField("安排人", rs.getString("MANAGER_ID"));
          rd.addField("说明", rs.getString("CONTENT"));
          rd.addField("提醒日期", "");
          rd.addField("提醒时间", "");
          rd.addField("完成日期", "");
          rd.addField("完成百分比", "");
          rd.addField("全部工作", "");
          rd.addField("实际工作", "");
          //System.out.println("dddddddddd");
          cvs.add(rd);
          //rd.addField("状态", rs.getString("OVER_STATUS"));
        }
      }
      if(type!=null&&type.equals("AFFAIR")){
        while(rs.next()){
          YHDbRecord rd = new YHDbRecord();
          rd.addField("主题", rs.getString("CONTENT"));
          if(!YHUtility.isNullorEmpty(rs.getString("BEGIN_TIME"))){
            rd.addField("开始日期", rs.getString("BEGIN_TIME").substring(0, 19));
          }else{
            rd.addField("开始日期", "");
          }
          if(!YHUtility.isNullorEmpty(rs.getString("END_TIME"))){
            rd.addField("结束日期", rs.getString("END_TIME").substring(0, 19));
          }else{
            rd.addField("结束日期", "");
          }
          rd.addField("类型", rs.getString("TYPE"));
          rd.addField("优先级", "");
          rd.addField("安排人", rs.getString("MANAGER_ID"));
          rd.addField("说明", rs.getString("CONTENT"));
          rd.addField("提醒日期", rs.getString("REMIND_DATE"));
          rd.addField("提醒时间", rs.getString("REMIND_TIME"));
          rd.addField("完成日期", "");
          rd.addField("完成百分比", "");
          rd.addField("全部工作", "");
          rd.addField("实际工作", "");
          cvs.add(rd);
          //rd.addField("状态", rs.getString("OVER_STATUS"));
        }
      }
      if(type!=null&&type.equals("TASK")){
        while(rs.next()){
          YHDbRecord rd = new YHDbRecord();
          rd.addField("主题", rs.getString("SUBJECT"));
          if(!YHUtility.isNullorEmpty(rs.getString("BEGIN_DATE"))){
            rd.addField("开始日期", rs.getString("BEGIN_DATE").substring(0, 19));
          }else{
            rd.addField("开始日期", "");
          }
          if(!YHUtility.isNullorEmpty(rs.getString("END_DATE"))){
            rd.addField("结束日期", rs.getString("END_DATE").substring(0, 19));
          }else{
            rd.addField("结束日期", "");
          }
          rd.addField("类型", rs.getString("TASK_TYPE"));
          rd.addField("优先级", rs.getString("IMPORTANT"));
          rd.addField("安排人", rs.getString("MANAGER_ID"));
          rd.addField("说明", rs.getString("CONTENT"));
          rd.addField("提醒日期", "");
          rd.addField("提醒时间", "");
          if(!YHUtility.isNullorEmpty(rs.getString("FINISH_TIME"))){
            rd.addField("完成日期", rs.getString("FINISH_TIME").substring(0, 19));
          }else{
            rd.addField("结完成日期", "");
          }
          rd.addField("完成百分比", rs.getString("RATE"));
          rd.addField("全部工作", rs.getString("TOTAL_TIME"));
          rd.addField("实际工作", rs.getString("USE_TIME"));

          cvs.add(rd);
          //rd.addField("状态", rs.getString("OVER_STATUS"));
        }
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
     YHDBUtility.close(pstmt, rs, log);
    }
    return cvs;
  }
  public List<Map<String,String>> parserXml(String fileName) throws Exception {   
    File inputXml=new File(fileName);   
    SAXReader saxReader = new SAXReader();   
    List<Map<String,String>> listXml = new ArrayList<Map<String,String>>();
    try {   
       Document document = saxReader.read(inputXml);   
       Element employees=document.getRootElement();  
       //System.out.println(employees.getName()+":"+employees.getText());
       
       for(Iterator i = employees.elementIterator(); i.hasNext();){   
            Element employee = (Element) i.next();   
            //System.out.println(employee.getName()+":"+employee.getText());
            Map<String,String> map = new HashMap<String,String>();
            for(Iterator j = employee.elementIterator(); j.hasNext();){   
                Element node=(Element) j.next();   
                //System.out.println(node.getName()+":"+node.getText());  
              
                map.put(node.getName(), node.getText());
            
            }   
            listXml.add(map);
       }   
   } catch (DocumentException e) {   
       //System.out.println(e.getMessage());   
   }
   //System.out.println("dom4j parserXml");   
   return listXml;

  }   
  public void addCalAffarTask(Connection dbConn,Set set,List<Map<String,String>> list)throws Exception {
    PreparedStatement pstmt = null;
    
    ResultSet rs = null;
    YHCalendarLogic cl = new YHCalendarLogic();
    YHAffairLogic al = new YHAffairLogic();
    YHTaskLogic tl = new YHTaskLogic();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    if(set!=null&&!set.isEmpty()){
      Iterator it = set.iterator();
      while(it.hasNext()){
        String userId = (String) it.next();
        for (int i = 0; i < list.size(); i++) {
          Map map = list.get(i);
          //System.out.println(map.get("FLAG"));
          if(map.get("FLAG")!=null&&map.get("FLAG").equals("CALENDAR")){
            YHCalendar calendar = new YHCalendar();
            calendar.setUserId(userId);
              if(YHUtility.isDayTime((String)map.get("CAL_TIME"))){
                calendar.setCalTime(dateFormat.parse((String)map.get("CAL_TIME")));
              }else{
                continue;
              }
         
              if(YHUtility.isDayTime((String)map.get("END_TIME"))){
                calendar.setEndTime(dateFormat.parse((String)map.get("END_TIME")));
              }else{
                continue;
              }


              if(YHUtility.isInteger((String)map.get("CAL_TYPE"))){
                calendar.setCalType((String)map.get("CAL_TYPE"));
              }else{
                continue;
              }


              if(YHUtility.isInteger((String)map.get("CAL_LEVEL"))){
                calendar.setCalLevel((String)map.get("CAL_LEVEL"));
              }else{
                continue;
              }

            if(map.get("CONTENT")!=null){
               calendar.setContent((String)map.get("CONTENT"));
            } 
            if(map.get("MANAGER_ID")!=null&&!map.get("MANAGER_ID").equals("")){
              if(YHUtility.isInteger((String)map.get("MANAGER_ID"))){
                calendar.setManagerId((String)map.get("MANAGER_ID"));
              }else{
                continue;
              }
            }else{
              calendar.setManagerId("");
            }
            cl.addCalendar(dbConn, calendar);
          }
          if(map.get("FLAG")!=null&&map.get("FLAG").equals("AFFAIR")){
            YHAffair affair = new YHAffair();
            affair.setUserId(userId);
              if(YHUtility.isDayTime((String)map.get("BEGIN_TIME"))){
                affair.setBeginTime(dateFormat.parse((String)map.get("BEGIN_TIME")));
              }else{
                continue;
              }
              if(YHUtility.isDayTime((String)map.get("END_TIME"))){
                affair.setEndTime(dateFormat.parse((String)map.get("END_TIME")));
              }
              if(YHUtility.isInteger((String)map.get("TYPE"))){
                affair.setType((String)map.get("TYPE"));
              }else{
                continue;
              }
            if(map.get("MANAGER_ID")!=null&&!map.get("MANAGER_ID").equals("")){
              if(YHUtility.isInteger((String)map.get("MANAGER_ID"))){
                affair.setManagerId((String)map.get("MANAGER_ID"));
              }else{
                continue;
              }
            }else{
              affair.setManagerId("");
            }
            if(map.get("REMIND_DATE")!=null){
              affair.setRemindDate((String)map.get("REMIND_DATE"));
            }
            if(map.get("REMIND_TIME")!=null){
              affair.setRemindTime((String)map.get("REMIND_TIME"));
            } else{
              continue;
            }
              if(YHUtility.isDayTime((String)map.get("LAST_REMIND"))){
                affair.setLastRemind(dateFormat.parse((String)map.get("LAST_REMIND")));
              }else{
                //continue;
              }
              if(map.get("LAST_SMS2_REMIND")!=null&&!((String)map.get("LAST_SMS2_REMIND")).equals("")){
                if(YHUtility.isDayTime((String)map.get("LAST_SMS2_REMIND"))){
                  affair.setLastSms2Remind(dateFormat.parse((String)map.get("LAST_SMS2_REMIND")));
                }else{
                  continue;
                }
              }
              if(!YHUtility.isNullorEmpty((String)map.get("CONTENT"))){
                affair.setContent((String)map.get("CONTENT"));
              }
             
            al.addAffair(dbConn, affair);
          }
          if(map.get("FLAG")!=null&&map.get("FLAG").equals("TASK")){
           YHTask task = new YHTask();
           task.setUserId(userId);
             if(YHUtility.isDayTime((String)map.get("BEGIN_DATE"))){
               task.setBeginDate(dateFormat.parse((String)map.get("BEGIN_DATE")));
             }else{
               continue;
             }
             if(YHUtility.isDayTime((String)map.get("END_DATE"))){
               task.setEndDate(dateFormat.parse((String)map.get("END_DATE")));
             }
             if(YHUtility.isInteger((String)map.get("TASK_TYPE"))){
               task.setTaskType((String)map.get("TASK_TYPE"));
             }else{
               continue;
             }
             if(YHUtility.isInteger((String)map.get("TASK_STATUS"))){
               task.setTaskStatus((String)map.get("TASK_STATUS"));
             }
             if(YHUtility.isInteger((String)map.get("COLOR"))){
               task.setColor((String)map.get("COLOR"));
             }else{
               continue;
             }
             if(YHUtility.isInteger((String)map.get("IMPORTANT"))){
               task.setImportant((String)map.get("IMPORTANT"));
             }else{
               continue;
             }
           if(map.get("RATE")!=null&&!map.get("RATE").equals("")){
             if(YHUtility.isInteger((String)map.get("RATE"))){
               task.setRate((String)map.get("RATE"));
             }else{
               continue;
             }
           }else{
             task.setRate("0");
           }
           if(map.get("TOTAL_TIME")!=null&&!map.get("TOTAL_TIME").equals("")){
             if(YHUtility.isInteger((String)map.get("TOTAL_TIME"))){
               task.setTotalTime((String)map.get("TOTAL_TIME"));
             }else{
               continue;
             }
           }else{
             task.setTotalTime("0");
           }
           if(map.get("USE_TIME")!=null&&!map.get("USE_TIME").equals("")){
             if(YHUtility.isInteger((String)map.get("USE_TIME"))){
               task.setUseTime((String)map.get("USE_TIME"));
             }else{
               continue;
             }
           }else{
             task.setUseTime("0");
           }
           if(map.get("MANAGER_ID")!=null&&!map.get("MANAGER_ID").equals("")){
             if(YHUtility.isInteger((String)map.get("MANAGER_ID"))){
               task.setManagerId((String)map.get("MANAGER_ID"));
             }else{
               continue;
             }
           }else{
             task.setManagerId("");
           }
           if(map.get("CAL_ID")!=null&&!map.get("CAL_ID").equals("")){
             if(YHUtility.isInteger((String)map.get("CAL_ID"))){
               task.setCalId(Integer.parseInt((String)map.get("CAL_ID")));
             }else{
               continue;
             }
           }else{
             task.setCalId(0);
           }
           if(map.get("CONTENT")!=null){
             task.setContent((String)map.get("CONTENT"));
           }
           if(map.get("SUBJECT")!=null){
             task.setSubject((String)map.get("SUBJECT"));
           }
           if(map.get("FINISH_TIME")!=null&&!((String)map.get("FINISH_TIME")).equals("")){
             if(YHUtility.isDayTime((String)map.get("FINISH_TIME"))){
               task.setFinishTime(dateFormat.parse((String)map.get("FINISH_TIME")));
             }else{
               continue;
             }
           }
           if(map.get("EDIT_TIME")!=null&&!((String)map.get("EDIT_TIME")).equals("")){
             if(YHUtility.isDayTime((String)map.get("EDIT_TIME"))){
               task.setEditTime(dateFormat.parse((String)map.get("EDIT_TIME")));
             }else{
               continue;
             }
           }
          
           tl.addTask(dbConn, task);
          }
        }
      }
    }
  }
  
  public void addCalAffarTaskCVS(Connection dbConn,Set set,ArrayList<YHDbRecord> list,String catType)throws Exception {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    YHCalendarLogic cl = new YHCalendarLogic();
    YHAffairLogic al = new YHAffairLogic();
    YHTaskLogic tl = new YHTaskLogic();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    if(set!=null&&!set.isEmpty()){
      Iterator it = set.iterator();
      while(it.hasNext()){
        String userId = (String) it.next();
        for (int i = 0; i < list.size(); i++) {
          YHDbRecord rd = list.get(i);
          if(catType!=null&&catType.equals("CALENDAR")){
            YHCalendar calendar = new YHCalendar();
            calendar.setUserId(userId);
              if(YHUtility.isDayTime((String)rd.getValueByName("开始日期"))){
                calendar.setCalTime(dateFormat.parse((String)rd.getValueByName("开始日期")));
              }else{
                continue;
              }
              if(YHUtility.isDayTime((String)rd.getValueByName("结束日期"))){
                calendar.setEndTime(dateFormat.parse((String)rd.getValueByName("开始日期")));
              }else{
                continue;
              }

              if(YHUtility.isInteger((String)rd.getValueByName("类型"))){
                calendar.setCalType((String)rd.getValueByName("类型"));
              }else{
                continue;
              }

              if(YHUtility.isInteger((String)rd.getValueByName("优先级"))){
                calendar.setCalLevel(((String)rd.getValueByName("优先级")));
              }else{
                continue;
              }

            if(rd.getValueByName("说明")!=null){
               calendar.setContent(""+(String)rd.getValueByName("说明"));
            }else{
              calendar.setContent("");
            } 
            if(rd.getValueByName("安排人")!=null&&!rd.getValueByName("安排人").equals("")){
              if(YHUtility.isInteger((String)rd.getValueByName("安排人"))){
                calendar.setManagerId((String)rd.getValueByName("安排人"));
              }else{
                continue;
              }
            }else{
              calendar.setManagerId("");
            }
            cl.addCalendar(dbConn, calendar);
          }
          if(catType!=null&&catType.equals("AFFAIR")){
            YHAffair affair = new YHAffair();
            affair.setUserId(userId);
              if(YHUtility.isDayTime((String)rd.getValueByName("开始日期"))){
                affair.setBeginTime(dateFormat.parse((String)(String)rd.getValueByName("开始日期")));
              }else{
                continue;
              }
              
              if (rd.getValueByName("说明")!=null) {
                affair.setContent(""+(String)rd.getValueByName("说明"));
              } else {
                affair.setContent("");
              }
              
              if (rd.getValueByName("提醒时间")!=null) {
                affair.setRemindTime(""+(String)rd.getValueByName("提醒时间"));
              } else {
                affair.setRemindTime("");
              }
              
              if(YHUtility.isDayTime((String)rd.getValueByName("结束日期"))){
                affair.setEndTime(dateFormat.parse((String)rd.getValueByName("结束日期")));
              }
              if(YHUtility.isInteger((String)rd.getValueByName("类型"))){
                affair.setType((String)rd.getValueByName("类型"));
              }else{
                continue;
              }
            if(rd.getValueByName("安排人")!=null&&!rd.getValueByName("安排人").equals("")){
              if(YHUtility.isInteger((String)rd.getValueByName("安排人"))){
                affair.setManagerId((String)rd.getValueByName("安排人"));
              }else{
                continue;
              }
            }else{
              affair.setManagerId("");
            }
            if(rd.getValueByName("提醒日期")!=null){
              affair.setRemindDate((String)rd.getValueByName("提醒日期"));
            }
            if(rd.getValueByName("提醒日期")!=null&&!((String)rd.getValueByName("提醒日期")).equals("")){
              affair.setRemindTime((String)rd.getValueByName("提醒日期"));
            } else{
             
            }
            al.addAffair(dbConn, affair);
          }
          if(catType!=null&&catType.equals("TASK")){
           YHTask task = new YHTask();
           task.setUserId(userId);
             if(YHUtility.isDayTime((String)rd.getValueByName("开始日期"))){
               task.setBeginDate(dateFormat.parse((String)rd.getValueByName("开始日期")));
             }else{
               continue;
             }
             if(YHUtility.isDayTime((String)rd.getValueByName("结束日期"))){
               task.setEndDate(dateFormat.parse((String)rd.getValueByName("结束日期")));
             }
             if(YHUtility.isInteger((String)rd.getValueByName("类型"))){
               task.setTaskType((String)rd.getValueByName("类型"));
             }else{
               continue;
             }
               task.setColor("0");
             if(YHUtility.isInteger((String)rd.getValueByName("优先级"))){
               task.setImportant((String)rd.getValueByName("优先级"));
             }else{
               continue;
             }
           if(rd.getValueByName("完成百分比")!=null&&!((String)rd.getValueByName("完成百分比")).equals("")){
             if(YHUtility.isInteger((String)rd.getValueByName("完成百分比"))){
               task.setRate((String)rd.getValueByName("完成百分比"));
             }else{
               continue;
             }
           }else{
             task.setRate("0");
           }
           if(rd.getValueByName("全部工作")!=null&&!((String)rd.getValueByName("全部工作")).equals("")){
             if(YHUtility.isInteger((String)rd.getValueByName("全部工作"))){
               task.setTotalTime((String)rd.getValueByName("全部工作"));
             }else{
               continue;
             }
           }else{
             task.setTotalTime("0");
           }
           if(rd.getValueByName("实际工作")!=null&&!((String)rd.getValueByName("实际工作")).equals("")){
             if(YHUtility.isInteger((String)rd.getValueByName("实际工作"))){
               task.setUseTime((String)rd.getValueByName("实际工作"));
             }else{
               continue;
             }
           }else{
             task.setUseTime("0");
           }
           if(rd.getValueByName("安排人")!=null&&!((String)rd.getValueByName("安排人")).equals("")){
             if(YHUtility.isInteger((String)rd.getValueByName("安排人"))){
               task.setManagerId((String)rd.getValueByName("安排人"));
             }else{
               continue;
             }
           }else{
             task.setManagerId("");
           }
           if(rd.getValueByName("说明")!=null){
             task.setContent((String)rd.getValueByName("说明"));
           }
           if(rd.getValueByName("主题")!=null){
             task.setSubject((String)rd.getValueByName("主题"));
           }

           tl.addTask(dbConn, task);
          }
        }
      }
    }
  }
}

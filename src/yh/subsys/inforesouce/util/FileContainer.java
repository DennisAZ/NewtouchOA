package yh.subsys.inforesouce.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import yh.subsys.inforesouce.data.TempFile;
public class FileContainer{
  /**
   * 假数据  
   * @return
   * @throws ParseException 
   */
  public static List<TempFile>  dBcontain()throws ParseException{
    /*{listData:[{namekey:"陈可超",lcbzname:"督查督办",lcvalue:"0(1)"},
               * {namekey:"系统管理员",lcbzname:"督查督办",lcvalue:"0(2)"},
               * {namekey:"秘书3",lcbzname:"督查督办",lcvalue:"0(5)"},
               * {namekey:"领导2",lcbzname:"收文",lcvalue:"0(5)"},
               * {namekey:"领导3",lcbzname:"收文",lcvalue:"0(1)"},
               * {namekey:"系统管理员",lcbzname:"收文",lcvalue:"0(1)"},
               * {namekey:"陈懿",lcbzname:"收文",lcvalue:"0(2)"},
               * {namekey:"吴勇文",lcbzname:"收文",lcvalue:"0(1)"}]}*/
  List<TempFile> list = new ArrayList();
  String size  ="120.326";
  String size1 ="130";
  String size2 ="140";
  String size3 ="150";
  String size4 ="160";
  String size5 ="170";
  String size6 ="180";
  String size7 ="190";
  double big = Double.parseDouble(size);
  double big1 = Double.parseDouble(size1);
  double big2 = Double.parseDouble(size2);
  double big3 = Double.parseDouble(size3);
  double big4 = Double.parseDouble(size4);
  double big5 = Double.parseDouble(size5);
  double big6 = Double.parseDouble(size6);
  double big7 = Double.parseDouble(size7);
  String date ="2010-07-01";
  DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
  Date date1 = sdf.parse(date);
 // date1.toLocaleString();
  TempFile file = new TempFile();
  TempFile file1 = new TempFile();
  TempFile file2 = new TempFile();
  TempFile file3 = new TempFile();
  TempFile file4 = new TempFile();
  TempFile file5 = new TempFile();
  TempFile file6 = new TempFile();
  TempFile file7 = new TempFile();
  /*String filename [] = {"name1","name2","name3"}; 
  
  for(int i=0; i<filename.length; i++){
    TempFile file6 = new TempFile();
    file6.setSeqId(1);
    file6.setName(filename[i]);
    file6.setSize(big);
    file6.setType("来源");
    file6.setChangeDate(date1);
    file6.setDept("开发部");
  }*/  
  file.setSeqId(1);
  file.setName("网络硬盘");
  file.setSize(big);
  file.setType("来源");
  file.setChangeDate(date1);
  file.setDept("开发部");
  file.setOper("阅读");
  
  file1.setSeqId(2);
  file1.setName("工作流");
  file1.setSize(big1);
  file1.setType("档号");
  file1.setChangeDate(date1);
  file1.setDept("系统部");
  file1.setOper("阅读");
  
  file2.setSeqId(3);
  file2.setName("日志管理");
  file2.setSize(big2);
  file2.setType("电子属性");
  file2.setChangeDate(date1);
  file2.setDept("业务部");
  file2.setOper("阅读");
  
  file3.setSeqId(4);
  file3.setName("登陆管理");
  file3.setSize(big3);
  file3.setType("电子签名");
  file3.setChangeDate(date1);
  file3.setDept("销售部");
  file3.setOper("阅读");
  
  file4.setSeqId(5);
  file4.setName("新闻管理");
  file4.setSize(big4);
  file4.setType("业务状态");
  file4.setChangeDate(date1);
  file4.setDept("运营部");
  file4.setOper("阅读");
  
  file5.setSeqId(6);
  file5.setName("短信管理");
  file5.setSize(big5);
  file5.setType("业务行为");
  file5.setChangeDate(date1);
  file5.setDept("研发部");
  file5.setOper("阅读");
  
  file6.setSeqId(7);
  file6.setName("工作流");
  file6.setSize(big6);
  file6.setType("关系类型");
  file6.setChangeDate(date1);
  file6.setDept("市场部");
  file6.setOper("阅读");
  
  file7.setSeqId(8);
  file7.setName("工作流");
  file7.setSize(big7);
  file7.setType("内容描述");
  file7.setChangeDate(date1);
  file7.setDept("综合部");
  file7.setOper("阅读");
  list.add(file);
  list.add(file1);
  list.add(file2);
  list.add(file3);
  list.add(file4);
  list.add(file5);
  list.add(file6);
  list.add(file7);
  return list;
  }

  /**
   查询集合中共有多少条数据 
   */
  public static List<TempFile>  countNumber()throws ParseException{
    List<TempFile> list = dBcontain();
    return list;
  }
  
  /**
   * 查找匹配的搜索信息
   * @return
   * @throws ParseException
   */
  public static List<TempFile> findDate(String finddate)throws ParseException{
   // System.out.println(finddate+"bbbbbbbbbbbb");
    int countnum = 0;
    List<TempFile> list = dBcontain();
    List <TempFile> li = new ArrayList(); 
    if(finddate!=null){
    for (TempFile f : list){
      String name = f.getName();
      if (name != null && name.equalsIgnoreCase(finddate)){
        f.getName();         
        f.getSize();
        f.getType();
        f.getChangeDate();
        f.getDept();
        f.getOper();
        li.add(f);
       }
    }
   } 
    return li;
  }
 /**
  * 查询 搜索内容共有多少条
  */
  public static List<TempFile> countDate(String finddate)throws ParseException{
    //System.out.println(finddate==null+"cccccccccc");
    //System.out.println(finddate.equals("")+"dddddddddd");
    int countnum = 0;
    List<TempFile> list = dBcontain();
    List <TempFile> li = new ArrayList(); 
    if(!"".equals(finddate)&& !finddate.equals("") && finddate!=null){
    for (TempFile f : list){
      String name = f.getName();
      if (name != null && name.equalsIgnoreCase(finddate)){
        f.getName();         
        f.getSize();
        f.getType();
        f.getChangeDate();
        f.getDept();
        f.getOper();
        li.add(f);
       }
    }
    
   } 
    return li;
  }  
  
 /**
  * 分页搜索 
  * @param args
  * @throws ParseException
  */
  public static List<TempFile> findpage(String finddate,int num)throws ParseException{
    List<TempFile> lis =  findDate(finddate);
    List<TempFile> list = dBcontain();
    List <TempFile> li = new ArrayList(); 
    if(finddate!=null && !finddate.equals("")){
      for(int i= (num-1)*2; i<=(num-1)*2+1&&i<lis.size(); i++){
       // System.out.println(lis.get(i)+"ddddddddd");
          li.add(lis.get(i));
      }
      return  li;
    }else{
    for(int i= (num-1)*2; i<=(num-1)*2+1; i++){
    li.add(list.get(i));
    }
    return li;
   }
  }
/**
 * 向上翻页方法  
 * @param args
 * @throws ParseException
 */
public static List<TempFile> pageUp(){
 
  return null;
}
/**
 * 鼠标经过排序按分页要求排序
 * @param num
 * @return
 * @throws ParseException
 */
public static List<TempFile> alignOder1(int num)throws ParseException{
  List<TempFile> list =dBcontain();
  TempFile[] files = new TempFile[list.size()];
   int len = files.length;
  for(int i=0; i<list.size(); i++){
    files[i] = list.get(i);    
  }
  List <TempFile> li = new ArrayList();
  TempFile temp=null;
  for(int i = 0; i<files.length; i++){    
    //System.out.println(files[i]);
    for(int j=i+1; j<files.length; j++){      
      if(files[i].getSize() < files[j].getSize()){ 
        temp = files[i];
        files[i] = files[j];
        files[j] = temp;             
      }
    }   
  }  
  for(int i = (num-1)*2; i<=(num-1)*2+1; i++) {
    li.add(files[i]);
  }
  return li;
}
/**
 * 鼠标划上排列顺序 冒泡法思想
 * @param finddate
 * @param num
 * @return
 * @throws ParseException
 */
public static List<TempFile> alignOder(String finddate,int num)throws ParseException{
  List<TempFile> list =dBcontain();
  TempFile[] files = new TempFile[list.size()];
   int len = files.length;
  for(int i=0; i<list.size(); i++){
    files[i] = list.get(i);    
  }
  List <TempFile> li = new ArrayList();
  TempFile temp=null;
  for(int i = 0; i<files.length; i++){    
    //System.out.println(files[i]);
    for(int j=i+1; j<files.length; j++){      
      if(files[i].getSize() < files[j].getSize()){ 
        temp = files[i];
        files[i] = files[j];
        files[j] = temp;             
      }
    }   
  }  

  for(int i = 0 ; i < files.length ; i ++) {
   li.add(files[i]);
   }
  return li;
}

public static void main(String[] args) throws ParseException {
  String sear="hh";
  int siz =1;
 List<TempFile> tf  = alignOder1(siz);
 for(int i=0; i<tf.size(); i++){
    //System.out.println(tf.get(i)+"*******");
 }
     /* List<TempFile> list= dBcontain();
      for(int i= 0; i<list.size(); i++){
       //System.out.println( list.get(i).getName());
       //System.out.println( list.get(i).getSize());
       //System.out.println( list.get(i).getType());
       Date da= list.get(i).getChangeDate();
       String time = da.toLocaleString();
       //System.out.println(time);
       //System.out.println(list.get(i).getDept());
      }*/
//冒泡排序
  int array[] = {-5,-9,2,5,10,7,895};
  for(int i = 0 ; i < array.length ; i ++) {
   for(int j = i +1 ; j < array.length ; j ++) {
    if(array[i] < array[j]) {
     int temp = array[i];
     array[i] = array[j];
     array[j] = temp;
    }
   }
  }
  for(int i = 0 ; i < array.length ; i ++) {
   //System.out.print(" "+array[i]+" ");
  }
 } 
}


  


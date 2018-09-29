package yh.core.funcs.allreport.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import yh.core.data.YHDbRecord;
import yh.core.funcs.allreport.data.YHAllReport;
import yh.core.funcs.allreport.data.YHField;
import yh.core.funcs.allreport.data.YHJionCondition;
import yh.core.funcs.allreport.data.YHLoadConfigFile;
import yh.core.funcs.person.data.YHPerson;
//import yh.core.funcs.workflow.data.YHFlowReport;
import yh.core.global.YHSysProps;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHDataReportLogic {

	public String getReportListLogic(Connection conn,YHPerson user)  throws Exception{
		   Statement stmt=null;
		   ResultSet rs=null;
		   String data="";
		   try{
		     String sql="select seq_id ,module_name from all_report_type";
		     stmt=conn.createStatement();
		     rs=stmt.executeQuery(sql);
		     while(rs.next()){
		     int seq_id=rs.getInt("seq_id");
		     String module_name=rs.getString("module_name");
		     String list="";
		     list=this.getListById(conn,seq_id);
		     data+="{seqId:'"+seq_id+"',moduleName:'"+module_name+"',list:["+list+"]}";
		     //data+="{seqId:'"+seq_id+"',moduleName:'"+module_name+"'}";
		     data+=",";
		     }
		   
		   }catch(Exception e){
		     throw e;
		   } finally{
		     YHDBUtility.close(stmt, rs, null);
		   }
		   if(data.endsWith(",")){
		     data=data.substring(0,data.length()-1);
		   }
		   return data;
		 }
	
	
	public String getListById(Connection conn,int seq_id)  throws Exception{
		   Statement stmt=null;
		   ResultSet rs=null;
		   String data="";
		   try{
		     String sql="select r.seq_id,r.r_name from all_report r,all_report_type t where r.module_id= "+seq_id;
		     stmt=conn.createStatement();
		     rs=stmt.executeQuery(sql);
		     while(rs.next()){
		       int id=rs.getInt("seq_id");
		       String name=rs.getString("r_name");
		       data+="{seqId:'"+id+"',name:'"+name+"'}";
		       data+=",";
		     }
		     
		   }catch(Exception e){
		     throw e;
		   }finally{
		     YHDBUtility.close(stmt, rs, null);
		   }
		   if(data.endsWith(",")){
		     data=data.substring(0,data.length()-1);
		   }
		   return data;
		   }
	
	public String getReportByRidLogic(Connection conn,String id,YHPerson person)  throws Exception{
    Statement stmt=null;
    ResultSet rs=null;
    String data="";
    String dept="";
    String userStr="";
    String query="";
    String dd="";
    try{
     String sql="select * from all_report_priv where r_id="+id+" and "+YHDBUtility.findInSet(person.getSeqId()+"", "user_str");

     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     while(rs.next()){
       dd=rs.getString("user_str");
      if(dd !=null && dd!="" )
        userStr = person.getUserId()+",";
       else{
         if(!userStr.endsWith(",")){
           userStr+=",";
         }
         userStr += dd;
       }
     }
     YHAllReport report=new YHAllReport();
     YHORM orm=new YHORM();
     report=(YHAllReport)orm.loadObjSingle(conn,YHAllReport.class,Integer.parseInt(id));
     query=report.getQueryItem();
    
     String item="";
     String name="";
     String data1="";
     if(!YHUtility.isNullorEmpty(query)){
     String org[]=query.split(",");
     for(int i=0;i<org.length;i++){
       item="";
       name="";
       if(!"".equals(org[i])){
         item=org[i].substring(0,org[i].indexOf("`"));
         name=org[i].substring(org[i].indexOf("`")+1,org[i].length());
       }
       String dataType=getItemDataType( conn,report.getModuleId()+"",item);
       data1+="{item:'"+item+"',name:'"+name+"',dataType:'"+dataType+"'}";
       data1+=",";
      }
     }
     if(data1.endsWith(",")){
       data1=data1.substring(0, data1.length()-1);
     }
     data="{rName:'"+report.getRName()+"',userStr:'"+userStr+"',query:["+data1+"]}";
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    return data;
  }
  
  public String getItemDataType(Connection conn,String mId,String item)throws Exception{
    Statement stmt=null;
    ResultSet rs=null;
    String dataType="";
    String data="";
    try{
      String sql="select * from all_report_type where seq_id='"+mId+"' ";
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      if(rs.next()){
        data=rs.getString("config");
      }
      
      String configFile=YHSysProps.getString("allreportconfig");
      configFile+=data;
      YHLoadConfigFile loadconfigfile=new YHLoadConfigFile(configFile);
      List<YHField> fieldList= loadconfigfile.getFieldList();
      for(int i=0;i<fieldList.size();i++){
        YHField field=fieldList.get(i);
        if(item.equals(field.getItem())){
          dataType=field.getDataType();
        }
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    return dataType;
  } 
	/**
	 * 查询
	 * @param conn
	 * @param user
	 * @param request
	 * @return
	 * @throws Exception
	 */
  public String getTableListLogic(Connection conn,YHPerson user,Map request)  throws Exception{
    Statement stmt=null;
    ResultSet rs=null;
    List<List<String>> dataList=new ArrayList();
    String data="";
    String query = request.get("query") == null ? null : ((String[]) request.get("query"))[0];
    String rid = request.get("rid") == null ? null : ((String[]) request.get("rid"))[0];

    try{
      String sql="select * from all_report where seq_id="+rid;
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      int mId=0;
      int rId=0;
      String list_item="";
      String query_item="";
      String group_type="";
      String group_field="";
      if(rs.next()){
        mId=rs.getInt("module_id");
        rId           = rs.getInt("seq_id");
        list_item     = rs.getString("list_item");
        query_item    = rs.getString("query_item");
        group_type    = rs.getString("group_type");
        group_field   = rs.getString("group_field");   
      }
      YHLoadConfigFile configFile=this.getFieldListbyMid( conn, mId+"");
      List<YHField> listFields=new ArrayList();
      List<YHJionCondition> listConditions=new ArrayList();
      listFields=configFile.getFieldList();
      listConditions=configFile.getConditionList();
      String itemsAndFromStr="";
      int itemsNum=0;
      String sqlItems="";
      String list_items=group_field+"`s`s`s`,"+list_item;
   //   String groupField= getGroupField(group_field,listFields);
      String selectItems[]= getSelectItems(list_items,listFields);
      if(selectItems.length!=0){
        itemsAndFromStr=selectItems[1];
        itemsNum=Integer.parseInt(selectItems[2]);
        sqlItems=selectItems[0];
      }
      
    //  itemsAndFromStr+=","+groupField.substring(0, groupField.indexOf("."));
      String jionCondition="";
      String dataStr="";
      jionCondition=getJionConditions(itemsAndFromStr,  listConditions);
      String queryStr=this.getQueryStr(listFields,request,query);
      
      if("0".equals(group_type)){
        sql=" select "+sqlItems+" where 1=1 "+jionCondition+queryStr+" group by " + group_field;
      }else if("1".equals(group_type)){
          sql=" select "+sqlItems+" where 1=1 "+jionCondition+queryStr;
        
      }
      
 
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      while(rs.next()){
        List itemList=new ArrayList();
        for(int i=0;i<itemsNum;i++){
          String dataItem=this.getItemData(conn,rs,i,itemsAndFromStr.substring(0, itemsAndFromStr.indexOf(" from ")-1),listFields);
          //int temp_int=itemList.indexOf(dataItem);
          itemList.add(dataItem);
       }
        dataList.add(itemList);
      }
      String ItemName=this.getTableHead(group_type,itemsAndFromStr.substring(0, itemsAndFromStr.indexOf(" from ")-1),listFields);   
      String calLine="";
      if("0".equals(group_type)){
     //  dataList=this.getGroupCalListLgic(list_item,dataList);
       List<String> calList= this.getCalLine(list_item,dataList);
       dataList.add(calList);
      }else if("1".equals(group_type)){
       dataList=this.getGroupListLogic(dataList);
      }
      dataStr=this.getTableStr( dataList ,group_type);
      
      data="<table border='0' width='85%' class='TableList'  align='center'>"+ItemName+dataStr+"</table>";
      data="{table:'"+YHUtility.encodeSpecial(data)+"'}";
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    return data;
  }
  
  /**
   * 
   * 导出EXCEL
   * 
   * */
  public List<List<String>> getExcelListLogic(Connection conn,YHPerson user,Map request)  throws Exception{
    Statement stmt=null;
    ResultSet rs=null;
    List<List<String>> dataList=new ArrayList();
    List<List<String>> dd=new ArrayList();
    String data="";
    String query = request.get("query") == null ? null : ((String[]) request.get("query"))[0];
    String rid = request.get("rid") == null ? null : ((String[]) request.get("rid"))[0];

    try{
      String sql="select * from all_report where seq_id="+rid;
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      int mId=0;
      int rId=0;
      String list_item="";
      String query_item="";
      String group_type="";
      String group_field="";
      if(rs.next()){
        mId=rs.getInt("module_id");
        rId           = rs.getInt("seq_id");
        list_item     = rs.getString("list_item");
        query_item    = rs.getString("query_item");
        group_type    = rs.getString("group_type");
        group_field   = rs.getString("group_field");   
      }
      YHLoadConfigFile configFile=this.getFieldListbyMid( conn, mId+"");
      List<YHField> listFields=new ArrayList();
      List<YHJionCondition> listConditions=new ArrayList();
      listFields=configFile.getFieldList();
      listConditions=configFile.getConditionList();
      String itemsAndFromStr="";
      int itemsNum=0;
      String sqlItems="";
      String list_items=group_field+"`s`s`s`,"+list_item;
   //   String groupField= getGroupField(group_field,listFields);
      String selectItems[]= getSelectItems(list_items,listFields);
      if(selectItems.length!=0){
        itemsAndFromStr=selectItems[1];
        itemsNum=Integer.parseInt(selectItems[2]);
        sqlItems=selectItems[0];
      }
      
    //  itemsAndFromStr+=","+groupField.substring(0, groupField.indexOf("."));
      String jionCondition="";
      String dataStr="";
      jionCondition=getJionConditions(itemsAndFromStr,  listConditions);
      String queryStr=this.getQueryStr(listFields,request,query);
      
      sql=" select "+sqlItems+" where 1=1 "+jionCondition+queryStr ;
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      while(rs.next()){
        List itemList=new ArrayList();
        for(int i=0;i<itemsNum;i++){
          String dataItem=this.getItemData(conn,rs,i,itemsAndFromStr.substring(0, itemsAndFromStr.indexOf(" from ")-1),listFields);
          itemList.add(dataItem);
       }
        dataList.add(itemList);
      }
      String ItemName=this.getTableHead(group_type,itemsAndFromStr.substring(0, itemsAndFromStr.indexOf(" from ")-1),listFields);   
      String calLine="";
      if("0".equals(group_type)){
     //  dataList=this.getGroupCalListLgic(list_item,dataList);
       List<String> calList= this.getCalLine(list_item,dataList);
       dataList.add(calList);
      }else if("1".equals(group_type)){
       dataList=this.getGroupListLogic(dataList);
      }
     
      List<String> title=new ArrayList();
      title = getExcTableHead(group_type,itemsAndFromStr.substring(0, itemsAndFromStr.indexOf(" from ")-1), listFields);
    //  System.out.println(title);
      dd.add(title);
      for(int i=0;i<dataList.size();i++){
      dd.add(dataList.get(i));
      }   
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    return dd;
  }
  /**
   * 导出EXCEL表  得到表头
   * @param list_item
   * @param dataList
   * @return
   * @throws Exception
   */
  public List<String> getExcTableHead(String type,String items,List<YHField> listFields)throws Exception{
    List<String> headTitle = new ArrayList();
    String headLine = "";
    String itemOrg[]=items.split(",");
    for(int i=0;i<itemOrg.length;i++){
      if(!"".equals(itemOrg[i])){
        if("0".equals(type) && i==1){
          headLine+="数量"; 
        } 
        YHField field=new YHField();
        String item="";
         if("0".equals(type)){
           field=listFields.get(0);
         item=itemOrg[i].substring(itemOrg[i].indexOf(".")+1, itemOrg[i].length());
          if(field.getItem().trim().equals(item.trim())){
           headLine+=field.getItemDesc(); 
         }
         }else if("1".equals(type)){
           for(int j=0;j<listFields.size();j++){
              field=listFields.get(j);
             item=itemOrg[i].substring(itemOrg[i].indexOf(".")+1, itemOrg[i].length());
             if(field.getItem().trim().equals(item.trim())){
              headLine=field.getItemDesc(); 
              headTitle.add(headLine);
             }
           }
           
         }

      }
      
    }
    
    return headTitle;
  }
  
  
  public List<List<String>> getGroupCalListLgic(String list_item,List<List<String>> dataList)throws Exception{
    List<List<String>> seqList=new ArrayList();
    dataList=this.getGroupListLogic(dataList);
    int num=0;
    List<String> newList;
    for(int i=0;i<dataList.size();i++){
      List<String> trlist=dataList.get(i);
     
      String groupName=trlist.get(0);
      num++;
      if(!YHUtility.isNullorEmpty(groupName)){
        num--;
        newList=new ArrayList();
        newList.add(groupName); 
        newList.add(num+"");
        num=1;
        seqList.add(newList);
      }
    }
    String lists[]=list_item.split(",");
    for(int i=0;i<lists.length;i++){
      if(!"".equals(lists[i])){
        String item[]=lists[i].split("`");
        String calcType="";
        if (item.length >= 3) {
          calcType=item[2];
        }
       
        if("0".equals(calcType)){
          seqList=getSUM(i+1,dataList,seqList);
        }else if("1".equals(calcType)){
         // sum=this.getAvgWeight(i,dataList);
        }else if("2".equals(calcType)){
        //  sum=this.getAvg(i,dataList);
        }else if("3".equals(calcType)){
       //   sum=this.getFormulate(i,dataList);
        } else {
      //    sum = -1;
        }
        
      }
    }
    return seqList;
  }
  
  public List<List<String>> getSUM(int index,List<List<String>> dataList,List<List<String>> seqList) throws Exception{
     int sum=0;
    for(int i=0;i<dataList.size();i++){
     List<String> trList=dataList.get(i);
     String name=trList.get(0);
     String value=trList.get(index);
     
     if(YHUtility.isNumber(value)){
       sum+=Integer.parseInt(value);
     }
     if(!YHUtility.isNullorEmpty(name)){
       
     }
     
      
    }
    return seqList;
  }
  
  public List<List<String>> getGroupListLogic(List<List<String>> dataList) throws Exception{
    List<List<String>> seqList=new ArrayList();
    for(int i=0;i<dataList.size();i++){
      List<String> itemList=dataList.get(i);
      String groupItem=itemList.get(0);
      
     if(!this.getIsExit(seqList, groupItem) && !"".equals(groupItem)){
      seqList.add(itemList);

      for(int j=i+1;j<dataList.size();j++){
        List<String> compList=dataList.get(j);
        String compItem=compList.get(0);
        if(groupItem.trim().equals(compItem.trim())){
        
           compList.set(0, "");
           seqList.add(compList);
         
       
        }
      }
     }
    }
    return seqList;
  }
  
  public boolean getIsExit(List<List<String>> seqList,String name){
    boolean result=false;
    for(int i=0;i<seqList.size();i++){
      List<String> rr=seqList.get(i);
      String Tname= rr.get(0);
      if(name.equals(Tname)){
        result=true;
        break;
      }
      
    }
    return result;
  }
  
  public String getTableStr(List<List<String>> dataList,String type)throws Exception{
    String tableStr="";
    for(int i=0;i<dataList.size()-1;i++){
      List<String> itemList=dataList.get(i);
      tableStr+="<tr>";
      for(int j=0;j<itemList.size();j++){
        String itemData=itemList.get(j);
        tableStr+="<td class='TableData'>"+itemData+"</td>";
      }
      tableStr+="</tr>";
    }
    
    if("0".equals(type)){
      List<String> lastList=dataList.get(dataList.size()-1);
      tableStr+="<tr>";
       tableStr+="<td class='TableContent'>合计：</td>";
      for(int j=1;j<lastList.size();j++){
        String itemData=lastList.get(j);
        tableStr+="<td class='TableData'>"+itemData+"</td>";
      }
      tableStr+="</tr>";
     
    }else{
      List<String> lastList=dataList.get(dataList.size()-1);
      for(int j=0;j<lastList.size();j++){
        String itemData=lastList.get(j);
        tableStr+="<td class='TableData'>"+itemData+"</td>";
      }
      tableStr+="</tr>";
    }
    
    
    return tableStr;
  }
  
  public List<String> getCalLine(String list_item,List<List<String>> dataList) throws Exception{
    List<String> itemList=new ArrayList();
    String data="";
    float sum=0;
    String calcType="";
    itemList.add("合计");
    try{
      String lists[]=list_item.split(",");
      for(int i=0;i<lists.length;i++){
        sum=0;
        if(!"".equals(lists[i])){
          String item[]=lists[i].split("`");
          calcType="";
          if (item.length >= 3) {
            calcType=item[2];
          }
          if("0".equals(calcType)){
            sum=this.getSum(i,dataList);
          }else if("1".equals(calcType)){
            sum=this.getAvgWeight(i,dataList);
          }else if("2".equals(calcType)){
            sum=this.getAvg(i,dataList);
          }else if("3".equals(calcType)){
            sum=this.getFormulate(i,dataList);
          } else {
            sum = -1;
          }
        }
        sum = (float)(Math.round(sum*100))/100; 

      //  data+="<td class='TableData'>"+(sum == -1 ? "" : sum )+"</td>";
        itemList.add(sum+"");
      }
      
     
    }catch(Exception e){
      throw e;
    }finally{
      //YHDBUtility.close(stmt, rs, null);
    }
    return itemList;
  }
  
  public float getSum(int index,List<List<String>> dataList) throws Exception{

    float sum=0;
    try{
      index+=1;
      for(int i=0;i<dataList.size();i++){
        List<String> itemList=dataList.get(i);
        String value=itemList.get(index);
        if(YHUtility.isNumber(value)){
          sum+=Integer.parseInt(value);
        }
      }
    }catch(Exception e){
      throw e;
    }finally{
    
    }
    return sum;
  }
  
  public float getAvgWeight(int index,List<List<String>> dataList) throws Exception{
    Statement stmt=null;
    ResultSet rs=null;
    float sumWeight=0;
    float sum=0;
    int run_id=0;
    
//    String Item=field.substring(0,field.indexOf("`"));
//    String weigth=field.substring(field.indexOf("`")+1,field.length());
    try{
//      stmt=conn.createStatement();
//      rs=stmt.executeQuery(query);
//      while(rs.next()){
//        run_id =rs.getInt("run_id");
//        if(this.getQueryStr(conn, run_id+"", rid, que, request)){
//        String itemData=this.getDataByFidAndItem(conn, run_id+"", Item);
//        String weigthData=this.getDataByFidAndItem(conn, run_id+"", weigth);
//        if(YHUtility.isNumber(itemData) && YHUtility.isNumber(weigthData)){
//          float Idata=Float.parseFloat(itemData);
//          float Wdata=Float.parseFloat(weigthData);
//          sumWeight+=Wdata;
//          if (sumWeight != 0 ) {
//            sum=((Idata * Wdata) + (sumWeight - Wdata) * sum)/sumWeight;
//          }
//         }
//        }
//      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    return sum;
  }
  

  public float getAvg(int index,List<List<String>> dataList) throws Exception{
    int num=0;
    float sum=0;
    try{
      index+=1;
      for(int i=0;i<dataList.size();i++){
        List<String> itemList=dataList.get(i);
        String value=itemList.get(index);
        if(YHUtility.isNumber(value)){
          sum+=Integer.parseInt(value);
        }
        num++;
      }
    }catch(Exception e){
      throw e;
    }finally{
    
    }
    if(num==0){
      num=1;
    }
    return sum/num;
  }
  public float getFormulate(int index,List<List<String>> dataList) throws Exception{
    Statement stmt=null;
    ResultSet rs=null;
    float sum=0;
    int run_id=0;
    LinkedList<String> titlelist=new LinkedList<String>();
    LinkedList<String> namelist=new LinkedList<String>();
    try{
//
//      String Item[]=field.split("`",-1);
//      String formulate=Item[3];
//      String sql="select title,name from flow_form_item where form_id in (select form_seq_id from flow_type where seq_id='"+flow_id+"')";
//      stmt=conn.createStatement();
//      rs=stmt.executeQuery(sql);
//      while(rs.next()){
//        String title=rs.getString("title");
//        String name=rs.getString("name");
//        titlelist.add(title);
//        namelist.add(name);
//      }
//      titlelist.add("流水号");
//      namelist.add("run_id");
//     rs=stmt.executeQuery(query);
//     while (rs.next()){
//       run_id=rs.getInt("run_id");
//       if(this.getQueryStr(conn, run_id+"", rid, que, request)){
//       Iterator iterTitle=titlelist.iterator();
//       Iterator iterName=namelist.iterator();
//       while(iterTitle.hasNext()){
//         String title=(String)iterTitle.next();
//         String name=(String)iterName.next();
//         String value=this.getDataByFidAndItem(conn, run_id+"", name);
//         if(!YHUtility.isNumber(value)){
//           value="0";
//         }
//         formulate=formulate.replace(title, value);
//         
//       }
//        sum+=this.getFormulateResult(formulate);
//       }
//     }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    
    return sum;
  }
  
  public String getItemData(Connection conn,ResultSet rs,int index,String itemStr,List<YHField> listField)throws Exception{
    String itemData="";
    String itemOrg[]=itemStr.split(",");
    String item=itemOrg[index];
    for(int i=0;i<listField.size();i++){
      YHField field=listField.get(i);
      if("1".equals(item))
      {
        item="*";
      }
      else
      {
      item=item.substring(item.indexOf(".")+1, item.length());
      }
      if(item.trim().equals(field.getItem().trim())){

        if("1".equals(field.getJavaType())){
          itemData=rs.getInt("item"+index)+"";
        }else if("0".equals(field.getJavaType())){
          itemData=rs.getString("item"+index)+"";
        }
        
        if(!"".equals(field.getSelectItem())){
           if("self".equals(field.getSelectItem())){
             String selectData="";
             selectData= field.getSelectData();
             String select[]=selectData.split(",");
             for(int j=0;j<select.length;j++){
               String itemsStr="";
               itemsStr=select[j];
               String itemId=itemsStr.substring(0,itemsStr.indexOf(":"));
               if(itemId.equals(itemData+"")){
                 itemData=itemsStr.substring(itemsStr.indexOf(":")+1,itemsStr.length());
               }
             }
           }else{
             String selectItem="";
             selectItem=field.getSelectItem();
             try{
               String sql="select class_desc from oa_kind_dict_item where class_no='"+selectItem+"' and sort_no='"+itemData+"' ";
               Statement stmt=conn.createStatement();
               ResultSet rs1=stmt.executeQuery(sql);
               if(rs1.next()){
                 itemData=rs1.getString("class_desc");
               }
                YHDBUtility.close(stmt, rs1, null);
             }catch(Exception ex){
               ex.printStackTrace();
             }finally{
            
             }
             
           }
          
          
          
          
        }
        
        
        if("1".equals(field.getForignKey())){
        itemData=dealForignKey(conn,field,itemData);
        }
        
        
      }
      
    }
    return itemData;
    
  }
  public String dealForignKey(Connection conn,YHField field,String data)throws Exception{
    Statement stmt=null;
    ResultSet rs=null;
    String keyData="";
    try{
      String table=field.getTable();
      String key=field.getKey();
      String descKey=field.getDescKey();
      String sql=" select "+descKey+" from  "+table+" where  "+key+"='"+data+"'";
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      if(rs.next()){
        if("1".equals(field.getForignKeyDataType())){
          keyData=rs.getInt(1)+"";
        }else if("0".equals(field.getJavaType())){
          keyData=rs.getString(1);
        }
      }
    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    return keyData;
  }
  
  public String getTableHead(String type,String items,List<YHField> listFields)throws Exception{
    String headLine="<tr>";
    String itemOrg[]=items.split(",");
    for(int i=0;i<itemOrg.length;i++){
      if(!"".equals(itemOrg[i])){
        if("0".equals(type) && i==1){
          headLine+="<td class='TableContent'>数量</td>"; 
        } 
        YHField field=new YHField();
        String item="";
         if("0".equals(type)){
           field=listFields.get(0);
         item=itemOrg[i].substring(itemOrg[i].indexOf(".")+1, itemOrg[i].length());
          if(field.getItem().trim().equals(item.trim())){
           headLine+="<td class='TableContent'>"+field.getItemDesc()+"</td>"; 
         }
         }else if("1".equals(type)){
           for(int j=0;j<listFields.size();j++){
              field=listFields.get(j);
             item=itemOrg[i].substring(itemOrg[i].indexOf(".")+1, itemOrg[i].length());
             if(field.getItem().trim().equals(item.trim())){
              headLine+="<td class='TableContent'>"+field.getItemDesc()+"</td>"; 
             }
           }
           
         }

      }
      
    }
    headLine+="</tr>";
    return headLine;
  }
  
  public String getQueryStr(List<YHField> listFields,Map request,String query )throws Exception{
    String queryStr="";
    String que[]=query.split(",");
    for(int i=0;i<que.length;i++){
      if(!"".equals(que[i])){
        String item=que[i];
        if("DATE".equals(getDataType(item,listFields))){
          String value = request.get(item) == null ? null : ((String[]) request.get(item))[0];
          if(!YHUtility.isNullorEmpty(value)){
          queryStr+=" and ";
          queryStr+=YHDBUtility.getDateFilter(this.getGroupField(item, listFields), value, ">=");  
          }
        }else if("STR".equals(getDataType(item,listFields))){
           String value = request.get(item) == null ? null : ((String[]) request.get(item))[0];
           if(!YHUtility.isNullorEmpty(value)){
           queryStr+=" and ";
           queryStr+=this.getGroupField(item, listFields)+" like '%"+value+"%' "  ;
           }
        }else if("".equals(getDataType(item,listFields))){
          String value = request.get(item) == null ? null : ((String[]) request.get(item))[0];
          if(!YHUtility.isNullorEmpty(value)){
          queryStr+=" and ";
          queryStr+=YHDBUtility.getDateFilter(this.getGroupField(item.substring(0, item.length()-1), listFields), value, "<=");  
           }
          }
        else {
          String value = request.get(item) == null ? null : ((String[]) request.get(item))[0];
          if(!YHUtility.isNullorEmpty(value)){
          queryStr+=" and ";
          queryStr+=this.getGroupField(item, listFields)+" in ("+value+") "  ;
          }
        }
       
      }
    }
    
    return  queryStr;
  }
  
  public String getDataType(String item,List<YHField> listFields)throws Exception{
   String result="";
    for(int i=0;i<listFields.size();i++){
      YHField field=listFields.get(i);
      if(item.equals(field.getItem()) ){
        result=field.getDataType();
      }
    }
    return result;
  }
  
  public String getGroupField(String group_field,List<YHField> listFields)throws Exception{
    String items="";
    for(int x=0;x<listFields.size();x++){
      YHField field=listFields.get(x);
      if(group_field.equals(field.getItem())){
        items+=field.getFromTable()+"."+field.getItem();
     }
    }
    return items;
  }
  
  public String getJionConditions(String itemsAndFromStr,List<YHJionCondition> listConditions)throws Exception{
   String jionCondition=" and ";
   String tables=itemsAndFromStr.substring(itemsAndFromStr.indexOf("from")+5,itemsAndFromStr.length());
   String jionTables="";
    String table1="";
    String table2="";
   for(int i=0;i<listConditions.size();i++){
     YHJionCondition condition=listConditions.get(i);
     jionTables=condition.getTables();
     if(jionTables.indexOf(",") >0){
       table1=jionTables.substring(0, jionTables.indexOf(","));
       table2=jionTables.substring(jionTables.indexOf(",")+1,jionTables.length());
     }
     
     String tableStr=itemsAndFromStr.substring(itemsAndFromStr.indexOf(" from ")+5, itemsAndFromStr.length());
     if(this.isHasTable(tableStr, table1) && this.isHasTable(tableStr, table2)){
       
       jionCondition+=condition.getItem1()+"="+condition.getItem2();
       jionCondition+=" and ";
     }
   }
   if(jionCondition.endsWith("and ")){
     jionCondition=jionCondition.substring(0, jionCondition.length()-4);
   }
    return jionCondition;
  }
  
  public String[] getSelectItems(String list_item,List<YHField> listFields)throws Exception{
    String items="";
    int num=0;
    String itemNum="";
    String fromTables=" from ";
    String itemsAndFromStr="";
    String listItem[]=list_item.split(",");
    for(int i=0;i<listItem.length;i++){
      String itemOrg=listItem[i];
      if(!YHUtility.isNullorEmpty(itemOrg)){
        String itemStr[]=itemOrg.split("`");
          String item=itemStr[0];
            for(int x=0;x<listFields.size();x++){
              YHField field=listFields.get(x);
              if(item.equals(field.getItem())){
                if("CAL".equals(field.getDataType()))
                {
                  items+=field.getItem();
                  items+=",";
                  itemNum+=field.getItem()+" as item"+num;
                  itemNum+=",";
                }
                else
                {
                items+=field.getFromTable()+"."+field.getItem();
                items+=",";
                itemNum+=field.getFromTable()+"."+field.getItem()+" as item"+num;
                itemNum+=",";
                }
                num++;
                if(!this.isHasTable(fromTables.substring(6, fromTables.length()), field.getFromTable())){
                  fromTables+=field.getFromTable();
                  fromTables+=",";
               }
             }
          }
       }
    }
    if(items.endsWith(",")){
      items=items.substring(0, items.length()-1);
    }
    if(fromTables.endsWith(",")){
      fromTables=fromTables.substring(0, fromTables.length()-1);
    }
    if(itemNum.endsWith(",")){
      itemNum=itemNum.substring(0, itemNum.length()-1);
    }
   // itemsAndFromStr=itemNum ":"+items+" "+fromTables+":"+num;
    String strOrg[]={itemNum+" "+fromTables,items+" "+fromTables,num+""};
    return strOrg;
    
  }
  
  public boolean isHasTable(String tablesStr,String table)throws Exception{
    boolean flag=false;
    String tables[]=tablesStr.split(",");
    for(int j=0;j<tables.length;j++){
      if(!"".equals(tables[j]) && table.trim().equals(tables[j].trim())){
        flag=true;
        break;
      }
    }
    return flag;
  }
  
  public YHLoadConfigFile getFieldListbyMid(Connection conn,String mId)throws Exception{
    Statement stmt=null;
    ResultSet rs=null;
    YHLoadConfigFile file=new YHLoadConfigFile();
    try{
      String sql=" select config from all_report_type where seq_id='"+mId+"' ";
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      String configFile="";
      if(rs.next()){
        configFile=rs.getString("config");
      }
      String configPath=YHSysProps.getString("allreportconfig");
      configPath+=configFile;
     file=new YHLoadConfigFile(configPath);
      
    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    return file;
  }
  
  
  
/**
 * 数据转换  将二维表转换成一维表
 * @param list
 * @return
 * @throws Exception
 */
  public ArrayList<YHDbRecord> convertList( List<List<String>> list)throws Exception{
    ArrayList<YHDbRecord > dbL = new ArrayList<YHDbRecord>();
    if(list != null && list.size() >0){
      List<String> namelist=list.get(0);
      int length=namelist.size();
      for(int i=1;i<list.size();i++){
        List<String> datalist=list.get(i);
        YHDbRecord dbrec=new YHDbRecord();
        for(int j=0;j<length;j++){       
          dbrec.addField(namelist.get(j),datalist.get(j));        
        }
         dbL.add(dbrec);
      }

    }     
    return dbL;    
  }
  
}

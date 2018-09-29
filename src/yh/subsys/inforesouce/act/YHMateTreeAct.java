package yh.subsys.inforesouce.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.syslog.logic.YHGlSyslogLogic;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

import yh.subsys.inforesouce.data.YHMateType;
import yh.subsys.inforesouce.data.YHMateValue;
import yh.subsys.inforesouce.data.YHSignFile;
import yh.subsys.inforesouce.db.YHMetaDbHelper;
import yh.subsys.inforesouce.logic.YHMataTreeLogic;
import yh.subsys.inforesouce.logic.YHMateElementLogic;
import yh.subsys.inforesouce.util.YHAjaxUtil;
import yh.subsys.inforesouce.util.YHStringUtil;

public class YHMateTreeAct {
  private static Logger log = Logger.getLogger(YHMateTreeAct.class);
  private   YHMataTreeLogic tree= new YHMataTreeLogic();
  /**
   * 查询父元素，子元素 以及值域
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String findParent(HttpServletRequest request,
      HttpServletResponse response) throws Exception {   
      Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
        YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        String typemenu = request.getParameter("typemenu");
        System.out.println(typemenu == null);
        if(typemenu == null){
          typemenu = "1";
        }
        YHMateElementLogic element= new YHMateElementLogic();
        StringBuffer sb = new StringBuffer("[");
        List<YHMateType> parent = tree.findParent(dbConn, person,typemenu);
        for (YHMateType mt : parent){         
          sb.append("{\"name\":\"");
          sb.append(mt.getcNname());
          sb.append("\",\"id\":\"");
          sb.append(mt.getNumberId());
          sb.append("\",\"items\":[");
          String valueRange = mt.getRangeId();
          if(valueRange!=null){
            for (String s : (valueRange.split(","))){
           // String s  = valueRange.split(",")[0]; //判断 父元素下 直接由值域
            YHMateValue mv = tree.sonDate(dbConn, person, Integer.parseInt(s));
            sb.append("{\"attr\":{");
            sb.append("\"id\":\"");
            sb.append(mt.getNumberId()+"-"+mv.getSeqId());
            sb.append("\"},");
            sb.append("\"data\":{");
            sb.append("\"title\":\"");
            String name = mv.getValueName();
            sb.append(name);
            sb.append("\"}");
            sb.append("}");
            sb.append(",");
            }
        } // 判断父元素下有子元素
          List<YHMateType> children = tree.findSon(dbConn, person, mt.getSeqId(),typemenu);
          for (YHMateType ch : children){
            sb.append("{\"attr\":{");
            sb.append("\"id\":\"");
            sb.append(ch.getNumberId());
            sb.append("\",");
            sb.append("\"rel\":\"nodeLv1\"},");
            
            sb.append("\"data\":{");
            sb.append("\"title\":\"");
            sb.append(ch.getcNname());
            sb.append("\"");
            
            String range = ch.getRangeId();
           // System.out.println(range);
            if (range == null || "".equals(range.trim())){
              //System.out.println(range+"jjjjjj");
              sb.append(",\"attr\":{\"class\":\"input\"}}}");
            }
            else{
              sb.append("},");
              sb.append("\"children\":["); 
              for (String s : range.split(",")){//把子元素下的值域进行分割,分割后的数字对应值域表中（mate_value）的seq_id
             // {
               // String s  = range.split(",")[0];
                YHMateValue mv = tree.sonDate(dbConn, person, Integer.parseInt(s));
               // "children" : [   "items": [
                sb.append("{\"attr\":{");
                sb.append("\"id\":\"");
                sb.append(ch.getNumberId()+"-"+mv.getSeqId());
                sb.append("\"},");
                sb.append("\"data\":{");
                sb.append("\"title\":\"");
                String name = mv.getValueName();
                sb.append(name);
                sb.append("\"}");
                sb.append("}");
                sb.append(",");
              }
              if (sb.charAt(sb.length() - 1) == ','){
                sb.deleteCharAt(sb.length() - 1);
              }
              sb.append("]}");
            }
            sb.append(",");
          }
           if (sb.charAt(sb.length() - 1) == ','){
            sb.deleteCharAt(sb.length() - 1);
          }
           sb.append("]}");
          sb.append(",");
        }
        if (sb.charAt(sb.length() - 1) == ','){
          sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        System.out.println(sb.toString());
        PrintWriter pw = response.getWriter();    
        String rtData = sb.toString();
        pw.println(rtData);    
        pw.flush();
        request.setAttribute("va", parent);
        request.setAttribute("treeData", sb.toString());
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     }catch(Exception ex) {
       String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
       request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
   //  D:\project\yh\webroot\yh\subsys\inforesource\tree\tree.jsp  "/subsys/inforesource/tree/tree.jsp"
     return null;
  }
  /**点击定义后，
   * 当你选中父元素，子元素或值域后拼好串返回到页面
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String findMymoth(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
  
    long start = System.currentTimeMillis();
    try{
    Connection dbConn = null;
    StringBuffer sb = new StringBuffer("[");
    dbConn = requestDbConn.getSysDbConn();
    String typemenu = request.getParameter("typemenu");
    System.out.println(typemenu == null);
    if(typemenu == null){
      typemenu = "1";
    }
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    boolean isHave = tree.iHaveSave(dbConn, person,typemenu);
    if(isHave){ 
      List<YHMateType> types = tree.findSelMenu(typemenu,dbConn, person); //查找父元素的seq_id 和 name
     if(types!=null && types.size()>0){
      for(int i=0; i<types.size()&&types!=null; i++){
       // System.out.println("父元素："+types.get(i).getcNname()+"\n");
       // System.out.println("他的子元素：");
        sb.append("{\"name\":\"");
        sb.append(types.get(i).getcNname());
        sb.append("\",\"id\":\"");
        sb.append(types.get(i).getNumberId());
        sb.append("\",\"items\":[");
        List<YHMateValue> list  = types.get(i).getValues();//???
        if(list != null){    //值域元素不为空 判断 父元素下的值域  
        for(int j=0; j<list.size(); j++){
          sb.append("{\"attr\":{");
          sb.append("\"id\":\"");
          sb.append(types.get(i).getNumberId()+"-"+list.get(j).getSeqId());
          sb.append("\"},");
          sb.append("\"data\":{");
          sb.append("\"title\":\"");
          String name = list.get(j).getValueName();
         // System.out.println(name);
          sb.append(name);
          sb.append("\"}");
          sb.append("}");
          sb.append(",");
        }
       }
        List<YHMateType> subs = types.get(i).getSubs();       
        if(subs != null){
          for(int k=0; k<subs.size(); k++){
        //判断父元素下的子元素 "子元素："+subs.get(k).toString();
            sb.append("{\"attr\":{");
            sb.append("\"id\":\"");
            sb.append(subs.get(k).getNumberId()+"-");
            sb.append("\",");
            sb.append("\"rel\":\"nodeLv1\"},");
            sb.append("\"data\":{");
            sb.append("\"title\":\"");
            sb.append(subs.get(k).getcNname());
            //sb.append("\"}}");
         // sb.append(",");
            sb.append("\"");
            List<YHMateValue> li = subs.get(k).getValues();
           /* if(li==null || "".equals(li) || li.size()<1){
           if(subs.get(k).getValues()==null && "".equals(subs.get(k).getValues())){
             sb.append(",\"attr\":{\"class\":\"input\"}}}");
           }
            }*/
            //YHOut.println(li+"KKKKKKKKKKK");
            if(li==null || "".equals(li) || li.size()<1){
              sb.append(",\"attr\":{\"class\":\"input\"}}}");
           }else{
             sb.append("},");
             sb.append("\"children\":["); 
             for(int m = 0; m<li.size(); m++){//判断子元素下的值域
               sb.append("{\"attr\":{");
               sb.append("\"id\":\"");
               sb.append(subs.get(k).getNumberId()+"-"+li.get(m).getSeqId());
               sb.append("\"},");
               sb.append("\"data\":{");
               sb.append("\"title\":\"");
               String name = li.get(m).getValueName();
               //System.out.println(name);
               sb.append(name);
               sb.append("\"}");
               sb.append("}");
               sb.append(",");
             }
             if (sb.charAt(sb.length() - 1) == ','){
               sb.deleteCharAt(sb.length() - 1);
             }
             sb.append("]}");
           }
            sb.append(",");
         }
        }else{
          
        }
        if (sb.charAt(sb.length() - 1) == ','){
          sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]}");
        sb.append(",");
      }
    }
      if (sb.charAt(sb.length() - 1) == ','){
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      //System.out.println("jjj"+sb.toString());
        
    }else{
     return findParent(request, response);
    }
    long end = System.currentTimeMillis();
    //System.out.println("用时："+ (end-start)+"ms");
    PrintWriter pw = response.getWriter();    
    String rtData = sb.toString();
    pw.println(rtData);    
    pw.flush();
    request.setAttribute("treeData", sb.toString());
    }catch(Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return null;
  }
  public String findMyFile(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    YHPerson loginUser = null;
    String paras = request.getParameter("par");
    //YHOut.println(paras);
    
    try{ 
      String str="";         
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson personLogin = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHGlSyslogLogic syslog =new YHGlSyslogLogic();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出日志");
      request.setAttribute("data", str); //到search.jsp 页面 去 接收data 对象
      
      request.setAttribute(YHActionKeys.RET_DATA, str);
    }catch(Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/funcs/rtjson.jsp";
  }
  /**
   * 点击节点传递参数
   */
  public static String passParameter(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String vsplit="";
    String para = request.getParameter("para");
    Connection dbConn = null;
    YHPerson loginUser = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    
    try{
      dbConn = requestDbConn.getSysDbConn();
    StringBuffer sb = new StringBuffer("[");
    String psplit[]= para.split(",");
    for(int i=0; i<psplit.length; i++){
    vsplit  += psplit[i]+",";
    }
   Map<String,String> map = YHStringUtil.toMap(vsplit);
   //YHOut.println(map);
   YHMetaDbHelper helper = new YHMetaDbHelper();
   List <YHSignFile>files=null;
    files = helper.searchFileList(dbConn, null, map);
    //System.out.println("files:::"+files.size());
    //YHFileMetaSaveAct act = new YHFileMetaSaveAct();
   // String jsons =act.toJson(files); 
   if(files.size()>0){
    //System.out.println(map+"999-----"+((YHSignFile)files.get(0)).toJson());
    }
  if(files.size()>0){
    for(int j = 0; j<files.size(); j++){
      sb.append("{");
      sb.append("name:\""+((YHSignFile)files.get(j)).getFileName()+"\"");
      sb.append(",size:\""+((YHSignFile)files.get(j)).getFileSize()+"\"");
      sb.append(",type:\""+((YHSignFile)files.get(j)).getSignType()+"\"");
      sb.append(",path:\""+((YHSignFile)files.get(j)).getFilePath()+"\"");
      sb.append("},");
    }
    if(files.size()>0){
      sb.deleteCharAt(sb.length()-1);
    }
   } 
   sb.append("]");
     YHAjaxUtil.ajax(sb.toString(), response);
 //  request.setAttribute(YHActionKeys.RET_DATA, jsons); 
   request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }

  public static void main(String[] agrs){
  Map<Integer,String> map = new HashMap();
  Map<Integer,String> map1 = new HashMap();
  Map<Integer,String> map2 = new HashMap();
  Map<Integer,String> map3 = new HashMap();
  map.put(1, "a");
  map.put(2, "b");
  map.put(3, "c");
  map.put(4, "d");
  map.put(5, "r");
   map1.put(1, "a");
  map1.put(2, "b");
  map1.put(3, "c");
  map1.put(4, "d");
  map1.put(5, "f");
 for(Iterator <Entry<Integer,String>> it = map.entrySet().iterator(); it.hasNext();){
   Entry<Integer,String> e = it.next();
   for(Iterator <Entry<Integer,String>> its = map1.entrySet().iterator(); its.hasNext();){
     Entry<Integer,String> es = its.next();
     if(e.getKey()==es.getKey()){
      String value = e.getValue()+","+es.getValue();
      map2.put(e.getKey(), value);
     }else{
       map3.put(e.getKey(), e.getValue());//key是不重复的 所以取得值没有重复
     }
   }
 }
 for(int i = 1; i<=map2.size(); i++){
   //System.out.println(i+"--"+ map2.get(i));
 }
 for(int j = 1; j<=map3.size(); j++){ 
   //System.out.println(j+"--"+ map3.get(j));
 }
 /* 第一种获取 如果键相等的话 获取键的不同值
 Set<Integer> keys = map2.keySet();
 for(Integer key: keys){
   //System.out.println("key:"+key+"--value:"+map2.get(key));
 }*/
  
  Set<Integer> keys1 = map.keySet(); //map的keySet()方法只返回一个set实例
  for(Integer key: keys1){
    //System.out.println("key:"+key+"--value:"+map.get(key));
  }
 // System.out.println(keys1+"::");
  }
}

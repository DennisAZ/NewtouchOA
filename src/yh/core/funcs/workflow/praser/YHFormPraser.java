package yh.core.funcs.workflow.praser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yh.core.funcs.workflow.data.YHFlowFormReglex;
/**
 * 表单解析类
 * @author TTlang
 *
 */
public class YHFormPraser{
  /**
   * 正则解析得到单个html控件代码
   * @param regex
   * @param endRegex
   * @param htmlCode
   * @return
   */
  public static ArrayList<String> praserHTML(String regex,String endRegex,String htmlCode){
    ArrayList<String> list = new ArrayList<String>();
    int start = 0;
    int start2 = 0;
    int len = htmlCode.length();
    int end = 0;//start + regex.length();
    int end2 = 0;//start + regex.length();
    while(end2 < len ){
      start = praserHTML(regex, htmlCode, end);
      if(start == -1){
        break;
      }
      end = start + regex.length();
      start2 = praserHTML(endRegex, htmlCode, start);
      if(start2 == -1){
        break;
      }
      end2 = start2 + endRegex.length();
      String domStr = htmlCode.substring(start,end2);
      //System.out.println(domStr);
      list.add(domStr);
    }
    return list;
  }
  /**
   * 正则解析
   * @param regex
   * @param htmlCode
   * @param start
   * @return
   */
  public static int praserHTML(String regex,String htmlCode,int start){
    int result = -1;
    Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE  );  
    Matcher matcher = pattern.matcher(htmlCode);  
    //debug
    //System.out.println("需要解析的字符串 ：" + htmlCode);
    //System.out.println("解析的格式 ：" + regex);
    if(start >= 0 && matcher.find(start)){
      result = matcher.start();
    }
    //System.out.println("起始位置 ： " + result);
    return result;
  }
  /**
   * 得到所有组件
   * @param htmlCode
   * @return
   */
  public static Map<String,ArrayList<String>> praserHTML2Dom(String htmlCode){
    Map<String,ArrayList<String>> domList = new HashMap<String, ArrayList<String>>();
    ArrayList<String> inputlist = YHFormPraser.praserHTML(YHFlowFormReglex.INPUT, YHFlowFormReglex.NOREND2, htmlCode);
    ArrayList<String> selectlist = YHFormPraser.praserHTML(YHFlowFormReglex.SELECT, YHFlowFormReglex.SELECTEND, htmlCode);
    ArrayList<String> texlist = YHFormPraser.praserHTML(YHFlowFormReglex.TEXTAREA, YHFlowFormReglex.TEXTAREAEND, htmlCode);
    ArrayList<String> butlist = YHFormPraser.praserHTML(YHFlowFormReglex.BUTTON, YHFlowFormReglex.BUTTONEND, htmlCode);
    ArrayList<String> imglist = YHFormPraser.praserHTML(YHFlowFormReglex.IMG, YHFlowFormReglex.NOREND2, htmlCode);
    
    if(inputlist.size() > 0){
      domList.put("INPUT",inputlist );
    }
    if(selectlist.size() > 0){
      domList.put("SELECT",selectlist );
    }
    if(texlist.size() > 0){
      domList.put("TEXTAREA",texlist );
    }
    if(butlist.size() > 0){
      domList.put("BUTTON",butlist );
    }
    if(imglist.size() > 0){
      domList.put("IMG",imglist );
    }
    return domList;
  }
  /**
   * 得到所有HTML属性，以map的形式返回
   * @param domlist
   * @return
   */
  public static HashMap<String, Map> praserHTML2Arr(Map<String,ArrayList<String>> domlist ){
    HashMap<String, Map> result = new HashMap<String, Map>();
    HashMap<String, Map> checkSameName = new HashMap<String, Map>();
    HashMap<String, String> dom = null;
    Set<String> keys = domlist.keySet();
    
    for (String key : keys){
      ArrayList<String> dl = domlist.get(key);
      if("TEXTAREA".equals(key)){
        for (String domStr : dl){
          int start = praserHTML(YHFlowFormReglex.TEXTAREA, domStr, 0);
          int start2 = praserHTML(YHFlowFormReglex.NOREND2, domStr, start);
          int end =  start + YHFlowFormReglex.TEXTAREA.length();
          int end2 =  start2 + YHFlowFormReglex.NOREND2.length();
          int start3 = praserHTML(YHFlowFormReglex.TEXTAREAEND, domStr, end2);
          String valueStr = domStr.substring(end2, start3);
          //System.out.println("value : " + valueStr);
          String arr = domStr.substring(end, end2 - 1);
          dom = getArr(YHFlowFormReglex.ATTRIBUTE, arr);
          dom.put("VALUE", valueStr);
          dom.put("TAG", "TEXTAREA");
          dom.put("CONTENT",domStr );
        //如果之前已经有了相同name的空间，则在页面给出提示
          if(result.get(dom.get("NAME")) != null){
        	  Map<String,String> map = new HashMap<String,String>();
        	  map.put("isSame", "1");
        	  checkSameName.put("isSame", map);
        	  return checkSameName;
          }
          result.put(dom.get("NAME"), dom);
        }
      }
      if("SELECT".equals(key)){
        String valueStr = "";
        for (String domStr : dl){
          int start = praserHTML(YHFlowFormReglex.SELECT, domStr, 0);
          int start2 = praserHTML(YHFlowFormReglex.NOREND2, domStr, start);
          int end =  start + YHFlowFormReglex.SELECT.length();
          int end2 =  start2 + YHFlowFormReglex.NOREND2.length();
          //VALUEZHIE
          int start4  = 0;
          while( start4 < praserHTML(YHFlowFormReglex.SELECTEND, domStr, end2)){
            int start3 = praserHTML("<OPTION", domStr, end2);
            if(start3 == -1){
              break;
            }
            start4 = praserHTML(">", domStr, start3);
            String tep =  domStr.substring(start3, start4);
            int start5 = praserHTML("selected",tep, 0);
            if(start5 != -1){
              tep = tep.substring(0, start5);
              int sti = praserHTML(" \\S+=\\S+", tep, 0);
                valueStr = getArr(YHFlowFormReglex.ATTRIBUTE, tep).get("VALUE");
              break;
            }else{
              end2 = start4;
            }
          }
          String arr = domStr.substring(end, end2 - 1);
          dom = getArr(YHFlowFormReglex.ATTRIBUTE, arr);
          if (arr.indexOf("dataSrc") != -1 || arr.indexOf("datasrc") != -1) {
            Pattern pattern = Pattern.compile("DATASRC=\\\\\"[^\"]+\\\\\"" , Pattern.CASE_INSENSITIVE);  
            Matcher matcher = pattern.matcher(arr);
            String value = "";
            if (matcher.find()) {
              int start5 = matcher.start();
              int end4 = matcher.end();
              value = arr.substring(start5 , end4);
            }
            if (!"".equals(value)) {
              value = value.substring("DATASRC=\\\"".length());
              if (value.endsWith("\"")) {
                value = value.substring(0 , value.length() -2 );
              }
              dom.put("DATASRC", value);
            }
          }
          dom.put("VALUE", valueStr);
          dom.put("TAG", "SELECT");
          dom.put("CONTENT",domStr );
        //如果之前已经有了相同name的空间，则在页面给出提示
          if(result.get(dom.get("NAME")) != null){
        	  Map<String,String> map = new HashMap<String,String>();
        	  map.put("isSame", "1");
        	  checkSameName.put("isSame", map);
        	  return checkSameName;
          }
          result.put(dom.get("NAME"), dom);
        }
      }
      if("BUTTON".equals(key)){
        for (String domStr : dl){
          int start = praserHTML(YHFlowFormReglex.BUTTON, domStr, 0);
          int start2 = praserHTML(YHFlowFormReglex.NOREND2, domStr, start);
          int end =  start + YHFlowFormReglex.BUTTON.length();
          int end2 =  start2 + YHFlowFormReglex.NOREND2.length();
          int start3 = praserHTML(YHFlowFormReglex.BUTTONEND, domStr, end2);
          String valueStr = domStr.substring(end2, start3);
          String arr = domStr.substring(end, end2 - 1);
          dom = getArr(YHFlowFormReglex.ATTRIBUTE, arr);
          dom.put("VALUE", valueStr);
          dom.put("TAG", "BUTTON");
          dom.put("CONTENT",domStr );
        //如果之前已经有了相同name的空间，则在页面给出提示
          if(result.get(dom.get("NAME")) != null){
        	  Map<String,String> map = new HashMap<String,String>();
        	  map.put("isSame", "1");
        	  checkSameName.put("isSame", map);
        	  return checkSameName;
          }
          result.put(dom.get("NAME"), dom);
        }
      }
      if("INPUT".equals(key)){
        for (String domStr : dl){
          int start = praserHTML(YHFlowFormReglex.INPUT, domStr, 0);
          int start2 = praserHTML(YHFlowFormReglex.NOREND2, domStr, start);
          int end =  start + YHFlowFormReglex.INPUT.length();
          int end2 =  start2 + YHFlowFormReglex.NOREND2.length();
          String arr = domStr.substring(end, end2 - 1);
          dom = getArr(YHFlowFormReglex.ATTRIBUTE, arr);
          
          if (arr.indexOf("dataSrc") != -1 || arr.indexOf("datasrc") != -1) {
            Pattern pattern = Pattern.compile("DATASRC=\\\\\"[^\"]+\\\\\"" , Pattern.CASE_INSENSITIVE);  
            Matcher matcher = pattern.matcher(arr);
            String value = "";
            if (matcher.find()) {
              int start4 = matcher.start();
              int end4 = matcher.end();
              value = arr.substring(start4 , end4);
            }
            if (!"".equals(value)) {
              value = value.substring("DATASRC=\\\"".length());
              if (value.endsWith("\"")) {
                value = value.substring(0 , value.length() -2 );
              }
              dom.put("DATASRC", value);
            }
          }
          dom.put("TAG", "INPUT");
          dom.put("CONTENT",domStr );
        //如果之前已经有了相同name的空间，则在页面给出提示
          if(result.get(dom.get("NAME")) != null){
        	  Map<String,String> map = new HashMap<String,String>();
        	  map.put("isSame", "1");
        	  checkSameName.put("isSame", map);
        	  return checkSameName;
          }
          result.put(dom.get("NAME"), dom);
        }
      }
      if("IMG".equals(key)){
        for (String domStr : dl){
          int start = praserHTML(YHFlowFormReglex.IMG, domStr, 0);
          int start2 = praserHTML(YHFlowFormReglex.NOREND2, domStr, start);
          int end =  start + YHFlowFormReglex.IMG.length();
          int end2 =  start2 + YHFlowFormReglex.NOREND2.length();
          String arr = domStr.substring(end, end2 - 1);
          dom = getArr(YHFlowFormReglex.ATTRIBUTE, arr);
          dom.put("TAG", "IMG");
          dom.put("CONTENT",domStr );
        //如果之前已经有了相同name的空间，则在页面给出提示
          if(result.get(dom.get("NAME")) != null){
        	  Map<String,String> map = new HashMap<String,String>();
        	  map.put("isSame", "1");
        	  checkSameName.put("isSame", map);
        	  return checkSameName;
          }
          result.put(dom.get("NAME"), dom);
        }
      }
    }
    
    Set<String> resultKey = result.keySet();
    Map<String,String> map = new HashMap<String,String>();
    for(String key : resultKey){
    	Map<String,String> domMap = result.get(key);
    	String id = domMap.get("ID");
    	if(id!=null&&!"".equals(id)){
    		if(map.get(id)!=null){
    			Map<String,String> map1 = new HashMap<String,String>();
    			map1.put("isSame", "1");
          	  	checkSameName.put("isSame", map1);
          	  	return checkSameName;
    		}
    		map.put(id, id);
    	}
    }
    
    return result;
  }
  /**
   * 取得html的所有属性段
   * @param regex
   * @param htmlCode
   * @return
   */
  public static HashMap<String, String> getArr(String regex,String htmlCode ){
    HashMap<String, String> dom = new HashMap<String, String>();
    Pattern pattern = Pattern.compile(regex);  
    Matcher matcher = pattern.matcher(htmlCode);
    //System.out.println("需要解析的字符串 ：" + htmlCode);
    //System.out.println("起始解析的格式 ：" + regex);
    boolean found = false; 
    int start = 0;
    int end = -1 ;
    String[] arrs = null;
    String arr = "";
    while (matcher.find()) {
      end = matcher.start();
      //System.out.println("起始位置：" + start + " ， 结束位置 ： " + end + " ,匹配字符：\"" + matcher.group() +"\"");
      if(end != start){
        arr = htmlCode.substring(start + 1, end);
        arrs = arr.split("=");
        if(arrs.length == 2){
          String key = arrs[0].toUpperCase();
          String value = "";
          if(!"STYLE".equals(key)){
            value = arrs[1].replaceAll("\\\\\"", "");
          }else{
            value = arrs[1];
          }
          dom.put(key, value);
        }
      }
      found = true; 
      start = matcher.start();
    }  
    arr = htmlCode.substring(end + 1, htmlCode.length());
    arrs = arr.split("=");
    if(arrs.length == 2){
      String key = arrs[0].toUpperCase();
      dom.put(key, arrs[1]);
    }
    if (!found) {  
      //System.out.printf("No match found.%n");  
    }
    String id = dom.get("ID");
    String name = dom.get("NAME");
    
    if (name!= null) {
      name = name.replaceAll("[\\\\\"]", "");
      name = name.replaceAll("/", "");
      name = name.replaceAll(" ", "");
      dom.put("NAME", name);
    }
    if(id == null || "".equals(id)){
      dom.put("ID", name);
    }
    return dom;
  }
  /**
   * 转变为json格式的数据
   * @param m
   * @return
   */
  public static StringBuffer toJson(Map m){
    StringBuffer rtBuf = new StringBuffer("{");
    StringBuffer filed = new StringBuffer();
    Iterator it = m.entrySet().iterator();
    while (it.hasNext()){
      Map.Entry entry = (Map.Entry) it.next();
      Object key = entry.getKey();
      Object value = entry.getValue();
      if(Map.class.isInstance(value)){
        StringBuffer subBuff = toJson((Map) value);
        if("".equals(filed.toString())){
          filed.append("'").append(key).append("':").append(subBuff.toString().trim());
        }else{
          filed.append(",").append("'").append(key).append("':").append(subBuff.toString().trim());
        }
        continue;
      }
      if("".equals(filed.toString())){
        filed.append("'").append(key).append("':").append("\"").append(value).append("\"");
      }else{
        filed.append(",").append("'").append(key).append("':").append("\"").append(value).append("\"");
      }
    }
    rtBuf.append(filed).append("}");
    return rtBuf;
  }
  /**
   * 转变为json格式的数据
   * @param html
   * @return
   */
  public static StringBuffer toJson(String html){
    StringBuffer rtBuf = null;
    HashMap hm = (HashMap) praserHTML2Dom(html);//取得所有控件
    Map m1 = praserHTML2Arr(hm);//取得所有属性
    rtBuf = YHFormPraser.toJson(m1);
    return rtBuf;
  }
  
  public static HashMap toMap(String html){
    if(html == null){
      HashMap map = new HashMap();
      return map;
    }
    StringBuffer rtBuf = null;
    HashMap hm = (HashMap) praserHTML2Dom(html);//取得所有控件
    HashMap m1 = praserHTML2Arr(hm);//取得所有属性
    rtBuf = YHFormPraser.toJson(m1);
    return m1;
  }
  
  public static String replace(String newStr,String oldStr,String rep){
   // System.out.println(rep.indexOf(oldStr));
    String newSt = rep.replace(oldStr, newStr);
//    System.out.println(newSt);
//    System.out.println(oldStr);
//    System.out.println(newStr);
    return newSt;
  }
  /**
   * 
   * @param m
   * @param html
   * @param repKey m中需要被替换的部分
   * @return
   */
  public static String toShortString(Map<String, Map> m,String html,String repKey){
    Set<String> keys = m.keySet();
    for (String key : keys){
      Map  val = m.get(key);
      String value = (String) val.get(repKey);
      
      if (key != null && !"null".equals(key)) {
        String upKey = key.toUpperCase();
        if (upKey.startsWith("DATA") || upKey.startsWith("OTHER")) {
          html = YHFormPraser.replace("{" + key + "}", value, html);
        }
      }
    }
    return html;
  }
  public static void main(String[] args) {
    String str = " id=DATA_10 dataSrc=\\\"select user_name from person where seq_id = 1\\\" class=AUTO title=ddd dataFld=SYS_SQL value={宏控件} name=DATA_10 classname=\"AUTO\"";
    Pattern pattern = Pattern.compile("DATASRC=\\\\\"[^\"]+\\\\\"" , Pattern.CASE_INSENSITIVE);  
    Matcher matcher = pattern.matcher(str);
    String value = "";
    if (matcher.find()) {
      int start4 = matcher.start();
      int end4 = matcher.end();
      value = str.substring(start4 , end4);
    }
    if (!"".equals(value)) {
      value = value.substring("DATASRC=\\\"".length());
      if (value.endsWith("\"")) {
        value = value.substring(0 , value.length() -2 );
      }
      System.out.println(value);
    }
  }
  
}

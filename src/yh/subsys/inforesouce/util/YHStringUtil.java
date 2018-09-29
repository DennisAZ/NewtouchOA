package yh.subsys.inforesouce.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import yh.core.util.YHOut;
import yh.core.util.YHUtility;

/**
 * 字符串工具类
 * @author qwx110
 *
 */
public class YHStringUtil{
  
  /**
   * 判断是否是空
   * @param agrs
   * @return
   */
   public static boolean isEmpty(String agrs){
     if(agrs==null ||agrs.length()==0 || "".equalsIgnoreCase(agrs) ||" ".equalsIgnoreCase(agrs)|| "null".equalsIgnoreCase(agrs)){
       return true;
     }
     return false; 
   }
   
   /**
    * 判断不为空
    * @param agrs
    * @return
    */
   public static boolean isNotEmpty(String agrs){
     return !isEmpty(agrs);
   }
   
   /**
    * 把一个字符串数组转化为字符串，","进行分割
    * @param args
    * @return
    */
   public static String array2AString(String[] args){
     if(args==null || args.length==0){
       return "";
     }
     String newString = "";
     for(int i=0; i<args.length; i++){
       newString += args[i] +",";
     }
     return newString.substring(0, newString.lastIndexOf(",")==-1?0: newString.lastIndexOf(","));
   }
   
   /**
    * 把一个串重新解析
    * @param str  传进来的串
    * @param splitprex 进行分割的标志,如”aaa，bbbb，sss，“的 逗号
    * @return returnStr 新串
    */
   public static String toAnewString(String str, String splitprex){
     String returnStr = "";
     if(isNotEmpty(str)){
       String[] astr = str.split(splitprex);
       for(int i=0; i<astr.length; i++){
         if(isNotEmpty(astr[i])){
           returnStr += astr[i] +",";
         }
       }
     }
     return returnStr.substring(0,returnStr.lastIndexOf(",")==-1?0:returnStr.lastIndexOf(","));
   }
   
 
   
   public static String dateFormat(Date date){
     if(date != null){
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       String ds = sdf.format(date);
       //YHOut.println(ds.toString());
       return ds.toString();
     }else{
       return "";
     }    
   }
   
   /**
    * 把一个串转化为map的形式，如M12-M23-123,M12-M23-976转换为map的形式为 map.put(M12,"123,976");
    * @param str
    * @return
    */
   public static Map<String, String> toMap(String str){
     String[] sub = str.split(",");
     Map<String, String> map = new HashMap<String, String>();
     Map<String, String> map1 = new HashMap<String, String>();
     for(int i=0; i<sub.length; i++){
       String key =substring(sub[i]);//M1-,M1-11,M1-你好
       String aKey = parseKey(key); 
       String ids = "";
       for(int k=i; k<sub.length; k++){
         String bKey =  parseKey(substring(sub[k])); 
         if(bKey!=null && aKey!=null && bKey.equalsIgnoreCase(aKey)){            
           ids += paraxValue(sub[k]) +",";
           i = k;
         }else{
           continue;
         }
         if(!YHUtility.isNullorEmpty(ids) && !",".equalsIgnoreCase(ids)){
            ids = ids.substring(0, ids.indexOf(",")==-1?ids.length():ids.indexOf(","));
            map.put(aKey, ids);   
         }
       }
    }
     return map;
 }
   /**
    * 设置一个字符串为一个map的key
    * @param sub
    * @return
    */
   public static String parseKey(String sub){
     String pkey = null;
     if(sub != null){
       if(sub.indexOf("-") < 0){
         pkey = sub;   
       }else{
         pkey = sub.substring(sub.indexOf("-")+1, sub.length());
       }  
     }
     if(!YHUtility.isNullorEmpty(pkey)){
       //pkey = pkey.substring(1, pkey.length());
       pkey = pkey.replace("MEX", "").replace("M", "");
       if(Integer.parseInt(pkey)>100){
         pkey = "MEX"+pkey;
       }else{
         pkey = "M"+pkey;
       }
     }
     return pkey;
   }
   
   /**
    * 截取串
    * @param sub
    * @return
    */
   public static String substring(String sub){
     if(sub.indexOf("-")>0){
       return sub.substring(0, sub.lastIndexOf("-"));
     }
     return null;
   }
   /**
    * 查找一个串种的值域，如M1-M23-123，
    * 把123找出来。
    * @param sub
    * @return
    */
   public static String paraxValue(String sub){
     String value = sub.substring(sub.lastIndexOf("-")+1, sub.length());
     if(YHStringUtil.isNotEmpty(value)){
       return value;
     }
     return "";
   }
    
   public static void main(String[] agrs){
     String s = "MEX150-广角,MEX151-北京,MEX152-海淀";
     /*String ss = substring(s);
     //System.out.println(ss);
     
     String test = "M12-M23";
     String key = parseKey(test);
     //System.out.println(key);
     */
     long a = System.currentTimeMillis();
     Map map = toMap(s);
     Set<String> keys = map.keySet();
     for(String key: keys){
       System.out.println("key:"+key+"--value:"+map.get(key));
     }
     long b = System.currentTimeMillis();
     System.out.println(b-a);
   }
   
}

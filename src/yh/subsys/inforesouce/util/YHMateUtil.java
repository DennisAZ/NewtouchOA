package yh.subsys.inforesouce.util;

import java.util.HashMap;
import java.util.Map;


/**
 * 元数据工具类
 * @author qwx110
 *
 */
public class YHMateUtil{
  
  private  static Map<String, String> mateMap =new HashMap<String, String>();;


  public static Map<String, String> getMateMap() {
    return mateMap;
  }

  public static void setMateMap(Map<String, String> mateMap) {
    YHMateUtil.mateMap = mateMap;
  }

  /**
   * <p>1.查找父元素   *    2.查找子元素  </p>
   *    用空替代所有的父元素如：M1-128_,M1-128_1,M1-128_2,M1-128_1_V102替换父元素后变成1,2,1_V102
   * @param str    传入的字符串
   * @param profix 需要替换的的前缀
   * @return
   */
  
  public static String findSub(String str, String profix){
    String after = str.replace(profix, "");
    return after.substring(0, after.length());
  }
  
  /**
   * <p>过滤处profix父类的子类与值域</p>
   * @param str 传入的字符串
   * @param profix 需要替换的的前缀

   * @return
   */
  public  static String filterSub(String str, String profix,String userId){    
    String[] sub = str.split(",");
    String subClass = "";       //子元素
    String value = "";          //值域
    for(int i=0; i<sub.length; i++){      
      if(YHStringUtil.isEmpty(sub[i])){
        continue;
      }     
      if(sub[i].indexOf("_")==-1 && YHStringUtil.isNotEmpty(sub[i]) && !"|".equalsIgnoreCase(sub[i])){//“_”过滤子元素下的值域 如1_V102 就是子元素下的值域，添上sub[i].indexOf("_")==-1 就过滤掉了
        if(sub[i].indexOf("V")== -1){   //"如果包含V字，说明是值域" 这句判断不是值域
           subClass += sub[i]+",";
        }else{
           value += sub[i].replace("V", "") +",";
        }
      }
    }   
    // 如果mateMap.get(userId+"_"+profix+"sub");获得的K 等于mateMap.put(userId+"_"+profix+"sub");的K，说明取得值相等
    mateMap.put(userId+"_"+profix+"sub", subClass.substring(0, subClass.lastIndexOf(",")==-1?0:subClass.lastIndexOf(","))); 
    mateMap.put(userId+"_"+profix+"rage", value.substring(0, value.lastIndexOf(",")==-1?0:value.lastIndexOf(",")));
    return subClass;
  }
  
  /**
   * 查找出一个串的所有的父类的子类，值域（迭代）（带V的是值域）
   * @param str  mate_show表中的所有的串 如：M1-128_,M1-128_1,M1-128_2,M1-128_1_V102,M1-128_V103,M1-128_V104,|M2-129_,
   * 第一次过滤格式 (调用findSub方法)1, 2, 1_V102, V103, V104（第二次过滤调用filterSub方法）1,2, 103,104 加rage是值域，加sub是子元素 
   * 过滤后获取child分别是 1,2,103,104 在回调self方法  通过这次取的值  就可以取出子元素下的值域了如：M1-128_1_V102 第一次过滤成V102
   * 第二次过滤 把V过滤变成 102 说明是子元素下的值域
   * @param profix mate_show父元素串
   */
  public  static void self(String str, String profix, String userId){
    String findsubs = findSub(str, profix); 
    String subs = filterSub(findsubs, profix, userId);     
    if(subs != null && subs !="," && subs.length()>0){
      String[] child = subs.split(",");
      for(int i=0; i<child.length; i++){
        if(YHStringUtil.isEmpty(child[i])){
          continue;
        }
          self(str, profix+child[i]+"_", userId);
      }
    }
  } 
  
  /**
   * 查找所有这个用户选择的要显示的所有的顶级父类id串
   * @param parent <p>传入进来的组合的包含父类seq_id的字符串
   *                  字符串格式为MXX-12_,MXX-23_,.......等
   *                  MXX是编号,12是seq_id</p>
   * @return   返回seq_id串,如:12,23,.......
   */
  public static String findParents(String parent){
    String newPIds ="";
    if(YHStringUtil.isNotEmpty(parent)){
      parent = parent.replaceAll("_", "");
      String[] ps = parent.split(",");
      for(int i=0; i<ps.length; i++){
        String astr = ps[i].substring(ps[i].lastIndexOf("-")+1, ps[i].length());
        if(YHStringUtil.isNotEmpty(astr)){
          newPIds += astr +",";
        }
      }
      
      return newPIds.substring(0, newPIds.lastIndexOf(",")==-1?0:newPIds.lastIndexOf(","));
    }
    return null;
  }
  
  public static void main(String[] args){    
    /*String test= "M232222-12_,M204-14_,M250-16_";
    test = mu.findParents(test);
    //System.out.println(test);*/
    
    String teststr = "M1----A---------128_,M1-128_V103,M1-128_V104,M1-128_12,M1-128_15,M1-128_178|M1-129_,M1-129_11,M1-129_V51,M1-129_2,M1-129_27,M1-129_1";
      YHMateUtil.self(teststr, "M1-129_","123");
    //System.out.println("子元素为:"+YHMateUtil.getMateMap().get("123_M1-129_sub"));
    //System.out.println("值域是为:"+YHMateUtil.getMateMap().get("123_M1-129_rage"));
  }
}

package yh.core.oaknow.util;
import java.util.List;

import yh.core.oaknow.data.YHCategoriesType;
import yh.core.oaknow.data.YHOAAsk;
import yh.core.util.YHOut;
/**
 * 转换为json
 * @author qwx110
 *
 */
public class YHOAToJsonUtil{
  
  @SuppressWarnings("unchecked")
  public static String toJson(List<YHOAAsk> list){
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    if(list != null){
      for(int i=0; i<list.size(); i++){        
        if(i != list.size()-1){
          sb.append(((YHOAAsk)list.get(i)).toString()).append(",");
        }else{
          sb.append(((YHOAAsk)list.get(list.size()-1)).toString());
        }
      }
    }
    sb.append("]");
    //YHOut.println(sb.toString()+">>>>");
    return sb.toString();
  }
  
  public static String toJsonTwo(List<YHCategoriesType> list){
    StringBuffer sb = new StringBuffer();
    sb.append("[");
      if(list != null && list.size() != 0){
         for(int i=0; i < list.size(); i++){
            if(i < list.size()-1){
               sb.append(list.get(i).toString()).append(",");
            }else{
              sb.append(list.get(list.size()-1).toString());
            }
         }
      }
    sb.append("]");
    //YHOut.println(sb.toString()+"****************");
    return sb.toString();
  }
}

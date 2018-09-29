package yh.subsys.jtgwjh.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import yh.core.util.YHUtility;

public class YHDocUtil {

  public static Map<String, String> attach2Dept(Map<String, String> attachMap){
    
    Map<String, String> deptMap = new HashMap<String, String>();
    
    Set<String> key = attachMap.keySet();
    Iterator<String> iKeys =  key.iterator();
    while(iKeys.hasNext()){
      
      String keyStr = (String) iKeys.next();
      String valueStr = (String) attachMap.get(keyStr);
      String valueArray[] = valueStr.split(",");
      for(int i = 0; i < valueArray.length;i++){
        String attachStr = deptMap.get(valueArray[i]);
        if(YHUtility.isNullorEmpty(attachStr)){
          deptMap.put(valueArray[i], keyStr);
        }
        else{
          if(!isHaveAttach(attachStr, keyStr)){
            deptMap.put(valueArray[i], attachStr+","+keyStr);
          }
        }
      }
    }
    return deptMap;
  }
  
  public static boolean isHaveAttach(String attachStr, String keyStr){
    
    String attachArray[] = attachStr.split(",");
    for(int i = 0; i < attachArray.length;i++){
      if(keyStr.equals(attachArray[i])){
        return true;
      }
    }
    return false;
  }
  
  /**
   * 判断 文件是否存在
   * 
   * @param savePath
   * @param fileExtName
   * @return
   * @throws IOException
   */
  public static boolean getExist(String savePath, String fileExtName)
      throws IOException {
    String filePath = savePath + File.separator + fileExtName;
    if (new File(filePath).exists()) {
      return true;
    }
    return false;
  }
}

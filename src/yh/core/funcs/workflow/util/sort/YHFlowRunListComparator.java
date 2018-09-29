package yh.core.funcs.workflow.util.sort;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

public class YHFlowRunListComparator  implements Comparator{
  public int compare(Object arg0, Object arg1) {
    // TODO Auto-generated method stub
     Map map1 = (Map) arg0;
    Map map2 = (Map) arg0;
    Date beginTime1 = (Date) map1.get("beginTime");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date beginTime2 = (Date) map2.get("beginTime");
    try {
      if (beginTime1.compareTo(beginTime2) <  0){
//        System.out.println(sdf.format(beginTime1));
//        System.out.println(sdf.format(beginTime2));
        return 0;
      }
      return 1;
    } catch (Exception ex){
       //throw ex;
      ex.printStackTrace();
    }
    return 0;
    
  }
  
}


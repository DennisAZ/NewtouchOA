package yh.user.taiji.system;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Map;

public class YHSysLogComparaor implements Comparator<Map> {

  @Override
  public int compare(Map map1, Map map2) {
    // TODO Auto-generated method stub
    Timestamp time1 = (Timestamp)map1.get("time");
    Timestamp time2 =(Timestamp)map2.get("time");
    if (time1.compareTo(time2) < 0) {
      return 1;
    } 
    return 0;
  }

}

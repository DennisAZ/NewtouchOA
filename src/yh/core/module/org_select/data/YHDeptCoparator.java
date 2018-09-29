package yh.core.module.org_select.data;

import java.util.Comparator;
import java.util.Map;

import yh.core.funcs.person.data.YHUserOnline;

public class YHDeptCoparator implements Comparator<YHUserOnline> {
  private Map<Integer, String> deptNoMap = null;
  
  public YHDeptCoparator(Map<Integer, String> deptNoMap) {
    this.deptNoMap = deptNoMap;
  }
  public int compare(YHUserOnline u1, YHUserOnline u2) {
    String deptNo1 = deptNoMap.get(u1.getUserId());
    String deptNo2 = deptNoMap.get(u2.getUserId());
    
    return deptNo1.compareTo(deptNo2);
  }
}

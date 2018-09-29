package yh.core.module.org_select.act;

import java.util.Comparator;

import yh.core.funcs.dept.data.YHDepartment;
import yh.core.util.YHUtility;

public class YHDepartmentComparator implements Comparator {
  public int compare(Object arg0, Object arg1) {
    // TODO Auto-generated method stub
    YHDepartment d1 = (YHDepartment)arg0;
    YHDepartment d2 = (YHDepartment)arg1;
    String deptNo1 = d1.getDeptNo();
    String deptNo2 = d2.getDeptNo();
    if (YHUtility.isInteger(deptNo1) 
        && YHUtility.isInteger(deptNo2)) {
      int dept1 = Integer.parseInt(deptNo1);
      int dept2 = Integer.parseInt(deptNo2);
      if (dept1 == dept2) {
        String deptName1 = d1.getDeptName();
        String deptName2 = d2.getDeptName();
        if (deptName2.compareTo(deptName1) > 0) {
          return 0;
        } else {
          return 1;
        }
      }
      if (dept1 < dept2) {
        return 0;
      } else {
        return 1;
      }
    }
    return 1;
  }

}

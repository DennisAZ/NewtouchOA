package yh.core.funcs.workflow.util.sort;

import java.util.Comparator;

import yh.core.funcs.workflow.data.YHFlowFormType;

public class YHFormVersionComparator implements Comparator {
  public int compare(Object arg0, Object arg1) {
    YHFlowFormType ft1 = (YHFlowFormType) arg0;
    YHFlowFormType ft2 = (YHFlowFormType) arg0;
    
    if (ft1.getVersionNo() > ft2.getVersionNo()) {
      return 1;
    }
    return 0;
  }

}

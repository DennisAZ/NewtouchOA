package yh.core.funcs.workflow.util.sort;

import java.util.Comparator;

import yh.core.funcs.workflow.data.YHFlowType;

public class YHFlowTypeComparator implements Comparator{
  public int compare(Object arg0, Object arg1) {
    // TODO Auto-generated method stub
    YHFlowType fp1 = (YHFlowType) arg0;
    YHFlowType fp2 = (YHFlowType) arg1;
    if( fp1.getFlowNo() < fp2.getFlowNo()){
      return 0;
    }
    return 1;
  }
}
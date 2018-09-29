package yh.core.funcs.doc.util.sort;

import java.util.Comparator;

import yh.core.funcs.doc.data.YHDocFlowType;

public class YHFlowTypeComparator implements Comparator{
  public int compare(Object arg0, Object arg1) {
    // TODO Auto-generated method stub
    YHDocFlowType fp1 = (YHDocFlowType) arg0;
    YHDocFlowType fp2 = (YHDocFlowType) arg1;
    if( fp1.getFlowNo() < fp2.getFlowNo()){
      return 0;
    }
    return 1;
  }
}
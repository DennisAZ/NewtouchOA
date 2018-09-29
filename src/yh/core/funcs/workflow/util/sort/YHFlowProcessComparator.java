package yh.core.funcs.workflow.util.sort;

import java.util.Comparator;

import yh.core.funcs.workflow.data.YHFlowProcess;

public class YHFlowProcessComparator implements Comparator{
  public int compare(Object arg0, Object arg1) {
    // TODO Auto-generated method stub
    YHFlowProcess fp1 = (YHFlowProcess) arg0;
    YHFlowProcess fp2 = (YHFlowProcess) arg1;
    if( fp1.getPrcsId() < fp2.getPrcsId()){
      return 0;
    }
    return 1;
  }
  
}

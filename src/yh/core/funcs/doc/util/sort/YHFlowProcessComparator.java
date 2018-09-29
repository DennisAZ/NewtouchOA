package yh.core.funcs.doc.util.sort;

import java.util.Comparator;

import yh.core.funcs.doc.data.YHDocFlowProcess;

public class YHFlowProcessComparator implements Comparator{
  public int compare(Object arg0, Object arg1) {
    // TODO Auto-generated method stub
    YHDocFlowProcess fp1 = (YHDocFlowProcess) arg0;
    YHDocFlowProcess fp2 = (YHDocFlowProcess) arg1;
    if( fp1.getPrcsId() < fp2.getPrcsId()){
      return 0;
    }
    return 1;
  }
  
}

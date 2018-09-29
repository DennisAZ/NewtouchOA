package yh.core.funcs.doc.util.sort;

import java.util.Comparator;

import yh.core.funcs.doc.data.YHDocFlowRunFeedback;

public class YHFeedbackComparator implements Comparator {

  public int compare(Object arg0, Object arg1) {
    // TODO Auto-generated method stub
    YHDocFlowRunFeedback feedback = (YHDocFlowRunFeedback) arg0;
    YHDocFlowRunFeedback feedback1 = (YHDocFlowRunFeedback) arg1;
    if( feedback.getEditTime().compareTo(feedback1.getEditTime()) > 0 ){
      return 0;
    }
    return 1;
  }

}

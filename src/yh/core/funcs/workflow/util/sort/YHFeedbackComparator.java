package yh.core.funcs.workflow.util.sort;

import java.util.Comparator;

import yh.core.funcs.workflow.data.YHFlowRunFeedback;

public class YHFeedbackComparator implements Comparator {

  public int compare(Object arg0, Object arg1) {
    // TODO Auto-generated method stub
    YHFlowRunFeedback feedback = (YHFlowRunFeedback) arg0;
    YHFlowRunFeedback feedback1 = (YHFlowRunFeedback) arg1;
    if( feedback.getEditTime().compareTo(feedback1.getEditTime()) > 0 ){
      return 0;
    }
    return 1;
  }

}

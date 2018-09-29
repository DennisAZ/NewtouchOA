package yh.core.funcs.workflow.util.sort;

import java.util.Comparator;

import yh.core.funcs.workflow.data.YHFlowFormItem;

public class YHFlowItemComparator implements Comparator {

  public int compare(Object arg0, Object arg1) {
    // TODO Auto-generated method stub
    YHFlowFormItem item = (YHFlowFormItem) arg0;
    YHFlowFormItem item1 = (YHFlowFormItem) arg1;
    if(item.getItemId() < item1.getItemId()){
      return 0;
    }
    return 1;
  }

}

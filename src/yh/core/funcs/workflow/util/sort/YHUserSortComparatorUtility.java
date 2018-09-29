package yh.core.funcs.workflow.util.sort;

import java.util.Comparator;

import yh.core.funcs.person.data.YHPerson;


public class YHUserSortComparatorUtility implements Comparator{
  private String formUserStr;
  public YHUserSortComparatorUtility(String formUserStr){
    this.formUserStr = formUserStr;
  }
  public int compare(Object arg0, Object arg1) {
    // TODO Auto-generated method stub
    YHPerson p = (YHPerson) arg0;
    YHPerson p2 = (YHPerson) arg1;
    int j = formUserStr.indexOf(p.getUserName());
    int k = formUserStr.indexOf(p2.getUserName());
    if( k > j){
      return 0;
    }
    return 1;
  }
  
}
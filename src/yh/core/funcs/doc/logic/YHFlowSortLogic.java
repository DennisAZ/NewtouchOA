package yh.core.funcs.doc.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.data.YHDocFlowFormType;
import yh.core.funcs.doc.data.YHDocFlowSort;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHFlowSortLogic {
  public List<YHDocFlowSort> getFlowSort(Connection conn) throws Exception{
    List<YHDocFlowSort> list = new ArrayList();
    String query = "select * from "+ YHWorkFlowConst.FLOW_SORT +" ORDER BY SORT_NO ";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()) {
        int seqId = rs.getInt("SEQ_ID");
        int sortNo = rs.getInt("SORT_NO");
        String sortName = rs.getString("SORT_NAME");
        int sortParent = rs.getInt("SORT_PARENT");
        int deptId = rs.getInt("DEPT_ID");
        String haveChild = rs.getString("HAVE_CHILD");
        YHDocFlowSort flowSort = new YHDocFlowSort();
        flowSort.setSeqId(seqId);
        flowSort.setDeptId(deptId);
        flowSort.setSortNo(sortNo);
        flowSort.setSortParent(sortParent);
        flowSort.setSortName(sortName);
        flowSort.setHaveChild(haveChild);
        list.add(flowSort);
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return list;
  }
  public YHDocFlowSort getFlowSortById(Connection conn , int sortId) throws Exception{
    String query = "select * from "+ YHWorkFlowConst.FLOW_SORT +" where seq_id="+ sortId +" ORDER BY SORT_NO ";
    Statement stm = null;
    ResultSet rs = null;
    YHDocFlowSort flowSort = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if  (rs.next()) {
        int seqId = rs.getInt("SEQ_ID");
        int sortNo = rs.getInt("SORT_NO");
        String sortName = rs.getString("SORT_NAME");
        int sortParent = rs.getInt("SORT_PARENT");
        int deptId = rs.getInt("DEPT_ID");
        String haveChild = rs.getString("HAVE_CHILD");
        
        flowSort = new YHDocFlowSort();
        flowSort.setSeqId(seqId);
        flowSort.setDeptId(deptId);
        flowSort.setSortNo(sortNo);
        flowSort.setSortParent(sortParent);
        flowSort.setSortName(sortName);
        flowSort.setHaveChild(haveChild);
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return flowSort;
  }
  public List<YHDocFlowSort> getFlowSortByIds(Connection conn , String sortId) throws Exception{
    String query = "select * from "+ YHWorkFlowConst.FLOW_SORT +" where seq_id in ("+ sortId +") ORDER BY SORT_NO ";
    Statement stm = null;
    ResultSet rs = null;
    ArrayList<YHDocFlowSort> list   = new ArrayList();
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while  (rs.next()) {
        int seqId = rs.getInt("SEQ_ID");
        int sortNo = rs.getInt("SORT_NO");
        String sortName = rs.getString("SORT_NAME");
        int sortParent = rs.getInt("SORT_PARENT");
        int deptId = rs.getInt("DEPT_ID");
        String haveChild = rs.getString("HAVE_CHILD");
        
        YHDocFlowSort flowSort = new YHDocFlowSort();
        flowSort.setSeqId(seqId);
        flowSort.setDeptId(deptId);
        flowSort.setSortNo(sortNo);
        flowSort.setSortParent(sortParent);
        flowSort.setSortName(sortName);
        flowSort.setHaveChild(haveChild);
        list.add(flowSort);
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return list;
  }
  public List<YHDocFlowSort> getFlowSortByDept(Connection conn , YHPerson u) throws Exception{
    List<YHDocFlowSort> list = new ArrayList();
    YHWorkFlowUtility w = new YHWorkFlowUtility();
    String query = "select * from "+ YHWorkFlowConst.FLOW_SORT +" ";
    query += " ORDER BY SORT_NO ";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()) {
        int deptId = rs.getInt("DEPT_ID");
        if (!w.isHaveSortRight(deptId, u, conn)) {
          continue;
        }
        int seqId = rs.getInt("SEQ_ID");
        int sortNo = rs.getInt("SORT_NO");
        String sortName = rs.getString("SORT_NAME");
        int sortParent = rs.getInt("SORT_PARENT");
        String haveChild = rs.getString("HAVE_CHILD");
        YHDocFlowSort flowSort = new YHDocFlowSort();
        flowSort.setSeqId(seqId);
        flowSort.setDeptId(deptId);
        flowSort.setSortNo(sortNo);
        flowSort.setSortParent(sortParent);
        flowSort.setSortName(sortName);
        flowSort.setHaveChild(haveChild);
        list.add(flowSort);
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return list;
  }
  public List<YHDocFlowSort> getFlowSortByDept(Connection conn , int deptId2) throws Exception{
    List<YHDocFlowSort> list = new ArrayList();
    String query = "select * from "+ YHWorkFlowConst.FLOW_SORT +" where DEPT_ID = "+ deptId2 +" ORDER BY SORT_NO ";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()) {
        int seqId = rs.getInt("SEQ_ID");
        int sortNo = rs.getInt("SORT_NO");
        String sortName = rs.getString("SORT_NAME");
        int sortParent = rs.getInt("SORT_PARENT");
        int deptId = rs.getInt("DEPT_ID");
        String haveChild = rs.getString("HAVE_CHILD");
        YHDocFlowSort flowSort = new YHDocFlowSort();
        flowSort.setSeqId(seqId);
        flowSort.setDeptId(deptId);
        flowSort.setSortNo(sortNo);
        flowSort.setSortParent(sortParent);
        flowSort.setSortName(sortName);
        flowSort.setHaveChild(haveChild);
        list.add(flowSort);
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return list;
  }
  public YHDocFlowSort getFlowSortById(int sortId , Connection conn) throws Exception{
    YHORM orm = new YHORM();
    return (YHDocFlowSort)orm.loadObjSingle(conn, YHDocFlowSort.class, sortId);
  }
  /**
   * 组织导航菜单
   * @param id
   * @return
   * @throws Exception
   */
  public StringBuffer getSortJson(int id , Connection conn , YHPerson u) throws Exception{
    StringBuffer sb = new StringBuffer();
    YHFlowFormLogic ffl = new YHFlowFormLogic();
    String seqIds = ffl.getIdBySort(conn, id);
    ArrayList<YHDocFlowFormType> typeList = (ArrayList<YHDocFlowFormType>) ffl.getFlowFormType(conn, seqIds);
    int count = 0;
    YHWorkFlowUtility w = new YHWorkFlowUtility();
    for(YHDocFlowFormType ftTmp : typeList){
      int deptId = ftTmp.getDeptId();
      if (!w.isHaveRight(deptId, u, conn)) {
        continue;
      }
      count ++;
      if(ftTmp.getFormName() == null){
        sb.append("{title:' '");
      }else{ 
        sb.append("{title:'" + ftTmp.getFormName() + "                                      '");
      }
      sb.append(",icon:imgPath + '/edit.gif'");
      sb.append(",action:actionFuntion");
      sb.append(",iconAction:iconActionFuntion");
      sb.append(",sortId:'" + id +"'");
      sb.append(",extData:'" + ftTmp.getSeqId() +"'}," );
    }
    if(count > 0){
      sb.deleteCharAt(sb.length() - 1);
    }  
    return sb;
  }
  /**
   * 根据流程分类id取得下面的流程数量
   * @param flowShort
   * @param conn
   * @return
   * @throws Exception 
   */
  public int getFlowTypeCount (int flowShort , Connection conn) throws Exception {
    int count = 0 ;
    String query = "select count(*) as count from "+ YHWorkFlowConst.FLOW_TYPE +" where"
      + " FLOW_SORT = " + flowShort;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        count = rs.getInt("count");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return count;
  }
}

package yh.core.funcs.workflow.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowFormType;
import yh.core.funcs.workflow.data.YHFlowSort;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHFlowSortLogic {
  public List<YHFlowSort> getFlowSort(Connection conn) throws Exception{
    List<YHFlowSort> list = new ArrayList();
    String query = "select * from oa_fl_sort ORDER BY SORT_NO ";
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
        YHFlowSort flowSort = new YHFlowSort();
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
  public YHFlowSort getFlowSortById(Connection conn , int sortId) throws Exception{
    String query = "select * from oa_fl_sort where seq_id="+ sortId +" ORDER BY SORT_NO ";
    Statement stm = null;
    ResultSet rs = null;
    YHFlowSort flowSort = null;
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
        
        flowSort = new YHFlowSort();
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
  public List<YHFlowSort> getFlowSortByIds(Connection conn , String sortId) throws Exception{
    String query = "select * from oa_fl_sort where seq_id in ("+ sortId +") ORDER BY SORT_NO ";
    Statement stm = null;
    ResultSet rs = null;
    ArrayList<YHFlowSort> list   = new ArrayList();
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
        
        YHFlowSort flowSort = new YHFlowSort();
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
  public List<YHFlowSort> getFlowSortByDept(Connection conn , YHPerson u) throws Exception{
    List<YHFlowSort> list = new ArrayList();
    YHWorkFlowUtility w = new YHWorkFlowUtility();
    String query = "select * from oa_fl_sort ";
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
        YHFlowSort flowSort = new YHFlowSort();
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
  public List<YHFlowSort> getFlowSortByDept(Connection conn , int deptId2) throws Exception{
    List<YHFlowSort> list = new ArrayList();
    String query = "select * from oa_fl_sort where DEPT_ID = "+ deptId2 +" ORDER BY SORT_NO ";
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
        YHFlowSort flowSort = new YHFlowSort();
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
  public YHFlowSort getFlowSortById(int sortId , Connection conn) throws Exception{
    YHORM orm = new YHORM();
    return (YHFlowSort)orm.loadObjSingle(conn, YHFlowSort.class, sortId);
  }
  
  /**
   * 组织导航菜单
   * @param id
   * @return
   * @throws Exception
   */
  public String getSortJson(int id , Connection conn , YHPerson u , String contextPath) throws Exception{
    StringBuffer sb = new StringBuffer();
    YHFlowFormLogic ffl = new YHFlowFormLogic();
    String seqIds = ffl.getIdBySort(conn, id);
    ArrayList<YHFlowFormType> typeList = (ArrayList<YHFlowFormType>) ffl.getFlowFormType(conn, seqIds);
    YHWorkFlowUtility w = new YHWorkFlowUtility();
    for(YHFlowFormType ftTmp : typeList){
      int deptId = ftTmp.getDeptId();
      if (!w.isHaveRight(deptId, u, conn)) {
        continue;
      }
      int nodeId = ftTmp.getSeqId();
      String name = YHWorkFlowUtility.encodeSpecial(ftTmp.getFormName());
      sb.append("{");
      sb.append("nodeId:\"F" + nodeId + ":"+ id +"\"");
      sb.append(",name:\"" + name + "\"");
      sb.append(",isHaveChild:0");
      sb.append(",imgAddress:\""+ contextPath +"/core/funcs/workflow/flowtype/img/workflow.gif\"");
      sb.append("},");
    }
    return sb.toString();
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
    String query = "select count(*) as count from oa_fl_type where"
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
  public boolean isHaveSon (int flowShort , Connection conn) throws Exception {
    String query = "select 1 from oa_fl_sort where"
      + " SORT_PARENT = " + flowShort;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        return true;
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return false;
  }
  /**
   * 取得下级级次树.
   * @param deptId
   * @param conn
   * @return String [{value :'dd', text:'dds'},{value:'dd', text:'dd'}]
   * @throws Exception
   */
  public String getSortTreeJson( Connection conn ,YHPerson user , int sortId , boolean flag) throws Exception{
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    String deptIds = user.getDeptId() + ",0";
    String deptOther = user.getDeptIdOther();
    if (!YHUtility.isNullorEmpty(deptOther) ) {
      deptIds = deptIds + "," + deptOther;
      deptIds = YHWorkFlowUtility.getOutOfTail(deptIds);
    }
    this.getSortTree(deptIds , sortId , sb, 0 , conn , user , flag);
    if(sb.charAt(sb.length() - 1) == ','){
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
  public void getSortTree(String  depts, int sortId , StringBuffer sb , int level , Connection conn,YHPerson user, boolean flag2) throws Exception{
    List<Map> list = new ArrayList();
    String query = "select SEQ_ID , SORT_NAME , SORT_NO  from oa_fl_sort where SORT_PARENT =" + sortId ;
    if (!user.isAdminRole()) {
      query += " AND  DEPT_ID IN (" + depts + ") " ;
    }
    query += " order by SORT_NO";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        String sortName = rs.getString("SORT_NAME");
        int seqId = rs.getInt("SEQ_ID");
        int sortNo = rs.getInt("SORT_NO");
        Map map = new HashMap();
        map.put("sortName", sortName);
        map.put("seqId", seqId);
        map.put("sortNo", sortNo);
        list.add(map);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    for(int i = 0; i < list.size(); i++){
      String flag = "&nbsp;├";
      if (flag2) {
        flag = "&nbsp;&nbsp;&nbsp;&nbsp;";
      }
      if(i == list.size() - 1 ){
        flag = "&nbsp;└";
        if (flag2) {
          flag = "&nbsp;&nbsp;&nbsp;&nbsp;";
        }
      }
      String tmp = "";
      for(int j = 0 ;j < level ; j++){
        if (flag2) {
          tmp += "&nbsp;&nbsp;&nbsp;&nbsp;";
        }else {
          tmp += "&nbsp;│";
        }
      }
      flag = tmp + flag;
      int flowTypeCount = 0 ;
      Map dp = list.get(i);
      int seqId = (Integer)dp.get("seqId");
      boolean isHaveChild = false;
      if (flag2) {
        flowTypeCount = this.getFlowTypeCount(seqId, conn);
        isHaveChild = this.isHaveSon(seqId, conn);
      }
      int sortNo = (Integer)dp.get("sortNo");
      String sortName = (String)dp.get("sortName");
      sb.append("{");
      sb.append("text:'" + flag + YHUtility.encodeSpecial(sortName) + "',");
      sb.append("value:" + seqId + "," );
      sb.append("sortNo:\"" + flag + sortNo + "\",");
      sb.append("flowTypeCount:" + flowTypeCount+ ","  );
      sb.append("isHaveChild:" + isHaveChild + ","  );
      sb.append("sortParent:" + sortId );
      sb.append("},");
      this.getSortTree(depts, seqId , sb, level + 1 , conn , user , flag2);
    }
    return;
  }
}

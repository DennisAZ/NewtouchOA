package yh.subsys.inforesouce.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import yh.core.funcs.dept.data.YHDepartment;
import yh.core.util.YHOut;
import yh.core.util.db.YHDBUtility;
import yh.subsys.inforesouce.data.YHMateType;

/**
 * 元数据树形插件
 * @author qwx110
 *
 */
public class YHMateTreePlugLogic{
  
  /**
   * 查找父节点，树默认显示父节点
   * @param conn
   * @return
   * @throws Exception 
   */
  public List<YHMateType> findMates(Connection dbConn, int seqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;   
    List<YHMateType> mates = new ArrayList<YHMateType>();
    String sql = "select SEQ_ID, " +
                        "NUMBER_ID,  " +
                        "CHNAME, " +
                        "ENNAME, " +
                        "DEFINEE, " +
                        "AIM, " +
                        "CONSTRAINTT, " +
                        "REPEATE, " +
                        "ELEMENT_TYP, " +
                        "TYPE_ID, " +
                        "CODE_ID, " +
                        "VALUE_RANGE, " +
                        "DEF_VALUE, " +
                        "ELEM_ID, " +
                        "PARENT_ID, " +
                        "NOTE from oa_mate_kind where PARENT_ID=" + seqId;
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();     
      while(rs.next()){
        YHMateType amate = new YHMateType(); 
        amate.setSeqId(rs.getInt("SEQ_ID"));
        amate.setNumberId(rs.getString("NUMBER_ID"));
        amate.setcNname(rs.getString("CHNAME"));
        amate.seteNname(rs.getString("ENNAME"));
        amate.setDefine(rs.getString("DEFINEE"));
        amate.setAim(rs.getString("AIM"));
        amate.setConstraint(rs.getString("CONSTRAINTT"));
        amate.setRepeat(rs.getString("REPEATE"));
        amate.setElement_type(rs.getString("ELEMENT_TYP"));
        amate.setTypeId(rs.getString("TYPE_ID"));
        amate.setCodeId(rs.getString("CODE_ID"));
        amate.setRangeId(rs.getString("VALUE_RANGE"));
        amate.setLessValue(rs.getString("DEF_VALUE"));
        amate.setElementId(rs.getString("ELEM_ID"));
        amate.setParentId(rs.getString("PARENT_ID"));
        amate.setNote(rs.getString("NOTE"));  
        mates.add(amate);
      }
    } catch (SQLException e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
   return mates;
  }
  
  /**
   * 点击父元素后显示的子元素
   * @param conn
   * @param pId
   * @return
   */
  public List<YHMateType> findSubNode(Connection conn, int pId){
    return null;
  }
  
  /**
   * 是否是有子元素
   * @param dbConn
   * @param seqId  这个元数据的主键
   * @return
   * @throws SQLException 
   */
  public boolean isHaveSubMate(Connection dbConn, int seqId) throws SQLException{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = "select * from oa_mate_kind where PARENT_ID ="+ seqId; 
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        return true;
      }
    } catch (SQLException e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return false;
  }
 
  /**
   * 把一个集合转换为串
   * @param dbConn
   * @param mates
   * @param contextPath
   * @return
   * @throws SQLException
   */
  public String mate2String(Connection dbConn, List<YHMateType> mates, String contextPath) throws SQLException{
    StringBuffer sb = new StringBuffer();
    String imgAddress = contextPath +  "/core/styles/style1/img/dtree/node_dept.gif";
    sb.append("[");
      if(mates != null && mates.size()>0){
        for(int i=0; i<mates.size(); i++){
          sb.append("{");
          sb.append("nodeId:\"" + mates.get(i).getSeqId() + "\"");
          sb.append(",name:\"" + mates.get(i).getcNname() + "\"");
          sb.append(",isHaveChild:" + isHaveSubMate(dbConn, mates.get(i).getSeqId()) + "");
          sb.append(",extData:"+ true);
          sb.append(",imgAddress:\"" + imgAddress + "\"");
          if(i < mates.size()-1){
           sb.append("},");
          }else{
           sb.append("}");
          }
        }
      }
    sb.append("]"); 
    return sb.toString();
  }
  
  /**
   * 查询元数据树
   * @param dbConn
   * @param seqId
   * @param contextPath
   * @return
   * @throws Exception
   */
  public String findMateTree(Connection dbConn, int seqId, String contextPath) throws Exception{
    List<YHMateType> mates = findMates(dbConn, seqId);
    return mate2String(dbConn, mates, contextPath);
  }
  
  /**
   * 取得元数据的Json数据
   * @param mate
   * @param mates
   * @param sb
   * @param level
   */
  public void setMateSingle(YHMateType mate, List<YHMateType> mates, StringBuffer sb, int level){
    String mateName = mate.getcNname();
    int mateId = mate.getSeqId();
    boolean isChecked = false;
    String nbsp = "├";
    for(int i = 0 ;i < level;i++){
      nbsp = "&nbsp&nbsp" + nbsp;
    }
    sb.append("{");
    sb.append("mateName:'" + nbsp + mateName + "',");
    sb.append("mateId:'" + mateId + "',");
    sb.append("isChecked:" + isChecked) ;
    sb.append("},");
    
    level++;
    for(int i = 0 ;i < mates.size() ; i++){
      YHMateType  mat = (YHMateType) mates.get(i);
      if(String.valueOf(mateId).equalsIgnoreCase(mat.getParentId())){
        setMateSingle(mat, mates, sb, level);
      }
    }
  }
  
  /**
   * 取得部门的Json数据
   * @param depts
   * @param procId
   * @param deptId
   * @return
   * @throws Exception
   */
  public StringBuffer getMateJson(List<YHMateType> mates , int mateId) throws Exception{
    StringBuffer sb = new StringBuffer("[");
    YHMateType mate = new YHMateType();
    if(mateId != 0){
      for(int i = 0 ;i < mates.size();i ++){
        mate = (YHMateType) mates.get(i);
        if(mate.getSeqId() == mateId){
          break;
        }
      }
      this.setMateSingle(mate, mates, sb , 0);
    }else{
      for(int i = 0 ;i < mates.size();i ++){
        mate = (YHMateType) mates.get(i);
        if("0".equalsIgnoreCase(mate.getParentId())){
          this.setMateSingle(mate, mates, sb , 0);
        }
      }
    }    
    sb.deleteCharAt(sb.length() - 1);    
    sb.append("]");
    //YHOut.println(sb.toString());
    return sb;
  }
  
  /**
   * 获得所有的元数据
   * @param dbConn
   * @return
   * @throws Exception
   */
  public List<YHMateType> findMateList(Connection dbConn) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;   
    List<YHMateType> mates = new ArrayList<YHMateType>();
    String sql = "select SEQ_ID, " +
                        "NUMBER_ID,  " +
                        "CHNAME, " +
                        "ENNAME, " +
                        "DEFINEE, " +
                        "AIM, " +
                        "CONSTRAINTT, " +
                        "REPEATE, " +
                        "ELEMENT_TYP, " +
                        "TYPE_ID, " +
                        "CODE_ID, " +
                        "VALUE_RANGE, " +
                        "DEF_VALUE, " +
                        "ELEM_ID, " +
                        "PARENT_ID, " +
                        "NOTE from oa_mate_kind ";
    //YHOut.println(sql);
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();     
      while(rs.next()){
        YHMateType amate = new YHMateType(); 
        amate.setSeqId(rs.getInt("SEQ_ID"));
        amate.setNumberId(rs.getString("NUMBER_ID"));
        amate.setcNname(rs.getString("CHNAME"));
        amate.seteNname(rs.getString("ENNAME"));
        amate.setDefine(rs.getString("DEFINEE"));
        amate.setAim(rs.getString("AIM"));
        amate.setConstraint(rs.getString("CONSTRAINTT"));
        amate.setRepeat(rs.getString("REPEATE"));
        amate.setElement_type(rs.getString("ELEMENT_TYP"));
        amate.setTypeId(rs.getString("TYPE_ID"));
        amate.setCodeId(rs.getString("CODE_ID"));
        amate.setRangeId(rs.getString("VALUE_RANGE"));
        amate.setLessValue(rs.getString("DEF_VALUE"));
        amate.setElementId(rs.getString("ELEM_ID"));
        amate.setParentId(rs.getString("PARENT_ID"));
        amate.setNote(rs.getString("NOTE"));  
        mates.add(amate);
      }
    } catch (SQLException e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
   return mates;
  }
}

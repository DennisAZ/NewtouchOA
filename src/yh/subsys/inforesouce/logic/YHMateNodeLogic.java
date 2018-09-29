package yh.subsys.inforesouce.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import yh.core.funcs.person.data.YHPerson;
import yh.core.util.db.YHDBUtility;
import yh.subsys.inforesouce.data.YHMateNode;

/**
 * 处理选择的树的选中的节点
 * @author qwx110
 *
 */
public class YHMateNodeLogic{
  
  /**
   * 插入选择树的节点
   * @param dbConn
   * @param node
   * @return
   * @throws Exception
   */
  public int saveAjax(Connection dbConn, YHMateNode node, String nodeType) throws Exception{    
    PreparedStatement ps = null;
    String sql = "insert into oa_mate_step(USER_ID, NODES, TAGNAME, NODE_TYPE) values(?,?,?,?)" ;   
    int k =0;   
    try{
      ps = dbConn.prepareStatement(sql);
      ps.setInt(1, node.getUserId());
      ps.setString(2, node.getNodes());
      ps.setString(3, node.getTagName());
      ps.setString(4, nodeType);
      k = ps.executeUpdate();
    } catch (SQLException e){      
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return k;
  } 
  
  /**
   * 删除某个用户下的所有选择的节点
   * @param dbConn
   * @param user
   * @return
   * @throws Exception
   */
  public int deleteNode(Connection dbConn, YHPerson user)throws Exception{
    PreparedStatement ps = null;
    String sql = "delete from oa_mate_step where USER_ID=" + user.getSeqId() ;   
    int k=0;
    try{
      ps = dbConn.prepareStatement(sql);
      k = ps.executeUpdate();
    } catch (Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return k;
  }
  
  /**
   * 查询某个用户下所有的标签名
   * @param dbConn
   * @param user
   * @return
   * @throws Exception
   */
  public List<YHMateNode> allTagName(Connection dbConn, YHPerson user, String nodeType)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = "select SEQ_ID, USER_ID, NODES, TAGNAME from oa_mate_step where USER_ID="+user.getSeqId()+" and node_type="+ nodeType +" order by SEQ_ID DESC"; 
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    List<YHMateNode> nodes = new ArrayList<YHMateNode>();
    while(rs.next()){
      YHMateNode node = new YHMateNode();
      node.setSeqId(rs.getInt("SEQ_ID"));
      node.setUserId(rs.getInt("USER_ID"));
      node.setNodes(rs.getString("NODES"));
      node.setTagName(rs.getString("TAGNAME"));
      nodes.add(node);
    }
    return nodes;    
  }
  
  /**
   * 把一个list转化为字符串类型
   * @param dbConn
   * @param user
   * @return
   * @throws Exception
   */
  public String tagName(Connection dbConn, YHPerson user, String nodeType)throws Exception{
    return toAString(allTagName(dbConn,user, nodeType ));
  }
  
  /**
   * 删除所选标签
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public int deleteTagName(Connection dbConn, int seqId)throws Exception{
    PreparedStatement ps = null;  
    int ok = 0;
    try{
      String sql = "delete from oa_mate_step where seqId=?"; 
      ps = dbConn.prepareStatement(sql);
      ps.setInt(1, seqId);
      ok = ps.executeUpdate();
    } catch (Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }   
    return ok;    
  }
  
  public String toAString(List<YHMateNode> nodes){
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    if(nodes != null && nodes.size()>0){
      for(int i=0; i<nodes.size(); i++){
        if(i < nodes.size()-1){
          sb.append(nodes.get(i).toString()).append(",");
        }else{
          sb.append(nodes.get(i).toString());
        }
      }
    }
    sb.append("]");
    return sb.toString();
  }
}

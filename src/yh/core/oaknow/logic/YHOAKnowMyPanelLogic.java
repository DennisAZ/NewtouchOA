package yh.core.oaknow.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yh.core.funcs.person.data.YHPerson;
import yh.core.oaknow.data.YHOAAsk;
import yh.core.oaknow.util.YHPageUtil;
import yh.core.oaknow.util.YHStringUtil;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
/**
 * oa管理面板
 * @author qwx110
 *
 */
public class YHOAKnowMyPanelLogic{
  /**
   * 查找oa知道的标题
   * @param dbConn
   * @return
   * @throws Exception
   */
  public String findOAName(Connection dbConn) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;   
    try{
      String sql = "select SYS_NAME"
                   +" from oa_wiki_info";
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        return rs.getString(1);
      }
    } catch (Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return "OA知道";
  } 
 
  /**
   * 查看我的问题的总数
   * @param dbConn
   * @param user
   * @return
   * @throws Exception
   */
  public int findMyAskCount(Connection dbConn, YHPerson user) throws Exception{
    String sql = "select COUNT(*) from oa_wiki_ask where CREATOR = " + user.getSeqId();
    PreparedStatement ps = null;
    ResultSet rs = null; 
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        return rs.getInt(1);
      }
    } catch (SQLException e){
      throw e;
    }
    finally{
      YHDBUtility.close(ps, rs, null);
    }
    return 0;
  }
  
  /**
   * oa管理面板我的问题分页
   * @param dbConn
   * @param user
   * @param pu
   * @return
   * @throws Exception
   */
  public List<YHOAAsk> findMyAsks(Connection dbConn, YHPerson user, YHPageUtil pu) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    List<YHOAAsk> asks = new ArrayList<YHOAAsk>();
    String sql = 
        " select SEQ_ID" 
        		+",ASK" 
        		+",CREATE_TIME" 
        		+",ASK_STATUS"         		
    +" from oa_wiki_ask"
    +" where creator =" + user.getSeqId() ;   
    //YHOut.println(sql);
    try{      
      ps = dbConn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
      ps.setMaxRows(pu.getCurrentPage() * pu.getPageSize());
      rs = ps.executeQuery();
      rs.first();   
      rs.relative((pu.getCurrentPage()-1) * pu.getPageSize() -1);  
      
      while(rs.next()){
        YHOAAsk ask = new YHOAAsk();
        ask.setSeqId(rs.getInt(1));
        ask.setAsk(YHStringUtil.subString(30, rs.getString(2)));
        ask.setCreateDate((Date)rs.getObject(3));
        ask.setStatus(rs.getInt(4));
        asks.add(ask);
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return  asks;
  }
 
  /**
   * 我所参与过的问题
   * @param dbConn
   * @param user
   * @param pu
   * @return
   * @throws Exception
   */
  public List<YHOAAsk> findMyReferenceAsks (Connection dbConn, YHPerson user, YHPageUtil pu) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<YHOAAsk> asks = new ArrayList<YHOAAsk>();   
    String ids = findMyAskIds(dbConn, user);
    if(YHUtility.isNullorEmpty(ids)){
      ids = "0";
    }
    String sql = "select SEQ_ID,CREATE_TIME,ASK_STATUS,ASK from oa_wiki_ask where SEQ_ID in (" + ids +")";
    //YHOut.println(sql);
    try{      
      ps = dbConn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);    
      ps.setMaxRows(pu.getCurrentPage() * pu.getPageSize());
      rs = ps.executeQuery();
      //rs.absolute( (pu.getCurrentPage()-1) * pu.getPageSize() +1); //跳到当前页的第一行
      rs.first();   
      rs.relative((pu.getCurrentPage()-1) * pu.getPageSize() -1);  
     
      while(rs.next()){
        YHOAAsk ask = new YHOAAsk();
        ask.setSeqId(rs.getInt(1));
        ask.setCreateDate((Date)rs.getObject(2));
        ask.setStatus(rs.getInt(3));
        ask.setAsk(YHStringUtil.subString(30, rs.getString(4)));
        asks.add(ask);
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return asks;
  }
  
  /**
   * 在回答的问题和评论中记录我参与的问题的ids串
   * @param dbConn
   * @param user
   * @return
   * @throws Exception
   */
  public String findMyAskIds(Connection dbConn, YHPerson user)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String ids = "";
    String sql = " select ASK_ID from oa_wiki_ask_answer where ANSWER_USER = "+ user.getSeqId() 
                 +" union "
                 +" select ASK_ID  from oa_wiki_comment where MEMBER =  " + user.getSeqId();
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        ids +=  rs.getInt("ASK_ID")+",";
      }
    } catch (Exception e){
      throw e;
    }finally{
      
    }
    return ids.substring(0,ids.lastIndexOf(",")==-1?0:ids.lastIndexOf(","));
  }
  
  /**
   * 我所参与过的问题个数(我回答的问题的个数加上我评论的问题的个数)
   * @param dbConn
   * @param user
   * @return
   * @throws Exception
   */
  public int findMyReferenceAsksCount(Connection dbConn, YHPerson user) throws Exception{  
    String ids = findMyAskIds(dbConn, user);
    int len = 0;
    if(!YHUtility.isNullorEmpty(ids)){
      String[] array = ids.split(",");
      len = array.length;
    }
    return len;
  }
  
  
  /**
   * 更新oa名字
   * @param dbConn
   * @param oaName
   * @return
   * @throws Exception
   */
  public int updateOAName(Connection dbConn, String  oaName) throws Exception{
    PreparedStatement ps = null;
    try{
      String sql = "update oa_wiki_info set SYS_NAME = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, oaName);
      //YHOut.println(sql);
      int k = ps.executeUpdate();
      return k;    
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
  }  
  
  /**
   * 更新或者插入
   * @param dbConn
   * @param oaName
   * @return
   * @throws Exception
   */
  public int updateOrSave(Connection dbConn, String  oaName) throws Exception{
    if(isNull(dbConn)==true){
      return insertOaName(dbConn, oaName);
    }else{
      return updateOAName(dbConn, oaName);
    }
  }
  
 /**
  * 看看是不是空
  * @param dbConn
  * @return
  * @throws Exception
  */
  public boolean isNull(Connection dbConn)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      String sql = "select  SYS_NAME from oa_wiki_info";
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        return false;
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return true;
  }
  
  /**
   * 插入oa名字
   * @param dbConn
   * @param oaName
   * @return
   * @throws Exception
   */
  public int insertOaName(Connection dbConn, String  oaName)throws Exception{
    PreparedStatement ps = null;
    try{
      String sql = "insert into oa_wiki_info(SYS_NAME) values(?)";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, oaName);
      //YHOut.println(sql);
      int k = ps.executeUpdate();
      return k;    
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
  }
  /**
   * 用户管理
   * @param dbConn
   * @param userKey
   * @param pu
   * @return
   * @throws Exception
   */
  public List<YHPerson> findPersons(Connection dbConn, String  userKey, YHPageUtil pu) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<YHPerson> persons = new ArrayList<YHPerson>();
    try{
      String sql =    
              " select SEQ_ID" 
              		+", USER_ID" 
              		+", USER_NAME" 
              		+", SCORE" 
              		+", TDER_FLAG " 
              		+",USER_NO" 
              		+",USER_PRIV"
              		+" from person"
              +" where NOT_LOGIN!='1' and DEPT_ID!='0'" ;
              if(YHStringUtil.isNotEmpty(userKey)){
                sql += " and (USER_NAME like '%"+ YHDBUtility.escapeLike(userKey) +"%' or USER_ID like '%"+ YHDBUtility.escapeLike(userKey) +"%')";
              }              
              sql += " order by USER_NO";                       
              ps = dbConn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);    
              ps.setMaxRows(pu.getCurrentPage() * pu.getPageSize());
              rs = ps.executeQuery();             
              rs.first();   
              rs.relative((pu.getCurrentPage()-1) * pu.getPageSize() -1);  
              while(rs.next()){
                YHPerson p = new YHPerson();
                p.setSeqId(rs.getInt(1));
                p.setUserId(rs.getString(2));
                p.setUserName(rs.getString(3));
                p.setScore(rs.getInt(4));
                p.setTderFlag(rs.getString(5));
                p.setUserNo(rs.getInt(6));
                p.setUserPriv(rs.getString(7));
                persons.add(p);
              }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return persons;
  }
  /**
   * 查找用户个数
   * @param dbConn
   * @return
   * @throws Exception
   */
  public int findPersonsCount(Connection dbConn, String nameKey) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = 
          "select count(*) " 
        + "from person "
        +" where NOT_LOGIN!= '1' and DEPT_ID!='0' ";    
    if(YHStringUtil.isNotEmpty(nameKey)){
      sql += "and (USER_NAME like '%"+ YHDBUtility.escapeLike(nameKey) +"%' or USER_ID like '%"+ YHDBUtility.escapeLike(nameKey) +"%')";
    }
    //YHOut.println(sql);
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    if(rs.next()){
      return rs.getInt(1);
    }
    return 0;
  }
  
  public YHPerson findPerson(Connection dbConn, int userId)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;  
    String sql = 
      "select SEQ_ID, USER_ID, USER_NAME, SCORE, TDER_FLAG, USER_PRIV from person where SEQ_ID =" + userId;
    //YHOut.println(sql);
    YHPerson p = new YHPerson();
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){        
        p.setSeqId(rs.getInt(1));
        p.setUserId(rs.getString(2));
        p.setUserName(rs.getString(3));
        p.setScore(rs.getInt(4));
        p.setTderFlag(rs.getString(5));
        p.setUserPriv(rs.getString(6));
      }
    } catch (Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return p;    
  }
  /**
   * 更新用户
   * @param dbConn
   * @param user
   * @return
   * @throws Exception
   */
  public int updatePerson(Connection dbConn, YHPerson user)throws Exception{
    PreparedStatement ps = null;
    int id =0;
    try{
      String sql = 
        "update person set  USER_NAME = '"+ user.getUserName() +"'" 
                            + ", SCORE= " + user.getScore() 
                            + ", USER_PRIV = '" + user.getTderFlag() +"'"
                            + " where SEQ_ID =" + user.getSeqId();
      //YHOut.println(sql);
      ps = dbConn.prepareStatement(sql);
      id = ps.executeUpdate();
    } catch (Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return id;
  }
  
  /**
   * 删除用户
   * @param dbConn
   * @param userId
   * @return
   * @throws Exception
   */
  public int deletePerson(Connection dbConn, int userId)throws Exception{
    PreparedStatement ps = null;
    int id =0;
    try{
      String sql = 
        "delete from person where seq_id = " + userId;
      ps = dbConn.prepareStatement(sql);
      id = ps.executeUpdate();
    } catch (Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return id;
  }
  
///////////////////////////////////////////////////////begin////////////////////////// 
  /**
   * 删除其他用户对用户问题的答案
   * @param dbConn
   * @param userId
   * @return
   * @throws Exception
   */
  public int deleteAskAnsOfOther(Connection dbConn, int userId) throws Exception{
    PreparedStatement ps = null;
    int id = 0; 
    String sql = "delete from oa_wiki_ask_answer where ASK_ID in ("
            +"select distinct SEQ_ID from oa_wiki_ask where CREATOR = '"+ userId 
            +"')";
    //YHOut.println(sql);
  try {
    ps = dbConn.prepareStatement(sql);
    id = ps.executeUpdate();
  } catch (Exception e) {
    throw e;
  }finally{
      YHDBUtility.close(ps, null, null);
    }
    return id;
  }
 
  /**
   * 更新其他用户的分数
   * @param dbConn
   * @param userId
   * @return
   * @throws Exception
   */
  public int updateOtherPerson(Connection dbConn, int userId)throws Exception{
    PreparedStatement ps = null;
    int id = 0; 
    String sql = 
      "update person set SCORE = SCORE - 1 where SEQ_ID in("
        +" select distinct  ans.ANSWER_USER"
        +" from oa_wiki_ask ask, oa_wiki_ask_answer ans" 
        +" where ask.CREATOR = '"+ userId +"' and ask.ASK_STATUS ='1'"
        +" and ans.GOOD_ANSWER ='1' and ask.SEQ_ID = ans.ASK_ID "
       +" )";
    try {
      ps = dbConn.prepareStatement(sql);
      id = ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    }finally{
        YHDBUtility.close(ps, null, null);
      }
  return id;
  }
  /**
   * 删除其他用户对userId用户提供的最佳答案的评论
   * @param dbConn
   * @param userId
   * @return
   * @throws Exception
   */
  public int deleteAskComment(Connection dbConn, int userId)throws Exception{
    PreparedStatement ps = null;
    int id = 0; 
    String sql = 
      "delete from oa_wiki_comment where ASK_ID in ("
      +" select ASK_ID from oa_wiki_ask_answer where ANSWER_USER = '"+ userId +"' and GOOD_ANSWER = '1'"
      +" )";
    try {
      ps = dbConn.prepareStatement(sql);
      id = ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    }finally{
        YHDBUtility.close(ps, null, null);
      }
  return id;
  }
  
  /**
   * 删除用户所有的问题
   * @param dbConn
   * @param userId
   * @return
   * @throws Exception
   */
  public int deleteUserAsk(Connection dbConn, int userId) throws Exception{
    PreparedStatement ps = null;
    int id = 0;
    try {
    String sql = 
      "delete from oa_wiki_ask where CREATOR = '" + userId +"'";
    ps = dbConn.prepareStatement(sql);
    id = ps.executeUpdate();
  } catch (Exception e) {
    throw e;
  }finally{
      YHDBUtility.close(ps, null, null);
    }
   return id;
  }
  
  /**
   * 删除用户提供的评论
   * @param dbConn
   * @param userId
   * @return
   * @throws Exception
   */
  public int deleteUsersComment(Connection dbConn, int userId)throws Exception{
    PreparedStatement ps = null;
    int id = 0; 
    String sql = "delete from oa_wiki_comment where MEMBER = '" + userId +"'";
    try {
      ps = dbConn.prepareStatement(sql);
      id = ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    }finally{
        YHDBUtility.close(ps, null, null);
      }
  return id;
  }
  
  /**
   * 删除用户所提供的答案  
   * @param dbConn
   * @param userId
   * @return
   * @throws Exception
   */
    public int deleteUserAnswes(Connection dbConn, int userId)throws Exception{
      PreparedStatement ps = null;
      int id = 0; 
      String sql = "delete from oa_wiki_ask_answer where ANSWER_USER = '" + userId +"'";
      try {
        ps = dbConn.prepareStatement(sql);
        id = ps.executeUpdate();
      } catch (Exception e) {
        throw e;
      }finally{
          YHDBUtility.close(ps, null, null);
        }
    return id;
    }
 /**
  * 删除用户是所引起的操作
  * @param dbConn
  * @param userId
  * @return
  * @throws Exception
  */
    public void deleteUserReference(Connection dbConn, int userId) throws Exception{
      try{
        //1.删除别人对用户问题的相关事项
        deleteByOther( dbConn,  userId);        
        //2.删除这个用户对别人问题的相关事项
        deleteBySelf(dbConn, userId);
      }catch(Exception e){
        throw e;
      }
    }
 /**
  *  删除别人对用户问题的相关事项
  * @param dbConn
  * @param userId
  * @return
 * @throws Exception 
  */
    public void deleteByOther(Connection dbConn, int userId) throws Exception{
      updateOtherPerson(dbConn, userId);
      deleteAskComment(dbConn, userId);     
      deleteAskAnsOfOther(dbConn, userId);    
    }
    
   /**
    * 删除这个用户对别人问题的相关事项 
    * @param dbConn
    * @param userId
    * @return
    * @throws Exception
    */
    public void deleteBySelf(Connection dbConn, int userId) throws Exception{
      deleteUserAsk(dbConn,  userId);
      deleteUsersComment(dbConn,  userId);
      deleteUserAnswes(dbConn,  userId);
      deletePerson(dbConn, userId);
    }
}

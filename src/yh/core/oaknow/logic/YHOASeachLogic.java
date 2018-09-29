package yh.core.oaknow.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import yh.core.oaknow.data.YHOAAsk;
import yh.core.oaknow.util.YHPageUtil;
import yh.core.oaknow.util.YHStringUtil;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
/**
 * oa知道搜索
 * @author qwx110
 *
 */
public class YHOASeachLogic{ 
/**
 * 与name相关的解决的问题
 * @param dbConn
 * @param name
 * @param pu
 * @return
 * @throws Exception
 */
  public List<YHOAAsk> findAllAskResolved(Connection dbConn, String name, YHPageUtil pu) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    List<YHOAAsk> askList = new ArrayList<YHOAAsk>();
    try{  
      String sql =  "select  "
                          +" ask.seq_id,ask.ASK" 
                          +" ,ask.ASK_COMMENT" 
                          +" ,ask.ask_status"
                          +" ,ans.good_answer"
                          +" ,ans.ANSWER_CONTENT"          
                     +" from oa_wiki_ask ask, oa_wiki_ask_answer ans "
                     +" where  ask.SEQ_ID = ans.ASK_ID "
                     +" and ask.ASK_STATUS = 1"
                     +" and ans.GOOD_ANSWER = 1 "        

                      +" and (ask like '%"+ YHDBUtility.escapeLike(name.trim()) +"%' " +YHDBUtility.escapeLike()
                      +" or ASK_COMMENT like  '%"+ YHDBUtility.escapeLike(name.trim())+"%'"+YHDBUtility.escapeLike()  
                      +" or ANSWER_CONTENT like  '%"+ YHDBUtility.escapeLike(name.trim())+"%' "+ YHDBUtility.escapeLike() +")";
      //YHOut.println(sql);
      ps = dbConn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);    
      ps.setMaxRows(pu.getCurrentPage() * pu.getPageSize());
      rs = ps.executeQuery();
      rs.first();   
      rs.relative((pu.getCurrentPage()-1) * pu.getPageSize() -1); 
      while(rs.next()){
        YHOAAsk ask = new YHOAAsk();
        ask.setSeqId(rs.getInt(1));
        ask.setAsk(YHStringUtil.toBright(rs.getString(2), name, 50));
        ask.setAskComment(YHStringUtil.toBright(rs.getString(3), name, 250));        
        ask.setAnswer(YHStringUtil.toBright(rs.getString(4), name, 250));
        askList.add(ask);
      }
    }catch(Exception e){
      throw e; 
    }finally{
      YHDBUtility.close(ps, rs, null);
    }    
    return askList;
  }  
  
 /**
  * 查找个数 
  * @param dbConn
  * @param name
  * @return
  * @throws Exception
  */
  public int findAllAskResolvedCount(Connection dbConn, String name)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    String sql = 
    /*  "select count(*)"
      +" from ("*/
            "select  "
              +" ask.seq_id,ask.ASK" 
              +" ,ask.ASK_COMMENT" 
              +" ,ask.ask_status"
              +" ,ans.good_answer"
              +" ,ans.ANSWER_CONTENT"          
           +" from oa_wiki_ask ask, oa_wiki_ask_answer ans "
          +" where  ask.SEQ_ID = ans.ASK_ID "
         +" and ask.ASK_STATUS = 1"
         +" and ans.GOOD_ANSWER = 1 "         
  /*   +")"*/
    +" and (ask like '%"+ YHDBUtility.escapeLike(name.trim()) +"%' " + YHDBUtility.escapeLike()
    +" or ASK_COMMENT like  '%"+ YHDBUtility.escapeLike(name.trim())+"%' "+ YHDBUtility.escapeLike()
   +" or ANSWER_CONTENT like  '%"+ YHDBUtility.escapeLike(name.trim())+"%' "+ YHDBUtility.escapeLike()+")";
    //YHOut.println(sql);
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      int cnt = 0;
      while(rs.next()){
         cnt ++;
      }
      return cnt;
    } catch (Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }   
  }
 /**
  *  与name相关的未解决的问题
  * @param dbConn
  * @param name
  * @param pu
  * @return
  * @throws Exception
  */
  public List<YHOAAsk> findAllAskNoResolved(Connection dbConn, String name, YHPageUtil pu) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    List<YHOAAsk> askList = new ArrayList<YHOAAsk>();
    try{
      String ids = findAskNoResolvedIds(dbConn, name);
      if(YHUtility.isNullorEmpty(ids)){
        ids = "0";
      }
      String sql =  "select  "
                        +" ask.SEQ_ID" 
                        +",ask.ASK" 
                        +",ask.ASK_COMMENT" 
                        +",ask.ASK_STATUS"
                   +" from oa_wiki_ask ask where seq_id in("+ ids +")";
      //YHOut.println(sql+"***");     
      ps = dbConn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);    
      ps.setMaxRows(pu.getCurrentPage() * pu.getPageSize());
      rs = ps.executeQuery();
      rs.first();   
      rs.relative((pu.getCurrentPage()-1) * pu.getPageSize() -1);  
      while(rs.next()){
        YHOAAsk ask = new YHOAAsk();
        ask.setSeqId(rs.getInt(1));
        ask.setAsk(YHStringUtil.toBright(rs.getString(2), name,50));
        ask.setAskComment(YHStringUtil.toBright(rs.getString(3), name, 250));        
        ask.setAnswer(YHStringUtil.toBright(rs.getString(4), name, 250));
        askList.add(ask);
      }
    }catch(Exception e){
      throw e; 
    }finally{
      YHDBUtility.close(ps, rs, null);
    }    
    return askList;
  }  
 
 /**
  * 待解决的问题的个数
  * @param dbConn
  * @param name 查询的内容
  * @return
  * @throws Exception
  */
  public int findAskNoResolvedCount(Connection dbConn, String name)throws Exception{
    String ids = findAskNoResolvedIds(dbConn, name);
    if(YHUtility.isNullorEmpty(ids)){
      return 0;
    }
    String[] id = ids.split(",");
    return  id.length;
  }
  
  /**
   * 获得待解决的问题的id串
   * @param dbConn
   * @param name  查询的内容
   * @return
   * @throws Exception
   */
  public String findAskNoResolvedIds(Connection dbConn, String name) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    String sql = " select ask.SEQ_ID as AID from oa_wiki_ask ask where ask.ask_status = 0 ";
           sql +=" and (ask.ASK like '%"+ YHDBUtility.escapeLike(name) +"%' " +YHDBUtility.escapeLike();
           sql +=" or ask.ASK_COMMENT like  '%"+ YHDBUtility.escapeLike(name) +"%' "+YHDBUtility.escapeLike()+")" ;
           sql +=" union ";
           sql +=" select ans.ASK_ID as AID from oa_wiki_ask_answer ans where ans.GOOD_ANSWER = 0 and ";
           sql +=" ans.ANSWER_CONTENT like  '%"+ YHDBUtility.escapeLike(name) +"%' " + YHDBUtility.escapeLike();
     //YHOut.println(sql);
     ps = dbConn.prepareStatement(sql);
     rs = ps.executeQuery();
     String ids = "";
     while(rs.next()){
       ids += rs.getInt("AID")+",";
     }    
    return ids.substring(0, ids.lastIndexOf(",")==-1?0:ids.lastIndexOf(","));
  }
}

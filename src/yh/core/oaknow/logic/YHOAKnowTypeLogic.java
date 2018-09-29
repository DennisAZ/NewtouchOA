package yh.core.oaknow.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yh.core.oaknow.data.YHCategoriesType;
import yh.core.oaknow.data.YHOAAsk;
import yh.core.oaknow.util.YHPageUtil;
import yh.core.oaknow.util.YHOAToJsonUtil;
import yh.core.util.YHOut;
import yh.core.util.db.YHDBUtility;

/**
 * 查找分类
 * @author qwx110
 *
 */
public class YHOAKnowTypeLogic{
  
  /**
   * 在某个分类下的问题(ajax)
   * @param dbConn
   * @param typeId 分类id
   * @param flag   区分：1 全部，2 已解决，0未解决 
   * @return
   * @throws Exception
   */
  public  String findAskByType(Connection dbConn, int typeId, int flag, YHPageUtil pu) throws Exception{
    List<YHOAAsk> askList = new ArrayList<YHOAAsk>();      
      try{
        if(flag == 1){                                //TYPEID下的所有的问题
          askList = findAllByTypeIdPage(dbConn, typeId, 1, pu);        
        }else if(flag == 2){
          askList = findAllByTypeIdPage(dbConn, typeId, 2, pu);//已解决的问题        
        }else if(flag == 0){
          askList = findAllByTypeIdPage(dbConn, typeId, 0, pu);//未解决的问题        
        }
      } catch (Exception e){
       throw e;
      }   
    return YHOAToJsonUtil.toJson(askList);
  }
 
/**
 * 通过分类id查找其子分类  
 * @param dbConn
 * @param typeId  分类id
 * @param parentId 父分类id, 如果夫分类为0，则传过来的事顶级夫类，直接查询顶级夫类及其子类，
 *                           如果不是0，则传来的是子类，则通过parentId查找夫类及其所有的子类
 * @return
 * @throws Exception
 */
  public YHCategoriesType findTypeByTypeId(Connection dbConn, int typeId, int parentId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    int temp = (parentId == 0)? typeId: parentId;//取父类 
    
    List<YHCategoriesType> typesList = new ArrayList<YHCategoriesType>();
    YHCategoriesType type = new YHCategoriesType();
    try{
      //todo
      String sql ="select CATEGORIE_NAME" +
      		"               ,SEQ_ID" +
      		"               ,PEARENT_ID" +
      		"        from oa_category_kind" +         
          " where SEQ_ID = " + temp;
         
      //YHOut.println(sql);
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      //YHOut.println(sql);
      if(rs.next()){        
        type.setName(rs.getString(1));
        type.setSeqId(rs.getInt(2));        
        type.setPearentId(rs.getInt(3));
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        String sql2 = "select CATEGORIE_NAME" +
        		"                ,SEQ_ID" +
        		"                ,PEARENT_ID" +
        		"          from oa_category_kind" +
        		"          where PEARENT_ID ="+rs.getInt(2)+
        		"          order by ORDER_ID"; //取出夫类下的子类
        ps2 = dbConn.prepareStatement(sql2);
        rs2 = ps2.executeQuery();
        //YHOut.println(sql2);
        while(rs2.next()){
          YHCategoriesType type2 = new YHCategoriesType(); 
          type2.setName(rs2.getString(1));
          type2.setSeqId(rs2.getInt(2));          
          type2.setPearentId(rs.getInt(2));
          typesList.add(type2);
        }
        type.setList(typesList);        
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return type;
  }
  
  /**
   * 查找对应的分类id下面的问题状态
   * @param dbConn
   * @param typeId
   * @param flag   区分：1 全部，2 已解决，0未解决 
   * @return
   * @throws Exception
   */
  public List<YHOAAsk> findAllByTypeId(Connection dbConn, int typeId, int flag) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    List<YHOAAsk> typesList = new ArrayList<YHOAAsk>();    
    try{
      String sql = "select ask.SEQ_ID" +
      		"               ,ask.CREATOR" +
      		"               ,ask.CREATE_TIME" +
      		"               ,ask.ASK_COMMENT" +
      		"               ,ask.ASK" +
      		"               ,ask.CATEGORIE_ID" +
      		"         from  oa_category_kind typ, oa_wiki_ask ask" +
      		"         where typ.SEQ_ID = ask.CATEGORIE_ID" ;
      		if(flag == 1){     //全部
      		  sql += "         and  (typ.SEQ_ID = " + typeId;
      		  sql += "           or  typ.PEARENT_ID = "+ typeId +")";  
      		  sql += "         order by typ.ORDER_ID";    		
      		}else if(flag == 2){  //已经解决的
      	    sql += "         and  (typ.SEQ_ID = " + typeId;
      	    sql += "           or  typ.PEARENT_ID = "+ typeId +")"; 
      	    sql += "         and  ask.ASK_STATUS = 1";
            sql += "         order by typ.ORDER_ID";      
      		}else if(flag == 0){//未解决的 
      		  sql += "         and  (typ.SEQ_ID = " + typeId;
      		  sql += "           or  typ.PEARENT_ID = "+ typeId +")"; 
            sql += "         and  ask.ASK_STATUS = 0";
            sql += "         order by typ.ORDER_ID";      
      		}
      //YHOut.println(sql);
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();    
      while(rs.next()){
        YHOAAsk ask = new YHOAAsk();
        ask.setSeqId(rs.getInt(1));
        ask.setCreator(rs.getString(2));
        ask.setCreateDate((Date)rs.getObject(3));
        ask.setAskComment(rs.getString(4));
        ask.setAsk(rs.getString(5));
        ask.setTypeId(rs.getInt(6));
        typesList.add(ask);
      }
    } catch (Exception e){
     throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return typesList;
  } 
  
  
  /**
   * 查找对应的分类id下面的问题状态
   * @param dbConn
   * @param typeId
   * @param flag   区分：1 全部，2 已解决，0未解决 
   * @return
   * @throws Exception
   */
  public List<YHOAAsk> findAllByTypeIdPage(Connection dbConn, int typeId, int flag, YHPageUtil pu) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    List<YHOAAsk> typesList = new ArrayList<YHOAAsk>();    
    try{
      String sql = " select ask.SEQ_ID" +
          "               ,ask.CREATOR" +
          "               ,ask.CREATE_TIME" +
          "               ,ask.ASK_COMMENT" +
          "               ,ask.ASK" +
          "               ,ask.CATEGORIE_ID" +          
          "         from  oa_category_kind typ, oa_wiki_ask ask" +
          "         where typ.SEQ_ID = ask.CATEGORIE_ID" ;
          if(flag == 1){     //全部
            sql += "         and  (typ.SEQ_ID = " + typeId;
            sql += "           or  typ.PEARENT_ID = "+ typeId +")";  
              
          }else if(flag == 2){  //已经解决的
            sql += "         and  (typ.SEQ_ID = " + typeId;
            sql += "           or  typ.PEARENT_ID = "+ typeId +")"; 
            sql += "         and  ask.ASK_STATUS = 1";
           
          }else if(flag == 0){//未解决的 
            sql += "         and  (typ.SEQ_ID = " + typeId;
            sql += "           or  typ.PEARENT_ID = "+ typeId +")"; 
            sql += "         and  ask.ASK_STATUS = 0";            
          }
         
      //YHOut.println(sql);
          ps = dbConn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
          ps.setMaxRows(pu.getCurrentPage() * pu.getPageSize());
          rs = ps.executeQuery();
          rs.first();   
          rs.relative((pu.getCurrentPage()-1) * pu.getPageSize() -1);     
      while(rs.next()){
        YHOAAsk ask = new YHOAAsk();
        ask.setSeqId(rs.getInt(1));
        ask.setCreator(rs.getString(2));
        ask.setCreateDate((Date)rs.getObject(3));
        ask.setAskComment(rs.getString(4));
        ask.setAsk(rs.getString(5));
        ask.setTypeId(rs.getInt(6));
        typesList.add(ask);
      }
    } catch (Exception e){
     throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return typesList;
  } 
  
  public int findAllCount(Connection dbConn, int typeId, int flag) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    String sql = 
      "select count(*)  " 
         + "  from  oa_category_kind typ, oa_wiki_ask ask" +
          "  where typ.SEQ_ID = ask.CATEGORIE_ID" ; 
    if(flag == 1){     //全部
      sql += "         and  (typ.SEQ_ID = " + typeId;
      sql += "           or  typ.PEARENT_ID = "+ typeId +")";  
      sql += "         order by typ.ORDER_ID";        
    }else if(flag == 2){  //已经解决的
      sql += "         and  (typ.SEQ_ID = " + typeId;
      sql += "           or  typ.PEARENT_ID = "+ typeId +")"; 
      sql += "         and  ask.ASK_STATUS = 1";
      sql += "         order by typ.ORDER_ID";      
    }else if(flag == 0){//未解决的 
      sql += "         and  (typ.SEQ_ID = " + typeId;
      sql += "           or  typ.PEARENT_ID = "+ typeId +")"; 
      sql += "         and  ask.ASK_STATUS = 0";
      sql += "         order by typ.ORDER_ID";      
    }
    //YHOut.println(sql);
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();    
      if(rs.next()){
        return rs.getInt(1);
      }
    } catch (Exception e){
     throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return 0;    
  }
  /**
   * 根据askI查找这个问题的分类以及父类
   * @param dbConn
   * @param askId
   * @return
   * @throws Exception
   */
  public List<YHCategoriesType> findTypseUtil(Connection dbConn, int askId)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    List<YHCategoriesType> types = new ArrayList<YHCategoriesType>();    
    String sql = 
      "select SEQ_ID,CATEGORIE_NAME,PEARENT_ID from oa_category_kind"
      +" connect by prior PEARENT_ID = seq_id"
      +" start with SEQ_ID = ("
      +" select distinct T.SEQ_ID"
      +" from oa_category_kind t , oa_wiki_ask q "
    +" where q.SEQ_ID = "+ askId +" and t.SEQ_ID = q.CATEGORIE_ID "
   +")";
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        YHCategoriesType type = new YHCategoriesType();
        type.setSeqId(rs.getInt(1));
        type.setName(rs.getString(2));
        type.setPearentId(rs.getInt(3));
        types.add(type);
      }
    } catch (Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return types;
  }
  
  /**
   * 查找这个问题的分类
   * @param dbConn
   * @param askId
   * @return
   */
  public  YHCategoriesType  findTypeUtil2(Connection dbConn, int askId)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    String sql = "select type.SEQ_ID,type.CATEGORIE_NAME,type.PEARENT_ID from oa_category_kind type, oa_wiki_ask q where type.SEQ_ID = q.CATEGORIE_ID and q.SEQ_ID ="+ askId;
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        YHCategoriesType type = new YHCategoriesType();
        type.setSeqId(rs.getInt(1));
        type.setName(rs.getString(2));
        type.setPearentId(rs.getInt(3));   
        return type;
      }
    } catch (Exception e){     
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    } 
    return null;
  }
  /**
   * 查找这个问题的分类以及父类
   * @param dbConn
   * @return
   * @throws Exception
   */
  public List<YHCategoriesType> findTypseUtil3(Connection dbConn, int askId)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    YHCategoriesType sub = findTypeUtil2(dbConn, askId);
    List<YHCategoriesType> types = new ArrayList<YHCategoriesType>();   
    if(sub != null){
      types.add(sub);
      String sql = "select SEQ_ID,CATEGORIE_NAME,PEARENT_ID from oa_category_kind type where SEQ_ID="+ sub.getPearentId(); 
      try{
        ps = dbConn.prepareStatement(sql);
        rs = ps.executeQuery();
        if(rs.next()){
          YHCategoriesType type = new YHCategoriesType();
          type.setSeqId(rs.getInt(1));
          type.setName(rs.getString(2));
          type.setPearentId(rs.getInt(3));
          types.add(type);
        }
      } catch (Exception e){
        throw e;
      }finally{
        YHDBUtility.close(ps, rs, null);
      } 
    }
    return types;
  }
}

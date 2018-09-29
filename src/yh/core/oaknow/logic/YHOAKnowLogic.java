package yh.core.oaknow.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.oaknow.data.YHCategoriesType;
import yh.core.oaknow.data.YHOAAsk;
import yh.core.oaknow.data.YHOAKnowUser;
import yh.core.oaknow.util.YHStringUtil;
import yh.core.util.db.YHDBUtility;
/**
 * OA知道首页
 * @author qwx110
 *
 */
public class YHOAKnowLogic{
  /**
   * 查找积分榜用户列表，最多显示10条数据
   * @param dbConn
   * @return
   * @throws Exception 
   */
  public List<YHOAKnowUser> findJiFenBang(Connection dbConn) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;   
    List<YHOAKnowUser> users = new ArrayList<YHOAKnowUser>();
    try {
      String sql = "select USER_NAME" +
      		"               ,SCORE " +
      		"         from  person " +
      		"         where NOT_LOGIN !='1'  " +
      		"         and   DEPT_ID!=0 " +
      		"         and SCORE >0 " +
      		"         order by SCORE desc";
      ps=dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      //YHOut.println(sql);
      while(rs.next()){        
        YHOAKnowUser user = new YHOAKnowUser();
        user.setName(rs.getString(1));
        user.setScore(rs.getInt(2));
        users.add(user);
      }
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return users;
  }

/**
 * 注册的用户数
 * @param dbConn
 * @return
 * @throws Exception
 */
  public int findRegCount(Connection dbConn)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;   
    int totalCount = 0;   //总的注册的用户数
    try{
      String sql = "select count(*) as total " +
      		"         from person where NOT_LOGIN != '1'" +
      		"         and DEPT_ID!=0  ";
      		
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();     
      if(rs.next()){
        totalCount = rs.getInt(1);
      }
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return totalCount;
  }
  
  /**
   * 已经解决的问题数
   * @param dbConn
   * @param flag 1:表示解决,0：表示未解决
   * @return
   * @throws Exception
   */
  public int hadResolved(Connection dbConn, int flag) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;   
    int totalCount = 0;
    try{
      String sql = "select count(*) " +
      		"         from oa_wiki_ask " +
      		"         where ASK_STATUS="+flag;
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        totalCount = rs.getInt(1);
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return totalCount;
  }
  
  /**
   * 分类
   * @param dbConn
   * @return
   * @throws Exception
   */
  public List<YHCategoriesType> findKind(Connection dbConn) throws Exception{//查找父类
    PreparedStatement ps = null;
    ResultSet rs = null; 
    List<YHCategoriesType> outerList = new ArrayList<YHCategoriesType>();
    try{
        String sql = "select CATEGORIE_NAME" +
        		"                ,SEQ_ID " +
        		"                ,PEARENT_ID" +
        		"         from oa_category_kind " +
        		"         where PEARENT_ID = '0' " +
        		"         order by ORDER_ID asc";
        ps = dbConn.prepareStatement(sql);
        rs = ps.executeQuery();
        //YHOut.println(sql);
        while(rs.next()){
          YHCategoriesType type = new YHCategoriesType();
          type.setName(rs.getString(1));
          type.setPearentId(0);
          type.setSeqId(rs.getInt(2));
          PreparedStatement ps2 = null;
          ResultSet rs2 = null; 
          String sql2 = "select CATEGORIE_NAME"+
          "                     ,SEQ_ID " +
          "                     ,PEARENT_ID" +
          "         from oa_category_kind " +
          "         where PEARENT_ID =  " +rs.getInt(2)+
          "         order by ORDER_ID asc";
          ps2 = dbConn.prepareStatement(sql2);
          rs2 = ps2.executeQuery();
          //YHOut.println(sql2);
          List<YHCategoriesType> innerList = new ArrayList<YHCategoriesType>();
          while(rs2.next()){
            YHCategoriesType type2 = new YHCategoriesType();
            type2.setName(rs2.getString(1));
            type2.setPearentId(rs.getInt(2)); 
            type2.setSeqId(rs2.getInt(2));
            innerList.add(type2);
          }
          type.setList(innerList);
          outerList.add(type);
        }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return outerList;
  }
  
  /**
   * 精彩问题推荐，最多显示10条数据
   * @param dbConn
   * @return
   * @throws Exception
   */
  public List<YHOAAsk> findGoodAnswer(Connection dbConn)throws Exception{
    //PreparedStatement ps = null;
    Statement smt =null;
    ResultSet rs = null; 
    List<YHOAAsk> askList = new ArrayList<YHOAAsk>();
    try{
      String sql = "select b.ASK" +
      		"               ,b.SEQ_ID" +
      		"               ,a.CATEGORIE_NAME" +
      		"               ,a.SEQ_ID" +
      		"               ,a.PEARENT_ID" +
      		"          from oa_category_kind a " +
      		"          join oa_wiki_ask b" +
      		"          on  a.SEQ_ID = b.CATEGORIE_ID" +
      		"          where b.COMMEND='1' " +      		
      		"          order by b.CREATE_TIME desc ";
      //ps = dbConn.prepareStatement(sql);
      smt = dbConn.createStatement();
      rs = smt.executeQuery(sql);
      //rs = ps.executeQuery(); 
      //YHOut.println(sql);
      while(rs.next()){
        YHOAAsk ask = new YHOAAsk();
        ask.setAsk(YHStringUtil.subString(30, rs.getString(1)));
        ask.setSeqId(rs.getInt(2));
        ask.setCategoryName(rs.getString(3));
        ask.setTypeId(rs.getInt(4));
        ask.setParentId(rs.getInt(5));
        askList.add(ask);
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(smt, rs, null);
    }
    return askList;
  }
  
  /**
   * 待解决的问题
   * @param dbConn
   * @return
   * @throws Exception
   */
  public  List<YHOAAsk> findNoResolvedAsk(Connection dbConn) throws Exception{
    PreparedStatement ps = null;
   
    ResultSet rs = null; 
    List<YHOAAsk> askList = new ArrayList<YHOAAsk>();
    try{
      String sql = "select b.ASK" +
      		"               ,b.SEQ_ID" +
      		"               ,a.CATEGORIE_NAME" +
      		"               ,a.SEQ_ID" +
      		"               ,a.PEARENT_ID" +
      		"         from  oa_category_kind a" +
      		"         ,  oa_wiki_ask b" +      		
      		"         where b.ASK_STATUS='0'" +
      		"         and a.SEQ_ID = b.CATEGORIE_ID" +
      		"         order by b.SEQ_ID desc";

     ps = dbConn.prepareStatement(sql);     
     rs = ps.executeQuery();
     //YHOut.println(sql);
     while(rs.next()){
       YHOAAsk ask = new YHOAAsk();
       ask.setAsk(YHStringUtil.subString(30, rs.getString(1)));
       ask.setSeqId(rs.getInt(2));
       ask.setCategoryName(rs.getString(3));
       ask.setTypeId(rs.getInt(4));
       ask.setParentId(rs.getInt(5));
       PreparedStatement ps2 = null;
       ResultSet rs2 = null;
       String sql2 = "select count(*) from oa_wiki_ask_answer where ASK_ID = " + rs.getInt(2);
       ps2 = dbConn.prepareStatement(sql2);
       rs2 = ps2.executeQuery();
       //YHOut.println(sql);
       if(rs2.next()){
         ask.setAskCount(rs2.getInt(1));
       }
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
   * 最近解决的问题
   * @param dbConn
   * @return
   * @throws Exception
   */
  public  List<YHOAAsk> findResolvedAsk(Connection dbConn) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    List<YHOAAsk> askList = new ArrayList<YHOAAsk>();
    try{
      String sql = "select b.ASK" +
      "               ,b.SEQ_ID" +
      "               ,a.CATEGORIE_NAME" +
      "               ,a.SEQ_ID" +
      "               ,a.PEARENT_ID" +
      "         from  oa_category_kind a" +
      "         join  oa_wiki_ask b" +
      "         on a.SEQ_ID = b.CATEGORIE_ID" +
      "         where b.ASK_STATUS='1'" +
     // "         and   ROWNUM <=15" +
      "         order by b.RESOLUTION_TIME desc";
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      //YHOut.println(sql);
      while(rs.next()){
        YHOAAsk ask = new YHOAAsk();
        ask.setAsk(YHStringUtil.subString(30,rs.getString(1)));
        ask.setSeqId(rs.getInt(2));
        ask.setCategoryName(rs.getString(3));
        ask.setTypeId(rs.getInt(4));
        ask.setParentId(rs.getInt(5));
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
   * 我的问题
   * @param dbConn
   * @param login_userId
   * @return
   * @throws Exception
   */
  public List<YHOAAsk> findMyAsk(Connection dbConn, String login_userId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    List<YHOAAsk> askList = new ArrayList<YHOAAsk>();
      try{ 
        String sql = "select b.ASK" +
        		"               ,b.SEQ_ID" +
        		"               ,a.CATEGORIE_NAME" +
        		"               ,a.SEQ_ID" +
        	  "               ,a.PEARENT_ID" +
        		"         from oa_category_kind a" +
        		"         join oa_wiki_ask b on a.SEQ_ID = b.CATEGORIE_ID" +
        		"         where b.CREATOR = '"+ login_userId + "'"+
        		"         order by b.SEQ_ID desc";
       //YHOut.println(sql);
       ps = dbConn.prepareStatement(sql);
       rs = ps.executeQuery();
       while(rs.next()){
         YHOAAsk ask = new YHOAAsk();
         ask.setAsk(YHStringUtil.subString(30, rs.getString(1)));
         ask.setSeqId(rs.getInt(2));
         ask.setCategoryName(rs.getString(3));
         ask.setTypeId(rs.getInt(4));
         ask.setParentId(rs.getInt(5));
         PreparedStatement ps2 = null;
         ResultSet rs2 = null; 
         String sql2 = "SELECT count(*) FROM oa_wiki_ask_answer where ASK_ID = "+ rs.getInt(2);
         ps2 = dbConn.prepareStatement(sql2);
         rs2 = ps2.executeQuery();
         if(rs2.next()){
           ask.setAskCount(rs2.getInt(1));
         }
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
   * OA首页搜索
   * @param dbConn
   * @param flag
   * @return
   * @throws Exception
   */
  public List searchByContent(Connection dbConn, String flag) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    List<Object> askList = new ArrayList<Object>();
    try{
      //todo
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }    
    return askList;
  } 
  /**
   * 与ask相关的问题
   * @param dbConn
   * @param ask
   * @return
   */
  public List<YHOAAsk> referenceQuestion(Connection dbConn, String askName)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    List<YHOAAsk> askList = new ArrayList<YHOAAsk>();
    try{
      String sql  = "select ASK"
                   + ",SEQ_ID"
                   + " from oa_wiki_ask"
                   + " where ASK_STATUS = 1 and RELATED_KEYWOED like '"
                   + YHDBUtility.escapeLike(askName) +"' " + YHDBUtility.escapeLike();
                  
      //YHOut.println(sql);
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        YHOAAsk ask = new YHOAAsk();
        ask.setAsk(YHStringUtil.subString(35, rs.getString(1)));
        ask.setSeqId(rs.getInt(2));
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
   * 保存新问题
   * @param dbConn
   * @param ask
   * @return
   * @throws Exception
   */
  public int saveAsk(Connection dbConn, YHOAAsk ask) throws Exception{
    PreparedStatement ps = null;
    try{
      String sql = "insert into oa_wiki_ask(CREATOR, CREATE_TIME,ASK_COMMENT,ASK,RELATED_KEYWOED,ASK_STATUS ,CATEGORIE_ID,COMMEND) values(?,"+ YHDBUtility.currDateTime()+",?,?,?,?,?,?)" ;
      //YHOut.println(sql);
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, ask.getCreator());
     
      ps.setString(2, ask.getAskComment());
      ps.setString(3, ask.getAsk());
      ps.setString(4, ask.getReplyKeyWord());
      ps.setInt(5, 0);
      ps.setInt(6, ask.getTypeId());
      ps.setInt(7, 0);
      int id =ps.executeUpdate();
      return id;
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
  }
  
  public String oaDesk(Connection dbConn)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);

    String sql ="";
    //YHOut.println(sql);
    if(dbms.equals(YHConst.DBMS_MYSQL)){// add by zyy 取前几条数据
    	sql= " select wa.seq_id, wa.ASK, wa.ASK_STATUS, wa.CATEGORIE_ID, ct.CATEGORIE_NAME, p.USER_NAME, ct.PEARENT_ID, wa.CREATE_TIME from oa_wiki_ask wa, oa_category_kind ct, person p" + 
                " where wa.CATEGORIE_ID = ct.SEQ_ID and wa.CREATOR = p.seq_id order by wa.CREATE_TIME desc limit 10" ;
        
    }else if(dbms.equals(YHConst.DBMS_ORACLE)){
    	sql= "select * from ( select wa.seq_id, wa.ASK, wa.ASK_STATUS, wa.CATEGORIE_ID, ct.CATEGORIE_NAME, p.USER_NAME, ct.PEARENT_ID, wa.CREATE_TIME from oa_wiki_ask wa, oa_category_kind ct, person p" + 
                " where wa.CATEGORIE_ID = ct.SEQ_ID and wa.CREATOR = p.seq_id order by wa.CREATE_TIME desc ) where rownum<=10" ;
    }
    
    
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    List<YHOAAsk> askList = new ArrayList<YHOAAsk>();
    int cnt = 0;
    while(rs.next() && ++cnt <=10){
      YHOAAsk ask = new YHOAAsk();
      ask.setSeqId(rs.getInt(1));
      ask.setAsk(YHStringUtil.subString(20, rs.getString(2)));
      ask.setStatus(rs.getInt(3));
      ask.setTypeId(rs.getInt(4));
      ask.setCategoryName(rs.getString(5));
      ask.setCreatorName(rs.getString(6));
      ask.setParentId(rs.getInt(7));
      ask.setCreateDate((Date)rs.getObject(8));
      //YHOut.println(ask.toString());
      askList.add(ask);
    }
    return toAString(askList);
  }
  
  public String  toAString(List<YHOAAsk> askList){
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    if(askList != null && askList.size()>0){
      for(int i=0; i<askList.size(); i++){
        if(i < askList.size()-1 ){
         sb.append(askList.get(i).toString()).append(",");
        }else{
         sb.append(askList.get(i).toString());
        }
      }
    }
    sb.append("]");
    return sb.toString();
  }
}
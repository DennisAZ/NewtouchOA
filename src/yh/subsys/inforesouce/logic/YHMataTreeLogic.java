package yh.subsys.inforesouce.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import yh.core.funcs.person.data.YHPerson;
import yh.core.util.db.YHDBUtility;
import yh.subsys.inforesouce.data.YHMateShow;
import yh.subsys.inforesouce.data.YHMateType;
import yh.subsys.inforesouce.data.YHMateValue;
import yh.subsys.inforesouce.util.YHMateUtil;
import yh.subsys.inforesouce.util.YHStringUtil;

public class YHMataTreeLogic {
  private static Logger log = Logger.getLogger(YHMataTreeLogic.class);
  /**
   * 查询父节点
   */
  public List<YHMateType> findParent(Connection conn,YHPerson person,String typemenu)
  throws Exception{

    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      String sql ="select seq_id, chname, parent_id, value_range, elem_id,number_id from oa_mate_kind where parent_id=0 and "+YHDBUtility.findInSet(typemenu, "ELEMENT_TYP");  
      rs = stmt.executeQuery(sql);
      //System.out.println(sql);
      List<YHMateType> va = new ArrayList<YHMateType>();
      while(rs.next()){
        YHMateType mv = new YHMateType();
        mv.setSeqId(rs.getInt("seq_id"));
        List<YHMateType> mate2 =  findSon(conn,person,mv.getSeqId(),typemenu);
        mv.setcNname(rs.getString("chname"));
        mv.setParentId(rs.getString("parent_id"));
        mv.setRangeId(rs.getString("value_range"));
        mv.setElementId(rs.getString("elem_id"));
        mv.setNumberId(rs.getString("number_id"));
        va.add(mv);
      }
     return va; 
     // rs = ps.executeQuery();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  /**
   * 查找子元素
   */
  public List<YHMateType> findSon(Connection conn,YHPerson person,int sonid,String typemenu)
  throws Exception{

    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      //如果传过来的sonid 与mate_typ表中的parent_id字段相等说明是子元素， parent_id为0表示是父元素       
      String sql ="select seq_id, chname, parent_id, value_range, number_id  from oa_mate_kind where parent_id="+sonid+" and "+YHDBUtility.findInSet(typemenu, "ELEMENT_TYP");  
      rs = stmt.executeQuery(sql);
      //System.out.println(sql);
      List<YHMateType> son = new ArrayList<YHMateType>();
      while(rs.next()){
        YHMateType mv = new YHMateType();
        mv.setSeqId(rs.getInt("seq_id")); //子元素的seq_id       
        mv.setcNname(rs.getString("chname"));
        mv.setParentId(rs.getString("parent_id"));
        mv.setRangeId(rs.getString("value_range"));
        mv.setNumberId(rs.getString("number_id"));
        //System.out.println(mv.getRangeId()+":::::");
       // mv.setParentId(rs.getString("value_range"));
      //  if(mv.getParentId()!=null&& !mv.getParentId().equals("null")){
        //  YHMateType mate1 =  valuerang(conn,person,mv.getSeqId(), mv.getParentId());//通过子元素对应的值域
      //    YHOut.println("mate1:::"+mate1);
      //  }
        
       son.add(mv);
      }
     return son; 
     // rs = ps.executeQuery();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  
  /**
   *  查询值域
   */
  public YHMateType valuerang(Connection conn,YHPerson person,int sonid,String parentId)
  throws Exception{

    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
       
      String sql ="select seq_id,chname,parent_id,value_range from oa_mate_kind where parent_id="+parentId;  
      rs = stmt.executeQuery(sql);
      //System.out.println(sql);
     // YHMateType son = new YHMateType();
      YHMateType mv = new YHMateType();
      while(rs.next()){
        mv.setSeqId(rs.getInt("seq_id")); //子元素的seq_id       
        mv.setcNname(rs.getString("chname"));
        mv.setParentId(rs.getString("parent_id"));
        mv.setRangeId(rs.getString("value_range"));
        YHMateValue mate =  sonDate(conn,person,Integer.parseInt(mv.getRangeId()));
        }
     return mv; 
     // rs = ps.executeQuery();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  /**
   * 查询值域 相对应的 父元素下的子元素ID  
   * @param seqId
   * @param conn
   * @param person
   * @return
   * @throws Exception
   */
  public YHMateValue sonDate( Connection conn,YHPerson person,int parentId)
  throws Exception{

    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
       
      String sql = "select seq_id, value_name from oa_mate_value where seq_id ="+parentId;  
      rs = stmt.executeQuery(sql);
      //System.out.println(sql);
     // YHMateType son = new YHMateType();
      YHMateValue mv = new YHMateValue();
      while(rs.next()){
        mv.setSeqId(rs.getInt("seq_id")); //子元素的seq_id       
        mv.setValueName(rs.getString("value_name"));
        }
     return mv; 
     // rs = ps.executeQuery();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  /**
   * 查询 设置权限表 mate_show的父节点
   */
  public List<YHMateShow> findMateShow(Connection conn,YHPerson person)
  throws Exception{
    
    Statement stmt = null;
    ResultSet rs = null;
   
    YHMateShow sh = new YHMateShow();
    //List<Map> list = new ArrayList();
    try{
      stmt = conn.createStatement();
      String sql = "select user_id, pr_id, idstr from oa_mate_display where user_id="+person+"";
     rs =  stmt.executeQuery(sql);
     List<YHMateShow> all = new ArrayList<YHMateShow>();
     while(rs.next()){
       sh.setUSER_ID((rs.getInt("user_id")));
       sh.setPR_ID(rs.getString("pr_id"));
       sh.setIDSTR(rs.getString("idstr"));
       all.add(sh);
     }
   //把父元素Id形式（M1-128_,M21-139_,）过滤成数字类型如:128,139 
    String parentId = YHMateUtil.findParents(sh.getPR_ID());
  //把前缀M1-128_ 过滤掉如（M1-128_V103,M1-128_V104,M21-139_143）剩下 V103, V104,143
   String prentStr = sh.getPR_ID();
   String [] prentNode =  prentStr.split(",");
   for(int i=0 ; i<prentNode.length; i++){
     YHMateUtil.self(sh.getIDSTR(), prentNode[i],String.valueOf(sh.getUSER_ID()));
   }   
    
  //  String findsubs = util.findSub(sh.getIDSTR(),sh.getPR_ID());
  // 此方法判断 是否为值域，还是子元素  。 如果有v的是值域，不带v的是子元素
  //  String subs = util.filterSub(String.valueOf(sh.getUSER_ID()),findsubs, sh.getPR_ID());
      return all;
    }
    catch(Exception e){
    throw e;
    }finally{
    YHDBUtility.close(stmt, null, log);     
    }
  //  return list;
  }
  

  
  
  /**
   * 返回用户选择的树
   * 判断用户设置树，如果没有，直接查询所有； 如果设置了，从mate_show里查询设置条件
   * @param conn
   * @param person
   * @return
   * @throws Exception 
   */
  public List<YHMateType> findSelMenu(String typemenu,Connection conn,YHPerson person) throws Exception{
      return findMySelMenu(typemenu,conn, person);
  }
  
    
  
  
  /**
   * 查询我选择的元数据及值域
   * @param conn
   * @param person
   * @return
   * @throws Exception 
   */
  public List<YHMateType> findMySelMenu(String typemenu,Connection conn,YHPerson person) throws Exception{
    YHMateShow show = findMyMateShow(conn, person,typemenu);
    String pNode = show.getPR_ID();//我选择的父节点  形式如：M1-128_,M2-129_,M8-135_
    if(pNode!=null && pNode!=""){
    String allNode = show.getIDSTR();//我选择的所有的节点   形式如：M1-128_,M1-128_V103,M1-128_V104,|M2-129_,M2-129_130,
    String[] parsePNode = pNode.split(",");// 开始解析我选择的串
    for(int i=0; i<parsePNode.length; i++){
      YHMateUtil.self(allNode, parsePNode[i], String.valueOf(show.getUSER_ID()));//组装数据
    }
    String pIdNodes = YHMateUtil.findParents(pNode);//把父节点解析为父id串
    List<YHMateType> types = findParentType(typemenu,conn, person, pIdNodes);
    return types;
    } else {
      return null;
    }
    
  }
  
  /**
   * 查找所有的父元素,以及这个父元素下的值域和子元素
   * @param conn
   * @param person
   * @return
   * @throws Exception 
   */
  public List<YHMateType> findParentType(String typemenu,Connection conn,YHPerson person,String pIdNode) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    List<YHMateType> pIdlist = new ArrayList<YHMateType>();
    try{
      conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
      String [] pId = pIdNode.split(",");
     // YHOut.println(pId);
      for(int i=0; i<pId.length; i++){      
      String sql = "select SEQ_ID,CHNAME,NUMBER_ID,PARENT_ID,VALUE_RANGE from oa_mate_kind where SEQ_ID ="+pId[i]+" and "+YHDBUtility.findInSet(typemenu, "ELEMENT_TYP");
      //YHOut.println(sql);
      stmt = conn.createStatement();
      rs =  stmt.executeQuery(sql);    
      while(rs.next()){
        YHMateType mate = new YHMateType();
        mate.setSeqId(rs.getInt("SEQ_ID"));
        mate.setcNname(rs.getString("CHNAME"));
        mate.setNumberId(rs.getString("NUMBER_ID"));
        mate.setParentId(rs.getString("PARENT_ID"));
        mate.setRangeId(rs.getString("VALUE_RANGE"));
        String key = person.getSeqId()+"_"+mate.getNumberId()+"-"+mate.getSeqId()+"_";//123_M23-139_
        String keyValue = key+"rage";                                                //取值域的key 123_M23-139_rage
        String keySub = key + "sub";                                                 //取子元素           123_M23-139_sub
        //取父 元素下的值域
         if(YHStringUtil.isNotEmpty(mate.getRangeId())){
          String valueRageIds = YHMateUtil.getMateMap().get(keyValue);//通过self方法已经把值域串拼好了
          if(YHStringUtil.isNotEmpty(valueRageIds)){           
            List<YHMateValue> values = findValueName(conn,valueRageIds);
            mate.setValues(values);
          }          
         }          
        //取父元素下的子元素        String subIds = YHMateUtil.getMateMap().get(keySub);           //这个父类下要显示的子元素id串        if(YHStringUtil.isNotEmpty(subIds)){
          List<YHMateType> subTypes = findMySubs(conn, subIds, key);
          mate.setSubs(subTypes);
        }        
        pIdlist.add(mate); 
      }
     }
      return pIdlist;
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
 } 
  
  
  /**
   * 从mate_show中查询出我选择的元数据
   * @param conn
   * @param person
   * @return
   * @throws Exception
   */
  public YHMateShow findMyMateShow(Connection conn,YHPerson person,String typemenu) throws Exception{ 
    Statement stmt = null;
    ResultSet rs = null;    
    YHMateShow sh = null;   
    try{
      conn.createStatement();
      stmt = conn.createStatement();
      String sql = "select user_id, pr_id, idstr from oa_mate_display where user_id="+person.getSeqId()+" and typeId="+typemenu.trim();
      rs =  stmt.executeQuery(sql);    
     if(rs.next()){
       sh = new YHMateShow();
       sh.setUSER_ID((rs.getInt("user_id")));
       sh.setPR_ID(rs.getString("pr_id"));
       sh.setIDSTR(rs.getString("idstr"));      
     }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    return sh;
  }
  /**
   * 判断用户是否设置过。

   * @param user 当前用户
   * @return <code>true</code> or <code>false</code>
  * @throws Exception 
   */
  public boolean iHaveSave(Connection dbConn, YHPerson user, String typemenu) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;   
    String sql = "select user_id from oa_mate_display where user_id =" + user.getSeqId()+" and TYPEID="+typemenu.trim();   
    try{
     ps = dbConn.prepareStatement(sql);
     rs = ps.executeQuery();
      if(rs.next()){
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
   * 从mate_value表中查找对应valueids的值域名称
   * @param dbConn
   * @param ids
   * @return
   * @throws Exception 
   */
  public List<YHMateValue> findValueName(Connection dbConn, String ids) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    String sql = "select SEQ_ID, VALUE_NAME from oa_mate_value where SEQ_ID in (" + ids +")";//值域串
    //YHOut.println(sql);
    List<YHMateValue> vals = new ArrayList<YHMateValue>();
    try{
     ps = dbConn.prepareStatement(sql);
     rs = ps.executeQuery();     
      while(rs.next()){
        YHMateValue mv = new YHMateValue();
        mv.setSeqId(rs.getInt("SEQ_ID"));
        mv.setValueName(rs.getString("VALUE_NAME"));
        vals.add(mv);
      }
   } catch (SQLException e){
     throw e;
   }finally{
     YHDBUtility.close(ps, rs, null);
   }
    return vals;
  }
  
  /**
   * 查询子元素
   * @param dbConn
   * @param subIds
   * @param pkey 父串
   * @return
   * @throws Exception 
   */
  public List<YHMateType> findMySubs(Connection dbConn, String subIds, String pkey) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    String sql = "select SEQ_ID,CHNAME,NUMBER_ID,PARENT_ID,VALUE_RANGE from oa_mate_kind where SEQ_ID in ("+ subIds +")";
    //YHOut.println(sql);
    List<YHMateType> types = new ArrayList<YHMateType>();
    try{
     ps = dbConn.prepareStatement(sql);
     rs = ps.executeQuery();     
      while(rs.next()){
        YHMateType mate = new YHMateType();
        mate.setSeqId(rs.getInt("SEQ_ID"));
        mate.setcNname(rs.getString("CHNAME"));
        mate.setNumberId(rs.getString("NUMBER_ID"));
        mate.setParentId(rs.getString("PARENT_ID"));
        mate.setRangeId((rs.getString("VALUE_RANGE")));
        
        String keyValue = pkey+mate.getSeqId()+"_rage";      //取子元素下的值域key        
        if(YHStringUtil.isNotEmpty(mate.getRangeId())){
          String valueRageIds = YHMateUtil.getMateMap().get(keyValue);
            if(YHStringUtil.isNotEmpty(valueRageIds)){
          List<YHMateValue> values = findValueName(dbConn,valueRageIds);
            
          mate.setValues(values);
         }
        }
        types.add(mate);
      }
   } catch (SQLException e){
     throw e;
   }finally{
     YHDBUtility.close(ps, rs, null);
   }
    return types;
  }
  public static String findParents(String parent){
    if(YHStringUtil.isNotEmpty(parent)){  
      return parent.replaceAll("_", "").replaceAll("[M][0-9]+[-]","");//replaceAll("[M][0-9]+[-]", "");
     }
    return null;
  }
   public static void main(String[] args){
    String test= "M232222-12_,M204-14_,M250-16_";
    test = findParents(test);
    //System.out.println(test);
   /* String subClass="";
    String value = "";
    String test= "M2-12_,M2-12_V23,M2-12_V12,M2-12_34,M2-12_,";
    String findsubs = YHMateUtil.findSub(test, "M2-12_");    
  
     String[] tt = findsubs.split(",");
    for(int i = 0; i<tt.length; i++){
   // System.out.println(tt[i]+":::::::");
    if(tt[i].indexOf("_")==-1){
      if(tt[i].indexOf("V")== -1){   //"如果包含V字，说明是值域"
      //System.out.println("1111");
        subClass += tt[i] +",";
       }else{
         value += tt[i].replace("V", "") +",";
         //System.out.println(value);
       }
     }
          
   }*/
 }
}

package yh.subsys.inforesouce.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHOut;
import yh.core.util.db.YHDBUtility;
import yh.subsys.inforesouce.data.YHMateShow;
import yh.subsys.inforesouce.data.YHMateType;
import yh.subsys.inforesouce.data.YHMateValue;
import yh.subsys.inforesouce.util.YHStringUtil;

/**
 * 用户控制自己要显示的元数据，影响到元数据树的显示
 * @author qwx110
 *
 */
public class YHMateShowLogic{
  
  /**
   * 保存用户所选择的
   * <p>保存数据的时候首先从数据库中查询，看看是否有此用户的seq_id,
   *    如果没有，则说明用户以前没有保存过，则直接向数据库中插入记录;
   *    如果有这个用户的id，则进行更新操作。</p>
   * @param user 登录的用户
   * @param menu 用户所选择的元数据菜单串
   * @throws Exception 
   */
   public int saveOrUpdate(Connection dbConn, YHPerson user, String pmenu,String menu, String ftype) throws Exception{
     if(iHaveSave(dbConn,user, ftype)==true){
       YHMateNodeLogic logic = new YHMateNodeLogic();
       //int ok = logic.deleteNode(dbConn, user);       
       return updateMates(dbConn, user, pmenu, menu, ftype);
       
      
     }else{
       return saveMates(dbConn, user, pmenu, menu, ftype);
     }
   }
   
   /**
    * 判断用户是否保存过。
    * @param user 当前用户
    * @return <code>true</code> or <code>false</code>
   * @throws Exception 
    */
   public boolean iHaveSave(Connection dbConn, YHPerson user, String ftype) throws Exception{
     PreparedStatement ps = null;
     ResultSet rs = null;   
     String sql = "select user_id from oa_mate_display where user_id =" + user.getSeqId() + " and typeId=" + ftype.trim();   
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
    * 保存元数据 
    * @param user 当前用户
    * @param menu 用户所选择的元数据菜单串
   * @throws Exception 
    */
   public int saveMates(Connection dbConn, YHPerson user, String pmenu, String menu, String ftype) throws Exception{
     PreparedStatement ps = null;
     ResultSet rs = null;   
     String sql = "insert into oa_mate_display(USER_ID, PR_ID, IDSTR, TYPEID) values(?,?,?,?)";   
     try{
      ps = dbConn.prepareStatement(sql);
       ps.setInt(1, user.getSeqId());
       ps.setString(2, pmenu);
       ps.setString(3, menu);
       ps.setString(4, ftype);
      int i = ps.executeUpdate();
      return i;
    } catch (SQLException e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
   }
   /**
    * 更新元数据
    * @param user 当前用户
    * @param menu 用户所选择的元数据菜单串
   * @throws Exception 
    */
   public int updateMates(Connection dbConn, YHPerson user, String pmenu, String menu, String ftype) throws Exception{
     PreparedStatement ps = null;
     ResultSet rs = null;   
     String sql = "update oa_mate_display set PR_ID=?, IDSTR=? where USER_ID =" + user.getSeqId() +" and TYPEID ="+ ftype;   
     //YHOut.println(sql +"--"+pmenu+"---"+menu);
     try{
      ps = dbConn.prepareStatement(sql);       
       ps.setString(1, pmenu);
       ps.setString(2, menu);
       int i = ps.executeUpdate();
       return i;
    } catch (SQLException e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
   }
   
   /**
    * 点击定义按钮的时候，要显示的选择的元数据菜单
    * <p>1.先把所有的元数据从库中查出来，用于显示在页面上。
    *    2.查找该用户下选择的菜单串，用于操作页面选中。
    *      如果为空，则说明没有选择，则只进行第一步。</p>            
    * @param dbConn
    * @param user 当前用户
    * @return
   * @throws Exception 
    */
   public List<YHMateType>findMyMenus(Connection dbConn, YHPerson user, String ftype) throws Exception{     
     return findAllMateType(dbConn, ftype);
   }
   
   /**
    * 查找我的选中的菜单
    * @param dbConn
    * @param user 当前用户
    * @return
   * @throws Exception 
    */
   public Map<String, String> findMySelectMenu(Connection dbConn, YHPerson user, String ftype) throws Exception{
     PreparedStatement ps = null;
     ResultSet rs = null;   
     String sql = "select SEQ_ID, USER_ID, PR_ID, IDSTR from oa_mate_display where USER_ID =" + user.getSeqId() +" and TYPEID="+ ftype.trim();
     Map<String, String> map = new HashMap<String, String>();
     try{
       ps = dbConn.prepareStatement(sql);
       rs = ps.executeQuery();
       while(rs.next()){
         map.put("paMenu", rs.getString("PR_ID"));
         map.put("subMenu", rs.getString("IDSTR"));
       }
    } catch (SQLException e){
      throw e;
    }
     return map;
   }
   
   
   /**
    * 从mate_type库中找出所有的元数据，包括这个元数据的值域(为了兼容数据库，此方法不用了)
    * @param dbConn
    * @return
   * @throws Exception 
    */
   public List<YHMateType> findAllMenus(Connection dbConn) throws Exception{
     PreparedStatement ps = null;
     ResultSet rs = null;   
     String sql = "select SEQ_ID,NUMBER_ID,CHNAME,ENNAME,DEFINEE,AIM,CONSTRAINTT,"+
                  " REPEATE,TYPE_ID,VALUE_RANGE,DEF_VALUE,ELEM_ID,PARENT_ID,NOTE from oa_mate_kind "+
                  " connect by prior seq_id = PARENT_ID"+
                  " start with PARENT_ID = 0";                    //一句话把所有的父子节点查出     
     List<YHMateType> typeList;
        try{
          ps = dbConn.prepareStatement(sql);
           rs = ps.executeQuery();
           typeList = new ArrayList<YHMateType>();
           while(rs.next()){
             YHMateType atype = new YHMateType();
             atype.setSeqId(rs.getInt("SEQ_ID"));                                          
             atype.setNumberId(rs.getString("NUMBER_ID"));
             atype.setcNname(rs.getString("CHNAME"));
             atype.seteNname(rs.getString("ENNAME"));
             atype.setDefine(rs.getString("DEFINEE"));
             atype.setAim(rs.getString("AIM"));
             atype.setConstraint(rs.getString("CONSTRAINTT"));
             atype.setRepeat(rs.getString("REPEATE"));
             atype.setTypeId(rs.getString("TYPE_ID"));
             atype.setRangeId(rs.getString("VALUE_RANGE"));
             atype.setLessValue(rs.getString("DEF_VALUE"));
             atype.setElementId(rs.getString("ELEM_ID"));
             atype.setParentId(rs.getString("PARENT_ID"));
             atype.setNote(rs.getString("NOTE"));
             if(YHStringUtil.isNotEmpty(atype.getRangeId())){//如果存在值域,取他的值域??(没有区分出是父,还是子的值域)
               List<YHMateValue> values = findValueName(dbConn, atype.getRangeId());
               atype.setValues(values);
             }
             typeList.add(atype);            
           }
        } catch (Exception e){
         throw e;
        }finally{
          YHDBUtility.close(ps, rs, null);
        }
     return typeList;
   }
   
   
   
   /**
    * 查找所有的元数据(父元素PARENT_ID = 0)
    * @param dbConn
    * @return
    * @throws Exception
    */
   public List<YHMateType> findAllMateType(Connection dbConn, String ftype)throws Exception{
     PreparedStatement ps = null;
     ResultSet rs = null;   
     String sql = "select SEQ_ID,NUMBER_ID,CHNAME,ENNAME,DEFINEE,AIM,CONSTRAINTT,"+
                  " REPEATE,TYPE_ID,VALUE_RANGE,DEF_VALUE,ELEM_ID,PARENT_ID,NOTE from oa_mate_kind " +
                  " where PARENT_ID = 0 and "+ YHDBUtility.findInSet(ftype, "ELEMENT_TYP")+" ORDER BY SEQ_ID";
     List<YHMateType> typeList;
     try{
       ps = dbConn.prepareStatement(sql);
       rs = ps.executeQuery();
       typeList = new ArrayList<YHMateType>();
       while(rs.next()){
         YHMateType atype = new YHMateType();
         atype.setSeqId(rs.getInt("SEQ_ID"));                                          
         atype.setNumberId(rs.getString("NUMBER_ID"));
         atype.setcNname(rs.getString("CHNAME"));
         atype.seteNname(rs.getString("ENNAME"));
         atype.setDefine(rs.getString("DEFINEE"));
         atype.setAim(rs.getString("AIM"));
         atype.setConstraint(rs.getString("CONSTRAINTT"));
         atype.setRepeat(rs.getString("REPEATE"));
         atype.setTypeId(rs.getString("TYPE_ID"));
         atype.setRangeId(rs.getString("VALUE_RANGE"));
         atype.setLessValue(rs.getString("DEF_VALUE"));
         atype.setElementId(rs.getString("ELEM_ID"));
         atype.setParentId(rs.getString("PARENT_ID"));
         atype.setNote(rs.getString("NOTE"));
         if(YHStringUtil.isNotEmpty(atype.getRangeId())){//如果父元素存在值域,取他的值域??()
           List<YHMateValue> values = findValueName(dbConn, atype.getRangeId());
           atype.setValues(values);
         }
         typeList.add(atype); 
         
         ////////////////////////////////一下查询atype的子元素（以上设置父元素的seq_id, atype.setSeqId(rs.getInt("SEQ_ID")),下面通过取父元素的seq_id获得父元素的子元素）/////////////////////////
         PreparedStatement ps2 = null;
         ResultSet rs2 = null;   
         String sql2 = "select SEQ_ID,NUMBER_ID,CHNAME,ENNAME,DEFINEE,AIM,CONSTRAINTT,"+
                      " REPEATE,TYPE_ID,VALUE_RANGE,DEF_VALUE,ELEM_ID,PARENT_ID,NOTE from oa_mate_kind " +
                      " where PARENT_ID ="+ atype.getSeqId() +" and " + YHDBUtility.findInSet(ftype, "ELEMENT_TYP");
         try{
          ps2 = dbConn.prepareStatement(sql2);
          rs2 = ps2.executeQuery();
          while(rs2.next()){
            YHMateType atype2 = new YHMateType();
            atype2.setSeqId(rs2.getInt("SEQ_ID"));                                          
            atype2.setNumberId(rs2.getString("NUMBER_ID"));
            atype2.setcNname(rs2.getString("CHNAME"));
            atype2.seteNname(rs2.getString("ENNAME"));
            atype2.setDefine(rs2.getString("DEFINEE"));
            atype2.setAim(rs2.getString("AIM"));
            atype2.setConstraint(rs2.getString("CONSTRAINTT"));
            atype2.setRepeat(rs2.getString("REPEATE"));
            atype2.setTypeId(rs2.getString("TYPE_ID"));
            atype2.setRangeId(rs2.getString("VALUE_RANGE"));
            atype2.setLessValue(rs2.getString("DEF_VALUE"));
            atype2.setElementId(rs2.getString("ELEM_ID"));
            atype2.setParentId(rs2.getString("PARENT_ID"));
            atype2.setNote(rs2.getString("NOTE"));
            if(YHStringUtil.isNotEmpty(atype2.getRangeId())){//如果存在值域,取子元素下的值域??()
              List<YHMateValue> values = findValueName(dbConn, atype2.getRangeId());
              atype2.setValues(values);
            }
            typeList.add(atype2);
          }
        } catch (Exception e){
          throw e;
        }finally{
          YHDBUtility.close(ps2, rs2, null);
        }
       }
       
       
     } catch (Exception e){
       throw e;
     }finally{
       YHDBUtility.close(ps, rs, null);
     }
                   
     return typeList;
   }
   /**
    * 从mate_value表中查找对应  valueids的值域名称
    * @param dbConn
    * @param ids
    * @return
    * @throws Exception 
    */
   public List<YHMateValue> findValueName(Connection dbConn, String ids) throws Exception{
     PreparedStatement ps = null;
     ResultSet rs = null; 
     String sql = "select SEQ_ID, VALUE_NAME from oa_mate_value where SEQ_ID in (" + ids +")";
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
    * 用户选择的元数据以及值域
    * @param dbConn
    * @param user 用户
    * @return
    * @throws Exception
    */
   public YHMateShow findMyShow(Connection dbConn, YHPerson user, String ftype)throws Exception{
     PreparedStatement ps = null;
     ResultSet rs = null; 
     String sql = "select SEQ_ID, USER_ID, PR_ID,IDSTR, TYPEID from oa_mate_display where USER_ID =" + user.getSeqId() + " and TYPEID="+ ftype.trim();
     
     try{
       ps = dbConn.prepareStatement(sql);
       rs = ps.executeQuery();
       while(rs.next()){
         YHMateShow show = new YHMateShow();
         show.setSEQ_ID(rs.getInt("SEQ_ID"));
         show.setUSER_ID(rs.getInt("USER_ID"));
         show.setPR_ID(rs.getString("PR_ID"));
         show.setIDSTR(rs.getString("IDSTR"));
         show.setTypeId(rs.getString("TYPEID"));
         return show;
       }
     }catch(Exception e){
       throw e;
     }finally{
       YHDBUtility.close(ps, rs, null);
     }
     return null;
   }
}

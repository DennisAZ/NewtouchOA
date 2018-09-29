package yh.core.funcs.setdescktop.setports.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.portal.data.YHPort;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHMytableLogic {
        
  private static Logger log = Logger.getLogger(YHMytableLogic.class);
  private static String[] MYTABLE_LEFT = new String[]{"内部邮件.js", "新闻.js", "待办工作.js"};
  private static String[] MYTABLE_RIGHT = new String[]{"便签.js", "图片新闻.js", "公告通知.js"};
  /**
   * 查询所有记录
   * @param conn
   * @return
   * @throws Exception
   */
  public List<YHPort> list(Connection conn) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<YHPort> list = new ArrayList<YHPort>();
    
    try{
      String sql = "select * from PORT" +
      		" order by SEQ_ID";
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        YHPort mytable = new YHPort();
        mytable.setSeqId(rs.getInt("SEQ_ID"));
        mytable.setModuleNo(rs.getInt("MODULE_NO"));
        mytable.setViewType(rs.getString("VIEW_TYPE"));
        mytable.setModuleScroll(rs.getString("MODULE_SCROLL"));
        mytable.setModulePos(rs.getString("MODULE_POS"));
        mytable.setModuleLines(rs.getInt("MODULE_LINES"));
        mytable.setFileName(rs.getString("FILE_NAME"));
        list.add(mytable);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
    return list;
  }

  /**
   * 详细查询单条记录
   * @param conn
   * @return
   * @throws Exception
   */
  public YHPort detail(Connection conn,int sqlId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    YHPort mytable = new YHPort();
    try{
      String sql = "select * from PORT" +
      		" where SEQ_ID = ?";
      ps = conn.prepareStatement(sql);
      ps.setInt(1, sqlId);
      rs = ps.executeQuery();
      if(rs.next()){
        mytable.setSeqId(rs.getInt("SEQ_ID"));
        mytable.setModuleNo(rs.getInt("MODULE_NO"));
        mytable.setViewType(rs.getString("VIEW_TYPE"));
        mytable.setModuleScroll(rs.getString("MODULE_SCROLL"));
        mytable.setModulePos(rs.getString("MODULE_POS"));
        mytable.setModuleLines(rs.getInt("MODULE_LINES"));
        mytable.setFileName(rs.getString("FILE_NAME"));
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
    return mytable;
  }
  /**
   * 查询所有记录的文件名称
   * @param conn
   * @return
   * @throws Exception
   */
  public List<String> listFiles(Connection conn) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<String> list = new ArrayList<String>();
    try{
      String sql = "select FILE_NAME from PORT";
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        if(!list.contains(rs.getString("FILE_NAME"))){
            list.add(rs.getString("FILE_NAME"));
        }
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
    return list;
  }

/**
 * 查询左侧或者右侧的模块列表
 * @param conn
 * @param side
 * @return
 * @throws Exception
 */
public List<YHPort> listSide(Connection conn,String side) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<YHPort> list = new ArrayList<YHPort>();
    
    try{
        String sql = "select * from PORT" +
        		" where MODULE_POS = ?" +
        		" order by SEQ_ID";
        ps = conn.prepareStatement(sql);
        ps.setString(1, side);
        rs = ps.executeQuery();
        while(rs.next()){
            YHPort mytable = new YHPort();
            
            mytable.setSeqId(rs.getInt("SEQ_ID"));
            mytable.setModuleNo(rs.getInt("MODULE_NO"));
            mytable.setViewType(rs.getString("VIEW_TYPE"));
            mytable.setModuleScroll(rs.getString("MODULE_SCROLL"));
            mytable.setModulePos(rs.getString("MODULE_POS"));
            mytable.setModuleLines(rs.getInt("MODULE_LINES"));
            mytable.setFileName(rs.getString("FILE_NAME"));
            list.add(mytable);
        }
    }catch(Exception ex) {
        throw ex;
    }finally {
        YHDBUtility.close(ps, rs, log);
    }
    return list;
  }

  /**
   * 通过FILE_NAME字段删除一条记录
   * @param conn
   * @param file
   * @throws Exception
   */

  public void delete(Connection conn, String file)throws Exception{
    PreparedStatement ps = null;
    try{
      String sql = "delete from PORT" +
      		" where FILE_NAME = ?";
      ps = conn.prepareStatement(sql);
      ps.setString(1, file);
      ps.executeUpdate();
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, log);
    }
  }

  /**
   * 向mytable表中添加一条记录
   * @param conn
   * @param mytable
   * @throws Exception
   */
  public void add(Connection conn, YHPort mytable) throws Exception{
    PreparedStatement ps = null;
    try{
      String sql = "insert into PORT(MODULE_NO" +
      		",VIEW_TYPE" +
      		",MODULE_SCROLL" +
      		",MODULE_POS" +
      		",MODULE_LINES" +
      		",FILE_NAME)" +
          " values(?,?,?,?,?,?)";
      
      ps = conn.prepareStatement(sql);
      ps.setInt(1, mytable.getModuleNo());
      ps.setString(2, mytable.getViewType());
      ps.setString(3, mytable.getModuleScroll());
      ps.setString(4, mytable.getModulePos());
      ps.setInt(5, mytable.getModuleLines());
      ps.setString(6, mytable.getFileName());
      ps.executeUpdate();
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, log);
    }
  }

  /**
   * 修改一条记录
   * @param conn
   * @param mytable
   * @throws Exception
   */

  public void modify(Connection conn, YHPort mytable) throws Exception{
    PreparedStatement ps = null;
    try{
      String sql = "update PORT set" +
      		" MODULE_NO = ?" +
              ",VIEW_TYPE = ?" +
              ",MODULE_SCROLL = ?" +
              ",MODULE_POS = ?" +
              ",MODULE_LINES = ?" +
              ",FILE_NAME = ?" +
              " where SEQ_ID = ?";
      ps = conn.prepareStatement(sql);
      ps.setInt(1, mytable.getModuleNo());
      ps.setString(2, mytable.getViewType());
      ps.setString(3, mytable.getModuleScroll());
      ps.setString(4, mytable.getModulePos());
      ps.setInt(5, mytable.getModuleLines());
      ps.setString(6, mytable.getFileName());
      ps.setInt(7, mytable.getSeqId());
      ps.execute();
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, log);
    }
  }

  /**
   * 修改显示属性(用户可选/用户必选/暂停显示)
   * @param conn
   * @param mytable
   * @throws Exception
   */
  public void modifyType(Connection conn, YHPort mytable) throws Exception{
    PreparedStatement ps = null;
    try{
      String sql = "update PORT set" +
      " VIEW_TYPE = ?" +
      " where SEQ_ID = ?";
      ps = conn.prepareStatement(sql);
      ps.setString(1, mytable.getViewType());
      ps.setInt(2, mytable.getSeqId());
      ps.execute();
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 修改默认为止
   * @param conn
   * @param mytable
   * @throws Exception
   */
  public void modifyPos(Connection conn, YHPort mytable) throws Exception{
    PreparedStatement ps = null;
    try{
      String sql = "update PORT set" +
      " MODULE_POS = ?" +
      " where SEQ_ID = ?";
      ps = conn.prepareStatement(sql);
      ps.setString(1, mytable.getModulePos());
      ps.setInt(2, mytable.getSeqId());
      ps.execute();
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 获取指定目录下文件的名称列表
   * @param url
   * @return
   * @throws Exception 
   */
  private List<String> checkModuleList(String url) throws Exception{
    List<String> list = null;
    try{
      list = new ArrayList<String>();
      File file = new File(url);
      for(File f:file.listFiles()){
          if(f.isFile() && f.getName().endsWith(".jsp")){
              list.add(f.getName());
          }
      }
    }catch(Exception e){
      throw e;
    }
    return list;
  }

  private int getAmountByPos(Connection dbConn, String pos) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      String sql = "select count(1) as AMOUNT from PORT where MODULE_POS = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, pos);
      rs = ps.executeQuery();
      if (rs.next()) {
        return rs.getInt("AMOUNT");
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, log);
    }
    return -1;
  }
  
  /**
   * 为所有用户添加模块
   * @param conn
   * @param moduleId
   * @param side
   * @throws Exception
   */
   public void addToAllPerson(Connection conn,int moduleId,String side) throws Exception{
     PreparedStatement ps = null;
     
     try{
       String sql = "";
       
       String dbms = YHSysProps.getProp("db.jdbc.dbms");
       if (dbms.equals("sqlserver")) {
         if("l".equals(side)){
           sql = "update PERSON set" +
               " MYTABLE_LEFT = isnull(MYTABLE_LEFT, '') + ?" +
               " where not " + YHDBUtility.findInSet("MYTABLE_LEFT", "?") + 
               " and not " + YHDBUtility.findInSet("MYTABLE_RIGHT", "?");
           
         }else if("r".equals(side)){
           sql = "update PERSON set" +
               " MYTABLE_RIGHT = isnull(MYTABLE_RIGHT, '') + ?" +
               " where not " + YHDBUtility.findInSet("MYTABLE_RIGHT", "?") + 
               " and not " + YHDBUtility.findInSet("MYTABLE_LEFT", "?");
         }
       }else if (dbms.equals("mysql")) {
         if("l".equals(side)){
           sql = "update PERSON set" +
               " MYTABLE_LEFT = concat(ifnull(MYTABLE_LEFT, ''), ?)" +
               " where not " + YHDBUtility.findInSet("MYTABLE_LEFT", "?") + 
               " and not " + YHDBUtility.findInSet("MYTABLE_RIGHT", "?");
           
         }else if("r".equals(side)){
           sql = "update PERSON set" +
               " MYTABLE_RIGHT = concat(ifnull(MYTABLE_RIGHT, ''), ?)" +
               " where not " + YHDBUtility.findInSet("MYTABLE_RIGHT", "?") + 
               " and not " + YHDBUtility.findInSet("MYTABLE_LEFT", "?");
         }
       }else if (dbms.equals("oracle")) {
         if("l".equals(side)){
           sql = "update PERSON set" +
               " MYTABLE_LEFT = MYTABLE_LEFT || ?" +
               " where not " + YHDBUtility.findInSet("MYTABLE_LEFT", "?") + 
               " and not " + YHDBUtility.findInSet("MYTABLE_RIGHT", "?");
           
         }else if("r".equals(side)){
           sql = "update PERSON set" +
               " MYTABLE_RIGHT = MYTABLE_RIGHT || ?" +
               " where not " + YHDBUtility.findInSet("MYTABLE_RIGHT", "?") + 
               " and not " + YHDBUtility.findInSet("MYTABLE_LEFT", "?");
         }
       }else {
         throw new SQLException("not accepted dbms");
       }
       
       ps = conn.prepareStatement(sql);
       ps.setString(1, moduleId + ",");
       ps.setString(2, moduleId + ",");
       ps.setString(3, moduleId + ",");
       ps.executeUpdate();
     }catch(Exception e){
       throw e;
     }finally{
       YHDBUtility.close(ps, null, log);
     }
   }
 
   /**
  * 查询viewtype < 3的所有记录
  * @param conn
  * @param pos
  * @return
  * @throws Exception
  */
   public List<YHPort> listMytableByViewType(Connection conn) throws Exception{
     Statement st = null;
     ResultSet rs = null;
     
     try{
       YHORM orm = new YHORM();
       List<YHPort> list = orm.loadListSingle(conn, YHPort.class, new String[]{"VIEW_TYPE != 3"});
       return list;
     }catch(Exception e){
       throw e;
     }finally{
       YHDBUtility.close(st, rs, log);
     }
   }
   
   
   public String getDesktopLeftWidth(Connection conn) throws Exception{
     Statement sm = null;
     ResultSet rs = null;
     try{
       String sql = "select PARA_VALUE" +
       		" from SYS_PARA" +
       		" where PARA_NAME = 'DESKTOP_LEFT_WIDTH'";
       
       sm = conn.createStatement();
       rs = sm.executeQuery(sql);
       
       if(rs.next()){
         String value = rs.getString("PARA_VALUE");
         if (!YHUtility.isNullorEmpty(value)) {
           return "0." + rs.getString("PARA_VALUE");
         }
       }
       return ".50";
     }catch(Exception e){
       throw e;
     }finally{
       YHDBUtility.close(sm, rs, log);
     }
   }
   
   /**
    * 根据id取得mytable
    * @param conn
    * @param seqId
    * @return
    * @throws Exception
    */
   public YHPort getDesktopPort(Connection conn, int seqId) throws Exception{
     PreparedStatement ps = null;
     ResultSet rs = null;
     try{
       String sql = "select count(1) as COUNT from PORT where SEQ_ID = ? and VIEW_TYPE < 3";
       
       ps = conn.prepareStatement(sql);
       ps.setInt(1, seqId);
       rs = ps.executeQuery();
       
       boolean viewFlag = false;
       
       if (rs.next()){
         viewFlag = rs.getInt("COUNT") > 0;
       }
       
       YHPort mytable = null;
       
       if (viewFlag){
         YHORM orm = new YHORM();
         mytable = (YHPort) orm.loadObjSingle(conn, YHPort.class, seqId);
       }
       return mytable;
     }catch(Exception e){
       throw e;
     }finally{
       YHDBUtility.close(ps, rs, log);
     }
   }
   
   
   public void setDefaultMytableLeft(YHPerson person, Connection dbConn) throws Exception {
     String mytableLeft = "";
     
     for (String s : MYTABLE_LEFT) {
       int id = getIdByName(dbConn, s);
       if (id >= 0) {
         mytableLeft += id + ",";
       }
     }
     
     person.setMytableLeft(mytableLeft);
   }
   
   public void setDefaultMytableRight(YHPerson person, Connection dbConn) throws Exception {
     String mytableRight = "";
     
     for (String s : MYTABLE_RIGHT) {
       int id = getIdByName(dbConn, s);
       if (id >= 0) {
         mytableRight += id + ",";
       }
     }
     
     person.setMytableRight(mytableRight);
   }
   
   private int getIdByName(Connection dbConn, String name) throws Exception {
     PreparedStatement ps = null;
     ResultSet rs = null;
     try {
       int id = -1;
       String sql = "select SEQ_ID" +
       		" from PORT" +
       		" where" +
       		" FILE_NAME = ?";
       
       ps = dbConn.prepareStatement(sql);
       ps.setString(1, name);
       rs = ps.executeQuery();
       if (rs.next()) {
         id = rs.getInt("SEQ_ID");
       }
       return id;
     } catch (Exception e) {
       throw e;
     } finally {
       YHDBUtility.close(ps, rs, log);
     }
   }
   
   public List<YHPort> getAllRequiredMytable(Connection dbConn) throws Exception {
     YHORM orm = new YHORM();
     Map<String, String> filters = new HashMap<String, String>();
     filters.put("VIEW_TYPE", "2");
     return (List<YHPort>)orm.loadListSingle(dbConn, YHPort.class, filters);
   }
}

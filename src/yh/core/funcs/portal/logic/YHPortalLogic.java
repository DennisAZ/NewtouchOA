package yh.core.funcs.portal.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.person.logic.YHUserPrivLogic;
import yh.core.funcs.portal.act.YHPortalAct;
import yh.core.funcs.portal.data.YHPort;
import yh.core.funcs.portal.data.YHPortStyle;
import yh.core.funcs.portal.data.YHPortal;
import yh.core.funcs.portal.data.YHPortalPort;
import yh.core.funcs.system.logic.YHSystemLogic;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;

public class YHPortalLogic {
  private String sp = System.getProperty("file.separator");
  private static Logger log = Logger.getLogger(YHPortalLogic.class);
  public String  getModData(Connection conn , int seqId , String wPath) throws Exception {
    StringBuffer sb = new StringBuffer();
    Statement stm = null;
    ResultSet rs = null;
    String fileName = "";
    String deptId = "";
    String privId = "";
    String userId = "";
    try {
      String query = "select FILE_NAME , DEPT_ID , PRIV_ID , USER_ID FROM PORT WHERE SEQ_ID="+ seqId;
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        fileName = rs.getString("FILE_NAME");
        deptId = rs.getString("DEPT_ID");
        privId = rs.getString("PRIV_ID");
        userId = rs.getString("USER_ID");
        deptId = (deptId == null) ? "" : deptId;
        userId = (userId == null) ? "" : userId;
        privId = (privId == null) ? "" : privId;
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, log);
    }
    YHDeptLogic deptLogic = new YHDeptLogic();
    String deptDesc = deptLogic.getNameByIdStr(deptId, conn);
    YHUserPrivLogic privLogic = new YHUserPrivLogic();
    String roleDesc = "";
    if (!YHUtility.isNullorEmpty(privId)) {
      roleDesc = privLogic.getNameByIdStr(privId, conn);
    }
    YHPersonLogic personLogic = new YHPersonLogic();
    String userDesc = personLogic.getNameBySeqIdStr(userId, conn);
    String priv = "{user:\"" + userId + "\",userDesc:\""+userDesc+"\",dept:\""+deptId+"\",deptDesc:\""+deptDesc+"\",role:\""+privId+"\",roleDesc:\""+roleDesc+"\"}";
    String path = wPath +  fileName;
    File file = new File(path);
    String script = "{}";
    if (file.exists()) {
      script = new String(YHFileUtility.loadFile2Bytes(path) , "UTF-8");
    }
    String dataPath = wPath + "data" + sp  + "data.properties";
    String defData = "";
    try {
      File dataFile = new File(dataPath);
      if (!dataFile.exists()) {
        dataFile.createNewFile();
      }
      fileName = fileName.replace(".js", "");
      Map<String , String> dataMap = new HashMap();
      YHFileUtility.load2Map(dataPath, dataMap);
      defData = dataMap.get(fileName);
      if (YHUtility.isNullorEmpty(defData)) {
        defData = "{}";
      }
    } catch (Exception ex) {
      throw ex;
    }
    sb.append("{").append("script:").append(script).append(",defData:").append(defData).append(",priv:").append(priv).append("}");
    return sb.toString();
  }
  public void newPort(Connection conn, YHPort port) throws Exception {
    if (port.getModuleLines() == 0) {
      port.setModuleLines(5);
    }
    
    if (YHUtility.isNullorEmpty(port.getModulePos())) {
      Random r = new Random();
      String pos = r.nextBoolean() ? "l" : "r";
      port.setModulePos(pos);
    }
    
    try {
      YHORM orm = new YHORM();
      orm.saveSingle(conn, port);
    } catch(Exception ex) {
      throw ex;
    } finally {
    }
  }
  
  
  /**
   * 新建门户
   * @param conn
   * @param port
   * @throws Exception
   */
  public int newPortal(Connection conn, YHPortal portal) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      YHORM orm = new YHORM();
      orm.saveSingle(conn, portal);
      
      String sql = "select SEQ_ID" +
      		" from oa_portal" +
      		" where FILE_NAME = ?";
      ps = conn.prepareStatement(sql);
      ps.setString(1, portal.getFileName());
      rs = ps.executeQuery();
      
      int id = -1;
      if (rs.next()) {
        id = rs.getInt("SEQ_ID");
      }
      return id;
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  
  public void deletePort(Connection conn, int seqId) throws Exception {
    String sp = System.getProperty("file.separator");
    String wPath = YHSysProps.getWebPath() + sp + "core" + sp 
    + "funcs" + sp 
    + "portal" + sp 
    + "modules" + sp;
    this.deletePort(conn, seqId, wPath);
  }
  
  
  public void deletePort(Connection conn, int seqId , String wPath) throws Exception {
    Statement stm = null;
    ResultSet rs = null;
    try {
      YHORM orm = new YHORM();
      String query = "select FILE_NAME FROM PORT WHERE SEQ_ID="+ seqId;
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      String fileName = "";
      if (rs.next()) {
        fileName = rs.getString("FILE_NAME");
      }
      String path = wPath +  fileName;
      File file = new File(path);
      if (file.exists()) {
        file.delete();
      }
      String dataPath = wPath + "data" + sp  + "data.properties"; 
      try {
        File dataFile = new File(dataPath);
        if (!dataFile.exists()) {
          dataFile.createNewFile();
        }
        fileName = fileName.replace(".js", "");
        Map<String , String> dataMap = new HashMap();
        YHFileUtility.load2Map(dataPath, dataMap);
        dataMap.remove(fileName);
        Set<String> set = dataMap.keySet();
        String str = "";
        for (String key : set) {
          String value = dataMap.get(key);
          str += key + " = " + value + "\r\n";
        }
        YHFileUtility.storeString2File(dataPath, str);
      } catch (Exception ex) {
        throw ex;
      }
      orm.deleteSingle(conn, YHPort.class, seqId);
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, log);
    }
  }
  
  public List<YHPort> listPort(Connection dbConn, int userId, Collection<String> depts, Collection<String> privs) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      
      String deptStr = findInSet4Collection(depts, "DEPT_ID");
      deptStr = YHUtility.isNullorEmpty(deptStr) ? "" : " or " + deptStr;
      
      String privStr = findInSet4Collection(privs, "PRIV_ID");
      privStr = YHUtility.isNullorEmpty(privStr) ? "" : " or " + privStr;
      
      String sql = "select SEQ_ID" +
          ",FILE_NAME" +
          ",REMARK" +
          " from PORT" +
          " where " +
          YHDBUtility.findInSet(String.valueOf(userId), "USER_ID") +
          " or ltrim(rtrim(DEPT_ID)) = '0'" +
          " or ltrim(rtrim(DEPT_ID)) = 'ALL_DEPT'" +
          deptStr +
          privStr;
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      
      List<YHPort> list = new ArrayList<YHPort>();
      while (rs.next()) {
        YHPort port = new YHPort();
        port.setSeqId(rs.getInt("SEQ_ID"));
        port.setFileName(YHSystemLogic.parseString(rs.getString("FILE_NAME")));
        list.add(port);
      }
      return list;
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 列出所有有权限的门户
   * @param dbConn
   * @param userId
   * @param depts
   * @param privs
   * @return
   * @throws Exception
   */
  public List<YHPortal> listPortal(Connection dbConn, int userId, Collection<String> depts, Collection<String> privs) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      
      String deptStr = findInSet4Collection(depts, "DEPT_ID");
      deptStr = YHUtility.isNullorEmpty(deptStr) ? "" : " or " + deptStr;
      
      String privStr = findInSet4Collection(privs, "PRIV_ID");
      privStr = YHUtility.isNullorEmpty(privStr) ? "" : " or " + privStr;
      
      String sql = "select SEQ_ID" +
      ",FILE_NAME" +
      ",STATUS" +
      ",PORTAL_NAME" +
      ",REMARK" +
      " from oa_portal" +
      " where " +
      YHDBUtility.findInSet(String.valueOf(userId), "USER_ID") +
      " or ltrim(rtrim(DEPT_ID)) = '0'" +
      " or ltrim(rtrim(DEPT_ID)) = 'ALL_DEPT'" +
      deptStr +
      privStr;
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      
      List<YHPortal> list = new ArrayList<YHPortal>();
      while (rs.next()) {
        YHPortal portal = new YHPortal();
        portal.setSeqId(rs.getInt("SEQ_ID"));
        portal.setPortalName(YHSystemLogic.parseString(rs.getString("PORTAL_NAME")));
        portal.setRemark(YHSystemLogic.parseString(rs.getString("REMARK")));
        portal.setStatus(rs.getInt("STATUS"));
        portal.setFileName(YHSystemLogic.parseString(rs.getString("FILE_NAME")));
        list.add(portal);
      }
      return list;
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 通过Id查询样式
   * @param dbConn
   * @param styleId
   * @return
   * @throws Exception
   */
  public YHPortStyle queryStyle(Connection dbConn, String styleId) throws Exception {
    try {
      YHORM orm = new YHORM();
      Map<String, String> filters = new HashMap<String, String>();
      filters.put("GUID", styleId);
      YHPortStyle style = (YHPortStyle)orm.loadObjSingle(dbConn, YHPortStyle.class, filters);
      return style;
    } catch(Exception ex) {
      throw ex;
    } finally {
    }
  }
 
  /**
   * 通过ID查询port信息
   * @param dbConn
   * @param portId
   * @return
   * @throws Exception
   */
  public YHPort queryPort(Connection dbConn, int portId) throws Exception {
    try {
      YHORM orm = new YHORM();
      YHPort port = (YHPort)orm.loadObjSingle(dbConn, YHPort.class, portId);
      return port;
    } catch(Exception ex) {
      throw ex;
    } finally {
    }
  }
  
  /**
   * 通过ID查询port信息,带权限判断   * @param dbConn
   * @param portId
   * @return
   * @throws Exception
   */
  public YHPort queryPort(Connection dbConn, int portId, int portalId, Collection<String> depts, Collection<String> privs) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String deptStr = findInSet4Collection(depts, "DEPT_ID");
      deptStr = YHUtility.isNullorEmpty(deptStr) ? "" : " or " + deptStr;
      
      String privStr = findInSet4Collection(privs, "PRIV_ID");
      privStr = YHUtility.isNullorEmpty(privStr) ? "" : " or " + privStr;
      
      String sql = "select SEQ_ID" +
      		",FILE_NAME" +
      		",REMARK" +
      		" from PORT" +
      		" where SEQ_ID = ?" +
      		" and (" + YHDBUtility.findInSet(String.valueOf(portalId), "USER_ID") +
      		" or ltrim(rtrim(DEPT_ID)) = '0'" +
      		" or ltrim(rtrim(DEPT_ID)) = 'ALL_DEPT'" +
      		deptStr +
      		privStr + ")";
      ps = dbConn.prepareStatement(sql);
      ps.setInt(1, portId);
      rs = ps.executeQuery();
      
      YHPort port = null;
      if (rs.next()) {
        port = new YHPort();
        port.setSeqId(rs.getInt("SEQ_ID"));
        port.setFileName(YHSystemLogic.parseString(rs.getString("FILE_NAME")));
      }
      return port;
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 扩展findInSet的参数成集合
   * @param c
   * @param dbFeildName
   * @return
   * @throws SQLException
   */
  private String findInSet4Collection(Collection<String> c, String dbFeildName) throws SQLException {
    String str = "";
    for (String s : c) {
      str += " or " + YHDBUtility.findInSet(s, dbFeildName);
    }
    return str.startsWith(" or") ? str.replaceFirst(" or", "") : str;
  }
  
  public List<YHPortalPort> listPortalPort(Connection dbConn, int portalId) throws Exception {
    try {
      YHORM orm = new YHORM();
      String[] filters = new String[]{"PORTAL_ID = " + portalId + " order by SORT_NO"};
      List<YHPortalPort> list = orm.loadListSingle(dbConn, YHPortalPort.class, filters);
      return list;
    } catch(Exception ex) {
      throw ex;
    } finally {
    }
  }
  
  private List<YHPortal> getPortalByFilters(Connection dbConn, String[] filters) throws Exception {
    //没有传递条件的时候返回空结果
    if (filters == null || filters.length == 0) {
      return null;
    }
    
    try {
      YHORM orm = new YHORM();
      List<YHPortal> list = (List<YHPortal>) orm.loadListSingle(dbConn, YHPortal.class, filters);
      
      return list;
    } catch(Exception e) {
      throw e;
    }
  }
  
  
  /**
   * 获取用户默认门户
   * 优先级为1:用户设置的默认门户;
   *        2:用户自定义的门户;
   *        3:用户部门的门户;
   *        4:用户角色的门户;
   *        5:用户辅助部门的门户(考虑了多个辅助部门的问题);
   *        6:用户辅助角色的门户(考虑了多个辅助角色的问题)
   * @param dbConn
   * @param person
   * @return
   * @throws Exception
   */
  public YHPortal getDisplayPortal(Connection dbConn, YHPerson person) throws Exception {
    
    String[] filters = new String[] {"SEQ_ID = " + person.getDefaultPortal()};
    List<YHPortal> list = this.getPortalByFilters(dbConn, filters);

    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    
    filters = new String[]{YHDBUtility.findInSet(String.valueOf(person.getSeqId()), "USER_ID")};
    list = this.getPortalByFilters(dbConn, filters);
    
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    
    filters = new String[]{YHDBUtility.findInSet(String.valueOf(person.getDeptId()), "DEPT_ID")};
    list = this.getPortalByFilters(dbConn, filters);
    
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    
    filters = new String[]{YHDBUtility.findInSet(String.valueOf(person.getUserPriv()), "PRIV_ID")};
    list = this.getPortalByFilters(dbConn, filters);
    
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    
    String deptOther = person.getDeptIdOther();
    Set<String> deptSet = YHPortalAct.string2Set(deptOther);
    String deptStr = findInSet4Collection(deptSet, "DEPT_ID");
    if (!YHUtility.isNullorEmpty(deptStr)) {
      String filter = findInSet4Collection(deptSet, "DEPT_ID");
      
      if (!YHUtility.isNullorEmpty(filter)) {
        filters = new String[]{filter};
        list = this.getPortalByFilters(dbConn, filters);
        
        if (list != null && list.size() > 0) {
          return list.get(0);
        }
      }
    }
    
    String privOther = person.getUserPrivOther();
    Set<String> privSet = YHPortalAct.string2Set(privOther);
    
    String privStr = findInSet4Collection(privSet, "PRIV_ID");
    if (!YHUtility.isNullorEmpty(privStr)) {
      String filter = findInSet4Collection(privSet, "PRIV_ID");
      if (!YHUtility.isNullorEmpty(filter)) {
        filters = new String[]{filter};
        list = this.getPortalByFilters(dbConn, filters);
        
        if (list != null && list.size() > 0) {
          return list.get(0);
        }
      }
    }
    
    return null;
  }
  
  public YHPortal queryPortal(Connection dbConn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      YHPortal up = (YHPortal) orm.loadObjSingle(dbConn, YHPortal.class, seqId);
      return up;
    } catch(Exception ex) {
      throw ex;
    } finally {
    }
  }
  
  public YHPortal queryPersonalPortal(Connection dbConn, int userId) throws Exception {
    try {
      String[] filters = new String[]{YHDBUtility.findInSet(String.valueOf(userId), "USER_ID")};
      List<YHPortal> list = this.getPortalByFilters(dbConn, filters);
      if (list.size() > 0) {
        return list.get(0);
      }
      
      return null;
    } catch(Exception ex) {
      throw ex;
    } finally {
    }
  }
  
  public void deletePortalPort(Connection dbConn, int id) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.deleteSingle(dbConn, YHPortalPort.class, id);
    } catch(Exception ex) {
      throw ex;
    } finally {
    }
  }
  
  /**
   * 添加用户的模块(用户显示在桌面上的模块)
   * 暂时没考虑权限的验证
   * @param dbConn
   * @param up
   * @throws Exception
   */
  public void addPortalPort(Connection dbConn, YHPortalPort up) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, up);
    } catch(Exception ex) {
      throw ex;
    } finally {
      
    }
  }
  
  /**
   * 增加模块的样式(对应用户或者部门角色的某一个模块的具体样式)
   * @param dbConn
   * @param ps
   * @throws Exception
   */
  public void addPortStyle(Connection dbConn, YHPortStyle ps) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, ps);
    } catch(Exception ex) {
      throw ex;
    } finally {
      
    }
  }
  
  /**
   * 更新模块的样式(对应用户或者部门角色的某一个模块的具体样式)
   * @param dbConn
   * @param ps
   * @throws Exception
   */
  public void updatePortStyle(Connection dbConn, YHPortStyle portStyle) throws Exception {
    PreparedStatement ps = null;
    try {
      String sql = "update oa_port_style" +
      		" set WIDTH = ?" +
      		",HEIGHT = ?" +
      		",POS_X = ?" +
      		",POS_Y = ?" +
      		" where GUID = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, portStyle.getWidth());
      ps.setString(2, portStyle.getHeight());
      ps.setInt(3, portStyle.getPosX());
      ps.setInt(4, portStyle.getPosY());
      ps.setString(5, portStyle.getGuid());
      ps.executeUpdate();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 更新模块的样式(对应用户或者部门角色的某一个模块的具体样式)
   * @param dbConn
   * @param ps
   * @throws Exception
   */
  public void updatePortalPort(Connection dbConn, String container, int seqId, int sortNo) throws Exception {
    PreparedStatement ps = null;
    try {
      String sql = "update oa_portal_port" +
      " set CONTAINER = ?" +
      ",SORT_NO = ?" +
      " where SEQ_ID = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, container);
      ps.setInt(2, sortNo);
      ps.setInt(3, seqId);
      ps.executeUpdate();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 更新模块的样式(对应用户或者部门角色的某一个模块的具体样式)
   * @param dbConn
   * @param ps
   * @throws Exception
   */
  public void deletePortStyle(Connection dbConn, String guid) throws Exception {
    PreparedStatement ps = null;
    try {
      String sql = "delete from oa_port_style" +
      " where GUID = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, guid);
      ps.executeUpdate();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 更新模块的样式(对应用户或者部门角色的某一个模块的具体样式)
   * @param dbConn
   * @param ps
   * @throws Exception
   */
  public void updatePortPriv(Connection dbConn, YHPort port) throws Exception {
    PreparedStatement ps = null;
    try {
      String sql = "update PORT" +
      		" set USER_ID = ?" +
      		",DEPT_ID = ?" +
      		",PRIV_ID = ?" +
      		" where SEQ_ID = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, port.getUserId());
      ps.setString(2, port.getDeptId());
      ps.setString(3, port.getPrivId());
      ps.setInt(4, port.getSeqId());
      ps.executeUpdate();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  public void updatePort(Connection dbConn, YHPort port) throws Exception {
    Statement ps = null;
    try {
      String userId = port.getUserId();
      if (userId == null) {
        userId = "";
      }
      String deptId = port.getDeptId();
      if (deptId == null) {
        deptId = "";
      }

      String privId = port.getPrivId();
      if (privId == null) {
        privId = "";
      }
      String sql = "update PORT" +
          " set FILE_NAME = '" +port.getFileName() + "'" +
          ",USER_ID = '" + userId + "'" +
          ",DEPT_ID = '" + deptId + "'" +
          ",PRIV_ID = '" + privId + "'" +
          " where SEQ_ID = " + port.getSeqId();
      ps = dbConn.createStatement();
      ps.executeUpdate(sql);
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 删除用户门户
   * @param dbConn
   * @param fileName
   * @param userId
   * @throws Exception
   */
  public void delUserPortal(Connection dbConn, int userId) throws Exception {
    PreparedStatement ps = null;
    try {
      String sql = "delete from oa_person_portal" +
      		" where USER_ID = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setInt(1, userId);
      ps.executeUpdate();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 删除用户门户
   * @param dbConn
   * @param fileName
   * @param userId
   * @throws Exception
   */
  public void addUserPortal(Connection dbConn, String fileName, int userId) throws Exception {
    PreparedStatement ps = null;
    try {
      String sql = "insert into oa_person_portal" +
      		"(PORTAL_ID" +
      		", USER_ID)" +
      		" select SEQ_ID, ? from oa_portal where FILE_NAME = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setInt(1, userId);
      ps.setString(2, fileName);
      ps.executeUpdate();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 列出所有门户   * @param dbConn
   * @param fileName
   * @param userId
   * @throws Exception
   */
  public String listPortal(Connection dbConn) throws Exception {
    Statement ps = null;
    ResultSet rs = null;
    StringBuffer sb = new StringBuffer();
    try {
      String sql = "select * from PORTAL";
      ps = dbConn.createStatement();
      rs = ps.executeQuery(sql);
      sb.append("[");
      int count = 0;
      while (rs.next()) {
        sb.append("{");
        sb.append("id:'" + rs.getString("SEQ_ID") + "'");
        sb.append(",name:'" + rs.getString("PORTAL_NAME") + "'");
        sb.append("},");
        count++;
      }
      if (count > 0) {
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
    return sb.toString();
  }
  public void deletePortal(Connection dbConn, int id) throws Exception {
    // TODO Auto-generated method stub
    Statement ps = null;
    try {
      String sql = "delete  from oa_portal where SEQ_ID=" + id;
      ps = dbConn.createStatement();
      ps.executeUpdate(sql);
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  public void setPortalPriv(Connection dbConn, int id, String role,
      String user, String dept) throws Exception {
    // TODO Auto-generated method stub
    Statement ps = null;
    try {
      String sql = "update  oa_portal set DEPT_ID = '"+dept +"' , PRIV_ID = '"+role +"' ,USER_ID = '" + user +"'  where SEQ_ID=" + id;
      ps = dbConn.createStatement();
      ps.executeUpdate(sql);
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  public boolean checkPortalName(Connection dbConn, String name) throws Exception {
    
    if (YHUtility.isNullorEmpty(name)) {
      return false;
    }
    
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String sql = "select SEQ_ID" +
      		" from oa_portal" +
      		" where PORTAL_NAME = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, name.trim());
      rs = ps.executeQuery();
      return !rs.next();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  public void setDefaultPortal(Connection dbConn, YHPerson person, int portalId) throws Exception {
    PreparedStatement ps = null;
    person.setDefaultPortal(portalId);
    try {
      String sql = "update PERSON" +
      		" set DEFAULT_PORTAL = ?" +
      		" where SEQ_ID = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setInt(1, portalId);
      ps.setInt(2, person.getSeqId());
      ps.executeUpdate();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  public boolean existPort(Connection dbConn, String fileName) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String sql = "select count(1) as AMOUNT" +
      " from PORT" +
      " where FILE_NAME = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, fileName);
      rs = ps.executeQuery();
      if (rs.next()) {
        return rs.getInt("AMOUNT") > 0;
      }
      return false;
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  public String getPortalPriv(Connection dbConn, int id) throws Exception {
    // TODO Auto-generated method stub
    Statement ps = null;
    ResultSet rs = null;
    String priv = "{}";
    try {
      String sql = "select * from oa_portal where SEQ_ID=" + id;
      ps = dbConn.createStatement();
      rs = ps.executeQuery(sql);
      String deptId  = "";
      String userId  = "";
      String privId  = "";
      if (rs.next()) {
        deptId = rs.getString("DEPT_ID");
        privId = rs.getString("PRIV_ID");
        userId = rs.getString("USER_ID");
        deptId = (deptId == null) ? "" : deptId;
        userId = (userId == null) ? "" : userId;
        privId = (privId == null) ? "" : privId;
      }
      YHDeptLogic deptLogic = new YHDeptLogic();
      String deptDesc = deptLogic.getNameByIdStr(deptId, dbConn);
      YHUserPrivLogic privLogic = new YHUserPrivLogic();
      String roleDesc = "";
      if (!YHUtility.isNullorEmpty(privId)) {
        roleDesc = privLogic.getNameByIdStr(privId, dbConn);
      }
      YHPersonLogic personLogic = new YHPersonLogic();
      String userDesc = personLogic.getNameBySeqIdStr(userId, dbConn);
      priv = "{user:\"" + userId + "\",userDesc:\""+userDesc+"\",dept:\""+deptId+"\",deptDesc:\""+deptDesc+"\",role:\""+privId+"\",roleDesc:\""+roleDesc+"\"}";
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return priv;
  }
  
  public String listPorts(Connection dbConn, YHPerson user, int id) throws Exception {
    String priv = user.getUserPriv();
    String privOther = user.getUserPrivOther();
    int dept = user.getDeptId();
    String deptOther = user.getDeptIdOther();
    YHPortal portal = null;
    
    if (id >= 0) {
      portal = queryPortal(dbConn, id);
    }
    else if (id == -2){
      portal = queryPersonalPortal(dbConn, user.getSeqId());
    }
    else {
      portal = getDisplayPortal(dbConn, user);
    }
    
    if (portal == null) {
      return null;
    }
    String path = "/" + YHSysProps.getString(YHSysPropKeys.JSP_ROOT_DIR) 
    + "/core/funcs/portal/modules/portals/" + portal.getFileName();
    
    
    Set<String> deptSet = YHPortalAct.string2Set(deptOther, String.valueOf(dept));
    Set<String> privSet = YHPortalAct.string2Set(privOther, String.valueOf(priv));
    
    List<YHPortalPort> portList = listPortalPort(dbConn, portal.getSeqId());
    
    
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    for (YHPortalPort up : portList) {
      YHPort p = queryPort(dbConn, up.getPortId(), portal.getSeqId(), deptSet, privSet);
      if (p == null) {
        continue;
      }
      
      YHPortStyle ps = queryStyle(dbConn, up.getStyleId());
      
      Map<String, String> map = new HashMap<String, String>();
      map.put("id", String.valueOf(p.getSeqId()));
      map.put("file", p.getFileName());
      map.put("parentCmp", up.getContainer());
      map.put("sortNo", String.valueOf(up.getSortNo()));
      if (ps != null) {
        map.put("width", ps.getWidth());
        map.put("height", ps.getHeight());
        map.put("posX", ps.getPosX() + "px");
        map.put("posY", ps.getPosY() + "px");
      }
      list.add(map);
    }
    
    
    return "{layoutPath:\"" + path + "\",records:"
      + this.toJson(list) + "}";
  }
  
  public StringBuffer toJson(List<Map<String, String>> list) throws Exception {
    StringBuffer sb = new StringBuffer("[");
    for (Map<String, String> map : list) {
      sb.append(YHFOM.toJson(map));
      sb.append(',');
    }
    if (sb.charAt(sb.length() - 1) == ',') {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb;
  }
}

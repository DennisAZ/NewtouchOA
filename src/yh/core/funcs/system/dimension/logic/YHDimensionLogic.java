package yh.core.funcs.system.dimension.logic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHUserPriv;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.system.dimension.data.YHDimension;
import yh.core.funcs.system.filefolder.data.YHFileSort;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHDimensionLogic {
  private static Logger log = Logger
      .getLogger("yh.core.funcs.system.filefolder.logic");

  public void saveFileSortInfo(Connection dbConn, YHDimension dimension) {
    YHORM orm = new YHORM();
    try {
      orm.saveSingle(dbConn, dimension);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 获取最大的SeqId值

   * 
   * @param dbConn
   * @return
   */
  public YHDimension getMaxSeqId(Connection dbConn) {
    // String sql="select MAX(SEQ_ID) from file_sort";
    String sql = "select SEQ_ID,SORT_NAME from oa_proportions where SEQ_ID=(select MAX(SEQ_ID) from oa_proportions ) ";
    YHDimension fileSort = null;
    int seqId = 0;
    String sortName = "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        fileSort = new YHDimension();
        seqId = rs.getInt("SEQ_ID");
        sortName = rs.getString("SORT_NAME");
        fileSort.setSeqId(seqId);
        fileSort.setSortName(sortName);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return fileSort;
  }

  public List<YHDimension> getDimensionInfo(Connection dbConn, Map map)
      throws Exception {
    YHORM orm = new YHORM();
    return orm.loadListSingle(dbConn, YHDimension.class, map);
  }
  public List<YHDimension> getDimensionInfo(Connection dbConn, String[] map) throws Exception {
    YHORM orm = new YHORM();
    return orm.loadListSingle(dbConn, YHDimension.class, map);
  }

  public YHDimension getFileSortInfoById(Connection dbConn, String seqId)
      throws NumberFormatException, Exception {
    YHORM orm = new YHORM();

    return (YHDimension) orm.loadObjSingle(dbConn, YHDimension.class, Integer
        .parseInt(seqId));
  }

  /**
   * 递归获取文件夹名路径
   * 
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public void getSortNamePath(Connection dbConn, int seqId, StringBuffer buffer)
      throws Exception {
    YHDimension fileSort = getSortNameById(dbConn, seqId);
    int sortParent = fileSort.getSortParent();
    String sortName = fileSort.getSortName();
    buffer.append(sortName + "/,");
    boolean flag = isHaveSortParent(dbConn, sortParent);
    if (flag) {
      getSortNamePath(dbConn, sortParent, buffer);
    }
  }

  /**
   * 查询文件夹信息

   * 
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public YHDimension getSortNameById(Connection dbConn, int seqId)
      throws Exception {
    YHORM orm = new YHORM();
    return (YHDimension) orm.loadObjSingle(dbConn, YHDimension.class, seqId);
  }

  /**
   * 判断是否还有子级文件夹

   * 
   * @param dbConn
   * @param sortParent
   * @return
   * @throws Exception
   */
  public boolean isHaveSortParent(Connection dbConn, int sortParent)
      throws Exception {
    Boolean flag = false;
    YHORM orm = new YHORM();
    Map map = new HashMap();
    map.put("SEQ_ID", sortParent);
    List<YHDimension> list = orm.loadListSingle(dbConn, YHDimension.class, map);
    if (list.size() > 0) {
      flag = true;
    }
    return flag;
  }

  /**
   * 递归获取单个文件夹名称

   * 
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public void getSortName(Connection dbConn, int seqId,
      StringBuffer bufferSeqId, StringBuffer bufferSortName) throws Exception {
    YHDimension fileSort = getSortNameById(dbConn, seqId);
    int sortParent = fileSort.getSortParent();
    int folderId = fileSort.getSeqId();
    String sortName = fileSort.getSortName();
    bufferSeqId.append(folderId + ",");
    bufferSortName.append(sortName + "|");
    boolean flag = isHaveSortParent(dbConn, sortParent);
    if (flag) {
      getSortName(dbConn, sortParent, bufferSeqId, bufferSortName);
    }
  }
  /**
   * 得到本级以及所有子维度的对象以及每个维度属于第几级
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public List selectDimension(Connection dbConn, int seqId,int parentId,List dimensionList,int maxSeqId) throws Exception {
    YHORM orm = new YHORM();
    Map map = new HashMap();
    //System.out.println(seqId);
    map.put("SORT_PARENT", seqId);
    List<YHDimension> list = new ArrayList<YHDimension>(); 


    list = orm.loadListSingle(dbConn, YHDimension.class, map);
    if(seqId>maxSeqId){
      return list;
    }
    int newParent = getChildDimension(dbConn,seqId, parentId, dimensionList);
    
    for (int i = 0; i < list.size(); i++) {
      YHDimension dimension = list.get(i);
      selectDimension(dbConn, dimension.getSeqId(), newParent,dimensionList,maxSeqId);
    }
    return  dimensionList;
  }
  public int getChildDimension(Connection dbConn,int seqId,int parentId,List dimensionList) throws Exception {
    YHORM orm = new YHORM();
 
    YHDimension dimension =  (YHDimension) orm.loadObjSingle(dbConn,YHDimension.class, parentId);
    YHDimension dimension2 =  (YHDimension) orm.loadObjSingle(dbConn,YHDimension.class, seqId);
    dimension.setDeptId(dimension.getDeptId());
    dimension.setDownUser(dimension.getDownUser());
    dimension.setManageUser(dimension.getManageUser());
    dimension.setNewUser(dimension.getNewUser());
    dimension.setOwner(dimension.getOwner());
    dimension.setShareUser(dimension.getShareUser());
    dimension.setSortNo(dimension.getSortNo());
    dimension.setSortName(dimension2.getSortName());
    dimension.setUserId(dimension.getUserId());
    dimension.setSortType(dimension.getSortType());
    dimension.setSortParent(parentId);
    orm.saveSingle(dbConn, dimension);
    YHDimension maxDimension = getMaxSeqId(dbConn);
    dimensionList.add(maxDimension.getSeqId());
    return maxDimension.getSeqId();
  }
  /*
   * 级联添加
   */
  public void addDimensionList(Connection dbConn,List<Map<String,String>> list,YHDimension dimension)throws Exception {
    YHORM orm = new YHORM();
    YHDimension dimensionCopy = new YHDimension();
    dimensionCopy.setDeptId(dimension.getDeptId());
    dimensionCopy.setDownUser(dimension.getDownUser());
    dimensionCopy.setManageUser(dimension.getManageUser());
    dimensionCopy.setNewUser(dimension.getNewUser());
    dimensionCopy.setOwner(dimension.getOwner());
    dimensionCopy.setShareUser(dimension.getShareUser());
    dimensionCopy.setSortNo(dimension.getSortNo());
    dimensionCopy.setSortName(dimension.getSortName());
    dimensionCopy.setUserId(dimension.getUserId());
    dimensionCopy.setSortType(dimension.getSortType());
    for (int i = 0; i < list.size(); i++) {
      Map<String,String> map = list.get(i);
      String seqId = map.get("seqId");
      String index = map.get("index");
      int MaxSeqId = getMaxSeqId(dbConn).getSeqId();
      if(index.equals("1")){
        dimensionCopy.setSortParent(dimension.getSeqId());
        orm.saveComplex(dbConn, dimensionCopy);
      }
    }
  }
  //添加子维度
  public void addChildDimension(Connection dbConn,int seqId){}
  /**
   * 从父结点递归到子结点，

   * 
   * @param dbConn
   * @param seqId
   * @return String
   * @throws Exception
   */
  public String getSortName(Connection dbConn, int seqId) throws Exception {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    YHORM orm = new YHORM();
    YHDimension fileSort = (YHDimension) orm.loadObjSingle(dbConn,YHDimension.class, seqId);
    getChild(dbConn, fileSort, sb, 0);
    if (sb.charAt(sb.length() - 1) == ',') {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");

    return sb.toString();
  }

  public void getChild(Connection dbConn, YHDimension fileSort, StringBuffer sb,
      int flag) throws Exception {
    int seqId = fileSort.getSeqId();
    String sortName = fileSort.getSortName();
    for (int i = 0; i < flag; i++) {
      sortName = "&nbsp;&nbsp;&nbsp;" + sortName;
    }
    String nameString = getAllPersonIdName(dbConn, seqId);
    sb.append("{seqId:\"" + seqId + "\",sortName:\"" + sortName + nameString
        + "\"},");
    Map map = new HashMap();
    map.put("SORT_PARENT", seqId);
    List<YHDimension> list = getFileSorts(dbConn, map);
    flag++;
    for (YHDimension sort : list) {
      getChild(dbConn, sort, sb, flag);
    }

  }

  public List<YHDimension> getSortParentId(Connection dbConn, Map map)
      throws Exception {
    YHORM orm = new YHORM();
    return orm.loadListSingle(dbConn, YHDimension.class, map);
  }

  public String getAllPersonIdName(Connection dbConn, int seqId)
      throws Exception {
    Map map = new HashMap();
    map.put("SEQ_ID", seqId);
    String visiId = "";
    String manageId = "";
    String newUserId = "";
    String downUserId = "";
    String ownerId = "";

    String visiName = "";
    String manageName = "";
    String newUserName = "";
    String downUserName = "";
    String ownerName = "";
    String nameString = "";
    String actions[] = new String[] {"OWNER", "USER_ID", "MANAGE_USER", "NEW_USER",
        "DOWN_USER" };
    YHPersonLogic personLogic = new YHPersonLogic();
    for (int i = 0; i < actions.length; i++) {
      if ("OWNER".equals(actions[i])) {
        String visiUserId = this.selectManagerIds(dbConn, map, "OWNER");
        String visiRoseId = getRoleIds(dbConn, map,"OWNER" );
        String visiDeptId = getDeptIds(dbConn, map, "OWNER");
        String visiUserRoseId = "";
        String visiUserDeptId = "";
        if(!visiRoseId.equals("")){
          visiUserRoseId = getUserIdsByRole(dbConn,visiRoseId);
        }
        if(!visiDeptId.equals("")){
          if(visiDeptId.equals("0")){
            visiUserDeptId = "0";
          }else{
            visiUserDeptId = getUserIdsByDept(dbConn, visiDeptId);
          }
        }
        ownerId = getUserIds(visiUserId, visiUserRoseId, visiUserDeptId);
        if (!ownerId.equals("")) {
          if(ownerId.equals("0")){
            ownerName = "所有人员";
          }else{
            ownerName = personLogic.getNameBySeqIdStr(ownerId, dbConn);
          }
        }
      }
      if ("USER_ID".equals(actions[i])) {
        String visiUserId  = this.selectManagerIds(dbConn, map, "USER_ID");
        String visiRoseId = getRoleIds(dbConn, map,"USER_ID" );
        String visiDeptId = getDeptIds(dbConn, map, "USER_ID");
        String visiUserRoseId = "";
        String visiUserDeptId = "";
        if(!visiRoseId.equals("")){
          visiUserRoseId = getUserIdsByRole(dbConn,visiRoseId);
        }
        if(!visiDeptId.equals("")){
          if(visiDeptId.equals("0")){
            visiUserDeptId = "0";
          }else{
            visiUserDeptId = getUserIdsByDept(dbConn, visiDeptId); 
          }
        }
        visiId = getUserIds(visiUserId, visiUserRoseId, visiUserDeptId);
        //与所有者的并集
        visiId = getUserId(visiId, ownerId);
        if (!visiId.equals("")) {
          if(visiId.equals("0")){
            visiName = "所有人员";
          }else{
            visiName = personLogic.getNameBySeqIdStr(visiId, dbConn);
          }
        }
      }
      if ("MANAGE_USER".equals(actions[i])) {
        String visiUserId = this.selectManagerIds(dbConn, map, "MANAGE_USER");
        String visiRoseId = getRoleIds(dbConn, map,"MANAGE_USER" );
        String visiDeptId = getDeptIds(dbConn, map, "MANAGE_USER");
        String visiUserRoseId = "";
        String visiUserDeptId = "";
        if(!visiRoseId.equals("")){
          visiUserRoseId = getUserIdsByRole(dbConn,visiRoseId);
        }
        if(!visiDeptId.equals("")){
          if(visiDeptId.equals("0")){
            visiUserDeptId = "0";
          }else{
            visiUserDeptId = getUserIdsByDept(dbConn, visiDeptId);
          }
        }
        manageId = getUserIds(visiUserId, visiUserRoseId, visiUserDeptId);
        //与访问权限交集
        manageId = getUserIds(manageId, visiId);
        if (!manageId.equals("")) {
          if(manageId.equals("0")){
            manageName = "所有人员";
          }else{
            manageName = personLogic.getNameBySeqIdStr(manageId, dbConn);
          }
        }
      }
      if ("NEW_USER".equals(actions[i])) {
        String visiUserId = this.selectManagerIds(dbConn, map, "NEW_USER");
        String visiRoseId = getRoleIds(dbConn, map,"NEW_USER" );
        String visiDeptId = getDeptIds(dbConn, map, "NEW_USER");
        String visiUserRoseId = "";
        String visiUserDeptId = "";
        if(!visiRoseId.equals("")){
          visiUserRoseId = getUserIdsByRole(dbConn,visiRoseId);
        }
        if(!visiDeptId.equals("")){
          if(visiDeptId.equals("0")){
            visiUserDeptId = "0";
          }else{
            visiUserDeptId = getUserIdsByDept(dbConn, visiDeptId);
          }
        }
        newUserId = getUserIds(visiUserId, visiUserRoseId, visiUserDeptId);
        //与访问权限交集
        newUserId = getUserIds(newUserId, visiId);
        if (!newUserId.equals("")) {
          if(newUserId.equals("0")){
            newUserName = "所有人员";
          }else{
            newUserName = personLogic.getNameBySeqIdStr(newUserId, dbConn);
          }
        }
      }
      if ("DOWN_USER".equals(actions[i])) {
        String visiUserId = this.selectManagerIds(dbConn, map, "DOWN_USER");
        String visiRoseId = getRoleIds(dbConn, map,"DOWN_USER" );
        String visiDeptId = getDeptIds(dbConn, map, "DOWN_USER");
        String visiUserRoseId = "";
        String visiUserDeptId = "";
        if(!visiRoseId.equals("")){
          visiUserRoseId = getUserIdsByRole(dbConn,visiRoseId);
        }
        if(!visiDeptId.equals("")){
          if(visiDeptId.equals("0")){
            visiUserDeptId = "0";
          }else{
            visiUserDeptId = getUserIdsByDept(dbConn, visiDeptId);
          }
        }
        downUserId = getUserIds(visiUserId, visiUserRoseId, visiUserDeptId);
        //与访问权限交集
        downUserId = getUserIds(downUserId, visiId);
        if (!downUserId.equals("")) {
          if(downUserId.equals("0")){
            downUserName = "所有人员";
          }else{
            downUserName = personLogic.getNameBySeqIdStr(downUserId, dbConn);
          }
        }
      }

    }
    return nameString = "\",visiName:\"" + visiName + "\",manageName:\""
        + manageName + "\",newUserName:\"" + newUserName + "\",downUserName:\""
        + downUserName+"\",ownerName:\"" + ownerName;

  }
  /**
   * 根据角色Ids得到人员 Ids
   * @param dbConn
   * @param roleId
   * @return
   * @throws Exception
   */
  public String getUserIdsByRole(Connection dbConn,String roleId)throws Exception{
  YHORM orm = new YHORM();

  String personIds = "";
  Map map = new HashMap();
  String[] str = {"USER_PRIV in(" + roleId + ")"};
  List<YHPerson> personList = orm.loadListSingle(dbConn, YHPerson.class, str);
  for (int i = 0; i < personList.size(); i++) {
    YHPerson person = personList.get(i);
    personIds = personIds + person.getSeqId() + ",";
  }
  return personIds;
 }
  /**
   * 根据部门Ids得到人员 Ids
   * @param dbConn
   * @param roleId
   * @return
   * @throws Exception
   */
  public String getUserIdsByDept(Connection dbConn,String deptId)throws Exception{
  YHORM orm = new YHORM();

  String personIds = "";
  Map map = new HashMap();
  String[] str = {"DEPT_ID in(" + deptId + ")"};
  List<YHPerson> personList = orm.loadListSingle(dbConn, YHPerson.class, str);
  for (int i = 0; i < personList.size(); i++) {
    YHPerson person = personList.get(i);
    personIds = personIds + person.getSeqId() + ",";
  }
  return personIds;
 }
  /**
   * 根据人员Ids由角色得到 的 Ids 由部门得到的Ids 得到他们的 并集
   * @param 
   * @param 
   * @return
   * @throws Exception
   */
  public String getUserIds(String userIds,String roleIds,String deptIds)throws Exception{
    String[] userIdArray = {};
    String[] roleIdArray = {};
    String[] deptIdArray = {};
    String userId = "";
    List list1 = new ArrayList();
    List list2 = new ArrayList();
    List list3 = new ArrayList();
    List listTemp = new ArrayList();
    //判断是否为全体部门userId为0
    if(deptIds!=null&&deptIds.equals("0")){
      userId = "0";
    }else{
      if(!userIds.equals("")){
        userIdArray = userIds.split(",");
        for (int i = 0; i < userIdArray.length; i++) {
          list1.add(userIdArray[i]);
        }
      }
      if(!roleIds.equals("")){
        roleIdArray = roleIds.split(",");
        for (int i = 0; i < roleIdArray.length; i++) {
          list2.add(roleIdArray[i]);
       }
      }
      if(!deptIds.equals("")){
        deptIdArray = deptIds.split(",");
        for (int i = 0; i < deptIdArray.length; i++) {
          list3.add(deptIdArray[i]);
        }
      }
      if(!userIds.equals("")){
        for (int i = 0; i < list1.size(); i++) {
          String temp1 = (String) list1.get(i);
          if(!roleIds.equals("")){
            for (int j = 0; j < list2.size(); j++) {
              String temp2 = (String) list2.get(j);
              if(temp1.equals(temp2)){
                break;
              }else{
                if(!temp1.equals(temp2)&&j==list2.size()-1){
                  list2.add(list1.get(i));
                  break;
                }
              }        
            }
          }else{
            list2 = list1;
          }
        }
      }
      if(!deptIds.equals("")){
        for (int i = 0; i < list3.size(); i++) {
          String temp1 = (String) list3.get(i);
          if(list2.size()>0){
            for (int j = 0; j < list2.size(); j++) {
              String temp2 = (String) list2.get(j);
              if(temp1.equals(temp2)){
                break;
              }else{
                if(!temp1.equals(temp2)&&j==list2.size()-1){
                  list2.add(list3.get(i));
                  break;
                }
              }
             
            }
          }else{
            list2 = list3;
          }
        }
      }
   
      for (int i = 0; i < list2.size(); i++) {
      userId = userId + list2.get(i) + ",";
      }
    }
  return userId;
 }
  /**
   * 根据访问权限的人员和所有者权限的人员Ids 得到他们的 并集
   * @param 
   * @param 
   * @return
   * @throws Exception
   */
  public String getUserId(String userIds,String deptIds)throws Exception{
    String[] userIdArray = {};
    String[] roleIdArray = {};
    String[] deptIdArray = {};
    String userId = "";
    List list1 = new ArrayList();
    List list2 = new ArrayList();
    List listTemp = new ArrayList();
    //判断是否为全体部门userId为0
    if(userIds!=null&&!userIds.equals("")){
      if(userIds.equals("0")){
        userId = "0";
        return userId;
      }else{
        userIdArray = userIds.split(",");
        for (int i = 0; i < userIdArray.length; i++) {
          list1.add(userIdArray[i]);
        }
      }
    }
    if(deptIds!=null&&!deptIds.equals("")){
      if(deptIds.equals("0")){
        userId = "0";
        return userId;
      }else{
        deptIdArray = deptIds.split(",");
        for (int i = 0; i < deptIdArray.length; i++) {
          list2.add(deptIdArray[i]);
        }
      }
    }
      for (int i = 0; i < list2.size(); i++) {
        String temp1 = (String) list2.get(i);
        if(list1.size()>0){
          for (int j = 0; j < list1.size(); j++) {
            String temp2 = (String) list1.get(j);
            if(temp1.equals(temp2)){
              break;
            }else{
              if(!temp1.equals(temp2)&&j==list1.size()-1){
                list1.add(list2.get(i));
                break;
              }
            }
          }
        }else{
          list1 = list2;
        }
      }
    for (int i = 0; i < list1.size(); i++) {
      userId = userId + list1.get(i) + ",";
    }
  return userId;
 }
  /**
   * 根据人员Ids由角色得到 的 Ids 由部门得到的Ids 得到他们的 并集后在得到访问权限的交集
   * @param 
   * @param 
   * @return
   * @throws Exception
   */
  public String getUserIds(String userIds,String visiIds)throws Exception{
    String[] userIdArray = {};
    String[] visiIdArray = {};
    String userId = "";
    List list1 = new ArrayList();
    List list2 = new ArrayList();
    List list3 = new ArrayList();
    if(userIds.equals("0")&&visiIds.equals("0")){
      return "0";
    }
    if(userIds.equals("0")&&!visiIds.equals("0")){
      return visiIds;
    }
    if(!userIds.equals("0")&&visiIds.equals("0")){
      return userIds;
    }
    if(!userIds.equals("")){
      userIdArray = userIds.split(",");
      for (int i = 0; i < userIdArray.length; i++) {
        list1.add(userIdArray[i]);
    }
    }
    if(!visiIds.equals("")){
      visiIdArray = visiIds.split(",");
      for (int i = 0; i < visiIdArray.length; i++) {
        list2.add(visiIdArray[i]);
       }
    }
    for (int i = 0; i < list1.size(); i++) {
    String temp1 = (String) list1.get(i);
    for (int j = 0; j < list2.size(); j++) {
      String temp2 = (String) list2.get(j);
      if(temp1.equals(temp2)){
        list3.add(list1.get(i));
      }
    }
  }
    for (int i = 0; i < list3.size(); i++) {
    userId = userId + list3.get(i) + ",";
  }
  return userId;
 }
/**
 * 递归重置所有下级子文件夹的权限
 * @param dbConn
 * @param seqId
 * @param setIdStr
 * @throws Exception
 */
  public void updateVisitOverride(Connection dbConn,int seqId,String setIdStr,String action) throws Exception{
    YHORM orm = new YHORM();
    YHDimension fileSort = (YHDimension) orm.loadObjSingle(dbConn,
        YHDimension.class, seqId);   
    getChildFolder(dbConn, fileSort,setIdStr,action);
    
  }
  
  public void getChildFolder(Connection dbConn,YHDimension fileSort,String setIdStr,String action) throws Exception{
    YHORM orm=new YHORM();
    int seqId = fileSort.getSeqId();
    if ("USER_ID".equals(action)) {
      fileSort.setUserId(setIdStr);      
    }
    if ("MANAGE_USER".equals(action)) {
      fileSort.setManageUser(setIdStr);
    }
    if ("NEW_USER".equals(action)) {
      fileSort.setNewUser(setIdStr);
    }
    if ("DOWN_USER".equals(action)) {
      fileSort.setDownUser(setIdStr);
    }
    if ("OWNER".equals(action)) {
      fileSort.setOwner(setIdStr);
    }
    orm.updateSingle(dbConn, fileSort);
    Map map = new HashMap();
    map.put("SORT_PARENT", seqId);
    List<YHDimension> list = getFileSorts(dbConn, map);
    //依次得到下级对象进行更新
    for (YHDimension sort : list) {
      getChildFolder(dbConn, sort, setIdStr,action);
    }
    
  }
  

  /**
   * 更新访问权限信息
   */
  public void updateVisitById(Connection dbConn, Map map) throws Exception {
    YHORM orm = new YHORM();
    // orm.updateSingle(dbConn, fileContent);
    // orm.updateSingle(dbConn, "FILE_CONTENT", map);
    PreparedStatement ps = null;
    String sql = "UPDATE oa_proportions SET USER_ID=? where SEQ_ID=? ";
    ps = dbConn.prepareStatement(sql);
    ps.setString(1, (String) map.get("userId"));
    ps.setInt(2, (Integer) map.get("seqId"));
    ps.executeUpdate();
    YHDBUtility.close(ps, null, log);

  }
  /**
   * 剪贴更新上级节点信息
   */
  public void updateVisitById(Connection dbConn, String parentId,int seqId) throws Exception {
    YHORM orm = new YHORM();
    // orm.updateSingle(dbConn, fileContent);
    // orm.updateSingle(dbConn, "FILE_CONTENT", map);
    PreparedStatement ps = null;
    String sql = "UPDATE oa_proportions SET SORT_PARENT=? where SEQ_ID=? ";
    ps = dbConn.prepareStatement(sql);
    ps.setString(1, parentId);
    ps.setInt(2, seqId);
    ps.executeUpdate();
    YHDBUtility.close(ps, null, log);

  }
  /**
   * 更新管理权限信息
   * 
   * @param dbConn
   * @param map
   * @throws Exception
   */
  public void updateManageUserById(Connection dbConn, Map map)  {
    YHORM orm = new YHORM();
    // orm.updateSingle(dbConn, fileContent);
    // orm.updateSingle(dbConn, "FILE_CONTENT", map);
    PreparedStatement ps = null;
    String sql = "UPDATE oa_proportions SET MANAGE_USER=? where SEQ_ID=? ";
    try {
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, (String) map.get("manageUser"));
      ps.setInt(2, (Integer) map.get("seqId"));
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }finally{
      
      YHDBUtility.close(ps, null, log);
    }
    

  }

  /**
   * 更新新建权限信息
   * 
   * @param dbConn
   * @param map
   * @throws Exception
   */
  public void updateNewUserById(Connection dbConn, Map map) throws Exception {
    YHORM orm = new YHORM();
    // orm.updateSingle(dbConn, fileContent);
    // orm.updateSingle(dbConn, "FILE_CONTENT", map);
    PreparedStatement ps = null;
    String sql = "UPDATE oa_proportions SET NEW_USER=? where SEQ_ID=? ";
    ps = dbConn.prepareStatement(sql);
    ps.setString(1, (String) map.get("newUser"));
    ps.setInt(2, (Integer) map.get("seqId"));
    ps.executeUpdate();
    YHDBUtility.close(ps, null, log);

  }

  /**
   * 更新下载权限信息
   * 
   * @param dbConn
   * @param map
   * @throws Exception
   */
  public void updateDownLoadById(Connection dbConn, Map map) throws Exception {
    YHORM orm = new YHORM();
    // orm.updateSingle(dbConn, fileContent);
    // orm.updateSingle(dbConn, "FILE_CONTENT", map);
    PreparedStatement ps = null;
    String sql = "UPDATE oa_proportions SET DOWN_USER=? where SEQ_ID=? ";
    ps = dbConn.prepareStatement(sql);
    ps.setString(1, (String) map.get("downUser"));
    ps.setInt(2, (Integer) map.get("seqId"));
    ps.executeUpdate();
    YHDBUtility.close(ps, null, log);

  }

  /**
   * 更新下载权限信息
   * 
   * @param dbConn
   * @param map
   * @throws Exception
   */
  public void updateOwnerById(Connection dbConn, Map map) throws Exception {
    YHORM orm = new YHORM();
    // orm.updateSingle(dbConn, fileContent);
    // orm.updateSingle(dbConn, "FILE_CONTENT", map);
    PreparedStatement ps = null;
    String sql = "UPDATE oa_proportions SET OWNER=? where SEQ_ID=? ";
    ps = dbConn.prepareStatement(sql);
    ps.setString(1, (String) map.get("owner"));
    ps.setInt(2, (Integer) map.get("seqId"));
    ps.executeUpdate();
    YHDBUtility.close(ps, null, log);

  }

  /**
   * 更新编辑文件夹信息

   * 
   * @param dbConn
   * @param map
   * @throws Exception
   */
  public void updateEditFileSort(Connection dbConn, Map map) throws Exception {
    YHORM orm = new YHORM();
    PreparedStatement ps = null;
    String sql = "UPDATE oa_proportions SET SORT_NO=?,SORT_NAME=? where SEQ_ID=? ";
    ps = dbConn.prepareStatement(sql);
    ps.setString(1, (String) map.get("sortNo"));
    ps.setString(2, (String) map.get("sortName"));
    ps.setInt(3, (Integer) map.get("seqId"));
    ps.executeUpdate();
    YHDBUtility.close(ps, null, log);

  }

  public void delDimensionInfoById(Connection dbConn, YHDimension dimension)
      throws Exception {
    YHORM orm = new YHORM();
    Map map = new HashMap();
    map.put("SORT_PARENT", dimension.getSeqId());
    List<YHDimension> dimensionList = orm.loadListComplex(dbConn, YHDimension.class,map );
    orm.deleteSingle(dbConn, dimension);
    for (int i = 0; i < dimensionList.size(); i++) {
      delDimensionInfoById(dbConn,dimensionList.get(i));
    }
  }

  public List<YHDimension> getFileSorts(Connection dbConn, Map map)
      throws Exception {
    YHORM orm = new YHORM();
    return (List<YHDimension>) orm.loadListSingle(dbConn, YHDimension.class, map);
  }

  /**
   * 判断是否有子级文件夹，不需要考虑权限
   * 
   * @param dbConn
   * @param id
   * @return
   * @throws Exception
   */
  public int isHaveChild(Connection dbConn, int id, int userSeqId,
      String loginUserRoleId, int loginUserDeptId) throws Exception {
    YHORM orm = new YHORM();
    Map map = new HashMap();
    Map map2 = new HashMap();
    map.put("SORT_PARENT", id);
    boolean userFlag = false;
    boolean roleFlag = false;
    boolean deptFlag = false;
    
    boolean ownerUserFlag=false;
    boolean ownerRoleFlag=false;
    boolean ownerDeptFlag=false;    
    int count = 0;

    List<YHDimension> list = orm.loadListSingle(dbConn, YHDimension.class, map);
    if (list.size() > 0) {
      for (YHDimension fileSort : list) {
        String idss = fileSort.getUserId();
        map2.put("SEQ_ID", fileSort.getSeqId());
        String userPrivsIds = this.selectManagerIds(dbConn, map2, "USER_ID");
        String rolePrivs = this.getRoleIds(dbConn, map2, "USER_ID");
        String deptPrivs = this.getDeptIds(dbConn, map2, "USER_ID");
        
        userFlag = this.getUserIdStr(userSeqId, userPrivsIds, dbConn);
        roleFlag = this.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
        deptFlag = this.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
        
        String ownerUserPrivs=this.selectManagerIds(dbConn, map2, "OWNER");
        String ownerRolePrivs=this.getRoleIds(dbConn, map2, "OWNER");
        String ownerDeptPrivs=this.getDeptIds(dbConn, map2, "OWNER");
        
        ownerUserFlag=this.getUserIdStr(userSeqId, ownerUserPrivs, dbConn);
        ownerRoleFlag=this.getRoleIdStr(loginUserRoleId,ownerRolePrivs,dbConn);
        ownerDeptFlag=this.getDeptIdStr(loginUserDeptId, ownerDeptPrivs, dbConn);       

        if (ownerUserFlag||ownerRoleFlag||ownerDeptFlag) {
          return 1;
        }else if (userFlag == true || roleFlag == true || deptFlag == true) {
          return 1;
        }
      }
      return 0;
    } else {
      return 0;
    }
  }

  /**
   * 判断是否有子级文件夹,考虑权限。

   * 
   * @param dbConn
   * @param id
   * @return
   * @throws Exception
   */
  public int isHaveChild(Connection dbConn, int id) throws Exception {
    YHORM orm = new YHORM();
    Map map = new HashMap();
    map.put("SORT_PARENT", id);
    List<YHDimension> list = orm.loadListSingle(dbConn, YHDimension.class, map);
    if (list.size() > 0) {
      return 1;
    } else {
      return 0;
    }
  }

  /*
   * 得到人员的id字符串

   */
  public String selectManagerIds(Connection dbConn, Map map, String action)
      throws Exception {
    YHORM orm = new YHORM();
    String ids = "";
    ArrayList<YHDimension> managerList = (ArrayList<YHDimension>) orm
        .loadListSingle(dbConn, YHDimension.class, map);
    if (managerList.size() > 0) {
      YHDimension manager = managerList.get(0);
      if ("USER_ID".equals(action)) {
        if (manager.getUserId() != null) {
          String idString = manager.getUserId();
          if(idString!=null&&!idString.trim().equals("")){
            //System.out.println(idString);
            String[] idsStrings = idString.split("\\|");
            if(idsStrings.length>0){
              //System.out.println(idsStrings);
              if (idsStrings.length == 2) {
                ids = "";
              } else if (idsStrings.length == 1) {
                ids = "";

              } else {
                ids = idsStrings[2];
              }
            }
          }
         
        }
      } else if ("MANAGE_USER".equals(action)) {
        if (manager.getManageUser() != null) {
          String idString = manager.getManageUser();
          String[] idsStrings = idString.split("\\|");
          //System.out.println(idsStrings);
          if(idsStrings.length>0){
            if (idsStrings.length == 2) {
              ids = "";
            } else if (idsStrings.length == 1) {
              ids = "";

            } else {
              ids = idsStrings[2];
            }
          }
         
        }
      } else if ("NEW_USER".equals(action)) {
        if (manager.getNewUser() != null) {
          String idString = manager.getNewUser();
          String[] idsStrings = idString.split("\\|");
          //System.out.println(idsStrings);
          if(idsStrings.length>0){
            if (idsStrings.length == 2) {
              ids = "";
            } else if (idsStrings.length == 1) {
              ids = "";

            } else {
              ids = idsStrings[2];
            }

          }
         
        }
      } else if ("DOWN_USER".equals(action)) {
        if (manager.getDownUser() != null) {
          String idString = manager.getDownUser();
          String[] idsStrings = idString.split("\\|");
          //System.out.println(idsStrings);
          if(idsStrings.length>0){
            if (idsStrings.length == 2) {
              ids = "";
            } else if (idsStrings.length == 1) {
              ids = "";

            } else {
              ids = idsStrings[2];
            }
          }
        }
      }else if ("OWNER".equals(action)) {
        if (manager.getOwner() != null) {
          String idString = manager.getOwner();
          String[] idsStrings = idString.split("\\|");
          //System.out.println(idsStrings);
          if(idsStrings.length>0){
            if (idsStrings.length == 2) {
              ids = "";
            } else if (idsStrings.length == 1) {
              ids = "";

            } else {
              ids = idsStrings[2];
            }

          }
        }
      }

    }
    //System.out.println("ids=====" + ids);
    return ids;
  }

  /*
   * 根据人员id字符串得到name字符串

   */
  public String getNamesByIds(Connection dbConn, Map map, String action)
      throws Exception {
    String names = "";
    YHPersonLogic tpl = new YHPersonLogic();
    String ids = selectManagerIds(dbConn, map, action);
    //System.out.println(ids);
    names = tpl.getNameBySeqIdStr(ids, dbConn);
    return names;
  }

  /**
   * 得到角色人员的id字符串

   * 
   * @param dbConn
   * @param map
   * @return
   * @throws Exception
   */
  public String getRoleIds(Connection dbConn, Map map, String action)
      throws Exception {
    YHORM orm = new YHORM();
    String ids = "";
    ArrayList<YHDimension> managerList = (ArrayList<YHDimension>) orm
        .loadListSingle(dbConn, YHDimension.class, map);
    if (managerList.size() > 0) {
      YHDimension manager = managerList.get(0);
      if ("USER_ID".equals(action)) {
        if (manager.getUserId() != null) {
          String idString = manager.getUserId();
          //System.out.println(idString);
          String[] idsStrings = idString.split("\\|");
          //System.out.println("角色idsStrings:" + idsStrings.length);
          if(idsStrings.length>0){
            if (idsStrings.length == 1) {
              ids = "";
            } else {
              ids = idsStrings[1];
            }
          }
        }
      } else if ("MANAGE_USER".equals(action)) {
        if (manager.getManageUser() != null) {
          String idString = manager.getManageUser();
          //System.out.println(idString);
          String[] idsStrings = idString.split("\\|");
          //System.out.println("角色idsStrings:" + idsStrings.length);
          if(idsStrings.length>0){
            if (idsStrings.length == 1) {
              ids = "";
            } else {
              ids = idsStrings[1];
            }
          }
        }
      } else if ("NEW_USER".equals(action)) {
        if (manager.getNewUser() != null) {
          String idString = manager.getNewUser();
          //System.out.println(idString);
          String[] idsStrings = idString.split("\\|");
          //System.out.println("角色idsStrings:" + idsStrings.length);
          if(idsStrings.length>0){
            if (idsStrings.length == 1) {
              ids = "";
            } else {
              ids = idsStrings[1];
            }
          }
          
        }
      } else if ("DOWN_USER".equals(action)) {
        if (manager.getDownUser() != null) {
          String idString = manager.getDownUser();
          //System.out.println(idString);
          String[] idsStrings = idString.split("\\|");
          //System.out.println("角色idsStrings:" + idsStrings.length);
          if(idsStrings.length>0){
            if (idsStrings.length == 1) {
              ids = "";
            } else {
              ids = idsStrings[1];
            }
          }
        }
      }else if ("OWNER".equals(action)) {
        if (manager.getOwner() != null) {
          String idString = manager.getOwner();
          //System.out.println(idString);
          String[] idsStrings = idString.split("\\|");
          //System.out.println("角色idsStrings:" + idsStrings.length);
          if(idsStrings.length>0){
            if (idsStrings.length == 1) {
              ids = "";
            } else {
              ids = idsStrings[1];
            }
          }

        }
      }

    }
    //System.out.println("ids=====" + ids);
    return ids;
  }

  /**
   * 根据角色人员id字符串得到name字符串

   * 
   * @param dbConn
   * @param map
   * @return
   * @throws Exception
   */
  public String getRoleNamesByIds(Connection dbConn, Map map, String action)
      throws Exception {
    String names = "";
    YHDimensionLogic tpl = new YHDimensionLogic();
    String ids = getRoleIds(dbConn, map, action);
    //System.out.println(ids);
    names = tpl.getRoleNameBySeqIdStr(dbConn, ids);
    return names;
  }

  /**
   * 根据userId中的角色Id串返回与登录的角色Id比较判断是否相等，返回boolean类型。

   * 
   * @param ids
   * @return
   * @throws Exception
   * @throws Exception
   */
  public boolean getRoleIdStr(String loginUserRoleId, String ids,
      Connection dbConn) throws Exception, Exception {
    boolean flag = false;
    if (ids != null && !"".equals(ids)) {
      String[] aId = ids.split(",");
      for (String tmp : aId) {
        if (!"".equals(tmp)) {
          if (Integer.parseInt(tmp) == Integer.parseInt(loginUserRoleId)) {
            flag = true;
          }
        }
      }
    }
    return flag;
  }

  /**
   * 根据seqId串返回一个角色名字串
   * 
   * @param ids
   * @return
   * @throws Exception
   * @throws Exception
   */
  public String getRoleNameBySeqIdStr(Connection dbConn, String ids)
      throws Exception, Exception {
    String names = "";
    if (ids != null && !"".equals(ids)) {
      String[] aId = ids.split(",");
      for (String tmp : aId) {
        if (!"".equals(tmp)) {
          // YHPerson person = this.getPersonById(Integer.parseInt(tmp));
          YHUserPriv privName = this.getPersonById(dbConn, Integer
              .parseInt(tmp));
          if (privName != null) {
            names += privName.getPrivName() + ",";
          }
        }
      }
    }
    return names;
  }

  /**
   * 根据seqId查询角色信息
   * 
   * @param dbConn
   * @param seqId
   * @return YHUserPriv对象
   * @throws Exception
   */
  public YHUserPriv getPersonById(Connection dbConn, int seqId)
      throws Exception {
    YHORM orm = new YHORM();
    YHUserPriv userPriv = (YHUserPriv) orm.loadObjSingle(dbConn,
        YHUserPriv.class, seqId);
    ;
    return userPriv;
  }

  /**
   * 得到部门人员的id字符串

   * 
   * @param dbConn
   * @param map
   * @return
   * @throws Exception
   */
  public String getDeptIds(Connection dbConn, Map map, String action)
      throws Exception {
    YHORM orm = new YHORM();
    String ids = "";
    ArrayList<YHDimension> managerList = (ArrayList<YHDimension>) orm
        .loadListSingle(dbConn, YHDimension.class, map);
    if (managerList.size() > 0) {
      YHDimension manager = managerList.get(0);
      if ("USER_ID".equals(action)) {
        if (manager.getUserId() != null) {
          String idString = manager.getUserId();
          //System.out.println(idString);
          String[] idsStrings = idString.split("\\|");
          //System.out.println(idsStrings);
          if(idsStrings.length>0){
            if (idsStrings[0].length() != 0) {
              ids = idsStrings[0];
            }
          }
        }
      } else if ("MANAGE_USER".equals(action)) {
        if (manager.getManageUser() != null) {
          String idString = manager.getManageUser();
          //System.out.println(idString);
          String[] idsStrings = idString.split("\\|");
          //System.out.println(idsStrings);
          if(idsStrings.length>0){
            if (idsStrings[0].length() != 0) {
              ids = idsStrings[0];
            }
          }
        }
      } else if ("NEW_USER".equals(action)) {
        if (manager.getNewUser() != null) {
          String idString = manager.getNewUser();
          //System.out.println(idString);
          String[] idsStrings = idString.split("\\|");
          //System.out.println(idsStrings);
          if(idsStrings.length>0){
            if (idsStrings[0].length() != 0) {
              ids = idsStrings[0];
            }
          }
        }
      } else if ("DOWN_USER".equals(action)) {
        if (manager.getDownUser() != null) {
          String idString = manager.getDownUser();
          //System.out.println(idString);
          String[] idsStrings = idString.split("\\|");
          //System.out.println(idsStrings);
          if(idsStrings.length>0){
            if (idsStrings[0].length() != 0) {
              ids = idsStrings[0];
            }
          }
        }
      }else if ("OWNER".equals(action)) {
        if (manager.getOwner() != null) {
          String idString = manager.getOwner();
          //System.out.println(idString);
          String[] idsStrings = idString.split("\\|");
          //System.out.println(idsStrings);
          if(idsStrings.length>0){
            if (idsStrings[0].length() != 0) {
              ids = idsStrings[0];
            }
          }
        }
      }

    }
    //System.out.println("ids=====" + ids);
    return ids;
  }

  /**
   * 根据部门id字符串得到name字符串

   * 
   * @param dbConn
   * @param map
   * @return
   * @throws Exception
   */
  public String getDeptByIds(Connection dbConn, Map map, String action)
      throws Exception {
    String names = "";
    YHDimensionLogic tpl = new YHDimensionLogic();
    String ids = getDeptIds(dbConn, map, action);
    //System.out.println(ids);
    names = tpl.getDeptNameBySeqIdStr(dbConn, ids);
    return names;
  }

  /**
   * 根据seqId串返回一个部门名字串
   * 
   * @param ids
   * @return
   * @throws Exception
   * @throws Exception
   */
  public String getDeptNameBySeqIdStr(Connection dbConn, String ids)
      throws Exception, Exception {
    String names = "";
    if (ids != null && !"".equals(ids)) {
      String[] aId = ids.split(",");
      for (String tmp : aId) {
        if (!"".equals(tmp)) {
          YHDepartment deptName = getDeptById(dbConn, Integer.parseInt(tmp));
          if (deptName != null) {
            names += deptName.getDeptName() + ",";
          }
        }
      }
    }
    return names;
  }

  /**
   * 根据登录用户的部门Id与权限中的部门Id对比返回boolean
   * 
   * @param ids
   * @return
   * @throws Exception
   * @throws Exception
   */
  public boolean getDeptIdStr(int loginUserDeptId, String ids, Connection dbConn)
      throws Exception, Exception {
    boolean flag = false;
    if(ids != null&&ids.equals("0")){
      flag = true;
    }
    if (ids != null && !"".equals(ids)) {
      String[] aId = ids.split(",");
      for (String tmp : aId) {
        if (!"".equals(tmp)) {
          if (loginUserDeptId == Integer.parseInt(tmp)) {
            flag = true;
          }
        }
      }
    }
    return flag;
  }

  /**
   * 根据seqId查询部门信息
   * 
   * @param dbConn
   * @param seqId
   * @return YHDepartment对象
   * @throws Exception
   */
  public YHDepartment getDeptById(Connection dbConn, int seqId)
      throws Exception {
    YHORM orm = new YHORM();
    YHDepartment department = (YHDepartment) orm.loadObjSingle(dbConn,
        YHDepartment.class, seqId);
    ;
    return department;
  }

  /**
   * 获取访问权限：根据ids串返回与登录的seqId比较判断是否相等，返回boolean类型。

   * 
   * @param ids
   * @return
   * @throws Exception
   * @throws Exception
   */
  public boolean getUserIdStr(int userSeqId, String ids, Connection dbConn)
      throws Exception, Exception {
    boolean flag = false;
    if (ids != null && !"".equals(ids)) {
      String[] aId = ids.split(",");
      for (String tmp : aId) {
        if (!"".equals(tmp)) {
          if (Integer.parseInt(tmp) == userSeqId) {
            flag = true;
          }
        }
      }
    }
    return flag;
  }

  /**
   * 获取访问权限
   * @param dbConn
   * @param map
   * @param loginUserSeqId
   * @param loginUserDeptId
   * @param loginUserRoleId
   * @return
   * @throws Exception
   */
  public String getVisiPriv(Connection dbConn, Map map, int loginUserSeqId,
      int loginUserDeptId, String loginUserRoleId) throws Exception {
    StringBuffer sb = new StringBuffer("[");
    int visiPrivFlag = 0;
    int managePrivFlag = 0;
    int newPrivFlag=0;
    int downPrivFlag =0;
    int ownerPrivFlag=0;
    int downUserPrivFlag=0;
    YHORM orm=new YHORM();
    YHDimension fileSort= (YHDimension) orm.loadObjSingle(dbConn, YHDimension.class, map);
    int sortParent=fileSort.getSortParent();
    String[] actions = new String[] { "USER_ID", "MANAGE_USER", "NEW_USER",
        "DOWN_USER","OWNER" };
    for (int i = 0; i < actions.length; i++) {
      if ("USER_ID".equals(actions[i])) {
        String userPrivs = this.selectManagerIds(dbConn, map, "USER_ID");
        String rolePrivs = this.getRoleIds(dbConn, map, "USER_ID");
        String deptPrivs = this.getDeptIds(dbConn, map, "USER_ID");

        boolean userFlag = this.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
        boolean deptFlag = this.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
        boolean roleFlag = this.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
        if (userFlag || deptFlag || roleFlag) {
          visiPrivFlag = 1;
        }
      }
      if ("MANAGE_USER".equals(actions[i])) {
        String userPrivs = this.selectManagerIds(dbConn, map, "MANAGE_USER");
        String rolePrivs = this.getRoleIds(dbConn, map, "MANAGE_USER");
        String deptPrivs = this.getDeptIds(dbConn, map, "MANAGE_USER");

        boolean userFlag = this.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
        boolean deptFlag = this
            .getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
        boolean roleFlag = this
            .getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
        if (userFlag || deptFlag || roleFlag) {
          managePrivFlag = 1;
        }
      }
      if ("NEW_USER".equals(actions[i])) {
        String userPrivs = this.selectManagerIds(dbConn, map, "NEW_USER");
        String rolePrivs = this.getRoleIds(dbConn, map, "NEW_USER");
        String deptPrivs = this.getDeptIds(dbConn, map, "NEW_USER");

        boolean userFlag = this.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
        boolean deptFlag = this
            .getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
        boolean roleFlag = this
            .getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
        if (userFlag || deptFlag || roleFlag) {
          newPrivFlag = 1;
        }
      }
      if ("DOWN_USER".equals(actions[i])) {
        String userPrivs = this.selectManagerIds(dbConn, map, "DOWN_USER");
        String rolePrivs = this.getRoleIds(dbConn, map, "DOWN_USER");
        String deptPrivs = this.getDeptIds(dbConn, map, "DOWN_USER");

        boolean userFlag = this.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
        boolean deptFlag = this
            .getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
        boolean roleFlag = this
            .getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
        if (userFlag || deptFlag || roleFlag) {
          downPrivFlag = 1;
        }
      }
      if ("OWNER".equals(actions[i])) {
        String userPrivs = this.selectManagerIds(dbConn, map, "OWNER");
        String rolePrivs = this.getRoleIds(dbConn, map, "OWNER");
        String deptPrivs = this.getDeptIds(dbConn, map, "OWNER");

        boolean userFlag = this.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
        boolean deptFlag = this
            .getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
        boolean roleFlag = this
            .getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
        if (userFlag || deptFlag || roleFlag) {
          ownerPrivFlag = 1;
        }
      }
      if ("DOWN_USER".equals(actions[i])) {
        String userPrivs = this.selectManagerIds(dbConn, map, "DOWN_USER");
        String rolePrivs = this.getRoleIds(dbConn, map, "DOWN_USER");
        String deptPrivs = this.getDeptIds(dbConn, map, "DOWN_USER");
        
        boolean userFlag = this.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
        boolean deptFlag = this
        .getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
        boolean roleFlag = this
        .getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
        if (userFlag || deptFlag || roleFlag) {
          downUserPrivFlag = 1;
        }
      }
    }
    
    sb.append("{");
    sb.append("visiPriv:\"" + visiPrivFlag + "\"");
    sb.append(",managePriv:\"" + managePrivFlag + "\"");
    sb.append(",newPriv:\"" + newPrivFlag + "\"");
    sb.append(",downPriv:\"" + downPrivFlag + "\"");
    sb.append(",ownerPriv:\"" + ownerPrivFlag + "\"");
    sb.append(",downUserPriv:\"" + downUserPrivFlag + "\"");
    sb.append(",sortParent:\"" + sortParent + "\"");
    sb.append(",seqId:\"" + fileSort.getSeqId() + "\"");
    sb.append("},");
    sb.append("]");
    
    return sb.toString();
  }

  public void getSortName(Connection dbConn,int seqId,int parentId){
    String sql="select SEQ_ID,SORT_PARENT,SORT_NO,SORT_NAME,SORT_TYPE from oa_proportions where SORT_PARENT =146 ";
    PreparedStatement ps=null;
    ResultSet rs=null;
    try {
      ps=dbConn.prepareStatement(sql);
      ps.setInt(1, parentId);
      rs=ps.executeQuery();
      while(rs.next()){
        
      }
      
      
      
    } catch (SQLException e) {
      
      e.printStackTrace();
    }
    
    
  }
  
  
  public static void main(String[] args) {
    String s = "|11|";
    //System.out.println(s.split("\\|").length);
    // String ids = "213,||";
    // String[] tem = ids.split("\\|");
    // System.out.println(tem.length);
    // System.out.println("tem[0]:" + tem[0]);
    // System.out.println("tem[1]:" + tem[1]);
    // System.out.println("tem[1].length>>" + tem[1].length());
    // System.out.println("tem[2]:" + tem[2]);
    // if (tem[1].length() == 0) {
    // tem[1] = "0,";
    // System.out.println("if>>tem[1]:" + tem[1]);
    // }

  }

}


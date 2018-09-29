package yh.subsys.inforesouce.act;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.office.ntko.logic.YHNtkoLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.personfolder.data.YHFileSort;
import yh.core.funcs.personfolder.logic.YHPersonFolderLogic;
import yh.core.funcs.system.filefolder.logic.YHFileSortLogic;
import yh.core.funcs.system.netdisk.data.YHNetdisk;
import yh.core.funcs.system.netdisk.logic.YHNetdiskLogic;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.subsys.inforesouce.logic.YHBeachDownLoadFileLogic;


/**
 * 批量下载，批量转存
 * 调用的类有：YHBeachDownLoadFileLogic,YHNtkoLogic
 * @see yh.subsys.inforesouce.logic.YHBeachDownLoadFileLogic
 * @see yh.core.funcs.office.ntko.logic.YHNtkoLogic
 * @author Administrator
 *
 */
public class YHBeachDownLoadFileAct{
  /**
   * 信息资源管理中批量下载
   * 调用YHBeachDownLoadFileLogic的toZipInfoMapFile方法生成文件的map
   * 调用YHNtkoLogic的zip进行打包成zip格式
   * @see yh.subsys.inforesouce.logic.YHBeachDownLoadFileLogic#toZipInfoMapFile(Connection, YHPerson)
   * @see yh.core.funcs.office.ntko.logic.YHNtkoLogic#zip(Map, OutputStream)
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String beanchDownload(HttpServletRequest request, HttpServletResponse response)throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String downName = "文件管理中心.zip";
      String fileName = URLEncoder.encode(downName, "UTF-8");
      OutputStream ops = null;
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control", "private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges", "bytes");
      response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
      ops = response.getOutputStream();
      YHBeachDownLoadFileLogic downLogic = new YHBeachDownLoadFileLogic();
      YHNtkoLogic nl = new YHNtkoLogic();
      Map<String, InputStream> map = downLogic.toZipInfoMapFile(dbConn,  user);
      nl.zip(map, ops);
      ops.flush();
    } catch (Exception ex){
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;  
    } 
    return null;
  }
  
  
  
  /**
   * 获取转存到个人文件柜的根目录树信息(copy自个人文件柜转存)
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getPersonFolderInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqIdStr = request.getParameter("seqId");
    int seqId = 0;
    if (seqIdStr != null) {
      seqId = Integer.parseInt(seqIdStr);
    }
    // 获取登录用户信息
    YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
    int loginUserSeqId = loginUser.getSeqId();

    List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
    YHPersonFolderLogic logic = new YHPersonFolderLogic();
    String sortType = "4";

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      int inIt = 0;
      if (seqId != 0) {
        inIt = 1;
      }

      YHFileSort fileSort = logic.getFileSortInfoById(dbConn, String.valueOf(seqId));
      int parentIdStr = 0;
      if (fileSort != null) {
        parentIdStr = fileSort.getSortParent();
      }

      String[] filters = { "SORT_PARENT=" + seqId + " and SORT_TYPE='" + sortType + "' order by SORT_NO,SORT_NAME" };
      List<YHFileSort> parentList = logic.getFileSorts(dbConn, filters);
      if (seqId == 0) {
        Map<String, String> defaulMap = new HashMap<String, String>();
        defaulMap.put("seqId", String.valueOf(0));
        defaulMap.put("sortName", "根目录");
        returnList.add(defaulMap);

      }

      if (parentList != null && parentList.size() > 0) {
        boolean userFlag = false;
        for (YHFileSort fileFolder : parentList) {
          String userPrivs = logic.getUserId(dbConn, fileFolder.getSeqId(), "USER_ID");
          userFlag = logic.getUserIdStr(loginUserSeqId, userPrivs, dbConn);

          if (userFlag) {
            Map<String, String> sortMap = new HashMap<String, String>();

            sortMap.put("seqId", String.valueOf(fileFolder.getSeqId()));
            sortMap.put("sortName", fileFolder.getSortName());
            sortMap.put("sortParent", String.valueOf(fileFolder.getSortParent()));
            returnList.add(sortMap);

          }
        }
      }
      request.setAttribute("inIt", inIt);
      request.setAttribute("seqId", seqId);
      request.setAttribute("parentId", parentIdStr);

      request.setAttribute("fileSortList", returnList);

    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
    }

    return "/subsys/inforesource/savefile/personFolderList.jsp";
  }
  

  /**
   * 个人文件柜转存
   * <br>调用YHBeachDownLoadFileLogic的transferFolder进行转存
   * <br>
   * @param request
   * @param response
   * @see yh.subsys.inforesouce.logic.YHBeachDownLoadFileLogic#transferFolder(Connection, YHPerson, int, int, String)
   * @return "/core/inc/rtjson.jsp"
   * @throws Exception
   */
  public String transferFolder(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqIdStr = request.getParameter("checkId");
    int seqId = 0;
    if (seqIdStr != null) {
      seqId = Integer.parseInt(seqIdStr);
    }
    YHBeachDownLoadFileLogic logic = new YHBeachDownLoadFileLogic();
    String separator = File.separator;   
    String folderPath = YHSysProps.getAttachPath() + separator + "file_folder";

    // 获取登录用户信息
    YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
    Connection dbConn = null;
    try {
      YHRequestDbConn requesttDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requesttDbConn.getSysDbConn();
      boolean flag = logic.transferFolder(dbConn, loginUser, seqId, 1, folderPath);
      if (flag) {
        request.setAttribute(YHActionKeys.RET_MSRG, "文件转存成功！");
      } else {
        request.setAttribute(YHActionKeys.RET_MSRG, "文件转存失败！");
      }
      String data = "{flag:\"" + flag + "\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);

    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取转存到公共文件柜的根目录信息（copy自公共文件柜转存）
   * 
   * @param request
   * @param response
   * @return "/subsys/inforesource/savefile/folder1.jsp"
   * @throws Exception
   */
  public String getFolderInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqIdStr = request.getParameter("seqId");
    String parentIdStr = request.getParameter("parentId");
    String attachId = request.getParameter("attachId");
    String attachName = request.getParameter("attachName");
    String backFlag = request.getParameter("backFlag");
    String module = request.getParameter("module");

    int parentId = 0;
    int seqId = 0;
    if (seqIdStr != null) {
      seqId = Integer.parseInt(seqIdStr);
    }
    if (parentIdStr != null) {
      parentId = Integer.parseInt(parentIdStr);
    }
    if (backFlag == null) {
      backFlag = "";
    }
    if (module == null) {
      module = "";
    }

    // 获取登录用户信息
    YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
    int loginUserSeqId = loginUser.getSeqId();
    int loginUserDeptId = loginUser.getDeptId();
    String loginUserRoleId = loginUser.getUserPriv();

    boolean userFlag = false;
    boolean roleFlag = false;
    boolean deptFlag = false;

    boolean newUserFlag = false;
    boolean newRoleFlag = false;
    boolean newDeptFlag = false;

    List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
    Map map = new HashMap();
    YHFileSortLogic fileSortLogic = new YHFileSortLogic();
    yh.core.funcs.system.filefolder.data.YHFileSort fileSort = new yh.core.funcs.system.filefolder.data.YHFileSort();
    List<yh.core.funcs.system.filefolder.data.YHFileSort> list = new ArrayList<yh.core.funcs.system.filefolder.data.YHFileSort>();

    int inIt = 0;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      int sortPar = 0;

      if ("back".equals(backFlag)) {
        if (parentId == 0 && seqId == 0) {
          parentId = 0;
          seqId = 0;
        } else {
          map.put("SORT_PARENT", parentId);
          list = fileSortLogic.getFileSorts(dbConn, map);
          fileSort = fileSortLogic.getFileSortInfoById(dbConn, parentIdStr);
          if (fileSort != null) {
            inIt = 1;
            sortPar = fileSort.getSeqId();
            if (fileSort.getSortParent() == 0) {
              parentId = 0;
              seqId = 0;
            } else {
              parentId = fileSort.getSortParent();
              seqId = fileSort.getSeqId();
            }
          } else {
            seqId = 0;
            inIt = 1;
          }
        }
      }
      //System.out.println(seqId == 0 && sortPar == 0);

      if (seqId == 0 && sortPar == 0) {
        
//        map.put("SORT_PARENT", parentId);
//        list = fileSortLogic.getFileSorts(dbConn, map);
        
        String[] condition = { " SORT_PARENT=" + parentId + " AND (SORT_TYPE !='4' or SORT_TYPE is null)  order by SORT_NO,SORT_NAME" };
        list = fileSortLogic.getFileFilderInfo(dbConn, condition);
        
      } else if (seqId != 0) {
        map.put("SORT_PARENT", seqId);
        list = fileSortLogic.getFileSorts(dbConn, map);
        fileSort = fileSortLogic.getFileSortInfoById(dbConn, String.valueOf(seqId));
        parentId = fileSort.getSortParent();
        seqId = fileSort.getSeqId();
      }

      if (list.size() != 0) {
        for (yh.core.funcs.system.filefolder.data.YHFileSort sort : list) {
          map.put("SEQ_ID", sort.getSeqId());
          yh.core.funcs.system.filefolder.data.YHFileSort fileSort2 = fileSortLogic.getFileSortInfoById(dbConn, map);

          String userPrivs = fileSortLogic.selectManagerIds(dbConn, fileSort2, "USER_ID");
          String rolePrivs = fileSortLogic.getRoleIds(dbConn, fileSort2, "USER_ID");
          String deptPrivs = fileSortLogic.getDeptIds(dbConn, fileSort2, "USER_ID");

          String newUserPrivs = fileSortLogic.selectManagerIds(dbConn, fileSort2, "NEW_USER");
          String newRolePrivs = fileSortLogic.getRoleIds(dbConn, fileSort2, "NEW_USER");
          String newDeptPrivs = fileSortLogic.getDeptIds(dbConn, fileSort2, "NEW_USER");

          userFlag = fileSortLogic.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
          roleFlag = fileSortLogic.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
          deptFlag = fileSortLogic.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);

          newUserFlag = fileSortLogic.getUserIdStr(loginUserSeqId, newUserPrivs, dbConn);
          newRoleFlag = fileSortLogic.getRoleIdStr(loginUserRoleId, newRolePrivs, dbConn);
          newDeptFlag = fileSortLogic.getDeptIdStr(loginUserDeptId, newDeptPrivs, dbConn);

          int visitFlag = 0;
          int newFlag = 0;
          if (userFlag || roleFlag || deptFlag) {
            visitFlag = 1;
          }
          if (newUserFlag || newRoleFlag || newDeptFlag) {
            newFlag = 1;
          }
          if (visitFlag==1 ) {
            
            
            Map<String, String> sortMap = new HashMap<String, String>();
            sortMap.put("seqId", String.valueOf(sort.getSeqId()));
            sortMap.put("sortName", sort.getSortName());
            sortMap.put("sortParent", String.valueOf(sort.getSortParent()));
            sortMap.put("visitFlag", String.valueOf(visitFlag));
            sortMap.put("newFlag", String.valueOf(newFlag));
            returnList.add(sortMap);
          }

        }

      }
      
      
      request.setAttribute("attachId", attachId);
      request.setAttribute("attachName", attachName);
      request.setAttribute("module", module);
      
      request.setAttribute("seqId", seqId);
      request.setAttribute("parentId", parentId);
      request.setAttribute("inIt", inIt);

      request.setAttribute("fileSortList", returnList);
      
      
      // seqId=sortPar;
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/subsys/inforesource/savefile/folder1.jsp";
  }


  /**
   * 获取转存到网络硬盘的根目录信息(copy自yh.core.funcs.system.netdisk.act.YHNetdiskAct)<br>
   * @param request
   * @param response
   * @return "/subsys/inforesource/savefile/netdisk.jsp"
   * @throws Exception
   */
  public String getNetDiskList(HttpServletRequest request, HttpServletResponse response) throws Exception {

    String seqIdStr = request.getParameter("seqId");
    String diskPath = request.getParameter("diskPath");
    String parentPath = request.getParameter("parentPath");
    String attachId = request.getParameter("attachId");
    String attachName = request.getParameter("attachName");
    String returnFlag = request.getParameter("returnFlag");
    String module = request.getParameter("module");

    int seqId = 0;
    if (seqIdStr != null && !"".equals(seqIdStr)) {
      seqId = Integer.parseInt(seqIdStr);
    }

    if (diskPath == null) {
      diskPath = "";
    }
    if (parentPath == null) {
      parentPath = "";
    }
    if (returnFlag == null) {
      returnFlag = "";
    }
    if (module == null) {
      module = "";
    }

    // 获取登录用户信息
    YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
    int loginUserSeqId = loginUser.getSeqId();
    int loginUserDeptId = loginUser.getDeptId();
    String loginUserRoleId = loginUser.getUserPriv();

    boolean visitUserFlag = false;
    boolean visitRoleFlag = false;
    boolean visitDeptFlag = false;

    boolean newUserFlag = false;
    boolean newRoleFlag = false;
    boolean newDeptFlag = false;

    YHNetdiskLogic logic = new YHNetdiskLogic();
    List list = new ArrayList();
    List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      if ("back".equals(returnFlag.trim())) {
        YHNetdisk netdisk = logic.getNetdiskInfoById(dbConn, seqId);
        String filePath = YHUtility.null2Empty(netdisk.getDiskPath());
        File file = new File(filePath);
        File file2 = new File(diskPath);
        if (file.exists() && file2.exists()) {
          if (file.getAbsolutePath().equals(file2.getAbsolutePath())) {
            diskPath = "";
          } else {
            String parentPathStr = diskPath.replace('\\', '/');
            int pathStr = parentPathStr.lastIndexOf('/') - 1;
            if (pathStr != -1) {
              diskPath = parentPathStr.substring(0, parentPathStr.lastIndexOf('/'));
              diskPath = diskPath + "/";
            }
          }
        }
      }

      if ("".equals(diskPath)) {
        list = logic.getNetdiskFolderInfo(dbConn);
        if (list != null && list.size() != 0) {
          for (int i = 0; i < list.size(); i++) {
            YHNetdisk netdisk = (YHNetdisk) list.get(i);
            Map diskMap = new HashMap();
            diskMap.put("SEQ_ID", netdisk.getSeqId());

            String visitUserStr = logic.selectManagerIds(dbConn, diskMap, "USER_ID");
            String visitRoleStr = logic.getRoleIds(dbConn, diskMap, "USER_ID");
            String visitDeptStr = logic.getDeptIds(dbConn, diskMap, "USER_ID");

            String newUserStr = logic.selectManagerIds(dbConn, diskMap, "NEW_USER");
            String newRoleStr = logic.getRoleIds(dbConn, diskMap, "NEW_USER");
            String newDeptStr = logic.getDeptIds(dbConn, diskMap, "NEW_USER");

            visitUserFlag = logic.getUserIdStr(loginUserSeqId, visitUserStr, dbConn);
            visitRoleFlag = logic.getRoleIdStr(loginUserRoleId, visitRoleStr, dbConn);
            visitDeptFlag = logic.getDeptIdStr(loginUserDeptId, visitDeptStr, dbConn);

            newUserFlag = logic.getUserIdStr(loginUserSeqId, newUserStr, dbConn);
            newRoleFlag = logic.getRoleIdStr(loginUserRoleId, newRoleStr, dbConn);
            newDeptFlag = logic.getDeptIdStr(loginUserDeptId, newDeptStr, dbConn);

            int visitFlag = 0;
            int newFlag = 0;
            if (visitUserFlag || visitRoleFlag || visitDeptFlag) {
              visitFlag = 1;
            }
            if (newUserFlag || newRoleFlag || newDeptFlag) {
              newFlag = 1;
            }

            if (visitFlag == 1 && newFlag == 1) {
              Map<String, String> map = new HashMap<String, String>();
              map.put("seqId", String.valueOf(netdisk.getSeqId()));
              map.put("diskName", netdisk.getDiskName());
              map.put("diskPath", YHUtility.encodeSpecial(netdisk.getDiskPath()));
              map.put("newUser", netdisk.getNewUser());
              map.put("managerUser", netdisk.getManageUser());
              map.put("userId", netdisk.getUserId());
              map.put("diskNo", String.valueOf(netdisk.getDiskNo()));
              map.put("spaceLimit", String.valueOf(netdisk.getSpaceLimit()));
              map.put("orderBy", netdisk.getOrderBy());
              map.put("ascDesc", netdisk.getAscDesc());
              map.put("downUser", netdisk.getDownUser());
              returnList.add(map);

            }

          }
        }
      } else {
        File file = new File(diskPath);
        if (file.exists()) {
          parentPath = file.getAbsolutePath().replace('\\', '/');
          File[] files = file.listFiles();
          for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory()) {
              Map<String, String> map = new HashMap<String, String>();
              map.put("seqId", String.valueOf(seqId));
              map.put("diskName", f.getName());
              map.put("diskPath", f.getAbsolutePath().replace('\\', '/'));
              returnList.add(map);
            }

          }
        }
      }

      request.setAttribute("attachId", attachId);
      request.setAttribute("attachName", attachName);
      request.setAttribute("module", module);

      request.setAttribute("seqId", seqId);
      request.setAttribute("diskPath", diskPath);
      request.setAttribute("parentPath", parentPath);

      request.setAttribute("diskList", returnList);

    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }

    return "/subsys/inforesource/savefile/netdisk.jsp";
  }

  /**
   * 网络硬盘转存<br>
   * 调用YHBeachDownLoadFileLogic的transferNetdisk进行转存
   * @see yh.subsys.inforesouce.logic.YHBeachDownLoadFileLogic#transferNetdisk(Connection, String, YHPerson, int)
   * @param request
   * @param response
   * @return "/core/inc/rtjson.jsp"
   * @throws Exception
   */
  public String transferNetdisk(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String diskPath = request.getParameter("diskPath"); // d:/bjfaoitc/
  
    if (diskPath == null) {
      diskPath = "";
    }
  
    YHBeachDownLoadFileLogic logic = new YHBeachDownLoadFileLogic();
    Connection dbConn = null;
    try {
      YHRequestDbConn requesttDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requesttDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = logic.transferNetdisk(dbConn, diskPath, loginUser,1);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件转存完毕！");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }

    return "/core/inc/rtjson.jsp";
  }

}

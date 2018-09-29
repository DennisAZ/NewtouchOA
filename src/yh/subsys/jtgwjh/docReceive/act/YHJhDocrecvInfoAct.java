package yh.subsys.jtgwjh.docReceive.act;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import yh.core.data.YHRequestDbConn;
import yh.core.esb.client.data.YHEsbConst;
import yh.core.esb.client.data.YHEsbMessage;
import yh.core.esb.client.logic.YHDeptTreeLogic;
import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.filefolder.data.YHFileSort;
import yh.core.funcs.system.filefolder.logic.YHFileSortLogic;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.jtgwjh.docReceive.data.YHJhDocrecvInfo;
import yh.subsys.jtgwjh.docReceive.logic.YHJhDocrecvInfoLogic;
import yh.subsys.jtgwjh.docReceive.logic.YHUnZipRarFile;


public class YHJhDocrecvInfoAct {
  YHJhDocrecvInfoLogic logic = new YHJhDocrecvInfoLogic();

  /**
   * 新建
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addUpdateGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHJhDocrecvInfo group = (YHJhDocrecvInfo) YHFOM.build(request.getParameterMap());
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      String seqId = request.getParameter("seqId");
      if (YHUtility.isInteger(seqId)) {
        logic.updateNation(dbConn, group);
        
        //安全日志
        //YHSecLogUtil.log(dbConn, user, request.getRemoteAddr(), "310",group,"1", "修改公文数据");
    
        //系统日志
        YHSysLogLogic.addSysLog(dbConn, "62", "修改公文数据：" + group.toString() ,user.getSeqId(), request.getRemoteAddr());
       
      } else {
        group.setReceiveUser(user.getSeqId() + "");
        group.setReceiveUserName(user.getUserName());
        group.setReceiveDatetime(new Date());
        group.setSendDatetime(new Date());
        int seqIdInt = logic.add(dbConn, group);
      }
      String data = "{}";
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 显示管理分页列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryGroupManage(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      //String groupNo = request.getParameter("groupNo");
      String data = logic.toSearchData(dbConn, request.getParameterMap(),request);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  /**
   *更新 状态
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String   updateStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      String seqIds = request.getParameter("seqId") ;
      String status = request.getParameter("status") == null ? "" :request.getParameter("status");
      String returnReason = request.getParameter("returnReason") == null ? "" :  request.getParameter("returnReason") ;
      if(!YHUtility.isNullorEmpty(seqIds)){
        String[] seqIdArray = seqIds.split(",");
        if(status.equals("4")){//退回先不处理
          //logic.updateStatusReturn(dbConn, seqIds, returnReason);
        }else{
          String desc = "成功签收公文";
          if(status.equals("2")){
            desc = "打印公文";
          }else if(status.equals("3")){
            desc = "转发公文";
          }
          for (int i = 0; i < seqIdArray.length; i++) {
            if(YHUtility.isInteger(seqIdArray[i])){
              YHJhDocrecvInfo recv = logic.getById(dbConn, seqIdArray[i]);
              recv.setStatus(status);
              if(status.equals("1")){//签收
                recv.setReceiveDatetime(new Date());
              }
              logic.updateNation(dbConn, recv);
              
              if(status.equals("1")){//签收返回状态
                //返回接收状态,发送任务
                 YHJhDocrecvInfoLogic.createXML(recv,request.getRealPath("/"),dbConn);
             
              }
              
         
              //安全日志
             // YHSecLogUtil.log(dbConn, user, request.getRemoteAddr(), "310",recv,"1", desc);
            
              //系统日志
              YHSysLogLogic.addSysLog(dbConn, "62", desc + ":" + recv.toString() ,user, request.getRemoteAddr());
           
            }
          }
         
        }
 
      }
      request.setAttribute(YHActionKeys.RET_DATA, "{}");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   *更新办理状态
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String   updateHandStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      String seqIds = request.getParameter("seqIds") ;
      String handstatus = request.getParameter("handstatus") == null ? "" :request.getParameter("handstatus");
       if(!YHUtility.isNullorEmpty(seqIds)){
        String[] seqIdArray = seqIds.split(",");
        String desc = "归档成功";
        if(handstatus.equals("1")){//逻辑删除
          
        }else if(handstatus.equals("2")){
          desc = "转发成功" ;
        }
        for (int i = 0; i < seqIdArray.length; i++) {
          YHJhDocrecvInfo recv = logic.getById(dbConn, seqIdArray[i]);
          YHJhDocrecvInfoLogic.updateHandStatus(dbConn, handstatus, seqIdArray[i]);
           //系统日志
          YHSysLogLogic.addSysLog(dbConn, "62", desc + ":" + recv.toString() ,user, request.getRemoteAddr());
        } 
   
      }
      request.setAttribute(YHActionKeys.RET_DATA, "{}");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   *删除BY Ids
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String   deleteByseqIds(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIds = request.getParameter("seqId") ;
      if(!YHUtility.isNullorEmpty(seqIds)){
        //String[] seqIdArray = seqIds.split(",");
        logic.delete(dbConn, seqIds);
      }
      request.setAttribute(YHActionKeys.RET_DATA, "{}");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 获取对象BYId
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectById(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      int port = request.getLocalPort();
      String serviceName = request.getServerName();
      String data = "";
      if(YHUtility.isInteger(seqId)){
        
        String filePath = YHSysProps.getAttachPath() + "\\docReceive\\" ;
        YHJhDocrecvInfo group  = logic.getById(dbConn, seqId);
        data = YHFOM.toJson(group).toString().substring(0, YHFOM.toJson(group).toString().length() -1) 
            + ",port:" +port + "," 
            + "serviceName:\"" +YHUtility.encodeSpecial(serviceName) + "\","
            + "filePath:\"" +YHUtility.encodeSpecial(filePath) + "\"}";
      }
      if(YHUtility.isNullorEmpty(data)){
        data = "{}";
      }
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 查询
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String select(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String status = request.getParameter("status") == null ? "0" : request.getParameter("status") ;
      String[] str  = {"STATUS = '" + status + "'"};

      List<YHJhDocrecvInfo> list = logic.select(dbConn, str);
      String data = "[";
      for (int i = 0; i < list.size(); i++) {
        data = data + YHFOM.toJson(list.get(i)) + ",";
      }
      if(list.size() > 0){
        data = data.substring(0, data.length()-1);
      }
      data = data +  "]";
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询lIst
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectByIds(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIds = request.getParameter("seqIds");
      String data = "[";
      if(!YHUtility.isNullorEmpty(seqIds)){
        if(seqIds.endsWith(",")){
          seqIds = seqIds.substring(0, seqIds.length() -1);
        }
        String[] str = {"SEQ_ID in(" + seqIds + ")"};
        List<YHJhDocrecvInfo> list  = logic.select(dbConn, str);
        for (int i = 0; i < list.size(); i++) {
          YHJhDocrecvInfo receive = list.get(i);
          String mainDoc =  receive.getMainDocId() == null ? "" : receive.getMainDocId();
          String manDocName = receive.getMainDocName() == null ? "" : receive.getMainDocName();
          String attachId = receive.getAttachmentId();
          String attachName = receive.getAttachmentName();
          if(!YHUtility.isNullorEmpty(mainDoc) && !YHUtility.isNullorEmpty(manDocName) ){
            data = data + "{seqId:" + receive.getSeqId() + ",attachId:\"" + YHUtility.encodeSpecial(mainDoc) + "\","
                + "attachName:\"" + YHUtility.encodeSpecial(manDocName) + "\",isMain:1},";
          }
          if(!YHUtility.isNullorEmpty(attachId) && !YHUtility.isNullorEmpty(attachName) ){
            String[] attachIdArr = attachId.split(",");
            String[] attachNameArr = attachName.split("\\*");
            if(attachIdArr.length == attachNameArr.length){
              for (int j = 0; j < attachNameArr.length; j++) {
                if(!YHUtility.isNullorEmpty(attachNameArr[j]) && !YHUtility.isNullorEmpty(attachIdArr[j]) 
                    && !mainDoc.equals(attachIdArr[j])){//普通附件，同时去掉正文
                  data = data + "{attachId:\"" + YHUtility.encodeSpecial(attachIdArr[j]) + "\","
                      + "attachName:\"" + YHUtility.encodeSpecial(attachNameArr[j]) + "\",isMain:0},";
                }
                
              }
            }
          }
        }
      }
      if(!data.equals("[")){
        data = data.substring(0, data.length()-1);
      }

      data = data + "]";
      String rand = YHDiaryUtil.getRondom();//pdf、word存放路径，打包下载
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, rand);
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 查询lIst
   * 脱密下载，检查带正文的
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectByIdsMian(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIds = request.getParameter("seqIds");
      String data = "[";
      if(!YHUtility.isNullorEmpty(seqIds)){
        if(seqIds.endsWith(",")){
          seqIds = seqIds.substring(0, seqIds.length() -1);
        }
        String[] str = {"SEQ_ID in(" + seqIds + ")","MAIN_DOC_ID <> ''"  };
        List<YHJhDocrecvInfo> list  = logic.select(dbConn, str);
        for (int i = 0; i < list.size(); i++) {
          YHJhDocrecvInfo receive = list.get(i);
          String mainDoc =  receive.getMainDocId();
          String manDocName = receive.getMainDocName();
          if(!YHUtility.isNullorEmpty(mainDoc) && !YHUtility.isNullorEmpty(manDocName) ){
            data = data + "{seqId:" + receive.getSeqId() + ",mainDocId:\"" + YHUtility.encodeSpecial(mainDoc) + "\","
                + "mainDocName:\"" + YHUtility.encodeSpecial(manDocName) + "\"},";
          }

        }
      }
      if(!data.equals("[")){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      String rand = YHDiaryUtil.getRondom();//pdf、word存放路径，打包下载
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, rand);
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 单文件附件上传

   * 
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String uploadFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    Map<String, String> attr = null;
    String attrId = (fileForm.getParameter("attachmentId") == null) ? ""
        : fileForm.getParameter("attachmentId");
    
    System.out.println( fileForm.getParameter("moduel"));
    String attrName = (fileForm.getParameter("attachmentName") == null) ? ""
        : fileForm.getParameter("attachmentName");
    String data = "";
    try {

      attr = logic.fileUploadLogic(fileForm);
      Set<String> keys = attr.keySet();
      for (String key : keys) {
        String value = attr.get(key);
        if (attrId != null && !"".equals(attrId)) {
          if (!(attrId.trim()).endsWith(",")) {
            attrId += ",";
          }
          if (!(attrName.trim()).endsWith("*")) {
            attrName += "*";
          }
        }
        attrId += key + ",";
        attrName += value + "*";
      }
      data = "{attrId:\"" + attrId + "\",attrName:\"" + attrName + "\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传成功");
      request.setAttribute(YHActionKeys.RET_DATA, data);

    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传失败");
      throw e;
    }
    return "/core/inc/rtuploadfile.jsp";
  }
  
  /**
   * 获取转存到公共文件柜的根目录信息
   *  支持多个附件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getFolderInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqIdStr = request.getParameter("seqId");
    String docReceiveSeqIds = request.getParameter("docReceiveSeqIds");//需要转存的公文seqId字符串
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
    YHFileSort fileSort = new YHFileSort();
    List<YHFileSort> list = new ArrayList<YHFileSort>();

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
        for (YHFileSort sort : list) {
          map.put("SEQ_ID", sort.getSeqId());
          YHFileSort fileSort2 = fileSortLogic.getFileSortInfoById(dbConn, map);

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
      request.setAttribute("docReceiveSeqIds", docReceiveSeqIds);
      request.setAttribute("fileSortList", returnList);
      
      
      // seqId=sortPar;
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/subsys/jtgwjh/receiveDoc/saveFile/folder1.jsp";
  }
  
  public String recvMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
    
    String filePath="D:\\cache\\d73f1534-2bc4-4f1a-b007-9a0d0e6cb02c\\00000_4112b02dd6e86f92c8502865b4b11c1a.zip";
    String guid=""; 
    String fromUnit = "client";
    Connection dbConn = null;
    File file = new File(filePath);
    String fileName = file.getName();
    if (fileName.endsWith("xml")) {
      StringBuffer sb = YHFileUtility.loadLine2Buff(filePath);
      YHEsbMessage message = YHEsbMessage.xmlToObj(sb.toString());
      if (YHEsbConst.SYS_DEPT.equals(message.getMessage())) {
        YHDeptTreeLogic logic = new YHDeptTreeLogic();
        logic.updateDept(message.getData());
        return null;
      }
    }
    if (fileName.endsWith("zip")) {
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();

        YHUnZipRarFile.unZipFileXml(filePath,fromUnit,"152747cd-78f3-4e51-8140-05b362370f31");
      }catch(Exception ex){
        ex.printStackTrace();
      }
    }
    
    return "RECVOK" + guid;
  }

  /**
   * 新建安全日志
   * ----脱密、转存
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addSelLog(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      String seqId = request.getParameter("seqId");
      String status = request.getParameter("status") == null ? "" : request.getParameter("status");
      if (YHUtility.isInteger(seqId)) {
        String desc = "下载";
        if(status.equals("2")){
          desc = "脱密下载正文";
        }else if(status.equals("3")){
          desc = "将附件脱密并转存到公共文件柜";
       }
        YHJhDocrecvInfo recv = logic.getById(dbConn, seqId);
        //logic.updateNation(dbConn, group);
        
        //安全日志
        //YHSecLogUtil.log(dbConn, user, request.getRemoteAddr(), "310",recv,"1", desc);
        

        //系统日志
        YHSysLogLogic.addSysLog(dbConn, "62", desc + ":" + recv.toString() ,user.getSeqId(), request.getRemoteAddr());
     
      }
      String data = "{}";
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  

  /**
   * 新建安全日志--打印
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addSelLogPrint(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      String seqId = request.getParameter("seqId");
      String printCount =  request.getParameter("printCount");
      String startNo = request.getParameter("startNo");
      String endNo =  request.getParameter("endNo");
      if (YHUtility.isInteger(seqId)) {
        YHJhDocrecvInfo recv = logic.getById(dbConn, seqId);
        //logic.updateNation(dbConn, group);
        
        //安全日志
       // YHSecLogUtil.log(dbConn, user, request.getRemoteAddr(), "310",recv,"1", "打印公文，打印 " + printCount + " 份数,编号为：" +  startNo + "-" + endNo );

        //系统日志
        YHSysLogLogic.addSysLog(dbConn, "62", "打印公文，打印 " + printCount + " 份数,编号为：" +  startNo + "-" + endNo  + recv.toString() ,user.getSeqId(), request.getRemoteAddr());
     
      }
      String data = "{}";
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 更改正文正在打开状态,sessionToken
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String updateDocMainOpen(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //获取sessionToken
      String openState = "0";
      String seqId = request.getParameter("seqId") == null ? "" : request.getParameter("seqId") ;
      String sessionToken = (String) request.getSession().getAttribute("sessionToken") == null ? "" : (String) request.getSession().getAttribute("sessionToken");
      if(YHUtility.isInteger(seqId)){
        YHJhDocrecvInfo modul  = logic.getById(dbConn, seqId);
        if(modul != null){
          if(YHUtility.isNullorEmpty(modul.getSessiontoken())){//假如为空，直接更新数据
            logic.updateById(dbConn, sessionToken, seqId);
          }else{
            if(logic.getSessionToken(dbConn, modul.getSessiontoken()) ){//sessiontoken没过期
              if(!modul.getSessiontoken().equals(sessionToken)){
                openState = "1";//已经被打开状态
              }
                
            }else{//已经过期了
              logic.updateById(dbConn, sessionToken, seqId);
            }
          }
        }
      }
      request.setAttribute(YHActionKeys.RET_DATA, "{openType:\"" + openState + "\"}");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "加载成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 清空sessionToken
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String clearSessionToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //获取sessionToken
      String openState = "0";
      String seqId = request.getParameter("seqId") == null ? "" : request.getParameter("seqId") ;
      String sessionToken = (String) request.getSession().getAttribute("sessionToken") == null ? "" : (String) request.getSession().getAttribute("sessionToken");
      if(YHUtility.isInteger(seqId)){
        YHJhDocrecvInfo modul  = logic.getById(dbConn, seqId);
        if(modul != null){
          if(!YHUtility.isNullorEmpty(modul.getSessiontoken())){//不为空，直接更新数据
            logic.updateById(dbConn, "", seqId);
            //if(logic.getSessionToken(dbConn, modul.getSessiontoken())){//sessiontoken没过期
             // if( modul.getSessiontoken().equals(sessionToken)){
               
             // }
            //}
          }
        }
      }
      request.setAttribute(YHActionKeys.RET_DATA, "{openType:\"" + openState + "\"}");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "加载成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 已发发文列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getReceiving(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHJhDocrecvInfoLogic logic = new YHJhDocrecvInfoLogic();
      String data = logic.getReceiving(dbConn, request.getParameterMap(), person, request);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
}

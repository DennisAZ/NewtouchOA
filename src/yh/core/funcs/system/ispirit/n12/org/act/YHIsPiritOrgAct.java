package yh.core.funcs.system.ispirit.n12.org.act;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.tree.DefaultMutableTreeNode;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.ispirit.n12.org.logic.YHIsPiritLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHLogConst;
import yh.core.global.YHSysProps;
import yh.core.module.org_select.logic.YHOrgSelectLogic;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHDigestUtility;
import yh.core.util.db.YHORM;

public class YHIsPiritOrgAct {
  
  /***
   *组织机构  org.xml
   *
   */
  public String getImRequest(HttpServletRequest request, HttpServletResponse response)throws Exception{
    String returnStr="";
    String cmd5=request.getParameter("imMd5");
    if(!this.getCheckMd5Act(cmd5)){  //如校验错误下载最新值
      
        this.getOrgDataStreamZipAct(request, response);
      
    }else{
      response.setCharacterEncoding("UTF-8");
      response.setContentType("text/html;charset=UTF-8");
      response.setHeader("Cache-Control","private");
      //response.setHeader("Accept-Ranges","bytes");
      PrintWriter out = response.getWriter();
      returnStr="+OK";
      out.print(returnStr);
      out.flush();

    }
   return null;
  }
  
  /**
   * 提供精灵组织机构org文件流
   */
  public static void getOrgDataStream(Connection dbConn) throws Exception{
    try{
      String webPath  =YHSysProps.getRootPath();
      YHIsPiritLogic IPLogic = new YHIsPiritLogic();
      IPLogic.getOrgXmlData(dbConn,webPath);
    
    }catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  /***
   * 
   * 获取组织机构数据
   */
  public String getModuleData(HttpServletRequest request, HttpServletResponse response)throws Exception{
    Connection dbConn=null;
    try{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    dbConn = requestDbConn.getSysDbConn();
    YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
    String data="";
    String RECENT_USER=request.getParameter("RECENT_USER");
    String module=request.getParameter("MODULE");
    if("recent".equals(module)){
      YHIsPiritLogic logic =new YHIsPiritLogic(); 
      data=logic.getRecentUserById(dbConn, RECENT_USER);
      
    }else if("im_group".equals(module)){
      YHIsPiritLogic logic =new YHIsPiritLogic(); 
      data=logic.getimGroupUserById(dbConn, person.getSeqId()+"");
      
    }
    data="{data:'"+YHUtility.encodeSpecial(data)+"'}";
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
    request.setAttribute(YHActionKeys.RET_DATA, data);
  } catch (Exception ex) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
    throw ex;
   }
     return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   *返回压缩流 
   **/
  public void getOrgDataStreamZipAct(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String webPath  =YHSysProps.getRootPath();
    File file =new File(webPath+File.separator+"org.zip");
    FileInputStream fio = new FileInputStream(file);
    InputStream is = fio;
    OutputStream ops = null;
    ops = response.getOutputStream();
    response.setCharacterEncoding("UTF-8");
    
    
    response.setContentType("application/zip");
    response.setHeader("Cache-Control","private");
    response.setHeader("Accept-Ranges","bytes");
    response.setHeader("Content-disposition","attachment; filename=\"org.zip\"");
    if(is != null){
      byte[] buff = new byte[8192];
      int byteread = 0;
      while( (byteread = is.read(buff)) != -1){
        ops.write(buff,0,byteread);
        ops.flush();
      }
    }
  }
  /**
   *判断MD5值
   */
  public boolean getCheckMd5Act(String cMd5) throws Exception{
    String webPath  =YHSysProps.getRootPath();
    String filePath=webPath+File.separator+"org.zip";
    boolean isValid=false;
    isValid=YHDigestUtility.isFileMatch(filePath, cMd5);
    return isValid;
  }
  
  public void getUserMarkStreamAct(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String KEY=request.getParameter("KEY");
    String seqId =request.getParameter("UID");
     try{
     if("Picture".equals(KEY)&& !YHUtility.isNullorEmpty(seqId) && !"0".equals(seqId)){
        this.getUserPictureStreamAct(request, response);
      }else if("Avatar".equals(KEY)&&!YHUtility.isNullorEmpty(seqId) && !"0".equals(seqId)){
        this.getUserAvatarStreamAct(request, response);
      }
     }catch(Exception e){
       e.printStackTrace();
     }
    
  }

  
  /**
   *用户头像流
   *@param 
   */
  public void getUserAvatarStreamAct(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    OutputStream ops = null;
    InputStream is = null;
    
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("UID");
      String avatMD5=request.getParameter("picMD5");
      
      YHIsPiritLogic IPLogic = new YHIsPiritLogic();
      String webPath =YHSysProps.getWebPath();
      String AvatarPath =IPLogic.getgetUserAvatorPath(dbConn, seqId, webPath);
      File file =new File(AvatarPath);
      if(!file.exists()){                            //文件不存在
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-Control","private");
        //response.setHeader("Accept-Ranges","bytes");
        PrintWriter out = response.getWriter();
        String returnStr="default";
        out.print(returnStr);
        out.flush();
      }else{
        if(IPLogic.getCheckMd5Act(avatMD5, AvatarPath)){  //文件没更新
          response.setCharacterEncoding("UTF-8");
          response.setContentType("text/html;charset=UTF-8");
          response.setHeader("Cache-Control","private");
          //response.setHeader("Accept-Ranges","bytes");
          PrintWriter out = response.getWriter();
          String returnStr="+OK";
          out.print(returnStr);
          out.flush();
        }else{                                              //文件更新
          
         String imageType =  AvatarPath.substring(AvatarPath.indexOf(".")+1, AvatarPath.length());
         String imageName =  AvatarPath.substring(AvatarPath.lastIndexOf(File.separator)+1, AvatarPath.length());
         String contentType="image/"+imageType;
          is=IPLogic.getUserAvatorStream(dbConn, seqId,webPath);
          ops = response.getOutputStream();
          response.setCharacterEncoding("UTF-8");
          response.setContentType(""+contentType+"");
          response.setHeader("Cache-Control","private");
          response.setHeader("Accept-Ranges","bytes");
          response.setHeader("Content-disposition","filename=\""+imageName+"\"");
          if(is != null){
            byte[] buff = new byte[8192];
            int byteread = 0;
            while( (byteread = is.read(buff)) != -1){
              ops.write(buff,0,byteread);
              ops.flush();
            }
          }
        } 
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
      if (is != null) {
        is.close();
      }
    }
  }
  /**
   *用户头像流
   *@param 
   */
  public void getUserPictureStreamAct(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    OutputStream ops = null;
    InputStream is = null;
    
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      String seqId = request.getParameter("UID");
      String avatMD5=request.getParameter("picMD5");
      
      YHIsPiritLogic IPLogic = new YHIsPiritLogic();
      String webPath =YHSysProps.getWebPath();
      String picPath =IPLogic.getgetUserPicPath(dbConn, seqId, webPath);
      File file =new File(picPath);
      if(!file.exists()){                            //文件不存在
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-Control","private");
        //response.setHeader("Accept-Ranges","bytes");
        PrintWriter out = response.getWriter();
        String returnStr="default";
        out.print(returnStr);
        out.flush();
      }else{
        if(IPLogic.getCheckMd5Act(avatMD5, picPath)){  //文件没更新
          response.setCharacterEncoding("UTF-8");
          response.setContentType("text/html;charset=UTF-8");
          response.setHeader("Cache-Control","private");
          //response.setHeader("Accept-Ranges","bytes");
          PrintWriter out = response.getWriter();
          String returnStr="+OK";
          out.print(returnStr);
          out.flush();
        }else{                                              //文件更新
          
         String imageType =  picPath.substring(picPath.indexOf(".")+1, picPath.length());
         String imageName =  picPath.substring(picPath.lastIndexOf(File.separator)+1, picPath.length());
         String contentType="image/"+imageType;
          is=IPLogic.getUserPicStream(dbConn, seqId,webPath);
          ops = response.getOutputStream();
          response.setCharacterEncoding("UTF-8");
          response.setContentType(contentType);
          response.setHeader("Cache-Control","private");
          response.setHeader("Accept-Ranges","bytes");
          response.setHeader("Content-disposition","filename=\""+imageName+"\"");
          if(is != null){
            byte[] buff = new byte[8192];
            int byteread = 0;
            while( (byteread = is.read(buff)) != -1){
              ops.write(buff,0,byteread);
              ops.flush();
            }
          }
        } 
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
      if (is != null) {
        is.close();
      }
    }
  }
  
  /**
   * 在线人员显示方式
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getOnLineTree(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHIsPiritLogic IPLogic = new YHIsPiritLogic();
      DefaultMutableTreeNode root = IPLogic.buildDeptTree(dbConn);
      //获取登陆人员
      List<YHPerson> onlineUsers = IPLogic.getOnlineUsers(dbConn);
      
      //反向排序(因为人员插入到树的左侧)
      this.sortUserListByRole(dbConn, onlineUsers, -1);
      this.buildOnlineTree(root, onlineUsers);
      
      StringBuffer sb = breadthFirst(dbConn, root);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   *userinfo 
   **/
  public void getUserInfoAct(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String uid=request.getParameter("UID");
      String xmlMD5=request.getParameter("xmlMD5");
      YHIsPiritLogic IPLogic = new YHIsPiritLogic();
      String xmlStr=IPLogic.getUserInfo(dbConn, uid);
      String xmlHeader="<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
      xmlStr="<org>"+xmlStr+"</org>";
      xmlStr=xmlHeader+xmlStr;
      //校验MD5
      String xmlfileMD5=YHDigestUtility.md5Hex(xmlStr.getBytes());
      xmlMD5=YHUtility.null2Empty(xmlMD5);
     // System.out.print(xmlMD5+"--"+xmlfileMD5);
      
      if(false){  //返回+OK
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-Control","private");
        //response.setHeader("Accept-Ranges","bytes");
        PrintWriter out = response.getWriter();
        String returnStr="+OK";
        out.print(returnStr);
        out.flush();
      }else{  //返回文件流
        PrintWriter out = response.getWriter();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/xml");
        response.setHeader("Cache-Control","no-cache");
       // System.out.println(data);
        out.print(xmlStr);
        out.flush();
        out.close();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } 
  }
  /***
   * 
   */
  public String version(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String FILE_NAME = request.getParameter("FILE_NAME");
    String fileName=FILE_NAME;
    
    if(FILE_NAME == "" || FILE_NAME.indexOf("..")!=-1 || FILE_NAME.indexOf("/")!=-1 || FILE_NAME.indexOf("\\")!=-1)
      return null;
    String webPath = YHSysProps.getRootPath();
    FILE_NAME = webPath+File.separator+"IM"+File.separator+"update"+File.separator+FILE_NAME;
    File file = new File(FILE_NAME);
    if(!file.exists())
    {
     
      response.sendRedirect("HTTP/1.1 404 Not Found");

       return null;
    }
    
   
    FileInputStream fio = new FileInputStream(file);
    InputStream is = fio;
    OutputStream ops = null;
    ops = response.getOutputStream();
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/ini");
    response.setHeader("Cache-Control","private");
    response.setHeader("Accept-Ranges","bytes");
    response.setHeader("Content-disposition","attachment; filename=\""+fileName+"\"");
    if(is != null){
      byte[] buff = new byte[8192];
      int byteread = 0;
      while( (byteread = is.read(buff)) != -1){
        ops.write(buff,0,byteread);
        ops.flush();
      }
    }
   
   return null;
  }
    
  
  
  /***
   * 提供精灵提醒
   * 
   */
  public void getRemindDataAct(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      
    }catch(Exception e){
      e.printStackTrace();
    }
    }
  
  
  /**
   * 构造在线用户树
   * @param root
   * @param list
   */
  private void buildOnlineTree(DefaultMutableTreeNode root, List<YHPerson> list) { 
    Enumeration<DefaultMutableTreeNode> em = root.depthFirstEnumeration();
    //生成list操作是为了避免操作树的时候使枚举失效
    List<DefaultMutableTreeNode> l = new ArrayList<DefaultMutableTreeNode>(Collections.list(em));
    for (DefaultMutableTreeNode n : l) {
      addUserNode(n, list);
    }
  }
  
  /**
   * 向一个节点中添加用户,如果没有用户和下级节点则删除这个部门
   * @param node
   * @param list
   */
  private void addUserNode(DefaultMutableTreeNode node, List<YHPerson> list) {
    Object o = node.getUserObject();
    if (o instanceof YHDepartment) {
      YHDepartment d = (YHDepartment)o;
      for (YHPerson p : list) {
        if (this.inDept(p, d.getSeqId())) {
          //插入新节点到左边
          node.insert(new DefaultMutableTreeNode(p), 0);
        }
      }
      if (node.getChildCount() == 0) {
        ((DefaultMutableTreeNode)node.getParent()).remove(node);
      }
    }
  }
  /**
   *获取人员部门 
   */
  public String getDeptName(Connection dbConn, int deptId){
    String deptNameStr = "";
    try {
      YHIsPiritLogic IPLogic = new YHIsPiritLogic();
      deptNameStr = IPLogic.getDeptNameLogic(dbConn, deptId);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return deptNameStr;
    
  }
  
  public String getUserName(HttpServletRequest request,
      HttpServletResponse response){
    Connection dbConn=null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String uid = request.getParameter("uid");
      String data="";
      if(YHUtility.isNullorEmpty(uid)){
        YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
        data=person.getUserName();
      }else{
        YHORM orm = new YHORM();
        YHPerson person =(YHPerson)orm.loadObjSingle(dbConn, YHPerson.class, Integer.parseInt(uid));
        data=person.getUserName();
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, "{data:'"+data+"'}");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      e.printStackTrace();
    }
    return "/core/inc/rtjson.jsp";
    
  }
  
  public String getOrgName(HttpServletRequest request,
      HttpServletResponse response){
    Connection dbConn=null;
    Statement stmt=null;
    ResultSet rs=null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data="";
     stmt=dbConn.createStatement();
     rs=stmt.executeQuery(" select * from oa_organization ");
     if(rs.next()){
       data=rs.getString("UNIT_NAME");
     }else{
       data="办公精灵";
     }
       
  
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, "{data:'"+data+"'}");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      e.printStackTrace();
    }
    return "/core/inc/rtjson.jsp";
    
  }
  
  
  
  /**
   * 判断人员是否在该部门,考虑了辅助部门

   * @param p
   * @param deptId
   * @return
   */
  private boolean inDept(YHPerson p, int deptId) {
    if (p.getDeptId() == deptId) {
      return true;
    }
    String other = "," + p.getDeptIdOther() + ",";
    return other.contains("," + deptId + ",");
  }
  
  /**
   * 获取人员角色
   * */
  public String getRoleName(Connection dbConn, int roleId){
    YHIsPiritLogic IPLogic = new YHIsPiritLogic();
    String roleNameStr = "";
    try {
      roleNameStr = IPLogic.getRoleNameLogic(dbConn, roleId);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return roleNameStr;
    
  }
  
  public String getIpState(HttpServletRequest request,
      HttpServletResponse response, Connection dbConn) throws Exception {
    String showIpFlag = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHIsPiritLogic IPLogic = new YHIsPiritLogic();
      showIpFlag = IPLogic.getSecrityShowIp(dbConn);
      
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return showIpFlag;
  }
  
  
  /**
   * 按照角色对在线人员列表排序

   * @param dbConn
   * @param userList
   * @param sort          排序方向: sort > 0 - asc, sort < 0 - desc
   * @throws Exception
   */
  private void sortUserListByRole(final Connection dbConn, List<YHPerson> userList, final int sort) throws Exception {
    Collections.sort(userList, new Comparator<YHPerson>() {
      YHOrgSelectLogic logic = new YHOrgSelectLogic();
      public int compare(YHPerson p1, YHPerson p2) {
        try {
          int r = logic.getUserPrivNo(dbConn, p1.getUserPriv()).compareTo(logic.getUserPrivNo(dbConn, p2.getUserPriv()));
          if (r == 0) {
            r = p2.getUserNo() - p1.getUserNo();
          }
          if (r == 0) {
//            Collator cmp = Collator.getInstance(java.util.Locale.CHINA); 
//            r = cmp.compare(p1.getUserName(), p2.getUserName());
            r = p1.getUserId().compareTo(p2.getUserId());
          }
          return sort > 0 ? r : -r;
        } catch (Exception e) {
          return 0;
        }
      }
    });
  }
  
  
  /**
   * 把树转为json串

   * @param root
   * @return
   * @throws Exception 
   */
  private StringBuffer breadthFirst(Connection dbConn, DefaultMutableTreeNode root) throws Exception {
    StringBuffer sb = new StringBuffer("[");
    Enumeration<DefaultMutableTreeNode> e = root.breadthFirstEnumeration();
    while (e.hasMoreElements()) {
      DefaultMutableTreeNode o = e.nextElement();
      Object uo = o.getUserObject();
      if (uo != null && uo instanceof YHDepartment) {
        this.deptToString((YHDepartment)uo, sb, o.getChildCount());
        sb.append(",");
      }
      else if (uo != null && uo instanceof YHPerson) {
        Object tmp = ((DefaultMutableTreeNode)o.getParent()).getUserObject();
        if (tmp instanceof YHDepartment) {
          this.userToString(dbConn, (YHPerson)uo, sb, ((YHDepartment)tmp).getSeqId());
          sb.append(",");
        }
      }
    }
    if (sb.charAt(sb.length() - 1) == ',') {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb;
  }
  
  /**
   * 在线人员树的人员节点转成json串

   * @param dbConn
   * @param p
   * @param sb
   * @param parentId
   * @throws Exception 
   */
  private void userToString(Connection dbConn, YHPerson p, StringBuffer sb, int parentId) throws Exception {
    sb.append("{\"nodeId\": \"r");
    sb.append(p.getSeqId());
    sb.append("\",\"name\": \"");
    sb.append(p.getUserName());
    sb.append("\",\"parentId\": \"");
    sb.append(parentId);
    sb.append("\",\"isHaveChild\": 0");
    String sex = p.getSex();
    String userStates = p.getOnStatus();
    if(YHUtility.isNullorEmpty(userStates)){
      userStates="";
    }
    String userStateImg;
    if(sex != null && sex.equals("1")){
      if(userStates.equals("1")){
        userStateImg = "/ispirit/images/U11.png";
      }else if(userStates.equals("2")){
        userStateImg = "/ispirit/images/U12.png";
      }else if(userStates.equals("3")){
        userStateImg = "/ispirit/images/U13.png";
      }else {
        userStateImg = "/ispirit/images/U11.png";
      }
    }else {
      if(userStates.equals("1")){
        userStateImg = "/ispirit/images/U01.png";
      }else if(userStates.equals("2")){
        userStateImg = "/ispirit/images/U02.png";
      }else if(userStates.equals("3")){
        userStateImg = "/ispirit/images/U03.png";
      }else {
        userStateImg = "/ispirit/images/U01.png";
      }
    }
    YHOrgSelectLogic logic = new YHOrgSelectLogic();
    String deptName = YHUtility.encodeSpecial(logic.getDeptNameLogic(dbConn, p.getDeptId()));
    String roleName; 
    try {
      roleName = YHUtility.encodeSpecial(logic.getRoleNameLogic(dbConn, Integer.parseInt(p.getUserPriv())));
    } catch (NumberFormatException e) {
      roleName = "";
    }
    
    String showIpFlag = logic.getSecrityShowIp(dbConn);
    String showIp = "";
    if("1".equals(showIpFlag)){
      if (p.isAdminRole()) {
        String ip = logic.getShowIp(dbConn, YHLogConst.LOGIN, p.getSeqId());
        ip = YHUtility.encodeSpecial(ip);
        showIp = "\\n最后登录IP:" + ip;
      }
    }
    else if("2".equals(showIpFlag)) {
      String ip = logic.getShowIp(dbConn, YHLogConst.LOGIN, p.getSeqId());
      ip = YHUtility.encodeSpecial(ip);
      showIp = "\\n最后登录IP:" + ip;
    }
    
    sb.append(",\"title\": \"部门:");
    sb.append(deptName);
    sb.append("\\n角色:");
    sb.append(roleName);
    sb.append("\\n工作电话:");
    sb.append(YHUtility.encodeSpecial(p.getTelNoDept()));
    sb.append("\\nemail:");
    sb.append(YHUtility.encodeSpecial(p.getEmail()));
    sb.append("\\nQQ:");
    sb.append(YHUtility.encodeSpecial(p.getOicq()));
    sb.append("\\n人员状态:");
    sb.append(YHUtility.encodeSpecial(p.getMyStatus()));
    sb.append(showIp);
    sb.append("\",\"imgAddress\": imgPath + \"");
    sb.append(userStateImg);
    sb.append("\"}");
    
  }
  
  /**
   * 在线人员树的部门节点转成json串

   * @param d
   * @param sb
   * @param childCount
   */
  private void deptToString(YHDepartment d, StringBuffer sb, int childCount) {
    sb.append("{\"nodeId\": \"");
    sb.append(d.getSeqId());
    sb.append("\",\"name\": \"");
    sb.append(d.getDeptName());
    sb.append("\",\"parentId\": \"");
    sb.append(d.getDeptParent());
    sb.append("\",\"isHaveChild\": ");
    sb.append(childCount > 0 ? 1 : 0);
    sb.append(",\"title\": \"部门:");
    sb.append(d.getDeptName());
    sb.append("\",\"imgAddress\": imgPath + \"/dtree/node_dept.gif\"}");
  }
  
  
  /**
   * 群聊获取人员列表
   **/
  public String getGroupUserList(HttpServletRequest request, HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    try {
     YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     dbConn = requestDbConn.getSysDbConn();
     String groupId=request.getParameter("groupId");
     String data="";
     
     

     
     
     YHIsPiritLogic IPLogic = new YHIsPiritLogic();
     Map<String,String> map=IPLogic.getGroupUserList(dbConn, groupId);
     String onLine=map.get("onLineUser");
     String offLine=map.get("offLineUser");
     
     data="{onLine:"+onLine+",offLine:"+offLine+"}";
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
    request.setAttribute(YHActionKeys.RET_DATA, data);
  } catch (Exception ex) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
    throw ex;
   }
     return "/core/inc/rtjson.jsp";
  }

  
}
 
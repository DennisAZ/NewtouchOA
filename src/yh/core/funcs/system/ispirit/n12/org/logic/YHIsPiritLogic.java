package yh.core.funcs.system.ispirit.n12.org.logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHUserOnline;
import yh.core.funcs.system.interfaces.data.YHSysPara;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHDigestUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHIsPiritLogic {
  private static Logger log = Logger.getLogger(YHIsPiritLogic.class);
  
  public void getOrgXmlData(Connection conn,String webP)throws Exception{
    String orgStr="";
    Statement stmt=null;
    ResultSet rs=null;
    try{
      String sql=" select * from oa_organization ";
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      String orgName="";
      int seqId=0;
      if(rs.next()){
        seqId= rs.getInt("seq_id");
        orgName=rs.getString("unit_name");
      }
      String deptData=this.getDeptStream(conn);     
      orgStr+="<org a=\"0\" b=\""+orgName+"\">";
      orgStr+=deptData;
      orgStr+="</org>";
    }catch(Exception ex){
      ex.printStackTrace();
    }
    orgStr="<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+orgStr;

    //生成xml文件
    String filePath=webP+File.separator+"org.xml";
     try {
      FileOutputStream fs = new FileOutputStream(filePath);
      OutputStreamWriter OS= new OutputStreamWriter (fs,"utf-8");
      BufferedWriter out = new BufferedWriter(OS);
      out.write(orgStr, 0, orgStr.length());
      out.close();
      OS.close();
      fs.close();
     } catch (IOException e) {
      e.printStackTrace();
     }
     
     
     //压缩文件
     try {
       MyZipUtil zip = new MyZipUtil();
       File file=new File(filePath);
       String zipFile=webP+File.separator+"org.zip";
    //   zip.ZipFile(zipFile, filePath);
       zip.doZip(filePath, zipFile);
       
     }catch (Exception e) {
       e.printStackTrace(System.out);
     }

  }
  
  /**
   * 生成最新org文件 
   * 供给精灵下载
   */
  public String getDeptStream(Connection DbConn)throws Exception{
    Statement stmt=null;
    ResultSet rs=null;
    String dXmlStr="";
    try{
      String sql=" select seq_id,dept_name,dept_no,is_org from oa_department order by dept_no asc ";
      stmt=DbConn.createStatement();
      rs=stmt.executeQuery(sql);
     
      while(rs.next()){
        int seqId=rs.getInt("seq_id");
        String deptName= rs.getString("dept_name");
        String deptNo= rs.getString("dept_no");
        String isOrg= rs.getString("is_org");
        String uxmlData=this.getUxmlData(DbConn, seqId+"");
        dXmlStr+="<d a=\""+seqId+"\" b=\""+deptName+"\" c=\""+YHUtility.null2Empty(isOrg)+"\">"+uxmlData+"</d>" ;
      }
      
    }catch(Exception ex){
      ex.printStackTrace();
    }
    
    
    return dXmlStr;
    
  }
  
  
  /**
   *根据部门ID获取人员xml 
   */
  public String getUxmlData(Connection conn,String deptId)throws Exception{
    String uXmlStr="";
    Statement stmt=null;
    ResultSet rs=null;
    try{
      String sql=" select * from person where dept_id='"+deptId+"' ";
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      while(rs.next()){
        String seqId= rs.getString("seq_id");
        String userName=rs.getString("user_name");
        String userId=rs.getString("user_id");
        String userPriv=rs.getString("user_priv");
        String sex=rs.getString("sex");
        String myStatus=rs.getString("my_status");
        String imRange=rs.getString("IM_RANGE");
        String concernUser= rs.getString("CONCERN_USER");
        uXmlStr+="<u a=\""+seqId+"\" b=\""+userName+"\" c=\""+YHUtility.null2Empty(userPriv)+"\" d=\""+YHUtility.null2Empty(sex)+"\"  e=\""+YHUtility.null2Empty(myStatus)+"\" f=\""+YHUtility.null2Empty(imRange)+"\" g=\""+YHUtility.null2Empty(concernUser)+"\" i=\""+YHUtility.null2Empty(deptId)+"\"/>";
      }
      
    }catch(Exception ex){
      ex.printStackTrace();
    }
    
    return uXmlStr;
    
  }
  /**
   *获取人员列表
   */
  public YHSysPara getUserListView(Connection dbConn,String status) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHSysPara org = null;
    try {
      String queryStr = "select SEQ_ID, PARA_NAME, PARA_VALUE from SYS_PARA where PARA_NAME='ONLINE_VIEW'";
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
      while (rs.next()) {
        org = new YHSysPara();
        org.setSeqId(rs.getInt("SEQ_ID"));
        org.setParaName(rs.getString("PARA_NAME"));
        org.setParaValue(rs.getString("PARA_VALUE"));
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return org;
  }
  
  
  /***
   *get user avator stram
   *   
   */
  public  FileInputStream getUserPicStream(Connection conn,String seqId,String webPath)throws Exception{
    FileInputStream result = null;
    YHORM orm = new YHORM();
    try{
      YHPerson person = (YHPerson)orm.loadObjSingle(conn, YHPerson.class, Integer.parseInt(seqId));
      String avator=person.getPhoto();
      String sex=person.getSex();
      String Filepath=webPath+"/attachment/photo/"+avator;
      Filepath=this.getFilePath(Filepath);
      File file =new File(Filepath);
      if(!file.exists()){
        String fileStr=webPath+"/core/styles/imgs/photo/default.gif";

        fileStr=this.getFilePath(fileStr);
        file=new File(fileStr);
      }
      result= new FileInputStream(file);
    }catch(Exception ex){
      ex.printStackTrace();
    }
    
    return result;
  }
  
  public  FileInputStream getUserAvatorStream(Connection conn,String seqId,String webPath)throws Exception{
    FileInputStream result = null;
    YHORM orm = new YHORM();
    try{
      YHPerson person = (YHPerson)orm.loadObjSingle(conn, YHPerson.class, Integer.parseInt(seqId));
      String avator=person.getAuatar();
      String sex=person.getSex();
      String Filepath=webPath+"/attachment/avatar/"+avator;
      Filepath=this.getFilePath(Filepath);
      File file =new File(Filepath);
      if(!file.exists()){
        String fileStr=webPath+"/core/styles/imgs/avatar/1.gif";
        if("1".equals(sex)){
          fileStr=webPath+"/core/styles/imgs/avatar/g.gif";
        }
        fileStr=this.getFilePath(fileStr);
        file=new File(fileStr);
      }
      result= new FileInputStream(file);
    }catch(Exception ex){
      ex.printStackTrace();
    }
    
    return result;
  }
  
  
  public String getgetUserAvatorPath(Connection conn,String seqId,String webPath)throws Exception{
    String filePath="";
    YHORM orm = new YHORM();
    YHPerson person = (YHPerson)orm.loadObjSingle(conn, YHPerson.class, Integer.parseInt(seqId));
    String avator=person.getAuatar();
    String sex=person.getSex();
    String Filepath=webPath+"/attachment/avatar/"+avator;
    filePath =Filepath;
    return getFilePath(filePath);
  }
  
  public String getgetUserPicPath(Connection conn,String seqId,String webPath)throws Exception{
    String filePath="";
    YHORM orm = new YHORM();
    YHPerson person = (YHPerson)orm.loadObjSingle(conn, YHPerson.class, Integer.parseInt(seqId));
    String pic=person.getPhoto();
    String sex=person.getSex();
    String Filepath=webPath+"/attachment/photo/"+pic;
    filePath =Filepath;
    return getFilePath(filePath);
  }
  
  /**
   *判断MD5值
   */
  public boolean getCheckMd5Act(String cMd5,String filePath) throws Exception{
    String webPath  =YHSysProps.getRootPath();
    boolean isValid=false;
    isValid=YHDigestUtility.isFileMatch(filePath, cMd5);
    return isValid;
  }
  
  
  /***
   *get user picture stram
   *   
   */
  public  FileInputStream getUserPictureStream(Connection conn,String seqId,String webPath)throws Exception{
    FileInputStream result = null;
    YHORM orm = new YHORM();
    try{
      YHPerson person = (YHPerson)orm.loadObjSingle(conn, YHPerson.class, Integer.parseInt(seqId));
      String pic=person.getPhoto();
      String sex=person.getSex();
      String Filepath=webPath+"/attachment/photo/"+pic;
      Filepath=this.getFilePath(Filepath);
      File file =new File(Filepath);
      if(!file.exists()){
        result=null;
      }else{
        result= new FileInputStream(file);
      }
   
    }catch(Exception ex){
      ex.printStackTrace();
    }
    
    return result;
  }
  /**
   * userinfo 
   */
  public String getUserInfo(Connection conn,String uid)throws Exception{
    String xmlStr="";
    Statement stmt=null;
    ResultSet rs=null;
    try{
      String uidOrg[]=uid.split(",");
      for(int i =0; i<uidOrg.length;i++){
        String id=uidOrg[i];
        if(!YHUtility.isNullorEmpty(id)){
          String sql=" select * from person where seq_id='"+id+"' "; 
          stmt=conn.createStatement();
          rs=stmt.executeQuery(sql);
          if(rs.next()){
            String userPriv=this.getRoleNameLogic(conn, Integer.parseInt(rs.getString("user_priv")));
            xmlStr+="<u a='"+rs.getString("seq_id")+"' b='"+rs.getString("user_name")+"' h='"+rs.getString("user_id")+"' i='"+rs.getString("dept_id")+"' j='"+YHUtility.null2Empty(rs.getString("TEL_NO_DEPT"))+"' k='"+YHUtility.null2Empty(rs.getString("MOBIL_NO"))+"' l='"+YHUtility.null2Empty(rs.getString("EMAIL"))+"'  m='"+YHUtility.null2Empty(rs.getString("OICQ"))+"' n='"+userPriv+"'>"+YHUtility.null2Empty(rs.getString("MY_STATUS"))+"</u>\n";
          }
        }
      }
      
      
    }catch(Exception ex){
      ex.printStackTrace();
    }

    return xmlStr;
  }
  
  
  
  /**
   * 装换路径表示
   */
  public String getFilePath(String filePath)throws Exception{

    return filePath.replaceAll("//", File.separator).replace("/", File.separator).replace("\\", File.separator);
    
  }
  
  /**
   * 取得部门名称
   * @param conn
   * @param deptId
   * @return
   * @throws Exception
   */
  public String getDeptNameLogic(Connection conn , int deptId) throws Exception{
    String result = "";
    String sql = " select DEPT_NAME from oa_department where SEQ_ID = " + deptId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  /**
   * 取得角色名称
   * @param conn
   * @param roleId
   * @return
   * @throws Exception
   */
  public String getRoleNameLogic(Connection conn , int roleId) throws Exception{
    String result = "";
    String sql = " select PRIV_NAME from USER_PRIV where SEQ_ID = " + roleId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  
  public String getSecrityShowIp(Connection conn) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String org = null;
    try {
      String queryStr = "select PARA_VALUE from SYS_PARA WHERE PARA_NAME='SEC_SHOW_IP'";
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      if (rs.next()) {
        org = rs.getString(1);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return org;
  }
  
  
  public ArrayList<YHUserOnline> getUserOnlineList(Connection dbConn) throws Exception{
    YHORM orm = new YHORM();
    String query = "select DISTINCT USER_ID, USER_STATE from oa_online";
    ArrayList<YHUserOnline> onLine = new ArrayList();
    Statement stm4 = null;
    ResultSet rs4 = null;
    try {
      stm4 = dbConn.createStatement();
      rs4 = stm4.executeQuery(query);
      while (rs4.next()) {
        YHUserOnline dept  = new YHUserOnline();
        dept.setUserId(rs4.getInt("USER_ID"));
        dept.setUserState(rs4.getString("USER_STATE"));
        onLine.add(dept);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm4 , rs4 , null);
    }
    return onLine;
  }
  
  /**
   * 获取在线的人员
   * @param dbConn
   * @return
   * @throws Exception
   */
  public List<YHPerson> getOnlineUsers(Connection dbConn) throws Exception{
    List<YHPerson> persons = new ArrayList<YHPerson>();
    List<YHUserOnline> list = getUserOnlineList(dbConn);
    YHORM orm = new YHORM();
    for (YHUserOnline u : list) {
      YHPerson p = (YHPerson) orm.loadObjSingle(dbConn, YHPerson.class, u.getUserId());
      if (p != null) {
      //  p.setOnStatus(u.getUserState());
        persons.add(p);
      }
    }
    return persons;
  }
  
  public String getUserOnlineUserId(Connection conn) throws Exception{
    String result = "";
    String sql = "select DISTINCT USER_ID from oa_online";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        int userId = rs.getInt("USER_ID");
        if(!"".equals(result)){
          result += ",";
        }
        result += userId;
      }
    } catch (Exception e) {
      throw e;   
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  

  public String getUserStatesLogic(Connection conn , int userId) throws Exception{
    String result = "";
    String sql = " select USER_STATE from oa_online where USER_ID = " + userId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  
  public String getShowIp(Connection conn, String sysLog, int userId) throws Exception {
    String result = "";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      String queryStr = "select IP from oa_sys_log where type = '" + sysLog + "' and USER_ID = " + userId + " order by TIME desc ";
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      if (rs.next()) {
        String org = rs.getString(1);
        if(org != null){
          result = org;
        }
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return result;
  }
  
  public List<YHDepartment> searchDeptparent(Connection dbConn, int seqId) throws Exception {
    List list = new ArrayList();
    YHDepartment de = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "SELECT DEPT_PARENT,SEQ_ID,DEPT_NAME,DEPT_NO FROM oa_department WHERE SEQ_ID = '" + seqId + "' order by DEPT_NO ASC, DEPT_NAME ASC";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        de = new YHDepartment();
        de.setDeptParent(rs.getInt("DEPT_PARENT"));
        de.setSeqId(rs.getInt("SEQ_ID"));
        de.setDeptName(rs.getString("DEPT_NAME"));
        de.setDeptNo(rs.getString("DEPT_NO"));
        list.add(de);
        if(rs.getInt("DEPT_PARENT") == 0){
          return list;
        }
        List srclist = searchDeptparent(dbConn,rs.getInt("DEPT_PARENT"));
        list.addAll(srclist);
      }
//      for(Iterator it = list.iterator(); it.hasNext();){
//        YHDepartment der = (YHDepartment)(it.next());
//        List srclist = searchDeptparent(dbConn,der.getSeqId());
//        list.addAll(srclist);
//      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return list;
  }
  
  
  /**
   * 构造所有部门树
   * @param dbConn
   * @return
   * @throws Exception
   */
  public DefaultMutableTreeNode buildDeptTree(Connection dbConn) throws Exception {
    DefaultMutableTreeNode root = new DefaultMutableTreeNode(new Object(), true);
    
    try {
      YHORM orm = new YHORM();
      List<YHDepartment> list = orm.loadListSingle(dbConn, YHDepartment.class, new HashMap<String, String>());
      Map<Integer, DefaultMutableTreeNode> nodes = new HashMap<Integer, DefaultMutableTreeNode>();
      for (YHDepartment d : list) {
        nodes.put(d.getSeqId(), new DefaultMutableTreeNode(d));
      }
      List<DefaultMutableTreeNode> values = new ArrayList<DefaultMutableTreeNode>(nodes.values());
      Collections.sort(values, new Comparator<DefaultMutableTreeNode>() {

        public int compare(DefaultMutableTreeNode node1, DefaultMutableTreeNode node2) {
          YHDepartment d1 = (YHDepartment)node1.getUserObject();
          YHDepartment d2 = (YHDepartment)node2.getUserObject();
          int c = d1.getDeptNo().compareTo(d2.getDeptNo());
          if (c == 0) {
//            Collator cmp = Collator.getInstance(java.util.Locale.CHINA); 
//            c = cmp.compare(d1.getDeptName(), d2.getDeptName());
            //d1.getDeptCode().compareTo(d2.getDeptCode());
            c = d1.getDeptName().compareTo(d2.getDeptName());
          }
          return c;
        }
        
      });
      for (DefaultMutableTreeNode node : values) {
        YHDepartment d = (YHDepartment)node.getUserObject();
        int parentId = d.getDeptParent();
        if (parentId == 0) {
          root.add(node);
        }
        else {
          nodes.get(parentId).add(node);
        }
      }
      
    } catch (Exception e) {
      throw e;
    } finally {
      
    }
    return root;
  }
  
  /**
   * 获取人员列表
   * */
  
  public Map<String,String> getGroupUserList(Connection conn,String groupId)throws Exception{
    Map<String,String> data= new HashMap();
    String onLineUser="";
    String offLineUser="";
    Statement stmt=null;
    ResultSet rs=null;
    try{
     String groupUID=this.getGroupUserByGId(conn, groupId);
     String sql=" SELECT person.seq_ID,USER_ID,SEX,USER_NAME,ON_status from person,USER_PRIV where person.seq_ID in ("+groupUID+") and person.USER_PRIV=USER_PRIV.seq_id and person.NOT_LOGIN!='1' order by PRIV_NO,USER_NO,USER_NAME ";
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     while(rs.next()){
       String seqId = rs.getString("seq_id");
       String userName=rs.getString("user_name");
       String sex = rs.getString("sex");
       String status=rs.getString("on_status");
       if(this.isOnLineUser(conn, seqId)){
         onLineUser+="{uId:\""+seqId+"\",uName:\""+userName+"\",uIcon:\"<img src='../images/U"+sex+status+".png' style='vertical-align:middle;' />\"},";
       }else{
         offLineUser+="{uId:\""+seqId+"\",uName:\""+userName+"\",uIcon:\"<img src='../images/U"+sex+"0.png' style='vertical-align:middle;' />\"},";
       }
       
           
     }
      if(onLineUser.endsWith(",")){
        onLineUser=onLineUser.substring(0, onLineUser.length()-1);
      }
      if(offLineUser.endsWith(",")){
        offLineUser=offLineUser.substring(0, offLineUser.length()-1);
      }
     
      data.put("offLineUser", "["+offLineUser+"]");
      data.put("onLineUser", "["+onLineUser+"]");
      
    }catch(Exception e){
      e.printStackTrace();
    }
    
    
    return data;
  }
  
  /**
   * 判断人员是否在线
   * */
  public boolean isOnLineUser(Connection conn,String uid)throws Exception{
    boolean isOnLine=false;
    try{
      List<YHPerson> persons=this.getOnlineUsers(conn);
      for(int i=0;i<persons.size();i++){
        YHPerson p = persons.get(i);
        if(uid.equals(p.getSeqId()+"")){
          isOnLine=true;
        }
        
      }
      
    }catch(Exception e){
      e.printStackTrace();
    }
    return isOnLine;
  }
  
  /**
   * 根据群id获取群人员
   * */
  public String getGroupUserByGId(Connection conn,String gId)throws Exception{
    String data="";
    Statement stmt=null;   
    ResultSet rs=null;
    try{
      String sql=" select GROUP_UID FROM oa_im_group where GROUP_ID='"+gId+"' ";
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      if(rs.next()){
        data=rs.getString("GROUP_UID");
      }
    }catch(Exception e){
      e.printStackTrace();
    }

    return data;
  }
  /**
   *获取最近用户
   **/
  public String getRecentUserById(Connection conn,String gId)throws Exception{
    String data="";
    Statement stmt=null;   
    ResultSet rs=null;
    try{
     if(!YHUtility.isNullorEmpty(gId)){
      if(gId.endsWith(",")){
        gId=gId.substring(0, gId.length()-1);
      }
      String sql=" SELECT seq_ID,USER_ID,USER_NAME,SEX,on_status from person where seq_id  in ("+gId+")";
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      while(rs.next()){

        String UID=rs.getString("seq_ID");
        String USER_ID=rs.getString("USER_ID");
        String USER_NAME=rs.getString("USER_NAME");
        String SEX=rs.getString("SEX");
      
        String status=rs.getString("on_status");
        String imgStr="";
        if(this.isOnLineUser(conn, UID)){
         imgStr=" <img src='../images/U"+SEX+status+".png' style='vertical-align:middle;' /> ";
        }else{
         imgStr=" <img src='../images/U"+SEX+"0.png' style='vertical-align:middle;' /> ";
        }
        data+="<tr onclick=\" onClickSentMsg('"+UID+"','"+USER_NAME+"')\" class=\"\" type=\"user\" uid='"+UID+"' user_id='"+USER_ID+"' user_name='"+USER_NAME+"'><td class='U"+SEX+"0"+"'>"+imgStr+""+USER_NAME+"</td></tr>";
        
        
      }
    }
      if(YHUtility.isNullorEmpty(data)){
        data="<tr class=\"\"><td align=\"center\"><br>暂无最近联系人<br><br></td></tr>";
      }
      
     data=" <table class=\"\" width=\"97%\" align=\"center\"> "+data+"</table>";

    }catch(Exception e){
      e.printStackTrace();
      data=" <table class=\"\" width=\"97%\" align=\"center\"> <tr class=\"\"><td align=\"center\"><br>程序出错<br><br></td></tr></table>";
    }

    return data;
  } 
  
  
  /**
   *获取最近用户
   **/
  public String getimGroupUserById(Connection conn,String gId)throws Exception{
    String data="";
    Statement stmt=null;   
    ResultSet rs=null;
    try{
      String sql="SELECT * from oa_im_group where GROUP_ACTIVE='1' and "+YHDBUtility.findInSet(gId, "GROUP_UID")+" order by ORDER_NO";
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      while(rs.next()){

        String GROUP_ID=rs.getString("GROUP_ID");
        String GROUP_NAME=rs.getString("GROUP_NAME");
        String GROUP_SUBJECT=rs.getString("GROUP_SUBJECT");
        String GROUP_INTRODUCTION=rs.getString("GROUP_INTRODUCTION");
        if(GROUP_SUBJECT.length()>11){
          GROUP_SUBJECT=GROUP_SUBJECT.substring(0, 11)+"...";
        }
        data+="<tr onclick=\"onClickSentGroupMsg('"+GROUP_ID+"','"+GROUP_NAME+"')\" class=\"TableData sub-module-item\" type=\"group\" group_id='"+GROUP_ID+"' group_name='"+GROUP_NAME+"'><td title=\""+GROUP_INTRODUCTION+"\" class=\"im-group\"><img src=\"images/group.png\"/>"+GROUP_NAME+"<span >"+GROUP_SUBJECT+"</span></td></tr>";  
    
      }
      if(YHUtility.isNullorEmpty(data)){
        data="<tr  class=\"noGroup\"><td align=\"center\"><br>未设置群组<br><br></td></tr>";
      }
      
     data=" <table class=\"\" width=\"97%\" align=\"center\"> "+data+"</table>";
     
    }catch(Exception e){
      e.printStackTrace();
    }

    return data;
  } 
  
  
  /**
   * 设置提醒的标志，接口 
   **/
  public static void setUserSmsRemind(String userId){
    String webPath = YHSysProps.getRootPath();
    webPath+=File.separator+"IM"+File.separator+"remindTemp"+File.separator;
    File mk =new File(webPath); 
    if(!mk.exists()){
      mk.mkdirs();
    }
    
    //file
    webPath+=userId+".txt";
    File userFile =new File(webPath); 
    if(!userFile.exists()){           //新建
       String filein="10";
       RandomAccessFile mm = null;
        try {
         mm = new RandomAccessFile(userFile,"rw");
         mm.writeBytes(filein);
        } catch (IOException e1) {
       
         e1.printStackTrace();
        } finally{
         if(mm!=null){
          try {
           mm.close();
          } catch (IOException e2) {
           e2.printStackTrace();
          }
        }
      }
    }else{  //  修改
      
      try{ //读取内容
        BufferedReader   bw; 
        String flags="";
        StringBuffer sb =new StringBuffer();
        bw=new  BufferedReader(new  FileReader(userFile));                                      //用字符流读取文件（用缓冲流封装文件的数据流） 
        while((flags=bw.readLine())!=null){ 
          sb.append(flags);
        } 
        bw.close(); 
        flags=sb.toString(); 
        flags=flags.trim();
        flags="1"+flags.substring(1, flags.length());
        //完成读取
        
        
        //写回文件
        RandomAccessFile mm = null;
        try {
         mm = new RandomAccessFile(userFile,"rw");
         mm.writeBytes(flags);
        } catch (IOException e1) {
         e1.printStackTrace();
        } finally{
         if(mm!=null){
          try {
           mm.close();
          } catch (IOException e2) {
           e2.printStackTrace();
          }
         } 
        }//完成修改
        
      }catch(IOException   e){
        e.printStackTrace();
      } 
    }
  }
  
  /**
   * 设置提醒的标志，接口 
   **/
  public static void setUserMessageRemind(String userId){
    String webPath = YHSysProps.getRootPath();
    webPath+=File.separator+"IM"+File.separator+"remindTemp"+File.separator;
    File mk =new File(webPath); 
    if(!mk.exists()){
      mk.mkdirs();
    }
    
    //file
    webPath+=userId+".txt";
    File userFile =new File(webPath); 
    if(!userFile.exists()){           //新建
       String filein="01";
       RandomAccessFile mm = null;
        try {
         mm = new RandomAccessFile(userFile,"rw");
         mm.writeBytes(filein);
        } catch (IOException e1) {
       
         e1.printStackTrace();
        } finally{
         if(mm!=null){
          try {
           mm.close();
          } catch (IOException e2) {
           e2.printStackTrace();
          }
        }
      }
    }else{  //  修改
      
      try{ //读取内容
        BufferedReader   bw; 
        String flags="";
        StringBuffer sb =new StringBuffer();
        bw=new  BufferedReader(new  FileReader(userFile));                                      //用字符流读取文件（用缓冲流封装文件的数据流） 
        while((flags=bw.readLine())!=null){ 
          sb.append(flags);
        } 
        bw.close(); 
        flags=sb.toString(); 
        flags=flags.trim();
        flags=flags.substring(1, flags.length())+"1";
        //完成读取
        
        
        //写回文件
        RandomAccessFile mm = null;
        try {
         mm = new RandomAccessFile(userFile,"rw");
         mm.writeBytes(flags);
        } catch (IOException e1) {
         e1.printStackTrace();
        } finally{
         if(mm!=null){
          try {
           mm.close();
          } catch (IOException e2) {
           e2.printStackTrace();
          }
         } 
        }//完成修改
        
      }catch(IOException   e){
        e.printStackTrace();
      } 
    }
  }
  
  
  //check remind for sms
  public static String checkUserSms(String userId){
    String count="0";
    String webPath = YHSysProps.getRootPath();
    webPath+=File.separator+"IM"+File.separator+"remindTemp"+File.separator;
    //file
    webPath+=userId+".txt";
 
    BufferedReader   bw; 
    String flags="";
    File userFile =new File(webPath); 
    if(userFile.exists()){
    StringBuffer sb =new StringBuffer();
    try{
      bw=new  BufferedReader(new  FileReader(userFile));                                      //用字符流读取文件（用缓冲流封装文件的数据流） 
      while((flags=bw.readLine())!=null){ 
        sb.append(flags);
      } 
      bw.close(); 
      flags=sb.toString(); 
      flags=flags.trim();
      flags=flags.substring(0,1);
      count=flags;
    //  System.out.println(webPath+"-----sms:"+count);
      //setback
      if(count.compareToIgnoreCase("0")>0){
      RandomAccessFile mm = null;
      try {
        flags="0"+sb.toString().substring(1, flags.length());
       mm = new RandomAccessFile(userFile,"rw");
       mm.writeBytes(flags);
      } catch (IOException e1) {
       e1.printStackTrace();
      } finally{
       if(mm!=null){
        try {
         mm.close();
        } catch (IOException e2) {
         e2.printStackTrace();
        }
       } 
      }
      }//完成修改
      
      
      
    }catch(IOException   e){
      userFile.delete();
      e.printStackTrace();
    } 
    }
    
    return count;
  } 
  
  //check remind for message
  public static String checkUserMessage(String userId){
    String count="0";
    String webPath = YHSysProps.getRootPath();
    webPath+=File.separator+"IM"+File.separator+"remindTemp"+File.separator;
    //file
    webPath+=userId+".txt";
   // System.out.println(webPath+"-----Message");
    
    BufferedReader   bw; 
    String flags="";
    File userFile =new File(webPath); 
    if(userFile.exists()){
    StringBuffer sb =new StringBuffer();
    try{
      bw=new  BufferedReader(new  FileReader(userFile));                                      //用字符流读取文件（用缓冲流封装文件的数据流） 
      while((flags=bw.readLine())!=null){ 
        sb.append(flags);
      } 
      bw.close(); 
      flags=sb.toString(); 
      flags=flags.trim();
      flags=flags.substring(1,flags.length());
      count=flags;
   //   System.out.println(webPath+"-----message:"+count);
      //setback
      if(count.compareToIgnoreCase("0")>0){
      RandomAccessFile mm = null;
      try {
        flags=sb.toString().substring(0, 1)+"0";
       mm = new RandomAccessFile(userFile,"rw");
       mm.writeBytes(flags);
      } catch (IOException e1) {
       e1.printStackTrace();
      } finally{
       if(mm!=null){
        try {
         mm.close();
        } catch (IOException e2) {
         e2.printStackTrace();
        }
       } 
      }
      }//完成修改
      
    }catch(IOException   e){
      userFile.delete();
      e.printStackTrace();
    } 
    }
    return count;
  } 
}

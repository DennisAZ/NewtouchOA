package yh.core.funcs.email.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import yh.core.funcs.email.data.YHEmailBox;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHEmailBoxLogic{
  /**
   * 邮箱操作保存邮箱
   * @param conn
   * @param request YHEmailBox 表单数据
   * @param userId
   * @throws Exception
   */
  public void saveBox(Connection conn,Map request,int userId) throws Exception{
    YHORM orm =new YHORM();
    YHEmailBox eb = (YHEmailBox) YHFOM.build(request, YHEmailBox.class, null);
    eb.setUserId(userId);
    //System.out.println("********新建邮箱******");
    orm.saveSingle(conn, eb);
    //System.out.println("*********成功新建*****");
  }
  /**
   * 修改邮箱
   * @param conn
   * @param request
   * @param userId
   * @throws Exception
   */
  public void updateBox(Connection conn,Map request) throws Exception{
    YHORM orm =new YHORM();
    YHEmailBox eb = (YHEmailBox) YHFOM.build(request, YHEmailBox.class, null);
    //eb.setUserId(userId);
    //System.out.println("********修改邮箱******");
    orm.updateSingle(conn, eb);
    //System.out.println("*********成功修改*****");
  }
  /**
   * 修改邮箱
   * @param conn
   * @param request
   * @param userId
   * @throws Exception
   */
  public void setPageSize(Connection conn,int pageSize,int seqId,String boxName,int userId) throws Exception{
    String sqlupdate = "";
    if(seqId != -1){
      sqlupdate = "update oa_email_box SET DEFAULT_COUNT=" + pageSize + " WHERE SEQ_ID = " + seqId;
    }else{
      sqlupdate = "update oa_email_box SET DEFAULT_COUNT=" + pageSize + " WHERE BOX_NAME = '" + boxName + "' and USER_ID = " + userId;
    }
    String sqlquery = "SELECT COUNT(SEQ_ID) FROM oa_email_box  WHERE BOX_NAME = '" + boxName + "' and USER_ID = " + userId;
    String sqlinsert = "insert into oa_email_box(BOX_NO,BOX_NAME,DEFAULT_COUNT,USER_ID) VALUES(0,'" + boxName + "'," + pageSize + "," + userId + ")";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sqlquery);
      rs = ps.executeQuery();
      if(rs.next()){
        int count = rs.getInt(1);
        if(count > 0){
          ps.executeUpdate(sqlupdate);
        }else{
          ps.executeUpdate(sqlinsert);
        }
      }else{
        ps.executeUpdate(sqlinsert);
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, rs, null);
    }
  }
  /**
   * 删除自定义邮箱
   * @param conn
   * @param request
   * @param userId
   * @throws Exception
   */
  public void deleteBox(Connection conn,int boxId) throws Exception{
    YHORM orm =new YHORM();
    //System.out.println("********删除邮箱******");
    orm.deleteSingle(conn, YHEmailBox.class, boxId);
    //System.out.println("*********成功删除*****");
  }
  /**
   * 列出所有用户的自定义邮箱
   * @param conn
   * @param userId
   * @param isAll 是否列出所有邮箱，false表示只列出自定义邮箱，不列出默认邮箱
   * @throws Exception
   */
  public ArrayList<YHEmailBox> listBoxByUser(Connection conn,int userId,boolean isAll) throws Exception{
    YHORM orm =new YHORM();
    String[] filters = null;
    if(isAll){
      filters = new String[]{"USER_ID=" + userId};
    } else{
      filters = new String[]{"USER_ID=" + userId ," NOT BOX_NO = 0"};
    }
    ArrayList<YHEmailBox> eblist = (ArrayList<YHEmailBox>) orm.loadListSingle(conn, YHEmailBox.class, filters);
    return eblist;
  }
  /**
   * 取得自定义邮箱的容量大小
   * @param conn
   * @param userId
   * @return
   * @throws Exception 
   */
  public long getBoxSizeById(Connection conn ,int boxId) throws Exception{
    //SELECT  SEQ_ID FROM EMAIL_BODY WHERE SEQ_ID IN( SELECT BODY_ID FROM EMAIL WHERE BOX_ID = 0)
    String sql = " SELECT  SUM(ENSIZE) FROM oa_email_body WHERE SEQ_ID IN( SELECT BODY_ID FROM EMAIL WHERE BOX_ID = " + boxId + ") ";
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    long result = 0l;
    try{
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while(rs.next()){
        result = rs.getLong(1);
      }
    } catch (Exception e){
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    return result;
  }
  /**
   * 取得默认邮箱的容量大小
   * @param conn
   * @param userId
   * @return
   * @throws Exception 
   */
  public StringBuffer getBoxSizeForDef(Connection conn,int userId) throws Exception{
    StringBuffer result = new StringBuffer();
    YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
    try{
      String inBoxIds = emul.getMailIds(conn, " TO_ID=" + userId +  " AND BOX_ID =0" + " AND DELETE_FLAG IN('0','2')");
    //  String outBoxIds = emul.getMailIds(conn, " TO_ID=" + userId +  " AND BOX_ID =0" + " AND DELETE_FLAG IN('0','2') AND READ_FLAG = 1 ");
      String sendBoxIds = emul.getMailIds(conn, " TO_ID=" + userId +  " AND BOX_ID =0" + " AND DELETE_FLAG IN('0','2') AND READ_FLAG = 1 ");
      String delBoxIds = emul.getMailIds(conn, " TO_ID=" + userId +  " AND BOX_ID =0" + " AND DELETE_FLAG IN('0','2') AND READ_FLAG = 1 ");
      String webBoxIds = emul.getMailIds(conn, " TO_ID=" + userId +  " AND BOX_ID =0" + " AND DELETE_FLAG IN('0','2') AND READ_FLAG = 1 ");

    } catch (Exception e){
      throw e ;
    }
    return result;
  }
  /**
   * 取得默认邮箱的容量大小(发件箱)
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public long getBoxSizeForSendDef(Connection conn,int userId) throws Exception{
    long result = 0l;
    YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
    try{
      String inBoxIds = emul.getMailIds(conn, " TO_ID=" + userId +  " AND BOX_ID =0" + " AND DELETE_FLAG IN('0','2')");
    //  String outBoxIds = emul.getMailIds(conn, " TO_ID=" + userId +  " AND BOX_ID =0" + " AND DELETE_FLAG IN('0','2') AND READ_FLAG = 1 ");
      String sendBoxIds = emul.getMailIds(conn, " TO_ID=" + userId +  " AND BOX_ID =0" + " AND DELETE_FLAG IN('0','2') AND READ_FLAG = 1 ");
      String delBoxIds = emul.getMailIds(conn, " TO_ID=" + userId +  " AND BOX_ID =0" + " AND DELETE_FLAG IN('0','2') AND READ_FLAG = 1 ");
      String webBoxIds = emul.getMailIds(conn, " TO_ID=" + userId +  " AND BOX_ID =0" + " AND DELETE_FLAG IN('0','2') AND READ_FLAG = 1 ");

    } catch (Exception e){
      throw e ;
    }
    return result;
  }
  /**
   * 设置邮件大小
   * @param conn
   * @param boxId
   * @param pageCount
   * @return
   * @throws Exception
   */
  public int setBoxMailPage(Connection conn,int boxId,String pageCount) throws Exception{
    String sql = "UPDATE oa_email_box SET DEFAULT_COUNT="+ pageCount + " WHERE SEQ_ID=" + boxId;
    PreparedStatement pstmt = null;
    int rowsuUp = -1;
    try{
      pstmt = conn.prepareStatement(sql);
      rowsuUp = pstmt.executeUpdate();
    } catch (Exception e){
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }
    return rowsuUp;
  }
  /**
   * 取得所有自定义的邮箱
   * @param conn
   * @param userId
   * @return
   * @throws Exception 
   */
  public StringBuffer getBoxSelfLogic(Connection conn , int userId) throws Exception{
    StringBuffer sb = new StringBuffer();
    StringBuffer fields = new StringBuffer();
    YHORM orm = new YHORM();
    String[] filters = new String[]{" USER_ID = " + userId+ " AND NOT BOX_NO = 0 order by BOX_NO asc,BOX_NAME asc"};
    ArrayList< YHEmailBox>  eboxList = (ArrayList<YHEmailBox>) orm.loadListSingle(conn, YHEmailBox.class, filters);
    for (YHEmailBox box : eboxList) {
      StringBuffer boxJson = YHFOM.toJson(box);
      int size = getSizeByBoxNo(conn, userId, box.getSeqId());
      if(!"".equals(fields.toString())){
        fields.append(",");
      }
      fields.append("{box:").append(boxJson)
        .append(",").append("size:").append(size)
        .append("}");
    }
    sb.append("[").append(fields).append("]");
    return sb;
  }
  /**
   * 取得所有自定义的邮箱
   * @param conn
   * @param userId
   * @return
   * @throws Exception 
   */
  public StringBuffer getBoxSelfForList(Connection conn , int userId) throws Exception{
    StringBuffer sb = new StringBuffer();
    StringBuffer fields = new StringBuffer();
    YHORM orm = new YHORM();
    String[] filters = new String[]{" USER_ID = " + userId+ " AND NOT BOX_NO = 0 order By BOX_NO asc,BOX_NAME ASC"};
    ArrayList< YHEmailBox>  eboxList = (ArrayList<YHEmailBox>) orm.loadListSingle(conn, YHEmailBox.class, filters);
    for (YHEmailBox box : eboxList) {
      StringBuffer boxJson = YHFOM.toJson(box);
      int size = getMailsByBoxNo(conn, userId, box.getSeqId(),1);
      int newsize = getMailsByBoxNo(conn, userId, box.getSeqId(),2);
      if(!"".equals(fields.toString())){
        fields.append(",");
      }
      fields.append("{box:").append(boxJson)
        .append(",").append("mails:").append(size)
        .append(",").append("newMails:").append(newsize)
        .append("}");
    }
    sb.append("[").append(fields).append("]");
    return sb;
  }
  
  /**
   * 取得所有自定义的邮箱

   * @param conn
   * @param userId
   * @return
   * @throws Exception 
   */
  public StringBuffer getBoxName(Connection conn , int userId,String boxId) throws Exception{
    StringBuffer sb = new StringBuffer();
    StringBuffer fields = new StringBuffer();
    String sql = "select BOX_NAME from oa_email_box WHERE USER_ID = " + userId+ " AND SEQ_ID = " + boxId ;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      String name = "";
      if (rs.next()) {
        name = rs.getString(1);
      }
      sb.append("{boxName:\"").append(name).append("\"}");
      return sb;
    } catch (Exception e) {
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    
  }
  /**
   * 取得所有自定义的邮箱
   * @param conn
   * @param userId
   * @return
   * @throws Exception 
   */
  public StringBuffer getBoxDefLogic(Connection conn , int userId) throws Exception{
    StringBuffer sb = new StringBuffer();
    StringBuffer fields = new StringBuffer();
    YHORM orm = new YHORM();
    String[] names = new String[]{"PAGESIZE_OUT","PAGESIZE_DEL","PAGESIZE_SENT","PAGESIZE_WEB","PAGESIZE_IN0"};
    for (String  name : names) {
      YHEmailBox box = getDefBox(conn, userId, name);
      StringBuffer boxJson = YHFOM.toJson(box);
      int size = getSizeByBoxNo(conn, userId, name);
      if(!"".equals(fields.toString())){
        fields.append(",");
      }
      fields.append("{box:").append(boxJson)
        .append(",").append("size:").append(size)
        .append(",").append("name:\"").append(name).append("\"")
        .append("}");
    }
    sb.append("[").append(fields).append("]");
    return sb;
  }
  private YHEmailBox getDefBox(Connection conn,int userId,String boxName) throws Exception{
    YHORM orm = new YHORM();
    HashMap<String , Object> filters = new HashMap<String, Object>();
    filters.put("USER_ID", userId);
    filters.put("BOX_NAME", boxName);
    YHEmailBox box = (YHEmailBox) orm.loadObjSingle(conn, YHEmailBox.class, filters);
    return box;
  }
  /**
   * 
   * @return
   * @throws Exception 
   */
  public int getSizeByBoxNo(Connection conn , int userId,int boxId) throws Exception{
    YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
    String field = " sum(T0.ENSIZE)";
    int intotal = emul.getCount(conn, " T1.TO_ID ='" + userId+ "' " +
        " AND T1.BOX_ID= " + boxId +
        " AND T1.DELETE_FLAG IN('0','2') " +
        " AND T1.BODY_ID = T0.SEQ_ID" , 1,field);
    return intotal;
  }
  /**
   * 
   * @return
   * @throws Exception 
   */
  public int getMailsByBoxNo(Connection conn , int userId,int boxId,int type) throws Exception{
    YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
    String field = " count(T0.SEQ_ID)";
    int intotal  = 0;
    if(type == 1){
     intotal = emul.getCount(conn, " T1.TO_ID ='" + userId+ "' " +
        " AND T1.BOX_ID= " + boxId +
        " AND T1.DELETE_FLAG IN('0','2') " +
        " AND T0.SEND_FLAG='1'"  + 
        " AND T1.BODY_ID = T0.SEQ_ID" , 1,field);
    }else{
      intotal =  emul.getCount(conn, " T1.TO_ID ='" + userId+ "' " +
          " AND T1.READ_FLAG= '0'" +
          " AND T1.BOX_ID=" + boxId +
          " AND T1.DELETE_FLAG IN('0','2') " +
          " AND T0.SEND_FLAG='1'"  + 
          " AND T1.BODY_ID = T0.SEQ_ID ",1,field);
    }
    return intotal;
  }
  /**
   * 
   * @return
   * @throws Exception 
   */
  public int getSizeByBoxNo(Connection conn , int userId,String name) throws Exception{
    YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
    String field = " sum(T0.ENSIZE)";
    //收件箱'EMAIL_IN':{'total':10,'newTotal':4}"PAGESIZE_OUT","PAGESIZE_DEL","PAGESIZE_SENT","PAGESIZE_WEB","PAGESIZE_IN0"
    if("PAGESIZE_IN0".equals(name)){
     return emul.getCount(conn, " T1.TO_ID ='" + userId+ "' " +
        " AND T1.BOX_ID= 0" + 
        " AND T1.DELETE_FLAG IN('0','2') " +
        " AND T1.BODY_ID = T0.SEQ_ID" , 1,field);
    }else if("PAGESIZE_DEL".equals(name)){
    //已删除邮件箱
      return emul.getCount(conn,  " T1.TO_ID ='" + userId+ "'  " 
        + " AND T1.BOX_ID= 0"  
        + " AND T1.DELETE_FLAG IN('3','4') "
        + " AND T1.BODY_ID = T0.SEQ_ID",1,field);
    } else if("PAGESIZE_SENT".equals(name)){
    //已发送邮件箱
      return emul.getCount(conn, " FROM_ID = '" + userId + "'" 
        + " AND T0.SEQ_ID in(select DISTINCT T1.BODY_ID FROM oa_email T1 WHERE NOT DELETE_FLAG in('2','4')) ",2,field);
    }else if("PAGESIZE_OUT".equals(name)){
    //草稿箱
      return emul.getCount(conn, " FROM_ID ='" + userId + "'" 
        + " AND NOT T0.SEQ_ID in(select DISTINCT T1.BODY_ID FROM oa_email T1) ",3,field);
    }else if("PAGESIZE_WEB".equals(name)){
    //草稿箱
      return 0;
    }else{
      return 0;
    }
  }
  
  public boolean isNameExist(Connection conn,int userId,String boxName,String boxId) throws Exception{
    String idFilter = "";
    if(boxId != null && !"".equals(boxId)){
      idFilter  = " AND NOT SEQ_ID =" + boxId;
    }
    String sql = " select count(SEQ_ID) from oa_email_box WHERE USER_ID=" + userId + " and BOX_NAME ='" + boxName + "'" + idFilter;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    boolean result = false;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        int cou = rs.getInt(1);
        if(cou > 0 ){
          result =  true;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
}

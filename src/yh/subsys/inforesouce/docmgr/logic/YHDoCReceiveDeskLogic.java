package yh.subsys.inforesouce.docmgr.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import yh.core.funcs.person.data.YHPerson;
import yh.subsys.inforesouce.docmgr.data.YHDocReceive;

public class YHDoCReceiveDeskLogic{
  /**
   * 桌面模块
   * @param conn
   * @param doc
   * @param user
   * @throws Exception 
   */
  public String myDocReceiveJsonDesk(Connection conn, YHPerson user) throws Exception{
    String sql = " select  dr.SEQ_ID, dr.DOC_NO, dr.RES_DATE, dr.FROMUNITS, dr.OPPDOC_NO, dr.TITLE, dr.COPIES, dr.CONF_LEVEL, dr.INSTRUCT, dr.SPONSOR, " +
                         " dr.RECE_USER_ID, dr.DOC_TYPE, dr.STATUS, dr.SEND_STATUS, dr.CREATE_USER_ID,P.USER_NAME ,dr.ATTACHNAME,dr.ATTACHID, dr.RUN_ID " +
                         " from oa_officialdoc_recv dr, PERSON P  " +
                         " where dr.CREATE_USER_ID = P.SEQ_ID  and dr.SEND_STATUS =1 and dr.SPONSOR=" + user.getDeptId() +" order by RES_DATE desc"; //" and dr.SEND_STATUS = 1 and dr.STATUS =" + status;
   
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<YHDocReceive> docs  = new ArrayList<YHDocReceive>();
    try{
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      YHDocReceiveLogic logic = new YHDocReceiveLogic();
      int cnt = 0;
      while(rs.next() && ++cnt <= 10){
        YHDocReceive doc = new YHDocReceive();
        doc.setSeq_id(rs.getInt("SEQ_ID"));
        doc.setDocNo(rs.getString("DOC_NO"));
        doc.setResDate(rs.getDate("RES_DATE"));
        doc.setFromUnits(rs.getString("FROMUNITS"));
        doc.setOppDocNo(rs.getString("OPPDOC_NO"));
        doc.setTitle(rs.getString("TITLE"));
        doc.setCopies(rs.getInt("COPIES"));
        doc.setConfLevel(rs.getInt("CONF_LEVEL"));
        doc.setInstruct(rs.getString("INSTRUCT"));
        doc.setSponsor(rs.getString("SPONSOR"));
        doc.setRecipient(rs.getString("RECE_USER_ID"));
        doc.setDocType(rs.getInt("DOC_TYPE"));
        doc.setStatus(rs.getInt("STATUS"));
        doc.setSendStauts(rs.getInt("SEND_STATUS"));
        doc.setUserId(rs.getInt("CREATE_USER_ID"));
        doc.setFromUserName(rs.getString("USER_NAME"));
        doc.setAttachNames(rs.getString("ATTACHNAME"));
        doc.setAttachIds(rs.getString("ATTACHID"));
        doc.setToUserName(user.getUserName());
        doc.setRunId(rs.getString("RUN_ID"));
        doc.setNext(logic.returnNext(conn, rs.getString("RUN_ID")));
        docs.add(doc);
      }
    } catch (SQLException e){
      throw e;
    }
    return list2Json(docs);
  }
  
  public String list2Json(List<YHDocReceive> docs){
    StringBuffer sb = new StringBuffer();
    if(docs == null || docs.size() == 0){
      return "[]";
    }
    sb.append("[");
    for(int i=0; i<docs.size(); i++){
      sb.append(docs.get(i).toJson());
      if(i < docs.size()-1){
        sb.append(",");
      }
    }
    sb.append("]");
    return sb.toString();
  }
}

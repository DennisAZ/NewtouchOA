package yh.core.funcs.doc.send.act;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.funcs.doc.send.data.YHSubjectTerm;
import yh.core.funcs.doc.send.logic.YHSubjectTermLogic;

public class YHWordTreeAct {
  public String getTree(HttpServletRequest request,HttpServletResponse response) throws Exception {
    String idStr = request.getParameter("id");
    int id = 0;
    if (idStr != null && !"".equals(idStr) && !"root".equals(idStr)) {
      id = Integer.parseInt(idStr);
    }
    Connection dbConn = null;
    Statement stmt = null;
    ResultSet rs = null;
    String queryStr = "SELECT * FROM oa_topic_term WHERE parent_id= " + id+" ORDER BY SORT_NO asc";
    try {
       YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       dbConn = requestDbConn.getSysDbConn();
       stmt = dbConn.createStatement();
       rs = stmt.executeQuery(queryStr);
       StringBuffer sb = new StringBuffer("[");
       if (id == 0 && !"root".equals(idStr)) {
  		   sb.append("{");
  			 sb.append("nodeId:\"" + "root" + "\"");
  			 sb.append(",name:\"" + "主题词(类别)列表" + "\"");
  			 sb.append(",isHaveChild:" + 1 + "");
  			 sb.append("}");
  			 sb.append("]");
       }else{
         ArrayList<YHSubjectTerm> words = new ArrayList<YHSubjectTerm>();
         while(rs.next()){
           YHSubjectTerm word = new YHSubjectTerm();
           word.setSeqId(rs.getInt("SEQ_ID"));
           word.setWord(rs.getString("WORD"));
           word.setTypeFlag(rs.getInt("type_flag"));
           words.add(word);
         }
         YHPerson person = (YHPerson) request.getSession().getAttribute("LOGIN_USER");
         String postDept = person.getPostDept();
         String postPriv = person.getPostPriv();
         YHPersonLogic dl = new YHPersonLogic();
         for (YHSubjectTerm d : words) {
           int nodeId = d.getSeqId();
           String deptId = String.valueOf(nodeId);
           String name = YHUtility.encodeSpecial(d.getWord());
           int isHaveChild = IsHaveChild(dbConn, d.getSeqId());
           String extData = "";
           if (person.isAdminRole() || postPriv.equals("1")) {
             extData = "isPriv";
           } else {
             if(dl.findId(postDept, deptId)) {
               extData = "isPriv";
             } else {
               extData = "";
             }
           }
           String imgAddress=null;
           if(d.getTypeFlag()==0){
             imgAddress = "/yh/core/styles/style1/img/dtree/folder.gif";
           }else{
             imgAddress = "/yh/core/styles/style1/img/dtree/file.jpg";
           }
           sb.append("{");
           sb.append("nodeId:\"" + nodeId + "\"");
           sb.append(",name:\"" + name + "\"");
           sb.append(",isHaveChild:" + isHaveChild + "");
           sb.append(",extData:\"" + extData + "\"");
           sb.append(",imgAddress:\"" + imgAddress + "\"");
           sb.append(",title:\"" + name + "\"");
           sb.append("},");
         }
         if (words.size() > 0) {
           sb.deleteCharAt(sb.length() - 1);
         }
         sb.append("]");
       }
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
       request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
       request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      ex.printStackTrace();
      throw ex;
    }finally {
        YHDBUtility.close(stmt, rs, null);
    }
    return "/core/inc/rtjson.jsp";
  }
  public int IsHaveChild(Connection conn, int id) throws Exception {
    Statement stm = null;
    ResultSet rs = null;
    try {
      String str = "SELECT * FROM oa_topic_term WHERE parent_id = " + id+" ORDER BY SORT_NO asc";
      stm = conn.createStatement();
      rs = stm.executeQuery(str);
      if (rs.next()) {
        return 1;
      } else {
        return 0;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
  }
  
  
  public String selectWordToAttendance(HttpServletRequest request,HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHSubjectTermLogic wordLogic = new YHSubjectTermLogic();
      String data = "";
      data = wordLogic.getWordTreeJson(0, dbConn);
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
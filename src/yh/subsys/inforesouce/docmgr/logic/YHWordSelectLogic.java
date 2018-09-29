package yh.subsys.inforesouce.docmgr.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.subsys.inforesouce.docmgr.data.YHSubjectTerm;

public class YHWordSelectLogic {
  private static Logger log = Logger.getLogger(YHWordSelectLogic.class);
  
  public List<YHSubjectTerm> getPersonsByDept(Connection conn, int deptId) throws Exception{
  	List<YHSubjectTerm> list = new ArrayList();
  	String query = "select SEQ_ID , WORD   from oa_topic_term where type_flag!=0 and PARENT_ID = " + deptId ;
  	Statement stm4 = null;
  	ResultSet rs4 = null;
  	Set set = new HashSet();
  	try {
  	  stm4 = conn.createStatement();
	    rs4 = stm4.executeQuery(query);
	    while (rs4.next()) {
  		  int seqId = rs4.getInt("SEQ_ID");
  		  String word = rs4.getString("WORD");
  		  if (!set.contains(seqId)) {
  		    YHSubjectTerm per = new YHSubjectTerm();
  		    per.setSeqId(seqId);
  		    per.setWord(word);
  		    list.add(per);
  		    set.add(seqId);
  		  }
	    }
  	}catch(Exception ex) {
  	  throw ex;
    }finally {
      YHDBUtility.close(stm4 , rs4 , null);
    }
    return  list;
  }
  
  public String getNameById(int deptId , Connection conn) throws Exception{
    String query = "select word from oa_topic_term where SEQ_ID=" + deptId;
  	Statement stmt = null;
  	ResultSet rs = null;
  	String deptName = "";
    try {
  		stmt = conn.createStatement();
  		rs = stmt.executeQuery(query);
  		if (rs.next()) {
  		  deptName = rs.getString("word");
  		}
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return deptName;
  }
  
  public StringBuffer getQueryUser2Json(Connection conn , String queryName) throws Exception{
    StringBuffer user = new StringBuffer();
    StringBuffer sb = new StringBuffer();
  	ArrayList<YHSubjectTerm> persons = getUserByName(conn, queryName);
  	for (int i = 0; i < persons.size(); i++) {
  	  YHSubjectTerm person = persons.get(i);
  	  String userName = person.getWord();
  	  sb.append(user2Json(conn,userName));
  	  if(!"".equals(sb.toString())){
  	    sb.append(",");
  	  }
  	}
  	if(sb.length()>0&&sb.charAt(sb.length() - 1) == ','){
      sb.deleteCharAt(sb.length() - 1);
    }
    user.append("[").append(sb).append("]");
    return user;
  }

  public ArrayList<YHSubjectTerm> getUserByName(Connection conn , String userName) throws Exception{
    String query = "select SEQ_ID , WORD  from oa_topic_term where type_flag=1 and"
    +" (WORD LIKE '%" + YHUtility.encodeLike(userName)  + "%' " + YHDBUtility.escapeLike()+")";
    ArrayList<YHSubjectTerm> persons = new ArrayList();
    Statement stm4 = null;
    ResultSet rs4 = null;
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      while (rs4.next()) {
        YHSubjectTerm person = new YHSubjectTerm();
        person.setSeqId(rs4.getInt("SEQ_ID"));
        person.setWord(rs4.getString("WORD"));
        persons.add(person);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm4 , rs4 , null);
    }
    return persons;
  }

  public StringBuffer user2Json(Connection conn ,String userName) throws Exception{
    StringBuffer sb = new StringBuffer();
    sb.append("\"").append(YHUtility.encodeSpecial(userName)).append("\"");
    return sb;
  }
}

package yh.subsys.oa.bbs.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.interfaces.data.YHSysPara;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHBbsLogic {

  public String getBbsUrlLogic(Connection conn,YHPerson person)throws Exception{
    Statement stmt=null;
    ResultSet rs=null;
    String data="";
    try{
      String sql="select * from sys_para where para_name='bbs_url'";
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      String url="";
      if(rs.next()){
        url=rs.getString("para_value");
      }
      if(YHUtility.isNullorEmpty(url)){
        url="";
      }
      data="{url:'"+url+"',userId:'"+person.getUserId()+"',userName:'"+person.getUserName()+"',email:'"+person.getEmail()+"',birth:'"+person.getBirthday()+"',role:'"+person.getUserPriv()+"'}";
    }catch(Exception ex){
      ex.printStackTrace();
    }
    finally{
      YHDBUtility.close(stmt, rs, null);
    }
    
    return data;
  }
  
  
  
  public void saveBbsUrlLogic(Connection conn,String url)throws Exception{
    Statement stmt=null;
    ResultSet rs=null;
    String data="";
    try{
      YHORM orm=new YHORM();
      YHSysPara param=new YHSysPara(); 
      String sql="select * from sys_para where para_name='bbs_url'";
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      if(rs.next()){
       param.setSeqId(rs.getInt("seq_id"));
       param.setParaName(rs.getString("para_name"));
       param.setParaValue(url);
       orm.updateSingle(conn, param);
      }
      param.setParaName("bbs_url");
      param.setParaValue(url);
      orm.saveSingle(conn, param);
     }catch(Exception ex){
      ex.printStackTrace();
    }
    finally{
      YHDBUtility.close(stmt, rs, null);
    }
    
  
  }
  
}

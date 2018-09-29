package yh.subsys.oa.vmeet.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;


import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.system.interfaces.data.YHSysPara;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHDigestUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.vmeet.data.YHVMeet;

public class YHVMeetLogic {
  
   public void addVMeetInfoLogic(Connection dbConn,YHPerson person,String inviteUsers,String content)throws Exception{
     YHORM orm=new YHORM();
     
     
     String VMEET=YHDigestUtility.md5Hex((person.getSeqId()+"").getBytes());
     VMEET=VMEET.substring(0, 8);
     FastDateFormat format=FastDateFormat.getInstance("yyyyMMddHHmmss");
     String VT=YHUtility.getCurDateTimeStr(format);
     String VCK=YHDigestUtility.md5Hex(getKeyed_str(VT,"TD_VMEET").getBytes());
     VCK=VCK.substring(0, 4);
     
    String users[]=inviteUsers.split(",");

     YHVMeet meet=new YHVMeet();
     meet.setVmeet(VMEET);
     meet.setVt(VT);
     meet.setVck(VCK);
     meet.setBeginUser(person.getSeqId()+"");
     meet.setInviteUsers(inviteUsers);
     meet.setContent(content);
     String ss=YHUtility.getDateTimeStr(new Date());
     meet.setAddTime(ss);
     orm.saveSingle(dbConn, meet);
     
     String id=this.getMaxSeqId(dbConn);
     for(int i=0;i<users.length;i++){
       String user=users[i];
       if(!"".equals(user)){
           YHSmsBack sb = new YHSmsBack();
           sb.setSmsType("0");
           sb.setContent(content);
           String url="/subsys/oa/vmeet/checkUser.jsp?seqId="+id; 
           sb.setRemindUrl(url);
           sb.setToId(user);
           sb.setFromId(person.getSeqId());
           YHSmsUtil.smsBack(dbConn, sb);
       }
     }
     
   }
   
   
   public void editUsersLogic(Connection dbConn,YHPerson person,String inviteUsers,String content,String seqId)throws Exception{
     YHORM orm=new YHORM();
     YHVMeet meet=(YHVMeet)orm.loadObjSingle(dbConn, YHVMeet.class, Integer.parseInt(seqId));
     String addUsers=meet.getInviteUsers();
     if(addUsers.endsWith(",")){
       addUsers+=inviteUsers;
     }else{
       addUsers+=","+inviteUsers;
     }
     meet.setInviteUsers(addUsers);
     orm.updateSingle(dbConn, meet);
     String users[]=inviteUsers.split(",");
     String id=seqId;
     for(int i=0;i<users.length;i++){
       String user=users[i];
       if(!"".equals(user)){
           YHSmsBack sb = new YHSmsBack();
           sb.setSmsType("6");
           sb.setContent(content);
           String url="/subsys/oa/vmeet/checkUser.jsp?seqId="+id; 
           sb.setRemindUrl(url);
           sb.setToId(user);
           sb.setFromId(person.getSeqId());
           YHSmsUtil.smsBack(dbConn, sb);
       }
     }
     
   }
   
   public String getMaxSeqId(Connection conn)throws Exception{
     Statement stmt=null;
     ResultSet rs=null;
     String data="";
     try{
       String sql=" select max(seq_id) from oa_vmeet ";
       stmt=conn.createStatement();
       rs=stmt.executeQuery(sql);
       if(rs.next()){
         data=rs.getInt(1)+"";
       }
     }catch(Exception ex){
       ex.printStackTrace();
     }finally{
       YHDBUtility.close(stmt, rs, null);
     }
     
     return data;
   }
   
   public String getKeyed_str(String txt ,String encrypt){
     String tmp="";
     encrypt=YHDigestUtility.md5Hex(encrypt.getBytes());
     tmp+=txt+encrypt;
     tmp=YHDigestUtility.md5Hex(tmp.getBytes());
     return tmp;
   }
   
   public String getVMeetPriv(Connection dbConn,YHPerson person)throws Exception{
     Statement stmt=null;
     ResultSet rs=null;
     String data="";
    try{
      String sql="select * from SYS_PARA where "+YHDBUtility.findInSet("VMEET_NEW_PRIV", "para_name");
     stmt=dbConn.createStatement();
     rs=stmt.executeQuery(sql);
      if(rs.next()){
        data=rs.getString("para_value");
      }
      String hasPriv="0";
      if(!YHUtility.isNullorEmpty(data) && data.indexOf(person.getSeqId()+"")!=-1){
        hasPriv="1";
      }
      data="{id:'"+data+"',name:'"+this.getUserNames(dbConn,data)+"',userPriv:'"+person.getUserPriv()+"',hasPriv:'"+hasPriv+"',curUser:'"+person.getUserName()+"'}";
      
    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
     return data;
   }
   
   public void setVMeetPrivLogic(Connection dbConn,String toIds)throws Exception{
     Statement stmt=null;
     ResultSet rs=null;
     int seqId=0;
    try{
     String sql="select * from SYS_PARA where "+YHDBUtility.findInSet("VMEET_NEW_PRIV", "para_name");
     stmt=dbConn.createStatement();
     rs=stmt.executeQuery(sql);
     YHORM orm=new YHORM();
      if(rs.next()){
        seqId=rs.getInt("seq_id");
        YHSysPara sp=(YHSysPara) orm.loadObjComplex(dbConn, YHSysPara.class, seqId);
        sp.setParaValue(toIds);
        orm.updateSingle(dbConn, sp);
      }else{
        YHSysPara sp=new YHSysPara();
        sp.setParaName("VMEET_NEW_PRIV");
        sp.setParaValue(toIds);
        orm.saveSingle(dbConn, sp);
      }
    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
   }
   
   
   public String getUserNames(Connection conn,String userIds)throws Exception{
     Statement stmt=null;
     ResultSet rs=null;
     String data="";
     if(YHUtility.isNullorEmpty(userIds)){
       return "";
     }
     try{
       String users[]=userIds.split(",");
       for(int i=0;i<users.length;i++){
           String user=users[i];
         if(!YHUtility.isNullorEmpty(user)){
         
           String sql=" select user_name from person where seq_id='"+user+"' ";
           stmt=conn.createStatement();
           rs=stmt.executeQuery(sql);
           if(rs.next()){
             data+=rs.getString("user_name");
             data+=",";
           }
         
         }
       }
     }catch(Exception ex){
       ex.printStackTrace();
     }finally{
       YHDBUtility.close(stmt, rs, null);
     }
     if(data.endsWith(",")){
       data=data.substring(0, data.length()-1);
     }
     return data;
   }
   
   public String getLastBeginMeet(Connection dbConn,YHPerson person)throws Exception{
     Statement stmt=null;
     ResultSet rs=null;
     String data="";
    try{
     String sql="select * from oa_vmeet where "+YHDBUtility.findInSet(person.getSeqId()+"", "begin_user") +" order by add_time desc ";
     stmt=dbConn.createStatement();
     rs=stmt.executeQuery(sql);
      while(rs.next()){
        String content=rs.getString("content");
        String add_time=rs.getString("add_time");
        int seqId=rs.getInt("seq_id");
        data+="{";
        data+=" seqId:'"+seqId+"',content:'"+content+"',addTime:'"+add_time+"' ";
        data+="},";
      }
     
    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    if(data.endsWith(",")){
      data=data.substring(0, data.length()-1);
    }
     return data;
   }
   
   public String getVMeetByIdLogic(Connection dbConn,YHPerson person,String seq_id)throws Exception{
     Statement stmt=null;
     ResultSet rs=null;
     String data="";
    try{
     String sql="select * from oa_vmeet where seq_id='"+seq_id+"'";
     stmt=dbConn.createStatement();
     rs=stmt.executeQuery(sql);
      if(rs.next()){
        String vmeet=rs.getString("vmeet");  
        String beginUser=rs.getString("begin_user");
        String role="3";
        if(beginUser.trim().equals(person.getSeqId()+"")){
          role="2";
        }
        data+="{";
        data+=" pass:'1',roomId:'"+vmeet+"',userName:'"+person.getUserName()+"',role:'"+role+"'";
        data+="}";
      }
     
    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    if(data.endsWith(",")){
      data=data.substring(0, data.length()-1);
    }
    if("".equals(data)){
      data="{pass:'0'}";
    }
     return data;
   }
   
   
   public void deleteVMeet(Connection dbConn,String seqId)throws Exception{
     YHORM orm=new YHORM();
     
    try{
     orm.deleteSingle(dbConn, YHVMeet.class, Integer.parseInt(seqId));
     
    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
    
    }
   }
  
   
   
   public String getLastInvitedMeet(Connection dbConn,YHPerson person)throws Exception{
     Statement stmt=null;
     ResultSet rs=null;
     String data="";
    try{
     String sql="select * from oa_vmeet where "+YHDBUtility.findInSet(person.getSeqId()+"", "invite_users")+" order by add_time desc ";
     stmt=dbConn.createStatement();
     rs=stmt.executeQuery(sql);
      while(rs.next()){
        String content=rs.getString("content");
        String add_time=rs.getString("add_time");
        int seqId=rs.getInt("seq_id");
        data+="{";
        data+=" seqId:'"+seqId+"',content:'"+content+"',addTime:'"+add_time+"' ";
        data+="},";
      }
     
    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    if(data.endsWith(",")){
      data=data.substring(0, data.length()-1);
    }
     return data;
   }
   
   
}

package yh.subsys.internationalOrg.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.internationalOrg.logic.PinYin;
import yh.subsys.internationalOrg.logic.YHGetFirstLetter;
import yh.subsys.internationalOrg.data.YHInternationalOrgLanguage;
import yh.subsys.internationalOrg.data.YHInternationalOrgMain;
import yh.subsys.internationalOrg.data.YHInternationalOrgShow;
import yh.subsys.internationalOrg.data.YHInternationalOrgSubject;
import yh.subsys.internationalOrg.data.YHInternationalOrgTypei;
import yh.subsys.internationalOrg.data.YHInternationalOrgTypeii;

public class YHInternationalOrgLogic {
  private static Logger log = Logger
  .getLogger(" yh.subsys.internationalOrg.logic.YHInternationalOrgLogic");
  
  public int getMaSeqId(Connection dbConn,String tableName)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    int maxSeqId = 0;
    String sql = "select max(SEQ_ID) as SEQ_ID from " + tableName;
    try{
     stmt = dbConn.createStatement();
     rs = stmt.executeQuery(sql);
     if(rs.next()){
       maxSeqId = rs.getInt("SEQ_ID");
     }
      
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return maxSeqId;
  }
  
  public List<YHInternationalOrgShow> selectOrgById(Connection dbConn, String[] str) throws Exception {
    YHORM orm = new YHORM();
    List<YHInternationalOrgShow> list = new ArrayList<YHInternationalOrgShow>();
    list = orm.loadListSingle(dbConn, YHInternationalOrgShow.class, str);
    return list;
  }
  
  public List<YHInternationalOrgMain> selectMainById(Connection dbConn, String[] str) throws Exception {
    YHORM orm = new YHORM();
    List<YHInternationalOrgMain> list = new ArrayList<YHInternationalOrgMain>();
    list = orm.loadListSingle(dbConn, YHInternationalOrgMain.class, str);
    return list;
  }
  
  //saveLanguage
  public void saveLanguage(Connection dbConn,String language,String mainId,YHInternationalOrgLanguage lan)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "select * from international_org_language where language = '" + language + "'";
    try{
     stmt = dbConn.createStatement();
     rs = stmt.executeQuery(sql);
     if(rs.next()){
       updateAny(dbConn,rs.getString("seq_id"),mainId,"international_org_language");
     }else{
       YHORM orm = new YHORM();
       lan.setLanguage(language);
       lan.setMainId(mainId);
       orm.saveSingle(dbConn, lan);
     } 
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
//saveTypei
  public void saveTypei(Connection dbConn,String typeiName,String mainId,YHInternationalOrgTypei tp1)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "select * from international_org_typei where typei_name = '" + typeiName + "'";
    try{
     stmt = dbConn.createStatement();
     rs = stmt.executeQuery(sql);
     if(rs.next()){
       updateAny(dbConn,rs.getString("seq_id"),mainId,"international_org_typei");
     }else{
       YHORM orm = new YHORM();
       tp1.setTypeiName(typeiName);
       tp1.setMainId(mainId);
       orm.saveSingle(dbConn, tp1);
     } 
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
//saveTypei
  public void saveTypeii(Connection dbConn,String typeiiName,String mainId,YHInternationalOrgTypeii tp2)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "select * from international_org_typeii where typeii_name = '" + typeiiName + "'";
    try{
     stmt = dbConn.createStatement();
     rs = stmt.executeQuery(sql);
     if(rs.next()){
       updateAny(dbConn,rs.getString("seq_id"),mainId,"international_org_typeii");
     }else{
       YHORM orm = new YHORM();
       tp2.setTypeiiName(typeiiName);
       tp2.setMainId(mainId);
       orm.saveSingle(dbConn, tp2);
     } 
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  public static String RemoveRepeat(String[] strRepeat)
  {
      ArrayList<String> list = new ArrayList<String>();//定义一个动态数组list用来装数组的元素
      for (String str : strRepeat)//foreach循环出 数组的元素
      {
          if (list.contains(str) == false && !YHUtility.isNullorEmpty(str))//list.Contains(str)判断list中是否有相同的元素，list.Contains(str) == false当不同时为true
              list.add(str);//把数组中的不相同元素添加到list中
      }
      String ids = "";
      for(String s : list){
        ids += s + ",";
      }
      if(ids.endsWith(",")){
        ids = ids.substring(0, ids.length()-1);
      }
      return ids;//返回list，list中的值就是所要得到的结果
  }
  
  public String toSearchData(Connection dbConn,Map request,String language,String subjecti,String subjectii,String city,String country,String year,String meetingName,String level,String typei,String typeii,String name) throws Exception{
    String events = getEventsSeqIds(dbConn,city,country,year,meetingName);
    //System.out.println(events);
    String subjects = getSubjectSeqIds(dbConn,subjecti,subjectii);
    String typeis = getTypeiSeqIds(dbConn,typei);
    String typeiis = getTypeiiSeqIds(dbConn,typeii);
    String sql =  "select " +
    "SEQ_ID" +
    ",SHOW_ID" +
    ",name" +
    ",star_level" +
    " from " +
    "international_org_main where 1=1";
    
    if (!YHUtility.isNullorEmpty(language)) {
      //language = language.replaceAll(" ", "");
      sql+= " and REGEXP_LIKE(language, '"+YHDBUtility.escapeLike(language)+"', 'i')" ;
      //sql += " and language like '%" + YHDBUtility.escapeLike(language) + "%' " + YHDBUtility.escapeLike(); 
    }
    if (!YHUtility.isNullorEmpty(name)) {
      //language = language.replaceAll(" ", "");
      sql += " and REGEXP_LIKE(name, '" + YHDBUtility.escapeLike(name) + "','i')"; 
    }
    if (!YHUtility.isNullorEmpty(level)) {
      sql += " and star_level ='" + level +"'"; 
    }
    if (!events.equals("kong")) {
      
      String[] ids = events.split(",");
      String[] list = RemoveRepeat(ids).split(",");
      //System.out.println(list.length);
      String idStr = "";
      if(list.length>1000){
        int num = list.length/1000;
        sql += " and (";
        for(int i = 0;i<num;i++){
          String idss = "";
          for(int j=i*1000; j < (i+1)*1000;j++){
            
            idss += "'" + list[j] +"'" + ","; 
          }
          //System.out.println(idss);
          if(!idss.equals("")){
            idss = idss.substring(0, idss.length()-1);
          }
          if(i == 0){
            sql += " seq_id in ("+ idss +")"; 
          }else{
            sql += " or seq_id in ("+ idss +")"; 
          }
          
        }
        for(int x=num*1000; x < list.length;x++){
          idStr += "'" + list[x] + "'"+ ","; 
        }
        if(!idStr.equals("")){
          idStr = idStr.substring(0, idStr.length()-1);
        }
        sql += " or seq_id in ("+ idStr +")"; 
        sql += " )";
      }else{
        for(String id : list){
          idStr += "'" + id + "'" + ","; 
        }
        if(!idStr.equals("")){
          idStr = idStr.substring(0, idStr.length()-1);
        }
        sql += " and seq_id in ("+ idStr +")"; 
      }
    }
    if (!subjects.equals("kong")) {
      
      String[] ids = subjects.split(",");
      String[] list = RemoveRepeat(ids).split(",");
      //System.out.println(list.length);
      String idStr = "";
      if(list.length>1000){
        int num = list.length/1000;
        sql += " and (";
        for(int i = 0;i<num;i++){
          String idss = "";
          for(int j=i*1000; j < (i+1)*1000;j++){
            
            idss += "'" + list[j] +"'" + ","; 
          }
          //System.out.println(idss);
          if(!idss.equals("")){
            idss = idss.substring(0, idss.length()-1);
          }
          if(i == 0){
            sql += " seq_id in ("+ idss +")"; 
          }else{
            sql += " or seq_id in ("+ idss +")"; 
          }
          
        }
        for(int x=num*1000; x < list.length;x++){
          idStr += "'" + list[x] + "'"+ ","; 
        }
        if(!idStr.equals("")){
          idStr = idStr.substring(0, idStr.length()-1);
        }
        sql += " or seq_id in ("+ idStr +")"; 
        sql += " )";
      }else{
        for(String id : list){
          idStr += "'" + id + "'" + ","; 
        }
        if(!idStr.equals("")){
          idStr = idStr.substring(0, idStr.length()-1);
        }
        sql += " and seq_id in ("+ idStr +")"; 
      }
    }
    if(!typeis.equals("kong")){
      
      String[] ids = typeis.split(",");
      String[] list = RemoveRepeat(ids).split(",");
      //System.out.println(list.length);
      String idStr = "";
      if(list.length>1000){
        int num = list.length/1000;
        sql += " and (";
        for(int i = 0;i<num;i++){
          String idss = "";
          for(int j=i*1000; j < (i+1)*1000;j++){
            
            idss += "'" + list[j] +"'" + ","; 
          }
          //System.out.println(idss);
          if(!idss.equals("")){
            idss = idss.substring(0, idss.length()-1);
          }
          if(i == 0){
            sql += " seq_id in ("+ idss +")"; 
          }else{
            sql += " or seq_id in ("+ idss +")"; 
          }
          
        }
        for(int x=num*1000; x < list.length;x++){
          idStr += "'" + list[x] + "'"+ ","; 
        }
        if(!idStr.equals("")){
          idStr = idStr.substring(0, idStr.length()-1);
        }
        sql += " or seq_id in ("+ idStr +")"; 
        sql += " )";
      }else{
        for(String id : list){
          idStr += "'" + id + "'" + ","; 
        }
        if(!idStr.equals("")){
          idStr = idStr.substring(0, idStr.length()-1);
        }
        sql += " and seq_id in ("+ idStr +")"; 
      }
      
    }
    if(!typeiis.equals("kong")){
      
      String[] ids = typeiis.split(",");
      String[] list = RemoveRepeat(ids).split(",");
      //System.out.println(list.length);
      String idStr = "";
      if(list.length>1000){
        int num = list.length/1000;
        sql += " and (";
        for(int i = 0;i<num;i++){
          String idss = "";
          for(int j=i*1000; j < (i+1)*1000;j++){
            
            idss += "'" + list[j] +"'" + ","; 
          }
          //System.out.println(idss);
          if(!idss.equals("")){
            idss = idss.substring(0, idss.length()-1);
          }
          if(i == 0){
            sql += " seq_id in ("+ idss +")"; 
          }else{
            sql += " or seq_id in ("+ idss +")"; 
          }
          
        }
        for(int x=num*1000; x < list.length;x++){
          idStr += "'" + list[x] + "'"+ ","; 
        }
        if(!idStr.equals("")){
          idStr = idStr.substring(0, idStr.length()-1);
        }
        sql += " or seq_id in ("+ idStr +")"; 
        sql += " )";
      }else{
        for(String id : list){
          idStr += "'" + id + "'" + ","; 
        }
        if(!idStr.equals("")){
          idStr = idStr.substring(0, idStr.length()-1);
        }
        sql += " and seq_id in ("+ idStr +")"; 
      }
      
    }
    sql += " order by star_level desc,seq_id asc"; 
    //System.out.println(sql);
    
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  
  //saveSubjectii
  public void saveSubjectii(Connection dbConn,String subjecti,String subjectii,String mainId,YHInternationalOrgSubject sub)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "select * from international_org_subject where subjectii = '" + subjectii + "'";
    try{
      //System.out.println(sql);
     stmt = dbConn.createStatement();
     rs = stmt.executeQuery(sql);
     if(rs.next()){
       updateAny(dbConn,rs.getString("seq_id"),mainId,"international_org_subject");
     }else{
       YHORM orm = new YHORM();
       sub.setMainId(mainId);
       sub.setSubjecti(subjecti);
       sub.setSubjectii(subjectii);
       orm.saveSingle(dbConn, sub);
     } 
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  public int checkSubjecti(Connection dbConn,String subjecti,String mainId)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    int subjectiNum = 0;
    String sql = "select * from international_org_subject where subjecti = '" + subjecti + "'";
    try{
     stmt = dbConn.createStatement();
     rs = stmt.executeQuery(sql);
     if(rs.next()){
       subjectiNum = 0;
     }else{
       subjectiNum = 1;
     } 
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return subjectiNum;
  }
  
  public String getLevel(Connection dbConn,String seqId)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String level = "0";
    String sql = "select * from international_org_main where seq_id = '" + seqId + "'";
    try{
     stmt = dbConn.createStatement();
     rs = stmt.executeQuery(sql);
     if(rs.next()){
       level = rs.getString("star_level");
     }
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return level;
  }
  
  public String getEventsSeqIds(Connection dbConn,String city,String country,String year,String meetingName)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String seqIds = "";
    String sql = "select main_id from international_org_events where 1=1";
    if (!YHUtility.isNullorEmpty(meetingName)) {
     // meetingName = meetingName.replaceAll(" ", "");
      sql += " and REGEXP_LIKE(meeting_name , '" + YHDBUtility.escapeLike(meetingName) + "','i')";
    }
    if (!YHUtility.isNullorEmpty(city)) {
      //city = city.replaceAll(" ", "");
      sql += " and REGEXP_LIKE(city , '" + YHDBUtility.escapeLike(city) + "','i')";
    }
    if (!YHUtility.isNullorEmpty(country)) {
      //country = country.replaceAll(" ", "");
      sql += " and REGEXP_LIKE(country , '" + YHDBUtility.escapeLike(country) + "','i')";
    }
    if (!YHUtility.isNullorEmpty(year)) {
      //year = year.replaceAll(" ", "");
      sql += " and REGEXP_LIKE(year, '" + YHDBUtility.escapeLike(year) + "' ,'i')";
    }
    if(YHUtility.isNullorEmpty(meetingName)&&YHUtility.isNullorEmpty(year)&&YHUtility.isNullorEmpty(city)&&YHUtility.isNullorEmpty(country)){
      sql = "select main_id from international_org_events where 1=2";
      seqIds = "kong";
    }
    //System.out.println(sql);
    try{
     stmt = dbConn.createStatement();
     rs = stmt.executeQuery(sql);
     while(rs.next()){
       seqIds += rs.getString("main_id") + ",";
     }
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return seqIds;
  }
  
  public String getSubjectSeqIds(Connection dbConn,String subjecti,String subjectii)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String seqIds = "";
    String sql = "select main_id from international_org_subject where 1=1";
    if (!YHUtility.isNullorEmpty(subjecti)) {
      sql += " and subjecti = '" + subjecti + "' ";
    }
    if (!YHUtility.isNullorEmpty(subjectii)) {
      sql += " and subjectii = '" + subjectii + "' ";
    }
    if(YHUtility.isNullorEmpty(subjecti)&&YHUtility.isNullorEmpty(subjectii)){
      sql = "select main_id from international_org_subject where 1=2";
      seqIds = "kong";
    }
    //System.out.println(sql);
    try{
     stmt = dbConn.createStatement();
     rs = stmt.executeQuery(sql);
     while(rs.next()){
       seqIds += rs.getString("main_id") + ",";
     }
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return seqIds;
  }
  
  public String getTypeiSeqIds(Connection dbConn,String typei)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String seqIds = "";
    String sql = "select main_id from international_org_typei where 1=1";
    if (!YHUtility.isNullorEmpty(typei)) {
      typei = typei.substring(0,3);
      sql += " and typei_name like '%" + YHDBUtility.escapeLike(typei) + "%' " + YHDBUtility.escapeLike();
    }else{
      sql = "select main_id from international_org_typei where 1=2";
      seqIds = "kong";
    }
    //System.out.println(sql);
    try{
     stmt = dbConn.createStatement();
     rs = stmt.executeQuery(sql);
     while(rs.next()){
       seqIds += rs.getString("main_id") + ",";
     }
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return seqIds;
  }
  
  public String getTypeiiSeqIds(Connection dbConn,String typeii)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String seqIds = "";
    String sql = "select main_id from international_org_typeii where 1=1";
    if (!YHUtility.isNullorEmpty(typeii)) {
      typeii = typeii.substring(0,3);
      sql += " and typeii_name like '%" + YHDBUtility.escapeLike(typeii) + "%' " + YHDBUtility.escapeLike();
    }else{
      sql = "select main_id from international_org_typei where 1=2";
      seqIds = "kong";
    }
    //System.out.println(sql);
    try{
     stmt = dbConn.createStatement();
     rs = stmt.executeQuery(sql);
     while(rs.next()){
       seqIds += rs.getString("main_id") + ",";
     }
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return seqIds;
  }
  
  /**
   * 
   * @return 
   */
  public static void updateAny(Connection dbConn,String seqId,String mainId,String tableName)throws Exception{
    String newId = ","+ mainId; 
    String sql = "update "+tableName+" set main_id = concat(main_id,'"+newId+"') where seq_id = " + seqId;
    Statement stmt = null;
    ResultSet rs = null;
   

    //System.out.println(sql);
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);

    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  public void setLevel(Connection dbConn,String level,String seqId)throws Exception{
    
    String sql = "update international_org_main set star_level = '"+level+"' where seq_id = " + seqId;
    //System.out.println(sql);
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);

    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  public String getSubjectiLogic(Connection conn)throws Exception{
    String data="";
    YHGetFirstLetter GFL=new YHGetFirstLetter();
    PinYin py = new PinYin();
    
    Statement stmt=null;
    ResultSet rs=null;
    String[] Letter = {"A", "B", "C", "D", "E", "F", "G", "H","I","J","K", "L", "M", "N", "O", "P", "Q", "R", "S","T","U","V","W", "X", "Y", "Z"};
   
    try{
     for(int i=0;i<Letter.length;i++){
        String sql=" select distinct(subjecti) from international_org_subject order by subjecti ";
        stmt=conn.createStatement();
        rs=stmt.executeQuery(sql);
         data+="{FL:'"+Letter[i]+"',name:[";
        while(rs.next()){
          String subjecti=rs.getString("subjecti");
     
          String let=py.chinese2PinYin(subjecti);
          
          if(!YHUtility.isNullorEmpty(let)){
            let=let.substring(0, 1).toUpperCase();
          }else{
            let="A";
          }
         
          String mark=Letter[i];
          if(let.equals(mark)){
            data+="{";
            data+="subjecti:'"+subjecti+"'";
            data+="},";
          }
        }
        if(data.endsWith(",")){
          data=data.substring(0, data.length()-1);
        }
       data+="]},";
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
  
  public String getSubjectiiLogic(Connection conn)throws Exception{
    String data="";
    YHGetFirstLetter GFL=new YHGetFirstLetter();
    PinYin py = new PinYin();
    
    Statement stmt=null;
    ResultSet rs=null;
    String[] Letter = {"A", "B", "C", "D", "E", "F", "G", "H","I","J","K", "L", "M", "N", "O", "P", "Q", "R", "S","T","U","V","W", "X", "Y", "Z"};
   
    try{
     for(int i=0;i<Letter.length;i++){
        String sql=" select * from international_org_subject order by subjectii ";
        stmt=conn.createStatement();
        rs=stmt.executeQuery(sql);
         data+="{FL:'"+Letter[i]+"',name:[";
        while(rs.next()){
          String subjectii=rs.getString("subjectii");
     
          String let=py.chinese2PinYin(subjectii);
          
          if(!YHUtility.isNullorEmpty(let)){
            let=let.substring(0, 1).toUpperCase();
          }else{
            let="A";
          }
         
          String mark=Letter[i];
          if(let.equals(mark)){
            data+="{";
            data+="subjectii:'"+subjectii+"'";
            data+="},";
          }
        }
        if(data.endsWith(",")){
          data=data.substring(0, data.length()-1);
        }
       data+="]},";
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
  
  public String getSubjectiiBySubjectiLogic(Connection conn,String subjecti)throws Exception{
    String data="";
    YHGetFirstLetter GFL=new YHGetFirstLetter();
    PinYin py = new PinYin();
    
    Statement stmt=null;
    ResultSet rs=null;
    String[] Letter = {"A", "B", "C", "D", "E", "F", "G", "H","I","J","K", "L", "M", "N", "O", "P", "Q", "R", "S","T","U","V","W", "X", "Y", "Z"};
   
    try{
     for(int i=0;i<Letter.length;i++){
        String sql=" select * from international_org_subject where subjecti = '"+subjecti+"' order by subjectii ";
        stmt=conn.createStatement();
        rs=stmt.executeQuery(sql);
         data+="{FL:'"+Letter[i]+"',name:[";
        while(rs.next()){
          String subjectii=rs.getString("subjectii");
     
          String let=py.chinese2PinYin(subjectii);
          
          if(!YHUtility.isNullorEmpty(let)){
            let=let.substring(0, 1).toUpperCase();
          }else{
            let="A";
          }
         
          String mark=Letter[i];
          if(let.equals(mark)){
            data+="{";
            data+="subjectii:'"+subjectii+"'";
            data+="},";
          }
        }
        if(data.endsWith(",")){
          data=data.substring(0, data.length()-1);
        }
       data+="]},";
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

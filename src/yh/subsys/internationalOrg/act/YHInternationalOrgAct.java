package yh.subsys.internationalOrg.act;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.internationalOrg.data.YHInternationalOrgEvents;
import yh.subsys.internationalOrg.data.YHInternationalOrgMain;
import yh.subsys.internationalOrg.data.YHInternationalOrgMainCn;
import yh.subsys.internationalOrg.data.YHInternationalOrgShow;
import yh.subsys.internationalOrg.data.YHInternationalOrgLanguage;
import yh.subsys.internationalOrg.data.YHInternationalOrgShowCn;
import yh.subsys.internationalOrg.data.YHInternationalOrgSubject;
import yh.subsys.internationalOrg.data.YHInternationalOrgTypei;
import yh.subsys.internationalOrg.data.YHInternationalOrgTypeii;
import yh.subsys.internationalOrg.logic.YHInternationalOrgLogic;

public class YHInternationalOrgAct {
  YHInternationalOrgLogic logic =  new YHInternationalOrgLogic();
  public String test(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    request.setCharacterEncoding("utf-8");
    Connection dbConn = null;
    
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Elements name = null;
      Elements nameOther = null;
      Elements madder = null;
      Elements history = null;
      Elements subject = null;
      Elements aims = null;
      Elements structure = null;
      Elements languages = null;
      Elements staff = null;
      Elements finances = null;
      Elements infoservices = null;
      Elements constatus = null;
      Elements igorel = null;
      Elements ngorel = null;
      Elements activities = null;
      Elements events = null;
      Elements publications = null;
      Elements members = null;
      Elements typei = null;
      Elements typeii = null;
      Elements datelastnews = null;
      File files = new File("F:/international/");
      String[] fileNames = files.list();
      int num = 0;
      for(String fileName : fileNames){
        num++;
        System.out.println("正在转化第"+num+"个网页。");
        File input = new File("F:/international/"+fileName);
        Document doc = Jsoup.parse(input, "utf-8", "http://example.com/");
        YHInternationalOrgShow org = new YHInternationalOrgShow();
        YHInternationalOrgMain main = new YHInternationalOrgMain();
        YHInternationalOrgLanguage lan = new YHInternationalOrgLanguage();
        YHInternationalOrgSubject sub = new YHInternationalOrgSubject();
        YHInternationalOrgEvents eve = new YHInternationalOrgEvents();
        YHInternationalOrgTypei tp1 = new YHInternationalOrgTypei();
        YHInternationalOrgTypeii tp2 = new YHInternationalOrgTypeii();
        YHORM orm = new YHORM();
        if(doc.getElementsByClass("views-field-nameabb") != null){
          name = doc.getElementsByClass("views-field-nameabb");
          org.setName(name.html().replaceAll("• ", ""));
          main.setName(name.text());
        }
        if(doc.getElementsByClass("views-field-nameabbother") != null){
          nameOther = doc.getElementsByClass("views-field-nameabbother");
          org.setNameOther(nameOther.html().replaceAll("• ", ""));
        }
        if(doc.getElementsByClass("views-field-maddr") != null){
          madder = doc.getElementsByClass("views-field-maddr");
          org.setMadder(madder.html().replaceAll("• ", ""));
        }
        if(doc.getElementsByClass("views-field-founded") != null){
          history = doc.getElementsByClass("views-field-founded");
          org.setHistory(history.html().replaceAll("• ", ""));
        }
        if(doc.getElementsByClass("views-field-aims") != null){
          aims = doc.getElementsByClass("views-field-aims");
          org.setAims(aims.html().replaceAll("• ", ""));
        }
        if(doc.getElementsByClass("views-field-structure") != null){
          structure = doc.getElementsByClass("views-field-structure");
          org.setStructure(structure.html().replaceAll("• ", ""));
        }
        if(doc.getElementsByClass("views-field-languages") != null){
          languages = doc.getElementsByClass("views-field-languages");
          org.setLanguages(languages.html().replaceAll("• ", ""));
        }
        if(doc.getElementsByClass("views-field-languages") != null){
          languages = doc.select("div.views-field-languages div");  
          main.setLanguage(languages.text());
        }
        if(doc.getElementsByClass("views-field-staff") != null){
          staff = doc.getElementsByClass("views-field-staff");
          org.setStaff(staff.html().replaceAll("• ", ""));
        }
        if(doc.getElementsByClass("views-field-finances") != null){
          finances = doc.getElementsByClass("views-field-finances");
          org.setFinances(finances.html().replaceAll("• ", ""));
        }
        if(doc.getElementsByClass("views-field-finances") != null){
          finances = doc.select("div.views-field-finances div");
          main.setFinances(finances.text());
        }
        if(doc.getElementsByClass("views-field-constatus") != null){
          constatus = doc.getElementsByClass("views-field-constatus");
          org.setConstatus(constatus.html().replaceAll("• ", ""));
        }
        if(doc.getElementsByClass("views-field-constatus") != null){
          constatus = doc.select("div.views-field-constatus div");
          main.setConstatus(constatus.text());
        }
        if(doc.getElementsByClass("views-field-igorel") != null){
          igorel = doc.getElementsByClass("views-field-igorel");
          String text = igorel.html().replaceAll("• ", "");
          org.setIgorel(text);
        }
        if(doc.getElementsByClass("views-field-igorel") != null){
          igorel = doc.select("div.views-field-igorel div");
          main.setIgorel(igorel.text());
        }
        if(doc.getElementsByClass("views-field-ngorel") != null){
          ngorel = doc.getElementsByClass("views-field-ngorel");
          String text = ngorel.html().replaceAll("• ", "");
          org.setNgorel(text);
        }
        if(doc.getElementsByClass("views-field-ngorel") != null){
          ngorel = doc.select("div.views-field-ngorel div");
          main.setNgorel(ngorel.text());
        }
        if(doc.getElementsByClass("views-field-activities") != null){
          activities = doc.getElementsByClass("views-field-activities");
          org.setActivities(activities.html().replaceAll("• ", ""));
        }
        if(doc.getElementsByClass("views-field-activities") != null){
          activities = doc.select("div.views-field-activities div");
          main.setActivities(activities.text());
        }
        if(doc.getElementsByClass("views-field-events") != null){
          events = doc.getElementsByClass("views-field-events");
          org.setEvents(events.html().replaceAll("• ", ""));
        }
        if(doc.getElementsByClass("views-field-events") != null){
          events = doc.select("div.views-field-events div");
          main.setEvents(events.text());
        }
        if(doc.getElementsByClass("views-field-publications") != null){
          publications = doc.getElementsByClass("views-field-publications");
          org.setPublications(publications.html().replaceAll("• ", ""));
        }
        if(doc.getElementsByClass("views-field-infoservices") != null){
          infoservices = doc.getElementsByClass("views-field-infoservices");
          org.setInfoservices(infoservices.html().replaceAll("• ", ""));
        }
        if(doc.getElementsByClass("views-field-wcode") != null){
          subject = doc.getElementsByClass("views-field-wcode");
          org.setSubject(subject.html().replaceAll("• ", ""));
        }
        if(doc.getElementsByClass("views-field-wcode") != null){
          subject = doc.select("div.views-field-wcode div");
          main.setSubject(subject.text());
        }
        if(doc.getElementsByClass("views-field-members") != null){
          members = doc.getElementsByClass("views-field-members");
          String text = members.html().replaceAll("• ", "");
          org.setMembers(text);
        }
        if(doc.getElementsByClass("views-field-typei-entry") != null){
          typei = doc.getElementsByClass("views-field-typei-entry");
          org.setTypei(typei.html().replaceAll("• ", ""));
        }
        if(doc.getElementsByClass("views-field-typeii-entry") != null){
          typeii = doc.getElementsByClass("views-field-typeii-entry");
          org.setTypeii(typeii.html().replaceAll("• ", ""));
        }
        if(doc.getElementsByClass("views-field-datelastnews") != null){
          datelastnews = doc.getElementsByClass("views-field-datelastnews");
          org.setDatelastnews(datelastnews.html().replaceAll("• ", ""));
        }
        orm.saveSingle(dbConn, org);    
        int showId = logic.getMaSeqId(dbConn, "international_org_show");
        main.setShowId(showId);
        orm.saveSingle(dbConn, main);
        String mainId = logic.getMaSeqId(dbConn, "international_org_main")+"";
        //工作语言分类
        if(doc.getElementsByClass("views-field-languages") != null){
          languages = doc.select("div.views-field-languages div");
          if(!YHUtility.isNullorEmpty(languages.text())){
            String[] languageStr = languages.text().replaceAll(";", ",").replaceAll(".", ",").substring(0, languages.text().length()-1).split(",");
            for(String language : languageStr){
              
              logic.saveLanguage(dbConn,language.trim(),mainId,lan);
            }
          }
        }      
        
        //组织类型i
        if(doc.getElementsByClass("views-field-typei-entry") != null){
          typei = doc.select("div.views-field-typei-entry ul");
          Elements typeis = typei.select("li");
          if(typeis!=null){
            for(Element typeiOne : typeis){
              
              logic.saveTypei(dbConn,typeiOne.text().trim(),mainId,tp1);
            }
          }
        }      
        
      //组织类型ii
        if(doc.getElementsByClass("views-field-typeii-entry") != null){
          typeii = doc.select("div.views-field-typeii-entry ul");
          Elements typeiis = typeii.select("li");
          if(typeiis!=null){
            for(Element typeiiOne : typeiis){
              
              logic.saveTypeii(dbConn,typeiiOne.text().trim(),mainId,tp2);
            }
          }
        }      
        
        //组织分类
        if(doc.getElementsByClass("views-field-wcode") != null){
          Element test = doc.select("div.views-field-wcode div ul").first();
          if(test!=null){
            String[] uls = test.html().substring(0, test.html().length()-5).split("</ul>");
            for(String ul : uls){
              Document ulDoc = Jsoup.parse(ul);
              Element subjecti = ulDoc.select("li").first();
              int subjectiNum = logic.checkSubjecti(dbConn,subjecti.text(),mainId);
              Elements subjectiiAll = ulDoc.select("ul");
              if(subjectiNum == 1){
                for(Element subjectii : subjectiiAll.select("li")){
                  sub.setMainId(mainId);
                  sub.setSubjecti(subjecti.text());
                  sub.setSubjectii(subjectii.text());
                  orm.saveSingle(dbConn, sub);
                } 
              }else{
                for(Element subjectii : subjectiiAll.select("li")){
                  logic.saveSubjectii(dbConn,subjecti.text().replaceAll("'", ""),subjectii.text().replaceAll("'", ""),mainId,sub);
                }  
              }
            }
          }
        }   
        
        //主要会议举办地点
        if(doc.getElementsByClass("views-field-events") != null){
          Element test = doc.select("div.views-field-events div ul li").first();
          if(test!=null){         
            String[] evs = test.html().substring(0, test.html().length()-1).split("\\.");           
            for(String ev : evs){
              Document meet = Jsoup.parse(ev);
              Elements meetingName = meet.select("i"); 
              int index = ev.indexOf("</i>");
              String content = ev.substring(index+5);
              Document con = Jsoup.parse(content);
              String[] contents = con.text().split(",");
              for(String contentOne : contents){
                if(contentOne.indexOf("(")>0){
                  String city = contentOne.substring(0,contentOne.indexOf("(")).trim();
                  String country = contentOne.substring(contentOne.indexOf("(")+1,contentOne.indexOf(")")).trim();
                  String year = contentOne.substring(contentOne.indexOf(")")+1).trim();
                  eve.setCity(city);
                  eve.setCountry(country);
                  eve.setYear(year);
                  eve.setMainId(mainId);
                  eve.setMeetingName(meetingName.text());
                  orm.saveSingle(dbConn, eve);
                }else{
                   Pattern pattern = Pattern.compile("[0-9]");  
                   Matcher matcher = pattern.matcher(contentOne.trim()); 
                   if (matcher.find()) { 
                     String country = contentOne.trim().substring(0,contentOne.trim().indexOf(matcher.group())).trim();
                     String year = contentOne.trim().substring(contentOne.trim().indexOf(matcher.group())).trim();
                     eve.setCountry(country);
                     eve.setYear(year);
                     eve.setMainId(mainId);
                     eve.setMeetingName(meetingName.text());
                     orm.saveSingle(dbConn, eve);
                   }
                }
              }
            }
          }
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      //System.out.println(111);
      String language = request.getParameter("language");
      String subjecti = request.getParameter("subjecti");
      //System.out.println(subjecti);
      String subjectii = request.getParameter("subjectii");
      String city = request.getParameter("city");
      String country = request.getParameter("country");
      String year = request.getParameter("year");
      String meetingName = request.getParameter("meetingName");
      String name = request.getParameter("name");
      String typei = request.getParameter("typei");
      String typeii = request.getParameter("typeii");
      String level = request.getParameter("level");
      //System.out.println("language:"+language+","+city);
      String data =logic.toSearchData(dbConn, request.getParameterMap(),language,subjecti,subjectii,city,country,year,meetingName,level,typei,typeii,name);
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
   
   public String getDetails(HttpServletRequest request,
       HttpServletResponse response) throws Exception {
     Connection dbConn = null;
     try {
       YHRequestDbConn requestDbConn = (YHRequestDbConn) request
           .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       dbConn = requestDbConn.getSysDbConn();
       
       String seqId = request.getParameter("seqId") == null ? "" :  request.getParameter("seqId") ;
       String data = "[";
       String[] str = {"seq_id=" + seqId};
       List<YHInternationalOrgShow> list = logic.selectOrgById(dbConn, str);
       for (int i = 0; i < list.size(); i++) {
         data = data + YHFOM.toJson(list.get(i)) + ",";
       }
       if(!data.equals("[")){
         data = data.substring(0, data.length()-1);
       }
       data = data + "]";
       
       //System.out.println(data);
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
   
   public String getLevel(HttpServletRequest request,
       HttpServletResponse response) throws Exception {
     Connection dbConn = null;
     try {
       YHRequestDbConn requestDbConn = (YHRequestDbConn) request
           .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       dbConn = requestDbConn.getSysDbConn();
       
       String seqId = request.getParameter("seqId") == null ? "" :  request.getParameter("seqId") ;
       String data = logic.getLevel(dbConn,seqId);
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
   
   public String setLevel(HttpServletRequest request,
       HttpServletResponse response) throws Exception {
     Connection dbConn = null;
     try {
       YHRequestDbConn requestDbConn = (YHRequestDbConn) request
           .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       dbConn = requestDbConn.getSysDbConn();
       
       String seqId = request.getParameter("seqId") == null ? "" :  request.getParameter("seqId") ;
       String level = request.getParameter("level") == null ? "0" :  request.getParameter("level") ;
       logic.setLevel(dbConn,level,seqId);
       //request.setAttribute(YHActionKeys.RET_DATA, data);
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
       request.setAttribute(YHActionKeys.RET_MSRG, "评星成功！");
     } catch (Exception e) {
       e.printStackTrace();
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
       request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
     }
     return "/core/inc/rtjson.jsp";
   }
   
   public String getSubjectAct(HttpServletRequest request,
       HttpServletResponse response) throws Exception {
     YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
     try{
       YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       Connection dbConn = requestDbConn.getSysDbConn();
       String subjectType = request.getParameter("type");
       String subjecti = request.getParameter("subjecti");
       //System.out.println(subjectType);
       String data = "";
       if(subjectType.equals("1")){
         data=logic.getSubjectiLogic(dbConn);
       }
       if(subjectType.equals("2")){
         data=logic.getSubjectiiLogic(dbConn);
       }
       if(subjectType.equals("3")){
         data=logic.getSubjectiiBySubjectiLogic(dbConn,subjecti);
       }
       data="{data:["+data+"]}";
       //System.out.println(data);
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
       request.setAttribute(YHActionKeys.RET_DATA, data);
     } catch(Exception e){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
       request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
       throw e;
     }
     return "/core/inc/rtjson.jsp";
   }
   
   public String englishTochina(HttpServletRequest request,
       HttpServletResponse response) throws Exception {
     request.setCharacterEncoding("utf-8");
     Connection dbConn = null;
     
     try {
       YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       dbConn = requestDbConn.getSysDbConn();
       TranslateUtil translante = new TranslateUtil();
       Elements name = null;
       Elements nameOther = null;
       Elements madder = null;
       Elements history = null;
       Elements subject = null;
       Elements aims = null;
       Elements structure = null;
       Elements languages = null;
       Elements staff = null;
       Elements finances = null;
       Elements infoservices = null;
       Elements constatus = null;
       Elements igorel = null;
       Elements ngorel = null;
       Elements activities = null;
       Elements events = null;
       Elements publications = null;
       Elements members = null;
       Elements typei = null;
       Elements typeii = null;
       Elements datelastnews = null;
       File files = new File("F:/internationalCn/");
       String[] fileNames = files.list();
       int num = 0;
       for(String fileName : fileNames){
         num++;
         System.out.println("正在转化第"+num+"个网页。");
         File input = new File("F:/internationalCn/"+fileName);
         Document doc = Jsoup.parse(input, "utf-8", "http://example.com/");
         YHInternationalOrgShowCn org = new YHInternationalOrgShowCn();
         YHInternationalOrgMainCn main = new YHInternationalOrgMainCn();

         YHORM orm = new YHORM();
         if(doc.getElementsByClass("views-field-nameabb") != null){
           name = doc.getElementsByClass("views-field-nameabb");
           System.out.println(name.html().replaceAll("• ", ""));
           org.setName(translante.en2cn(name.html().replaceAll("• ", "")));
           //main.setName(name.text());
         }
         if(doc.getElementsByClass("views-field-nameabbother") != null){
           nameOther = doc.getElementsByClass("views-field-nameabbother");
           org.setNameOther(translante.en2cn(nameOther.html().replaceAll("• ", "")));
         }
         if(doc.getElementsByClass("views-field-maddr") != null){
           madder = doc.getElementsByClass("views-field-maddr");
           org.setMadder(translante.en2cn(madder.html().replaceAll("• ", "")));
         }
         if(doc.getElementsByClass("views-field-founded") != null){
           history = doc.getElementsByClass("views-field-founded");
           org.setHistory(translante.en2cn(history.html().replaceAll("• ", "")));
         }
         if(doc.getElementsByClass("views-field-aims") != null){
           aims = doc.getElementsByClass("views-field-aims");
           org.setAims(translante.en2cn(aims.html().replaceAll("• ", "")));
         }
         if(doc.getElementsByClass("views-field-structure") != null){
           structure = doc.getElementsByClass("views-field-structure");
           org.setStructure(translante.en2cn(structure.html().replaceAll("• ", "")));
         }
         if(doc.getElementsByClass("views-field-languages") != null){
           languages = doc.getElementsByClass("views-field-languages");
           org.setLanguages(translante.en2cn(languages.html().replaceAll("• ", "")));
         }
//         if(doc.getElementsByClass("views-field-languages") != null){
//           languages = doc.select("div.views-field-languages div");  
//           main.setLanguage(languages.text());
//         }
         if(doc.getElementsByClass("views-field-staff") != null){
           staff = doc.getElementsByClass("views-field-staff");
           org.setStaff(translante.en2cn(staff.html().replaceAll("• ", "")));
         }
         if(doc.getElementsByClass("views-field-finances") != null){
           finances = doc.getElementsByClass("views-field-finances");
           org.setFinances(translante.en2cn(finances.html().replaceAll("• ", "")));
         }
//         if(doc.getElementsByClass("views-field-finances") != null){
//           finances = doc.select("div.views-field-finances div");
//           main.setFinances(finances.text());
//         }
         if(doc.getElementsByClass("views-field-constatus") != null){
           constatus = doc.getElementsByClass("views-field-constatus");
           org.setConstatus(translante.en2cn(constatus.html().replaceAll("• ", "")));
         }
//         if(doc.getElementsByClass("views-field-constatus") != null){
//           constatus = doc.select("div.views-field-constatus div");
//           main.setConstatus(constatus.text());
//         }
         if(doc.getElementsByClass("views-field-igorel") != null){
           igorel = doc.getElementsByClass("views-field-igorel");
           String text = igorel.html().replaceAll("• ", "");
           org.setIgorel(translante.en2cn(text));
         }
//         if(doc.getElementsByClass("views-field-igorel") != null){
//           igorel = doc.select("div.views-field-igorel div");
//           main.setIgorel(igorel.text());
//         }
         if(doc.getElementsByClass("views-field-ngorel") != null){
           ngorel = doc.getElementsByClass("views-field-ngorel");
           String text = ngorel.html().replaceAll("• ", "");
           org.setNgorel(translante.en2cn(text));
         }
//         if(doc.getElementsByClass("views-field-ngorel") != null){
//           ngorel = doc.select("div.views-field-ngorel div");
//           main.setNgorel(ngorel.text());
//         }
         if(doc.getElementsByClass("views-field-activities") != null){
           activities = doc.getElementsByClass("views-field-activities");
           org.setActivities(translante.en2cn(activities.html().replaceAll("• ", "")));
         }
//         if(doc.getElementsByClass("views-field-activities") != null){
//           activities = doc.select("div.views-field-activities div");
//           main.setActivities(activities.text());
//         }
         if(doc.getElementsByClass("views-field-events") != null){
           events = doc.getElementsByClass("views-field-events");
           org.setEvents(translante.en2cn(events.html().replaceAll("• ", "")));
         }
//         if(doc.getElementsByClass("views-field-events") != null){
//           events = doc.select("div.views-field-events div");
//           main.setEvents(events.text());
//         }
         if(doc.getElementsByClass("views-field-publications") != null){
           publications = doc.getElementsByClass("views-field-publications");
           org.setPublications(translante.en2cn(publications.html().replaceAll("• ", "")));
         }
         if(doc.getElementsByClass("views-field-infoservices") != null){
           infoservices = doc.getElementsByClass("views-field-infoservices");
           org.setInfoservices(translante.en2cn(infoservices.html().replaceAll("• ", "")));
         }
         if(doc.getElementsByClass("views-field-wcode") != null){
           subject = doc.getElementsByClass("views-field-wcode");
           org.setSubject(translante.en2cn(subject.html().replaceAll("• ", "")));
         }
//         if(doc.getElementsByClass("views-field-wcode") != null){
//           subject = doc.select("div.views-field-wcode div");
//           main.setSubject(subject.text());
//         }
         if(doc.getElementsByClass("views-field-members") != null){
           members = doc.getElementsByClass("views-field-members");
           String text = members.html().replaceAll("• ", "");
           org.setMembers(translante.en2cn(text));
         }
         if(doc.getElementsByClass("views-field-typei-entry") != null){
           typei = doc.getElementsByClass("views-field-typei-entry");
           org.setTypei(translante.en2cn(typei.html().replaceAll("• ", "")));
         }
         if(doc.getElementsByClass("views-field-typeii-entry") != null){
           typeii = doc.getElementsByClass("views-field-typeii-entry");
           org.setTypeii(translante.en2cn(typeii.html().replaceAll("• ", "")));
         }
         if(doc.getElementsByClass("views-field-datelastnews") != null){
           datelastnews = doc.getElementsByClass("views-field-datelastnews");
           org.setDatelastnews(translante.en2cn(datelastnews.html().replaceAll("• ", "")));
         }
         orm.saveSingle(dbConn, org);    
//         int showId = logic.getMaSeqId(dbConn, "international_org_show");
//         main.setShowId(showId);
//         orm.saveSingle(dbConn, main);
//         String mainId = logic.getMaSeqId(dbConn, "international_org_main")+"";

       }
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
       request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
     }catch(Exception ex) {
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
       request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
       throw ex;
     }
     return "/core/inc/rtjson.jsp";
   }

}

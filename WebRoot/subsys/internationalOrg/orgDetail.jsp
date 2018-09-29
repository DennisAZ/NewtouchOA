<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%
String seqId = request.getParameter("seqId") == null ? "0" : request.getParameter("seqId");
String language = request.getParameter("language") == null ? "":request.getParameter("language");
String name = request.getParameter("name") == null ? "":request.getParameter("name");
String meetingName = request.getParameter("meetingName") == null ? "":request.getParameter("meetingName");
String city = request.getParameter("city") == null ? "":request.getParameter("city");
String country = request.getParameter("country") == null ? "":request.getParameter("country");
String year = request.getParameter("year") == null ? "":request.getParameter("year");
String typei = request.getParameter("typei") == null ? "":request.getParameter("typei");
String typeii = request.getParameter("typeii") == null ? "":request.getParameter("typeii");
String subjecti = request.getParameter("subjecti") == null ? "":request.getParameter("subjecti");
String subjectii = request.getParameter("subjectii") == null ? "":request.getParameter("subjectii");
%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/internationalOrg/js/internationalOrg.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/internationalOrg/js/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<style type="text/css">
input.BigButton,
input.BigButtonA,
input.BigButtonB,
input.BigButtonC,
input.BigButtonD,
input.BigButtonW,
input.BigButtonE {
  border: none;
  color: #36434E;
  font-size: 13px;
  margin: 0px;
  height: 26px;
  width: 130px;
  text-decoration: none;
  vertical-align: middle;
}

input.BigButtonB, input.BigButton {
  background: url('<%=imgPath %>/subpages/big_btn_b.png') no-repeat top left;
  width : 89px;
}
</style>
<script type="text/javascript">
var seqId = '<%=seqId%>';
var languageKey = '<%=language%>';
var nameKey = '<%=name%>';
var meetingNameKey = '<%=meetingName%>';
var cityKey = '<%=city%>';
var countryKey = '<%=country%>';
var yearKey = '<%=year%>';
var seqId = '<%=seqId%>';
var typeiKey = '<%=typei%>';
var typeiiKey = '<%=typeii%>';
var subjectiKey = '<%=subjecti%>';
var subjectiiKey = '<%=subjectii%>';

//if(childs[0].nodeType === 3){
//  var newDom = childs[0].replace(new RegExp(keyWords,"gim"),"<strong><font color = 'red'>"+keyWords+"</font></strong>");
//  jQuery(newDom).replace(childs[0]);
//}

function getKeyWordsRed(str,keyWords){
  var text;
  jQuery("#languages fieldset>div").each(function(){
    text = $(this).text();
    text = text.replace(new RegExp(keyWords,"gim"),"<strong><font color = 'red'>"+keyWords+"</font></strong>");
    text = "<div>"+text+"</div>";
    $(this).html(text);
    });

}

function getKeyWordsRedName(str,keyWords){
  var text;
  jQuery("#name span>span").each(function(){
    text = $(this).text();
    text = text.replace(new RegExp(keyWords,"gim"),"<strong><font color = 'red'>"+keyWords+"</font></strong>");
    text = text+"<sup><font color=grey style='font-size: x-small;'></font></sup>";
    $(this).html(text);
    });

}

function getKeyWordsRed1(str,keyWords){
  
  str = str.replace(new RegExp(">"+keyWords,"gim"),">"+"####start####"+keyWords+"####end####").replace(new RegExp("####start####","gim"),"<strong><font color=\"#FF0000\">").replace(new RegExp("####end####","gim"),"</font></strong>");    
  //alert(str);
  return str;
}

function getKeyWordsRedYear(str,keyWords){
  
  str = str.replace(new RegExp(keyWords,"gim"),"####start####"+keyWords+"####end####").replace(new RegExp("####start####","gim"),"<strong><font color=\"#FF0000\">").replace(new RegExp("####end####","gim"),"</font></strong>");    
  //alert(str);
  return str;
}

function getKeyWordsRed2(str,keyWords){
  keyWords = keyWords.substring(0,3);
  str = str.replace(new RegExp(">"+keyWords,"gim"),">"+"####start####"+keyWords+"####end####").replace(new RegExp("####start####","gim"),"<strong><font color=\"#FF0000\">").replace(new RegExp("####end####","gim"),"</font></strong>");    
  //alert(str);
  return str;
}

function replaceAll(str1,str2)
{
var newValue = str.replace(new RegExp(str1,"gim"),str2);
alert(newValue);
return newValue;
}

function doInit(){
  
    var url = "<%=contextPath%>/yh/subsys/internationalOrg/act/YHInternationalOrgAct/getDetails.act?seqId="+seqId; 
    var rtJson = getJsonRs(url); 
    //alert(rsText);
    if (rtJson.rtState == "0") {
      if(rtJson.rtData.length>0){
        for(var i =0;i<rtJson.rtData.length;i++){
	        var prc = rtJson.rtData[0];
	        
	        var name = prc.name;
	        document.getElementById("name").innerHTML = name;
	        if(nameKey!=""){
	          name = getKeyWordsRedName(name,nameKey);
          }
	        var madder = prc.madder;
	        var nameOther = prc.nameOther;
	        var history = prc.history;
	        //alert(history);
	        var languages = prc.languages;
	        document.getElementById("languages").innerHTML = languages;
	        if(languageKey!=""){
	          languages = getKeyWordsRed(languages,languageKey);
	        }
	        //alert(languages);
	        var aims = prc.aims;
	        var structure = prc.structure;
	        var staff = prc.staff;
	        var finances = prc.finances;
	        var constatus = prc.constatus;
	        var igorel = prc.igorel;
	        var ngorel = prc.ngorel;
	        var activities = prc.activities;
	        var events = prc.events;
	        if(meetingNameKey!=""){
	          events = getKeyWordsRedYear(events,meetingNameKey);
          }
	        if(cityKey!=""){
	          events = getKeyWordsRed1(events,cityKey);
          }
          //alert(events);
	        if(countryKey!=""){
	          events = getKeyWordsRed1(events,countryKey);
          }
	        if(yearKey!=""){
	          events = getKeyWordsRedYear(events,yearKey);
          }
	        var publications = prc.publications;
	        var infoservices = prc.infoservices;
	        var members = prc.members;
	        var subject = prc.subject;
	        if(subjectiKey!=""){
	          subject = getKeyWordsRed1(subject,subjectiKey);
          }
	        if(subjectiiKey!=""){
            subject = getKeyWordsRed1(subject,subjectiiKey);
          }
	        var typei = prc.typei;
	        if(typeiKey!=""){
	          typei = getKeyWordsRed2(typei,typeiKey);
          }
          //alert(typei);
	        var typeii = prc.typeii;
	        if(typeiiKey!=""){
            typeii = getKeyWordsRed2(typeii,typeiiKey);
          }
	        var datelastnews = prc.datelastnews;
	        
	        //document.getElementById("name").innerHTML = name;
	        document.getElementById("madder").innerHTML = madder;
	        document.getElementById("nameOther").innerHTML = nameOther;
	        document.getElementById("history").innerHTML = history;
	       
	        document.getElementById("aims").innerHTML = aims;
	        //document.getElementById("languages").innerHTML = languages;
	        document.getElementById("structure").innerHTML = structure;   
	        document.getElementById("staff").innerHTML = staff;
	        document.getElementById("finances").innerHTML = finances;
	        document.getElementById("constatus").innerHTML = constatus;
	        document.getElementById("igorel").innerHTML = igorel;
	        document.getElementById("ngorel").innerHTML = ngorel;
	        document.getElementById("activities").innerHTML = activities;
	        document.getElementById("events").innerHTML = events;
	        document.getElementById("publications").innerHTML = publications;
	        document.getElementById("infoservices").innerHTML = infoservices;
	        document.getElementById("members").innerHTML = members;
	        document.getElementById("subject").innerHTML = subject;
	        document.getElementById("typei").innerHTML = typei;
	        document.getElementById("typeii").innerHTML = typeii;
	        document.getElementById("datelastnews").innerHTML = datelastnews;
	        
        }
      }
      
    } else{  
      alert(rtJson.rtMsrg);
    } 
}
</script>
</head>
<body id="pixture-reloaded" onLoad = "doInit()" ">


    <div id="page" style="width: 100%;">
 
    <div id="main" class="clear-block with-header-blocks">

      <div id="content"><div id="content-inner">

      <table class="views-view-grid col-1">
  <tbody>
                <tr class="row-1 row-first row-last">
                  <td class="col-1 col-first">
              
  <div class="views-field-nameabb" id = "name">
               
  </div>
  
  <div class="views-field-nameabbother" id = "nameOther">
               
  </div>
  
  <div class="views-field-maddr" id = "madder">
  
  </div>
  
  <div class="views-field-founded" id = "history">
                
  </div>
  
  <div class="views-field-aims" id = "aims">
               
  </div>
  
  <div class="views-field-structure" id = "structure" >
                
  </div>
  
  <div class="views-field-languages" id = "languages">
               
  </div>
  
  <div class="views-field-staff" id = "staff">
                
  </div>
  
  <div class="views-field-finances" id = "finances">
                
  </div>
  
  <div class="views-field-constatus" id = "constatus">
               
  </div>
  
  <div class="views-field-igorel" id = "igorel">
               
  </div>
  
  <div class="views-field-ngorel" id = "ngorel">
                
  </div>
  
  <div class="views-field-activities" id = "activities">
                
  </div>
  
  <div class="views-field-events" id= "events">
              
  </div>
  
  <div class="views-field-publications" id = "publications">
               
  </div>
  
  <div class="views-field-infoservices" id= "infoservices">
                
  </div>
  
  <div class="views-field-members" id = "members">
                
  </div>
  
  <div class="views-field-wcode" id = "subject">
                
  </div>
  
  <div class="views-field-typei-entry" id= "typei">
                
  </div>
  
  <div class="views-field-typeii-entry" id = "typeii">
                
  </div>
  
  <div class="views-field-datelastnews" id = "datelastnews">
                
  </div>
          </td>
              </tr>
      </tbody>
</table>
    </div>
  
  
  </div>
  </div>
  </div>

<div>
<table style = "width:100%;">
<tr >
  <td align = "center">
    <input type="button" value="返回" class="BigButton" onClick="javascript:history.go(-1);">
  </td>
</tr>
</table>
</div>


</body>
</html>
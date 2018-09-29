<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Date"%>
<%
String projId = request.getParameter("projId") == null ? "0" : request.getParameter("projId");
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String dayTime = sf.format(new Date());
YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
int userId = user.getSeqId();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>标题</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" type="text/css" href="css/candidate.css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/subsys/internationalOrg/js/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/internationalOrg/js/jquery.jscrollpane.min.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/internationalOrg/js/jquery.mousewheel.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/jquery/jquery.blockUI.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/internationalOrg/js/internationalOrg.js"></script>
<script type="text/javascript">
var projId = '<%=projId%>';
var userId = '<%=userId%>';
var pageMgr = null;

function doInit() {
  
  selectManage();
  //jQuery("#candidate").jScrollPane();
}

function selectManage() {
  
  pYxTotal = 0;
  pAllTotal = 0;
  var url =  contextPath + "/yh/subsys/internationalOrg/act/YHInternationalOrgAct/getList.act";
   cfgs = {
    dataAction: url,
    container: "listDiv",
    paramFunc: getParam,
    colums: [
       {type:"data", name:"seqId", text:"ID", width: "5%",align:"center"},
       {type:"hidden", name:"showId", dataType:"int"},
       {type:"data",name:"name",text:"组织名称",align:"left",render:link},
       {type:"data", width: "10%",text:"评星", align:"center",render:getNumLevel}
       ]
  };
   pageMgr = new YHJsPage(cfgs);
   pageMgr.show();
   var total = pageMgr.pageInfo.totalRecord;
   if(total>0){
   }else{
     $("listDiv").style.display = "none";
     var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
         + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件信息!</div></td></tr>"
         );
     $('returnNull').update(table); 
   }
 }

function centerFunc(cellData, recordIndex, columIndex){
  return "<center>" + cellData + "</center>";
}

function getNumLevel(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<div id="+seqId+" style='float:center;'><a href='javascript:optRender("+seqId+")'>" + cellData + "星</a></div>";
}

function link(cellData, recordIndex, columIndex){
  var showId = this.getCellData(recordIndex,"showId");
  return "<a href='#' onclick='goLink("+showId+")'>" + cellData + "</a>";
}

function goLink(showId){
  var language = $("language").value;
  var name = $("name").value;
  var meetingName = $("meetingName").value;
  var city = $("city").value;
  var country = $("country").value;
  var year = $("year").value;
  var typei = $("typei").value;
  var typeii = $("typeii").value;
  var subjecti = $("subjecti").value;
  var subjectii = $("subjectii").value;
  var url = contextPath + "/subsys/internationalOrg/orgDetail.jsp?seqId=" + showId +"&language=" + language + "&meetingName=" + meetingName + "&city=" + city + "&country="+country + "&year=" + year + "&typei=" + typei + "&typeii=" + typeii + "&subjecti=" + subjecti + "&subjectii=" + subjectii + "&name="+name;
  //alert(url);
  window.location.href = url;
}


function toCheckForum(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<input type='checkbox'  id='email_select' name='email_select' value='" + seqId + "' onClick='check_one(self);'>";
}

function deleteAll(){
  var idStrs = get_checked().trim(); 
  if(!idStrs) { 
    alert("要删除记录，请至少选择其中一个。"); 
   return; 
  } 
	if(idStrs){
		idStrs = idStrs.substr(0,idStrs.length -1);
	}
	var msg = "确定要删除所选的记录吗？删除后不可恢复！";
  if(window.confirm(msg)) {
    var url =  contextPath + "/yh/subsys/organizationDept/proj/act/YHProjForumAct/deleteField.act?seqId=" + idStrs;
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
     alert(rtJson.rtMsrg);
      location.reload();
    }else{
      alert(rtJson.rtMsrg);
    }
  }
}
function toSub(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<a href='javascript:toSubject("+seqId+")'>" +cellData+ "</a>";
}
function WarningMsrg(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"360\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  $(cntrlId).innerHTML = msrgDom;
}


//列表控制
function getTotal(){
  var table = pageMgr.getDataTableDom();
  insertTr(table);
}

//表单数据
function getParam(){
  var queryParam = $("form1").serialize();
  return queryParam;
}

//选择框

function optRender(seqId){

  var oldLevel = getLevel(seqId);
  var imgId; 
  var html = "";
  for(var i = 0;i<5;i++){
    imgId = i +1;
    var imgaaa = (oldLevel>i) ? "/xing-a.jpg" : "/xing.jpg" ;

    html += "<div id="+seqId+"div"+imgId+" style='float:left;'><a  href='javascript:setLevel("+seqId+","+imgId+","+oldLevel+")'><img  src="+imgPath+""+imgaaa+" ></img></a></div>";

  }
 
  document.getElementById(seqId).innerHTML = html;
}

/**
 * 获得星级
 */
function getLevel(seqId){
  var url = "<%=contextPath%>/yh/subsys/internationalOrg/act/YHInternationalOrgAct/getLevel.act"; 
  var rtJson = getJsonRs(url, "seqId="  + seqId); 
  if (rtJson.rtState == "0") {
    var prc = rtJson.rtData;
    return prc;
  } else{  
    alert(rtJson.rtMsrg);
  } 
  return null;
}

/**
 * 评星级

 */
function setLevel(seqId,nowLevel,oldLevel){
  var url = "<%=contextPath%>/yh/subsys/internationalOrg/act/YHInternationalOrgAct/setLevel.act?level="+nowLevel; 
  var rtJson = getJsonRs(url, "seqId="  + seqId); 
  if(rtJson.rtState == "0"){
    //alert(rtJson.rtMsrg);
    for(var i = 1;i<=nowLevel;i++){
      imgId = i ;
      var imgaaa =  "/xing-a.jpg" ;

      document.getElementById(seqId+"div"+i).innerHTML = "<a  href='javascript:setLevel("+seqId+","+imgId+","+nowLevel+")'><img  src="+imgPath+""+imgaaa+" ></img></a>";
    }
      for(var i = nowLevel+1;i<=5;i++){
        imgId = i ;
        var imgaaa =  "/xing.jpg" ;

        document.getElementById(seqId+"div"+i).innerHTML = "<a  href='javascript:setLevel("+seqId+","+imgId+","+nowLevel+")'><img  src="+imgPath+""+imgaaa+" ></img></a>";
      }
    //location.reload();
  }else{
    alert(rtJson.rtMsrg);
  }
}



function checkForm(){
  if((!Number($("year").value))&&($("year").value!="")){
    alert("年份必须为数字！"); 
    $("year").focus(); 
    $("year").select(); 
    return false; 
  }
  if(($("year").value.length<2)&&($("year").value!="")){
    alert("最少输入两位数字！"); 
    $("year").focus(); 
    $("year").select(); 
    return false; 
  }
  return true;
}

function clearInput(input){
  $(input).value = "";
}

//确定查询
function getGroup(){
  if(checkForm()){
    if(!pageMgr){
      pageMgr = new YHJsPage(cfgs);
      pageMgr.show();
    } else {
      pageMgr.search();
    }
    var total = pageMgr.pageInfo.totalRecord;
    if(total>0){
      $("listDiv").style.display = "";
      $('returnNull').style.display = "none";
     }else{
       $("listDiv").style.display = "none";
       var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
           + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件的组织!</div></td></tr>"
           );
       $('returnNull').style.display = "";
       $('returnNull').update(table); 
     }
  }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<div><h2><img src="<%=imgPath%>/notify_new.gif"></img>国际组织</h2></div>
<form id="form1" name="form1">
<table align="center" class="TableBlock" >
	
  <tr>
      <td nowrap class="TableHeader">组织名称： </td>
        <td class="TableData" >
        <input class="SmallInput" type="hidden" name="meetingName" id="meetingName" value="">
          <input class="SmallInput" type="text" name="name" id="name" value="">
        </td>

      <td nowrap class="TableHeader">星级： </td>
      <td class="TableData">
        <select name="level" id="level">
          <option value = "">请选择星级</option>
          <option value = "0">零星</option>
          <option value = "1">一星</option>
          <option value = "2">两星</option>
          <option value = "3">三星</option>
          <option value = "4">四星</option>
          <option value = "5">五星</option>
        </select>
      </td>
      <td nowrap class="TableHeader">组织类型I： </td>
      <td class="TableData" colspan="3">
        <select name="typei" id="typei">
          <option value = "">请选择类型I</option>
          <option value = "A: Federations of international organizations">A: Federations of international organizations</option>
          <option value = "B: Universal membership organizations">B: Universal membership organizations</option>
          <option value = "C: Intercontinental membership organizations">C: Intercontinental membership organizations</option>
          <option value = "D: Regionally defined membership organizations">D: Regionally defined membership organizations</option>
          <option value = "E: Organizations emanating from places, persons, proprietary products or other bodies">E: Organizations emanating from places, persons, proprietary products or other bodies</option>
          <option value = "F: Organizations having a special form, including foundations and funds">F: Organizations having a special form, including foundations and funds</option>
          <option value = "G: Internationally-oriented national organizations">G: Internationally-oriented national organizations</option>
          <option value = "H: Inactive or dissolved international organizations">H: Inactive or dissolved international organizations</option>
          <option value = "J: Recently reported bodies -- not yet confirmed">J: Recently reported bodies -- not yet confirmed</option>
          <option value = "K: Subsidiary and internal bodies of other internal bodies">K: Subsidiary and internal bodies of other internal bodies</option>
          <option value = "N: National organizations">N: National organizations</option>
          <option value = "R: Religious orders, fraternities and secular institutes">R: Religious orders, fraternities and secular institutes</option>
          <option value = "S: Autonomous conference series (without secretariat)">S: Autonomous conference series (without secretariat)</option>
          <option value = "T: Multilateral treaties and agreements">T: Multilateral treaties and agreements</option>
          <option value = "U: Currently inactive non-conventional or unconfirmed bodies">U: Currently inactive non-conventional or unconfirmed bodies</option>
          
        </select>
      </td>
      
    </tr>
    
    <tr>

        
      <td nowrap class="TableHeader">工作语言： </td>
      <td class="TableData">
        <input class="SmallInput" type="text" name="language" id="language" value="">
      </td>
      
       
      <td nowrap class="TableHeader">会议举办城市： </td>
      <td class="TableData">
        <input class="SmallInput" type="text" name="city" id="city" value="">
      </td>
      <td nowrap class="TableHeader">组织类型II： </td>
      <td class="TableData" colspan = "3">
         <select name="typeii" id="typeii">
          <option value = "">请选择类型II</option>
          <option value = "b: bilateral intergovernmental organization normally but not always assigned to Type G)">b: bilateral intergovernmental organization (normally but not always assigned to TypeG</option>
          <option value = "c: conference series (normally but not always assigned to Type S)">c: conference series (normally but not always assigned to Type S)</option>
          <option value = "d: dissolved, dormant (normally but not always assigned to Type H or Type U)">d: dissolved, dormant (normally but not always assigned to Type H or Type U)</option>
          <option value = "e: commercial enterprise">e: commercial enterprise</option>
          <option value = "f: foundation, fund (normally but not always assigned to Type F)">f: foundation, fund (normally but not always assigned to Type F)</option>
          <option value = "g: intergovernmental">g: intergovernmental</option>
          <option value = "j: research institute">j: research institute</option>
          <option value = "n: has become national (normally but not always assigned to Type N)">n: has become national (normally but not always assigned to Type N)</option>
          <option value = "p: proposed body (normally but not always assigned to Type J)">p: proposed body (normally but not always assigned to Type J)</option>
          <option value = "s: information suspect">s: information suspect</option>
          <option value = "v: individual membership only">v: individual membership only</option>
          <option value = "x: no recent information received ">x: no recent information received </option>
        </select>
      </td>
      </tr>
      <tr>
    <td nowrap class="TableHeader">会议举办国家： </td>
      <td class="TableData">
        <input class="SmallInput" type="text" name="country" id="country" value="">
      </td>
      
       <td nowrap class="TableHeader">会议举办年份： </td>
      <td class="TableData">
        <input class="SmallInput" type="text" name="year" id="year" value="">
      </td>
      
       <td nowrap class="TableHeader">组织分类I： </td>
      <td class="TableData">
        <a href = "#" onclick = "javascript:index(1,'');">按首字母索引</a>
        <input type="text" class= "BigStatic" size = "10" name = "subjecti" id = "subjecti" value = "" readonly></input>&nbsp;&nbsp;<a href = "#" onclick="clearInput('subjecti');">清空</a>
      </td>
      
       <td nowrap class="TableHeader">组织分类II： </td>
      <td class="TableData">
        <a href = "#" onclick = "javascript:index(2,'');">按首字母索引</a>
        
        <input type="text" class= "BigStatic" size = "10" name = "subjectii" id = "subjectii" value = "" readonly></input>&nbsp;&nbsp;<a href = "#" onclick="clearInput('subjectii');">清空</a>
      </td>
    </tr>
    
   <tr class="TableControl">
    <td nowrap align="center" colspan="8">
     <input type="button" value="查询" onClick="javascript:getGroup();" class="BigButton" title="模糊查询">
    </td>
   </tr>
  
</table>
<div id="container" style = "display:none;">
<div id="abc">

  <span><a href="#A">A</a></span><span><a href="#B">B</a></span><span><a href="#C">C</a></span><span><a href="#D">D</a></span><span><a href="#E">E</a></span><span><a href="#F">F</a></span><span><a href="#G">G</a></span><span><a href="#H">H</a></span><span><a href="#I">I</a></span><span><a href="#J">J</a></span><span><a href="#K">K</a></span><span><a href="#L">L</a></span><span><a href="#M">M</a></span><span><a href="#N">N</a></span><span><a href="#O">O</a></span><span><a href="#P">P</a></span><span><a href="#Q">Q</a></span><span><a href="#R">R</a></span><span><a href="#S">S</a></span><span><a href="#T">T</a></span><span><a href="#U">U</a></span><span><a href="#V">V</a></span><span><a href="#W">W</a></span><span><a href="#X">X</a></span><span><a href="#Y">Y</a></span><span><a href="#Z">Z</a></span>
  </div>
  <div id="candidate" >
  
    <div id="cd-content">
  
      <div class="capital">
     
  
      </div>
    </div>
  </div>
  </div>
 </form>
 <br>
<div id="listDiv"></div>
<div id="returnNull"></div>
</body>
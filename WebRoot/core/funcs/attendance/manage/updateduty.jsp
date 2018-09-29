<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String userId = request.getParameter("userId");
  String date = request.getParameter("date");
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy年MM月dd日");
  String dateStr = dateFormat2.format(dateFormat1.parse(date));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>上下班登记修改</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<style  type="text/css">
.tdnowrap{
   nowrap: true;
 }
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
var userId = '<%=userId%>';
var date = '<%=date%>';
function checkForm(){
  var type1 =   "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
  var type2 = "^\:[0-5]?[0-9]\:[0-5]?[0-9]$"　;
  var re1 = new RegExp(type1); 
  var re2 = new RegExp(type2);
  if(document.getElementById("registerTime1")&&document.getElementById("registerTime1").value!=""){
    if(document.getElementById("registerTime1").value.match(re1) == null || document.getElementById("registerTime1").value.match(re2) != null) {
      alert( "登记的时间格式不正确,应形如 8:00:00"); 
      document.getElementById("registerTime1").focus();
      document.getElementById("registerTime1").select();
      return false;
    }
  }
  if(document.getElementById("registerTime2")&&document.getElementById("registerTime2").value!=""){
    if(document.getElementById("registerTime2").value.match(re1) == null || document.getElementById("registerTime2").value.match(re2) != null) {
      alert( "登记的时间格式不正确,应形如 8:00:00"); 
      document.getElementById("registerTime2").focus();
      document.getElementById("registerTime2").select();
      return false;
    }
  }
  if(document.getElementById("registerTime3")&&document.getElementById("registerTime3").value!=""){
    if(document.getElementById("registerTime3").value.match(re1) == null || document.getElementById("registerTime3").value.match(re2) != null) {
      alert( "登记的时间格式不正确,应形如 8:00:00"); 
      document.getElementById("registerTime3").focus();
      document.getElementById("registerTime3").select();
      return false;
    }
  }
  if(document.getElementById("registerTime4")&&document.getElementById("registerTime4").value!=""){
    if(document.getElementById("registerTime4").value.match(re1) == null || document.getElementById("registerTime4").value.match(re2) != null) {
      alert( "登记的时间格式不正确,应形如 8:00:00"); 
      document.getElementById("registerTime4").focus();
      document.getElementById("registerTime4").select();
      return false;
    }
  }
  if(document.getElementById("registerTime5")&&document.getElementById("registerTime5").value!=""){
    if(document.getElementById("registerTime5").value.match(re1) == null || document.getElementById("registerTime5").value.match(re2) != null) {
      alert( "登记的时间格式不正确,应形如 8:00:00"); 
      document.getElementById("registerTime5").focus();
      document.getElementById("registerTime5").select();
      return false;
    }
  }
  if(document.getElementById("registerTime6")&&document.getElementById("registerTime6").value!=""){
    if(document.getElementById("registerTime6").value.match(re1) == null || document.getElementById("registerTime6").value.match(re2) != null) {
      alert( "登记的时间格式不正确,应形如 8:00:00"); 
      document.getElementById("registerTime6").focus();
      document.getElementById("registerTime6").select();
      return false;
    }
  }
  return true;
}
function doOnload(){
  config();
}
function config(){
  var URL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/selectDutyByUserIdNameDate.act?userId="+userId+"&date="+date;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userName = rtJson.rtMsrg;
  document.getElementById("userName").innerHTML =userName ;
  //建表
  var table = new Element('table',{"class":"TableList", "align":"center", "width":"95%"}).update("<tr id='title' class='TableHeader'></tr><tr id='body' class='TableHeader'></tr>");
  $("dutyTable").appendChild(table);
  var prcs= rtJson.rtData;
  if(prcs.length>0){
    var prcsJson = prcs[0];
    var seqId = prcsJson.seqId;
    var dutyName = prcsJson.dutyName;
    var dutyTime1 = prcsJson.dutyTime1;
    var dutyTime2 = prcsJson.dutyTime2;
    var dutyTime3 = prcsJson.dutyTime3;
    var dutyTime4 = prcsJson.dutyTime4;
    var dutyTime5 = prcsJson.dutyTime5;
    var dutyTime6 = prcsJson.dutyTime6;
    var dutyType1 = prcsJson.dutyType1;
    var dutyType2 = prcsJson.dutyType2;
    var dutyType3 = prcsJson.dutyType3;
    var dutyType4 = prcsJson.dutyType4;
    var dutyType5 = prcsJson.dutyType5;
    var dutyType6 = prcsJson.dutyType6;
    var dutyStatus1 = prcsJson.dutyStatus1;
    var dutyStatus2 = prcsJson.dutyStatus2;
    var dutyStatus3 = prcsJson.dutyStatus3;
    var dutyStatus4 = prcsJson.dutyStatus4;
    var dutyStatus5 = prcsJson.dutyStatus5;
    var dutyStatus6 = prcsJson.dutyStatus6;
    var dutyTypeNames = ["上班","下班"];
    var registerTime ='';
    var registerType = '';
    var seqIdduty ='';
    var userIdduty='' ;
    var registerIp='';
    var remark='';
   //1次
    var dutytitleStr = "";
    var dutybodyStr = "";
    if(dutyTime1.trim()!=''){
      dutytitleStr = dutytitleStr + "<td align='center' nowrap>"+dutyTypeNames[parseInt(dutyType1)-1]+"("
      + dutyTime1 + ")"
      +"<input type='hidden' id='dutyType1' name='dutyType1' value='"+dutyType1+"'>"+"</td>";
      var td1 = new Element('td',{"align":"center","class":"tdnowrap"}).update(""+dutyTypeNames[parseInt(dutyType1)-1]+"("
          + dutyTime1 + ")"
          +"<input type='hidden' id='dutyType1' name='dutyType1' value='"+dutyType1+"'>");
      var td2;
      for(var i = 1; i< prcs.length; i++){
        var duty = prcs[i];
        registerTime = duty.registerTime;
        registerType = duty.registerType;
        seqIdduty = duty.seqId;
        userIdduty = duty.userId;
        registerIp = duty.registerIp;
        remark = duty.remark;
        if(registerType =="1"){break;}
      }
      if(registerType=="1"){
        td2 = new Element('td',{"align":"center","class":"tdnowrap"}).update("<input type='text' id='registerTime1' name='registerTime1' value='"+registerTime.substr(11,8)+"' class='SmallInput' size='8'>"
            +"<input id='seqId1' type='hidden' name='seqId1' value='"+seqIdduty+"' class='SmallInput' size='8'>"  
            );
        dutybodyStr = dutybodyStr + "<td align='center' nowrap=true><input type='text' id='registerTime1' name='registerTime1' value='"+registerTime.substr(11,8)+"' class='SmallInput' size='8'>"
        +"<input id='seqId1' type='hidden' name='seqId1' value='"+seqIdduty+"' class='SmallInput' size='8'></td>";
      }else{
        
        td2 = new Element('td',{"align":"center","width":"50"}).update("<input type='text' id='registerTime1' name='registerTime1' value='' class='SmallInput' size='8'>");
        dutybodyStr = dutybodyStr + "<td align='center' nowrap=true><input type='text' id='registerTime1' name='registerTime1' value='' class='SmallInput' size='8'></td>";
      }
    // $("tr_title").appendChild(td1);
     // $("tr_body").appendChild(td2);
    }
    //2次

    if(dutyTime2.trim()!=''){
      var td1 = new Element('td',{"align":"center","class":"tdnowrap"}).update(""+dutyTypeNames[parseInt(dutyType2)-1]+"("
          + dutyTime2 + ")"
          +"<input type='hidden' id='dutyType2' name='dutyType2' value='"+dutyType2+"'>");
      dutytitleStr = dutytitleStr + "<td align='center' nowrap=true>"+dutyTypeNames[parseInt(dutyType2)-1]+"("
      + dutyTime2 + ")"
      +"<input type='hidden' id='dutyType2' name='dutyType2' value='"+dutyType2+"'>"+"</td>";
     
      var td2;
      for(var i = 1; i< prcs.length; i++){
        var duty = prcs[i];
        registerTime = duty.registerTime;
        registerType = duty.registerType;
        seqIdduty = duty.seqId;
        userIdduty = duty.userId;
        registerIp = duty.registerIp;
        remark = duty.remark;
        if(registerType =="2"){break;}
      }
      if(registerType=="2"){
        td2 = new Element('td',{"align":"center","class":"tdnowrap"}).update("<input type='text' id='registerTime2' name='registerTime2' value='"+registerTime.substr(11,8)+"' class='SmallInput' size='8'>"
            +"<input id='seqId2' type='hidden' name='seqId2' value='"+seqIdduty+"' class='SmallInput' size='8'>"  
            );

        dutybodyStr = dutybodyStr + "<td align='center' nowrap=true><input type='text' id='registerTime2' name='registerTime2' value='"+registerTime.substr(11,8)+"' class='SmallInput' size='8'>"
        +"<input id='seqId2' type='hidden' name='seqId2' value='"+seqIdduty+"' class='SmallInput' size='8'></td>";
      }else{
        td2 = new Element('td',{"align":"center","class":"tdnowrap"}).update("<input type='text' id='registerTime2' name='registerTime2' value='' class='SmallInput' size='8'>");
        dutybodyStr = dutybodyStr + "<td align='center' nowrap=true><input type='text' id='registerTime2' name='registerTime2' value='' class='SmallInput' size='8'></td>";
      }
     //$("tr_title").appendChild(td1);
     // $("tr_body").appendChild(td2);
    }
    //3次

    if(dutyTime3.trim()!=''){
      var td1 = new Element('td',{"align":"center","class":"tdnowrap"}).update(""+dutyTypeNames[parseInt(dutyType3)-1]+"("
          + dutyTime3 + ")"
          +"<input type='hidden' id='dutyType3' name='dutyType3' value='"+dutyType3+"'>");
      dutytitleStr = dutytitleStr + "<td align='center' nowrap=true>"+dutyTypeNames[parseInt(dutyType3)-1]+"("
          + dutyTime3 + ")"
          +"<input type='hidden' id='dutyType3' name='dutyType3' value='"+dutyType3+"'></td>";
       var td2;
      for(var i = 1; i< prcs.length; i++){
        var duty = prcs[i];
        registerTime = duty.registerTime;
        registerType = duty.registerType;
        seqIdduty = duty.seqId;
        userIdduty = duty.userId;
        registerIp = duty.registerIp;
        remark = duty.remark;
        if(registerType =="3"){break;}
      }
      if(registerType=="3"){
        td2 = new Element('td',{"align":"center","class":"tdnowrap"}).update("<input type='text' id='registerTime3' name='registerTime3' value='"+registerTime.substr(11,8)+"' class='SmallInput' size='8'>"
            +"<input id='seqId3' type='hidden' name='seqId3' value='"+seqIdduty+"' class='SmallInput' size='8'>"  
            );
        dutybodyStr = dutybodyStr + "<td align='center' nowrap=true><input type='text' id='registerTime3' name='registerTime3' value='"+registerTime.substr(11,8)+"' class='SmallInput' size='8'>"
        +"<input id='seqId3' type='hidden' name='seqId3' value='"+seqIdduty+"' class='SmallInput' size='8'></td>";  
      }else{
        td2 = new Element('td',{"align":"center","class":"tdnowrap"}).update("<input type='text' id='registerTime3' name='registerTime3' value='' class='SmallInput' size='8'>");
        dutybodyStr = dutybodyStr + "<td align='center' nowrap=true><input type='text' id='registerTime3' name='registerTime3' value='' class='SmallInput' size='8'></td>";
      }
     //$("tr_title").appendChild(td1);
     // $("tr_body").appendChild(td2);
    }
    //4次

    if(dutyTime4.trim()!=''){
      var td1 = new Element('td',{"align":"center","class":"tdnowrap"}).update(""+dutyTypeNames[parseInt(dutyType4)-1]+"("
          + dutyTime4 + ")"
          +"<input type='hidden' id='dutyType4' name='dutyType4' value='"+dutyType4+"'>"
          );
      dutytitleStr = dutytitleStr + "<td align='center' nowrap=true>"+dutyTypeNames[parseInt(dutyType4)-1]+"("
      + dutyTime4 + ")"
      +"<input type='hidden' id='dutyType4' name='dutyType4' value='"+dutyType4+"'></td>";

      var td2;
      for(var i = 1; i< prcs.length; i++){
        var duty = prcs[i];
        registerTime = duty.registerTime;
        registerType = duty.registerType;
        seqIdduty = duty.seqId;
        userIdduty = duty.userId;
        registerIp = duty.registerIp;
        remark = duty.remark;
        if(registerType =="4"){break;}
      }
      if(registerType=="4"){
        td2 = new Element('td',{"align":"center","class":"tdnowrap"}).update("<input  type='text' id='registerTime4' name='registerTime4' value='"+registerTime.substr(11,8)+"' class='SmallInput' size='8'>"  
               +"<input id='seqId4' type='hidden' name='seqId4' value='"+seqIdduty+"' class='SmallInput' size='8'>"  
            );
        dutybodyStr = dutybodyStr + "<td align='center' nowrap=true><input  type='text' id='registerTime4' name='registerTime4' value='"+registerTime.substr(11,8)+"' class='SmallInput' size='8'>"  
        +"<input id='seqId4' type='hidden' name='seqId4' value='"+seqIdduty+"' class='SmallInput' size='8'></td>";
      }else{
        td2 = new Element('td',{"align":"center","class":"tdnowrap"}).update("<input  type='text' id='registerTime4' name='registerTime4' value='' class='SmallInput' size='8'>");
        dutybodyStr = dutybodyStr + "<td align='center' nowrap=true><input  type='text' id='registerTime4' name='registerTime4' value='' class='SmallInput' size='8'></td>";
      }
    // $("tr_title").appendChild(td1);
      //$("tr_body").appendChild(td2);
    }
    //5次

    if(dutyTime5.trim()!=''){
      var td1 = new Element('td',{"align":"center","class":"tdnowrap"}).update(""+dutyTypeNames[parseInt(dutyType5)-1]+"("
          + dutyTime5 + ")"
          +"<input type='hidden' id='dutyType5' name='dutyType5' value='"+dutyType5+"'>");
      dutytitleStr = dutytitleStr + "<td align='center' nowrap=true>"+dutyTypeNames[parseInt(dutyType5)-1]+"("
      + dutyTime5 + ")"
      +"<input type='hidden' id='dutyType5' name='dutyType5' value='"+dutyType5+"'></td>";
       var td2;
      for(var i = 1; i< prcs.length; i++){
        var duty = prcs[i];
        registerTime = duty.registerTime;
        registerType = duty.registerType;
        seqIdduty = duty.seqId;
        userIdduty = duty.userId;
        registerIp = duty.registerIp;
        remark = duty.remark;
        if(registerType =="5"){break;}
      }
      if(registerType=="5"){
        td2 = new Element('td',{"align":"center","class":"tdnowrap"}).update("<input  type='text' id='registerTime5' name='registerTime5' value='"+registerTime.substr(11,8)+"' class='SmallInput' size='8'>"
            +"<input id='seqId5' type='hidden' name='seqId5' value='"+seqIdduty+"' class='SmallInput' size='8'>"  
        );
        dutybodyStr = dutybodyStr + "<td align='center' nowrap=true><input  type='text' id='registerTime5' name='registerTime5' value='"+registerTime.substr(11,8)+"' class='SmallInput' size='8'>"
        +"<input id='seqId5' type='hidden' name='seqId5' value='"+seqIdduty+"' class='SmallInput' size='8'></td>";  
      }else{
        td2 = new Element('td',{"align":"center","tdnowrap":"class"}).update("<input  type='text' id='registerTime5' name='registerTime5' value='' class='SmallInput' size='8'>");
        dutybodyStr = dutybodyStr + "<td align='center' nowrap=true><input  type='text' id='registerTime5' name='registerTime5' value='' class='SmallInput' size='8'></td>";
      }
     //$("tr_title").appendChild(td1);
     // $("tr_body").appendChild(td2);
    }
    //6次

    if(dutyTime6.trim()!=''){
      var td1 = new Element('td',{"align":"center","class":"tdnowrap"}).update(""+dutyTypeNames[parseInt(dutyType6)-1]+"("
          + dutyTime6 + ")"
          +"<input type='hidden' id='dutyType6' name='dutyType6' value='"+dutyType4+"'>");
      dutytitleStr = dutytitleStr + "<td align='center' nowrap=true>"+dutyTypeNames[parseInt(dutyType6)-1]+"("
      + dutyTime6 + ")"
      +"<input type='hidden' id='dutyType6' name='dutyType6' value='"+dutyType4+"'></td>";
      var td2;
      for(var i = 1; i< prcs.length; i++){
        var duty = prcs[i];
        registerTime = duty.registerTime;
        registerType = duty.registerType;
        seqIdduty = duty.seqId;
        userIdduty = duty.userId;
        registerIp = duty.registerIp;
        remark = duty.remark;
        if(registerType =="6"){break;}
      }
      if(registerType=="6"){
        td2 = new Element('td',{"align":"center","width":"50"}).update("<input type='text' id='registerTime6' name='registerTime6' value='"+registerTime.substr(11,8)+"' class='SmallInput' size='8'>"
            +"<input id='seqId6' type='hidden' name='seqId6' value='"+seqIdduty+"' class='SmallInput' size='8'>"  
            );
        dutybodyStr = dutybodyStr + "<td align='center' nowrap=true><input type='text' id='registerTime6' name='registerTime6' value='"+registerTime.substr(11,8)+"' class='SmallInput' size='8'>"
        +"<input id='seqId6' type='hidden' name='seqId6' value='"+seqIdduty+"' class='SmallInput' size='8'></td>";  
      }else{
        td2 = new Element('td',{"align":"center","class":"tdnowrap"}).update("<input  type='text' id='registerTime6' name='registerTime6' value='' class='SmallInput' size='8'>");
        dutybodyStr = dutybodyStr + "<td align='center' nowrap=true><input  type='text' id='registerTime6' name='registerTime6' value='' class='SmallInput' size='8'></td>";
      }
     //$("tr_title").appendChild(td1);
     // $("tr_body").appendChild(td2);
    }
    $("title").update(dutytitleStr);
    $("body").update(dutybodyStr);
  }
  
}
function Init(){
  if(checkForm()){
    var URL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/updateDuty.act";
    var rtJson = getJsonRs(URL,mergeQueryString($("form1")));
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    history.go(-1);
  }
}
</script>
</head>
<body class="" topmargin="5" onload="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 上下班登记修改 - &nbsp;<span id="userName"></span> &nbsp;- &nbsp;<%=dateStr %></span><br>
    </td>
  </tr>
</table>
<br>
<table class="MessageBox" align="center" width="500">
  <tr>
    <td class="msg info">
      <h4 class="title">注意</h4>
      <div class="content" style="font-size:12pt">输入的时间格式要形如 12:12:12，不填写则代表未登记</div>
    </td>
  </tr>
</table>
<form action="#" id="form1" name="form1" method ="post" >
<div id="dutyTable"></div>
<br>
<div align="center">
  <input type="hidden" id="userId" name="userId" value="<%=userId %>">
  <input type="hidden" id="date" name="date" value="<%=date %>">
  <input type="button"  value="保存" class="BigButton" onclick="Init();">&nbsp;&nbsp;
  <input type="button"  value="返回" class="BigButton" onClick="history.back();">
</div>
</form>
</body>
</html>
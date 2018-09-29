<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String userId = request.getParameter("userId");
  String date = request.getParameter("date");
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yyyy");
  String dateStr = dateFormat2.format(dateFormat1.parse(date));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>人员考勤记录</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
function doOnload(){
  var userId = '<%=userId%>';
  var date = '<%=date%>';
  //建表
  newTable();
  //得到上下班类型
  config(userId,date);
}
function newTable(){
  var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tbody id = 'tboday'><tr class='TableHeader'><td nowrap align='center'>登记次序</td>"
      + "<td nowrap align='center'>登记类型</td>"
      + "<td nowrap align='center'>规定时间</td>"
      + "<td nowrap align='center'>登记时间</td>"
      + "<td nowrap align='center'>登记IP</td></tr></tbody>");
    $('dutyTable').appendChild(table); 
}
function config(userId,date){
  var URL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/selectDutyByUserIdNameDate.act?userId="+userId+"&date="+date;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs= rtJson.rtData;
  if(prcs.length>0){
    var prcsJson = prcs[0];
    document.getElementById("dutyName").innerHTML =prcsJson.dutyName ;
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
    var general = prcsJson.general;
    var dutyTypeNames = ["上班登记","下班登记"];
    var registerStatus = "未登记";
    var holidayStr = "";
    if(evection(userId)){
      registerStatus = "<font color='#008000'>出差</font>";
      holidayStr = '1';
    }
    if(generalIs(general)){
      registerStatus = "<font color='#008000'>公休日</font>";
      holidayStr = '1';
    }
    if(holiday()){
      registerStatus = "<font color='#008000'>节假日</font>";
      holidayStr = '1';
    }
    //第一次登记是否存在

    if(dutyTime1.trim() != ""){ 
      var tr = new Element('tr',{"class":"TableData"});
      $('tboday').appendChild(tr);
      var registerTime;
      var registerType;
      var seqId;
      var userId ;
      var registerIp;
      var remark;
      for(var i = 1; i< prcs.length; i++){
        var duty = prcs[i];
        registerTime = duty.registerTime;
        registerType = duty.registerType;
        seqId = duty.seqId;
        userId = duty.userId;
        registerIp = duty.registerIp;
        remark = duty.remark;
        if(registerType =="1"){break;}
      }
      if(registerType =="1"){
        var ZC = "";
        var update = "";
        var dutyTime1Int = strInt(dutyTime1);
        var  registerTimeInt = strInt(registerTime.substr(11,19));
        if(dutyType1=="1"){
          if(dutyTime1Int<registerTimeInt&&holidayStr!=1){
            ZC = "<span class=big4>迟到</span><br>";
          }
        }else{
          if(dutyTime1Int>registerTimeInt&&holidayStr!=1){
            ZC = "<span class=big4>早退</span>";
          }
        }
        if(remark!=""){
          update = "<br>备注："+remark +" <a href='javascript:remark("+seqId+");' title='修改备注'>修改</a> ";
        }
       tr.update("<td nowrap align='center'>第1次登记</td>"
          + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType1)-1]+"</td>"
          + " <td nowrap align='center'>" + dutyTime1 +"</td> "
          + " <td nowrap align='center'>" + registerTime.substr(11,19) + ZC + update+"</td>"
          + " <td nowrap align='center'>"+registerIp+"</td>");
      }else{
        tr.update("<td nowrap align='center'>第1次登记</td>"
            + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType1)-1]+"</td>"
            + " <td nowrap align='center'>" + dutyTime1 +"</td> "
            + "  <td nowrap align='center'>" + registerStatus + "</td>"
            + " <td nowrap align='center'></td>");
      }
    }
  //第二次登记是否存在

    if(dutyTime2.trim() != ""){ 
      var tr = new Element('tr',{"class":"TableData"});
      $('tboday').appendChild(tr);
      var registerTime;
      var registerType;
      var seqId;
      var userId ;
      var registerIp;
      var remark;
      for(var i = 1; i< prcs.length; i++){
        var duty = prcs[i];
        registerTime = duty.registerTime;
        registerType = duty.registerType;
        seqId = duty.seqId;
        userId = duty.userId;
        registerIp = duty.registerIp;
        remark = duty.remark;
        if(registerType =="2"){break;}
      }
      if(registerType =="2"){
        var ZC = "";
        var update = "";
        var dutyTime2Int = strInt(dutyTime2);
        var  registerTimeInt = strInt(registerTime.substr(11,19));
        if(dutyType2=="1"){
          if(dutyTime2Int<registerTimeInt&&holidayStr!='1'){
            ZC = "<span class=big4>迟到</span><br>";
          }
        }else{
          if(dutyTime2Int>registerTimeInt&&holidayStr!='1'){
            ZC = "<span class=big4>早退</span>";
          }
        }
        if(remark!=""){
          update = "<br>备注："+remark +" <a href='javascript:remark("+seqId+");' title='修改备注'>修改</a> ";
        }
       tr.update("<td nowrap align='center'>第2次登记</td>"
          + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType2)-1]+"</td>"
          + " <td nowrap align='center'>" + dutyTime2 +"</td> "
          + " <td nowrap align='center'>" + registerTime.substr(11,19) + ZC + update+"</td>"
          + " <td nowrap align='center'>"+registerIp+"</td>");
      }else{
        tr.update("<td nowrap align='center'>第2次登记</td>"
            + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType2)-1]+"</td>"
            + " <td nowrap align='center'>" + dutyTime2 +"</td> "
            + "  <td nowrap align='center'>" + registerStatus + "</td>"
            + " <td nowrap align='center'></td>");
      }
    }
  //第三次登记是否存在

    if(dutyTime3.trim() != ""){ 
      var tr = new Element('tr',{"class":"TableData"});
      $('tboday').appendChild(tr);
      var registerTime;
      var registerType;
      var seqId;
      var userId ;
      var registerIp;
      var remark;
      for(var i = 1; i< prcs.length; i++){
        var duty = prcs[i];
        registerTime = duty.registerTime;
        registerType = duty.registerType;
        seqId = duty.seqId;
        userId = duty.userId;
        registerIp = duty.registerIp;
        remark = duty.remark;
        if(registerType =="3"){break;}
      }
      if(registerType =="3"){
        var ZC = "";
        var update = "";
        var dutyTime3Int = strInt(dutyTime3);
        var  registerTimeInt = strInt(registerTime.substr(11,19));
        if(dutyType3=="1"){
          if(dutyTime3Int<registerTimeInt&&holidayStr != "1"){
            ZC = "<span class=big4> 迟到</span><br>";
          }
        }else{
          if(dutyTime3Int>registerTimeInt&&holidayStr != "1"){
            ZC = "<span class=big4> 早退</span>";
          }
        }
        if(remark!=""){
          update = "<br>备注："+remark +" <a href='javascript:remark("+seqId+");' title='修改备注'>修改</a> ";
        }
       tr.update("<td nowrap align='center'>第3次登记</td>"
          + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType3)-1]+"</td>"
          + " <td nowrap align='center'>" + dutyTime3 +"</td> "
          + " <td nowrap align='center'>" + registerTime.substr(11,19) + ZC + update+"</td>"
          + " <td nowrap align='center'>"+registerIp+"</td>");
      }else{
        tr.update("<td nowrap align='center'>第3次登记</td>"
            + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType3)-1]+"</td>"
            + " <td nowrap align='center'>" + dutyTime3 +"</td> "
            + "  <td nowrap align='center'>" + registerStatus + "</td>"
            + " <td nowrap align='center'></td>");
      }
    }
  //第四次登记是否存在

    if(dutyTime4.trim() != ""){ 
      var tr = new Element('tr',{"class":"TableData"});
      $('tboday').appendChild(tr);
      var registerTime;
      var registerType;
      var seqId;
      var userId ;
      var registerIp;
      var remark;
      for(var i = 1; i< prcs.length; i++){
        var duty = prcs[i];
        registerTime = duty.registerTime;
        registerType = duty.registerType;
        seqId = duty.seqId;
        userId = duty.userId;
        registerIp = duty.registerIp;
        remark = duty.remark;
        if(registerType =="4"){break;}
      }
      if(registerType =="4"){
        var ZC = "";
        var update = "";
        var dutyTime4Int = strInt(dutyTime4);
        var  registerTimeInt = strInt(registerTime.substr(11,19));
        if(dutyType4=="1"){
          if(dutyTime4Int<registerTimeInt&&holidayStr != "1"){
            ZC = "<span class=big4>迟到</span><br>";
          }
        }else{
          if(dutyTime4Int>registerTimeInt&&holidayStr != "1"){
            ZC = "<span class=big4>早退</span>";
          }
        }
        if(remark!=""){
          update = "<br>备注："+remark +" <a href='javascript:remark("+seqId+");' title='修改备注'>修改</a> ";
        }
       tr.update("<td nowrap align='center'>第4次登记</td>"
          + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType4)-1]+"</td>"
          + " <td nowrap align='center'>" + dutyTime4 +"</td> "
          + " <td nowrap align='center'>" + registerTime.substr(11,19) + ZC + update+"</td>"
          + " <td nowrap align='center'>"+registerIp+"</td>");
      }else{
        tr.update("<td nowrap align='center'>第4次登记</td>"
            + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType4)-1]+"</td>"
            + " <td nowrap align='center'>" + dutyTime4 +"</td> "
            + "  <td nowrap align='center'>" + registerStatus + "</td>"
            + " <td nowrap align='center'></td>");
      }
    }
  //第五次登记是否存在

    if(dutyTime5.trim() != ""){ 
      var tr = new Element('tr',{"class":"TableData"});
      $('tboday').appendChild(tr);
      var registerTime;
      var registerType;
      var seqId;
      var userId ;
      var registerIp;
      var remark;
      for(var i = 1; i< prcs.length; i++){
        var duty = prcs[i];
        registerTime = duty.registerTime;
        registerType = duty.registerType;
        seqId = duty.seqId;
        userId = duty.userId;
        registerIp = duty.registerIp;
        remark = duty.remark;
        if(registerType =="5"){break;}
      }
      if(registerType =="5"){
        var ZC = "";
        var update = "";
        var dutyTime5Int = strInt(dutyTime5);
        var  registerTimeInt = strInt(registerTime.substr(11,19));
        if(dutyType5=="1"){
          if(dutyTime5Int<registerTimeInt&&holidayStr != "1"){
            ZC = "<span class=big4>迟到</span><br>";
          }
        }else{
          if(dutyTime5Int>registerTimeInt&&holidayStr != "1"){
            ZC = "<span class=big4>早退</span>";
          }
        }
        if(remark!=""){
          update = "<br>备注："+remark +" <a href='javascript:remark("+seqId+");' title='修改备注'>修改</a> ";
        }
       tr.update("<td nowrap align='center'>第5次登记</td>"
          + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType5)-1]+"</td>"
          + " <td nowrap align='center'>" + dutyTime5 +"</td> "
          + " <td nowrap align='center'>" + registerTime.substr(11,19) + ZC + update+"</td>"
          + " <td nowrap align='center'>"+registerIp+"</td>");
      }else{
        tr.update("<td nowrap align='center'>第5次登记</td>"
            + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType5)-1]+"</td>"
            + " <td nowrap align='center'>" + dutyTime5 +"</td> "
            + "  <td nowrap align='center'>" + registerStatus + "</td>"
            + " <td nowrap align='center'></td>");
      }
    }
  //第六次登记是否存在

    if(dutyTime6.trim() != ""){ 
      var tr = new Element('tr',{"class":"TableData"});
      $('tboday').appendChild(tr);
      var registerTime;
      var registerType;
      var seqId;
      var userId ;
      var registerIp;
      var remark;
      for(var i = 1; i< prcs.length; i++){
        var duty = prcs[i];
        registerTime = duty.registerTime;
        registerType = duty.registerType;
        seqId = duty.seqId;
        userId = duty.userId;
        registerIp = duty.registerIp;
        remark = duty.remark;
        if(registerType =="6"){break;}
      }
      if(registerType =="6"){
        var ZC = "";
        var update = "";
        var dutyTime6Int = strInt(dutyTime6);
        var  registerTimeInt = strInt(registerTime.substr(11,19));
        if(dutyType6=="1"){
          if(dutyTime6Int<registerTimeInt&&holidayStr != "1"){
            ZC = "<span class=big4>迟到</span><br>";
          }
        }else{
          if(dutyTime6Int>registerTimeInt&&holidayStr != "1"){
            ZC = "<span class=big4>早退</span>";
          }
        }
        if(remark!=""){
          update = "<br>备注："+remark +" <a href='javascript:remark("+seqId+");' title='修改备注'>修改</a> ";
        }
       tr.update("<td nowrap align='center'>第6次登记</td>"
          + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType6)-1]+"</td>"
          + " <td nowrap align='center'>" + dutyTime6 +"</td> "
          + " <td nowrap align='center'>" + registerTime.substr(11,19) + ZC + update+"</td>"
          + " <td nowrap align='center'>"+registerIp+"</td>");
      }else{
        tr.update("<td nowrap align='center'>第6次登记</td>"
            + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType6)-1]+"</td>"
            + " <td nowrap align='center'>" + dutyTime6 +"</td> "
            + "  <td nowrap align='center'>" + registerStatus + "</td>"
            + " <td nowrap align='center'></td>");
      }
    }
  }
  
  //mergeQueryString($("form1"))
}
//判断是否是节假日
function holiday(userId){
  var date = '<%=date%>';
  var requestURLHoliday = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/checkHoliday.act?date="+date;
  var rtJson = getJsonRs(requestURLHoliday);
  var holidayJson = rtJson.rtData;
  var holiday = holidayJson.status;//0为公假日
  if(holiday=="1"){
    return false;
  }
  return true;
}
//判断是否为公休日
function generalIs(general){
  var date = '<%=dateStr%>';
  var generals = '';
  if(general!=''){
    generals = general.split(',');
  }
  var d=new Date(date);   
  var week = d.getDay();
  if(week==0){
    week = 7;
  }
  for(var i = 0;i<generals.length;i++){
    if(week==generals[i]){
      return true;
    }
  }
  return false; 
}
//判断是否为出差时间
function evection(userId){
  var date = '<%=date%>';
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/isEvection.act?date="+date+"&userId="+userId;
  var rtJson = getJsonRs(requestURL);
  var data = rtJson.rtData;
  if(data.evection=="1"){//0为出差日
    return false;
  }
  return true;
}
//得到整数时间秒HH:mm:ss
function strInt(str){
  var min = 0;
  var max = 24*3600;
  var strInt;
  var strInt1;
  var strInt2;
  var strArray = str.split(":");
  for(var i = 0 ; i<strArray.length; i++){
    if(i==0){
      strInt1 = parseInt(strArray[i]*3600,10);
    }else if(i==1){
      strInt2 = parseInt(strArray[i]*60,10); 
    }  
  }
  strInt = strInt1+strInt2+parseInt(strArray[2]);
  return strInt;
}
function remark(seqId){
  var URL="<%=contextPath%>/core/funcs/attendance/personal/dutyRemark.jsp?seqId=" + seqId ;
  myleft=(screen.availWidth-650)/2;
  window.open(URL,"formul_edit","height=250,width=450,status=0,toolbar=no,menubar=no,location=no,scrollbars=no,top=150,left="+myleft+",resizable=yes");
}
function updateduty(){
  var date = '<%=date%>';
  var userId = '<%=userId%>';
  window.location.href = "<%=contextPath%>/core/funcs/attendance/manage/updateduty.jsp?userId="+userId+"&date="+date;
}
</script>
</head>
<body class="" topmargin="5" onload="doOnload();">
<!----  上下班登记 ---->
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle">
    <span class="big3"> 上下班登记查询 (<span id="dutyName"></span>)<%=date %></span><br>
    </td>
  </tr>
</table>
<br>
<div id="dutyTable"></div>
<br>
<div align="center">
  <input type="button"  value="修改登记时间" class="BigButtonC" onClick="updateduty();">&nbsp;
  <input type="button"  value="返回" class="BigButton" onClick="history.go(-1);">
</div>

</body>
</html>
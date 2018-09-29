<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
   String curDateStr = YHUtility.getCurDateTimeStr("yyyy-MM-dd");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>免签节假日设置</title>
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

function CheckForm(){
  if(document.getElementById("beginDate").value==""){ 
    alert("起始日期不能为空！");
    document.getElementById("beginDate").focus();
    document.getElementById("beginDate").select();
    return (false);
  }
  if(document.getElementById("endDate").value==""){ 
    alert("结束日期不能为空！");
    document.getElementById("endDate").focus();
    document.getElementById("endDate").select();
    return (false);
  }
  if(!isValidDateStr(document.getElementById("beginDate").value)){
    alert("起始日期格式不正确,应形如 2010-02-01");
    document.getElementById("beginDate").focus();
    document.getElementById("beginDate").select();
    return false;
    }
  if(!isValidDateStr(document.getElementById("endDate").value)){
    alert("结束日期格式正确,应形如 2010-02-01");
    document.getElementById("endDate").focus();
    document.getElementById("endDate").select();
    return false;
    }
  var beginDate = document.getElementById("beginDate").value;
  var endDate = document.getElementById("endDate").value;
  var beginInt;
  var endInt;
  var beginArray = (document.getElementById("beginDate").value).split("-");
  var endArray = (document.getElementById("endDate").value).split("-");
  for(var i = 0 ; i<beginArray.length; i++){
    beginInt = parseInt(" " + beginArray[i]+ "",10);  
    endInt = parseInt(" " + endArray[i]+ "",10);
    if((beginInt - endInt) > 0){
      alert("起始日期不能大于结束日期!");
      document.getElementById("endDate").focus();
      document.getElementById("endDate").select();
      return false;
    }else if(beginInt<endInt){
      return true;
    }  
  }
   return true;
}
function Init(){
  if(CheckForm()){
    var requestURL; 
    var prcsJson; 
    requestURL = ""; 
    window.location = requestURL;  
  }
}
function edit_holiday(seqId){
  window.location.href = "<%=contextPath%>/core/funcs/system/attendance/editholiday.jsp?seqId=" + seqId;
}
function delete_holiday(seqId){
  msg='确认要删除该项节假日吗？';
  if(window.confirm(msg)){
    var URL = "<%=contextPath%>/yh/core/funcs/system/attendance/act/YHAttendHolidayAct/deleteHolidayById.act?seqId=" + seqId;
    window.location = URL;
  }
}
function delete_all(){
  msg='确认要删除所有节假日吗？';
  if(window.confirm(msg)){
    var URL = "<%=contextPath%>/yh/core/funcs/system/attendance/act/YHAttendHolidayAct/deleteAllHoliday.act";
    window.location = URL;
  }
}
function doOnload(){
  var requestURL; 
  var prcsJson; 
  requestURL = "<%=contextPath%>/yh/core/funcs/system/attendance/act/YHAttendHolidayAct/selectHoliday.act"; 
  var json = getJsonRs(requestURL); 
  //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  prcsJson = json.rtData;
  if(prcsJson.length>0){
  var table = new Element('table',{ "class":"MessageBox", "width":"450" , "align":"center"}).update("<tbody id='tbody'><tr class='TableHeader' style='font-size:10pt'><td align='center'>序号</td><td align='center'>起始日期</td><td align='center'>结束日期</td><td align='center'>操作</td></tr></tbody>"); 
  $('listDiv').appendChild(table); 
  }
  if(prcsJson.length>0){
    for(var i=0; i<prcsJson.length;i++){ 
      var prcs = prcsJson[i]; 
      var seqId = prcs.seqId;
      var no = i + 1; 
      var beginDate = prcs.beginDate;
      var endDate = prcs.endDate;
      var tr = new Element('tr',{'width':'95%','class':'TableList','font-size':'10pt'}); 
      $('tbody').appendChild(tr); 
      $(tr).update("<td align='center'>" 
          + no + "</td><td align='center'>" 
          + beginDate.substr(0,10) + "</td><td align='center'>" 
          + endDate.substr(0,10) + "</td><td align='center'>" 
          + "<a href='#' onclick='edit_holiday(\""+ seqId +"\")'> 编辑</a>&nbsp;&nbsp;&nbsp;" 
          + "<a href='#' onclick='delete_holiday(\""+ seqId +"\")'>删除</a></td></tr>"); 
    }
    var trs = new Element('tr',{"width":"95%","class":"TableList","font-size":"10pt"}); 
    $('tbody').appendChild(trs);
    trs.update("<td nowrap align='center' colspan='4'>"
      + "<input type='button' class='BigButton' onclick='delete_all();' value='全部删除'>"
      + "</td>");
  }else{
    addTable();
  }
  var beginParameters = {
      inputId:'beginDate',
      property:{isHaveTime:false}
      ,bindToBtn:'beginDateImg'
  };
  new Calendar(beginParameters);
  var endParameters = {
      inputId:'endDate',
      property:{isHaveTime:false}
      ,bindToBtn:'endDateImg'
  };
  new Calendar(endParameters);
}
function addTable(){
  var table = new Element('table',{ "class":"MessageBox", "align":"center" ,"width":"260"}).update("<tr>"
    + "<td class='msg info'>"
    + "<div class='content' style='font-size:12pt'>尚未添加日期</div>"
    + "</td></tr>"); 
  $('listDiv').appendChild(table); 
}
function returnBefore(){
  window.location.href = "<%=contextPath%>/core/funcs/system/attendance/index.jsp";
}
</script>
</head>
<body class="" topmargin="5" onload = "doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> &nbsp;添加免签节假日</span>
    </td>
  </tr>
</table>
<form action="<%=contextPath%>/yh/core/funcs/system/attendance/act/YHAttendHolidayAct/addHoliday.act"  method="post" id = "form1" name="form1" onsubmit="return CheckForm();">
<input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.system.attendance.data.YHAttendHoliday"/>
<table class="TableBlock" width="450"  align="center" >
   <tr>
    <td nowrap class="TableData">起始日期：</td>
    <td nowrap class="TableData">
        <input type="text" id = "beginDate" name="beginDate" class="BigInput" size="10" maxlength="10" value="<%=curDateStr %>">
        <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
    </td>
   <tr>
    <td nowrap class="TableData" >结束日期：</td>
    <td nowrap class="TableData">
        <input type="text" id = "endDate" name="endDate" class="BigInput" size="10" maxlength="10" value="<%=curDateStr %>">
        <img id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="submit" value="添加" class="BigButton" title="添加日期"  name="button">
    </td>
</table>
</form>
<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="/images/dian1.gif" width="100%"></td>
 </tr>
</table>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td  class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absmiddle"><span class="big3"> &nbsp;管理免签节假日</span>
    </td>
  </tr>
</table>
<br>
<div align="center" id = "listDiv">
</div>
<br><br>
<div align="center">
  <input type="button"  value="返回" class="BigButton" onClick="returnBefore();">
</div>
</body>
</html>
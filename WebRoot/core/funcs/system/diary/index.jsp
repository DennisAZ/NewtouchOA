<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>工作日志设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
function doInit(){
  var paraValue = "";
  var paraValue1 = "";
  var paraValue2= "";
  var beginParameters = {
      inputId:'statrTime',
      property:{isHaveTime:false}
      ,bindToBtn:'beginDateImg'
  };
  new Calendar(beginParameters);
  var endParameters = {
      inputId:'endTime',
      property:{isHaveTime:false}
      ,bindToBtn:'endDateImg'
  };
  new Calendar(endParameters);
   var url = "<%=contextPath%>/yh/core/funcs/system/diary/act/YHDiaryAct/getDiary.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     bindJson2Cntrl(rtJson.rtData);
     var str = document.getElementById("paraValue").value.split(',');
     for(var i = 0; i < str.length; i++){
        paraValue = str[0];
        paraValue1 = str[1] || "";
        paraValue2 = str[2] || "";
     }
     document.getElementById("endTime").value = paraValue1;
     document.getElementById("days").value = paraValue2;
     if (paraValue=="") {
       document.getElementById("statrTime").value="";
     } else {
       document.getElementById("statrTime").value = paraValue;
     }
     if (document.getElementById("endTime").value=="") {
       document.getElementById("endTime").value="";
     } else {
       document.getElementById("endTime").value = paraValue1;
     }
     if (document.getElementById("days").value=="") {
       document.getElementById("days").value="";
     } else {
       document.getElementById("days").value = paraValue2;
     }
   }else {
     alert(rtJson.rtMsrg); 
   }
}

function isDate(dataValue){
  var year = "";
  var month = "";
  var day = "";
  var offset = 0;
  var len = dataValue.length;
  var i = dataValue.indexOf("-");
  year = dataValue.substr(offset,(i-offset));
  offset = i + 1;
  if(offset > len){
    return false;
  }
  if(i){
    i = dataValue.indexOf("-",offset);
    
    day = dataValue.substr(sum,(len-offset));
  }
  if(year==""||month==""||day==""){
    return false;
  }
  return true;
}

function check(){
  if(document.getElementById("statrTime").value!=""){
    var da = isDate(document.getElementById("statrTime").value);
    if(!da){
      alert("错误 开始时间格式不对，应形如 1999-1-2");
      }
  }
}

function checkDate(cntrlId){
  var dateStr = $(cntrlId).value;
  return isValidDateStr(dateStr) ;
}

function commit(){
  if(checkDate("statrTime") == false){
    $("statrTime").focus();
    $("statrTime").select();
    alert("日期格式不对，请输入形：2010-04-09");
    return;
  }
  if(checkDate("endTime") == false){
    $("endTime").focus();
    $("endTime").select();
    alert("日期格式不对，请输入形：2010-04-09");
    return;
  }
  if($("statrTime").value && $("endTime").value){
    if(document.getElementById("statrTime").value > document.getElementById("endTime").value){
      alert("错误 开始时间不能大于结束时间！");
      $("statrTime").focus();
      $("statrTime").select();
      return false;
    }
  }
  if ($("days").value != "" && ($("days").value < 0 || $("days").value != parseInt($("days").value))){
    alert("锁定日志天数应为正整数！");
    $("days").focus();
    $("days").select();
    return false;
  }
  var url = null;
  var rtJson = null;
  var paraName = document.getElementById("paraName").value;
  var seqId = document.getElementById("seqId").value;
  var statrTime = document.getElementById("statrTime").value;
  var endTime = document.getElementById("endTime").value;
  var days = document.getElementById("days").value;
  url = "<%=contextPath%>/yh/core/funcs/system/diary/act/YHDiaryAct/";
  if (paraName=="LOCK_TIME") {
    url += "updateDiary.act?statrTime="+statrTime+"&endTime="+endTime+"&days="+days+"&seqId="+seqId;
  }else{
    url += "addDiary.act?statrTime="+statrTime+"&endTime="+endTime+"&days="+days+"&seqId="+seqId;
  }
  rtJson = getJsonRs(url, mergeQueryString($("form1")));
  //alert(rtJson.rtMsrg);
  location = "<%=contextPath %>/core/funcs/system/diary/insert.jsp";
}
</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big">
    <img src="<%=imgPath%>/diary.gif" WIDTH="16" HEIGHT="16" align="absmiddle" /><span class="big3">&nbsp;工作日志设置</span>
    </td>
  </tr>
</table>
<br>
<br>
<form name="form1" id="form1" method="post">
<input type="hidden" name="seqId" id="seqId" value="">
<input type="hidden" name="paraName" id="paraName" value="">
<input type="hidden" name="paraValue" id="paraValue" value="">
<input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.system.diary.data.YHDiary.java"/>
<table class="TableBlock" width="500" align="center" >
   <tr>
    <td class="TableHeader"colspan="2" align="center">设置锁定时间范围</td>
   </tr>
   <tr>
    <td nowrap class="TableData">锁定以下时间范围的日志：</td>
    <td class="TableData">
      <input type="text" id="statrTime" name="statrTime" size="10" maxlength="19" class="BigInput" value="">
      <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
     至
      <input type="text" id="endTime" name="endTime" size="10" maxlength="19" class="BigInput" value="">
     <img id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      <br><br>说明：都为空表示不锁定，也可以只填写其中一个
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">锁定指定天数以前的日志：</td>
    <td class="TableData">
      锁定 <input type="text" name="days" id="days" size="3" maxlength="19" class="BigInput" value="" style="text-align:center;"> 天前的日志
      <br><br>说明：0或空表示不锁定
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="button" value="保存设置" class="BigButton" name="button" onclick="commit()">&nbsp;&nbsp;
    </td>
  </tr>
</table>
</form>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<% 
  String deptId = request.getParameter("deptId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>安排查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript">
function checkForm(){
  var minTime = document.getElementById("minTime");
  var maxTime = document.getElementById("maxTime");
  if(minTime.value!=""){
    if(!isValidDateStr(minTime.value)){
      alert("起始日期格式不正确,应形如 2010-02-01");
      minTime.focus();
      minTime.select();
      return false;
      }
  }
  if(maxTime.value!=""){
    if(!isValidDateStr(maxTime.value)){
      alert("起始日期格式不正确,应形如 2010-02-01");
      maxTime.focus();
      maxTime.select();
      return false;
     }
  }
  if (minTime.value != "" && maxTime.value != "") {
    if(compareDate(minTime , maxTime)){
      alert("起始日期不能大于结束日期!");
      minTime.focus();
      maxTime.select();
      return false;
    }
  }
  return true;
}
function compareDate(beginDate , endDate) {
  d1Arr=beginDate.value.split('-');
  d2Arr=endDate.value.split('-');
  v1=new Date(d1Arr[0],d1Arr[1],d1Arr[2]);
  v2=new Date(d2Arr[0],d2Arr[1],d2Arr[2]);
  return v1>v2;
}
function export_excel(){
  if(checkForm()){
    var deptId = $("deptId").value;
    var minTime = $("minTime").value;
    var maxTime = $("maxTime").value;
    var calLevel = $("calLevel").value;
    var overStatus = $("overStatus").value;
    var content = $("content").value;
    var URL = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/selectCalendarByDeptTermOut.act?deptId="+deptId+"&minTime="+minTime+"&maxTime="+maxTime+"&calLevel="+calLevel+"&overStatus="+overStatus+"&content="+ encodeURIComponent(content);
    location.href = URL;
  }
}
function doOnload(){
  //得到部门
  var deptId = '<%=deptId%>';
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/selectDeptByParentId.act?deptId=0";
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData;
  var selects = document.getElementById("deptId");
  for(var i=0;i<prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value; 
    option.innerHTML = prc.text; 
    if(deptId==prc.value){
      option.selected = "selected";
    }
    if(prc.value=='0'){
      document.getElementById("deptIdTR").style.display = 'none';
      break;
    }
    selects.appendChild(option);
  }
  //初始化时间
  var date1Parameters = {
      inputId:'minTime',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);
  var date2Parameters = {
      inputId:'maxTime',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);
}
function Init(){
  if(checkForm()){
    document.getElementById("form1").submit();
  }
}
</script>
</head>

<body class="" topmargin="5" onload="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absmiddle"><span class="big3"> 日程安排查询</span>
    </td>
  </tr>
</table>

<br>
<form action="<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/selectCalendarByDeptTerm.act"  method="post" id="form1"  name="form1" onsubmit="return checkForm();">
 <table class="TableBlock" width="450" align="center">
    <tr  class="TableLine1" id="deptIdTR">
      <td nowrap  width="100" > 部门：</td>
      <td >
   <select id="deptId" name="deptId" >
   </select>
      </td>
    </tr>
    <tr class="TableLine2">
      <td nowrap width="100"> 日期：</td>
      <td >
        <input id="minTime" type="text" name="minTime" size="12" maxlength="10" class="BigInput" value="">
        <img id="date1" align="absMiddle" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" > 至&nbsp;
        <input id="maxTime" type="text" name="maxTime" size="12" maxlength="10" class="BigInput" value="">
        <img id="date2"  align="absMiddle" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr  class="TableLine2">
      <td nowrap> 优先程度：</td>
      <td>
        <select id="calLevel"  name="calLevel" class="BigSelect">
          <option value="">所有</option>
          <option value="0">未指定</option>
          <option value="1">重要/紧急</option>
          <option value="2">重要/不紧急</option>
          <option value="3">不重要/紧急</option>
          <option value="4">不重要/不紧急</option>
        </select>
      </td>
    </tr>
    <tr  class="TableLine1">
      <td nowrap> 状态：</td>
      <td>
        <select id="overStatus" name="overStatus" class="BigSelect">
          <option value="">所有</option>
          <option value="1">未开始</option>
          <option value="2">进行中</option>
          <option value="3">已超时</option>
          <option value="4">已完成</option>
        </select>
      </td>
    </tr>
    <tr  class="TableLine1">
      <td> 事务内容：</td>
      <td>
        <input id="content" name="content" size="33" class="BigInput">
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="查询"  class="BigButton" onClick="Init();">&nbsp;&nbsp;&nbsp;
        <input type="button" value="导出Excel" class="BigButton" onClick="export_excel();">&nbsp;&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onClick="history.back();">
      </td>
    </tr>
  </table>
</form>

</body>
</html>
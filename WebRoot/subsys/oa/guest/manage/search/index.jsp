<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>>贵宾综合信息查询</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
function doOnload(){
  getQuestType();
//时间
  var parameters1 = {
      inputId:'guestAttendTime',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(parameters1);
  var parameters2 = {
      inputId:'guestAttendTime1',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
  };
  new Calendar(parameters2);

  var parameters3 = {
      inputId:'guestLeaveTime',
      property:{isHaveTime:false}
      ,bindToBtn:'date3'
  };
  new Calendar(parameters3);
  
  var parameters4 = {
      inputId:'guestLeaveTime1',
      property:{isHaveTime:false}
      ,bindToBtn:'date4'
  };
  new Calendar(parameters4);
}
function getQuestType(){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectAct/getCodeItem.act?classNo=GUEST_TYPE"; 
  var json=getJsonRs(requestURL); 
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prcs = json.rtData; 
  var selectObj = $("guestType");
  for(var i = 0 ; i < prcs.length ; i++){
    var prc = prcs[i];
    var seqId = prc.sqlId;
    var classNO = prc.classNo;
    var calssDesc = prc.classDesc;
    var myOption = document.createElement("option");
    myOption.value = seqId;
    myOption.text = calssDesc;
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
  }
}
function toSearch(){
  if(!isValidDateStr($("guestAttendTime").value) && $("guestAttendTime").value != ""){
    alert("日期格式不对，应形如 1999-01-01"); 
    $("guestAttendTime").focus(); 
    $("guestAttendTime").select(); 
    return ; 
  }
  if(!isValidDateStr($("guestAttendTime1").value)  && $("guestAttendTime1").value != ""){
    alert("日期格式不对，应形如 1999-01-01"); 
    $("guestAttendTime1").focus(); 
    $("guestAttendTime1").select(); 
    return ; 
  }
  if(!isValidDateStr($("guestLeaveTime").value)  && $("guestLeaveTime").value != ""){
    alert("日期格式不对，应形如 1999-01-01"); 
    $("guestLeaveTime").focus(); 
    $("guestLeaveTime").select(); 
    return ; 
  }
  if(!isValidDateStr($("guestLeaveTime1").value)  && $("guestLeaveTime1").value != ""){
    alert("日期格式不对，应形如 1999-01-01"); 
    $("guestLeaveTime1").focus(); 
    $("guestLeaveTime1").select(); 
    return ; 
  }
  
  var pars = $('form1').serialize();
  var param = encodeURI(pars);
  var url = "<%=contextPath%>/subsys/oa/guest/manage/search/search.jsp?" + param;
  window.location = url;
}
</script>
</head>
 
<body topmargin="5" onload="doOnload();">
<br>
  <form  action="#"  method="post" id="form1" name="form1">
<table class="TableBlock" width="60%" align="center">

    <tr>
      <td colspan="5" nowrap class="TableHeader">
      <img src="<%=imgPath %>/green_arrow.gif"> 请指定查询条件：
      </td>
    </tr>
 		 <tr>
  		<td nowrap class="TableContent" >贵宾编号：</td>
  	  <td nowrap class="TableData">
  	  	<input type="text" class="BigInput" id="guestNum" name="guestNum" size=20 value="">
  	  </td>  	  	
  		<td nowrap class="TableContent" >贵宾类型：</td>
  	  <td nowrap class="TableData">
  	  	<select id="guestType" name="guestType">
  	  	<option value="">请选择类型</option>
     </select>
贵宾类型可在
“系统管理”->“系统代码设置”模块设置
    </td>  	
  	</tr>
    <tr>
    	<td nowrap class="TableContent">贵宾姓名：</td>
  	  <td nowrap class="TableData">
  	  	<input type="text" class="BigInput" id="guestName" name="guestName" size=20>
  	  </td>
  		<td nowrap class="TableContent" width="90">是否用餐：</td>
  	  <td class="TableData">
  	  <select id="guestDiner" id="guestDiner" name="guestDiner">
  	     <option value="">请选择类型</option>
        <option value="0">否</option>
        <option value="1">是</option>
      </select>
      </td>    	
	  </tr>
	  <tr>
   		<td nowrap class="TableContent">贵宾所在单位：</td>
  	  <td nowrap class="TableData">
  	  	<input type="text" id="guestUnit" class="BigInput" name="guestUnit" size=20>
  	  </td>
  		<td nowrap class="TableContent" >贵宾联系电话：</td>
  	  <td nowrap class="TableData">
  	  <input type="text" id="guestPhone" name="guestPhone" size="20" class="BigInput">
       </td> 
    </tr>   
	  <tr>
  		<td nowrap class="TableContent">来会时间：</td>
      <td nowrap class="TableData" colspan="3">  
        <INPUT type="text" id="guestAttendTime" name="guestAttendTime" class=BigInput size="10">
        <img id="date1" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">至
             <INPUT type="text" id="guestAttendTime1" name="guestAttendTime1" class=BigInput size="10">
        <img id="date2" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      </td>
      </tr>
      <tr>
      <td nowrap class="TableContent">离会时间：</td>
      <td nowrap class="TableData" colspan="3">  
        <INPUT type="text" id="guestLeaveTime" name="guestLeaveTime" class=BigInput size="10">
        <img id="date3" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >至
       <INPUT type="text" id="guestLeaveTime1" name="guestLeaveTime1" class=BigInput size="10">
        <img id="date4" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
   		<td nowrap class="TableContent">数据录入人：</td>
  	  <td nowrap class="TableData" colspan="3">
  	    <input type="hidden" id="guestCreator" name="guestCreator" value="">
  	  <input type="text" id="guestCreatorName" name="guestCreatorName" size="20" class="BigInput" readonly>
    <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['guestCreator','guestCreatorName']);">选择</a>
       <a href="javascript:;" class="orgClear" onClick="$('guestCreator').value='';$('guestCreatorName').value='';">清空</a>
	  </td> 
  	</tr>
  	<tr>
  		<td nowrap class="TableContent">接待部门：</td>
  	  <td class="TableData" colspan="3">
			<input type="hidden" id="guestDept" name="guestDept">
        <textarea cols="40" id="guestDeptName" name="guestDeptName" rows="2" style="overflow-y:auto;" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectDept(['guestDept','guestDeptName']);">选择</a>
       <a href="javascript:;" class="orgClear" onClick="$('guestDept').value='';$('guestDeptName').value='';">清空</a>
  	  </td>
  	</tr>   
  	<tr>
  		<td nowrap class="TableContent">备注：</td>
      <td class="TableData" colspan="3">
      	<textarea cols="40" name="guestNote" id="guestNote" rows="2" style="overflow-y:auto;" class="BigInput" wrap="yes"></textarea>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td nowrap colspan="5">
        <input type="button" value="查询" class="BigButton" onclick="toSearch();">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">
      </td>
    </tr>
  </table>
</form>
</body>
</html>

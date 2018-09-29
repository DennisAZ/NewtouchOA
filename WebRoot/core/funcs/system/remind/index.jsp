<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>短信提醒设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
var total = "";
var sum1 = "";
var sum2 = "";
var sum3 = "";
var check1 = true;
var check2 = true;
var check3 = true;
var flag = 0;
var seqIdRemind = "";
var privFlag = "";  //用来标识该用户有没有权限使用手机短信提醒　１可以
function doInit(){
  remindPriv();
  var url = "<%=contextPath%>/yh/core/funcs/system/remind/act/YHRemindAct/getRemind.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    bindJson2Cntrl(rtJson.rtData);
    var valStr = document.getElementById("paraValue").value;
    seqIdRemind = document.getElementById("seqId").value;
    //alert(seqId);
    var str = valStr.split('|');
    for (var i = 0; i < str.length; i++) {
      sum1 = str[0] || "";
      sum2 = str[1] || "";
      sum3 = str[2] || "";
    }
    var day = document.getElementById("paraName").value;
  } else {
    alert(rtJson.rtMsrg); 
  }
  
  var url = "<%=contextPath%>/yh/core/funcs/system/remind/act/YHRemindAct/getRemindCode.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    bindJson2Cntrl(rtJson.rtData);
    
    var table = document.getElementById("tbody");
    var tr = document.createElement("tr");
    tr.className = "TableHeader";
    tr.align = "center";
    
  	var td = document.createElement("td");
    td.width = "150";
    td.innerHTML = "模块名称";
  	tr.appendChild(td);
  	
  	var td3 = document.createElement("td");
  	td3.innerHTML = "内部短信允许提醒&nbsp;";
  	var a = document.createElement("a"); 
  	a.href = "javascript:selectAll(3);";
  	a.className = "small";
  	a.innerHTML = "全选";
  	td3.appendChild(a);
  	tr.appendChild(td3);

  	var td1 = document.createElement("td");
  	td1.innerHTML = "内部短信默认提醒&nbsp;";
  	var a1 = document.createElement("a"); 
  	a1.href = "javascript:selectAll(1);";
  	a1.className = "small";
  	a1.innerHTML = "全选";
  	td1.appendChild(a1);
  	tr.appendChild(td1);

  	var td2 = document.createElement("td");
  	td2.innerHTML = "手机短信默认提醒&nbsp;";
  	var a2 = document.createElement("a"); 
  	a2.href = "javascript:selectAll(2);";
  	a2.className = "small";
  	a2.innerHTML = "全选";
  	td2.appendChild(a2);
  	tr.appendChild(td2);
  	table.appendChild(tr);
    var num = 0;
    for (var i = 0; i < rtJson.rtData.length; i++) {
      //show1td.className = (i % 2 == 0) ? "TableLine1" : "TableLine2";
     // total += rtJson.rtData[i].codeNo+",";
      if (rtJson.rtData[i].codeNo == "10" || rtJson.rtData[i].codeNo == "23" || rtJson.rtData[i].codeNo == "24") {
        continue;
      }
      num++;
      total += rtJson.rtData[i].codeNo+",";
      var tr2 = document.createElement("tr");
      table.appendChild(tr2);
      tr2.className = "TableLine2";
      tr2.align = "center";
      var td = document.createElement("td");
      var b= document.createElement("b");
      td.innerHTML = rtJson.rtData[i].codeName;
      td.appendChild(b);
      tr2.appendChild(td);
      
      var td3 = document.createElement("td");
      td3.className = (num % 2 == 0) ? "TableLine1" : "TableLine2";
      var input = document.createElement("input");
      input.setAttribute("type", "checkbox");
      input.setAttribute("id", "SMS3_REMIND_" + rtJson.rtData[i].codeNo);
      input.setAttribute("name", "SMS3_REMIND_" + rtJson.rtData[i].codeNo);
      input.setAttribute("value", rtJson.rtData[i].codeNo);
      td3.appendChild(input);
      tr2.appendChild(td3);
      input.checked = function(){
        var sumStr3 = sum3.split(','); 
        for (var x = 0; x < sumStr3.length - 1; x++) {
          if (rtJson.rtData[i].codeNo == sumStr3[x]) {
            return true;
          }
        }
           return false;
        }();
      check3 = check3 && input.checked;
      var td1 = document.createElement("td");
      td1.className = (num % 2 == 0) ? "TableLine1" : "TableLine2";
      var input = document.createElement("input");
      input.setAttribute("type", "checkbox");
      input.setAttribute("id", "SMS1_REMIND_" + rtJson.rtData[i].codeNo);
      input.setAttribute("name", "SMS1_REMIND_" + rtJson.rtData[i].codeNo);
      input.setAttribute("value", rtJson.rtData[i].codeNo);
      td1.appendChild(input);
      tr2.appendChild(td1);
      input.checked = function(){
        var sumStr = sum1.split(','); 
        for(var y = 0; y < sumStr.length - 1; y++){
          if(rtJson.rtData[i].codeNo == sumStr[y]){
            return true;
          }
        }
           return false;
        }();
      check1 = check1 && input.checked;
      if(privFlag != 1){
        
      }
      var td2 = document.createElement("td");
      td2.className = (num % 2 == 0) ? "TableLine1" : "TableLine2";
      input = document.createElement("input");
      input.setAttribute("type", "checkbox");
      input.setAttribute("id", "SMS2_REMIND_" + rtJson.rtData[i].codeNo);
      input.setAttribute("name", "SMS2_REMIND_" + rtJson.rtData[i].codeNo);
      input.setAttribute("value", rtJson.rtData[i].codeNo);
      //var ids = "SMS2_REMIND_" + rtJson.rtData[i].codeNo;
      td2.appendChild(input);
      tr2.appendChild(td2);
      //$(ids).style.disabled = true;
      input.checked = function(){
        var sumStrs = sum2.split(','); 
        for(var z = 0; z < sumStrs.length - 1; z++){
          if(rtJson.rtData[i].codeNo == sumStrs[z]){
            return true;
          }
        }
           return false;
        }();
      check2 = check2 && input.checked;
      //table.appendChild(tr2);
      //$(ids).style.disabled = true;
    }
    
    var table2 = document.getElementById("buttonDiv");
    var tr5 = document.createElement("tr");
    table2.appendChild(tr5);
    tr.className = "TableControl";
    tr.align = "center";
    var td5 = document.createElement("td");
    td.colspan = "3";
    var input = document.createElement("input");
    input.setAttribute("type", "button");
    input.setAttribute("id", "button");
    input.setAttribute("name", "button");
    input.setAttribute("value", "保存");
    input.className = "BigButton";
    input.onclick = function(){
      commitRemind();
    }
    td5.appendChild(input);
    tr5.appendChild(td5);
    
  }else {
    alert(rtJson.rtMsrg); 
  }
}

function remindPriv(){
  var url = "<%=contextPath%>/yh/core/funcs/system/remind/act/YHRemindAct/getRemindPriv.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    privFlag = rtJson.rtData;
  }else {
    alert(rtJson.rtMsrg); 
  }
}

function selectAll(t){
  var inputArray = document.getElementsByTagName("INPUT");
  var checks = true;
  for (var i = 0; i < inputArray.length; i++) {
    if (t == 1 && (inputArray[i].name.substr(0,12) != "SMS1_REMIND_" || inputArray[i].type != "checkbox")) {
      continue;
    }
    if (t == 2 && (inputArray[i].name.substr(0,12) != "SMS2_REMIND_" || inputArray[i].type != "checkbox")) {
      continue;
    }
    if (t == 3 && (inputArray[i].name.substr(0,12) != "SMS3_REMIND_" || inputArray[i].type != "checkbox")) {
      continue;
    }
    if (inputArray[i].checked) {
      checks = checks && inputArray[i].checked;
    } else{
      checks = checks && inputArray[i].checked;
    }
  }
  for (var i = 0; i < inputArray.length; i++) {
    if (t == 1 && (inputArray[i].name.substr(0,12) != "SMS1_REMIND_" || inputArray[i].type != "checkbox")) {
      continue;
    }
    if (t == 2 && (inputArray[i].name.substr(0,12) != "SMS2_REMIND_" || inputArray[i].type != "checkbox")) {
      continue;
    }
    if (t == 3 && (inputArray[i].name.substr(0,12) != "SMS3_REMIND_" || inputArray[i].type != "checkbox")) {
      continue;
    }
    if (checks == true) {
      inputArray[i].checked = false;
    } else{
      inputArray[i].checked = true;
    }
  }
}

function selectAlls(t){
  var inputArray = document.getElementsByTagName("INPUT");
  
  var checkStr = checkedAll(inputArray, t);
  if (t == 1) {
    check = check1;
    check1 = !check1;
  }else if (t == 2) {
    check = check2;
    check2 = !check2;
  }else if (t == 3){
    check = check3;
    check3 = !check3;
  }
  
  for (var i = 0; i < inputArray.length; i++) {
    if (t == 1 && (inputArray[i].name.substr(0,12) != "SMS1_REMIND_" || inputArray[i].type != "checkbox")) {
      continue;
    }
    if (t == 2 && (inputArray[i].name.substr(0,12) != "SMS2_REMIND_" || inputArray[i].type != "checkbox")) {
      continue;
    }
    if (t == 3 && (inputArray[i].name.substr(0,12) != "SMS3_REMIND_" || inputArray[i].type != "checkbox")) {
      continue;
    }
    if (check == true) {
      inputArray[i].checked = false;
    } else{
      inputArray[i].checked = true;
    }
  }

}

function commitRemind(){
  //alert(total);
  var remind1 = "";
  var remind2 = "";
  var remind3 = "";
  var totalStr = total.split(',');
  for(var i = 0; i < totalStr.length-1; i++){
    var sumStr = totalStr[i];
    var sms2 = document.getElementById("SMS2_REMIND_"+sumStr).checked;
    if (sms2 == true) {
      remind2 += document.getElementById('SMS2_REMIND_'+sumStr).value+",";
    }
  }
  for(var i = 0; i < totalStr.length-1; i++){
    var sumStr = totalStr[i];
    var sms1 = document.getElementById("SMS1_REMIND_"+sumStr).checked;
    if (sms1 == true) {
      remind1 += document.getElementById('SMS1_REMIND_'+sumStr).value+",";
    }
  }
  for(var i = 0; i < totalStr.length-1; i++){
    var sumStr = totalStr[i];
    var sms3 = document.getElementById("SMS3_REMIND_"+sumStr).checked;
    if (sms3 == true) {
      remind3 += document.getElementById('SMS3_REMIND_'+sumStr).value+",";
    }
  }
  var remindSum = remind1+"|"+remind2+"|"+remind3;
  var url = "<%=contextPath%>/yh/core/funcs/system/remind/act/YHRemindAct/updateRemind.act?remindSum="+remindSum+"&seqIdRemind="+seqIdRemind;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    location = "<%=contextPath %>/core/funcs/system/remind/submit.jsp";
    //alert(rsText);
  }else{
    //alert(rtJson.rtMsrg); 
  }
}
</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/sys.gif" align="absmiddle"><span class="big3">&nbsp;短信提醒设置</span>
    	<span class="small1">&nbsp;选中某模块默认提醒，则在模块界面中的短信发送选项，会默认自动选中</span>
    </td>
  </tr>
</table>
<br>
<div align="center">
<table class="TableList" align="center">
<tbody id="tbody">
</tbody>
</table>
<form name="form1" id="form1" method="post">
<input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.system.remind.data.YHRemind.java"/>
<input type="hidden" name="seqId" id="seqId" value="">
<input type="hidden" name="paraName" id="paraName" value="">
<input type="hidden" name="paraValue" id="paraValue" value="">
<table>
<tbody id="buttonDiv">
</tbody>
</table>
</form>
</div>
</body>
</html>
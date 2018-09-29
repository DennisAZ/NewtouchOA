<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>调整角色排序</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/views.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/grid.js" ></script>
<script type="text/javascript">

function doInit(){
  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHUserPrivAct/getSelectData.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    //bindJson2Cntrl(rtJson.rtData);
    //alert(rsText);
	var select = document.getElementById("selectMul");
	for(var i = 0; i < rtJson.rtData.length; i++) {
	  var option = document.createElement("option");
	  option.no = rtJson.rtData[i].privNo;
	  option.value = rtJson.rtData[i].seqId;
	  option.innerHTML = rtJson.rtData[i].privName;
	  select.appendChild(option);
	}
  }else{
	alert(rtJson.rtMsrg); 
  }
}

/**
 * 上移
 */
function selectUp(){
  var selCount = 0;
  var selectMul = document.getElementById("selectMul");
  var options = document.getElementsByTagName("option");
  for(var i = selectMul.options.length - 1; i >= 0; i--){
    if(selectMul.options[i].selected)
      selCount++;
  }
  if(selCount == 0){
    alert("调整顺序时，请选择其中一项！");
    return;
  }else if(selCount > 1){
    alert("调整顺序时，只能选择其中一项！");
    return;
  }
  
  var x = selectMul.selectedIndex;
  //var sel = selectMul.options[selectMul.selectedIndex-1];
  var sel = selectMul.selectedIndex - 1;
  if(x > 0){
    var cur_no = selectMul.options[x].no;           //自身的privNo
    var myOption = document.createElement('option');
    myOption.text = selectMul.options[x].text;      //获得name
    myOption.value = selectMul.options[x].value;    //对映的SEQ_ID
    myOption.no = selectMul.options[x - 1].no;      //获得上一个的privNo
    //selectMul.insertBefore(myOption, sel);
    selectMul.add(myOption, sel);
    selectMul.remove(x + 1);
    selectMul.options[x - 1].selected = true; 
    selectMul.options[x].no = cur_no;
  }
}

/**
 * 下移
 */
function selectDown(){
  var selCount = 0;
  var selectMul = document.getElementById("selectMul");
  var options = document.getElementsByTagName("option");
  for (var i = selectMul.options.length-1; i >= 0; i--){
    if(selectMul.options[i].selected)
       selCount++;
  }
  if(selCount == 0){
    alert("调整顺序时，请选择其中一项！");
    return;
  }else if(selCount > 1){
    alert("调整顺序时，只能选择其中一项！");
    return;
  }
  var i = selectMul.selectedIndex;
  //var sel = selectMul.options[selectMul.selectedIndex + 2];
  var sel = selectMul.selectedIndex + 2;
  if(i != selectMul.options.length - 1){
    var cur_no = selectMul.options[i].no;       //自身的privNo
    var myOption = document.createElement('option');
    myOption.text = selectMul.options[i].text;
    myOption.value = selectMul.options[i].value;
    myOption.no = selectMul.options[i + 1].no;

    //selectMul.insertBefore(myOption, sel);
    selectMul.add(myOption, sel);
    selectMul.remove(i);
    selectMul.options[i + 1].selected = true;
    selectMul.options[i].no = cur_no;
  }
}

function Submit(){
   var privSeqId = ""; 
   var privNo = "";
   var selectMul = document.getElementById("selectMul");
   for (var i = 0; i < selectMul.options.length; i++){
     privSeqId += selectMul.options[i].value+",";
     privNo += selectMul.options[i].no+",";
   }
   var url = "<%=contextPath%>/yh/core/funcs/person/act/YHUserPrivAct/updateSelectPriv.act?privSeqId=" + privSeqId + "&privNo=" + privNo;
   var rtJson = getJsonRs(url);
   if(rtJson.rtState == "0"){
     //bindJson2Cntrl(rtJson.rtData);
     //alert(rsText);
     alert(rtJson.rtMsrg); 
     location.href = '<%=contextPath %>/core/funcs/userpriv/manage.jsp';
   }else{
 	alert(rtJson.rtMsrg); 
   }
}

</script>
</head>
<body class="" topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 调整角色序号高低</span>
    </td>
  </tr>
</table>
<br>
<table class="TableBlock" width="400" height="300" align="center">
  <tr class="TableHeader">
    <td align="center"><b>角色</b></td>
    <td align="center">排序</td>
  </tr>
  <tr class="TableData">
    <td valign="top" align="center">
    <select  name="selectMul" id="selectMul" MULTIPLE style="width:230px;height:300px">
    </select>
    </td>
    <td align="center" style="width:60px;">
      <input type="button" class="SmallInput" value=" ↑ " onclick="selectUp();">
      <br><br>
      <input type="button" class="SmallInput" value=" ↓ " onclick="selectDown();">
    </td>
  </tr>
  <tr class="TableControl">
    <td align="center" valign="top" colspan="4">
      <input type="button" class="BigButton" value="保 存" onclick="Submit();">&nbsp;&nbsp;
    </td>
  </tr>
</table>
</body>
</html>
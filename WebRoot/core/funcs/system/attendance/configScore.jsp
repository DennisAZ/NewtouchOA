<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>考核分数设定</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/fillRegister/js/util.js"></script>
<script type="text/javascript">
var requestURL = "<%=contextPath%>/yh/subsys/oa/fillRegister/act/YHAttendTimeAct";
var dutyId = ${param.seqId};
function doInit(){
  $("dutyId").value = ${param.seqId};
  var url = requestURL + "/getAttendTimeList.act?dutyId=${param.seqId}";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var table=new Element('table',{ "width":"70%","class":"TableList","align":"center"})
        .update("<tbody id='tbody'><tr class='TableHeader'>"
           +"<td nowrap width='' align='center'>迟到/早退时间(分钟)</td>"
           +"<td nowrap width='' align='center'>扣除分值</td>"				
           +"<td nowrap width='' align='center'>操作</td></tr><tbody>");
   $('listDiv').appendChild(table);
   for(var i = 0; i < rtJson.rtData.length; i++){
     var seqId = rtJson.rtData[i].seqId;
     var minLateTime = rtJson.rtData[i].minLateTime;
     var maxLateTime = rtJson.rtData[i].maxLateTime;
     var score = rtJson.rtData[i].score
     var re1 = /\'/gi; 
     minLateTime = minLateTime.replace(re1,"&lsquo;"); 
     var tr=new Element('tr',{'class':'TableLine1'});
     table.firstChild.appendChild(tr);
       tr.update("<td align='center'><input type='text' maxlength='3'  name='minLateTime_"+seqId+"' id='minLateTime_"+seqId+"' class='SmallInput' size='3' value='" + minLateTime + "'> ～  <input type='text' name='maxLateTime_" + seqId + "' id='maxLateTime_" + seqId + "' size='3' class='SmallInput' maxlength='3' value='" + maxLateTime + "'></td><td align='center'><input type='text' name='score_"+seqId+"' id='score_"+seqId+"' class='SmallInput' size='3' maxlength='5' value='" + score + "'>"	
       + "</td><td align='center'>"
       + "<a href="
       + "javascript:doEditItem('" + seqId + "');"
       + ">修改</a>&nbsp;&nbsp;"
       + "<a href="	
       + "javascript:deleteItem('" + seqId + "');"
       + ">删除</a>&nbsp;&nbsp;"
       + "</td>"					
       );
    }
  }else{
    alert(rtJson.rtMsrg); 
  }
}
function doSubmit(){
  if(checkForm()){
    var pars = Form.serialize($('form1'));
    var url = requestURL + "/addAttendTimeItem.act?dutyId=${param.seqId}";
    var rtJson = getJsonRs(url,pars);
    if(rtJson.rtState == "0"){
      window.location.reload();
    }else{
      alert(rtJson.rtMsrg); 
    }
  }
}
function checkForm(){
  var minLateTime = $("minLateTime").value;
  var maxLateTime = $("maxLateTime").value;
  var score = $("score").value;
  if(minLateTime == ""){ 
    alert("迟到/早退时间最小值不能为空！！！");
    $("minLateTime").focus();
    return false;
  }
  if(maxLateTime == ""){ 
    alert("迟到/早退时间最大值不能为空！！！");
    $("maxLateTime").focus();
    return false;
  }
  if(score == ""){ 
    alert("扣分值不能为空！！！");
    $("score").focus();
    return false;
  }
  if(minLateTime){
    if(!isNumbers(minLateTime)){
     alert("最小时间范围应为数字");
     $("minLateTime").focus();
     $("minLateTime").select();  
     return false;
    }
  }
  if(maxLateTime){
    if(!isNumbers(maxLateTime)){
     alert("最大时间范围应为数字");
     $("maxLateTime").focus();
     $("maxLateTime").select();  
     return false;
    }
  }
  if(score){
    if(!isNumbers(score)){
     alert("扣分值应为数字");
     $("score").focus();
     $("score").select();  
     return false;
    }
  }
  var minValue = parseInt(minLateTime);
  var maxValue = parseInt(maxLateTime);
  if(minValue >= maxValue){
    alert("时间最小值不能大于或等于最大时间值！");
    $("minLateTime").focus();
    $("minLateTime").select(); 
    return false;
  }
	return true;
}
function doEditItem(seqId){
	var minLateTime = $("minLateTime_"+seqId).value.trim();
	var maxLateTime = $("maxLateTime_"+seqId).value.trim();
	var score = $("score_"+seqId).value;
	if(minLateTime == ""){ 
    alert("迟到时间最小值不能为空！！！");
    $("minLateTime_"+seqId).focus();
    return ;
  }
	if(maxLateTime == ""){ 
    alert("迟到时间最大值不能为空！！！");
    $("maxLateTime_"+seqId).focus();
    return ;
  }
  if(score == ""){ 
    alert("扣分值不能空！！！");
    $("score_"+seqId).focus();
    return ;
  }
  if(minLateTime){
    if(!isNumbers(minLateTime)){
     alert("最小时间范围应为数字");
     $("minLateTime_"+seqId).focus();
     $("minLateTime_"+seqId).select();  
     return ;
    }
  }
  if(maxLateTime){
    if(!isNumbers(maxLateTime)){
     alert("最大时间范围应为数字");
     $("maxLateTime_"+seqId).focus();
     $("maxLateTime_"+seqId).select();  
     return ;
    }
  }
  if(score){
    if(!isNumbers(score)){
     alert("扣分值应为数字");
     $("score_"+seqId).focus();
     $("score_"+seqId).select();  
     return ;
    }
  }
  var minValue = parseInt(minLateTime);
  var maxValue = parseInt(maxLateTime);
  if(minValue >= maxValue){
    alert("时间最小值不能大于或等于最大时间值！");
    $("minLateTime_"+seqId).focus();
    $("minLateTime_"+seqId).select(); 
    return;
  }
	var url = requestURL + "/updateAttendTimeItem.act?dutyId=${param.seqId}&seqId=" + seqId + "&score=" + score + "&minLateTime=" + minLateTime + "&maxLateTime=" + maxLateTime;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    alert("修改成功!");
    window.location.reload();
  }else{
    alert(rtJson.rtMsrg); 
  }
	
}
/**
 * 删除一条记录
 * @param seqId
 * @return
 */
function deleteItem(seqId){
  if(!confirm("您真的要删除该条选项吗？注意：该操作不可恢复!")) {
   return ;
  }  
  var url = requestURL + "/delAttendTimeItem.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}



</script>
</head>
<body onload="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
	<tr>
		<td class="Big"><img src="<%=imgPath%>/views/attendance.gif" align="middle" width=22 height=20><span class="big3"> 考核分数设定</span>
		</td>
	</tr>
</table>
<div id="listDiv" align="center"></div>
<form id="form1" name="form1" action="" method="post">
<table width="70%" align="center" class="TableList">
	<tfoot class="TableFooter">
	<td colspan="4">迟到/早退时间:
		<INPUT type="text"name="minLateTime" id="minLateTime" class=SmallInput size="3" maxlength="3"> ～
    <INPUT type="text" name="maxLateTime" id="maxLateTime" class=SmallInput size="3" maxlength="3">&nbsp;&nbsp;扣分值:
		<INPUT type="text" name="score" id="score" class=SmallInput size="3" maxlength="5">
        <input type="hidden" name="dutyId" id="dutyId" value="">
		<input type="button" name="submit" class="SmallButton" value="添加" onclick="doSubmit();">
        <input type="button" value="返回" class="SmallButton" onclick="history.go(-1);">
  </td>
	</tfoot>
</table>
</form>


</body>
</html>
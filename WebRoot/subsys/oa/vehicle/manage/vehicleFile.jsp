<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if(seqId == null){
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>车辆档案</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
var upload_limit=1,limit_type="php,php3,php4,php5,";
var seqId = '<%=seqId%>';
var  selfdefMenu = {
  	office:["downFile","dump","read","deleteFile"], 
    img:["downFile","dump","play","deleteFile"],  
    music:["downFile","dump","play","deleteFile"],  
    video:["downFile","dump","play","deleteFile"], 
    others:["downFile","dump","deleteFile"]
	}

function doOnloadFile(seqId){
  var attr = $("attr");
  attachMenuSelfUtil(attr,"vehicle",$('attachName').value ,$('attachId').value, '','',seqId,selfdefMenu);
}
function doOnload(){
  var url= "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleAct/selectVehicleById.act?seqId="+seqId;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ;
  }
  var prc =rtJson.rtData;
  if(prc.seqId){
    var carUserName = prc.carUserName;
    var carUser = prc.carUser;
    $("carUser").value = prc.carUser;
    $("carUserName").value = prc.carUserName;
    $("history").value = prc.history;
    $("attachId").value = prc.attachId;
    $("attachName").value = prc.attachName;
    doOnloadFile(seqId);
  }

}

//浮动菜单文件的删除
function deleteAttachBackHand(attachName,attachId,attrchIndex){
	var url= "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleAct/deleleFile.act?attachId=" + attachId +"&attachName=" + encodeURIComponent(attachName) + "&seqId=" + attrchIndex;
	 var rtJson = getJsonRs(url); 
	  if(rtJson.rtState == "1"){
	    alert(rtJson.rtMsrg); 
	    return ;
	  }else{
	   prcsJson=rtJson.rtData;
	   var updateFlag=prcsJson.updateFlag;
	   if(updateFlag=='1'){
	     return true;
	   }else{
		  return false;
	   }
	}
}
function doSave(){
  $("form1").submit();
  alert("保存成功！");
}
</script>
</head>
 
<body topmargin="5" onload="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/vehicle.gif" HEIGHT="20"><span class="big3"> 车辆档案</span>
    </td>
  </tr>
</table>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleAct/updateFile.act" method="post" name="form1" id="form1">
<table class="TableBlock" align="center" width="80%">
    <tr>
      <td nowrap class="TableContent" width="20%">当前使用人：</td>
      <td class="TableData">
       <input type="hidden" name="carUser" id="carUser" value=""> 	
        <input type="text" name="carUserName" id="carUserName"  size="13" class="BigStatic" value="" readonly>
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['carUser','carUserName']);">添加</a>
         <a href="javascript:;" class="orgClear" onClick="$('carUser').value='';$('carUserName').value='';">清空</a>
	 	 </tr>
	 <tr>
      <td nowrap class="TableContent" width="20%"> 历史记录：</td>
      <td class="TableData">
       <textarea name="history" id="history" class="BigInput" cols="65" rows="5"></textarea>&nbsp </td>	  	
    </tr>
	<tr>
      <td nowrap class="TableContent" width="20%"> 附件：</td>
      <td class="TableData">
         <span id="attr"></span>
         <input type="hidden" id="attachId"  name="attachId" value="">
	     <input type="hidden" id="attachName"  name="attachName" value="">	
	          <input type = "hidden" id="attachmentName" name="attachmentName" value=""></input>
       <input type = "hidden" id="attachmentId" name="attachmentId" value=""></input>
         <input type="hidden" id="moduel" name="moduel" value="vehicle">
  	     <script>ShowAddFile();</script>
	   </td>
    </tr>
	<tr class="TableControl">
      <td nowrap colspan="2" align="center">
		<input type="hidden" value="<%=seqId %>" name="seqId" id="seqId">
        <input type="button" value="保存" class="BigButton" onclick="doSave();">&nbsp;&nbsp;
		<input type="button" value="关闭" class="BigButton" onclick="window.close();">&nbsp;&nbsp;
      </td>
    </tr>
</table>
</form>
</body>
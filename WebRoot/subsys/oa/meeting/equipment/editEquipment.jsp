<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String seqId = request.getParameter("seqId");
	if(YHUtility.isNullorEmpty(seqId)){
		seqId = "0";
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑设备</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/applayMeetinglogic.js"></script>
<script type="text/javascript">
var requestUrl = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingEquipmentAct";
var fckContentStr = "";


function doInit(){
	getSecretFlag("MEETING_EQUIPMENT","groupNo");
	var url = requestUrl + "/getEquipmentInfoById.act"
	var rtJson = getJsonRs(url,"seqId=<%=seqId %>" );
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
    bindJson2Cntrl(rtJson.rtData);
    getMRoomName(data.mrId);
    if(data.groupYn == "1"){
    	getSecretFlag("MEETING_EQUIPMENT","groupNo",data.groupNo);
    	showType();
    }
    fckContentStr = data.remark;
	}
}
/**
 * 获取会议室名称
 */
function getMRoomName(extValue){
	var url = requestUrl + "/getMRoomName.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
	  alert(rtJson.rtMsrg); 
	  return ;
	}
  var prcs = rtJson.rtData;
	var selects = document.getElementById("mrId");
	for(var i=0;i<prcs.length;i++){
	  var prc = prcs[i];
	  var option = document.createElement("option"); 
	  option.value = prc.value; 
	  option.innerHTML = prc.text; 
	  selects.appendChild(option);	  
	  if(extValue && (extValue == prc.value)){
      option.selected = true;
    }
	}
}

function checkForm(){
	if($("equipmentNo").value.trim() == "" ){
		alert("设备编号不能为空！");
		$("equipmentNo").focus();
		$("equipmentNo").select();
    return (false);
	}
	
	if($("equipmentName").value.trim() == "" ){
		alert("设备名称/型号不能为空！");
		$("equipmentName").focus();
		$("equipmentName").select();
    return (false);
	}
	return true;
}

function doSubmit(){
	if(checkForm()){
		var oEditor = FCKeditorAPI.GetInstance('fileFolder');
		var fckContentStr = encodeURIComponent(oEditor.GetXHTML());
		var pars = $("form1").serialize() + "&remark=" + fckContentStr; 
		var url = requestUrl + "/updateEquipmentById.act";
		 //var pars = Form.serialize($('form1'));
	  var json = getJsonRs(url,pars);
	  if(json.rtState == "0"){
	    window.location.href = "<%=contextPath %>/subsys/oa/meeting/equipment/newFileWarn.jsp?optFlag=edit";
	  }else{
	    alert("修改失败");
	  }
	}
}

/**
 * 同类设备名称
 */
function showType(){
	var obj = document.getElementById("show");
	if(obj.style.display=='none'){
		obj.style.display=''; 
	}else{
		obj.style.display='none';	
	} 
}

function FCKeditor_OnComplete( editorInstance ) {
  editorInstance.SetData( fckContentStr ) ;
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif" HEIGHT="20"><span class="big3"> 新建设备</span>
    </td>
  </tr>
</table>
 
<form  method="post" name="form1" id="form1">
<table align="center" width="660" class="TableBlock">
   <tr>
     <td nowrap class="TableData">设备编号：<font style='color:red'>*</font></td>
     <td class="TableData">
       <input type="text" name="equipmentNo" id="equipmentNo" maxlength="100" class="BigInput" value="">
     </td>
     <td nowrap class="TableData">设备状态：</td>
     <td class="TableData">
       <select name="equipmentStatus" id="equipmentStatus">
       	 <option value="1" >可用</option>
       	 <option value="0" >不可用</option>       	 
       </select>
     </td>     
   </tr>
   <tr>
     <td nowrap class="TableData">设备名称/型号：<font style='color:red'>*</font></td>
     <td class="TableData">
       <input type="text" name="equipmentName" id="equipmentName" size="30" maxlength="100" class="BigInput" value="">
     </td>
     <td nowrap class="TableData">同类设备：</td>
     <td class="TableData">
       <select name="groupYn"  id="groupYn" onchange="showType();">
       	 <option value="0" >没有</option>
       	 <option value="1" >有</option>       	 
       </select>
     </td>        
   </tr>
   <tr id="show" style="display:none">
     <td nowrap class="TableData">同类设备名称：</td>
     <td class="TableData" colspan="3">
     	 <select name="groupNo" id="groupNo" >
       </select>
       &nbsp;同类设备名称可在“系统管理”->“分类代码管理”模块中设置。
     </td>      
   </tr>      
   <tr>
     <td nowrap class="TableData">所属会议室：</td>
     <td class="TableData" colspan="3">
    	<select name="mrId" id="mrId" >
      </select>
     </td>      
   </tr>
   <tr>
     <td nowrap class="TableData" colspan="4">设备描述：</td>
   </tr>   
   <tr id="EDITOR">
    <td class="TableData" colspan="4">
			<div>
		     <script language=JavaScript>    
		      var sBasePath = contextPath+'/core/js/cmp/fck/fckeditor/';
		      var oFCKeditor = new FCKeditor( 'fileFolder' ) ;
		      oFCKeditor.BasePath = sBasePath ;
		      oFCKeditor.Height = 200;
		      var sSkinPath = sBasePath + 'editor/skins/office2003/';
		      oFCKeditor.Config['SkinPath'] = sSkinPath ;
		      oFCKeditor.Config['PreloadImages'] =
		                      sSkinPath + 'images/toolbar.start.gif' + ';' +
		                      sSkinPath + 'images/toolbar.end.gif' + ';' +
		                      sSkinPath + 'images/toolbar.buttonbg.gif' + ';' +
		                      sSkinPath + 'images/toolbar.buttonarrow.gif' ;
		      //oFCKeditor.Config['FullPage'] = true ;
		       oFCKeditor.ToolbarSet = "fileFolder";
		      oFCKeditor.Value = '' ;
		      oFCKeditor.Create();
		     </script>
  		</div>    
    </td>
  </tr>
  <tr class="TableControl">
    <td nowrap colspan="4" align="center">
    	<input type="hidden" name="seqId" Value="<%=seqId %>">
    	<input type="hidden" name="ZHEFROM" Value="">
      <input type="button" value="确定" class="BigButton" onclick="doSubmit();">&nbsp;&nbsp;&nbsp;
    </td>
  </tr>
  </table>
</form>

</body>
</html>
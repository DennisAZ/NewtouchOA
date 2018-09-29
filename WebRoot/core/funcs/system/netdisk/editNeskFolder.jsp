<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqId=request.getParameter("seqId");
	String flag=request.getParameter("fileName");
	if(seqId==null){
	  seqId="";
	}	  
	
	String isDemo = YHSysProps.getString("IS_ONLINE_EVAL");
	if (isDemo == null) {
		isDemo = "";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑共享目录</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/ExchangeSelect.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script Language="JavaScript">
var isDemo="<%=isDemo%>";
var requestURL="<%=contextPath%>/yh/core/funcs/system/netdisk/act/YHNetdiskAct";	
function doInit(){
	var url=requestURL + "/getNetdiskFolderInfoById.act?seqId=<%=seqId%>";
	var json=getJsonRs(url);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	//alert(rsText);
	var prcsJson=json.rtData;
	if(prcsJson.length>0){
		$("diskNo").value=prcsJson[0].diskNo;
		$("diskName").value=prcsJson[0].diskName;
		$("diskPath").value=prcsJson[0].diskPath;
		$("spaceLimit").value=prcsJson[0].spaceLimit;
		$("orderBy").value = prcsJson[0].orderBy;
		$("ascDesc").value=prcsJson[0].ascDesc;		
	}	
}
function checkForm(){
	var reg1 = /[^\d]/g;
	var diskNo=$("diskNo").value.trim();
	var spaceLimit=$("spaceLimit").value.trim();
	if(diskNo.match(reg1) || diskNo<0){
		alert("排序号应为数字！");
		$("diskNo").focus();
		$('diskNo').select();		
		return false;
	}
	
	if($("diskName").value.trim()==""){
		alert("共享目录名称不能为空!");
		$("diskName").focus();
		$("diskName").select();
		return false;
	}
	if(checkStr($("diskName").value)){
		alert("不能包含有以下字符/\:*<>?\"|！");
		$("diskName").select();
		$("diskName").focus();
		return false;
	}
	if($("diskPath").value.trim()==""){
		alert("共享目录路径不能为空!");
		$("diskPath").focus();	
		$("diskPath").select();	
		return false;
	}
	if(isDemo == "1"){
		if(!checkDisk($("diskPath").value)){
			$("diskPath").focus();
			$('diskPath').select();
			return false;
		}
	}
	if(checklocation()==false){
		$("diskPath").focus();
		$('diskPath').select();		
		return false;
	}
	if(checkDiskPaht()!=1){
		return false;
	}
	
	if(spaceLimit.match(reg1) || spaceLimit<0){
		alert("最大容量应为整数！");
		$("spaceLimit").focus();
		$('spaceLimit').select();		
		return false;
	}else{
		var spaceSize=parseInt(spaceLimit)
		var limitSize=2147483647;
		if(spaceSize>limitSize){
			spaceSize=limitSize
		}
		//alert("spaceSize>"+spaceSize);
		$("spaceLimit").value=spaceSize;
		
	}
	return true;
}
function checkStr(str){ 
	var re=/["\/\\:*?"<>|]/; 
	return re.match(str); 
}
function checkDisk(diskPath){
	var pattern =/[Cc]\:[\/ | \\]{1}[^\:\?\"\>\<\*]*/;
	var notDiskC = pattern.test(diskPath);
	if(notDiskC){
		alert("目录路径不能为C盘!");
		return false;
	}
	return true;
}

function checklocation(){
	var file = $("diskPath").value;
	var pattern =/[\/ | \\]{1}[^\:\?\"\>\<\*]*/;
	//var pattern =/[A-Za-z]\:[\/ | \\]{1}[^\:\?\"\>\<\*]*/;
	var flag = pattern.test(file);
	if(!flag){
		alert("目录路径格式不正确!");		
		return false;
	}
	return true;
}

function checkDiskPaht(){
  var flag=0;
  var pathName = $("diskPath").value.trim();
  if(pathName){
    var pars = $('form1').serialize();
    var checkUrl = requestURL + "/checkDiskPath.act?seqId=<%=seqId%>";
    var rtJson = getJsonRs(checkUrl,pars);
    if(rtJson.rtState == '0'){
      var prcsJsonStr = rtJson.rtData;
  		var isHaveFlag = prcsJsonStr.isHaveFlag;
  		if(isHaveFlag==1){
			  $("errorSubject").innerHTML="<b style='color:red'>该共享目录路径已存在,请重新输入!";
				$("diskPath").focus();
				$("diskPath").select();
			}else{
			  flag=1;
			}
    }
  }
  return flag;
}
function clearErrSubjName(){
  $("errorSubject").innerHTML="";  
}

function sendForm(){
	if(checkForm()){
		var url = requestURL+"/updateNetdisFolderById.act?seqId=<%=seqId%>";
		var rtJson = getJsonRs(url,mergeQueryString($("form1")));
		if(rtJson.rtState == '0'){
			//alert(rsText);			
			var prcJson=rtJson.rtData;
			var isHaveName=prcJson.isHaveName;
			var createFlag=prcJson.createFlag;
			if(isHaveName==0){
				location.href="editWarn.jsp?flag=isHaveName&diskName="+$('diskName').value;
			}else if(createFlag==0){
				location.href="editWarn.jsp?flag=createFlag";
			}else{
				location.href="index.jsp";
			}			
		}else{
		 	alert(rtJson.rtMsrg); 
		}
	}	
}


</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/system/netdisk/images/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 编辑共享目录</span>
    </td>
  </tr>
</table>
<br>
<form action=""  method="post" name="form1" id="form1" >
<input type="hidden" value="yh.core.funcs.system.netdisk.data.YHNetdisk" name="dtoClass">
 <table class="TableBlock" width="500" align="center">
    <tr>
      <td nowrap class="TableData"> 排序号：</td>
      <td class="TableData">
        <input type="text" name="diskNo" id="diskNo" size="4" maxlength="8" class="BigInput">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 共享目录名称：</td>
      <td class="TableData">
        <input type="text" name="diskName" id="diskName" size="30" maxlength="100" class="BigInput">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 共享目录路径：</td>
      <td class="TableData">
        <input type="text" name="diskPath" id="diskPath" size="30" class="BigInput" onblur="clearErrSubjName();"> <br>OA服务器的文件路径，windows系统如 d:\soft，linux系统如/home
        <br><span id="errorSubject"></span>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 最大容量：</td>
      <td class="TableData">
        <input type="text" name="spaceLimit" id="spaceLimit" size="10" maxlength="8" class="BigInput" value="0"> MB (0为不限制)
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 默认排序：</td>
      <td class="TableData">
        <select name="orderBy" id="orderBy" class="BigSelect">
         <option value="nom">名称</option>
         <option value="taille">大小</option>
         <option value="type">类型</option>
         <option value="mod">最后修改时间</option>
        </select>
        <select name="ascDesc" id="ascDesc" class="BigSelect">
         <option value="0">升序</option>
         <option value="1">降序</option>
        </select>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="确定" onclick="sendForm();" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onClick="location='index.jsp'">
      </td>
    </tr>
  </table>
</form>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String isDemo = YHSysProps.getString("IS_ONLINE_EVAL");
	if (isDemo == null) {
		isDemo = "";
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建图片目录</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/ExchangeSelect.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var isDemo="<%=isDemo%>";
//alert(isDemo);

function checkForm(){
	if($("dept").value.trim()=="" && $("role").value.trim()=="" && $("user").value.trim()==""){
		alert("请至少指定一种发布范围!");
		return(false);
	}
	if($("picName").value.trim()==""){
		alert("图片目录名称不能为空!");
		$("picName").select();
		$("picName").focus();
		return false;
	}
	if(checkStr($("picName").value)){
		alert("不能包含有以下字符/\:*<>?\"|！");
		$("picName").select();
		$("picName").focus();
		return false;
	}
	
	if($("picPath").value.trim()==""){
		alert("图片目录路径不能为空!");
		$("picPath").select();
		$("picPath").focus();
		return false;
	}

	if(isDemo == "1"){
		if(!checkDisk($("picPath").value)){
			$("picPath").focus();
			$('picPath').select();
			return false;
		}
	}
	
	if(checklocation()==false){
		$("picPath").focus();
		$('picPath').select();
		return false;
	}
	return true;	
}

function checkStr(str){ 
	var re=/["\/\\:*?"<>|]/; 
	return str.match(re); 
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
	var file = $("picPath").value;
	var pattern =/[\/ | \\]{1}[^\:\?\"\>\<\*]*/;
	//var pattern =/[A-Za-z]\:[\/ || \\]{1}[^\:\?\"\>\<\*]*/
	var flag = pattern.test(file);
	if(!flag){
		alert("目录路径格式不正确!");		
		return false;
	}
	return true;
}
function sendForm(){
	var requestURL="<%=contextPath%>/yh/core/funcs/system/picture/act/YHPictureAct";
	if(checkForm()){
		var url = requestURL+"/addPicSortInfo.act";
		var rtJson = getJsonRs(url,mergeQueryString($("form1")));
		if(rtJson.rtState == '0'){
		//alert(rsText);
			var prcsJson= rtJson.rtData;
			if(prcsJson.isPath=="isNone"){
				location.href="newWarn.jsp?flag=isNone";
			}else{
				location.href="../index.jsp";
			}   
		}else{
		 	alert(rtJson.rtMsrg); 
		}
	}	
}
</script>
</head>
<body>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath%>/core/funcs/system/picture/images/sys_config.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 新建图片目录</span>
    </td>
  </tr>
</table>

<br>
<form action=""  method="post" name="form1" id="form1" onsubmit="return CheckForm();">
<input type="hidden" value="yh.core.funcs.system.picture.data.YHPicture" name="dtoClass">
 <table class="TableBlock"  width="85%" align="center">
    <tr>
      <td nowrap class="TableData">发布范围（部门）：</td>
      <td class="TableData">
        <input type="hidden" name="dept" id="dept">
        <textarea cols=40 name="deptDesc" id="deptDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectDept()">添加</a>
     		<a href="javascript:;" class="orgClear" onClick="$('dept').value='';$('deptDesc').value='';">清空</a>    
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">发布范围（角色）：</td>
      <td class="TableData">
        <input type="hidden" name="role" id="role" value="">
        <textarea cols=40 name="roleDesc" id="roleDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectRole();">添加</a>
      	<a href="javascript:;" class="orgClear" onClick="$('role').value='';$('roleDesc').value='';">清空</a> 
      </td>
   </tr>
   <tr>
      <td nowrap class="TableData">发布范围（人员）：</td>
      <td class="TableData">
        <input type="hidden" name="user" id="user" value="">
        <textarea cols=40 name="userDesc" id="userDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
      	<a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
      </td>
   </tr>
   <tr>
      <td nowrap class="TableData"> 图片目录名称：</td>
      <td class="TableData">
        <input type="text" name="picName" id="picName" size="36" maxlength="100" class="BigInput">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 图片目录路径：</td>
      <td class="TableData">
        <input type="text" name="picPath" id="picPath" size="36" class="BigInput">  说明：OA服务器的本地路径(如:D:\MYOA)
      </td>

    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="确定" onclick="sendForm();" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onClick="location='<%=contextPath %>/core/funcs/system/picture/index.jsp'">
      </td>
    </tr>
  </table>
</form>











</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqId=request.getParameter("seqId");
	if(seqId==null){
  	seqId="";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建子文件夹</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>

<script Language="JavaScript"> 
var requestURL="<%=contextPath%>/yh/core/funcs/system/filefolder/act/YHFileSortAct";
//alert('newFolder:<%=seqId%>');
function checkForm(){
  if(document.form1.sortName.value.trim()==""){
    alert("文件夹名称不能为空！");
    document.form1.sortName.focus();
    $('sortName').select();
		$('sortName').focus();
    return (false);
  }
  //alert(document.form1.sortName.value);
  var checkString=checkStr(document.form1.sortName.value.trim());
  if(checkString){
		alert("不能包含有以下字符/\:*<>?\"|！");
		document.form1.sortName.select();
		document.form1.sortName.focus();
		return (false);
	}
  return (true);
}

function checkStr(str){ 
	var re=/["\/\\:*?"<>|]/; 
	return str.match(re); 
}

function sendForm(){
	var seqId='<%=seqId%>';
	var url=requestURL + "/addSubFolderInfo.act?seqId="+seqId;
	if(checkForm()){
		var rtJson = getJsonRs(url,mergeQueryString($("form1")));
  	if(rtJson.rtState == '0'){
    	bindJson2Cntrl(rtJson.rtData);
    	prcsJson = rtJson.rtData;
    	var isHaveFlag=prcsJson[0].isHaveFlag;
    	
    	//alert("rtJson.rtData:"+rtJson.rtData[0].nodeId+"  sortName:"+rtJson.rtData[0].sortName);
    	
    	if(isHaveFlag==1){
    		$("formDiv").hide();
		  	$("folderError").innerHTML=$("sortName").value + " 已存在";  	
				$("errorDir").show();
      }else{
       	var curTree = parent.frames["file_tree"].tree;
      	//alert("curTree:"+curTree);
      	var curNode = curTree.getCurrNode();
      	if(curNode == null){
      	  curNode=curTree.getNode(seqId);
        }
      	var nodeId = prcsJson[0].nodeId;
      	var nodeName = prcsJson[0].sortName;
      	//alert("nodeId :"+nodeId);
      	//alert("nodeName :"+nodeName);
      	//var opt = "";
      	var imgAddress = "<%=imgPath%>/dtree/folder.gif";
      	var node = {
      			parentId:curNode.nodeId,
      			nodeId:nodeId,
      			name:nodeName,
      			isHaveChild:0,
      			extData:'',
      			imgAddress:imgAddress
      	}
      	curTree.addNode(node);   
      	history.back(); 
      }
    	
    }else{
  	 	  alert(rtJson.rtMsrg); 
  	}	
	}
	
}

function toBack(){
	//alert("tt");
	$("errorDir").hide();
	$("formDiv").show();
	$("sortName").focus();
	$("sortName").select();
}
</script>
</head> 
<body class="" topmargin="5" onload="document.form1.sortName.focus();"> 
<div id="formDiv" style="display: ">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/filefolder/images/folder_new.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3"> 新建子文件夹</span>
    </td>
  </tr>
</table>
 
<br>
<form action=""  method="post" name="form1" id="form1" onsubmit="">
<input type="hidden" value="yh.core.funcs.system.filefolder.data.YHFileSort" name="dtoClass">
<table class="TableBlock" width="450" align="center">
    <tr>
      <td nowrap class="TableData"> 排序号：</td>
      <td class="TableData">
        <input type="text" name="sortNo" id="sortNo" size="20" maxlength="20" class="BigInput">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 文件夹名称：</td>
      <td class="TableData">
        <input type="text" name="sortName" id="sortName" size="25" maxlength="100" class="BigInput">
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="确定" onclick="sendForm();" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onClick="javascript:location='<%=contextPath%>/core/funcs/filefolder/folder.jsp?seqId=<%=seqId%>'">
      </td>
    </tr>
  </table>
</form>
</div>


<div id="errorDir" style="display: none">
<table class="MessageBox" align="center" width="310">
	<tr>
		<td class="msg error">
		<h4 class="title">错误</h4>
		<div class="content" style="font-size: 12pt">文件夹 <span id="folderError"> </span> </div>
		</td>
	</tr>
</table>
<br>
<center><input type="button" class="BigButton" value="返回"	onclick="toBack();"></center>&nbsp;&nbsp;
</div>



</body>

</html>
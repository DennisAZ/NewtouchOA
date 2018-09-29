<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqId=request.getParameter("seqId");
	String parentId=request.getParameter("parentId");
	if(seqId==null){
  	seqId="";
	}	
	//获取登录用户的信息
	YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
	String userName=loginUser.getUserName();
	int deptId=loginUser.getDeptId();
	int userSeqId=loginUser.getSeqId();
	String roleId=loginUser.getUserPriv();
	String userId=loginUser.getUserId();
	
	//获取复制、剪切的session
	String actionFlag = (String) request.getSession().getAttribute("action");
	//System.out.println("actionFlag:"+actionFlag);
	if(actionFlag==null){
	  actionFlag="";
	}
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="yh.core.funcs.person.data.YHPerson"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件柜</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<!-- 文件上传 -->
<link href="<%=cssPath %>/cmp/swfupload.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<!-- <script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script> -->
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"><!--
//alert('folder.jsp:<%=seqId%>');
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;
var seqId='<%=seqId%>';

var menuData2 = [{ name:'<div style="padding-top:5px;margin-left:10px">下载<div>',action:set_status,extData:'0'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">转存<div>',action:save_File,extData:'1'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">阅读<div>',action:set_status,extData:'2'}
,{ name:'<div style="color:#FF0000;padding-top:5px;margin-left:10px">编辑<div>',action:set_status,extData:'3'}
,{ name:'<div style="color:#00AA00;padding-top:5px;margin-left:10px">删除<div>',action:set_status,extData:'4'}
]

function showMenu(event,attachmentId,attachmentName){
	//alert(attachmentName);

	var divStyle = {border:'1px solid #69F',width:'100px',position :'absolute',backgroundColor:'#FFFFFF',fontSize:'10pt',display:"block"};	
	
  var menu = new Menu({bindTo:attachmentId+','+ attachmentName , menuData:menuData2 , attachCtrl:true},divStyle);
  menu.show(event,attachmentId,attachmentName);
}

function set_status(){
	alert("建筑中...");
}

function save_File(){
	//alert(ATTACHMENT_ID);
	var extDataStr= arguments[2];
	
	var status= arguments[1];
	//alert(status+"12345");
	var attachId=status.split(",")[0];
	var attachName=status.split(",")[1];
	alert(attachName);
	URL="<%=contextPath%>/core/funcs/savefile/index.jsp?attachId=" + attachId + "&attachName=" + attachName;
  loc_x=screen.availWidth/2-200;
  loc_y=screen.availHeight/2-90;
  window.open(encodeURI(URL),null,"height=180,width=400,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+loc_y+",left="+loc_x+",resizable=yes");
	
	
}




var requestURL="<%=contextPath%>/yh/core/funcs/filefolder/act/YHFileContentAct";
var requestURL1="<%=contextPath%>/yh/core/funcs/system/dimension/act/YHDimensionAct";
function doInit(){
     var seqId='<%=seqId%>';
	//alert("userName:"+'<%=userName%>'+"  deptId:"+'<%=deptId%>'+"  userSeqId:"+'<%=userSeqId%>'+"  userId:"+'<%=userId%>'+"  roleId:"+'<%=roleId%>');
	$('newSubFolder').hide();
	$('reNameSubFolder').hide();
	$('copyFolder').hide();
	$('cutFolder').hide();
	$('paste_sort').hide();
	$('delFolder').hide();
	$('setFolderPriv').hide();
	var actionFlag='<%=actionFlag%>';
	//alert("actionFlag:"+actionFlag);
	if(actionFlag=="copy"||actionFlag=="cut"){
		$("paste_sort").show();	
	}
	if(actionFlag=="copyFile"||actionFlag=="cutFile"){
		$("paste_file").show();
	}
	
	//seqId = document.getElementById("seqId").value;
	var url1=requestURL1 + "/getSortNameById.act?seqId="+seqId;
	var json=getJsonRs(url1);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	prcsJson=json.rtData;
	var sortParent = json.rtMsrg;
	document.getElementById("sortParent").value = sortParent;
	if(prcsJson.length>0){
		var prcs=prcsJson[0];
	  $("sortName").innerHTML=prcs.sortName;
	}
	
	//getPrivate();
//获取权限信息
		var visiPriv;
		var managePriv;
		var newPriv;
		var downPriv;
		var ownerPriv;
		var downUserPriv;
		var sortParent;
		var seqId;
		var url2=requestURL1 + "/getPrivteById.act?seqId=<%=seqId%>";
		//alert(url2);
		var json=getJsonRs(url2);
		if(json.rtState =='1'){
			alert(json.rtMsrg);
			return ;
		}
		prcsJson=json.rtData;
		//alert("rsText>>:"+rsText);
		if(prcsJson.length>0){
			var prcs=prcsJson[0];
			visiPriv=prcs.visiPriv;
			managePriv=prcs.managePriv;
			newPriv=prcs.newPriv;
			downPriv=prcs.downPriv;
			ownerPriv=prcs.ownerPriv;
			downUserPriv=prcs.downUserPriv;
			sortParent=prcs.sortParent;
			seqId=prcs.seqId;
			//alert("visiPriv:"+visiPriv+" managePriv:"+managePriv+"  newPriv:"+newPriv+"  downPriv:"+downPriv+"  ownerPriv:"+ownerPriv+"  sortParent:"+sortParent+"  seqId:"+seqId+"  downUserPriv:"+downUserPriv);
			if(sortParent=="0"){
				if(newPriv=="1"){
					$('newSubFolder').show();
				}
				if(ownerPriv=="1"){
					$('setFolderPriv').show();
				}
			}else{
				if(newPriv=="1"){
					$('newSubFolder').show();
				}
				if(ownerPriv=="1"){
					$('setFolderPriv').show();
				}
				var parentManagePriv=getParentPriv(sortParent);
				//alert("parentManagePriv:"+parentManagePriv);
				if(managePriv=="1"){
					$('reNameSubFolder').show();
					$('copyFolder').show();
					$('cutFolder').show();					
					
				}
				if(parentManagePriv!="0" && parentManagePriv==managePriv){
					$('delFolder').show();
				}
			}
			
		}	
		 warnDiv();
}

function warnDiv(){
	var table=new Element('table',{ "width":"300","class":"MessageBox","align":"center"})
		.update("<tbody id='tbody'><tr >"
			+"<td align='center' class='msg info'><div class='content' style='font-size:12pt'>该维度尚无信息</div></td>"
			+"</tr><tbody>");
	$('nothingDiv').appendChild(table);
}


function getParentPriv(sortParent){
	//alert("sortParent:"+sortParent);
	var parentManagePriv;
	var url2=requestURL1 + "/getPrivteById.act?seqId="+sortParent;
	var json=getJsonRs(url2);
	if(json.rtState =='1'){
		alert(json.rtMsrg);
		return ;
	}
	prcsJson=json.rtData;
	//alert("rsText>>:"+rsText);
	if(prcsJson.length>0){
		var prcs=prcsJson[0];
		parentManagePriv=prcs.managePriv;
	}
	return parentManagePriv;		
}
//全选
function check_all(){  
  var t =document.getElementsByName("email_select");
  for (i=0;i<document.getElementsByName("email_select").length;i++){
    if(document.getElementsByName("allbox")[0].checked){
      document.getElementsByName("email_select").item(i).checked=true;
    }else{
      document.getElementsByName("email_select").item(i).checked=false;
    }
  }
  if(i==0){
    if(document.getElementsByName("allbox")[0].checked){
      document.getElementsByName("email_select").checked=true;
    }else{
      document.getElementsByName("email_select").checked=false;
    }
  }
}

//单选
function check_one(e1){
	if(!e1.checked){
		document.getElementsByName("allbox")[0].checked=false;
	}
}
//获取选项
function get_checked(){
  checked_str="";
  for(i=0;i<document.getElementsByName("email_select").length;i++){
    el=document.getElementsByName("email_select").item(i);
    if(el.checked){  
      val=el.value;
      checked_str+=val + ",";
      //alert("val:"+val);
    }
  }
  if(i==0) {
    el=document.getElementsByName("email_select");
    if(el.checked){  
      val=el.value;
      checked_str+=val + ",";
    }
  }
  return checked_str;
}


function delete_arrang(){
	var delStr=get_checked();
	//alert("del:"+delStr);
	if(delStr==""){
		alert("请至少选择一个文件");
		return;
	}
	msg="确定要删除选择文件吗？这将不可恢复！"
	if(window.confirm(msg)){
		var url=requestURL + "/delCheckedFile.act?seqIdStr="+delStr;
		//alert("url:"+url);
		var json=getJsonRs(url);
		if(json.rtState == '1'){
			alert(json.rtMsrg);
			return ;				
		}
		//alert(rsText);
		window.location.reload();
	}
}

window.onload = function() {	
	doInit();
}


function delete_sort(seqId){
	var msg="确定要删除该维度吗？这将删除该纬度中的所有信息和子维度，且不可恢复！";
	if(window.confirm(msg)){
		var url="<%=contextPath%>/yh/core/funcs/system/dimension/act/YHDimensionAct/delDimensionInfoById.act";
		//alert(url);
  	var json=getJsonRs(url, 'seqId=' + seqId);
    if(json.rtState == '0'){
    	var curTree = parent.frames["file_tree"].tree;  	
  		curTree.removeNode(seqId);
  		
  		var sortParent = document.getElementById("sortParent").value;
  		 window.location.href = "<%=contextPath%>/core/funcs/dimension/folder.jsp?seqId="+sortParent;
    }else{
			alert(json.rtMsrg);
    }
	}
}

//对维度操作
function sort_action(action){
	//alert(action);
	var folderId=seqId;
	//document.getElementById("paste_file").style.display='none';		
	if(action=="copy"){
		var url=requestURL1+"/copyFolderById.act?folderId="+folderId+"&action="+action;
		var json=getJsonRs(url);
    if(json.rtState == '0'){
    	alert("选择的维度已“复制”\n请到目标目录中进行“粘贴”操作");    	
    }else{
			alert(json.rtMsrg);
    }		
		
	}else{
		var url=requestURL1+"/copyFolderById.act?folderId="+folderId+"&action="+action;
		var json=getJsonRs(url);
    if(json.rtState == '0'){
    	alert("选择的惟独已“剪切”\n请到目标目录中进行“粘贴”操作");
    }else{
			alert(json.rtMsrg);
    }
		
	}
	
}

function paste_sort(){
  var seqId='<%=seqId%>';
	var url=requestURL1+"/pasteFolder.act?pasteSeqId="+seqId;
	var rtJson=getJsonRs(url);
  if(rtJson.rtState == '0'){
  	prcsJson = rtJson.rtData;
  	var action=prcsJson[0].action;
  	if(action=="copy"){
    	//alert(rsText);
  		add_TreeNode(prcsJson);
			
    }
    if(action=="cut"){
      //parent.file_tree.location.reload();
			var nodeId=prcsJson[0].seqId;
			var curTree = parent.frames["file_tree"].tree;  		
  		curTree.removeNode(nodeId);
  		add_TreeNode(prcsJson);  		
    }  	
  }else{
		alert(json.rtMsrg);
  }	
  	
}

function add_TreeNode(prcsJson){
	var curTree = parent.frames["file_tree"].tree;
	var curNode = curTree.getCurrNode();
	var nodeId = prcsJson[0].nodeId;
	var isHaveChild = prcsJson[0].isHaveChild;
	var nodeName = prcsJson[0].sortName;
 	var imgAddress = "<%=imgPath%>/dtree/folder.gif";
	var node = {
			parentId:curNode.nodeId,
			nodeId:nodeId,
			name:nodeName,
			isHaveChild:isHaveChild,
			extData:'',
			imgAddress:imgAddress
	}
	curTree.addNode(node);
	location.reload();
}

//对文件操作
function do_action(action){
	//alert(action);
	var selects=get_checked();
	var idStr=selects.split(",");
	var count=idStr.length-1;
	//alert("count:"+count);
	if(count <= 0){
		alert("请至少选择一个文件");
		return ;
	}
	switch(action){
	case "copyFile":
	case "cutFile":
		if(action == "copyFile"){
			
			var url=requestURL+"/copyFileByIds.act?seqIdStrs="+selects+"&action="+action;
			//alert(url);
			var json=getJsonRs(url);
	    if(json.rtState == '0'){
	    	alert("选择的文件已“复制”\n请到目标目录中进行“粘贴”操作"); 	
	    }else{
				alert(json.rtMsrg);
	    }
		
		}else{			
			var url=requestURL+"/copyFileByIds.act?seqIdStrs="+selects+"&action="+action;
			//alert(url);
			var json=getJsonRs(url);
	    if(json.rtState == '0'){
	    	alert("选择的文件已“剪切”\n请到目标目录中进行“粘贴”操作");
	    }else{
				alert(json.rtMsrg);
	    }
		}
		break;
	
	}
	
	
}

function paste_File(){
	var url=requestURL + "/pasteFile.act?sortId=<%=seqId%>";
	//alert(url);
	var rtJson=getJsonRs(url);
	//alert(rtText);
  if(rtJson.rtState == '0'){
 		//alert(rtJson.rt);	
 		window.location.reload();
    
  }
}

--></script>

</head>
<body >
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/filefolder/images/notify_open.gif" align="middle">&nbsp;&nbsp;<b><span class="Big1" id="sortName"> </span></b><br>
   </td>
  </tr>
</table>

<div id="listDiv" align="center"></div>
<div id="nothingDiv" align="center"></div>

<br>
<form name="form1" id="form1" action="" method="post" enctype="multipart/form-data" >
<input type="hidden" name="sortParent" id="sortParent" value="" >

<table>
  <tr id="">
    <td colspan="3">
	    <div id="fsUploadArea" class="flash" style="width:380px;">
		  <div id="fsUploadProgress"></div>
			<div id="totalStatics" class="totalStatics"></div>
			<div>				
			  <input type="button" id="btnStart" class="SmallButtonW" value="开始上传" onclick="swfupload.startUpload();" >&nbsp;&nbsp;
				<input type="button" id="btnCancel" class="SmallButtonW" value="全部取消" onclick="swfupload.cancelQueue();" >&nbsp;&nbsp;
				<input type="button" class="SmallButtonW" value="刷新页面" onclick="window.location.reload();">
		  </div>
		 </div>
	 </td>
  </tr>
</table>
</form>  



<br>
<table class="TableBlock" width="100%" align="center">
  <tr>
    <td class="TableContent" nowrap align="center" width="80"><b>操作：</b></td>
    <td class="TableControl"><!--
    	<a href="query.jsp?seqId=<%=seqId %>&FILE_SORT=1&SORT_ID=11" title="查询文件" style="height:20px;"><img src="<%=contextPath %>/core/funcs/filefolder/images/folder_search.gif" align="middle" border="0">&nbsp;查询</a>&nbsp;&nbsp;
			<a  title="全局搜索" style="height:20px;"><img src="<%=contextPath %>/core/funcs/filefolder/images/folder_search.gif" align="middle" border="0">&nbsp;全局搜索</a>&nbsp;&nbsp;
   		<span id="paste_file" style="display:none;" title="粘贴文件"><a href="javascript:paste_File()" style="height:20px;"><img src="<%=contextPath %>/core/funcs/filefolder/images/paste.gif" align="middle" border="0">粘贴</a>&nbsp;&nbsp;</span>
   		<a href="new/newFile.jsp?seqId=<%=seqId %>" title="创建新的文件" style="height:20px;"><img src="<%=imgPath%>/notify_new.gif" align="middle" border="0">&nbsp;新建文件</a>&nbsp;&nbsp;
   		<span id="spanButtonUpload" title="批量上传"></span>
 	
 		--></td>
  </tr>
  <tr>
    <td class="TableContent" nowrap align="center" width="80"><b>维度操作：</b></td>
    <td class="TableControl">
   		<span id="paste_sort" style="" title="粘贴文件夹" ><a href="javascript:paste_sort()"><img src="<%=contextPath %>/core/funcs/filefolder/images/paste.gif" align="middle" border="0">粘贴</a>&nbsp;&nbsp;&nbsp;</span>
   		<a href="newSubDimension.jsp?seqId=<%=seqId %>" title="创建子文件夹" id="newSubFolder" ><img src="<%=imgPath%>/newfolder.gif" align="middle" border="0">&nbsp;新建子维度</a>&nbsp;&nbsp;&nbsp;
   		<a href="editSubDimension.jsp?seqId=<%=seqId %>" title="重命名此文件夹" id="reNameSubFolder"><img src="<%=contextPath %>/core/funcs/filefolder/images/folder_edit.gif" align="middle" border="0">&nbsp;重命名</a>&nbsp;&nbsp;&nbsp;
   		<a href="javascript:sort_action('copy');" id="copyFolder"><img src="<%=contextPath %>/core/funcs/filefolder/images/copy.gif" align="middle" border="0" title="复制当前维度" >&nbsp;复制</a>
   		<a href="javascript:sort_action('cut');" id="cutFolder"><img src="<%=contextPath %>/core/funcs/filefolder/images/cut.gif" align="middle" border="0" title="剪切当前维度">&nbsp;剪切</a>
  		<a href="javascript:delete_sort('<%=seqId %>');" title="删除此维度"
			id="delFolder"><img
			src="<%=contextPath %>/core/funcs/filefolder/images/delete.gif"
			align="middle" border="0">&nbsp;删除维度</a>&nbsp;&nbsp;
   		
   		<a href="set_priv/index.jsp?seqId=<%=seqId %>&parentId=<%=parentId %>" title="设置该维度的访问、管理、新建和下载权限" id="setFolderPriv"><img src="<%=imgPath%>/folder_priv.gif" align="middle" border="0">&nbsp;设置权限</a>&nbsp;&nbsp;
    </td>
  </tr>
</table>


<input type="hidden" name="sortParent" id = "sortParent" value="" >


</body>
</html>
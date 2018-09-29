<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String usePortalFunc = YHSysProps.getString("usePortalFunc");
	if(YHUtility.isNullorEmpty(usePortalFunc)){
		usePortalFunc = "";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件柜设置</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>

<style type="text/css">
#overlay {display: none; z-index: 10000; height:100%;BACKGROUND: #B5C5E0; filter: alpha(opacity=50);LEFT: 0px; POSITION: absolute; TOP: 0px; -moz-opacity:0.5;opacity:0.5;}

.ModalDialog {border: 1px #83ACCF solid; background: #FFF; padding: 0px;margin: 10px auto; clear: both; position: absolute;  z-index: 10000;display:none;}
.header {border-bottom: 1px #83ACCF solid; font-size: 12px; overflow: hidden; font-weight: normal; background: url('<%=imgPath%>/headerbg.gif') top left repeat-x;height:24px;}
.header .title {padding: 2px 3px;  float: left; font-weight: bold;line-height:20px;}
.header .operation {padding-top: 8px;padding-right:5px; float: right; cursor: pointer; text-decoration: none;}
.body {font-size: 9pt;margin: 0 !important; overflow-y: auto; padding: 10px 5px;}
.footer {padding: 0; text-align: center; font-size: 12px; margin-bottom: 10px;}
</style>

<script type="text/javascript">
var requestURL="<%=contextPath%>/yh/core/funcs/system/filefolder/act/YHFileSortAct";
function doInit(){
	var usePortalFunc = "<%=usePortalFunc%>";
	var type = "FILE_SORT";
	var url=requestURL + "/getFileSortInfo.act";
	var json=getJsonRs(url);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	prcsJson=json.rtData;
	if(prcsJson.length>0){
		var length=prcsJson.length;
		var table=new Element('table',{ "width":"90%","class":"TableList","align":"center"})
		.update("<tbody id='tbody'><tr class='TableHeader' style='font-size:10pt'>"
				+"<td nowrap align='center'>排序号</td>"
				+"<td nowrap align='center'>文件夹名称</td>"				
				+"<td align='center'>操作</td></tr><tbody>");
		$('listDiv').appendChild(table);

		for(var i=0;i<prcsJson.length;i++){
			var prcs=prcsJson[i];
			var sqlId=prcs.sqlId;
			var sortParent=prcs.sortParent;
			var sortNo=prcs.sortNo;
			var sortName=prcs.sortName;
			var sortType=prcs.sortType;
			var deptId=prcs.deptId;			
			var userId=prcs.userId;
			var newUser=prcs.newUser;
			var manageUser=prcs.manageUser;
			var DownUser=prcs.DownUser;
			var shareUser=prcs.shareUser;
			var owner=prcs.owner;
			
			var sortNameString = sortName.replace('"' , '\\"');
			sortNameString = sortNameString.replace(" - ","&nbsp;-&nbsp;");
			//sortName = sortName.replace("'" , "\\\\'");
			var className = (i % 2 == 0) ? "TableLine1" : "TableLine2";					
			var tr=new Element('tr',{'width':'90%','class':className,'font-size':'10pt'});			
			table.firstChild.appendChild(tr);
			var sendDeskTop = "";
			if(usePortalFunc == "1"){
				sendDeskTop = "<a href='#' onclick='publicFile(\""+sqlId +"\");'>发布</a>" ;
			}
			tr.update("<td align='center'>"					
			  + sortNo + "</td><td align='center'>"
				+ sortName + "</td><td align='center'>"					
				+ "<a href=javascript:clone(\""+ sqlId+"\"," +"\"" +sortNo.trim()+ "\"," + "\"" + sortNameString.trim() +"\");>克隆 </a>&nbsp;"
				+ "<a href='editFileSort.jsp?seqId="+ sqlId  + "' >编辑</a>&nbsp;"
				+ "<a href='#' onclick='delete_Proces(\""+ sqlId +"\")'>删除 </a>&nbsp;"
				+ "<a href='#' onclick='set_priv(\""+ sqlId +"\")'>权限设置 </a>&nbsp;"
				+ "<a href='#' onclick='show_priv(\""+sqlId +"\")'>权限报表 </a>&nbsp;"
				+ "<a href='#' onclick='menuCode(\""+type +"\"," +"\"" +sqlId+ "\");'>菜单定义指南</a>&nbsp;"
				+ sendDeskTop
				+ "</td>" 
			);
		}	
	}
}
function publicFile(seqId){ 
	if(seqId){ 
	window.location.href = contextPath + "/core/funcs/portal/cfgPortal.jsp?type=2&&publicPath="+seqId; 
	return true; 
	} 
	}

function menuCode(type, id){
  var title = "菜单定义指南";
  var URL = contextPath + "/core/module/menucode/index.jsp?type=" + type + "&id=" + id;
  //alert(URL);
  showModalWindow(URL , title , "menuWindow" ,600,350);
}

function set_priv(seqId){
	//alert(seqId);
	var url="set_priv/index.jsp?seqId="+seqId;
	location.href=url;
}

function show_priv(seqId){
	var url="show_priv/index.jsp?seqId="+seqId;
	location.href=url;
}


function clone(id,No,Name){
  $('sortNo').value=No;
  $('sortName').value=Name+" - 复件";
  ShowDialog('ModalDialog');
  $('sortName').select();
	
}

function ShowDialog(id){
  $("overlay").style.display = 'block';
  $(id).style.display = 'block';

  var bb=(document.compatMode && document.compatMode!="BackCompat") ? document.documentElement : document.body;
  if(document.compatMode && document.compatMode!="BackCompat"){
    $("overlay").style.width = bb.scrollWidth+"px";
    $("overlay").style.height = bb.scrollHeight+"px";
   }
   else{
    $("overlay").style.width = bb.scrollWidth+"px";
    $("overlay").style.height = bb.scrollHeight+"px";
   }   
   $(id).style.left = ((bb.offsetWidth - $(id).offsetWidth)/2)+"px";
   $(id).style.top  = (90 + bb.scrollTop)+"px";
}

function HideDialog(id){
  $("overlay").style.display = 'none';
  $(id).style.display = 'none';
}

function checkForm(){
	if($("sortNo").value.trim()){
  	if(checkStr(document.form1.sortNo.value)){
  		alert("排序号不能包含有以下字符/\:*<>?\"|！");
  		$("sortNo").select();
  		$("sortNo").focus();
  		return false;
  	}
   }
  if($("sortName").value.trim()==""){
    alert("文件夹名称不能为空！");
  	$("sortName").focus();
  	return false;
  }
  if(checkStr($("sortName").value)){
		alert("不能包含有以下字符/\:*<>?\"|！");
		$("sortName").select();
		$("sortName").focus();
		return false;
	}
  return (true);
}
function checkStr(str){ 
	var re=/["\/\\:*?"<>|]/; 
	return str.match(re); 
}

function sendForm(){
	var url=requestURL + "/addFileSortInfo.act";
	if(checkForm()){
		var rtJson = getJsonRs(url,mergeQueryString($("form1")));
  	if(rtJson.rtState == '0'){
    	bindJson2Cntrl(rtJson.rtData);
     	var prcsJson = rtJson.rtData;
    	var isHaveFlag=prcsJson.isHaveFlag;
    	if(isHaveFlag==1){
				var returnName=$("sortName").value;
				alert("文件夹 " + returnName + " 已存在");
				$("sortName").focus();
				$('sortName').select();
      }else{
        location.href="<%=contextPath%>/core/funcs/system/filefolder/index.jsp";
      }
    	
    }else{
  	 	  alert(rtJson.rtMsrg); 
  	}	
	}	
}

function delete_Proces(seqId){
	//alert(seqId);	
 	msg='确认要删除该文件夹吗？这将删除该文件夹中的所有文件且不可恢复！';
 	if(window.confirm(msg)) {
		//var url=requestURL+"/delFileSortInfoById.act";
		var url = "<%=contextPath%>/yh/core/funcs/filefolder/act/YHFileSortAct/delFileSortInfoById.act";
		//alert(url);
  	var json=getJsonRs(url, 'seqId=' + seqId);
    if(json.rtState == '0'){
	    window.location.reload();	    		
    }else{
			alert(json.rtMsrg);
    }
	} 	
}




</script>



</head>
<body class="" topmargin="5" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/system/filefolder/images/notify_new.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3"> 新建文件夹</span><br>
    </td>
  </tr>
</table>

<div align="center">
<input type="button"  value="新建文件夹" class="BigButton" onClick="location='new/newFileSort.jsp';" title="创建新的文件夹">
</div>
<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="/images/dian1.gif" width="100%"></td>
 </tr>
</table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/system/filefolder/images/notify_open.gif" align="middle"><span class="big3"> 管理文件夹</span>
    </td>
  </tr>
</table>

<br>
  <%-- 
  <table class="TableList" width="90%" align="center">
    <tr class="TableLine1">
      <td nowrap align="center">1</td>
      <td nowrap align="center">001</td>
      <td nowrap align="center">
        <a href="javascript:clone('3','1','001');"> 克隆</a>&nbsp;
        <a href="edit.php?SORT_ID=3"> 编辑</a>&nbsp;
        <a href="javascript:delete_sort(3);"> 删除</a>&nbsp;
        <a href="set_priv?SORT_ID=3"> 权限设置</a>&nbsp;
        <a href="show_priv?SORT_ID=3"> 权限报表</a>&nbsp;
        <a href="javascript:menu_code('FILE_SORT','3')">菜单定义指南</a>
      </td>
    </tr>
   <thead class="TableHeader">
     <td nowrap align="center">排序号</td>
     <td nowrap align="center">文件夹名称</td>
     <td nowrap align="center">操作</td>
   </thead>
   </table>
--%>

<dir id="listDiv"></dir>

<div id="overlay"></div>

<div id="ModalDialog" class="ModalDialog" style="width:300px;">
  <div class="header"><span id="title" class="title">克隆文件夹</span><a class="operation" href="javascript:HideDialog('ModalDialog');"><img src="<%=contextPath %>/core/funcs/system/filefolder/images/close.png"/></a></div>
  <div id="body" class="body">
    <form name="form1" id="form1" action="">
    	<input type="hidden" value="yh.core.funcs.system.filefolder.data.YHFileSort" name="dtoClass">
      <div>排序号：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" id="sortNo" name="sortNo" class="SmallInput" size="6" / ></div>
      <div style="padding-top:3px">新文件夹名：<input type="text" id="sortName" name="sortName" class="SmallInput" size="20" /></div>     
   </form>
  </div>
  <div id="footer" class="footer">
    <input class="BigButton" onclick="sendForm();" type="button" value="确定"/>&nbsp;&nbsp;
    <input class="BigButton" onclick="HideDialog('ModalDialog')" type="button" value="取消"/>
  </div>  
</div>

</body>

</html>
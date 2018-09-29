<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>维度设置</title>
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
var requestURL="<%=contextPath%>/yh/core/funcs/system/dimension/act/YHDimensionAct";
function doInit(){
	var type = "DIMENSION";
	var url=requestURL + "/getDimensionInfo.act";
	var json=getJsonRs(url);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	prcsJson=json.rtData;
	//alert("prcsJson>>"+rsText);
	if(prcsJson.length>0){
		var length=prcsJson.length;
		//var counter=document.getElementById('counter');
		//counter.innerHTML=length;
		var table=new Element('table',{ "width":"90%","class":"TableList","align":"center"})
		.update("<tbody id='tbody'><tr class='TableHeader' style='font-size:10pt'>"
				+"<td nowrap align='center'>排序号</td>"
				+"<td nowrap align='center'>维度名称</td>"				
				+"<td align='center'>操作</td></tr><tbody>");
		$('listDiv').appendChild(table);

		for(var i=0;i<prcsJson.length;i++){
			var prcs=prcsJson[i];
			//alert(prcs.sortName);
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
			//alert(sqlId+''+roomName+''+personNum+''+roomDesc+''+roomAddr);	
			var className = (i % 2 == 0) ? "TableLine1" : "TableLine2";					
			var tr=new Element('tr',{'width':'90%','class':className,'font-size':'10pt'});			
			table.firstChild.appendChild(tr);
			tr.update("<td align='center'>"					
			  + sortNo + "</td><td align='center'>"
				+ sortName + "</td><td align='center'>"					
				//+ "<a href='javascript:clone(\""+ sqlId+"\"," +"\"" +sortNo+ "\"," + "\"" + sortName +"\");'>克隆 </a>&nbsp;"
				+ "<a href='editDimension.jsp?seqId="+ sqlId  + "' >编辑</a>&nbsp;"
				+ "<a href='child_dimension/index.jsp?seqId="+ sqlId  + "' >管理子维度</a>&nbsp;"
				+ "<a href='#' onclick='delete_Proces(\""+ sqlId +"\")'>删除 </a>&nbsp;"
				+ "<a href='#' onclick='set_priv(\""+ sqlId +"\")'>权限设置 </a>&nbsp;"
				+ "<a href='#' onclick='show_priv(\""+sqlId +"\")'>权限报表 </a>&nbsp;</td>"
			);
		}	

		
	}
}


/*function menuCode(type, id){
  var title = "菜单定义指南";
  var URL = "/yh/core/module/menucode/index.jsp?type=" + type + "&id=" + id;
  openDialog(URL,'600', '350');
 // showModalWindow(url , title , "sss" ,600,350)
}*/
function menuCode(type, id){
  var title = "菜单定义指南";
  var URL = contextPath + "/core/module/menucode/index.jsp?type=" + type + "&id=" + id;
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
  if($("sortName").value==""){
    alert("维度名称不能为空！");
  	$("sortName").focus();
  	return false;
  }
  return (true);
}

function sendForm(){
	var url=requestURL + "/addDemensionInfo.act";
	if(checkForm()){
	   var rtJson = getJsonRs(url,mergeQueryString($("form1")));
  	   if(rtJson.rtState == '0'){
    	 bindJson2Cntrl(rtJson.rtData);
    	 location.href="index.jsp";
    	 //history.go('-1');
       }else{
  	 	  alert(rtJson.rtMsrg); 
  	   }	
	}	
}

function delete_Proces(seqId){
	//alert(seqId);	
 	msg='确认要删除该维度吗？这将删除该维度中的所有子维度且不可恢复！';
 	if(window.confirm(msg)) {
		var url=requestURL+"/delDimensionInfoById.act";
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
    <td class="Big"><img src="<%=contextPath %>/core/funcs/system/filefolder/images/notify_new.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3"> 新建维度</span><br>
    </td>
  </tr>
</table>

<div align="center">
<input type="button"  value="新建维度" class="BigButton" onClick="location='newDimension.jsp';" title="创建新的维度">
</div>
<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/system/filefolder/images/notify_open.gif" align="middle"><span class="big3"> 管理维度</span>
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
  <div class="header"><span id="title" class="title">克隆维度</span><a class="operation" href="javascript:HideDialog('ModalDialog');"><img src="<%=contextPath %>/core/funcs/system/filefolder/images/close.png"/></a></div>
  <div id="body" class="body">
    <form name="form1" id="form1" action="">
    	<input type="hidden" value="yh.core.funcs.system.dimension.data.YHDimension" name="dtoClass">
      <div>排序号：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" id="sortNo" name="sortNo" class="SmallInput" size="6" / ></div>
      <div style="padding-top:3px">新维度名：<input type="text" id="sortName" name="sortName" class="SmallInput" size="20" /></div>     
   </form>
  </div>
  <div id="footer" class="footer">
    <input class="BigButton" onclick="sendForm();" type="button" value="确定"/>&nbsp;&nbsp;
    <input class="BigButton" onclick="HideDialog('ModalDialog')" type="button" value="取消"/>
  </div>  
</div>

</body>

</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<head>
<title>内部邮件菜单</title>
<!--[if lt IE 7.0]>
<style type='text/css' title=''>
LI A {
  cursor:   hand;DISPLAY: block; PADDING-LEFT: 10px; LINE-HEIGHT: 25px;PADDING:3px
}
</style>
<![endif]-->
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/email.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
</head>
<script type="text/javascript"><!--
function initFolder(id){
  var url = contextPath + "/yh/core/funcs/email/act/YHEmailBoxAct/getSelfForLi.act";
  var rtJson = getJsonRs(url);
  var hasBox = 0;
  if(rtJson.rtState == "0"){
    $(id).innerHTML = "";
    if(rtJson.rtData.length > 0){
      hasBox = 1;
    }
    for(var i = 0 ; i < rtJson.rtData.length ;i++){
      var obj = rtJson.rtData[i].box;
      var boxName = obj.boxName;
      var str = "<li><a onclick=\'toUrl(\""+ contextPath +"/core/funcs/email/inbox/index.jsp?boxId=" + obj.seqId + "&boxName=" + encodeURI(boxName) + "\",this);\' target=\"mail_main\"><img src=\"" + contextPath + "/core/styles/style1/img/cmp/email/inbox.gif\" align=\"absMiddle\" /> &nbsp;" 
      + boxName + "(<span>" + rtJson.rtData[i].mails + "</span>)";
      if(rtJson.rtData[i].newMails){
        str += "<img src=\"" + contextPath + "/core/styles/style1/img/cmp/email/email_new.gif\"  align=\"absMiddle\" alt=\"有新邮件\"/>";
      }
      str += "</a></li>";
      $(id).insert(str,'bottom');
    }
  }
  return hasBox;
}
function setBoxCount(id,count)
{
   document.getElementById(id).innerHTML=count;
}
function getBoxCount(id)
{
   return parseInt(document.getElementById(id).innerHTML);
}
function show_folder(id)
{
   
   var hasBox = initFolder("folders");
   if(hasBox == 1){
     var obj=document.getElementById(id);
     if(!obj) return;

     if(obj.style.display=="")
      obj.style.display="none";
     else
       obj.style.display="";
  }else{
    toUrl('mailbox');
  }
}


function doInit(){
	  //initFolder("folders");
	  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/getCount.act";
	  var rtJson = getJsonRs(url);
	  if(rtJson.rtState == "0"){
	    var counts = rtJson.rtData;
	    if(counts.inNew){
	      $('isNew').style.display = ''; 
	    }
	  
	    bindJson2Cntrl(counts);
	  }
	}


function doInit2(){
//	alert("开始");
	getIn();
//	 alert("收");
	getSent();
//	 alert("发");
	getNew();
	// alert("新");
	getDel();
	// alert(" 删除");
	getOut();
	 //alert("out");
}

function getIn(){
  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/getCountIn.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var counts = rtJson.rtData;
    bindJson2Cntrl(counts);
  }
}
function getSent(){
	  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/getCountSent.act";
	  var rtJson = getJsonRs(url);
	  if(rtJson.rtState == "0"){
	    var counts = rtJson.rtData;
	    bindJson2Cntrl(counts);
	  }
	}
function getNew(){
    var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/getCountNew.act";
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
      var counts = rtJson.rtData;
      if(counts.inNew){
        $('isNew').style.display = ''; 
      }
    }
  }

function getDel(){
    var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/getCountDel.act";
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
      var counts = rtJson.rtData;
      bindJson2Cntrl(counts);
    }
  }

function getOut(){
    var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/getCountOut.act";
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
      var counts = rtJson.rtData;
      bindJson2Cntrl(counts);
    }
  }

function toUrl(url){
  //webkit的浏览器用parent.mail_main.location跳转都有问题
  parent.document.getElementsByName("mail_main")[0].contentWindow.location.href = url;
  //parent.mail_main.location = url;
}
--></script>
<body onload="doInit();" style="margin:10px 10px;">
<img src="<%=imgPath %>/pageheaders/email.jpg"></img>
<div id="body">
	<div class="toolBar" style="padding-bottom: 10px;">
		<a class="ToolBtn2" onclick="toUrl('<%=contextPath%>/core/funcs/email/new/new.jsp?boxId=0',this)" target="mail_main" title="点击查看新邮件"><span><img src="<%=imgPath%>/cmp/email/nm.png" align="absMiddle">未 读</span></a>
		<a class="ToolBtn2" onclick="toUrl('new',this);" target="mail_main"><span><img src="<%=imgPath%>/cmp/email/wm.png" align="absMiddle">写 信</span></a>
	</div>
  <table class="BlockTop" width="100%">
    <tr>
      <td class="left">
      </td>
      <td class="center" align="left">
      <a class="arExpand" onclick="toUrl('mailbox',this);" target="mail_main">邮件箱管理</a>
      </td>
      <td class="right">
      </td>
    </tr>
  </table>
<UL id="folder" class="folder">
<li><a onclick="toUrl('<%=contextPath%>/core/funcs/email/inbox/index.jsp?boxId=0',this);" target="mail_main"><img src="<%=imgPath%>/cmp/email/inbox.gif" align="absMiddle" />&nbsp; 收件箱  <span id="inbox0"></span>  <span id="isNew" style="display:none"><img src="<%=imgPath%>/cmp/email/email_new.gif" alt="有新邮件"></span></a></li>
<li><a onclick="toUrl('<%=contextPath%>/core/funcs/email/outbox/index.jsp?',this);" target="mail_main"><img src="<%=imgPath%>/cmp/email/outbox.gif" align="absMiddle" /> &nbsp;草稿箱  <span id="outbox"></span></a></li>
<li><a onclick="toUrl('<%=contextPath%>/core/funcs/email/sendbox/index.jsp?boxId=0',this);" target="mail_main"><img src="<%=imgPath%>/cmp/email/sentbox.gif" align="absMiddle" />&nbsp; 已发送  <span id="sendbox"></span></a></li>
<li><a onclick="toUrl('<%=contextPath%>/core/funcs/email/delbox/index.jsp?boxId=0',this);" target="mail_main"><img src="<%=imgPath%>/cmp/email/trash.gif" align="absMiddle" />&nbsp; 已删除  <span id="delbox"></span></a></li>
<li><a onclick="toUrl('<%=contextPath%>/core/funcs/email/query/index.jsp?boxId=0',this);" target="mail_main"><img src="<%=imgPath%>/cmp/email/infofind.gif" align="absMiddle" />&nbsp; 查询邮件</a></li>
<li><a onclick="toUrl('<%=contextPath%>/core/funcs/email/nameset/index.jsp',this);" target="mail_main"><img src="<%=imgPath%>/edit.gif" align="absMiddle" />&nbsp; 设置签名</a></li>
</UL>
<div class="emailheader" ><a class="arExpand" onclick="show_folder('folders');">自定义邮件箱</a></div>
<div class="folder">
<UL id="folders"  style="padding-bottom: 10px;overflow-y:auto;display:none">
</UL>
</div>
<table class="BlockBottom" width="100%">
  <tr>
    <td class="left">
    </td>
    <td class="center" align="left">
    <a class="arClose" onclick="toUrl('<%=contextPath%>/core/funcs/email/webbox/manager/index.jsp')"  target="mail_main">Internet邮箱配置</a>
    </td>
    <td class="right">
    </td>
  </tr>
</table>
</div>
</body>
</html>
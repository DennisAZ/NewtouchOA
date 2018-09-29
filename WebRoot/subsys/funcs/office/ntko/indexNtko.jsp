<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
  String op = request.getParameter("op") == null ? "" : request.getParameter("op");
  String attachmentName = request.getParameter("attachmentName") == null ? "" : request.getParameter("attachmentName");
  String attachmentId = request.getParameter("attachmentId") == null ? "" : request.getParameter("attachmentId");
  String moudle = request.getParameter("moudle")== null ? "" : request.getParameter("moudle");
  String signKey = request.getParameter("signKey") == null ? "" : request.getParameter("signKey");
  String print = request.getParameter("print") == null ? "" : request.getParameter("print");
  boolean useResInfoSubSys = "1".equals(YHSysProps.getString("useInfoResSubsys"));
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/office/ntko/tanger.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/office/ntko/tangerUtil.js"></script>
<title>ntko</title>
<style media="all" type="text/css">		
	h2{
		color:#FFFFFF;
		font-size:90%;
		font-family:arial;
		margin:10px 10px 10px 10px;
		font-weight:bold;
	}
	
	h2 span{
		font-size:105%;
		font-weight:normal;
	}
	
	ul{
		margin:0px 0px 0px 0px;
		padding:0px 0px 0px 0px;
	}
	
	li{
		margin:0px 10px 3px 10px;
		padding:2px;
		list-style-type:none;
		display:block;
		background-color:#DA1074;
		width:177px;
	}
	
	li a{
		width:100%;
	}
	
	li a:link,
	li a:visited{
		color:#FFFFFF;
		font-family:verdana;
		font-size:70%;
		text-decoration:none;
		display:block;
		margin:0px 0px 0px 0px;
		padding:0px;
		width:100%;
	}
	
	li a:hover{
		color:#FFFFFF;
		text-decoration:underline;
	}
	
	#sideBar{
		position: absolute;
		width: auto;
		height: auto;
		top: 0px;
		right:-7px;
		background-image:url(images/background.gif);
		background-position:top left;
		background-repeat:repeat-y;
		z-index: 20;
	}
	
	#sideBarTab{
		float:left;
		height:137px;
		width:28px;
	}
	#ifraClass{
	  position: absolute;
		width: 200px;
		height: 615px;
		top: 0px;
		right:-7px;		
		background-position:top left;
		background-repeat:repeat-y;
		z-index: 10;
	}
</style>

<script type="text/javascript">
  var op = "<%=op %>"; 
  var print = "<%=print %>"; 
  var attachmentName = "<%=attachmentName %>";
  var attachmentId = "<%=attachmentId %>";
  //alert(attachmentId);
  var moudle = "<%=moudle %>";
  var useResInfoSubSys = <%=useResInfoSubSys %>;
  var TANGER_OCX_str;//文档路径或者URL
  var TANGER_OCX_obj;//文档的自动化接口
  var secOcRevision;//是否显示/隐藏痕迹
  var secOcMarkDefault;//默认不/保留痕迹
  var secOcMark;//保留痕迹设置
  var logPage ;//附件操作日志
  function doInit(){
  //此函数在网页装载时被调用。用来获取控件对象并保存到TANGER_OCX_OBJ
  //同时，可以设置初始的菜单状况，打开初始文档等等。

   if(op == 5 && print == 1){
      op = 6;
    }
   var param = encodeURI("attachmentName=" + attachmentName + "&attachmentId=" + attachmentId + "&module=" + moudle);
   if(!fileExits(param)){
     document.body.innerHTML = " 文件名："+ attachmentName + "<br>抱歉，您所访问的文件不存在，可能已经被删除或转移，请联系OA管理员。<br>";
     return ;
   }
   if(op == 4){
     var isCanEditObj = isCanEidt(attachmentId);
     if(isCanEditObj){
       if(isCanEditObj.isCanEidt == 1){
         var msrg = " 用户：<input type=hidden id=\"canEditUser\" value=\"" + isCanEditObj.userId + "\"><span id=\"canEditUserDesc\"></span> 正在编辑此文件，请稍候重试";
         WarningMsrg( msrg, $(document.body),"info" );
         bindDesc([{cntrlId:"canEditUser", dsDef:"PERSON,SEQ_ID,USER_NAME"} ]);
         return ;
       }else{
         lockRef(attachmentId);
         var time = (isCanEditObj.sysTime)*1000;
         //setTimeout("lockRef('" + attachmentId + "');",time);
       }
     }
   }
   var URL = contextPath + "/yh/core/funcs/office/ntko/act/YHNtkoAct/upload.act?" + param;
   $('TANGER_OCX_attachURL').innerHTML = URL;
    var secOcObj = getMarkSetting();
    if(secOcObj){
      secOcRevision = secOcObj.secOcRevision;
      secOcMarkDefault = secOcObj.secOcMarkDefault;
      secOcMark = secOcObj.secOcMark;
      if (secOcMark == "") {
        secOcMark = "0";
      }
      if(!secOcMarkDefault){
        secOcMarkDefault = "0";
      }
    }
    setOpView(op ,secOcMark,secOcMarkDefault,attachmentName);
    TANGER_OCX_SetInfo();
    if(op == 5){
      TANGER_OCX_EnableFilePrintMenu(false);//禁止打印
    }
    logPage = getLogPageMgr("list",attachmentId);

    initMateTree();
  }
  
  window.attachEvent('onbeforeunload',ColseDoc);
</script>
<script type="text/javascript">
var showit = false;	
function showMate(){ 
  document.getElementById("sideBarContents").style.display="none";
	document.getElementById("imgshow").src = "images/slide-button.gif";
	document.getElementById("sideBarContents").style.display="none";
   if(showit == false){//没有显示
     document.getElementById("ifraClass").style.display="";
     document.getElementById("sideBarContents").style.display="";
     document.getElementById("imgshow").src = "images/slide-button-active.gif";
     showit = true;
   }else{
     document.getElementById("sideBarContents").style.display="none";
     document.getElementById("ifraClass").style.display="none";
     document.getElementById("imgshow").src = "images/slide-button.gif";
     showit = false;
   }
}

/**
 * 初始化树
 */
function initMateTree(){
  var guid = $("attachmentId").value; 
  if(guid){
    if (window["tree"]) {
      window["tree"].initMateTree(guid);
    }
  }
}

function translate(){
 $("condition").value = test();
 $("translator").submit();
  return false;
}

function test(){
  var value = TANGER_OCX_GetSelection();
  if(value.length <= 1){
    alert("请选择要翻译的内容！");
    return false;
  }
  return value;
}
</script>
</head>
<body onload="doInit()">
<form name="form1" id="form1" method="post" action="<%=contextPath %>/yh/core/funcs/office/ntko/act/YHNtkoAct/updateFile.act" enctype="multipart/form-data">
<table width=100% height=100% class="small" cellspacing="1" cellpadding="3" align="center">
<tr width=100%>
<td valign=top width=100>
  <table class="TableBlock" width="100%" align="center">
   <tr class="TableHeader" style="display:none" id="e_id">
     <td nowrap align="center">文件操作</td>
   </tr>
      <tr id="" class="TableData" onclick="translate();" style="cursor:pointer;line-height:20px;">
       <td nowrap align="center">即时翻译
       <!-- <input type="button" value="即时翻译" onclick="test()"></input> -->
       </td>
     </tr>
      <tr id="" class="TableData" onclick="kengine();" style="cursor:pointer;line-height:20px;">
       <td nowrap align="center">知识引擎</td>
     </tr>
  </table>
<td width=100% valign="top">
<div style="z-index:-1;"> 
<object  id="TANGER_OCX" classid="<%=StaticData.Classid%>" codebase="<%=contextPath %>/core/cntrls/<%=StaticData.Codebase%>" width="99%" height="800">
<param name="wmode" value="transparent">
<param name="BorderStyle" value="1">
<param name="BorderColor" value="14402205">
<param name="TitlebarColor" value="14402205">
<param name="TitlebarTextColor" value="0">
<param name="Caption" value="Office文档在线编辑">
<param name="IsShowToolMenu" value="-1">
<param name="IsHiddenOpenURL" value="0">
<param name="IsUseUTF8URL" value="1">
<param name="MakerCaption" value="<%=StaticData.MakerCaption%>">
<param name="MakerKey" value="<%=StaticData.MakerKey%>">
<param name="ProductCaption" value="<%=StaticData.ProductCaption%>">
<param name="ProductKey" value="<%=StaticData.ProductKey%>">
<SPAN STYLE="color:red"><br>不能装载文档控件，请设置好IE安全级别为中或中低，不支持非IE内核的浏览器。</SPAN>
</object>
</div>
<div id="ocLog" style="display:none;padding-left:40px;" >
  <div align="center"><h2>操作日志</h2></div>
  <div id="list"style="display:none;"></div>
   <div id="msrg" align="center" ></div>
  <div align="center" style="padding-bottom:10px;">
  <input type="button" class="BigButton" onclick="getLog();" value="刷新日志">
  <input type="button" class="BigButton" onclick="ShowLog();" value="隐藏日志">
</div>
</div>
</td>
</tr>
</table>
<script language="JScript" for=TANGER_OCX event="OnDocumentClosed()">
  //文档被关闭时执行的操作

  TANGER_OCX_OnDocumentClosed();
</script>
<script language="JScript" for=TANGER_OCX event="OnWordWPSSelChange(obj)">
  //文档被关闭时执行的操作

  isMustSave = true;
 // TANGER_OCX_OnDocumentClosed();
</script>
<script language="JScript" for=TANGER_OCX event="OnDocumentOpened(TANGER_OCX_str,TANGER_OCX_obj)">
  //文档被关闭时执行的操作

  //TANGER_OCX_str 已被自动复值

  //alert(TANGER_OCX_obj);
  //TANGER_OCX_bDocOpen = true;
  TANGER_OCX_OnDocumentOpened(TANGER_OCX_str, TANGER_OCX_obj);
  TANGER_OCX_SetReadOnly(false);
  if(findStr(TANGER_OCX_filename,".doc")){
    if(secOcRevision == "1") {
      TANGER_OCX_ShowRevisions(true);
    } else{
      TANGER_OCX_ShowRevisions(false);
    } 
    if(op != 4 && op != 7){
      TANGER_OCX_SetReadOnly(true);
    }
  }
  if (secOcMark == "0" || secOcMark == "2") {//允许保留痕迹
    if (secOcMarkDefault == "0") {//默认不保留痕迹

      TANGER_OCX_SetMarkModify(false);
    } else if (secOcMarkDefault == "1") { //默认保留痕迹
      TANGER_OCX_SetMarkModify(true);
    }
  } else if (secOcMark == "1") {//强制保留痕迹
    TANGER_OCX_SetMarkModify(true);
  } else if (secOcMark == "3") {//强制不保留痕迹

    TANGER_OCX_SetMarkModify(false);
  }
</script>
<span id="TANGER_OCX_op" style="display:none"><%=op %></span>
<span id="TANGER_OCX_filename" style="display:none"><%=attachmentName %></span>
<span id="TANGER_OCX_attachName" style="display:none"><%=attachmentName %></span>
<span id="TANGER_OCX_attachURL" style="display:none"></span>
<span id="TANGER_OCX_user" style="display:none"><%=loginUser.getUserName() %></span>
<INPUT type=hidden NAME="attachmentId" id="attachmentId" value="<%=attachmentId %>">
<INPUT TYPE=hidden NAME="attachmentName" id="attachmentName" value="<%=attachmentName %>">
<INPUT TYPE=hidden NAME="moudle"  id="moudle" value="<%=moudle %>">
<input type="hidden" id="docSize" name="docSize" value="">
<input style="display:none" type="file" name="ATTACHMENT">
</FORM>

<% if (useResInfoSubSys) { %>
<iframe id="ifraClass" src="" style="display:none;"></iframe>
<div id="sideBar">
	<a href="javascript:void(0)" id="sideBarTab" onclick="showMate();return false;"><img id="imgshow" src="images/slide-button.gif" alt="sideBar" title="sideBar" /></a>			
	<div id="sideBarContents" style="display:none;background-color:#696969;">
			<div id="sideBarContentsInner" style="">
			 <iframe src="<%=contextPath%>/subsys/inforesource/tree/tree2.jsp" name="tree"style="width:170px; height:590px;"></iframe>
			 <br>
				 <div style="width:150px;height:25px;position:relative;left:50px;" >		
				 <button  onclick="toSave();return false;" class="SmallButton">保存</button>
				 </div>
		  </div>		   
	</div>
</div>
<%
}
%>

<form  action="http://192.168.0.5:8000/trans/instantTranslatorNKTO.php" id="translator" name="translator" method="post" target="_blank">
  <input type="hidden" id="condition" name="condition" value="">
</form>
</body>
<% if (useResInfoSubSys) { %>
<script type="text/javascript">
var node="";
var attachmentId = $("attachmentId").value;
var attachmentName = $("attachmentName").value;
var moudle = $("moudle").value;	
function toSave(){	
  window['tree'].getSelected();		
  var ids = getNode();								
  if(!ids){
    alert("请选择元数据");
    return false;
  }else{
    var zhaiYao = TANGER_OCX_OBJ.ActiveDocument.content.Text;
    zhaiYao = zhaiYao.substring(0,100);	
    saveAjax(ids, attachmentId, attachmentName, moudle, zhaiYao);
  }
}

function setNode(node){  		  	  
  this.node=node;
}
   	
function getNode(){
  return this.node;
} 
/**
 * 用ajax保存
 */
 function saveAjax(ids, attachmentId, attachmentName, moudle, zhaiYao){
   var queryParam = "ids="+ ids +"&&attachmentId="+ attachmentId +"&&attachmentName="+ attachmentName +"&&moudle="+ moudle +"&abstracts=" +zhaiYao;;
   var rtJson = getJsonRs(contextPath + "/yh/subsys/inforesouce/act/YHFileMetaSaveAct/saveFileMeta.act", queryParam);
   if (rtJson.rtState == "0") {
     alert("保存成功!");
   }else {
     alert("保存失败!");
   }
 } 	  
 /**
 知识引擎
 **/
 function kengine(){
  // var par = 'attachmentId='+attachmentId; \yh\subsys\inforesouce\act\YHFileMetaSaveAct
   var url = contextPath+ "/yh/subsys/inforesouce/act/YHFileMetaSaveAct/getKengine.act?attachmentId="+attachmentId;
   //var json = getJsonRs(url, par); 
   window.open(url);
   return false;
 }
 
</script>
<%
}
%>
</html>
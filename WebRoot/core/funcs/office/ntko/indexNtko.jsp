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
<title><%=attachmentName %>---在线文档</title>
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
  var moudle = "<%=moudle %>";
  var TANGER_OCX_str;//文档路径或者URL
  var TANGER_OCX_obj;//文档的自动化接口
  var secOcRevision;//是否显示/隐藏痕迹
  var secOcMarkDefault;//默认不/保留痕迹
  var secOcMark;//保留痕迹设置
  var logPage ;//附件操作日志
  function doInit(){
    TANGER_OCX_SetInfo();
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
      if (secOcRevision == "") {
        secOcRevision = "0";
      }
      if(!secOcMarkDefault){
        secOcMarkDefault = "0";
      }
    }
    setOpView(op ,secOcMark,secOcMarkDefault,attachmentName,secOcRevision);
    TANGER_OCX_SetInfo();
    if(op == 5){
      TANGER_OCX_EnableFilePrintMenu(false);//禁止打印
    }
    /*
    if (op == 7) {
      TANGER_OCX_EnableFilePrintMenu(false);//禁止打印
    }*/
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

</script>
</head>
<body onload="doInit()">
<form name="form1" id="form1" method="post" action="<%=contextPath %>/yh/core/funcs/office/ntko/act/YHNtkoAct/updateFile.act" enctype="multipart/form-data">
<table width=100% height=100% class="small" cellspacing="1" cellpadding="3" align="center">
<tr width=100%>
<td valign=top width=100>
  <table class="TableBlock" width="100%" align="center">
     <tr class="TableHeader" style="display:none" id="doc_fun">
       <td nowrap align="center">文件操作</td>
     </tr>
     <tr id="doc_fun_save" class="TableData" onclick="TANGER_OCX_SaveDoc(0);getLog();" style="cursor:pointer;line-height:20px;display:none">
       <td nowrap align="center">保存文件</td>
     </tr>
     <tr id="doc_fun_pageSet" class="TableData" onclick="TANGER_OCX_ChgLayout()" style="cursor:pointer;line-height:20px;display:none">
       <td nowrap align="center">页面设置</td>
     </tr>
     <tr id="doc_fun_print" class="TableData" onclick="TANGER_OCX_PrintDoc()" style="cursor:pointer;line-height:20px;display:none">
       <td nowrap align="center">打印</td>
     </tr>
     <tr  id="doc_fun_log" class="TableData" onclick="ShowLog()" style="cursor:pointer;line-height:20px;display:none">
       <td nowrap align="center">操作日志</td>
     </tr>
   <tr class="TableHeader" style="display:none" id="doc_edit">
     <td nowrap align="center">文件编辑</td>
   </tr>
 
   <tr id="doc_edit_saveMark" onclick="MY_SetMarkModify(true)" style="cursor:pointer;line-height:20px;display:none">
     <td nowrap class="TableControl" align="center" id="mflag1" >保留痕迹 </td>
   </tr>
   <tr id="doc_edit_noMark" onclick="MY_SetMarkModify(false)" style="cursor:pointer;line-height:20px;display:none">
     <td nowrap class="TableData" align="center" id="mflag2" >不留痕迹 </td>
   </tr>
   <tr id="doc_edit_showRev" onclick="MY_ShowRevisions(true)" style="cursor:pointer;line-height:20px;display:none">
     <td nowrap class="TableData" align="center" id="sflag1">显示痕迹</td>
   </tr>
   <tr id="doc_edit_disRev" onclick="MY_ShowRevisions(false)" style="cursor:pointer;line-height:20px;display:none">
     <td nowrap class="TableControl" align="center" id="sflag2">隐藏痕迹</td>
   </tr>
    <tr id="doc_edit_redWord" onclick="return selectword();" style="cursor:pointer;line-height:20px;display:none">
     <td nowrap class="TableData" align="center">文件套红</td>
   </tr>
   <tr id="doc_edit_addPic"  onclick="AddPictureFromLocal()" style="cursor:pointer;line-height:20px;display:none">
     <td nowrap class="TableData" align="center">插入图片</td>
   </tr>
   <tr class="TableHeader" style="display:none" id="e_id">
     <td nowrap align="center">电子认证</td>
   </tr>
     <tr id="e_id_check" class="TableData" onclick="DoCheckSign(<%=signKey %>)" style="cursor:pointer;line-height:20px;display:none">
       <td nowrap align="center">验证签名及印章</td>
     </tr>
     <tr id="e_id_handSign2" class="TableData" onclick="DoHandSign2(<%=signKey %>)" style="cursor:pointer;line-height:20px;display:none">
       <td nowrap align="center">全屏手写签名</td>
     </tr>
     <tr id="e_id_handDraw2"  class="TableData" onclick="DoHandDraw2(<%=signKey %>)" style="cursor:pointer;line-height:20px;display:none">
       <td nowrap align="center">全屏手工绘图</td>
     </tr>
     <tr id="e_id_handSign1" class="TableData" onclick="DoHandSign(<%=signKey %>)" style="cursor:pointer;line-height:20px;display:none">
       <td nowrap align="center">插入手写签名</td>
     </tr>
     <tr id="e_id_handDraw1" class="TableData" onclick="DoHandDraw()" style="cursor:pointer;line-height:20px;display:none">
       <td nowrap align="center">插入手工绘图</td>
     </tr>
     <tr id="e_id_addSign1" class="TableData" onclick="AddSignFromLocal(<%=signKey %>)"style="cursor:pointer;line-height:20px;display:none">
       <td nowrap align="center">加盖电子印章</td>
     </tr>
     <tr id="e_id_addSign2" class="TableData" onclick="SelSign()"style="cursor:pointer;line-height:20px;display:none">
       <td align="center">加盖电子印章<br>(从服务器)</td>
     </tr>
     <tr class="TableData" style="display:none" id="SelSign">
       <td nowrap align="center">
         <div id="SelFileDiv">请选择印章文件</div>
         <input type="hidden" value="" name="ATTACH_NAME" id="ATTACH_NAME">
         <input type="hidden" value="" name="ATTACH_DIR"  id="ATTACH_DIR">
         <input type="hidden" value="" name="DISK_ID"  id="DISK_ID">
         <input type="button" class="SmallButton" value="选择" onclick="SelSignFromURL('SelFileDiv','ATTACH_DIR','ATTACH_NAME','DISK_ID')">
         <input type="button" class="SmallButton" value="加盖印章" onclick="AddSignFromURL('487230944')">
       </td>
     </tr>
     <tr id="e_id_addSign3" class="TableData" onclick="AddSecSignFromEkey()"style="cursor:pointer;line-height:20px;display:none;" id="tr_ekey">
       <td align="center">加盖电子印章<br>(从EKey)</td>
     </tr>
  </table>
<td width=100% valign="top">
<div style="z-index:-1;"> 
<object  id="TANGER_OCX" classid="<%=StaticData.Classid%>" codebase="<%=contextPath %>/core/cntrls/<%=StaticData.Codebase%>" width="99%" height="800">
<%
if ("4".equals(op)) {
%>
<param name="IsNoCopy" value="0">
<param name="FileSave" value="1">
<param name="FileSaveAs" value="1">
<%
} else {
%>
<param name="IsNoCopy" value="1">
<param name="FileSave" value="0">
<param name="FileSaveAs" value="0">
<%
}
%>
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
      TANGER_OCX_EnableFilePrintMenu(false);
      TANGER_OCX_EnableFilePrintPreviewMenu(false);
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
</body>
</html>
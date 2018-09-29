<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
response.setHeader("Cache-Control", "no-store"); //HTTP 1.1 
response.setHeader("Pragma", "no-cache"); //HTTP 1.0 
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server  
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="<%=cssPath%>/style.css" rel="stylesheet" type="text/css" />
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>

<SCRIPT LANGUAGE="JavaScript" src="<%=contextPath%>/core/js/tangerocx.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" src="/inc/js/utility.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
function lock_ref(op)
{
  //_get("lock_ref.jsp", "ATTACHMENT_ID=&OP="+op);
  //setTimeout("lock_ref()",1000);
}
</SCRIPT>
<script>
function myload()
{
  var coll = window.opener.document.all.tags("A");
  var my_flag1=0;
  for (i=0; i<coll.length; i++)
  {
    if(coll[i].innerText== "编辑")
    {
       my_flag1=1;
       break;
    }
  }

  if(!my_flag1)
     window.close();

  //setTimeout("lock_ref()",1000);
  TANGER_OCX_SetInfo();
}

function MY_SetMarkModify(flag)
{
  if(flag)
  {
     mflag1.className="TableControl";
     mflag2.className="TableData";
  }
  else
  {
     mflag1.className="TableData";
     mflag2.className="TableControl";
  }
  TANGER_OCX_SetMarkModify(flag);
}

function MY_ShowRevisions(flag)
{
  if(flag)
  {
     sflag1.className="TableControl";
     sflag2.className="TableData";
  }
  else
  {
     sflag1.className="TableData";
     sflag2.className="TableControl";
  }
  TANGER_OCX_ShowRevisions(flag);
}

function SelSign()
{
   var SelSign=document.getElementById("SelSign");
   if(SelSign.style.display=="")
      SelSign.style.display="none";
   else
      SelSign.style.display="";
}

function SelSignFromURL(div_id,dir_field,name_field,disk_id)
{
   URL="/module/sel_file?EXT_FILTER=esp&DIV_ID=" + div_id + "&DIR_FIELD=" + dir_field + "&NAME_FIELD=" + name_field + "&TYPE_FIELD=" + disk_id;
   loc_x=event.clientX+100;
   loc_y=event.clientY-100;
   window.open(URL,"","height=300,width=500,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+loc_y+",left="+loc_x+",resizable=yes");
}
function selectword()
{
   URL="../word_model/view";
   myleft=(screen.availWidth-650)/2;
   window.open(URL,"formul_edit","height=350,width=400,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
}
function ShowLog()
{
   if($('OC_LOG').style.display == "none")
   {
      $('OC_LOG').style.display = "block";
      $('TANGER_OCX').style.display = "none";
      if($('OC_LOG').innerText == "")
         GetLog();
   }
   else
   {
      $('OC_LOG').style.display = "none";
      $('TANGER_OCX').style.display = "block";
   }
}
function GetLog(req)
{
  if(isUndefined(req))
  {
     _get("get_log.jsp","ATTACH_ID=",GetLog);
  }
  else if(req.status==200)
  {
     $('OC_LOG').innerHTML = req.responseText;
  }
}
</script>
</head>

<BODY class="" leftmargin="0" topmargin="5" onLoad="myload();this.focus();" onunload="javascript:close_doc()">
<form name="form1" id="form1" method="post" action="upload_OC.jsp" enctype="multipart/form-data">

<table width=100% height=100% class="small" cellspacing="1" cellpadding="3" align="center">
<tr width=100%>
<td valign=top width=80>
  <table class="TableBlock" width="100%" align="center">
     <tr class="TableHeader">
       <td nowrap align="center">文件操作</td>
     </tr>
     <tr class="TableData" onclick="TANGER_OCX_SaveDoc(0)" style="cursor:pointer;line-height:20px;">
       <td nowrap align="center">保存文件</td>
     </tr>
     <tr class="TableData" onclick="TANGER_OCX_ChgLayout()" style="cursor:pointer;line-height:20px;">
       <td nowrap align="center">页面设置</td>
     </tr>
     <tr class="TableData" onclick="TANGER_OCX_PrintDoc()" style="cursor:pointer;line-height:20px;">
       <td nowrap align="center">打印</td>
     </tr>
     <tr class="TableData" onclick="ShowLog()" style="cursor:pointer;line-height:20px;">
       <td nowrap align="center">操作日志</td>
     </tr>
   <tr class="TableHeader">
     <td nowrap align="center">文件编辑</td>
   </tr>

   <tr onclick="MY_SetMarkModify(true)" style="cursor:pointer;line-height:20px;">
     <td nowrap class="TableControl" align="center" id="mflag1" >保留痕迹 </td>
   </tr>
   <tr onclick="MY_SetMarkModify(false)" style="cursor:pointer;line-height:20px;">
     <td nowrap class="TableControl" align="center" id="mflag2" >不留痕迹 </td>
   </tr>
   <tr onclick="MY_ShowRevisions(true)" style="cursor:pointer;line-height:20px;">
     <td nowrap class="TableControl" align="center" id="sflag1">显示痕迹</td>
   </tr>
   <tr onclick="MY_ShowRevisions(false)" style="cursor:pointer;line-height:20px;">
     <td nowrap class="TableData" align="center" id="sflag2">隐藏痕迹</td>
   </tr>
    <tr onclick="return selectword();" style="cursor:pointer;line-height:20px;">
     <td nowrap class="TableData" align="center">文件套红</td>
   </tr>
   <tr onclick="AddPictureFromLocal()" style="cursor:pointer;line-height:20px;">
     <td nowrap class="TableData" align="center">插入图片</td>
   </tr>
   <tr class="TableHeader">
     <td nowrap align="center">电子认证</td>
   </tr>
     <tr class="TableData" onclick="DoCheckSign('')" style="cursor:pointer;line-height:20px;">
       <td nowrap align="center">验证签名及印章</td>
     </tr>
     <tr class="TableData" onclick="DoHandSign2('')" style="cursor:pointer;line-height:20px;">
       <td nowrap align="center">全屏手写签名</td>
     </tr>
     <tr class="TableData" onclick="DoHandDraw2('')" style="cursor:pointer;line-height:20px;">
       <td nowrap align="center">全屏手工绘图</td>
     </tr>
     <tr class="TableData" onclick="DoHandSign('')" style="cursor:pointer;line-height:20px;">
       <td nowrap align="center">插入手写签名</td>
     </tr>
     <tr class="TableData" onclick="DoHandDraw()" style="cursor:pointer;line-height:20px;">
       <td nowrap align="center">插入手工绘图</td>
     </tr>
     <tr class="TableData" onclick="AddSignFromLocal('')"style="cursor:pointer">
       <td nowrap align="center">加盖电子印章</td>
     </tr>
     <tr class="TableData" onclick="SelSign()"style="cursor:pointer">
       <td align="center">加盖电子印章<br>(从服务器)</td>
     </tr>
     <tr class="TableData" style="display:none" id="SelSign">
       <td nowrap align="center">
         <div id="SelFileDiv">请选择印章文件</div>
         <input type="hidden" value="" name="ATTACH_NAME">
         <input type="hidden" value="" name="ATTACH_DIR">
         <input type="hidden" value="" name="DISK_ID">
         <input type="button" class="SmallButton" value="选择" onclick="SelSignFromURL('SelFileDiv','ATTACH_DIR','ATTACH_NAME','DISK_ID')">
         <input type="button" class="SmallButton" value="加盖印章" onclick="AddSignFromURL('')">
       </td>
     </tr>
     <tr class="TableData" onclick="AddSecSignFromEkey()"style="cursor:pointer;line-height:20px;display:none;" id="tr_ekey">
       <td align="center">加盖电子印章<br>(从EKey)</td>
     </tr>
  </table>
</td>
<td width=100% valign="top">
<object id="TANGER_OCX" classid="<%=StaticData.Classid%>"
codebase="<%=contextPath%>/core/cntrls/<%=StaticData.Codebase%>" width="100%" height="750px">

<param name="IsNoCopy" value="0">
<param name="FileSave" value="1">
<param name="FileSaveAs" value="1">
<param name="IsNoCopy" value="1">
<param name="FileSave" value="0">
<param name="FileSaveAs" value="0">
<param name="BorderStyle" value="1">
<param name="BorderColor" value="14402205">
<param name="TitlebarColor" value="14402205">
<param name="TitlebarTextColor" value="0">
<param name="Caption" value="Office文档在线编辑">
<param name="IsShowToolMenu" value="-1">
<param name="IsHiddenOpenURL" value="0">
<param name="IsUseUTF8URL" value="-1">
<param name="MakerCaption" value="<%=StaticData.MakerCaption%>">
<param name="MakerKey" value="<%=StaticData.MakerKey%>">
<param name="ProductCaption" value="<%=StaticData.ProductCaption%>">
<param name="ProductKey" value="<%=StaticData.ProductKey%>">

<SPAN STYLE="color:red"><br>不能装载文档控件，请设置好IE安全级别为中或中低，不支持非IE内核的浏览器。</SPAN>
</object>
<div id="OC_LOG" align="center" style="display:none;"></div>
</td>
</tr>
</table>

<script language="JavaScript" for=TANGER_OCX event="OnDocumentClosed()">
TANGER_OCX_OnDocumentClosed()
</script>

<script language="JavaScript">
var TANGER_OCX_str;
var TANGER_OCX_obj;
var TANGER_OCX_bDocOpen;
function close_doc()
{
   document.all("TANGER_OCX").setAttribute("IsNoCopy",false);
   if(TANGER_OCX_bDocOpen)
   {
     msg='是否保存对  \'\'  的修改？';
     if(window.confirm(msg))
        TANGER_OCX_SaveDoc(0);
   }
   lock_ref('1');
}
</script>

<script language="JavaScript" for=TANGER_OCX event="OnDocumentOpened(TANGER_OCX_str,TANGER_OCX_obj)">
			TANGER_OCX_OnDocumentOpened(TANGER_OCX_str,TANGER_OCX_obj);
			TANGER_OCX_SetReadOnly(false);
      TANGER_OCX_ShowRevisions(true);
   		TANGER_OCX_SetMarkModify(true);
</script>
<script language="JavaScript" for="TANGER_OCX" event="OnSignSelect(issign,signinfo)">
//TANGER_OCX_OnSignSelect(issign,signinfo)
</script>
<span id="TANGER_OCX_op" style="display:none">4</span>
<span id="TANGER_OCX_filename" style="display:none"></span>
<span id="TANGER_OCX_attachName" style="display:none"></span>
<span id="TANGER_OCX_attachURL" style="display:none"><%=contextPath%>/yh/core/funcs/netdisk/act/YHNetDiskAct/getFileContent.act?name=weblogic安装及配置指南.doc&baseFilePath=core</span>
<span id="TANGER_OCX_user" style="display:none"></span>

<input style="display:none" type="file" name="ATTACHMENT">
<input type="hidden" name="NETDISK_ID" value="">
<input type="hidden" name="fichier" value="">
<input type="hidden" name="DOC_SIZE" value="">
</form>

</body></html>
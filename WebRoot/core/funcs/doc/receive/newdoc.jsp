<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="yh.core.funcs.doc.receive.data.YHDocConst"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%
YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER"); 
  String userName = "";
  int userId = 0;
  if(user != null){
	  userName = user.getUserName();
	  userId = user.getSeqId();
  }
  String seqId = (String)request.getAttribute("seqId");
  String id = "";
  if(seqId != null && seqId != ""){
    id = seqId;
  }
  String webroot = request.getRealPath("/");
%>
<head>
<title>收文登记</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css"/>
<link rel="stylesheet" type="text/css" href="/inc/js/jquery/page/css/page.css"/>
<link rel="stylesheet" type="text/css" href="/theme/<?=$LOGIN_THEME?>/calendar.css"/>
<style>
.tip {position:absolute;display:none;text-align:center;font-size:9pt;font-weight:bold;z-index:65535;background-color:#DE7293;color:white;padding:5px}
.auto{text-overflow:ellipsis;white-space:nowrap;overflow:hidden;}
</style>
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<link rel="stylesheet" href ="<%=cssPath %>/style.css"/>
<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/doc/receive/js/newdoc.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/doc/receive/js/MultiUserSelect.js" ></script>
<script type="text/Javascript">
var upload_limit=1,limit_type=limitUploadFiles;
var isUploadBackFun = false;
var seqId = "<%=id%>";
var secrtGrade = "<%=YHDocConst.getProp(webroot , YHDocConst.SECRET_GRADE)%>";
var docType = "<%=YHDocConst.getProp(webroot , YHDocConst.DOC_TYPE)%>";
var seqId = "<%=id%>";
  function doInit(){
    getNRegList();
    var beginParameters = {
        inputId:'startTime',
        property:{isHaveTime:false}
        ,bindToBtn:'beginDateImg'
        };
    new Calendar(beginParameters);

    var defauDate = new Date();
    var year = defauDate.getFullYear();
    var month = defauDate.getMonth()+1;
    var day = defauDate.getDate();
   // $("startTime").value = year +"-"+ initDate(month) +"-" + initDate(day);
    parseObj(secrtGrade, "confLevel");
    parseObj(docType, "docType");
    var userName = "<%=userName%>";
    var userId = "<%=userId%>";
    if(userName){
      $("userId").value = userName;
    }
    if(userId != "0"){
      $("user").value = userId;
    }
    initDept();
    getMaxOrderNo(1);
  }
</script>
</head>
<body topmargin="5" class="bodycolor" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif"/><span class="big3">未登记收文</span>
    </td>
  </tr>
</table>
<div id="container"></div><br/>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif"/><span class="big3">收文登记</span>
    </td>
  </tr>
</table>
<form action="<%=contextPath%>/yh/core/funcs/doc/receive/act/YHDocReceiveAct/addYHDocReceive.act" id="form1" method="post" name="form1" >
<table class="TableBlock" width="70%" align="center">
  <tr>
    <td nowrap class="TableContent" width="120">收文类型：<font style="color:red">*</font>&nbsp;</td>
    <td nowrap class="TableData">
       <select name="docType" id="docType" class="BigSelect" onchange="onselChange();return false;">
         
       </select>
        <font id="docTypeMsg" style="color:red"></font>
    </td>
   </tr>
   <tr>
	    <td nowrap class="TableContent">收文文号：<font style="color:red">*</font> &nbsp;</td>
	    <td nowrap class="TableData">
	       <input type="text" name="docNo" id="docNo" class="BigInput" size="33" maxlength="100" value="" readonly/>&nbsp;
	       <font id="docNoMsg" style="color:red"></font> 
	    </td>
   </tr>
   <tr>
	    <td nowrap class="TableContent" width="120">来文单位：<font style="color:red">*</font>&nbsp;</td>
	    <td nowrap class="TableData">
	        <input type="text" name="fromUnits" id="fromUnits" class="BigInput" size="33" maxlength="100" value=""/>&nbsp;
	         <font id="fromUnitsMsg" style="color:red"></font> 
	    </td>
   </tr>
   <tr>
    <td nowrap class="TableContent">密级：<font style="color:red">*</font>&nbsp;</td>
    <td nowrap class="TableData">
        <select name="confLevel" id="confLevel"  class="BigSelect">       
        </select>
        <font id="confLevelMsg" style="color:red"></font> 
    </td>
   </tr>
    <tr>
    <td nowrap class="TableContent" width="120">对方文号：<font style="color:red">*</font>&nbsp;</td>
    <td nowrap class="TableData">
        <input type="text" name="oppDocNo" id="oppDocNo" class="BigInput" size="33" maxlength="100" value=""/>&nbsp;
        <font id="oppDocNoMsg" style="color:red"></font> 
    </td>
   </tr>
   <tr>
    <td nowrap class="TableContent" width="120">标题：<font style="color:red">*</font>&nbsp;</td>
    <td nowrap class="TableData">
        <input type="text" name="title" id="title" class="BigInput" size="33" maxlength="100" value=""/>&nbsp;
        <font id="titleMsg" style="color:red"></font> 
    </td>
   </tr>
   <tr>
    <td nowrap class="TableContent" width="120">份数：<font style="color:red">*</font>&nbsp;</td>
    <td nowrap class="TableData">
        <input type="text" name="copies" id ="copies" class="BigInput" size="33" maxlength="100" value=""/>&nbsp;
        <font id="copiesMsg" style="color:red"></font> 
    </td>
   </tr>
  <!--  <tr>
		    <td nowrap class="TableContent">日期：<font style="color:red">*</font>&nbsp;</td>
		    <td nowrap class="TableData">
			    <input type="text" id="startTime" onfocus="this.select();" name="startTime" class="BigInput" value="" style="width:80px;" />
	        <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:hand" />
	        <font id="startTimeMsg" style="color:red"></font> 
		    </td>
	</tr> -->
	<tr>
    <td nowrap class="TableContent" width="120">承办单位:&nbsp;</td>
    <td nowrap class="TableData">
        <!--  <input type="text" name="sponsor" id="sponsor" class="BigInput" size="33" maxlength="100" value=""/>&nbsp;
        <font id="sponsorMsg" style="color:red"></font> -->
        <select name="deptId" id="deptId">
	       </select>
    </td>
   </tr>
  <!--
	<tr>
    <td nowrap class="TableContent" width="120">领导批示：</td>
    <td nowrap class="TableData">
      <textarea cols=37 rows=3 name="instruct" id ="instruct" class="BigInput" ></textarea>
    </td>
   </tr>
  <tr>
	  			<td nowrap  class="TableContent" >签收人：<font style="color:red">*</font></td>
	  			<td nowrap class="TableData">
           <input type="hidden" id="recipient" name="recipient" value="" />
           <input type="text"name="recipientId" id="recipientId" class="SmallStatic"  value="" readonly/>
           <a href="javascript:void(0);" class="orgAdd" onclick="selectSingleUserByDept(['recipient', 'recipientId'],1)" title="添加收件人">添加</a>
           <a href="javascript:void(0);" class="orgClear" onclick="javascript:clearValue();" title="清空收件人">清空</a>
           <font id="recipientMsg" style="color:red"></font>           					  				
	  			</td>
	  		</tr>  --> 	    
    <tr style="display:none">
	  			<td nowrap  class="TableContent" >登记人(默认当前用户)：<font style="color:red">*</font></td>
	  			<td nowrap class="TableData">
           <input type="hidden" id="user" name="user" value="" />
           <input type="text" name="userId" id="userId" class="SmallStatic"  value="" readonly/>
           <a href="javascript:void(0);" class="orgAdd" onclick="selectSingleUser(['user', 'userId'])" title="添加收件人">添加</a>
           <a href="javascript:void(0);" class="orgClear" onclick="javascript:clearUser();" title="清空收件人">清空</a>
           <!--  <input type="checkbox" name="alarm" id="alarm"/>提醒签收人&nbsp;&nbsp;&nbsp;-->
           <font id="userMsg" style="color:red"></font>  
	  			</td>
	  		</tr>	  
   
   <tr id="attr_tr">
      <td nowrap class="TableContent">附件: </td>
      <td class="TableData" id="showAttachment">
        <input type="hidden" id="attachmentId" name="attachmentId">
        <input type="hidden" id="attachmentName" name="attachmentName">
        <input type="hidden" id="ensize" name="ensize">
        <input type="hidden" id="moduel" name="moduel" value="doc">
        <span id="showAtt">
        </span>
      </td>
    </tr>
      <tr id="fileShowId">
      <td nowrap class="TableContent">附件选择：</td>
      <td class="TableData" id="fsUploadRow">
	         <div id="fsUploadArea" class="flash" style="width:380px;">
				     <div id="fsUploadProgress"></div>
				     <div id="totalStatics" class="totalStatics"></div>
				     <div style="display:none;">
				       <input type="button" id="btnStart" class="SmallButtonW" value="开始上传" onclick="swfupload.startUpload();" disabled="disabled">&nbsp;&nbsp;
				       <input type="button" id="btnCancel" class="SmallButtonW" value="全部取消" onclick="swfupload.cancelQueue();" disabled="disabled">&nbsp;&nbsp;
				       <input type="button" class="SmallButtonW" value="刷新页面" onclick="upload_attach_group();">
				    </div>
			      </div>
			      <div id="attachment1">
		          <script>ShowAddFile();ShowAddImage();</script>
		          <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传</a>'</script>
		           <input type="hidden" name="ATTACHMENT_ID_OLD" id="ATTACHMENT_ID_OLD" value="">
                  <input type="hidden" name="ATTACHMENT_NAME_OLD" id="ATTACHMENT_NAME_OLD" value="">
                   <span id="spanButtonUpload" title="批量上传附件"> </span>
                  
		        </div><!--
		        <input type="checkbox" name="download" id="download" checked><label for="download">允许下载Office附件</label>&nbsp;&nbsp;
              <input type="checkbox" name="print" id="print" checked><label for="print">允许打印Office附件</label>&nbsp;&nbsp;&nbsp;<font color="gray">都不选中则只能阅读附件内容</font>-->
      &nbsp;</td>
    </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="button" id="save" name="save" value="登记" class="BigButton"  name="button" onclick="doSubmit('1');"/>&nbsp;
   </td>
   </tr>
   <input type="hidden" id="ftype" name="ftype" value=""/>
</table>
</form>
<form id="formFile" action="<%=contextPath %>/yh/core/funcs/doc/receive/act/YHDocReceiveAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
</form>
<iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe>
</body>
</html>
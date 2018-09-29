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

<%@page import="yh.core.funcs.person.data.YHPerson"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件柜</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/orgselect.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>

<!-- 文件上传 -->
<link href="<%=cssPath %>/cmp/swfupload.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<!-- <script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script> -->
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/pagePilot.js"></script>

<script type="text/javascript" src="<%=contextPath %>/subsys/oa/confidentialFile/showConfidentialFile/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/confidentialFile/showConfidentialFile/js/showConfidentialFilelogic.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript">
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;
var seqId='<%=seqId%>';

var pageMgr = null;
function doInit(){
	getFolderPathById("<%=seqId%>");
	showManage("<%=seqId%>");
  var url = "<%=contextPath%>/yh/subsys/oa/confidentialFile/act/YHConfidentialContentAct/getContentFileListJson.act?seqId=<%=seqId%>";
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 2,
      sortDirect: "desc",
      moduleName:"confidential", 
      colums: [
         {type:"selfdef", text:"选择", width: '0', render:checkBoxRender},
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"subject",  width: '10%', text:"文件名称", align:"left",render:fileSendReadFunc,sortDef:{type: 0, direct:"desc"}},       
         {type:"hidden", name:"attachId", text:"附件ID",align:"center",width:"1%"},
         {type:"text", name:"attach", text:"附件",align:"left",width:"20%",dataType:"attach"},
         {type:"data", name:"sendTime",  width: '5', text:"发布时间", render:sendDateFunc,sortDef:{type: 0, direct:"desc"}},
         {type:"data", name:"contentNo",  width: '5', text:"排序号", render:alignFunc,sortDef:{type: 0, direct:"desc"}},
         {type:"data", name:"fileSend",  width: '2', text:"报送状态", render:fileSendFunc},
         {type:"hidden", name:"sortId",  width: '1', text:"文件夹号", dataType:"int"},
         {type:"selfdef", text:"操作", width: '2',render:optsList}]
    };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
    showCntrl('delOpt');
    if(managePriv == "1"){
			$('cutFile').show();
			$('delFile').show();			
		}
  }else{
  	$("queryFile").hide();
    WarningMsrg("该文件夹尚无文件", 'nothingDiv');
  }
}

window.onload = function() {	
	doInit();
  var linkColor = document.linkColor;
  var settings = {
    flash_url : "<%=contextPath %>/core/cntrls/swfupload.swf",
    upload_url: $("form1").action,	
    post_params: {"PHPSESSID" : "<%=session.getId()%>"},
    file_size_limit : "1000 MB",
    file_types : "*.*",
    file_types_description : "All Files",
    file_upload_limit : 100,
    file_queue_limit : 0,
    custom_settings : {
      uploadRow : "fsUploadRow",
      uploadArea : "fsUploadArea",
      progressTarget : "fsUploadProgress",	//上传处理
      startButtonId : "btnStart",						//开始上传      cancelButtonId : "btnCancel"  				//全部取消
    },
    debug: false,
    button_image_url: "<%=imgPath %>/uploadx4.gif",
    button_width: "65",
    button_height: "29",
    button_placeholder_id: "spanButtonPlaceHolder",
    button_text: '<span class=\"textUpload\">批量上传</span>',
    button_text_style: ".textUpload{color:" + linkColor + ";}",
    button_text_top_padding : 1,
    button_text_left_padding : 18,
    button_placeholder_id : "spanButtonUpload",
    button_width: 70,
    button_height: 18,
    button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
    button_cursor: SWFUpload.CURSOR.HAND,
    
    file_queued_handler : fileQueued,
    file_queue_error_handler : fileQueueError,
    file_dialog_complete_handler : fileDialogComplete,
    upload_start_handler : uploadStart,
    upload_progress_handler : uploadProgress,
    upload_error_handler : uploadError,
    upload_success_handler : uploadSuccess,
    upload_complete_handler : uploadComplete,
    queue_complete_handler : queueComplete
  };

  swfupload = new SWFUpload(settings);
}


</script>
</head>
<body >
<table id="headTableStr" border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_open.gif" align="middle"><b>&nbsp;<span class="Big1" id="folderPath"> </span></b><br>
   </td>
  </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
<table class="TableList" width="100%">
	<tr class="TableControl">
		<td colspan="9"> 
			&nbsp;&nbsp;<input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this)">&nbsp;<label for='checkAlls'>全选</label> &nbsp;
			<a href='javascript:doAction("fileSend","<%=seqId %>");' id='sign'><img src='<%=imgPath %>/email_open.gif' align="middle" border='0' title='报送文件'>报送&nbsp;&nbsp;</a>
			<a href='javascript:doAction("getBack","<%=seqId %>");' id='sign'><img src='<%=imgPath %>/email_open.gif' align="middle" border='0' title='撤回文件'>撤回&nbsp;&nbsp;</a>
			<a href='javascript:doAction("copyFile","<%=seqId %>")' id='copyFile'><img src='<%=imgPath %>/copy.gif' align='middle' border='0' title='复制所选文件'>复制&nbsp;&nbsp;</a>
			<a style="display: none;" href='javascript:doAction("cutFile","<%=seqId %>")' id='cutFile'><img src='<%=imgPath %>/cut.gif' align='middle' border='0' title='剪切所选文件'>剪切&nbsp;&nbsp;</a>
			<a style="display: none;" href='javascript:deleteArrang();' id='delFile'><img src='<%=imgPath %>/delete.gif' align='middle' border='0' title='删除所选文件'>删除&nbsp;&nbsp;</a>
			<a href='javascript:doAction("downFile","<%=seqId %>");' id='downFile' ><img src='<%=imgPath%>/download.gif' align='middle' border='0' title='批量压缩后下载'><span id='label_down'>下载</span>&nbsp;&nbsp;</a>
		</td>
	</tr>
</table>
</div>


<div id="nothingDiv" align="center"></div>
<div id="noPrivDiv" style="display: none">
<table class="MessageBox" align="center" width="340">
  <tr>
    <td class="msg error">
      <h4 class="title">错误</h4>
      <div class="content" style="font-size:12pt">您没有权限访问该目录</div>
    </td>
  </tr>
</table>
</div>

<br>
<form name="form1" id="form1" action="<%=contextPath%>/yh/subsys/oa/confidentialFile/act/YHConfidentialContentAct/uploadBatchFile.act?seqId=<%=seqId %>" method="post" enctype="multipart/form-data" >
<input type="hidden" name="sortParentStr" id="sortParentStr" value="" >
<table>
  <tr id="">
    <td colspan="3">
	    <div id="fsUploadArea" class="flash" style="width:380px;">
		  <div id="fsUploadProgress"></div>
			<div id="totalStatics" class="totalStatics"></div>
			<div>	
			<div>
				内部短信提醒：<br>
				<input type="radio"	name="SMS_SELECT_REMIND" id="SMS_SELECT_REMIND0" value="0" onclick="document.getElementById('SMS_SELECT_REMIND_SPAN').style.display='';" checked><label for="SMS_SELECT_REMIND0">手动选择被提醒人员</label> 
				<input 	type="radio" name="SMS_SELECT_REMIND" id="SMS_SELECT_REMIND1"	value="1"	onclick="document.getElementById('SMS_SELECT_REMIND_SPAN').style.display='none';"><label	for="SMS_SELECT_REMIND1">提醒全部有权限人员</label><br>
				<span id="SMS_SELECT_REMIND_SPAN"> <input type="hidden"	name="user" id="user" value=""> <textarea cols=30	name="userDesc" id="userDesc" rows="2" class="BigStatic" wrap="yes"	readonly></textarea> 
					<a href="javascript:;" class="orgAdd"	onClick="selectUser();">添加</a> 
					<a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
				</span>
			</div><br>	
			<div id="sms2remindDiv">
				手机短信提醒：<br>
				<input type="radio"	name="moblieSMS_SELECT_REMIND" id="moblieSMS_SELECT_REMIND0" value="0" onclick="document.getElementById('moblieSMS_SELECT_REMIND_SPAN').style.display='';" checked><label for="moblieSMS_SELECT_REMIND0">手动选择被提醒人员</label> 
				<input 	type="radio" name="moblieSMS_SELECT_REMIND" id="moblieSMS_SELECT_REMIND1"	value="1"	onclick="document.getElementById('moblieSMS_SELECT_REMIND_SPAN').style.display='none';"><label	for="moblieSMS_SELECT_REMIND1">提醒全部有权限人员</label><br>
				<span id="moblieSMS_SELECT_REMIND_SPAN">
				<input type="hidden"	name="user1" id="user1" value=""><textarea cols=30	name="userDesc1" id="userDesc1" rows="2" class="BigStatic" wrap="yes"	readonly></textarea> 
			  <a href="javascript:;" class="orgAdd" onClick="selectUser(['user1', 'userDesc1']);">添加</a> 
			  <a href="javascript:;"	class="orgClear" onClick="$('user1').value='';$('userDesc1').value='';">清空</a>
				</span>
			</div><br>						
			  <input type="button" id="btnStart" class="SmallButtonW" value="开始上传" onclick="sms_submit();swfupload.startUpload();" >&nbsp;&nbsp;
				<input type="button" id="btnCancel" class="SmallButtonW" value="全部取消" onclick="swfupload.cancelQueue();" >&nbsp;&nbsp;
				<input type="button" class="SmallButtonW" value="刷新页面" onclick="window.location.reload();">
		  </div>
		 </div>
	 </td>
  </tr>
</table>
</form>  
<br>
<table id="fileTable" class="TableBlock" width="100%" align="center">
  <tr>
    <td class="TableContent" nowrap align="center" width="80"><b>文件操作：</b></td>
    <td class="TableControl">&nbsp;
    	<a id='queryFile' href="<%=contextPath %>/subsys/oa/confidentialFile/showConfidentialFile/fileRegister/query.jsp?seqId=<%=seqId %>" title="查询文件" style="height:20px;"><img src="<%=imgPath %>/folder_search.gif" align="middle" border="0">&nbsp;查询&nbsp;&nbsp;</a>
			<a id="globalQuery" href="<%=contextPath %>/subsys/oa/confidentialFile/showConfidentialFile/fileRegister/globalQuery.jsp" title="全局搜索" style="height:20px;"><img src="<%=imgPath %>/folder_search.gif" align="middle" border="0">&nbsp;全局搜索&nbsp;&nbsp;</a>
   		<span id="paste_file" style="display:none;" title="粘贴文件"><a href="javascript:pasteFile(<%=seqId %>)" style="height:20px;"><img src="<%=contextPath %>/core/funcs/filefolder/images/paste.gif" align="middle" border="0">&nbsp;粘贴</a>&nbsp;&nbsp;</span>
   		<span id="newFile" style="display: none"><a href="javascript:newFile('<%=seqId %>')"; title="创建新的文件" style="height:20px;"><img src="<%=imgPath%>/notify_new.gif" align="middle" border="0">&nbsp;新建文件&nbsp;&nbsp;</a></span>
   		<span id="battUpload" style="display: none"><span id="spanButtonUpload" title="批量上传">&nbsp;&nbsp;</span></span>
 		</td>
  </tr>
  <tr>
    <td class="TableContent" nowrap align="center" width="80"><b>文件夹操作：</b></td>
    <td class="TableControl">&nbsp;
   		<span id="paste_sort" style="display: none;" title="粘贴文件夹" ><a href="javascript:pasteSort('<%=seqId %>')"><img src="<%=contextPath %>/core/funcs/filefolder/images/paste.gif" align="middle" border="0">&nbsp;粘贴</a>&nbsp;&nbsp;</span>
   		<a style="display: none;" href="<%=contextPath%>/subsys/oa/confidentialFile/showConfidentialFile/fileRegister/new/newFolder.jsp?seqId=<%=seqId %>" title="创建子文件夹" id="newSubFolder" ><img src="<%=imgPath%>/newfolder.gif" align="middle" border="0">&nbsp;新建子文件夹&nbsp;&nbsp;</a>
   		<a style="display: none;" href="<%=contextPath%>/subsys/oa/confidentialFile/showConfidentialFile/fileRegister/setPriv/index.jsp?seqId=<%=seqId %>" title="设置该文件夹的访问、管理、新建和下载权限" id="setFolderPriv"><img src="<%=imgPath%>/folder_priv.gif" align="middle" border="0">&nbsp;设置权限&nbsp;&nbsp;</a>
   		<a style="display: none;" href="<%=contextPath%>/subsys/oa/confidentialFile/showConfidentialFile/fileRegister/editSubFolder.jsp?seqId=<%=seqId %>" title="重命名此文件夹" id="reNameSubFolder"><img src="<%=contextPath %>/core/funcs/filefolder/images/folder_edit.gif" align="middle" border="0">&nbsp;重命名&nbsp;&nbsp;</a>
   		<a style="display: none;" href="javascript:sortAction('copy','<%=seqId %>');" id="copyFolder"><img src="<%=contextPath %>/core/funcs/filefolder/images/copy.gif" align="middle" border="0" title="复制当前文件夹" >&nbsp;复制&nbsp;&nbsp;</a>
   		<a style="display: none;" href="javascript:sortAction('cut','<%=seqId %>');" id="cutFolder"><img src="<%=contextPath %>/core/funcs/filefolder/images/cut.gif" align="middle" border="0" title="剪切当前文件夹">&nbsp;剪切&nbsp;&nbsp;</a>
  		<a style="display: none;" href="javascript:deleteSort('<%=seqId %>');" title="删除此文件夹" id="delFolder"><img src="<%=contextPath %>/core/funcs/filefolder/images/delete.gif" align="middle" border="0">&nbsp;删除目录&nbsp;&nbsp;</a>   		
    </td>
  </tr>
</table>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="yh.core.funcs.news.data.YHNews"%>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null) {
	  seqId = "";
  }
  YHNews news = (YHNews)request.getAttribute("news");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<title>发布新闻</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/swfupload.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="js/interfaceLogic.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var oFCKeditor = new FCKeditor('Econtent');
var fckTimer = null; 
String.prototype.trim= function()  
{  
    return this.replace(/(^\s*)|(\s*$)/g, "");
}
function addDept() {
 	var URL="<%=contextPath %>/core/funcs/orgselect/MultiDeptSelect.jsp";
  	openDialog(URL,'500', '500');
}
function selectUser() {
 	var URL="<%=contextPath %>/core/funcs/orgselect/MultiUserSelect.jsp";
  	openDialog(URL,'500', '500');
}
function selectRole() {
 	var URL="<%=contextPath %>/core/funcs/orgselect/MultiRoleSelect.jsp";
  	openDialog(URL,'500', '500');
}
function changeRange()
{
   if (document.getElementById("rang_role").style.display=="none")
   {
      document.getElementById("rang_role").style.display="";
      document.getElementById("rang_user").style.display="";
      document.getElementById("href_txt").innerText="隐藏按人员或角色发布";
   }
   else
   {
      document.getElementById("rang_role").style.display="none";
      document.getElementById("rang_user").style.display="none";
      document.getElementById("href_txt").innerText="按人员或角色发布";
   }
}

function CheckForm()
{
   if(document.newsForm.subject.value.trim()=="请输入公告标题...")
      document.newsForm.subject.value="";
      
   if(document.newsForm.subject.value.trim()=="")
   { 
      alert("公告通知的标题不能为空！");
      return (false);
   }
   
   if(document.newsForm.dept.value==""&&document.newsForm.role.value==""&&document.newsForm.user.value=="")
   { 
      alert("请指定发布范围！");
      return (false);
   }

   if(document.newsForm.format.value=="1")
   { 
      var inputs=document.getElementsByTagName("input");
      var file_count=0;
      for(var i=0;i<inputs.length;i++)
      {
         var el = inputs[i];
         var elType = el.type.toLowerCase();
         if(elType=="file")
         {
            if(el.value!="")
            file_count++;
         }		
      }
      if (file_count==0)
      {
         alert("请选择MHT文件 ！");
         return (false);
      }
   }
   
   if(document.newsForm.urlAdd.value=="" && document.newsForm.format.value=="2")
   { 
      alert("请指定超级链接地址 ！");
      return (false);
   }

   if(document.newsForm.print.value=="on")
	   document.newsForm.print.value='1';
	 else
		 document.newsForm.print.value='0';
	   
	 if(document.newsForm.download.value=="on")
		 document.newsForm.download.value='1';
	 else
		 document.newsForm.download.value='0';
   
   return (true);
}

function sendForm(publish)
{
   document.newsForm.publish.value=publish;
   if(CheckForm())
   {
      document.newsForm.op.value = publish=="0" ? "0" : "1";
      if(publish!=2)
    	  savenews();
      else
         _edit();
   }
}

function _edit()
{
   ShowDialog("optionBlock");	
}
function savenews(){
	  var FCK = FCKeditorAPI.GetInstance('Econtent'); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
	  var FORM_MODE = FCK.EditingArea.Mode;
	 
	  //获取编辑区域的常量——源文件模式
	  var editingAreaFrame = document.getElementById('Econtent___Frame');
	  var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;//常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
	  if(FORM_MODE == editModeSourceConst)
	  {
	    FCK.Commands.GetCommand( 'Source' ).Execute();
	  } 
	 // $("formName").value = formName;
	  var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
	  var textStr = FORM_HTML;
	  textStr = textStr.replace(/\"/g,"\\\"");
	  textStr = textStr.replace(/\'/g,"\'");
	  textStr = textStr.replace(/[[\n\r\f]/g,"");
	  var url = "/yh/yh/core/funcs/news/act/YHNewsHandleAct/addNews.act";
	  document.newsForm.content.value = textStr;
	  //alert(textStr);
	  $("newsForm").action = url;
	  $("newsForm").submit();
}

var seqId = "<%=seqId%>";
var oFCKeditor = new FCKeditor('Econtent');
var fckTimer = null; 
function doInit(){
  var FCK = FCKeditorAPI.GetInstance('Econtent'); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
  if (FCK.EditingArea) {
    if (fckTimer) {
      clearTimeout(fckTimer);
      fckTimer = null;
    }
  }else {
	  
    fckTimer = setTimeout(doInit, 100);
    return;
  }
  
  initSwfUpload();
}
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;

function initSwfUpload() {
    var linkColor = document.linkColor;
    var settings = {
      flash_url : "<%=contextPath %>/core/cntrls/swfupload.swf",
      upload_url: "<%=contextPath %>/yh/core/funcs/news/act/YHNewsHandleAct/fileLoad.act",
      post_params: {"PHPSESSID" : "<%=session.getId()%>"},
      file_size_limit : (maxUploadSize + " MB"),
      file_types : "*.*",
      file_types_description : "All Files",
      file_upload_limit : 100,
      file_queue_limit : 0,
      custom_settings : {
        uploadRow : "fsUploadRow",
        uploadArea : "fsUploadArea",
        progressTarget : "fsUploadProgress",
        startButtonId : "btnStart",
        cancelButtonId : "btnCancel"
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
      upload_success_handler : uploadSuccessOver,
      upload_complete_handler : uploadComplete,
      queue_complete_handler : queueComplete
    };

    swfupload = new SWFUpload(settings);
    var attachmentIds = $("attachmentId").value;
    var attachmentName = $("attachmentName").value;
    showAttach(attachmentIds,attachmentName,"showAtt");
  }
function uploadSuccessOver(file, serverData){
	  try {
	    var progress = new FileProgress(file, this.customSettings.progressTarget);
//	    progress.setComplete();
//	    progress.setStatus("Complete.");
	    progress.toggleCancel(false);
	    var json = null;
	    json = serverData.evalJSON();
	    if(json.state=="1") {
	       progress.setError();
	       progress.setStatus("上传失败：" + serverData.substr(5));
	       
	       var stats=this.getStats();
	       stats.successful_uploads--;
	       stats.upload_errors++;
	       this.setStats(stats);
	    } else {
	       $('attachmentId').value += json.data.attachmentId;
	       $('attachmentName').value += json.data.attachmentName;
	       var attachmentIds = $("attachmentId").value;
	       var attachmentName = $("attachmentName").value;
	       var ensize =  $('ensize').value;
	       if(ensize){
	       $('ensize').value =(json.data.size + parseInt(ensize));
	       }else{
	         $('ensize').value =json.data.size ;
	       }//附件大小
	       showAttach(attachmentIds,attachmentName,"showAtt");
	    }
	  } catch (ex) {
	    this.debug(ex);
	  }
	}

/**
 * 处理附件的显示

 * @param cntrlId
 * @return
 */
function showAttach(attrIds,attrNames,cntrId){
  var reStr = "<div id='attrDiv'>";
  var ym = "";
  var attrId = ""
  var attrIdArrays = attrIds.split(",");
  var attrNameArrays = attrNames.split("*");
  for(var i = 0 ; i <= attrIdArrays.length; i++){
    if(!attrIdArrays[i]){
      continue;
    }
    var key = attrIdArrays[i];
    var attrName = attrNameArrays[i];
    var value = attrName.substring( attrName.indexOf("_")+1, attrName.length);
    reStr += "<a href=\"javascript:downFile(\'" + key + "\',\'" + value + "\');\" title=\"" + value + "\">" + value + "</a><br>";
    //MODULE=email&amp;YM=1001&amp;ATTACHMENT_ID=216664316&amp;ATTACHMENT_NAME=SoftMgrUninst.exe
  }
  reStr += "</div>";
  if(cntrId){
    $(cntrId).innerHTML = reStr;
  }else{
    document.write(reStr);
  }
}
function upload_attach()
{
  saveMaiByUp();
}
function showMenu(selectesId,i) {
}
function changeFormat(typeID)
{ 
   if(typeID=="1")
   {
      document.getElementById("editor").style.display="none";
      document.getElementById("attachment1").style.display="";
      document.getElementById("url_address").style.display="none";
      document.getElementById("format").value="1";
//      document.getElementById("status").innerText="MHT格式";
   }
   else if(typeID=="0")
   {
      document.getElementById("editor").style.display="";
      document.getElementById("attachment1").style.display="";
      document.getElementById("url_address").style.display="none";
      document.getElementById("format").value="0";
//      document.getElementById("status").innerText="普通格式";
   }
   else if(typeID=="2")
   {
      document.getElementById("editor").style.display="none";
      document.getElementById("attachment1").style.display="none";
      document.getElementById("url_address").style.display="";
      document.getElementById("urlAdd").value="http://";
      document.getElementById("format").value="2";
 //     document.getElementById("status").innerText="超级链接";
   }
}
function showSubject(){
  var actionSize = $('actionSize').value;
  var actionLight = $('actionLight').value;
  var actionFont = $('actionFont').value;
  var actionLights = $('actionLights').value;
  var actionColor = $('actionColor').value;
  var actionFlag = $('actionLightFlag').value;
  
  var subjectFont = "font-family:" + actionFont + ";font-size:" + actionSize + ";color:" + actionColor + ";filter:" + actionFlag + "(Direction=120, color=" + actionLights + ");";
  $('subjectMainShow').innerHTML = $('subject').value;
  $('subjectMainShow').setStyle(subjectFont);
  
  $('subject_tr').style.display = "";
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=contextPath %>/core/styles/imgs/green_plus.gif"><span class="big1"> 新建新闻</span>&nbsp;&nbsp;
    <select name="typeSelect" class="BigSelect" onChange="changeFormat(this.value)">
        <option value="0" selected> <a href="javascript:changeFormat(0);" style="color:#0000FF;">普通格式</a></option>
        <option value="1"><a href="javascript:changeFormat(1);" style="color:#0000FF;" title="mht格式支持图文混排，Word文档可以直接另存为mht文件；超级链接可直接链接到具体网址。">MHT格式</a></option>
        <option value="2"><a href="javascript:changeFormat(2);" style="color:#0000FF;">超级链接</a></option>
     </select>
    </td>
  </tr>
</table>
<form enctype="multipart/form-data" action="<%=contextPath %>/yh/core/funcs/news/act/YHNewsHandleAct/addNews.act"  method="post" name="newsForm" id="newsForm">
<table class="TableBlock" width="95%" align="center">
    <tr>
      <td nowrap class="TableData">&nbsp;<select name="typeId" style="background: white;" title="新闻类型可在“系统管理”->“分类码管理”模块设置。">
          <option value="0">选择新闻类型</option>
          <option value="1">选择新闻类型测试一</option>
          <option value="2">选择新闻类型测试二</option>
        </select></td>
      <td class="TableData" >
        <input type="text" name="subject" id="subject" size="55" maxlength="200" class="BigInput" value="<%=news.getSubject() %>" style="color: #8896A0"
         onMouseOver="if($F('subject')=='请输入新闻标题...') document.getElementById('subject').style.color='#000000';" 
         onMouseOut="if($F('subject')=='请输入新闻标题...') document.getElementById('subject').style.color='#8896A0';" 
         onFocus="if($F('subject')=='请输入新闻标题...') {document.getElementById('subject').value='';document.getElementById('subject').style.color='#000000';}">
      </td>
    </tr>
 	<tr id="subject_tr" style="display: none;height:50px;">
   <td nowrap class="TableData" >预览</td>
   <td class="TableData">
     <span id="subjectMainShow"></span>
   </td>
	</tr>
	<tr>
	  <td nowrap class="TableData">标题样式：</td>
	  <td nowrap class="TableData">
	      <input type="hidden" name="FONT_FAMILY" value="">
	      <input type="hidden" name="FONT_SIZE" value="">
	      <input type="hidden" name="FONT_COLOR" value="">
	      <input type="hidden" name="FONT_FILTER" value="">
	  <span style="padding-top:5px;padding-left:10px;">
	   <a id="showMeunA" href="#" onclick="showFont(event);"><span id="actionNameFont">字体</span></a><img src="<%=imgPath%>/cal_list.gif" align="absmiddle">&nbsp;
	  </span>
	  <span style="padding-top:5px;padding-left:10px;">
	   <a id="showMeunB" href="#" onclick="showSize(event);"><span id="actionNameSize">大小</span></a><img src="<%=imgPath%>/cal_list.gif" align="absmiddle">&nbsp;
	  </span>
	  <span style="padding-top:5px;padding-left:10px;">
	   <a id="showMeunC" href="#" onclick="showColor(event);"><span id="actionNameColor">颜色</span></a><img src="<%=imgPath%>/cal_list.gif" align="absmiddle">&nbsp;
	  </span>
	  <span style="padding-top:5px;padding-left:10px;display:none;">
	   <a id="showMeunD" href="#" onclick="showLight(event);"><span id="actionNameLight">效果</span></a><img src="<%=imgPath%>/cal_list.gif" align="absmiddle">&nbsp;
	  </span>
	  <span style="padding-top:5px;padding-left:10px;"><a href="#" onclick="showSubject();">预览</a></span>
	  <input type="hidden" name="actionFont" id="actionFont" value="">
	  <input type="hidden" name="actionSize" id="actionSize" value="">
	  <input type="hidden" name="actionLight" id="actionLight" value="">
	  <input type="hidden" name="actionLights" id="actionLights" value="">
	  <input type="hidden" name="actionColor" id="actionColor" value="">
	  <input type="hidden" name="actionLightFlag" id="actionLightFlag" value="">
	  <input type="hidden" name="subjectFont" id="subjectFont" value="">
	  </td>
	</tr>
    <tr>
      <td nowrap class="TableData">&nbsp;按部门发布：<br>&nbsp;<a href="javascript:;" id="href_txt" onClick="javascript:changeRange();">按人员或角色发布</a></td>
      <td class="TableData">
        <input type="hidden" id="dept" name="toId" value="<%=news.getToId()%>">
        <textarea cols=40 id="deptDesc" name="ToName" rows=2 class="BigStatic" wrap="yes" value="<%=news.getToId()%>" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectDept(['dept','deptDesc'],6);">添加</a>
       <a href="javascript:;" class="orgClear" onClick="ClearUser('toId','toName')">清空</a>
       &nbsp;&nbsp;&nbsp;&nbsp;
      </td>
    </tr>
   <tr id="rang_user" style="display:none">
      <td nowrap class="TableData">&nbsp;按人员发布：</td>
      <td class="TableData">
        <input type="hidden" id="user" name="userId" value="">
        <textarea cols=40 id="userDesc" name="userName" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectUser(['user', 'userDesc'] , 6);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="ClearUser('userId','userName')">清空</a>
      </td>
   </tr>
   <tr id="rang_role" style="display:none;">
      <td nowrap class="TableData">&nbsp;按角色发布：</td>
      <td class="TableData">
        <input type="hidden" id="role" name="privId" value="">
        <textarea cols=40 id="roleDesc" name="privName" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectRole('', 6);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="ClearUser('privId','privName')">清空</a><br>
        发布范围取部门、人员和角色的并集


      </td>
   </tr>
    <tr id="url_address" style="display:none">
      <td nowrap class="TableData"> &nbsp;超级链接地址：</td>
      <td class="TableData">
        <input type="text" id="urlAdd" name="urlAdd" size="55" maxlength="200" class="BigInput" value="">
      </td>
    </tr>
     <tr>
      <td nowrap class="TableData"> 评论：</td>
      <td class="TableData">
        <select name="anonymityYn" class="BigSelect">
          <option value="0">实名评论</option>
          <option value="1">匿名评论</option>
          <option value="2">禁止评论</option>
        </select>&nbsp;
      </td>
    </tr>
   <tr>
      <td nowrap class="TableData">附件: </td>
      <td class="TableData">
        <input type="hidden" id="attachmentId" name="attachmentId">
        <input type="hidden" id="attachmentName" name="attachmentName">
        <input type="hidden" id="ensize" name="ensize">
        <span id="showAtt">
        </span>
      </td>
    </tr>
      <tr>
      <td nowrap class="TableData">附件选择：</td>
      <td class="TableData" id="fsUploadRow">
	         <div id="fsUploadArea" class="flash" style="width:380px;">
				     <div id="fsUploadProgress"></div>
				     <div id="totalStatics" class="totalStatics"></div>
				     <div>
				       <input type="button" id="btnStart" class="SmallButtonW" value="开始上传" onclick="swfupload.startUpload();" disabled="disabled">&nbsp;&nbsp;
				       <input type="button" id="btnCancel" class="SmallButtonW" value="全部取消" onclick="swfupload.cancelQueue();" disabled="disabled">&nbsp;&nbsp;
				       <input type="button" class="SmallButtonW" value="刷新页面" onclick="upload_attach();">
				    </div>
			      </div>
			      <div id="attachment1">
		          <script>ShowAddFile();ShowAddImage();</script>
		          <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script>
		          <span id="spanButtonUpload" title="批量上传附件"> </span>
		        </div>
		        <input type="checkbox" name="download" id="download" checked><label for="download">允许下载Office附件</label>&nbsp;&nbsp;
              <input type="checkbox" name="print" id="print" checked><label for="print">允许打印Office附件</label>&nbsp;&nbsp;&nbsp;<font color="gray">都不选中则只能阅读附件内容</font>
      </td>
    </tr>
 
    <tr id="newsAuditingSingle">
    
      <td nowrap class="TableData"> &nbsp;提醒：</td>
      <td class="TableData">
           <input type="checkbox" name="tixing" id="tixing" checked><label for="download">使用内部短信提醒   </label>&nbsp;&nbsp;
      </td>
    </tr>
    
   <tr id="editor">
   <td class="TableData" colspan='2'>
    <div style="MARGIN-TOP: 5px; RIGHT: 40px; POSITION: absolute"><A title=清空邮件内容 onclick="clearEdit();" href="#">清空</A></div>
    <script language=JavaScript>    
       oFCKeditor.Config["CustomConfigurationsPath"] = "yh/core/funcs/email/js/fckconfig.js"; 
       oFCKeditor.BasePath = "/yh/core/js/cmp/fck/fckeditor/";  
       oFCKeditor.Height = "300px";
       // oFCKeditor.Width = "600px";
       oFCKeditor.SkinPath = oFCKeditor.BasePath + 'skins/silver/' ; 
       oFCKeditor.Config["PluginsPath"] = "yh/core/funcs/email/editor/plugins/";
       oFCKeditor.ToolbarSet="EmailBar";
       //  alert(loadDataDom);   
       oFCKeditor.Create();  
      </script>  
      </td>
    </tr>
    <input type="hidden" id="content" name="content"/>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="hidden" name="publish" value="">
        <input type="hidden" name="op" value="">
         <input type="button" value="发布" class="BigButton" onClick="sendForm('1');">&nbsp;&nbsp;
         <input type="button" value="保存" class="BigButton" onClick="sendForm('0');">&nbsp;&nbsp;
     </td>
  </tr>
 </table>
 <input type="hidden" id="format" name="format" value="0">
 

</form>


</body>
</html>
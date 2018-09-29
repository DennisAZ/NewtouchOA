<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page language="java" import="oa.core.funcs.bbs.act.BbsService,oa.core.funcs.bbs.act.BbsComment,oa.core.funcs.bbs.act.BBSUtil" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html><head><script type="text/javascript">
/** 常量定义 **/
var TDJSCONST = {
  YES: 1,
  NO: 0
};
/** 变量定义 **/
var contextPath = "/yh";
var imgPath = "/yh/core/styles/style1/img";
var ssoUrlGPower = "";
var limitUploadFiles = "jsp,java,jspx,exe"
var signFileServiceUrl = "http://192.168.0.5:9000/BjfaoWeb/TitleSign";
var isOnlineEval = "0";
var useSearchFunc = "1";
var maxUploadSize = 500;
var isDev = "0";
var ostheme = "1";
</script>





<title>发布新闻</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/yh/core/styles/style1/css/style.css">
<link rel="stylesheet" href="/yh/core/styles/style1/css/cmp/Calendar.css">
<link rel="stylesheet" href="/yh/core/styles/style1/css/cmp/tab.css" type="text/css">
<link rel="stylesheet" href="/yh/core/styles/style1/css/cmp/swfupload.css" type="text/css">
<script type="text/javascript" src="/yh/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="/yh/core/js/prototype.js"></script>
<script type="text/javascript" src="/yh/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="/yh/core/js/cmp/fck/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="/yh/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="/yh/core/js/datastructs.js"></script>
<script type="text/javascript" src="/yh/core/js/sys.js"></script>
<script type="text/javascript" src="/yh/core/js/smartclient.js"></script>
<script type="text/javascript" src="/yh/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="/yh/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="/yh/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="/yh/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/Javascript" src="/yh/core/funcs/notify/js/openWin.js"></script>
<script type="text/javascript" src="/yh/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="/yh/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="/yh/core/js/orgselect.js"></script>
<script type="text/javascript" src="/yh/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="js/interfaceLogic.js"></script>
<script type="text/javascript">
//seqId 为空标示新建，不为空标示修改
var seqId = "2007";
//var contentId  = '';
var oFCKeditor = new FCKeditor('Econtent');
var fckTimer = null; 
var newsInfo = null;
var toId = null;
var userId = null;
var privId = null;
var defaultType = null;
var isUploadBackFun = false;
var savePar;


var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;

function initSwfUpload() {
    var linkColor = document.linkColor;
    var settings = {
      flash_url : "/yh/core/cntrls/swfupload.swf",
      upload_url: "/yh/yh/core/funcs/news/act/YHNewsHandleAct/fileLoad.act",
      post_params: {"PHPSESSID" : "1FD157718C9398E86185F6839FC994BF.worker_1"},
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
      button_image_url: "/yh/core/styles/style1/img/uploadx4.gif",
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
    
    if(attachmentIds){
        attachMenuUtil("showAtt","news",null,attachmentName,attachmentIds,false);
    }
      
  }

function InsertImage(src){ 
  var oEditor = FCKeditorAPI.GetInstance('Econtent') ; //FCK实例 
  if ( oEditor.EditMode == FCK_EDITMODE_WYSIWYG ) {     
     oEditor.InsertHtml( "<img src='"+ src + "'/>") ; 
  } 
  } 
function resetTime(){
  var  currentDate = new Date();
  currentDate = currentDate.format('yyyy-MM-dd hh:mm:ss');
  document.newsForm.newsTime.value = currentDate;
}
function doInit(){
	  if(seqId != null && seqId !=""){
	    $("newTitle").update("修改新闻");
	  }
	  var beginParameters = {
	      inputId:'newsTime',
	      property:{isHaveTime:true}
	      ,bindToBtn:'sendTimeImg'
	        };
	  new Calendar(beginParameters);
	  if (!isTouchDevice) {
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
		}

	  var url =contextPath+'/yh/core/funcs/news/act/YHNewsHandleAct/beforeAddNews.act';
	  var json = getJsonRs(url);
	  if(json.rtState == "0"){
	       var rtData = json.rtData;
	     var typeData = rtData.typeData;  
	     optionStrs = rtData.optionStr;
	     
	       
	  //      var optionStr = rtData.optionStr;
	  //      document.getElementById("ttt").innerHTML = optionStr;
	      if(typeData.length > 0){
	         for(var i = 0 ;i < typeData.length ;i ++){
	           var optionStr = typeData[i];
	           $("typeId").options.add(new Option(optionStr.typeDesc,optionStr.typeId)); 
	         }	         
	         defaultType = typeData[0].typeId;   
	      }
	      defaultType = "";      
	  }else{
	      document.body.innerHTML = json.rtMsrg;
	  } 
	  
	  if(seqId) {//seqId 为空标示新建，不为空标示修改
	      document.getElementById("editBack").style.display = "";      
	    var url = contextPath + "/yh/core/funcs/news/act/YHNewsHandleAct/editNews.act?seqId="+seqId;
	    var json = getJsonRs(url);     
	      if(json.rtState == "0"){
	        var rtData = json.rtData;
	        bannerFone(rtData.subjectFont);
	        var userId = rtData.userId;
	        var privId = rtData.privId; 
	        var toId = rtData.toId; 
	        if(userId){
	          if(privId){  //显示用户，显示角色

	            document.getElementById("href_txt").innerText = "";
	            $("user").value = userId;
	            $("role").value = privId;
	            document.getElementById("rang_user").style.display = "";
	            document.getElementById("rang_role").style.display = "";
	          }else{      //显示用户，不显示角色
	            document.getElementById("href_txt").innerText = "按照角色发布";
	            $("user").value = userId;
	            $("role").value = "";
	            document.getElementById("rang_user").style.display = "";
	            document.getElementById("rang_role").style.display = "none";
	          }
	        }else{//    不显示用户，显示角色
	          if(privId){
	            document.getElementById("href_txt").innerText = "按照人员发布";
	            $("user").value = "";
	            $("role").value = privId;
	            document.getElementById("rang_user").style.display = "none";
	            document.getElementById("rang_role").style.display = "";
	          }else{  //不显示用户，不显示角色

	            document.getElementById("href_txt").innerText = "按人员或角色发布";
	            $("user").value = "";$("role").value = "";
	            document.getElementById("rang_user").style.display = "none";
	            document.getElementById("rang_role").style.display = "none";
	          }
	        }   
	         bindJson2Cntrl(json.rtData);
	        
	         if(toId && toId.trim() && toId!=0 && toId!='ALL_DEPT'){
	           bindDesc([{cntrlId:"dept", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
	         }else{
		         if((toId == 0 || toId == 'ALL_DEPT')&&toId !=""){			         
		           $('dept').value = 0;
		           $('deptDesc').value = "全体部门";
		         }
	         }
	         bindDesc([{cntrlId:"user", dsDef:"PERSON,SEQ_ID,USER_NAME"}
	      ,{cntrlId:"role", dsDef:"USER_PRIV,SEQ_ID,PRIV_NAME"}
	        ]);
	         
	           var download = rtData.download;
	           var format = rtData.format;
	           var formatName = "普通格式";
	           if(format=='1'){
	             formatName = "MHT格式";
	            }
	           if(format=='2'){
	             formatName = "超级链接";
	             document.getElementById("attr_tr").style.display="none";  
	             document.getElementById("fileShowId").style.display="none";   
	            }
	           document.getElementById("status").value = format;
	           document.getElementById("name").innerHTML = formatName;
	           var typeId = rtData.typeId;
	           
	           //$("typeId").selected = true;
	            if(typeId.trim()==''){
	              $("typeId")[0].text = '无类型';
	             }else{
	               $("typeId").value = typeId;
	             }
	           if(format=='1') {
	             document.getElementById("editor").style.display="none";
	             document.getElementById("add_image").style.display="none";
	           }
	           if(format == '2') {
	             document.getElementById("editor").style.display="none";
	             document.getElementById("url_address").style.display="";
	             document.getElementById("urlAdd").value= rtData.content;
	             document.getElementById("content").value = rtData.content;
	           }
	           if(download == '1') {
	             $('download').checked = 'checked';
	           }
	           var print = rtData.print;
	           if(print == '1') {
	             $('print').checked = 'checked';
	           }
	           var top = rtData.top;
	           if(top == '1') {
	             $('top').checked = 'checked';
	           }
	           var attrIds = rtData.attachmentId;
	           var attrNames = rtData.attachmentName;
	           $('attachmentName').value = attrNames;
	           $('attachmentId').value = attrIds;
	           attachMenuUtil("showAtt","news",null,$('attachmentName').value ,$('attachmentId').value,false);
//	           $('showAttachment').update(attachStr);
//	           <a id="status" href="javascript:;" class="dropdown" onclick="showMenuStatus(event);" hidefocus="true">
//	           <span><font color='#00A00'><span id="name" style="">选择格式</span></font><span style="font-family:Webdings">
//	           6</span></span></a>&nbsp;
	           if (isTouchDevice) {
	             $("contentTextarea").value = rtData.content;
		         }
	           else {
		           var FORM_MODE = FCK.EditingArea.Mode; //获取编辑区域的常量——源文件模式 
		           var editingAreaFrame = document.getElementById("Econtent___Frame"); 
		           var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;//常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js 
		           if(FORM_MODE == editModeSourceConst) { 
		           FCK.Commands.GetCommand( 'Source' ).Execute(); 
		           } 
		           FCK.EditingArea.Window.document.body.innerHTML = rtData.content;
		         }
	      }else{
	      alert(json.rtMsrg); 
	      }
	      var newtime =  document.getElementById("newsTime").value;     
	      if(newtime){       
	        newtime = newtime.substring(0, newtime.length-2);
	        var i = newtime.indexOf('.');
            if (i >= 0) {
              newtime = newtime.substring(0 , i);
            }
	        document.getElementById("newsTime").value = newtime;
	      }
	  }
	  getSysRemind();
	  initSwfUpload();
	  moblieSmsRemind('remidDiv','remind');
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
<%
HttpSession s =request.getSession();
Object o=s.getAttribute("LOGIN_USER");
String boardName=request.getParameter("boardName");
String boardId=request.getParameter("boardId");
String commentId=request.getParameter("commentId");
BbsComment bbsC=new BbsService().getUserBBSCommentDetailByCommentId(request,o,commentId);
%>
<script type="text/javascript" src="/yh/core/funcs/news/manage/js/newsAdd.js"></script>
</head>
<body onload="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3">
  <tbody><tr>
    <td><img src="/yh/core/styles/imgs/green_plus.gif"><span class="big1" id="newTitle">修改新闻</span>&nbsp;&nbsp;
      <a id="status" href="javascript:;" class="dropdown" onclick="showMenuStatus(event);" hidefocus="true"><span id="name" color="#00A00" style="">普通格式</span></a>&nbsp;
    </td>
  </tr>
</tbody></table>
<form enctype="multipart/form-data" action="/yh/yh/core/funcs/news/act/YHNewsHandleAct/addNews.act" method="post" name="newsForm" id="newsForm">
<table class="TableBlock" width="95%" align="center">
    <tbody><tr>
      <td nowrap="" class="TableData" width="15%"><select name="typeId" id="typeId" title="新闻类型可在“系统管理”-&gt;“分类码管理”模块设置。">
          <option value="">选择新闻类型</option>
        <option value="1403">商行新闻</option><option value="1405">商行文化</option><option value="1404">理财资讯</option><option value="1406">业务交流</option><option value="1407">行业资讯</option><option value="1408" selected="selected">图片新闻</option><option value="1409">财经资讯</option></select></td>
      <td class="TableData">
        <input type="text" name="subject" id="subject" size="55" maxlength="200" class="BigInput" value="请输入新闻标题..." style="color: #8896A0" onmouseover="if($F('subject')=='请输入新闻标题...') document.getElementById('subject').style.color='#000000';" onmouseout="if($F('subject')=='请输入新闻标题...') document.getElementById('subject').style.color='#8896A0';" onfocus="if($F('subject')=='请输入新闻标题...') {document.getElementById('subject').value='';document.getElementById('subject').style.color='#000000';}">
      </td>
    </tr>
	<tr id="subject_tr" style="display: none;height:50px;">
   <td nowrap="" class="TableData">预览</td>
   <td class="TableData">
     <span id="subjectMainShow"></span>
   </td>
	</tr>
	<tr>
	  <td nowrap="" class="TableData">标题样式：</td>
	  <td nowrap="" class="TableData">
	      <input type="hidden" name="FONT_FAMILY" value="">
	      <input type="hidden" name="FONT_SIZE" value="">
	      <input type="hidden" name="FONT_COLOR" value="">
	      <input type="hidden" name="FONT_FILTER" value="">
	  <span style="padding-top:5px;padding-left:10px;">
	   <a id="showMeunA" href="#" onclick="showFont(event);"><span id="actionNameFont">字体</span></a><img src="/yh/core/styles/style1/img/cal_list.gif" align="absmiddle">&nbsp;
	  </span>
	  <span style="padding-top:5px;padding-left:10px;">
	   <a id="showMeunB" href="#" onclick="showSize(event);"><span id="actionNameSize">大小</span></a><img src="/yh/core/styles/style1/img/cal_list.gif" align="absmiddle">&nbsp;
	  </span>
	  <span style="padding-top:5px;padding-left:10px;">
	   <a id="showMeunC" href="#" onclick="showColor(event);" style=""><span id="actionNameColor">颜色</span></a><img src="/yh/core/styles/style1/img/cal_list.gif" align="absmiddle">&nbsp;
	  </span>
	  <span style="padding-top:5px;padding-left:10px;display:none;">
	   <a id="showMeunD" href="#" onclick="showLight(event);"><span id="actionNameLight">效果</span></a><img src="/yh/core/styles/style1/img/cal_list.gif" align="absmiddle">&nbsp;
	  </span>
	  <span style="padding-top:5px;padding-left:10px;"><a href="#" onclick="showSubject();">预览</a></span>
	  <input type="hidden" name="actionFont" id="actionFont" value="字体">
	  <input type="hidden" name="actionSize" id="actionSize" value="">
	  <input type="hidden" name="actionLight" id="actionLight" value="">
	  <input type="hidden" name="actionLights" id="actionLights" value=")">
	  <input type="hidden" name="actionColor" id="actionColor" value="">
	  <input type="hidden" name="actionLightFlag" id="actionLightFlag" value="">
	  <input type="hidden" name="subjectFont" id="subjectFont" value="font-family:;font-size:;color:;filter:(Direction=120, color=);">
	  </td>
	</tr>
    <tr>
      <td nowrap="" class="TableData">按部门发布：<br><a href="javascript:;" id="href_txt" onclick="javascript:changeRange();">按人员或角色发布</a></td>
      <td class="TableData">
        <input type="hidden" id="dept" name="toId" value="0">
        <textarea cols="40" id="deptDesc" name="toName" rows="2" class="BigStatic" wrap="yes" readonly=""></textarea>
        <a href="javascript:;" class="orgAdd" onclick="javascript:selectDept(['dept','deptDesc'],6);">添加</a>
       <a href="javascript:;" class="orgClear" onclick="ClearUser('toId','toName')">清空</a>
       &nbsp;&nbsp;&nbsp;&nbsp;
      </td>
    </tr>
   <tr id="rang_user" style="display: none; ">
      <td nowrap="" class="TableData">按人员发布：</td>
      <td class="TableData">
        <input type="hidden" id="user" name="userId" value="">
        <textarea cols="40" id="userDesc" name="userName" rows="2" class="BigStatic" wrap="yes" readonly=""></textarea>
        <a href="javascript:;" class="orgAdd" onclick="javascript:selectUser(['user', 'userDesc'] , 6);">添加</a>
        <a href="javascript:;" class="orgClear" onclick="ClearUser('userId','userName')">清空</a>
      </td>
   </tr>
   <tr id="rang_role" style="display: none; ">
      <td nowrap="" class="TableData">按角色发布：</td>
      <td class="TableData">
        <input type="hidden" id="role" name="privId" value="">
        <textarea cols="40" id="roleDesc" name="privName" rows="2" class="BigStatic" wrap="yes" readonly=""></textarea>
        <a href="javascript:;" class="orgAdd" onclick="javascript:selectRole('', 6);">添加</a>
        <a href="javascript:;" class="orgClear" onclick="ClearUser('privId','privName')">清空</a><br>
        发布范围取部门、人员和角色的并集
      </td>
   </tr>
    <tr id="url_address" style="display:none">
      <td nowrap="" class="TableData">超级链接地址：</td>
      <td class="TableData">
        <input type="text" id="urlAdd" name="urlAdd" size="55" maxlength="200" class="BigInput" value="">
      </td>
    </tr>
    <tr id="sendTimeTr" style="">
      <td nowrap="" class="TableData">发布时间：</td>
      <td class="TableData">
        <input type="text" id="newsTime" name="newsTime" class="BigInput" size="15" maxlength="19" value="">
         <img id="sendTimeImg" src="/yh/core/styles/style1/img/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
          &nbsp;&nbsp;<a href="javascript:resetTime();">设置为当前时间</a>
      </td>
    </tr>
     <tr>
      <td nowrap="" class="TableData"> 评论：</td>
      <td class="TableData">
        <select name="anonymityYn" class="BigSelect">
          <option value="0" selected="selected">实名评论</option>
          <option value="1">匿名评论</option>
          <option value="2">禁止评论</option>
        </select>&nbsp;
      </td>
    </tr>
   <tr id="attr_tr">
      <td nowrap="" class="TableData">附件: </td>
      <td class="TableData" id="showAttachment">
        <input type="hidden" id="attachmentId" name="attachmentId" value="1210_130c06f01958f6ac929ae8113f995295,">
        <input type="hidden" id="attachmentName" name="attachmentName" value="照片 511.jpg*">
        <input type="hidden" id="ensize" name="ensize">
        <input type="hidden" id="moduel" name="moduel" value="news">
        <span id="showAtt"><div><input type="hidden" value="照片 511.jpg" id="attachLink_0_name"><input type="hidden" value="1210_130c06f01958f6ac929ae8113f995295" id="attachLink_0_id"><input type="hidden" value="news" id="attachLink_0_moudel"><img src="/yh/core/styles/style1/img/fileExt/jpg.gif" style="width:12px;height:12px" title="照片 511.jpg">&nbsp;<a id="attachLink_0" attachname="照片 511.jpg" attachid="1210_130c06f01958f6ac929ae8113f995295" moudel="news" href="#">照片 511.jpg</a></div></span>
      </td>
    </tr>
      <tr id="fileShowId">
      <td nowrap="" class="TableData">附件选择：</td>
      <td class="TableData" id="fsUploadRow">
	         <div id="fsUploadArea" class="flash" style="width:380px;">
				     <div id="fsUploadProgress"></div>
				     <div id="totalStatics" class="totalStatics"></div>
				     <div>
				       <input type="button" id="btnStart" class="SmallButtonW" value="开始上传" onclick="swfupload.startUpload();" disabled="disabled">&nbsp;&nbsp;
				       <input type="button" id="btnCancel" class="SmallButtonW" value="全部取消" onclick="swfupload.cancelQueue();" disabled="disabled">&nbsp;&nbsp;
				       <input type="button" class="SmallButtonW" value="刷新页面" onclick="upload_attach_group();">
				    </div>
			      </div>
			      <div id="attachment1">
		          <script>ShowAddFile();ShowAddImage();</script><span id="ATTACHMENT_div"></span><span id="ATTACHMENT_upload_div" style="display:none;"><a href="javascript:upload_attach();">上传附件</a></span><div id="SelFileDiv"></div><a id="linkAddAttach" class="addfile" href="javascript:;">添加附件<input class="addfile" type="file" name="ATTACHMENT_0" id="ATTACHMENT_0" size="1" hidefocus="true" onchange="AddFile();"></a>&nbsp;|&nbsp;<a href="#" onclick="sel_attach('SelFileDiv','ATTACH_DIR','ATTACH_NAME','DISK_ID');" class="selfile">从文件柜和网络硬盘选择附件</a><input type="hidden" value="" id="ATTACH_NAME" name="ATTACH_NAME"><input type="hidden" value="" id="ATTACH_DIR" name="ATTACH_DIR"><input type="hidden" id="DISK_ID" value="" name="DISK_ID">&nbsp;&nbsp;<a id="add_image" class="addimage" href="javascript:;">插入图片<input class="addfile" type="file" name="ATTACHMENT_1000" id="ATTACHMENT_1000" size="1" hidefocus="true" onchange="AddFile('image');"></a>
		          <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script>
		           <input type="hidden" name="ATTACHMENT_ID_OLD" id="ATTACHMENT_ID_OLD" value="">
                  <input type="hidden" name="ATTACHMENT_NAME_OLD" id="ATTACHMENT_NAME_OLD" value="">
                   <object id="SWFUpload_0" type="application/x-shockwave-flash" data="/yh/core/cntrls/swfupload.swf?swfuploadrnd=255799259" width="70" height="18" class="swfupload"><param name="wmode" value="transparent"><param name="movie" value="/yh/core/cntrls/swfupload.swf?swfuploadrnd=255799259"><param name="quality" value="high"><param name="menu" value="false"><param name="allowScriptAccess" value="always"><param name="flashvars" value="movieName=SWFUpload_0&amp;uploadURL=%2Fyh%2Fyh%2Fcore%2Ffuncs%2Fnews%2Fact%2FYHNewsHandleAct%2FfileLoad.act&amp;useQueryString=false&amp;requeueOnError=false&amp;httpSuccess=&amp;params=PHPSESSID%3D1FD157718C9398E86185F6839FC994BF.worker_1&amp;filePostName=Filedata&amp;fileTypes=*.*&amp;fileTypesDescription=All%20Files&amp;fileSizeLimit=500%20MB&amp;fileUploadLimit=100&amp;fileQueueLimit=0&amp;debugEnabled=false&amp;buttonImageURL=%2Fyh%2Fcore%2Fstyles%2Fstyle1%2Fimg%2Fuploadx4.gif&amp;buttonWidth=70&amp;buttonHeight=18&amp;buttonText=%3Cspan%20class%3D%22textUpload%22%3E%E6%89%B9%E9%87%8F%E4%B8%8A%E4%BC%A0%3C%2Fspan%3E&amp;buttonTextTopPadding=1&amp;buttonTextLeftPadding=18&amp;buttonTextStyle=.textUpload%7Bcolor%3A%3B%7D&amp;buttonAction=-110&amp;buttonDisabled=false&amp;buttonCursor=-2"></object>
                  
		        </div><!--
		        <input type="checkbox" name="download" id="download" checked><label for="download">允许下载Office附件</label>&nbsp;&nbsp;
              <input type="checkbox" name="print" id="print" checked><label for="print">允许打印Office附件</label>&nbsp;&nbsp;&nbsp;<font color="gray">都不选中则只能阅读附件内容</font>-->
      </td>
    </tr>
 
    <tr id="newsAuditingSingle">
    
      <td nowrap="" class="TableData"> &nbsp;提醒：</td>
      <td class="TableData">
           <span id="smsRemindDiv">
           <input type="checkbox" name="mailRemind" id="mailRemind"><label for="mailRemind">使用内部短信提醒   </label>&nbsp;&nbsp;
           </span>
           <span id="remidDiv" style="display: none; ">
           <input type="checkbox" name="remind" id="remind"><label for="remind">使用手机短信提醒   </label>&nbsp;&nbsp;
           </span>
      </td>
    </tr>
    
   <tr id="editor">
   <td class="TableData" colspan="2" id="contentTd">
    
      <input type="hidden" id="Econtent" name="Econtent" value="<%=bbsC.getContent()%>" style="display:none">
      <input type="hidden" id="Econtent___Config" value="CustomConfigurationsPath=yh%2Fcore%2Ffuncs%2Femail%2Fjs%2Ffckconfig.js&amp;PluginsPath=yh%2Fcore%2Ffuncs%2Femail%2Feditor%2Fplugins%2F" style="display:none"><iframe id="Econtent___Frame" src="/yh/core/js/cmp/fck/fckeditor/editor/fckeditor.html?InstanceName=Econtent&amp;Toolbar=EmailBar" width="100%" height="200" frameborder="0" scrolling="no" style="margin: 0px; padding: 0px; border: 0px; background-color: transparent; background-image: none; width: 100%; height: 200px; "></iframe>  
      </td>
    </tr>
    
    <input type="hidden" id="content" name="content" value="<%=bbsC.getContent()%>">
    <tr align="center" class="TableControl">
    
      <td colspan="2" nowrap="">

        <input type="hidden" name="publish" value="1">
        <input type="hidden" name="op" value="">
         <input type="hidden" id="seqId" name="seqId" value="2007">
         <input type="button" value="发布" class="BigButton" onclick="sendForm('1');">&nbsp;&nbsp;
         <input type="button" value="保存" class="BigButton" onclick="sendForm('0');">&nbsp;&nbsp;
         <span id="editBack" style="">
         <input type="button" value="返回" class="BigButton" onclick="goBack();">
         </span>
     </td>
  </tr>
 </tbody></table>
 <input type="hidden" id="format" name="format" value="0">
</form>
<form id="formFile" action="/yh/yh/core/funcs/news/act/YHNewsHandleAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;">
  </form>
<iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe>

<script type="text/javascript"><!--
/**
 * 判断有没有上传的附件
 */
function  jugeFile(){
  var formDom  = document.getElementById("formFile");
  var inputDoms  = formDom.getElementsByTagName("input"); 
  for(var i=0; i<inputDoms.length; i++){
    var idval = inputDoms[i].id;
		if(idval.indexOf("ATTACHMENT")!=-1){
			return true;
		}
  } 
  return false; 
}

function bindValidDtFuncDt() {
	bindAssertDateTimePrcBatch([{id:"newsTime", type:"dt"}]);
}

bindValidDtFuncDt();
--></script>
<iframe style="margin: 0px; padding: 0px; border: 0px; background-color: transparent; background-image: none; height: 0px; width: 0px; position: absolute; z-index: 10000; " src="javascript:void(0)" frameborder="0" scrolling="no"></iframe><iframe style="margin: 0px; padding: 0px; border: 0px; background-color: transparent; background-image: none; height: 0px; width: 0px; position: absolute; z-index: 10000; " src="javascript:void(0)" frameborder="0" scrolling="no"></iframe><iframe style="margin: 0px; padding: 0px; border: 0px; background-color: transparent; background-image: none; height: 0px; width: 0px; position: absolute; z-index: 10000; " src="javascript:void(0)" frameborder="0" scrolling="no"></iframe><iframe style="margin: 0px; padding: 0px; border: 0px; background-color: transparent; background-image: none; height: 0px; width: 0px; position: absolute; z-index: 10000; " src="javascript:void(0)" frameborder="0" scrolling="no"></iframe><iframe style="margin: 0px; padding: 0px; border: 0px; background-color: transparent; background-image: none; height: 0px; width: 0px; position: absolute; z-index: 10000; " src="javascript:void(0)" frameborder="0" scrolling="no"></iframe></body><script>window.FCKeditorAPI = {Version : "2.6.5",VersionBuild : "23959",Instances : window.FCKeditorAPI && window.FCKeditorAPI.Instances || {},GetInstance : function( name ){return this.Instances[ name ];},_FormSubmit : function(){for ( var name in FCKeditorAPI.Instances ){var oEditor = FCKeditorAPI.Instances[ name ] ;if ( oEditor.GetParentForm && oEditor.GetParentForm() == this )oEditor.UpdateLinkedField() ;}this._FCKOriginalSubmit() ;},_FunctionQueue	: window.FCKeditorAPI && window.FCKeditorAPI._FunctionQueue || {Functions : new Array(),IsRunning : false,Add : function( f ){this.Functions.push( f );if ( !this.IsRunning )this.StartNext();},StartNext : function(){var aQueue = this.Functions ;if ( aQueue.length > 0 ){this.IsRunning = true;aQueue[0].call();}else this.IsRunning = false;},Remove : function( f ){var aQueue = this.Functions;var i = 0, fFunc;while( (fFunc = aQueue[ i ]) ){if ( fFunc == f )aQueue.splice( i,1 );i++ ;}this.StartNext();}}}</script></html>
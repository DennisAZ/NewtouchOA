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





<title>新建BBS</title>
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
var seqId = "";
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
      post_params: {"PHPSESSID" : "566CE34504FC38BF988C632309E23EED.worker_1"},
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
<body>
<table border="0" width="90%" cellspacing="0" cellpadding="3">
  <tbody><tr>
    <td><img src="/yh/core/styles/imgs/green_plus.gif"><span class="big1" id="newTitle"> 编辑帖子</span>&nbsp;&nbsp;
    </td>
  </tr>
</tbody></table>
<form enctype="multipart/form-data" action="editBBSSubmit.jsp" method="post" name="bbsForm" id="bbsForm">
<input type="hidden" name="boardId" id="boardId" value="<%=request.getParameter("boardId") %>"/>
<input type="hidden" name="boardName" id="boardName" value="<%=request.getParameter("boardName") %>"/>
<input type="hidden" name="parent" id="parent" value="<%=bbsC.getParent()%>"/>
<input type="hidden" name="commentId" id="commentId" value="<%=bbsC.getCommentId()%>"/>
<table class="TableBlock" width="95%" align="center">
    <tbody>
    <tr>
      <td nowrap="" class="TableData" width="15%">
      　　标题
      　</td>
       <td class="TableData">
        <input type="text" name="subject" id="subject" size="55" maxlength="200" class="BigInput" value="<%=bbsC.getSubject()%>">
      </td>
    </tr>
	<!-- 
    <tr>
      <td nowrap="" class="TableData">按部门发布：<br><a href="javascript:;" id="href_txt" onclick="javascript:changeRange();">按人员或角色发布</a></td>
      <td class="TableData">
        <input type="hidden" id="dept" name="toId" value="">
        <textarea cols="40" id="deptDesc" name="toName" rows="2" class="BigStatic" wrap="yes" readonly=""></textarea>
        <a href="javascript:;" class="orgAdd" onclick="javascript:selectDept(['dept','deptDesc'],6);">添加</a>
       <a href="javascript:;" class="orgClear" onclick="ClearUser('toId','toName')">清空</a>
       &nbsp;&nbsp;&nbsp;&nbsp;
      </td>
    </tr>
   <tr id="rang_user" style="display:none">
      <td nowrap="" class="TableData">按人员发布：</td>
      <td class="TableData">
        <input type="hidden" id="user" name="userId" value="">
        <textarea cols="40" id="userDesc" name="userName" rows="2" class="BigStatic" wrap="yes" readonly=""></textarea>
        <a href="javascript:;" class="orgAdd" onclick="javascript:selectUser(['user', 'userDesc'] , 6);">添加</a>
        <a href="javascript:;" class="orgClear" onclick="ClearUser('userId','userName')">清空</a>
      </td>
   </tr>
   <tr id="rang_role" style="display:none;">
      <td nowrap="" class="TableData">按角色发布：</td>
      <td class="TableData">
        <input type="hidden" id="role" name="privId" value="">
        <textarea cols="40" id="roleDesc" name="privName" rows="2" class="BigStatic" wrap="yes" readonly=""></textarea>
        <a href="javascript:;" class="orgAdd" onclick="javascript:selectRole('', 6);">添加</a>
        <a href="javascript:;" class="orgClear" onclick="ClearUser('privId','privName')">清空</a><br>
        发布范围取部门、人员和角色的并集
      </td>
   </tr>
    --> 
    <tr>
      
      <td nowrap="" class="TableData">原附件：</td>
      <td class="TableData">
			  
	   <div>
		 <%String []aid=bbsC.getAttachmentIdList();
         String []ain=bbsC.getAttachmentNameList();
         
          if(aid!=null && ain!=null && aid.length>0){
           int j=aid.length;
          if(aid.length>ain.length){
          	j=ain.length;
          }
          for(int i=0;i<j;i++){
          if(ain[i]!=null && !ain[i].equals(""))
         { 
          String attEnd=BBSUtil.returnGif(ain[i]);
         %>
         <img src="/yh/core/styles/style1/img/fileExt/<%=attEnd%>.gif" style="width:12px;height:12px">
         <a href="down.jsp?attachId=<%=aid[i]%>&&attachName=<%=ain[i]%>&&attEnd=<%=attEnd%>">
           <%=ain[i]%>
         </a><input type="checkbox" name="delAttachs" value="<%=aid[i]%>">选择删除
         <br>
        <%        
        }
        }
        }
         %>
        	    			     
	   </div>
	</td>
    </tr>
      <tr>
      
      <td nowrap="" class="TableData">增加附件选择：</td>
      <td class="TableData">
			  
			      <div>
			       <span id="ATTACHMENT_div"></span><span id="ATTACHMENT_upload_div" style="display:none;"></span><div id="SelFileDiv"></div><a id="linkAddAttach" class="addfile" href="javascript:;">添加附件<input class="addfile" type="file" name="ATTACHMENT_0" id="ATTACHMENT_0" size="1" hidefocus="true" onchange="AddFile();"></a><input type="hidden" value="" id="ATTACH_NAME" name="ATTACH_NAME"><input type="hidden" value="" id="ATTACH_DIR" name="ATTACH_DIR">
			     
			   </div>
	</td>
    </tr>
    <tr id="editor">
   <td class="TableData" colspan="2" id="contentTd">
    
      <input type="hidden" id="Econtent" name="Econtent" value="<%=bbsC.getContent()%>" style="display:none">
      <input type="hidden" id="Econtent___Config" value="CustomConfigurationsPath=yh%2Fcore%2Ffuncs%2Femail%2Fjs%2Ffckconfig.js&amp;PluginsPath=yh%2Fcore%2Ffuncs%2Femail%2Feditor%2Fplugins%2F" style="display:none"><iframe id="Econtent___Frame" src="/yh/core/js/cmp/fck/fckeditor/editor/fckeditor.html?InstanceName=Econtent&amp;Toolbar=EmailBar" width="100%" height="200" frameborder="0" scrolling="no" style="margin: 0px; padding: 0px; border: 0px; background-color: transparent; background-image: none; width: 100%; height: 200px; "></iframe>  
      </td>
    </tr>
    
    <input type="hidden" id="content" name="content" value="<%=bbsC.getContent()%>">

    <tr>
      <td nowrap="" class="TableData">署名：</td>
      <td class="TableData">
      <%String anony=(request.getSession().getAttribute(BBSUtil.bbsAnonyKey)).toString(); %>
        <input type="radio" name="AUTHOR_NAME" value="1" checked onclick="set_name(1)">
        <input type="text" name="USER_NAME" size="10" maxlength="25" class="BigStatic" value="<%=BBSUtil.getSessionProperty(request,"userName")%>" readonly="">
        <%if(anony.equals("1")){ %>
        <input type="radio" name="AUTHOR_NAME" value="2" checked="" onclick="set_name(2)">昵称
        <input type="text" name="NICK_NAME" size="10" maxlength="25" class="BigInput" value="<%=BBSUtil.getSessionProperty(request,"userName")%>">
        <%} %>
      </td>
    </tr>

    <tr align="center" class="TableControl">
      <td colspan="2" nowrap="">
        <input type="hidden" name="publish" value="">
        <input type="hidden" name="op" value="">
         <input type="hidden" id="seqId" name="seqId" value="">
        <input type="hidden" id="attachsDelIds" name="attachsDelIds" value="">
         <input type="button" value="发布" class="BigButton" onclick="sendForm();">&nbsp;&nbsp;
         <input type="button" value="返回" class="BigButton" onclick="javascript:window.location.href='commentDetail.jsp?commentId=<%=bbsC.getCommentId()%>&&boardId=<%=request.getParameter("boardId")%>&&boardName=<%=request.getParameter("boardName")%>'">
     </td>
  </tr>
 </tbody></table>
   <script type="text/javascript">
    function sendForm(){
      var ches = document.getElementsByName("delAttachs");
            var j = 0;
            var str="";
            for(var i = 0;i < ches.length;i++){
                if(ches[i].checked){
                    j++;
                    str+=ches[i].value+",";
                }
            }
     document.getElementById("attachsDelIds").value=str;
     bbsForm.submit();
    }
    </script>
 <input type="hidden" id="format" name="format" value="0">
</form>
</html>
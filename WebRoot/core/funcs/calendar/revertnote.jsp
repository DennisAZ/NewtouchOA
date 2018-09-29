<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if(seqId==null){
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>回复短信 </title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
var oFCKeditor = new FCKeditor('DIARY_CONTENT');
function show_msg()
{
	var msg_height=300;
	if($('msg_frame').style.display=='block')
	{
      window.resizeBy(0, -(msg_height));
      window.moveBy(0, msg_height);
	   $('msg_frame').style.display='none'
	   return;
	}
	$('msg_frame').style.display='block';
	$("msg_frame").style.height=msg_height+"px";
	
   window.moveBy(0, -(msg_height));
	window.resizeBy(0,msg_height);
	if(window.screenTop < 30)
	   window.moveBy(0, -(window.screenTop-30));
	
	if($("msg_frame").src!="")
	   return;
	$("msg_frame").src="../sms/history_sms.php?SMS_ID=53715";
}
function CheckForm(){
   if($("user").value==""){ 
     alert("请添加收信人！");
     return (false);
   }
   var FCK = FCKeditorAPI.GetInstance('DIARY_CONTENT'); // 获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 
    // by dq 090521 
    var FORM_MODE = FCK.EditingArea.Mode; 

   // 获取编辑区域的常量——源文件模式 
    var editingAreaFrame = document.getElementById('DIARY_CONTENT___Frame'); 
    var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;// 常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js 
    if(FORM_MODE == editModeSourceConst) { 
      FCK.Commands.GetCommand( 'Source' ).Execute(); 
     } 
     // $("formName").value = formName; 

   var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
   if(FORM_HTML==""){
     alert("短信内容不能为空！");
     return (false);
   }
   $("content").value = FORM_HTML;
   return (true);
}

function sendNote(){
  if(CheckForm()){
    var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/sendNote.act";
    var rtJson = getJsonRs(requestUrl,mergeQueryString($("form1")));
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    window.close();
  }
}
function MyLoad(){
   var offset_height = 269 - document.body.offsetHeight;
   if(offset_height != 0){
      window.moveBy(0, -(offset_height));
	   window.resizeBy(0,offset_height);
	}
}
//查询
function doOnload(){
  MyLoad();
  var seqId = '<%=seqId%>';
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/selectCalendarById.act?seqId="+seqId;
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
    }
  var prc = rtJson.rtData;
  if(prc){
    var seqId = prc.seqId;
    var userId = prc.userId;
    var managerId = prc.managerId;
    var managerName = prc.managerName
    $("user").value = managerId;
    $("userDesc").innerHTML = managerName;
  }

  var FCK = FCKeditorAPI.GetInstance('DIARY_CONTENT');
  if (FCK.EditingArea) {
    if (fckTimer) {
      clearTimeout(fckTimer);
      fckTimer = null;
    }
  }else {
    fckTimer = setTimeout(doOnload, 100);
    return;
  }
  var FORM_MODE = FCK.EditingArea.Mode; //获取编辑区域的常量——源文件模式
  var editingAreaFrame = document.getElementById("DIARY_CONTENT___Frame");
  var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;//常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
  if(FORM_MODE == editModeSourceConst) {
    FCK.Commands.GetCommand( 'Source' ).Execute();
  }
  FCK.EditingArea.Window.document.body.innerHTML = "您好，已收到您的日程安排。";
}
/** 
* 处理键盘按键press事件 
*/ 
function documentKeypress(e){ 
  alert("d");
  var currKey = 0; 
  var e = e || event; 
  currKey = e.keyCode || e.which || e.charCode; 
  if(window.event.keycode==10&&CheckForm()){

    alert("3");
    }
  if(document.keydown==10){

    alert("3e");
  }
  //var flag = document.keydown
 // if (currKey == KEY_ENTER){ 
  //  alert("dd");
   // sendNote(); 
 // } 
} 

//document.onkeypress = documentKeypress;
function checkkey(keys)     //上一段中介绍的event所携带的值传给了keys
{
      if(keys.ctrlKey && keys.keyCode == 13){     //判断一下是否同时按了CTRL键和ENTER键，13是什么？就是ENTER键啊，不知道？查查ASCII表
         if(CheckForm()){
              sendNote();
          }
 
        }     //如果判断的结果确实是两个键准确无误的按下了，那么就提交数据
}

　function CheckSend(){
  if(CheckForm()){
      alert("dd");
    //sendNote(); 
    }
　}　

</script>
</head>

<body  style="margin:5px;" onload="doOnload()" onkeydown= "checkkey(event);">
<iframe id="msg_frame" name="msg_frame" src="" width="418" frameborder="0" scrolling="no" style="display:none;" align="center"></iframe>
 <form action="#"  method="post" id="form1" name="form1" onsubmit="return CheckForm();">
 <table class="TableBlock" width="390" align="center">

    <tr>
      <td class="TableData" nowrap>
         <input type="hidden" name="user" id="user" value="" />
          <textarea id="userDesc"  rows="2" cols="40" readOnly ></textarea>
        <a href="#" class="orgAdd" onClick="selectUser();">添加</a>
        <a href="#" class="orgClear" onClick="Clear('user','userDesc')">清空</a>
     <br>
           <div >
	      <script language=JavaScript>    
	       oFCKeditor.Config["CustomConfigurationsPath"] = contextPath + "/core/funcs/email/js/fckconfig.js"; 
	       oFCKeditor.BasePath = "/yh/core/js/cmp/fck/fckeditor/";  
	       oFCKeditor.Height = "150px";
	       //oFCKeditor.Width = "350px";
	       oFCKeditor.SkinPath = oFCKeditor.BasePath + 'skins/silver/' ; 
	       oFCKeditor.Config["PluginsPath"] = "yh/core/funcs/email/editor/plugins/";
	       oFCKeditor.ToolbarSet="EmailBar";
	 
	      // oFCKeditor.StartFocus = '1' ;
	      // oFCKeditor.CtrlEnter = function(){alert(1);
//	       parent.CheckSend;
	       //}
	       //  alert(loadDataDom);   
	       oFCKeditor.Create();  
	   
	      </script>  
	      <input type="hidden" id="content" name="content" value="" />
      </div>  
   </tr>
    <tr align="center" class="TableControl">
      <td nowrap><span style="float:left;">按Ctrl+回车发送</span>
      	<input type="hidden" name="I_VER" value="">
        <input type="button" value="发送" class="BigButton" onclick="sendNote();">&nbsp;&nbsp;
        <input type="hidden" value="消息记录" class="BigButton" onClick="show_msg()">&nbsp;&nbsp;
         <input type="hidden" value="<%=seqId %>" id="seqId" name="seqId">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>
</body>
</html>
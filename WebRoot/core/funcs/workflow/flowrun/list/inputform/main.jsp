<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson,yh.core.global.YHSysProps,yh.core.funcs.workflow.act.YHWorkHandleAct" %>
<%
String runId = request.getParameter("runId");
String flowId = request.getParameter("flowId");
String prcsId = request.getParameter("prcsId");
String flowPrcs = request.getParameter("flowPrcs");
String isNewStr = request.getParameter("isNew");
String isWriteLog = request.getParameter("isWriteLog");
if (isWriteLog == null || "".equals(isWriteLog)) {
  isWriteLog = "0";
} 
YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
int userId = loginUser.getSeqId();
int isNew = 0 ;
if(isNewStr != null && !"".equals(isNewStr)){
  isNew = Integer.parseInt(isNewStr);
}
String sortId = request.getParameter("sortId");
if (sortId == null) {
  sortId = "";
}
String host = request.getServerName() + ":" + request.getServerPort() + request.getContextPath() ;
String skin = request.getParameter("skin");
String skinJs = "messages";
if (skin != null && !"".equals(skin)) {
  skinJs = "messages_" + skin;
} else {
  skin = "";
}
YHWorkHandleAct act = new YHWorkHandleAct();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工作办理</title>

<link rel="stylesheet" href ="<%=cssPath %>/workflow.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
<link href="<%=cssPath %>/cmp/swfupload.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/orgselect.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/workflow/flowform/util/dateUtil.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/workflow/flowform/util/praserUtil.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/fck/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/skin.js"></script>
<script type="text/javascript" src="js/index.js"></script>
<script type="text/javascript" src="js/mouse_mon.js"></script>
<script type="text/javascript" src="js/tab.js"></script>
<script type="text/javascript" src="js/tab2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript">
var upload_limit=1,limit_type="<%=YHSysProps.getLimitUploadFiles()%>";
var oa_upload_limit="<%=YHSysProps.getLimitUploadFiles()%>";
var attachUrl = "<%=YHSysProps.getAttachPath()%>";//附件路径
var swfupload;
var isNew = <%=isNew%>;
var runId = '<%=runId%>';
var flowId = '<%=flowId%>';
var prcsId = '<%=prcsId%>';
var flowPrcs = '<%=flowPrcs%>';
var userId = '<%=userId%>';
var userName = '<%=loginUser.getUserName()%>'
var host = "<%=host%>";
var sortId = "<%=sortId%>";
var skin = '<%=skin%>';
var isWriteLog = '<%=isWriteLog %>';

function openPrintForm(){
  formView(runId , flowId) ;
}
function openWorkFlow(){
  var title = "流程图:(" + runId + ")" + runName; 
  flowView(runId , flowId , title  ,  sortId  , skin) ;
}
function deliverNextProc(){
  
}
function selectSign(event)
{
    var loc_x=(screen.availWidth-300)/2;
    var loc_y=event.clientY-100;
    window.open("feedHistory.jsp","FEED_HISTORY","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=300,height=400,left="+loc_x+",top="+loc_y);
}
function checkClose(){
  if (mouse_is_out) {
    if (isNew == 1 ) {
      cancelRun(true);
    } else {
      mouse_is_out = false;
      return '确定离开此页面吗？建议您先保存表单';
    }
  }
}
window.onbeforeunload = function() {
  return checkClose();
}
function loadAIP(seq_id) {
  var rtJson = getJsonRs( contextPath + "/yh/core/funcs/workflow/act/YHFlowPrintAct/loadAip.act", "seq_id="+seq_id);
  if (rtJson.rtState == "0") {
    var data=rtJson.rtData;
    return data.content;
  }else {
    alert(rtJson.rtMsrg);
    return false;
  }
}
function aip_upload()
{
  var obj = $("HWPostil1");     
  if(obj.IsSaved())
      return;
    try{  
        obj.HttpInit(); //初始化HTTP引擎。

        obj.HttpAddPostCurrFile("AIP_FILE");
        obj.HttpAddPostString("runId",runId);
        obj.HttpAddPostString("seqId",dispAip);
        obj.HttpAddPostString("MODULE","workflow");
        obj.HttpAddPostString("ATTACHMENT_ID",dispAipFile);
        URL = "http://<%=host %>/yh/core/funcs/workflow/act/YHFlowPrintAct/uploadAipFile.act";
        obj.HttpPost(URL);//上传数据。

    }catch(err) {
        return;
    } 
}

function docPrint()
{
    var obj = $("HWPostil1");
    obj.PrintDoc(1,1);  
}

function AIP_handWrite()
{
  var obj = $("HWPostil1");
  AIP_login();
    obj.CurrAction = 264;
}

function AIP_addSeal()
{
  var obj = $("HWPostil1");
  AIP_login();
    obj.CurrAction = 3592;
}

function AIP_login()
{
  var obj = $("HWPostil1"); 
    if(!obj.IsLogin())
    {
        var dtrer =  obj.Login("<%=loginUser.getUserName() %>",1,65535,"","");
        if(dtrer==-200){
          alert("未发现智能卡（UKey）,您无法使用手写签批功能!");
        }
        else if(0!=dtrer)
        {
          alert("登录失败！");   
        }
    }
}
function JSNotifyBeforeAction(lActionType,lType,strName,strValue){
  var obj = $("HWPostil1");
  return;
}
function setHWPostil1() {
  var obj = $("HWPostil1");
  var h = document.viewport.getDimensions().height - 80 ;
  var w = document.viewport.getDimensions().width - 10;
  obj.style.height = h + "px";
  obj.style.width = w + "px";
}
function getAipData( flag) {
  var obj = $("HWPostil1");
  if(flag)
  {
      //设置表单字段
      var el = document.workFlowForm.elements;
      var MyValue;
      for(var i=0;i<el.length;i++)
      {
          if(el[i].name.indexOf("DATA_")==0)
          {
              if(el[i].type=="checkbox")
            {
                if(el[i].checked==true)
                   MyValue="是";
                else
                   MyValue="否";
            }
              else
                  MyValue=el[i].value;
              obj.SetValue(el[i].title,"");
              obj.SetValue(el[i].title,el[i].value);
          }
      }
      
  }
  else
    alert("文档装载失败，请检查网络是否正常！");
  var NoteInfo;
  var nodes = "";
  var nodes2 = [];
  while(NoteInfo=obj.GetNextNote("sys_admin",0,NoteInfo)){
    if (NoteInfo.indexOf(".") != -1) {
      var nodeName =  NoteInfo.split(".")[1];
      if (NoteInfo.indexOf("#[") != -1) {
        nodes += nodeName + "@@";
        nodes2.push(nodeName);
      }
    }
  } 
  if (nodes) {
    var rtJson = getJsonRs(contextPath + "/yh/core/funcs/workflow/act/YHFlowPrintAct/loadAutoFlag.act", "runId=<%=runId %>&flowId=<%=flowId %>&nodes=" + encodeURIComponent(nodes));
    if (rtJson.rtState == "0") {
      var data=rtJson.rtData;
      var datas = data.split("@@");
      for (var i = 0 ;i < nodes2.length ;i++) {
        var value = "";
        if (i < datas.length) {
          value = datas[i];
        }
        obj.SetValue(nodes2[i],"");
        obj.SetValue(nodes2[i],value);
        obj.SetValue(nodes2[i], ":PROP:BORDER:0");
        obj.SetValue(nodes2[i], ":PROP:BACKCOLOR:-1");
      }
    }
  }
}
</script>
<SCRIPT LANGUAGE=javascript FOR=HWPostil1 EVENT=NotifyCtrlReady>
// 控件"HWPostil1"的NotifyCtrlReady事件，一般在这个事件中完成初始化的动作
var obj = $("HWPostil1");
obj.ShowDefMenu = false; //隐藏菜单
obj.ShowToolBar = false; //隐藏工具条

obj.TextSmooth = 1 //字体平滑 
obj.ShowScrollBarButton = 1;
obj.JSEnv = 1;  //取消进度条


var vRet;
var success;  
if (!dispAipFile) {
  success =  "0";
  var content = loadAIP(dispAip);
  vRet = obj.LoadFileBase64(content);
} else {
  success = "1";
  var URL =  "http://<%=host %>/yh/core/funcs/workflow/act/YHFlowPrintAct/getAipFile.act?module=workflow&attachmentId=" + dispAipFile;
  vRet = obj.LoadFile(URL);
}
var isSuccess = (vRet == success);

getAipData( isSuccess);
setHWPostil1();
obj.style.width="100%";
window.onresize = function() {
  setHWPostil1();
}
setTimeout("setHWPostil1()",100);
setInterval("getAipData("+isSuccess+")", 100);
</SCRIPT>
<SCRIPT LANGUAGE=javascript FOR=HWPostil1 EVENT=NotifyChangePage>
var obj = $("HWPostil1");
curPage = obj.CurrPage+1 ;
$("page").innerHTML = curPage;
</SCRIPT> 

<SCRIPT LANGUAGE=javascript FOR=HWPostil1 EVENT=NotifyDocOpened>
var obj = $("HWPostil1");
totalPage = obj.PageCount;
$("page").innerHTML = "1";
$("total").innerHTML = "/"+totalPage;
</SCRIPT>

<SCRIPT LANGUAGE=javascript FOR=HWPostil1 EVENT=JSNotifyBeforeAction(lActionType,lType,strName,strValue)>
JSNotifyBeforeAction(lActionType,lType,strName,strValue);
</SCRIPT>
</head>
<body style="background:#FFF;padding-bottom: 0px; margin: 0px; padding-left: 0px; padding-right: 0px; background: none transparent scroll repeat 0% 0%; padding-top: 0px;"  onload="doInit()">

<form style="display:none" action="<%=contextPath %>/yh/core/funcs/workflow/act/YHAttachmentAct/uploadFile.act" name="workFlowForm" target="returnPage" id="workFlowForm"  method="post" enctype="multipart/form-data">
<div id="workHandleDiv"></div>
<div id="spaceDiv" style="height:35px"></div>
<div id="handleDiv">
<div id="formDiv" style="">
<div id="noFormData" align=center style='display:none'>
<table class="MessageBox" width="300" >
    <tbody>
        <tr>
            <td class="msg info">表单内容为空！</td>
        </tr>
    </tbody>
</table>
</div>
</div>
<div id="div-extend"  style="display:none">
</div>
<div id="attachDiv" style="margin-top:20px;display:none" align=center>
<div id="hasData">
<table width="90%" cellspacing="0" cellpadding="3" bordercolor="#b8d1e2" border="1" align="center" class="TableList" style="border-collapse: collapse;">
    <tbody><tr class="TableHeader">
      <td><div style="float: left;"><img align="absmiddle" src="<%=imgPath %>/green_arrow.gif"/> 公共附件区</div></td>
    </tr>
<tr class="TableData" id="noAttachments"><td align="left">无附件</td></tr>
<tbody id="attachmentsList"></tbody>
<tbody id="attachmentTbody">
<tr height="25" id="">
      <td class="TableData" align=left>
         新建附件：
         <input type="radio" id="NEW_TYPE1" name="DOC_TYPE" onclick="$('newType').value='doc'"/><label for="NEW_TYPE1">Word文档</label>
         <input type="radio" id="NEW_TYPE2" name="DOC_TYPE" onclick="$('newType').value='xls'"/><label for="NEW_TYPE2">Excel文档</label>
         <input type="radio" id="NEW_TYPE3" name="DOC_TYPE" onclick="$('newType').value='ppt'"/><label for="NEW_TYPE3">PPT文档</label>  
         <b>附件名：</b><input type="text" value="新建文档" class="SmallInput" size="20" name="newName" id="newName" onkeyup="value=value.replace(reg,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(reg,''))"/>
         <input type="button" onclick="newAttach();" value="新建附件" class="SmallButtonW"/>
         <input type="hidden" name="newType" id="newType" value=""/>
      </td>
    </tr>

<tr>
   <td class="TableContent"   align=left>
<script>ShowAddFile();</script> 
<script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:uploadAttach1();">上传附件</a>'</script>
</td>
 </tr>
 </tbody>
</table>
</div>
<div id="noData" style='display:none'>
<table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td id="noAttachMsg" class="msg info">无公共附件,并且您无权上传附件!</td>
        </tr>
    </tbody>
</table></div>
</div>


<div id="feedBackDiv" style="margin-top:20px;margin-bottom:20px;display:none" align=center>
<table id="feedbackTable" width="90%" cellspacing="0" cellpadding="3" bordercolor="#b8d1e2" border="1" align="center" class="TableList" style="border-collapse: collapse;">
    <tbody><tr class="TableHeader">
      <td><div style="float: left;"><img align="absmiddle" src="<%=imgPath %>/green_arrow.gif"/> <span id="span1">会签意见区</span></div></td>
    </tr>
    <tbody id="feedbackList"></tbody>
    <tr class="TableContent">
      <td id="SIGN_INFO_POS" align=left>
        <b>我的意见：</b> <input type="checkbox" onclick="setSignCookie(this.checked)" id="flow_sign_flag"/>
        <label for="flow_sign_flag"><span id="span2">启用会签手写签章功能</span></label>
        <div style='color:red' id="mustFeedbackDiv">        	
    </div>
      </td>
    </tr>
    <tr><td>
        <script type="text/javascript">
        var sBasePath = contextPath+'/core/js/cmp/fck/fckeditor/';
        var oFCKeditor = new FCKeditor( 'FCKeditor1' ) ;
        oFCKeditor.BasePath    = sBasePath ;
        oFCKeditor.Height       = 150 ;
        var sSkinPath = sBasePath + 'editor/skins/silver/';
        oFCKeditor.Config['SkinPath'] = sSkinPath ;
        oFCKeditor.Config['PreloadImages'] =
                        sSkinPath + 'images/toolbar.start.gif' + ';' +
                        sSkinPath + 'images/toolbar.end.gif' + ';' +
                        sSkinPath + 'images/toolbar.buttonbg.gif' + ';' +
                        sSkinPath + 'images/toolbar.buttonarrow.gif' ;
        oFCKeditor.ToolbarSet = 'feedback';
        oFCKeditor.Value  = '' ;
        oFCKeditor.Create();
        </script>
        </td></tr>
        <tr>
   <td class="TableContent" style="display:none" align=left id="feedAttachments"></td>
 </tr>
        <tr>
   <td class="TableContent" align=left>
<script>ShowAddFile('1');</script> 
<script>$("ATTACHMENT1_upload_div" ).innerHTML='<a href="javascript:uploadFeedAttach();">上传附件</a>'</script>
</td>
 </tr>
   <tr class="TableControl">
   	<td>
        <div align="left">
        <input type="hidden" id="attachmentIds" name="attachmentIds"/>
        <input type="hidden" id="attachmentNames" name="attachmentNames"/>
        <input type="button" onclick="saveFeedback()" value="保存意见" class="SmallButtonW"/> 
        <input type="button" style="display:none" onclick="clearFeedbackContent()" value="清空" class="SmallButtonW"/> 
        <input type="button" onclick="selectSign(event)" value="快捷输入" class="SmallButtonW"/> 
        <input type="button" class="SmallButtonW3" style="display:none" id="writeButton" value="手写" onclick="WebSignHandWritePop();">&nbsp;
        <input type="button" class="SmallButtonW3" style="display:none" id="signButton" value="签章" onclick="WebSignAddSeal();"><input type="hidden" name="signData" id="signData">&nbsp;
        </div>
      </td>
    </tr>
</table>
<div id="forbidDiv" style='display:none'>
<table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td class="msg info"><span id="span3">此步骤禁止会签!</span></td>
        </tr>
    </tbody>
</table></div>
</div>

<div id="disAip" style="display:none">
<table class="TableBlock" id="HWPostil1Table" width=100% height=100% style="border-collapse: collapse;">
  <tr class="TableHeader" height=25>
    <td>
    <div style="margin-left:5px;float:left;text-align:center;><span class="Big3" style="font-size:18px;"><input type="button" onclick="AIP_handWrite()" value="手写" class="BigButton">&nbsp;&nbsp;<input type="button" onclick="AIP_addSeal()" value="盖章" class="BigButton"></span></div>
    <div style="margin-right:15px;float:right"><span class="Big3" color="red">页数<span id="page"></span><span id="total"></span></span>&nbsp;<input type="button" onclick="docPrint()" value="打印" class="BigButton"></div>
    </td>
  </tr>
    <tr class="TableHeader">
    <td id="HWPostil1Td">
    <% if (act.getAip(request , response)) {  %>
   <OBJECT id=HWPostil1 style="WIDTH:0;HEIGHT:0" classid=clsid:FF3FE7A0-0578-4FEE-A54E-FB21B277D567 codeBase='<%=contextPath %>/core/cntrls/HWPostil.cab#version=3,0,7,0' >"
    </OBJECT>
    <% } %>
    </td>
  </tr>
  </table>
</div>
<input type="hidden" id="flowId" name="flowId" value="<%=flowId %>"/>
<input type="hidden" id="runId" name="runId" value="<%=runId %>"/>
<input type="hidden" id="prcsId" name="prcsId" value="<%=prcsId %>"/>
<input type="hidden" id="flowPrcs" name="flowPrcs" value="<%=flowPrcs %>"/>
<div style="height:2px"></div></div>
</form>
<iframe id="returnPage" name="returnPage" style="display:none"></iframe>
<form  style="display:none" action="<%=contextPath %>/yh/core/funcs/workflow/act/YHAttachmentAct/uploadFile.act" name="formFile" target="returnPage" id="formFile"  method="post" enctype="multipart/form-data">
<input type="hidden"  name="flowId" value="<%=flowId %>"/>
<input type="hidden"  name="runId" value="<%=runId %>"/>
<input type="hidden" name="prcsId" value="<%=prcsId %>"/>
<input type="hidden"  name="flowPrcs" value="<%=flowPrcs %>"/>
 <input type="hidden"  name="isFeedAttach" value="false"/>
</form>

<form  style="display:none" action="<%=contextPath %>/yh/core/funcs/workflow/act/YHAttachmentAct/uploadFile.act" name="ATTACHMENT1_formFile" target="returnPage" id="ATTACHMENT1_formFile"  method="post" enctype="multipart/form-data">
<input type="hidden"  name="flowId" value="<%=flowId %>"/>
<input type="hidden"  name="runId" value="<%=runId %>"/>
<input type="hidden" name="prcsId" value="<%=prcsId %>"/>
<input type="hidden"  name="flowPrcs" value="<%=flowPrcs %>"/>
 <input type="hidden" name="isFeedAttach" value="true"/>
</form>
<%@ include file="/core/funcs/workflow/websign/ver.jsp" %>
<script>
var sign_str = "";
var sign_check={};
var sign_arr = [];
var sealForm = 1; 
function addSeal(item,seal_id) {
  var DWebSignSeal = document.getElementById("DWebSignSeal");
  try {
    if(DWebSignSeal.FindSeal(item+"_seal",2)!="") {
  	alert("您已经签章，请先删除已有签章！");
      return;
    }
    var str = SetStore(item);
    DWebSignSeal.SetPosition(0,0,"SIGN_POS_"+item);
    if (sealForm == 1 ) {
      DWebSignSeal.addSeal("", item+"_seal");
    } else {
      
      if(typeof seal_id == "undefined") {
        show_seal(item,"addSeal");
      } else {
    	  var URL = "http://"+ host +"/yh/core/funcs/workflow/act/YHFlowFormViewAct/getSeal.act?id=" + seal_id; 
    	  DWebSignSeal.AddSeal(URL, item+"_seal");
      }
    }
    DWebSignSeal.SetSealSignData(item+"_seal",str); 
    DWebSignSeal.SetMenuItem(item+"_seal",261);
  } catch (err) {
    //alert('websign控件没有加载成功！')
  }
}
function handWrite(item  , color) {
  var DWebSignSeal = document.getElementById("DWebSignSeal");
  try {
    if(DWebSignSeal.FindSeal(item+"_hw",2)!="") {
      alert("您已经签章，请先删除已有签章！");
      return;
    }
    var fontColor = 255;
    if(typeof(color) == "string" && color)
    {
      fontColor = parseInt(color);
    }
    var str=SetStore(item);
    DWebSignSeal.SetPosition(0,0,"SIGN_POS_"+item);
    DWebSignSeal.HandWrite(0,fontColor,item+"_hw");
  
    DWebSignSeal.SetSealSignData(item+"_hw",str);
    DWebSignSeal.SetMenuItem(item+"_hw",261);
  } catch (err) {
    //alert('websign控件没有加载成功！')
  }
}


function GetDataStr(item) {
  if(typeof item == 'undefined') {
    return; 
  }
  var str="";
  var separator = "::";  // 分隔符


  var TO_VAL = sign_check[item];
  if(TO_VAL!="") {
    var item_array = TO_VAL.split(",");
    for (i=0; i < item_array.length; i++) {
      var MyValue="";
      if (item_array[i] && $(item_array[i])){
        var obj =  $(item_array[i]);
        if(obj.type=="checkbox"){
          if(obj.checked==true) {
            MyValue="on";
          } else {
            MyValue="";
          }
        } else {
          MyValue=obj.value;
        }
        str += obj.name + "separator" + MyValue + "\n";
      }
    }
  }
  return str;
}

function SetStore(item)
{ 
  var DWebSignSeal = document.getElementById("DWebSignSeal");
  try {
	var str= GetDataStr(item);
    DWebSignSeal.SetSignData("-");
    DWebSignSeal.SetSignData("+DATA:" + str);
	return str;
  } catch (err) {
    //alert('websign控件没有加载成功！')
  }
}

function LoadSignData() {
  var DWebSignSeal = document.getElementById("DWebSignSeal");
  try {
    for(var i=0;i<sign_arr.length;i++) {
      if(sign_arr[i] && $(sign_arr[i])){
        DWebSignSeal.SetStoreData($(sign_arr[i]).value);
      }
    }
    DWebSignSeal.ShowWebSeals();
   
    var str= "";
    var strObjectName ;
    strObjectName = DWebSignSeal.FindSeal("",0);
    while(strObjectName  != "")
    {
      if(strObjectName.indexOf("_hw")>0)
         item_arr = strObjectName.split("_hw");
      else if(strObjectName.indexOf("_seal")>0)
         item_arr = strObjectName.split("_seal");
      if(item_arr)
      {
        str = GetDataStr(item_arr[0]);
        DWebSignSeal.SetSealSignData(strObjectName,str);
        var tmp = "4";
        if (opFlag == 1) {
          tmp = '261';
        }
        DWebSignSeal.SetMenuItem(strObjectName , tmp);
      }
      strObjectName = DWebSignSeal.FindSeal(strObjectName,0);
    }
    DWebSignSeal.SetCurrUser("<%=loginUser.getSeqId()%>[<%=loginUser.getUserName()%>]");
    //加载完成标识
    SignLoadFlag = true;
  } catch (err) {
    //alert('websign控件没有加载成功！')
  }
}

function WebSign_Submit(){ 
  var DWebSignSeal = document.getElementById("DWebSignSeal");
  try {
    var sign_val;
    for(var i=0;i<sign_arr.length;i++)
    {
      if(sign_arr[i]!="")
      {
        var oldstr="";
        var objName_hw = DWebSignSeal.FindSeal(sign_arr[i]+"_hw",2);
        var objName_seal = DWebSignSeal.FindSeal(sign_arr[i]+"_seal",2);
        //保存兼容老数据，老数据存在本次可写的第一个字段里。


        if(i==0)
        {
          var strObjectName = DWebSignSeal.FindSeal("",0);
          while(strObjectName !="")
          {
            if(strObjectName.indexOf("_hw")<0 && strObjectName.indexOf("_seal")<0 && strObjectName.indexOf("SIGN_INFO")<0)
               oldstr += strObjectName+";";
            strObjectName = DWebSignSeal.FindSeal(strObjectName,0);
          }
  
          if(objName_hw=="" && objName_seal=="" && oldstr=="")
            sign_val="";
          else
            sign_val=DWebSignSeal.GetStoreDataEx(oldstr+sign_arr[i]+"_hw"+";"+sign_arr[i]+"_seal");
        }
        else
        {
          if(objName_hw=="" && objName_seal=="")
            sign_val="";
          else
            sign_val=DWebSignSeal.GetStoreDataEx(sign_arr[i]+"_hw"+";"+sign_arr[i]+"_seal");
        }
        $(sign_arr[i]).value=sign_val ;
      }
    }
  } catch (err) {
    //alert('websign控件没有加载成功！')
  }
}
</script>
<div id="erreMsgDiv" style="display:none">
<table class="MessageBox" align="center" width="500">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt" id="erreMsg"></div>
    </td>
  </tr>
</table>
<div align="center"><input type="button" class="BigButton" value="返回" onclick="parent.location='<%=contextPath %>/core/funcs/workflow/flowrun/list/index.jsp?sortId=<%=sortId %>&skin=<%=skin %>'"></div>
</div>

</body>
</html>
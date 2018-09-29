<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.workflow.act.YHFlowSaveFileAct" %>
    <%@ include file="/core/inc/header.jsp" %>
    
    <%
    String IsWebmailOnly = YHSysProps.getProp("IsWebmailOnly");
    boolean webmailOnly = false;
    if ("1".equals(IsWebmailOnly)) {
      webmailOnly = true;
    }
    %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="noscrollX">
<head>
<title>内部邮件</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link href="<%=cssPath %>/cmp/swfupload.css" rel="stylesheet" type="text/css" />
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />  
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/censorcheck/js/censorcheckUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/flowEmaillogic.js"></script>
<% 
String reply = request.getParameter("reply") == null ? "" : request.getParameter("reply");
String emailId = request.getParameter("emailId") == null ? "" : request.getParameter("emailId");
String seqId = request.getParameter("seqId") == null ? "" : request.getParameter("seqId");
String fw = request.getParameter("FW") == null ? "" : request.getParameter("FW");
String resend = request.getParameter("resend") == null ? "" : request.getParameter("resend");
String boxId = request.getParameter("boxId") == null ? "" : request.getParameter("boxId");
String resendUserId = request.getParameter("resendUserId") == null ? "" : request.getParameter("resendUserId");
String sendEdit = request.getParameter("sendEdit") == null ? "" : request.getParameter("sendEdit");
String toId = request.getParameter("toId") == null ? "" : request.getParameter("toId");
String maxUploadFileSizeEmail =  YHSysProps.getProp("maxUploadFileSize");

String flowId= request.getParameter("flowId");
String runId= request.getParameter("runId");
String prcsId= request.getParameter("prcsId");
String flowPrcs= request.getParameter("flowPrcs");

String title=request.getParameter("title");
  if(title==null || title.equals("")){
  YHFlowSaveFileAct fs=new YHFlowSaveFileAct();
    title=fs.getRunName(request);
  }else{
  title= URLDecoder.decode(title,"UTF-8");
  }
%>
<script>
  isFull();
  var file_size_limit =  getSpare();
  var emailId = "<%=emailId%>";
  var resendUserId = "<%=resendUserId%>";
  var reply = "<%=reply%>";
  var fw = "<%=fw%>";
  var resend = "<%=resend%>";
  var sendEdit = "<%=sendEdit%>";
  var boxId = "<%=boxId%>";
  var seqId = "<%=seqId%>";
  var toId = "<%=toId%>";
  var fckTimer = null; 
  function doInit(){
 
    bindWebMail(); 
    getSysRemind("smsRemindDiv","smsRemind");
    moblieSmsRemind("sms2RemindDiv","sms2Remind");
    //start_timing_save();
    bindLikeMan("recent_linkman","toId");
    setTimeout(setLinkManMeun.bind(event,"toId"),50);
    var url = "";
    var json = null;
    if (emailId){
	    url = "<%=contextPath %>/yh/core/funcs/email/act/YHInnerEMailAct/mailDeatil.act?seqId=" + emailId + "&boxId=" + boxId;
    }else if(seqId){
	    url = "<%=contextPath %>/yh/core/funcs/email/act/YHInnerEMailAct/mailDeatil.act?seqId=" + seqId + "&boxId=" + boxId;
    }else {
      if(toId.trim()){
        document.getElementById("toId").value = toId;
        bindDesc([{cntrlId:"toId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]); 
      }
      bindImportant("importCont",0);
      return;
    }
    json = getJsonRs(url);
    if (json.rtState == "0"){
     
      if (reply) {
        if (json.rtData.data.isWebmail) {
          var wtomail = json.rtData.wtomail;
          json.rtData.data.toWebmail = wtomail;
          
          if (reply == '1') {
            var wccmail = json.rtData.wccmail;
            var frommail = $('fromWebmail').value;
            json.rtData.data.toWebmailCopy = wccmail;
          }
        }
      }
	  if (reply && reply == "2"){
           bindReply(json,'Econtent',url);
	  } else if (reply && reply == "1"){
	       //回复所有
	     bindReplyAll(json,'Econtent',url);
	    if(json.rtData.copyToId){
		    showTr('tr_cc');//显示抄送
	     }
	  } else if(fw && fw == "1"){
	     bindFw(json,'Econtent',url);
	  } else if(resend == "1" || sendEdit == "1" ){
		 if(json.rtData.data.copyToId && sendEdit != "1"){
		     showTr('tr_cc');//显示抄送
		  }
  	     if(json.rtData.data.secretToId && sendEdit != "1"){
  		   showTr('tr_bcc');//显示密送
          }
	      bindCg(json,'Econtent',url,resendUserId);
	  }else if(resend == "2"){
	    bindCg(json,'Econtent',url,resendUserId);
      }
      if (reply  && json.rtData.data.isWebmail) {
        if (json.rtData.data.toWebmail) {
          showTr('tr_webmail');
        }
        if (json.rtData.data.toWebmailCopy) {
          showTr('tr_webmail_cc');
        }
        showTr('tr_webmaildesc');
      } else {
        if(json.rtData.data.toWebmail){
         showTr('tr_webmail');
         showTr('tr_webmaildesc');
         }
           if(json.rtData.data.toWebmailCopy){
           showTr('tr_webmail_cc');
           showTr('tr_webmaildesc');
           }
           if(json.rtData.data.toWebmailSecret){
           showTr('tr_webmail_bcc');
           showTr('tr_webmaildesc');
           }
      }
      
    } else{
      alert(json.rtMsrg);
    }
  }
  
    
  var upload_limit=1,limit_type=limitUploadFiles;
  var oa_upload_limit=limitUploadFiles;
  var file_size_limit_cf = "<%=maxUploadFileSizeEmail%>";
  var swfupload;
var webmailOnly = <%=webmailOnly %>;

  function upload_attach()
  {
    saveMaiByUp();
  }
  function deleteAttachBackHand(attachName,attachId,attrchIndex){
    var attachNameOld = $('attachmentName').value;
    var attachIdOld =  $('attachmentId').value;
    var attachNameArrays = attachNameOld.split("*");
    var attachIdArrays = attachIdOld.split(",");
    var attaName = "";
    var attaId = "";
    for(var i = 0 ; i < attachNameArrays.length ; i++){
      if(!attachIdArrays[i] || attachIdArrays[i] == attachId){
        continue;
      }
      attaName += attachNameArrays[i] + "*";
      attaId += attachIdArrays[i] + ",";
    }
    $('attachmentId').value = attaId;
    $('attachmentName').value = attaName;
    try{
      saveMaiByUp();
      return true;
    }catch(ex){
     return false;
    }
  }
  function start_timing_save() {
    setInterval('timing_save()', 180000); //××毫秒自动保存到草稿箱 by dq 090605
  }
  function deletByShow(id){
    var url = contextPath + "/core/funcs/email/sendbox/index.jsp";
    deleteMail(2,url,id,true);
  }
 
  var isNosave = 1;
   function checkLeave(){
     event.returnValue="确定离开当前页面吗？";
   }
   function clearnOnBor(){
     window.onbeforeunload = "";
   }
   function FCKeditor_OnComplete( editorInstance ) {
     if (!seqId && !emailId) {
       var url = contextPath + "/yh/core/funcs/email/act/YHEmailNameAct/getName2.act";
       var json = getJsonRs(url);
       if (json.rtState == '0') {
         var nameStr = json.rtData.name;
         var isUse = json.rtData.isUse;
         if (isUse == '1') {
           editorInstance.SetData("<br />"
           + "<br />"
           + nameStr) ;
         } 
       }
     }
   }

   var ctrlConst = "toWebmail";
   function selectAddress(ctrl){
     window.onbeforeunload = null;
     if (ctrl) {
       ctrlConst = ctrl;
     } else {
       ctrlConst = "toWebmail";
     }
     var url = contextPath + "/core/funcs/email/new/emailselect/MultiEmailSelect.jsp";
     openDialogResize(url,  710 , 400);
   }

   function addUnloadEvt() {
     setTimeout(function() {
       window.onbeforeunload = checkLeave;
     }, 100);
   }
   function setIndex(index , ctrl) {
      if ($(ctrl).style.display == '') {
        eval(index + " = '1'");
      } else {
        eval(index + " = ''"); 
      }
   }
   var userRetNameArray = null;
   function selectEmailUser(retArray , moduleId,privNoFlag , notLoginIn) {
     userRetNameArray = retArray;
     var url = contextPath + "/core/funcs/email/new/emailselect/MultiEmailSelect2.jsp?1=1" ;
     if (moduleId) {
       url += "&moduleId=" + moduleId;
       if (!privNoFlag) {
         privNoFlag = 0;
       }
       url += "&privNoFlag=" + privNoFlag;
     }
     if (notLoginIn) {
       url += "&notLoginIn=" + notLoginIn;
     }
     openDialogResize(url,  580 + 180, 400);
   }
   
   var runId = '<%=runId%>';
   var flowId = '<%=flowId%>';
   var prcsId = '<%=prcsId%>';
   var flowPrcs = '<%=flowPrcs%>';
   function getAttachments(){
		  var param = 'runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId +"&flowPrcs=" + flowPrcs;
		  var url = contextPath + "/yh/core/funcs/workflow/act/YHAttachmentAct/getAttachments.act";
		  var json = getJsonRs(url , param);
		  var attachmentName="";
		  var attachmentId="";
		  if(json.rtState == '0'){
			    var attachment = json.rtData;
			    if(attachment.length > 0){
			    	var str="";
			      for(var i = 0 ;i < attachment.length ;i ++){
			        var attach = attachment[i];
			        attachmentName+=attach.attachmentName+"*";
			        attachmentId+=attach.attachmentId+",";
			        str+=addAttachment(attach);
			      }
			      $("showAtt").innerHTML=str;
			      $("attachmentId").value=attachmentId;
			      $("attachmentName").value=attachmentName;
			    }else{
			    	$("showAtt").innerHTML="无附件";
			    }
			  }
	}
	function getAttachImage(ext) {
		  var imgSrc = "";
		  if(ext == 'gif' || ext == 'jpg' || ext == 'bmp' || ext == 'png'){
		    imgSrc = imgPath + "/gif.gif";
		  }else if(ext == 'doc' || ext == 'docx'){
		    imgSrc = imgPath + "/doc.gif";
		  }else if(ext == 'xlsx' || ext == 'xls'){
		    imgSrc = imgPath + "/xls.gif";
		  }else if(ext == 'ppt' || ext == 'pptx'){
		    imgSrc = imgPath + "/ppt.gif";
		  }else if(ext == 'exe'){
		    imgSrc = imgPath + "/exe.gif";
		  }else if(ext == 'chm'){
		    imgSrc = imgPath + "/chm.gif";
		  }else if(ext== 'txt'){
		    imgSrc = imgPath + "/txt.gif";
		  }else if (ext == 'rar' || ext == 'zip') {
		    imgSrc = imgPath + "/rar.gif";
		  } else{
		    imgSrc = imgPath + "/defaut.gif";
		  }
		  return imgSrc;
		}
	function downAction(id,name){
		 downLoadFile(name ,  id, "workflow");
		}
	function addAttachment(attachment){
		  var ext = attachment.ext;
		  var imgStr = getAttachImage(ext);
		  var str = "<a href='javascript:void(0)' onclick='downAction( \""+ attachment.attachmentId +"\", \""+ attachment.attachmentName +"\")'><img src='" + imgStr + "'/>&nbsp;" + attachment.attachmentName + "</a><br/>";
		  return str; 
		}
	function doMyInit(){
		getAttachments();
		doInit();
	}
		
</script>
</head>
<body onload="doMyInit();" onbeforeunload="checkLeave()";>
<form name="form1" id="form1" action="<%=contextPath %>/yh/core/funcs/email/act/YHInnerEMailAct/sendMailFromFlow.act" method="post" >
<table class="TableTop" width="100%">
  <tr>
    <td class="left">
    </td>
    <td class="center">
    		工作流转发邮件
    </td>
    <td class="right">
    </td>
  </tr>
</table>
<input type="hidden" id="seqId" name="seqId" value="<%=seqId%>">
<table class="TableBlock no-top-border" width="100%" align="center">
   <tr>
      <td nowrap class="TableData"> 邮件主题：</td>
      <td class="TableData">
        <input type="text" name="subject" id="subject" style="width:300px;" maxlength="100" class="SmallInput" value="<%=title %>" onKeyPress="if(event.keyCode==13) return false;">
        <input id="important" type="hidden" name="important"></input>
        <a href="javascript:void(0)" id="importCont"></a>
      </td>
    </tr>
    <tr id="emailTr" style="display:<% if (webmailOnly) {%>none<%}%>">
      <td nowrap class="TableData">收件人：</td>
      <td class="TableData" nowrap>
        <input type="hidden" name="toId" id="toId" value="">
        <textarea style="width:70%;" id="toIdDesc" rows="2" style="overflow-y:auto;" class="SmallStatic" wrap="yes" readonly></textarea>
        <a href="javascript:void(0)" class="orgAdd" onClick="selectUser(['toId', 'toIdDesc'],11);return false;" title="添加收件人">添加</a>
        <a href="javascript:void(0)" class="orgClear" onClick="ClearUser('toId', 'toIdDesc');return false;" title="清空收件人">清空</a><br>
        <div style="height:20px;padding:5px 0px 2px 0px;">
          <a href="javascript:void(0)" onClick="showTr('tr_cc',this);setIndex('tr_cc_index' ,'tr_cc');return false;">添加抄送</a> 
          - <a href="javascript:void(0)" onClick="showTr('tr_bcc',this);setIndex('tr_bcc_index' ,'tr_bcc');return false;">添加密送</a>
          <span id="webmailSpan">
           - <a href="javascript:void(0)" onClick="showTr('tr_webmail',this);setIndex('tr_w_index' ,'tr_webmail');return false;">外部收件人</a>
             - <a href="javascript:void(0)" onClick="showTr('tr_webmail_cc',this);setIndex('tr_wcc_index' ,'tr_webmail_cc');return false;">外部抄送</a>
               - <a href="javascript:void(0)" onClick="showTr('tr_webmail_bcc',this);setIndex('tr_wbcc_index' ,'tr_webmail_bcc');return false;">外部密送</a>
            </span>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <% if (!webmailOnly) {%><a href="javascript:void(0)" onClick="return false;" id="recent_linkman" >最近联系人</a><%}%>
        </div>
      </td>
    </tr>
    <tr id="tr_cc" style="display:none;">
      <td nowrap class="TableData">抄送：</td>
      <td class="TableData" nowrap>
        <input type="hidden" name="copyToId" id="copyToId" value="">
        <textarea style="width:70%;" id="copyToIdDesc" rows="1" style="overflow-y:auto;" class="SmallStatic" wrap="yes" readonly></textarea>
        <a href="javascript:void(0)" class="orgAdd" onClick="selectUser(['copyToId', 'copyToIdDesc'],11);return false;" title="添加抄送人">添加</a>
        <a href="javascript:void(0)" class="orgClear" onClick="ClearUser('copyToId', 'copyToIdDesc');return false;" title="添加抄送人">清空</a>
      </td>
    </tr>
    <tr id="tr_bcc" style="display:none;">
      <td nowrap class="TableData">密送：</td>
      <td class="TableData" nowrap>
        <input type="hidden" name="secretToId" id="secretToId" value="">
        <textarea style="width:70%;" id="secretToIdDesc" rows="1" style="overflow-y:auto;" class="SmallStatic" wrap="yes" readonly></textarea>
        <a href="javascript:void(0)" class="orgAdd" onClick="selectUser(['secretToId', 'secretToIdDesc'],11);return false;" title="添加密送人">添加</a>
        <a href="javascript:void(0)" class="orgClear" onClick="ClearUser('secretToId', 'secretToIdDesc');return false;" title="添加密送人">清空</a>
              </td>
    </tr>
    <tr id="tr_webmail" style="display:none;">
      <td nowrap class="TableData" nowrap><% if (!webmailOnly) {%>外部<%}%>收件人：</td>
      <td class="TableData">
        <textarea style="width:70%;" rows="2" name="toWebmail" id="toWebmail" rows="1" style="overflow-y:auto;"  wrap="yes" ></textarea>
        <a href="javascript:void(0)" class="orgAdd" onClick="selectAddress('toWebmail');return false;" title="从通讯簿添加收信人">添加</a>
        <a href="javascript:void(0)" class="orgClear" onClick="$('toWebmail').value='';return false;" title="添加外部收件人">清空</a><br />
                 &nbsp;&nbsp;多个<% if (!webmailOnly) {%>外部<%}%>收件人请使用","分隔
      </td>
    </tr>
    <tr id="tr_webmail_cc" style="display:none;">
      <td nowrap class="TableData" nowrap ><% if (!webmailOnly) {%>外部<%}%>抄送人：</td>
      <td class="TableData">
        <textarea style="width:70%;" name="toWebmailCopy" id="toWebmailCopy" rows="2" style="overflow-y:auto;"  wrap="yes" ></textarea>
        <a href="javascript:void(0)" class="orgAdd" onClick="selectAddress('toWebmailCopy');return false;" title="从通讯簿添加收信人">添加</a>
        <a href="javascript:void(0)" class="orgClear" onClick="$('toWebmailCopy').innerHTML='';return false;" title="添加外部收件人">清空</a><br />
                 &nbsp;&nbsp;多个<% if (!webmailOnly) {%>外部<%}%>抄送人请使用","分隔
      </td>
    </tr>
     <tr id="tr_webmail_bcc" style="display:none;">
      <td nowrap class="TableData" nowrap ><% if (!webmailOnly) {%>外部<%}%>密送人：</td>
      <td class="TableData">
        <textarea style="width:70%;" name="toWebmailSecret" id="toWebmailSecret" rows="1" style="overflow-y:auto;"  wrap="yes" ></textarea>
        <!-- <a href="javascript:void(0)" class="orgAdd" onClick="alert('完善中')" title="添加外部收件人">添加</a> -->
        <a href="javascript:void(0)" class="orgAdd" onClick="selectAddress('toWebmailSecret');return false;" title="从通讯簿添加收信人">添加</a>
        <a href="javascript:void(0)"   class="orgClear" onClick="$('toWebmailSecret').innerHTML='';return false;" title="添加外部收件人">清空</a><br />
                 &nbsp;&nbsp;多个<% if (!webmailOnly) {%>外部<%}%>密送人请使用","分隔
      
      </td>
    </tr>
    <tr id="tr_webmaildesc" style="display:none;">
      <td nowrap class="TableData">Internet邮箱：</td>
      <td class="TableData">
        <select name="fromWebmailId" id="fromWebmail" class="BigSelect">
            <option value=""></option>
        </select> <span id="webmailInfo"></span>
        <span style="display:<% if (!webmailOnly) {%>none<%}%>">
        <a href="javascript:void(0)" onClick="showTr('tr_webmail_cc',this);setIndex('tr_wcc_index' ,'tr_webmail_cc');return false;">抄送</a>
               - <a href="javascript:void(0)" onClick="showTr('tr_webmail_bcc',this);setIndex('tr_wbcc_index' ,'tr_webmail_bcc');return false;">密送</a>
        <% if (webmailOnly) {%> <a href="javascript:void(0)" onClick="return false;" id="recent_linkman" >最近联系人</a><%} %>
        </span>
      </td>
    </tr>
 
  
    <tr>
      <td nowrap class="TableData">邮件附件：</td>
      <td class="TableData" id="fsUploadRow">
	        <span id="showAtt"></span>
      </td>
    </tr>
    <tr >
      <td nowrap class="TableData">提醒：</td>
      <td class="TableData">
      <div id="smsRemindDiv" style="float:left;">      
      <INPUT id="smsRemind" type=checkbox  name="smsRemind" value="1"><LABEL for=smsRemind>使用内部短信提醒</LABEL>&nbsp;&nbsp;
      </div>
      <div id="sms2RemindDiv" style="float:left;">   
     <input type="checkbox" name="sms2Remind" id="sms2Remind" value="1"><label for="sms2Remind">使用手机短信提醒</label>
      </div>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">收条：</td>
      <td class="TableData">
         <input type="checkbox" name="receipt" id="receipt" value="1"><label for="receipt">请求阅读收条 (收件人第一次阅读邮件时，短信提醒发件人)</label>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
       <input type="hidden" id="moduel" name="moduel" value="email">
       <input id="content" type="hidden" name="content"></input>
        <input id="boxId" name="boxId" type="hidden"></input>
         <input id="sendEdit" name="sendEdit" type="hidden"></input>
        <input id="sendFlag" name="sendFlag" type="hidden"></input>
          <input type="hidden" id="attachmentId" name="attachmentId" value="">
  <input type="hidden" id="attachmentName" name="attachmentName" value="">
    <input type="hidden" id="runId" name="runId" value="<%=runId%>">
  <input type="hidden" id="flowId" name="flowId" value="<%=flowId%>">
        <input id="btn_submit" type="button" value="立即发送" class="BigButton" title="立即发送此邮件" onClick="sendMail();"  >&nbsp;
        
      
       </td>
    </tr>
  </table>
</form>
  <div id="msgArea" style="width:280px;height:150px;z-index: 10; left:10; top:200; border: 1px solid #000000;text-align:center;display:none" class="TableContent">
  <br><img src="<%=imgPath%>/loading.gif" align="absMiddle"> <h3>正在发送，请稍候……</h3>
</div>
<!-- 隐藏帧，用于邮件定时自动保存 -->
<iframe name="hiddenFrame" id="hiddenFrame" width=0 height=0 frameborder=0 scrolling=no></iframe>
<iframe style="visibility: hidden;" width="0" height="0" name="commintFrame" id="commintFrame"></iframe>
</body>
</html>
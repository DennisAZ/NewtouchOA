<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="yh.core.funcs.doc.receive.data.YHDocConst"%>
<html>
<%
  String rec_seqId =  YHUtility.null2Empty(request.getParameter("rec_seqId"));
  String webroot = request.getRealPath("/");
  String seqId = YHUtility.null2Empty(request.getParameter("seqId"));
%>
<head>
<title>收文登记</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css"/>
<link rel="stylesheet" href ="<%=cssPath %>/style.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/doc/receive/js/MultiUserSelect.js" ></script>
<script type="text/Javascript">
var upload_limit=1,limit_type=limitUploadFiles;
var isUploadBackFun = false;
var rec_seqId = "<%=rec_seqId %>";
var reName = "";
var reId = "";
var seqId = "<%=seqId %>";
var secrtGrade = "<%=YHDocConst.getProp(webroot , YHDocConst.SECRET_GRADE)%>";
var docType = "<%=YHDocConst.getProp(webroot , YHDocConst.DOC_TYPE)%>";
/**
 * 获得最大的收文编号
 * @return
 */
function getMaxOrderNo(typeId){
  var url = contextPath +"/yh/core/funcs/doc/receive/act/YHDocRegisterAct/getMaxOrderNo.act" ;
  var rtJson = getJsonRs(url , "type=" + typeId); 
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  $("recNo").value = (rtJson.rtData + 1);
}
function doInit(){
  if (rec_seqId) {
    var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocRegisterAct/getRecReg.act?rec_seqId="+rec_seqId;
    var json = getJsonRs(url); 
    if(json.rtState == "0"){
      var data = json.rtData;
      if (data.isCancel == '0') {
        $('fromDeptId').value = data.fromDeptId;
        $('fromDeptName').value = data.fromDeptName;
        $('title').value = data.title;
        $('sendDocNo').value = data.recDoc;
        $('recId').value = rec_seqId;

        $('recDocName').value = data.recDocName;
        $('recDocId').value = data.recDocId;
        if (data.recDocId) {
          attachMenuUtil("showRecDoc","doc",null,data.recDocName,data.recDocId,true,"doc","","",false);
        }
        $('attachmentName').value = data.attachmentName;
        $('attachmentId').value = data.attachmentId;
        if (data.attachmentId) {
          attachMenuUtil("showAtt","doc",null,$('attachmentName').value ,$('attachmentId').value,true,"att","","",true);
        }
        $('doc').show();
      } else {
        $('noshow').show();
        $('show').hide();
      }
    }
  }
  if (seqId) {
    var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocRegisterAct/getRecRegBySeqId.act?seqId="+seqId;
    var json = getJsonRs(url); 
    if(json.rtState == "0"){
      var data = json.rtData;
      $('fromDeptName').value = data.fromDeptName;
      $('title').value = data.title;
      $('sendDocNo').value = data.sendDocNo;
      $('recDocName').value = data.recDocName;
      $('recDocId').value = data.recDocId;
      if (data.recDocId) {
        attachMenuUtil("showRecDoc","doc",null,data.recDocName,data.recDocId,true,"doc","","",true);
      }
      $('doc').show();
      parseObj(secrtGrade, "secretsLevel" , data.secretsLevel);
      parseObj(docType, "recType", data.recType);
      $('recNo').value = data.recNo;
      $('copies').value = data.copies;
      $('attachmentName').value = data.attachmentName;
      $('attachmentId').value = data.attachmentId;
      if (data.attachmentId) {
        attachMenuUtil("showAtt","doc",null,$('attachmentName').value ,$('attachmentId').value,false,"att","","",true);
      }
    }
  } else {
    parseObj(secrtGrade, "secretsLevel");
    parseObj(docType, "recType");
    this.getMaxOrderNo($("recType").value);
  }
}
function parseObj(docType, id , value){
  if(docType){
    var tt = docType.split(",");
    var sel = document.getElementById(id);
    if(tt && sel){
      for(var i=0; i<tt.length; i++ ){  
        var op = new Option(tt[i], tt[i]);
        if (tt[i] == value) {
          op.selected = true;
        }       
        sel.options.add(op);
      }
    }
  }
}
function upload_attach() {
  $('formFile').submit();
}
var isUploadBackFun = false;
var flagConst = "1";
function handleSingleUpload(returnState, rtMsrg, rtData) {
  var data = rtData.evalJSON(); 
  $('attachmentId').value +=  data.attrId;
  $('attachmentName').value +=  data.attrName; 
  attachMenuUtil("showAtt","doc",null,$('attachmentName').value ,$('attachmentId').value,false,"att","","",true);
  removeAllFile();
  if(isUploadBackFun==true){
   // $("form1").submit();       //如果文件上传上去了，则保存
    register(flagConst);
    isUploadBackFun = false;
  }
}
function deleteAttachBackHand(attachName,attachId,attrchIndex) {
   var ids = $('attachmentId').value ;
   if (!ids) {
     ids = ""; 
   }
   var names = $('attachmentName').value;
   if (!names) {
     names = ""; 
   }
   var idss = ids.split(",");
   var namess = names.split("*");
   
   var newId = getStr(idss , attachId , ",");
   var newname = getStr(namess , attachName , "*");  
   
   $('attachmentId').value = newId;
   $('attachmentName').value = newname;
   return true;
}
function register(flag) {
  if (!isInteger($("copies").value)) {
    alert("份数必须是正整数!");
    return false;
  }
  if (!isInteger($("recNo").value)) {
    alert("收文文号必须是正整数!");
    return false;
  }
  if (!$('sendDocNo').value) {
    alert("对方文号不能为空!");
    return false;
  }
  
  var attach = $('ATTACHMENT_div').innerHTML;
  if (attach.trim()) {
    isUploadBackFun = true;
    flagConst = flag;
    upload_attach();
    return null;
  }
  if (flag == '1' && rec_seqId ) {
    var name= $('recDocName').value;
    var id  = $('recDocId').value
    if (id && name) {
      var param = encodeURI("attachmentName=" +name  + "&attachmentId=" +id  + "&module=doc");
      var docUrl = contextPath + "/yh/core/funcs/office/ntko/act/YHNtkoAct/upload.act?" + param;
      re = /&amp;/g;
      TANGER_OCX_OBJ = $('TANGER_OCX');
      docUrl = docUrl.replace(re, "&");
      var useUTF8 = (document.charset == "utf-8");
      TANGER_OCX_OBJ.IsUseUTF8Data = useUTF8;
      TANGER_OCX_OBJ.FileNew = true;
      TANGER_OCX_OBJ.FileClose = false;
      TANGER_OCX_OBJ.OpenFromURL(docUrl);
      TANGER_OCX_OBJ.ActiveDocument.AcceptAllRevisions();
      var TANGER_OCX_actionURL = contextPath + "/yh/core/funcs/doc/act/YHWorkTurnAct/updateFile.act";
      var retStr = TANGER_OCX_OBJ.SaveToURL(TANGER_OCX_actionURL, "DOC_NAME", "docName=" + name + "&module=doc");
      $('recDocId').value = retStr;
      $('recDocName').value = name;
    }
  }
  var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocRegisterAct/register.act?flag=" + flag;
  var json = getJsonRs(url , $('form1').serialize()); 
  if(json.rtState == "0"){
    if (flag == '2') {
      alert("修改成功！");
      window.opener.pageMgr.search();
      window.close();
    }else {
      alert("登记成功！");
      window.opener.pageMgr.search();
      window.close();
    }
  }
}
function getStr(ids , id , split) {
  var str = "";
  for (var i = 0 ; i< ids.length ;i ++){
    var tmp = ids[i];
    if (tmp) {
      if (tmp != id) {
        str += tmp + split;
      }
    }
  }
  return str;
}
</script>
</head>
<body onload="doInit();">
<% if (!YHUtility.isNullorEmpty(rec_seqId)) {%>
<object  id="TANGER_OCX" classid="<%=StaticData.Classid%>" codebase="<%=contextPath %>/core/cntrls/<%=StaticData.Codebase%>" width="0" height="0">
<param name="IsNoCopy" value="0">
<param name="FileSave" value="1">
<param name="FileSaveAs" value="1">
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
<%  } %>
<div id="show">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif"/><span class="big3">收文登记</span>
    </td>
  </tr>
</table>
<form  id="form1" method="post" name="form1" >
<input type="hidden" value="<%=seqId %>" id="seqId" name="seqId"/>
<table class="TableBlock" width="70%" align="center">
  <tr>
    <td nowrap class="TableContent" width="120">收文类型：<font style="color:red">*</font>&nbsp;</td>
    <td nowrap class="TableData">
       <select name="recType" id="recType" onchange="getMaxOrderNo(this.value)">
       </select>
    </td>
   </tr>
   <tr>
	    <td nowrap class="TableContent">收文文号：<font style="color:red">*</font> &nbsp;</td>
	    <td nowrap class="TableData">
	       <input type="text" name="recNo"  id="recNo" class="BigInput" size="33" maxlength="100" value=""/>&nbsp;
	    </td>
   </tr>
   <tr>
	    <td nowrap class="TableContent" width="120">来文单位：<font style="color:red">*</font>&nbsp;</td>
	    <td nowrap class="TableData">
	        <input type="text" name="fromDeptName" id="fromDeptName" class="BigInput" size="33" maxlength="100" value=""/>&nbsp;
           <input type="hidden" name="fromDeptId" id="fromDeptId"  value=""/>&nbsp;
	    </td>
   </tr>
   <tr>
    <td nowrap class="TableContent">密级：<font style="color:red">*</font>&nbsp;</td>
    <td nowrap class="TableData">
        <select name="secretsLevel" id="secretsLevel"  class="BigSelect">       
        </select>
    </td>
   </tr>
    <tr>
    <td nowrap class="TableContent" width="120">对方文号：<font style="color:red">*</font>&nbsp;</td>
    <td nowrap class="TableData">
        <input type="text" name="sendDocNo" id="sendDocNo" />&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableContent" width="120">标题：<font style="color:red">*</font>&nbsp;</td>
    <td nowrap class="TableData">
        <input type="text" name="title" id="title" />&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableContent" width="120">份数：<font style="color:red">*</font>&nbsp;</td>
    <td nowrap class="TableData">
        <input type="text" name="copies" id ="copies" class="BigInput" size="33" maxlength="100" value="1"/>&nbsp;
    </td>
   </tr>
   <tr style="display:none" id="doc">
      <td nowrap class="TableContent">来文正文: </td>
      <td class="TableData">
        <span id="showRecDoc">
        </span>
        <input type="hidden" id="recDocId" name="recDocId">
        <input type="hidden" id="recDocName" name="recDocName">
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
			      <div id="attachment1">
		          <script>ShowAddFile();</script>
		          <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传</a>'</script>
		        </div></td>
    </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <% if(YHUtility.isNullorEmpty(seqId)) {%>
         <input type="button"  value="登记" class="BigButton"  name="button" onclick="register('1');"/>&nbsp;
        <% } else {%>
          <input type="button"  value="修改" class="BigButton"  name="button" onclick="register('2');"/>&nbsp;
        <% } %>
        <input type="button" value="关闭" class="BigButton"  name="button" onclick="window.close()"/>
        <input type="hidden" id="recId" name="recId"/>
   </td>
   </tr>
</table>
</form>
<form id="formFile" action="<%=contextPath %>/yh/core/funcs/doc/receive/act/YHDocRegisterAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
</form>
<iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe>
</div>
<div id="noshow" align="center" style="display:none">
<table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td id="outMsgInfo" class="msg info">
            公文已被收回不能登记！<input type="button" value="关闭" class="BigButton"  name="button" onclick="window.close()"/>
            </td>
        </tr>
    </tbody>
</table>
</div>

</body>
</html>
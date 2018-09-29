<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String seqId = request.getParameter("seqId") == null ? "0" :  request.getParameter("seqId");
%>
<html>
<head>
<title>项目相关文档管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=contextPath%>/core/styles/style1/css/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
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
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script Language="JavaScript">
var projId = <%=seqId%>;
//查询是否有项目相关文档
var pageMgr = null;
var cfgs = null;
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectFileAct/queryOutFileByProjId.act?projId=" + projId;
   cfgs = {
    dataAction: url,
    container: "giftList",
    moduleName:"profsys",  
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"fileNum", text:"文档编号", width: "6%",align:"center"},
       {type:"text", name:"fileName", text:"文档名称", width: "6%",align:"center"},
       {type:"text", name:"fileType", text:"文档类别", width: "6%",align:"center"},
       {type:"text", name:"fileCreator", text:"创建人", width: "6%",align:"center"},
       {type:"text", name:"fileTitle", text:"文档主题词", width: "6%",align:"center"},
       {type:"hidden", name:"attachId", text:"附件ID",align:"center",width:"1%"},
       {type:"text", name:"attach", text:"附件",align:"center",width:"8%",dataType:"attach"},
       {type:"selfdef",name:"opts",align:"center",align:"center", text:"操作", width:"8%",render:toOpts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if (total <= 0) {
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>尚未添加项目相关文档！</div></td></tr>"
        );
    $('giftList').style.display = "none";
    $('returnNull').update(table); 
  }
}
function toOpts(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<a href='#' onclick='getFileById(" + seqId + ");'>查看明细</a> "
        // +"<a href='javascript:deleteFileById(" + seqId + ");'> 删除 </a> ";
}
function deleteFileById(seqId){
  var msg="确定要删除该项 吗?";
  if(window.confirm(msg)){
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectFileAct/deleteFileById.act?seqId=" + seqId; 
    var json=getJsonRs(requestURL); 
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    }
    alert("删除成功！");
    window.location.reload();
  }
}
//修改
function getFileById(seqId) {
  if (seqId != "") {
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectFileAct/getFileById.act?seqId=" + seqId; 
    var json = getJsonRs(requestURL);
    if (json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ;
    }
    var prc = json.rtData; 
    if (prc.seqId) {
      var seqId = prc.seqId;
      $("fileNum").update(prc.fileNum);
      $("fileName").update(prc.fileName);
      $("fileType").update(prc.fileType);
      $("projCreator").value= prc.projCreator;
      if ($("projCreator").value != "") {
        bindDesc([{cntrlId:"projCreator",dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
      }
      $("fileTitle").update(prc.fileTitle);
      $("fileContent").update(prc.fileContent);
      $("fileNote").update(prc.fileNote); 
      $("attachmentName").value = prc.attachmentName;
      $("attachmentId").value = prc.attachmentId;
      attachMenuUtil("showAtt","profsys",null,$('attachmentName').value ,$('attachmentId').value,true);
    }
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInit()">

<table border="0" width="50%" cellspacing="0" cellpadding="3" class="small">
  <tr><td>
  <img src="<%=imgPath%>/notify_new.gif" align="absmiddle"/>
  <span class="big3">添加项目相关文档</span><td></tr>
</table>
<form id="form1" name="form1">
<input id="projCreator" name="projCreator" value="" type="hidden">
<table class="TableBlock" border="0" width="70%" align="center">
    <tr>
      <td nowrap class="TableContent" width="90">文档编号：</td>
      <td nowrap class="TableData" id="fileNum">
      </td>
    <td nowrap class="TableContent" width="90">文档名称</td>
      <td nowrap class="TableData"  id="fileName">
      </td> 
    </tr>
    <tr>
      <td nowrap class="TableContent" width="90">文档类别：</td>
      <td nowrap class="TableData" id="fileType">
      </td>
       <td nowrap class="TableContent" width="90">创建人：</td>
      <td nowrap class="TableData" id="projCreatorDesc">
      </td> 
    </tr>
    <tr>
      <td nowrap class="TableContent" width="90">文档主题词：</td>
      <td class="TableData" colspan="3" id="fileTitle">
      </td>     
    </tr>
    <tr>
      <td nowrap class="TableContent">内容：</td>
      <td class="TableData" colspan="3" id="fileContent">
</td>
    </tr>
     <tr>
      <td nowrap class="TableContent">备注：</td>
      <td class="TableData" colspan="3" id="fileNote">
       </td>
    </tr>   
    <tr id="attr_tr">
      <td nowrap class="TableData">附件文档：</td>
      <td class="TableData" colspan="3">
      <input type = "hidden" id="attachmentName" name="attachmentName"></input>
       <input type = "hidden" id="attachmentId" name="attachmentId"></input>
        <span id="showAtt">无附件</span>
      </td>
    </tr>
   <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
      <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.opener.location.reload();parent.window.close()">&nbsp;
      </td>
  </tr> 
</table>
 </form>

<table width="100%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr><td>
  <img src="<%=imgPath %>/user_group.gif" align="absmiddle"/>
  <span class="big3">项目相关文档列表</span>
  <td></tr>
</table>
<div id="giftList" style="padding-left: 10px; padding-right: 10px"></div>
<div id="returnNull"></div>
</body>
</html>
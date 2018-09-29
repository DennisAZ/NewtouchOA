<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String seqId = request.getParameter("seqId") == null ? "0" :  request.getParameter("seqId");
%>
<html>
<head>
<title>会谈纪要管理</title>
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
//查询是否有会议纪要
var pageMgr = null;
var cfgs = null;
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectCommAct/queryOutCommByProjId.act?projId=" + projId;
   cfgs = {
    dataAction: url,
    container: "giftList",
    moduleName:"profsys",  
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"commNum", text:"纪要编号", width: "6%",align:"center"},
       {type:"text", name:"commName", text:"纪要名称", width: "6%",align:"center"},
       {type:"text", name:"commMemCn", text:"中方人员", width: "6%",align:"center"},
       {type:"text", name:"commMemFn", text:"外方人员", width: "6%",align:"center"},
       {type:"text", name:"commTime", text:"时间", width: "6%",align:"center",render:toTime},
       {type:"text", name:"commPlace", text:"地点", width: "6%",align:"center"},
       {type:"hidden", name:"attachId", text:"附件ID",align:"center",width:"1%"},
       {type:"text", name:"attach", text:"附件",align:"center",width:"8%",dataType:"attach"},
       {type:"selfdef",name:"opts",align:"center",align:"center", text:"操作", width:"8%",render:toOpts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if (total <= 0) {
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>尚未添加会议纪要！</div></td></tr>"
        );
    $('giftList').style.display = "none";
    $('returnNull').update(table); 
  }
}
function toTime(cellData, recordIndex, columInde){
  var commTime = this.getCellData(recordIndex,"commTime");
  return commTime.substr(0,10);
}
function toOpts(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<a href='#' onclick='getCommById(" + seqId + ");'>查看明细</a>";
         //+"<a href='javascript:deleteCommById(" + seqId + ");'> 删除 </a> ";
}
function deleteCommById(seqId){
  var msg="确定要删除该项 吗?";
  if(window.confirm(msg)){
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectCommAct/deleteCommById.act?seqId=" + seqId; 
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
function getCommById(seqId) {
  if (seqId != "") {
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectCommAct/getCommById.act?seqId=" + seqId; 
    var json = getJsonRs(requestURL);
    if (json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ;
    }
    var prc = json.rtData; 
    if (prc.seqId) {
      var seqId = prc.seqId;
      $("commNum").update(prc.commNum);
      $("commName").update(prc.commName);
      $("commMemCn").update(prc.commMemCn);
      $("commMemFn").update(prc.commMemFn);
      $("commTime").update(prc.commTime.substr(0,10));
      $("commPlace").update(prc.commPlace);
      $("commContent").update(prc.commContent);
      $("commNote").update(prc.commNote);
      $("attachmentName").value = prc.attachmentName;
      $("attachmentId").value = prc.attachmentId;
      attachMenuUtil("showAtt","profsys",null,$('attachmentName').value ,$('attachmentId').value,true);
    }
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInit()">
<form id="form1" name="form1">
<table class="TableBlock" border="0" width="70%" align="center">
    <tr>
      <td nowrap class="TableContent"  width="90">纪要编号：</td>
      <td nowrap class="TableData" id="commNum">
      </td>  
    <td nowrap class="TableContent"  width="90">纪要名称：</td>
      <td nowrap class="TableData" id="commName">
      </td> 
    </tr>
    <tr>
      <td nowrap class="TableContent"  width="90">中方人员：</td>
      <td nowrap class="TableData" id="commMemCn">
      </td>  
    <td nowrap class="TableContent"  width="90">外方人员：</td>
      <td nowrap class="TableData" id="commMemFn">
      </td> 
    </tr>
      <tr>
      <td nowrap class="TableContent"  width="90">时间：</td>
      <td nowrap class="TableData" id="commTime">     
      </td>
      <td nowrap class="TableContent"  width="90">地点：</td>
      <td nowrap class="TableData" id="commPlace">
      </td> 
     </tr>
     <tr>
      <td nowrap class="TableContent">内容：</td>
      <td class="TableData" colspan="3" id="commContent">
</td>
    </tr>
     <tr>
      <td nowrap class="TableContent">备注：</td>
      <td class="TableData" colspan="3" id="commNote">
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
      <div id="but">
     
      <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.opener.location.reload();parent.window.close()">&nbsp;
      </div>
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
  <img src="<%=imgPath%>/user_group.gif" align="absmiddle"/>
  <span class="big3">会议纪要列表</span>
  <td></tr>
</table>
<br>
<div id="giftList" style="padding-left: 10px; padding-right: 10px"></div>
<div id="returnNull"></div>
</body>
</html>
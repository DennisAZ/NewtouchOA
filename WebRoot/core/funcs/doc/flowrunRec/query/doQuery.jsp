<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@page isELIgnored="false"%> 
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%@ page import="java.sql.Timestamp,java.util.Set,java.util.List,java.util.Map" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ include file="/core/inc/header.jsp" %>
<%
YHPerson user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
String flowId = request.getParameter("flowId");  
String sortId = request.getParameter("sortId");
if (sortId == null) {
  sortId = "";
}
String skin = request.getParameter("skin");
String skinJs = "messages";
if (skin != null && !"".equals(skin)) {
  skinJs = "messages_" + skin;
} else {
  skin = "";
}

String error = (String)request.getAttribute("error");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工作流查询</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/skin.js"></script>
<script type="text/javascript">
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";
function openCommentWindow(runId , flowId) {
  var url = contextPath + "<%=moduleContextPath %>/flowrun/list/comment.jsp?runId=" + runId + "&flowId=" + flowId;
  openDialog(url,  600, 400);
}
function handleWork(par){
  var url = contextPath + "<%=moduleContextPath %>/flowrun/list/inputform/index.jsp?" + par;
  location =  url;
}
function focus1(a,par) {
  if(window.confirm(tooltipMsg1)) {
    var url = contextPath + "<%=moduleSrcPath %>/act/YHFlowManageAct/focus.act";
    var json = getJsonRs(url, par) ;
    if (json.rtState == '0') {
      alert(json.rtMsrg); 
      a.innerHTML = "取消关注";
      a.title = "取消关注";
      a.onclick = function() {
        calFocus(this,par);
      }
    }
  }
}
function calFocus(a, par) {
  if(window.confirm(tooltipMsg2)) {
    var url = contextPath + "<%=moduleSrcPath %>/act/YHFlowManageAct/calFocus.act";
    var json = getJsonRs(url, par) ;
    if (json.rtState == '0') {
      alert(json.rtMsrg); 
      a.innerHTML = "关注";
      a.title = "关注";
      a.onclick = function() {
        focus1(this,par);
      }
    }
  }
}
/**
 * 委托
 * @return
 */
function trustAction(flowType , par){
  var page = "others";
  if (flowType == '2') {
    page="othersfree";
  }
  var url = contextPath + "<%=moduleContextPath %>/flowrun/list/others/"+page+".jsp?"+ par;
  myleft=(screen.availWidth-700)/2;
  mytop=(screen.availHeight-450)/2;
  window.open(url,"others","status=0,toolbar=no,menubar=no,width=700,height=450,location=no,scrollbars=yes,resizable=no,left="+myleft+",top="+mytop);
}
function edit(par){
  var url = contextPath + "<%=moduleContextPath %>/flowrun/list/inputform/edit.jsp?" + par;
  myleft=(screen.availWidth-800)/2;
  window.open(url ,"edit_run","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=800,height=600,left="+myleft+",top=50");
}
/**
 * 
 * @param par
 * @return
 */
function callBack(par){ 
  msg="下一步骤尚未接收时可收回至本步骤重新办理，确认要收回吗？"; 
  if(window.confirm(msg)) { 
    var url = contextPath+'<%=moduleSrcPath %>/act/YHMyWorkAct/callBack.act'; 
    var json = getJsonRs(url , par); 
    if (json.rtState == '0') { 
      alert(json.rtMsrg); 
    } else { 
      alert(json.rtMsrg); 
    } 
  } 
}
function restore(par) {
  var url = contextPath + "<%=moduleSrcPath %>/act/YHFlowManageAct/restore.act";
  var json = getJsonRs(url , par);
  if (json.rtState == '0') {
    alert(json.rtMsrg);
  }
}
function check_one(el)
{
   if(!el.checked)
      $("allbox_for").checked = false; 
}
function check_all()
{
  if($("allbox_for").checked) {
    var inputs = document.getElementsByTagName("input");
    for (var i = 0 ;i < inputs.length  ;i ++) {
      var input = inputs[i];
      if (input.name == 'run_select') {
        input.checked = true;
      }
    }
  } else {
    var inputs = document.getElementsByTagName("input");
    for (var i = 0 ;i < inputs.length  ;i ++) {
      var input = inputs[i];
      if (input.name == 'run_select') {
        input.checked = false;
      }
    }
  }
}
function endWorkFlow() {
  var runIds =  getRunIds();
  if (!runIds) {
    alert(tooltipMsg3) 
    return ;
  }
  if(!confirm(tooltipMsg4)) {
    return ;
  }
 var url = contextPath + "<%=moduleSrcPath %>/act/YHFlowManageAct/endWorkFlow.act";
 var json = getJsonRs(url, "runIdStr=" + runIds) ;
 if (json.rtState == '0') {
   alert('操作成功!');
 }
}
function getRunIds() {
  var runIds = "";
  var deleteFlags = document.getElementsByName("run_select") ;
  for(var i = 0 ; i < deleteFlags.length ; i++) {
    if(deleteFlags[i].checked){
      if(runIds){
        runIds += ",";
      }
      runIds += deleteFlags[i].value;
    }
  }
  return runIds;
}
function delWorkFlow() {
  var runIds =  getRunIds();
  if (!runIds) {
    alert(tooltipMsg5) 
    return ;
  }
  if(!confirm(tooltipMsg6)) {
    return ;
  }
  var url = contextPath + "<%=moduleSrcPath %>/act/YHFlowManageAct/delWorkFlow.act";
  var json = getJsonRs(url, "runIdStr=" + runIds) ;
  if (json.rtState == '0') {
    var aRunId = runIds.split(",");
    for (var i=0;i<aRunId.length;i++) {
      var runId = aRunId[i];
      var tr = $('tr-' + runId);
      if (tr) {
        $('tbody').removeChild(tr);
      }
    }
  }
}
function attachRender(attachStr) {
  if (attachStr) {
    var attach = eval(attachStr);
    var str = "";
    if (attach.length > 0) {
      for (var i = 0 ;i < attach.length ;i ++) {
        var attachment = attach[i];
        str += addAttachment(attachment);
      }
      document.write(str);
    }
  } else {
    document.write('无');
  }
}
/**
 * 添加附件
 * attachment:{attachmentName:'', attachmentId:'',ext:''}
 */
function addAttachment(attachment){
  var imgSrc = "";
  var ext = attachment.ext;
  var imgStr = getAttachImage(ext);
  var priv = attachment.priv;
  var tmp = attachment.attachmentName;
  if (tmp.length > 10){
    tmp = tmp.substring(0 , 10);
    tmp += "...";
  }
  var str = "<a title='"+attachment.attachmentName+"' href='javascript:' onmouseover='createDiv(event  ,this , \""+ attachment.attachmentId +"\" , \""+ attachment.attachmentName +"\" , \""+ attachment.ext +"\", \""+priv+"\")'><img src='" + imgStr + "'/>&nbsp;" + tmp + "</a><br>";
  return str;
 }
/**
 * 创建右建菜单
 * 
 */
function createDiv(event , node , attachId , attachName , ext , priv){
  var down = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">下载<div>',action:downAction ,extData: ""};
  var save = { attachmentId:attachId , attachmentName: attachName ,name:'<div style="padding-top:5px;margin-left:10px">转存<div>',action:saveAction,extData: ""};
  var read = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">阅读<div>',action:readAction,extData: ""};
  var print = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">阅读<div>',action:printAction,extData: ""};
  var preview = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">预览<div>',action:previewAction,extData: ""};
  var play = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">播放<div>',action:playAction,extData: ""};
  var menuD = [];
  var pr = priv.split(",");
  if ( isMedia(attachName) || isVideo(attachName)){
    menuD.push( play );
    menuD.push( down );
    menuD.push( save );
  }else if( findIsIn(docEnd , ext ) ){
    if (pr[0] == '1') {
      menuD.push( down );
      menuD.push( save );
    }
    if (pr[1] == '1' ) {
      menuD.push( print );
    } else {
      menuD.push( read );
    }
   } else {
     menuD.push( down );
     menuD.push( save );
   }
  var divStyle = {border:'1px solid #69F',width:'100px',position :'absolute',backgroundColor:'#FFFFFF',fontSize:'10pt',display:"block"};
  var menu = new Menu({bindTo:node , menuData:menuD , attachCtrl:true},divStyle);
  menu.show(event);
}
function exportRunSel() {
  var runIds =  getRunIds();
  if (!runIds) {
    alert(tooltipMsg7);
    return;
  }
  var url = contextPath + "<%=moduleSrcPath %>/act/YHWorkQueryAct/getExcelData.act?runIds=" + runIds;
  window.open(url);
}
function exportZip() {
  var runIds =  getRunIds();
  if (!runIds) {
    alert(tooltipMsg7);
    return;
  }
  var url = contextPath + "<%=moduleSrcPath %>/act/YHFlowExportAct/exportFlowZip.act?runIdStr=" + runIds;
  window.open(url);
}
function doInit() {
  skinObjectToSpan(flowrun_query_doQuery);
}
</script>
</head>
<body onload="doInit()">
<img src="<%=imgPath %>/infofind.gif" align="absmiddle">&nbsp;<span class="big3" ><span id="span1"> </span> ${param.flowName}</span>
<%

if (error != null) {
  out.println("<div align=center>");
  out.println(error);
  out.println("</div>");
  return ;
}
List<Map> list = (List<Map>)request.getAttribute("result");
%>
<table id="flow_table" width="100%" class="TableList" align="center" style="display:<% if (list.size() <= 0 ) out.print("none"); %>">
    <tr class="TableHeader">
<%if (user.isAdminRole()) {%>
<td nowrap align="center">选择</td>
<%} %>
<td nowrap align="center" width='60px'><a href="javascript:void(0)">流水号</a></td>
      <td nowrap align="center" width='120px'><a href="javascript:void(0)"><span id="span2"></span></a></td>
      <td nowrap align="center"><a href="javascript:void(0)">开始时间</a></td>
      <td nowrap align="center"><a href="javascript:void(0)">公共附件</a></td>
      <td nowrap align="center"><a href="javascript:void(0)">状态</a></td>
      <td nowrap align="center">操作</td>
    </tr>
    <tbody id="tbody">
<% 

int count = 0 ;
for (Map map : list) {
  String runId = (String)map.get("runId");
  String runName = (String)map.get("runName");
  String bgTime = (String)map.get("beginTime");
  Timestamp endTime =(Timestamp)map.get("endTime");
  String flowType = (String)map.get("flowType");
  String attachs = (String)map.get("attach");
  String flowName = (String)map.get("flowName");
  String status = (String)map.get("status");
  
  String prcsId = (String)map.get("prcsId");
  if (prcsId == null) {
    prcsId = "";
  }
  String flowPrcs = (String)map.get("flowPrcs");
  if (flowPrcs == null) {
    flowPrcs = "";
  }
  boolean hasComPriv = (Boolean)map.get("hasComPriv");
  boolean hasHandler = (Boolean)map.get( "hasHandler");
  boolean hasOther = (Boolean)map.get( "hasOther");
  
  boolean hasFocus = (Boolean)map.get( "hasFocus");
  boolean hasCalFocus = (Boolean)map.get( "hasCalFocus");
  Boolean bHasEdit = (Boolean)map.get( "hasEdit");
  boolean hasEdit= false;
  if (bHasEdit != null ) {
    hasEdit = bHasEdit;
  }
  boolean hasCallback = (Boolean)map.get( "hasCallback");
  
  String par = "runId="+runId+"&flowId="+flowId+"&prcsId="+prcsId+"&flowPrcs="+flowPrcs+"&sortId=" + sortId + "&skin=" + skin;

  String opStr = "<a href='javascript:;' onclick='flowView("+runId+","+flowId+",\"\",\"" + sortId +"\",\"" + skin +"\")'>流程图</a>&nbsp;";
  if (hasHandler) {
    opStr += "<a href=\"javascript:handleWork('"+par+"');\" >办理</a>&nbsp;";
  }
  if (hasComPriv) {
    opStr += "<a href='javascript:void(0)' title=\"添加点评意见\" onclick='openCommentWindow("+runId+" , "+flowId+")'>点评</a>&nbsp;";
  }
  if (hasOther) {
    opStr += "<a href='javascript:void(0)' title=\"委托\" onclick=\"trustAction('"+flowType+"' , '"+par+"')\">委托</a>&nbsp;";
  }
  if (hasFocus) {
    opStr += "<a href='javascript:void(0)' title=\"关注\" onclick=\"focus1(this,'"+par+"')\">关注</a>&nbsp;";
  }
  if (hasCalFocus) {
    opStr += "<a href='javascript:void(0)' title=\"取消关注\" onclick=\"calFocus(this,'"+par+"')\">取消关注</a>&nbsp;";
  }
  if (hasEdit) {
    opStr += "<a href='javascript:void(0)' title=\"编辑\" onclick=\"edit('"+par+"')\">编辑</a>&nbsp;";
  }
  if (hasCallback) {
    opStr += "<a href='javascript:void(0)' title=\"收回\" onclick=\"callBack('"+par+"')\">收回</a>&nbsp;";
  }
  if (endTime != null) {
    opStr += "<a href='javascript:void(0)'  onclick=\"restore('"+par+"')\">恢复执行</a>&nbsp;";
  }
  String className = "TableLine1";
  if (count%2 == 0) {
    className = "TableLine2";
  }
  %>
   <tr class="<%=className %>" id="tr-<%=runId %>">
   <%if (user.isAdminRole()) {%>
    <td nowrap align="center">
    <input type="checkbox" name="run_select" value="<%=runId %>" onClick="check_one(this)">
    </td>
    <%} %>
   <td nowrap align=center width=60><%=runId %></td>
  <td nowrap  width=120 class='auto' title="<%=runName %>"><a onclick="formView(<%=runId %>,<%=flowId %>)" href="javascript:;"><%=runName %></a></td>
  <td nowrap align=center class='auto'><%=bgTime %></td>
  <td nowrap align=center class='auto'><script type="javascript">attachRender("<%=attachs %>")</script></td>
  <td nowrap align=center class='auto'><%=status %></td>
  <td nowrap align=left class='auto'><%=opStr %></td>
    </tr>
  <%
  count++;
}
%>
</tbody>
 <%if (user.isAdminRole()) {%>
    <TR class=TableControl>
<TD colSpan=10>&nbsp;
<INPUT id=allbox_for onclick=check_all(); type=checkbox name=allbox> 
<LABEL style="CURSOR: hand" for=allbox_for><U><B>全选</B></U></LABEL> 
&nbsp; <INPUT class=SmallButtonC  onclick=exportRunSel(); value=导出Excel type=button> 
&nbsp; <INPUT class=SmallButtonC onclick=exportZip(); value=导出ZIP type=button> 
&nbsp; <INPUT class=SmallButtonC  onclick=delWorkFlow();  value=管理人员删除 type=button> 
&nbsp; <INPUT class=SmallButtonC  onclick=endWorkFlow();  value=强制结束 type=button> &nbsp;
 </TD></TR>
    <%} %>
</table>
 <div align=center>
   <table class="MessageBox" align="center" width="210">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">共 <%=count %> 条</div>
    </td>
  </tr>
</table>
<input type="button" onclick="history.back();" value="返回" class="SmallButton"/>
</div>
</body>
</html>
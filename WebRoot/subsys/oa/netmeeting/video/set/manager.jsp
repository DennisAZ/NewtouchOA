<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置管理员</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/netmeeting/video/js/util.js"></script>
<script> 
var pageMgr;
function doInit(){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/netmeeting/video/act/YHVideoMeetingAct/getPage.act";
  var cfgs = {
    dataAction: requestURL,
    container: "listDiv",
    colums: [
       {type: "hidden", name: "seqId", text: "ID", width: 100},
       {type: "hidden", name: "userId", text: "序号", width: 100,align: 'center'},
       {type: "text", name: "userName", text: "OA用户", width: 200,align: 'center'},
       {type: "text", name: "redUsername", text: "红杉树系统用户名", width: 200,align:'center'},
       {type:"selfdef", text:"操作", width: '20%',render:opts}]
  };
  
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
}

function opts(cellData, recordIndex, columIndex){
  var edit = '<a href="javascript:void(0)" onclick="updateRecord(' + recordIndex + ');return false;">编辑</a>';
  var del = '<a href="javascript:void(0)" onclick="deleteRecord(' + recordIndex + ')">删除</a>';
  var sp = '&nbsp;&nbsp;';
  
  return '<center>' + edit + sp + del + '</center>';
}

function updateRecord(recordIndex){
  $('seqId').value = pageMgr.getCellData(recordIndex,"seqId");
  $('userId').value = pageMgr.getCellData(recordIndex,"userId");
  $('userName').value = pageMgr.getCellData(recordIndex,"userName");
  $('redUsername').value = pageMgr.getCellData(recordIndex,"redUsername");
  $('userName').focus();
}

function deleteRecord(recordIndex){
  if (!confirm('是否删除记录?')){
    return;
  }
  var url = "<%=contextPath %>/yh/subsys/oa/netmeeting/video/act/YHVideoMeetingAct/deleteManager.act";
  var seqId = pageMgr.getCellData(recordIndex,"seqId");
  var pars = "seqId=" + seqId;
  var json = getJsonRs(url,pars);
  if(json.rtState == "0"){
    alert("删除成功");
    pageMgr.refreshAll();
  }else{
    alert("删除失败");
  }
}

function submitForm() {
  if (!$F('userName')){
    alert('OA用户不能为空');
    return;
  }
  if (!$F('redUsername')){
    alert('红杉树系统用户名不能为空');
    return;
  }
  if (!$F('redPassword')){
    alert('红杉树系统密码不能为空');
    return;
  }
  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath %>/yh/subsys/oa/netmeeting/video/act/YHVideoMeetingAct/editManager.act";
  var json = getJsonRs(url,pars);
  if (json.rtState == "0"){
    alert("操作成功");
    pageMgr.refreshAll();
    clear1();
  } else{
    alert("添加失败");
  }
}

function clear1(){
	$('userId').value = "";
	$('userName').value = "";
	$('redUsername').value = "";
	$('redPassword').value = "";
	$('seqId').value = "";
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<br><br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/notify.gif" align="absMiddle" width="17"><span class="big3">&nbsp;编辑管理员    </span>
   </td>
 </tr>
</table>

<form action=""  method="post" name="form1" id="form1">
  <table class="TableBlock" width="450" align="center">
   <tr>
    <td nowrap class="TableData">OA用户：<font color="red">*</font></td>
    <td nowrap class="TableData">
      <input type="hidden" name="userId" id="userId" value="" >
      <input type="text" name="userName" id="userName" class="BigStatic" readonly size="18" >
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['userId', 'userName']);">添加</a>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">红杉树系统用户名：<font color="red">*</font></td>
    <td nowrap class="TableData">
        <input type="text" name="redUsername" id="redUsername" class="BigInput" size="25" maxlength="50" >
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">红杉树系统密码：<font color="red">*</font></td>
    <td nowrap class="TableData">
        <input type="password" name="redPassword" id="redPassword" class="BigInput" size="27" maxlength="50" >
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
      <input type="hidden" id="seqId" name="seqId">
      <input type="hidden" value="yh.subsys.oa.netmeeting.video.data.YHVideoMeetingManager" name="dtoClass" id="dtoClass">
      <input type="button" value="确定" onclick="submitForm()">&nbsp;&nbsp;
      <input type="button" value="清空" onclick="clear1()">&nbsp;&nbsp;
    </td>
   </tr>
  </table>
</form>

<br>

<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absmiddle"><span class="big3"> 管理员列表</span>
    </td>
  </tr>
</table>

<br>
<div align="center" id="listDiv">
</div>
</body>
</html>
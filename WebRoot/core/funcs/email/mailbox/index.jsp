<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>InnerMail</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/emaillogic.js"></script>
<script>
function CheckForm(){
  if($('boxNo').value==""){ 
    alert("邮箱序号不能为空！");
    $('boxNo').focus();
    return false;
  }
  if(checkNum($('boxNo').value)){ 
    alert("邮箱序号必须为数值！");
    $('boxNo').focus();
    $('boxNo').select();
    return false;
  }

  if($('boxNo').value < 1){
    alert("邮箱序号必须大于0！");
    $('boxNo').focus();
    $('boxNo').select();
    return false;
   }
  if($('boxName').value==""){
    alert("邮箱名称不能为空！");
    $('boxName').focus();
    return false;
  }
  if(checkStr($('boxName').value)){
    alert("邮箱名称不能包含:[\",\',&,%,#,\\] 等特殊字符！");
    $('boxName').focus();
    $('boxName').select();
    return false;
  }
  if(isBoxNameExist($('boxName').value)){
    alert("邮箱名称不能重复！");
    $('boxName').focus();
    $('boxName').select();
    return false;
   }
  return true;
}
function checkNum(str){
   var re=/\D/;
   return str.match(re);
}
function checkStr(str){
  var re=/["'&%#\\]/;
  return str.match(re);
}
function doSubmit(){
  if(CheckForm()){
    saveBox();
    location.reload();
  }
}
/**
 * 显示邮箱列表
 [{seqId:,boxNo:,boxName:,boxSize:,}...]
 */
function showBox(){
  var boxTable = $('boxManager');
  var count = 0;
  //默认邮箱
  count += bindDefaultBox();
  var url = contextPath + "/yh/core/funcs/email/act/YHEmailBoxAct/getSelf.act";
  var rtJson = getJsonRs(url);
  //自定义邮件箱
  if(rtJson.rtState == "0"){
    for(var i = 0 ; rtJson.rtData && i < rtJson.rtData.length ; i++){
      var tabLen = boxTable.rows.length;
      var dataRow = boxTable.insertRow(tabLen);
      var obj = rtJson.rtData[i].box;
      var boxSize = rtJson.rtData[i].size;
      var msize = (boxSize/(1024*1024)).toFixed(2);
      dataRow.className = "TableData";
      var tdstr = " <td nowrap align=\"center\">" + obj.boxNo + "</td>";
      tdstr += "<td nowrap align=\"center\">" + obj.boxName + "</td>";
      tdstr +=  "<td nowrap><span id=\"PAGESIZE_IN_" + obj.seqId + "_SIZEB\" >" + boxSize + "</span>字节 （约合<span id=\"PAGESIZE_IN_" + obj.seqId + "_SIZEM\" >" + msize + "</span>M)</td>";
      //tdstr +=  "<td nowrap style=\"display:none\"><input type=\"hidden\" value=\"" + obj.defaultCount + "\" id=\"PAGESIZE_IN_" + obj.seqId + "\" class=\"SmallInput\" size=\"3\"> ";
      //tdstr += "<input type=\"hidden\" value=\"设置\" class=\"SmallButtonW\" onclick=\"setEmailNums('" + obj.seqId  + "','" + obj.boxName + "','PAGESIZE_IN_" + obj.seqId + "');\"></td>";
      tdstr += "<td nowrap align=\"center\"><a href=\"" + contextPath + "/core/funcs/email/mailbox/edit.jsp?boxId=" + obj.seqId + "\"> 编辑</a>";
      tdstr +=  "<a href=\"javascript:deleteMisBox(\'" + obj.seqId + "\');\"> 删除</a></td>";
      $(dataRow).insert(tdstr,'bottom');
      //rows.innerHTML = tdstr;
      count += boxSize;
    }
  }
  var countRow = boxTable.insertRow(boxTable.rows.length);
  countRow.className = "TableContent";
  var countStr = (count/(1024*1024)).toString();
  var countTdStr = "<td nowrap align=\"center\" colspan=\"2\"><b>合计：</b></td>";
  countTdStr += "<td nowrap colspan=\"2\">&nbsp;&nbsp;" + ((countStr.indexOf(".") == -1 )?countStr: countStr.substring(0,countStr.indexOf(".") + 3)) + "MB</td>";
  $(countRow).insert(countTdStr,'bottom');
}

function setEmailNums(boxSeqId,boxName,cntrlId){
  var pageSize = $(cntrlId).value;
  if(checkNum(pageSize)){
   alert("请输入数值!");
   return;
  }
  var param = "seqId=" + boxSeqId + "&boxName=" + boxName + "&pageSize=" + pageSize;
  var url = contextPath + "/yh/core/funcs/email/act/YHEmailBoxAct/setBoxPage.act?" + param;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    alert("修改成功！");
  }else{
    alert(rtJson.rtMsrg);
  }
}
function bindDefaultBox(){
  var count = 0;
  var url = contextPath + "/yh/core/funcs/email/act/YHEmailBoxAct/getDefBox.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    for(var i = 0 ; i < rtJson.rtData.length; i++){
      var obj = rtJson.rtData[i].box;
      var name = rtJson.rtData[i].name;
      var size = rtJson.rtData[i].size;
      //if(obj && obj.defaultCount){
     //   $(name).value = obj.defaultCount;
      //}else{
     //   $(name).value = 10;
     // }
      var sizeName = name + "_size";
      var sizeNameM = name + "_sizeM";
      var M = (size/(1024*1024)).toString();
      $(sizeName).innerHTML = size;
      $(sizeNameM).innerHTML = (M.indexOf(".") == -1 )? M : M.substring(0,M.indexOf(".") + 3);
      count += size;
    }
  }
  return count;
}
function doInit(){
  showBox();
}
function deleteMisBox(boxId){
  msg='确认要删除所选邮箱吗？';
  if(window.confirm(msg)){
    var url = contextPath + "/yh/core/funcs/email/act/YHEmailBoxAct/deleteBox.act?boxId=" + boxId;
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
      alert(rtJson.rtMsrg);
      window.location.reload();
    }
  }
}
</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/cmp/email/notify_new.gif" align="absmiddle">&nbsp;&nbsp;<span class="big3"> 添加邮件箱</span>
    </td>
  </tr>
</table>
<form id="form1" name="form1" >
<table class = "TableBlock" width="450" align="center" >
   <tr>
    <td nowrap class="TableData">排序号：</td>
    <td nowrap class="TableData">
        <input type="text" id="boxNo" name="boxNo" class="BigInput" size="25" maxlength="25">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">名称：</td>
    <td nowrap class="TableData">
        <input type="text" id="boxName" name="boxName" class="BigInput" size="25" maxlength="25">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="button" class="BigButton" value="添加" title="添加" onclick="doSubmit();">
    </td>
</table>
</form>
<br>
 
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="/images/dian1.gif" width="100%"></td>
 </tr>
</table>
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/cmp/email//notify_open.gif" align="absmiddle">&nbsp;&nbsp;<span class="big3"> 管理邮件箱</span>
    </td>
  </tr>
</table>
<br>
<table class="TableList" width="90%" align="center" id="boxManager">
     <tr class="TableHeader">
      <td nowrap align="center" width="50">排序号</td>
      <td nowrap align="center">名称</td>
      <td nowrap align="center">占用空间</td>
      <!-- <td nowrap align="center" style="display:none">每页显示邮件数</td> -->
      <td nowrap align="center" width="60">操作</td>
    </tr>
    <tr class="TableData">
      <td nowrap>
      </td>
      <td nowrap align="center">收件箱</td>
       <td><span id="PAGESIZE_IN0_size" >108,881</span>字节 （约合<span id="PAGESIZE_IN0_sizeM" >0.1</span>M）</td>
     <!-- <td nowrap style="display:none"><input type="hidden" value="10" name="PAGESIZE_IN0" id="PAGESIZE_IN0" class="SmallInput" size="3"> <input type="hidden" value="设置" class="SmallButtonW" onClick="setEmailNums('','PAGESIZE_IN0','PAGESIZE_IN0');"></td>-->
      <td nowrap align="center"></td>
    </tr>
    <tr class="TableData">
      <td nowrap></td>
      <td nowrap align="center">草稿箱</td>
      <td><span id="PAGESIZE_OUT_size" >108,881</span>字节 （约合<span id="PAGESIZE_OUT_sizeM" >0.1</span>M）</td>
      <!-- <td nowrap style="display:none"><input type="hidden" value="10" name="PAGESIZE_OUT" id="PAGESIZE_OUT" class="SmallInput" size="3"> <input type="hidden" value="设置" class="SmallButtonW" onClick="setEmailNums('','PAGESIZE_OUT','PAGESIZE_OUT');"></td>-->
      <td nowrap align="center"></td>
    </tr>
    <tr class="TableData">
      <td nowrap></td>
      <td nowrap align="center">已发送邮件箱</td>
      <td><span id="PAGESIZE_SENT_size" >108,881</span>字节 （约合<span id="PAGESIZE_SENT_sizeM" >0.1</span>M）</td>
      <!-- <td nowrap style="display:none"><input type="hidden" value="10" name="PAGESIZE_SENT" id="PAGESIZE_SENT" class="SmallInput" size="3"> <input type="hidden" value="设置" class="SmallButtonW" onClick="setEmailNums('','PAGESIZE_SENT','PAGESIZE_SENT');"></td>-->
      <td nowrap align="center"></td>
    </tr>
    <tr class="TableData">
      <td nowrap></td>
      <td nowrap align="center">外发邮件箱</td>
      <td><span id="PAGESIZE_WEB_size" >108,881</span>字节 （约合<span id="PAGESIZE_WEB_sizeM" >0.1</span>M）</td>
      <!-- <td nowrap style="display:none"><input type="hidden" value="10" name="PAGESIZE_WEB" id="PAGESIZE_WEB" class="SmallInput" size="3"> <input type="hidden" value="设置" class="SmallButtonW" onClick="setEmailNums('','PAGESIZE_WEB','PAGESIZE_WEB');"></td>-->
      <td nowrap align="center"></td>
    </tr>
    <tr class="TableData">
      <td nowrap></td>
      <td nowrap align="center">废件箱</td>
      <td><span id="PAGESIZE_DEL_size" >108,881</span>字节 （约合<span id="PAGESIZE_DEL_sizeM" >0.1</span>M）</td>
      <!-- <td nowrap style="display:none"><input type="hidden" value="10" name="PAGESIZE_DEL" id="PAGESIZE_DEL" class="SmallInput" size="3"> <input type="hidden" value="设置" class="SmallButtonW" onClick="setEmailNums('','PAGESIZE_DEL','PAGESIZE_DEL');"></td>-->
      <td nowrap align="center"></td>
    </tr>
</table>
</body>
</html>
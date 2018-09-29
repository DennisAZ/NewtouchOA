<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>收藏夹管理</title>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/ExchangeSelect.css">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script  type="text/Javascript">

var pageMgr;
function doInit(){
  var requestURL = "<%=contextPath%>/yh/core/funcs/setdescktop/fav/act/YHFavAct/getPage.act";
  var cfgs = {
    dataAction: requestURL,
    container: "listDiv",
    colums: [
       {type: "hidden", name: "seqId", text: "ID", width: 100},
       {type: "text", name: "urlNo", text: "序号", width: '10%',align: 'center'},
       {type: "text", name: "urlDesc", text: "说明", width: '30%',align: 'center'},
       {type: "text", name: "url", text: "网址", width: '30%',align:'center',render: urlRender},
       {type: "text", name: "openType", text: "新窗口打开", width: '10%',align: 'center',render: openTypeRender},
       {type:"selfdef", text:"操作", width: '20%',render:opts}]
  };
  
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
}

function openTypeRender(cellData, recordIndex, columInde){
  if(cellData == '1')
	  return '是';
  else
	  return '否';
}

function urlRender(cellData, recordIndex, columInde){
  var url = cellData.replace(/1:/,'');
  return "<a target=\"_blank\" href=\"" + url + "\">" + url + "</a>";
}

function updateRecord(recordIndex){
  var seqId = pageMgr.getCellData(recordIndex,"seqId");
  var urlNo = pageMgr.getCellData(recordIndex,"urlNo");
  var urlDesc = pageMgr.getCellData(recordIndex,"urlDesc");
  var url = pageMgr.getCellData(recordIndex,"url");

  //对get方式的参数进行编码
  urlDesc = encodeURIComponent(urlDesc.replace(new RegExp("\"", "g"), "\\\"").replace(new RegExp("\'", "g"), "\\\'"));
  url = encodeURIComponent(url.replace(new RegExp("\"", "g"), "\\\"").replace(new RegExp("\'", "g"), "\\\'"));
  
  window.location.href = "<%=contextPath %>/core/funcs/setdescktop/fav/edit.jsp?seqId=" 
    + seqId + "&urlNo=" 
    + urlNo + "&url=" 
    + url + "&urlDesc=" 
    + urlDesc;
}

function opts(cellData, recordIndex, columIndex){
  var edit = '<a href="javascript:void(0)" onclick="updateRecord(' + recordIndex + ');return false;">编辑</a>';
  var del = '<a href="javascript:void(0)" onclick="deleteRecord(' + recordIndex + ')">删除</a>';
  var sp = '&nbsp;&nbsp;';
  
  return '<center>' + edit + sp + del + '</center>';
}

function deleteRecord(recordIndex){
  
  if (!confirm('是否删除该收藏?')){
    return;
  }
  
  var url = "<%=contextPath %>/yh/core/funcs/setdescktop/fav/act/YHFavAct/deleteUrl.act";
  var seqId = pageMgr.getCellData(recordIndex,"seqId");
  var pars = "seqId=" + seqId;
  var json = getJsonRs(url,pars);
  if(json.rtState == "0"){
    alert("删除成功");
    top.reloadFavMenu && top.reloadFavMenu();
    pageMgr.refreshAll();
  }else{
    alert("删除失败");
  }
}

function submitForm() {
  if (!(/^[0-9]+$/).exec(($F('urlNo')))){
    alert('序号必须为数字!');
    $('urlNo').select();
    return;
  }
  if (!$F('urlDesc')){
    alert('名称不能为空');
    $('urlDesc').select()
    return;
  }
  if (!$F('urltext')){
    alert('网址不能为空');
    $('urltext').select()
    return;
  }
  $('url').value = ($('openWindow').checked ? "1:":"") + $F('urltext');
  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath %>/yh/core/funcs/setdescktop/fav/act/YHFavAct/addUrl.act";
  var json = getJsonRs(url,pars);
  if (json.rtState == "0"){
    pageMgr.refreshAll();
    $('form1').reset();
    top.reloadFavMenu && top.reloadFavMenu();
  } else{
    alert("添加失败");
  }
}
</script>
</head>

<body onload="doInit()">
<div class="PageHeader">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 添加收藏</span>
    </td>
  </tr>
</table>
</div>
<form action=""  method="post" name="form1" id="form1">
  <table class="TableBlock" width="450" align="center">
   <tr>
    <td nowrap class="TableData">序号：<span style="color: red;">*</span></td>
    <td nowrap class="TableData">
        <input type="text" name="urlNo" id="urlNo" class="BigInput" size="10" maxlength="25">
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">名称：<span style="color: red;">*</span></td>
    <td nowrap class="TableData">
        <input type="text" name="urlDesc" id="urlDesc" class="BigInput" size="25" maxlength="200">
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">网址：<span style="color: red;">*</span></td>
    <td nowrap class="TableData">
        <input type="text" name="urltext" id="urltext" class="BigInput" size="50" maxlength="200" value="">
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">选项：</td>
    <td nowrap class="TableData">
        <input type="checkbox" id="openWindow" name="openWindow"><label for="openWindow">在新窗口打开</label>
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
      <input type="hidden" value="yh.core.funcs.system.url.data.YHUrl" name="dtoClass" id="dtoClass">
      <input type="hidden" name="url" id="url">
      <input type="hidden" name="urlType" id="urlType" value="2">
      <input type="button" value="添加" class="BigButtonA" title="添加网址" onclick="submitForm()">&nbsp;&nbsp;
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
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absmiddle"><span class="big3"> 管理收藏夹</span>
    </td>
  </tr>
</table>

<br>
<div align="center" id="listDiv">
</div>
</body>
</html>
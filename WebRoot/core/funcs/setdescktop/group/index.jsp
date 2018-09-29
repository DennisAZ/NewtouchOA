<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>用户组管理</title>
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
  var requestURL = "<%=contextPath%>/yh/core/funcs/setdescktop/group/act/YHUserGroupAct/getPage.act";
  var cfgs = {
    dataAction: requestURL,
    container: "listDiv",
    colums: [
       {type: "hidden", name: "seqId", width: 100},
       {type: "text", name: "orderNo", text: "排序号", width: '10%',align: 'center'},
       {type: "text", name: "groupName", text: "用户组名称", width: '30%',align: 'center'},
       {type: "hidden", name: "userStr", width: '40%',align: 'center'},
       {type:"selfdef", text:"操作", width: '20%',render:opts}]
  };
  
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
}


function opts(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var psnName = this.getCellData(recordIndex,"psnName");
  var groupId = this.getCellData(recordIndex,"groupId");
  var edit = '<a href="javascript:void(0)" onclick="updateRecord(' + recordIndex + ');return false;">编辑</a>';
  var del = '<a href="javascript:void(0)" onclick="deleteRecord(' + recordIndex + ')">删除</a>';
  var setUser = '<a href="<%=contextPath %>/core/funcs/setdescktop/group/setUser.jsp?seqId=' + seqId + '">设置用户</a>';
  var sp = '&nbsp;&nbsp;';
  
  return '<center>' + edit + sp + del + sp + setUser + '</center>';
}


function openTypeRender(cellData, recordIndex, columInde){
  if(cellData == '1')
	  return '是';
  else
	  return '否';
}

function urlRender(cellData, recordIndex, columInde){
  return cellData.replace(/1:/,'');
}

function updateRecord(recordIndex){
  var seqId = pageMgr.getCellData(recordIndex,"seqId");
  var orderNo = pageMgr.getCellData(recordIndex,"orderNo");
  var groupName = pageMgr.getCellData(recordIndex,"groupName");

  //对get方式的参数进行编码
  groupName = encodeURIComponent(groupName.replace(new RegExp("\"", "g"), "\\\"").replace(new RegExp("\'", "g"), "\\\'"));
  
  window.location.href = "<%=contextPath %>/core/funcs/setdescktop/group/edit.jsp?seqId=" 
    + seqId + "&orderNo=" 
    + orderNo + "&groupName=" 
    + groupName;
}

function deleteRecord(recordIndex){
  
  if (!confirm('是否删除该用户组?')){
    return;
  }
  
  var url = "<%=contextPath %>/yh/core/funcs/setdescktop/group/act/YHUserGroupAct/delete.act";
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
  if (!(/^[0-9]+$/).exec(($F('urlNo')))){
    alert('序号为数字!');
    return;
  }
  if (!$F('urlDesc')){
    alert('名称不能为空');
    return;
  }
  if (!$F('urltext')){
    alert('网址不能为空');
    return;
  }
  $('url').value = ($('openWindow').checked ? "1:":"") + $F('urltext');
  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath %>/yh/core/funcs/setdescktop/fav/act/YHFavAct/addUrl.act";
  var json = getJsonRs(url,pars);
  if (json.rtState == "0"){
    pageMgr.refreshAll();
    $('form1').reset();
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
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 新建用户组</span><br>
    </td>
  </tr>
</table>
</div>
<div align="center">
<input type="button" value="新建用户组" class="BigButton" onClick="location='<%=contextPath %>/core/funcs/setdescktop/group/new.jsp';" title="创建新的用户组">
</div>

<br>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absmiddle"><span class="big3"> 管理用户组</span>
    </td>
  </tr>
</table>

<br>
<div id="listDiv"></div>
</body>
</html>
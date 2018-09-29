<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>签章管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<link rel="stylesheet" href="<%=cssPath%>/style.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript">
var pageMgr;
function doInit(){
  
  var requestURL = "<%=contextPath%>/yh/core/funcs/mobilesms/act/YHMobileSms2Act/listSendSms.act";
  var content = encodeURI(encodeURI("${param.content}"));
  var pars = "?beginTime=${param.beginTime}&endTime=${param.endTime}&phone=${param.phone}&sendFlag=${param.sendFlag}&content=" + content;
  var cfgs = {
    dataAction: requestURL + pars,
    container: "listDiv",
    colums: [
       {type:"hidden", name:"seqId", text:"ID"},
       {type:"selfdef", name:"", text:"选择", width:50,render:checkBoxRender},
       {type:"text", name:"userName", text:"收信人", width:100,align:'center'},
       {type:"text", name:"phone", text:"手机号码", width:100,align:'center'},
       {type:"text", name:"content", text:"内容", width:200,align:'center'},
       {type:"text", name:"sendTime", text:"发送日期", width:150,align:'center',dataType:'dateTime',format:'YY-MM-DD hh:min:ss'},
       {type:"text", name:"sendFlag", text:"状态", width:100,align:'center',render:statusRender}
       ]
  };
  
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  initPage();
}

function initPage() {
  if (!pageMgr.pageInfo.recordCnt){
    $('listRecords').hide();
    $('noRecord').show();
    return;
  }
  $('recordsCount').innerHTML = '共' + pageMgr.pageInfo.recordCnt + '条记录';
}

function statusRender(cellData, recordIndex, columInde){
  switch(cellData){
  case '0':return "未发送";
  case '1':return "发送成功";
  case '2':return "发送超时，请人工确认";
  case '3':return "发送中...";
  default:return "";
  };
}

function checkBoxRender(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  if (this.getCellData(recordIndex,"sendFlag") == '1'){
    return "";
  }
  return '<input type="checkbox" value="' + seqId + '" class="sendSms">';
}

function checkAll(t){
  $$('input.sendSms').each(function(e,i){
    e.checked = t.checked;
  });
}

function deleteRecords(){
  var seqIdStr = '';
  $$('input.sendSms').each(function(e,i){
    if (e.checked){
      seqIdStr += e.value + ',';
    }
  });

  if (seqIdStr != '' && !confirm('确定要删除短信吗?')) {
    return;
  }


  var url = "<%=contextPath%>/yh/core/funcs/mobilesms/act/YHMobileSms2Act/deleteSms.act?seqIdStr=" + seqIdStr;
  var json = getJsonRs(url);
  if(json.rtState == "0"){
    pageMgr.refreshAll();
    initPage();
  }else{
    alert("删除失败");
  }
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<div id="listRecords" style="width:670px">
	<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
	  <tr>
	    <td class="Big"><img src="<%=imgPath%>/mobile_sms.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 短信发送查询结果 （最多显示500条记录）</span>
	      <input type="button" class="BigButton" value="刷新" onClick="location.reload();">
	    </td>
	  </tr>
	</table>
	<div id="listDiv" align="left"></div>
	<table border="0" width="670px" cellspacing="0" cellpadding="3" class="small" align="left">
	  <tr class="TableControl">
	    <td class="Big">
	      <input type="checkbox" onclick="checkAll(this)"><span>全选</span>
	      <input type="button" class="BigButton" value="删除" onClick="deleteRecords()">
	    </td>
	  </tr>
	</table>
	<br>
	<br>
	<br>
	<br>
	<br>
	<table class="MessageBox" align="center" width="250">
	  <tr>
	    <td class="msg info">
	      <div class="content" style="font-size:12pt" id="recordsCount"></div>
	    </td>
	  </tr>
	</table>
	<br>
	<table align="center">
  <tr>
    <td>
      <div style="width:100%"><input type="button" class="BigButton" value="返回" onClick="location='index.jsp';"></div>
    </td>
  </tr>
</table>
</div>
<div id="noRecord" style="display:none">
	<table class="MessageBox" align="center" width="380" >
	  <tr>
	    <td class="msg info">
	      <div class="content" style="font-size:12pt">无符合条件的手机短信记录</div>
	    </td>
	  </tr>
	</table>
	<table align="center">
	  <tr>
	    <td>
	      <div style="width:100%"><input type="button" class="BigButton" value="返回" onClick="location='index.jsp';"></div>
	    </td>
	  </tr>
	</table>
</div>
</body>
</html>
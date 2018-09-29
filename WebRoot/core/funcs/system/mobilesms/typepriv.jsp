<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<head>
<title>修改界面设置</title>
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link  rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/tab.js" ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/cmp/ExchangeSelect1.0.js"  ></script>
<script type="text/javascript">
var selectbox;
var selectValue = "cancel";

function  doInit(){
  var typePrivStr = "";
  var seqId = "";
  var url = "<%=contextPath%>/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/getTypePriv.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    $("seqId").value = rtJson.rtData.seqId;
    typePrivStr = rtJson.rtData.typePriv;
  }else {
    alert(rtJson.rtMsrg); 
  };
  
  var url = "<%=contextPath%>/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/getTypePrivConfig.act?typePriv="+typePrivStr;
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    var  selected  =  rtJson.rtData.selected;
    var  disselected  =  rtJson.rtData.disselected;
    selectbox = new  ExchangeSelectbox({
	    containerId:'div',
	    selectedArray:selected,
	    disSelectedArray:disselected,
	    isSort:true,
	    isOneLevel:false,
	    title:'',
	    selectName:'nextProcess',
	    selectedChange:exchangeHandler
    });
  }
}

function  exchangeHandler(ids){
  selectValue = ids;
}

function submitForm(){
  var seqId = $("seqId").value;
  var url = "<%=contextPath%>/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/setTypePrivConfig.act";
  var rtJson = getJsonRs(url, "selectValue=" + selectValue + "&seqId=" + seqId);
  if (rtJson.rtState == "0") {
    window.location.href = "<%=contextPath%>/core/funcs/system/mobilesms/submit.jsp";
  }
  else{
    alert("修改未成功");
  }
}
</script>
</head>
<body onload="doInit()" topmargin="5">
<form name="form1" method="post" id="form1">
<input type="hidden" id="seqId" name="seqId" value=""/>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Small1"><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;指定模块权限</span>&nbsp;&nbsp;
    	&nbsp;指定允许使用手机短信提醒的模块    </td>
  </tr>
</table>
<br>

<div id="div" align="center"></div>
<div align="center">
<p></>点击条目时，可以组合CTRL或SHIFT键进行多选</p>
<input type="button" value="保存设置" class="BigButton" onclick="submitForm()">
</div>
</form>
</body>
</html>
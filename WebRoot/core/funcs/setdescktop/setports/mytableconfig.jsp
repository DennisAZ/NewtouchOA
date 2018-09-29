<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<title>桌面模块选择</title>
<link  rel="stylesheet"  href  =  "<%=cssPath  %>/cmp/ExchangeSelect.css">
<link  rel="stylesheet"  href  ="<%=cssPath  %>/style.css">
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/cmp/ExchangeSelect1.0.js"  ></script>
<script  type="text/javascript">
var selectbox;

function  doInit(){
  var url = "<%=contextPath%>/yh/core/funcs/setdescktop/setports/act/YHDesktopDefineAct/getDesktopConfigForAll.act";
	var rtJson = getJsonRs(url + "?pos=" + "${param.pos}");

	//$('title-span').innerHTML = "${param.pos}" == 'r'?'右侧显示桌面':'左侧显示桌面';
  if (rtJson.rtState == "0") {
      
    var selected = [];
    var disselected = [];
    var array = rtJson.rtData.selected.concat(rtJson.rtData.disselected);
    array.each(function(e, i) {
      if (',${param.selected},'.indexOf(e.value) >= 0 || e.isMustSelect) {
        selected.add(e);
      }
      else {
        disselected.add(e);
      }
    });
    
    selectbox = new  ExchangeSelectbox({
	    containerId:'div',
	    selectedArray:selected,
	    disSelectedArray:disselected,
	    isSort:true,
	    isOneLevel:false,
	    title:'桌面模块选择',
	    selectName:'nextProcess',
	    selectedChange:exchangeHandler
    });

    dialogArguments.mytable.value = "";
    dialogArguments.mytableDesc.innerHTML = "";
    selected.each(function(e,i){
      dialogArguments.mytable.value += e.value + ',';
      dialogArguments.mytableDesc.value += e.text + ',';
    });
  }
}

function  exchangeHandler(ids , text){
  dialogArguments.mytable.value = ids;
  dialogArguments.mytableDesc.value = text;
}

function submitForm(){
  window.close();
}

</script>
</head>
<body  onload="doInit()">
<div id="div"></div>
<div class="exchange-info">
<p></>点击条目时，可以组合CTRL或SHIFT键进行多选</p>
<input type="button" value="保存设置" class="BigButtonB" onclick="submitForm()">
</div>
</body>
</html>

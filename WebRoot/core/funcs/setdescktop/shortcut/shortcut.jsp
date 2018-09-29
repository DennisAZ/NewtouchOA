<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<title>Insert  title  here</title>
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/ExchangeSelect.css">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/cmp/ExchangeSelect1.0.js"  ></script>
<script  type="text/javascript">

var selectbox;
var selectValue = "cancel";

function  doInit(){
  var url = "<%=contextPath%>/yh/core/funcs/setdescktop/shortcut/act/YHShortcutAct/getFuncsAll.act";
  var rtJson = getJsonRs(url);
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
      title:'',
      selectName:'nextProcess',
      selectedChange:exchangeHandler
    });
  }
}

function  exchangeHandler(ids , text){
  dialogArguments.shortcut.value = ids;
  dialogArguments.shortcutDesc.innerHTML = text;
}

function submitForm(){
  window.close();
}
</script>
</head>
<body  onload="doInit()">
<table width="100%" cellspacing="0" cellpadding="3" border="0" class="small">
  <tbody><tr>
    <td class="Big"><img height="20" width="22" align="absmiddle" src="<%=imgPath%>/edit.gif"><span class="big3"> 菜单快捷组定义</span>
    </td>
  </tr>
</tbody></table>
<div id="div"></div>
<p></>点击条目时，可以组合CTRL或SHIFT键进行多选</p>
<input type="button" value="保存设置" class="BigButton" onclick="submitForm()">

</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/core/inc/header.jsp" %>
<head>
<title>AIP Reader</title>
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
/**
 * 加载AIP数据
 */
function loadAIP() {
  var rtJson = getJsonRs(contextPath + "/test/core/act/YHAIPAct/loadAip.act", {T_NAME: "1"});
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  }else {
    alert(rtJson.rtMsrg);
    return false;
  }
}
</script>
<SCRIPT LANGUAGE=javascript FOR=HWPostil1 EVENT=NotifyCtrlReady>
var content = loadAIP();
if (content) {
  // 控件"HWPostil1"的NotifyCtrlReady事件，一般在这个事件中完成初始化的动作
  var obj = $("HWPostil1");
  obj.ShowDefMenu = false; //隐藏菜单
  obj.ShowToolBar = false; //隐藏工具条
  obj.ShowScrollBarButton = 1;
  obj.LoadFileBase64(content);
}
</SCRIPT>
</head>
<body>
<OBJECT id=HWPostil1 style="WIDTH:800px;HEIGHT:600px" classid=clsid:FF3FE7A0-0578-4FEE-A54E-FB21B277D567 codeBase='<%=contextPath %>/test/core/aip/HWPostil.cab#version=3,0,4,8' >"
</OBJECT>
</body>
</html>
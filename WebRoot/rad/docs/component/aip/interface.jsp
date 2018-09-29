<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/core/inc/header.jsp" %>
<head>
<title>AIP Reader</title>
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/index1.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var argObj = null;
var _userName = "sys_admin";
var notes = new Array();
/**
 * 加载AIP数据
 */
function loadAIP() {
  var rtJson = getJsonRs(contextPath + "/yh/rad/docs/component/act/YHAIPAct/loadAip.act", {T_NAME: "1"});
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  }else {
    alert(rtJson.rtMsrg);
    return false;
  }
}
function call() {
  var obj = $("HWPostil1");
  //设置aip的属性
  obj.ShowDefMenu = false; //隐藏菜单
  obj.ShowToolBar = false; //隐藏工具条
  //登陆
  var vRet = obj.Login( _userName , 5, 32767, "", "");
  //取得当前文件的Base64一般用于保存Base64码
  var content = obj.GetCurrFileBase64();
}
</script>
<SCRIPT LANGUAGE=javascript FOR=HWPostil1 EVENT=NotifyCtrlReady>
var content = loadAIP();
if (content) {
  // 控件"HWPostil1"的NotifyCtrlReady事件，一般在这个事件中完成初始化的动作
  var obj = $("HWPostil1");
  obj.ShowDefMenu = false; //隐藏菜单
  obj.ShowToolBar = false; //隐藏工具条
  obj.JSEnv = 1;
  obj.ShowScrollBarButton = 1;
  obj.LoadFileBase64(content);
  
}
</SCRIPT>
</head>
<body>
<OBJECT id=HWPostil1
 style="WIDTH:800px;HEIGHT:500px" 
 classid=clsid:FF3FE7A0-0578-4FEE-A54E-FB21B277D567 
 codeBase='<%=contextPath %>/rad/docs/component/aip/HWPostil.cab#version=3,0,6,8' >
</OBJECT>
</body>
</html>
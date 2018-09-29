<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%
 String seq_id=request.getParameter("seq_id");
String flow_id=request.getParameter("flow_id");
%>
<head>
<title>编辑打印模板</title>

<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/ExchangeSelect.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/ExchangeSelect.css">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/cmp/ExchangeSelect1.0.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%><%=moduleContextPath %>/flowtype/setprint/js/new.js"  ></script>

<script type="text/javascript">
var seq_id=<%=seq_id%>;
var flow_id=<%=flow_id%>;
var argObj = null;
function  doInit(){
	
    var  selected  =  [];
    var  disselected  =  [];
         disselected=getDisSelected();
         selected=getSelected();
         setSelectValue(selected);
         disselected=dealDis(selected,disselected);
    new  ExchangeSelectbox({containerId:'showSelect'
        ,selectedArray:selected
        ,disSelectedArray:disselected  
        ,title:'步骤设置'
        ,isSort:true
        ,selectName:'nextProcess'
        ,selectedChange:exchangeHandler});  
	}
	function  exchangeHandler(ids){
	    $('selectValue').value  =  ids;
	}
 
function setHWPostil1() {
  var obj = $("HWPostil1");
  var h = document.viewport.getDimensions().height - 128 ;
  obj.style.height = h + "px";
  obj.style.width = "100%";
}

</script>
<SCRIPT LANGUAGE=javascript FOR=HWPostil1 EVENT=NotifyCtrlReady>
var content = loadAIP();
if (content) {
  // 控件"HWPostil1"的NotifyCtrlReady事件，一般在这个事件中完成初始化的动作  var obj = $("HWPostil1");
  obj.ShowDefMenu = false; //隐藏菜单
  obj.ShowToolBar = false; //隐藏工具条
  obj.ShowScrollBarButton = 1;
//  obj.InDesignMode = true;
  obj.LoadFileBase64(content);
  setHWPostil1();
}
</SCRIPT>

<SCRIPT LANGUAGE=javascript FOR=HWPostil1 EVENT=NotifyLineAction(lPage,lStartPos,lEndPos)>
NotifyLineAction(lPage,lStartPos,lEndPos);
</SCRIPT>


</head>
<body onLoad="doInit();">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/print.gif" align="absMiddle" align="absmiddle"><span class="big3">编辑打印模板</span><span class="small" style="color:red;">(在打印模板区域内拖动鼠标以添加映射区域)</span>
    </td>
    <td align=right>
      <input type="button" class="BigButtonD" value="设置模板为可编辑模式" onClick="addField()" />&nbsp;
      <input type="button" class="BigButtonA" value="保存" onClick="saveFile();"/>&nbsp;
      <input type="button" class="BigButtonA" value="返回" onClick=" history.go(-1) "/>
    </td> 
  </tr>
</table>
<table class="TableList" width="100%" align="center">
     <tr>
      <td class="TableContent" width=100>模板名称</td>
      <td class="TableData" ><input type="text" size=25 class="BigInput" id=tName name="tName" value=""></span>&nbsp;<input type="hidden" name="FLOW_ID" value="">
      </td>
    <td class="TableContent" width=100>模板类别</td>
    <td class="TableData">
      <select name="tType" class="SmallSelect" id="tType">
       <option value="1" >打印模板</option>
       <option value="2" >手写呈批单</option>
      </select>
    </td>
    </tr>
    <tr>
      <td colspan=4 class="TableContent"><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle"> 使用步骤设置  <a href="javascript:showOrHide('prcs');">显示/隐藏 </a></td>
    </tr>
    <tr align="center" id='prcs' style="display:none">
	    <td colspan='4'>
	    <input type="hidden" id="selectValue"/>
	    <div id="showSelect"></div>
	    </td>
    </tr>
 


</table>

<OBJECT id=HWPostil1 style="WIDTH:0;HEIGHT:0" classid=clsid:FF3FE7A0-0578-4FEE-A54E-FB21B277D567 codeBase='<%=contextPath %>/core/cntrls/HWPostil.cab#version=3,0,7,0' >"
</OBJECT>

<div id="overlay"></div>
<div id="setFrm" class="ModalDialog" style="border:none;">
  <div id="setFrm_body" class="body bodycolor" style="padding:0px;">
    <input type="hidden" name="FIELD_STR" id="FIELD_STR">
    <iframe frameborder=0 style="width:320px;height:300px"  id="frm_field" src="set_filed.jsp?flow_id=<%=flow_id %>"></iframe>
  </div>
</div>


</body>
</html>
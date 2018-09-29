<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%@ page import="yh.core.funcs.doc.act.YHFlowFormPrintAct" %>
<%
Cookie[] myCookie = request.getCookies();
String flowView = "";
for(int i = 0; i < myCookie.length ; i++){
  Cookie tmp= myCookie[i];
  if(tmp != null && tmp.getName().equals("flowViewCookie")){
    flowView = tmp.getValue();
    break;
  }
}
if(flowView == null || "".equals(flowView)){
  flowView = "1234";
}
String sRunId = request.getParameter("runId");
String sFlowId = request.getParameter("flowId");
String host = request.getServerName() + ":" + request.getServerPort() + request.getContextPath() ;
YHFlowFormPrintAct act = new YHFlowFormPrintAct();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/root.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/core/ext/resources/css/ext-all.css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/adapter/prototype/ext-prototype-adapter.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/ext-all.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/frame/ux/Window.js"></script>
<style type="text/css">
.title {  font-family: "宋体"; font-size: 18pt; font-weight: bold}
</style>
<style media="print" type="text/css"> 
.Noprint{display:none;}
 
</style>
<script type="text/javascript" src="/yh/core/js/jquery/jquery-1.4.2.js"></script>
<script language="JavaScript"> 
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var runId = "<%=sRunId %>";
var flowId = "<%=sFlowId %>";
var oldFlowView = "<%=flowView %>";
var delFlag = 0;
var title  = "";
function doInit(){
  window.onresize = setBody;
  var url = contextPath + "<%=moduleSrcPath %>/act/YHFlowFormPrintAct/getFormPrintInfo.act";
  var json = getJsonRs(url , "runId=" + runId);
  if (json.rtState == '0') {
    title = document.title = "公文登记信息打印-"  +  json.rtData.docTitle;
    $('docTitle').update( json.rtData.docTitle);
      //+ json.rtData.runName;
    if (json.rtData.delFlag ==  1) {
      adj_del();
      $("print_body").onscroll = adj_del;
    }
  }
  setBody();
  setTemp(json.rtData.aipFile);
}

function setTemp(aipFile){
   for (var i = 0 ;i < aipFile.length ;i++) {
      var aip = aipFile[i];
      jQuery("#templetePrint").append("<option value='"+aip.value+":1'>"+aip.name+"</option>");
   }
	var url =  contextPath + "<%=moduleSrcPath %>/act/YHFlowPrintAct/getTempOptionAct.act";
	  var json = getJsonRs(url , "flowId=<%=sFlowId%>&runId=<%=sRunId%>" );
	  if(json.rtState == '0'){
	    var data = json.rtData;
	    for(var i=0;i<data.length;i++){
            jQuery("#templetePrint").append("<option value='"+data[i].seqId+":2'>"+data[i].tName+"</option>"); 
         }
		   
	  }
	
}

function select_model(val)
{
  if(val==0)
  {
    $("print_frm").style.display="block";
    $("print_temp").style.display="none";
    $('btnSave').hide();
    var obj = $("HWPostil1");
    obj.style.width="0";
    obj.style.height="0";
  }
  else
  {
    $("print_frm").style.display="none";
    $("print_temp").style.display="block";
    $('btnSave').show();
    var obj = $("HWPostil1");
    obj.ShowDefMenu = false; //隐藏菜单
    obj.ShowToolBar = false; //隐藏工具条

    obj.TextSmooth = 1 //字体平滑 
    obj.ShowScrollBarButton = 1;
    obj.InDesignMode = true;
    obj.style.width="100%";
    obj.style.height="100%";
    var types = val.split(":");
    if (types[1] ==  "1") {
      var URL =  "http://<%=host %><%=moduleSrcPath %>/act/YHFlowPrintAct/getAipFile.act?module=workflow&attachmentId=" + types[0];
      vRet = obj.LoadFile(URL);
    } else {
      var content=getAipContent(types[0]);
      obj.LoadFileBase64(content);
    }
    setValue(obj);
  }
}

  function setValue(obj){
	  var rtJson = getJsonRs( contextPath + "<%=moduleSrcPath %>/act/YHFlowPrintAct/getFlowItemData.act", "runId="+runId+"&flowId="+flowId);
	    if (rtJson.rtState == "0") {
	      var data=rtJson.rtData;
	      for(var i=0;i<data.length;i++){
		     // alert(data[i].name+":"+data[i].data);
	    	  obj.SetValue(data[i].name,data[i].data);
	    	  
		      }
	    }else {
	      alert(rtJson.rtMsrg);
	      
	    }
	  }


function getAipContent( seqId ){
	var rtJson = getJsonRs( contextPath + "<%=moduleSrcPath %>/act/YHFlowPrintAct/loadAip.act", "seq_id="+seqId);
	  if (rtJson.rtState == "0") {
	    var data=rtJson.rtData;
	     bindJson2Cntrl(data);
	    return data.content;
	  }else {
	    alert(rtJson.rtMsrg);
	    return false;
	  }
}


function flow_view() {
  flowView = GetView();
  document.frames["print_frm"].window.location = contextPath + "<%=moduleContextPath %>/flowrun/list/print.jsp?runId="+ runId +"&flowId="+ flowId +"&flowView=" + flowView;
}
function flowWindow()
{
  var flowView = GetView();
  var url = contextPath +  "<%=moduleContextPath %>/flowrun/list/print.jsp?runId="+ runId +"&flowId="+ flowId +"&flowView=" + flowView;
  window.open(url);
  //showModalWindow(url,title , "preview" + flowId ,800 , 600 , false);
}
function printPreview()
{
  var flowView = GetView();
  var url = contextPath +  "<%=moduleContextPath %>/flowrun/list/print.jsp?runId="+ runId +"&flowId="+ flowId +"&flowView=" + flowView + "&printView=1";
  window.open(url);
  // showModalWindow(url, title + '打印预览' , "preview" + flowId ,800 , 600 , false);
}
function GetView() {
  var FLOW_VIEW="";
  if($("CHECK1").checked)
     FLOW_VIEW+="1";
  if($("CHECK2").checked)
     FLOW_VIEW+="2";
  if($("CHECK3").checked)
     FLOW_VIEW+="3";
  if($("CHECK4").checked)
     FLOW_VIEW+="4";
  return FLOW_VIEW;
}
function myprint(){
  if ($("templetePrint").value == "0" ) {
	document.frames["print_frm"].focus();
    document.frames["print_frm"].window.print();
  } else {
    var obj = $("HWPostil1");
   obj.PrintDoc(1,1);	
  }	
}
function setBody(){
  var ajustHeight = $('print_control').offsetHeight + $('print_view').offsetHeight;
  var iFrame =  $("print_frm");
  var width = document.viewport.getDimensions().width + "px";
  var height = ((document.viewport.getDimensions().height - ajustHeight) - 5) + "px";
  iFrame.style.width = width;
  iFrame.style.height = height;
  var iFrame2 = $('print_temp');
  if (iFrame2) {
    iFrame2.style.width = width;
    iFrame2.style.height = height;
  }
}

function adj_del() {
  var del = document.frames["print_frm"].document.getElementById("del_flag");
  del.style.display = "";
  del.style.top = $("print_body").scrollTop+200;
  del.style.left = (document.body.clientWidth-128)/2;
}
function formExport(ext)
{
  var FLOW_VIEW = GetView();
  var url = contextPath + "<%=moduleSrcPath %>/act/YHFlowFormPrintAct/getWordAndHtml.act?flowId=" + flowId + "&runId=" + runId + "&flowView=" + FLOW_VIEW + "&ext=" + ext;
  window.open(url,"","");
}
function aipSaveTo()
{
    var obj = $("HWPostil1");
    obj.saveTo("","",0);
}

</script>
<SCRIPT LANGUAGE=javascript FOR=HWPostil1 EVENT=NotifyCtrlReady>
// 控件"HWPostil1"的NotifyCtrlReady事件，一般在这个事件中完成初始化的动作

var obj = $("HWPostil1");
obj.ShowDefMenu = false; //隐藏菜单
obj.ShowToolBar = false; //隐藏工具条

obj.TextSmooth = 1 //字体平滑 
obj.ShowScrollBarButton = 1;
$("print_temp").style.display="none";
</SCRIPT>

</head>
<body style="padding-bottom: 0px; margin: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;" SCROLL=no onload="doInit()">
<div id="print_view" class="Noprint" style="height:30px;margin:0;padding:0px 0 3px 0">
<table class=small width=100% height=30 border=0 cellpadding="4" cellspacing="0" >
<tr class="TableControl">
	<td align="left" nowrap><b>
		公文标题：<span id="docTitle"></span>
   <input type="checkbox" name="CHECK1" id="CHECK1" onclick="flow_view();" <%=(flowView.indexOf("1") == -1 ? "" : "checked") %>> <label for="CHECK1" style="cursor:pointer">登记信息</label>
   <input type="checkbox" name="CHECK2" id="CHECK2" onclick="flow_view();" <%=(flowView.indexOf("2") == -1 ? "" : "checked") %>> <label for="CHECK2" style="cursor:pointer">公共附件</label>
   <input type="checkbox" name="CHECK3" id="CHECK3" onclick="flow_view();" <%=(flowView.indexOf("3") == -1 ? "" : "checked") %>> <label for="CHECK3" style="cursor:pointer">会签与点评</label>
   <input type="checkbox" name="CHECK4" id="CHECK4" onclick="flow_view();" <%=(flowView.indexOf("4") == -1 ? "" : "checked") %>> <label for="CHECK4" style="cursor:pointer">流程图</label></b>
  </td>
  <td align="right" nowrap>
</td>
</tr>
</table>
</div>
	
<div id="print_body">
<iframe id="print_frm" frameborder=0
 src="<%=contextPath%><%=moduleContextPath %>/flowrun/list/print.jsp?runId=<%=sRunId %>&flowId=<%=sFlowId %>&flowView=<%=flowView %>" width=100% height=100%></iframe>
<% if (act.getAip(request , response)) { %>
<div id="print_temp" >
<OBJECT id=HWPostil1 style="WIDTH:800px;HEIGHT:600px" classid=clsid:FF3FE7A0-0578-4FEE-A54E-FB21B277D567 codeBase='<%=contextPath %>/core/cntrls/HWPostil.cab#version=3.0.7.0' >"
</OBJECT>
</div> 
<% } %>
</div> 
<div class="Noprint" id="print_control" style="padding-bottom: 3px; margin: 0px; padding-left: 0px; padding-right: 0px; height: 30px; padding-top: 3px;">
<TABLE class=small border=0 cellSpacing=0 cellPadding=4 width="100%" height=30>
<TBODY>
<TR class=TableControl>
<TD noWrap align=left>
<B>&nbsp;相关操作：</B>
  &nbsp;选择打印模板：  <select name="templetePrint" id="templetePrint" class="SmallButton"  onChange="select_model(this.value)">
  <option value="0">原始登记信息</option>
  </select>

 <input type="button" id="btnSave" style="display:none" value="保存" class="SmallButton" onclick="aipSaveTo()">
  &nbsp;<input value="打印" onclick="myprint()" type="button" class="SmallButtonW"/>
<input value="打印预览"  onclick="printPreview()" type="button" class="SmallButtonW"/>
<input type="button" value="新窗口" class="SmallButtonW"  onclick="flowWindow();"title="在新窗口显示表单"> 
<input type="button" value="导出Word" class="SmallButtonC"  onclick="formExport('doc')">
<input type="button" value="导出HTML" class="SmallButtonC" onclick="formExport('html')">
<input type="button" value="关闭" class="SmallButtonW" onclick="window.close();"> &nbsp;&nbsp;

</TBODY></TABLE>
</div>
</body>
</html>
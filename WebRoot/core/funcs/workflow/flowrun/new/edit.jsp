<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String flowId = request.getParameter("flowId");  
String sortId = request.getParameter("sortId");
if (sortId == null) {
  sortId = "";
}
String skin = request.getParameter("skin");
String skinJs = "messages";
if (skin != null && !"".equals(skin)) {
  skinJs = "messages_" + skin;
} else {
  skin = "";
}
String isMenu = request.getParameter("isMenu");
if (isMenu == null) {
  isMenu = "";
}
%>
<%@ include file="/core/inc/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建工作</title>
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
   <script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/skin.js"></script>
<script type="text/javascript">
var flowId = "<%=flowId%>";
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";
var formId;
var requestUrl  = contextPath + "/yh/core/funcs/workflow/act/YHFlowRunAct";
function doInit(){
  skinObjectToSpan(flowrun_new_edit);
  //异步请求,取得数据json格式为:{rtState:"0|1", rtMsrg:"some message", rtData:{formId:23,flowName:'ddd',runName:'请假申请(2010-01-20 14:45:45)',prcsList:[{prcsNo:'1',prcsName:'请假申请',prcsTo:'2,'},{prcsNo:'1',prcsName:'请假申请',prcsTo:'2,'}]}}
  var url = requestUrl + "/getNewMsg.act";
  var json = getJsonRs(url , "flowId=" + flowId );
  //如果成功
  if(json.rtState == '0'){
    //取出流程名,文号并且赋值
    $('flowNameTd').innerHTML = json.rtData.flowName + " - 名称/文号";
    formId = json.rtData.formId;
    $('runName').value = json.rtData.runName;
	//取出列表
	$('flowTypeDiv').show();
	var autoEdit = json.rtData.autoEdit;
	//不允许修改
	if (autoEdit == 0){
	  setReadOnly();
	//允许在自动文号前加前缀
	} else if (autoEdit == 2) {
	  setReadOnly();
      $('runNameLeftDiv').show();
	} else if (autoEdit == 3) {
	  setReadOnly();
      $('runNameRightDiv').show();
	} else if (autoEdit == 4) {
	  setReadOnly();
	  $('runNameLeftDiv').show();
      $('runNameRightDiv').show();
    } 
	var flowType = json.rtData.flowType;
	if (json.rtData.introduction) {
	  $('introduction').update(json.rtData.introduction);
      $('introductionTBody').show();
    } else if (flowType == 2) {
      $('prcsListDiv').hide();
    }
	
	if (flowType  ==  1) {
	  if (json.rtData.introduction) {
		$('introduction').update(json.rtData.introduction);
		$('introductionTBody').show();
	  }
	  $('prcsHeader').show();
	  $('tableControl').show();
	  var prcsList = json.rtData.prcsList;
		//如果列表有数据		if(prcsList.length >0 ){
		  for(var i = 0 ;i < prcsList.length ;i ++){
			var tmp = prcsList[i];
		    var prcsNo = tmp.prcsNo; //序号
		    var prcsName = tmp.prcsName;//流程名
		    var aPrcsTo = tmp.prcsTo.split(",");//下一步
		    var prcsToStr = "";
			   //循环prcsTo数组,加上→,以及零变成结束
		    for(var j = 0 ;j < aPrcsTo.length ; j++){
		    if(aPrcsTo[j]){//不为空
	
			  if(aPrcsTo[j] == '0'){
			    prcsToStr += "→结束,";
			  }else{
			    prcsToStr += "→" + aPrcsTo[j] + ",";
			  }
		 	}
		  }
		  prcsToStr = prcsToStr.substring(0 , prcsToStr.length - 1);
		  var tr = document.createElement("tr");
		  $('listBody').appendChild(tr); //添加行
		  var td = document.createElement("td");//添加列
		  td.align = 'center';
		  tr.appendChild(td);
		  td.innerHTML = prcsNo;
		  td = document.createElement("td"); 
		  td.align = 'center'; 
		  tr.appendChild(td);
		  td.innerHTML = prcsName;
		  td = document.createElement("td");  
		  td.align = 'center';
		  tr.appendChild(td);
		  td.innerHTML = prcsToStr;
		  if(i%2 == 0){
		    tr.className = "TableLine1";
		  }else{
			tr.className = "TableLine2";
		  }
		  }
		}else{
		//没有数据,隐藏列表,显示提示信息
		  $('prcsListDiv').hide();
		  $('noPrcsListDiv').show();
	    }
	}
  }else{
    //如果不成功打印提示信息
    //清空body元素
    document.body.innerHTML = "";
    //创建div,设置div align,内容
    var div = document.createElement("div");
    document.body.appendChild(div);
    div.align = 'center';
    div.innerHTML = json.rtMsrg;
  }
}
function view_graph(FLOW_ID)
{
  myleft=(screen.availWidth-800)/2;
  window.open("../list/viewgraph/index.jsp?flowId="+FLOW_ID,"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=800,height=500,left="+myleft+",top=50");
}
function view_form(formId)
{
  myleft=(screen.availWidth-800)/2;
  window.open("../list/formView.jsp?formId=" + formId,"form_view","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=800,height=600,left="+myleft+",top=50");
}
function createWork(){
  if(!$('runName').present()){
    alert('名称/文号不能为空');
    $('runName').focus();
    return ;
  }
  var url = requestUrl + "/createNewWork.act";
  var json = getJsonRs(url , $('workForm').serialize());
  if(json.rtState == "0"){
    <% if ("".equals(isMenu)) { %>
      parent.location = contextPath + "/core/funcs/workflow/flowrun/list/inputform/index.jsp?skin="+ skin +"&sortId="+sortId+"&runId=" + json.rtData + "&flowId=" + flowId + "&prcsId=1&flowPrcs=1&isNew=1";
    <% } else { %>
      location.href = contextPath + "/core/funcs/workflow/flowrun/list/inputform/index.jsp?skin="+ skin +"&sortId="+sortId+"&runId=" + json.rtData + "&flowId=" + flowId + "&prcsId=1&flowPrcs=1&isNew=1";
    <%  } %>
  }else{
    alert(json.rtMsrg);
  }
}
function setReadOnly() {
  $('runName').readOnly = true;
  $('runName').className = "BigStatic";
  $('orgClear').hide();
}
var reg = /['"]/g;
</script>
</head>

<body  topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">

  <tr>
    <td class="Big"><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle">&nbsp;<span class="big3" id="span1"></span><br>
    </td>
  </tr>
</table>

<br>
<form id="workForm" action="<%=contextPath %>/yh/core/funcs/workflow/act/YHFlowRunAct/createNewWork.act" method="post" target="_parent" name="workForm">
  
<table border="0" align="center" width="500"  class="TableBlock" >
    <tr class="TableHeader" height=20  style="font-size:10pt">
    <td colspan=2 id="flowNameTd" align=center></td>
    </tr>
    <tr>
      <td class="TableData">
        <div id="runNameLeftDiv" style="display:none">前缀：<input  onkeyup="value=value.replace(reg,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(reg,''))"  type=text value="" size="50" id="runNameLeft" name="runNameLeft"/></div>
        <textarea name="runName"   id="runName"  rows=2 cols=44 class=BigInput> </textarea>
        <a href="javascript:;"  id="orgClear" class="orgClear" onclick="$('runName').value = ''">清空</a>
        <div id="runNameRightDiv" style="display:none;padding-top:3px">后缀：<input  onkeyup="value=value.replace(reg,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(reg,''))"  type=text value="" size="50"  id="runNameRight" name="runNameRight"/></div>
      </td>

    </tr>
    
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type='hidden' value="" name="MENU_FLAG">
        <input type='hidden' value="<%=flowId%>" name="flowId">
        <input type='hidden' value="1" name="autoNew">
        <input type="button" onclick="createWork()"  value="新建并办理" class="SmallButtonC">&nbsp;&nbsp;
       <input type="button"  value="返回" class="SmallButtonB" onClick="location='<%=contextPath %>/core/funcs/workflow/flowrun/new/flowtop.jsp?sortId=<%=sortId %>&skin=<%=skin %>'">
       <input type="button" style="display:none"  value="清空"  class="BigButton" onClick="$('runName').value = ''">
      </td>
    </tr>
</table>

 </form>
<br>
<div id="flowTypeDiv" style="display:none">
<table id="prcsListTable" border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle"><span class="big3"> 流程说明及步骤列表</span><br>

    </td>
  </tr>
</table>
<br>

<div id="prcsListDiv" align=center>

  <table border="0" width="500" class="TableList" align="center">
  <tbody id="introductionTBody" style="display:none">
  <tr align="center" height="20px" style="font-size:10pt" class="TableHeader">
      <td align="center" colspan="3">流程说明</td>
    </tr>
    <tr align="center" height="20px" style="font-size:10pt" class="TableLine1">
      <td align="center" colspan="3" id="introduction"></td>
    </tr>
    </tbody>
    <tr id="prcsHeader"  align="center" height="20px" style="font-size:10pt;display:none" class="TableHeader">
      <td align="center" >步骤序号</td>
      <td align="center">名称</td>

      <td align="center" >流程可选方向</td>
    </tr>
    <tbody id="listBody"></tbody>
    <tr class="TableControl" id=tableControl style="display:none">
      <td align="center" colspan=3>
      	<input type="button"  value="查看流程设计图" class="BigButtonC" name="back" onClick="viewGraph(<%=flowId %>)">&nbsp;&nbsp;
      	<input type="button"  value="查看表单模板" class="BigButtonC" name="back" onClick="viewForm(formId)">
      </td>

    </tr>
</table>
</div>
<div  id="noPrcsListDiv" align=center style="display:none">
<table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td class="msg info" >还没有定义流程步骤</td>
        </tr>
    </tbody>
</table></div>

</div>
</body>
</html>
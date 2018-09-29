<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%

String search = request.getParameter("search");
if (search == null) {
  search = "";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程管理</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript">
var search = '<%=search%>';
function revise(){
  var flowId = arguments[2];
  location = "examineFlow.jsp?flowId=" + flowId;
}
function clone(){
  var flowId = arguments[2];
  var title = "流程克隆";
  var url = contextPath + "/core/funcs/workflow/flowtype/clone/index.jsp?flowId="+flowId;
  showModalWindow(url , title , "clone" ,800,600 , false , function () {parent.leftFrame.location.reload();location.reload();});
}

function clear(){
  var flowId = arguments[2];
  if(confirm("确定清空依托于该流程的所有工作数据吗?"))
  {
    var url =  contextPath + "/yh/core/funcs/workflow/act/YHFlowTypeAct/empty.act";
    var json = getJsonRs(url, "flowId=" + flowId);
    if(json.rtState == '0'){
      alert(json.rtMsrg);
      parent.leftFrame.location.reload();
      location.reload();
    }else{
      alert(json.rtMsrg);
    }
  }
}
function del(){
  var flowId = arguments[2];
  if(confirm("确定删除吗？\n这将删除以下数据：\n1,流程描述与步骤设置。\n2,依托于该流程的所有工作。"))
  {
    var url =  contextPath + "/yh/core/funcs/workflow/act/YHFlowTypeAct/delFlowType.act";
    var json = getJsonRs(url, "flowId=" + flowId);
    if(json.rtState == '0'){
      alert(json.rtMsrg);
      parent.leftFrame.location.reload();
      location.reload();
  }else{
      alert(json.rtMsrg);
    }
  }
}
function exportFlowType(){
  var flowId = arguments[2];
  window.open(contextPath + "/yh/core/funcs/workflow/act/YHFlowExportAct/exportFlow.act?flowId=" + flowId);
}
function inport(){
  var flowId = arguments[2];
  location= 'import.jsp?flowId=' + flowId;
}

function doInit(){
  doSearch(search);
}
function doSearch(searchKey) {
  $('key').update(searchKey);
  $("loading").innerHTML = "<img src='"+ imgPath +"/loading.gif' height='20' width='20' align='absMiddle'> 加载中，请稍候……";
  $('listDiv').update("");
  $('noData').hide();
  if (!searchKey) {
    $('noDataMsg').update("请输入关键字进行检索！");
    $('noData').show();
  } else {
    loadData(searchKey);
  }
  $("loading").innerHTML = "";
}
function loadData(searchKey) {
  var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowTypeCorAct/search.act";
  var getList = getJsonRs(url , "searchKey=" + searchKey);
  if(getList.rtState == '1'){
    alert(getList.rtMsrg);
    return ;
  }
  flowJson = getList.rtData.flowList;
  count = getList.rtData.workCounts;

  if(flowJson.length > 0){
	var table = new Element('table',{ "width":"100%" ,'class':'TableList'}).update("<tbody id='tbody'><tr class='TableHeader' style='font-size:10pt'><td  align='center' width='50px'>序号</td><td align='center'>流程</td><td align='center'>类型</td><td align='center'>表单</td><td align='center'>工作数</td><td align='center'>管理</td><tbody>");
    table.align = 'center';
	$('listDiv').appendChild(table);
	for(var i = 0 ; i < flowJson.length ;i++){
	  var flow = flowJson[i];
	  if(flow.flowType == '1'){
	    name = "<a href=\"javascript:openDesign( '" +flow.seqId +"')\">" + flow.flowName + "</a>";
	    design = "<a href=\"javascript:openDesign( '" +flow.seqId +"')\">流程设计器</a>";
	  }else{
        name = flow.flowName;
	  }
	  var setProperty = "main.jsp?flowId=" + flow.seqId;
	  var tmpArray = new Array();
	  tmpArray[0] =  "<td align='center'>";
	  tmpArray[1] =  flow.flowNo;
	  tmpArray[3] =  "</td><td  align='center'>";
	  tmpArray[4] =  name;
	  tmpArray[5] = "</td><td align='center'>";
	  tmpArray[6] =  (flow.flowType == '1' ? "固定" : "自由");
	  tmpArray[7] =   "</td><td align='center'>";
	  tmpArray[8] =  "<a href='javascript:viewForm(" +flow.formId+ ")'>预览</a></td><td align='center'>";
	  
	  tmpArray[9] =  count[i].workCount + "/" + count[i].delCount;
	  tmpArray[10] =  "</td><td align='left'>&nbsp;&nbsp;&nbsp;&nbsp;";
	  tmpArray[11] =  "<img src='"+ imgPath +"/edit.gif'/><a href='"+ setProperty;
	  tmpArray[12] =  "'>属性设置</a>&nbsp;&nbsp;&nbsp;&nbsp;";
	  tmpArray[13] =    (flow.flowType == '1' ? "<img src='"+ imgPath +"/arrow_down.gif'/>" + design + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
	  	   : "<img src='"+ imgPath +"/node_user.gif'/><a href='setNewPriv.jsp?flowId="+ flow.seqId +"&sortId="+ flow.flowSort + "'>新建权限</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
	  tmpArray[14] =   "<a href='#' id='more-" + flow.seqId + "' onmouseover='createDiv(event,\""+ flow.seqId +"\",\""+ flow.isSystem +"\")'>更多<img align='absMiddle' src='"+ imgPath +"/menu_arrow_down.gif'/></a>&nbsp;</td>";
    var tr = new Element('tr',{'font-size':'10pt'});
      
  	if(i%2 == 0){
		  tr.className = "TableLine2";
 	  }else{
		  tr.className = "TableLine1";
	  }
 	  tr.onmouseover = function(){
 	    this.style.backgroundColor = '#D9E5F1';
 	  }
 	  tr.onmouseout = function(){
 		  this.style.backgroundColor = '';
 	  }
	  table.firstChild.appendChild(tr);
	  
	  tr.update(tmpArray.join("")); 

	}
  }else{
    //提示
    $('noDataMsg').update("没有所搜到结果！");
    $('noData').show();
  }
}
function createDiv(event , flowId , isSystem){
  menuData = [//{ name:'<div  style="padding-top:5px;margin-left:10px">校验<div>',action:revise ,extData:flowId}
  { name:'<div style="padding-top:5px;margin-left:10px">克隆<div>',action:clone,extData:flowId}
  ,{ name:'<div  style="padding-top:5px;margin-left:10px">清空<div>',action:clear,extData:flowId}
  ,{ name:'<div  style="padding-top:5px;margin-left:10px">导出<div>',action:exportFlowType,extData:flowId}
  ,{ name:'<div  style="padding-top:5px;margin-left:10px">导入<div>',action:inport,extData:flowId}
  ];
  if (isSystem == '0') {
    menuData.push({ name:'<div  style="padding-top:5px;margin-left:10px">删除<div>',action:del,extData:flowId});
  }
  var divStyle = {border:'1px solid #69F',width:'100px',position :'absolute',backgroundColor:'#FFFFFF',fontSize:'10pt',display:"block"};
  var menu = new Menu({bindTo:$('more-' + flowId) , menuData:menuData , attachCtrl:true},divStyle);
  menu.show(event);
}
function openDesign(id){ 
  openFlowDesign(id);
}

</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_open.gif" align="absmiddle">&nbsp;<span class="big3">流程检索 - 关键字:<span id="key"></span>&nbsp;</span><span class="small" id="loading"></span><br>
    </td>
  </tr>
</table>

<div id="listDiv"></div>
<div id="noData"  align=center style="display:none">
<table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td class="msg info" id="noDataMsg">没有所搜到结果！</td>
        </tr>
    </tbody>
</table>

</div>
</body>
</html>
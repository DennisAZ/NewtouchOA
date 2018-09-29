<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String sortId = request.getParameter("sortId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>表单管理</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript">


var sortId = '<%=sortId%>';
function doInit(){
  var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowFormAct/listBySort.act";
  var getList = getJsonRs(url , "sortId=" + sortId);
  if(getList.rtState == '1'){
    alert(getList.rtMsrg);
    return ;
  }
  flowJson = getList.rtData.flowList;
  if(flowJson.length > 0){
	var table = new Element('table',{ "width":"98%" , 'class':'TableList'}).update("<tbody id='tbody'><tr class='TableHeader'   style='font-size:10pt'><td  align='center' width='40%'>表单名称</td><td align='center'>操作</td><tbody>");
    table.align = 'center';
	$('listDiv').appendChild(table);
	for(var i = 0 ; i < flowJson.length ;i++){
	  var form = flowJson[i];
      if (form) {
  	  name = "<a href=\"javascript:openFormDesign(" + form.seqId + " , "+sortId+");\">" + form.formName + "</a>";
  	  design = "<a href=\"javascript:openFormDesign(" + form.seqId + ", "+sortId+");\">表单设计器</a>";
  	
  	  var setProperty = "main2.jsp?seqId=" + form.seqId;
        var trString = "<td align='left'>"
    	  	+ name  + "</td><td align='center'>" 
    	  	+ "<a href='edit.jsp?seqId=" + form.seqId + "'>编辑名称</a>&nbsp;&nbsp;&nbsp;&nbsp;"
    	  	+ design + "&nbsp;&nbsp;&nbsp;&nbsp;"
    	  	+ "<a href=\"javascript:viewForm(" + form.seqId + ");\">预览</a>&nbsp;&nbsp;&nbsp;&nbsp;"
    	  	+ "<a href='javascript:location=\"import.jsp?formId=" + form.seqId + "&sortId=<%=sortId %>\"'>导入</a>&nbsp;&nbsp;&nbsp;&nbsp;"
    	  	+ "<a href='javascript:exportForm(" + form.seqId + ")'>导出</a>&nbsp;&nbsp;&nbsp;&nbsp;";
  	  //有删除选项
        if (!form.noDelete) {
  		trString += "<a href=\"javascript:deleteForm(" + form.seqId + ");\">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;";
    	  }
    	  trString +=  "</td>";
    	  	
        var tr = new Element('tr',{'font-size':'10pt','height':'24px'});
        
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
  	  tr.update(trString); 
      }
	}
  }else{
    //提示
    $('noData').show();
  }
}
function confirmDel() {
  if(confirm("确认要删除该表单吗？这将删除表单描述与字段设置且不可恢复！")){
    return true;
  }else{
    return false;
  }
}

function deleteForm(seqId){
  if(!confirmDel()){
    return ;
  }
  var url = "<%=contextPath %>/yh/core/funcs/workflow/act/YHFlowFormAct/deleteForm.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if(rtJson.rtState == "0"){
    parent.sort.location.reload();
    location.reload();
  }else{
    alert(rtJson.rtMsrg); 
  }
}
function exportForm(formId) {
  window.open(contextPath + '/yh/core/funcs/workflow/act/YHFlowExportAct/exportForm.act?formId=' + formId); 
}
</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_open.gif" align="absmiddle"><span class="big3"> 管理表单</span><br>
    </td>
  </tr>
</table>

<div id="listDiv"></div>
<div id="noData"  align=center style="display:none">
<table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td class="msg info">尚未定义表单</td>
        </tr>
    </tbody>
</table>

</div>
</body>
</html>
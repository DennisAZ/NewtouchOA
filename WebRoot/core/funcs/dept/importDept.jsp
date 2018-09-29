<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.data.YHCensorWords" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
  String flag = request.getParameter("flag");
  if(flag == null) {
    flag = "";
  }
  String data = request.getParameter("data");
  if(data == null) {
    data = "";
  }
  String isCount = request.getParameter("isCount");
  if(isCount == null) {
    isCount = "";
  }
  Object dataObj = request.getAttribute("contentList");
  if(dataObj == null) {
    dataObj = "";
  }
%>
<head>
<title>导入部门</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/dept/js/deptlogic.js"></script>
<script type="text/javascript">
var isCount = "<%=isCount%>";
var flag = "<%=flag%>";
var dataObj = '<%=dataObj%>';

function doInit(){
  if(flag != "1"){
    var count = 0;
    dataObj = dataObj.evalJSON();
    if(dataObj){
      var table=new Element('table',{ "width":"540","class":"TableList","align":"center"})
		  .update("<tbody id='tbody'><tr class='TableHeader' align='center'>"
				+"<td nowrap align='center' width='90'>部门名称</td>"				
				+"<td nowrap align='center' width='30'>排序号</td>"					
				+"<td nowrap align='center' width='90'>上级部门</td>"	
				+"<td nowrap align='center' width='150'>信息</td></tr><tbody>");
		  $('listDiv').appendChild(table);
  	  for(var i = 0; i < dataObj.length; i++){
  	    count++;
  	    var deptName = dataObj[i].deptName;
  	    var deptNo = dataObj[i].deptNo;
  	    var deptParent = dataObj[i].deptParent;
  	    var info = dataObj[i].info;
        var color = dataObj[i].color;
        var trColor = (count % 2 == 0) ? "TableLine1" : "TableLine2";
  	    var tr=new Element('tr',{'class':trColor});			
			table.firstChild.appendChild(tr);
			tr.update("<td align='center'><font color="+color+">"					
			    + deptName + "</font></td><td align='center'><font color="+color+">"	
				+ deptNo + "</font></td><td align='center'><font color="+color+">"
				+ deptParent + "</font></td><td align='left'><font color="+color+">"
				+ info + "</font></td>"					
			);
  	  }
    }
  }
  if(count > 0){
    $("cionDiv").style.display = 'none';
  }else{
    $("turnDiv").style.display = 'none';
  }
  if(flag != "1"){
    if(isCount != "0"){
      parent.deptListTree.location.reload();
    }
    showCntrl('listContainer');
    var mrs = " 共 "+ isCount + " 条数据导入!";
    WarningMsrg(mrs, 'msrg');
  }
  
}

</script>
</head>
<body topmargin="5" onload="doInit();">
<div id="cionDiv">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
    <tr>
      <td class="Big"><img src="<%=imgPath%>/sys_config.gif" align="absmiddle"><span class="big3"> 导入部门</span><br>
      </td>
    </tr>
  </table>

  <br>
  <br>

  <div align="center" class="Big1">
  <b>请指定用于导入的CSV文件：</b>
  <form name="form1" id="form1" action="<%=contextPath%>/yh/core/funcs/dept/act/YHDeptAct/importDept.act" method="post" enctype="multipart/form-data">
    <input type="file" id="csvFile" name="csvFile" class="BigInput" size="30">
    <input type="hidden" id="FILE_NAME" name="FILE_NAME">
    <input type="hidden" id="GROUP_ID" name="GROUP_ID" value="">
    <input type="button" value="导入" class="BigButton" onClick="importDept2();">
  </form>
  <br>
  </div>
  </div>
  <div id="listDiv" align="center"></div>
  <br>
  <div id="listContainer" style="display:none">
</div>
<div id="msrg"></div>
  <br>
  <div align="center" id="turnDiv">
   <input type="button" value="返回" class="BigButton" onClick="location='<%=contextPath%>/core/funcs/dept/importDept.jsp?flag=1';" title="返回">
</div>
</body>
</html>
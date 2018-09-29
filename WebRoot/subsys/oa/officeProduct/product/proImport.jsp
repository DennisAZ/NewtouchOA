<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
Object dataObj = request.getAttribute("contentList");
Object isCount =  request.getAttribute("isCount");
if(dataObj == null) {
	dataObj = "";
}

if(isCount == null) {
	isCount = "";
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>导入办公用品信息</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var dataObj = '<%=dataObj%>';
var isCount = "<%= isCount%>";
function doInit(){
	//alert(dataObj);
	var count = 0;
	if(dataObj){
		dataObj = dataObj.evalJSON();
		var table=new Element('table',{ "width":"640","class":"TableList","align":"center"})
				.update("<tbody id='tbody'><tr class='TableHeader' align='center'>"
				+"<td nowrap align='center' width='50'>办公用品库</td>"    
				+"<td nowrap align='center' width='50'>办公用品类别</td>"     
				+"<td nowrap align='center' width='40'>办公用品名称</td>" 
				+"<td nowrap align='center' width='40'>单价</td>" 
				+"<td nowrap align='center' width='30'>当前库存</td>" 
				
				+"<td nowrap align='center' width='150'>状态</td></tr><tbody>");
		$('listDiv').appendChild(table);
		for(var i = 0; i < dataObj.length; i++){
			count++;
			var officeDepository = dataObj[i].officeDepository;
			var officeProtype = dataObj[i].officeProtype;
			var proName = dataObj[i].proName;
			var proUnit = dataObj[i].proUnit;
			var proStockStr = dataObj[i].proStockStr;			
			
			var infoStr = dataObj[i].infoStr;
			var color = dataObj[i].color;
			var trColor = (count % 2 == 0) ? "TableLine1" : "TableLine2";
			var tr=new Element('tr',{'class':trColor});   
			table.firstChild.appendChild(tr);
			tr.update("<td align='center'><font color="+color+">"     
				+ officeDepository + "</font></td><td align='center'><font color="+color+">" 
				+ officeProtype + "</font></td><td align='center'><font color="+color+">"
				+ proName + "</font></td><td align='center'><font color="+color+">"
				+ proUnit + "</font></td><td align='center'><font color="+color+">"
				+ proStockStr + "</font></td><td align='center'><font color="+color+">"
				+ infoStr + "</font></td>"
				);
		}
	}
	if(count > 0){
		$("showFormDiv").style.display = 'none';
		$("infoStr").innerHTML = "共" + isCount + "条数据导入!";
		$("remindDiv").show();
		if(isCount>0){
			parent.frames["file_tree"].location.reload();
		}
	}
}

function downCSVTemplet(){
	var downUrl = "<%=contextPath%>/yh/subsys/oa/officeProduct/product/act/YHOfficeProductsAct";
	location.href = downUrl + "/downCSVTemplet.act";
}
function checkForm(){
	var csvFile = $("csvFile").value;
	if(csvFile.trim() == ""){
		alert("请选择要导入的文件！");
		$("csvFile").focus();
		return false;
	}
	var csvStr = csvFile.substr(csvFile.length - 3, csvFile.length);
	if(csvStr != "csv"){
		alert("错误,只能导入CSV文件!");
		$("csvFile").focus();
		$("csvFile").select();
		return false;
	}
	return true;
}
function doSubmit(){
	if(checkForm()){
		$("form1").submit();
	}
}

function returnBack(){
	window.location.href = "<%=contextPath%>/subsys/oa/officeProduct/product/proImport.jsp";
}

</script>

</head>
<body onload="doInit();">
<div id="showFormDiv">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
<tr>
      <td class="Big"><img src="<%=imgPath %>/sys_config.gif" WIDTH="18" HEIGHT="18" align="middle"><span class="big3">&nbsp;办公用品信息导入</span>
      </td>
</tr>
</table>
<br>
 <div align="center"  class="Big1">
  <b>请指定用于导入的CSV文件：</b>
  <form name="form1" id="form1" method="post" action="<%=contextPath%>/yh/subsys/oa/officeProduct/product/act/YHOfficeProductsAct/impOfficeProToCsv.act" enctype="multipart/form-data"  >
    <input type="file" name="csvFile" id="csvFile" class="BigInput" size="30">
    <input type="hidden" name="fileName" id="fileName">
    <input type="hidden" name="GROUP_ID" value="">
    <input type="button" value="导入" onclick="doSubmit();" class="BigButton">
  </form>
  请使用办公用品信息模板导入数据！<a href="#" onClick="downCSVTemplet();">办公用品信息模板下载</a>
 </div>
 </div>
<br>
<div id="listDiv" align="center"></div>
<br>
<div id="remindDiv" style="display: none">
<table class="MessageBox" align="center" width="320">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt"><span id="infoStr"></span></div>
    </td>
  </tr>
</table>
<br><center>
	<input type="button" class="BigButton" value="返回" onclick="returnBack();">
</center>
</div>


</body>
</html>
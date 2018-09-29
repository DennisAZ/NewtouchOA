<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
Object dataObj = request.getAttribute("contentList");
if(dataObj == null) {
	dataObj = "";
}
Object isCount =  request.getAttribute("isCount");
Object updateCount =  request.getAttribute("updateCount");
if(isCount == null) {
	isCount = "0";
}
if(updateCount == null) {
	updateCount = "0";
}


%>
<html>
<head>
<title>导入数据</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var dataObj = '<%=dataObj%>';
var isCount = "<%= isCount%>";
var updateCount = "<%= updateCount%>";

function doInit(){
	var count = 0;
	if(dataObj){
		dataObj = dataObj.evalJSON();
		var table=new Element('table',{ "width":"540","class":"TableList","align":"center"})
				.update("<tbody id='tbody'><tr class='TableHeader' align='center'>"
				+"<td nowrap align='center' width='50'>办公用品名称</td>"    
				+"<td nowrap align='center' width='50'>登记类型</td>"     
				+"<td nowrap align='center' width='40'>申请人</td>" 
				+"<td nowrap align='center' width='40'>数量</td>" 
				+"<td nowrap align='center' width='30'>单价</td>" 
				+"<td nowrap align='center' width='70'>操作日期</td>" 
				+"<td nowrap align='center' width='50'>备注</td>" 
				+"<td nowrap align='center' width='90'>说明</td></tr><tbody>"); 
		$('listDiv').appendChild(table);
		for(var i = 0; i < dataObj.length; i++){
			count++;
			var proName = dataObj[i].proName;
			var transFlag = dataObj[i].transFlag;
			var borrower = dataObj[i].borrower;
			var transQty = dataObj[i].transQty;
			var price = dataObj[i].price;
			
			var transDate = dataObj[i].transDate;
			var remark = dataObj[i].remark;
			var infoStr = dataObj[i].infoStr;
			var color = dataObj[i].color;
			var trColor = (count % 2 == 0) ? "TableLine1" : "TableLine2";
			var tr=new Element('tr',{'class':trColor});   
			table.firstChild.appendChild(tr);
			tr.update("<td align='center'><font color="+color+">"     
				+ proName + "</font></td><td align='center'><font color="+color+">" 
				+ transFlag + "</font></td><td align='center'><font color="+color+">"
				+ borrower + "</font></td><td align='center'><font color="+color+">"
				+ transQty + "</font></td><td align='center'><font color="+color+">"
				+ price + "</font></td><td align='center'><font color="+color+">"
				+ transDate + "</font></td><td align='left'><font color="+color+">"
				+ remark + "</font></td><td align='left'><font color="+color+">"
				+ infoStr + "</font></td>"
				);
		}
	}
	if(count > 0){
		$("impDiv").style.display = 'none';
		$("turnDiv").style.display = '';
		var mrs = "共导入 "+ isCount + " 条数据, 更新" + updateCount + "条数据!";
	  WarningMsrgLong(mrs, 'msrg');
	}
}
function WarningMsrgLong(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"410\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=" <h4 class=\"title\">信息</h4>"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  $(cntrlId).innerHTML = msrgDom;
}

function downCSVTemplet(){
	var downUrl = "<%=contextPath%>/yh/subsys/oa/officeProduct/manage/act/YHOfficeTranshistoryAct";
	location.href = downUrl + "/downCSVTemplet.act";
}

function doSubmit(){
	if(checkForm()){
		$("form1").submit();
	}
	
	
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

</script>
</head>
<body class="bodycolor" topmargin="5" onload="doInit();">
<div id="impDiv">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/sys_config.gif" align="middle"><span class="big3"> 导入办公用品登记数据</span><br>
    </td>
  </tr>
</table>
  <br>
  <form name="form1" id="form1" action="<%=contextPath%>/yh/subsys/oa/officeProduct/manage/act/YHOfficeTranshistoryAct/impTransInfoToCsv.act" method="post" enctype="multipart/form-data">
<table class="TableBlock" align="center">  
 <tr class="TableData" align="center" height="30">
   <td width="250" align="right"><b>请使用模板导入数据！</b></td>
   <td width="350" align="left">
    <a href="#" onclick="downCSVTemplet();">办公用品登记模板下载</a>
   </td>
 </tr>
 <tr class="TableData" align="center" height="30">
   <td width="250" align="right"><b>&nbsp;&nbsp;选择导入文件：</b></td>
   <td align="left" width="400">
    <input type="file" name="csvFile" id="csvFile" class="BigInput" size="30">
    <input type="hidden" name="FILE_NAME">
    <input type="hidden" name="GROUP_ID" value="">
   </td>
 </tr> 
 <tr class="TableData" align="center" height="30">
  <td width="250" align="right"><b>说明：</b></td>
  <td width="400" align="left">
    <span>
    1)导入办公用品登记信息后，库存数据会进行更新；<BR>
    2)将改好的EXECL办公用品登记报表另存为CSV格式的文件；<BR>
    3)模板中的登记类型应为采购入库、领用、借用、归还、报废中的一项；
    </span>
  </td>
 </tr>  
<tr>
  <td nowrap  class="TableControl" colspan="2" align="center">
    <input type="button" value="导入" onclick="doSubmit();" class="BigButton">
  </td>
 </tr> 
</table>
</form>
</div>
<div id="listDiv" align="center"></div>
<br>
<div id="listContainer" style=""></div>
<div id="msrg"></div>
<br>
<div align="center" id="turnDiv" style="display: none">
	<input type="button" value="返回" class="BigButton" onClick="location='<%=contextPath%>/subsys/oa/officeProduct/manage/query/import.jsp';" title="返回">
</div>

</body>
</html>
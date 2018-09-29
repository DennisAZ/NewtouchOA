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
				+"<td nowrap align='center' width='50'>姓名</td>"    
				+"<td nowrap align='center' width='50'>性别</td>"     
				+"<td nowrap align='center' width='40'>出生日期</td>" 
				+"<td nowrap align='center' width='40'>民族</td>" 
				+"<td nowrap align='center' width='30'>籍贯</td>" 
				+"<td nowrap align='center' width='40'>政治面貌</td>" 
				+"<td nowrap align='center' width='40'>身份证号码</td>" 
				+"<td nowrap align='center' width='40'>备注</td>" 
				+"<td nowrap align='center' width='100'>状态</td></tr><tbody>");
		$('listDiv').appendChild(table);
		for(var i = 0; i < dataObj.length; i++){
			count++;
			var staffName = dataObj[i].staffName;
			var staffSex = dataObj[i].staffSex;
			var staffBirth = dataObj[i].staffBirth;
			var staffNationality = dataObj[i].staffNationality;
			var staffNativePlace = dataObj[i].staffNativePlace;
			
			var staffPoliticalStatus = dataObj[i].staffPoliticalStatus;
			var staffCardNo = dataObj[i].staffCardNo;
			var remark = dataObj[i].remark;
			var infoStr = dataObj[i].infoStr;
			var color = dataObj[i].color;
			var trColor = (count % 2 == 0) ? "TableLine1" : "TableLine2";
			var tr=new Element('tr',{'class':trColor});   
			table.firstChild.appendChild(tr);
			tr.update("<td align='center'><font color="+color+">"     
				+ staffName + "</font></td><td align='center'><font color="+color+">" 
				+ staffSex + "</font></td><td align='center'><font color="+color+">"
				+ staffBirth + "</font></td><td align='center'><font color="+color+">"
				+ staffNationality + "</font></td><td align='center'><font color="+color+">"
				+ staffNativePlace + "</font></td><td align='center'><font color="+color+">"
				+ staffPoliticalStatus + "</font></td><td align='left'><font color="+color+">"
				+ staffCardNo + "</font></td><td align='center'><font color="+color+">"
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
	var downUrl = "<%=contextPath%>/yh/subsys/oa/hr/manage/staffInfo/act/YHImpHrStaffInfoAct";
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
    <td class="Big"><img src="<%=imgPath%>/sys_config.gif" align="middle"><span class="big3"> 导入人事数据</span><br>
    </td>
  </tr>
</table>
  <br>
  <form name="form1" id="form1" action="<%=contextPath%>/yh/subsys/oa/hr/manage/staffInfo/act/YHImpHrStaffInfoAct/impStaffInfoToCsv.act" method="post" enctype="multipart/form-data">
<table class="TableBlock" align="center" width="70%">  
 <tr class="TableData" align="center" height="30">
   <td width="150" align="right"><b>下载导入模板：</b></td>
   <td width="400" align="left">
    <a href="#" onclick="downCSVTemplet();">人事档案模板下载</a>
   </td>
 </tr>
 <tr class="TableData" align="center" height="30">
   <td width="150" align="right"><b>&nbsp;&nbsp;选择导入文件：</b></td>
   <td align="left" width="400">
    <input type="file" name="csvFile" id="csvFile" class="BigInput" size="30">
    <input type="hidden" name="FILE_NAME">
    <input type="hidden" name="GROUP_ID" value="">
   </td>
 </tr> 
 <tr class="TableData" align="center" height="30">
  <td width="150" align="right"><b>说明：</b></td>
  <td width="400" align="left">
    <span>
    1、EXECL的人事报表的前三列为姓名、用户名、部门,部门名必须在OA系统管理中存在，余下的列名称必须与人员档案基本信息相对应；    <br>
    2、将改好的EXECL人员信息报表另存为CSV格式的文件；
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
	<input type="button" value="返回" class="BigButton" onClick="location='<%=contextPath%>/subsys/oa/hr/manage/staffInfo/import.jsp';" title="返回">
</div>

</body>
</html>
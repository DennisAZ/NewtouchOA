<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 

<%
	String seqId=request.getParameter("seqId");
	if(seqId==null){
	  seqId="";
	}	
  
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>按维度显示</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript">
var requestURL="<%=contextPath%>/yh/core/funcs/system/dimension/act/YHDimensionAct";
function doInit(){
	USER_ID();	
}
function USER_ID(){
	var url=requestURL + "/getAllPersonIdStr.act?seqId=<%=seqId%>";
	var json=getJsonRs(url);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	var prcsJson=json.rtData;
	//alert(rsText);

	var table=new Element('table',{ "width":"100%","class":"TableList","align":"center"})
	.update("<tbody id='tbody'><tr class='TableHeader' style='font-size:10pt'>"
			+"<td nowrap align='center'>维度名称</td>"				
			+"<td nowrap align='center'>访问权限</td>"
			+"<td nowrap align='center'>新建权限</td>"				
			+"<td nowrap align='center'>下载/打印权限</td>"				
			+"<td nowrap align='center'>管理权限</td>"				
			+"<td align='center'>所有者</td></tr><tbody>");
	$('listDiv').appendChild(table);

	if(prcsJson.length>0){
		for(var i=0;i<prcsJson.length;i++){
			var prcs=prcsJson[i];
			//alert(prcs.sortName);
			var sortName=prcs.sortName;
			var visiName=prcs.visiName
			var newUserName=prcs.newUserName;
			var downUserName=prcs.downUserName;
			var manageName=prcs.manageName;
			var ownerName = prcs.ownerName;

			var tr=new Element('tr',{'width':'90%','class':'TableLine1','font-size':'10pt'});			
			table.firstChild.appendChild(tr);
			tr.update("<td align='left'>"					
			  + sortName + "</td><td align='left'>"
			  + visiName + "</td><td align='left'>"
				+ newUserName + "</td><td align='left'>"					
				+ downUserName + "</td><td align='left'>"					
				+ manageName + "</td><td align='left'>"					
				+ ownerName+ "</td>"
			);
		}
		
	}
	
}


</script>
</head >
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/filefolder/images/notify_open.gif" align="absmiddle"><span class="big3"> 按维度显示</span>
    <span class="small">&nbsp;&nbsp;具有访问权限或所有者权限时，其他权限设置才有效。</span>
    </td>
  </tr>
</table>
<div id="listDiv"></div>

<br>
<div align="center">
   <input type="button" value="返回" class="BigButton" onClick="parent.location='../index.jsp'">
</div>


</body >
</html>
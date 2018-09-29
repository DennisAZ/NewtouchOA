<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>办公用品类别编辑</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
function doInit(){
	var requestURI = "<%=contextPath%>/yh/subsys/oa/officeProduct/product/act/YHOfficeProductsAct";
	var url = requestURI + "/getOfficeType.act";
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
		var prcs = rtJson.rtData;
		//var table = new Element('table',{"align":"center","class":"TableBlock" });
		var strTable = "<table class='TableBlock' align='center'><tr class='TableHeader' align='center'>"
			+ "<td>&nbsp;&nbsp;<b>办公用品类别<b>&nbsp;&nbsp;</td>"
			+ "<td>&nbsp;&nbsp;<b>所属库<b>&nbsp;&nbsp;</td>"
			+ "<td>&nbsp;&nbsp;<b>操作<b>&nbsp;&nbsp;</td> </tr>";
		if(prcs.length>0){
			var trStr = "";
			for(var i=0;i<prcs.length;i++){
				//alert(trStr);
				var deptNameStr = "";
				if(prcs[i].typeDepository == "0"){
					deptNameStr = "默认库";
				}else{
					deptNameStr = prcs[i].depositoryName;
				}
				trStr += "<tr class='TableData' align='center'>"
					+ "<td>&nbsp;&nbsp;" + prcs[i].typeName +  "&nbsp;&nbsp;</td>"
					+ "<td>&nbsp;&nbsp;" + deptNameStr + "&nbsp;&nbsp;</td>"
					+ "<td>&nbsp;&nbsp;<a href=javascript:editType(\"" + prcs[i].typeId + "\") >编辑</a>&nbsp;&nbsp;<a href=javascript:delType(\"" + prcs[i].typeId + "\")>删除</a></td> </tr>";
			}
			var tableStr = strTable + trStr+ "</table>";
			$("listDiv").update(tableStr);
		}
		else {
		  $("msg").style.display = "";
	  }
	}else{
		alert(rtJson.rtMsrg);
	}
}

function delType(typeId){
	var msg = "确认要删除该类别项吗？";
	if(window.confirm(msg)){
		var requestURI = "<%=contextPath%>/yh/subsys/oa/officeProduct/product/act/YHOfficeProductsAct";
		var url = requestURI + "/delTypeName.act";
		var rtJson = getJsonRs(url,"typeId="+typeId);
		if(rtJson.rtState == "0"){
			parent.file_tree.location.reload();
			window.location.href = "<%=contextPath%>/subsys/oa/officeProduct/product/typeManage.jsp";
		}
	}
}


function editType(typeId){
	window.location.href = "<%=contextPath%>/subsys/oa/officeProduct/product/typeEdit.jsp?typeId=" + typeId;
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3">&nbsp;&nbsp;办公用品类别编辑</span>
    </td>
  </tr>
</table>

<div id="listDiv"></div>
<table id="msg" style="display: none" class="MessageBox" align="center" width="240">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无物品类别,请先新建物品类别!</div>
    </td>
  </tr>
</table>
</body>
</html>
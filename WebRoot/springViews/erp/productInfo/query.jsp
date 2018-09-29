<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" href="<%=cssPath%>/style.css">
		<link rel="stylesheet" href="<%=cssPath%>/page.css">
		<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css">
		<style type="text/css">
<!--
#deplist{display: none;position:absolute;left: 300px;top:50px;width: 0px solid #c30;width: 200px;height: 180px;background: #ccc;overflow: hidden;}
-->
</style>
		<!-- 封装表单的数据提交 -->
		<script type="text/javascript"src="<%=contextPath%>/springViews/js/jquery-1.4.2.js">
		jQuery.noConflict();
		</script>
		<script type="text/javascript" src="<%=contextPath %>/springViews/js/dtree.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/springViews/js/json.js" /></script>
		<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
		<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
		<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
		<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
		<script type="text/javascript" src="<%=contextPath %>/cms/station/js/util.js"></script>

<script type="text/javascript">
function doInit(){
	getProductTypeTree();
}

//获取产品类别树
function getProductTypeTree(){
	  var url = "<%=contextPath%>/yh/core/funcs/system/sealmanage/act/YHSealAct/selectProductTypeTree.act";
	  var rtJson = getJsonRs(url);
	  if(rtJson.rtState == "1"){
	    alert(rtJson.rtMsrg); 
	    return ;
	  }
	  var prcs = rtJson.rtData;
	  var selects = document.getElementById("typeId");
	  for(var i=0;i<prcs.length;i++){
	    var prc = prcs[i];
	    var option = document.createElement("option"); 
	    option.value = prc.value; 
	    option.innerHTML = prc.text; 
	    selects.appendChild(option);
	  }
}


	function short(){
	var param="";
	var shortName=jQuery("#shortName").val();
	var name=jQuery("#name").val();
	var typeId=jQuery("#typeId").val();
		if((shortName==""||shortName==null)&&(name==null||name=="")&&(typeId==""||typeId==null)){
			param="-1";
		}else{
	param=shortName+","+name+","+typeId;
	}
		parent.product.location='productManage.jsp?param='+encodeURIComponent(param);
	}
	function importExcel(){
		window.parent.location.href="import.jsp";
	}
	</script>
	</head>
	<body topmargin="5" onload="doInit();">
		<table border="0" width="100%" cellspacing="0" cellpadding="3"
			class="small">
			<tr>
				
				<td style="text-align:right">产品简称:</td><td style="text-align:left"><input type="text" id="shortName"  name="shortName"  value="" class="BigInput"></td>
				<td style="text-align:right">产品名称:</td><td style="text-align:left"><input type="text" id="name"  name="name"  value="" class="BigInput"></td>
					<td nowrap class="TableData">选择类别：</td>
    	 	    <td class="TableData" >
    	 	    <select name="typeId" id="typeId" class="BigSelect">
		          <option value="0">全部</option>
		        </select>
     		    </td>
				<td style="text-align:center"><input type="button" value="搜索"   class="BigButton"  onclick="short()"/></td>
				<td style="text-align:center"><input type="button" value="批量导入"   class="BigButton"  onclick="importExcel()"/></td>
			</tr>
		</table>
		<br>
	</body>
</html>
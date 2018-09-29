<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>
<%
	String shortName=request.getParameter("shortName");
	if(shortName==null||shortName.equals("")){
		shortName="";
			}
	String typeId=request.getParameter("typeId");
	if(typeId==null||typeId.equals("")){
		typeId="-1";
			}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" href="<%=cssPath%>/style.css">
		<link rel="stylesheet" href="<%=cssPath%>/page.css">
		<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css">
		<!-- 封装表单的数据提交 -->
		<script type="text/javascript"src="<%=contextPath%>/springViews/js/jquery-1.4.2.js">
		jQuery.noConflict();
		</script>
		<script type="text/javascript" src="<%=contextPath%>/springViews/js/json.js" /></script>
		<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
		<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
		<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
		<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
		<script type="text/javascript" src="<%=contextPath %>/cms/station/js/util.js"></script>
	<script type="text/javascript">
	var shortName="<%=shortName%>";
	var typeId="<%=typeId%>";
	function short(){
	var param="";
	var shortName=jQuery("#shortName").val();
	var typeId=jQuery("#typeId").val();
		if((shortName==""||shortName==null)&&(name==null||name=="")&&(typeId==""||typeId==null)){
			param="-1";
		}else{
		param=shortName+",-2,"+typeId;
	}
		parent.product.location='selectPro.jsp?param='+param;
	}
	function onInit() {
	jQuery("#shortName").val(shortName);
	var url = "<%=contextPath %>/SpringR/product/getType/"+new Date().getTime();
	jQuery.ajax({
	   type : 'POST',
	   async:false,
	   url : url,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
					if(typeId==item.id){
						jQuery('#typeId').append("<option  value='"+item.id+"' selected >"+item.name+"</option>");
					}else{
						jQuery('#typeId').append("<option  value='"+item.id+"' >"+item.name+"</option>");
						}
				});
			}
	   }
	 });

}
function setName(shortName,typeId){
	jQuery("#shortName").val(shortName);
	jQuery("#typeId").val(typeId);
}
function create(){
	 var url="proAdd.jsp";
 	openDialogResize(url,  520, 400);
}
	</script>
	</head>
	<body topmargin="5" onload="onInit()">
		<table border="0" width="100%" cellspacing="0" cellpadding="3"
			class="small">
			<tr>
				<td>产品简称：</td>
				<td><input type="text" id="shortName" name="shortName" value=""></td>
				<td nowrap class="TableData">选择类别：</td>
    	 	    <td class="TableData" >
       		         <select id="typeId" name="typeId">
       		         	<option value="0">全部</option>
      	 	         </select>
     		    </td>
				<td><input type="button" class="BigButton" value="搜索"   onclick="short()"></td>
				<td><input type="button" class="BigButton" value="新建产品"   onclick="create()"></td>
			</tr>
		</table>
		<br>
	</body>
</html>
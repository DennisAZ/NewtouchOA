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
	function short(){
	var param="";
	var shortName=jQuery("#shortName").val();
	var typeId=jQuery("#typeId").val();
			if((shortName==""||shortName==null)&&(name==null||name=="")&&(typeId==""||typeId==null)){
			param="-1";
		}else{
		param=shortName+",,"+typeId;
	}
		parent.product.location='chooseProduct.jsp?param='+param;
	}
	function onInit() {
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
						jQuery('#typeId').append("<option  value='"+item.id+"' >"+item.name+"</option>");
				});
			}
	   }
	 });

}
	</script>
	</head>
	<body topmargin="5" onload="onInit()" style="repeat:repeat;">
		<table border="0" width="100%" cellspacing="0" cellpadding="3"
			class="small">
			<tr>
				<td class="Big">
					<img src="<%=imgPath%>/infofind.gif" align="absMiddle">
					<span class="big3">&nbsp;
						产品选择</span>
				</td>
				<td>产品简称：</td>
				<td><input type="text" id="shortName" name="shortName" value=""></td>
				<td nowrap class="TableData">选择类别：</td>
    	 	    <td class="TableData" >
       		         <select id="typeId" name="typeId">
       		         	<option value="0">全部</option>
      	 	         </select>
     		    </td>
				<td><input type="button" value="搜索"   onclick="short()"></td>
			</tr>
		</table>
		<br>
	</body>
</html>
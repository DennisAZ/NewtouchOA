<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<title>仓库信息管理</title>
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
	var whName=jQuery("#whName").val();
	var styleId=jQuery("#styleId").val();
	if((whName==null||whName=="")&&(styleId==""||styleId==null)){
		param="-1";
	}else{
		param=whName+","+styleId;
	}
		parent.wh.location='dbManage.jsp?param='+encodeURIComponent(param);
	}
	
function onInit() {
	var url = "<%=contextPath %>/SpringR/warehouse/getStyle/"+new Date().getTime();
	jQuery.ajax({
	   type : 'POST',
	   async:false,
	   url : url,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
						jQuery('#styleId').append("<option  value='"+item.id+"' >"+item.name+"</option>");
				});
			}
	   }
	 });

}
	</script>
	</head>
	<body topmargin="5" onload="onInit()">
		<table border="0" width="100%" cellspacing="0" cellpadding="3"
			class="small">
			<tr>
				<td class="Big">
					<img src="<%=imgPath%>/infofind.gif" align="absMiddle">
					<span class="big3">&nbsp;
						仓库信息管理</span>
				</td>
				
			</tr>
     
			<tr>
				<td  align="right">请输入仓库名称：</td>
				<td align="center"><input type="text" id="whName"  name="whName"  value=""></td>
				<td nowrap class="TableData">选择仓库：</td>
    	 	    <td class="TableData" >
       		         <select id="styleId" name="styleId">
       		         	<option value="0">全部</option>
      	 	         </select>
     		    </td>
				<td><input type="button" value="搜索"  class="BigButton"  onclick="short()"/> </td> 
				
				<td class="Big">
  					 <input type="button" value="添加" class="BigButton" onclick="javascript:window.parent.location.href='adddb.jsp'">
  				</td>
			</tr>
			
		</table>
		<br>
	</body>
</html>
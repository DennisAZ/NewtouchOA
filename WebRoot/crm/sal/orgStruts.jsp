<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
   <title>部门结构图</title>
    <link rel="shortcut icon" href="favicon.ico"/>
<link type="text/css" rel="stylesheet" href="crm/css/CssOrgStruct.css" />
<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
 <script type="text/javascript" src="crm/js/prototype.js"></script>
<script type="text/javascript" language="javascript"  src="crm/js/JsOrgStruct.js"></script>
<script type="text/javascript" language="javascript"  src="crm/js/common.js"></script>
<script type="text/javascript">
	
	createProgressBar();	
	window.onload=function(){
		closeProgressBar();
	}
</script>

</head>
<body>
	<div id="orgStruts_DV" class="autoBr" ></div>
	<logic:notEmpty name="orgList">
        <script type="text/javascript">
          var i=0;
          var orgitem=new Array();
        </script>
		<logic:iterate id="salOrg" name="orgList">
		 <script type="text/javascript">
		    //alert("部门号："+'${salOrg.soCode}'+"上级号:"+'${salOrg.salOrg.soCode}');
		    orgitem[i]=i+"|"+'${salOrg.soCode}'+"|"+'<c:out value="${salOrg.soName}"/>'+"|"+'${salOrg.salOrg.soCode}'+"|";
		    i++;
		</script>
		
		</logic:iterate>
	</logic:notEmpty>
<script>
  //系统默认值为2;
  JsOrgStruct.DisplayLevel=2;
  //设置当前Image的路径
  JsOrgStruct.CurrentImagePath="./orgStrutsImage/";
  //绘制控件的ID，Array数组对象，根节点编码，用户响应方法名，节点元素宽度值，是否绘制所有层次的节点
  JsOrgStruct.CreatOrgStruct("orgStruts_DV",orgitem,"${orgTopCode}","",100,true);  
</script>
</body>
</html>

<%@ page contentType="text/html;" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<title>产品类别树(类别管理)</title>
<link rel="shortcut icon" href="favicon.ico"/>
<link rel="StyleSheet" href="css/dtree.css" type="text/css">
<style type="text/css">
	a {
		zoom:1;
	}
	body{
		background-color:#FFFFFF;
		padding:0px;
		margin:0px;
		overflow:hidden;

	}
	
	.scrollBar{
		width:100%;
		margin:0px;
		height:480px;
		padding:5px; 
		background-color:#f0eeee;
		
		overflow-x:auto;
		overflow-y:auto;
		scrollbar-face-color:#A6CFF4;
		scrollbar-highlight-color:#f9fbff;
		scrollbar-shadow-color:#f2f5fd;
		scrollbar-3dlight-color:#7DB4DB;
		scrollbar-arrow-color:#3243A3;
		scrollbar-track-color:#f0f0f1;
		scrollbar-darkshadow-color:#7DB4DB;
		text-align:left;	  
	}
</style>
<script type="text/javascript" src="js/prototype.js"></script>
<script type="text/javascript" src="js/dtree.js"></script>
<script type="text/javascript">
	
</script>
</head>

<body>
	<div id="tree" onMouseOver="this.style.backgroundColor='#f6f6f6'" onMouseOut="this.style.backgroundColor='#f0eeee'" class="scrollBar">
	<script type="text/javascript">
		var wptTree = new dTree('wptTree');
		wptTree.config.folderLinks=true;
		//id, pid, name, url, title, target, icon, iconOpen, open
		var p='orgimg/folder.gif';
		var pi='orgimg/folderopen.gif';			
		wptTree.add(0,-1,'全部类别',"javascript:loadProType('');",'全部类别','_parent');
		var url="javascript:loadProType('none');";
		wptTree.add('code','','未分类',url,'未分类','_parent',p,pi);
	</script>
    <c:if test="${!empty proType}">
    	<c:forEach var="pt" items="${proType}">
		<script type="text/javascript">
			url="javascript:loadProType('${pt.wptId}');";
			wptTree.add('${pt.wptId}','${pt.wmsProType.wptId}','<c:out value="${pt.wptName}"/>',url,'<c:out value="${pt.wptName}"/>','_parent',p,pi);
		</script>
		</c:forEach>
	</c:if>
	<script type="text/javascript">
		document.write(wptTree);
		wptTree.openAll();
		var ifm=parent.document.getElementById("ifm");
		ifm.height=480;
		parent.loadEnd("loadDiv");
	</script>
    </div>
</body>
</html>
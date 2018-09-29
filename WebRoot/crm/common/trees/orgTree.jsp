<%@ page contentType="text/html;" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<title>部门目录</title>
<link rel="shortcut icon" href="favicon.ico"/>
<link rel="StyleSheet" href="css/dtree.css" type="text/css">
<style type="text/css">
	body{
		padding:0px;
		margin:0px;
		overflow:hidden;
	}
	
	a {
		zoom:1;
	}
	
	.scrollBar{
		width:100%;
		margin:0px;
		height:380px;
		padding:5px;
		
		overflow-x:auto;
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
	function rightchange(url){
		parent.document.getElementById("rightList").src=url;
	}
	var orgTree = new dTree('orgTree');
	orgTree.config.folderLinks=true;	
	orgTree.icon.folder = 'images/dtree/orgnode.gif';
	orgTree.icon.folderOpen	= 'images/dtree/orgopen.gif';
</script>
</head>

<body>
	<div class="scrollBar">
    <c:forEach var="sorg" items="${orgList}">
    	<c:if test="${orgTopCode == sorg.soCode}">
		<script type="text/javascript">
			//id, pid, name, url, title, target, icon, iconOpen, open	
			var root="javascript:rightchange('empAction.do?op=getAllOrg&mark=orgList');";
			orgTree.add('${orgTopCode}',-1,'<c:out value="${sorg.soName}"/>',root,'<c:out value="${sorg.soName}"/>','_self');
	    </script>
		</c:if>
	</c:forEach>
	<c:if test="${!empty orgList}">
    	<c:forEach var="salOrg" items="${orgList}">
        	<c:if test="${orgTopCode != salOrg.soCode}">
		 	<script type="text/javascript">
			var location="javascript:rightchange('empAction.do?op=salOrgDesc&soCode=${salOrg.soCode}');";
			orgTree.add('${salOrg.soCode}','${salOrg.salOrg.soCode}','<c:out value="${salOrg.soName}"/>',location,'<c:out value="${salOrg.soName}"/>','_self','images/dtree/orgnode.gif','images/dtree/orgnode.gif');
			</script>
			</c:if>
		</c:forEach>
	</c:if>
	<script type="text/javascript">
		document.write(orgTree);
		orgTree.openAll();
	</script>
	</div>
</body>
</html>
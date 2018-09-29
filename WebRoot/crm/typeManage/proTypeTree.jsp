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
	
	.scrollBar{
		width:220px;
		height:400px;
		margin:0px;
		padding:0px;
		background-color:#fff;
		
		overflow:auto;
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
</head>

<body>
	<div style=" padding:5px; border:#ccc 1px solid; background-color:#f0eeee" onMouseOver="this.style.backgroundColor='#f6f6f6'" onMouseOut="this.style.backgroundColor='#f0eeee'" class="scrollBar">
	<script type="text/javascript">
		var proTree = new dTree('proTree');
		proTree.config.folderLinks=true;
		//id, pid, name, url, title, target, icon, iconOpen, open		
		proTree.add(0,-1,'产品类别','','产品类别','_parent');
	</script>
    <c:if test="${!empty proType}">
    	<c:forEach var="pt" items="${proType}">
        	<script type="text/javascript">
				proTree.add('${pt.wptId}','${pt.wmsProType.wptId}','<c:out value="${pt.wptName}"/>','wwoAction.do?op=getProType&wptId=${pt.wptId}','<c:out value="${pt.wptName}"/>','_parent');
			</script>
        </c:forEach>
    </c:if>
	<script type="text/javascript">
		document.write(proTree);
		proTree.openAll();
	</script>
    </div>
</body>
</html>
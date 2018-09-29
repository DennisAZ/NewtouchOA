<%@ page contentType="text/html;" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<title>产品类别树(弹出窗口中选择)</title>
    <link rel="shortcut icon" href="favicon.ico"/>
<link rel="StyleSheet" href="css/dtree.css" type="text/css">
<style type="text/css">
	a {
		zoom:1;
	}
</style>
<script type="text/javascript" src="js/prototype.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/dtree.js"></script>
<script type="text/javascript">
	function selectProType(n,id,name){
		if(n == 1 ){
			var iframe=parent.document.getElementById("popInside").contentWindow;
			try{
				if(iframe.document.getElementById("newWptId")!=null){
					iframe.document.getElementById("newWptId").value=id;
					iframe.document.getElementById("wptName").value=name;
				}
				else if(iframe.document.getElementById("upTypeId")!=null&&iframe.document.getElementById("upTypeName")!=null){
					iframe.document.getElementById("upTypeId").value=id;
					iframe.document.getElementById("upTypeName").value=name;
				}
			}catch(error){}
		parent.removeCd("brow_popDiv");
		}
		else{
			try{
				if(parent.document.getElementById("newWptId")!=null){
					parent.document.getElementById("newWptId").value=id;
					parent.document.getElementById("wptName").value=name;
				}
				else if(parent.document.getElementById("upTypeId")!=null&&parent.document.getElementById("upTypeName")!=null){
					parent.document.getElementById("upTypeId").value=id;
					parent.document.getElementById("upTypeName").value=name;
				}
			}catch(error){}
			parent.removeCd("brow_popDiv");			
		}
	}
	
</script>
</head>

<body>
	<div style=" padding:5px; border:#ccc 1px solid; background-color:#f0eeee" onMouseOver="this.style.backgroundColor='#f6f6f6'" onMouseOut="this.style.backgroundColor='#f0eeee'">
	<script type="text/javascript">
		var selProTree = new dTree('selProTree');
		selProTree.config.folderLinks=true;
		//id, pid, name, url, title, target, icon, iconOpen, open		
		selProTree.add(0,-1,'产品类别','','产品类别','_parent');
	</script>
    <c:if test="${!empty proType}">
    	<c:forEach var="pt" items="${proType}">
        <script type="text/javascript">				
			selProTree.add('${pt.wptId}','${pt.wmsProType.wptId}','<c:out value="${pt.wptName}"/>',"javascript:selectProType('${type}','${pt.wptId}','<c:out value="${pt.wptName}"/>');",'<c:out value="${pt.wptName}"/>');
		</script>
        </c:forEach>
    </c:if>
	<script type="text/javascript">
		document.write(selProTree);
		selProTree.openAll();
	</script>
    </div>
</body>
</html>
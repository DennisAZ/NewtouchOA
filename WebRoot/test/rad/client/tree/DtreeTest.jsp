<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/raw/lh/fckeditor/fckeditor.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/javascript"><!--
var tree = null;
function doInit(){
 tree =  new DTree({bindToContainerId:'di'
   						,isOnceLoad:false
   						,checkboxPara:{isHaveCheckbox:false}
						,linkPara:{clickFunc:test,linkAddress:'index.jsp?id=',target:'_blank'}
						,treeStructure:{isNoTree:false}	
					});

  tree.show();
}
function test(id){  
alert('you click' + id);
}

--></script>
</head>

<body onload="doInit()">
<div id="di" style="border: 1px solid #6E91C7"></div>

</body>
</html>
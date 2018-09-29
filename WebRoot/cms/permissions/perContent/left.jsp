<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>栏目列表</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/javascript">
var tree = null;
var stationId = 0;
var parentId = 0;
var id = 0;
function doInit(){
  tree = new DTree({bindToContainerId:'xtree' 
    ,requestUrl:contextPath + '/yh/cms/permissions/act/YHPermissionsAct/getColumnTree.act?GIFT_PROTYPE=GIFT_PROTYPE&id='
    ,isOnceLoad:false
    ,checkboxPara:{isHaveCheckbox:false}
    ,treeStructure:{isNoTree:false}
  	,linkPara:{clickFunc:test}
  	,treeStructure:{isNoTree:false}	
  });

  tree.show();
}
function test(id){
  this.id = id;
  setTimeout(function() {
  	$('-a-'+id).style.backgroundColor="rgb(217, 232, 251)";
  }, 0);
	var node = tree.getNode(id);//node得到的是你点击的结点的一个js对象
	var parentId = node.parentId;
	var extData = node.extData;//库seqId
	var url = "";
	if(parentId == 0){
	  url = contextPath + "/cms/permissions/perContent/newRemind.jsp";
	}
	else{
	  var idArry = id.split(",");
	  this.parentId = idArry[0];
	  url = contextPath + "/cms/permissions/perContent/index1.jsp?seqId=" + idArry[0];
	}
	parent.file_main.location.href = url;
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_open.gif" align="middle"><span class="big3"> 站点栏目列表</span><br>
    </td>
  </tr>
</table>
<div id="xtree"></div>

<br>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
  YHPerson person = (YHPerson) session.getAttribute(YHConst.LOGIN_USER);
  String personDeptId = person.getDeptId() + "";
  String personUserPriv = person.getUserPriv();
  String personId = person.getSeqId()+ "";
%>
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
var personDeptId = "<%= personDeptId%>";
var personUserPriv = "<%= personUserPriv%>";
var personId = "<%= personId%>";
var tree = null;
var stationId = 0;
var parentId = 0;
var id = 0;
function doInit(){
  tree = new DTree({bindToContainerId:'xtree' 
    ,requestUrl:contextPath + '/yh/cms/column/act/YHColumnAct/getColumnTree.act?GIFT_PROTYPE=GIFT_PROTYPE&id='
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
	
	var boo = isPermissions(extData.editUser);
	var disableStr = "";
	if(boo){
	  disableStr = "&disable=1";
	}
	else{
	  disableStr = "&disable=0";
	}
	
	if(parentId == 0){
	  var idArry = id.split(",");
	  stationId = idArry[0] ;
	  this.parentId = 0;
	  url = contextPath + "/cms/column/index1.jsp?seqId=" + idArry[0] + "&flag=1" + disableStr;
	}
	else{
	  var idArry = id.split(",");
	  this.parentId = idArry[0];
	  url = contextPath + "/cms/column/index1.jsp?seqId=" + idArry[0] + disableStr;
	}
	parent.file_main.location.href = url;
}

function addColumn(){
  if(stationId == 0 && parentId == 0){
    alert("请选择站点或者栏目！");
  }
  else{
    var node = tree.getNode(id);//node得到的是你点击的结点的一个js对象
    var extData = node.extData;//库seqId
    if(isPermissions(extData.newUser)){
      parent.file_main.location.href = contextPath + "/cms/column/new.jsp?stationId="+stationId+"&parentId="+parentId+"&treeId="+id;
    }
    else{
      alert("您没有该栏目新建子栏目的权限！");
    }
  }
}

function isPermissions(userPermissions){
  if(userPermissions == null || userPermissions == ""){
    return;
  }
  var permissions = new Array("","","");
  var permissionsTemp = userPermissions.split("|");
  for(var j = 0; j < permissionsTemp.length; j++){
    permissions[j] = permissionsTemp[j];
  }
  var permissionsDept = permissions[0] == null ? "" : permissions[0];
  permissionsDept = "," + permissionsDept + ",";
  var permissionsPriv = permissions[1] == null ? "" : permissions[1];
  permissionsPriv = "," + permissionsPriv + ",";
  var permissionsPerson = permissions[2] == null ? "" : permissions[2];
  permissionsPerson = "," + permissionsPerson + ",";
  
  if(permissionsDept.indexOf((","+personDeptId+",")) > -1 || ",0," == permissionsDept){
    return true;
  }
  else if(permissionsPriv.indexOf((","+personUserPriv+",")) > -1){
    return true;
  }
  else if(permissionsPerson.indexOf((","+personId+",")) > -1){
    return true;
  }
  return false;
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
<div align="center">
<input type="button"  value="新建栏目" class="BigButtonC" onClick="addColumn();" title="添加新的栏目">
</div>
 
</body>
</html>
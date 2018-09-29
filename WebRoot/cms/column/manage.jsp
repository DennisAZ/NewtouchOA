<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String seqId = request.getParameter("seqId");
String flag = request.getParameter("flag");
YHPerson person = (YHPerson) session.getAttribute(YHConst.LOGIN_USER);
String personDeptId = person.getDeptId() + "";
String personUserPriv = person.getUserPriv();
String personId = person.getSeqId()+ "";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理下级栏目</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/cms/station/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/cms/column/js/columnLogic.js"></script>
<script> 
var pageMgr = null;
var seqId = '<%=seqId %>';
var flag = '<%=flag %>';
var personDeptId = "<%= personDeptId%>";
var personUserPriv = "<%= personUserPriv%>";
var personId = "<%= personId%>";
function doInit(){
  var url = "<%=contextPath%>/yh/cms/column/act/YHColumnAct/getColumnList.act?seqId="+seqId+"&flag="+flag;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    colums: [
       {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data", name:"columnName",  width: '30%', text:"文章标题" ,align: 'center'},
       {type:"data", name:"columnDate", width: '12%', text:"发布时间" ,align: 'center' ,render: dateRender},
       {type:"data", name:"contentIndex",  width: '8%', text:"调序" ,align: 'center' ,render: stringIndex},
       {type:"hidden", name:"visitUser", text:"顺序号", dataType:"int"},
       {type:"hidden", name:"editUser", text:"顺序号", dataType:"int"},
       {type:"hidden", name:"newUser", text:"顺序号", dataType:"int"},
       {type:"hidden", name:"delUser", text:"顺序号", dataType:"int"},
       {type:"hidden", name:"relUser", text:"顺序号", dataType:"int"},
       {type:"selfdef", text:"操作", width: '15%',render:opts1}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
    showCntrl('delOpt');
  }else{
    WarningMsrg('无下级栏目信息', 'msrg');
  }
}

/**
 * 操作
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function opts1(cellData, recordIndex, columIndex){
	var seqId = this.getCellData(recordIndex,"seqId");
	
  //发布权限
  var relUser = this.getCellData(recordIndex,"relUser");
  var relBoo = isPermissions(relUser);
  var relUserStr = "";
  if(relBoo){
    relUserStr = "<a href=javascript:toRelease(" + seqId + ")>发布</a>&nbsp;";
  }
	
  //删除权限
  var delUser = this.getCellData(recordIndex,"delUser");
  var delBoo = isPermissions(delUser);
  var delUserStr = "";
  if(delBoo){
    delUserStr = "<a href=javascript:deleteSingle(" + seqId + ")>删除</a>&nbsp;";
  }
	return  "<center>"
				+ relUserStr
				+ delUserStr
				+ "</center>";
}

function toRelease(seqId){
  var requestURLStr = contextPath + "/yh/cms/column/act/YHColumnAct/toRelease.act?seqId=" + seqId;
  var rtJson = getJsonRs(requestURLStr);
  if(rtJson.rtState == "0"){
    if(rtJson.rtData == 1){
      alert("发布成功！")
      location.reload();
    }
    else if(rtJson.rtData == 0){
      alert("请正确配置该栏目的索引模板！"); 
    }
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function dateRender(cellData, recordIndex, columIndex){
  if(cellData){
    return cellData.substring(0,19);
  }
  return cellData;
}

function stringIndex(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  return  	"<center>"
  				+ "<span style=\"width:7px;height:12px;display: inline-block;background-image:url('../image/prev.gif');cursor:pointer;\" onclick=\"toSort("+seqId+","+recordIndex+","+1+")\"></span>&nbsp;&nbsp;"
  				+ "<span style=\"width:7px;height:12px;display: inline-block;background-image:url('../image/next.gif');cursor:pointer;\" onclick=\"toSort("+seqId+","+recordIndex+","+2+")\"></span>&nbsp;&nbsp;"
  				+ "<span style=\"width:7px;height:12px;display: inline-block;background-image:url('../image/first.gif');cursor:pointer;\" onclick=\"toSort("+seqId+","+recordIndex+","+3+")\"></span>&nbsp;&nbsp;"
  				+ "<span style=\"width:7px;height:12px;display: inline-block;background-image:url('../image/last.gif');cursor:pointer;\" onclick=\"toSort("+seqId+","+recordIndex+","+4+")\"></span>"
					+ "</center>";
}

function toSort(seqId, recordIndex, flag){
  var seqIdPrev = pageMgr.getCellData(recordIndex-1,"seqId");
  var seqIdNext = pageMgr.getCellData(recordIndex+1,"seqId");
  var toSeqId = "0";
  switch(flag){
  	case 1 : toSeqId = seqIdPrev == "" ? 0 : seqIdPrev;break;
  	case 2 : toSeqId = seqIdNext == "" ? 0 : seqIdNext;break;
  }
  var requestURLStr = contextPath + "/yh/cms/column/act/YHColumnAct/toSort.act?seqId="+seqId+"&toSeqId="+toSeqId+"&flag="+flag;
  var rtJson = getJsonRs(requestURLStr);
  if(rtJson.rtState == "0"){
    location.reload();
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function isPermissions(userPermissions){
  if(userPermissions == null || userPermissions == ""){
    return false;
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
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absMiddle"><span class="big3">&nbsp;管理下级栏目 </span>
   </td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
<table class="TableList" width="100%">
<tr class="TableControl">
      <td colspan="19">
         <input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this);"><label for="checkAlls">全选</label> &nbsp;
         <a href="javascript:deleteAll();" title="删除所选记录"><img src="<%=imgPath%>/delete.gif" align="absMiddle">删除所选记录</a>&nbsp;
      </td>
 </tr>
</table>
</div>

<div id="msrg">
</div>
</body>
</html>
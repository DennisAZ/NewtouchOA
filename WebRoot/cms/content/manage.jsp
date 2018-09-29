<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String stationId = request.getParameter("stationId");
String columnId = request.getParameter("columnId");
YHPerson person = (YHPerson) session.getAttribute(YHConst.LOGIN_USER);
String personDeptId = person.getDeptId() + "";
String personUserPriv = person.getUserPriv();
String personId = person.getSeqId()+ "";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理文章</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/cms/station/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/cms/content/js/contentLogic.js"></script>
<script> 
var pageMgr = null;
var stationId = '<%=stationId %>';
var columnId = '<%=columnId %>';
var personDeptId = "<%= personDeptId%>";
var personUserPriv = "<%= personUserPriv%>";
var personId = "<%= personId%>";
function doInit(){
  var url = "<%=contextPath%>/yh/cms/content/act/YHContentAct/getContentList.act?stationId="+stationId+"&columnId="+columnId;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    colums: [
       {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data", name:"contentName",  width: '30%', text:"文章标题" ,align: 'center', render: titleDetail},
       {type:"hidden", name:"createId",   text:"创建者ID" ,dataType:"int"},
       {type:"data", name:"userName",  width: '10%', text:"创建者" ,align: 'center'},
       {type:"data", name:"columnName",  width: '10%', text:"所属栏目" ,align: 'center'},
       {type:"data", name:"contentDate", width: '12%', text:"发布时间" ,align: 'center' ,render: dateRender},
       {type:"data", name:"contentStatus",  width: '5%', text:"状态" ,align: 'center' ,render: stringRender},
       {type:"data", name:"contentIndex",  width: '8%', text:"调序" ,align: 'center' ,render: stringIndex},
       {type:"hidden", name:"contentTop",   text:"置顶" ,dataType:"int"},
       {type:"hidden", name:"visitUser", text:"顺序号", dataType:"int"},
       {type:"hidden", name:"editUserContent", text:"顺序号", dataType:"int"},
       {type:"hidden", name:"approvalUserContent", text:"顺序号", dataType:"int"},
       {type:"hidden", name:"releaseUserContent", text:"顺序号", dataType:"int"},
       {type:"hidden", name:"recevieUserContent", text:"顺序号", dataType:"int"},
       {type:"hidden", name:"orderContent", text:"顺序号", dataType:"int"},
       {type:"selfdef", text:"操作", width: '15%',render:opts},
       {type:"selfdef", text:"置顶", width: '5%',render:cTop}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
    showCntrl('delOpt');
  }else{
    WarningMsrg('无内容信息', 'msrg');
  }
}

function titleDetail(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<a href=javascript:toDetail(" + seqId + ")>"+cellData+"</a>";
}

function toDetail(seqId){
  var URL = contextPath + "/cms/content/detail.jsp?seqId=" + seqId;
  newWindow(URL,'820', '500');
}

function dateRender(cellData, recordIndex, columIndex){
  if(cellData){
    return cellData.substring(0,19);
  }
  return cellData;
}

function stringRender(cellData, recordIndex, columIndex){
  var str = "";
  switch(cellData){
  	case 0 : str = "新搞";break;
  	case 1 : str = "已编";break;
  	case 2 : str = "已否";break;
  	case 3 : str = "已签";break;
  	case 4 : str = "已撤";break;
  	case 5 : str = "已发";break;
  }
  return str;
}

function cTop(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var contentTop = this.getCellData(recordIndex,"contentTop");
  var str = "";
  if(contentTop == 1){
    str = "取消置顶";
  }
  else{
    str = "置顶";
  }
  
  //排序权限
  var orderContent = this.getCellData(recordIndex,"orderContent");
  var orderContentBoo = isPermissions(orderContent);
  var orderContentStr = "";
  if(orderContentBoo){
    orderContentStr = "<a href=javascript:toTop(" + seqId + "," + contentTop + ")>"+str+"</a>&nbsp;";
  }
  
	return 		"<center>"
					+ orderContentStr
					+ "</center>";
}

function toTop(seqId, contentTop){
  var requestURLStr = contextPath + "/yh/cms/content/act/YHContentAct/toTop.act?seqId="+seqId+"&contentTop="+contentTop;
  var rtJson = getJsonRs(requestURLStr);
  if(rtJson.rtState == "0"){
    location.reload();
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function stringIndex(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  
  //排序权限
  var orderContent = this.getCellData(recordIndex,"orderContent");
  var orderContentBoo = isPermissions(orderContent);
  var orderContentStr = "";
  if(orderContentBoo){
    orderContentStr = "<span style=\"width:7px;height:12px;display: inline-block;background-image:url('../image/prev.gif');cursor:pointer;\" onclick=\"toSort("+seqId+","+recordIndex+","+1+")\"></span>&nbsp;&nbsp;"
									    + "<span style=\"width:7px;height:12px;display: inline-block;background-image:url('../image/next.gif');cursor:pointer;\" onclick=\"toSort("+seqId+","+recordIndex+","+2+")\"></span>&nbsp;&nbsp;"
									    + "<span style=\"width:7px;height:12px;display: inline-block;background-image:url('../image/first.gif');cursor:pointer;\" onclick=\"toSort("+seqId+","+recordIndex+","+3+")\"></span>&nbsp;&nbsp;"
									    + "<span style=\"width:7px;height:12px;display: inline-block;background-image:url('../image/last.gif');cursor:pointer;\" onclick=\"toSort("+seqId+","+recordIndex+","+4+")\"></span>";
  }
  return  "<center>"
          + orderContentStr
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
  var requestURLStr = contextPath + "/yh/cms/content/act/YHContentAct/toSort.act?seqId="+seqId+"&toSeqId="+toSeqId+"&flag="+flag+"&columnId="+columnId;
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
   <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absMiddle"><span class="big3">&nbsp;管理内容 </span>
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
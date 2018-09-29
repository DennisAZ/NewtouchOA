<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>收文权限设置 </title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/manage/staffCare/js/util.js"></script>
<script> 
var pageMgr = null;
function doInit(){
  var url = "<%=contextPath%>/yh/core/funcs/doc/act/YHDocRecvPrivAct/getAllPriv.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
	  var data = rtJson.rtData;
	  if(data.userId != 0){
		  $('tableTitle1').style.display = "";
		  $('table1').style.display = "";
		  var url = contextPath + "/yh/core/funcs/doc/act/YHDocRecvPrivAct/getPrivName.act";
		  var rtJson = getJsonRs(url, "privIdStr=" + data.userId);
		  if(rtJson.rtState == '0'){
	    	$('recvName').innerHTML = rtJson.rtData;
	    	$('recvId').value = data.userId;
		  }
	  }
	  else{
		  $('tableTitle1').style.display = "none";
		  $('table1').style.display = "none";
	  }
  }
	
  var url = "<%=contextPath%>/yh/core/funcs/doc/act/YHDocRecvPrivAct/getDeptPrivListJson.act";
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"hidden", name:"deptId",  width: '20%', text:"部门Id" ,align: 'center'},
         {type:"data", name:"deptName",  width: '10%', text:"部门" ,align: 'center'},
         {type:"data", name:"userName",  width: '20%', text:"收文人员" ,align: 'center' , render: staffNameFunc},
         {type:"hidden", name:"userId",  width: '10%', text:"收文人员Id" ,align: 'center'},
         {type:"selfdef", text:"操作", width: '15%',render:opts}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
    	$('tableTitle2').style.display = "";
      showCntrl('listContainer');
      var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
    }
}

function staffNameFunc(cellData, recordIndex, columIndex){
	  var url = contextPath + "/yh/subsys/oa/hr/manage/act/YHHrStaffIncentiveAct/getUserName.act";
	  var rtJson = getJsonRs(url, "userIdStr=" + cellData);
	  if (rtJson.rtState == "0") {
	    return "<center>" + rtJson.rtData + "</center>";
	  } else {
	    alert(rtJson.rtMsrg); 
	  }
	}

function opts(cellData, recordIndex, columIndex){
	var seqId = this.getCellData(recordIndex,"seqId");
  var deptId = this.getCellData(recordIndex,"deptId");
  return "<center>"
        + "<a href=javascript:doEdit("+recordIndex+"," + deptId + ")>编辑</a>&nbsp;"
        + "<a href=javascript:deleteAllPriv(" + seqId + ")>删除</a>"
        + "</center>";
}

function doEdit(recordIndex,deptId){
	var userId = pageMgr.getCellData(recordIndex,"userId");
  var URL = contextPath + "/core/funcs/doc/docRecvPriv/privSelect.jsp?deptId='"+deptId+"'&userId='"+userId+"'&flag=0";
  newWindow(URL,'550', '210');
}

function deleteAllPriv(seqId){
  if(!window.confirm("确认删除该权限吗？")){
    return ;
  }
  var url = "<%=contextPath%>/yh/core/funcs/doc/act/YHDocRecvPrivAct/deleteAllPriv.act?seqId="+seqId;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
	  alert("删除成功！")
	  window.location.reload();
  }	
}

function clickButton(){
  var URL = contextPath + "/core/funcs/doc/docRecvPriv/privSelect.jsp?flag=0";
  newWindow(URL,'550', '210');
}

function newWindow(url,width,height){
  var locX=(screen.width-width)/2;
  var locY=(screen.height-height)/2;
  window.open(url, "meeting", 
      "height=" +height + ",width=" + width +",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
      + locY + ", left=" + locX + ", resizable=yes");
}

function edit_priv(){
  var URL = contextPath + "/core/funcs/doc/docRecvPriv/privSelect.jsp?privId='"+$('recvId').value+"'&flag=1";
  newWindow(URL,'550', '210');
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/new_email[1].gif" align="absMiddle"><span class="big3">&nbsp;收文权限管理  </span><span style="color:red">说明:设置权限后方能进行收文登记</span>
   </td>
 </tr>
</table>
<br>
<center>
<input type="button" class="BigButton" value="添加权限" onclick="clickButton()">
</center>
<br>
<table id="tableTitle1" border="0" width="100%" cellspacing="0" cellpadding="3" class="small" style="display:none">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/new_email[1].gif" align="absMiddle"><span class="big3">&nbsp;全局权限  </span>
   </td>
 </tr>
</table>
<table id="table1" border="0" width="95%" class="TableList" cellpadding="3" align="center" style="display:none">
  <tr class="TableHeader">
    <td nowrap width="30%">收文角色</td>
    <td nowrap width="30%">操作</td>
  </tr>
  <tr>
    <td nowrap>
      <span id="recvName"></span>
      <input type="hidden" id="recvId">
    </td>
    <td nowrap align="center"><a href="#" onclick="javascript:edit_priv()">编辑</a>&nbsp;&nbsp;
    <a href="#" onclick="javascript:deleteAllPriv(0)">删除</a></td>
  </tr>
</table>
<br>
<table id="tableTitle2" border="0" width="100%" cellspacing="0" cellpadding="3" class="small" style="display:none">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/new_email[1].gif" align="absMiddle"><span class="big3">&nbsp;按部门权限  </span>
   </td>
 </tr>
</table>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
<table class="TableList" width="100%">
<tr class="TableControl">
 </tr>
</table>
</div>
</body>
</html>
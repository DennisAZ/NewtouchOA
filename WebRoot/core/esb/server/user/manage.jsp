<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/esb/server/user/js/util.js"></script>
<script> 
var pageMgr = null;
function doInit(){
  var url = "<%=contextPath%>/yh/core/esb/server/user/act/TdUserAct/getUserListJson.act";
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    colums: [
       //{type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
       {type:"hidden", name:"seqId", text:"主键", dataType:"int"},
       {type:"data", name:"userCode",  width: '12%', text:"用户账号" ,align: 'center'},
       {type:"data", name:"userName",  width: '12%', text:"用户名称" ,align: 'center'},
       //{type:"data", name:"appId",  width: '12%', text:"交换应用ID" ,align: 'center' },
       {type:"data", name:"userType",  width: '10%', text:"用户类型" ,align: 'center' ,render : userTypeFun},
       {type:"data", name:"status",  width: '10%', text:"用户状态" ,align: 'center' ,render : userStateFun},
       {type:"data", name:"description",  width: '20%', text:"用户描述" ,align: 'center'},
       {type:"selfdef", text:"操作", width: '20%',render:opts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
    showCntrl('delOpt');
  }else{
    WarningMsrg('无用户信息', 'msrg');
  }
}

function userTypeFun(cellData, recordIndex,columInde) {
  var str = ""
  if(cellData == 0){
    str = "下级部门";
  }
  else if(cellData == 1){
    str = "总部";
  }
  return "<center>" + str + "</center>";
}

function userStateFun(cellData, recordIndex,columInde) {
  var str = ""
  if(cellData == 0){
    str = "启用";
  }
  else if(cellData == 1){
    str = "未启用";
  }
  return "<center>" + str + "</center>";
}

/**
 * 详细信息
 * @param seqId
 * @return
 */
function detail(seqId){
  var URL = contextPath + "/core/esb/server/user/detail.jsp?seqId=" + seqId;
  newWindow(URL,'600', '300');
}

/**
 * 修改
 * @param seqId
 * @return
 */
function doEdit(seqId){
  var URL = contextPath + "/core/esb/server/user/modify.jsp?seqId=" + seqId;
  window.location.href = URL;
}

//删除
function deleteSingle(seqId){
	if(!window.confirm("确认要删除该用户信息 ？")){
		return ;
	}
	var url = contextPath + "/yh/core/esb/server/user/act/TdUserAct/deleteUser.act";
	var rtJson = getJsonRs(url, "seqId=" + seqId );
	if (rtJson.rtState == "0") {
		window.location.reload();
	}else {
	 alert(rtJson.rtMsrg); 
	}
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absMiddle"><span class="big3">&nbsp;用户管理</span>
   </td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
<table class="TableList" width="100%">
  <tr class="TableControl">
    <!--td colspan="19">
      < input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this);"><label for="checkAlls">全选</label> &nbsp;
        <a href="javascript:deleteAll();" title="删除所选记录"><img src="<%=imgPath%>/delete.gif" align="absMiddle">删除所选记录</a>&nbsp;
    </td-->
  </tr>
</table>
</div>

<div id="msrg">
</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统更新管理</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type = "text/javascript" src="<%=contextPath %>/core/esb/client/update/js/util.js"></script>
<script type = "text/javascript" src="<%=contextPath %>/core/esb/client/update/js/update.js"></script>
<script type="text/javascript">
var pageMgr = null;
function doInit(){
  var url = "<%=contextPath%>/yh/core/esb/client/update/act/YHUpdateClientAct/getUpdateClientList.act";
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    paramFunc: getParam,
    colums: [
       {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data", name:"updateDesc",  width: '20%', text:"更新说明" ,align: 'left' },
       {type:"data", name:"toVersion",  width: '10%', text:"新版本号" ,align: 'center' },
       {type:"data", name:"updateUser",  width: '10%', text:"更新人" ,align: 'center' },
       {type:"data", name:"doneTime",  width: '10%', text:"完成时间" ,align: 'center' },
       {type:"data", name:"updateStatus",width:'5%', text:"更新状态" ,align: 'center' ,render:status},
       {type:"selfdef", text:"操作", width: '10%',render:opts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
    showCntrl('delOpt');
  }else{
    WarningMsrg('无系统升级信息', 'msrg');
  }
}
function status(cellData,recordIndex,columIndex){
	var status=this.getCellData(recordIndex,"updateStatus");
	if(status==0){return "接收成功";}
	if(status==1){return "等待更新";}
	if(status==2){return "更新成功";}
}
function getParam(){
	  var queryParam = $("form1").serialize();
	  return queryParam;
	}
/**
 * 操作
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function opts(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var status = this.getCellData(recordIndex,"updateStatus")
  if(status=="2"){
	  return "<center>"
		  + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a>&nbsp;"
		  + "</center>";
  }else{
  return "<center>"
        + "<a href=javascript:updateClient("+ seqId +")>更新</a>&nbsp;"
        + "</center>";
  }
}
function doSubmit(){
	$("form1").submit();
}

function updateClient(seqId){
	if(!window.confirm("确认要更新系统吗？")){
		return ;
	}
	var requestURLStr = contextPath + "/yh/core/esb/client/update/act/YHUpdateClientAct";
	var url = requestURLStr + "/updateClient.act";
	var rtJson = getJsonRs(url, "seqId=" + seqId );
	if (rtJson.rtState == "0") {
		window.location.href="<%=contextPath%>/core/esb/client/update/updateSuccess.jsp"
	}else {
	 alert(rtJson.rtMsrg); 
	}
}
</script>
</head>
<body  onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3">系统升级管理</span><br>
    </td>
  </tr>
</table>
<form id="form1" action="<%=contextPath%>/yh/core/esb/client/update/act/YHUpdateClientAct/updateClient.act" method="post" enctype="multipart/form-data">
	<!-- <input type="button" id="update" name="update" value="更新" onclick="doSubmit()"> -->
</form>
<br>
<div id="listContainer" style="display:none;width:100;"></div>
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
<div id="msrg"></div>
</body>
</html>
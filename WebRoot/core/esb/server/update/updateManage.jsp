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
<script type = "text/javascript" src="<%=contextPath %>/core/esb/server/update/js/util.js"></script>
<script type = "text/javascript" src="<%=contextPath %>/core/esb/server/update/js/update.js"></script>
<script type="text/javascript">
var pageMgr = null;
function doInit(){
	//alert("hello");
  var url = "<%=contextPath%>/yh/core/esb/server/update/act/YHUpdateServerAct/getUpdateServerList.act";
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    paramFunc: getParam,
    colums: [
       {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data", name:"updateDesc",  width: '15%', text:"更新说明" ,align: 'left' },
       {type:"data", name:"toVersion",  width: '10%', text:"新版本号" ,align: 'center' },
       {type:"data", name:"deployUser",  width: '10%', text:"发布人" ,align: 'center' },
       {type:"data", name:"deployTime",  width: '10%', text:"发布时间" ,align: 'center' },
       {type:"selfdef",  width:'5%', text:"更新详情" ,align: 'center' ,render:renderDetail},
       {type:"selfdef", text:"操作", width: '15%',render:opts}]
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
/**
 * 操作
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function opts(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<center>"
        + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a>&nbsp;"
        + "</center>";
}

function getParam(){
	  var queryParam = $("form1").serialize();
	  return queryParam;
	}
	
function doUpload(){
	  var fileName = $('updateFile').value;
	  var index = fileName.lastIndexOf(".");
	  var extName = "";
	  if(index >= 0){
	    extName = fileName.substring(index+1).toLowerCase().trim();
	   }
	  if(extName == "zip"){
	    $('form1').action = contextPath + "/yh/core/esb/server/update/act/YHUpdateServerAct/uploadFile.act";
	    $('form1').submit();
	  }else{
	    alert("请选择.zip压缩文件!");
	    return;
	  }
	}
</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3">系统升级包上传</span><br>
    </td>
  </tr>
</table>
<br>
<form id="form1" name="form1" action="" method="post" enctype="multipart/form-data">
<table class="TableBlock" width="90%" align="center">
  <tr>
	<td align="left" width="30%" class="TableContent" nowrap>更新包路径：</td>
    <td align="center" class="TableData" width="70%" nowrap>
      <input id="updateFile" name="updateFile" type="file" style="width: 100%;">
    </td>
  </tr> 
  <tr>
  	<td colspan="2" align="center">
  	  <input type="button" value="上传" onclick="doUpload()" class="BigButton">
  	</td>
  </tr>
</table>
</form>
<br>
<br>
<div><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3">系统更新日志</span><br></div>
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
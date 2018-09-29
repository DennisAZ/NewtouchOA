<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>查看文件</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/rollfilelogic.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/rmsrolllogic.js"></script>
<script type="text/javascript">
var pageMgr = null;
var seqIdStr = "";
var total = 0;
function doInit(){
  checkSelectBox();
  var url = "<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsRollAct/getRmsFileJosn.act?seqId=${param.seqId}";
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 3,
      sortDirect: "desc",
      colums: [
         {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"hidden", name:"rollId", text:"rollId号", dataType:"int"},
         {type:"data", name:"fileCode",  width: '10%', text:"文件号", render:rmsFileCodeFunc, sortDef:{type: 0, direct:"desc"}},       
         {type:"data", name:"fileTitle",  width: '15%', text:"文件标题", render:rollFileFunc, sortDef:{type: 0, direct:"desc"}},
         {type:"data", name:"secret",  width: '10%', text:"密级", render:secretFunc}, 
         {type:"data", name:"sendUnit",  width: '15%', text:"发文单位", render:rollFileFunc, sortDef:{type: 0, direct:"desc"}}, 
         {type:"data", name:"sendDate",  width: '10%', text:"发文时间", render:sendDateFunc, sortDef:{type: 0, direct:"desc"}}, 
         {type:"data", name:"urgency",  width: '15%', text:"紧急等级", render:getUrgencyFunc}, 
         {type:"selfdef", text:"操作", width: '10%',render:optsFile}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    total = pageMgr.pageInfo.totalRecord;
    if(total){
      count = total;
      showCntrl('listContainer');
      var mrs = " 共 "+ total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
      WarningMsrg('无文件', 'msrg');
    }
    if(!total){
      $("delOpt").style.display = "none";
    }
}

function checkSelectBox(){
  var url = "<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsRollAct/getRmsRollSelect.act";
  var rtJson = getJsonRs(url);
  var selectObj =  $('rollId');
  //selectObj.length = 2;
  if(rtJson.rtState == "0"){
   for(var i = 0; i < rtJson.rtData.length; i++){
     var option = document.createElement("option");
     option.value = rtJson.rtData[i].seqId;
     option.innerHTML = rtJson.rtData[i].rollCode + " - " + rtJson.rtData[i].rollName;
     selectObj.appendChild(option);
   }
  }
}

function changeBox(dom){
  var ids = getChecked();
  if(ids == "") {
    alert("要移卷文件，请至少选择其中一个");
    $('opForm').reset();
    return;
  }
  var rollId = dom.value;
  var url = "<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsRollAct/changeRmsRollSelect.act?seqIds="+ ids + "&rollId=" + rollId;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    window.location.reload();
    //pageMgr.refreshAll();
    $('opForm').reset();
  }
}


</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="middle"><span class="big3"> 查看文件</span>&nbsp;
    </td>
  </tr>
</table>

<br>
<div id="listContainer" style="display:none;width:100%;"></div>

<div id="delOpt" style="display:none">
<form id="opForm"  >
	<table class="TableList" width="100%">
		<tr class="TableControl">
		<td colspan="9">
         &nbsp;<input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this)"><label for='checkAlls'>全选</label> &nbsp;<input type="button"  value="导出" class="SmallButton" onClick="exportRmsFile();" title="导出">
         <input type="button"  value="批量销毁" class="SmallButton" onClick="destroyAll()" title="销毁已选中文件">　
			组卷至：
          <select id="rollId" name="rollId" onChange="changeBox(this);">
           <option value="">请选择案卷</option>
		   <option value=0>移出本卷</option>
          </select>
　　　　　</td>
	</tr>
	</table>
 </form>
</div>

<div id="msrg"></div>

</body>
</html>
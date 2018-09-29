<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件管理</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/rollfilelogic.js"></script>
<script type="text/javascript">

var pageMgr = null;
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsFileAct/getDestroyRmsFileJosn.act";
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 2,
      sortDirect: "desc",
      colums: [
				 {type:"selfdef", text:"选择", width: '3%', render:checkBoxRender},
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"fileCode",  width: '10%', text:"文件号", render:rmsFileCodeFunc, sortDef:{type: 0, direct:"desc"}},       
         {type:"data", name:"fileTitle",  width: '10%', text:"文件标题", render:rollFileFunc,sortDef:{type: 0, direct:"desc"}},
         {type:"data", name:"secret",  width: '10%', text:"密级", render:secretFunc}, 
         {type:"data", name:"sendUnit",  width: '10%', text:"发文单位", render:rollFileFunc,sortDef:{type: 0, direct:"desc"}}, 
         {type:"data", name:"sendDate",  width: '10%', text:"发文时间", render:sendDateFunc,sortDef:{type: 0, direct:"desc"}}, 
         {type:"data", name:"rollName",  width: '15%', text:"所属案卷", render:rmsRollNameFunc}, 
         {type:"selfdef", text:"操作", width: '15%',render:destroyOptsFile}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      count = total;
      showCntrl('listContainer');
      var mrs = " 共 "+ total + " 条记录 ！";
      showCntrl('delOpt');
      
    }else{
      WarningMsrg('无文件', 'msrg');
    }
}


</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="middle"><span class="big3"> 已销毁文件</span>&nbsp;
    </td>
  </tr>
</table>

<br>
<div id="listContainer" style="display:none;width:100%;"></div>

<div id="delOpt" style="display:none">
	<table class="TableList" width="100%">
		<tr class="TableControl">
			<td colspan="9"> 
				&nbsp;&nbsp;<input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this)"><label for='checkAlls'>全选</label> &nbsp;
				<input type="button"  value="批量还原" class="SmallButton" onClick="recover_all()" title="还原已选中文件">&nbsp;&nbsp;
    		<input type="button"  value="批量删除" class="SmallButton" onClick="delete_all()" title="删除已选中文件">
			</td>
		</tr>
	</table>
</div>

<div id="msrg"></div>


</body>
</html>
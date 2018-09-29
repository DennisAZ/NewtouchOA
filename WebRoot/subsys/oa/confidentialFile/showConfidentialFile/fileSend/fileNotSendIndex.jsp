<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>未报送文件</title>
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/confidentialFile/showConfidentialFile/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/confidentialFile/showConfidentialFile/js/fileSendrlogic.js"></script>
<script type="text/javascript">
var pageMgr = null;
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/confidentialFile/act/YHConfidentialContentAct/getFileNotSendListJson.act?fileSend=0";
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 2,
      sortDirect: "desc",
      moduleName:"confidential", 
      colums: [
         {type:"selfdef", text:"选择", width: '3%', render:selectedRender},
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"subject",  width: '10%', text:"文件名称", align:"left",sortDef:{type: 0, direct:"desc"}},       
         {type:"hidden", name:"attachId", text:"附件ID",align:"center",width:"1%"},
         {type:"text", name:"attach", text:"附件",align:"left",width:"20%",dataType:"attach"},
         {type:"data", name:"sendTime",  width: '10', text:"发布时间", render:sendDateFunc,sortDef:{type: 0, direct:"desc"}},
         {type:"data", name:"contentNo",  width: '10%', text:"排序号", render:alignFunc,sortDef:{type: 0, direct:"desc"}},
         {type:"data", name:"fileSend",  width: '5', text:"报送状态", render:fileSendFunc},
         {type:"hidden", name:"sortId",  width: '3%', text:"文件夹号", dataType:"int"},
         {type:"selfdef", text:"操作", width: '10%',render:optsList}]
    };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    showCntrl('appendDiv');
    
  }else{
  	//$("queryFile").hide();
    WarningMsrg("无未报送文件", 'nothingDiv');
  }
}


</script>
</head>
<body onload="doInit();">
<table id="headTableStr" border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_open.gif" align="middle"><b>&nbsp;<span class="big3" >未报送文件 </span></b>&nbsp;
   </td>
  </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;"></div>
<div id="appendDiv" style="display:none">
<table class="TableList" width="100%">
	<tr class="TableControl">
		<td colspan="9"> 
			&nbsp;&nbsp;<input type="checkbox" name="checkAlls" id="checkAlls" onClick="selectedkAll(this)">&nbsp;<label for='checkAlls'>全选</label> &nbsp;
			<a href='javascript:fileSend();' id='sign'><img src='<%=imgPath %>/email_open.gif' align="middle" border='0' title='报送文件'>报送&nbsp;&nbsp;</a>
		</td>
	</tr>
</table>
</div>

<div id="nothingDiv" align="center"></div>
</body>
</html>
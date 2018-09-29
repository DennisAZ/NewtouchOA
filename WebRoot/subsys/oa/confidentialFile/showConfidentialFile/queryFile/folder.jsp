<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqId=request.getParameter("seqId");
	if(seqId==null){
  	seqId="";
	}	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="yh.core.funcs.person.data.YHPerson"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机要文件</title>
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
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/confidentialFile/showConfidentialFile/js/queryFileLogic.js"></script>
<script type="text/javascript">
var pageMgr = null;
function doInit(){
	getFolderPathById("<%=seqId%>");
	//showManage("<%=seqId%>");
  var url = "<%=contextPath%>/yh/subsys/oa/confidentialFile/act/YHConfidentialContentAct/getContentFileListJson.act?seqId=<%=seqId%>";
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 2,
      sortDirect: "desc",
      moduleName:"confidential", 
      colums: [
         {type:"selfdef", text:"选择", width: '0', render:checkBoxRender},
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"subject",  width: '10%', text:"文件名称", align:"left",sortDef:{type: 0, direct:"desc"}},       
         {type:"hidden", name:"attachId", text:"附件ID",align:"center",width:"1%"},
         {type:"text", name:"attach", text:"附件",align:"left",width:"20%",dataType:"attach"},
         {type:"data", name:"sendTime",  width: '5', text:"发布时间", render:sendDateFunc,sortDef:{type: 0, direct:"desc"}},
         {type:"data", name:"contentNo",  width: '5', text:"排序号", render:alignFunc,sortDef:{type: 0, direct:"desc"}},
         {type:"data", name:"fileSend",  width: '2', text:"报送状态", render:fileSendFunc},
         {type:"hidden", name:"sortId",  width: '1', text:"文件夹号", dataType:"int"},
         {type:"selfdef", text:"操作", width: '2',render:optsList}]
    };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
    showCntrl('delOpt');
  }else{
  	$("queryFile").hide();
    WarningMsrg("该文件夹尚无文件", 'nothingDiv');
  }
}

</script>
</head>
<body onload="doInit();">
<table id="headTableStr" border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_open.gif" align="middle"><b>&nbsp;<span class="Big1" id="folderPath"> </span></b><br>
   </td>
  </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
<table class="TableList" width="100%">
	<tr class="TableControl">
		<td colspan="9"> 
			&nbsp;&nbsp;<input type="checkbox" name="checkAlls" id="checkAlls" onClick="selectedkAll(this)">&nbsp;<label for='checkAlls'>全选</label> &nbsp;
			<a href='javascript:fileSend();' id='sign'><img src='<%=imgPath %>/email_open.gif' align="middle" border='0' title='报送文件'>报送&nbsp;&nbsp;</a>
			<a href='javascript:getBack();' id='sign'><img src='<%=imgPath %>/email_open.gif' align="middle" border='0' title='报送撤回'>撤回&nbsp;&nbsp;</a>
		</td>
	</tr>
</table>
</div>

<div id="nothingDiv" align="center"></div>
<div id="noPrivDiv" style="display: none">
<table class="MessageBox" align="center" width="340">
  <tr>
    <td class="msg error">
      <h4 class="title">错误</h4>
      <div class="content" style="font-size:12pt">您没有权限访问该目录</div>
    </td>
  </tr>
</table>
</div>
<br>
<br>
<table id="fileTable" class="TableBlock" width="100%" align="center">
  <tr>
    <td class="TableContent" nowrap align="center" width="80"><b>文件操作：</b></td>
    <td class="TableControl">&nbsp;
    	<a id='queryFile' href="<%=contextPath %>/subsys/oa/confidentialFile/showConfidentialFile/queryFile/query.jsp?seqId=<%=seqId %>" title="查询文件" style="height:20px;"><img src="<%=imgPath %>/folder_search.gif" align="middle" border="0">&nbsp;查询&nbsp;&nbsp;</a>
			<a id="globalQuery" href="<%=contextPath %>/subsys/oa/confidentialFile/showConfidentialFile/queryFile/globalQuery.jsp" title="全局搜索" style="height:20px;"><img src="<%=imgPath %>/folder_search.gif" align="middle" border="0">&nbsp;全局搜索&nbsp;&nbsp;</a>
 		</td>
  </tr>
</table>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<%
  String dwName=request.getParameter("dwName");
  String indexStyle=request.getParameter("indexStyle");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理员文件字</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/flowrun/docword/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/flowrun/docword/js/dwLogic.js"></script>

<script type="text/javascript">  
var dwName="<%=dwName%>";
var indexS="<%=indexStyle%>";
var pageMgr = null;

function doInit(){
  var url = "<%=contextPath%>/yh/core/funcs/workflow/act/YHDocWordAct/queryDocWordListJson.act?dwName="+dwName+"&indexStyle="+indexS;
  var cfgs = {
		    dataAction: url,
		    container: "listContainer",
		    sortIndex: 1,
		    sortDirect: "desc",
		    colums: [
		       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
		       {type:"data", name:"dwName",  width: '10%', text:"文件字名称" ,align: 'center' },
		       {type:"data", name:"indexStyle",  width: '15%', text:"序号样式" ,align: 'center' },
		       {type:"data", name:"deptPriv",  width: '20%', text:"部门权限" ,align: 'center',render:deptNameFunc },
		       {type:"data", name:"rolePriv",  width: '10%', text:"角色权限" ,align: 'center',render:roleNameFunc },
		       {type:"data", name:"userPriv",  width: '10%', text:"人员权限" ,align: 'center',render:userNameFunc }]
		  };

  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
    showCntrl('delOpt');
  }else{
    WarningMsrg('无查询结果', 'msrg');
  }
}
</script>
</head>
<body topmargin="5" onLoad="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absMiddle"><span class="big3">&nbsp;查询文件字</span>
   </td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>

<div id="msrg">
</div>
</body>
</html>
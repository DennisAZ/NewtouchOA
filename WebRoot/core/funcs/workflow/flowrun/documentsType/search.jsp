<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String documentsName = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("documentsName")));
  String flowType = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("flowType")));
  String documentsFont = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("documentsFont")));
  String documentsWordModel = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("documentsWordModel")));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件类型查询 </title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/manage/staffCare/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/flowrun/documentsType/js/documentsTypeLogic.js"></script>
<script> 
var pageMgr = null;
function doInit(){
 	var param = "";
  param = "documentsName=" + encodeURIComponent("<%=documentsName%>");
  param += "&flowType=" + encodeURIComponent("<%=flowType%>");
  param += "&documentsFont=" + encodeURIComponent("<%=documentsFont%>");
  param += "&documentsWordModel=" + encodeURIComponent("<%=documentsWordModel%>");
  var url = "<%=contextPath%>/yh/core/funcs/workflow/act/YHDocumentsTypeAct/queryDocumentsTypeList.act?"+param;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    colums: [
       {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data", name:"documentsName",  width: '20%', text:"文件名称" ,align: 'center' },
       {type:"data", name:"flowType",  width: '20%', text:"绑定流程" ,align: 'center' , render: renderFlowType},
       {type:"data", name:"documentsFont",  width: '20%', text:"文件字" ,align: 'center' , render: renderDwName},
       {type:"data", name:"documentsWordModel",  width: '10%', text:"套红模板" ,align: 'center' },
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
    WarningMsrg('无文件类型信息', 'msrg');
  }
}

function renderFlowType(cellData, recordIndex, columIndex){
  if(cellData == 1){
    return "发文管理";
  }
  return cellData;
}

function renderDwName(cellData, recordIndex, columIndex){
	  var urls = contextPath + "/yh/core/funcs/workflow/act/YHDocumentsTypeAct/getDocWordById.act?seqId="+cellData;
	  var rtJsons = getJsonRs(urls);
	  var prc = rtJsons.rtData;
	  if(rtJsons.rtState == '0'){
	    if(prc){
	      return "<center>" + prc + "<center>";
	    }
	    return  "<center><center>";
	  }else{
	    alert(rtJson.rtMsrg);
	  }
	}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;文件类型查询结果 </span>
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
<div id="backDiv" style="display:none" align="center">
<br>
  <input type="button" value="返回" class="BigButton" onclick="window.location.href='<%=contextPath %>/subsys/oa/hr/manage/staffCare/query.jsp';">&nbsp;&nbsp;
</div>

<div id="msrg">
</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String stampType = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("stampType")));
  String stampUser = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("stampUser")));
  String sendDate1 = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("sendDate1")));
  String sendDate2 = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("sendDate2")));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用印查询 </title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/sendDoc/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/sendDoc/js/sendLogic.js"></script>
<script> 
var pageMgr = null;
function doInit(){
 	var param = "";
  param = "stampType=" + encodeURIComponent("<%=stampType%>");
  param += "&stampUser=" + encodeURIComponent("<%=stampUser%>");
  param += "&sendDate1=" + encodeURIComponent("<%=sendDate1%>");
  param += "&sendDate2=" + encodeURIComponent("<%=sendDate2%>");
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/search/act/YHDocSearchAct/getStampListJson.act?" + param;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    colums: [
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data", name:"docTitle",  width: '15%', text:"公文名称" ,align: 'left' ,render: renderDocTitle},
       {type:"data", name:"stampType",  width: '10%', text:"盖章类型" ,align: 'center' ,render: renderStampType},
       {type:"data", name:"deptName",  width: '10%', text:"盖章部门" ,align: 'center'},
       {type:"data", name:"userName",  width: '10%', text:"盖章人员" ,align: 'center'},
       {type:"data", name:"stampTime",  width: '15%', text:"盖章时间" ,align: 'center' ,render: renderCreateDatetime}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
    showCntrl('delOpt');
  }else{
    WarningMsrg('无符合条件的收文登记信息', 'msrg');
  }
}

function renderStampType(cellData, recordIndex, columIndex){
  var str = "";
  if(cellData == 1){
    str = '协办盖章';
  }
  else{
    str = '主办盖章';
  }
  return str;
}

/**
 * 查看收文详情
 * @param paraName
 * @return
 */
function selectInfo(seqId){
  var url = contextPath + "/subsys/jtgwjh/receiveDoc/manage/details.jsp?seqId=" + seqId;
  newWindow(url,700,450,"info");
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif"><span class="big3"> 用印查询 </span></td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
</div>
<div id="msrg">
</div>
<div id="backDiv" align="center">
<br>
  <input type="button" value="返回" class="BigButton" onclick="window.location.href='<%=contextPath %>/subsys/jtgwjh/search/stampsQuery.jsp';">&nbsp;&nbsp;
</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String abroadUserId = request.getParameter("abroadUserId")== null ? "" :   YHUtility.encodeSpecial(request.getParameter("abroadUserId"));
  String abroadName = request.getParameter("abroadName")== null ? "" :   YHUtility.encodeSpecial(request.getParameter("abroadName"));
  String beginDate = request.getParameter("beginDate") == null ? "" : YHUtility.encodeSpecial(request.getParameter("beginDate"));
  String beginDate1 = request.getParameter("beginDate1") == null ? "" :   YHUtility.encodeSpecial(request.getParameter("beginDate1"));
  String endDate = request.getParameter("endDate") == null ? "" : YHUtility.encodeSpecial(request.getParameter("endDate"));
  String endDate1 = request.getParameter("endDate1") == null ? "" : YHUtility.encodeSpecial(request.getParameter("endDate1"));
  String remark = request.getParameter("remark") == null ? "" : YHUtility.encodeSpecial(request.getParameter("remark"));

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>出国记录查询 </title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/abroad/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/abroad/js/abroadRecordLogic.js"></script>
<script type="text/javascript">
var pageMgr = null;
function doInit(){
 	var param = "";
  param = "abroadUserId=" + encodeURIComponent("<%=abroadUserId%>");
  param += "&abroadName=" + encodeURIComponent("<%=abroadName%>");
  param += "&beginDate=" + encodeURIComponent("<%=beginDate%>");
  param += "&beginDate1=" + encodeURIComponent("<%=beginDate1%>");
  param += "&endDate=" + encodeURIComponent("<%=endDate%>");
  param += "&endDate1=" + encodeURIComponent("<%=endDate1%>");
  param += "&remark=" + encodeURIComponent("<%=remark%>");
  var url = "<%=contextPath%>/yh/subsys/oa/abroad/act/YHHrAbroadRecordAct/queryAbroadRecordListJson.act?" + param;
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"abroadUserId",  width: '20%', text:"出国人员", render:abroadUserIdFunc},       
         {type:"data", name:"abroadName",  width: '20%', text:"到访国家", render:recordCenterFunc},
         {type:"data", name:"beginDate",  width: '10%', text:"出国开始日期", render:beginDateFunc},
         {type:"data", name:"endDate",  width: '10%', text:"出国结束日期", render:endDateFunc},
         {type:"selfdef", text:"操作", width: '15%',render:opts}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      showCntrl('listContainer');
      var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
      showCntrl('backDiv');
    }else{
      WarningMsrg('无符合条件的出国记录', 'msrg');
    }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;查询结果</span>
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
         <a href="javascript:deleteAll();" title="删除所选出国记录"><img src="<%=imgPath%>/delete.gif" align="absMiddle">删除所选出国记录</a>&nbsp;
      </td>
 </tr>
</table>
</div>
<div id="backDiv" style="display:none" align="center">
<br>
  <input type="button" value="返回" class="BigButton" onclick="window.location.href='<%=contextPath %>/subsys/oa/abroad/record/query.jsp';">&nbsp;&nbsp;
</div>

<div id="msrg">
</div>
</body>
</html>
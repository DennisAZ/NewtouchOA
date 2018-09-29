<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String staffId = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("staffId")));
  String welfareItem = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("welfareItem")));
  String welfareMonth = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("welfareMonth")));
  String welfarePayment = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("welfarePayment")));
     
  String taxAffares = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("taxAffares")));
  String startDate = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("startDate")));
  String endDate = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("endDate")));
  String freeGift = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("freeGift")));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>员工福利查询 </title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/salary/welfare_manager/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/salary/welfare_manager/js/welfaremanageLogic.js"></script>
<script> 
var pageMgr = null;
function doInit(){
 	var param = "";
  param = "staffId=" + encodeURIComponent("<%=staffId%>");
  param += "&welfareItem=" + encodeURIComponent("<%=welfareItem%>");
  param += "&welfareMonth=" + encodeURIComponent("<%=welfareMonth%>");
  param += "&welfarePayment=" + encodeURIComponent("<%=welfarePayment%>");
  param += "&taxAffares=" + encodeURIComponent("<%=taxAffares%>");
  param += "&startDate=" + encodeURIComponent("<%=startDate%>");
  param += "&endDate=" + encodeURIComponent("<%=endDate%>");
  param += "&freeGift=" + encodeURIComponent("<%=freeGift%>");
 
  var url = "<%=contextPath%>/yh/subsys/oa/hr/salary/welfare_manager/act/YHHrWelfareManageAct/queryWelfareListJson.act?"+param;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    colums: [
       {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data", name:"staffName",  width: '20%', text:"单位员工" ,align: 'center' ,render:staffNameFunc},
       {type:"data", name:"welfareItem",  width: '20%', text:"福利项目" ,align: 'center' ,render:welfareItemFunc},
       {type:"data", name:"welfareMonth",  width: '20%', text:"工资月份" ,align: 'center' ,render:splitDateFunc},
       {type:"data", name:"paymentDate",  width: '10%', text:"发放日期" ,align: 'center' ,render:splitDateFunc},
       {type:"data", name:"welfarePayment",  width: '10%', text:"福利金额" ,align: 'center' ,render:careFeesRMB},
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
    WarningMsrg('无符合条件的员工福利信息', 'msrg');
  }
}

</script>
</head>
<body topmargin="5" onLoad="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath %>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;员工福利信息查询结果 </span>
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
  <input type="button" value="返回" class="BigButton" onClick="window.location.href='<%=contextPath %>/subsys/oa/hr/salary/welfare_manager/query.jsp';">&nbsp;&nbsp;
</div>

<div id="msrg">
</div>
</body>
</html>
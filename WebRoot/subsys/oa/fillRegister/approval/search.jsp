<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%
  String assessingOfficer = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("assessingOfficer")));
  String assessingStatus = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("assessingStatus")));
  String beginDate = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("beginDate")));
  String endDate = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("endDate")));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>补登记(审批)查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/fillRegister/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/fillRegister/js/registerApprovalLogic.js"></script>
<script type="text/javascript">
var pageMgr = null;
function doInit(){
  var param = "";
  param = "assessingOfficer=" + encodeURIComponent("<%=assessingOfficer%>");
  param += "&assessingStatus=" + encodeURIComponent("<%=assessingStatus%>");
  param += "&beginDate=" + encodeURIComponent("<%=beginDate%>");
  param += "&endDate=" + encodeURIComponent("<%=endDate%>");
  
  var url =  contextPath + "/yh/subsys/oa/fillRegister/act/YHAttendApprovalAct/queryApprovalListJson.act?" + param;
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"proposer", width: '10%', text:"申请人", render:proposerFunc},
         {type:"data", name:"registerType", width: '10%', text:"登记次序", render:registerTypeFunc},
         {type:"data", name:"fillTime", width: '10%', text:"补登记日期", render:fillTimeFunc},
         {type:"data", name:"assessingOfficer",  width: '10%', text:"审批人", render:assessingOfficerFunc},
         {type:"data", name:"assessingTime",  width: '10%', text:"审批时间", render:assessingTimeFunc},
         {type:"data", name:"assessingStatus",  width: '10%', text:"审批状态", render:assessingStatusFunc},
         {type:"selfdef", text:"操作", width: '10%',render:opts}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      count = total;
      showCntrl('listContainer');
      var mrs = " 共 "+ total + " 条记录 ！";
      showCntrl('backOpt');
    }else{
      WarningMsrg('无符合条件的审批记录', 'msrg');
      showCntrl('backOpt');
    }
}

/**
 * 查看页面格局css操作
 * @return
 */
function ShowDialog(){
  $("apply").style.left = (parseInt(document.body.clientWidth) - parseInt($("apply").style.width))/2;
  $("apply").style.top = 150;
  $("overlay").style.width = document.body.clientWidth;
  if(parseInt(document.body.scrollHeight) < parseInt(document.body.clientHeight)){
     $("overlay").style.height = document.body.clientHeight;
  }else{
   //$("overlay").style.height = document.body.scrollHeight;
   $("overlay").style.display = 'block';
   $("apply").style.display = 'block';
  }
  window.scroll(0,0);
}

function HideDialog(){
  var div = $('apply');
  div.style.display = "none";
  var overlay = $('overlay');
  overlay.style.display = "none";
}

function doSubmit(){
  if($("assessingView").value == ""){ 
    alert("请填写审批意见！");
    $("assessingView").focus();
	return false;
  }
  var pars = Form.serialize($('form1'));
  var url = contextPath + "/yh/subsys/oa/fillRegister/act/YHAttendApprovalAct/updateStatus.act";
  var rtJson = getJsonRs(url, pars);
  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;
    location = "<%=contextPath %>/subsys/oa/fillRegister/approval/approval.jsp?data="+data;
  } else {
    alert(rtJson.rtMsrg); 
  }
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="middle"><span class="big3"> 查询结果</span>
    </td>
  </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>

<div id="msrg">
</div>

<div id="backOpt" style="display:none" align="center">
<br>
  <input type="button" value="返回" class="BigButton" onclick="window.location.href='<%=contextPath %>/subsys/oa/fillRegister/approval/query.jsp';">&nbsp;&nbsp;
</div>

<div id="overlay" style="width: 1238px; height: 610px; display: '';"></div>
<div id="apply" class="ModalDialog" style="display:none;width:400px;">
  <div class="header"><span id="title" class="title">审批意见</span><a class="operation" href="javascript:HideDialog();"><img src="<%=imgPath%>/close.png"/></a></div>
  <form name="form1" id="form1" method="post" action="">
  <div id="detail_body" class="body">
  <span id="confirm"></span>
  <textarea id="assessingView" name="assessingView" cols="45" rows="5" style="overflow-y:auto;" class="BigInput" wrap="yes"></textarea>
  </div>
  <input type="hidden" name="seqId" id="seqId">
  <input type="hidden" name="assessingStatus" id="assessingStatus">
  <div id="footer" class="footer">
    <input class="BigButton" type="button" value="确定" onclick="doSubmit();"/>
    <input class="BigButton" onclick="HideDialog()" type="button" value="关闭"/>
  </div>
  </form>
</div>


</body>
</html>
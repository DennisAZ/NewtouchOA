<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
 <%
  String assessingStatus = request.getParameter("assessingStatus") == null ? "" :  request.getParameter("assessingStatus");
  String name = "";
  if (assessingStatus.equals("0")) {
    name = "待批计划";
  }
  if (assessingStatus.equals("1")) {
    name = "已准计划";
  }
  if (assessingStatus.equals("2")) {
    name = "未批准计划";
  }
%>

<html>
<head>
<title>培训计划审批</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/training/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/training/js/trainingapprovallogic.js"></script>
<script> 
var assessingStatus = "<%=assessingStatus%>";
var name = "<%=name%>";
var pageMgr = null;
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/training/act/YHTrainingApprovalAct/getTrainingApprovalListJson.act?assessingStatus="+assessingStatus;
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"hidden", name:"assessingStatus", text:"顺序号", dataType:"int"},
         {type:"data", name:"tPlanNo",  width: '20%', text:"培训计划编号", render:trainingCenterFunc},       
         {type:"data", name:"tPlanName",  width: '10%', text:"培训计划名称", render:trainingCenterFunc},
         {type:"data", name:"tChannel",  width: '10%', text:"培训渠道", render:tChannelFunc},
         {type:"data", name:"tCourseTypes",  width: '10%', text:"培训形式", render:tCourseTypesFunc},
         {type:"data", name:"tAddress",  width: '10%', text:"培训地点", render:trainingCenterFunc},
         {type:"selfdef", text:"操作", width: '20%',render:opts}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      showCntrl('listContainer');
      var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
      WarningMsrg('没有'+name, 'msrg');
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
  var url = contextPath + "/yh/subsys/oa/training/act/YHTrainingApprovalAct/updateStatus.act";
  var rtJson = getJsonRs(url, pars);
  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;
    location = "<%=contextPath %>/subsys/oa/training/approval/approval.jsp?data="+data;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/meeting.gif" width="22" height="18"><span class="big3">&nbsp;<%=name%></span>
   </td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">

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
<div id="msrg">
</div>
</body>
</html>
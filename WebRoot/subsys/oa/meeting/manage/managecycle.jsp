<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
 <%
  String mStatus = request.getParameter("mStatus") == null ? "" :  request.getParameter("mStatus");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>会议管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/meetingmanagelogic.js"></script>
<script> 
var mStatus = "<%=mStatus%>";
var pageMgr = null;
var day1 = "";
var day2 = "";
var day3 = "";
var day4 = "";
var day5 = "";
var day6 = "";
var day7 = "";
var parm = "";
var flag = "";
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingAct/getMeetingManageCycleList.act?cycleNo=${param.cycleNo}";
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      paramFunc: getQueryParam,
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"mrName",  width: '20%', text:"名称", render:mrNameFunc},       
         {type:"data", name:"mProposer",  width: '10%', text:"申请人", render:mProposerFunc},
         {type:"data", name:"mStart",  width: '10%', text:"开始时间", render:mStartFunc},
         {type:"data", name:"mRoom",  width: '10%', text:"会议室", render:mRoomFunc},
         {type:"hidden", name:"cycleNo",  width: '10%', text:"会议周期号", render:mRoomFunc},
         <%if("0".equals(mStatus)){%>
         {type:"data", name:"mStatus",  width: '10%', text:"预约状态", render:mStatusFunc},
         <%}else{%>
         {type:"hidden", name:"mStatus",  width: '10%', text:"预约状态", render:mStatusFunc},
         <%}%>
         {type:"selfdef", text:"操作", width: '20%',render:optsCycle}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      $("totalSpan").innerHTML = total;
      showCntrl('listContainer');
      var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
      $("spanDiv").style.display = 'none';
      WarningMsrg('无待批周期性会议', 'msrg');
    }
}

function getQueryParam() {
  return $("form1").serialize();
}

function myQuery(){
  if($("W1").checked){
    $("W11").value = $("W1").value;
  }else{
    $("W11").value = "";
  }
  
  if($("W2").checked){
    $("W12").value = $("W2").value;
  }else{
    $("W12").value = "";
  }
  
  if($("W3").checked){
    $("W13").value = $("W3").value;
  }else{
    $("W13").value = "";
  }
  
  if($("W4").checked){
    $("W14").value = $("W4").value;
  }else{
    $("W14").value = "";
  }
  
  if($("W5").checked){
    $("W15").value = $("W5").value;
  }else{
    $("W15").value = "";
  }
  
  if($("W6").checked){
    $("W16").value = $("W6").value;
  }else{
    $("W16").value = "";
  }
  
  if($("W7").checked){
    $("W17").value = $("W7").value;
  }else{
    $("W17").value = "";
  }
  
  $("flag").value = "1";
  pageMgr.search();
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/meeting.gif" width="22" height="18"><span class="big3">&nbsp;待批周期性会议</span>
   </td>
   <td valign="bottom" class="small1"><div id="spanDiv" style="display:'';">共&nbsp;<span class="big4" id="totalSpan"></span>&nbsp;条会议记录</div>
    </td>
 </tr>
</table>
<br>
<table>

</table>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
<table class="TableList" width="100%">
<tr class="TableControl">
<td colspan="9">
  &nbsp;<input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this)"><label for='checkAlls'>全选</label> &nbsp;
    <a href="javascript:checkUpAll();" title="批量批准待批会议"><img src="<%=imgPath%>/user_group.gif" align="absMiddle">批量批准待批会议</a>&nbsp;
</td>
</tr>
</table>
</div>

<div id="msrg">
</div>
<div>
<form id="form1" method="post" name="form1">
<table class="TableList" width="100%">
<tr>
    <td nowrap class="TableContent" width="100"> 申请星期：</td>
    <td class="TableData" colspan="3">
     <input type="hidden" id="flag" name="flag" value="0">
     <input type="hidden" id="W11" name="W11" value="">
     <input type="hidden" id="W12" name="W12" value="">
     <input type="hidden" id="W13" name="W13" value="">
     <input type="hidden" id="W14" name="W14" value="">
     <input type="hidden" id="W15" name="W15" value="">
     <input type="hidden" id="W16" name="W16" value="">
     <input type="hidden" id="W17" name="W17" value="">
     <span id="WEEKEND1"><input type="checkbox" name="W1" id="W1" value="1" checked>星期一</span>
     <span id="WEEKEND2"><input type="checkbox" name="W2" id="W2" value="2" checked>星期二</span>
     <span id="WEEKEND3"><input type="checkbox" name="W3" id="W3" value="3" checked>星期三</span>
     <span id="WEEKEND4"><input type="checkbox" name="W4" id="W4" value="4" checked>星期四</span>
     <span id="WEEKEND5"><input type="checkbox" name="W5" id="W5" value="5" checked>星期五</span>
     <span id="WEEKEND6"><input type="checkbox" name="W6" id="W6" value="6" checked>星期六</span>
     <span id="WEEKEND0"><input type="checkbox" name="W7" id="W7" value="7" checked>星期日</span>
     <input type="button" onclick="myQuery()" value="查询">
    </td>
  </tr>
</table>
</form>
</div>
</body>
</html>
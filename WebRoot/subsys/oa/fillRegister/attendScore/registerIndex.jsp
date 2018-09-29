<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建个人补登记</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/fillRegister/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/fillRegister/js/attendScoreLogic.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/fillRegister/js/registerApprovalLogic.js"></script>
<script type="text/javascript">

function addRegister(){
  window.location.href = "/yh/subsys/oa/fillRegister/attendScore/newRegister.jsp";
}

function historyRegister(){
  window.location.href = "/yh/subsys/oa/fillRegister/attendScore/historyRegister.jsp";
}

var pageMgr = null;
function doInit(){
  var assessingStatus = 2;
  var url = "<%=contextPath%>/yh/subsys/oa/fillRegister/act/YHAttendApprovalAct/getRegisterApprovalListJson.act?assessingStatus="+assessingStatus;
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"assessingStatus",  width: '10%', text:"审批状态", render:assessingStatusFunc},
         {type:"data", name:"proposer", width: '20%', text:"申请人", render:proposerFunc},
         {type:"data", name:"registerType", width: '20%', text:"登记次序", render:registerTypeFunc},
         {type:"data", name:"fillTime", width: '15%', text:"补登记日期", render:fillTimeFunc},
         {type:"data", name:"assessingOfficer",  width: '20%', text:"审批人", render:assessingOfficerFunc},    
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
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/yh/core/styles/style3/img/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;个人补登记</span><br>
    </td>
  </tr>
</table>
<br>
<div align="center">
<input type="button"  value="新建个人补登记" class="BigButtonC" onClick="addRegister();" title="新建个人补登记">&nbsp;&nbsp;
<input type="button"  value="个人补登记历史记录" class="BigButtonC" onClick="historyRegister();" title="查看过往的个人补登记记录">
</div>
<br>
<br>
<div id = "listDiv"  align="center"></div>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
<table class="TableList" width="100%">
<tr class="TableControl">
<td colspan="9">
  &nbsp;<input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this)"><label for='checkAlls'>全选</label> &nbsp;
    <input type="button"  value="批量删除" class="BigButton" onClick="deleteAll()" title="删除所有卷库">
</td>
</tr>
</table>

<div id="msrg">
</div>
</body>
</html>
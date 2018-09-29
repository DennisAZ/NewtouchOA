<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>

<html>
<head>
<title>补登记历史记录</title>
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
<script> 

var pageMgr = null;
function doInit(){
  var assessingStatus = "1";
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
      WarningMsrg('无补登记历史记录', 'msrg');
    }
}


</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/meeting.gif" width="22" height="18"><span class="big3">&nbsp;补登记历史记录</span>
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
  &nbsp;<input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this)"><label for='checkAlls'>全选</label> &nbsp;
    <input type="button"  value="批量删除" class="BigButton" onClick="deleteAll()" title="删除所有卷库">
</td>
</tr>
</table>
</div>
<div id="msrg">
</div>
<br>
<div align="center"><br><input type="button" value="返回上页" class="BigButton" onclick="location='registerIndex.jsp'">&nbsp;&nbsp; </div>
</body>
</html>
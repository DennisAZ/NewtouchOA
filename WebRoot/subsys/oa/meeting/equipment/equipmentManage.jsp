<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>设备管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/meetingequipmentlogic.js"></script>
<script> 
var pageMgr = null;

function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingEquipmentAct/getMeetingEquipmentList.act";
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      colums: [
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"hidden", name:"groupNo", text:"顺序号", dataType:"int"},
         {type:"data", name:"equipmentNo",  width: '10%', text:"设备编号", align: "center", render:equipmentCenterFunc},       
         {type:"data", name:"equipmentName",  width: '10%', text:"设备名称", align: "center", render:equipmentCenterFunc},
         {type:"data", name:"mrId",  width: '10%', text:"所属会议室", align: "center", render:mRoomFunc},
         {type:"data", name:"equipmentStatus",  width: '10%', text:"设备状态", align: "center", render:equipmentStatusFunc},
         {type:"data", name:"groupYn",  width: '10%', text:"同类设备名称", align: "center", render:groupYnFunc},
         {type:"data", name:"remark",  width: '10%', text:"设备描述", align: "center", render:equipmentCenterFunc},
         {type:"selfdef", text:"操作", width: '10%', align: "center", render:opts}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      showCntrl('listContainer');
      var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
     // $("spanDiv").style.display = 'none';
      WarningMsrg('无记录', 'msrg');
    }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/meeting.gif" align="absmiddle"><span class="big3"> 设备管理</span>&nbsp;</td>
    <td align="right" valign="bottom" class="small1"></td>
  </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
</div>
<div id="msrg">
</div>
</body>
</html>
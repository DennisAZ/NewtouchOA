<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%
  String equipmentNo = request.getParameter("equipmentNo")== null ? "" :   YHUtility.encodeSpecial(request.getParameter("equipmentNo"));
  String equipmentName = request.getParameter("equipmentName")== null ? "" :   YHUtility.encodeSpecial(request.getParameter("equipmentName"));
  String equipmentStatus = request.getParameter("equipmentStatus") == null ? "" :   YHUtility.encodeSpecial(request.getParameter("equipmentStatus"));
  String mrId = request.getParameter("mrId") == null ? "" :   YHUtility.encodeSpecial(request.getParameter("mrId"));
  String remark = request.getParameter("remark") == null ? "" :  YHUtility.encodeSpecial(request.getParameter("remark"));

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>会议设备查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/meetingequipmentlogic.js"></script>
<script type="text/javascript">
var pageMgr = null;
function doInit(){
  var param = "";
  param =  "equipmentNo="+encodeURI('<%=equipmentNo%>')+"&equipmentName="+encodeURI('<%=equipmentName%>')+"&equipmentStatus="+encodeURI('<%=equipmentStatus%>')+"&mrId="; 
  param +=  encodeURI('<%=mrId%>')+"&remark="+encodeURI('<%=remark%>')+"";
  var url =  contextPath + "/yh/subsys/oa/meeting/act/YHMeetingEquipmentAct/getSearchMeetingEquipment.act?" + param;
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      colums: [
             {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
             {type:"hidden", name:"groupNo", text:"顺序号", dataType:"int"},
             {type:"data", name:"equipmentNo",  width: '10%', text:"设备编号", render:equipmentCenterFunc},       
             {type:"data", name:"equipmentName",  width: '10%', text:"设备名称", render:equipmentCenterFunc},
             {type:"data", name:"mrId",  width: '10%', text:"所属会议室", render:mRoomFunc},
             {type:"data", name:"equipmentStatus",  width: '10%', text:"设备状态", render:equipmentStatusFunc},
             {type:"data", name:"groupYn",  width: '10%', text:"同类设备名称", render:groupYnFunc},
             {type:"data", name:"remark",  width: '10%', text:"设备描述", render:equipmentCenterFunc},
             {type:"selfdef", text:"操作", width: '10%',render:opts}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      count = total;
      showCntrl('listContainer');
      var mrs = " 共 "+ total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
      WarningMsrg('无相关记录', 'msrg');
      showCntrl('delOpt');
    }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absmiddle"><span class="big3"> 会议设备查询结果</span>
    </td>
  </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="msrg">
</div>
<div id="delOpt" style="display:none" align="center">
<br>
  <input type="button" value="返回" class="BigButton" onclick="window.location.href='<%=contextPath %>/subsys/oa/meeting/equipment/query.jsp';">&nbsp;&nbsp;
</div>
</form>
</body>
</html>
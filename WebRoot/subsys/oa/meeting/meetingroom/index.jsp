<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>会议室设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/meetingroomlogic.js"></script>
<script type="text/javascript">
var pageMgr = null;
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingRoomAct/getMeetingRoomJson.act";
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"mrName",  width: '10%', text:"名称", render:meetingRoomCenterFunc},       
         {type:"data", name:"mrCapacity",  width: '10%', text:"可容纳人数", render:meetingRoomCenterFunc},
         {type:"data", name:"mrDevice",  width: '10%', text:"设备情况", render:meetingRoomCenterFunc},
         {type:"data", name:"mrPlace",  width: '10%', text:"所在地点", render:meetingRoomCenterFunc},
         {type:"data", name:"mrDesc",  width: '10%', text:"会议室描述", render:mrDescFunc},
         {type:"data", name:"operator",  width: '10%', text:"会议室管理员", render:operatorFunc},
         {type:"data", name:"toId",  width: '15%', text:"申请权限（部门）", render:toIdFunc}, 
         {type:"data", name:"secretToId",  width: '15%', text:"申请权限（人员）", render:secretToIdFunc}, 
         {type:"selfdef", text:"操作", width: '15%',render:opts}]
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
      WarningMsrg('无定义的会议室', 'msrg');
    }
}

function doSubmit(){
 
}

</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 新建会议室</span>
    </td>
  </tr>
</table>
<div align="center">
  <input type="button"  value="新建会议室" class="BigButton" onClick="location='newroom.jsp';" title="新建会议室，并添加基本信息">
</div>
<br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absmiddle"><span class="big3"> 管理会议室</span>
    </td>
    <td valign="bottom" class="small1"><div id="spanDiv" style="display:'';">共&nbsp;<span class="big4" id="totalSpan"></span>&nbsp;个会议室</div>
    </td>
  </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
<table class="TableList no-top-border" width="100%">
<tr class="TableData">
<td colspan="9" align="center">
    <input type="button"  value="全部删除" class="BigButton" onClick="deleteAll()" title="删除所有会议室">
</td>
</tr>
</table>
</div>
<div id="msrg">
</div>
<div>
<input type="hidden" value="" name="operator" id="operator">
<input type="hidden" value="" name="operatorDesc" id="operatorDesc">
<input type="hidden" value="" name="toId" id="toId">
<input type="hidden" value="" name="toIdDesc" id="toIdDesc">
<input type="hidden" value="" name="secretToId" id="secretToId">
<input type="hidden" value="" name="secretToIdDesc" id="secretToIdDesc">
</div>
</body>
</html>
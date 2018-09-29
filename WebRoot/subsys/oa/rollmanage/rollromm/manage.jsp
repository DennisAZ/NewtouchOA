<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="yh.core.global.YHRegistProps" %>
<%@ page import="yh.core.data.YHAuthKeys" %>
<%
  boolean regist = YHUtility.isNullorEmpty(YHRegistProps.getProp(YHAuthKeys.REGIST_ORG + ".yh"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>卷库管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/rmsrollroomlogic.js"></script>
<script type="text/javascript">
var regist = "<%=regist%>";
var pageMgr = null;
function doInit(){
  if(!regist){
    $("registDiv").style.display = '';
  }else{
    $("registDiv").style.display = 'none';
  }
  var url = "<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsRollRoomAct/getRmsRollRoomJosn.act";
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 2,
      sortDirect: "desc",
      colums: [
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"roomCode",  width: '20%', text:"卷库号", render:roomCodeFunc, sortDef:{type: 0, direct:"desc"}},       
         {type:"data", name:"roomName",  width: '28%', text:"卷库名称", render:roomNameFunc,sortDef:{type: 0, direct:"desc"}},
         {type:"data", name:"deptId",  width: '25%', text:"所属部门", render:getDeptNameFunc}, 
         {type:"selfdef", text:"操作", width: '25%',render:opts}]
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
      WarningMsrg('无卷库', 'msrg');
    }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absmiddle"><span class="big3"> 管理卷库</span>&nbsp;
    </td>
  </td>
    </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:98%;">
</div>
<div id="delOpt" style="display:none">
<table class="TableList" width="98%">
<tr class="TableControl">
<td colspan="9" align="center">
    <input type="button"  value="全部删除" class="BigButton" onClick="deleteAll()" title="删除所有卷库">
</td>
</tr>
</table>
</div>
<div id="msrg">
</div>
<br><br><div id="registDiv" style="display:'none'" align=center style='font-size:9pt;color:gray'>
</div>
</body>
</html>
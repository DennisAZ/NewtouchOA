<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String staffName = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("staffName")));
  String member = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("member")));
  String relationship = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("relationship")));
  String jobOccupation = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("jobOccupation")));
  String workUnit = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("workUnit")));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>社会关系信息查询 </title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/manage/staffRelatives/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/manage/staffRelatives/js/staffRelativesLogic.js"></script>
<script> 
var pageMgr = null;
function doInit(){
 	var param = "";
  param = "staffName=" + encodeURIComponent("<%=staffName%>");
  param += "&member=" + encodeURIComponent("<%=member%>");
  param += "&relationship=" + encodeURIComponent("<%=relationship%>");
  param += "&jobOccupation=" + encodeURIComponent("<%=jobOccupation%>");
  param += "&workUnit=" + encodeURIComponent("<%=workUnit%>");
  var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/relatives/act/YHHrStaffRelativesAct/queryRelativesListJson.act?" + param;
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
               {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
               {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
               {type:"data", name:"staffName",  width: '20%', text:"单位员工" ,align: 'center'},
               {type:"data", name:"member",  width: '20%', text:"成员姓名" ,align: 'center'},
               {type:"data", name:"relationship",  width: '10%', text:"与本人关系" ,align: 'center' ,render:relativesItemFunc},
               {type:"data", name:"jobOccupation",  width: '10%', text:"职业" ,align: 'center'},
               {type:"data", name:"personalTel",  width: '10%', text:"联系电话（个人）" ,align: 'center'},
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
      WarningMsrg('无符合条件的社会关系信息', 'msrg');
    }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;社会关系信息查询结果 </span>
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
  <input type="button" value="返回" class="BigButton" onclick="window.location.href='<%=contextPath %>/subsys/oa/hr/manage/staffRelatives/query.jsp';">&nbsp;&nbsp;
</div>

<div id="msrg">
</div>
</body>
</html>
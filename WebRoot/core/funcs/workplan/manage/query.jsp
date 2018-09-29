<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null) {
    seqId = "";
  }
  String treeId = request.getParameter("treeId");
  if (treeId == null) {
    treeId = "";
  }
  String deptParent = request.getParameter("deptParent");
  if (deptParent == null) {
    deptParent = "";
  }
  String TO_ID = request.getParameter("TO_ID");
  String deptLocal = request.getParameter("deptLocal");
  if (deptLocal == null){
    deptLocal = "";
  }
  String TO_NAME = request.getParameter("TO_NAME");
  String deptParentDesc = request.getParameter("deptParentDesc");
  if (deptParentDesc == null) {
    deptParentDesc = "";
  }
  String parentdesc = (String)request.getAttribute("desc");
  if (parentdesc == null) {
    parentdesc = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工作计划</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tree.css">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryLogic.js"></script>

<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
  var TO_ID = "<%=TO_ID%>";
  var TO_NAME = "<%=TO_NAME%>";
  var treeId = "<%=treeId%>";
  var deptLocal = "<%=deptLocal%>";
  var deptParent = "<%=deptParent%>";
  var deptParentDescValue = "<%=deptParentDesc%>";
  var parentdesc = "<%=parentdesc%>";

  function doInit() {
    planType();
    Init();
    //var mgr = new SelectMgr();
    //mgr.addSelect({cntrlId: "deptParent", tableName: "oa_department", codeField: "SEQ_ID", nameField: "DEPT_NAME", value: <%=deptParent%>, isMustFill: "1", filterField: "", filterValue: "", order: "", reloadBy: "", actionUrl: ""});
   //mgr.loadData();
  //mgr.bindData2Cntrl();
  }

  function clearUser(TO_ID, TO_NAME) {
    if (TO_ID == "" || TO_ID == "undefined" || TO_ID == null) {
      TO_ID = "TO_ID";
      TO_NAME = "TO_NAME";
    }
    document.getElementsByName(TO_ID)[0].value = "";
    document.getElementsByName(TO_NAME)[0].value = "";
  }

  function Init() { 
    showCalendar('statrTime',false,'beginDateImg');
    showCalendar('endTime',false,'endDateImg');
  }
  function planType() {
    var url = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanTypeAct/planType.act";
    var json = getJsonRs(url);
    var rtData = json.rtData;
    for(var i = 0; i < rtData.length; i++) {
      var opt = document.createElement("option");      
      opt.value = rtData[i].seqId;      
      opt.text = rtData[i].typeName;   
      var selectObj = $('WORK_TYPE');
      selectObj.options.add(opt, selectObj.options ? selectObj.options.length : 0); 
    }
  }

  function addDept() {
    var URL="<%=contextPath%>/core/funcs/orgselect/MultiDeptSelect.jsp";
      openDialog(URL,'500', '500');
  }
</script>
</head>
<body topmargin="5" onLoad="doInit()">
<div id="Dis">
<table border="0" width="90%" cellspacing="0" cellpadding="3"
  class="small">
  <tr>
    <td><img src="<%=imgPath%>/edit.gif"
      width="18" height="18"><span class="big3">
    工作计划查询</span></td>
  </tr>
</table>
<br>
<form name="form1" id="form1" action="<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanWorkAct/selectRes.act">
<table class="TableBlock" width="500" align="center">
  <tr class="TableHeader">
    <td colspan="2" nowrap class=""><img
      src="<%=imgPath%>/edit.gif" width="18" height="18">&nbsp;<span class="big3">
    工作计划查询</span></td>
  </tr>
  <tr>
    <td nowrap class="TableContent">&nbsp;计划名称：</td>
    <td class="TableData"><input type="text" name="NAME" size="36"
      id="NAME" maxlength="200" class="BigInput" value=""></td>
  </tr>
  <tr>
    <td class="TableContent">&nbsp;计划内容：</td>
    <td class="TableData"><input type="text" name="CONTENT"
      id="CONTENT" size="36" maxlength="200" class="BigInput" value=""></td>
  </tr>
 <tr>
<td nowrap class="TableContent">&nbsp;有效期：</td>
<td class="TableData">&nbsp;开始日期： <input type="text" id="statrTime" name="statrTime" size="10"
maxlength="19" class="BigInput" value="" readonly> <img
id="beginDateImg" src="<%=imgPath%>/calendar.gif"
align="absMiddle" border="0" style="cursor: hand"><br>&nbsp;结束日期： <input
type="text" id="endTime" name="endTime" size="10" maxlength="19"
class="BigInput" value="" readonly> <img id="endDateImg"
src="<%=imgPath%>/calendar.gif" align="absMiddle"
border="0" style="cursor: hand"></td> 
</tr>
  <tr>
    <td class="TableContent">&nbsp;计划类型：</td>
    <td class="TableData">&nbsp;
    <select name="WORK_TYPE" id="WORK_TYPE">
      <option value="">所有计划</option>
    </select>
    </td>
  </tr>
  <!--  treeId -->
  <tr>
    <td nowrap class="TableContent">&nbsp;发布范围(部门)：</td>
   <td class="TableData"><input type="hidden" name="deptParent"
id="dept" class="BigInput" size="25" maxlength="25" value="">
<textarea cols="45" name="deptParentDesc" id="deptDesc" rows="2"
style="overflow-y: auto;" class="SmallStatic" wrap="yes" readonly></textarea>
<a href="javascript:;" class="orgAdd" onClick="javascript:addDept();">添加</a>
<a href="javascript:;" class="orgClear" onClick="clearUser('deptParent', 'deptParentDesc')">清空</a></td>
  </tr>
  <tr>
    <td class="TableContent">&nbsp;发布范围(人员)：</td>
    <td nowrap class="TableData"><input type="hidden" name="manager"
      id="manager" value=""> <textarea cols="45" name="managerDesc"
      id="managerDesc" rows="2" style="overflow-y: auto;"
      class="SmallStatic" wrap="yes" readonly></textarea> <a
      href="javascript:;" class="orgAdd"
      onClick="selectUser(['manager', 'managerDesc'])">添加</a> <a
      href="javascript:;" class="orgClear"
      onClick="clearUser('manager', 'managerDesc')">清空</a></td>
  </tr>
  <tr>
    <td class="TableContent">&nbsp;参与人：</td>
    <td nowrap class="TableData"><input type="hidden" name="leader1"
      id="leader1" value=""> <textarea cols="45" name="leader1Desc"
      id="leader1Desc" rows="2" style="overflow-y: auto;"
      class="SmallStatic" wrap="yes" readonly></textarea> <a
      href="javascript:;" class="orgAdd"
      onClick="selectUser(['leader1', 'leader1Desc'])">添加</a> <a
      href="javascript:;" class="orgClear"
      onClick="clearUser('leader1', 'leader1Desc')">清空</a></td>
  </tr>
  <tr>
    <td class="TableContent">&nbsp;负责人：</td>
    <td nowrap class="TableData"><input type="hidden" name="leader2"
      id="leader2" value=""> <textarea cols="45" name="leader2Desc"
      id="leader2Desc" rows="2" style="overflow-y: auto;"
      class="SmallStatic" wrap="yes" readonly></textarea> <a
      href="javascript:;" class="orgAdd"
      onClick="selectUser(['leader2', 'leader2Desc'])">添加</a> <a
      href="javascript:;" class="orgClear"
      onClick="clearUser('leader2', 'leader2Desc')">清空</a></td>
  </tr>
  <tr>
    <td class="TableContent">&nbsp;批注领导：</td>
    <td nowrap class="TableData"><input type="hidden" name="leader3"
      id="leader3" value=""> <textarea cols="45" name="leader3Desc"
      id="leader3Desc" rows="2" style="overflow-y: auto;"
      class="SmallStatic" wrap="yes" readonly></textarea> <a
      href="javascript:;" class="orgAdd"
      onClick="selectUser(['leader3', 'leader3Desc'])">添加</a> <a
      href="javascript:;" class="orgClear"
      onClick="clearUser('leader3', 'leader3Desc')">清空</a></td>
  </tr>
  <tr>
    <td nowrap class="TableContent">&nbsp;备注：</td>
    <td class="TableData"><input type="text" name="REMARK" size="40"
      id="REMARK" maxlength="200" class="BigInput" value=""></td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan="2" nowrap><input type="submit" value="查询 " class="BigButton">&nbsp;&nbsp;
    <input type="button" value="返回 " class="BigButton" onClick="javascript:history.back();">
    </td>
  </tr>
</table>
</form>
</div>
</body>
</html>
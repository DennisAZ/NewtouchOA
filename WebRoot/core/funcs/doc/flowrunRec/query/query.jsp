<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson,yh.core.global.YHSysProps" %>
<%@ include file="/core/inc/header.jsp" %>
<%
YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
int userId = loginUser.getSeqId();
String userName = loginUser.getUserName();
String sortId = request.getParameter("sortId");
if (sortId == null) {
  sortId = "";
}
String skin = request.getParameter("skin");
String skinJs = "messages";
if (skin != null && !"".equals(skin)) {
  skinJs = "messages_" + skin;
} else {
  skin = "";
}
String flowName = request.getParameter("flowName");
String flowId = request.getParameter("flowId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工作流高级查询</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/orgselect.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/utility.js"></script>
  <script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/skin.js"></script>
<script type="text/javascript" src="js/query.js"></script>
<script type="text/javascript">
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";
var flowId = "<%=flowId%>";
function htmlSubmit() {
  document.form1.action= contextPath + "<%=moduleSrcPath %>/act/YHWorkQueryAct/doQuery.act";
  document.form1.target="_self";
  document.form1.submit();
}
function set_user(select){
  if(select.value=="1"){
     $('beginUser').value = '<%=userId%>';
     $('beginName').value = '<%=userName%>';
  }
}
function flowReport(op) {
  $('OP').value = op;
  var url = contextPath + "<%=moduleSrcPath %>/act/YHWorkQueryAct/flowReport.act";
  document.form1.action= url;
  document.form1.target="_blank";
  document.form1.submit();
}
</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle">
      <span class="big3" id="span1"> </span>&nbsp;
    </td>
  </tr>
</table>

<br>
<form id="form1" name="form1"  method="post" >
<input type=hidden value="<%=sortId %>" name="sortId" id="sortId"/>
<input type=hidden value="<%=skin %>" name="skin" id="skin"/>
<table border="0" width="90%" align="center" class="TableList">
    <tr style="display:none">
      <td nowrap class="TableHeader" height=25 colspan="3">
        <div style="float:left;font-weight:bold">
          <img src="<%=imgPath%>/green_arrow.gif" align="absmiddle">
          选择查询模板
          &nbsp;&nbsp;
          <a href="javascript:define_query_tpl_for_self('1');"><U>自定义模板</U> </a>
        </div>
      </td>
    </tr>
    <tr  style="display:none">
      <td class="TableData">
        <input type="radio" onclick="select_one_query_tpl('1')"  ><label>111111 </label>
      </td>
      <td class="TableData">
        <input type="radio" onclick="select_one_query_tpl('2')"  ><label>44444444 </label>
      </td>
      <td class="TableData">
        &nbsp;
      </td>
    </tr>
 
    <tr align="center" class="TableFooter"  style="display:none">
      <td colspan="3" nowrap>
        <input type="button" value="查询列表" class="BigButton" onclick="htmlSubmit()">&nbsp;&nbsp;
        <input type="button" value="HTML统计报表" class="BigButtonC" onclick="">&nbsp;&nbsp;
        <input type="button" value="EXCEL统计报表" class="BigButtonC" onclick="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableHeader" height=25 colspan="3"> <div style="float:left;font-weight:bold"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle"><span  id="span3"></span> </div></td>
    </tr>
    <tr>
      <td nowrap class="TableContent"><b>流程名称：</b></td>
      <td class="TableData" colspan="2">
        <%=flowName %><input type="hidden" name="flowId" id="flowId" value="<%=flowId%>">
        <input type="hidden" name="flowName" id="flowName" value="<%=flowName%>">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"><b><span id="span2"></span></b></td>
      <td class="TableData" colspan="2">
        <select name="flowStatus" id="flowStatus" class="SmallSelect">
          <option value="ALL" >所有</option>
          <option value="1" >已结束</option>
          <option value="0" >执行中</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"><b>查询范围：</b></td>
      <td class="TableData" colspan="2">
        <select name="flowQueryType" id="flowQueryType" class="SmallSelect" onchange="set_user(this);">
          <option value="0" >所有</option>
          <option value="1" >我发起的</option>
          <option value="2" >我经办的</option>
          <option value="3" >我管理的</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"><b>流程发起人：</b></td>
      <td class="TableData" colspan="2">
         <input type="text" name="beginName" id="beginName" size="10" class="SmallInput" readonly value="">&nbsp;
         <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['beginUser', 'beginName'])" title="指定代办人">选择</a>
          <a href="javascript:;" class="orgClear" onClick="$('beginName').value='';$('beginUser').value='';">清空 </a>
         <input type="hidden" name="beginUser" id="beginUser" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"><b>名称/文号：</b></td>
      <td nowrap class="TableData"  width="100px">
       <select name="runNameRelation" id="runNameRelation" class="SmallSelect">
        <option value="1" >等于</option>
        <option value="2" >大于</option>
        <option value="3" >小于</option>
        <option value="4" >大于等于</option>
        <option value="5" >小于等于</option>
        <option value="6" >不等于</option>
        <option value="7" >开始为</option>
        <option value="8" selected>包含</option>
        <option value="9" >结束为</option>
       </select>
      </td>
      <td class="TableData">
        <input type="text" id="runName" name="runName" value="" size="30" maxlength="100" class="SmallInput">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"><b>流程开始日期范围：</b></td>
      <td class="TableData" colspan="2">
                从 <input type="text" name="prcsDate1" id=prcsDate1 size="10" class="SmallInput" value="" readonly>
      <img id="prcsDate1Img" src="<%=imgPath%>/calendar.gif" align="absMiddle" align=absmiddle border="0" style="cursor:pointer" >
        至 <input type="text" name="prcsDate2" id="prcsDate2" size="10" class="SmallInput" value="" readonly>
        <img id="prcsDate2Img" src="<%=imgPath%>/calendar.gif" align="absMiddle" align=absmiddle border="0" style="cursor:pointer" >
        <a href="javascript:empty_date();" class="orgClear" onclick="empty_date();">清空</a>
        &nbsp;<select name="dateRange" id="dateRange" class="SmallSelect" onchange="date_change(this.value)">
          <option value="0">快捷选择</option>
          <option value="1">今日</option>
          <option value="2">昨日</option>
          <option value="3">本周</option>
          <option value="4">上周</option>
          <option value="5">本月</option>
          <option value="6">上月</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"><b>公共附件名称：</b></td>
      <td nowrap class="TableData">包含</td>
      <td class="TableData">
        <input type="text" name="attachmentName" id="attachmentName" value="" size="30" class="SmallInput">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableHeader" height=25 colspan="3">
        <div style="float:left;font-weight:bold"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle"> 表单数据条件 <a href="javascript:data_show_hide()">显示/隐藏条件</a></div>
      </td>
    </tr>
    <tbody id="fromItemList"></tbody>
    <tr class="TableLine2" style="display:none">
      <td nowrap><b>表单字段条件公式：</b></td>
      <td class="TableData" colspan="2">
        <input type="text" name="condFormula" id="condFormula" value="" size="52" class="SmallInput" onblur="check_condition_formula(this);">&nbsp;形如：([1] or [2]) and [3]
      </td>
    </tr>
    <tr>
      <td nowrap class="TableHeader" height=25 colspan="3"><div style="float:left;font-weight:bold"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle"> 统计报表选项</div></td>
    </tr>
    
<!-- 显示分组字段 -->    
    <tr>
      <td nowrap class="TableContent"><b>分组字段：</b></td>
      <td class="TableData" colspan="2">
        <select name="groupFld" id="groupFld" class="SmallSelect">
   <option value="runId" selected>流水号</option>
   <option value="runName" >名称/文号</option>
   <option value="runStatus" >流程状态</option>
   <option value="runDate" >流程开始日期</option>
   <option value="runTime" >流程开始时间</option>
        </select>&nbsp;
        <select name="groupSort" id="groupSort" class="SmallSelect">
          <option value="ASC" >升序</option>
          <option value="DESC" >降序</option>
        </select>
      </td>
    </tr>
 
<!-- 显示统计字段 -->
    <tr>
      <td class="TableContent" valign="top"><br><b>统计字段：</b><span><br><br>如需进行合计<br>请在统计字段中选中<br>按住CTRL键可多选</span></td>
      <td class="TableData" colspan="2">
        <div id="exchangeDiv"></div>
      </td>
    </tr>
 
    <tr align="center" class="TableFooter">
      <td colspan="3" nowrap>
       <input type="hidden" name="flowId" id="flowId" value="<%=flowId %>">
        <input type="hidden" name="OP" id="OP">
        <input type="hidden" value="" name="LIST_VIEW">
        <input type="hidden" value="" name="SUM_FLD">
        <input type="hidden" value="" name="RUN_ID">
        <input type="hidden" value="" name="MENU_FLAG">
        <input type="hidden" value="" name='listFldsStr' id="listFldsStr">
        <input type="button" value="查询列表" class="BigButton" onclick="htmlSubmit()">&nbsp;&nbsp;
        <input type="button" value="HTML统计报表" class="BigButtonC" onclick="flowReport('1');">&nbsp;&nbsp;
        <input type="button" value="EXCEL统计报表" class="BigButtonC" onclick="flowReport('2');">&nbsp;&nbsp;
        <input type="button" value="返 回" class="BigButton" name="back1" onClick="location='flowList.jsp?sortId=<%=sortId %>&skin=<%=skin %>'">
      </td>
    </tr>
</table>
</form>
</body>
</html>
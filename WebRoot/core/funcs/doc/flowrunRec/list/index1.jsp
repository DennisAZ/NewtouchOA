<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.core.global.YHConst" %>
 <%@ include file="/core/inc/header.jsp" %>
  <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
  <%@ page import="yh.core.funcs.doc.receive.data.YHDocConst" %>
 <% 
 String sNoOperate = request.getParameter("noOperate");
 boolean noOperate = false;
 if (!"".equals(sNoOperate) && sNoOperate != null) {
   noOperate = true;
 } 
 String type = request.getParameter("type");
 if(type == null || "".equals(type)){
   type = "1";
 }
 YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
 String flowId = request.getParameter("flowId");  
 String sortId = request.getParameter("sortId");
 if (sortId == null) {
   sortId = "";
 }
 if (flowId == null || "".equals(flowId)) {
   flowId = "0";
 }
 String skin = request.getParameter("skin");
 String skinJs = "messages";
 if (skin != null && !"".equals(skin)) {
   skinJs = "messages_" + skin;
 } else {
   skin = "";
 }
 %>
 
<html>
<head>
<title>工作流</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">

<style type="text/css">

input{height:20px;line-height:20px;padding:0;margin:0;width:120px;vertical-align:middle;}
input.Log_input{border:1px solid #7fb5da;padding:2px 0px 0px 3px;*padding:1px 3px;background:#fff url(<%=imgPath %>/frame/textinputbg.gif) repeat-x;}
input.Log_submit{width:65px;height:22px;padding:0px;margin:0px;border:none;background:#fff url(<%=imgPath %>/frame/login_bt_login.png) no-repeat 0 0;margin-left:10px;color:#333333;font-weight:bold;cursor:pointer;}
input.Log_submit:hover{background-position:0 -38px;}
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/doc/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/doc/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/doc/workflowUtility/skin.js"></script>
<script type="text/javascript" src="js/index1.js"></script>
<script type="text/javascript">
var type = <%=type%>;
var loginUserId = <%=loginUser.getSeqId()%>;
var sortId = "<%=sortId%>";
var flowType = "<%=flowId%>";
var noOperate = <%=noOperate%>;
var skin = "<%=skin %>";

function beforePaste() {
  var str = clipboardData.getData('text').replace(/[^\d]/g,'');
  if (str == '0') {
    str = '';
  }
  clipboardData.setData('text', str);
}
</script>
</head>
<body onload="doInit()">
   <div style="display:none" >
 <div style="color:red" id=startTime></div>
   <div style="color:red" id=endTime></div>
   <div style="color:red" id=useTime></div>
   </div>
<div id="hasData">
<div id="pagebar">
<div class="pgPanel">
<div>每页<select id="pageLen"  onchange="selectLen(this.value)">
<option value="5">5</option>
<option value="10" selected>10</option>
    <option value="15">15</option>
    <option value="20">20</option>
</select>条</div>
 <div class="separator"></div>
 <div id="pgFirst" title="" class="pgBtn pgFirst pgFirstDisabled">
 </div>
 <div id="pgPrev" title="" class="pgBtn pgPrev pgPrevDisabled">
 </div><div class="separator">
 </div><div>第 
 <input onkeyup="value=value.replace(/[^\d]/g,'');if(value=='0') value='';" onbeforepaste="beforePaste()" id="pageIndex" type="text" title="" value="1" size="5" class="SmallInput pgCurrentPage"> 页 / 共

  <span id="pageCount" class="pgTotalPage"></span> 页</div>
  <div class="separator"></div>
  <div title="下页" id="pgNext" class="pgBtn pgNext pgNextDisabled">
  </div>
  <div title="" id="pgLast" class="pgBtn pgLast pgLastDisabled">
  </div><div class="separator">
  </div><div id="freshLoad" title="刷新" class="pgBtn pgRefresh" onclick="loadData($F('pageIndex') , $F('pageLen') , $F('flowList'));">
  </div><div class="separator"></div>
  <div id="pgSearchInfo" class="pgSearchInfo"></div>
  
  </div></div>
<table id="flow_table" border="0" width="100%" class="TableList"  style="clear:both;table-layout:fixed;" >
  <tr class="TableTr" id="flowTableHeader">
    <td class="TableHeader"  nowrap align="center" width=40><b>流水号</b></td>
     <td class="TableHeader"  nowrap align="center" width=200><b>标题</b></td>
    <td  class="TableHeader" nowrap align="center" width=60>
    <select name="flowList" id="flowList" style="width:120px" onchange="selectFlow(this.value)">
    <option value="0" id="span3">所有流程类型</option>
      
    </select>
      </td>
      <td class="TableHeader"  nowrap align="center" width=80><b>发起人</b></td>
  <%if(!"3".equals(type)){ %>
    <td  class="TableHeader" nowrap align="center" width=120>
	     步骤与流程图 
    </td>
    <%} else {%>
    <td   class="TableHeader" nowrap align="center" width=60>
	     <select name="opFlag" 
	     id="opFlag" style="width:120px" 
	     onchange="selectOpFlag(this.value)">
    <option value="">步骤与流程图 </option>
     <option value="1">我主办的</option>
    <option value="0">我经办的</option>
    </select>
    </td >
    <td  class="TableHeader" nowrap align="center" width=100><b>整个流程状态</b></td>
    <%}%>
    <% if (!noOperate) { %>
      <td id="operate" class="TableHeader" nowrap align="center" width=210><b>流程操作</b></td>
    <% } %>
    </tr>
    <tbody id="dataBody"></tbody>
 </table>

</div>
<div id="noData" align=center style="display:none">
   <table class="MessageBox" width="250">
    <tbody>
        <tr>
            <td id="msgInfo" class="msg info"><span id="span2"></span>
            </td>
        </tr>
    </tbody>
</table>
<div><input type="button" value="返回 " class="SmallButton" onclick='location.reload()'/></div>
&nbsp;
</div>
</body>
</html>
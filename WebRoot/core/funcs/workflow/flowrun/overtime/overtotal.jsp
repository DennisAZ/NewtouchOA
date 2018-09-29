<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%
YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
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
%>
<html>
<head>
<title>超时工作统计</title>
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" type="text/css" href="/inc/js/jquery/page/css/page.css"/>
<link rel="stylesheet" type="text/css" href="/theme/<?=$LOGIN_THEME?>/calendar.css"/>
<style>
.tip {position:absolute;display:none;text-align:center;font-size:9pt;font-weight:bold;z-index:65535;background-color:#DE7293;color:white;padding:5px}
.auto{text-overflow:ellipsis;white-space:nowrap;overflow:hidden;}
</style>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
 <script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
 <script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
  <script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
 <script type="text/javascript" src="js/overjs.js"></script>
 <script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/skin.js"></script>
 <script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
 <script type="text/javascript"><!--
 var sortId = "<%=sortId%>";
 var skin = "<%=skin%>";
//根据选中的统计分类方式，显示不同的选择框（人员、部门、角色）
 function selectValue(obj) {
 	if (obj.value == "user") {//人员
 		document.getElementById("td_user").style.display = "";
 		document.getElementById("td_dept").style.display = "none";
 		document.getElementById("td_role").style.display = "none";
 	} else if (obj.value == "dept") {//部门
 		document.getElementById("td_user").style.display = "none";
 		document.getElementById("td_dept").style.display = "";
 		document.getElementById("td_role").style.display = "none";
 	} else {//角色
 		document.getElementById("td_user").style.display = "none";
 		document.getElementById("td_dept").style.display = "none";
 		document.getElementById("td_role").style.display = "";
 	}  
 }
 //最多选10条
 function check_form()
 {
    var counter = 0;
    for (i=0; i<document.form1.flowList.options.length; i++)
    {
       if(document.form1.flowList.options[i].selected)
          counter++;
    }

    if (counter == 0)
    {
       alert("请指定流程类型！");
       return false;
    }
    else if (counter > 10)
    {
       alert("流程类型选择过多，最多指定十个流程！");
       return false;
    }
    return true;
 }
 
 function get_flow_id_query()
 {
    var flow_id_query = "";
    for (i=0; i<document.form1.flowList.options.length; i++)
    {
       if(document.form1.flowList.options[i].selected)
          flow_id_query += document.form1.flowList.options[i].value + ",";
    }

    return flow_id_query;
 }

function html_stat(isColumn){

  if (check_form())
  {  //par = "pageIndex=" + pageIndex + "&showLength=" + showLength + "&flowId=" + flowId;
    var flow_id_query = get_flow_id_query();
    var queryParam = $("form1").serialize();
    var temp ="flowquery="+ flow_id_query + "&"+queryParam;
    if (isColumn == 1) {
      temp += "&flag=1";
    } else if (isColumn == 2 ) {
      temp += "&flag=2";
    }
    var url = contextPath+'/yh/core/funcs/workflow/act/YHWorkOverTimeAct/getOverTimeTotal.act?';
    if (isColumn) {
      window.open(url + temp);
    } else {
      window.location.href=url+temp;
    }  
  }
}
function columnChart(){
  html_stat(1);
}
function exportCsv() {
  html_stat(2);
}
 --></script>

</head>
<body onload="doInit(1)">
<form  name="form1" id="form1" method="post" >
<table id="flowTable" class="TableList" border="0" width="600" align="center">
  <tr>   
  <td class="TableContent" width="190">&nbsp; 流程类型：
  </td>
  <td class="TableData">
  <select name="flowList" id="flowList" Multiple  style="width:280px;height:200px">
  </select>&nbsp;&nbsp;
  </td>
  </tr>
 
  <tr>
  <td class="TableContent">&nbsp;<span id="span1"> </span>
  </td>
   <td class="TableData">
  从
  <input type="text" id="statrTime" name="statrTime" size="10" class="SmallInput" readonly value="">
  <img id="beginDateImg" src="<%=imgPath %>/calendar.gif" border="0" style="cursor:pointer;vertical-align:middle;">
      到
  <input type="text" id="endTime" name="endTime" size="10" readonly class="SmallInput">
  <img id="endDateImg" src="<%=imgPath %>/calendar.gif"  border="0" style="cursor:pointer;vertical-align:middle;">
  <a href="javascript:empty_date()">清空</a>
  </td> 
  </tr>
  
   <tr>
      <td nowrap class="TableContent"">
		    &nbsp;统计分类方式：
		    <select name="STATCS_MANNER_QUERY" id="STATCS_MANNER_QUERY" onchange="selectValue(this);">
		      <option value="user">指定人员 </option>
		      <option value="dept">按部门选人 </option>
		      <option value="role">按角色选人 </option>
		    </select>
      </td>
      
      <td class="TableData" id="td_user">
        <input type="hidden" name="user" id ="user">
        <textarea cols=30 name="userDesc" id="userDesc" rows="3" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['user', 'userDesc']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
      </td>
      <td class="TableData" id="td_dept" style="display:none">
        <input type="hidden" id ="dept" name="dept">
        <textarea cols=30 name="deptDesc" id="deptDesc" rows="3" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectDept(['dept','deptDesc']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="ClearUser('dept','deptDesc');">清空</a>
      </td>
      <td class="TableData" id="td_role" style="display:none">
        <input type="hidden" id ="role" name="role">
        <textarea cols=30 name="roleDesc" id="roleDesc" rows="3" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectRole(['role','roleDesc']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('role').value='';$('roleDesc').value='';">清空</a>
      </td>
  </tr>
  <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
      <input type="button"  value="HTML列表" class="BigButton" onClick="html_stat();">
      <input type="button"  value="导出CSV" class="BigButton" onClick="exportCsv();">
      <input type="button"  style="display:"  value="柱状图分析" class="BigButton" onClick="columnChart()">
    </td>
   </tr>
   <input type="hidden" name="FLOW_ID_QUERY" id="FLOW_ID_QUERY" value="">
   <input type="hidden" name="sortId" id="sortId" value="<%=sortId %>"/>
   <input type="hidden" name="skin" id="skin" value="<%=skin %>"/>
  </table>
  </form>
  <div id="noData" align=center style="display:none">
  <table class="MessageBox" width="300">
  <tbody>
  <tr>
  <td id="msgInfo" class="msg info"> 没有检索到数据
  </td>
  </tr>
  </tbody>
  </table>
  </div>    
</body>
</html>
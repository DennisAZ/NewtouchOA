<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>工作监控</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
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
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/raw/cc/js/control.js"></script>
<script type="text/javascript">
<!--
function doInit(){ 

  loadFlowType() ;
}
  // loadFlowType() 查询所有类型 方法
function loadFlowType(){
  var url = contextPath+'/yh/core/funcs/workflow/act/YHFlowTypeAct/getFlowTypeJson.act';
  var json = getJsonRs(url);
  var rtData = json.rtData;   
  for(var i = 0 ;i < rtData.length ; i ++) {      
  var opt = document.createElement("option") ;      
	    opt.value = rtData[i].seqId ;      
	    opt.innerHTML = rtData[i].flowName ;      
	    $('flowList').appendChild(opt) ;      
  }    
}
  //工作监控查询 
function mySearch(){

  var queryParam = $("form1").serialize();
//alert(queryParam);
  alert('ss');
  doInit1(queryParam);
  
//  return false;
}
// 下拉列表你选择条数 如：5，10
function selectLen(selectValue){
  var queryParam = $("form1").serialize();
  $("pageIndex").value = "1";
  var flowId = $F("flowList");
  var userType = $F("userType");
  var user = $F("user");
  var runId = $F('runId');
  var runName = $F('runName'); 
  loadData1(1, selectValue, flowId,userType,user,runId,runName,queryParam);
}

--></script>
</head>

<body onload="doInit()">
<form  name="form1" id="form1">
<table id="flow_table" border="0" width="100%" class="TableList"  style="clear:both;table-layout:fixed;" >
  <tr class=TableContent>
  <td><img src="/yh/core/styles/styl<%=imgPath%>ign="absmiddle">请选择要监控的流程
  <select name="flowList" id="flowList" style="width:100px">
  <option value="0">所有流程类型</option>
  </select>
  <select name="userType" id="userType" class="SmallSelect">
  <option value="0">当前主办人</option>
  <option value="1">流程发起人</option>
  </select>

  <input type="hidden" name="user" id="user" value="" />
  <input type="text" name="userDesc" id="userDesc" style="vertical-align: top;" size="10" class="SmallStatic" size="10" value="" READONLY>
  <!--selectUser() 这个函数是多选  -->
  <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['user', 'userDesc']);">选择</a>
  <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
  </td> 
  </tr>
   <tr class="TableContent">
    <td valign="absmiddle" >
    	&nbsp;流水号 <input type="text" id="runId" name="runId" class="SmallInput" size="5" value="" onkeypress=""> <!--if(event.keyCode==13)  zan bu kao lv -->
    	&nbsp;名称/文号<input type="text" id="runName" name="runName" size="20" class="SmallInput">&nbsp;
      &nbsp;<input type="button" id="queryBtn" class="BigButton" onClick="mySearch();" value="查询">
      &nbsp;<input type="button" id="smsBtn" class="SmallButton" style="display:none" value="催办超时流程" onclick="">
    </td>
  </tr>   
 </table>
 </form>
  <!-- 显示分页的内容      开始 -->
  <div id="hasData" style="display:none">
  <div id="pagebar">
  <div class="pgPanel">
  <div>每页<select id="pageLen"  onchange="selectLen(this.value)">
  <option value="5"  selected>5</option>
  <option value="10">10</option>
  <option value="15">15</option>
  <option value="20">20</option>
  </select>条</div>
  <div class="separator"></div>
  <div id="pgFirst" title="" class="pgBtn pgFirst pgFirstDisabled">
  </div>
  <div id="pgPrev" title="" class="pgBtn pgPrev pgPrevDisabled">
  </div><div class="separator">
  </div><div>第 
  <input onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" id="pageIndex" type="text" title="" value="1" size="5" class="SmallInput pgCurrentPage"> 页 / 共



  <span id="pageCount" class="pgTotalPage"></span> 页</div>
  <div class="separator"></div>
  <div title="下页" id="pgNext" class="pgBtn pgNext pgNextDisabled">
  </div>
  <div title="" id="pgLast" class="pgBtn pgLast pgLastDisabled">
  </div><div class="separator">
  </div><div id="freshLoad" title="刷新" class="pgBtn pgRefresh" onclick="javascript:;">
  </div><div class="separator"></div>
  <div id="pgSearchInfo" class="pgSearchInfo"></div>
  
  </div></div> </div>
  <!-- 分页结束  --> 
<table id="flow_table" width="100%" class="TableList" align="center" style="border-bottom:0px;table-layout:fixed;">
 <tbody>
  <tr class="TableHeader">
    <td nowrap align="center" width=40><a href="#">状态</a></td>
    <td nowrap align="center" width=60><a href="#">流水号</a></td>
    <td nowrap align="center" width=100><a href="#">流程名称</a></td>
    <td nowrap align="center" colspan=5><a href="#">工作名称/文号</a></td>
    <td nowrap align="center" width=100><a href="#">当前步骤</a></td>
    <td nowrap align="center" width=80><a href="#">当前主办人</a></td>
    <td nowrap width=130 align="center"><a href="#">办理时间</a></td>
    <td nowrap align="center" width=120><b>操作</b></td>   
    </tr> </tbody>
    <tbody id="dataBody"></tbody>
 </table>
 
<div id="noData" align=center style="display:none">
  <table class="MessageBox" width="300">
  <tbody>
  <tr>
  <td id="msgInfo" class="msg info"> 没有检索到数据
  </td>
  </tr>
  </tbody>
  </table>
  <div><input type="button" value="返回 " class="Log_submit" onclick='location.reload()'/></div>
  </div>
</body>
</html>
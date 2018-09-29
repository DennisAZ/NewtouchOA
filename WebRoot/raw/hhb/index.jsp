<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>工作流</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
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
 <script type="text/javascript" src="js/index3.js"></script>
<script type="text/javascript"><!--
// 全选实现功能function checkAll(field) {
 //alert("aaa");
	  var deleteFlags = document.getElementsByName("run_select") ;
	  for(var i = 0 ; i < deleteFlags.length ; i++) {
	    deleteFlags[i].checked = field.checked ;
	  }
	}
//显示单选框提示信息
function check_select(){
  var select_check = document.getElementsByName("run_select") ;
  var select_str = "";
  for(var i = 0; i < select_check.length ; i ++){
  if(select_check[i].checked){
    select_str += select_check[i].value + "," ; 
    }
  }
  alert(select_str) ;
}
//开始查询函数function checkForm(){ 
  var queryParam = $("form1").serialize();
  doInit3(queryParam); 
}
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
function doInit(){
  
  loadFlowType() ;
 
  var beginParameters = {
      inputId:'statrTime',
      property:{isHaveTime:false} //isHaveTime:true 为true添加时钟
      ,bindToBtn:'beginDateImg'
        };
  new Calendar(beginParameters);
  var endParameters = {
      inputId:'endTime',
      property:{isHaveTime:false}
      ,bindToBtn:'endDateImg'
        };
  new Calendar(endParameters);
}
function selectLen(selectValue){
  alert("111");
  var queryParam = $("form1").serialize();
  $("pageIndex").value = "1";
  var flowId = $F("flowList");
  var flowStatus = $F('FLOW_STATUS');
  var statrTime = $F('statrTime');
  var endTime = $F('endTime');
  var runId = $F('RUN_ID');
  var runName = $F('RUN_NAME'); 
  loadData(1, selectValue, flowId,flowStatus,statrTime,endTime,runId,runName,queryParam);
}
// 清空时间组件
function empty_date(){
  document.form1.statrTime.value="";
	document.form1.endTime.value="";
}
function isDate(dataValue){
  var year = "";
  var month = "";
  var day = "";
  var offset = 0;
  var len = dataValue.length;
  var i = dataValue.indexOf("-");
  alert(i);
  year = dataValue.substr(offset, (i-offset));
  offset = i + 1;
  if (offset > len){
    return false;
  }
  if (i){
    i = dataValue.indexOf("-",offset);
    day = dataValue.substr(sum,(len-offset));
  }
  if(year == ""||month == ""||day == ""){
    return false;
  }
  return true;
}
function check(){
  if(document.getElementById("statrTime").value!=""){
    var da = isDate(document.getElementById("statrTime").value);
    if(!da){
      alert("错误 开始时间格式不对，应形如 1999-1-2");
    }
  }
}
--></script>
</head>
<body onload="doInit()">
<form  name="form1" id="form1">
<table id="flow_table" border="0" width="100%" class="TableList"  style="clear:both;table-layout:fixed;" >
  <tr class=TableTr>
  <td nowrap align="left" width=140> 流程
  <select name="flowList" id="flowList" style="width:100px">
  <option value="0">所有流程类型</option>
  </select>
  </td> 
  <td align="center" width=120>状态<select name="FLOW_STATUS" id="FLOW_STATUS">
  <option value="ALL">所有状态</option>
  <option value="0">正在执行</option>
  <option value="1">已经结束</option>
  </select>
  </td>
  <td>  范围
  <select name="FLOW_QUERY_TYPE" id="FLOW_QUERY_TYPE">
  <option value="ALL">所有范围</option>
  <option value="1">我发起的</option>
  <option value="2" selected>我经办的</option>
  <option value="3" >我管理的</option>
  <option value="4" >我关注的</option>
  <option value="5" >指定发起人</option>
  </select>
  </td> 
  </tr>
  <tr>
  <td class="" colspan=3>从  <input type="text" id="statrTime" name="statrTime" size="10" maxlength="19" class="BigInput" value="444">
  <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      到 <input type="text" id="endTime" name="endTime" size="10" maxlength="19" class="BigInput">
  <img id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
  <a href="javascript:empty_date()">清空</a>
     流水号<input type="text" id="RUN_ID" name="RUN_ID" size="6" class="SmallInput" value="777">&nbsp;
     名称/文号<input type="text" id="RUN_NAME" name="RUN_NAME" size="20" class="SmallInput" value="666">
  </td> 
  <td align="center">
  <input id="queryBtn" type="button" class="BigButton" onclick="checkForm();" value="开始查询">
  </td> 
  </tr>
  <!--   <input type="hidden" name="dddd" value=""/>  --> 
  </table>
  </form>
  <!-- 显示分页的内容      开始 -->
  <div id="hasData">
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
  <input onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" id="pageIndex" type="text" title="" value="1" size="5" class="SmallInput pgCurrentPage"> 页 / 共 <span id="pageCount" class="pgTotalPage"></span> 页</div>
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
  <tr class=TableHeader>
  <td nowrap align="center" width=40>选择</td>
  <td nowrap align="center" width=60><a href="#">流水号</a></td>
  <td nowrap align="center"><a href="#">工作名称/文号</a></td>
  <td nowrap align="center" width=140><a href="#">开始时间</a></td>
  <td nowrap align="center" width=200><a href="#">公共附件</a></td>
  <td nowrap align="center" width=60><a href="#">状态</a></td>
  <td nowrap align="center" width=170>操作</td>
  </tr>
  </tbody>
  <tbody id="dataBody"></tbody><!--

  --></table>
  <table class="TableList" border=0 width="100%" style="margin:0;">
  <tr class="TableControl">
  <td colspan="10">
 	&nbsp;<input type="checkbox" name="allbox" id="allbox_for" onClick="javascript:checkAll(this);">
  <label for="allbox_for" style="cursor:pointer"><u><b>全选</b></u></label> &nbsp;
  <input type="button"  value="导出Excel" class="BigButton"  title="导出所选工作到Excel"> &nbsp;
  <input type="button"  value="导出ZIP" class="BigButton"  title="批量导出所选工作"> &nbsp;
  <input type="button"  value="管理人员删除" class="BigButton"  title="删除所选工作"> &nbsp; 
  <input type="button"  value="强制结束" class="BigButton"  title="强制结束">
  </td>
  </tr>
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
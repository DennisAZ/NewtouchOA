<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/wordmodel/js/wordLogic.js"></script>
<title>套红模板管理</title>
<script type="text/javascript"><!--
var pageMgr = null;
var param;
function doInit(){
  showCalendar('beginTime',false,'beginTimeImg');
  showCalendar('endTime',false,'endTimeImg');
  var url =  contextPath + "/yh/core/funcs/system/wordmoudel/act/YHWordModelAct/listAllModel.act";
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    paramFunc: getQueryParam,
    moduleName:"wordModel",
    colums: [
      {type:"hidden", name:"seqId", text:"顺序号", width: 150, dataType:"int"},
      {type:"data", name:"modelName", text:"模板名称", width: 150},       
      {type:"data", name:"attach", text:"模板文件", width: 200, dataType:"attach"},
      {type:"hidden", name:"attachId"},
      {type:"data", name:"createTime", text:"上传时间", width: 100, dataType:"date"},
      {type:"data",  name:"privStr",text:"授权范围", width: 350, render:showPriv},
      {type:"selfdef", width: 200,text:"操作", render:optRender}]
     // {type:"opts", width: 200, opts:[{clickFunc:doEdit, text:"编辑"},{clickFunc:doDelete, text:"删除 "}]}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
}
function getQueryParam() {
  return $("form1").serialize();
}
function doEdit(recordIndex){
  var modelId  = this.getCellData(recordIndex,"seqId");
  //alert(modelId);
  editLogic(modelId);
}
function doDelete(modelId){
  var msg = "确定要删除此模板吗？";
  if(window.confirm(msg)){
    deleteLogic(modelId);
   }
  location.reload();
  //obj.refreshAll();
}
function optRender(cellData, recordIndex, columIndex){
  var modelId = this.getCellData(recordIndex,"seqId");
 // alert(this);
  var html = "";
  html += "<A href=\"javascript:editLogic('" + modelId + "');\">编辑</A>&nbsp;&nbsp;"; 
  html += "<A href=\"javascript:doDelete('" + modelId + "');\">删除</A>&nbsp;";
  return html;
}
function doSearch(){
  if(checkDate("beginTime") == false){
    $("beginTime").focus();
    $("beginTime").select();
    alert("日期格式不对，请输入形：2010-04-09");
    return;
  }
  if(checkDate("endTime") == false){
    $("endTime").focus();
    $("endTime").select();
    alert("日期格式不对，请输入形：2010-04-09");
    return;
  }
 // var param = getQueryParam();
 // var url =  contextPath + "/yh/core/funcs/system/wordmoudel/act/YHWordModelAct/listModelSearch.act?" + param;
  //alert(pageMgr.dataAction);
 // pageMgr.dataAction = url;
 // alert(pageMgr.dataAction);
 // pageMgr.clearCatch();
  //pageMgr.loadPageData(0);
 // pageMgr.showPage(0);
 // pageMgr.loadPageData(0);
  pageMgr.search();
}
--></script>
</head>
<body onload="doInit()">
<div align="left">
<fieldset style="width:95%;padding-bottom:5px;">
	 <legend class="small" align=left>
      <b>快速查询</b>
  </legend>
<form id="form1" method="post" name="form1">
<table class="TableList" align="left" width=100%>
  <tr>
    <td nowrap class="TableContent">&nbsp;模板类别： </td>
    <td nowrap class="TableData">
       <select name="sortId" class="BigSelect">
       	<option value="">所有类别</option>
             </select>
   </td>
    <td nowrap class="TableContent">&nbsp;上传时间:</td>
    <td nowrap class="TableData">
	     <input type="text" id="beginTime" name="beginTime" class="BigInput" size="10" value="">
    	 <img src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" id="beginTimeImg">  
    	 至
	     <input type="text" name="endTime" id="endTime"  class="BigInput" size="10" value="">
    	 <img src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" id="endTimeImg">      	   	
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent">&nbsp;模板名称： </td>
    <td nowrap class="TableData">
       <input type="text" name="modelName" class="BigInput" value="" size=30>
   </td>
    <td nowrap class="TableData" align=center colspan=2>
    <input type="button" value="快速搜索" class="BigButton" onclick="doSearch()">
       &nbsp;<input type="button" class="BigButton" value="上传模板" onclick="toSaveModel();">
    </td>
   </tr>
</table>
</form>
</fieldset>
</div>
<div id="listContainer"></div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<title>数据表管理</title>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript">
var requestUrl = contextPath + "/raw/lh/generate/act/YHSysTableAct";
var url = requestUrl + "/getTableList.act";
function doInit() {
  cfgs = {
      dataAction: url,
      container: "datalist",
      paramFunc: getParam,
      showRecordCnt: true,
      colums: [
         {type:"selfdef", align:"center", text:"选择", width:"7%",render:checkBoxRender},
         {type:"hidden", name:"seqId"},
         {type:"data", name:"tableName", text:"表名"},
         {type:"selfdef", text:"操作",width:"18%", render:opRender},
         ]
    };
  doQuery();
}
function getParam() {
  return $('form1').serialize();
}
var pageMgr = null;
var cfgs = null;
function doQuery(){
  if(!pageMgr){
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
  }else{
    pageMgr.search();
  }
}
/**
* 单选按钮

* @param cellData
* @param recordIndex
* @param columIndex
* @return
*/
function checkBoxRender(cellData, recordIndex, columIndex){
 var seqId = this.getCellData(recordIndex,"seqId");
 return "<input type=checkbox name=run_select value='"+ seqId +"'>";
}
/**
* 流程操作描画器


* @param cellData
* @param recordIndex
* @param columIndex
* @return
*/
function opRender(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var tableName = this.getCellData(recordIndex,"tableName");
  return "<center><a href=\"javascript:void(0)\" onclick=\"turnEdit("+ seqId + ")\">编辑</a>" 
  + "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"generateSql("+ seqId + ",'"+tableName+"')\">生成数据库</a>"
  + "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"generateCode("+ seqId + ",'"+tableName+"')\">生成代码</a>"
  + "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"turnFieldEdit("+ seqId + ",'"+tableName+"')\">管理属性</a>"
  + "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"del("+ seqId + ")\"><img src=\"" + imgPath + "/delete.gif\"></a></center>";
}
function del(seqIds) {
  var url2 = requestUrl + "/delTable.act";
  var json = getJsonRs(url2 , "seqId=" + seqIds);
  if (json.rtState == '0') {
    alert("删除成功！");
    doQuery();
  }
}
function dels() {
  var runIds = "";
  var deleteFlags = document.getElementsByName("run_select") ;
  for(var i = 0 ; i < deleteFlags.length ; i++) {
    if(deleteFlags[i].checked){
      if(runIds){
        runIds += ",";
      }
      runIds += deleteFlags[i].value;
    }
  }
  if (!runIds) {
    alert("请选择删除的表！") 
    return ;
  }
  del(runIds);
}
//全选实现功能
function checkAll(field) {
  var deleteFlags = document.getElementsByName("run_select") ;
  for(var i = 0 ; i < deleteFlags.length ; i++) {
    deleteFlags[i].checked = field.checked ;
  }
}
function turnEdit(seqId) {
  var url = contextPath + "/raw/lh/generate/edit.jsp?1=1";
  if (seqId) {
    url += "&seqId=" + seqId;
  }
  document.location.href = url;
}
function turnFieldEdit(seqId , tableName) {
  var url = contextPath + "/raw/lh/generate/editField.jsp?";
   url += "seqId=" + seqId + "&tableName=" +encodeURIComponent(tableName)  ;
  document.location.href = url;
}
function generateSql(seqId , tableName) {
  var url = contextPath + "/raw/lh/generate/generateSql.jsp?";
   url += "seqId=" + seqId + "&tableName=" +encodeURIComponent(tableName)  ;
  document.location.href = url;
}
function generateCode(seqId , tableName) {
  var url = contextPath + "/raw/lh/generate/generateCode.jsp?";
   url += "seqId=" + seqId + "&tableName=" +encodeURIComponent(tableName)  ;
  document.location.href = url;
}
</script>
</head>
<body onload="doInit()">

<div id="total"><table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle">数据表的管理
    </td>
  </tr>
</table></div><br>
<div id="query" style="padding-left:5px"><form action="" id="form1">
<table id="flow_table" border="0" width="100%"  class="TableList" >
  <tr class="TableLine1">
  <td valign="absmiddle">
  表名：<input type="text" id="queryTableName" name="queryTableName">&nbsp;&nbsp;<input class="SmallButton" type="button" value="查询" onclick="doQuery()">
  </td> 
  </tr>
 </table>
 </form>
</div><br>
<div id="datalist"></div>

<br>
<div id="manager"><br>
 <table class="TableBlock" border=0 width="100%" style="margin:0;" id="flowRunOpTab" >
  <tr class="TableData">
  <td>
  &nbsp;&nbsp;<input type="checkbox" name="allbox" id="allbox_for" onClick="javascript:checkAll(this);">
  <label for="allbox_for" style="cursor:pointer"><u><b>全选</b></u></label> &nbsp;
<input class="SmallButton" type="button" onclick="turnEdit()" value="新增数据表">
&nbsp;&nbsp;
<input class="SmallButton" onclick="dels()" type="button" value="删除数据表">  </td>
  </tr>
  </table>

</div>

</body>
</html>
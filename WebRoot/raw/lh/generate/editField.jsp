<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String seqId = YHUtility.null2Empty(request.getParameter("seqId"));
String tableName =  YHUtility.null2Empty(request.getParameter("tableName"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<title>管理数据表－字段</title>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript">
var requestUrl = contextPath + "/raw/lh/generate/act/YHSysTableAct";

var seqId = "<%=seqId %>";
var pageMgr = null;
var cfgs = null;
function doInit() {
  var url = requestUrl + "/getTableField.act?seqId=" + seqId;
  cfgs = {
      dataAction: url,
      container: "list",
      showRecordCnt: true,
      colums: [
         {type:"selfdef", align:"center", text:"选择", width:"7%",render:checkBoxRender},
         {type:"hidden", name:"seqId"},
         {type:"data", name:"fieldName", text:"属性名称"},
         {type:"data", name:"fieldType", text:"属性类型" , render:typeRender},
         {type:"data", name:"fieldLength", text:"属性长度"},
         {type:"selfdef", text:"操作",width:"18%", render:opRender},
         ]
    };
  doQuery();
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
//全选实现功能
function checkAll(field) {
  var deleteFlags = document.getElementsByName("run_select") ;
  for(var i = 0 ; i < deleteFlags.length ; i++) {
    deleteFlags[i].checked = field.checked ;
  }
}
function typeRender(cellData, recordIndex, columIndex) {
  var tip = "文本";
  if (cellData == '2') {
    tip = "整型";
  } else if (cellData == '3') {
    tip = "实数";
  } else if (cellData == '4') {
    tip = "日期";
  } 
  return tip;
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
  var fieldName = this.getCellData(recordIndex,"fieldName");
  var fieldType =  this.getCellData(recordIndex,"fieldType");
  var fieldLength =  this.getCellData(recordIndex,"fieldLength");
  return "<center><a href=\"javascript:void(0)\" onclick=\"turnEdit("+ seqId + ", '"+fieldName+"', '"+fieldType+"', '"+fieldLength+"')\">编辑</a>" 
  + "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"del("+ seqId + ")\"><img src=\"" + imgPath + "/delete.gif\"></a></center>";
}
function doQuery(){
  if(!pageMgr){
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
  }else{
    pageMgr.search();
  }
}
function turnEdit(seqId , name , type , length ) {
  $('tip').update("");
  $('fieldSeqId').value = seqId;
  $('FIELD_LENGTH_EDIT').value = length;
  $('FIELD_TYPE_EDIT').value = type;
  $('FIELD_NAME_EDIT').value = name;
}
function addTr(fieldName , fieldType , fieldLength ){
  var tr = new Element("tr");
  $('list').appendChild(tr);
  var td1 = new Element("td");
  var td2 = new Element("td");
  var td3 = new Element("td");
  var td4 = new Element("td");
  tr.appendChild(td1);
  tr.appendChild(td2);
  tr.appendChild(td3);
  tr.appendChild(td4);
}
function save(){
  var url2 = requestUrl + "/addOrUpdateTableField.act";
  var json = getJsonRs(url2 ,$('form1').serialize());
  if (json.rtState == '0') {
    $('tip').update("保存成功！");
    clearField();
    doQuery();
  }
}
function clearField() {
  $('tip').update("");
  $('fieldSeqId').value = "";
  $('FIELD_LENGTH_EDIT').value = '11';
  $('FIELD_TYPE_EDIT').value = '1';
  $('FIELD_NAME_EDIT').value = "";
}
function typeChange(val) {
  if (val == '1') {
    $('FIELD_LENGTH_EDIT').value = '45';
  } else if (val == '2') {
    $('FIELD_LENGTH_EDIT').value = '11';
  } else if (val == '3') {
    $('FIELD_LENGTH_EDIT').value = '11';
  } else if (val == '4') {
    $('FIELD_LENGTH_EDIT').value = '45';
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
    alert("请选择删除的属性！") 
    return ;
  }
  del(runIds);
}
function del(seqIds) {
  var url2 = requestUrl + "/delTableField.act";
  var json = getJsonRs(url2 , "seqId=" + seqIds + "&tableId=" + $('SEQ_ID').value);
  if (json.rtState == '0') {
    $('tip').update("删除成功！");
    doQuery();
  }
}
</script>
</head>
<body onload="doInit()">

<div id="total"><table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle">管理数据表－字段
    </td>
  </tr>
</table></div><br>
<form action="" id="form1" name="form1">
<table class="TableBlock"   width="70%"  align="center">
<tr>
   <td  class="TableData"  colspan="4"> 表名：<span name="TABLE_NAME"><%=tableName %>
   </span>&nbsp;&nbsp;
   <input type="button" value="新增列" class="BigButton" onclick="clearField()">&nbsp;&nbsp;
   </td>
</tr>
<tr id="edit">
<td>属性名称：<font color="red">*</font><input type="text" name="FIELD_NAME_EDIT" id="FIELD_NAME_EDIT"  class="BigInput" value=""></td>
<td>属性类型：
<SELECT name="FIELD_TYPE_EDIT" id="FIELD_TYPE_EDIT" onclick="typeChange(this.value)">
<option value="1">文本</option>
<option value="2">整数</option>
<option value="3">实数</option>
<option value="4">日期</option>
</SELECT>
</td>
<td>属性长度：<font color="red">*</font><input type="text" name="FIELD_LENGTH_EDIT" id="FIELD_LENGTH_EDIT"  class="BigInput" value="45"></td>
<td><input type="button" value="保存" class="BigButton" onclick="save()">
<span id="tip"></span>
<input type="hidden" value="" id="fieldSeqId" name="fieldSeqId"></td></tr>
     <tr align="center" class="TableControl">
      <td colspan="4">
      <div id="list"  align="center">

    </div>
      </td>
    </tr>
  </table>

 <table class="TableBlock"  width="70%" align="center" id="flowRunOpTab" >
  <tr class="TableData">
  <td>
  &nbsp;&nbsp;<input type="checkbox" name="allbox" id="allbox_for" onClick="javascript:checkAll(this);">
  <label for="allbox_for" style="cursor:pointer"><u><b>全选</b></u></label> &nbsp;
<input class="SmallButton" onclick="dels()" type="button" value="删除属性"> 
<input type="hidden"  name="SEQ_ID" id="SEQ_ID"    value="<%=seqId %>"> &nbsp; &nbsp;
        <input type="button" onclick="location.href='<%=contextPath %>/raw/lh/generate/index.jsp'"  value="返回" class="BigButton">
 </td>
  
  </tr>
  </table>
</form>
</body>
</html>
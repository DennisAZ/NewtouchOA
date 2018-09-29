<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>礼品查询</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>

<script type="text/javascript"> 
function excel_export()
{
  document.form1.action="export.php";
  document.form1.submit();
  document.form1.action="list.php";
}
function doOnload(){
  //礼品类型
  giftType();
  //礼品
  allQueryGift();
}
function giftType(){

  var requestURL = "<%=contextPath%>/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/getCodeItem.act?GIFT_PROTYPE=GIFT_PROTYPE"; 
  var json = getJsonRs(requestURL); 
  //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  var prcsJson = json.rtData;
  var selectObj = document.getElementById("giftType");
  for(var i = 0;i<prcsJson.length;i++){
    var prc = prcsJson[i];
    var seqId = prc.sqlId;
    var classNO = prc.classNo;
    var calssDesc = prc.classDesc;
    var myOption = document.createElement("option");
    myOption.value = seqId;
    myOption.text = calssDesc;
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
  }
}
var pageMgr = null;
var cfgs = null;
function allQueryGift(){
  var giftName = $("giftName").value;
  var giftDesc = $("giftDesc").value;
  var giftCode = $("giftCode").value;
  var giftType = $("giftType").value;
  var timestamps =new Date().valueOf();
  var url = "<%=contextPath%>/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/queryAllGift.act";
   cfgs = {
    dataAction: url,
    container: "giftList",
    paramFunc: getParam,
    colums: [
       {type:"selfdef",name:"email_select", text:"选择",align:"center", width:"5%",render:toCheck},
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"giftName", text:"礼品名称", width: "10%",align:"center"},
       {type:"text", name:"giftType", text:"礼品类型", width: "8%",align:"center"},
       {type:"text", name:"giftPrice", text:"单价", width: "6%",align:"center"},
       {type:"text", name:"giftSuppLier", text:"供应商",align:"center", width: "6%"},
       {type:"text", name:"giftQty", text:"数量",align:"center", width: "6%" ,render:toQty},           
       {type:"text", name:"giftKeeper", text:"保管员", width: "8%",align:"center"},
       {type:"text", name:"deptId", text:"上缴部门",align:"center", width: "8%"},
       {type:"text", name:"giftCreator", text:"经手人", width:"8%",align:"center"},
       {type:"date", name:"createDate", text:"创建时间", width:"8%" ,align:"center",render:toDate},
       {type:"text", name:"giftMemo", text:"备注", width:"12%",align:"center"},
       {type:"selfdef",name:"opts",align:"center",align:"center", text:"操作", width:"8%",render:toOpts}]
  };
   pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    var div = new Element('div',{"class":"TableControl"}).update("<input type='checkbox' name='allbox' id='allbox_for' onClick='check_all();'>"
        +"<label for='allbox_for'>全选</label> &nbsp;<a href='javascript:delete_arrang();' title='删除所选礼品'>"
        +"<img src='<%=imgPath%>/delete.gif' align='absMiddle'>&nbsp;删除所选礼品</a>&nbsp;");
    $('giftList').appendChild(div);
   //document.getElementById("deleteAll").style.display = "";
  }else{
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件的信息</div></td></tr>"
        );
    $('giftList').style.display="none";
    $('returnNull').update(table); 
    
  }
}
function getParam(){
  queryParam = $("form1").serialize();
  return queryParam;
}
function toCheck(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<input type='checkbox'  id='email_select' name='email_select' value='" + seqId + "' onClick='check_one(self);'>";
}
function toDate(cellData, recordIndex, columIndex){
  var createDate = this.getCellData(recordIndex,"createDate");
  return createDate.substr(0,10);
}
function toOpts(cellData, recordIndex, columInde){
  //var giftQty = this.getCellData(recordIndex,"giftQty");
 // if(parseInt(giftQty,10)>0){
   // return "<a href='#' target='_blank'> 申领礼品</a>";////general/workflow/new1/edit.php?FLOW_NAME=%
 // }
  var seqId =  this.getCellData(recordIndex,"seqId");
  return "<a href='#' onclick='updateGift("+seqId+")'> 编辑</a>&nbsp;<a href='#' onclick='delGift("+seqId+")'> 删除</a>";
}
function toQty(cellData, recordIndex, columInde){
  var giftQty = this.getCellData(recordIndex,"giftQty");
  return "现存 " + giftQty;
}

function updateGift(seqId){
  var url="<%=contextPath%>/subsys/oa/gift_product/instock/queryinstosk/updateInstock.jsp?seqId=" + seqId;
  window.location.href = url;
}
function delGift(seqId){
  msg='确认要删除此办公用品吗？';
  if(window.confirm(msg)){
    var url="<%=contextPath%>/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/delGiftInstock.act?seqIds="+seqId;
    var json = getJsonRs(url); 
    //alert(rsText);
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    }
    window.location.reload();
  }
}
function check_all(){
  
  var t =document.getElementsByName("email_select");
  for (i=0;i<document.getElementsByName("email_select").length;i++){
    if(document.getElementsByName("allbox")[0].checked){
      document.getElementsByName("email_select").item(i).checked=true;
    }else{
      document.getElementsByName("email_select").item(i).checked=false;
    }
  }
  if(i==0){
    if(document.getElementsByName("allbox")[0].checked){
      document.getElementsByName("email_select").checked=true;
    }else{
      document.getElementsByName("email_select").checked=false;
    }
  }
}
function check_one(el){
   if(!el.checked)
      document.getElementsByName("allbox")[0].checked=false;
}
function get_checked(){
  checked_str="";
  for(i=0;i<document.getElementsByName("email_select").length;i++){
    el=document.getElementsByName("email_select").item(i);
    if(el.checked){  
      val=el.value;
      checked_str+=val + ",";
    }
  }
  if(i==0) {
    el=document.getElementsByName("email_select");
    if(el.checked){  
      val=el.value;
      checked_str+=val + ",";
    }
  }
  return checked_str;
}
function delete_arrang(){
  var delete_str=get_checked();
  //alert(delete_str);
  if(delete_str==""){
     alert("要删除办公用品，请至少选择其中一条。");
     return;
  }
  msg='确认要删除所选办公用品吗？';
  if(window.confirm(msg)){
    var url="<%=contextPath%>/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/delGiftInstock.act?seqIds="+delete_str;
    var json = getJsonRs(url); 
    //alert(rsText);
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    }
    window.location.reload();
  }
}
function queryGift(){
  if(!pageMgr){
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
  }else{
    pageMgr.search();
  }
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    $('giftList').style.display="";
    $('returnNull').style.display="none"; 
  }else{
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件的信息</div></td></tr>"
        );
    $('giftList').style.display="none";
    $('returnNull').style.display=""; 
    $('returnNull').update(table);  
  }
}
</SCRIPT>
</head>
<body class="" topmargin="5" onload="doOnload();">
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="/images/dian1.gif" width="100%"></td>
 </tr>
</table>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/system/filefolder/images/notify_open.gif" align="absmiddle"><span class="big3"> 礼品信息查询</span>
    </td>
  </tr>
</table>
<br>
<div align="center">
   <form action="#"  method="post" name="form1"  id="form1">
<table width="450" class="TableBlock" align="center" >

   <tr>
    <td nowrap class="TableContent">礼品名称： </td>
    <td nowrap class="TableData">
        <input type="text" id="giftName" name="giftName" class="BigInput" size="33" maxlength="100">&nbsp;
    </td>
    <td nowrap class="TableContent">礼品描述： </td>
    <td nowrap class="TableData">
        <input type="text" id="giftDesc" name="giftDesc" class="BigInput" size="33" maxlength="100">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableContent">礼品编码： </td>
    <td nowrap class="TableData">
        <input type="text" id="giftCode" name="giftCode" class="BigInput" size="33" maxlength="100">&nbsp;
    </td>
    <td nowrap class="TableContent">礼品类别： </td>
    <td nowrap class="TableData" align="left">
        <select name="giftType" id="giftType">
        <option value=""></option>
        </select>
        
    </td>
   </tr>
 
   <tfoot align="center" class="TableFooter">
    <td colspan="4" align="center">
        <input type="button" value="查询" class="BigButton" title="模糊查询" name="button" onclick="queryGift();">
    </td>
   </tfoot>

</table>
  </form>
</div>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/system/filefolder/images/notify_open.gif" align="absmiddle"><span class="big3"> 礼品列表 </span>
    </td>
  </tr>
</table>
 <div id="giftList"></div>
<div id="returnNull"></div>

<br>
</body>
</html>


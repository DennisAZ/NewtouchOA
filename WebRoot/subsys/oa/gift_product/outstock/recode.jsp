<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>礼品查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/workflow/workflowUtility/utility.js" ></script>

<script type="text/javascript"> 
function CheckForm(){
   if(document.form1.user.value==""&&document.form1.userDesc.value==""){      
   	 alert( "领用人不能为空！");
   	document.form1.userDesc.focus();
   	document.form1.userDesc.select();
     return (false);
   }
  if(document.form1.giftId.value==""){ 
     alert("请选择礼品！");
     form1.giftId.focus();
     form1.giftId.select();
     return (false);
  }
  if(document.form1.transQty.value=="") { 
     alert("数量不能为空！");
     form1.transQty.focus();
     form1.transQty.select();
     return (false);
  }
  var IsInt = "^[0-9]*[1-9][0-9]*$";//正整数　
  var IsInt2 =   "^-?\\d+$";//整数
  var re2 = new RegExp(IsInt2);
  if(document.getElementById("transQty").value.match(re2) == null ){
    alert("数量应为整数!");
    document.getElementById("transQty").focus();
    document.getElementById("transQty").select();
    return false;
  }
  //var giftId = $("giftId").value;
 // var giftIdArray  = giftId.split(",");
 // var transQty = $("transQty").value;
  //alert(giftIdArray[1]+":"+transQty);
 // if(parseInt(giftIdArray[1],10)<parseInt(transQty,10)){
   // alert("领用数量大于库存数量，请仔细检查www!");
  	// form1.transQty.focus();
  	//form1.transQty.select();
   // return (false);
  //}
  	
  return true;
}
 
function checknumber(q){
  if(document.form1.qtyCheck.value!="" &&  parseInt(q)>parseInt(document.form1.qtyCheck.value))
  return 0;
  return 1;
}
 
function checknum(p){
  var l = p.length;
  var count=0;
  for(var i=0; i<l; i++)
  {
    var digit = p.charAt(i);
    if(digit == "." )
    {
      ++count;
      if(count>1)
      {
       return 0;
      }
    }
    else if(digit < "0" || digit > "9")
    {
       return 0;
    }
  }
  return 1;
}
 

 
 
function deleteVote(seqId){
  msg='确认放弃该操作？放弃该操作库存将回滚到操作前的状态！';
  if(window.confirm(msg)){
    var requestURL = "<%=contextPath%>/yh/subsys/oa/giftProduct/outstock/act/YHGiftOutstockAct/delOutstockById.act?seqId="+seqId;
    var json = getJsonRs(requestURL); 
        //alert(rsText);
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    }
    alert("删除成功！");
    window.location.reload();
   }
}
function updateVote(seqId){
  window.location.href ="<%=contextPath%>/subsys/oa/gift_product/outstock/updateOutstock.jsp?seqId="+seqId;
}
function doOnload(){
  giftType();
  getInstock();
  todayOutstock();
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
  //selectObj.options.length=1;
  //document.all.selectObj.options.length = 0;  
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
function getInstock2(){
  var giftType = "";
  $("giftType").value = giftType ;
  var requestURL = "<%=contextPath%>/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/getInstockByGiftType.act?giftType="+giftType;
  var json = getJsonRs(requestURL); 
  //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  var prcsJson = json.rtData;
  var selectObj = document.getElementById("giftId");
  selectObj.options.length=1;
  for(var i = 0;i<prcsJson.length;i++){
    var prc = prcsJson[i];
    var seqId = prc.seqId;
    var giftName = prc.giftName;
    var giftQty = prc.giftQty;
    //现存数量
    //var useGiftQty = prc.useGiftQty;
    var myOption = document.createElement("option");
    myOption.value = seqId;//+","+giftQty;
    myOption.text = giftName+"/现存" +giftQty;
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
  }
}
//从下机页面跳过来的
function getInstock(){
  var giftType = "";
  giftType = $("giftType").value;
  var requestURL = "<%=contextPath%>/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/getInstockByGiftType.act?giftType="+giftType;
  var json = getJsonRs(requestURL); 
  //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  var prcsJson = json.rtData;
  var selectObj = document.getElementById("giftId");
  selectObj.options.length=1;
  for(var i = 0;i<prcsJson.length;i++){
    var prc = prcsJson[i];
    var seqId = prc.seqId;
    var giftName = prc.giftName;
    var giftQty = prc.giftQty;
    //现存数量
    //var useGiftQty = prc.useGiftQty;
    var myOption = document.createElement("option");
    myOption.value = seqId;//+","+giftQty;
    myOption.text = giftName+"/现存" +giftQty;
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
  }
}
function Init(){
  if(CheckForm()){
    var requestURL = "<%=contextPath%>/yh/subsys/oa/giftProduct/outstock/act/YHGiftOutstockAct/addGiftOutstock.act?"
    var json = getJsonRs(requestURL,mergeQueryString($("form1"))); 
      //alert(rsText);
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    }
    var prc = json.rtData;
   // alert(rsText);
    if(prc.type=='2'){
      alert("领用数量大于库存数量，请仔细检查!");
       form1.transQty.focus();
       form1.transQty.select();
       return;
    }
    $("returnDiv").style.display ="";
    $("bodyDiv").style.display="none";
  }
}
function todayOutstock(){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/giftProduct/outstock/act/YHGiftOutstockAct/getOutstockByToday.act?"
    var json = getJsonRs(requestURL); 
      //alert(rsText);
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    }
    var prcs = json.rtData;
    var table = new Element('table',{"border":"0","width":"100%" ,"cellspacing":"0", "cellpadding":"3" ,"class":"small"}).update("<tbody id='tbody'>"
        +"<tr class='TableHeader'>"
    	+"<td nowrap align='center'>礼品名称</td>"
        +"<td nowrap align='center'>领用人</td>"
        +"<td nowrap align='center'>现存</td>"
		+"<td nowrap align='center'>本次领用</td>" 
		+"<td nowrap align='center'>退回数</td> "
		+"<td nowrap align='center'>实际领用</td>"
		+"<td nowrap align='center'>礼品用途</td>"
        +"<td nowrap align='center'>单价</td>"
        +"<td nowrap align='center'>操作日期</td>"
        +"<td nowrap align='center'>经手人</td>"
		+"<td nowrap align='center'>操作</td></tr></tbody>");
    if(prcs.length>0){
      var tableStr = "<table width='95%' border='0' cellspacing='0' cellpadding='0' height='3'>"
      +"<tr><td background='/yh/core/funcs/system/filefolder/images/dian1.gif' width='100%'></td>"
      +"</tr></table>"
      +"<table width='95%' border='0' cellspacing='0' cellpadding='0' height='3'>"
      +"<tr><td class='ig'><img src='/yh/core/funcs/system/filefolder/images/notify_open.gif' WIDTH='22' HEIGHT='22' align='absmiddle'><span class='big3'>&nbsp;今日操作查看</span>"
      +"</td></tr></table>";
      $("todayOutstock").update(tableStr);
      $("todayOutstock").appendChild(table);
    }

      for(var i = 0;i<prcs.length;i++){
        var prc = prcs[i];
        var seqId = prc.seqId;
        var transFlagStr = prc.transFlag;
        var runId = prc.runId;
        var transFlag = prc.transFlag;
        var useGiftQty = prc.useGiftQty;
        
        if(useGiftQty==''){
          useGiftQty = 0;
        }
        if(transFlag=='0'||transFlag==''){
          transFlagStr = "未退回";
          transFlag = 0;
        }
        var opts = "<a href='javascript:updateVote("+seqId+");'>编辑</a>&nbsp;"
           +"<a href='javascript:deleteVote("+seqId+");'>删除</a>&nbsp;"
           +"<a href='javascript:backOut("+seqId+");'>退库</a>&nbsp;";
        if(runId>0){
          opts = "<a href='javascript:deleteVote("+seqId+");'>删除</a>&nbsp;"
                 +"<a href='javascript:backOut("+seqId+");'>退库</a>&nbsp;"
		 	     +"<a href='javascript:formViewByName("+runId+" ,\"礼品领用\");' >工作流详情</a>";
        }
        var useTrans = parseInt(prc.transQty,10)-parseInt(transFlag,10);
        var tr = new Element('tr',{"class":"TableLine1"}).update("<td nowrap align='center'>"+prc.giftName+"</td>"
            +"<td nowrap align='center'>"+prc.transUser+"</td>"
            +"<td nowrap align='center'>"+prc.giftQty+"</td>"
            +"<td nowrap align='center'>"+prc.transQty+"</td>"
            +"<td nowrap align='center'>"+transFlagStr+"</td>"
            +"<td nowrap align='center'>"+useTrans+"</td>"
            +"<td nowrap align='center'>"+prc.transUses+"</td>"
            +"<td nowrap align='center'>"+prc.giftPrice+"</td>"
            +"<td nowrap align='center'>"+prc.transDate.substr(0,10)+"</td>"
            +"<td nowrap align='center'>"+prc.operator+"</td>"
            +"<td nowrap align='center'>"
            +opts +"</td>");//target="_blank"
   		 	 // $("tbody").appendChild(tr);

   		 	newTrElement(table,[{className:"TableLine1"}],11,[prc.giftName,prc.transUser,prc.giftQty,prc.transQty,transFlagStr,useTrans,prc.transUses,prc.giftPrice,prc.transDate.substr(0,10),prc.operator,opts])

    }
}
function newTrElement(tbObj,tbAttri,index,config){
  var currentRows = tbObj.rows.length;//原来的行数
  var mynewrow = tbObj.insertRow(currentRows);

  for(var i = 0; i<tbAttri.length ; i ++){
    if(tbAttri[i].className){
      mynewrow.className = tbAttri[i].className;
    }
    if(tbAttri[i].width){
      mynewrow.width = tbAttri[i].width;
    }
  }
  for(var i = 0 ; i<index ;i++){
    var currentCells = mynewrow.cells.length;
    var mynewcell=mynewrow.insertCell(currentCells);
    mynewcell.align = 'center';
    mynewcell.nowrap = true;
    mynewcell.innerHTML = config[i];
  }
}
function backOut(seqId){
  var requestURL = "<%=contextPath%>/subsys/oa/gift_product/outstock/back.jsp?seqId="+seqId;
  window.location.href = requestURL;
}
var giftIdField = null;
function openWindow(giftId){
  giftIdField = giftId;
  var URL= contextPath + "/subsys/oa/gift_product/outstock/searchTree.jsp";
  openDialogResize(URL , 320, 355);
 // window.open(URL,this,"height=355px,width=320px,directories=no,menubar=no,toolbar=no,status=no,scrollbars=yes,location=no,top="+loc_y+",left="+loc_x+"");
}

</script>
</head>
<body class="" topmargin="5" onload="doOnload();">
<div id="bodyDiv" >
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/system/filefolder/images/notify_new.gif" align="absmiddle"><span class="big3">&nbsp;新建领用登记</span>
    </td>
  </tr>
</table>
<br>
  <form enctype="multipart/form-data" action="#"  method="post" id="form1" name="form1">
    <input type="hidden" id="dtoClass" name="dtoClass" value="yh.subsys.oa.giftProduct.outstock.data.YHGiftOutstock"/>
<table width="70%" align="center" class="TableBlock">

    <tr>
      <td nowrap  class="TableData" style="">领用人：</td>
      <td class="TableData">
       <input type="hidden" name="user" id="user" value="" />
      <textarea name="userDesc" id="userDesc"  rows="1" cols="10" class="BigStatic" readonly="readonly" ></textarea>
       <a href="javascript:;" class="orgAdd" onClick="selectSingleUser();">添加</a>
         <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
	  </td>
    </tr>
   <tr>
      <td nowrap class="TableData">礼品类别：</td>
      <td class="TableData">
        <select id="giftType" name="giftType" class="" onChange="getInstock();">
           <option value="">请选择类别</option>
         </select>
       </td>
   </tr>
  	<tr>
      <td nowrap class="TableData">礼品：</td>
      <td class="TableData">
        <select name="giftId" id="giftId" class="">
           <option value="">---请选择---</option>
         </select> &nbsp;
		 <input type="button" name="SelectPro" title="模糊选择" value="模糊选择" class="SmallButtonW" onClick="openWindow('giftId');">
       </td>
      </tr>
   <tr>
      <td nowrap class="TableData">数量：</td>
      <td class="TableData">
        <input type="text" name="transQty" id="transQty" size="10" maxlength="20" class="BigInput" value="" >
		<input type="hidden" name="qtyCheck" id="qtyCheck"   value="">
		<font color=red></font>				
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">礼品用途：</td>
      <td class="TableData">
        <textarea id="transUses" name="transUses" cols="45" rows="3" class="BigInput"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData">
        <textarea id="transMemo" name="transMemo" cols="45" rows="5" class="BigInput"></textarea>
      </td>
    </tr>
    <tfoot align="center" class="TableFooter">
      <td colspan="2" nowrap>
        <input type="hidden" id="seqId" name="seqId" value="">
        <input type="button" value="确认" class="BigButton" onClick="Init();">&nbsp;&nbsp;
      </td>
    </tfoot>
  </table>
</form>
<div id="todayOutstock"></div>
   </div>
<div id="returnDiv" style="display:none">
<table class="MessageBox" align="center" width="440">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">管理记录保存成功！！</div>
    </td>
  </tr>
</table>
<br>
<div align="center">
 <input type="button"  value="返回" class="BigButton" onClick="window.location.reload();">
</div>

</div>
</body>
</html>

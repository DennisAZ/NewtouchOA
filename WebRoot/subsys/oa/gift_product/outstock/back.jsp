<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if(seqId==null){
    seqId = "";
  }
%> 

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>礼品退库</title>
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
<script type="text/Javascript" src="/yh/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/workflow/workflowUtility/utility.js" ></script>

<script type="text/javascript"> 
function CheckForm(){
  if(document.form1.giftId.value==""){ 
     alert("请选择礼品！");
     form1.giftId.focus();
     form1.giftId.select();
     return (false);
  }
  if(document.form1.transFlag.value=="") { 
     alert("数量不能为空！");
     form1.transFlag.focus();
     form1.transFlag.select();
     return (false);
  }
  var IsInt = "^[0-9]*[1-9][0-9]*$";//正整数　
  var IsInt2 =   "^-?\\d+$";//整数
  var re2 = new RegExp(IsInt2);
  if(document.getElementById("transFlag").value.match(re2) == null ){
    alert("数量应为整数!");
    document.getElementById("transFlag").focus();
    document.getElementById("transFlag").select();
    return false;
  }
  //var giftId = $("giftId").value;
 // var giftIdArray  = giftId.split(",");
 // var transQty = $("transQty").value;
  //alert(giftIdArray[1]+":"+transQty);
 // if(parseInt(giftIdArray[1],10)<parseInt(transQty,10)){
   // alert("领用数量大于库存数量，请仔细检查!");
  	// form1.transQty.focus();
  	//form1.transQty.select();
   // return (false);
  //}
  
  if(parseInt(document.form1.transFlag.value,10)>parseInt($("useTrans").value,10)){ 
    alert("回退数量不能超过当次申领数量!");
   	 form1.transFlag.focus();
   	form1.transFlag.select();
     return (false);
   }	
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
 
function LoadWindow2()
{
  URL="tree.php";
  var event = getEvent();
  loc_x=document.body.scrollLeft+event.clientX-event.offsetX-100;
  loc_y=document.body.scrollTop+event.clientY-event.offsetY+170;
  window.open(URL,"","height=355px,width=320px,directories=no,menubar=no,toolbar=no,status=no,scrollbars=yes,location=no,top="+loc_y+",left="+loc_x+"");
 
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
    $("returnDiv").style.display ="none";
    $("bodyDiv").style.display="none";
    $("returnDelDiv").style.display="";
   }
}
function updateVote(seqId){
  window.location.href ="<%=contextPath%>/subsys/oa/gift_product/outstock/updateOutstock.jsp?seqId="+seqId;
}
function doOnload(){
  var seqId = '<%=seqId%>';
  getOutstockBack(seqId);
  
  todayOutstock();
}
function getOutstockBack(seqId){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/giftProduct/outstock/act/YHGiftOutstockAct/getOutstockByIdBack.act?seqId="+seqId;
    var json = getJsonRs(requestURL); 
      //alert(rsText);
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    }
    var prc = json.rtData;
    if(prc.seqId){
     var seqId = prc.seqId;
     var transQty = prc.transQty;
     var transFlag = prc.transFlag;
     $("seqId").value = seqId;
     $("operator").value = prc.operator;
     $("giftType").value = prc.giftType;
     $("giftId").value = prc.giftName;
     var useTrans = 0;
     if(transQty==''){
       transQty = 0;
     }
     if(transFlag==''){
       transFlag = 0;
     }
     useTrans = parseInt(transQty,10)-parseInt(transFlag,10);
     $("useTrans").value = useTrans;
     $("transUses").value = prc.transUses;
     $("transMemo").value = prc.transMemo;
     $("useTransStr").innerHTML = useTrans;
    }
}
function Init(){
  if(CheckForm()){
    var requestURL = "<%=contextPath%>/yh/subsys/oa/giftProduct/outstock/act/YHGiftOutstockAct/updateGiftstock.act";
    var json = getJsonRs(requestURL,mergeQueryString($("form1"))); 
      //alert(rsText);
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    }
    $("returnDiv").style.display ="";
    $("bodyDiv").style.display="none";
    $("returnDelDiv").style.display="none";
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
        var useGiftQty = prc.useGiftQty;
        if(useGiftQty==''){
          useGiftQty = 0;
        }
        var transFlag = prc.transFlag;
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
function returnOutstock(){
  window.location.href = "<%=contextPath%>/subsys/oa/gift_product/outstock/recode.jsp";
}
</script>
</head>
<body class="" topmargin="5" onload="doOnload();">
<div id="bodyDiv" >
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/system/filefolder/images/notify_new.gif" align="absmiddle"><span class="big3">&nbsp;礼品领用退库</span>
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
       <input type="text" name="operator" id="operator" value="" class="BigStatic" readonly="readonly"/>
    
	  </td>
    </tr>
   <tr>
      <td nowrap  class="TableData" style="">领用类型 : </td>
      <td nowrap class="TableData" style="">&nbsp;回退登记&nbsp;&nbsp;<font color=red>!!!注意,回退时只对回退数量有效!</font></td>
    </tr>
  
   <tr>
      <td nowrap class="TableData">礼品类别：</td>
      <td class="TableData">
      <input type="text" id="giftType" name="giftType" class="BigStatic" value="" readonly="readonly" >
       </td>
   </tr>
  	<tr>
      <td nowrap class="TableData">礼品：</td>
      <td class="TableData">
       <input type="text" id="giftId" name="giftId" class="BigStatic" value="" readonly="readonly" >
    </td>
      </tr>
   <tr>
      <td nowrap class="TableData">数量：</td>
      <td class="TableData">
        <input type="text" name="transFlag" id="transFlag" size="10" maxlength="20" class="BigInput" value="" >
         <input type="hidden" name="useTrans" id="useTrans" size="10" maxlength="20" class="BigInput" value="" >
		<font color=red>回退数量不能超过当次申领数量:<span id="useTransStr"></span></font>			
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">礼品用途：</td>
      <td class="TableData">
        <textarea id="transUses" name="TransUses" cols="45" rows="3" class="BigInput"></textarea>
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
      <div class="content" style="font-size:12pt">回退成功！！</div>
    </td>
  </tr>
</table>
<br>
<div align="center">
 <input type="button"  value="返回" class="BigButton" onClick="returnOutstock();">
</div>

</div>
<div id="returnDelDiv" style="display:none">
<table class="MessageBox" align="center" width="440">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">删除成功！！</div>
    </td>
  </tr>
</table>
<br>
<div align="center">
 <input type="button"  value="返回" class="BigButton" onClick="returnOutstock();">
</div>

</div>

</body>
</html>

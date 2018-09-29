<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>库存登记管理</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="/yh/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="/yh/core/styles/style3/css/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/officeProduct/js/transInfoLogic.js"></script>
<script type="text/javascript">

function sel_change(){
   document.form1.borrower.value="";
   document.form1.borrowerDesc.value="";
   document.form1.PRICE.value="";
   
   if(form1.TRANS_FLAG.value=="-1")
   {
      if(document.all("BORROWER1").style.display=="")
         document.all("BORROWER1").style.display="none";
      if(document.all("COST").style.display=="")
         document.all("COST").style.display="none";
      if(document.all("COMP").style.display=="")
         document.all("COMP").style.display="none";
      if(document.all("BANDER").style.display=="")
         document.all("BANDER").style.display="none";
      if(document.all("REMEND").style.display=="")
         document.all("REMEND").style.display="none";
      if(document.all("REPAIR").style.display=="")
         document.all("REPAIR").style.display="none";
     // if(document.all("RE_REMEND").style.display=="")
     //    document.all("RE_REMEND").style.display="none";
      if(document.all("PCONTENT2").style.display=="")
         document.all("PCONTENT2").style.display="none";
         document.all("PCONTENT1").style.display="";
   }
   if(form1.TRANS_FLAG.value=="0")
   {
      if(document.all("BORROWER1").style.display=="")
         document.all("BORROWER1").style.display="none";
      if(document.all("REMEND").style.display=="")
         document.all("REMEND").style.display="none";
      if(document.all("REPAIR").style.display=="")
         document.all("REPAIR").style.display="none";
     // if(document.all("RE_REMEND").style.display=="")
     //    document.all("RE_REMEND").style.display="none";
     // if(document.all("REM_TIME1").style.display=="")
     //    document.all("REM_TIME1").style.display="none";
      if(document.all("PCONTENT2").style.display=="")
         document.all("PCONTENT2").style.display="none";
         document.all("PCONTENT1").style.display="";
      document.all("COST").style.display="";
      document.all("COMP").style.display="";
      document.all("BANDER").style.display="";
  	  document.all("PCOUNT").style.display="";
   }

      if(form1.TRANS_FLAG.value=="1")
      {
        if(document.all("COST").style.display=="")
      	   document.all("COST").style.display="none";  
      	if(document.all("COMP").style.display=="")
      	   document.all("COMP").style.display="none"; 
      	if(document.all("BANDER").style.display=="")
      	   document.all("BANDER").style.display="none"; 
        if(document.all("REPAIR").style.display=="")
           document.all("REPAIR").style.display="none";
     //   if(document.all("RE_REMEND").style.display=="")
     //      document.all("RE_REMEND").style.display="none";
     //   if(document.all("REM_TIME1").style.display=="")
     //      document.all("REM_TIME1").style.display="none";
      if(document.all("PCONTENT2").style.display=="")
         document.all("PCONTENT2").style.display="none";
         document.all("PCONTENT1").style.display="";
        document.all("BORROWER1").style.display="";
        document.all("REMEND").style.display="";
  	    document.all.t1.style.display='';
  	    document.all.t2.style.display='none';
  	    document.all.t3.style.display='none';
  	    document.all("PCOUNT").style.display="";
      }

      if(form1.TRANS_FLAG.value=="2")
      {
       	if(document.all("COST").style.display=="")
      	  document.all("COST").style.display="none";  
      	if(document.all("COMP").style.display=="")
      	   document.all("COMP").style.display="none"; 
      	if(document.all("BANDER").style.display=="")
      	   document.all("BANDER").style.display="none";   
        if(document.all("REPAIR").style.display=="")
           document.all("REPAIR").style.display="none";
      //  if(document.all("RE_REMEND").style.display=="")
      //     document.all("RE_REMEND").style.display="none";
      //  if(document.all("REM_TIME1").style.display=="")
      //     document.all("REM_TIME1").style.display="none";
      if(document.all("PCONTENT2").style.display=="")
         document.all("PCONTENT2").style.display="none";
         document.all("PCONTENT1").style.display="";
        document.all("BORROWER1").style.display="";
        document.all("REMEND").style.display="";
  	    document.all.t2.style.display='';
  	    document.all.t1.style.display='none';
  	    document.all.t3.style.display='none';
  	    document.all("PCOUNT").style.display="";
      }

      if(form1.TRANS_FLAG.value=="3")
      {
       	if(document.all("COST").style.display=="")
      	  document.all("COST").style.display="none";   
      	if(document.all("COMP").style.display=="")
      	   document.all("COMP").style.display="none"; 
      	if(document.all("BANDER").style.display=="")
      	   document.all("BANDER").style.display="none"; 
        if(document.all("REMEND").style.display=="")
          document.all("REMEND").style.display="none"; 
        if(document.all("REPAIR").style.display=="")
           document.all("REPAIR").style.display="none";
      //  if(document.all("RE_REMEND").style.display=="")
      //     document.all("RE_REMEND").style.display="none";
      //  if(document.all("REM_TIME1").style.display=="")
      //     document.all("REM_TIME1").style.display="none";
      if(document.all("PCONTENT2").style.display=="")
         document.all("PCONTENT2").style.display="none";
         document.all("PCONTENT1").style.display="";
        document.all("BORROWER1").style.display="";
  	    document.all.t3.style.display='';
  	    document.all.t1.style.display='none';
  	    document.all.t2.style.display='none';
  	    document.all("PCOUNT").style.display="";
      }
      if(form1.TRANS_FLAG.value=="4")
      {

        if(document.all("BORROWER1").style.display=="")
      	   document.all("BORROWER1").style.display="none";
      	if(document.all("COMP").style.display=="")
      	   document.all("COMP").style.display="none"; 
      	if(document.all("BANDER").style.display=="")
      	   document.all("BANDER").style.display="none"; 
      	if(document.all("COST").style.display=="")
      	   document.all("COST").style.display="none";     
        if(document.all("REMEND").style.display=="")
           document.all("REMEND").style.display="none";  
        if(document.all("REPAIR").style.display=="")
           document.all("REPAIR").style.display="none"; 
     //   if(document.all("RE_REMEND").style.display=="")
     //      document.all("RE_REMEND").style.display="none";
     //   if(document.all("REM_TIME1").style.display=="")
     //      document.all("REM_TIME1").style.display="none";
      if(document.all("PCONTENT2").style.display=="")
         document.all("PCONTENT2").style.display="none";
         document.all("PCONTENT1").style.display="";
      	document.all.t3.style.display='none';
  	    document.all.t1.style.display='none';
  	    document.all.t2.style.display='none';
  	    document.all("PCOUNT").style.display="";
      }
      if(form1.TRANS_FLAG.value=="5")
      {
        if(document.all("BORROWER1").style.display=="")
      	   document.all("BORROWER1").style.display="none";
      	if(document.all("COMP").style.display=="")
      	   document.all("COMP").style.display="none"; 
      	if(document.all("BANDER").style.display=="")
      	   document.all("BANDER").style.display="none"; 
      	if(document.all("COST").style.display=="")
      	   document.all("COST").style.display="none";     
        if(document.all("REMEND").style.display=="")
           document.all("REMEND").style.display="none";             
        if(document.all("PCOUNT").style.display=="")
           document.all("PCOUNT").style.display="none";
        if(document.all("PCONTENT1").style.display=="")
           document.all("PCONTENT1").style.display="none";
           document.all("PCONTENT2").style.display="";
        document.all("REPAIR").style.display="";
     //   document.all("RE_REMEND").style.display="";
     //   document.all("REM_TIME1").style.display="";
      	document.all.t3.style.display='none';
  	    document.all.t1.style.display='none';
  	    document.all.t2.style.display='none';
      }
}
var mhmc = 0;
function LoadWindow1(){
	if(document.getElementById('BLURRED').style.display=="none")   {
		document.getElementById('BLURRED').style.display="";
		document.getElementById('officePro').disabled="true";
		document.getElementById('officePro').value="";
		document.getElementById('officeProtype').disabled="true";
		document.getElementById('officeProtype').value="";
		document.getElementById('OFFICE_DEPOSITORY').disabled="true";
		document.getElementById('OFFICE_DEPOSITORY').value="";
		mhmc = 1;
	} else{
		document.getElementById('BLURRED').style.display="none";   	  
		document.getElementById('officePro').disabled="";
		document.getElementById('officeProtype').disabled="";
		document.getElementById('OFFICE_DEPOSITORY').disabled="";
		mhmc = 0;
	}
}

function check_pro_name(name){
	if(name){
		//alert(encodeURIComponent(name));
		var urlStr = contextPath + "/yh/subsys/oa/officeProduct/manage/act/YHOfficeTranshistoryAct";
		var url = urlStr + "/getProductsByProName.act";
		var rtJson = getJsonRs(url,"name=" + encodeURIComponent(name));
		//alert(rsText);
		if (rtJson.rtState == "0") {
			var prcs = rtJson.rtData;
			var infoStr = "";
			if(prcs.length>0){
				for ( var i = 0; i < prcs.length; i++) {
					var proId = prcs[i].proId;
					var proName = prcs[i].proName;
					var proStock = prcs[i].proStock;
					var proValue = proId + "|" + proName  + "/库存" + proStock;
					infoStr += "<a href = 'javascript:;' onclick=submitit(\"" +  proValue + "\")  >" + proName + "/库存" + proStock + "</a>"
				}
			}
			$("PRO_NAME_AREA").show();
			$("PRO_NAME_AREA").innerHTML = infoStr;
		}else{
			alert(rtJson.rtMsrg);
			return;
		}
	}
}

function submitit(valueStr){
	//alert(valueStr);
	var value = decodeURI(valueStr);
	var strs = value.split("|"); 
	document.getElementById("PRO_NAME").value = strs[1];
	$("PRO_ID_TEXT").value = strs[0];
	//alert($("PRO_ID_TEXT").value);
	document.getElementById("PRO_NAME_AREA").style.display = "none";
}

function checkForm(){
	var officePro = $("officePro").value;
	var proIdText = $("PRO_ID_TEXT").value;
	var transQty = $("TRANS_QTY").value;
	
	var transFlag = $("TRANS_FLAG").value;
	var price = $("PRICE").value;	
	var borrower = $("borrower").value;
	var borrowerDesc = $("borrowerDesc").value;
	var repTime = $("REP_TIME").value;

	if(transFlag.trim() == "-1"){
		alert("请选择登记类型！");
		$("TRANS_FLAG").focus();
		$("TRANS_FLAG").select();
		return (false);
	}
	if(transFlag.trim() == "0"){
		if(price.trim() == "" || price == "0.00" || price == "0.0" || price == "0"){
			alert("请输入正确的采购单价！");
			$("PRICE").focus();
			$("PRICE").select();
			return (false);
		}
		if(!isNumber(price.trim()) ){
			alert("采购单价必须是数字！");
			$("PRICE").focus();
			$("PRICE").select();
			return (false);
		}
	}
	if(transFlag.trim() == "1" || transFlag.trim() == "2" || transFlag.trim() == "3"){
		if(borrower.trim() == "" && borrowerDesc.trim() == ""){

			if(transFlag == "1"){
				alert( "领用人不能为空！");
			}
			if(transFlag == "2"){
				alert( "借用人不能为空！");
			}
			if(transFlag == "3"){
				alert( "归还人不能为空！");
			}
			$("borrowerDesc").focus();
			$("borrowerDesc").select();
			return (false);
		}
	}
	
	if(officePro.trim() == "-1" && proIdText.trim() == ""){
		alert("请选择办公用品！");
		//$("officePro").focus();
		//$("officePro").select();
		return (false);
	}
	if(transQty.trim() == "" && transFlag != "5"){
		alert("数量不能为空！");
		$("TRANS_QTY").focus();
		$("TRANS_QTY").select();
		return (false);
	}
	if(transQty.trim() == "0" ){
		alert("数量不能为0！");
		$("TRANS_QTY").focus();
		$("TRANS_QTY").select();
		return (false);
	}
	if(!isNumber(transQty.trim()) && transFlag != "5"){
		alert("数量必须是数字！");
		$("TRANS_QTY").focus();
		$("TRANS_QTY").select();
		return (false);
	}


	if(transFlag.trim() == "5"){
		if(repTime.trim() == ""){
			alert("维护日期不能为空！");
			$("REP_TIME").focus();
			$("REP_TIME").select();
			return (false);
		}
	}
	return true;
}

function doSubmit(){
	if(checkForm()){
		var requestURI = "<%=contextPath%>/yh/subsys/oa/officeProduct/manage/act/YHOfficeTranshistoryAct";
		var pars = Form.serialize($('form1'));
		var url = requestURI + "/addOfficeTrans.act";
		var rtJson = getJsonRs(url,pars);
		if(rtJson.rtState == "0"){
			//window.location.reload();
			$("showFormDiv").hide();
			$("remindDiv").show();
		}else{
			alert(rtJson.rtMsrg);
		}
	}
}

function todayOutstock(){
	var requestURL = "<%=contextPath%>/yh/subsys/oa/officeProduct/manage/act/YHOfficeTranshistoryAct";
	var url = requestURL + "/getProductsByToday.act";
	var json = getJsonRs(url);
	//alert(rsText);
	if(json.rtState == '1'){ 
		alert(json.rtMsrg); 
		return ; 
	}
	var prcs = json.rtData;
	var table = new Element('table',{"border":"0","width":"95%" ,"cellspacing":"0", "cellpadding":"3" ,"class":"TableList"}).update("<tbody id='tbody'>"
		+"<tr class='TableHeader'>"
		+"<td nowrap align='center'>办公用品名称</td>"
		+"<td nowrap align='center'>登记类型</td>"
		+"<td nowrap align='center'>领用/借用/归还人</td>"
		+"<td nowrap align='center'>数量</td>" 
		+"<td nowrap align='center'>当前库存</td> "
		+"<td nowrap align='center'>单价</td>"
		+"<td nowrap align='center'>操作日期</td>"
		+"<td nowrap align='center'>操作员</td>"
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
		for(var i=0;i<prcs.length;i++){
			var prc = prcs[i];
			var transId = prc.transId;
			var proName = prc.proName;
			var transName = prc.transName;
			var borrowerName = prc.borrowerName;
			var transQty = "";
			if(isNaN(prc.transQty)){
				transQty = "-";
			}
			else{
				transQty = Math.abs(prc.transQty);
			}
			var proStock = prc.proStock;
			var price = prc.price;
			var transDate = prc.transDate;
			var operatorName = prc.operatorName;

			if(transDate){
				transDate = transDate.substr(0,10);
			}
			

		  var opts = "<a href='javascript:updateVote("+transId+");'>修改</a>&nbsp;"
      +"<a href='javascript:deleteVote("+transId+");'>放弃操作</a>&nbsp;";
			
		  var tr = new Element('tr',{"class":"TableLine1"}).update("<td nowrap align='center'>" + proName + "</td>"
          +"<td nowrap align='center'>" + transName + "</td>"
          +"<td nowrap align='center'>" + borrowerName + "</td>"
          +"<td nowrap align='center'>" + transQty + "</td>"
          +"<td nowrap align='center'>" + proStock + "</td>"
          +"<td nowrap align='center'>" + price + "</td>"
          +"<td nowrap align='center'>" + transDate+  "</td>"
          +"<td nowrap align='center'>" + operatorName + "</td>"
          +"<td nowrap align='center'>"
          +opts +"</td>");
			newTrElement(table,[{className:"TableLine1"}],9,[proName,transName,borrowerName,transQty,proStock,price,transDate,operatorName,opts])
		}
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



function updateVote1(transId){
	
}
function updateVote(transId){
	window.location.href = "<%=contextPath%>/subsys/oa/officeProduct/manage/editRecord.jsp?transId=" + transId;
}



function deleteVote1(transId){
	
}
function deleteVote(transId){
	var msg='确认放弃该操作？放弃该操作库存将回滚到操作前的状态！';
	if(window.confirm(msg)){
		var requestURL = "<%=contextPath%>/yh/subsys/oa/officeProduct/manage/act/YHOfficeTranshistoryAct";
		var url = requestURL + "/deleteTransInfo.act";
		var json = getJsonRs(url,"transId=" + transId);
		//alert(rsText);
		if(json.rtState == '1'){ 
			alert(json.rtMsrg); 
			return ; 
		}
		//var prcs = json.rtData;
		window.location.reload();
	}
}

//日期
function setDate(){
	var date1Parameters = {
			inputId:'REP_TIME',
			property:{isHaveTime:false},
			bindToBtn:'date1'
		};
	new Calendar(date1Parameters);
	
//	var date2Parameters = {
//			inputId:'REM_TIME',
//			property:{isHaveTime:true},
//			bindToBtn:'date2'
//		};
//	new Calendar(date2Parameters);
}
function clearValue(str){
	if(str){
		str.value = "";
	}
}



function doInit(){
	setDate();
	getproEditDepositoryNames("OFFICE_DEPOSITORY");
	getProductsNames();
	todayOutstock();
}


</script>
</head>
<body onload="doInit();">
<div id="showFormDiv">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3">&nbsp;新建库存登记</span>
    </td>
  </tr>
</table>
<br>
<form  action="add.php"  method="post" name="form1" id="form1">
<table width="70%" align="center" class="TableBlock">
    <tr>
      <td nowrap class="TableData">登记类型：</td>
      <td class="TableData">
         <select name="TRANS_FLAG" id="TRANS_FLAG" onChange="sel_change()">
           <option value="-1">---请选择---</option>
           <option value="0" >采购入库</option>
           <option value="1" >领用</option>
           <option value="2" >借用</option>
           <option value="3" >归还</option>
           <option value="5" >维护</option>
           <option value="4" >报废</option>
        </select>
      </td>
    </tr>
    <tr id="BORROWER1" style=display:none>
      <td nowrap id="t1" class="TableData" style=display:none>领用人：</td>
      <td nowrap id="t2" class="TableData" style=display:none>借用人：</td>
      <td nowrap id="t3" class="TableData" style=display:none>归还人：</td>
      <td class="TableData">
      	<input type="hidden" name="borrower" id="borrower" value="">
        <input type="text" name="borrowerDesc" id="borrowerDesc" size="20" readonly class="BigInput" maxlength="20"  value="">
       <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['borrower', 'borrowerDesc']);">添加</a>
       <a href="javascript:;" class="orgClear" onClick="$('borrower').value='';$('borrowerDesc').value='';">清空</a>
      </td>
    </tr>
   <tr id="COST" style=display:none>
      <td nowrap class="TableData">单价：<font style='color:red'>*</font>
      </td>
      <td class="TableData">
      	<input type="text" name="PRICE" id="PRICE" size="20" maxlength="6" class="BigInput" value="0.0">
      </td>
   </tr>
   <tr id="BANDER" style=display:none>
      <td nowrap class="TableData">品牌：</td>
      <td class="TableData">
      	<input type="text" name="BAND" id="BAND" size="20" maxlength="20" class="BigInput">
      </td>
   </tr>
   <tr id="COMP" style=display:none>
      <td nowrap class="TableData">厂家：</td>
      <td class="TableData">
      	<input type="text" name="COMPANY" id="COMPANY" size="20" maxlength="20" class="BigInput">
      </td>
   </tr>
    <tr>
      <td nowrap class="TableData">办公用品库： </td>
      <td nowrap class="TableData">
      <select id="OFFICE_DEPOSITORY" name="OFFICE_DEPOSITORY"  onchange = "depositoryOfType(this.value);">
       <option value="-1">请选择类别 </option>
		</select>
    </td>
   </tr>
   <tr>
      <td nowrap class="TableData">办公用品类别：</td>
      <td class="TableData"  id="OFFICE_TYPE">
        <select id="officeProtype" name="officeProtype" onchange = "depositoryOfProducts(this.value);">
           <option value="-1">请选择类别 </option>
         </select>
       </td>
   </tr>
  	<tr>
      <td nowrap class="TableData">办公用品：</td>
      <td class="TableData" id="OFFICE_PRODUCTS" >
        <select name="officePro" id="officePro"  >
           <option value="-1">---请选择---</option>
         </select> &nbsp;
		 <input type="button" name="SelectPro" title="模糊选择" value="模糊选择" class="SmallButtonC" onClick="LoadWindow1()">
       </td>
      </tr>
    <tr id="BLURRED" style="display:none">
      <td nowrap class="TableData">模糊名称:</td>
      <td class="TableData">
        <input type="text" id="PRO_NAME" name="PRO_NAME" size="20" maxlength="20" onkeyup="check_pro_name(this.value)" class="BigInput" value="">&nbsp;&nbsp;
      <div id="PRO_NAME_AREA"></div>
      </td>
    </tr>
   <tr id="PCOUNT" style=>
      <td nowrap class="TableData">数量：</td>
      <td class="TableData">
        <input type="text" name="TRANS_QTY" id="TRANS_QTY" size="20" maxlength="6" class="BigInput" value="">
      </td>
    </tr>
    <!--
     <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData">
        <textarea name="REMARK" cols="45" rows="5" class="BigInput"></textarea>
      </td>
    </tr>
    -->
    <tr id="REPAIR" style=display:none>
      <td nowrap class="TableData">维护日期：</td>
      <td class="TableData">
      <input type="text" name="REP_TIME" id="REP_TIME" size="15" maxlength="20" class="BigInput" value="" readonly="readonly" onfocus="clearValue(this);" />
    	<img id="date1" align="middle" src="/yh/core/styles/style3/img/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
   </tr>
   <!-- <tr id="RE_REMEND" style=display:none>
      <td nowrap class="TableData">提醒：</td>
      <td class="TableData"></td>
     </tr>
     <tr id="REM_TIME1" style=display:none>
      <td nowrap class="TableData"> 提醒时间：</td>
      <td class="TableData">
      <input type="text" name="REM_TIME" id="REM_TIME" size="15" maxlength="20" class="BigInput" value="" readonly="readonly" onfocus="clearValue(this);" />
    	<img id="date2" align="middle" src="/yh/core/styles/style3/img/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      
      </td>
    </tr> -->
     <tr id ="PCONTENT1" style=>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData">
        <textarea name="REMARK1" id="REMARK1" cols="45" rows="5" class="BigInput"></textarea>
      </td>
     </tr>
     <tr id ="PCONTENT2" style=display:none>
      <td nowrap class="TableData">维护内容：</td>
      <td class="TableData">
        <textarea name="REMARK2" id="REMARK2" cols="45" rows="5" class="BigInput"></textarea>
      </td>
     </tr>
     <tr id="REMEND" style=display:none>
      <td nowrap class="TableData">提醒：</td>
      <td class="TableData"><input type="checkbox" name="SMS_REMIND" id="SMS_REMIND"><label for="SMS_REMIND">使用内部短信提醒</label>&nbsp;&nbsp;      	</td>
     </tr>
    <tfoot align="center" class="TableFooter">
      <td colspan="2" nowrap>
        <input type="hidden" name="TRANS_ID" value="">
        <input type="hidden" name="OLD_PRO_ID" value="">
        <input type="hidden" name="TRANS_NAME" value="">
        <input type="hidden" name="PRO_ID_TEXT" id="PRO_ID_TEXT" value="">
        <input type="button" value="确认" class="BigButton" onClick="doSubmit();">&nbsp;&nbsp;
      </td>
    </tfoot>
  </table>
</form>
<div id="todayOutstock"></div>
</div>
<br>
<div id="remindDiv" style="display: none">
<table class="MessageBox" align="center" width="320">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">新建库存登记成功！</div>
    </td>
  </tr>
</table>
<br><center>
	<input type="button" class="BigButton" value="返回" onclick="window.location.href='<%=contextPath %>/subsys/oa/officeProduct/manage/record.jsp';">
</center>
</div>
</body>
</html>
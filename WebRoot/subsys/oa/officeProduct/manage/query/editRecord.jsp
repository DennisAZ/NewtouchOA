<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
String transId = request.getParameter("transId");
if(transId==null){
	transId = "";
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
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
  	 transFlag11a();
   }
   if(form1.TRANS_FLAG.value=="0")
   {
  	 transFlag0a();
   }

      if(form1.TRANS_FLAG.value=="1")
      {
      	transFlag1a();
      }

      if(form1.TRANS_FLAG.value=="2")
      {
      	transFlag2a();
      }

      if(form1.TRANS_FLAG.value=="3")
      {
      	transFlag3a();
      }
      if(form1.TRANS_FLAG.value=="4")
      {
      	transFlag5a();
      }
      if(form1.TRANS_FLAG.value=="5")
      {
      	transFlag5a();
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
		$("officePro").focus();
		$("officePro").select();
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
		var officePro = $("officePro").value;
		if(officePro && officePro !="-1"){
			$("PRO_ID_TEXT").value = officePro;
		//alert($("PRO_ID_TEXT").value);
		}
		var requestURI = "<%=contextPath%>/yh/subsys/oa/officeProduct/manage/act/YHOfficeTranshistoryAct";
		var pars = Form.serialize($('form1'));
		var url = requestURI + "/updateOfficeTrans.act?transId=<%=transId%>";
		var rtJson = getJsonRs(url,pars);
		if(rtJson.rtState == "0"){
			window.location.reload();
			//$("showFormDiv").hide();
			//$("remindDiv").show();
		}else{
			alert(rtJson.rtMsrg);
		}
	}
}


function updateVote(transId){
	//alert(transId);
	window.location.href = "<%=contextPath%>/subsys/oa/officeProduct/manage/editRecord.jsp?transId=" + transId;
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
		window.location.reload();
	}
}


function doInit(){
	//getproEditDepositoryNames("OFFICE_DEPOSITORY");
	getProductsNames();
	//setDate();
	var transId = "<%=transId%>";
	if(transId){
		var requestURL = "<%=contextPath%>/yh/subsys/oa/officeProduct/manage/act/YHOfficeTranshistoryAct";
		var url = requestURL + "/getTransInfoById.act";
		var json = getJsonRs(url,"transId="+transId);
		//alert(rsText);
		if(json.rtState == '0'){ 
			var prcs = json.rtData;
			//alert(prcs.transQty);
			if(prcs.transFlag){
				setSelectValue("TRANS_FLAG" , prcs.transFlag);
			}
			if(prcs.proId){
				setSelectValue("officePro" , prcs.proId);
				$("officePro").disabled = true;
			}
			if(prcs.transQty){
				$("TRANS_QTY").value = prcs.transQty;
			}
			if(prcs.transFlag =="-1"){
				transFlag11();
			}
			if(prcs.transFlag =="0"){
				transFlag0a();
				$("PRICE").value = prcs.price;
				$("BAND").value = prcs.band;
				$("COMPANY").value = prcs.company;
			}
			if(prcs.transFlag =="1"){
				transFlag1a();
				$("borrower").value = prcs.borrower;
				getPersonName("borrower");
			}
			if(prcs.transFlag =="2"){
				transFlag2a();
				//alert(prcs.borrower);
				$("borrower").value = prcs.borrower;
				getPersonName("borrower");
			}
			if(prcs.transFlag =="3"){
				transFlag3a();
				$("borrower").value = prcs.borrower;
				getPersonName("borrower");
			}
			if(prcs.transFlag =="4"){
				transFlag4a();
			}
			if(prcs.transFlag =="5"){
				transFlag5a();
			}
			$("REMARK1").value = prcs.remark
		}else{
			alert(json.rtMsrg); 
			return ; 
		}
	}
}
function setSelectValue(optionType , extValue) {
	var selects = document.getElementById(optionType);
	for ( var i = 0; i < selects.length; i++) {
		var prc = selects[i];
		if (extValue && (extValue == prc.value)) {
			prc.selected = true;
		}
	}
}
function getPersonName(personIdDiv){
	if($(personIdDiv) && $(personIdDiv).value.trim()){
		bindDesc([{cntrlId:personIdDiv, dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
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
	
	var date2Parameters = {
			inputId:'REM_TIME',
			property:{isHaveTime:true},
			bindToBtn:'date2'
		};
	new Calendar(date2Parameters);
}
function clearValue(str){
	if(str){
		str.value = "";
	}
}

function transFlag0a(){
	if(document.all("BORROWER").style.display=="")
	   document.all("BORROWER").style.display="none";
 if(document.all("PCONTENT2").style.display=="")
    document.all("PCONTENT2").style.display="none";
    document.all("PCONTENT1").style.display="";
	document.all("COST").style.display="";
 document.all("COMP").style.display="";
 document.all("BANDER").style.display="";
 document.all("PCOUNT").style.display="";
	
}
function transFlag1a(){
 if(document.all("COST").style.display=="")
	  document.all("COST").style.display="none";
	if(document.all("COMP").style.display=="")
	   document.all("COMP").style.display="none"; 
	if(document.all("BANDER").style.display=="")
	   document.all("BANDER").style.display="none"; 
if(document.all("PCONTENT2").style.display=="")
  document.all("PCONTENT2").style.display="none";
  document.all("PCONTENT1").style.display="";
 document.all("BORROWER").style.display="";
 document.all.t1.style.display='';
 document.all.t2.style.display='none';
 document.all.t3.style.display='none';
 document.all("PCOUNT").style.display="";
}
function transFlag2a(){
	if(document.all("COST").style.display=="")
	  document.all("COST").style.display="none";
	if(document.all("COMP").style.display=="")
	   document.all("COMP").style.display="none"; 
	if(document.all("BANDER").style.display=="")
	   document.all("BANDER").style.display="none"; 
if(document.all("PCONTENT2").style.display=="")
   document.all("PCONTENT2").style.display="none";
   document.all("PCONTENT1").style.display="";
  document.all("BORROWER").style.display="";
  document.all.t2.style.display='';
  document.all.t1.style.display='none';
  document.all.t3.style.display='none';
  document.all("PCOUNT").style.display="";

}
function transFlag3a(){
	if(document.all("COST").style.display=="")
	  document.all("COST").style.display="none";
	if(document.all("COMP").style.display=="")
	   document.all("COMP").style.display="none"; 
	if(document.all("BANDER").style.display=="")
	   document.all("BANDER").style.display="none"; 
if(document.all("PCONTENT2").style.display=="")
   document.all("PCONTENT2").style.display="none";
   document.all("PCONTENT1").style.display="";
  document.all("BORROWER").style.display="";
  document.all.t3.style.display='';
  document.all.t1.style.display='none';
  document.all.t2.style.display='none';
  document.all("PCOUNT").style.display="";

}
function transFlag4a(){
	if(document.all("BORROWER").style.display=="")
	   document.all("BORROWER").style.display="none";
	if(document.all("COMP").style.display=="")
	   document.all("COMP").style.display="none"; 
	if(document.all("BANDER").style.display=="")
	   document.all("BANDER").style.display="none"; 
	if(document.all("COST").style.display=="")
	   document.all("COST").style.display="none";
 if(document.all("PCONTENT2").style.display=="")
    document.all("PCONTENT2").style.display="none";
    document.all("PCONTENT1").style.display="";
	document.all.t3.style.display='none';
 document.all.t1.style.display='none';
 document.all.t2.style.display='none';
 document.all("PCOUNT").style.display="";

}
function transFlag5a(){
	if(document.all("BORROWER").style.display=="")
	   document.all("BORROWER").style.display="none";
	if(document.all("COMP").style.display=="")
	   document.all("COMP").style.display="none"; 
	if(document.all("BANDER").style.display=="")
	   document.all("BANDER").style.display="none"; 
	if(document.all("COST").style.display=="")
	   document.all("COST").style.display="none";               
 if(document.all("PCOUNT").style.display=="")
    document.all("PCOUNT").style.display="none";
 if(document.all("PCONTENT1").style.display=="")
    document.all("PCONTENT1").style.display="none";
    document.all("PCONTENT2").style.display="";
	document.all.t3.style.display='none';
 document.all.t1.style.display='none';
 document.all.t2.style.display='none';

}
//-1
function transFlag11a(){
    if(document.all("BORROWER").style.display=="")
  	  document.all("BORROWER").style.display="none";
  	if(document.all("COST").style.display=="")
  	  document.all("COST").style.display="none";
    if(document.all("COMP").style.display=="")
       document.all("COMP").style.display="none";
    if(document.all("BANDER").style.display=="")
       document.all("BANDER").style.display="none";
  if(document.all("PCONTENT2").style.display=="")
     document.all("PCONTENT2").style.display="none";
     document.all("PCONTENT1").style.display="";



    
}


</script>
</head>
<body onload="doInit();">
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
           <option value="4" >报废</option>
           <option value="5" >维护</option>
        </select>
      </td>
    </tr>
    <tr id="BORROWER" style=display:none>
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
      <td nowrap class="TableData">单价：</td>
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
   <%-- <tr>
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
   </tr> --%>
  	<tr>
      <td nowrap class="TableData">办公用品：</td>
      <td class="TableData" id="OFFICE_PRODUCTS" >
        <select name="officePro" id="officePro"  >
           <option value="-1">---请选择---</option>
         </select> 
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
    <%--<tr id="REPAIR" style=display:none>
      <td nowrap class="TableData">维护日期：</td>
      <td class="TableData">
      	<input type="text" name="REP_TIME" id="REP_TIME" size="15" maxlength="20" class="BigInput" value="" readonly="readonly" onfocus="clearValue(this);" />
    	<img id="date1" align="middle" src="/yh/core/styles/style3/img/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
   </tr> --%>
   <%--<tr id="RE_REMEND" style=display:none>
      <td nowrap class="TableData">提醒：</td>
      <td class="TableData"></td>
     </tr> --%>
     <tr id="REM_TIME1" style=display:none>
      <td nowrap class="TableData"> 提醒时间：</td>
      <td class="TableData">
        <input type="text" name="REM_TIME" id="REM_TIME" size="15" maxlength="20" class="BigInput" value="" readonly="readonly" onfocus="clearValue(this);" />
    	<img id="date2" align="middle" src="/yh/core/styles/style3/img/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      
      </td>
    </tr>
     <tr id ="Pcontent1" style=>
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
        <input type="button" value="返回" class="BigButton" onClick="location='javascript:history.back();'">
      </td>
    </tfoot>
  </table>
</form>

<div id="todayOutstock"></div>

</body>
</html>
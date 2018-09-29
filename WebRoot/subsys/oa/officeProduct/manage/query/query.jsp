<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>办公用品登记查询</title>
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

function doInit(){
	setDate();
	getproEditDepositoryNames("OFFICE_DEPOSITORY");
	
}


function sel_change()
{
   document.form1.user.value="";
   document.form1.userDesc.value="";
   if(form1.TRANS_FLAG.value=="-1")
   {
      if(document.all("BORROWER").style.display=="")
        document.all("BORROWER").style.display="none";
      document.all.TRANS_DEPT.style.display='none';
   }
   if(form1.TRANS_FLAG.value=="0")
   {
      if(document.all("BORROWER").style.display=="")
        document.all("BORROWER").style.display="none";
      document.all.TRANS_DEPT.style.display='none';
   }

   if(form1.TRANS_FLAG.value=="1")
   {
      document.all("BORROWER").style.display="";
      document.all.t1.style.display='';
      document.all.t2.style.display='none';
      document.all.t3.style.display='none';
      document.all.TRANS_DEPT.style.display='';
   }

   if(form1.TRANS_FLAG.value=="2")
   {
      document.all("BORROWER").style.display="";
      document.all.t2.style.display='';
      document.all.t1.style.display='none';
      document.all.t3.style.display='none';
      document.all.TRANS_DEPT.style.display='';
   }

   if(form1.TRANS_FLAG.value=="3")
   {
      document.all("BORROWER").style.display="";
      document.all.t3.style.display='';
      document.all.t1.style.display='none';
      document.all.t2.style.display='none';
      document.all.TRANS_DEPT.style.display='';
   }
   if(form1.TRANS_FLAG.value=="4")
   {
      if(document.all("BORROWER").style.display=="")
         document.all("BORROWER").style.display="none";
      document.all.t3.style.display='none';
      document.all.t1.style.display='none';
      document.all.t2.style.display='none';
      document.all.TRANS_DEPT.style.display='none';
   }
   if(form1.TRANS_FLAG.value=="5")
   {
      if(document.all("BORROWER").style.display=="")
         document.all("BORROWER").style.display="none";
      document.all.t3.style.display='none';
      document.all.t1.style.display='none';
      document.all.t2.style.display='none';
      document.all.TRANS_DEPT.style.display='none';
   }
}

//日期
function setDate(){
	var date1Parameters = {
			inputId:'beginDate',
			property:{isHaveTime:false},
			bindToBtn:'date1'
		};
	new Calendar(date1Parameters);
	
	var date2Parameters = {
			inputId:'endDate',
			property:{isHaveTime:false},
			bindToBtn:'date2'
		};
	new Calendar(date2Parameters);
}
function clearValue(str){
	if(str){
		str.value = "";
	}
}

function checkForm(){
	if($("beginDate").value.trim() && !isValidDateStr($("beginDate").value.trim())){
		alert("起始日期格式不对,应形如 2010-02-01");
		document.getElementById("beginDate").focus();
		document.getElementById("beginDate").select();
		return false;
	}
	if($("endDate").value.trim() && !isValidDateStr($("endDate").value.trim())){
		alert("结束日期格式不对,应形如 2010-02-01");
		document.getElementById("endDate").focus();
		document.getElementById("endDate").select();
		return false;
	}
	return true;	
}


function query(){
	if(checkForm()){
		var query = $("form1").serialize();
		location = "<%=contextPath%>/subsys/oa/officeProduct/manage/query/search.jsp?"+query;
	}
}
function exreport(){
	var query = $("form1").serialize();
	location.href = "<%=contextPath%>/yh/subsys/oa/officeProduct/manage/act/YHOfficeTranshistoryAct/exportToCSV.act?" + query;;
}
</script>

</head>
<body onload="doInit();">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/infofind.gif"  align="middle"><span class="big3"> 办公用品登记查询</span>
    </td>
  </tr>
</table>
<br>
<div >
 
<form method="post" name="form1" id="form1">
<table class="TableBlock" align="center" >
  <tr>
      <td nowrap class="TableData">登记类型：</td>
      <td class="TableData">
         <select name="TRANS_FLAG" id="TRANS_FLAG"  onChange="sel_change()">
           <option value="-1">请选择</option>
           <option value="0">采购入库</option>
           <option value="1">领用</option>
           <option value="2">借用</option>
           <option value="3">归还</option>
           <option value="5">维护</option>
           <option value="4">报废</option>
        </select>
      </td>
   </tr>
   <tr id="TRANS_DEPT" style=display:none>
    <td class="TableData">登记部门：</td>
   	<td class="TableData" >
      <input type="hidden" name="dept" id="dept" value="">
      <textarea cols=30 name="deptDesc" id="deptDesc" rows=3 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectDept()">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('dept').value='';$('deptDesc').value='';">清空</a>
     </td>
   </tr>
   <tr id="BORROWER" style=display:none>
      <td nowrap id="t1" class="TableData" style=display:none>领用人：</td>
      <td nowrap id="t2" class="TableData" style=display:none>借用人：</td>
      <td nowrap id="t3" class="TableData" style=display:none>归还人：</td>
      <td class="TableData">
         <input type="hidden" name="user" id="user" value="">
	      <textarea cols=30 name="userDesc" id="userDesc" rows=3 class="BigStatic" wrap="yes" readonly></textarea>
	      <a href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
	      <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
      </td>
   </tr>
   <tr>
      <td nowrap class="TableData">办公用品库： </td>
      <td nowrap class="TableData">
      <select name="OFFICE_DEPOSITORY" id="OFFICE_DEPOSITORY"  onchange="depositoryOfType(this.value);">
	  <option value="">请选择</option>
		 </select>
    </td>
   </tr>
   <tr>
      <td nowrap class="TableData">办公用品类别：</td>
      <td class="TableData" id="OFFICE_TYPE" >
        <select name="officeProtype" id="officeProtype" onchange = "depositoryOfProducts(this.value);">
        	 <option value="-1">请选择</option>
         </select>
       </td>
   </tr>
   <tr>
      <td nowrap class="TableData">办公用品：</td>
      <td class="TableData" id="OFFICE_PRODUCTS" >
        <select name="officePro" id="officePro">
           <option value="-1">请选择</option>
        </select> &nbsp;
       </td>
    </tr>
    <tr>
    <td nowrap class="TableData">办公用品名称(模糊)：</td>
    <td nowrap class="TableData">
      <input type="text" name="PRO_NAME" id="PRO_NAME" class="BigInput" size="15" maxlength="10">
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">日期： </td>
    <td nowrap class="TableData">
       <input type="text" name="beginDate" id="beginDate" size="10" maxlength="10" class="BigInput" value=""  />
    	<img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
        
        &nbsp; &nbsp;至&nbsp;
  			<input type="text" name="endDate" id="endDate" size="10" maxlength="10" class="BigInput" value=""  />
    		<img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td>
   </tr>
   <tfoot align="center" class="TableFooter">
    <td colspan="2" align="center">
        <input value="查询" class="BigButton" title="模糊查询" type="button" onclick="query();">
        &nbsp;<input type="button" value="导出" class="BigButton" title="导出办公用品信息" name="button" onClick="exreport()">
    </td>
   </tfoot>
</table>
</form>
</div>



</body>
</html>
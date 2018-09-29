<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>借阅记录</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/util.js"></script>
<script type="text/javascript">
var requestURL = "<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsLendAct";
function doInit(){

	waitApproveFuncs();
	alreadyApproveFuncs();
	returnApproveFuncs();
	notApproveFuncs();
	 
}
function newWindow(url,width,height){
  var locX=(screen.width-width)/2;
  var locY=(screen.height-height)/2;
  window.open(url, "meeting", 
      "height=" +height + ",width=" + width +",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
      + locY + ", left=" + locX + ", resizable=yes");
}

/**
 * 删除、撤销 
 */
function delLendFile(seqId){
	var url = requestURL + "/revocationLend.act";
	var json=getJsonRs(url,"seqId=" + seqId );
	if(json.rtState == '0'){
		location.reload();
	}else{
		alert(json.rtMsrg);
		return ;	
	}
}

/**
 * 归还
 */
function returnLendFile(seqId){
	var url = requestURL + "/returnLend.act";
	var json=getJsonRs(url,"seqId=" + seqId + "&allow=3");
	if(json.rtState == '0'){
		location.reload();
	}else{
		alert(json.rtMsrg);
		return ;	
	}
}


/**
 * 查看文件号
 * @param seqId
 * @return
 */
function open_file(fileId,allow){
	//alert(fileId +"  " + allow);
	var URL = "";
	if(allow == 0){
		URL = contextPath + "/subsys/oa/rollmanage/rolllend/readFile.jsp?seqId=" + fileId;
	}else{
		URL = contextPath + "/subsys/oa/rollmanage/readFile.jsp?seqId=" + fileId;
	}
  //window.open(URL);
  newWindow(URL,'500', '400')
}



function waitApproveFuncs(){

	var url = requestURL + "/getApprovalToBorrow.act";
	var json=getJsonRs(url,"allow=0");
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	prcsJson=json.rtData;
	if(prcsJson.length>0){
		var table=new Element('table',{ "width":"100%","class":"TableList","align":"center"});
		var strTable="<tbody id='tbody'><tr class='TableHeader' align='center' style='font-size:10pt'>"
			+ "<td nowrap width='30%' align='center'>文件号</td>"
			+ "<td nowrap width='20%' align='center'>申请时间</td>"				
			+ "<td nowrap width='20%' align='center'>审批时间</td>"				
			+ "<td nowrap width='20%' align='center'>归还时间</td>"		
			+ "<td nowrap width='10%' align='center'>操作</td>"	
			+ "</tr><tbody>";	

		table.update(strTable);
		$('waitApproveDiv').update(table);
		for(var i = 0;i<prcsJson.length;i++){
			var prcs = prcsJson[i];
			var lendId = prcs.seqId;
			var fileId = prcs.fileId;
			var fileCode = prcs.fileCode;
			var addTime = prcs.addTime;
			var allowTime = prcs.allowTime;
			var returnTime = prcs.returnTime;

			var className = (i % 2 == 0) ? "TableLine1" : "TableLine2";  										
			var tr=new Element('tr',{'width':'90%','class': className ,'font-size':'10pt'});		
			table.firstChild.appendChild(tr);	
			var str = "<td align='center'><a href ='#' onclick='open_file(" + fileId + ",0 )' >"			
				+ fileCode + "</a></td><td align='center'>"					
				+ addTime + "</td><td align='center'>"					
				+ allowTime + "</td><td align='center'>"					
				+ returnTime + "</td>"
				+ "<td align='center'><a href='#' onclick='delLendFile(" + lendId + ")'>撤销</a></td>";
				tr.update(str);
		}


		
		
	}else{
		$("noInfoDiv1").show();
	}
	
}
function alreadyApproveFuncs(){

	var url = requestURL + "/getApprovalToBorrow.act";
	var json=getJsonRs(url,"allow=1");
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	prcsJson=json.rtData;
	if(prcsJson.length>0){
		var table=new Element('table',{ "width":"100%","class":"TableList","align":"center"});
		var strTable="<tbody id='tbody'><tr class='TableHeader' align='center' style='font-size:10pt'>"
			+ "<td nowrap width='30%' align='center'>文件号</td>"
			+ "<td nowrap width='20%' align='center'>申请时间</td>"				
			+ "<td nowrap width='20%' align='center'>审批时间</td>"				
			+ "<td nowrap width='20%' align='center'>归还时间</td>"		
			+ "<td nowrap width='10%' align='center'>操作</td>"	
			+ "</tr><tbody>";	

		table.update(strTable);
		$('alreadyApproveDiv').update(table);
		for(var i=0;i<prcsJson.length;i++){
			var prcs=prcsJson[i];
			var lendId=prcs.seqId;
			var fileId=prcs.fileId;
			var fileCode=prcs.fileCode;
			var addTime=prcs.addTime;
			var allowTime=prcs.allowTime;
			var returnTime=prcs.returnTime;

			var className = (i % 2 == 0) ? "TableLine1" : "TableLine2";  										
			var tr=new Element('tr',{'width':'90%','class': className ,'font-size':'10pt'});		
			table.firstChild.appendChild(tr);	
			var str = "<td align='center'><a href ='#' onclick='open_file(" + fileId + ",1 )' >"			
				+ fileCode + "</a></td><td align='center'>"					
				+ addTime + "</td><td align='center'>"					
				+ allowTime + "</td><td align='center'>"					
				+ returnTime + "</td>"
				+ "<td align='center'><a href='#' onclick='returnLendFile(" + lendId + ")'>归还</a></td>";
				tr.update(str);
		}


		
		
	}else{
		$("noInfoDiv2").show();
	}
	
}




function notApproveFuncs(){

	var url = requestURL + "/getApprovalToBorrow.act";
	var json=getJsonRs(url,"allow=2");
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	prcsJson=json.rtData;
	if(prcsJson.length>0){
		var table=new Element('table',{ "width":"100%","class":"TableList","align":"center"});
		var strTable="<tbody id='tbody'><tr class='TableHeader' align='center' style='font-size:10pt'>"
			+ "<td nowrap width='30%' align='center'>文件号</td>"
			+ "<td nowrap width='20%' align='center'>申请时间</td>"				
			+ "<td nowrap width='20%' align='center'>审批时间</td>"				
			+ "<td nowrap width='20%' align='center'>归还时间</td>"		
			+ "<td nowrap width='10%' align='center'>操作</td>"	
			+ "</tr><tbody>";	

		table.update(strTable);
		$('notApproveDiv').update(table);
		for(var i=0;i<prcsJson.length;i++){
			var prcs=prcsJson[i];
			var lendId=prcs.seqId;
			var fileId=prcs.fileId;
			var fileCode=prcs.fileCode;
			var addTime=prcs.addTime;
			var allowTime=prcs.allowTime;
			var returnTime=prcs.returnTime;
			
			var className = (i % 2 == 0) ? "TableLine1" : "TableLine2";  										
			var tr=new Element('tr',{'width':'90%','class': className ,'font-size':'10pt'});		
			table.firstChild.appendChild(tr);	
			var str = "<td align='center'><a href ='#' onclick='open_file(" + fileId + ",0 )' >"			
				+ fileCode + "</a></td><td align='center'>"					
				+ addTime + "</td><td align='center'>"					
				+ allowTime + "</td><td align='center'>"					
				+ returnTime + "</td>"
				+ "<td align='center'><a href='#' onclick='delLendFile(" + lendId + ")'>撤销</a></td>";
				tr.update(str);
			
		}


		
		
	}else{
		$("noInfoDiv3").show();
	}
	
}
function returnApproveFuncs(){

	var url = requestURL + "/getApprovalToBorrow.act";
	var json=getJsonRs(url,"allow=3");
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	prcsJson=json.rtData;
	if(prcsJson.length>0){
		var table=new Element('table',{ "width":"100%","class":"TableList","align":"center"});
		var strTable="<tbody id='tbody'><tr class='TableHeader' align='center' style='font-size:10pt'>"
			+ "<td nowrap width='30%' align='center'>文件号</td>"
			+ "<td nowrap width='20%' align='center'>申请时间</td>"				
			+ "<td nowrap width='20%' align='center'>审批时间</td>"				
			+ "<td nowrap width='20%' align='center'>归还时间</td>"		
			+ "<td nowrap width='10%' align='center'>操作</td>"	
			+ "</tr><tbody>";	

		table.update(strTable);
		$('returnApproveDiv').update(table);
		for(var i=0;i<prcsJson.length;i++){
			var prcs=prcsJson[i];
			var lendId=prcs.seqId;
			var fileId=prcs.fileId;
			var fileCode=prcs.fileCode;
			var addTime=prcs.addTime;
			var allowTime=prcs.allowTime;
			var returnTime=prcs.returnTime;

			var className = (i % 2 == 0) ? "TableLine1" : "TableLine2";  										
			var tr=new Element('tr',{'width':'90%','class': className ,'font-size':'10pt'});		
			table.firstChild.appendChild(tr);	
			var str = "<td align='center'><a href ='#' onclick='open_file(" + fileId + ",0 )' >"			
				+ fileCode + "</a></td><td align='center'>"					
				+ addTime + "</td><td align='center'>"					
				+ allowTime + "</td><td align='center'>"					
				+ returnTime + "</td>"
				+ "<td align='center'><a href='#' onclick='delLendFile(" + lendId + ")'>删除</a></td>";
				tr.update(str);
		}


		
		
	}else{
		$("noInfoDiv4").show();
	}
	
}





</script>
</head>
<body topmargin="5" onload="doInit();">
<!------------------------------------- 待批准 ------------------------------->

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/roll_manage.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 待批准借阅</span><br>
    </td>
  </tr>
</table>
<br>
<div id="waitApproveDiv"></div>
<div id="noInfoDiv1" style="display:none">
<table class="MessageBox" align="center" width="240">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无待批借阅</div>
    </td>
  </tr>
</table>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath %>/dian1.gif" width="100%"></td>
 </tr>
</table>
</div>
<br>

<!------------------------------------- 已批准 ------------------------------->
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/roll_manage.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 已批准借阅</span><br>
    </td>
  </tr>
</table> 
<br>
<div id="alreadyApproveDiv"></div>

<div id="noInfoDiv2"  style="display:none">
<table class="MessageBox" align="center" width="240">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无已准借阅</div>
    </td>
  </tr>
</table>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath %>/dian1.gif" width="100%"></td>
 </tr>
</table>
</div>
<br>



<!------------------------------------- 未批准 ------------------------------->
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/roll_manage.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 未批准借阅</span><br>
    </td>
  </tr>
</table> 
<br>
<div id="notApproveDiv"></div>

<div id="noInfoDiv3" style="display:none">
<table class="MessageBox" align="center" width="240">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无未准借阅</div>
    </td>
  </tr>
</table>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath %>/dian1.gif" width="100%"></td>
 </tr>
</table>
</div>
<br>


 
<!------------------------------------- 已归还 ------------------------------->
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/roll_manage.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 已归还借阅</span><br>
    </td>
  </tr>
</table>
<br>

<div id="returnApproveDiv"></div>
<div id="noInfoDiv4" style="display:none">
<table class="MessageBox" align="center" width="240">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无归还借阅</div>
    </td>
  </tr>
</table>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath %>/dian1.gif" width="100%"></td>
 </tr>
</table>
</div>

<br>








</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
  String menuModleId =  request.getParameter("menuModleId");
String seqId = request.getParameter("seqId");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发文登记</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/src/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/sendDoc/js/sendLogic.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/subsys/jtgwjh/sendDoc/js/logic.js"></script>
<script type="text/javascript">

function doInit(){
  
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/docSend/act/YHDocSendAct/getDetail.act?seqId=<%=seqId%>";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    bindJson2Cntrl(rtJson.rtData);
    $('securityLevel').innerHTML = renderSecurityLevel(data.securityLevel);
	  if(data.isSign == 1){
	    var data3 =  data.data3;
	    var countAll = 0;
	    var reciveDeptDescArray = data.reciveDeptDesc.split(',');
	    for(var i = 0; i < reciveDeptDescArray.length; i++){
	      for(var j = 0; j < data3.length; j++){
	        if(data3[j].reciveDept == reciveDeptDescArray[i]){
			      var td1 = $C('td');
			      td1.align = 'center';
			      td1.innerHTML = data3[j].reciveDept
			                    + '<input type="hidden" id="taskId_'+i+'" name="taskId_'+i+'" value="'+data3[j].seqId+'">';
			      
			      var td2 = $C('td');
			      td2.align = 'right';
			      td2.innerHTML = '<input type="text" id="print_count_'+i+'" name="print_count_'+i+'" size="5" style="text-align:right;" value="'+data3[j].printCount+'" onchange="consider('+data3.length+')">';
			      countAll = parseInt(countAll) + parseInt(data3[j].printCount);
			      
			      var td3 = $C('td');
			      td3.align = 'right';
			      td3.innerHTML = '<input type="text" id="print_no_start_'+i+'" name="print_no_start_'+i+'" size="5" style="text-align:right;" value="'+data3[j].printNoStart+'">';
			      
			      var td4 = $C('td');
			      td4.align = 'right';
			      td4.innerHTML = '<input type="text" id="print_no_end_'+i+'" name="print_no_end_'+i+'" size="5" style="text-align:right;" value="'+data3[j].printNoEnd+'">';
			      
			      var tr = $C('tr');
			      tr.id = "print_tr_" + i;
			      tr.name = "print_tr_" + i;
			      
			      tr.appendChild(td1);
			      tr.appendChild(td2);
			      tr.appendChild(td3);
			      tr.appendChild(td4);
			      $('showPrintTable').appendChild(tr);
	        }
	      }
	    }
	    $('companyCount').innerHTML = reciveDeptDescArray.length;
	    $('docAllCount').innerHTML = countAll;
	    doPrintControl();
	  }
	  else{
	    printTable(data.data3);
	    doPrintControl();
	  }
  }
}

//确定事件，验证成功后提交到后台
function doSubmit(){
  if(checkForm()){
    $("form1").submit();
  }
}

//确认页面发送事件
function doSubmit2(){
  $("form1").action = "<%=contextPath%>/yh/subsys/jtgwjh/docSend/act/YHDocSendAct/addDocInfo.act?sendFlag=1";
  $("form1").submit();
}

//验证
function checkForm(){
	return true;
}

//打印控制按钮事件
function doPrintControl(){
  $('printControlBut').style.display = 'none';
  $('showPrintTableMain').style.display = '';
}

//打印控制列表实现
function printTable(data3){
  
  var reciveDept = $('reciveDept').value;
  var reciveDeptDesc = $('reciveDeptDesc').innerHTML;
  
  $('showPrintTable').innerHTML = '';
  if(reciveDept == ''){
    $('showPrintTableMain').style.display = 'none';
    $('printControlBut').style.display = ''
    return;
  }
  else if(!$('printControlBut').style.display == ''){
    $('showPrintTableMain').style.display = '';
  }
  

  var reciveDeptArray = reciveDept.split(',');
  var reciveDeptDescArray = reciveDeptDesc.split(',');
  for(var i = 0; i < reciveDeptArray.length ; i++){
    for(var j = 0; j < data3.length; j++){
      if(data3[j].reciveDept == reciveDeptDescArray[i]){
			  var td1 = $C('td');
			  td1.align = 'center';
			  td1.innerHTML = reciveDeptDescArray[i]
			                + '<input type="hidden" id="taskId_'+i+'" name="taskId_'+i+'" value="'+data3[j].seqId+'">';
			  
			  var td2 = $C('td');
			  td2.align = 'right';
			  td2.innerHTML = '<input type="text" id="print_count_'+i+'" name="print_count_'+i+'" size="5" style="text-align:right;" value="1" onchange="consider('+reciveDeptArray.length+')">';
			  
			  var td3 = $C('td');
			  td3.align = 'right';
			  td3.innerHTML = '<input type="text" id="print_no_start_'+i+'" name="print_no_start_'+i+'" size="5" style="text-align:right;" value="-">';
			  
			  var td4 = $C('td');
			  td4.align = 'right';
			  td4.innerHTML = '<input type="text" id="print_no_end_'+i+'" name="print_no_end_'+i+'" size="5" style="text-align:right;" value="-">';
			  
			  var tr = $C('tr');
			  tr.id = "print_tr_" + i;
			  tr.name = "print_tr_" + i;
			  
			  tr.appendChild(td1);
			  tr.appendChild(td2);
			  tr.appendChild(td3);
			  tr.appendChild(td4);
			  $('showPrintTable').appendChild(tr);
      }
    }
  }
  $('companyCount').innerHTML = reciveDeptArray.length;
  $('docAllCount').innerHTML = reciveDeptArray.length;
}

//计算总打印份数
function consider(length){
  var count = 0;
  for(var i = 0; i < length ; i++){
    count = count + parseInt($('print_count_'+i).value);
  }
  $('docAllCount').innerHTML = count;
}

//跳转到列表页面
function returnList(){
  location = '<%=contextPath %>/subsys/jtgwjh/sendDoc/manage.jsp';
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 发文登记</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/jtgwjh/docSend/act/YHDocSendAct/updateDocInfoSign.act?seqId=<%=seqId %>"  method="post" name="form1" id="form1" onsubmit="">
<table class="TableBlock" width="80%" align="center">
  <tr>
    <td align="left" width="120" class="TableContent">文件标题：</td>
    <td align="left" class="TableData" colspan="3"><div id="docTitle"></div> </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">密级：</td>
    <td align="left" class="TableData" width="180"><div id="securityLevel"></div> </td>
    <td align="left" width="120" class="TableContent">打印份数：</td>
    <td align="left" class="TableData" width="180"><div id="printCount"></div> </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">接收单位：</td>
    <td align="left" class="TableData" colspan="3"><div id="reciveDeptDesc"></div>
      <input type="hidden" id="reciveDept" name="reciveDept"> 
    </td>
  </tr>
</table>
<br>
<table class="TableBlock" width="80%" align="center">    
  <tr height="25">
    <td nowrap class="TableData">打印控制：</td>
    <td class="TableData" colspan="5" >
      <input type="button" value="打印控制" id="printControlBut" name ="printControlBut" onclick="doPrintControl();" class="BigButton" >
      <input type="hidden" id="isSign" name="isSign" value="1">
      <div id="showPrintTableMain" style="display:none;">
       <table class="TableList" >
         <thead>
          <tr class="TableHeader">
            <td align="center" nowrap style="background-color: rgb(247, 248, 189);padding-left: 0px;width:250px;">接收单位</td>
            <td align="center" nowrap style="background-color: rgb(247, 248, 189);padding-left: 0px;width:70px;">公文份数</td>
            <td align="center" nowrap style="background-color: rgb(247, 248, 189);padding-left: 0px;width:70px;">开始编号</td>
            <td align="center" nowrap style="background-color: rgb(247, 248, 189);padding-left: 0px;width:70px;">结束编号</td>
          </tr>
         </thead>
         <tbody id="showPrintTable"></tbody>
       </table>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;单位数：<span id="companyCount"></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;公文总分数：<span id="docAllCount"></span>
      </div>
    </td>
  </tr>
  
  <tr align="center" class="TableControl">
    <td colspan='6' nowrap>
      <input type="hidden" name="careContent" id="careContent" value="">
      <input type="button" value="返回" onclick="returnList()" class="BigButton">
      <input type="button" value="保存" onclick="doSubmit();" class="BigButton">
      <input type="button" value="盖章" onclick="toStamp(<%=seqId %>)" class="BigButton">
      <input type="button" value="发送" onclick="doSubmit2()" class="BigButton">
    </td>
  </tr>
</table>
</form>

</body>
</html>
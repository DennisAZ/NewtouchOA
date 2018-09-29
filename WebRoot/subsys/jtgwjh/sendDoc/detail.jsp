<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发文详细信息</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/sendDoc/js/sendLogic.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/subsys/jtgwjh/sendDoc/js/logic.js"></script>
<script type="text/javascript">
function doInit(){
	var url = "<%=contextPath%>/yh/subsys/jtgwjh/docSend/act/YHDocSendAct/getDetail.act?seqId=<%=seqId%>";
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
		bindJson2Cntrl(rtJson.rtData);
		$('docType').innerHTML = renderDocType(data.docType);
    $('urgentType').innerHTML = renderUrgentType(data.urgentType);
    $('securityLevel').innerHTML = renderSecurityLevel(data.securityLevel);
    //if(data.careDate){
    //  $("careDate").innerHTML = data.careDate.substr(0,10);
    //}
    
    //附件列表
		if(data.attachmentId){
			$("returnAttId").value = data.attachmentId;
			$("returnAttName").value = data.attachmentName;
			var selfdefMenu = {
          office:["downFile","read"], 
          img:["downFile","play"],  
          music:["downFile","play"],  
          video:["downFile","play"], 
          others:["downFile"]
      }
			var attachmentIdArray =  (data.attachmentId).split(",");
			var attachmentNameArray =  (data.attachmentName).split("*");
			for(var i = 0 ; i < attachmentIdArray.length; i++){
			  var data2 =  data.data2;
			  for(var j = 0; j < data2.length; j++){
			    if(data2[j].fileId == attachmentIdArray[i]){
			      var checked = false;
			      if(data2[j].fileId == data.mainDocId){
			        checked = true;
			      }
					  attachMenuSelfUtilTableDetail("showAttTbody","jtgw",attachmentNameArray[i] ,attachmentIdArray[i], data2[j].fileSize, '','','',selfdefMenu,i,data2[j].reciveDeptDesc,checked);
			    }
			  }
			}
			$('showAttTable').style.display = '';
		}
		else{
			$('attr').innerHTML = "无文件";
		}
	  //打印控制列表
    if(data.isSign == 1){
      
      var data3 =  data.data3;
      var countAll = 0;
      var reciveDeptDesc = data.reciveDeptDesc.split(',');
      for(var i = 0; i < data.reciveDeptDesc.length; i++){
        for(var j = 0; j < data3.length; j++){
          if(data3[j].reciveDeptDesc == reciveDeptDesc[i]){
            var td1 = $C('td');
            td1.align = 'center';
            td1.innerHTML = data3[j].reciveDeptDesc;
            
            var td2 = $C('td');
            td2.align = 'right';
            td2.innerHTML = '<span id="print_count_'+i+'" name="print_count_'+i+'" >'+data3[j].printCount+'</span>';
            countAll = parseInt(countAll) + parseInt(data3[j].printCount);
            
            var td3 = $C('td');
            td3.align = 'right';
            td3.innerHTML = '<span id="print_no_start_'+i+'" name="print_no_start_'+i+'" >'+data3[j].printNoStart+'</span>';
            
            var td4 = $C('td');
            td4.align = 'right';
            td4.innerHTML = '<span id="print_no_end_'+i+'" name="print_no_end_'+i+'" >'+data3[j].printNoEnd+'</span>';
            
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
      $('companyCount').innerHTML = reciveDeptDesc.length;
      $('docAllCount').innerHTML = countAll;
      $('printControl').style.display = '';
    }
    
	  //盖章情况列表
    if(data.stampComplete == 1){
      var data4 =  data.data4;
      for(var i = 0; i < data4.length; i++){
        var td1 = $C('td');
        td1.align = 'center';
        td1.innerHTML = data4[i].userName;
        
        var td2 = $C('td');
        td2.align = 'center';
        if(data4[i].stampType == 0){
	        td2.innerHTML = '主办';
        }
        else{
          td2.innerHTML = '协办';
        }
        
        var td3 = $C('td');
        td3.align = 'center';
        if(data4[i].stampStatus == 0){
	        td3.innerHTML = '待盖章';
        }
        else{
          td3.innerHTML = '已盖章';
        }
        
        var td4 = $C('td');
        td4.align = 'center';
        td4.innerHTML = data4[i].stampTime;
        
        var tr = $C('tr');
        tr.id = "print_tr_" + i;
        tr.name = "print_tr_" + i;
        
        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        tr.appendChild(td4);
        $('showStampTable').appendChild(tr);
      }
      $('stampControl').style.display = '';
    }
    else if(data.isStamp == 2){
      $('waitStamp').innerHTML = '待盖章';
      $('stampControl').style.display = '';
    }
    
	  //发送情况列表
    if(data.sendDatetime){
      var data3 =  data.data3;
      for(var i = 0; i < data3.length; i++){
        var td1 = $C('td');
        td1.align = 'center';
        td1.innerHTML = data3[i].reciveDeptDesc;
        
        var td2 = $C('td');
        td2.align = 'center';
        //状态：-2本地失败-1发送失败，0-草拟，1-待发，2-发送中，3-上传完毕，4-已接收
        switch(data3[i].status){
          case  '-3': td2.innerHTML = '接收失败';break;
          case  '-2': td2.innerHTML = '本地失败';break;
          case  '-1': td2.innerHTML = '发送失败';break;
          case   '0': td2.innerHTML = '草拟';   break;
          case   '1': td2.innerHTML = '待发';   break;
          case   '2': td2.innerHTML = '发送中'; break;
          case   '3': td2.innerHTML = '上传完毕';break;
          case   '4': td2.innerHTML = '对方已接收';  break;
          case   '5': td2.innerHTML = '已重发';  break;
          case   '6': td2.innerHTML = '对方已签收';  break;
        }
        
        var td3 = $C('td');
        td3.align = 'center';
        td3.innerHTML = data3[i].processTime;
        
        var td4 = $C('td');
        td4.align = 'center';
        if(data3[i].status == '-2' || data3[i].status == '-1' || data3[i].status == '-3'){
          td4.innerHTML = '<a href="javascript:void(0);" onclick="reSend(\''+data3[i].guid+'\',\''+data3[i].reciveDept+'\')">重新发送</a>';
        }
        else{
	        td4.innerHTML = '';
        }
        
        var tr = $C('tr');
        tr.id = "print_tr_" + i;
        tr.name = "print_tr_" + i;
        
        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        tr.appendChild(td4);
        $('showSendTable').appendChild(tr);
      }
      $('sendControl').style.display = '';
    }
	}else{
		alert(rtJson.rtMsrg);
	}
}

function reSend(guid, toId){
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/docSend/act/YHDocSendAct/sendDocInfoTasks.act";
  var rtJson = getJsonRs(url, "guid="+guid+"&toId="+toId);
  if(rtJson.rtState == "0"){
    alert("已重新加入发送队列中！");
  }
  else{
    alert(rtJson.rtMsrg);
  }
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3"> 发文详细信息</span><br>
    </td>
  </tr>
</table>

<br>
<table class="TableBlock" width="90%" align="center">
  <tr>
    <td align="left" width="120" class="TableContent">文件标题：</td>
    <td align="left" class="TableData" width="180" colspan="3"><div id="docTitle"></div> </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">文号：</td>
    <td align="left" class="TableData" width="180" colspan="3"><div id="docNo"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">文件类型：</td>
    <td align="left" class="TableData" width="180"><div id="docType"></div> </td>
    <td align="left" width="120" class="TableContent">紧急程度：</td>
    <td align="left" class="TableData" width="180"><div id="urgentType"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">密级：</td>
    <td align="left" class="TableData" width="180"><div id="securityLevel"></div></td>
    <td align="left" width="120" class="TableContent">打印份数：</td>
    <td align="left" class="TableData" width="180"><span id="printCount"></span>份</td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">备注：</td>
    <td align="left" class="TableData" colspan="3"><div id="remark"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">接收单位：</td>
    <td align="left" class="TableData" colspan="3"><div id="reciveDeptDesc"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">文件：</td>
    <td align="left" class="TableData" colspan="3">
			<input type = "hidden" id="returnAttId" name="returnAttId"></input>
			<input type = "hidden" id="returnAttName" name="returnAttName"></input>
			<span id="attr"></span>
      <table class="TableList" id="showAttTable" style="display:none;">
        <tr class="TableHeader" >
          <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:250px;">文件名</td>
          <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:50px;">大小</td>
          <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:250px;">发送范围</td>
          <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:40px;">正文</td>
        </tr>
        <tbody id="showAttTbody"></tbody>
      </table>
    </td>
  </tr>
  <tr id="printControl" style="display:none;">
    <td align="left" width="120" class="TableContent">打印控制：</td>
    <td align="left" class="TableData" colspan="3">
      <table class="TableList" >
      <thead>
        <tr class="TableHeader">
          <td align="center" style="background-color: rgb(242, 242, 242);padding-left: 0px;width:250px;">接收单位</td>
          <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:70px;">公文份数</td>
          <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:70px;">开始编号</td>
          <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:70px;">结束编号</td>
        </tr>
      </thead>
      <tbody id="showPrintTable"></tbody>
      </table>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;单位数：<span id="companyCount"></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;公文总分数：<span id="docAllCount"></span>
    </td>
  </tr>
  
  <tr id="stampControl" style="display:none;">
    <td align="left" width="120" class="TableContent">盖章情况：</td>
    <td align="left" class="TableData" colspan="3" id="waitStamp">
      <table class="TableList" >
      <thead>
        <tr class="TableHeader">
          <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:250px;">盖章人员</td>
          <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:70px;">盖章类型</td>
          <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:70px;">盖章状态</td>
          <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:150px;">盖章时间</td>
        </tr>
      </thead>
      <tbody id="showStampTable"></tbody>
      </table>
    </td>
  </tr>
  
    <tr id="sendControl" style="display:none;">
    <td align="left" width="120" class="TableContent">发送情况：</td>
    <td align="left" class="TableData" colspan="3">
      <table class="TableList" >
      <thead>
        <tr class="TableHeader">
          <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:250px;">接收单位</td>
          <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:70px;">状态</td>
          <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:150px;">接收时间</td>
          <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:70px;">操作</td>
        </tr>
      </thead>
      <tbody id="showSendTable"></tbody>
      </table>
    </td>
  </tr>
  
  <tr>
    <td align="left" width="120" class="TableContent">创建人：</td>
    <td align="left" class="TableData" width="180"><div id="createUserName"></div></td>
    <td align="left" width="120" class="TableContent">创建人部门：</td>
    <td align="left" class="TableData" width="180"><div id="createDeptName"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">创建时间：</td>
    <td align="left" class="TableData" width="180"><div id="createDatetime"></div></td>
    <td align="left" width="120" class="TableContent">发文发送时间：</td>
    <td align="left" class="TableData" width="180"><div id="sendDatetime"></div></td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan="4">
      <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
    </td>
  </tr>
</table>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/smsbox.css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/sms/js/divwin.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/sms/js/smsutil.js"></script>
<title>提醒提醒页面</title>
<script type="text/javascript">
var ids = new Array(); 
/**
 * 全部标记为读
 */
function readAllRemind(){
  var idstr = "";
  for(var i = 0 ; i < ids.length ; i ++ ){
    if(idstr){
      idstr += ",";
    }
    idstr += ids[i];
  }
  markRead(idstr);
  ids.clear();
  closeRemindWin();
}
/**
 * 查看所有详情
 */
function detailAll(detailSum){
  top.dispParts('<%=contextPath %>/yh/core/funcs/sms/act/YHSmsTestAct/acceptedSms.act?pageNo=0&pageSize=20');
  top.hideShortMsrg();
}
/**
 * 回复
 */
function SmsBack(seqId,fromId){
  var from = fromId;
  var URL = "<%=contextPath %>/core/funcs/sms/smsback.jsp?seqId=" + seqId + "&fromId=" + from;
  openDialog(URL,'700', '280');
}
/**
 * 查看详情
 */
function Details(url,id,type){
  gotoURLSms(url,id,type);
  SmsRead(id);
}
/**
 * 标记为读功能
 * 包含标记和页面信息隐藏
 */
function SmsRead(seqId){
  markRead(seqId);
  ids.pop(seqId);
  RemoveSms(seqId);
  if(ids.length == 0){
    closeRemindWin();
  }
}
/**
 * 将以读信息移出页面
 */
function RemoveSms(seqId){
  var module = $("module_" + seqId);
  if (module) {
    module.parentNode.removeChild(module);
  }
  if(!module.parentNode.firstChild){
    closeRemindWin();
  }
}
/**
 * 标记为读函数
 */
function markRead(seqId){
  var url = "<%=contextPath%>/yh/core/funcs/sms/act/YHSmsAct/resetFlag.act";
  var rtJson = getJsonRs(url,"seqId=" + seqId);
}
/**
 * 关闭窗口
 */
function closeRemindWin(){
  parent.hideShortMsrg();
}
function doInit(){
 // var d1 = new Date();
  var url = "<%=contextPath%>/yh/core/funcs/sms/act/YHSmsAct/remindFlag.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    bindJson2Cntrl(rtJson.rtData);
    var promStr = "";
    if(rtJson.rtData.length < 1){
      
      $('smsTable').style.display = "none";
      $('acceptSms').style.display = "";
      return;
    }
    for(var i = 0; i < rtJson.rtData.length; i++){
      var obj = rtJson.rtData[i];
      promStr += toPromptStr(obj).join('');   
     // var d3 = new Date();
     // $('sms_body').insert(promStr,'bottom');
    //  var d4 = new Date();
      //alert(d4.getTime() - d3.getTime());
      ids.push(obj.seqId);
    }
    $('sms_body').insert(promStr,'content');
    //var d2 = new Date();
    //alert(d2.getTime() - d1.getTime());
    //绑定所有的
    for(var i = 0; i < rtJson.rtData.length; i++){
      var obj = rtJson.rtData[i];
      bindDesc([{cntrlId:"fromId_" + obj.seqId, dsDef:"PERSON,SEQ_ID,USER_NAME"}]); 
    }

  }
  createOpDiv('sms_op');
}


function createOpDiv(dom){
  var sum="";
  var pr = $(dom);
  var btnContainer = document.createElement("div");
  btnContainer.className = 'module_body';
  btnContainer.width = '100%';
  pr.appendChild(btnContainer);
  var inputRead = document.createElement("input"); 
  inputRead.setAttribute("type", "button");
  inputRead.id = sum;
  inputRead.setAttribute("size", "7");
  inputRead.setAttribute("name", "read" );
  inputRead.setAttribute("value", "全部已阅");
  inputRead.setAttribute("title", "标记以上所有提醒为已阅读，点击后再有新提醒提醒将不再显示以上提醒");
  inputRead.className="SmallInput";
  inputRead.onclick = function() {
	  readAllRemind();
	}
  btnContainer.appendChild(inputRead);

	var inputDetail = document.createElement("input"); 
	inputDetail.setAttribute("type", "button");
	inputDetail.id = sum;
	inputDetail.setAttribute("size", "7");
	inputDetail.setAttribute("name", "detail_" );
	inputDetail.setAttribute("value", "全部详情");
	inputDetail.setAttribute("title", "查看以上所有提醒详情");
	inputDetail.className="SmallInput";
	inputDetail.onclick = function() {
    var detailSum = this.id;
	  detailAll(detailSum);
	}
	btnContainer.appendChild(inputDetail);

 	var inputClose = document.createElement("input"); 
 	inputClose.setAttribute("type", "button");
 	inputClose.id = sum;
 	inputClose.setAttribute("size", "7");
 	inputClose.setAttribute("name", "close" );
 	inputClose.setAttribute("value", "关闭");
 	inputClose.setAttribute("title", "关闭");
 	inputClose.className="SmallInput";
 	inputClose.onclick = function() {
 	 closeRemindWin();
	}
 	btnContainer.appendChild(inputClose);
}
</script>
</head>
<body onload="doInit();" style="width: 400px;height:234px;overflow: hidden;">
<table width="400px" height="230px" id="smsTable">
  <tr> 
    <td><div id="sms_body" style="width:400px;height:210px;overflow: auto"></div></td>
  </tr>
  <tr>
    <td align="center"><div id="sms_op"></div></td>
  </tr>
</table>
<div id="acceptSms" style="display:none;">
  <table class="MessageBox" align="center" width="300">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无新提醒</div>
    </td>
  </tr>
</table>
</div>
</body>
</html>
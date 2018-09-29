<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
</head>
<body onload="">
<div id="module_69_body" class="module_body" style="" >
     <div id="module_69_top" class="moduleTypeLink"><a href="javascript:getWork(1);" id="today">待办工作</a> | <a href="javascript:getSign();" id="ground">会签工作</a> | <a href="javascript:getFocusWork();" id="task">我的关注</a></div>

    <div id="module_69_ul" class="module_div" style="overflow:hidden;position:relative;"  >
    <ul type="disc" id="workList" style="position:relative;">
    </ul>
</div>
  </div>
  <script type="text/javascript" >
//日程
window.formView = function(runId , flowId) {
  var url = contextPath + "/core/funcs/workflow/flowrun/list/print/index.jsp?runId="+runId+"&flowId="+flowId;
  window.open(url ,"","status=0,toolbar=no,menubar=no,width="+(screen.availWidth-12)+",height="+(screen.availHeight-38)+",location=no,scrollbars=yes,resizable=yes,left=0,top=0");
}
window.getWork = function (index){
  var actionUrl = contextPath + "/yh/core/funcs/workflow/act/YHMyWorkAct";
  var URL = actionUrl + "/getMyWork.act" ;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData;
  $('workList').update("");
  
  if (prcs.length > 0 ) {
    for(var i = 0; i < prcs.length ;i++){
      var prc = prcs[i];
      var str = getLi(prc);
      $('workList').insert(str);
    }
  } else {
    $('workList').update("<li>暂无待办工作</li>");
  }
  doInitPlan(prcs.length);
}
window.getLi = function (prc) {
  var status = "";
  var prcsFlag = prc.prcsFlag; 
  if (prcsFlag ==  '1') {
    status = "<img src='"+imgPath+"/email_close.gif' width=16 height=16 alt='未接收' align='absmiddle'>";
  } else if ( prcsFlag == '2' ) {
    status = "<img src='"+imgPath+"/email_open.gif' width=16 height=16 alt='已接收' align='absmiddle'>";
  } else {
    status = "<img src='"+imgPath+"/flow_next.gif' width=16 height=16 alt='已办结' align='absmiddle'>";
  }
  var tmp = prc.runName+' '+prc.prcsName;
  var moduleBody = '<li style="line-height:20px;" class=auto title="'+tmp+'"><a href="javascript:top.dispParts(\''+contextPath+'/core/funcs/workflow/flowrun/list/index.jsp?flowId=' + prc.flowId +'\')">[' + prc.flowName + ']</a>' + status;
  if (prcsFlag != '3') {
    moduleBody += '<a href="javascript:top.dispParts(\''+contextPath+'/core/funcs/workflow/flowrun/list/inputform/index.jsp?runId='+prc.runId+'&flowId='+prc.flowId+'&prcsId='+prc.prcsId+'&flowPrcs='+prc.flowPrcs+'\')">'+tmp+'</a></li>';
  } else {
    moduleBody += '<a href="javascript:formView('+ prc.runId +',' + prc.flowId +')">'+ tmp +'</a></li>';
  }
  return moduleBody;
}

window.getSign = function () {
  var actionUrl = contextPath + "/yh/core/funcs/workflow/act/YHMyWorkAct";
  var URL = actionUrl + "/getSign.act" ;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData;
  $('workList').update("");

  var newStr = "";
  if (prcs.length > 0 ) {
    for(var i = 0; i < prcs.length ;i++){
      var prc = prcs[i];
      var str = getSignLi(prc);
      $('workList').insert(str);
    }
  } else {
    $('workList').update("<li>暂无会签流程</li>");
  }
  doInitPlan(prcs.length);
}
window.getSignLi = function (prc){
  var status = "";
  var prcsFlag = prc.prcsFlag;
  if (prcsFlag ==  '1') {
    status = "<img src='"+imgPath+"/email_close.gif' width=16 height=16 alt='未接收' align='absmiddle'>";
  } else if ( prcsFlag == '2' ) {
    status = "<img src='"+imgPath+"/email_open.gif' width=16 height=16 alt='已接收' align='absmiddle'>";
  } else {
    status = "<img src='"+imgPath+"/flow_next.gif' width=16 height=16 alt='已办结' align='absmiddle'>";
  }
  var tmp = prc.flowName+' - '+prc.runName;
  var moduleBody ='<li style="line-height:20px;" title="'+tmp+'" class=auto>' + status;
  if (prcsFlag != '3' && prcsFlag != '4') {
    moduleBody += '<a href="javascript:top.dispParts(\''+contextPath+'/core/funcs/workflow/flowrun/list/inputform/index.jsp?runId='+prc.runId+'&flowId='+prc.flowId+'&prcsId='+prc.prcsId+'&flowPrcs='+prc.flowPrcs+'\')">'+tmp+'</a></li>';
  } else {
    moduleBody += '<a href="javascript:formView('+ prc.runId +',' + prc.flowId +')">'+tmp+'</a></li>';
  }
  return moduleBody;
}
window.getFocusWork = function () {
  var actionUrl = contextPath + "/yh/core/funcs/workflow/act/YHMyWorkAct";
  var URL = actionUrl + "/getFocusWork.act" ;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData;
  $('workList').update("");
  
  
  if (prcs.length > 0 ) {
    for(var i = 0; i < prcs.length ;i++){
      var prc = prcs[i];
      var str = getFocusLi(prc);
      $('workList').insert(str);
    }
  } else {
    $('workList').update("<li>暂无关注工作</li>");
  }
  doInitPlan(prcs.length);
}
window.getFocusLi = function (prc) {
  var moduleBody = '<li><a href="javascript:formView('+ prc.runId +',' + prc.flowId +')">【'+prc.flowName+'】 - '+prc.runName+'</a></li>';
  return moduleBody;
}

window.doInitPlan = function (records){
  var lines = ${param.lines};
  var scroll = ${param.scroll};
  //设置
  $('module_69_ul').setStyle({height: 20 * lines + 'px'});
  
  cfgModule({
    records: records,
    lines: lines,
    name: '待办工作',
    showPage:  function(i){
      $('workList').setStyle({'top': (- i * lines * 20) + 'px'});
    }
  });
}

getWork(1);
var scroll = ${param.scroll};
if (scroll){
  Marquee('workList',80,1);
}
</script>
  </body>
  </html>
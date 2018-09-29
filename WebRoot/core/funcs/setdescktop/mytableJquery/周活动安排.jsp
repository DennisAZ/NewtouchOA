<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
</head>
<body>
<div class="module_body">
<div class="moduleTypeLink">
<a href="javascript:getActive(1);" id="todayActive">今日活动安排</a> | <a href="javascript:getActive(2);" id="weekActive">本周活动安排</a> 
</div>
<div id="active" class="" style="position:relative;overflow:hidden;width:100%;">

	<div id="active_ul" class="module_div" style="width:100%;">
	
		<ul id="active_li" style="float:left;text-align:left;width:100%;" type="disc">
		</ul>

	  <div style="clear:both;"></div>
	</div>
</div>
</div>
<script type="text/javascript" >
//日程
window.getActive = function (index){
  var URL = "<%=contextPath%>/yh/subsys/oa/active/act/YHActiveAct/selectActive.act?index="+index;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData;
  //alert(rsText);
  if(prcs.length>0){
    var curDate = getCurrDateStr();
    var activeStr = "";
    for(var i = 0; i<prcs.length;i++){
      var prc = prcs[i];
      var seqId = prc.seqId;
      var activeTime = prc.activeTime;
      var activeContent = prc.activeContent;
      if(index=='1'){
        activeStr = activeStr+ "<li>&nbsp;"+activeTime.substr(11,5)+"&nbsp;<a href='javascript:activeNote("+seqId+")'><span style='color:#0000FF'>" + activeContent+"</span>";
      }
      if(index=='2'){
        if(curDate==activeTime.substr(0,10)){
          activeStr = activeStr+ "<li>&nbsp;"+activeTime.substr(11,5)+"&nbsp;<a href='javascript:activeNote("+seqId+")'><span style='color:#0000FF'>" + activeContent+"</span>";
        }else{
          activeStr = activeStr+ "<li>&nbsp;"+activeTime.substr(0,16)+"&nbsp;<a href='javascript:activeNote("+seqId+")'><span style='color:#0000FF'>" + activeContent+"</span>";
        }
      }
    }
    $("active_li").update(activeStr);
  }else{
    if(index=='1'){
      $("active_li").update("<li>今日暂无活动安排<li>");
    }
    if(index=='2'){
      $("active_li").update("<li>本周暂无活动安排<li>");
    }
  }

  if(index=='1'){
    $("todayActive").style.color="red";
    $("weekActive").style.color="";
  }
  if(index=='2'){
    $("todayActive").style.color="";
    $("weekActive").style.color="red";
  }
  doInitPlan(prcs.length);
}
window.getCurrDateStr = function (){
  var currDate = new Date();
  var currDay = currDate.getDate();  
  var currMonth = currDate.getMonth();
  var currYear = currDate.getFullYear(); 
  if(currMonth<10){
    currMonth = "0"+currMonth;
  }
  if(currDay<10){
    currDay = "0"+currDay;
  }
  return currYear+"-"+currMonth+"-"+currDay;
}
window.activeNote = function (seqId){
  var URL = "<%=contextPath%>/core/funcs/active/activenote.jsp?seqId="+seqId;
  window.open(URL,"my_note","height=200,width=200,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=600,top=300,resizable=yes");
}

window.doInitPlan = function (records){
  var lines = <%=request.getParameter("lines")%>;
  //设置
  $('active').setStyle({height:20 * lines + 'px'});
  $('active_ul').setStyle({position: 'relative'});
    
  cfgModule({
      records: records,
      lines: lines,
      name: '周活动安排',
      showPage:  function(i){
        $('active_ul').setStyle({'top': (- i * lines * 20) + 'px'});
      }
   });
}
var scroll = <%=request.getParameter("scroll")%>;
if (scroll){
  Marquee('active_ul',80,1);
}
getActive(1);
</script>
  </body>
  </html>
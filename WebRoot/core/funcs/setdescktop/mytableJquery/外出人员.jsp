<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
</head>
<body >
  <div id="out" class="" style="overflow:hidden;position:relative;width:100%;">
	<div id="out_ul" class="module_div" style="width:100%;">
	<ul id="out_li" style="text-align:left;width:90%;" type="disc">
		</ul>
	</div>
</div>
  </body >
  <script type="text/javascript" >
//外出
window.getOut = function (){
  var URL = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendOutAct/selectOutToDisk.act";
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData;
  //alert(rsText);
  if(prcs.length>0){
     var outStr = "<legend align=left><b>外出人员</b></legend> ";
     for(var i = 0; i<prcs.length;i++){
       var prc = prcs[i];
       var seqId = prc.seqId;
       var outTime1 = prc.outTime1;
       var outTime2 = prc.outTime2;
       var reason = prc.reason;
       var userId = prc.userId;
       var outType = prc.outType;
       var userName = prc.userName;
       outStr = outStr + "<u title='" + outTime1 + " 至 " + outTime2 + " 原因: " + outType + "' style='cursor:pointer'>" + userName + "</u>&nbsp;";
    }
     outStr = outStr + "";
     $("out_li").update(outStr);
   }else{
     $("out_li").update("<li>无外出人员</li>");
   }
  var index = 0;
  if(prcs.length>0){
    index = ((prcs.length-1)/3) + 1;
    doInitOut(index);
  }
}

window.doInitOut = function (records){
  var lines = <%=request.getParameter("lines")%>;
  //设置
  $('out').setStyle({height: 20 * lines + 'px'});
  $('out_ul').setStyle({position: 'relative'});
    
  cfgModule({
      records: records,
      lines: lines,
      name: '外出人员',
      showPage:  function(i){
        $('out_ul').setStyle({'top': (- i * lines * 20) + 'px'});
      }
   });
 // }else {
    
  //}
}
getOut();
var scroll = <%=request.getParameter("scroll")%>;
if (scroll){
  Marquee('out_ul',80,1);
}

</script>
  </html>
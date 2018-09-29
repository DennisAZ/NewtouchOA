<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
</head>
<body onload="getTodayCalendar();">
    <div id="birthday" class="" style="overflow:hidden;position:relative;width:100%;">
		<div id="birthday_ul" class="module_div" style="width:100%;">
		<ul id="birthday_li" style="float:left;text-align:left;width:90%;" type="disc">
		</ul>
	</div>
</div>
  </body >
  <script type="text/javascript" >
//生日
window.getBirthday = function (){
  var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/selectBirthdayToDisk.act";
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData;
  if(prcs.length>0){
     var birthdayStr = "近期生日： ";
    for(var i = 0; i<prcs.length;i++){
       var prc = prcs[i];
       var seqId = prc.seqId;
       var userName = prc.userName;
       var birthday = prc.birthday;
       birthdayStr = birthdayStr + "<img src='<%=imgPath%>/cake.png' align='absMiddle'>" +userName+"("+birthday.substr(5,5)+"),";
    }
    if(prcs.length>0){
      birthdayStr = birthdayStr.substr(0,birthdayStr.length-1);
    }
    $("birthday_li").innerHTML = birthdayStr;
   }else{
     document.getElementById("birthday_li").innerHTML = "近期无生日";
   }
  var index = 0;
  if(prcs.length>0){
    index = ((prcs.length-1)/3) + 1;
  }
  doInitBirthday(index);
}

window.doInitBirthday = function (records){
  var lines = <%=request.getParameter("lines")%>;
  var scroll = <%=request.getParameter("scroll")%>;
  //设置
  $('birthday').setStyle({height: 20 * lines + 'px'});
  $('birthday_ul').setStyle({position: 'relative'});
    
  cfgModule({
    records: records,
    lines: lines,
    name: '日程安排',
    showPage: function(i){
      $('birthday_ul').setStyle({'top': (- i * lines * 20) + 'px'});
    }
  });
    
  if (scroll){
    Marquee('birthday_ul',80,1);
  }
}
getBirthday();
</script>
  </html>
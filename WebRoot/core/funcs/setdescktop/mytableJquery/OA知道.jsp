<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
</head>
<body>
<div id="oaknow_out">
<div id="oaknow" class="" style="overflow:hidden;position:relative;width:100%;">
	<div id="oaknow_ul" class="module_div" style="width:100%;">
		<ol id="oaknow_ul1" style="float:left;text-align:left;width:100%;">
		</ol>
	  <div style="clear:both;"></div>
	</div>
</div>
	<div style="background-color:white;position:relative;bottom:0px;" id="seemore"></div>
	</div>
	<script type="text/javascript">
  
	window.doInitOAKnowDesk = function (){          
       var rtJson = getJsonRs("<%=contextPath%>/yh/core/oaknow/act/YHOAKnowAct/ajaxOaDesk.act");  
       var temp = "";
       var asks = rtJson["rtData"];        
       var records = Math.round(rtJson.rtData.length);
       var lines = <%= request.getParameter("lines")%>;
       var scroll = <%= request.getParameter("scroll")%>;
       if(asks.length >0){
         for(var i=0; i< asks.length; i++){
           temp += "<li>";
           temp += "[<a href=javascript:top.dispParts('<%=contextPath%>/yh/core/oaknow/act/YHOAKnowTypeAct/findType.act?typeId=";
           temp += asks[i]["typeId"];
           temp += "&parentId=" + asks[i]["pid"];
           temp += "&showFlag=CONTENT')>";
           temp += asks[i]["categoryName"];
           temp += "</a>]&nbsp;&nbsp;";
           temp +="<a href=javascript:top.dispParts('<%=contextPath%>/yh/core/oaknow/act/YHOAAskReferenceAct/findAskRef.act?askId=";
           temp += asks[i]["seqId"];
           temp += "&showFlag=CONTENT')>";
           temp += asks[i]["ask"];
           temp += "</a>&nbsp;&nbsp;";
           temp += "(" + asks[i]["userName"] + "&nbsp;&nbsp;" + asks[i]["createDateStr"] +")";
           temp += "-";
           if(asks[i]["status"]=='1'){
             temp += "<font color='green'>已解决</font>";
           }else{
             temp += "<font color='red'>未解决</font>";
            }
         }
         $("seemore").innerHTML="<a href=javascript:top.dispParts('<%=contextPath%>/yh/core/oaknow/act/YHOAKnowAct/OAKnowIndex.act')>查看更多...</a>";
       }else{
          temp +="暂无数据...";
       }       
       $("oaknow_ul1").innerHTML = temp;
       $('oaknow').setStyle({height: 20 * lines + 'px'});
       $('oaknow_out').setStyle({height: 20 * lines +14 + 'px'});
       $('oaknow_ul').setStyle({position: 'relative'});     
       cfgModule({
         records: records,
         lines: lines,
         name: 'OA知道',
         showPage:  function(i){
           $('oaknow_ul').setStyle({'top': (- i * lines * 20) + 'px'});
         }
       });

       if (scroll){
          Marquee('oaknow_ul',80,1);
        }
     }

     doInitOAKnowDesk();
  </script>
  </body>
  </html>
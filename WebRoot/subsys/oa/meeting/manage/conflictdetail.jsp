<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>预约冲突信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/meetingmanagelogic.js"></script>
<script> 
var pageMgr = null;
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingAct/getMeetingConflict.act?seqId=${param.seqId}";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
   	var date = rtJson.rtData;
    if(date.length > 0){
      var table=new Element('table',{ "width":"90%","class":"TableList","align":"center"});
   	  var strTable="<tbody id='tbody'><tr class='TableHeader' align='center' style='font-size:10pt'>"
     		    + "<td nowrap width='30%' align='center'>会议名称</td>"
     		    + "<td nowrap width='30%' align='center'>开始时间</td>"				
     		    + "<td nowrap width='30%' align='center'>结束时间</td>"				
     	        + "</tr><tbody>";	
   	  table.update(strTable);
   	  $('listContainer').update(table);
   	  for(var i = 0; i < date.length; i++){
        var prcs = date[i];
       	var mroomId = prcs.seqId;
       	var mName = prcs.mName;
       	var mStart = prcs.mStart;
       	var mEnd = prcs.mEnd;
       
       	var className = (i % 2 == 0) ? "TableLine1" : "TableLine2";  										
       	var tr=new Element('tr',{'width':'90%','class': className ,'font-size':'10pt'});		
       	table.firstChild.appendChild(tr);	
       	var str = "<td align='center'>"
                +"<a href="
                + "javascript:meetingDetailFunc('" + mroomId + "');"
                + "> " + mName + "</a>&nbsp</td><td align='center'>"					
                + mStart.substr(0, mStart.length - 2) + "</td><td align='center'>"					
                + mEnd.substr(0, mEnd.length - 2) + "</td>";
                tr.update(str);
      }
    }else{
   	  WarningMsrg('无会议冲突', 'msrg');
    }
  }else{
    alert(rtJson.rtMsrg); 
  }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/meeting.gif" width="22" height="18"><span class="big3">&nbsp;与下列会议冲突</span>
   </td>
 </tr>
</table>
<br>
<div id="listContainer">
</div>
<br>
<center><input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口"></center>
<div id="msrg">
</div>
</body>
</html>
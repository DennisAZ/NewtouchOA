<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId=request.getParameter("seqId");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建员工福利信息</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var seqId=<%=seqId%>;
function doInit(){
  getVMeet(seqId);
	
}

function getVMeet(seqId){

    var url = contextPath + "/yh/subsys/oa/vmeet/act/YHVMeetAct/getVMeetByIdAct.act";
     var rtJsons = getJsonRs(url,"seqId="+seqId);
     if (rtJsons.rtState == "0") {
       var data=rtJsons.rtData;
 
        if(data.pass=="1"){
            window.location.href="zlchat/zlchat.jsp?seqId="+seqId+"&userName="+data.userName+"&roomId="+data.roomId+"&role="+data.role; 
        } else{
          $("showNoData").style.display="block";
        }
    	
     } else {
       alert(rtJsons.rtMsrg); 
     }
}


</script>

<body onLoad="doInit();">

<div align="center" style="display:none" id="showNoData">
  <table class="MessageBox" align="center" width="240">
  <tr>
  <td class="msg info"><div class='content' style='font-size:12pt'>会议信息丢失</div></td>
  </tr>
  </table>
           
</div>
<div align="center">
 <input type="button" class="BigButton" value="返回" onclick="history.go(-1)">
</div>
</body>



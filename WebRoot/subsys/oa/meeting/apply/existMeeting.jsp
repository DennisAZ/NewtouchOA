<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String message = request.getParameter("message");
	String meetingIdStr = request.getParameter("meetingIdStr");
	String seqIdStr = request.getParameter("returnSeqId");
	if(YHUtility.isNullorEmpty(message)){
		message = "";
	}
	if(YHUtility.isNullorEmpty(meetingIdStr)){
		meetingIdStr = "0";
	}
	int seqId = 0;
	if(!YHUtility.isNullorEmpty(seqIdStr)){
		seqId = Integer.parseInt(seqIdStr.trim());
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">

function detailFunc(){
	var url = contextPath + "/subsys/oa/meeting/manage/conflictdetail.jsp?seqId=<%=meetingIdStr%>";
	newWindow(url,450,350);
}

/**
 * 打开新窗口  newWindow(URL,'740', '540');
 * @param url
 * @param width
 * @param height
 * @return
 */
function newWindow(url,width,height){
	var locX=(screen.width-width)/2;
	var locY=(screen.height-height)/2;
	window.open(url, "meeting", 
			"height=540,width=740,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
			+ locY + ", left=" + locX + ", resizable=yes");
}

</script>
</head>
<body>
<div id="doSubmitDiv" style="display: ">
<table class="MessageBox" align="center" width="350">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt"><%=message %></div>
    </td>
  </tr>
</table>
<br>
 <center>
   <input type="button" class="BigButton" value="详情" onClick="detailFunc();">&nbsp;
   <%
   		if(seqId != 0){
   %>
   		<input type="button" class="BigButton" value="返回" onclick="window.location.href='<%=contextPath %>/subsys/oa/meeting/apply/modifyMeeting.jsp?seqId=<%=seqId %>';">
   <%
   		}else{
   %>
   		<input type="button" class="BigButton" value="返回" onclick="window.location.href='<%=contextPath %>/subsys/oa/meeting/apply/newMeeting.jsp';">
   <%
   		}
   %>
 </center>	    
</div>
</body>
</html>
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
<link href="<%=cssPath%>/cmp/swfupload.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/src/cmp/attachMenu.js"></script>
<script type="text/javascript" src="js/new.js"></script>
<script type="text/javascript" src="js/notifyLogic.js"></script>
<script type="text/javascript" src="js/util.js"></script>
<script type="text/javascript">
function doInit(){
	var url = "<%=contextPath%>/yh/subsys/jtgwjh/notifyManage/act/YHJhNotifyInfoAct/getDetail.act?seqId=<%=seqId%>";
	var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
        var data = rtJson.rtData;
        bindJson2Cntrl(data);
        $("reciveDept").value = data.reciveDept;
        $("reciveDeptDesc").innerHTML = data.receiveDeptName;
        $("content").innerHTML = data.content;
        if(data.attachmentId){
      	  $("attachmentId").value = data.attachmentId;
            $("attachmentName").value = data.attachmentName;
            var selfdefMenu = {
                office:["downFile","read"], 
                img:["downFile","dump","play"],  
                music:["downFile","play"],  
                video:["downFile","play"], 
                others:["downFile"]
            }
            attachMenuSelfUtil("attr","notifyRecive",$('attachmentName').value ,$('attachmentId').value, '','','<%=seqId%>',selfdefMenu);
          }else{
            $('attr').innerHTML = "无附件";
          }
      }
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3">公告详细信息</span><br>
    </td>
  </tr>
</table>

<br>
<table class="TableBlock" width="90%" align="center">
  <tr>
    <td align="left" width="120" class="TableContent">公告标题：</td>
    <td align="left" class="TableData" colspan="3"><div id="title"></div> </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">接收单位：</td>
    <td align="left" class="TableData" colspan="3">
    <input type = "hidden" id="reciveDept" name="reciveDept"></input>
    <div id="reciveDeptDesc"></div></td>
  </tr>
   <tr>
    <td align="left" width="120" class="TableContent">内容：</td>
    <td align="left" class="TableData" colspan="3">
    <div id="content"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">附件：</td>
    <td align="left" class="TableData" colspan="3">
			<input type = "hidden" id="attachmentId" name="attachmentId"></input>
			<input type = "hidden" id="attachmentName" name="attachmentName"></input>
			<span id="attr"></span>
    </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">接收时间：</td>
    <td align="left" class="TableData" colspan="3"><div id="createDate"></div></td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan="4">
      <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
    
    		<input type = "hidden" id="moduel" name="moduel" value="notifyRecive"></input>
    </td>
  </tr>
</table>
</body>
</html>
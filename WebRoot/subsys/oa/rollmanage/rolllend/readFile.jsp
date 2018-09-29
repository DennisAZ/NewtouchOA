<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<%
	String seqId=request.getParameter("seqId");
	if(YHUtility.isNullorEmpty(seqId)){
		seqId = "0";
	}

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看文件</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/rollfilelogic.js"></script>
<script type="text/javascript">
var requestURL = "<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsFileAct";
function doInit(){
	var url = requestURL + "/getRmsFileDetailById.act?seqId=<%=seqId%>";
	var json = getJsonRs(url);
	//alert(rsText);
  if(json.rtState == "0"){
    var data = json.rtData;
    $('fileCode').innerHTML = data.fileCode;
    $('fileSubject').innerHTML = data.fileSubject;
    $('fileTitle').innerHTML = data.fileTitle;
    $('fileTitleo').innerHTML = data.fileTitleo;    
    $('sendUnit').innerHTML = data.sendUnit;    
    var sendDateStr = data.sendDate;
    if(sendDateStr){
    	$('sendDate').innerHTML = sendDateStr.substr(0, 10);
    }
    
    $('secret').innerHTML = getCodeName("RMS_SECRET",data.secret);
    
    $('urgency').innerHTML =  getCodeName("RMS_URGENCY",data.urgency);
    $('fileType').innerHTML = getCodeName("RMS_FILE_TYPE",data.fileType);
    $('fileKind').innerHTML = getCodeName("RMS_FILE_KIND",data.fileKind);

    $('filePage').innerHTML = data.filePage;
    $('printPage').innerHTML = data.printPage;
    $('remark').innerHTML = data.remark;

    
  }else{
    alert(rtJson.rtMsrg); 
  }
	

	
}






</script>
</head>
<body onload="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="middle"><span class="big3"> 查看文件</span>
    </td>
  </tr>
</table>
 
<form enctype="multipart/form-data" action="update.php"  method="post" name="form1">
	<table class="TableList" width="85%"  align="center">
  <TR>
      <TD class="TableData">文件号：</TD>
      <TD class="TableData"><div id="fileCode"></div></TD>
      <TD class="TableData">文件主题词：</TD>
      <TD class="TableData"><div id="fileSubject"></div></TD>
  </TR>
  <TR>
      <TD class="TableData">文件标题：</TD>
      <TD class="TableData"><div id="fileTitle"></div></TD>
      <TD class="TableData">文件辅标题：</TD>
      <TD class="TableData"><div id="fileTitleo"></div></TD>
  </TR>
  <TR>
      <TD class="TableData">发文单位：</TD>
      <TD class="TableData"><div id="sendUnit"></div></TD>
      <TD class="TableData">发文日期：</TD>
      <TD class="TableData"><div id="sendDate"></div></TD>
  </TR>
  <TR>
      <TD nowrap class="TableData">密级：</TD>
      <TD class="TableData"><div id="secret"></div></TD>
      <TD class="TableData">紧急等级：</TD>
      <TD class="TableData"><div id="urgency"></div></TD>
  </TR>
  <TR>
      <TD nowrap class="TableData">文件分类：</TD>
      <TD class="TableData"><div id="fileType"></div></TD>
      <TD class="TableData">公文类别：</TD>
      <TD class="TableData"><div id="fileKind"></div></TD>
  </TR>
  <TR>
      <TD nowrap class="TableData">文件页数：</TD>
      <TD class="TableData"><div id="filePage"></div></TD>
      <TD class="TableData">打印页数：</TD>
      <TD class="TableData"><div id="printPage"></div></TD>
  </TR>
  <TR>
      <TD nowrap class="TableData">备注：</TD>
      <TD class="TableData" colSpan=3><div id="remark"></div></TD>
   </TR>
    <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
        <input type="button" value="关闭" class="BigButton" onClick="javascript:window.close();">
      </td>
    </tr>
  </table>
</form>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<%
	String seqId=request.getParameter("seqId");
	if(YHUtility.isNullorEmpty(seqId)){
		seqId = "0";
	}
    String flag = request.getParameter("flag");
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
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
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
    var downloadYn = data.downloadYn;    
    //alert("downloadYn>>"+downloadYn);
    var sendDateStr = data.sendDate;
    if(sendDateStr){
    	$('sendDate').innerHTML = sendDateStr.substr(0, 10);
    }
    
    $('secret').innerHTML = getCodeName("RMS_SECRET",data.secret);
    $('fileWord').innerHTML = data.fileWord;
      //getCodeName("FILE_WORD",data.fileWord);
    $('fileYear').innerHTML = getCodeName("FILE_YEAR",data.fileYear);
    $('issueNum').innerHTML = data.issueNum;
      //getCodeName("ISSUE_NUM",);
    $('urgency').innerHTML =  getCodeName("RMS_URGENCY",data.urgency);
    $('fileType').innerHTML = getCodeName("RMS_FILE_TYPE",data.fileType);
    $('fileKind').innerHTML = getCodeName("RMS_FILE_KIND",data.fileKind);

    $('filePage').innerHTML = data.filePage;
    $('printPage').innerHTML = data.printPage;
    $('remark').innerHTML = data.remark;
    if (data.deadline != "" ){
    var urll= requestURL + "/getDeadlineID.act?seqId="+data.deadline;
    var json1 = getJsonRs(urll);
    var d=json1.rtData;
    $('deadline').innerHTML=d.deadline_desc;
    }
    $("returnAttId").value = data.attachmentId;
		$("returnAttName").value = data.attachmentName;

		$("returnDocAttId").value = data.docAttachmentId;
		$("returnDocAttName").value = data.docAttachmentName;
		//alert(data.docAttachmentId + "   >>" + data.docAttachmentName);

		var selfdefMenu = {
      	office:["downFile","read"], 
        img:["downFile","play"],  
        music:["downFile","play"],  
		    video:["downFile","play"], 
		    others:["downFile"]
			}

		 var docSelfdefMenu = {
      	office:["downFile","read"], 
        img:["downFile","play"],  
        music:["downFile","play"],  
		    video:["downFile","play"], 
		    others:["downFile"]
		 }
		 if(downloadYn == 0){
			selfdefMenu.office = selfdefMenu.office.without("downFile");				 
			docSelfdefMenu.office = selfdefMenu.office.without("downFile");				 
		 }
		 attachMenuSelfUtil("attr","roll_manage",$('returnAttName').value ,$('returnAttId').value, '','','<%=seqId%>',selfdefMenu);
		 attachMenuSelfUtil("docAttr","roll_manage",$('returnDocAttName').value ,$('returnDocAttId').value, 'docAttachment','','<%=seqId%>',docSelfdefMenu);

		 if(!data.attachmentId ){
				$("attr").innerHTML = "无附件 ";		
	   }
		 if(!data.docAttachmentId ){
				$("docAttr").innerHTML = "无正文附件 ";		
	   }
    
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
      
      <TD class="TableData">公文字：</TD>
       <TD class="TableData"><div id="fileWord"></div></TD>
  </TR>
  
  
  <TR>
      <TD class="TableData">公文年号：</TD>
      <TD class="TableData">
      <div id="fileYear"></div>
      </TD>
      <TD class="TableData">公文期号：</TD>
      <TD class="TableData">
      <div id="issueNum"></div>
      </TD>
  </TR>
  <tr>
  <TD class="TableData">文件主题词：</TD>
      <TD class="TableData" colspan="3"><div id="fileSubject"></div></TD>
  </tr>
  <TR>
      <TD class="TableData">文件标题：</TD>
      <TD class="TableData"><div id="fileTitle"></div></TD>
      <TD class="TableData">文件辅标题：</TD>
      <TD class="TableData"><div id="fileTitleo"></div></TD>
  </TR>
  <tbody style="display:<% if ("1".equals(flag)) {%>none<% }  %>">
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
      <TD class="TableData" ><div id="remark"></div></TD>
      <TD nowrap class="TableData">归档期限：</TD>
      <TD class="TableData" ><div id="deadline"></div></TD>
   </TR>
   <tr height="25">
      <td nowrap class="TableData">正文：</td>
      <td class="TableData" colspan="3">
      	<input type = "hidden" id="returnDocAttId" name="returnDocAttId"></input>
      	<input type = "hidden" id="returnDocAttName" name="returnDocAttName"></input>
       	<span id="docAttr"></span>      
      </td>
    </tr> 
   <tr class="TableData">
     <td nowrap>附件文档：</td>
     <td nowrap colSpan=3>
     	<input type = "hidden" id="returnAttId" name="returnAttId"></input>
     	<input type = "hidden" id="returnAttName" name="returnAttName"></input>
      	<span id="attr"></span> 
     </td>
   </tr>
   </tbody>
    <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
        <input type="button" value="关闭" class="BigButton" onClick="javascript:window.close();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>
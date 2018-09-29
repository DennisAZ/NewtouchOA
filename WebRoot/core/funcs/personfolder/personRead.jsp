<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String sortId=request.getParameter("sortId");
	String contentIdStr=request.getParameter("contentId");
	String resendFlag=request.getParameter("resendFlag");
	String shareFlag=request.getParameter("shareFlag");
	String managerPriSeqId=request.getParameter("managerPriSeqId");
	if(sortId==null){
		sortId="0";
	}
	if(resendFlag==null){
		resendFlag="";
	}
	if(shareFlag==null){
		shareFlag="";
	}
	if(managerPriSeqId==null){
		managerPriSeqId="";
	}
	int contentId=0;
	if(contentIdStr!=null){
		contentId=Integer.parseInt(contentIdStr);
	}
%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看文件</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
//alert("sortId>><%=sortId%>"+ "  contentId>>><%=contentId%>");
var requestURL="<%=contextPath%>/yh/core/funcs/filefolder/act/YHFileContentAct";
var getRequestURL="<%=contextPath%>/yh/core/funcs/personfolder/act/YHPersonFileContentAct";

function show_all(){
	document.all("div1").style.display="none";
	document.all("div2").style.display="";
}
function hide_all(){
	document.all("div1").style.display="";
	document.all("div2").style.display="none";   
}

function doInit(){
  //signFile();
	var readUrl = getRequestURL + "/getFileContentInfoById.act?contentId=<%=contentId%>";
	var json=getJsonRs(readUrl);
	//alert(rsText);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	prcsJson=json.rtData;
	if(prcsJson.length>0){
		var prcs=prcsJson[0];
		var readers = prcs.readers;
		var subject = prcs.subject;
		var content =prcs.content;
		var attachmentDesc = prcs.attachmentDesc;
		var creater = prcs.creater;
		var logs = prcs.logs;
		var attIdArray = prcs.attachmentId.split(",");
		var attNameArray = prcs.attachmentName.split("*");
			
		$("subject").innerHTML=subject;
		$("contentStr").innerHTML=content
	
		//$("creater").innerHTML=creater;

		if(prcs.attachmentId || prcs.attachmentName){
			$("attch").show();
			for(var j=0;j<attIdArray.length-1;j++){
				$("returnAttId").value += "," + attIdArray[j];
				$("returnAttName").value += "*" + attNameArray[j];
				
			}
			//attachMenuUtil("attr","file_folder",null,$('returnAttName').value ,$('returnAttId').value,false,"","","","","",'<%=contentId%>');

			var  selfdefMenu = {
        	office:["downFile","dump","read"], 
	        img:["downFile","dump","play"],  
	        music:["downFile","dump","play"],  
			    video:["downFile","dump","play"], 
			    others:["downFile","dump"]
				}
	
			attachMenuSelfUtil(attr,"file_folder",$('returnAttName').value ,$('returnAttId').value, '','','<%=contentId%>',selfdefMenu);
			
		}
		
	}
	
}

function signFile(){
  var contentIdStr='<%=contentId%>' + ",";  
	var signUrl = requestURL + "/fileSign.act?&contentId=" + contentIdStr;
	//alert(signUrl);
	getJsonRs(signUrl);
  
}

//浮动菜单文件的删除
function deleteAttachBackHand(attachName,attachId,attrchIndex){
	var url= requestURL +"/delFloatFile.act?attachId=" + attachId +"&attachName=" + attachName + "&contentId=" + attrchIndex;
  //alert("attachId>>>"+attachId  +"  attachName>>>" +attachName +"   attrchIndex>>" +attrchIndex);
  //alert(url);
	var json=getJsonRs(encodeURI(url));
	if(json.rtState =='1'){
		alert(json.rtMsrg);
		return false;
	}else{
	  prcsJson=json.rtData;
		var updateFlag=prcsJson.updateFlag;
		if(updateFlag){
			//alert(updateFlag);
		  return true;
		 
		}else{
			return false;
		}
  	
	}
  
}

</script>

</head>
<body onload="doInit();">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/folder_file.gif" width=22 align="absmiddle"><b><span class="Big1">查看文件</span></b>
    </td>
  </tr>
</table>
 
<form action=""  method="post" name="form1">
<table class="TableBlock" width="100%" align="center">
	<tr>
	  <td class="TableHeader" align="center" colspan="2"><b><span class="big" id="subject"></span><font color="#FF0000"><span id="sign_desc"></span></font></b></td>
	</tr>
	<tr>
	  <td class="TableData Content" height="250" valign="top" colspan="2">
	  	<div id="contentStr">						  	
	  	</div>
	  
	  </td>
	</tr>
	
	<tr class=small id="attch" style="display: none">		
	  <td class="TableData" width="100">附件：</td>
	  <td class="TableData" width="400">
	  		<input type = "hidden" id="returnAttId" name="returnAttId"></input>
      	<input type = "hidden" id="returnAttName" name="returnAttName"></input>
       	<span id="attr"></span>
	  </td>
	</tr>
	<tr class=small id="attachDesc" style="display: none">
	  <td class="TableData" width="100">附件说明：</td>
	  <td class="TableData" width="400">
	  	<div id="attacDesc"></div> 
	 	</td>
	</tr>

	<tr align="center" class="TableControl">
		<td colspan="2">
		  <input type="button" value="打印" class="BigButton" onClick="document.execCommand('Print');" title="打印文件内容">&nbsp;&nbsp;
		  <%
		  	if("1".equals(resendFlag.trim())){
		  		
		  	
		  %>
		  
		 		<input id="closeFlag" type="button" value="关闭" class="BigButton" onClick="window.close();">
		  <%
		  	}else if("1".equals(shareFlag.trim())){
		  %>
		  	<input type="button" value="返回" class="BigButton" onClick="location='<%=contextPath%>/core/funcs/personfolder/shareFolder.jsp?seqId=<%=sortId %>&managerPriSeqId=<%=managerPriSeqId %> '">
		  	
		  <%	
		  	}else{	
		  %>
		  	<input type="button" value="返回" class="BigButton" onClick="location='<%=contextPath%>/core/funcs/personfolder/folder.jsp?seqId=<%=sortId %>'"> 
		  <%
		  	}
		  %>
	  </td>
	</tr>
  </table>
</form>




</body>
</html>
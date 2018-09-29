<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String sortId=request.getParameter("sortId");
	String contentIdStr=request.getParameter("contentId");
	String newFileFlag=request.getParameter("newFileFlag");
	if(sortId==null){
		sortId="0";
	}
	if(newFileFlag==null){
		newFileFlag="";
	}
	int contentId=0;
	if(contentIdStr!=null){
		contentId=Integer.parseInt(contentIdStr);
	}
	
	//桌面传过来
	String showFlag=request.getParameter("showFlag");
	if(showFlag==null){
		showFlag="";
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
function show_all(){
	document.all("div1").style.display="none";
	document.all("div2").style.display="";
}
function hide_all(){
	document.all("div1").style.display="";
	document.all("div2").style.display="none";   
}

function doInit(){
  signFile();
  getPrivate();
	var readUrl = requestURL + "/getFileContentInfoById.act?contentId=<%=contentId%>";
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
		

		if(readers == 1){
			$("sign_desc").innerHTML="(已签阅)";
		}
		$("subject").innerHTML=subject;
		$("contentStr").innerHTML=content
	
		$("creater").innerHTML=creater;

		if(prcs.attachmentId || prcs.attachmentName){
			$("attch").show();
			for(var j=0;j<attIdArray.length-1;j++){
				$("returnAttId").value += "," + attIdArray[j];
				$("returnAttName").value += "*" + attNameArray[j];
				
			}
			//attachMenuUtil("attr","file_folder",null,$('returnAttName').value ,$('returnAttId').value,false,"","","","","",'<%=contentId%>');

			var  selfdefMenu = {
       	office:["downFile", "dump", "read", "readNoPrint", "edit", "setSign", "deleteFile"], 
        img:["downFile", "dump", "play"],  
        music:["downFile", "dump", "play"],  
		    video:["downFile", "dump", "play"],
		    pdf: ["readpdf", "downFile", "dump"], 
		    others:["downFile", "dump"]
			};

			if(visiPriv == "1" && managePriv != "1" && downPriv != "1"){
			  selfdefMenu.others = selfdefMenu.others.without("downFile", "dump");
			  selfdefMenu.pdf = selfdefMenu.pdf.without("downFile", "dump");
			  selfdefMenu.music = selfdefMenu.music.without("downFile", "dump");
			  selfdefMenu.img = selfdefMenu.img.without("downFile", "dump");
			  selfdefMenu.video = selfdefMenu.video.without("downFile", "dump");
				selfdefMenu.office = selfdefMenu.office.without("downFile","dump","read","edit","setSign","deleteFile");				  
			}
			if(managePriv == "1"){
			  selfdefMenu.office = selfdefMenu.office.without("setSign","deleteFile","readNoPrint");		
			}
			if(downPriv == "1" && managePriv != "1"){
			  selfdefMenu.office = selfdefMenu.office.without("setSign","deleteFile","edit","readNoPrint");		
			}
				
			attachMenuSelfUtil(attr,"file_folder",$('returnAttName').value ,$('returnAttId').value, '','','<%=contentId%>',selfdefMenu);



			
		}
		if(attachmentDesc){
			$("attachDesc").show();
			$("attacDesc").innerHTML=attachmentDesc;
		}
		if(logs){
			$("recode").show();
			$("logDesc").innerHTML=logs;
		}else{
		  $("noRecode").show();
		  
		}

		//var contentStrImg="<img alt='' src='/inc/attach.php&#63;MODULE=file_folder&amp;YM=1002&amp;ATTACHMENT_ID=-1601324162&amp;ATTACHMENT_NAME=%BB%E6%CD%BC1.jpg' />";

	

		
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

//获取权限信息
var visiPriv;
var managePriv;
var newPriv;
var downPriv;
var ownerPriv;
var downUserPriv;
var sortParent;
var seqId;
function getPrivate(){
  var requestURL1="<%=contextPath%>/yh/core/funcs/system/filefolder/act/YHFileSortAct";
	var url1=requestURL1 + "/getPrivteById.act?seqId=<%=sortId%>";
	var json=getJsonRs(url1);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	prcsJson=json.rtData;
	//alert("rsText>>:"+rsText);

	if(prcsJson.length>0){
		var prcs=prcsJson[0];
		visiPriv=prcs.visiPriv;
		managePriv=prcs.managePriv;
		newPriv=prcs.newPriv;
		downPriv=prcs.downPriv;
		ownerPriv=prcs.ownerPriv;
		downUserPriv=prcs.downUserPriv;
		sortParent=prcs.sortParent;
		seqId=prcs.seqId;
		//alert("visiPriv:"+visiPriv+" managePriv:"+managePriv+"  newPriv:"+newPriv+"  downPriv:"+downPriv+"  ownerPriv:"+ownerPriv+"  sortParent:"+sortParent+"  seqId:"+seqId+"  downUserPriv:"+downUserPriv);
		
	}

  
}

function getBack(){
	var showFlag='<%=showFlag%>'; 
	if(showFlag == 'CONTENT'){
		//history.back();
		top.dispDesk();
	}else{
		location='<%=contextPath%>/core/funcs/filefolder/folder.jsp?seqId=<%=sortId%>';
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
	<tr class=small>
	  <td class="TableData" width="100">创建人：</td>
	  <td class="TableData" width="400">
	  	<div id="creater"></div>
	  </td>
	</tr>
  <tr class=small>
		<td class="TableData" width="100">修改记录：</td>
		<td class="TableData" width="400">
			<div id="recode" style="display: none" >
				<div id="div1" onclick="show_all();" title="点击展开详细记录" style="cursor:pointer"><img src="<%=imgPath%>/collapsed.gif" align="absMiddle">点击展开详细修改记录</div>
	     	<div id="div2" title="点击收缩" onclick="hide_all();" style="cursor:pointer;display:none;"><img src="<%=imgPath%>/expanded.gif" align="absMiddle">点击收缩修改明细<br><span id="logDesc"></span><br />
				</div>
			</div>
			<div id="noRecode" style="display: none" >无修改记录</div>
   	</td>
  </tr>
	<tr align="center" class="TableControl">
		<td colspan="2">
		  <input type="button" value="打印" class="BigButton" onClick="document.execCommand('Print');" title="打印文件内容">&nbsp;&nbsp;
		 <%
		 		if("1".equals(newFileFlag.trim())){
		 %>
		 	 <input type="button" value="关闭" class="BigButton" onClick="top.close();">		 
		 <%
		 		}else{
		 %>
		  <input type="button" value="返回" class="BigButton" onClick="getBack();">
	  <%
	 			}
	  %>
	  </td>
	</tr>
  </table>
</form>




</body>
</html>
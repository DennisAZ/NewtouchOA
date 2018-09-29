<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String seqIdStr = request.getParameter("seqId");
	int seqId = 0;
	if (!YHUtility.isNullorEmpty(seqIdStr)) {
		seqId = Integer.parseInt(seqIdStr);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会议纪要</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<!-- 文件上传 -->
<link href="<%=cssPath%>/cmp/swfupload.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/attach.js"></script>
<script type="text/javascript">
//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;

function doInit(){
	getMeetingSummaryDetail();
	getNewCommentInfo();
}

function getMeetingSummaryDetail(){
	var url = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingAct/getMeetingSummaryDetail.act?seqId=<%=seqId %>";
  var json = getJsonRs(url);
  if(json.rtState == "0"){
    var data = json.rtData;
    bindJson2Cntrl(json.rtData);
    if($("readPeopleId") && $("readPeopleId").value){
      bindDesc([{cntrlId:"readPeopleId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
  	var selfdefMenu = {
      	office:["downFile","read","edit","deleteFile"], 
        img:["downFile","play","deleteFile"],  
        music:["downFile","play","deleteFile"],  
		    video:["downFile","play","deleteFile"], 
		    others:["downFile","deleteFile"]
			}
		attachMenuSelfUtil("attr","meeting",$('attachmentName1').value ,$('attachmentId1').value, 'jj','','<%=seqId%>',selfdefMenu);
  }else{
    $('haveData').hide();
    $('msgContent').update(json.rtMsrg);
    $("noData").show();
  }
}

/**
 * 获取最新评论
 */
function getNewCommentInfo(){
	var url = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingCommentAct/getNewCommentInfo.act?seqId=<%=seqId %>";
  var json = getJsonRs(url);
  if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	prcsJson=json.rtData;
	var table=new Element('table',{ "width":"90%","class":"TableList","align":"center"});
	var strTable="<tbody id='tbody'><tr class='TableHeader' align='center' style='font-size:10pt'>"
		+ "<td nowrap width='30%' align='center'>最新5条评论</td>"
		+ "</tr><tbody>";	

	table.update(strTable);
	$('commentDiv').update(table);
	if(prcsJson.length>0){
		for(var i = 0;i<prcsJson.length;i++){
			var prcs = prcsJson[i];
			var commentId = prcs.commentId;
			var content = prcs.content;
			var reTime = prcs.reTime;
			var attachmentId = prcs.attachmentId;
			var attachmentName = prcs.attachmentName;
			var personName = prcs.personName;
			var deptName = prcs.deptName;
			var delPrivFlag = prcs.delPrivFlag;
			
			var showDel = "";
			if(delPrivFlag == "1"){
				showDel = "&nbsp;&nbsp;&nbsp;<a align='right' href='#' onclick='delCommentInfo(" + commentId + ")' style='text-decoration:underline'>删除</a>";
			}
			var className = (i % 2 == 0) ? "TableData" : "TableData";  										
			var tr = new Element('tr',{'class': className ,'font-size':'10pt'});		
			tr.style.width = "100%"
			table.firstChild.appendChild(tr);	
			var str = "<td align='left' ><u title='部门：" + deptName + "' style='cursor:pointer' >"
					+ personName + "</u>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发表时间：" + reTime
					+ showDel
					+ "</td>"					
				tr.update(str);

			var tr2 =new Element('tr',{'width':'100%','class': className ,'font-size':'10pt'});	
			table.firstChild.appendChild(tr2);		
			var str2 = "<td align='left' >"
					+ content
					+ "</td>"					
			tr2.update(str2);

			var hiddenDiv = "<input type = 'hidden' id='returnAttId_" + i + "' name='returnAttId_" +i + "'></input>"
					+ "<input type = 'hidden' id='returnAttName_" + i + "' name='returnAttName_" + i + "'></input>"
					+ "<div id='attr_" + i + "'></div>";
			
			var tr3 =new Element('tr',{'width':'100%','class': className ,'font-size':'10pt'});	
			table.firstChild.appendChild(tr3);		
			var str3 = "<td align='left' >"
					+ "附件文件:" + hiddenDiv
					+ "</td>"					
			tr3.update(str3);
			$("returnAttId_"+i).value = attachmentId;
			$("returnAttName_"+i).value = attachmentName;
			var  selfdefMenu = {
	        	office:["downFile","dump","read","edit","setSign","deleteFile"], 
		        img:["downFile","dump","play"],  
		        music:["downFile","dump","play"],  
				    video:["downFile","dump","play"], 
				    others:["downFile","dump"]
					}
			attachMenuSelfUtil("attr_"+i,"meeting",$('returnAttName_'+i).value ,$('returnAttId_'+i).value, i,'',commentId,selfdefMenu);
		}
	}else{
		var tr4 =new Element('tr',{'width':'100%','class': className ,'font-size':'10pt'});	
		table.firstChild.appendChild(tr4);		
		var str4 = "<td align='left' >"
				+ "暂无评论"
				+ "</td>"					
		tr4.update(str4);
		
	}

	
}



/**
 * 删除 评论信息
 */
function delCommentInfo(commentId){
	var url = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingCommentAct/delCommentInfo.act";
  var json = getJsonRs(url,"commentId=" + commentId);
  if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	location.reload();
}


function checkForm(){
	if($("content").value == ""){
		alert("请填写会议纪要！");
    return (false);
	}
	return true;
}

function doSubmit(){
  $("form1").submit();
}


</script>
</head>
<body onload="doInit();">
<div id="haveData">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif" width="22" height="18"><span class="big3"> 会议纪要</span>
    </td>
  </tr>
</table>
 
 
<table class="TableBlock" width="90%" align="center"><tr>
   <td nowrap class="TableContent" width="80">会议名称：</td>
   <td class="TableData" colspan="3"><div id="mName"></div> </td>
</tr>
<tr>
   <td nowrap class="TableContent" width="80">指定读者：</td>
   <td class="TableData" colspan="3">
   	<input type="hidden" name="readPeopleId" id="readPeopleId" value="">
   	<div id="readPeopleIdDesc"> </div>   
   	</td>
</tr>
<tr>
   <td valign="top" nowrap class="TableContent" height="160" width="80">纪要内容：</td>
   <td valign="top" class="TableData" colspan="3"><div id="summary"></div></td>      
</tr>
 
<tr>
   <td class="TableData">附件文件:</td>
   <td class="TableData">
   	<input type = "hidden" id="attachmentId1" name="attachmentId1"></input>
     <input type = "hidden" id="attachmentName1" name="attachmentName1"></input>
     <span id="attr"></span>
	 </td>
</tr>
</table>
<br>

<div id="commentDiv"></div>
<br> 
 <br>
 
 
<form name="form1" id="form1" enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingCommentAct/addMeetingCommentInfo.act"  method="post" name="form1" onsubmit="return CheckForm();">
<table class="TableBlock" width="90%" align="center">	
<tr>
	<td class="TableHeader" colspan="2"><img src="<%=imgPath %>/green_arrow.gif"> 添加评论：</td>
</tr>
<tr>
   <td valign="top" nowrap class="TableContent" width="80">内容：</td>
   <td class="TableData" colspan="3">
   <textarea cols="57" name="content" id="content" rows="5" class="BigInput" wrap="on"></textarea>
   </td>
</tr>
<tr height="25" >
  <td nowrap class="TableData"><span id="ATTACH_LABEL">附件上传：</span></td>
  <td class="TableData">
    <script>ShowAddFile();</script>
	 	<input type="hidden" name="ATTACHMENT_ID_OLD" id="ATTACHMENT_ID_OLD" value="">
	 	<input type="hidden" name="ATTACHMENT_NAME_OLD"	id="ATTACHMENT_NAME_OLD" value="">
  </td>
</tr>
<tr class="TableControl">
<td align="center" colspan="4">
	<input type="hidden" value="<%=seqId %>" name="meetingId" id="meetingId">
	<input type="hidden" value="1" name="status">
  <input type="button" value="保存" class="BigButton" title="保存会议纪要" onclick="doSubmit();">&nbsp;&nbsp;&nbsp;&nbsp;
  <input type="button" class="BigButton" value="关闭" onclick="window.close()">
</td>
</tr>
</table>
</form>

</div>
<div id="noData" style="display:none">
<table class="MessageBox" align="center" width="350">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" id="msgContent" style="font-size:12pt"></div>
    </td>
  </tr>
</table></div>
</body>
</html>
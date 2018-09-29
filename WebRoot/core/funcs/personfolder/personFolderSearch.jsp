<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqIdStr = request.getParameter("seqId");
	int seqId=0;
	if(seqIdStr!=""){
	  seqId=Integer.parseInt(seqIdStr);
	}
	
	
	
	String subject = request.getParameter("SUBJECT");// 标题包含文字
	String contentNo = request.getParameter("CONTENT_NO");// 排序号
	String key1 = request.getParameter("KEY1");// 排序号
	String key2 = request.getParameter("KEY2");// 排序号
	String key3 = request.getParameter("KEY3");// 排序号

	String attachmentDesc = request.getParameter("ATTACHMENT_DESC");// 附件说明包含文字
	String attachmentName = request.getParameter("ATTACHMENT_NAME");// 附件文件名包含文字
	String attachmentData = request.getParameter("ATTACHMENT_DATA");// 附件内容包含文字

	String sendTimeMin = request.getParameter("sendTime_Min");// 最小日期
	String sendTimeMax = request.getParameter("sendTime_Max");// 最大日期
	
	if(subject == null){
		subject = "";
  }
	if(contentNo == null){
		contentNo = "";
  }
	if(key1 == null){
		key1 = "";
  }
	if(key2 == null){
		key2 = "";
  }
	if(key3 == null){
		key3 = "";
  }
	if(attachmentDesc == null){
		attachmentDesc = "";
  }
	if(attachmentName == null){
		attachmentName = "";
  }
	if(attachmentData == null){
		attachmentData = "";
  }
	if(sendTimeMin == null){
		sendTimeMin = "";
  }
	if(sendTimeMax == null){
		sendTimeMax = "";
  }
	
  String pageIndex = request.getParameter("pageNo");
  if(pageIndex == null){
    pageIndex = "0";
  }
  String pageSize = request.getParameter("pageSize");
  if(pageSize == null){
    pageSize = "10";
  }
	
	
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.net.URLEncoder"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件查询结果</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" /> 
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/pagePilot.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript">
var requestURL="<%=contextPath%>/yh/core/funcs/filefolder/act/YHFileContentAct";
var requestURL1="<%=contextPath%>/yh/core/funcs/system/filefolder/act/YHFileSortAct";

var cfgs;
var pageSize = "<%=pageSize%>";
var totalRecord = 0;
var pageIndex = "<%=pageIndex%>";


var returnSortId;
var returnSubject;
var returnContentNo;
var returnKey1;
var returnKey2;
var returnKey3;
var returnSendTimeMin;
var returnSendTimeMax;
var returnAttachmentName;
var  returnAttachmentData;
var returnAttachmentDesc;


function doInit(){
	getFolderName();

	var managePriv=1;
	
	//输出列表
	var url=requestURL+"/queryFileContentInfoById.act";
	var pars = $("form1").serialize();
	var json = getJsonRs(url,pars);
	//alert(rsText);
  if(json.rtState=='0'){
 		var prcJson=json.rtData;
 		if(prcJson.length>0){

 			//分页处理 			
			totalRecord=prcJson[0].totalRecord;	
			pageIndex = prcJson[0].pageNo;
			pageSize = prcJson[0].pageSize;   
			 cfgs = {
			    dataAction: "",
			    container: "pageInfo",
			    pageSize:pageSize,
			    loadData:loadDataAction,
			    totalRecord:totalRecord,
			    pageIndex:pageIndex
			  };
			 showPage();

			 returnSortId=prcJson[0].returnSortId;
			 returnSubject=prcJson[0].returnSubject;
			 returnContentNo=prcJson[0].returnContentNo;
			 returnKey1=prcJson[0].returnKey1;
			 returnKey2=prcJson[0].returnKey2;
			 returnKey3=prcJson[0].returnKey3;
			 returnSendTimeMin=prcJson[0].returnSendTimeMin;
			 returnSendTimeMax=prcJson[0].returnSendTimeMax;
			 returnAttachmentName=prcJson[0].returnAttachmentName;
			 returnAttachmentData=prcJson[0]. returnAttachmentData;
			 returnAttachmentDesc=prcJson[0].returnAttachmentDesc;

			 
			//alert("totalRecord>>"+totalRecord +" pageIndex>>>"+pageIndex +"  pageSize>>"+pageSize);	
 			
 			var table=new Element('table',{ "width":"100%","class":"TableList","align":"center"});

 			var strTable="<tbody id='tbody'><tr class='TableHeader' align='center' style='font-size:10pt'>"
 				+"<td align='center'> 文件名称</td>"
				+"<td align='center'>附件文件</td>"
				+"<td align='center'>附件说明</td>"
				+"<td width='125' align='center'>发布时间</td>"
				if(managePriv=="1"){
					strTable+="<td width='120' align='center'>操作</td>";
				}
				strTable+="</tr><tbody>";
			table.update(strTable);
			$('listDiv').appendChild(table);



 			

 			for(var i=0;i<prcJson.length;i++){
 				var prcs=prcJson[i];
 				//alert(prcs.equipmentName);
 				var contentId=parseInt(prcs.contentId);
 				var subject=prcs.subject;
 				var attachmentId=prcs.attachmentId;
 				var attachmentNames=prcs.attachmentName;
 				var attachmentDesc=prcs.attachmentDesc;
 				var sendTime=prcs.sendTime;


 				var hiddenDiv="<input type = 'hidden' id='returnAttId_" + i + "' name='returnAttId_" +i + "'></input>"
				+ "<input type = 'hidden' id='returnAttName_" + i + "' name='returnAttName_" + i + "'></input>"
				+ "<div id='attr_" + i + "'></div>";

				var className = (i % 2 == 0) ? "TableLine1" : "TableLine2";  

 				var tr=new Element('tr',{'width':'90%','class':className,'font-size':'10pt'});
 				table.firstChild.appendChild(tr);

 				var str = "<td align='center'>"	
 					+ "<a href='<%=contextPath%>/core/funcs/personfolder/personRead.jsp?sortId=<%=seqId%>&contentId="+contentId +"'> "			
 					+ subject + "</a></td><td align='left'>"
 					+ hiddenDiv + "</td><td align='center'>"
 					+ attachmentDesc + "</td><td align='center'>"
 					+ sendTime + "</td>"

 					if (managePriv=="1") {
 						str+="<td align='center'>"					
 							+ "<a href='"+ contextPath +"/core/funcs/personfolder/edit.jsp?seqId=<%=seqId %>&queryFlag=1&contentId="+contentId +"'> 编辑&nbsp;</a>" 
 							+ "<a href='#' onclick='delete_Proces(\""+ contentId +"\")'>删除 </a>&nbsp;"
 						str += "</td>"	;	
 					}
 					tr.update(str);


 				$("returnAttId_"+i).value= attachmentId;
				$("returnAttName_"+i).value= attachmentNames;
 				
 				var  selfdefMenu = {
        	office:["downFile","dump","read"], 
	        img:["downFile","dump","play"],  
	        music:["downFile","dump","play"],  
			    video:["downFile","dump","play"], 
			    others:["downFile","dump"]
				}

				

 				attachMenuSelfUtil("attr_"+i,"file_folder",$('returnAttName_'+i).value ,$('returnAttId_'+i).value, i,'',contentId,selfdefMenu);
 				
 			}
 			$("back").show();
 		
 			
 		}else{
			$("warnDiv").show();
 	 	}
  }
}




function delete_Proces(contentId){
	//alert(contentId);	
	msg="确定要删除选择文件吗？这将不可恢复！"
	if(window.confirm(msg)){
		var personRequestURL="<%=contextPath%>/yh/core/funcs/personfolder/act/YHPersonFileContentAct";
			
		var url = personRequestURL + "/delCheckedFile.act?seqIdStr=" + contentId;
		//alert("url:"+url);
		var json=getJsonRs(url);
		if(json.rtState == '1'){
			alert(json.rtMsrg);
			return ;				
		}
		//alert(rsText);
		window.location.reload();
	} 	
}

//显示分页
var pageInfoS = null;
function showPage() {
  //alert('test');
  pageInfoS = new YHJsPagePilot(cfgs);
  pageInfoS.show();
  $('pageDiv').style.display = "";

}
function loadDataAction(obj){
  var pageNo = obj.pageIndex;
  var pageSize = obj.pageSize;
	//alert("loadDataAction>>>field>>>"+field  +"  asc>>"+ascDesc);
  //alert("pageNo.F>>"+pageNo +"  pageSize>>"+pageSize);
 // window.location.href ="search.jsp?seqId=<%=seqId%>&pageNo=" + pageNo + "&pageSize=" + pageSize;
 
 	
	$("seqId").value= returnSortId;
	$("SUBJECT").value= returnSubject;
	$("CONTENT_NO").value= returnContentNo;
	
	$("KEY1").value= returnKey1;
	$("KEY2").value= returnKey2;
	$("KEY3").value= returnKey3;
	
	$("sendTime_Min").value= returnSendTimeMin;
	$("sendTime_Max").value= returnSendTimeMax;
	$("ATTACHMENT_DESC").value= returnAttachmentDesc;
	
	$("ATTACHMENT_NAME").value= returnAttachmentName;
	$("ATTACHMENT_DATA").value= returnAttachmentData;
	$("pageNo").value= pageNo;
	$("pageSize").value= pageSize;
	$("form2").submit();
  
}

function getFolderName(){
	var getFolderNameUrl =contextPath + "/yh/core/funcs/filefolder/act/YHFileSortAct/getFolderName.act?seqId=<%=seqId%>";
	var json=getJsonRs(getFolderNameUrl);
	//alert(rsText);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	prcsJson=json.rtData;	

	$("folderName").innerHTML = prcsJson.folderName;
	


	
}


</script>

</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath%>/core/funcs/filefolder/images/folder_search.gif" align="absmiddle">&nbsp;<b><span class="Big1" id="folderName"> - 文件查询结果</span></b><br>
    </td>
    <td id="pageDiv" align="right" valign="bottom" class="small1">
	   <div id="pageInfo" style="float:right;"></div>
  	</td>
  </tr>
</table>


<div id="listDiv" align="center"></div>
<br>
<div align="center" id="back" style="display: none">
  <input type="button" value="返回" class="BigButton" onclick="location.href='<%=contextPath %>/core/funcs/personfolder/personFolderQuery.jsp?seqId=<%=seqId%>'">
</div>


<div>
<form id="form1" name="form1" action="" method="post">
	<input type="hidden" id="sortId" name="seqId" value="<%=seqId%>">
	<input type="hidden" id="subject" name="subject" value="<%=URLEncoder.encode(subject,"UTF-8")%>">
	<input type="hidden" id="contentNo" name="contentNo" value="<%=URLEncoder.encode(contentNo,"UTF-8")%>">	
	
	<input type="hidden" id="key1" name="key1" value="<%=URLEncoder.encode(key1,"UTF-8")%>">	
	<input type="hidden" id="key2" name="key2" value="<%=URLEncoder.encode(key2,"UTF-8")%>">	
	<input type="hidden" id="key3" name="key3" value="<%=URLEncoder.encode(key3,"UTF-8")%>">	
	
	<input type="hidden" id="attachmentDesc" name="attachmentDesc" value="<%=URLEncoder.encode(attachmentDesc,"UTF-8")%>">	
	<input type="hidden" id="attachmentName" name="attachmentName" value="<%=URLEncoder.encode(attachmentName,"UTF-8")%>">	
	<input type="hidden" id="attachmentData" name="attachmentData" value="<%=URLEncoder.encode(attachmentData,"UTF-8")%>">
	<input type="hidden" id="sendTimeMin" name="sendTimeMin" value="<%=URLEncoder.encode(sendTimeMin,"UTF-8")%>">	
	<input type="hidden" id="sendTimeMax" name="sendTimeMax" value="<%=URLEncoder.encode(sendTimeMax,"UTF-8")%>">	
	
	<input type="hidden" id="currNo" name="currNo" value="<%=pageIndex %>">	
	<input type="hidden" id="pageSizeStr" name="pageSizeStr" value="<%=pageSize %>">	

</form>
</div>

<div>
<form id="form2" name="form2" action="<%=contextPath %>/core/funcs/personfolder/personFolderSearch.jsp" method="post">
	<input type="hidden" id="seqId" name="seqId" value="">
	<input type="hidden" id="SUBJECT" name="SUBJECT" value="">
	<input type="hidden" id="CONTENT_NO" name="CONTENT_NO" value="">	
	
	<input type="hidden" id="KEY1" name="KEY1" value="">	
	<input type="hidden" id="KEY2" name="KEY2" value="">	
	<input type="hidden" id="KEY3" name="KEY3" value="">	
	
	<input type="hidden" id="ATTACHMENT_DESC" name="ATTACHMENT_DESC" value="">	
	<input type="hidden" id="ATTACHMENT_NAME" name="ATTACHMENT_NAME" value="">	
	<input type="hidden" id="ATTACHMENT_DATA" name="ATTACHMENT_DATA" value="">	
	<input type="hidden" id="sendTime_Min" name="sendTime_Min" value="">	
	<input type="hidden" id="sendTime_Max" name="sendTime_Max" value="">	
	
	<input type="hidden" id="pageNo" name="pageNo" value="">	
	<input type="hidden" id="pageSize" name="pageSize" value="">	
	
</form>
</div>








<div id="warnDiv" style="display:none">
<table class="MessageBox" align="center" width="380">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt"><br>未找到符合条件的文件</div>
    </td>
  </tr>
</table>
 
<br>
<div align="center">
  <input type="button" value="返回" class="BigButton" onclick="location='<%=contextPath %>/core/funcs/personfolder/personFolderQuery.jsp?seqId=<%=seqId %>'">
</div>


</div>



</body>
</html>
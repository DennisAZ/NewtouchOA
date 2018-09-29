<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqIdStr = request.getParameter("sortId");
	int seqId=0;
	if(seqIdStr!=""){
	  seqId=Integer.parseInt(seqIdStr);
	}

%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
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
<script type="text/javascript">
var requestURL="<%=contextPath%>/yh/core/funcs/filefolder/act/YHFileContentAct";
function doInit(){
	var sortId='<%=seqId%>';
	var subject='<%=request.getParameter("subject")%>';
	var contentNo='<%=request.getParameter("contentNo")%>';
	var attachmentDesc='<%=request.getParameter("attachmentDesc")%>';
	var attachmentName='<%=request.getParameter("attachmentName")%>';
	var sendTimeMin='<%=request.getParameter("sendTimeMin")%>';
	var sendTimeMax='<%=request.getParameter("sendTimeMax")%>';
	//alert(subject);

	var url=requestURL+"/queryFileContentInfoById.act?sortId="+sortId+"&subject="+subject+"&contentNo="+contentNo+"&attachmentDesc="+attachmentDesc+"&attachmentName="+attachmentName+"&sendTimeMin="+sendTimeMin+"&sendTimeMax="+sendTimeMax;
	var json = getJsonRs(url);
  if(json.rtState=='0'){
 		var prcJson=json.rtData;
 		if(prcJson.length>0){
 			var table=new Element('table',{ "width":"100%","class":"TableList","align":"center"})
 			.update("<tbody id='tbody'><tr class='TableHeader' style='font-size:10pt'>"
 					+"<td align='center'> 文件名称</td>"
 					+"<td align='center'>附件文件</td>"
 					+"<td align='center'>附件说明</td>"
 					+"<td align='center'>发布时间</td>"
 					+"<td align='center'>操作</td></tr><tbody>");
 			$('listDiv').appendChild(table);

 			for(var i=0;i<prcJson.length;i++){
 				var prcs=prcJson[i];
 				//alert(prcs.equipmentName);
 				var sqlId=parseInt(prcs.seqId);
 				var subject=prcs.subject;
 				var attachmentName=prcs.attachmentName;
 				var attachmentDesc=prcs.attachmentDesc;
 				var sendTime=prcs.sendTime;


 				var tr=new Element('tr',{'width':'95%','class':'TableLine1','font-size':'10pt'});
 				table.firstChild.appendChild(tr);
 				tr.update("<td align='center'>"					
 					+ subject + "</td><td align='center'>"
 					+ attachmentName + "</td><td align='center'>"
 					+ attachmentDesc + "</td><td align='center'>"
 					+ sendTime + "</td><td align='center'>"
 					+ "<a href='#' onclick='modify_Proces(\""+ sqlId +"\")'>编辑 </a>&nbsp;"
 					+ "<a href='#' onclick='delete_Proces(\""+ sqlId +"\")'>删除</a>&nbsp;</td>"
 				);
 				
 			}
 			$("back").show();
 			
 		}
  }
}




</script>



</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath%>/core/funcs/filefolder/images/folder_search.gif" align="absmiddle"><b><span class="Big1"> 0088 - 文件查询结果</span></b><br>
    </td>
    <td align="right" valign="bottom" class="small1">共1条 <script>function goto_page(){var page_no=parseInt(document.getElementById('page_no').value);if(isNaN(page_no)||page_no<1||page_no>1){alert("页数必须为1-1");return;}window.location="/general/file_folder/search.php?SUBJECT=&CONTENT_NO=&KEY1=&KEY2=&KEY3=&ATTACHMENT_DESC=&ATTACHMENT_NAME=&ATTACHMENT_DATA=&SEND_TIME_MIN=&SEND_TIME_MAX=2010-02-05+16%3A12%3A52&SORT_ID=12&FILE_SORT=1&start="+(page_no-1)*10+"&TOTAL_ITEMS=1&PAGE_SIZE=10";} function input_page_no(){if(event.keyCode==13) goto_page();if(event.keyCode<47||event.keyCode>57) event.returnValue=false;}</script><div id="pageArea" class="pageArea">
第<span id="pageNumber" class="pageNumber">1/1</span>页<a href="javascript:;" id="pageFirst" class="pageFirstDisable" title="首页"></a>

  <a href="javascript:;" id="pagePrevious" class="pagePreviousDisable" title="上一页"></a><a href="javascript:;" id="pageNext" class="pageNextDisable" title="下一页"></a>
  <a href="javascript:;" id="pageLast" class="pageLastDisable" title="末页"></a>转到 第 <input type="text" size="3" class="SmallInput" name="page_no" id="page_no" onkeypress="input_page_no()" style='text-align:center;'> 页 <a href="javascript:goto_page();" id="pageGoto" class="pageGoto" title="转到"></a></td>
  </tr>
</table>


<div id="listDiv" align="center"></div>
<br>
<div align="center" id="back" style="display: none">
  <input type="button" value="返回" class="BigButton" onclick="location.href='query.jsp?seqId=<%=seqId%>'">
</div>



</body>
</html>
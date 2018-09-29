<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqId=request.getParameter("seqId");
	String parentId=request.getParameter("parentId");
	if(seqId==null){
  	seqId="";
	}
	if(seqId!=null &&!"".equals(seqId) &&"root".equals(seqId)){
  	seqId="0";
	}
	
	//获取复制、剪切的文件session
	String actionFlagaa = (String) request.getSession().getAttribute("personAction");
	//System.out.println("actionFlag:"+actionFlag);
	if(actionFlagaa==null){
	  actionFlagaa="";
	}
	//获取复制、剪切的文件夹session
	String folderActionFlag = (String) request.getSession().getAttribute("perActionStr");
	String folderSeqId = (String) request.getSession().getAttribute("perFolderSeqId");
	//System.out.println("actionFlag:"+actionFlag);
	if(folderActionFlag==null){
	  folderActionFlag="";
	}
	if(folderSeqId==null){
		folderSeqId="";
	}
	
	
	 String pageIndex = request.getParameter("pageNo");
	  if(pageIndex == null){
	    pageIndex = "0";
	  }
	  String pageSize = request.getParameter("pageSize");
	  if(pageSize == null){
	    pageSize = "20";
	  }
	  String pageAscDesc=request.getParameter("ascDescFlag");
	  String pageField=request.getParameter("field");
		if(pageAscDesc==null || "".equals(pageAscDesc.trim())){
			pageAscDesc="1";
		}
		if(pageField==null || "".equals(pageField.trim())){
			pageField="NAME";
		}
	  
	  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
	  int folderCapacity = loginUser.getFolderCapacity();

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="yh.core.funcs.person.data.YHPerson"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>个人文件柜</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<!-- 文件上传 -->
<link href="<%=cssPath %>/cmp/swfupload.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<!-- <script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script> --> 
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/pagePilot.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript">
//alert("seqId..."+'<%=seqId%>');

var field = '<%=pageField%>'; 
var ascDesc = '<%=pageAscDesc%>';

var pageSize = "<%=pageSize%>";
var totalRecord = 0;
var pageIndex = "<%=pageIndex%>";
var cfgs;

//alert('<%=folderCapacity%>');
var actionFlag='<%=folderActionFlag%>'; 

var requestURL="<%=contextPath%>/yh/core/funcs/personfolder/act/YHPersonFileContentAct";
var requestFolderURL="<%=contextPath%>/yh/core/funcs/personfolder/act/YHPersonFolderAct";
function doInit(){
 
	getFileFolderName();
	var seqIdFlag = '<%=seqId%>';
	if(seqIdFlag == 0){
		$("delFolder").hide();
		$("reNameSubFolder").hide();
		$("cutFolder").hide();
		$("copyFolder").hide();
		//$("reNameSubFolder").hide();
		$("shareFolder").hide();
		//$("paste_sort").hide();		
	}
	
	//var folderActionFlag='<%=folderActionFlag%>'; 
	var folder_SeqId='<%=seqId%>';
	var folderSeqId='<%=folderSeqId%>';
	
	//alert("folder_SeqId:"+folder_SeqId +"  folderSeqId>>"+folderSeqId +"  folderActionFlag>>"+folderActionFlag);
	if(actionFlag=="copy" || actionFlag=="cut"){
		if(folder_SeqId != folderSeqId){
			$("paste_sort").show();	
		}
	}
	if(actionFlag=="copyFile"||actionFlag=="cutFile"){
		if(folder_SeqId != folderSeqId){
			$("paste_file").show();	
		}
	}


	
	//输出列表信息
	var url=requestURL + "/getFileContentInfo.act?seqId=<%=seqId%>&pageNo=" + pageIndex + "&pageSize=" + pageSize  + "&ascDescFlag=" + ascDesc + "&field="+field;
	var json=getJsonRs(url);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	//alert("rsText>>>"+rsText);
	prcsJson=json.rtData;


	if(prcsJson.length>0){
		totalRecord=prcsJson[0].totalRecord;	
		pageIndex = prcsJson[0].pageNo;
		pageSize = prcsJson[0].pageSize;   

		//分页处理
		 cfgs = {
		    dataAction: "",
		    container: "pageInfo",
		    pageSize:pageSize,
		    loadData:loadDataAction,
		    totalRecord:totalRecord,
		    pageIndex:pageIndex
		  };
		//alert("totalRecord>>"+totalRecord +" pageIndex>>>"+pageIndex +"  pageSize>>"+pageSize);	
		 showPage();
	}

	
	if(prcsJson.length>0){
		var length=prcsJson.length;
		var table=new Element('table',{ "width":"100%","class":"TableList","align":"center"});
		var strTable="<tbody id='tbody'><tr class='TableHeader' align='center' style='font-size:10pt'>"
			+"<td nowrap width='30' align='center'>选择</td>"
			+"<td nowrap align='center'><a style='text-decoration: underline;' href='javascript:order_by(\"NAME\")'>文件名称<span id='subjectImg'></span></a></td>"				
			+"<td nowrap align='center'>附件</td>"				
			+"<td nowrap width='120' align='center'><a style='text-decoration: underline;' href='javascript:order_by(\"SENDTIME\")'>发布时间<span id='sendTimeImg'></span></a></td>"				
			+"<td nowrap align='center'><a style='text-decoration: underline;' href='javascript:order_by(\"CONTENTNO\")'>排序号<span id='sortNoImg'></span></a></td>"	;	
		var managePriv=1;					
		if( managePriv=="1"){
			strTable+="<td width='50' align='center'>操作</td>";
		}
		strTable+="</tr><tbody>";
		table.update(strTable);
		//$('listDiv').appendChild(table);
		 $('listDiv').update(table);
			var returnAscDesc = prcsJson[0].ascDesc;  
			var returnOrderBy = prcsJson[0].orderBy ; 
			field=returnOrderBy;
			ascDesc=returnAscDesc;
			//alert("returnAscDesc>>"+returnAscDesc);
			 if(returnOrderBy == "NAME"){
				  if(returnAscDesc == "0"){				
						   $("subjectImg").innerHTML="<img id='subjectImg' src='<%=imgPath%>/arrow_up.gif'>";
						}else {
							$("subjectImg").innerHTML="<img id='subjectImg' src='<%=imgPath%>/arrow_down.gif'>";
						}
				}
			 checkShunXu();

		for(var i=0;i<prcsJson.length;i++){
			var prcs=prcsJson[i];
			//alert(prcs.sortName);
			var contentId=prcs.contentId;
			var sortId=prcs.sortId;
			var subject=prcs.subject;
			var content=prcs.content;
			var sendTime=prcs.sendTime;
			var attachmentId=prcs.attachmentId;			
			var attachmentNames=prcs.attachmentName;
			var attachmentDesc=prcs.attachmentDesc;
			var userId=prcs.userId;
			var contentNo=prcs.contentNo;
			var newPerson=prcs.newPerson;
			var readers=prcs.readers;
			var creater=prcs.creater;
			var logs=prcs.logs;

			var imgStr="";
			if(attachmentNames!=""){
				imgStr="<img src='"+ contextPath +"/core/funcs/netdisk/images/defaut.gif' >";
			}
			
			var arry=attachmentNames.split("*");
			var attachmentName="";
			for(var k=0;k<arry.length-1;k++){
				//alert(k+":"+arry[k]);
				attachmentName+=arry[k]+"<br>";
			}
			var attachmentNameString = "";
			
			var arryAttId = attachmentId.split(",");			
			//var attachmentIdStr = "";							
		
			var hiddenDiv="<input type = 'hidden' id='returnAttId_" + i + "' name='returnAttId_" +i + "'></input>"
									+ "<input type = 'hidden' id='returnAttName_" + i + "' name='returnAttName_" + i + "'></input>"
									+ "<div id='attr_" + i + "'></div>";

			var spanCount=5;
			var className = (i % 2 == 0) ? "TableLine1" : "TableLine2";  										
			var tr=new Element('tr',{'width':'90%','class': className ,'font-size':'10pt'});			
			table.firstChild.appendChild(tr);	
			var str = "<td align='center'>"					
		  + "<input type='checkbox' name='email_select' id='email_select' onClick='check_one(this)' value="+contentId  +" ></td><td align='left'>"
			+ "<a href='<%=contextPath%>/core/funcs/personfolder/personRead.jsp?sortId=<%=seqId%>&contentId="+contentId +"'> " 
			+ subject + "</a></td><td align='left'>"					
			+ hiddenDiv + "</td><td align='center'>"					
			+ sendTime + "</td><td align='center'>"					
			+ contentNo + "</td>";
			if (managePriv=="1") {
				//alert("managePriv:"+managePriv);
				spanCount=6
				str+="<td align='center'>"					
					+ "<a href='<%=contextPath%>/core/funcs/personfolder/edit.jsp?seqId=<%=seqId%>&contentId="+contentId +"'> 编辑</a>&nbsp;&nbsp;";
				str += "</td>"	;	
			}
		
			tr.update(str);
			$("returnAttId_"+i).value= attachmentId;
			$("returnAttName_"+i).value= attachmentNames;
			//attachMenuUtil("attr_"+i,"file_folder",null,$('returnAttName_'+i).value ,$('returnAttId_'+i).value,false, i,"","","","",contentId);
			var  selfdefMenu = {
	        	office:["downFile","dump","read","edit"], 
		        img:["downFile","dump","play"],  
		        music:["downFile","dump","play"],  
				    video:["downFile","dump","play"], 
				    others:["downFile","dump"]
					}
			attachMenuSelfUtil("attr_"+i,"file_folder",$('returnAttName_'+i).value ,$('returnAttId_'+i).value, i,'',contentId,selfdefMenu);


			
		}

		var tr1=new Element('tr',{'class':'TableControl'});			
		table.lastChild.appendChild(tr1);
		tr1.update("<td align='left' colspan='" + spanCount +" '>"
			+	"&nbsp;"				
		  + "<input type='checkbox' name='allbox' id='allbox' onClick='check_all();'><label for='allbox' style='cursor:pointer'>全选</label>&nbsp;&nbsp;"
		  + "<a href='javascript:do_action(\"resend\");' id='sign'><img src='<%=imgPath%>/movetofolder.gif' align='center' border='0' title='将选中的文件转发给指定的人员'>转发</a>&nbsp;&nbsp;"
			+ "<a  href='javascript:do_action(\"copyFile\")' id='copyFile'><img src='<%=contextPath %>/core/funcs/filefolder/images/copy.gif' align='center' border='0' title='复制所选文件'>复制</a>&nbsp;&nbsp;"
			+ "<a href='javascript:do_action(\"cutFile\")' id='cutFile'><img src='<%=contextPath %>/core/funcs/filefolder/images/cut.gif' align='center' border='0' title='剪切所选文件'>剪切</a>&nbsp;&nbsp;"
			+ "<a href='javascript:delete_arrang();' id='delFile'><img src='<%=contextPath %>/core/funcs/filefolder/images/delete.gif' align='center' border='0' title='删除所选文件'>删除</a>&nbsp;&nbsp;"
			+ "<a href='javascript:do_action(\"downFile\");' id='downFile' ><img src='<%=imgPath%>/download.gif' align='center' border='0' title='批量压缩后下载'><span id='label_down'>下载</span>&nbsp;&nbsp;</a>"
		  +"</td>"
		);

		
		
	}else{
		$("queryFile").hide();
		warnDiv();
	}
	
}

function order_by(fieldtemp){
	//alert(field);
	//var field="sentdk";
	if(field==fieldtemp) {
	  if(ascDesc == '1'){
	     ascDesc = '0';
	   }else{
	     ascDesc = '1';
	   }
	}else {
	  field = fieldtemp;
	  ascDesc = '1';
	}
	///alert("field>>"+field);
	sortAction();
}
function sortAction(){
	doInit();
	//checkShunXu();
}

function checkShunXu(){
	if(field=='NAME'){
		  if(ascDesc=='1') {
			   $("subjectImg").innerHTML="<img id='subjectImg' src='<%=imgPath%>/arrow_down.gif'>";
			}else {
				$("subjectImg").innerHTML="<img id='subjectImg' src='<%=imgPath%>/arrow_up.gif'>";
			}
	}else if(field=='SENDTIME'){
		 if(ascDesc=='1') {
			 $("sendTimeImg").innerHTML="<img id='sendTimeImg' src='<%=imgPath%>/arrow_down.gif'>";
			 
		 }else {
		 		$("sendTimeImg").innerHTML="<img id='sendTimeImg' src='<%=imgPath%>/arrow_up.gif'>";
		 }
	}else if(field=='CONTENTNO'){

		  if(ascDesc=='1') {
			   $("sortNoImg").innerHTML="<img id='sortNoImg' src='<%=imgPath%>/arrow_down.gif'>";
			}else {
				$("sortNoImg").innerHTML="<img id='sortNoImg' src='<%=imgPath%>/arrow_up.gif'>";
			}
		
	}
}





function warnDiv(){
	var table=new Element('table',{ "width":"300","class":"MessageBox","align":"center"})
		.update("<tr >"
			+"<td align='center' class='msg info'><div class='content' style='font-size:12pt'>该文件夹尚无文件</div></td>"
			+"</tr>");
	$('nothingDiv').appendChild(table);
}

function getFileFolderName(){
	var url = requestFolderURL + "/getSortNameById.act?seqId=<%=seqId%>";
	var json=getJsonRs(url);
	//alert("根目录rsText>>:"+rsText);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	prcsJson=json.rtData;	
	
	var sortParentStr = json.rtMsrg;
	document.getElementById("sortParentStr").value = sortParentStr;
	//alert("sortParentStr>>>"+sortParentStr);
	if(prcsJson.length>0){
		var prcs=prcsJson[0];
		if(prcs.sortName){
			if(prcs.sortName.length>40){
				nameStr = prcs.sortName.substr(0,40)+ "……";
				$("sortName").title = prcs.sortName;
			}else{
				nameStr = prcs.sortName;
			}
			$("sortName").innerHTML=nameStr;
      		
		}else{				
			$("sortName").innerHTML="根目录";
		}
	}
}

var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;


window.onload = function() {	
	doInit();
	
	var fileLimit=treeSize();
	//alert("fileLimit>>"+fileLimit);

	//if(fileLimit<=0){
	//  fileLimit=1;
	//}
	//alert(fileLimit);
  var linkColor = document.linkColor;
  var settings = {
    flash_url : "<%=contextPath %>/core/cntrls/swfupload.swf",
    upload_url: requestURL+"/uploadFile.act?seqId=<%=seqId%>",
    post_params: {"PHPSESSID" : "<%=session.getId()%>"},
    file_size_limit : fileLimit + " B",
    file_types : "*.*",
    file_types_description : "All Files",
    file_upload_limit : 100,
    file_queue_limit : 0,
    custom_settings : {
      uploadRow : "fsUploadRow",
      uploadArea : "fsUploadArea",
      progressTarget : "fsUploadProgress",	//上传处理
      startButtonId : "btnStart",						//开始上传
      cancelButtonId : "btnCancelBatchFile"  				//全部取消
    },
    debug: false,

    button_image_url: "<%=imgPath %>/uploadx4.gif",
    button_width: "65",
    button_height: "29",
    button_placeholder_id: "spanButtonPlaceHolder",
    button_text: '<span class=\"textUpload\">批量上传</span>',
    button_text_style: ".textUpload{color:" + linkColor + ";}",
    button_text_top_padding : 1,
    button_text_left_padding : 18,
    button_placeholder_id : "spanButtonUpload",
    button_width: 70,
    button_height: 18,
    button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
    button_cursor: SWFUpload.CURSOR.HAND,
    
    file_queued_handler : fileQueued,
    file_queue_error_handler : fileQueueError,
    file_dialog_complete_handler : fileDialogComplete,
    upload_start_handler : uploadStart,
    upload_progress_handler : uploadProgress,
    upload_error_handler : uploadError,
    upload_success_handler : uploadSuccess,
    upload_complete_handler : uploadComplete,
    queue_complete_handler : queueComplete
  };

  swfupload = new SWFUpload(settings);
}

//全选var selected_count = 0;
function check_all(){  
	var all =  $("allbox");
  var file_list =document.getElementsByName("email_select");
  //alert(file_list.length);
  if(!file_list)	{
		return;
	}
	for(var i = 0; i < file_list.length; i++)	{
		if(all.checked)		{
			file_list.item(i).checked = true;
		}	else{
			file_list.item(i).checked = false;
		}
	}
	if(all.checked && file_list.length > 1){
		//label_down.innerText = "批量下载";
		$(label_down).update("批量下载");
		selected_count = file_list.length;
	}	else{
		//label_down.innerText = "下载";
		$(label_down).update("下载");
		selected_count = 0;
	}
}

//单选function check_one(el){	
	if(!el.checked)	{
		$("allbox").checked = false;
	}
	if(!el.checked && selected_count > 0)	{
		selected_count--;
	}	else{
		selected_count++;
	}
	//alert(selected_count);
	if(selected_count > 1){
		//label_down.innerText = "批量下载";
		$(label_down).update("批量下载");
	}	else{
		//label_down.innerText = "下载";
		$(label_down).update("下载");
	}
}
//获取选项
function get_checked(){
  checked_str="";
  for(i=0;i<document.getElementsByName("email_select").length;i++){
    el=document.getElementsByName("email_select").item(i);
    if(el.checked){  
      val=el.value;
      checked_str+=val + ",";
      //alert("val:"+val);
    }
  }
  if(i==0) {
    el=document.getElementsByName("email_select");
    if(el.checked){  
      val=el.value;
      checked_str+=val + ",";
    }
  }
  return checked_str;
}
function delete_arrang(){
	var delStr=get_checked();
	//alert("del:"+delStr);
	if(delStr==""){
		alert("请至少选择一个文件");
		return;
	}
	msg="确定要删除选择文件吗？这将不可恢复！"
	if(window.confirm(msg)){
		var url=requestURL + "/delCheckedFile.act?seqIdStr="+delStr;
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


function delete_sort(seqId){
	var msg="确定要删除该文件夹吗？这将删除该文件夹中的所有文件和子文件夹，且不可恢复！";
	if(window.confirm(msg)){
		var url=requestFolderURL + "/delFileSortInfoById.act";
		
		//alert(url);
  	var json=getJsonRs(url, 'seqId=' + seqId);
    if(json.rtState == '0'){
    	var curTree = parent.frames["file_tree"].tree;  		
  		curTree.removeNode(seqId);
  		//history.back(); 
  		var sortParentStr = $("sortParentStr").value;
  		window.location.href = "<%=contextPath%>/core/funcs/personfolder/folder.jsp?seqId=" + sortParentStr;
    }else{
			alert(json.rtMsrg);
    }
	}
}



//对文件夹操作
function sort_action(action){
	//alert(action);
	var folderId='<%=seqId%>';
	//document.getElementById("paste_file").style.display='none';		
		$("paste_file").hide();
		//$("paste_sort").show();
	if(action=="copy"){
		var url=requestFolderURL+"/copyFolderById.act?folderId="+folderId+"&action="+action;
		var json=getJsonRs(url);
    if(json.rtState == '0'){
    	alert("选择的文件夹已“复制”\n请到目标目录中进行“粘贴”操作");  
    	location.reload(); 	  	
    }else{
			alert(json.rtMsrg);
    }		
		
	}else{
		var url=requestFolderURL+"/copyFolderById.act?folderId="+folderId+"&action="+action;
		var json=getJsonRs(url);
    if(json.rtState == '0'){
    	alert("选择的文件夹已“剪切”\n请到目标目录中进行“粘贴”操作");
    	location.reload();
    }else{
			alert(json.rtMsrg);
    }
		
	}
	
}

function paste_sort(){
	//alert('<%=seqId%>');
	var url=requestFolderURL+"/pasteFolder.act?sortParent=<%=seqId%>";
	//alert(url);
	var rtJson=getJsonRs(url);
	//alert(rsText);
  if(rtJson.rtState == '0'){
  	prcsJson = rtJson.rtData;
  	var action=prcsJson.action;
  	var nullFlag = prcsJson.nullFlag;
  	if(nullFlag!="1"){
  		if(action=="copy"){
    		add_TreeNode(prcsJson);
  			
      }
      if(action=="cut"){
  			var nodeId=prcsJson.seqId;
  			var curTree = parent.frames["file_tree"].tree;  		
    		curTree.removeNode(nodeId);
    		add_TreeNode(prcsJson);  		
      }
  	}else{
  		location.reload();
    }
  	  	
  }else{
		alert(json.rtMsrg);
  }	
  	
}

function add_TreeNode(prcsJson){
	var curTree = parent.frames["file_tree"].tree;
	var curNode = curTree.getCurrNode();
	var nodeId = prcsJson.nodeId;
	var isHaveChild = prcsJson.isHaveChild;
	var nodeName = prcsJson.sortName;
 	var imgAddress = "<%=imgPath%>/dtree/folder.gif";
	var node = {
			parentId:curNode.nodeId,
			nodeId:nodeId,
			name:nodeName,
			isHaveChild:isHaveChild,
			extData:'',
			imgAddress:imgAddress
	}
	curTree.addNode(node);
	location.reload();
}








//对文件操作
function do_action(action){
	//alert(action);
	var selects=get_checked();
	var idStr=selects.split(",");
	var count=idStr.length-1;
	//alert("count:"+count);
	if(count <= 0){
		alert("请至少选择一个文件");
		return ;
	}
	switch(action){
	case "copyFile":
	case "cutFile":
		$("paste_sort").hide();
		//$("paste_file").show();
		if(action == "copyFile"){
			
			var url=requestURL+"/copyFileByIds.act?perFolderSeqId=<%=seqId%>&seqIdStrs="+selects+"&action=copyFile";
			//alert(url);
			var json=getJsonRs(url);
	    if(json.rtState == '0'){
	    	alert("选择的文件已“复制”\n请到目标目录中进行“粘贴”操作"); 	
	    }else{
				alert(json.rtMsrg);
	    }
		
		}else{			
			var url=requestURL+"/copyFileByIds.act?seqIdStrs="+selects+"&action=cutFile";
			//alert(url);
			var json=getJsonRs(url);
	    if(json.rtState == '0'){
	    	alert("选择的文件已“剪切”\n请到目标目录中进行“粘贴”操作");
	    }else{
				alert(json.rtMsrg);
	    }
		}
		break;

	case "resend":  //转发
		var forwardURL="<%=contextPath%>/core/funcs/personfolder/forward.jsp?contentId=" + selects;
		newWindow(forwardURL, 550,250);
		break;

	case "downFile": //下载

		if(count>1 && window.confirm("一次下载多个文件需要在服务器上做压缩处理，会占用较多服务器CPU资源，确定继续下载吗？\n该操作请不要下载超过128MB的大文件")){
			location.href = "<%=contextPath%>/yh/core/funcs/filefolder/act/YHFileContentAct/batchDownload.act?contentIdStr=" + selects + "&name=";
		}else if(count == 1){
			location.href = "<%=contextPath%>/yh/core/funcs/filefolder/act/YHFileContentAct/batchDownload.act?contentIdStr=" + selects + "&name=";
		}
		break;

	
		/**
		if(window.confirm("一次下载多个文件需要在服务器上做压缩处理，会占用较多服务器CPU资源，确定继续下载吗？\n该操作请不要下载超过128MB的大文件")){
			var downFileUrl=requestURL + "/downFile.act?contentId=" + selects;
			var json=getJsonRs(downFileUrl);
			if(json.rtState == '0'){
		    	var prcs=json.rtData;

		    	var attIdArry=prcs.attIdArry;
		    	var attNamedArry=prcs.attNamedArry;
		    	var counterNum=prcs.counter;
		    	if(counterNum == 1){
		    		 downLoadFile(attNamedArry,attIdArry,"file_folder");
						
			    }else{
			    	batchDownload(attNamedArry,attIdArry,"file_folder","文件附件下载");
				  }
		    		
		    }else{
					alert(json.rtMsrg);
		    }
		}  **/
	
	}
	
}

function paste_File(){
	var url=requestURL + "/pasteFile.act?sortId=<%=seqId%>";
	//alert(url);
	var rtJson=getJsonRs(url);
	//alert(rtText);
  if(rtJson.rtState == '0'){
 		//alert(rtJson.rt);	
 		window.location.reload();
    
  }
}


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
  //alert("pageNo.F>>"+pageNo +"  pageSize>>"+pageSize);
  window.location.href ="folder.jsp?seqId=<%=seqId%>&pageNo=" + pageNo + "&pageSize=" + pageSize + "&ascDescFlag=" + ascDesc + "&field="+field;
}

//浮动菜单文件的删除
function deleteAttachBackHand(attachName,attachId,attrchIndex){
	var url = requestURL + "/updateAttchNameById.act?contentId=" + attrchIndex +"&attId=" + attachId + "&attName=" + attachName;
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

function newWindow(url, width, height){
  var loc_x=(screen.availWidth-width)/2;
  var loc_y=(screen.availHeight-height)/2;
  window.open(url,"folder","height="+height+",width="+width+",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+loc_y+",left="+loc_x+",resizable=yes");
}


function treeSize() {
	var folderCapacity='<%=folderCapacity%>';
  var flag=0;
  var requestUrl = contextPath + "/yh/core/funcs/personfolder/act/YHFolderSizeAct";
  var url = requestUrl + "/getFolderSize.act";
  var json = getJsonRs(url);
  if (json.rtState == '0'){
    flag = json.rtData.fileSize;   
  }
  //alert(flag);
  if(folderCapacity == 0){
	  flag=0;
  }else{
	  if(flag<=0){
		  flag=1;
		}
	}
	
  return flag;
}


</script>
</head>
<body >

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/filefolder/images/notify_open.gif" align="middle"><b>&nbsp;<span class="Big1" id="sortName"> </span></b><br>
   </td>
   </tr>
   <tr>
    <td id="pageDiv" align="right" valign="bottom" class="small1">
	   <div id="pageInfo" style="float:right;"></div>
  	</td>
  </tr>
</table>

<div id="listDiv" align="center"></div>
<div id="nothingDiv" align="center"></div>

<br>
<form name="form1" id="form1" action="" method="post" enctype="multipart/form-data" >
<input type="hidden" name="sortParentStr" id="sortParentStr" value="" >

<table>
  <tr id="">
    <td colspan="3">
	    <div id="fsUploadArea" class="flash" style="width:380px;">
		  <div id="fsUploadProgress"></div>
			<div id="totalStatics" class="totalStatics"></div>
			<div>				
			  <input type="button" id="btnStart" class="SmallButtonW" value="开始上传" onclick="swfupload.startUpload();" >&nbsp;&nbsp;
				<input type="button" id="btnCancelBatchFile" class="SmallButtonW" value="全部取消" onclick="swfupload.cancelQueue();" >&nbsp;&nbsp;
				<input type="button" class="SmallButtonW" value="刷新页面" onclick="window.location.reload();">
		  </div>
		 </div>
	 </td>
  </tr>
</table>
</form>  

<br>
<table class="TableBlock" width="100%" align="center">
  <tr>
    <td class="TableContent" nowrap align="center" width="80"><b>文件操作：</b></td>
    <td class="TableControl">&nbsp;
    	<a class="ToolBtn" id='queryFile' style='display:' href="<%=contextPath %>/core/funcs/personfolder/personFolderQuery.jsp?seqId=<%=seqId %>&FILE_SORT=1&SORT_ID=11" title="查询文件"><span>查询</span></a>
			<a class="ToolBtn" style="display: " href="<%=contextPath %>/core/funcs/filefolder/globalQuery.jsp" title="全局搜索"><span>全局搜索</span></a>
   		<span id="paste_file" style="display:none" title="粘贴文件"><a class="ToolBtn" href="javascript:paste_File()" ><span>粘贴</span></a></span>
   		<a href="<%=contextPath%>/core/funcs/personfolder/new/newFile.jsp?seqId=<%=seqId %>" class="ToolBtn" title="创建新的文件"><span>新建文件</span></a>
   		<a class="ToolBtn" href="javascript: void(0)"><span><span id="spanButtonUpload" title="批量上传"></span></span></a>
 		</td>
  </tr>
  <tr>
    <td class="TableContent" nowrap align="center" width="80"><b>文件夹操作：</b></td>
    <td class="TableControl">&nbsp;
   		<span  id="paste_sort" style="display: none;" title="粘贴文件夹" ><a href="javascript:paste_sort()"><img src="<%=contextPath %>/core/funcs/filefolder/images/paste.gif" align="middle" border="0">&nbsp;粘贴</a>&nbsp;&nbsp;</span>
   		<a  href="<%=contextPath%>/core/funcs/personfolder/new/newFolder.jsp?seqId=<%=seqId %>" title="新建文件夹" id="newSubFolder" ><img src="<%=imgPath%>/newfolder.gif" align="middle" border="0">&nbsp;新建子文件夹&nbsp;&nbsp;</a>
   		<a style="display: " href="<%=contextPath%>/core/funcs/personfolder/setPriv/index.jsp?seqId=<%=seqId %>&parentId=<%=parentId %>" title="共享此文件夹" id="shareFolder"><img src="<%=imgPath%>/endnode_share.gif" align="middle" border="0">&nbsp;共享此文件夹&nbsp;&nbsp;</a>
   		<a style="display: " href="<%=contextPath%>/core/funcs/personfolder/editSubFolder.jsp?seqId=<%=seqId %>" title="重命名此文件夹" id="reNameSubFolder"><img src="<%=contextPath %>/core/funcs/filefolder/images/folder_edit.gif" align="middle" border="0">&nbsp;重命名&nbsp;&nbsp;</a>
   		<a style="display: " href="javascript:sort_action('copy');" id="copyFolder"><img src="<%=contextPath %>/core/funcs/filefolder/images/copy.gif" align="middle" border="0" title="复制当前文件夹" >&nbsp;复制&nbsp;&nbsp;</a>
   		<a style="display: " href="javascript:sort_action('cut');" id="cutFolder"><img src="<%=contextPath %>/core/funcs/filefolder/images/cut.gif" align="middle" border="0" title="剪切当前文件夹">&nbsp;剪切&nbsp;&nbsp;</a>
  		<a style="display: " href="javascript:delete_sort('<%=seqId %>');" title="删除此文件夹" id="delFolder"><img src="<%=contextPath %>/core/funcs/filefolder/images/delete.gif" align="middle" border="0">&nbsp;删除目录&nbsp;&nbsp;</a>   		
   		
    </td>
  </tr>
</table>

</body>
</html>
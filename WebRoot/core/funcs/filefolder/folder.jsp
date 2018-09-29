<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqId=request.getParameter("seqId");
	String parentId=request.getParameter("parentId");
	if(seqId==null){
  	seqId="";
	}	
	//获取复制、剪切的session
	String actionFlag = (String) request.getSession().getAttribute("folderActionStr");
	String folderSeqId = (String) request.getSession().getAttribute("folderSeqId");
	if(actionFlag==null){
	  actionFlag="";
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
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="yh.core.funcs.person.data.YHPerson"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件柜</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/orgselect.js"></script>
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
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;
var seqId='<%=seqId%>';

var field = '<%=pageField%>'; 
var ascDesc = '<%=pageAscDesc%>';

var pageSize = "<%=pageSize%>";
var totalRecord = 0;
var pageIndex = "<%=pageIndex%>";
var cfgs;
var actionFlag='<%=actionFlag%>';
var requestURL="<%=contextPath%>/yh/core/funcs/filefolder/act/YHFileContentAct";
var requestURL1="<%=contextPath%>/yh/core/funcs/system/filefolder/act/YHFileSortAct";
function doInit(){
	getSysRemind("smsRemindDiv","",16);
	moblieSmsRemind("sms2RemindDiv","",16);	
	$('newSubFolder').hide();
	$('reNameSubFolder').hide();
	$('copyFolder').hide();
	$('cutFolder').hide();
	$('paste_sort').hide();
	$('delFolder').hide();
	$('setFolderPriv').hide();
	
	var folder_SeqId='<%=seqId%>';
	var folderSeqId='<%=folderSeqId%>';
	if(actionFlag=="copy" || actionFlag=="cut"){
		if(folder_SeqId != folderSeqId){
			$("paste_sort").show();	
		}
	}
	var url1=requestURL1 + "/getSortNameById.act?seqId=<%=seqId%>";
	var json=getJsonRs(url1);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	prcsJson=json.rtData;	
	var sortParentStr = json.rtMsrg;
	document.getElementById("sortParentStr").value = sortParentStr;
	if(prcsJson.length>0){
		var prcs=prcsJson[0];
		var nameStr = "";
		if(prcs.sortName){
			if(prcs.sortName.length>40){
				nameStr = prcs.sortName.substr(0,40)+ "……";
				$("sortName").title = prcs.sortName;
			}else{
				nameStr = prcs.sortName;
			}
			$("sortName").innerHTML=nameStr;
		}
	}
	//getPrivate();
//获取权限信息
		var visiPriv;
		var managePriv;
		var delPriv;
		var newPriv;
		var downPriv;
		var ownerPriv;
		var downUserPriv;
		var sortParent;
    
		var seqId;
		var url2=requestURL1 + "/getPrivteById.act?seqId=<%=seqId%>";
		var json=getJsonRs(url2);
		if(json.rtState =='1'){
			alert(json.rtMsrg);
			return ;
		}
		prcsJson=json.rtData;
		var rtMsrg = json.rtMsrg;
		if(prcsJson.length>0){
			var prcs=prcsJson[0];
			visiPriv=prcs.visiPriv;
			managePriv=prcs.managePriv;
             delPriv = prcs.delPriv;
			newPriv=prcs.newPriv;
			downPriv=prcs.downPriv;
			ownerPriv=prcs.ownerPriv;
			downUserPriv=prcs.downUserPriv;
			sortParent=prcs.sortParent;
			seqId=prcs.seqId;
			//alert("visiPriv:"+visiPriv+" managePriv:"+managePriv+"  newPriv:"+newPriv+"  downPriv:"+downPriv+"  ownerPriv:"+ownerPriv+"  sortParent:"+sortParent+"  seqId:"+seqId+"  downUserPriv:"+downUserPriv);
			if(sortParent=="0"){	//如果不根目录
				if(newPriv=="1"){
					$('newSubFolder').show();
					$('newFile').show();
					$('battUpload').show();
				}
				if(ownerPriv=="1"){
					$('setFolderPriv').show();
				}
			}else{
				if(newPriv=="1"){
					$('newSubFolder').show();
					$('newFile').show();
					$('battUpload').show();
				}
				if(ownerPriv=="1"){
					$('setFolderPriv').show();
				}
                 var prcs = getParentPriv(sortParent);
				var parentManagePriv = prcs.managePriv;
				if(managePriv=="1"){
					$('reNameSubFolder').show();
					$('copyFolder').show();
					$('cutFolder').show();					
				}
                var parentDelPriv = prcs.delPriv;
				if(parentDelPriv!="0" && parentDelPriv==delPriv){
				  $('delFolder').show();
				}
			}
		}
		if(actionFlag=="copyFile"||actionFlag=="cutFile"){
			if(folder_SeqId != folderSeqId && newPriv=="1"){
				$("paste_file").show();	
			}
		}
	//输出列表信息
	var url=requestURL + "/getFileContentInfo.act?seqId=<%=seqId%>&pageNo=" + pageIndex + "&pageSize=" + pageSize + "&ascDescFlag=" + ascDesc + "&field="+field;
	var json=getJsonRs(url);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
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
		 showPage();
	}
	if(prcsJson.length>0){
		var length=prcsJson.length;		
		var table=new Element('table',{ "width":"100%","class":"TableList","align":"center"});
		var strTable="<tbody id='tbody'><tr class='TableHeader' align='center' style='font-size:10pt'>"
			+"<td nowrap width='30' align='center'>选择</td>"
			+"<td nowrap align='center'><a style='text-decoration: underline;' href='javascript:order_by(\"NAME\")'>文件名称<span id='subjectImg'></span></a></td>"				
			+"<td nowrap width='150' align='center'>附件</td>"				
			+"<td nowrap width='120' align='center'><a style='text-decoration: underline;' href='javascript:order_by(\"SENDTIME\")'>发布时间<span id='sendTimeImg'></span></a></td>"				
			+"<td nowrap align='center'><a style='text-decoration: underline;' href='javascript:order_by(\"CONTENTNO\")'>排序号<span id='sortNoImg'></span></a></td>"	;						
		if(managePriv=="1"){
			strTable+="<td width='150' align='center'>操作</td>";
		}
		strTable+="</tr><tbody>";
		table.update(strTable);
		$('listDiv').update(table);
		var returnAscDesc = prcsJson[0].ascDesc;  
		var returnOrderBy = prcsJson[0].orderBy ; 
		field=returnOrderBy;
		ascDesc=returnAscDesc;
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
			var arrtImgStr="";
			var arry=attachmentNames.split("*");
			var attachmentName="";
			for(var k=0;k<arry.length-1;k++){
				attachmentName+=arry[k]+"<br>";
			}
			var attachmentNameString = "";
			var arryAttId = attachmentId.split(",");			
			var attachmentIdStr = "";							
			for (var t=0 ; t<arryAttId.length ; t++) {
				if (arryAttId[t]) {			
				  var imgType=getFileType(arry[t]);
				  var imgName=getImage(imgType);
				  arrtImgStr="<img src=\"" + "<%=imgPath%>" + "/fileExt/" + imgName + ".gif\" style=\"width:12px;height:12px\" title=\"" + arry[t] + "\">&nbsp;";
				  attachmentNameString += arrtImgStr+ arry[t] + "<br>";
				 }
			}
			var hiddenDiv="<input type = 'hidden' id='returnAttId_" + i + "' name='returnAttId_" +i + "'></input>"
									+ "<input type = 'hidden' id='returnAttName_" + i + "' name='returnAttName_" + i + "'></input>"
									+ "<div id='attr_" + i + "'></div>";
			var readImg="";
			if(readers == 0){
				readImg = "<img src='<%=imgPath%>/email.png' align='absMiddle' title='未签阅'>";
				hiddenDiv=attachmentNameString;
			}						
			var spanCount=5;
			var className = (i % 2 == 0) ? "TableLine1" : "TableLine2";  										
			var tr=new Element('tr',{'width':'90%','class': className ,'font-size':'10pt'});			
			table.firstChild.appendChild(tr);	
			var str = "<td align='center'>"					
		  + "<input type='checkbox' name='email_select' id='email_select' onClick='check_one(this)' value="+contentId  +" ></td><td align='left'>"
			+ "<a href='<%=contextPath%>/core/funcs/filefolder/read.jsp?sortId=<%=seqId%>&contentId="+contentId +"'> "
			+ readImg + subject + "</a></td><td align='left'>"					
			+ hiddenDiv + "</td><td align='center'>"					
			+ sendTime + "</td><td align='center'>"					
			+ contentNo + "</td>";
			if (managePriv=="1") {
				spanCount=6
				str+="<td align='center'>"					
					+ "<a href='"+ contextPath +"/core/funcs/filefolder/edit.jsp?seqId="+seqId +"&contentId="+contentId +"'> 编辑&nbsp;&nbsp;</a>" + " <a href='#' onClick='openSignWindow("+sortId+" , "+ contentId+ ")'>签阅情况&nbsp;</a>";
				str += "</td>"	;	
			}
			tr.update(str);
			if(readers != 0){
				$("returnAttId_"+i).value= attachmentId;
				$("returnAttName_"+i).value= attachmentNames;
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
				attachMenuSelfUtil("attr_"+i,"file_folder",$('returnAttName_'+i).value ,$('returnAttId_'+i).value, i,'',contentId,selfdefMenu);
			}
		}
		var tr1=new Element('tr',{'class':'TableControl'});			
		table.lastChild.appendChild(tr1);
		tr1.update("<td align='left' colspan='" + spanCount +" '>"
			+	"&nbsp;"				
		  + "<input type='checkbox' name='allbox' id='allbox' onClick='check_all();'><label for='allbox' style='cursor:pointer'>全选</label>&nbsp;&nbsp;"
		  + "<a href='javascript:do_action(\"sign\");' id='sign'><img src='<%=contextPath %>/core/funcs/filefolder/images/email_open.gif' align='center' border='0' title='签阅文件'>签阅&nbsp;&nbsp;</a>"
			+ "<a href='javascript:do_action(\"copyFile\")' id='copyFile'><img src='<%=contextPath %>/core/funcs/filefolder/images/copy.gif' align='center' border='0' title='复制所选文件'>复制&nbsp;&nbsp;</a>"
			+ "<a href='javascript:do_action(\"cutFile\")' id='cutFile'><img src='<%=contextPath %>/core/funcs/filefolder/images/cut.gif' align='center' border='0' title='剪切所选文件'>剪切&nbsp;&nbsp;</a>"
			+ "<a href='javascript:delete_arrang();' id='delFile'><img src='<%=contextPath %>/core/funcs/filefolder/images/delete.gif' align='center' border='0' title='删除所选文件'>删除&nbsp;&nbsp;</a>"
			+ "<a href='javascript:do_action(\"downFile\");' id='downFile' ><img src='<%=imgPath%>/download.gif' align='center' border='0' title='批量压缩后下载'><span id='label_down'>下载</span>&nbsp;&nbsp;</a>"
		  +"</td>"
		);		
		$('cutFile').hide();
		$('delFile').hide();
		if(managePriv=="1"){
			$('cutFile').show();
			//$('delFile').show();			
		}
        if (delPriv == '1') {
          $('delFile').show();
        }
	}else{
		if(rtMsrg == "1"){
			$("noPrivDiv").show();
			$("headTableStr").hide();
			$("fileTable").hide();
		}else{
			$("queryFile").hide();
			warnDiv();
		}
	}	
}

function order_by(fieldtemp){
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
	sortAction();
}
function sortAction(){
	doInit();
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

//取得文件对应的类型图片function getImage(extName){
  var imgArray = ["ai", "avi","bmp","cs", "dll", "doc", "docx","exe", "fla", "gif", "htm", "html"
                  , "jpg","js","mdb", "mp3", "pdf", "png","ppt", "pptx", "rar", "rdp", "swf"
                  , "swt","txt","vsd", "xls", "xlsx", "xml","zip","dot" ];
  extName = extName.toLowerCase();
   if(imgArray.contains(extName)){
     return extName;
   }else{
     return "default";
   }
}
function getFileType(fileName){
  var index = fileName.lastIndexOf(".");
  var extName = "";
  if (index >= 0) {
    extName = fileName.substring(index + 1).toLowerCase();
  }
  return extName;
}
function openSignWindow(sortId , contentId) {
  var url = contextPath + "/core/funcs/filefolder/showReader.jsp?sortId=" + sortId + "&contentId=" + contentId;
  window.open(url ,'sign' + contentId ,'menubar=0,toolbar=0,status=0,scrollbars=1,resizable=1');
}
//浮动菜单文件的删除function deleteAttachBackHand(attachName,attachId,attrchIndex){
	var url= requestURL +"/delFloatFile.act?attachId=" + attachId +"&attachName=" + attachName + "&contentId=" + attrchIndex;
	var json=getJsonRs(encodeURI(url));
	if(json.rtState =='1'){
		alert(json.rtMsrg);
		return false;
	}else{
	  prcsJson=json.rtData;
		var updateFlag=prcsJson.updateFlag;
		if(updateFlag){
		  return true;
		}else{
			return false;
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
function getParentPriv(sortParent){
	var parentManagePriv;
	var url2=requestURL1 + "/getPrivteById.act?seqId="+sortParent;
	var json=getJsonRs(url2);
	if(json.rtState =='1'){
		alert(json.rtMsrg);
		return ;
	}
	prcsJson=json.rtData;
	if(prcsJson.length>0){
		var prcs=prcsJson[0];
		return prcs;
	}
	return parentManagePriv;		
}

//全选var selected_count=0;
function check_all(){  
	var all =  $("allbox");
  var file_list =document.getElementsByName("email_select");
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
	if(delStr==""){
		alert("请至少选择一个文件");
		return;
	}
	msg="确定要删除选择文件吗？这将不可恢复！"
	if(window.confirm(msg)){
		var url=requestURL + "/delCheckedFile.act?seqIdStr="+delStr;
		var json=getJsonRs(url);
		if(json.rtState == '1'){
			alert(json.rtMsrg);
			return ;				
		}
		window.location.reload();
	}
}

window.onload = function() {	
	doInit();
  var linkColor = document.linkColor;
  var settings = {
    flash_url : "<%=contextPath %>/core/cntrls/swfupload.swf",
    upload_url: $("form1").action,									//requestURL+"/uploadFile.act?seqId="+seqId+"&smsPerson="+smsPerson,
    post_params: {"PHPSESSID" : "<%=session.getId()%>"},
    file_size_limit : (maxUploadSize + " MB"),
    file_types : "*.*",
    file_types_description : "All Files",
    file_upload_limit : 100,
    file_queue_limit : 0,
    custom_settings : {
      uploadRow : "fsUploadRow",
      uploadArea : "fsUploadArea",
      progressTarget : "fsUploadProgress",	//上传处理
      startButtonId : "btnStart",						//开始上传      cancelButtonId : "btnCancel"  				//全部取消
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

function delete_sort(seqId){
	var msg="确定要删除该文件夹吗？这将删除该文件夹中的所有文件和子文件夹，且不可恢复！";
	if(window.confirm(msg)){
		var url="<%=contextPath%>/yh/core/funcs/filefolder/act/YHFileSortAct/delFileSortInfoById.act";
  	var json=getJsonRs(url, 'seqId=' + seqId);
    if(json.rtState == '0'){
    	var curTree = parent.frames["file_tree"].tree;  		
  		curTree.removeNode(seqId);
  		var sortParentStr = $("sortParentStr").value;
  		window.location.href = "<%=contextPath%>/core/funcs/filefolder/folder.jsp?seqId=" + sortParentStr;
    }else{
			alert(json.rtMsrg);
    }
	}
}

//对文件夹操作
function sort_action(action){
	var folderId=seqId;
	$("paste_file").hide();
	if(action=="copy"){
		var url=requestURL1+"/copyFolderById.act?folderId="+folderId+"&action="+action;
		var json=getJsonRs(url);
    if(json.rtState == '0'){
    	alert("选择的文件夹已“复制”\n请到目标目录中进行“粘贴”操作");   
    	location.reload(); 	
    }else{
			alert(json.rtMsrg);
    }		
	}else{
		var url=requestURL1+"/copyFolderById.act?folderId="+folderId+"&action="+action;
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
	var url=requestURL1+"/pasteFolder.act?sortParent=<%=seqId%>";
	var rtJson=getJsonRs(url);
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
//对文件操作function do_action(action){
	var selects=get_checked();
	var idStr=selects.split(",");
	var count=idStr.length-1;
	if(count <= 0){
		alert("请至少选择一个文件");
		return ;
	}
	switch(action){
	case "copyFile":
	case "cutFile":
		$("paste_sort").hide();
		if(action == "copyFile"){
			var url=requestURL+"/copyFileByIds.act?folderSeqId=<%=seqId%>&seqIdStrs="+selects+"&action="+action;
			var json=getJsonRs(url);
	    if(json.rtState == '0'){
	    	alert("选择的文件已“复制”\n请到目标目录中进行“粘贴”操作"); 	
	    }else{
				alert(json.rtMsrg);
	    }
		}else{			
			var url=requestURL+"/copyFileByIds.act?seqIdStrs="+selects+"&action="+action;
			var json=getJsonRs(url);
	    if(json.rtState == '0'){
	    	alert("选择的文件已“剪切”\n请到目标目录中进行“粘贴”操作");
	    }else{
				alert(json.rtMsrg);
	    }
		}
		break;
	case "sign": //签阅
		var url=requestURL + "/fileSign.act?sortId=<%=seqId%>&contentId=" + selects;
		var json=getJsonRs(url);
		if(json.rtState == '0'){
    	window.location.reload(); 	
    }else{
			alert(json.rtMsrg);
    }
	  break;
	case "downFile": //下载
		if(count>1 && window.confirm("一次下载多个文件需要在服务器上做压缩处理，会占用较多服务器CPU资源，确定继续下载吗？\n该操作请不要下载超过128MB的大文件")){
			location.href = requestURL + "/batchDownload.act?sortId=<%=seqId%>&contentIdStr=" + selects + "&name=";
		}else if(count == 1){
			location.href = requestURL + "/batchDownload.act?sortId=<%=seqId%>&contentIdStr=" + selects + "&name=";
		}
		break;
	}
}

function paste_File(){
	var url=requestURL + "/pasteFile.act?sortId=<%=seqId%>";
	var rtJson=getJsonRs(url);
  if(rtJson.rtState == '0'){
 		window.location.reload();
  }
}

function newFile(){
  var folderPath= $("sortName").innerHTML.trim();
  var url="<%=contextPath%>/core/funcs/filefolder/new/newFile.jsp?seqId=<%=seqId %>&folderPath=" + folderPath;
 	location.href = encodeURI(url);
}

var pageInfoS = null;
function showPage() {
  pageInfoS = new YHJsPagePilot(cfgs);
  pageInfoS.show();
  $('pageDiv').style.display = "";
}

function loadDataAction(obj){
  var pageNo = obj.pageIndex;
  var pageSize = obj.pageSize;
  window.location.href ="folder.jsp?seqId=<%=seqId%>&pageNo=" + pageNo + "&pageSize=" + pageSize + "&ascDescFlag=" + ascDesc + "&field="+field;
}

function getSmsCheck(){
  var smsPerson="";
  var smsArry = document.getElementsByName("SMS_SELECT_REMIND");
  if(smsArry[0].checked){
		smsPerson = $("user").value;
  }else if(smsArry[1].checked){
		smsPerson = "allPrivPerson";
  }
  return smsPerson;
}

function getMobileSmsCheck(){
  var smsPerson="";
  var smsArry = document.getElementsByName("moblieSMS_SELECT_REMIND");
  if(smsArry[0].checked){
		smsPerson = $("user1").value;
  }else if(smsArry[1].checked){
		smsPerson = "allPrivPerson";
  }
  return smsPerson;
}
function sms_submit(){
  var smsFolderPath=$("sortName").innerHTML.trim();
  var smsPerson=getSmsCheck();  
  var mobileSmsPerson=getMobileSmsCheck();
  swfupload.addPostParam("smsPerson",smsPerson);
  swfupload.addPostParam("mobileSmsPerson",mobileSmsPerson);
  swfupload.addPostParam("folderPath",smsFolderPath);
}

//判断是否要显示短信提醒 
function getSysRemind(remidDiv,remind,type){ 
	var requestUrl = contextPath + "/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=" + type; 
	var rtJson = getJsonRs(requestUrl); 
	if(rtJson.rtState == "1"){ 
		alert(rtJson.rtMsrg); 
		return ; 
	} 
	var prc = rtJson.rtData; 
	//alert(rsText);
		var allowRemind = prc.allowRemind;;//是否允许显示 
		var defaultRemind = prc.defaultRemind;//是否默认选中 
		var mobileRemind = prc.mobileRemind;//手机默认选中 
	if(allowRemind=='2'){ 
		$(remidDiv).style.display = 'none'; 
	}else{
		$(remidDiv).style.display = ''; 
		if(defaultRemind=='1'){ 
			if(remind){
				$(remind).checked = true; 
			}
		} 
	}
	if(remind){
		if(document.getElementById(remind).checked){
			document.getElementById(remind).value = "1";
		}else{
			document.getElementById(remind).value = "0";
		}
	}
}

//判断是否要显示手机短信提醒 
function moblieSmsRemind(remidDiv,remind,type){
	var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=" + type; 
	var rtJson = getJsonRs(requestUrl); 
	if(rtJson.rtState == "1"){ 
		alert(rtJson.rtMsrg); 
		return ; 
	} 
	var prc = rtJson.rtData; 
	//alert(rsText);
	var moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中 
	if(moblieRemindFlag == '2'){ 
		$(remidDiv).style.display = '';
		if(remind){
			$(remind).checked = true;
		}
	}else if(moblieRemindFlag == '1'){ 
		$(remidDiv).style.display = '';
		if(remind){
			$(remind).checked = false;
		}
	}else{
		$(remidDiv).style.display = 'none'; 
	}
	if(remind){
		if(document.getElementById(remind).checked){
			document.getElementById(remind).value = "1";
		}else{
			document.getElementById(remind).value = "0";
		}
	}
}
</script>
</head>
<body >
<table id="headTableStr" border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big3"><img src="<%=contextPath %>/core/funcs/filefolder/images/notify_open.gif" align="middle"><b>&nbsp;<span class="Big1" id="sortName"> </span></b><br>
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

<div id="noPrivDiv" style="display: none">
<table class="MessageBox" align="center" width="340">
  <tr>
    <td class="msg error">
      <h4 class="title">错误</h4>
      <div class="content" style="font-size:12pt">您没有权限访问该目录</div>
    </td>
  </tr>
</table>

</div>

<br>
<form name="form1" id="form1" action="<%=contextPath%>/yh/core/funcs/filefolder/act/YHFileContentAct/uploadFile.act?seqId=<%=seqId %>" method="post" enctype="multipart/form-data" >
<input type="hidden" name="sortParentStr" id="sortParentStr" value="" >

<table>
  <tr id="">
    <td colspan="3">
	    <div id="fsUploadArea" class="flash" style="width:380px;">
		  <div id="fsUploadProgress"></div>
			<div id="totalStatics" class="totalStatics"></div>
			<div>	
			<div id="smsRemindDiv" style="display: none">
				内部短信提醒：<br>
				<input type="radio"	name="SMS_SELECT_REMIND" id="SMS_SELECT_REMIND0" value="0" onclick="document.getElementById('SMS_SELECT_REMIND_SPAN').style.display='';" checked><label for="SMS_SELECT_REMIND0">手动选择被提醒人员</label> 
				<input 	type="radio" name="SMS_SELECT_REMIND" id="SMS_SELECT_REMIND1"	value="1"	onclick="document.getElementById('SMS_SELECT_REMIND_SPAN').style.display='none';"><label	for="SMS_SELECT_REMIND1">提醒全部有权限人员</label><br>
				<span id="SMS_SELECT_REMIND_SPAN"> <input type="hidden"	name="user" id="user" value=""> <textarea cols=30	name="userDesc" id="userDesc" rows="2" class="BigStatic" wrap="yes"	readonly></textarea> 
					<a href="javascript:;" class="orgAdd"	onClick="selectUser();">添加</a> 
					<a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
				</span>
			
			</div><br>	
			<div id="sms2RemindDiv" style="display: none">
				手机短信提醒：<br>
				<input type="radio"	name="moblieSMS_SELECT_REMIND" id="moblieSMS_SELECT_REMIND0" value="0" onclick="document.getElementById('moblieSMS_SELECT_REMIND_SPAN').style.display='';" checked><label for="moblieSMS_SELECT_REMIND0">手动选择被提醒人员</label> 
				<input 	type="radio" name="moblieSMS_SELECT_REMIND" id="moblieSMS_SELECT_REMIND1"	value="1"	onclick="document.getElementById('moblieSMS_SELECT_REMIND_SPAN').style.display='none';"><label	for="moblieSMS_SELECT_REMIND1">提醒全部有权限人员</label><br>
				<span id="moblieSMS_SELECT_REMIND_SPAN">
				<input type="hidden"	name="user1" id="user1" value=""><textarea cols=30	name="userDesc1" id="userDesc1" rows="2" class="BigStatic" wrap="yes"	readonly></textarea> 
			  <a href="javascript:;" class="orgAdd" onClick="selectUser(['user1', 'userDesc1']);">添加</a> 
			  <a href="javascript:;"	class="orgClear" onClick="$('user1').value='';$('userDesc1').value='';">清空</a>
				</span>
			</div><br>						
			  <input type="button" id="btnStart" class="SmallButtonW" value="开始上传" onclick="sms_submit();swfupload.startUpload();" >&nbsp;&nbsp;
				<input type="button" id="btnCancel" class="SmallButtonW" value="全部取消" onclick="swfupload.cancelQueue();" >&nbsp;&nbsp;
				<input type="button" class="SmallButtonW" value="刷新页面" onclick="window.location.reload();">
		  </div>
		 </div>
	 </td>
  </tr>
</table>
</form>  



<br>
<table id="fileTable" class="TableBlock" width="100%" align="center">
  <tr>
    <td class="TableContent" nowrap align="center" width="65"><b>文件操作：</b></td>
    <td class="TableControl">&nbsp;
    	<a id='queryFile' style='display:' href="<%=contextPath %>/core/funcs/filefolder/query.jsp?seqId=<%=seqId %>" title="查询文件" style="height:20px;"><img src="<%=contextPath %>/core/funcs/filefolder/images/folder_search.gif" align="middle" border="0">&nbsp;查询&nbsp;&nbsp;</a>
			<a id="globalQuery" style="display:" href="<%=contextPath %>/core/funcs/filefolder/globalQuery.jsp" title="全局搜索" style="height:20px;"><img src="<%=contextPath %>/core/funcs/filefolder/images/folder_search.gif" align="middle" border="0">&nbsp;全局搜索&nbsp;&nbsp;</a>
   		<span id="paste_file" style="display:none;" title="粘贴文件"><a href="javascript:paste_File()" style="height:20px;"><img src="<%=contextPath %>/core/funcs/filefolder/images/paste.gif" align="middle" border="0">&nbsp;粘贴</a>&nbsp;&nbsp;</span>
   		<span id="newFile" style="display: none;"><a href="javascript:newFile();" title="创建新的文件" style="height:20px;"><img src="<%=imgPath%>/notify_new.gif" align="middle" border="0">&nbsp;新建文件&nbsp;&nbsp;</a></span>
   		<span id="battUpload" style="display: none;"><span id="spanButtonUpload" title="批量上传">&nbsp;&nbsp;</span></span>
 		</td>
  </tr>
  <tr>
    <td class="TableContent" nowrap align="center" width="65"><b>文件夹操作：</b></td>
    <td class="TableControl">&nbsp;
   		<span id="paste_sort" style="" title="粘贴文件夹" ><a href="javascript:paste_sort()"><img src="<%=contextPath %>/core/funcs/filefolder/images/paste.gif" align="middle" border="0">&nbsp;粘贴</a>&nbsp;&nbsp;</span>
   		<a href="<%=contextPath%>/core/funcs/filefolder/new/newFolder.jsp?seqId=<%=seqId %>" title="创建子文件夹" id="newSubFolder" ><img src="<%=imgPath%>/newfolder.gif" align="middle" border="0">&nbsp;新建&nbsp;&nbsp;</a>
   		<a href="<%=contextPath%>/core/funcs/filefolder/set_priv/index.jsp?seqId=<%=seqId %>&parentId=<%=parentId %>" title="设置该文件夹的访问、管理、新建和下载权限" id="setFolderPriv"><img src="<%=imgPath%>/folder_priv.gif" align="middle" border="0">&nbsp;设置权限&nbsp;&nbsp;</a>
   		<a href="<%=contextPath%>/core/funcs/filefolder/editSubFolder.jsp?seqId=<%=seqId %>" title="重命名此文件夹" id="reNameSubFolder"><img src="<%=contextPath %>/core/funcs/filefolder/images/folder_edit.gif" align="middle" border="0">&nbsp;重命名&nbsp;&nbsp;</a>
   		<a href="javascript:sort_action('copy');" id="copyFolder"><img src="<%=contextPath %>/core/funcs/filefolder/images/copy.gif" align="middle" border="0" title="复制当前文件夹" >&nbsp;复制&nbsp;&nbsp;</a>
   		<a href="javascript:sort_action('cut');" id="cutFolder"><img src="<%=contextPath %>/core/funcs/filefolder/images/cut.gif" align="middle" border="0" title="剪切当前文件夹">&nbsp;剪切&nbsp;&nbsp;</a>
  		<a href="javascript:delete_sort('<%=seqId %>');" title="删除此文件夹" id="delFolder"><img src="<%=contextPath %>/core/funcs/filefolder/images/delete.gif" align="middle" border="0">&nbsp;删除&nbsp;&nbsp;</a>   		
   		
    </td>
  </tr>
</table>
</body>
</html>
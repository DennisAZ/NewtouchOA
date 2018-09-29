<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@ include file="utility.jsp"%>
<%
  response.setHeader("Cache-Control", "no-store"); //HTTP 1.1 
  response.setHeader("Pragma", "no-cache"); //HTTP 1.0 
  response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<%
  String seqIdStr = request.getParameter("seqId");
  String pathId = request.getParameter("DISK_ID");
  String smsRemindFlag = request.getParameter("smsRemindFlag");
  //if("1".equals(smsRemindFlag)){
  //pathId = request.getParameter("DISK_ID");
  //}

  int seqId = 0;

  if (pathId == null) {
    //pathId = (String)request.getAttribute("DISK_ID");
    pathId = "";
  }
  if (seqIdStr != null) {
    seqId = Integer.parseInt(seqIdStr);
  }

  //获取复制、剪切的session
  String actionFlag = (String) request.getSession().getAttribute(
      "NETDISK_ACTION");
  //System.out.println("actionFlag:"+actionFlag);
  if (actionFlag == null) {
    actionFlag = "";
  }

  /**
   String totalRecord = request.getParameter("sizeNo");
   if(totalRecord == null){
   totalRecord = "0";
   }
   **/

  String pageIndex = request.getParameter("pageNo");
  if (pageIndex == null) {
    pageIndex = "0";
  }
  String pageSize = request.getParameter("pageSize");
  if (pageSize == null) {
    pageSize = "20";
  }

  String pageAscDesc = request.getParameter("ascDescFlag");
  String pageField = request.getParameter("field");
  if (pageAscDesc == null || "".equals(pageAscDesc.trim())) {
    pageAscDesc = "";
  }
  if (pageField == null || "".equals(pageField.trim())) {
    pageField = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件列表</title>
<link href="<%=cssPath%>/cmp/swfupload.css" rel="stylesheet"
	type="text/css" />
<link href="<%=cssPath%>/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript"
	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript"
	src="<%=contextPath%>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript"
	src="<%=contextPath%>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript"
	src="<%=contextPath%>/core/js/cmp/attach.js"></script>
<script type="text/Javascript"
	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"
	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"
	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"
	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript"
	src="<%=contextPath%>/core/js/orgselect.js"></script>
<script type="text/javascript"
	src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"
	src="<%=contextPath%>/core/js/cmp/attachMenu2Net.js"></script>
<script type="text/javascript"
	src="<%=contextPath%>/core/js/cmp/pagePilot.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<style>
td.select {
	width: 40px;
}
</style>
<script type="text/javascript">
//var pageSize = 20;
//var totalRecord = 0;
//var pageIndex = 0;


 var field = '<%=pageField%>'; 
 var ascDesc = '<%=pageAscDesc%>';


var pageSize = "<%=pageSize%>";
var totalRecord = 0;
var pageIndex = "<%=pageIndex%>";
var cfgs;




var seqId='<%=seqId%>';
//alert("seqId>>>:"+seqId);
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;
var count = 0;
var selected_count = 0;
function getPath()
{
	var diskPath = "<%=pathId%>";
	var smsRemindFlag = "<%=smsRemindFlag%>";
	var diskPathStr = "";
	if(smsRemindFlag =="1"){
		diskPathStr = decodeURI(diskPath);
	}else{
		diskPathStr = diskPath;
	}
	//return "<%=pathId%>";
	return diskPathStr;
}
function getUrl(funcname)
{
	var requestURL = "<%=contextPath%>/yh/core/funcs/netdisk/act/YHNetDiskAct";
	var url = requestURL + "/" + funcname + ".act?DISK_ID=" + encodeURIComponent(getPath())+"&seqId=<%=seqId%>&pageNo=" + pageIndex + "&pageSize=" + pageSize + "&ascDescFlag=" + ascDesc + "&field="+field;
	//alert(url);
	return url;
}

//初始加载
//加载权限
//获取权限信息
var visiPriv=0;
var managePriv=0;
var newPriv=0;
var downPriv=0;
var rootDir=0;
function doInit(){
	showPathName();
	getSysRemind("smsRemindDiv","",17);
	moblieSmsRemind("sms2RemindDiv","",17);
	getSysRemind("smsRemindDiv1","",17);
	moblieSmsRemind("sms2RemindDiv2","",17);
	var json=getJsonRs(getUrl("getPrivInfoById"));
	//alert("rsText>>:"+rsText);
	if(json.rtState=='1'){
		alert(json.rtState);
		return;
	}
	var privJson = json.rtData;
	var priv = privJson[0];
	var counter=privJson.length;
	//alert("priv:"+count);
	if(counter>0){
		visiPriv=priv.visiPriv;
		managePriv=priv.managePriv;
		newPriv=priv.newPriv;
		downPriv=priv.downPriv;				
		rootDir=priv.rootDir;	
			
	}
	//alert("newPriv:"+newPriv + "  managePriv:"+managePriv + "  visiPriv:"+visiPriv + "  downPriv:"+downPriv);
	//alert(seqId);
	//// 对文件夹
	if(managePriv==1){
		$("folderOpt").show();
		$("newFolder").show();		
		if(rootDir!=1 ){
			$("reNameFolder").show();
			$("delFolder").show();
		}
	}
	if(newPriv==1){
		$("singleUpload").show();
		$("battUpload").show();
	}
	

	//得到返回数据
	var json = getJsonRs(getUrl("getNetDiskInfo"));
	if(json.rtState == '1')	{
		alert(json.rtState);
		return;				
	}
	//alert(rsText);
	var prcsJson = json.rtData;	
	count = prcsJson.length;

	if(count>0){
		
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


	
	//alert("totalRecord>>"+totalRecord +" pageIndex>>>"+pageIndex +"  pageSize>>"+pageSize);	
	
	if(count > 0)	{
		//$('netdisk_message').remove();
		var subjectImgReturn="<span id='subjectImg'></span>";
		var returnAscDesc = prcsJson[0].ascDesc;  	
		var returnOrderBy = prcsJson[0].orderBy ;  
		fileLimit =	prcsJson[0].flolderSize;

		field=returnOrderBy;
		ascDesc=returnAscDesc;
		
		var message = "<table class='TableList' width='100%'>"
				+ "<tr class='TableHeader' align='center'><td class='select'>选择</td>"
				+ "<td id='subject' name='subject' ><a style='text-decoration: underline;' href='javascript:order_by(\"NAME\")'>文件名" +subjectImgReturn + "</a></td>"
				+ "<td id='fileSize' name='fileSize' width='60'><a style='text-decoration: underline;' href='javascript:order_by(\"SIZE\")'>大小<span id='fileSizeImg'></span></td>"
				+ "<td id='fileType' name='fileType'><a style='text-decoration: underline;' href='javascript:order_by(\"TYPE\")'>类型<span id='fileTypeImg'></span></td>"
	    	+ "<td id='fileModifyTime' name='fileModifyTime' width='125'><a style='text-decoration: underline;' href='javascript:order_by(\"TIME\")'>修改时间<span id='fileTimeImg'></span></td>"
	    	+ "</tr>"
	    	+ "<tr class='TableLine' id='netdisk_list'></tr>"
	    	+ "<tr class='TableControl' id='fileAction' style='display:none'><td colspan='5'>"
	    	+ "<input type='checkbox' id='allbox' name='allbox' onClick='check_all();'><label for='allbox'>全选</label> &nbsp;"
	    	+ "<a id='copyFile' style='display:' href=\"javascript:do_action('copy');\"><img src=\"<%=contextPath%>/core/funcs/netdisk/images/copy.gif\" align=\"absMiddle\" border=\"0\" title=\"复制此文件\">复制&nbsp;&nbsp;</a>"
	    	+ "<a id='cutFile' style='display:none' href=\"javascript:do_action('cut');\"><img src=\"<%=contextPath%>/core/funcs/netdisk/images/cut.gif\" align=\"absMiddle\" border=\"0\" title=\"剪切此文件\">剪切&nbsp;&nbsp;</a>"
	    	+ "<a id='reNameFile' style='display:none' href=\"javascript:do_action('rename');\"><img src=\"<%=contextPath%>/core/funcs/netdisk/images/folder_edit.gif\" align=\"absMiddle\" border=\"0\" title=\"重命名此文件\">重命名&nbsp;&nbsp;</a>"
	    	+ "<a id='deleteFile' style='display:none' href=\"javascript:do_action('delete');\"><img src=\"<%=contextPath%>/core/funcs/netdisk/images/delete.gif\" align=\"absMiddle\" border=\"0\" title=\"删除此文件\">删除&nbsp;&nbsp;</a>"
	      + "<a id='downFile' style='display:none' href=\"javascript:do_action('down');\"><img src=\"<%=contextPath%>/core/funcs/netdisk/images/download.gif\" align=\"absMiddle\" border=\"0\" title=\"批量压缩后下载\"><span id=\"label_down\">下载&nbsp;&nbsp;</span></a>"
	    	+ "</td></tr></table>";
	    $('list').update(message);
	 
		checkShunXu();
				
		//new Insertion.After('netdisk_menu', message);		
    // $('netdisk_menu').update(message);
		// 对文件
		if(managePriv==1){
			$("cutFile").show();
			$("reNameFile").show();
			$("deleteFile").show();
		}	

		
		
	}else{
		$('netdisk_message').show();
	}

	var actionFlag='<%=actionFlag%>';
	//alert("actionFlag>>"+actionFlag + "  newPriv>>"+newPriv);
	if(actionFlag=="copy"||actionFlag=="cut"){
		if(newPriv==1){
			$("paste_file").show();
		}
	}
	//alert(rsText);
	for(var i = 0; i < count; i++)	{
		var prcs = prcsJson[i];
		var filePath=prcs.filePath;
		var fileName=prcs.fileName;
		var officeFlag=prcs.officeFlag;
		var checkBoxStr="<input type=\"checkbox\" name=\"file_select\" value=\"" + prcs.fileName + "\" onClick=\"check_one(this);\"><\/td>";

		if(downPriv !=1 && officeFlag =="1"){
			checkBoxStr="";
		}

		if(downPriv ==1 || officeFlag !="1"){
			$("fileAction").show();
		}
		
		if (downPriv == 1) {
		  $("downFile").show();
		}
		
		var hiddenDiv="<input type = 'hidden' id='returnAttId_" + i + "' name='returnAttId_" +i + "'></input>"
									+ "<input type = 'hidden' id='returnAttName_" + i + "' name='returnAttName_" + i + "'></input>"
									+ "<div id='attr_" + i + "'></div>";
		
		var message = "<tr class=\"TableLine" + (i % 2 + 1) + "\"><td align=\"center\">"
			+ checkBoxStr					
		 	+ "<td>" + hiddenDiv  +"</td>"
		  + "<td align='center'>" + prcs.fileSpace + "<\/td>"					
		  + "<td align='center'>" + prcs.fileType + "<\/td>"
		  + "<td align='center'>" + prcs.fileModifyTime + "<\/td><tr>";				  
		  new Insertion.Before('netdisk_list', message);

		  $("returnAttId_"+i).value= filePath;
			$("returnAttName_"+i).value= fileName;
			
			//var downFlag1=false;
			//if(downPriv == 1){
			//  downFlag1=true;
			//}

			var selfdefMenu = {
       	office:["downFile","read","edit","readNoPrint"], 
        img:["play"],
        music:["play"],  
		    video:["play"],
		    text:["readText"],
		    pdf: ["readpdf"],
		    others:[]
			}
			
			if(visiPriv == 1 && managePriv == 1 && newPriv == 1 && downPriv == 1){
			  selfdefMenu.office = selfdefMenu.office.without("readNoPrint");
			} else if(visiPriv == 1 && managePriv == 1 && newPriv == 1){
			  selfdefMenu.office = selfdefMenu.office.without("downFile","read");
			} else if(managePriv == 1 && newPriv == 1 && downPriv == 1){
			  selfdefMenu.office = selfdefMenu.office.without("readNoPrint");
			} else if(visiPriv == 1 && managePriv == 1 ){
				selfdefMenu.office = selfdefMenu.office.without("downFile","read");
			} else if(visiPriv == 1 && newPriv == 1 ){
				selfdefMenu.office = selfdefMenu.office.without("downFile", "edit","read");
			} else if(visiPriv == 1 && downPriv == 1 ){
				selfdefMenu.office = selfdefMenu.office.without("edit","readNoPrint");
			} else if(managePriv == 1 && newPriv == 1 ){
			   selfdefMenu.office = selfdefMenu.office.without("readNoPrint");
			} else if(managePriv == 1 && downPriv == 1 ){
			  selfdefMenu.office = selfdefMenu.office.without("readNoPrint");
			} else if(newPriv == 1 && downPriv == 1 ){
			  selfdefMenu.office = selfdefMenu.office.without("readNoPrint");
			} else if(visiPriv == 1){
				selfdefMenu.office = selfdefMenu.office.without("downFile", "edit","read");
			} else if(managePriv == 1){
			  selfdefMenu.office = selfdefMenu.office.without("readNoPrint");
			} else{
			  selfdefMenu.office = selfdefMenu.office.without("readNoPrint");
			}
			if(managePriv != 1){
				selfdefMenu.text = selfdefMenu.text.without("readText");
			}
			attachMenuSelfUtil("attr_"+i,$('returnAttName_'+i).value,$('returnAttId_'+i).value,i , "","",selfdefMenu);
	}
}


function showPathName(){
	var diskPath = "<%=pathId%>";
	var smsRemindFlag = "<%=smsRemindFlag%>";
	var diskPathStr = "";
	if(smsRemindFlag =="1"){
		diskPathStr = decodeURI(diskPath);
	}else{
		diskPathStr = diskPath;
	}
	//alert("diskPath>>"+diskPath);
	//alert("smsRemindFlag>>"+smsRemindFlag);
	//alert("diskPathStr>>"+diskPathStr);
	//var ccss = decodeURI(diskPath);
	//alert(ccss);
	var urlStr = "<%=contextPath%>/yh/core/funcs/netdisk/act/YHNetDiskAct/showPathName.act?seqId=<%=seqId%>" + "&diskPath=" + encodeURIComponent(diskPathStr);
	var rtJson = getJsonRs(urlStr); 
	//alert(rsText);
	if(rtJson.rtState == 1){
		alert(rtJson.rtMsrg); 
		return ; 
	}
	var prc = rtJson.rtData; 
	var nameStr = "";
	if(prc.diskPathNameStr ){
		if(prc.diskPathNameStr.length>20){
			nameStr = prc.diskPathNameStr.substr(0,20)+ "...";
			$("diskPathName").title = prc.diskPathNameStr;
		}else{
			nameStr = prc.diskPathNameStr;
		}
		$("diskPathName").innerHTML = nameStr;
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
	//checkShunXu();
}

function checkShunXu(){
	//alert("checkShunXu>>field:"+field  +"  ascDesc>>"+ascDesc);
	if(field=='NAME'){
		  if(ascDesc=='1') {
			   $("subjectImg").innerHTML="<img id='subjectImg' src='<%=imgPath%>/arrow_down.gif'>";
			}else {
				$("subjectImg").innerHTML="<img id='subjectImg' src='<%=imgPath%>/arrow_up.gif'>";
			}
	}else if(field=='SIZE'){
		 if(ascDesc=='1') {
			 $("fileSizeImg").innerHTML="<img id='fileSizeImg' src='<%=imgPath%>/arrow_down.gif'>";
			 
		 }else {
		 		$("fileSizeImg").innerHTML="<img id='fileSizeImg' src='<%=imgPath%>/arrow_up.gif'>";
		 }
	}else if(field=='TYPE'){

		  if(ascDesc=='1') {
			   $("fileTypeImg").innerHTML="<img id='fileTypeImg' src='<%=imgPath%>/arrow_down.gif'>";
			}else {
				$("fileTypeImg").innerHTML="<img id='fileTypeImg' src='<%=imgPath%>/arrow_up.gif'>";
			}
		
	}else if(field=='TIME'){
		
		if(ascDesc=='1') {
			   $("fileTimeImg").innerHTML="<img id='fileTimeImg' src='<%=imgPath%>/arrow_down.gif'>";
			}else {
				$("fileTimeImg").innerHTML="<img id='fileTimeImg' src='<%=imgPath%>/arrow_up.gif'>";
			}
	
	}
}


function do_action(action){
	var file_nodes = document.getElementsByName("file_select");
	var file_list = "";
	var file_count = 0;
	if(!file_nodes)	{
		return;
	}
	for(var i = 0; i < file_nodes.length; i++)	{
		if(file_nodes.item(i).checked)		{
			file_list += file_nodes.item(i).value + "*";
      file_count++;
		}
	}
	if(file_count == 0)	{
		alert("请至少选择一个文件");
		return;
	}
	if((action == "rename") && file_count > 1)	{
		alert("请选择一个文件");
		return;
	}
	switch(action)	{ 
		case "copy":
		case "cut":
			if(newPriv==1){
				$("paste_file").show();
			}
			
			//$("paste_file").style.display='';
			$("paste_file").title="粘贴所选文件";
			var urlStr="&FILE_LIST=" + encodeURIComponent(file_list) + "&NETDISK_ACTION=" + action;
			//alert(urlStr);
			var url = getUrl("doAction") + urlStr;
			var json = getJsonRs(url);
			alert("选择的文件已标记，\n请到目标目录中进行“粘贴”操作");
			break;
		case "rename":
			newWindow("<%=contextPath%>/core/funcs/netdisk/rename.jsp?seqId=<%=seqId%>&DISK_ID=" + encodeURIComponent(getPath()) + "&FILE_NAME=" + encodeURIComponent(file_list));
			break;
		case "delete":
			if(window.confirm("删除文件后将不可恢复，确定删除吗？"))
			{
				var encodeFileList=encodeURIComponent(file_list)
				var urlStr="&FILE_LIST=" + encodeFileList;
				var url = getUrl("delFile") + urlStr;
				var json = getJsonRs(url);
				self.location.reload();
			}
			break;
		case "down":
			if(file_count > 1 && window.confirm("一次下载多个文件需要在服务器上做压缩处理，会占用较多服务器CPU资源，确定继续下载吗？\n该操作请不要下载超过128MB的大文件"))
			{
				//alert(getPath());
				batchDownloadFnet(file_list,getPath(),'');
				//location = "down.jsp?DISK_ID=" + getPath() + "&FILE_NAME=" + file_list;
			}else if(file_count == 1){
				var fileNameStr;
				if(file_list.lastIndexOf("*")!=-1){
					fileNameStr=file_list.substring(0,file_list.lastIndexOf("*"));
				}
				batchDownloadFnet(file_list,getPath(),'');
				//downLoadFile(fileNameStr,getPath()+fileNameStr);
				//location = "down.jsp?DISK_ID=" + getPath() + "&FILE_NAME=" + file_list;
			}
      break;
   }
}
function pasteFile()
{
	var json = getJsonRs(getUrl('pasteFile'));
	if(json.rtState == '1')
	{
		alert(json.rtState);
		return;
	}
	self.location.reload();
}

function showOp(id)
{
   if($(id + "_menu") && $(id + "_menu").innerHTML.trim() == "")
   {
      $(id + "_menu").innerHTML = "<img id='" + id + "_loading' title='加载中...' src='/images/loading.gif' style='vertical-align:middle;'>";
      $(id + "_menu").innerHTML += "<img id='" + id + "_img' src='" + $(id).href + "&THUMB=1' onload='showImg(\"" + id + "\")' style='display:none;vertical-align:middle;'>";
   }
   //alert(id);
   showMenu(id);
}
function sendForm(){
	//if(!window.confirm("文件 "+file_name+" 已存在，确定上传并覆盖该文件吗？"))
	if(fileLimit==1){
		//alert(parent.file_main.location);
		parent.file_main.location.href=contextPath + "/core/funcs/netdisk/sendResult.jsp";
	}else{
		$('DISK_ID').value = getPath();	
		if(checkForm()){
		  var smsPerson=getSmsCheck();
		  $("smsPerson").value=smsPerson;
		  var mobileSmsPerson=getMobileSmsCheck();
		  $("mobileSmsPerson").value=mobileSmsPerson;
			$('upload').submit();
		}
	}
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
  //alert(smsPerson);
  return smsPerson;
}

function getBattSmsCheck(){
  var smsPerson="";
  var smsArry = document.getElementsByName("battSMS_SELECT_REMIND");
  if(smsArry[0].checked){
		smsPerson = $("user2").value;
  }else if(smsArry[1].checked){
		smsPerson = "allPrivPerson";
  }
  return smsPerson;
}



function getBattMobileSmsCheck(){
  var smsPerson="";
  var smsArry = document.getElementsByName("battMoblieSMS_SELECT_REMIND");
  if(smsArry[0].checked){
		smsPerson = $("user3").value;
  }else if(smsArry[1].checked){
		smsPerson = "allPrivPerson";
  }
  //alert(smsPerson);
  return smsPerson;
}

function checkForm(){
	var attachment=$("ATTACHMENT").value;
	if(attachment==""){
		alert("请指定上传文件！");
		$("ATTACHMENT").focus();
		return false;
	}
	if (!upload_limit_check($("ATTACHMENT").value)) {
    return false;
  }
	return true;
}

function get_size(){
	$('netdisk_count').innerHTML = "共" +  totalRecord + "个文件";
}
function newWindow(url){
	loc_x = (screen.availWidth-300)/2;
	loc_y = (screen.availHeight-300)/2;
	window.open(url, "netdisk", 
			"height=250,width=400,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
			+ loc_y + ", left=" + loc_x + ", resizable=yes");
}
function folder(action){
	if(action == "new" || action == "edit")	{
		//newWindow("folder/" + action + ".jsp?DISK_ID=" + getPath());
		//alert("<%=seqId%>");
		var url = contextPath + "/core/funcs/netdisk/folder/" + action + ".jsp?seqId=<%=seqId%>&DISK_ID=" + encodeURIComponent(getPath());
		//alert(url);
		loc_x = (screen.availWidth-300)/2;
		loc_y = (screen.availHeight-300)/2;
		window.open(url, "netdisk", 
				"height=250,width=400,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
				+ loc_y + ", left=" + loc_x + ", resizable=yes")
		
		return;
	}
	if(window.confirm("删除文件夹会将其所有下级文件夹和文件删除，并且删除后不可恢复，确定删除吗？"))	{
		var json = getJsonRs(getUrl("delFileFolder"));
    if (json && json.rtState == "0") {
      var curTree = parent.frames["file_list"].tree;
      var curNode = curTree.getNode(json.rtData.diskId);
      curTree.removeNode(json.rtData.diskId);
      //alert(curNode.parentId);
      if(curNode.parentId !="0"){
      	window.location.href = "<%=contextPath%>/core/funcs/netdisk/fileList.jsp?seqId=" + json.rtData.seqId + "&DISK_ID=" + curNode.parentId;
      }else{
      	parent.frames["file_list"].location.reload();
      }
    }else {
      alert("删除失败！");
    }
	}
}
//全选
function check_all(){
	var file_list = document.getElementsByName("file_select");
	var all = $("allbox");
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
		label_down.innerText = "批量下载";
		selected_count = file_list.length;
	}	else{
		label_down.innerText = "下载";
		selected_count = 0;
	}
}
//单项选择
function check_one(el){
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
		label_down.innerText = "批量下载";
	}	else{
		label_down.innerText = "下载";
	}
}
var fileLimit=0;
window.onload = function()
{
	doInit();
	//alert(fileLimit);	
	var linkColor = document.linkColor;
  var settings = {
    flash_url : "<%=contextPath%>/core/cntrls/swfupload.swf",
    upload_url: getUrl("uploadFile"),
    post_params: {"PHPSESSID" : "<%=session.getId()%>"},
    file_size_limit : fileLimit + " B",					//"1000 MB",
    file_types : "*.*",
    file_types_description : "All Files",
    file_upload_limit : 100,
    file_queue_limit : 0,
    custom_settings : {
      uploadArea : "fsUploadArea",
      progressTarget : "fsUploadProgress",
      startButtonId : "btnStart",
      cancelButtonId : "btnCancel"
    },
    debug: false,

    button_image_url: "<%=imgPath%>/uploadx4.gif",
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
  window.location.href ="<%=contextPath%>/core/funcs/netdisk/fileList.jsp?DISK_ID=" + getPath()+"&seqId=<%=seqId%>&pageNo=" + pageNo + "&pageSize=" + pageSize + "&ascDescFlag=" + ascDesc + "&field="+field;
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
/** 
*js代码 
*是否显示手机短信提醒 
*/ 
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

//var fileLimit=0;
function sms_submit(){
	//fileLimit=1
  //var smsFolderPath=$("sortName").innerHTML.trim();
  //alert(smsFolderPath);
  var battSmsPerson=getBattSmsCheck();  
  var battMobileSmsPerson=getBattMobileSmsCheck();
  swfupload.addPostParam("smsPerson",battSmsPerson);
  swfupload.addPostParam("mobileSmsPerson",battMobileSmsPerson);
  //swfupload.addPostParam("folderPath",smsFolderPath);
  //alert(smsPerson);
  
}


</script>
</head>
<body>
<table border="0" width="100%" cellspacing="0" cellpadding="3"
	class="small" id="netdisk_menu">
	<tr>
		<td class="Big" width=""><img
			src="<%=contextPath%>/core/funcs/netdisk/images/diary.gif" WIDTH="18"
			HEIGHT="18" align="middle">&nbsp; <span class="big3"
			id="diskPathName"></span>&nbsp;&nbsp; <span id="tree_size"
			class="small1"><a href="javascript:get_size();" class="A1"
			id="netdisk_count">查看文件数量</a></span></td>
	</tr>
	<tr>
		<td width="" id="pageDiv" align="right" valign="bottom" class="small1">
		<div id="pageInfo" style="float: right;"></div>
		</td>
	</tr>
</table>
<div id="list"></div>
<div id="netdisk_message" style="display: none">
<%
  Msg msg = new Msg(out);
  msg.message("", "该目录下没有文件");
%>
</div>
<form name="form1" id="form1"
	action="<%=contextPath%>/yh/core/funcs/netdisk/act/YHNetDiskAct/uploadFile.act"
	method="post" enctype="multipart/form-data">
<table>
	<tr id="">
		<td colspan="3">
		<div id="fsUploadArea" class="flash" style="width: 380px;">
		<div id="fsUploadProgress"></div>
		<div id="totalStatics" class="totalStatics"></div>

		<div id="smsRemindDiv" style="display: none">内部短信提醒：<br>
		<input type="radio" name="battSMS_SELECT_REMIND"
			id="battSMS_SELECT_REMIND0" value="0"
			onclick="document.getElementById('battSMS_SELECT_REMIND_SPAN').style.display='';"
			checked><label for="battSMS_SELECT_REMIND0">手动选择被提醒人员</label>
		<input type="radio" name="battSMS_SELECT_REMIND"
			id="battSMS_SELECT_REMIND1" value="1"
			onclick="document.getElementById('battSMS_SELECT_REMIND_SPAN').style.display='none';"><label
			for="battSMS_SELECT_REMIND1">提醒全部有权限人员</label><br>
		<span id="battSMS_SELECT_REMIND_SPAN"> <input type="hidden"
			name="user2" id="user2" value=""> <textarea cols=30
			name="userDesc2" id="userDesc2" rows="2" class="BigStatic" wrap="yes"
			readonly></textarea> <a href="javascript:;" class="orgAdd"
			onClick="selectUser(['user2', 'userDesc2']);">添加</a> <a
			href="javascript:;" class="orgClear"
			onClick="$('user2').value='';$('userDesc2').value='';">清空</a> </span></div>
		<br>

		<div id="sms2RemindDiv" style="display: none">手机短信提醒：<br>
		<input type="radio" name="battMoblieSMS_SELECT_REMIND"
			id="battMoblieSMS_SELECT_REMIND0" value="0"
			onclick="document.getElementById('battMoblieSMS_SELECT_REMIND_SPAN').style.display='';"
			checked><label for="battMoblieSMS_SELECT_REMIND0">手动选择被提醒人员</label>
		<input type="radio" name="battMoblieSMS_SELECT_REMIND"
			id="battMoblieSMS_SELECT_REMIND1" value="1"
			onclick="document.getElementById('battMoblieSMS_SELECT_REMIND_SPAN').style.display='none';"><label
			for="battMoblieSMS_SELECT_REMIND1">提醒全部有权限人员</label><br>
		<span id="battMoblieSMS_SELECT_REMIND_SPAN"> <input
			type="hidden" name="user3" id="user3" value=""><textarea
			cols=30 name="userDesc3" id="userDesc3" rows="2" class="BigStatic"
			wrap="yes" readonly></textarea> <a href="javascript:;" class="orgAdd"
			onClick="selectUser(['user3', 'userDesc3']);">添加</a> <a
			href="javascript:;" class="orgClear"
			onClick="$('user3').value='';$('userDesc3').value='';">清空</a> </span></div>
		<br>

		<div><input type="button" id="btnStart" class="SmallButtonW"
			value="开始上传" onclick="sms_submit();swfupload.startUpload();">&nbsp;&nbsp;
		<input type="button" id="btnCancel" class="SmallButtonW" value="全部取消"
			onclick="swfupload.cancelQueue();">&nbsp;&nbsp; <input
			type="button" class="SmallButtonW" value="刷新页面"
			onclick="window.location.reload();"></div>
		</div>
		</td>
	</tr>
</table>
</form>
<br>
<form id="upload" style="display: none;"
	action="<%=contextPath%>/yh/core/funcs/netdisk/act/YHNetDiskAct/uploadFile.act?seqId=<%=seqId%>"
	enctype="multipart/form-data" method="post" name="upload"><input
	type="hidden" name="smsPerson" id="smsPerson"> <input
	type="hidden" name="mobileSmsPerson" id="mobileSmsPerson">
<table class="TableBlock" align="center" width="100%">
	<tbody>
		<tr>
			<td class="TableContent">选择文件：</td>
			<td class="TableData"><input class="SmallInput"
				UNSELECTABLE="on" name="ATTACHMENT" id="ATTACHMENT" size="45"
				type="file"> <input class="SmallButton" value="上传"
				type="button" onClick="sendForm();"></td>
		</tr>

		<tr id="smsRemindDiv1" style="display: none">
			<td class="TableContent">内部短信提醒：</td>
			<td class="TableData"><input name="SMS_SELECT_REMIND"
				id="SMS_SELECT_REMIND0" value="0"
				onclick="document.getElementById('SMS_SELECT_REMIND_SPAN_SINGLE').style.display='';"
				checked="checked" type="radio"><label
				for="document.all.form1.SMS_SELECT_REMIND0">手动选择被提醒人员</label> <input
				name="SMS_SELECT_REMIND" id="SMS_SELECT_REMIND1" value="1"
				onclick="document.getElementById('SMS_SELECT_REMIND_SPAN_SINGLE').style.display='none';"
				type="radio"><label
				for="document.all.form1.SMS_SELECT_REMIND1">提醒全部有权限人员</label><br>
			<span id="SMS_SELECT_REMIND_SPAN_SINGLE"> <input type="hidden"
				name="user" id="user" value=""> <textarea cols="40"
				name="userDesc" id="userDesc" rows="2" class="BigStatic" wrap="yes"
				readonly="readonly"></textarea> <a href="javascript:;"
				class="orgAdd" onclick="selectUser();">添加</a> <a href="javascript:;"
				class="orgClear"
				onclick="$('user').value='';$('userDesc').value='';">清空</a></span></td>
		</tr>

		<tr id="sms2RemindDiv2" style="display: none">
			<td nowrap class="TableContent">手机短信提醒：</td>
			<td class="TableData"><input type="radio"
				name="moblieSMS_SELECT_REMIND" id="moblieSMS_SELECT_REMIND0"
				value="0"
				onclick="document.getElementById('moblieSMS_SELECT_REMIND_SPAN').style.display='';"
				checked><label for="moblieSMS_SELECT_REMIND0">手动选择被提醒人员</label>
			<input type="radio" name="moblieSMS_SELECT_REMIND"
				id="moblieSMS_SELECT_REMIND1" value="1"
				onclick="document.getElementById('moblieSMS_SELECT_REMIND_SPAN').style.display='none';"><label
				for="moblieSMS_SELECT_REMIND1">提醒全部有权限人员</label><br>
			<span id="moblieSMS_SELECT_REMIND_SPAN"> <input type="hidden"
				name="user1" id="user1" value=""><textarea cols=40
				name="userDesc1" id="userDesc1" rows="2" class="BigStatic"
				wrap="yes" readonly></textarea> <a href="javascript:;"
				class="orgAdd" onClick="selectUser(['user1', 'userDesc1']);">添加</a>
			<a href="javascript:;" class="orgClear"
				onClick="$('user1').value='';$('userDesc1').value='';">清空</a> </span></td>
		</tr>




	</tbody>
</table>
<input id="DISK_ID" name="DISK_ID" value="" type="hidden"> <input
	name="URL" value="asd%2F999%2F" type="hidden"> <input
	name="ORDER_BY" value="NAME" type="hidden"> <input
	name="ASC_DESC" value="4" type="hidden"> <input name="start"
	value="0" type="hidden"> <br>
</form>
<table class="TableList" width="100%" align="center">
	<tr>
		<td class="TableContent" nowrap align="center" width="80"><b>文件操作</b></td>
		<td class="TableControl">&nbsp; <span id="paste_file"
			style="display: none" title="粘贴文件："><a
			href="javascript:pasteFile();" style="height: 20px;"><img
			src="<%=contextPath%>/core/funcs/netdisk/images/paste.gif"
			align="middle" border="0">粘贴&nbsp;&nbsp;</a></span> <span
			id="singleUpload" style="display: none"><a href="javascript:;"
			onclick="$('upload').style.display='';" style="height: 20px;"><img
			src="<%=contextPath%>/core/funcs/netdisk/images/notify_new.gif"
			align="middle" border="0">单个上传&nbsp;&nbsp;</a></span> <span
			id="battUpload" style="display: none"><span
			id="spanButtonUpload" title="批量上传"></span>&nbsp;&nbsp;</span> <a
			href="<%=contextPath%>/core/funcs/netdisk/netdiskQuery.jsp?seqId=<%=seqId%>&DISK_ID=<%=URLEncoder.encode(pathId, "UTF-8")%>"
			style="height: 20px; display: "><img
			src="<%=contextPath%>/core/funcs/netdisk/images/folder_search.gif"
			align="middle" border="0">全文检索&nbsp;&nbsp;</a></td>
	</tr>

</table>
<div id="folderOpt" style="display: none">
<table class="TableList" width="100%" align="center">
	<tr>
		<td class="TableContent" nowrap align="center" width="80"><b>文件夹操作</b></td>
		<td class="TableControl">&nbsp; <a id="newFolder"
			style="display: none" href="javascript:folder('new');"><img
			src="<%=contextPath%>/core/funcs/netdisk/images/newfolder.gif"
			align="middle" border="0">新建子文件夹&nbsp;&nbsp;</a> <a
			id="reNameFolder" style="display: none"
			href="javascript:folder('edit');"><img
			src="<%=contextPath%>/core/funcs/netdisk/images/folder_edit.gif"
			align="middle" border="0">重命名此文件夹&nbsp;&nbsp;</a> <a id="delFolder"
			style="display: none" href="javascript:folder('delete');"><img
			src="<%=contextPath%>/core/funcs/netdisk/images/delete.gif"
			align="middle" border="0">删除此文件夹&nbsp;&nbsp;</a>
	</tr>
</table>
</div>
</body>
</html>
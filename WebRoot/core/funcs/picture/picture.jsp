<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.net.URLEncoder" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="yh.core.funcs.picture.data.YHPicture"%>
<%@page import="yh.core.funcs.picture.act.YHPage"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.net.URLDecoder"%><html>
<%
	String seqIdStr=(String)request.getParameter("seqId");
	int seqId=0;
	if(seqIdStr!=null){
	  seqId=Integer.parseInt(seqIdStr);
	}
	

	List<Map<String,String>> pictures=(List<Map<String,String>>)request.getAttribute("picList");
	YHPage page2=(YHPage)request.getAttribute("page");
	String showDir=(String)request.getAttribute("showDir"); 	//目录显示路径
	String subDir=(String)request.getAttribute("subDir"); 	//目录显示路径
	String noFolderFlag=(String)request.getAttribute("noFolderFlag"); 	//是否有文件夹
	String picFilePath=(String)request.getAttribute("picFilePath"); 	//文件夹全路径
	
	if(showDir==null){
		showDir="";
	}
	if(subDir==null){
		subDir="";
	}
	
	if(noFolderFlag==null){
	  noFolderFlag="";
	}
	if(picFilePath==null){
		picFilePath="";
	}
	
 	int rowCount=0;
 	int length=0;
 	if(pictures!=null && pictures.size()>0){
	 	length=pictures.size();
	 	if(length!=0){
	 		rowCount=(length-1)/7+1;	 	  
	 	} 	  
 	}
 	
%>
<%
	String pageAscDesc=request.getParameter("ascDescFlag");
	String pageField=request.getParameter("field");
	
	if(pageAscDesc==null || "".equals(pageAscDesc.trim())){
		pageAscDesc="0";
	}
	if(pageField==null || "".equals(pageField.trim())){
		pageField="NAME";
	}
	

%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>图片浏览</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<link rel="stylesheet"	href="<%=cssPath%>/cmp/ExchangeSelect.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/orgselect.js"></script>
<%--上传 --%>
<link href="<%=cssPath %>/cmp/swfupload.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript"	src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript"	src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript"	src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript"	src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript"	src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript">
//var requestURL="<%=contextPath%>/yh/core/funcs/picture/act/YHPictureAct";
var getPicInfoURL="<%=contextPath%>/yh/core/funcs/picture/act/YHPictureAct";
var uploadPriv=0;
var managePriv=0;
var rootDir=0;
var noFolderFlag='<%=noFolderFlag%>';
var field = '<%=pageField%>'; 
var ascDesc = '<%=pageAscDesc%>';

var pageSubDir="<%=subDir%>";
var encodePageSubDir=encodeURIComponent(pageSubDir);
//alert("pageSubDir>>>" + pageSubDir +"   encodePageSubDir>>>"+encodePageSubDir);

getPrivate();
function  pager(currNo){
	window.location.href = getPicInfoURL + "/getPicInfoById.act?seqId=<%=seqId%>&currNo="+currNo+ "&subDir=" + encodePageSubDir + "&ascDescFlag=" + ascDesc + "&field="+field;
}

function getPrivate(){
	var url=getPicInfoURL + "/getPrivInfoById.act?seqId=<%=seqId%>&subDir=" + encodePageSubDir;
	//alert(url);
	var json=getJsonRs(url);
	//alert("rsText>>:"+rsText);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	var privJson = json.rtData;
	var priv = privJson[0];
	var count = privJson.length;
	if(count>0){
		uploadPriv=priv.uploadPriv;
		managePriv=priv.managePriv;
		rootDir=priv.rootDir;
		
	}
	//alert("uploadPriv:"+uploadPriv +"  managePriv:"+managePriv+"   rootDir:"+rootDir);	
//alert(noFolderFlag);

	
	
	
}


//上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;

window.onload = function() {
  var linkColor = document.linkColor;
  var settings = {
    flash_url : "<%=contextPath %>/core/cntrls/swfupload.swf",
    upload_url: "<%=contextPath %>/yh/core/funcs/picture/act/YHPictureAct/uploadFile.act?seqId=<%=seqId%>&subDir=" + encodePageSubDir,
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
      startButtonId : "btnStart",						//开始上传

      cancelButtonId : "btnCancel"  				//全部取消
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

//单选

function checkStr(e1){
	//alert(e1);
	var show_str="";
	if(!e1.checked){
		document.getElementsByName("allbox")[0].checked=false;
		 show_str = "全选";
	}
	 document.getElementById('allboxlabel').innerText=show_str;
}

//获取选中项

function get_checked(){
	var checked_str="";
	for(var i=0;i<document.getElementsByName("checkStr").length;i++){
		e1=document.getElementsByName("checkStr").item(i);
		if(e1.checked){
			val=e1.value;
			checked_str += val + "*";
			//alert("val:"+val);
		}
	}
	if(i==0){
		e1=document.getElementsByName("checkStr");
		if(e1.checked){
			val=e1.value;
			checked_str+=val + "*";
		}
	}
	//alert(checked_str);
	return checked_str;
}

//全选

function check_allStr(){
	//alert("ccc");
	var show_str;
  var t =document.getElementsByName("checkStr");
  //alert(t);
  var allCKbox = document.getElementById('allbox_for');
  var chk_status;
  var show_str;
  if(allCKbox.checked ===false)  {
  	 chk_status = false;
  	 show_str = "全选";
  	 TmpFileNameStr="";
  }else{
  	 chk_status = true;
  	 show_str = "全选";
  }
  
  for (i=0;i<document.getElementsByName("checkStr").length;i++){
    if(document.getElementsByName("allbox")[0].checked){
      document.getElementsByName("checkStr").item(i).checked=true;
    }else{
      document.getElementsByName("checkStr").item(i).checked=false;
    }
  }
  if(i==0){
    if(document.getElementsByName("allbox")[0].checked){
      document.getElementsByName("checkStr").checked=true;
    }else{
      document.getElementsByName("checkStr").checked=false;
    }
  }
  document.getElementById('allboxlabel').innerText=show_str;
}




function newFolder(){
	//alert('<%=seqId%>');
	loc_x=(screen.availWidth-300)/2;
  loc_y=(screen.availHeight-300)/2;
  URL = "<%=contextPath%>/core/funcs/picture/newfolder.jsp?subDir=" +encodePageSubDir + "&picId=<%=seqId%>";
  window.open(URL,"picture","height=230,width=380,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+loc_y+",left="+loc_x+",resizable=yes");
}

function renameFolder(){
  loc_x=(screen.availWidth-300)/2;
  loc_y=(screen.availHeight-300)/2;
  URL = "<%=contextPath%>/core/funcs/picture/folderRename.jsp?picId=<%=seqId%>&ascDescFlag=<%=pageAscDesc %>&field=<%=pageField %>&subDir=" + encodePageSubDir;
  window.open(URL,"picture","height=230,width=380,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+loc_y+",left="+loc_x+",resizable=yes");
 
}

function delFolder(){
	msg="确定要删除本文件夹吗?该文件夹下的文件将同时被删除。";
  if(window.confirm(msg)){
  	var url=getPicInfoURL+"/delFolder.act?seqId=<%=seqId%>&subDir=" + encodePageSubDir;
  	//alert(url);
  	var json = getJsonRs(url);	 
	  //alert("rsText:"+rsText);
	  if(json.rtState == "1"){
	    alert(json.rtMsrg); 
	    return ;
	  }	  
	  var prc = json.rtData;
	  var flag=prc.flag;
	  var subDir=prc.subDir;
	  var thisDir=prc.thisDir;

	  var encodeDelSubDir=encodeURIComponent(subDir);
	  //alert(encodeDelSubDir);
	  
	  //var seqIdString=prc.seqId;
	  //alert(flag);
	  if(flag=="true"){
		  location.href="<%=contextPath%>/yh/core/funcs/picture/act/YHPictureAct/getPicInfoById.act?ascDescFlag=<%=pageAscDesc %>&field=<%=pageField %>&subDir=" + encodeDelSubDir + "&seqId=<%=seqId%>";
		}
  	  	
	}
}

function comeBack(){
	var url=getPicInfoURL+"/comeBack.act?seqId=<%=seqId%>&subDir=" + encodePageSubDir;
	//alert(url);
	var json = getJsonRs(url);	 
  //alert("rsText:"+rsText);
  if(json.rtState == "1"){
    alert(json.rtMsrg); 
    return ;
  }	  
  
  var prc = json.rtData;
  var returnSubDir=prc.subDir;
  var encodeReturnSubDir=encodeURIComponent(returnSubDir);
  var flag=prc.flag;
  var seqId=prc.seqId;
  if(flag == "true"){
  	location.href="<%=contextPath%>/yh/core/funcs/picture/act/YHPictureAct/getPicInfoById.act?ascDescFlag=<%=pageAscDesc %>&field=<%=pageField %>&subDir=" + encodeReturnSubDir +"&seqId="+seqId;
  }else{
		location.href="<%=contextPath %>/core/funcs/picture/index.jsp";
  }    

}

function refresh(){
	//alert("aa");
	location.href="<%=contextPath%>/yh/core/funcs/picture/act/YHPictureAct/getPicInfoById.act?seqId=<%=seqId%>&subDir=" + encodePageSubDir + "&ascDescFlag=<%=pageAscDesc %>&field=<%=pageField %>";
}

function picDelete(){
	var fileStr=get_checked()
	//alert("fileStr:"+fileStr);
  if(fileStr!=""){
     msg="确定要删除选中文件吗?";
     if(window.confirm(msg)){
       var url=getPicInfoURL + "/delFile.act?seqId=<%=seqId%>&subDir=" + encodePageSubDir + "&fileStr="+fileStr;
       
       //var fileName= fileStr.replaceAll();      
       //var decodeUrl="URLDecoder.decode("  +url, "UTF-8"  +")";
       //alert(url);
   		 var json=getJsonRs(url);
  		 if(json.rtState == '1'){
  			alert(json.rtMsrg);
  			return ;				
  		 }
  		//window.location.reload();  		
       //window.location=URL;
  		location.href="<%=contextPath%>/yh/core/funcs/picture/act/YHPictureAct/getPicInfoById.act?seqId=<%=seqId%>&subDir=" + encodePageSubDir + "&ascDescFlag=<%=pageAscDesc %>&field=<%=pageField %>";       
     }
  }else{
  	alert("请至少选择一张图片")
  }
}



//打开图片
function open_pic(pic_id,sub_dir,file_name)
{
	//alert("sub_dir>>>"+'<%=picFilePath %>');
	
   aWidth=screen.availWidth-10;
   aHeight=screen.availHeight-40;
 
   window_top=0;
   window_left=0;
   window_width=aWidth;
   window_height=aHeight;
 
   var VIEW_TYPE = document.getElementById('VIEW_TYPE').value;
   var ASC_DESC = document.getElementById('ASC_DESC').value;
   //alert("VIEW_TYPE>>>"+VIEW_TYPE +"  ASC_DESC>>>"+ASC_DESC);
   
   var picFilePath = "<%=picFilePath %>";
   var encodePicFilePath=encodeURIComponent(picFilePath)
   
   
   var encodeSub_dir = encodeURIComponent(sub_dir);
   var encodeFile_name = encodeURIComponent(file_name);
   
   URL="<%=contextPath%>/core/funcs/picture/open.jsp?PIC_ID="+pic_id+"&SUB_DIR="+ encodeSub_dir +"&URL_FILE_NAME="+ encodeFile_name +"&picFilePath=" + encodePicFilePath +"&VIEW_TYPE="+VIEW_TYPE+"&ASC_DESC="+ASC_DESC;
	 //alert(URL);
   window.open(URL,"图片浏览","toolbar=0,status=0,menubar=0,scrollbars=no,resizable=1,width="+window_width+",height="+window_height+",top="+window_top+",left="+window_left);
}
function set_view_type(){
	//var selectStr=document.getElementsByName("VIEW_TYPE").checked;
	var VIEW_TYPE =document.getElementById('VIEW_TYPE').value;
	var ASC_DESC =document.getElementById('ASC_DESC').value;
	$("field").value=VIEW_TYPE;
	$("ascDescFlag").value=ASC_DESC;
	

	//alert("field>>"+field +"   ascDesc>>>"+ascDesc);
	
   document.form2.submit();
}

//上传单张图片
function uploadSinPic(){
	if(checkForm()){
		$("uploadSingleFile").submit();
	}
}
function checkForm(){
	var attachment=$("ATTACHMENT").value.trim();
	if(attachment==""){
		alert("请指定上传图片！");
		$("ATTACHMENT").focus();
		$("ATTACHMENT").select();
		return false;
	}
  if (!upload_limit_check($("ATTACHMENT").value)) {
    return false;
  }
	var index = attachment.lastIndexOf("."); 
	var extName = ""; 
	if (index >= 0) { 
		extName = attachment.substring(index + 1).toLowerCase(); 
	}
	//alert(extName);
	if(!["gif", "png","jpg","bmp"].contains(extName)){ 
		alert("图片类型不正确！");
		$("ATTACHMENT").focus();
		$("ATTACHMENT").select();
		return false;
	}
	return true;
}



function downLoadFile(picPath,fileName){
	//alert(picPath);
	//alert(picPath +"  "+fileName);
	var pathStr=encodeURI(picPath + "/" + fileName);
	//var url ="<%=contextPath%>/getFile?uploadFileNameServer=" + pathStr ;
	var url ="<%=contextPath%>/yh/core/funcs/office/ntko/act/YHNtkoAct/downFileByLocal2.act?path=" + picPath + "/" +fileName + "&fileName="+fileName;
	//alert(url);
	location = encodeURI(url);
}

//设置图片显示宽高
function resize(imgNode){	
	var width=imgNode.width; 
	var height=imgNode.height;
	//alert("width>>"+width +"  height>>"+height);
	if(width>=80 && width>height){
		imgNode.width=80;
		imgNode.height=80*height/width;
	}	else if(height>=60 && width<height) {
		imgNode.height=80;
		imgNode.width=80* width/height;
		//alert("width>>"+width +"  height>>"+height);
	}else {
		imgNode.width=width;
		imgNode.height=height;
		
	}
	
	//imgNode.style.marginLeft=(80-imgNode.width)/2+"px";//js操作css,marginLeft为图片与边框的距离

	//imgNode.style.marginTop=(80-imgNode.height)/2+"px"; 		
	
	//var sizeDivNode=findSizeDiv(imgNode);
	//if(sizeDivNode!=null){			
	//	sizeDivNode.innerHTML=width + "x" + height;//向该div输出图片的宽和高
	//}
}




</script>
</head>
<body>
<div id="listDiv" >
<form	action="<%=contextPath%>/yh/core/funcs/picture/act/YHPictureAct/getPicInfoById.act?seqId=<%=seqId %>"	method="post" name="form2">
<input type="hidden" id="field" name="field" value="">
<input type="hidden" id="ascDescFlag" name="ascDescFlag" value="">
<input type="hidden" id="subDir" name="subDir" value="<%=subDir%>">
<table border="0" width="100%" cellspacing="0" cellpadding="3"
	class="small">
	<tr>
		<td class="Big"><img src="<%=contextPath%>/core/funcs/picture/images/picture.gif"	align="middle"><span class="big3"> 图片浏览</span> &nbsp;&nbsp; 
			<select	class="" name="VIEW_TYPE" id="VIEW_TYPE"	onChange="set_view_type();">
				<option value="NAME" <%if("NAME".equals(pageField)) out.print("selected"); %> >按名称排序</option>
				<option value="TYPE" <%if("TYPE".equals(pageField)) out.print("selected"); %> >按类型排序</option>
				<option value="TIME" <%if("TIME".equals(pageField)) out.print("selected"); %> >按修改时间</option>
				<option value="SIZE" <%if("SIZE".equals(pageField)) out.print("selected"); %> >按大小排序</option>
				</select> 
			<select class="" name="ASC_DESC" id="ASC_DESC"	onChange="set_view_type();">
				<option value=0 <%if("0".equals(pageAscDesc)) out.print("selected"); %> >升序</option>
				<option value=1 <%if("1".equals(pageAscDesc)) out.print("selected"); %> >降序</option>
		</select></td>
	</tr>
</table>
</form>


<table border="0" cellspacing="1" width="700" align="center" class="TableBlock"
	 cellpadding="3">
	<tr>
		<td  class="TableHeader" nowrap align="left" colspan="7"><span style="width: 80%;"><b>当前位置：</b><%=showDir %></span>
			<span style="float:right;width: 18%; text-align: right;"> <a href="javascript:comeBack();"><img	src='<%=contextPath%>/core/funcs/picture/images/parent.gif' border=0 align="middle"> 返回上级目录</a> </span>
		
		</td>
	</tr>
	<%
		if(pictures!=null&&pictures.size()!=0){		  
		  for(int i=0;i<rowCount;i++){				  			    
	%>
	<tr bgcolor="#FFFFFF" >
		<%
		//for(int h=0;h<rowCount;h++){		
  	for(int j=1;j<=7;j++){ 
  	  if(length>=(i*7+j)){
  	   //YHPicture pic= new YHPicture();
  	   Map<String,String>pic=new HashMap<String,String>();
  	   pic =pictures.get((i*7+j-1)); 	
  	   String picName=pic.get("picName");
  	   String picIdStr=pic.get("seqId");  	   
  	   String isPath=pic.get("isPath");  	   
  	   String picPath=pic.get("picPath").replace('\\','/');  	  
  	   String lengthStr=pic.get("length");  	   
  	   String lastModifyDate=pic.get("lastModify");  	   
  	   int picId=0;
  	   if(picIdStr!=null){picId=Integer.parseInt(picIdStr);}
  	   //SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  	   //String lastModifyDate=format.format(new Date(Long.parseLong(lastModify)));  	  
  	   
  	   String picNameHidden="";
  	   int picNameLength=0;
  	   if(picName.length()>=18){
  		 		picNameHidden=picName.substring(0,8)+"....";
  	   }else{
  		 		picNameHidden=picName;//.replace("'","\'");
  	   }
  	   
  	     
  	 	String imgFlag="";
  	 	String checkBox="";
  	 	String title="";
  	 	String cacheDri="";
  	 	  	 
	%>
		<td align="center" valign="top" width="100" height="100">
		<%
				if("isImage".equals(isPath)){	
					cacheDri="/tdoa_cache/";
					title="文件名:"+picName +"\n 大小:"+lengthStr +"\n修改日期:"+lastModifyDate;
					String picPathEncode=picPath+cacheDri+picName;
				
		%> 
			<a href="javascript:" onclick="open_pic(<%=picId%>,'<%=subDir.replace("'","\\\'")%>','<%=picName.replace("'","\\\'") %>');"> <img border="0" align="middle" title="<%=title %>" src="<%=contextPath %>/getFile?uploadFileNameServer=<%=URLEncoder.encode(picPathEncode,"UTF-8") %>"></a><br>
			<%-- <a href="<%=contextPath%>/getFile?uploadFileNameServer=<%=picPath %>/<%=URLEncoder.encode(picName) %>"><%=picNameHidden %></a>--%>
			<a href="<%=contextPath%>/yh/core/funcs/office/ntko/act/YHNtkoAct/downFileByLocal2.act?path=<%=URLEncoder.encode(picPath + "/" + picName,"UTF-8") %>&fileName=<%=URLEncoder.encode(picName,"UTF-8")%>"><%=picNameHidden %></a>
			<input style="display: none;" type='checkbox' name='checkStr' id="checkStr_<%=i*7+j-1 %>" value="<%=URLEncoder.encode(picName,"UTF-8") %>" onclick="checkStr(self);"></input>
			<script type="text/javascript">
				if(managePriv ==1){
					$("checkStr_<%=i*7+j-1%>").show();
				}
			
			</script>
			
	 	<%} if("isDir".equals(isPath)){ 
	 		title=picName;
	 	 	imgFlag="<img src=\""+contextPath + "/core/funcs/picture/images/folder.gif\" border=\"0"+title +"\"></img>";
	 	%>
	 	
	 	<script type="text/javascript">
	 		var subDirStr="<%=subDir%>" + "/" + "<%=picName%>";
	 		var encodeSubDirStr=encodeURIComponent(subDirStr);
	 		//alert("subDirStr>>>"+subDirStr + "  encodeSubDirStr>>"+encodeSubDirStr);
	 	</script>
	 	
			<a href="<%=contextPath%>/yh/core/funcs/picture/act/YHPictureAct/getPicInfoById.act?subDir=<%=URLEncoder.encode(subDir+"/"+picName,"UTF-8") %>&seqId=<%=picId %>&ascDescFlag=<%=pageAscDesc %>&field=<%=pageField %>" 
			title="<%=title %>"><%=imgFlag%><br><%=picNameHidden%></a> 
			<%--<a href="javascript:location.href=getPicInfoURL +'/getPicInfoById.act?subDir=' + encodeSubDirStr +'&seqId=<%=picId %>&ascDescFlag=<%=pageAscDesc %>&field=<%=pageField %> ' " title="<%=title %>"><%=imgFlag%><br><%=picNameHidden %></a> --%>
		<%} if("isFile".equals(isPath)){
			title="title='文件名:"+picName +"\n 大小:"+lengthStr +"\n修改日期:"+lastModifyDate  +"'";
	 		imgFlag="<img src='"+contextPath + "/core/funcs/picture/images/unknown.GIF' border='0'"+title +"></img>";
		%>
		
		<script type="text/javascript">
	 		var fileNameString = "<%=picName%>";
	 		var encodeFileNameString=encodeURIComponent(fileNameString);
	 		
	 		var filePathStr = "<%=picPath%>" + "/" + "<%=picName%>";
	 		var encodeFilePath = encodeURIComponent(filePathStr);
	 		//alert(encodeDirStr);
	 	</script>
	 	
			<a href="<%=contextPath%>/yh/core/funcs/office/ntko/act/YHNtkoAct/downFileByLocal2.act?path=<%=URLEncoder.encode(picPath + "/" + picName,"UTF-8") %>&fileName=<%=URLEncoder.encode(picName,"UTF-8")%>"><%=imgFlag%></a><br>
			<%--<a href="javascript:downLoadFile('<%=picPath %>','<%=picName %>')"><%=picNameHidden %></a>--%> 
			
			<%--<a href="javascript:location.href=contextPath+ '/yh/core/funcs/office/ntko/act/YHNtkoAct/downFileByLocal2.act?path='+ encodeFilePath + '&fileName=' + encodeFileNameString;"><%=picNameHidden %></a>--%>  
			<%--<a href="javascript:location.href=contextPath+ '/yh/core/funcs/office/ntko/act/YHNtkoAct/downFileByLocal2.act?path=<%=URLEncoder.encode(picPath + "/" + picName,"UTF-8") %>&fileName=<%=URLEncoder.encode(picName,"UTF-8") %> '"><%=picNameHidden %></a> --%> 
			
			<a href="<%=contextPath%>/yh/core/funcs/office/ntko/act/YHNtkoAct/downFileByLocal2.act?path=<%=URLEncoder.encode(picPath + "/" + picName,"UTF-8") %>&fileName=<%=URLEncoder.encode(picName,"UTF-8") %> "><%=picNameHidden %></a>  
			
			<input style="display: none ;" type='checkbox' name='checkStr' id="checkStr_<%=i*7+j-1 %>" value="<%=URLEncoder.encode(picName,"UTF-8") %>" onclick="checkStr(self);"></input>
			<script type="text/javascript">
				if(managePriv ==1){
					$("checkStr_<%=i*7+j-1 %>").show();
				}
			
			</script>
		<%} %>
		</td>

		<%				
  	  }else{
  	    %>
		<td align="center" valign="top" width="100" height="100"></td>
		<% 
  	  }  	  
  	} 
	%>

	</tr>

	<%		    
		  }
		}
	%>
</table>
</div>

<%if ("".equals(noFolderFlag.trim())){} %>

<br>
<%
if(pictures!=null){
  //out.print("pictures.size():"+pictures.size());
  //out.print("<br>page2.getCurrentPageIndex():"+page2.getCurrentPageIndex());
  //out.print("<br>page2.getTotalPageNum:"+page2.getTotalPageNum());
  //out.print("<br>page2.getCurrentPageIndex()-1:"+(page2.getCurrentPageIndex()-1));
  //out.print("<br>rowCount:"+rowCount);
}
%>



<div class="pagebar" align="center"><!-- 分页 -->
   <%
   		if(page2!=null){   		
   	 if(page2.getCurrentPageIndex()>1){
   %>
   	<a href=javascript:pager(1)>首页</a>&nbsp;&nbsp;<a href=javascript:pager(<%=page2.getCurrentPageIndex()-1%>)>上一页&nbsp;&nbsp;</a>
   <%
   	 }
     if(page2.getCurrentPageIndex() -4 >0){
       for(int no = page2.getCurrentPageIndex()-4; no<page2.getCurrentPageIndex(); no++){
         %>
         	<a href=javascript:pager(<%=no%>)><%=no%></a>&nbsp;&nbsp;
         <%
       }       
     }else{
       for(int no=1; no<page2.getCurrentPageIndex(); no++){
         %>
        	<a href=javascript:pager(<%=no%>)><%=no%></a>&nbsp;&nbsp;
        <%
       }
     } 
     if(page2.getTotalPageNum()>1){
     %>
     		<a href=javascript:pager(<%=page2.getCurrentPageIndex()%>)>[<%=page2.getCurrentPageIndex()%>]</a>
     <%   
     }
     if(page2.getCurrentPageIndex()+5 < page2.getTotalPageNum()){
       for(int no2= page2.getCurrentPageIndex()+1; no2<page2.getCurrentPageIndex()+5; no2++){
         %>
         	<a href=javascript:pager(<%=no2%>)><%=no2%></a>&nbsp;&nbsp;
         <%
       }
     }else{
       for(int no2=page2.getCurrentPageIndex()+1; no2<=page2.getTotalPageNum(); no2++){
         %>
        	<a href=javascript:pager(<%=no2%>)><%=no2%></a>&nbsp;&nbsp;
        <%
       }
    }
     if(page2.getCurrentPageIndex() < page2.getTotalPageNum()){
       %>
       <a href=javascript:pager(<%=page2.getCurrentPageIndex()+1 %>)>下一页</a>&nbsp;
       <a href=javascript:pager(<%=page2.getEndPageIndex() %>)>末页</a>
       <%
     }
   		}
   %>
  </div>


<div id="noMess" style="display:none" >
<table class="MessageBox" align="center" width="220">
	<tr>
		<td class="msg info">
		<h4 class="title">提示</h4>		
		<div class="content" style="font-size: 12pt">目录为空</div>
	
		</td>
	</tr>
</table>

</div>

<br>
<form name="form1" id="form1" action="" method="post"	enctype="multipart/form-data"><input type="hidden"	name="sortParent" id="sortParent" value="">

<table class="" width="749" align="center">
	<tr id="fsUploadRow">
		<td colspan="3">
		<div id="fsUploadArea" class="flash" style="width: 380px;">
		<div id="fsUploadProgress"></div>
		<div id="totalStatics" class="totalStatics"></div>
		<div><input type="button" id="btnStart" class="SmallButtonW"
			value="开始上传" onclick="swfupload.startUpload();">&nbsp;&nbsp;
		<input type="button" id="btnCancel" class="SmallButtonW" value="全部取消"
			onclick="swfupload.cancelQueue();">&nbsp;&nbsp; <input
			type="button" class="SmallButtonW" value="刷新页面"
			onclick="window.location.reload();"></div>
		</div>
		</td>
	</tr>
</table>
</form>


<table class="TableList" width="749" align="center" id="optionStr" >
	<tr>
		<td class="TableContent" nowrap align="center" width="100"><b>操作：</b></td>
		<td class="TableData">
		<div style="display: inline;">
		<div style="float: left; padding-top: 4px;"><span style="display: none" id="checkFlag"><input 	type="checkbox" name="allbox" id="allbox_for" onClick="check_allStr();">
			<label  for="allbox_for" id="allboxlabel">全选</label> &nbsp;</span>
			<a href="javascript:refresh();"><img	src="<%=contextPath%>/core/funcs/picture/images/refresh.gif" align="middle" border="0">&nbsp;刷新&nbsp;</a> 
			<a href="javascript:picDelete();" id="delFile" style="display: none"><img	src="<%=contextPath%>/core/funcs/picture/images/delete.gif"	align="middle" border="0">&nbsp;删除&nbsp;</a> 
			<span id="battUpload" style="display: none"><span id="spanButtonUpload"  title="批量上传" ></span></span>
			<a href="javascript:newFolder();" id="newFolder" style="display: none"><img src="<%=contextPath%>/core/funcs/picture/images/notify_new.gif"	align="middle" border="0">新建文件夹&nbsp;</a>
			<a href="javascript:renameFolder();" id="reNameFolder" style="display: none"><img src="<%=contextPath%>/core/funcs/picture/images/folder_edit.gif" align="absMiddle" border="0">&nbsp;重命名此文件夹&nbsp;</a>
			<a href="javascript:delFolder();" id="delFolder" style="display: none"><img src="<%=contextPath%>/core/funcs/picture/images/delete.gif" align="absMiddle" border="0">删除此文件夹&nbsp;</a>
			
			
		</div>
		</div>
		</td>
	</tr>
</table>

<table class="TableList" width="749" align="center" id="singleUpload" style="display: none">
	<tr>
		<td class="TableContent" nowrap align="center" width="100"><b>上传单个图片：</b></td>
		<td class="TableData" align="left">
		<form name="uploadSingleFile" id="uploadSingleFile" method="post" action="<%=contextPath %>/yh/core/funcs/picture/act/YHPictureAct/uploadSingleFile.act?seqId=<%=seqId %>"	enctype="multipart/form-data" >
			<input type="file" name="ATTACHMENT" id="ATTACHMENT" UNSELECTABLE="on" align="left"  size="30"	class="BigInput"> <INPUT TYPE="hidden" name="ACTION"	value="upload"> 
			<input type="button" class="SmallButtonC" value="上传单个图片"	onClick="uploadSinPic();">
			
			<input type="hidden" name="fileSubDir"  value="<%=subDir%>">
			<input type="hidden" name="ascDescFlag"  value="<%=pageAscDesc%>">
			<input type="hidden" name="field"  value="<%=pageField%>">
			
			
		</form>		
		</td>
	</tr>
</table>
<script type="text/javascript">
	if(uploadPriv == 1 && managePriv == 1 ){
		if(rootDir != 1){
			$("reNameFolder").show();
			$("delFolder").show();
		}
		$("delFile").show();
		$("battUpload").show();
		$("newFolder").show();	
		$("checkFlag").show();
		if(noFolderFlag!="noFolder"){
			$("singleUpload").show();
		}
		//$("singleUpload").show();
	}else if(uploadPriv == 1){
		$("battUpload").show();
		$("newFolder").show();
		if(noFolderFlag!="noFolder"){
			$("singleUpload").show();
		}
		
	}else if(managePriv == 1){
		if(rootDir != 1){
			$("reNameFolder").show();
			$("delFolder").show();
		}
		$("delFile").show();
		$("checkFlag").show();
	}

</script>
<br>
<br>
<div id="noFolder" style="display:none" >
<table class="MessageBox" align="center" width="220">
	<tr>
		<td class="msg info">
		<h4 class="title">提示</h4>		
		<div class="content" style="font-size: 12pt">目录不存在</div>
		</td>
	</tr>
</table>
<br>
<div align="center"><input align="middle" class="SmallButton" type="button" value="返回" onclick="comeBack();"></div>
</div>

<%
if ("noFolder".equals(noFolderFlag.trim()))
{
 %> 

<script type="text/javascript">
$("optionStr").hide();
$("listDiv").hide();
//$("singleUpload").hide();
$("noFolder").show();


</script>


<%
}else if(length<=0){
%>
<script type="text/javascript">
//$("optionStr").hide();
//$("listDiv").hide();
$("noMess").show();

</script>


<%
}
%>

</body>
</html>
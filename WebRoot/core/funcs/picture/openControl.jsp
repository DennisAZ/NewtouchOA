<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%@page import="java.net.URLEncoder"%><html>
<%
	String seqIdStr=request.getParameter("PIC_ID");
	String subDir=request.getParameter("SUB_DIR");
	String fileName=request.getParameter("FILE_NAME");
	String viewType=request.getParameter("VIEW_TYPE");
	String ascDesc=request.getParameter("ASC_DESC");
	String picFilePath=request.getParameter("picFilePath");
	
  String encodeSubDir = URLEncoder.encode(picFilePath, "UTF-8");
  String encodeFileName = URLEncoder.encode(fileName, "UTF-8");
	int seqId=0;
	if(seqIdStr!=null){
	  seqId=Integer.parseInt(seqIdStr);
	}

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>图片管理</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/ExchangeSelect.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/ccore/funcs/picture/js/ccorrect_btn.js" ></script>
</head>
<body topmargin=0 leftMargin=0 onload="inionload('<%=fileName %>')" style="background-image:url(<%=contextPath %>/core/funcs/picture/images/topbar.gif);">
<script type="text/javascript">
//alert('<%=viewType%>' +"  aa>>"+'<%=ascDesc%>');

var subDirString="<%=subDir%>"
var fileNameString="<%=fileName%>"

var encodeSub_dir = encodeURIComponent(subDirString);
var encodeFile_name = encodeURIComponent(fileNameString);

var FILE_ATTR_ARRAY=new Array();

var requestURL="<%=contextPath%>/yh/core/funcs/picture/act/YHPictureAct";
var url=requestURL + "/showPicInfo.act?seqId=<%=seqId%>&fileDir=" + encodeSub_dir + "&viewType=<%=viewType%>&ascDesc=<%=ascDesc%>";
//alert(url);
var json=getJsonRs(url);
//alert("rsText>>:"+rsText);
if(json.rtState == '1'){
  alert(json.rtMsrg);
}
var prcsJson=json.rtData;
var count=prcsJson.length

if(count>0){
  for(var i=0;i<count;i++){
    var prcs=prcsJson[i];
    if(i==0){
      FILE_ATTR_ARRAY.push( new Array(i,prcs.picName,prcs.fileTime,prcs.fileSize,prcs.imgWidth,prcs.imgHeight));
    }else{
      FILE_ATTR_ARRAY.push(new Array(i,prcs.picName,prcs.fileTime,prcs.fileSize,prcs.imgWidth,prcs.imgHeight));
    }
    
  }
}

//图片数组游标
var cur_pic_no=0;

//放大
var up_width,up_height;
function blowup()
{
 mywidth=parent.open_main.image.width;
 myheight=parent.open_main.image.height;

 up_width=mywidth * 1.1;
 up_height=myheight * 1.1;

 parent.open_main.image.width=up_width;
 parent.open_main.image.height=up_height;
}
//缩小
function reduce()
{
  mywidth=parent.open_main.image.width;
  myheight=parent.open_main.image.height;

  up_width=mywidth * 0.9;
  up_height=myheight * 0.9;

  parent.open_main.image.width=up_width;
  parent.open_main.image.height=up_height;
}

//最适合  实际大小
function adapt(flag)
{
  parent.open_main.pictable.style.zoom="100%";

  true_width = FILE_ATTR_ARRAY[cur_pic_no][4];	//获取图片真实宽
  true_height = FILE_ATTR_ARRAY[cur_pic_no][5]	//获取图片真实高
  if (!true_width || !true_height || true_width == "0" || true_height == "0") {
    //从服务器端获取图片宽度和高度
    var url = "<%=contextPath%>/yh/core/funcs/picture/act/YHPictureAct/getPicDimension.act";
    var file_name=FILE_ATTR_ARRAY[cur_pic_no][1];
    var encodeFileName = encodeURIComponent(file_name);
    var encodeSubDir = "<%=encodeSubDir%>";
    if (encodeSubDir.length > 3 && encodeSubDir.substring(encodeSubDir.length - 3, encodeSubDir.length) == "%2F") {
      encodeSubDir = encodeSubDir.substring(0, encodeSubDir.length - 3);
    }
    var param = "uploadFileNameServer=" + encodeSubDir + "%2F" + encodeFileName;
    var rtJson = getJsonRs(url, param);
    if(rtJson.rtState == '0'){
      true_width = FILE_ATTR_ARRAY[cur_pic_no][4] = rtJson.rtData.width;
      true_height = FILE_ATTR_ARRAY[cur_pic_no][5] = rtJson.rtData.height;
    }else {
      true_width = FILE_ATTR_ARRAY[cur_pic_no][4] = 800;
      true_height = FILE_ATTR_ARRAY[cur_pic_no][5] = 600;
    }
  }
  clientWidth = parent.open_main.document.viewport.getWidth();	//浏览器的宽
  clientHeight = parent.open_main.document.viewport.getHeight();	////浏览器的高
 //clientHeight = parent.open_main.document.body.clientHeight;
 //clientWidth = parent.open_main.document.body.clientWidth;
 
 //alert("true_width>>>"+true_width);
 //alert("true_height>>>"+true_height);
 //alert("clientWidth>>>"+clientWidth);
 //alert("clientHeight>>>"+clientHeight);

 if(flag==1) //实际大小
 {
   up_width=true_width;
   up_height=true_height;
   //alert("clientWidth>>"+clientWidth);
   //alert("clientHeight>>"+clientHeight);
 }
 else if(flag==2) //最适合
 {
 	 padbottom = 30;
 	 if(true_width > clientWidth && true_height <= clientHeight)
   {
   	  up_width=clientWidth;
   	  up_height=true_height*clientHeight/true_width - padbottom;
   }
   if(true_height > clientHeight && true_width <= clientWidth)
   {
   	  up_height=clientHeight - padbottom;
   	  up_width=true_width*clientHeight/true_height;
   }
 	 if(true_width > clientWidth && true_height > clientHeight)
   {
   	  if(true_width >= true_height)
   	  {
   	  	 up_height=clientHeight - padbottom;
   	     up_width=true_width*clientHeight/true_height;
   	  }
   	  else
   	  {
   	    up_width=clientWidth;
   	    up_height=true_height*clientWidth/true_width - padbottom;
   	  }
   }
 	 if(true_width < clientWidth && true_height < clientHeight)
   {
   	  up_height=true_height;
   	  up_width=true_width;
   }
 }
 parent.open_main.image.width =up_width;		//设置图片显示的宽
 parent.open_main.image.height =up_height;	//设置图片显示的高
 	//alert("parent.open_main.image.width>>>"+parent.open_main.image.width);
	//alert("parent.open_main.image.height>>>"+parent.open_main.image.height);
}

function inionload(file_name)
{//alert("file_name>>"+file_name);
	//alert("length>>"+FILE_ATTR_ARRAY.length);
 for(var i=0;i<FILE_ATTR_ARRAY.length;i++)
 {
 	 if(FILE_ATTR_ARRAY[i][1]==file_name)
 	    cur_pic_no = FILE_ATTR_ARRAY[i][0];
	    //alert("cur_pic_no>>"+cur_pic_no);
 }
  if(typeof(parent.open_main.div_image)=="object") {
	  //parent.open_main.div_image.innerHTML="<img onload='parent.open_control.adapt(2);' src='header.php?PIC_ID=<?=$PIC_ID?>&SUB_DIR=<?=$SUB_DIR?>&FILE_NAME="+file_name+"' alt='鼠标滚轮缩放，点击图片翻页' border=0 id='image' width=1 height=1>";
	  parent.open_main.div_image.innerHTML="<img onload='parent.open_control.adapt(2);' src='<%=contextPath%>/getFile?uploadFileNameServer=<%=encodeSubDir%>/<%=encodeFileName%>' alt='点击图片翻页' border=0 id='image' width=1 height=1>";
	  parent.open_main.file_name.innerText=file_name.substr(0,file_name.length-4);
	  parent.open_main.pictable.style.zoom="100%";
	  //alert(parent.open_main.pictable.style.zoom);
  }
}

function open_pic(op) {
  cur_pic_no=parseInt(cur_pic_no)+op;
  if(parseInt(cur_pic_no) <= -1) {
    cur_pic_no = FILE_ATTR_ARRAY.length - 1;
  }else if(parseInt(cur_pic_no) >= FILE_ATTR_ARRAY.length) {
 	  cur_pic_no = 0;
  }
  file_name=FILE_ATTR_ARRAY[cur_pic_no][1];
  var encodeFileName = encodeURIComponent(file_name);
  parent.open_main.image.src="<%=contextPath%>/getFile?uploadFileNameServer=<%=encodeSubDir%>/" + encodeFileName;
  parent.open_main.file_name.innerText=file_name;
  parent.open_main.pictable.style.zoom="100%";
}

function down_pic()
{
	var parameterName=FILE_ATTR_ARRAY[cur_pic_no][1];
	var parameterStr="<%=picFilePath%>/" + FILE_ATTR_ARRAY[cur_pic_no][1];
  //window.location="<%=contextPath%>/getFile?uploadFileNameServer=<%=subDir%>/" + FILE_ATTR_ARRAY[cur_pic_no][1];
   var downUrl="<%=contextPath%>/yh/core/funcs/office/ntko/act/YHNtkoAct/downFileByLocal.act?path=" + encodeURIComponent(parameterStr) + "&fileName=" + encodeURIComponent(parameterName);
	//alert(downUrl);
   window.location=downUrl;
   
}
</script>
<table align=center width=100% border=0 cellspacing=0 cellpadding=2 class=small>
  <tr><td align=center valign="middle">
    <span style='padding-top:2px;'>
    <A href="javascript:open_pic(-1);"><img src='<%=contextPath %>/core/funcs/picture/images/pre_pic.png' width=48 height=48 title=上一张 border=0></A>&nbsp;
    <A href="javascript:open_pic(1);"><img src='<%=contextPath %>/core/funcs/picture/images/next_pic.png' width=48 height=48 title=下一张 border=0 id=a_id></A>&nbsp;
    <A href="javascript:adapt(2);"><img src='<%=contextPath %>/core/funcs/picture/images/adapt.png' width=48 height=48 title=最适合 border=0></A>&nbsp;
    <A href="javascript:adapt(1);"><img src='<%=contextPath %>/core/funcs/picture/images/original.png' width=48 height=48 title=实际大小 border=0></A>&nbsp;
    <A href="javascript:blowup()"><img src='<%=contextPath %>/core/funcs/picture/images/plus.gif' width=48 height=48 title=放大 border=0></A>&nbsp;
    <A href="javascript:reduce();"><img src='<%=contextPath %>/core/funcs/picture/images/minus.gif' width=48 height=48 title=缩小 border=0></A>&nbsp;
    <A href="javascript:down_pic();"><img src='<%=contextPath %>/core/funcs/picture/images/save.gif' width=48 height=48 title=保存图片 border=0></A>
  </span></td>
</tr>
</table>
</body>
</html>
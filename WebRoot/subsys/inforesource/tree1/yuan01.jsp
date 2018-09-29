<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript" src="http://localhost/yh/subsys/inforesource/tree1/js/jquery.js"></script>
<title>无标题文档</title>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
.yuan-box {
	width: 186px;
	height: 420px;
	background-color: #f0f1f6;
}
.frame-box {
	height: 380px;
	padding: 0px;
	width: 186px;
}
.button-bg {
	width: 141px;
	padding-top: 5px;
	padding-left: 45px;
}
.but-a {
	float: left;
	height: 22px;
	width: 56px;
}
.but-b {
	float: left;
	height: 22px;
	width: 72px;
	margin-left: 10px;
}
-->
</style>
<script type="text/JavaScript">
<!--
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->

</script>
<script type="text/JavaScript"><!--
var node="";
function setNode(node){  
  this.node=node;
}

function getNode(){
  return node;
}
var mods = "";
function setModules(mods){
  this.mods = mods;
}

function getModules(){
  return this.mods;
}
function refreshMe(type){ 
  window["sampleframe3"].findTypeMenu(type); 
}
function refreshContent(){
  //var a = getIFrameDOM("contentframe");
  //window.location.reload();
}
function freshContent(){
  alert(111);
  var treeNode = getNode(); 
  rightOpt(treeNode);
}
var ftype="1";
function setFtype(ftype){
	this.ftype = ftype;
}
function getFtype(){
	return this.ftype;
}
function tomanage(){ 
	var url = contextPath + "/yh/subsys/inforesouce/act/YHMateShowAct/toManage.act?ftype="+getFtype();
	myleft=(screen.availWidth-500)/2;
	window.open(url,"read_news","height=800,width=900,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=100,left="+myleft+",resizable=yes"); 
}
/**
 * 点击左边的树进行的操作

 */
	function rightOpt(treeNode){  
		alert(222222);
	  modAjax("", treeNode);
	}
	function modAjax(modules, treeNode){
		alert(33333);   
	  //$("#dataa").html("");
	  //$(".hiddiv").attr("id","tab-a-con");
	  $.ajax({
			url:contextPath + "/yh/subsys/inforesouce/act/YHFileMetaSaveAct/findFileList.act" ,
		  type:"post",
			data:"nodes="+ treeNode +"&modules="+modules,		
			//async:false,                                  //同步问题(false), 异步：(true)
			success:function(data){	
				alert(444444444);
	 		  /*setData(data); 
	 		  var data = getData();
	 	    var toJson = eval("("+data+")"); 
	 	    var len  = toJson.rtData.length;
	 	    //alert(len+"****");
	 	    setLen(len);
	 	    var page = new Pager(len, 1);  	  
	 	    setPage(page); 	
	 	    pagerNo(1); 	 
	 	    createPage(1, page);	*/	   
			}
	  });
	}
--></script>
</head>
<body style="overflow-y:hidden;overflow-x:hidden" onload="MM_preloadImages('../images/button-yuan-b2.jpg','../images/button-yuan-a2.jpg')">
<div class="yuan-box">
      <div class="frame-box">
	     <iframe id="sampleframe3" name="sampleframe3" width="100%" height="100%" frameborder="0" src="http://localhost/yh/subsys/inforesource/tree1/_demo/tree.jsp" style="border: 0px"></iframe>
	  </div>
	  <div><img src="../images/left-line.jpg" /></div>
	  <div class="button-bg">
	     <div class="but-a"><a href="javascript:void(0);" onclick="javascript:tomanage();" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image2','','../images/button-yuan-b2.jpg',1)"><img src="../images/button-yuan-b1.jpg" name="Image2" width="56" height="22" border="0" id="Image2" /></a></div>
		 <div class="but-b"><a href="javascript:void(0);" onclick="javascript:tomanage1();" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image3','','../images/button-yuan-a2.jpg',1)"><img src="../images/button-yuan-a1.jpg" name="Image3" width="72" height="22" border="0" id="Image3" /></a></div>
	  </div>
</div>
</body>
</html>

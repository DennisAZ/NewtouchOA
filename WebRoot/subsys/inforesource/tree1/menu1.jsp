<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
        <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<style type="text/css">
<!--
#menu-page {
	margin: 0px;
	padding: 0px;
	text-align: left;
	height: 47px;
	width: 202px;
}
#menu-page #menu-box1 {
	margin: 0px;
	padding: 0px;
	height: 47px;
	width: 57px;
	list-style-type:none;
	float: left;
}
#menu-page #menu-box2 {
	margin: 0px;
	padding: 0px;
	height: 47px;
	width: 76px;
	list-style-type:none;
	float: left;
}
#menu-page #menu-box3 {
	margin: 0px;
	padding: 0px;
	height: 47px;
	width: 68px;
	list-style-type:none;
	float: left;
}
#menu-page #menu-box1 #id-nv-a { 
position: absolute; 
visibility: hidden; 
} 
#menu-page #menu-box1 #id-nv-a1 { 
position: absolute; 
} 
#menu-page #menu-box2 #id-nv-b { 
position: absolute; 
} 
#menu-page #menu-box2 #id-nv-b1 { 
position: absolute; 
visibility: hidden; 
} 
#menu-page #menu-box3 #id-nv-c { 
position: absolute; 
} 
#menu-page #menu-box3 #id-nv-c1 { 
position: absolute; 
visibility: hidden; 
}
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-color: #6b99e2;
}
-->
</style>
<script type="text/JavaScript">
<!--
function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_showHideLayers() { //v6.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
    obj.visibility=v; }
}
//-->
</script>
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree1/js/jquery.js"></script>
<script type="text/javascript">
$(document).ready(function(){
  $("#taga").bind("click", function(){
    var c1 = $(window.parent.document.body).find(".left1");
    var c2 = $(window.parent.document.body).find(".left2");
    var c3 = $(window.parent.document.body).find(".left3");
    $("#txt-a").empty();
    c1.show();
    c2.hide();
    c3.hide();
  });
  $("#tagb").bind("click", function(){
    var c1 = $(window.parent.document.body).find(".left1");
    var c2 = $(window.parent.document.body).find(".left2");
    var c3 = $(window.parent.document.body).find(".left3");
    c1.hide();
    c2.show();
    c3.hide();
    window.parent.showSelTag();
  });
  $("#tagc").bind("click", function(){
    var c1 = $(window.parent.document.body).find(".left1");
    var c2 = $(window.parent.document.body).find(".left2");
    var c3 = $(window.parent.document.body).find(".left3");
    c1.hide();
    c2.hide();
    c3.show();
    window.parent.ajax();
  });
  $("#taga1").bind("click", function(){
    var c1 = $(window.parent.document.body).find(".left1");
    window.parent.ajaxTag();//此方法在index-common.jsp中（点击tag标签触发事件）
    //var c2 = $(window.parent.document.body).find(".left2");
    //var c3 = $(window.parent.document.body).find(".left3");
    //$("#txt-a").empty();
    
    });
});
</script>
</head>

<body style="overflow-y:hidden;overflow-x:hidden">
<div id="menu-page">
  <div id="menu-box1">
	    <div id="id-nv-a" onclick="MM_showHideLayers('id-nv-a','','hide','id-nv-a1','','show','id-nv-b','','show','id-nv-b1','','hide','id-nv-c','','show','id-nv-c1','','hide')"><a id="taga"  href="#"><img src="../images/nv-tag-a.jpg"  border="0"/></a></div>
	 <div id="id-nv-a1" onclick="MM_showHideLayers('id-nv-a','','hide','id-nv-a1','','show','id-nv-b','','show','id-nv-b1','','hide','id-nv-c','','show','id-nv-c1','','hide')"><a id="taga1"  href="#"><img src="../images/nv-tag-b.jpg"  border="0"/></a></div>
  </div>
  <div id="menu-box2">
	    <div id="id-nv-b" onclick="MM_showHideLayers('id-nv-a','','show','id-nv-a1','','hide','id-nv-b','','hide','id-nv-b1','','show','id-nv-c','','show','id-nv-c1','','hide')"><a id="tagb" href="#"><img src="../images/nv-mytag-a.jpg"  border="0"/></a></div>
	 <div id="id-nv-b1" onclick="MM_showHideLayers('id-nv-a','','show','id-nv-a1','','hide','id-nv-b','','hide','id-nv-b1','','show','id-nv-c','','show','id-nv-c1','','hide')"><img src="../images/nv-mytag-b.jpg"  border="0"/></div>
  </div>	
	 <div id="menu-box3">
		<div id="id-nv-c" onclick="MM_showHideLayers('id-nv-a','','show','id-nv-a1','','hide','id-nv-b','','show','id-nv-b1','','hide','id-nv-c','','hide','id-nv-c1','','show')"><a id="tagc" href="#"><img src="../images/nv-yuan-a.jpg"  border="0"/></a></div>
        <div id="id-nv-c1" onclick="MM_showHideLayers('id-nv-a','','show','id-nv-a1','','hide','id-nv-b','','show','id-nv-b1','','hide','id-nv-c','','hide','id-nv-c1','','show')"><img src="../images/nv-yuan-b.jpg"  border="0"/></div>		
	 </div>
</div>
</body>
</html>

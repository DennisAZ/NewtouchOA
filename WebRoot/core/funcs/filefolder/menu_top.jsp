<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<style type="text/css">
*{margin:0px;padding:0px;}

A.A1:link { COLOR: #003366; TEXT-DECORATION: underline;}
A.A1:visited { COLOR: #003366; TEXT-DECORATION: underline}
A.A1:active  { COLOR: #124164; TEXT-DECORATION: underline}
A.A1:hover   { COLOR: #124164; TEXT-DECORATION: underline}

img{border:0px;}
body{
	background: #D6E4EF;
}
#navPanel{
	width: 100%;
	height: 30px;
	padding-top:2px;
	font-size:10pt;
	background: #D6E4EF url('menu_top_bg.png') top left repeat-x;
}
#navMenu{
	float: left;
	white-space: nowrap;
	padding: 0px 20px;
	height: 28px;
	overflow:hidden;
}
#navMenu a
{
   float:left;
	overflow:hidden;
	color:#000000;
	text-decoration: none;
   padding-left:5px;
   background: url('tab_inactive.gif') no-repeat top right;
	height: 30px;
}
#navMenu a span{
   float:left;
   cursor:pointer;
	padding: 6px 10px 6px 5px;
}
#navMenu a.active, #navMenu a:hover
{
	color:#FFFFFF;
   font-weight:bold;
   background: url('menu_top_hover_l.png') no-repeat top left;
}
#navMenu a.active span, #navMenu a:hover span
{
	height: 28px;
   background: url('menu_top_hover_r.png') no-repeat top right;
}

</style>
<script type="text/javascript">
function hide_tree(){
  var frame2 = parent.document.getElementById('frame2');
  if(frame2.cols=='0,*'){
    frame2.cols = '200,*';
    document.getElementById('btn').innerHTML='<<隐藏目录树';
   }
   else{
    frame2.cols = '0,*';
    document.getElementById('btn').innerHTML='显示目录树>>';
   }
}

window.onload=function(){
  var menu_id=0,menu=document.getElementById("navMenu");
  if(!menu) return;   
  for(var i=0; i<menu.childNodes.length;i++){
    if(menu.childNodes[i].tagName!="A"){
       continue;
    }
    if(menu_id==0){
       menu.childNodes[i].className="active";      
    }
    menu.childNodes[i].onclick=function(){
	    var menu=document.getElementById("navMenu");
	    for(var i=0; i<menu.childNodes.length;i++){
	      if(menu.childNodes[i].tagName!="A"){
	        continue;
	      }
	      menu.childNodes[i].className="";
	    }
	    this.className="active";
	  }
   menu_id++;
  }
   
var navScroll = document.getElementById("navScroll");
	if(navScroll){
  navScroll.onclick = function(){
    if(menu.scrollTop + menu.clientHeight >= menu.scrollHeight || menu.scrollTop + menu.clientHeight*2 > menu.scrollHeight){
      menu.scrollTop = 0;
    }else{
      menu.scrollTop += menu.clientHeight;
    }
  }
      var panel = document.getElementById("navPanel");
      panel.onmouseover = function()
      {
         if(menu.scrollHeight >= menu.clientHeight*2)
            navScroll.style.display = '';
      }
      panel.onmouseout  = function()
      {
         navScroll.style.display = 'none';
      }
   }
   
   onresize();
};

window.onresize = function()
{
   var navScroll = document.getElementById("navScroll");
   if(navScroll)
   {
      var panel = document.getElementById("navPanel");
      var menu=document.getElementById("navMenu");
      panel.style.width = "100%";
      if(menu.clientWidth >= panel.clientWidth)
         menu.style.width = panel.clientWidth - navScroll.clientWidth - 70 + "px";
   }
}
</script>
</head>
<body>

<div id="navPanel">
  <div id="navMenu">
    <a href="left.jsp?seqId=0" target="file_tree" title="公共文件柜" hidefocus="hidefocus"><span><img src="<%=contextPath %>/core/funcs/filefolder/images/file_folder.gif" width="16" height="16" align="middle" /> 公共文件柜</span></a>
  </div>
  <div id="navRight" style="float:left;">
    <img id="navScroll" src="/images/nav_r1.gif" style="display:none;cursor:pointer;" align="middle" title="显示下一行" />
    <div style="float:left;padding-top:6px;"><a id="btn" href="javascript:hide_tree();" onfocus="this.blur();" class="A1" style="text-decoration: none;">&lt;&lt;隐藏目录树</a>
    </div> 
  </div>
</div>


</body>
</html>
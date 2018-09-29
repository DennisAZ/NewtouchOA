<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<title>test</title>
<script> 
function createIframe(){ 
var newMask=document.createElement("div"); 
newMask.id="mDiv"; 
newMask.style.position="absolute"; 
newMask.style.zIndex="1"; 
_scrollWidth=Math.max(document.body.scrollWidth,document.documentElement.scrollWidth); 
_scrollHeight=Math.max(document.body.scrollHeight,document.documentElement.scrollHeight); 
// _scrollHeight = Math.max(document.body.offsetHeight,document.documentElement.scrollHeight); 
newMask.style.width=_scrollWidth+"px"; 
newMask.style.height=_scrollHeight+"px"; 
newMask.style.top="0px"; 
newMask.style.left="0px"; 
newMask.style.background="#33393C"; 
//newMask.style.background = "#FFFFFF"; 
newMask.style.filter="alpha(opacity=40)"; 
newMask.style.opacity="0.40"; 
newMask.style.display='none'; 
var objDiv=document.createElement("DIV"); 
objDiv.id="div1"; 
objDiv.name="div1"; 
objDiv.style.width="480px"; 
objDiv.style.height="200px"; 
objDiv.style.left=(_scrollWidth-480)/2+"px"; 
objDiv.style.top=(200)/2+"px"; 
objDiv.style.position="absolute"; 
objDiv.style.zIndex="2"; 
objDiv.style.display="none"; 
//objDiv.innerHTML=' <div id="drag" style="position:absolute;height:40px;width:100%;z-index:10001;top:0; background-color:#0033FF;cursor:move ;" align="right"> <input type=button value="X" onclick="HideIframe(document.getElementById(\'mDiv\'),document.getElementById(\'div1\'));"/> </div>'; 

objDiv.style.border="solid #0033FF 3px;"; 
var frm=document.createElement("div"); 
frm.id="ifrm"; 
frm.name="ifrm"; 
frm.style.position="absolute"; 
frm.style.width="100%"; 
frm.style.height=180; 
frm.style.top=20; 
frm.style.display=''; 
frm.frameborder=0; 
objDiv.appendChild(frm); 
// newMask.appendChild(objDiv);
document.body.appendChild(newMask); 
document.body.appendChild(objDiv); 
var objDrag=document.getElementById("drag"); 
var drag=false; 
var dragX=0; 
var dragY=0; 
objDrag.attachEvent("onmousedown",startDrag); 
function startDrag(){ 
if(event.button==1&&event.srcElement.tagName.toUpperCase()=="DIV"){ 
objDrag.setCapture(); 
objDrag.style.background="#0000CC"; 
drag=true; 
dragX=event.clientX; 
dragY=event.clientY; 
} 
}; 
objDrag.attachEvent("onmousemove",Drag); 
function Drag(){ 
if(drag){ 
var oldwin=objDrag.parentNode; 
oldwin.style.left=oldwin.offsetLeft+event.clientX-dragX; 
oldwin.style.top=oldwin.offsetTop+event.clientY-dragY; 
oldwin.style.left=event.clientX-100; 
oldwin.style.top=event.clientY-10; 
dragX=event.clientX; 
dragY=event.clientY; 
} 
}; 
objDrag.attachEvent("onmouseup",stopDrag); 
function stopDrag(){ 
objDrag.style.background="#0033FF"; 
objDrag.releaseCapture(); 
drag=false; 
}; 
} 
function htmlEditor(){ 
var frm=document.getElementById("ifrm"); 
var objDiv=document.getElementById("div1"); 
var mDiv=document.getElementById("mDiv"); 
mDiv.style.display=''; 
var text = getTextRs("<%=contextPath %>/test/rad/client/codeautogener/rawtest/div.html");
frm.innerHTML = text;
objDiv.style.display = "";
var objGo=frm.contentWindow.document.getElementById("btGo"); 
objGo.attachEvent("onclick",function (){ 
HideIframe(mDiv,objDiv); 
}); 
} 
function HideIframe(mDiv,objDiv){ 
mDiv.style.display='none'; 
objDiv.style.display = "none"; 
} 
</script> 
</head> 
<body onLoad="createIframe()"> 
<table> 
<tr> 
<td>aa</td> 
<td><input type="text"/></td> 
</tr> 
<tr> 
<td>bb</td> 
<td><input type="text"/></td> 
</tr> 
</table> 
<br> 
<input type="button"id="tt"name="tt"value="Click"onClick="htmlEditor()"/> 
</body> 
</html>
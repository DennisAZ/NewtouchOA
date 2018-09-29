<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

String contextPath = request.getContextPath();
if (contextPath.equals("")) {
  contextPath = "/yh";
}
int styleIndex = 1;
String stylePath = contextPath + "/core/styles/style" + styleIndex;
String imgPath = stylePath + "/img";
String cssPath = stylePath + "/css";
String data = request.getParameter("data");
%>
<HTML xmlns:vml="urn:schemas-microsoft-com:vml">
<HEAD>
<title>关系搜索图</title>
<OBJECT id="vmlRender" classid="CLSID:10072CEC-8CC1-11D1-986E-00A0C955B42E" VIEWASTEXT></OBJECT>
<STYLE>
vml\:* { FONT-SIZE: 12px; BEHAVIOR: url(#VMLRender) }
</STYLE>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/javascript" src="jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="jquery/jquery.ui.core.js"></script>
<script type="text/javascript" src="jquery/jquery.ui.widget.js"></script>
<script type="text/javascript" src="jquery/jquery.ui.mouse.js"></script>
<script type="text/javascript" src="jquery/jquery.ui.sortable.js"></script>
<script type="text/javascript" src="jquery/jquery.ui.draggable.js"></script>
<script type="text/javascript">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script language="JavaScript" src="main.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/keymap.js"></script>
<script language="JavaScript" src="<%=contextPath %>/core/module/touchgraph/Container.js"></script>
<script type="text/javascript">
var imgPath = '<%=imgPath%>';
var contextPath = "<%=contextPath%>";
var searchData = "<%=data%>";
function search() {
  
}
function setTouchGraph(value) {
  value = 2000 - value ;
  if (value != 0) {
    $('group1').coordsize = value +"," + value;
  }
}

function getSwf(swfID) {
  if (navigator.appName.indexOf("Microsoft") != -1) {
    return window[swfID];
  } else {
    return document[swfID];
  }
}
function moveLeft(isLeft) {
  var sLeft = group.style.left;
  var left = sLeft.substring(0 , sLeft.length - 2);
  var iLeft = parseInt(left);
  if (isLeft) {
    iLeft -= 10 ;
  } else {
    iLeft += 10;
  }
  group.style.left = iLeft + "px";
}
function moveTop(isUp) {
  var sLeft = group.style.top;
  var left = sLeft.substring(0 , sLeft.length - 2);
  var iLeft = parseInt(left);
  if (isUp) {
    iLeft -= 10 ;
  } else {
    iLeft += 10;
  }
  group.style.top = iLeft + "px";
}

var sss = [{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}
,{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}
,{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}
,{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}
,{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}
];
var mynodes = [[{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}
,{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}]
               ,[{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}]
                      ,[{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}]
                           ,[{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}]
                                ,[{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '} ,{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}]];
function loadRight(subject) {
  var name =  centerNode.nodeName ;
  $('tooltip').update("&nbsp;和<span style='color:"+ centerColor +"'>" + name + "</span>相关的文件");
  $('rightList').update("");
  if (subject) {
    var i =  Math.random();
    var index = Math.ceil( i * 5 ) - 1;
    sss = mynodes[index];
  }
  for (var i = 0 ;i < sss.length;i ++) {
    var tmp = sss[i];
    addRightRow(tmp , i);
  }
}
function addRightRow(node , i) {
  var div = new Element("div");
  div.onmouseover = function() {
    this.style.backgroundColor = "#999";
  }
  div.onmouseout = function() {
    this.style.backgroundColor = "";
  }
  div.style.cursor = "hand";
  div.id = "doc-" + i;
  div.style.paddingTop = "10px";
  var div2 = new Element("div");
  div2.id = "title-" + i;
  div2.style.fontSize = "12pt";
  div2.update("<a href='javascript:void(0)'>" + node.title + "</a>");
  div.appendChild(div2);
  var div3 = new Element("div");
  div3.id = "content-" + i;
  div3.style.paddingTop = "3px";
  div3.update(node.content);
  div.appendChild(div3);
  var div4 =new Element("div");
  div4.style.paddingTop = "2px";
  div4.update(node.info);
  div.appendChild(div4);
  $('rightList').appendChild(div);
}
function setDate(val1 , val2) {
  $('date1').update(val1);
  $('date2').update(val2);
}
</script>
</HEAD>
<BODY style="margin:0px;padding:0px;background-color:#000;overflow:hidden" onload="doInit()"  onmousedown="DoRightClick();" oncontextmenu="nocontextmenu();">
<div id="flash" style="position:relative;float:top;z-index:101">
<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			id="setGroup" width="100%" height="38"
			codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
			<param name="movie" value="setGroup.swf" />
			<param name="quality" value="high" />
			<param name="wmode" value="transparent">
			<param name="allowScriptAccess" value="sameDomain" />
			<embed src="setGroup.swf" quality="high" bgcolor="#869ca7"
				width="100%" height="38" name="setGroup" align="middle"
				play="true"
				loop="false"
				quality="high"
				allowScriptAccess="sameDomain"
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">
			</embed>
	</object></div>
<div id="left" style="z-index:101;padding-top:5px;color:#fff;position:relative;float:left;width:200px">
<table width="100%" align="center">
<tr><td style="color:#fff"  style="margin-left:5px">
<div><img  WIDTH="16" HEIGHT="16" src="perImage/mbi_014.gif">&nbsp;选择分类>><a href="javascript:void(0)" onclick="comeBack();">返回主题词</a></div>
<hr/>
</td></tr>
   <tbody id="list">
     
   </tbody>
</table>
<br><br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big" align=center>
    </td>
  </tr>
</table>
<div style="width:60px;height:60px;padding-left:5px">
<div align="center" style="height:20px"><img title="向上移" src="perImage/top.png" onclick="moveTop(true)" style="cursor:pointer;position:relative;float:bottom;"/></div>
<div  style="height:20px"><img  title="向左移" src="perImage/left.png" onclick="moveLeft(true)" style="cursor:pointer;position:relative;float:left;"/><img  title="向右移" src="perImage/right.png" onclick="moveLeft(false)" style="cursor:pointer;position:relative;float:right;"/></div>
<div align="center"  style="height:20px"><img  title="向下移" src="perImage/bottom.png" style="cursor:pointer;" onclick="moveTop(false)"/></div>
</div> 

</div> 
<div id=right  style="z-index:101;padding-top:5px;color:#fff;position:relative;float:right;width:200px">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img  WIDTH="16" HEIGHT="16" align="absmiddle" src="<%=imgPath %>/notify_new.gif"/><span class="big3" style="color:#fff"  id="tooltip"></span>
    </td>
  </tr>
</table>
<div id="rightList" style="border-top:1px solid #b2d235">
</div>
</div>
<vml:group id="group1" style="position:absolute;left:0;top:48;">
</vml:group>

<div id="timeDiv" style="z-index:101;position:absolute;left:0;">
  <div style="color:#fff;padding-left:5px">
所选的日期范围是: <br/><span id="date1"></span>至<span id="date2"></span>
</div>
    <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
      id="time" width="100%" height="40"
      codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
      <param name="movie" value="time.swf" />
      <param name="quality" value="high" />
      <param name="wmode" value="transparent">
      <param name="allowScriptAccess" value="sameDomain" />
      <embed src="time.swf" quality="high" bgcolor="#869ca7"
        width="501" height="40" name="time" align="middle"
        play="true"
        loop="false"
        quality="high"
        allowScriptAccess="sameDomain"
        type="application/x-shockwave-flash"
        pluginspage="http://www.adobe.com/go/getflashplayer">
      </embed>
  </object></div>
</BODY>
</HTML>
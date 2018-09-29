<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson,yh.core.global.YHSysProps" %>
<%
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
  int folderCapacity = loginUser.getFolderCapacity();
  String seqId=request.getParameter("sortId");
  if(seqId==null){
    seqId="";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>个人文件柜</title>
<link rel="stylesheet"  href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/filefolder.css" type="text/css" />
<script type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript">

function initFrame() {
  var h = document.viewport.getDimensions().height - 35;
  if (h < 200 || isNaN(h)) {
    h = 200;
  }
  $('frameTable').style.height = h + "px";
  $('file_tree').style.height = h - 128 + "px";
}

window.onresize = initFrame;

var requestUrl = contextPath + "/yh/core/funcs/personfolder/act/YHFolderSizeAct";
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

function doInit(){
  initFrame();
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
function treeSize() {
  var str1 = "<img src='"+ imgPath +"/loading.gif' height='20' width='20' align='absMiddle'>正在计算……";
  $('tree_size').update(str1);
  var url = requestUrl + "/getFolderSize.act";
  var json = getJsonRs(url);
  if (json.rtState == '0'){
    var size = json.rtData.result;
    //alert(size);
    var str = " <a href=\"javascript:treeSize();\" class=\"A1\">"+ size +"</a>";
    $('tree_size').update(str);
  }
}

function doActive(index) {
  if (index == 0) {
    $$('#top_menu .left a')[0].addClassName("active");
    $$('#top_menu .right a')[0].removeClassName("active");
  }
  else if (index == 1) {
    $$('#top_menu .left a')[0].removeClassName("active");
    $$('#top_menu .right a')[0].addClassName("active");
  }
}

function collapse() {
  if (collapse.flag) {
	  $('treeTd').show();
	  $('colBtn').className = "scroll-left";
  }
  else {
	  $('treeTd').hide();
	  $('colBtn').className = "scroll-right";
  }
  collapse.flag = !collapse.flag;
}
</script>
</head>
<body onload="doInit()">
  <table class="FrameTable" width="100%" id="frameTable">
    <tr>
      <td width="268px" id="treeTd">
        <div class="PageHeader" id="left_top"></div>
        <div id="top_menu">
          <div class="left"><a onclick="doActive(0)" class="active" index="1" href="left.jsp?SORT_ID=0&FILE_SORT=1" target="file_tree"><span>个人文件柜</span></a></div>
          <div class="right"><a onclick="doActive(1)" index="2" href="shareTree.jsp?SORT_ID=0&FILE_SORT=1" target="file_tree" ><span>共享文件柜</span></a></div>
        </div>
        <div id="tree_container">
          <iframe id="file_tree" src="left.jsp?seqId=<%=seqId %>" name="file_tree" allowTransparency="true" frameborder="0" style="border:none;width:100%;">
          </iframe>
        </div>
        <table class="BlockBottom2">
          <tr>
            <td class="left"></td>
            <td class="center"></td>
            <td class="right"></td>
          </tr>
        </table>
        <div id="folderCapacityDiv" class="capacity" style="display:<% if (folderCapacity <= 0) out.print("none");%>">最大<%=folderCapacity %>MB，已用<span id="tree_size"> <a href="javascript:treeSize();" class="A1">查看</a></span></div>
      </td>
      <td id="colBtn" class="scroll-left" onclick="collapse()">
      </td>
      <td style="height:100%">
        <iframe src="folder.jsp?seqId=0" id="file_main" name="file_main" allowTransparency="true" frameborder="0" style="border:none;width:100%;height:100%">
        </iframe>
      </td>
    </tr>
  </table>
</body>
</html>
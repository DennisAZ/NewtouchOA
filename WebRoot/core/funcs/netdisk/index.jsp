<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
//从桌面传来
String filePath=request.getParameter("filePath");
String seqId=request.getParameter("seqId");

//从自定义菜单传来
String diskId=request.getParameter("diskId");
if(diskId == null){
  diskId = "0";
}
if(seqId == null){
  seqId = "0";
}
if(filePath == null){
  filePath = "";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>网络硬盘</title>
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
  $('file_list').style.height = h - 128 + "px";
}

window.onresize = initFrame;

var diskId='<%=diskId %>';
var requestURL="<%=contextPath %>/yh/core/funcs/netdisk/act/YHNetDiskAct";

function getFilePath(){
  initFrame();
  if(diskId !='0'){
    var url= requestURL + "/getFiilePathBySeqId.act?seqId=<%=diskId%>";
    var rtJson = getJsonRs(url);
    //alert(rsText);
    if(rtJson.rtState == "0"){      
      var prcJson = rtJson.rtData;  
      var diskPath = prcJson.folderPath;
      window["file_list"].location.href = "left.jsp?diskId=" + encodeURIComponent(diskPath) + "&seqId=<%=diskId %>";
    }
  }else{
    var diskPath = "<%=filePath %>";
    //alert("diskPath>>>"+diskPath);
    window["file_list"].location.href = "left.jsp?diskId=" + encodeURIComponent(diskPath) + "&seqId=<%=seqId %>";
  }
}

function doInit() {
  getFilePath();
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
        <div class="PageHeader" id="netdisk_top"></div>
        <table class="BlockTop2">
          <tr>
            <td class="left"></td>
            <td class="center"></td>
            <td class="right"></td>
          </tr>
        </table>
        <div id="tree_container">
          <iframe id="file_list" src="left.jsp?seqId=<%=seqId %>" name="file_list" allowTransparency="true" frameborder="0" style="border:none;width:100%;">
          </iframe>
        </div>
        <table class="BlockBottom2">
          <tr>
            <td class="left"></td>
            <td class="center"></td>
            <td class="right"></td>
          </tr>
        </table>
      </td>
      <td id="colBtn" class="scroll-left" onclick="collapse()">
      </td>
      <td style="height: 100%">
        <iframe src="blank.jsp" id="file_main" name="file_main" allowTransparency="true" frameborder="0" style="border:none;width:100%;height:100%">
        </iframe>
      </td>
    </tr>
  </table>
</body>
</html>
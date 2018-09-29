<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String sortIdStr = request.getParameter("sortId");
	String contentIdStr= request.getParameter("contentId");
    int sortId = 0 ;
	if(sortIdStr != null && !"".equals(sortIdStr) ){
		sortId = Integer.parseInt(sortIdStr);
	}
	int contentId=0;
	if(contentIdStr!=null && !"".equals(contentIdStr)){
		contentId=Integer.parseInt(contentIdStr);
	}
%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件签阅情况</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript">
var sortId = "<%=sortId%>";
var contentId = "<%=contentId%>";
var deptList = null;
var isShowAll = false;
var requestUrl = contextPath + "/yh/core/funcs/filefolder/act/YHSignReaderAct";

function deleteReader(){
 msg='确认要清空签阅情况吗？';
 if(window.confirm(msg)){
   var url = requestUrl + "/delSignReader.act";
   var json = getJsonRs(url, "contentId=" + contentId);
   if (json.rtState == '0'){
     alert(json.rtMsrg);
     $('deptList').update("");
     doInit();
   }
 }
}
function doInit() {
  var url = requestUrl + "/getSignReader.act";
  var json = getJsonRs(url, "sortId=" + sortId + "&contentId=" + contentId);
  if (json.rtState == '0'){
    var subject = json.rtData.subject;
    var readCount = json.rtData.readCount;
    var unReadCount = json.rtData.unReadCount;
    var isHavePriv = json.rtData.isHavePriv;
    var list = json.rtData.detpList;
    deptList = list;
    $('subjectSpan').update("签阅情况 — " + subject);
    if(isHavePriv == 1){
      if (list.size() > 0 ) {
        $('subjectTr').update(subject);
        $('readCount').update(readCount);
        $('unReadCount').update(unReadCount);
        list.each(function(dept, index) { addRow(dept , index);}); 
        $("hasTable").show();
        $('hasData').show();
      } else {
        $('noData').show();
      }
    }else{
			$("hasTable").hide();
			$("noPriv").show();
    }
  }
}

function addRow(dept, index) {
  var deptName = dept.deptName;
  var noReader = dept.noReader;
  var hasReader = dept.hasReader;
  
  var tr = new Element("tr" , {'class':'TableData'});
  var td = new Element("td" , {'class':'TableContent'});
  td.update(deptName);
  tr.appendChild(td);

  var td3 =  new Element("td");
  td3.style.cursor = 'hand';
  td3.title =  hasReader ;
  if (!isShowAll){
    if (hasReader.length > 30) {
      hasReader = hasReader.substring(0,30) + "...";
    }
  }
  td3.update(hasReader);
  tr.appendChild(td3);
  
  var td2 =  new Element("td");
  td2.style.cursor = 'hand';
  td2.title =  noReader ;
  if (!isShowAll){
    if (noReader.length > 30) {
      noReader = noReader.substring(0,30) + "...";
    }
  }
  td2.update(noReader);
  tr.appendChild(td2);

  $('deptList').appendChild(tr);
}
function showAll() {
  $('deptList').update("");
  if (isShowAll) {
    isShowAll = false;
  } else {
    isShowAll = true;
  }
  deptList.each(function(dept, index) { addRow(dept , index);}); 
}
</script>
</head>
<body onload="doInit()">
<table border="0"  width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/file_folder.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3" id="subjectSpan">  </span>
      &nbsp;&nbsp;&nbsp;
      <span id="hasTable" style='display:none'>
	      <input type="button" value="清空签阅情况" class="SmallButtonC" onclick="deleteReader();">&nbsp;&nbsp;
	      <input type="button" value="完整显示签阅情况" class="SmallButtonD" onclick="showAll()">
      </span>
    </td>
    </tr>
</table>
 
  <table class="TableBlock" width="100%" align="center" id="hasData" style="display:none">
    <tr>
      <td class="TableHeader" align="center" colspan="3" id="subjectTr"></td>
    </tr>
    <tr class="TableHeader">
      <td nowrap align="center">部门/成员单位</td>
      <td nowrap align="center">已读人员</td>
      <td nowrap align="center">未读人员</td>
    </tr>
    
    <tbody id="deptList">
  
    </tbody>
   <tfoot class="TableControl">
      <td nowrap align="center"><b>合计：</b></td>
      <td nowrap align="center"><b id="readCount">0</b></td>
      <td nowrap align="center"><b id="unReadCount">1</b></td>
    </tfoot>
  </table>
<br>
<div id="noData" style='display:none'>
<table class="MessageBox" align="center" width="320">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无人签阅！</div>
    </td>
  </tr>
</table>
</div>
<div id="noPriv" style='display:none'>
<table class="MessageBox" align="center" width="320">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无查看签阅权限,请设置"访问权限"！</div>
    </td>
  </tr>
</table>
</div>

</body>
</html>
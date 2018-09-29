<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String idstr = request.getParameter("diaId");
  if(idstr == null){
    idstr = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" >
var diaId = "<%=idstr%>";
function doInit(){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/showReader.act";
  var rtJson = getJsonRs(url , "diaId=" +  diaId);
  if(rtJson.rtState == "0"){
    var dataArray = rtJson.rtData.data;
    if(dataArray.length <= 0){
      WarningMsrg("无人查阅", "msrg","info" ) ;
      return;
    }
    $('subject').innerHTML = rtJson.rtData.subject;
    var table = $('reader');
    for( var i = 0; i < dataArray.length ; i++){
      var obj = dataArray[i];
      var row = table.insertRow(table.rows.length);
      row.className = 'TableData';
      var cell1 = row.insertCell(0);
      cell1.className = 'TableContent';
      cell1.width = '180';
      cell1.innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;|- " + obj.deptName;
      var cell2 = row.insertCell(1);
      cell2.style.Cursor = "hand";
      cell2.title = obj.userName;
      cell2.innerHTML = obj.userName;
    }
    var footrow = table.insertRow(table.rows.length);
    footrow.className = "TableControl";
    var footcell1 = footrow.insertCell(0);
    footcell1.align = "center";
    footcell1.innerHTML = "<b>合计：</b>";
    var footcell2 = footrow.insertCell(1);
    footcell2.align = "center";
    footcell2.innerHTML = "<b>" + rtJson.rtData.count + "</b>";
    showCntrl(table);
  }
}
</script>
<title>日志阅读人员</title>
</head>
<body onload="doInit()">
<table class="TableBlock" width="100%" align="center" id="reader" style="display:none">
    <tr>
      <td class="TableHeader" align="center" colspan="2" id="subject"></td>
    </tr>
    <tr class="TableHeader">
      <td nowrap align="center" width="180">部门</td>
      <td nowrap align="center">阅读人员</td>
    </tr>
  </table>
  <div id="msrg"></div>
</body>
</html>
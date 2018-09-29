<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="/core/inc/header.jsp" %>
  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>主要模块数据量统计 </title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">

function doInit() {
  var url = contextPath + "/yh/core/funcs/system/resManage/act/YHResManageAct/getModuleData.act";
  var json = getJsonRs(url);
  if (json.rtState == "0") {
    var list = json.rtData;
    
    for (var i = 0 ;i < list.length ; i++ ) {
      var data = list[i];
      addTr(data);
    }
    $('dataList').show();
  }
}
function addTr(data) {
  var tableDesc = data.tableDesc;
  var recordRows = data.recordRows;
  var dataSize = data.dataSize;
  var tr = new Element("tr" , {'class' : 'TableData'});
  $("dataList").appendChild(tr);
  var td = new Element("td");
  td.align = 'center';
  td.update(tableDesc);
  tr.appendChild(td);
  td = new Element("td");
  td.align = 'center';
  td.update(recordRows);
  tr.appendChild(td);
  td = new Element("td");
  td.align = 'center';
  td.update(dataSize);
  tr.appendChild(td);
}

</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td style="height:5px;">
    </td>
  </tr>
  <tr>
    <td class="Big"><img src="<%=imgPath %>/infofind.gif" align="absmiddle"><span class="big3"> 主要模块数据量统计 </span>
    </td>
  </tr>
</table>

<center>
    <input type="button"  value="返回" class="BigButton" onclick="location='index.jsp'" title="返回到资源占用状况">&nbsp;
</center>
<table class="TableList" width="90%" align="center">
  <tr class="TableHeader" align="center">
     <td width="25%">数据表 </td>
     <td width="35%">记录数 </td>
     <td>表文件大小 </td>
  </tr>
  <tbody id="dataList">
    </tbody>
</table>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>公交查询 </title>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/ExchangeSelect.css">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script  type="text/Javascript">
var pageMgr;
var start = "${param.start}";
var end = "${param.end}";
var lineId = "${param.lineId}";
function doInit(){

  var requestURL = "<%=contextPath%>/yh/core/funcs/utilapps/info/bus/act/YHBusAct/searchBus.act?address=${param.address}";
  if(start.trim() != ""){
	    requestURL += "&start=" + encodeURI(start);
	}
  if(end.trim() != ""){
	    requestURL += "&end=" + encodeURI(end);
	}
  if(lineId.trim() != ""){
	  requestURL += "&lineId=" + encodeURI(lineId);
  }
  var cfgs = {
    dataAction: requestURL,
    container: "listDiv",
    colums: [
       {type: "hidden", name: "seqId", text: "SEQID", width: 80}, 
       {type: "hidden", name: "address", text: "ADDRESS", width: 80}, 
       {type: "text", name: "lineId", text: "线路", width: 80},
       {type: "text", name: "startTime", text: "首班车时间", width: 80,align: 'center'},
       {type: "text", name: "endTime", text: "末班车时间", width: 80,align: 'center'},
       {type: "text", name: "passBy", text: "途经站点", width: 400,align:'center',render:changeColor},
       {type: "text", name: "busType", text: "车型", width: 80,align: 'center'},
       {type:"selfdef", text:"操作", width: '20%',render:opts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
}

function changeColor(cellData, recordIndex, columIndex){
	if(lineId.trim() == ""){
		cellData = cellData.replaceAll(start , "<font color='FF0000'>"+ start +"</font>");
		cellData = cellData.replaceAll(end , "<font color='FF0000'>"+ end +"</font>");
	}
  return cellData;
}

function opts(cellData, recordIndex, columIndex){
  var edit = '<a href="javascript:void(0)" onclick="updateRecord(' + recordIndex + ');return false;">编辑</a>';
  return '<center>' + edit + '</center>';
}

function updateRecord(recordIndex){
	  var seqId = pageMgr.getCellData(recordIndex,"seqId");
	  var address = pageMgr.getCellData(recordIndex,"address");
	  var lineId = pageMgr.getCellData(recordIndex,"lineId");
	  var startTime = pageMgr.getCellData(recordIndex,"startTime");
	  var endTime = pageMgr.getCellData(recordIndex,"endTime");
	  var passBy = pageMgr.getCellData(recordIndex,"passBy");
	  var busType = pageMgr.getCellData(recordIndex,"busType");

	  //对get方式的参数进行编码

	  lineId = encodeURI(lineId);
	  startTime = encodeURI(startTime);
	  endTime = encodeURI(endTime);
	  passBy = encodeURI(passBy);
	  busType = encodeURI(busType);
	  
	  window.location.href = "<%=contextPath %>/core/funcs/utilapps/info/bus/new.jsp?"
	    + "seqId="  + seqId 
	    + "&city="  + address 
	    + "&lineId=" + lineId 
	    + "&startTime=" + startTime 
	    + "&endTime=" + endTime
	    + "&passBy=" + passBy
	    + "&busType=" + busType;
}
</script>
</head>

<body class="bodycolor" topmargin="5" onload="doInit()">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/yh/core/styles/imgs/menuIcon/new_email.gif" align="absmiddle"><span class="big3"> 公交线路查询结果 </span><br><br>
    </td>
  </tr>
</table>

<br>
<div align="center" id="listDiv"></div>

<br>
<div align="center">
 <input type="button"  value="返回" class="BigButton" onClick="location='index.jsp';">
</div>

</body>
</html>
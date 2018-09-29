<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>列车时刻查询</title>
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
function doInit(){
  var train = "${param.train}";
  var requestURL = "<%=contextPath%>/yh/core/funcs/utilapps/info/train/act/YHTrainAct/searchTrain.act";
  var cfgs;
  if(start != "" || end != ""){
	  requestURL += "?start=" + encodeURI(start) + "&end=" + encodeURI(end);
	  cfgs = {
      dataAction: requestURL,
      container: "listDiv",
      colums: [
         {type: "hidden", name: "seqId", text: "SEQID", width: 80}, 
         {type: "text", name: "train", text: "车次", width: 180,align: 'center',render:trainSubSE}, 
         {type: "hidden", name: "starts", text: "START", width: 80}, 
         {type: "hidden", name: "ends", text: "END", width: 80}, 
         {type: "hidden", name: "pstart", text: "PSTART", width: 80},
         {type: "hidden", name: "pend", text: "PEND", width: 80},
         {type: "hidden", name: "kind", text: "KIND", width: 80}, 
         {type: "text", name: "ftime", text: "发车时间", width: 60,align: 'center',render:ftimeSub},
         {type: "text", name: "etime", text: "到站时间", width: 60,align: 'center',render:etimeSub},
         {type: "text", name: "distance", text: "公里数", width: 60,align:'center'},
         {type: "hidden", name: "day", text: "DAY", width: 80},
         {type: "hidden", name: "pdistance", text: "PDISTANCE", width: 80},
         {type: "hidden", name: "typePrice", text: "TYPEPRICE", width: 80},
         {type: "text", name: "price", text: "硬座票价", width: 40,align:'center'},
         {type:"selfdef", text:"操作", width: '20%',render:opts}]
    };
  }
  else if(train != ""){
    requestURL += "?train=" + encodeURI(train);
    cfgs = {
 	    dataAction: requestURL,
 	    container: "listDiv",
 	    colums: [
 	       {type: "hidden", name: "seqId", text: "SEQID", width: 80}, 
 	       {type: "text", name: "train", text: "车次", width: 180,align: 'center',render:trainSubT}, 
 	       {type: "hidden", name: "starts", text: "START", width: 80}, 
 	       {type: "hidden", name: "ends", text: "END", width: 80}, 
 	       {type: "hidden", name: "kind", text: "KIND", width: 80}, 
 	       {type: "text", name: "ftime", text: "发车时间", width: 60,align: 'center',render:ftimeSub},
 	       {type: "text", name: "etime", text: "到站时间", width: 60,align: 'center',render:etimeSub},
 	       {type: "text", name: "distance", text: "公里数", width: 60,align:'center'},
 	       {type: "hidden", name: "day", text: "DAY", width: 80},
 	       {type: "hidden", name: "pdistance", text: "PDISTANCE", width: 80},
 	       {type: "hidden", name: "typePrice", text: "TYPEPRICE", width: 80},
 	       {type: "text", name: "price", text: "硬座票价", width: 40},
 	       {type:"selfdef", text:"操作", width: '20%',render:opts}]
 	  };
  }
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
}

function trainSubSE(cellData, recordIndex, columIndex){
	var pstart = this.getCellData(recordIndex,"pstart");
	var pend = this.getCellData(recordIndex,"pend");
	var start = this.getCellData(recordIndex,"starts");
	var end = this.getCellData(recordIndex,"ends");
	var kind = this.getCellData(recordIndex,"kind");
	return pstart + '到' + pend + '<br><a href="javascript:void(0)" onclick="searchInfo(' + recordIndex + ');return false;">' + cellData + '次（' + start + '--' + end + '）' + kind + '列车</a>';
}

function trainSubT(cellData, recordIndex, columIndex){
	  var start = this.getCellData(recordIndex,"starts");
	  var end = this.getCellData(recordIndex,"ends");
	  var kind = this.getCellData(recordIndex,"kind");
	  return '<a href="javascript:void(0)" onclick="searchInfo(' + recordIndex + ');return false;">' + cellData + '次（' + start + '--' + end + '）' + kind + '列车</a>';
	}

function ftimeSub(cellData, recordIndex, columIndex){
	return cellData;
}

function etimeSub(cellData, recordIndex, columIndex){
	var day ;
  switch(this.getCellData(recordIndex,"day"))  {
    case 0 : day = "当天";break;
    case 1 : day = "次日";break;
    case 2 : day = "第三天";break;
    case 3 : day = "第四天";break;
    case 4 : day = "第五天";break;
    case 5 : day = "第六天";break;
    case 6 : day=  "第七天";break;
    case '0' : day = "当天";break;
    case '1' : day = "次日";break;
    case '2' : day = "第三天";break;
    case '3' : day = "第四天";break;
    case '4' : day = "第五天";break;
    case '5' : day = "第六天";break;
    case '6' : day=  "第七天";break;    
  }
	return day + cellData;
}

function opts(cellData, recordIndex, columIndex){
  var edit = '<a href="javascript:void(0)" onclick="searchInfo(' + recordIndex + ');return false;">详情</a>';
  return '<center>' + edit + '</center>';
}

function searchInfo(recordIndex){
	
	var url = "<%=contextPath %>/yh/core/funcs/utilapps/info/train/act/YHTrainAct/searchTrainInfoShow.act"
		
	var seqId = pageMgr.getCellData(recordIndex,"seqId");
	var train = pageMgr.getCellData(recordIndex,"train");
	var start = pageMgr.getCellData(recordIndex,"starts");
	var end = pageMgr.getCellData(recordIndex,"ends");
	var kind = pageMgr.getCellData(recordIndex,"kind");
	var ftime = pageMgr.getCellData(recordIndex,"ftime");
	var etime = pageMgr.getCellData(recordIndex,"etime");
	var distance = pageMgr.getCellData(recordIndex,"distance");
	var day = pageMgr.getCellData(recordIndex,"day");
	var pdistance = pageMgr.getCellData(recordIndex,"pdistance");
	var typePrice = pageMgr.getCellData(recordIndex,"typePrice");
	var pars = "seqId="+seqId+"&train="+train+"&start="+start+"&end="+end+"&kind="+kind+"&ftime="+ftime+
	           "&etime="+etime+"&distance="+distance+"&day="+day+"&pdistance="+pdistance+"&typePrice="+typePrice;
  if("${param.start}".trim() != "" || "${param.end}".trim() != ""){
	  pars += "&pstart="+pageMgr.getCellData(recordIndex,"pstart")+"&pend="+pageMgr.getCellData(recordIndex,"pend");
  }
	var json = getJsonRs(url,pars);
  if(json.rtState == "0"){
	  pars += "&countStation="+json.rtData.countStation+"&allPrice="+json.rtData.allPrice;
	  location = "<%=contextPath%>/core/funcs/utilapps/info/train/detail.jsp?"+pars;
  }
  else{
    alert("查询失败");
  }
}

</script>
</head>

<body class="bodycolor" topmargin="5" onload="doInit()">

<br>
<div align="center" id="listDiv"></div>

<br>
<div align="center">
 <input type="button"  value="返回" class="BigButton" onClick="history.go(-1);">
</div>

</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>双语标示查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<style>
body{
  text-align:center;
}

a {
  display:block;
  margin:0px 33px;
  padding-top:2px;
  outline-style:none;
}

a:link,a:visited,a:active,a:hover {
  text-decoration: none;
  color:#000
}

a img{
  float:left;
  border:none;
}

a span{
  float:left;
  display:block;
  border:none;
  cursor:pointer;
}

.pgPanel{
  padding-left: 50px; 
  width: 822px;
  text-align:center;
  border-left: 1px solid rgb(210, 210, 210); 
  border-right: 1px solid rgb(210, 210, 210);
  border-bottom:1px solid #d2d2d2;
}

#dataHeader{
  height:0px;
  border:none;
  visibility:hidden;
}

.red_header{
  overflow:hidden;
  height:10px;
  width:906px;
  background: url(<%=imgPath%>/subsys/red_header.jpg)  no-repeat left top;
}

.table_header{
  height:33px;
  width:874px;
  background: url(<%=imgPath%>/subsys/table_header.jpg)  no-repeat left top;
}

.pgTable{
}

.header_logo{
  height:47px;
  width:196px;
  background: url(<%=imgPath%>/subsys/header_logo.jpg)  no-repeat left top;
}

.TableTr{
  height:40px;
  font-size:15px;
  color:：#d2d2d2;
  font-weight:bold;
  border-width:0 0 1px;
  background: url(images/th.jpg) repeat-x left top;
}

.TableLine1 {
	background-color:#F6F6F6;
	border-color:#0E6F67;
	border-width:0 0 0;
}

.TableLine2 {
	background-color:#FFFFFF;
	border-color:#0E6F67;
	border-width:0 0 0;
}

table tr{
	background-color:#F6F6F6;
	border-color:#0E6F67;
	border-style:solid;
}

table{
  border-spacing:0;
  border-collapse:collapse;
  line-height:25px;
  font-size:9pt;
  border-left:1px solid #d2d2d2;
  border-right:1px solid #d2d2d2;
}
</style>
<script type="text/javascript">
var pageMgr;

document.onresize = function(){
  alert(1);
}

function pgPanelResize(){
  $$('.pgPanel')[0].setStyle({
    'position':'relative',
    'top': $('dataTable').getHeight() + 'px'
  });
}

function doInit(){
  
  var para = "?cnName=${param.cnName}&enName=${param.enName}&type=${param.type}";
  var requestURL = "<%=contextPath%>/yh/subsys/infomgr/bilingual/act/YHBilingualAct/searchPage.act" + para;
  var cfgs = {
    dataAction: requestURL,
    container: "listDiv",
    colums: [
       {type:"hidden", name:"seqId", text:"ID"},
       {type:"text", name:"type", text:"", width:100,render:typeRender,align:'center'},
       {type:"text", name:"cnName", text:"", width:254,align:'center',render:nameRender},
       {type:"text", name:"enName", text:"", width:390,align:'center',render:nameRender},
       {type:"text", name:"soundFile", text:"", width:120,align:'center',render:soundRender}
       ]
  };
  
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();

  $('dataTable').setStyle({
    'position':'relative',
    'top':'-25px',
    'width':'auto'
  });

  $('dataTable').onresize = pgPanelResize;

  pgPanelResize();
}

function nameRender(cellData, recordIndex, columInde){
  return '<font color="#0a8530">' + cellData + '</font>';
}

function typeRender(cellData, recordIndex, columInde){
  switch(cellData){
  case '0':return "<font color='#585858'>职务职称</font>";
  case '1':return "<font color='#585858'>菜谱</font>";
  case '2':return "<font color='#585858'>标识标准</font>";
  default:return "";
  };
}

function enableRender(cellData, recordIndex, columInde){
  switch(cellData){
  case '0':return "<font color='red'>不启用</font>";
  case '1':return "启用";
  default:return "";
  };
}

function soundRender(cellData, recordIndex, columInde){
  if (cellData){
    var renderData = '<a onclick="playSound(' + recordIndex + ');return false;" href="javascript:void(0)"><img src="<%=imgPath%>/subsys/sound.gif"></img><span>&nbsp;<u>试听</u></span></a>';
    return renderData;
  }
  else{
    var renderData = '<a onclick="return false;" href="javascript:void(0)"><img src="<%=imgPath%>/subsys/sound.gif"></img><span>&nbsp;无</span></a>';
    return renderData;
  }
}

function playSound(recordIndex){
  var snd = $('snd');
  var soundSrc = pageMgr.getCellData(recordIndex, "soundFile");
  if (soundSrc != '' && typeof soundSrc != undefined){
    snd.src = '<%=contextPath%>/bilingual/' + soundSrc.replace(/^[\s\S]+\\/g, "");
  }
}
</script>
</head>
<body onload="doInit()">
	<bgsound border="0" id="snd" loop="0" src=""></bgsound>
	<center>
		<div style="text-align:left;width:906px; border: 1px solid rgb(255, 45, 45)">
			<div class="red_header">
			</div>
			<div style="margin:0px 16px;">	
				<div class="header_logo"></div>
				<div class="table_header">
			  </div>
			  <div id="listDiv" align="center"></div>
		  </div>
		  <div style="height:20px"></div>
		</div>
	</center>
</body>
</html>
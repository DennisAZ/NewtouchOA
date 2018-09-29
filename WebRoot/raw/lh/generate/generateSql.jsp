<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String seqId = YHUtility.null2Empty(request.getParameter("seqId"));
String tableName =  YHUtility.null2Empty(request.getParameter("tableName"));
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/page.css" type="text/css" >
<script type="text/Javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<title>反向生成数据库表创建文件</title>
<script type="text/javascript">
var requestUrl = contextPath + "/raw/lh/generate/act/YHSysTableAct";
var seqId = "<%=seqId %>";
var tableName = "<%=tableName %>";
var pageMgr = null;
var cfgs = null;
function doInit() {
  var url = requestUrl + "/getTableField.act?seqId=" + seqId;
  cfgs = {
      dataAction: url,
      container: "datalist",
      showRecordCnt: true,
      colums: [
         {type:"hidden", name:"seqId"},
         {type:"data", name:"fieldName" , width:'50%', text:"属性名称"},
         {type:"data", name:"fieldType", text:"属性类型" , render:typeRender},
         {type:"data", name:"fieldLength", text:"属性长度"}
         ]
    };
  doQuery();
}

function typeRender(cellData, recordIndex, columIndex) {
  var tip = "文本";
  if (cellData == '2') {
    tip = "整型";
  } else if (cellData == '3') {
    tip = "实数";
  } else if (cellData == '4') {
    tip = "日期";
  } 
  return tip;
}
function doQuery(){
  if(!pageMgr){
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
  }else{
    pageMgr.search();
  }
}
function explort(){
  var url =  contextPath + "/raw/lh/generate/act/YHGenerateAct/explortSql.act?1=1" ;
  url += "&seqId=<%=seqId %>&tableName=" +encodeURIComponent(tableName) ;
  $('f1').action = url;
  $('f1').submit();
}
function createTable(){
  var url =  contextPath + "/raw/lh/generate/act/YHGenerateAct/createTable.act?1=1" ;
  url += "&seqId=" + seqId + "&tableName=" +encodeURIComponent(tableName)  ;
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == '0') {
    alert("表生成功能！");
  }
}
</script>
</head>
<body  onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle">反向生成数据库表创建文件
    </td>
  </tr>
</table>
<form id="f1" method="post">
 <table  class="TableBlock"   width="70%"  align="center">
	   <tr>
	    <td colspan="2"  class="TableData">表名：<%=tableName %></td>
	  </tr>
    <tr>
    <td colspan="2"  class="TableData">
    <div id="datalist"></div>
    </td>
    </tr>
	  <tr>
	    <td  class="TableData">选择要生成的sql格式：</td>
	    <td  class="TableData">
	      <label for="dialect_1">Orcale</label><input type="checkbox" name="dialect" id="dialect_1" value="Oracle" checked>
          <label for="dialect_2">MySql</label><input type="checkbox" name="dialect"  id="dialect_2" value="MySql">
          <label for="dialect_3">MsSql</label><input type="checkbox" name="dialect"  id="dialect_3" value="MsSql">
	    </td>
	  </tr>
    <tr><td  colspan="2"  class="TableData" align=center>
    <input type="button" value="导出sql" onclick="explort()" class="BigButton"></input>
    <input type="button" value="生成表" onclick="createTable()" class="BigButton"></input>
     <input type="button" onclick="location.href='<%=contextPath %>/raw/lh/generate/index.jsp'"  value="返回" class="BigButton">
    </td>
    </tr>
  </table>
</form>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/rad/CodeUtil/curd/code.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/rad/codeSel/codeSel.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/rad/grid/grid.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/rad/CodeUtil/openWin.js" ></script>
<script type="text/javascript">
var pagesLoadUrl = '<%=contextPath%>/yh/rad/velocity/act/YHCodeUtilAct/showField.act';
var param ="";
function doInit(){
var jso = [
           {title:"实体类配置",onload:showUserInfo.bind(window, "pojo"),useTextContent:true, contentUrl:"/yh/rad/CodeUtil/curd/pojo.jsp", imgUrl:"/yh/raw/ljf/imgs/1news.gif", useIframe:false}
           ,{title:"ACT类配置",onload:showUserInfo.bind(window, "act"),useTextContent:true, contentUrl:"/yh/rad/CodeUtil/curd/act.jsp", imgUrl:"/yh/raw/ljf/imgs/1news.gif", useIframe:false}
           ,{title:"页面信息类配置",onload:showUserInfo.bind(window, "curd"),useTextContent:true, contentUrl:"/yh/rad/CodeUtil/curd/pages.jsp", imgUrl:"/yh/raw/ljf/imgs/1news.gif", useIframe:false}
           ,{title:"字段信息配置",onload:refrcahData.bind(window, "f"),useTextContent:true, contentUrl:"/yh/rad/CodeUtil/curd/fileds.jsp", imgUrl:"/yh/raw/ljf/imgs/1news.gif", useIframe:false}
           ];
//loadData();
buildTab(jso, 'contentDiv');
}
function showUserInfo() {
  //alert("sss");
  //if (!userInfo) {
   // return;
  //}
  //var infoColum = arguments[0];  
  //param += $('form1').serialize() ;  
  //bindJson2Cntrl(userInfo, infoColum);
  try{
    $(fieldValue).value = getFieldConfig('fieldTab');
  }catch(e) {

  }
  
}
function showField() {
  var tabNo =  $('tempTabNo').value;
  loadData('fieldTab',pagesLoadUrl,tabNo);
  try{
    $(fieldValue).value = getFieldConfig('fieldTab');
  }catch(e) {
  }
}
function refrcahData() {
  var tabNo =  $('tempTabNo').value;
  var tab = $('fieldTab');
  var index = tab.rows.length;
  var j = 0;
  for(var i = 1; i < index ; i++){
    tab.deleteRow(1);
    //j = i;
   }
  //alert("j : " + j);
  var keys = selectHashMap.keys();
  for(var i = 0 ; i < keys.length ; i++){
    selectHashMap.unset(keys[i]);
    selectShowHashMap.unset(keys[i]);
  } 
  var rkeys = radioHashMap.keys();
  for(var i = 0 ; i < keys.length ; i++){
    radioHashMap.unset(keys[i]);
    radioShowHashMap.unset(keys[i]);
  } 
  loadData('fieldTab',pagesLoadUrl,tabNo);
}
function doSubmit(){
  var url = '<%=contextPath%>/yh/rad/velocity/act/YHCodeUtilAct/code2java.act';
  //alert($('form1').serialize());
  //alert($('tempTabNo').value);
  var param = toSelQueryString();
  alert(param);
  var rtJson = getJsonRs(url, mergeQueryString($('form1'),param)) ;
  alert(rtJson.rtMsrg);
}
</script>
<title>代码自动生成页面</title>
</head>
<body onload="doInit()">
<input type="button" value="test" onclick="doSubmit()">
<input type="hidden" value="" id="projectSrcUrl">
<input type="hidden" value="" id="projectWebRootUrl">
<form id="form1" name="form1">
<div id="contentDiv">
</div>
<input type="hidden" value="" id="tempTabNo">
</form>
</body>
</html>
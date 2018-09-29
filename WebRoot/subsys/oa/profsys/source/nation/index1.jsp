<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>国家基本信息</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tree.css">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
function showInfo(seqId) {
  var url = "<%=contextPath%>/subsys/oa/profsys/source/nation/info.jsp?seqId=" + seqId;
  var myleft = (screen.availWidth - 800)/2;
  window.open(url,"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=800,height=500,left=" + myleft + ",top=50");
}
function updateInfo(seqId) {
  var url = "<%=contextPath%>/subsys/oa/profsys/source/nation/news/update.jsp?seqId=" + seqId;
  window.location = url;
}
function deleteInfo(seqId) {
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/source/nation/act/YHSourceNationAct/deleteInfo.act?seqId=" + seqId;
  if (window.confirm("确认要删除该记录吗？")) {
    window.location = url;
    location.reload();
  }
}
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/source/nation/act/YHSourceNationAct/selectNation.act";
   cfgs = {
    dataAction: url,
    container: "giftList",
    paramFunc: getParam,
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"natNum", text:"国家编号", width: "7%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"natName", text:"国家名称", width: "6%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"natStatus", text:"国家情况", width: "7%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"natCustom", text:"风土人情", width: "6%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"natBackground", text:"政治背景",align:"center", width: "6%",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"caozuo",text:"操作", width:"7%",align:"center",render:toCaoZuo}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;//是否有数据
  if(total <= 0){
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无合条件的信息!</div></td></tr>"
        );
    $('giftList').style.display="none";
    $('returnNull').update(table); 
  }
}
//操作
function toCaoZuo(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  return    "<a href='javascript:showInfo(" + seqId + ");'>详细信息 </a>&nbsp;"
           + "<a href='javascript:updateInfo(" + seqId + ");'>修改</a>&nbsp;"
           + "<a href='javascript:deleteInfo(" + seqId + ");'> 删除 </a>";
}
function getParam(){
  queryParam = $("form1").serialize();
  return queryParam;
}
//查询
function queryGift(){
  if(!pageMgr){
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
  }else{
    pageMgr.search();
  }
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    $('giftList').style.display="";
    $('returnNull').style.display="none"; 
  }else{
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无合条件的信息!</div></td></tr>"
        );
    $('giftList').style.display="none";
    $('returnNull').style.display=""; 
    $('returnNull').update(table);  
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5px" onLoad="doInit()">
<br>
<form id="form1" name="form1">
<table class="TableList" align="center">
  <tr class="TableContent">
    <td nowrap style="padding:3px">
         &nbsp;国家编号：
          <INPUT type="text"  name="natNum" id="natNum" size="10" class="BigInput">
         &nbsp;国家情况：
          <INPUT type="text"  name="natStatus" id="natStatus" size="10" class="BigInput">
         &nbsp;政治背景：
          <INPUT type="text"  name="natBackground" id="natBackground" size="10" class="BigInput">
      </td>
  </tr>
  <tr class="TableContent">
    <td nowrap>
    &nbsp;国家名称：
    <INPUT type="text"  name="natName" id="natName" size="10" class="BigInput">
    &nbsp;风土人情：
     <INPUT type="text"  name="natCustom" id="natCustom" size="10" class="BigInput">
    </td>
   </tr>
  <tr class="TableControl">
    <td rowspan=2 align="center">
      <input value="查询" type="button" class="BigButton" onClick="queryGift();" title="查询" name="button">&nbsp;&nbsp;
    </td>
  </tr>   
</table>
</form>
<br>
<div id="giftList" style="padding-left: 10px; padding-right: 10px"></div>
<div id="returnNull">
</div>
</body>
</html>
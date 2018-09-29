<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>友好人士信息</title>
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
function doTime() {
//时间
  var parameters = {
      inputId:'perBirthday',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(parameters);
  var parameters = {
      inputId:'perBirthday1',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
  };
  new Calendar(parameters);
}
function showInfo(seqId) {
  var url = "<%=contextPath%>/subsys/oa/profsys/source/person/info.jsp?seqId=" + seqId;
  var myleft = (screen.availWidth - 800)/2;
  window.open(url,"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=800,height=500,left=" + myleft + ",top=50");
}
function updateInfo(seqId) {
  var url = "<%=contextPath%>/subsys/oa/profsys/source/person/news/update.jsp?seqId=" + seqId;
  window.location = url;
}
function deleteInfo(seqId) {
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/source/person/act/YHSourcePersonAct/deleteInfo.act?seqId=" + seqId;
  if (window.confirm("确认要删除该记录吗？")) {
    window.location = url;
    location.reload();
  }
}
function doInit(){
  doTime();
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/source/person/act/YHSourcePersonAct/selectPerson.act";
   cfgs = {
    dataAction: url,
    container: "giftList",
    paramFunc: getParam,
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"perNum", text:"人员编号", width: "7%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"perName", text:"姓名", width: "6%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"perSex", text:"性别", width: "7%",align:"center",render:toSex,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"perNation", text:"国籍", width: "6%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"perPosition", text:"职务",align:"center", width: "6%",render:toUser,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"perVocation", text:"职业",align:"center", width: "7%",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"perBirthday", text:"出生日期",align:"center",width:"7%",render:toBirth,sortDef:{type:0, direct:"asc"}},
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
//职位
function toUser(cellData,recordIndex,columIndex){
  var perPosition = this.getCellData(recordIndex,"perPosition");
  var seqId = "?seqId=" + perPosition;
  var requestUrl = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectMemAct/userName.act" + seqId;
  var rtJson = getJsonRs(requestUrl);
  var userList = rtJson.rtData;
  return userList;
}
//男女
function toSex(cellData, recordIndex, columInde){
  var perSex = this.getCellData(recordIndex,"perSex");
  var sexStr = "";
  if (perSex == 1) {
    sexStr = "男";
  }
  if (perSex == 2) {
    sexStr = "女";
  }
  return sexStr;
}
//生日
function toBirth(cellData, recordIndex, columInde){
  var perBirthday = this.getCellData(recordIndex,"perBirthday");
  return perBirthday.substr(0,10);
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
  if(!isValidDateStr($("perBirthday").value) && $("perBirthday").value != ""){
    alert("日期格式不对，应形如 1999-01-01"); 
    $("perBirthday").focus(); 
    $("perBirthday").select(); 
    return ; 
  }
  if(!isValidDateStr($("perBirthday1").value)  && $("perBirthday1").value != ""){
    alert("日期格式不对，应形如 1999-01-01"); 
    $("perBirthday1").focus(); 
    $("perBirthday1").select(); 
    return ; 
  }
  
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
<form id="form1" method="post" name="form1">
<table class="TableList" align="center">
  <tr class="TableContent">
    <td nowrap style="padding:3px">
         &nbsp;人员编号：
          <INPUT type="text" value=""  name="perNum"  id="perNum" size="10" class="BigInput">
         &nbsp;国籍：
        <INPUT type="text"  value="" name="perNation" id="perNation" size="10" class="BigInput">
         &nbsp;职务：
       <input type="hidden" class="BigInput" name="perPosition" id="perPosition" size=10 readonly>
       <textarea cols=20 name="perPositionName" id="perPositionName" rows=1 class="BigStatic" wrap="yes" readonly></textarea>
         
      <a href="javascript:;" class="orgAdd" onClick="selectRole(['perPosition','perPositionName']);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('perPosition').value='';$('perPositionName').value='';">清空 </a>
      &nbsp;职业：
        <INPUT type="text"  value="" name="perVocation" id="perVocation" size="10" class="BigInput">   
      </td>
  </tr>
  <tr class="TableContent">
    <td nowrap>
    &nbsp;姓名：
    <INPUT type="text" value="" name="perName" id="perName" size="10" class="BigInput">
    &nbsp;性别：
      <select name="perSex" id="perSex">
       <option value="">请选择类别</option>
       <option value="1">男</option>
<option value="2">女</option>
      </select>
    &nbsp;出生日期：
     <INPUT type="text" name="perBirthday" id="perBirthday" class=BigInput size="10" value="">
        <img src="<%=imgPath%>/calendar.gif" id="date1"  align="absMiddle" border="0" style="cursor:pointer">
     &nbsp;至
     <INPUT type="text" name="perBirthday1" id="perBirthday1" class=BigInput size="10" value="">
        <img src="<%=imgPath %>/calendar.gif" id="date2" align="absMiddle" border="0" style="cursor:pointer">
    </td>
    </tr>
  <tr class="TableControl">
    <td rowspan=2 align="center">
      <input value="查询" class="BigButton" type="button" onClick="queryGift();" name="button">&nbsp;&nbsp;
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
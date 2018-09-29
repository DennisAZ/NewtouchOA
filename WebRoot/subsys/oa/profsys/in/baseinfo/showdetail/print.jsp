<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.subsys.oa.profsys.data.YHProject"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
List<YHProject> list = (List<YHProject>)request.getAttribute("project");
String printStr = (String)request.getAttribute("printStr");
YHProject project = new YHProject();
int sun = 0;//总人数

int pYx = 0;//友协人数
int pGuest = 0;//外宾人数;
int pCouncil = 0;//理事人数
int countryTotal = 0;//国家总数
String country = "";
%>
<html>
<head>
<title>出访信息打印&nbsp;&nbsp;</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tree.css">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<style media = print>  
.Noprint  {display:none;}  
.PageNext  {page-break-after: always;}  
 </style>  
<script language="JavaScript"> 
function exportExcel(){
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHProjectAct/exportXls.act?printStr=" + '<%=printStr%>';
  window.location = url;
}
function doInt() {
  pagesetupNull();
  var length = document.getElementById("listSize").value;
  for (var i = 0; i < length; i++) {
    var gropType = "projGropu" + i + "Type";
    if(document.getElementById(gropType).value.trim() != ""){
      bindDesc([{cntrlId:gropType, dsDef:"BUDGET_APPLY,SEQ_ID,BUDGET_ITEM"}]);
    }
    var visiType = "visit" + i + "Type";
    if(document.getElementById(visiType).value.trim() != ""){
      bindDesc([{cntrlId:visiType, dsDef:"CODE_ITEM,SEQ_ID,CLASS_DESC"}]);
    }
    var activeType = "active" + i + "Type";
    if(document.getElementById(activeType).value.trim() != ""){
      bindDesc([{cntrlId:activeType, dsDef:"CODE_ITEM,SEQ_ID,CLASS_DESC"}]);
    }
    var projleader = "proj" + i + "leader";
    if(document.getElementById(projleader).value.trim() != ""){
      bindDesc([{cntrlId:projleader, dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
  }
}

var hkey_root,hkey_path,hkey_key
hkey_root="HKEY_CURRENT_USER"
hkey_path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\"
//设置网页打印的页眉页脚为空

function pagesetupNull(){
try{
var RegWsh = new ActiveXObject("WScript.Shell")
hkey_key="header" 
RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"")
hkey_key="footer"
RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"")
}catch(e){}
}
</script>
</head>
<body class="bodycolor" style="margin:0;padding:0;" onload="doInt()">
<input type="hidden" id="listSize" name="listSize" value="<%=list.size()%>">
 <center  class = " Noprint ">  
 <p>  
 <OBJECT id = WebBrowser classid = CLSID:8856F961 - 340A - 11D0 - A96B - 00C04FD705A2 height = 0  width = 0>  
 </OBJECT>  
 <font color='red'>打印此界面时请先打印预览->页面设置->页眉页脚全设置为空（此行文字不会显示在打印效果中）

 <input type='button' value='  导出到EXCEL  ' onClick="javascript:exportExcel()"></font>  
 <hr align = "center"  width = " 90% "  size = " 1 "  noshade>  
 </center>  
 <%if (list.size() > 0) {
 %>
<div style="overflow:auto;" SCROLL=auto align='center'>
 <%
 for (int i = 0; i < list.size(); i++) {
   project = list.get(i);
   sun = sun + project.getPTotal();
   pYx = pYx + project.getPYx();
   pCouncil = pCouncil + project.getPCouncil();
   pGuest = pGuest + project.getPGuest();
   countryTotal = countryTotal + project.getCountryTotal();
   country = country + project.getPurposeCountry();
 }
 %>
<table width='88%' cellspacing="1"   cellpadding="1">
<tr>
  <td align=center><font size='5'><b>出访统计表</b></font></td>
</tr>
</table>
<table width='88%' cellspacing="1"   cellpadding="1">
<tr>
  <td align=left><font size='3'><b>&nbsp;&nbsp;&nbsp;出访:<%=list.size() %>个团组，<%=sun%>人次</b></font></td>
</tr>
</table>
 
<table border="1" bordercolor="#000000" style="border:1px solid #000;margin:auto;width:89%;border-collapse:collapse;">
  <tr height=33>
    <td width='3%' rowspan="2" align='center' nowrap>序<br><br>号

    </td>
    <td width='10%' rowspan="2" align='center' nowrap>时间
    </td>
    <td width='14%' rowspan="2" align='center' nowrap>团组名称
    </td>
    <td width='8%' rowspan="2" align='center' nowrap>出访类别
    </td>
    <td width='8%' rowspan="2" align='center' nowrap>项目类别
    </td>
    <td width='8%' rowspan="2" align='center' nowrap>国家数

    </td>
    <td width='12%' rowspan="2" align='center' nowrap>国家名

    </td>
    <td width='8%' rowspan="2" align='center' nowrap>负责人

    </td>
    <td width='30%' colspan="4" align='center' nowrap>活动人数
    </td>
  </tr>
  <tr height=33>
  <td align="center" nowrap>总人数</td>
  <td align="center" nowrap>理事人数</td>
  <td align="center" nowrap>外宾人数</td>
  <td align="center" nowrap>友协人数</td>
</tr>
<% for (int i = 0; i < list.size(); i++) {
  project = list.get(i);
%>
    <tr height=33>
      <td nowrap align="center"><%=i+1%></td>
       <td nowrap align="center"><%=project.getProjStartTime().toString().substring(5,7)%>月<%=project.getProjStartTime().toString().substring(8,10)%>日 至

       <%=project.getProjEndTime().toString().substring(5,7)%>月<%=project.getProjEndTime().toString().substring(8,10)%>日</td>
       <td align="left" id="projGropu<%=i%>TypeDesc">
       <input type="hidden" id="projGropu<%=i%>Type" name="projGropu<%=i%>Type" value="<%=project.getBudgetId()%>">
       </td>
      <td nowrap align="center" id="visit<%=i%>TypeDesc">
      <%
      if (YHUtility.isNullorEmpty(project.getProjVisitType())) {
      %>
      <input type="hidden" id="visit<%=i%>Type" name="visit<%=i%>Type" value="0">
      <%} else {%>
      <input type="hidden" id="visit<%=i%>Type" name="visit<%=i%>Type" value="<%=project.getProjVisitType()%>">
      <%} %>
      </td>
       <td nowrap align="center" id="active<%=i%>TypeDesc">
       <%
      if (YHUtility.isNullorEmpty(project.getProjActiveType())) {
      %>
       <input type="hidden" id="active<%=i%>Type" name="active<%=i%>Type" value="0">
      <%} else {%>
      <input type="hidden" id="active<%=i%>Type" name="active<%=i%>Type" value="<%=project.getProjActiveType()%>">
      <%} %>
      </td>
       <td nowrap align="center"><%=project.getCountryTotal()%></td>
       <td align="left"><%=project.getPurposeCountry()%></td>
      <td nowrap align="center" id="proj<%=i%>leaderDesc">
       <input type="hidden" id="proj<%=i%>leader" name="proj<%=i%>leader" value="<%=project.getProjLeader()%>">
      </td>
      <td nowrap align="center"><%=project.getPTotal()%></td>
      <td nowrap align="center"><%=project.getPCouncil() %></td>
      <td nowrap align="center"><%=project.getPGuest()%></td>
      <td nowrap align="center"><%=project.getPYx()%></td>
    </tr>
    <%} %>
<tr height=33>
      <td nowrap align="center">合计</td>
       <td nowrap align="center"></td>
       <td align="left"></td>
      <td nowrap align="center"></td>
      <td nowrap align="center"></td>
       <td nowrap align="center"><%=countryTotal%></td>
       <td align="left"><%=country%></td>
      <td nowrap align="center"></td>
      <td nowrap align="center"><%=sun %></td>
      <td nowrap align="center"><%=pCouncil%></td>
      <td nowrap align="center"><%=pGuest%></td>
      <td nowrap align="center"><%=pYx%></td>    
    </tr>
</table>
</div>
<%}%>
</body>
</html>
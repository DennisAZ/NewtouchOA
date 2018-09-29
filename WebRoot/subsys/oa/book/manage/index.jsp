<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.subsys.oa.book.data.*"%>
<%@ page  import="java.util.List"%>
<html>
<head>
<title>新建图书</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" type="text/css" href="/inc/js/jquery/page/css/page.css"/>
<link rel="stylesheet" type="text/css" href="/theme/<?=$LOGIN_THEME?>/calendar.css"/>
<style>
.tip {position:absolute;display:none;text-align:center;font-size:9pt;font-weight:bold;z-index:65535;background-color:#DE7293;color:white;padding:5px}
.auto{text-overflow:ellipsis;white-space:nowrap;overflow:hidden;}
</style>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
 <script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
 <script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"><!--
function newbook(){
  window.location.href= contextPath + "/yh/subsys/oa/book/act/YHBookTypeAct/selectBookType.act"
}
/*
function LoadWindow2(){
	 var URL= contextPath + "/subsys/oa/book/borrow_manage/borrow/bookno_select/index.jsp";
	 var loc_x=document.body.scrollLeft+event.clientX-event.offsetX-100;
	 var loc_y=document.body.scrollTop+event.clientY-event.offsetY+170;
   window.showModalDialog(URL,self,"edge:raised;scroll:0;status:0;help:0;resizable:1;dialogWidth:320px;dialogHeight:245px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px"); 
}
*/
function LoadWindow2(event)
{
  //URL= contextPath + "/subsys/oa/book/query/queryindex.jsp";
  //loc_x=document.body.scrollLeft+event.clientX-event.offsetX-100;
  //loc_y=document.body.scrollTop+event.clientY-event.offsetY+170;


  event = event || window.event;
  var URL= contextPath + "/subsys/oa/book/query/queryindex.jsp";
  loc_x=document.body.scrollLeft+event.clientX-event.offsetX-100;
  loc_y=document.body.scrollTop+event.clientY-event.offsetY+170;
  window.showModalDialog(URL,self,"edge:raised;scroll:0;status:0;help:0;resizable:1;dialogWidth:500px;dialogHeight:320px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px");
}
function export_csv(){
  var typeId = $("typeId").value;
  var lend = $("lend").value; 
  var bookName = $("bookName").value;
  var bookNo = $("bookNo").value; 
  var author = $("author").value;
  var isbn = $("isbn").value;
  var pub_house = $("pub_house").value;
  var area = $("area").value;
  var orderflag = $("orderflag").value;
  var parameter ="typeId="+typeId+"&lend="+lend+"&bookName="+bookName+"&bookNo="+bookNo+"&author="+author+"&isbn="+isbn+"&pub_house="+pub_house+"&area="+area+"&orderflag"+orderflag;
  var src=contextPath+"/yh/subsys/oa/book/act/YHBookTypeAct/SysExport.act?"+parameter;
  document.form1.action=src;
  document.form1.submit();
  return true;
}
/*
 *图书导入 
 */
function importBook(){
  window.location.href= contextPath+ "/subsys/oa/book/manage/import.jsp";
}
function findBookInfo(){
  ///yh/subsys/oa/book/act/YHBookTypeAct/findBooks.act
  var typeId = $("typeId").value;
  var lend = $("lend").value; 
  var bookName = $("bookName").value;
  var bookNo = $("bookNo").value; 
  var author = $("author").value;
  var isbn = $("isbn").value;
  var pub_house = $("pub_house").value;
  var area = $("area").value;
  var orderflag = $("orderflag").value;
  var parameter ="typeId="+typeId+"&lend="+lend+"&bookName="+bookName+"&bookNo="+bookNo+"&author="+author+"&isbn="+isbn+"&pub_house="+pub_house+"&area="+area+"&orderflag"+orderflag;
  var src = contextPath+"/yh/subsys/oa/book/act/YHBookTypeAct/findBooks.act?"+parameter;
  document.form1.action = src;
  document.form1.submit();
  return true;
}

--></script>
<% 
     List<YHBookType> booktype = (List<YHBookType>)request.getAttribute("booktype");
  %>
</head>
<body topmargin="5" class="bodycolor">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 新建图书 </span><br>
    </td>
  </tr>
</table>
<div align="center">
	<input type="button"  value="新建图书" class="BigButton" onClick="javascript:newbook();return false;" title="添加新的图书">&nbsp;&nbsp;
	<input type="button"  value="导入图书" class="BigButton" onClick="javascript:importBook();return false;" title="导入图书">
</div>
 <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_open.gif" align="absmiddle"><span class="big3"> 管理图书</span>
    </td>
  </tr>
</table>
 <div align="left">
 <form action="#"  method="get" name="form1" id="form1"> 
<table class="TableBlock" width="450" align="center" >
   
   <tr>
    <td nowrap class="TableData">图书类别：</td>
    <td class="TableData">
        <select name="typeId" id="typeId">
          <option value="all">所有</option>
           <%
     if(booktype!=null && booktype.size()>0){
       for(int i = 0; i < booktype.size(); i++){
   %>
   <option value="<%=booktype.get(i).getSeqId()%>"><%=booktype.get(i).getTypeName()%></option>
   <%
       }
     }
   %>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData" width="120">借阅状态：</td>
    <td nowrap class="TableData">
       <select name="lend" id="lend">
          <option value="" selected>请选择 </option>
          <option value="0">未借出 </option>
          <option value="1" >已借出 </option>
       </select>
    </td>   
   </tr>   
   <tr>
    <td nowrap class="TableData">书名： </td>
    <td nowrap class="TableData">
        <input type="text" name="bookName" id="bookName" class="BigInput" size="33" maxlength="100">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">图书编号：</td>
    <td class="TableData">
      <input type="text" name="bookNo" id="bookNo" class="BigStatic" size="26" maxlength="100" readonly value="">&nbsp;
      <input type="button" value="选 择" class="SmallButton" onClick="LoadWindow2(this)" title="选择图书编号" name="button">
    </td> 
   </tr>
   <tr>
    <td nowrap class="TableData">作者： </td>
    <td nowrap class="TableData">
        <input type="text" name="author" id="author" class="BigInput" size="33" maxlength="100">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">ISBN号： </td>
    <td nowrap class="TableData">
        <input type="text" name="isbn" id="isbn" class="BigInput" size="33" maxlength="100">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">出版社： </td>
    <td nowrap class="TableData">
        <input type="text" name="pub_house" id="pub_house" class="BigInput" size="33" maxlength="100">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">存放地点： </td>
    <td nowrap class="TableData">
        <input type="text" name="area" id="area" class="BigInput" size="33" maxlength="100">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">排序字段：</td>
    <td nowrap class="TableData">
        <select name="orderflag" id="orderflag">
          <option value="DEPT">部门 </option>
          <option value="TYPE_ID">类别 </option>
          <option value="BOOK_NAME">书名 </option>
          <option value="AUTHOR">作者 </option>
          <option value="PUB_HOUSE">出版社 </option>
        </select>
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="button" value="查询" class="BigButton" title="模糊查询" onClick="findBookInfo();" name="button">&nbsp;&nbsp;
        <input type="button"  value="导出" class="BigButton" onClick="export_csv();">
    </td>
   </tr>
   <!--  
   <tr><td nowrap class="TableControl" colspan="2"></td></tr>
   -->
   <input type="hidden" name="toId" value=""> 
  
</table>
</form>
</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*"%>
<%@ page  import="yh.subsys.oa.book.data.*"%> 
<html>
<head>
<title>图书信息查询 </title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
function LoadWindow2(event)
{
	  event = event || window.event;
	  var URL= contextPath + "/subsys/oa/book/query/queryindex.jsp";
	  loc_x=document.body.scrollLeft+event.clientX-event.offsetX-100;
	  loc_y=document.body.scrollTop+event.clientY-event.offsetY+170;
  
  window.showModalDialog(URL,self,"edge:raised;scroll:0;status:0;help:0;resizable:1;dialogWidth:320px;dialogHeight:245px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px");
}

/**
 * 查找图书类型
 */
function typeAjax(){
  var url = contextPath + "/yh/subsys/oa/book/act/YHBookQueryAct/findBookTypes.act";
  var rtjson = getJsonRs(url);
  if(rtjson.rtState == '0'){
    var catelogies = rtjson.rtData;   
    var sel = document.getElementById("typeId");
  	for(var i=0; i<catelogies.length; i++){
  	  sel.options.add(new Option(catelogies[i].typeName, catelogies[i].seqId));  	  
  	}    
  }
}

function doInit(){
  typeAjax();
}
</script>
</head>
<body  topmargin="5"  onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%= imgPath %>/infofind.gif" align="absmiddle"><span class="big3"> 图书信息查询 </span>
    </td>
  </tr>
</table>
<br>
  <form action="<%=contextPath%>/yh/subsys/oa/book/act/YHBookQueryAct/findBooks.act"  method="post" name="form1" id="form1">  
  <table class="TableBlock"  width="450" align="center" >
   <tr>
    <td nowrap class="TableData">图书类别：</td>
    <td nowrap class="TableData">
        <select name="typeId" id="typeId">  
          <option value="all">所有</option>        
        </select>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData" width="120">借阅状态：</td>
    <td nowrap class="TableData">
       <select name="lend">
          <option value="" selected>请选择 </option>
          <option value="0">未借出 </option>
          <option value="1" >已借出 </option>
       </select>
    </td>
   </tr>       
   <tr>
    <td nowrap class="TableData">书名： </td>
    <td nowrap class="TableData">
        <input type="text" name="bookName" class="BigInput" size="25" maxlength="100">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">图书编号：</td>
    <td class="TableData">
      <input type="text" name="bookNo" class="BigStatic" size="18" maxlength="100" readonly value="">&nbsp;
      <input type="button" value="选 择" class="SmallButton" onClick="LoadWindow2(this)" title="选择图书编号" name="button">
    </td> 
   </tr>
   <tr>
    <td nowrap class="TableData">作者： </td>
    <td nowrap class="TableData">
        <input type="text" name="author" class="BigInput" size="25" maxlength="25">&nbsp;        
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">ISBN号： </td>
    <td nowrap class="TableData">
        <input type="text" name="isbn" class="BigInput" size="25" maxlength="50">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">出版社： </td>
    <td nowrap class="TableData">
        <input type="text" name="pub_house" class="BigInput" size="25" maxlength="100">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">存放地点： </td>
    <td nowrap class="TableData">
        <input type="text" name="area" class="BigInput" size="25" maxlength="200">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">排序字段：</td>
    <td nowrap class="TableData">
        <select name="orderflag">
          <option value="DEPT">部门 </option>
          <option value="TYPE_ID">类别 </option>
          <option value="BOOK_NAME">书名 </option>
          <option value="AUTHOR">作者 </option>
          <option value="PUB_HOUSE">出版社 </option>
          <option value="BOOK_NO">图书编号 </option>
        </select>
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="submit" value="查询" class="BigButton" title="模糊查询">
    </td>
   </tr>   
</table>
<input type="hidden" name="toId" id="toId" value=""> 
</form>
</body>
</html>
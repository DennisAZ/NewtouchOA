<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*"%>
<%@ page  import="yh.subsys.oa.book.data.*"%> 
<html>
<head>
<%
	YHPage pages = (YHPage)request.getAttribute("page");
  List<YHBookInfo> books = (List<YHBookInfo>)request.getAttribute("books");
  YHBookInfo conditon = (YHBookInfo)request.getAttribute("conditon");
  String orderflag = (String)request.getAttribute("orderflag");
  
%>
<title>图书查询结果 </title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script Language="JavaScript">
   function goBack(){
     window.location.href= contextPath + "/yh/subsys/oa/book/act/YHBookTypeAct/jinruBookType.act";
   }

   function borrowBook(bookSeqId){
    //var bookSeqId = $("noHiddenId").value;
    //var url  = contextPath + "/subsys/oa/book/query/new.jsp?bookNo="+ bookSeqId;
    // window.open(url);
    var url  = contextPath + "/yh/subsys/oa/book/act/YHBookTypeAct/selectBookTypeId.act?bookSeqId="+ bookSeqId;
    window.location.href=url;
   
   }

   function gotoPage(currNo){
     $("currNo").value = currNo;
    // var url  = contextPath + "/yh/subsys/oa/book/act/YHBookQueryAct/findBooks.act?currNo="+ currNo;   
    // window.location.href= url;
     $("form1").submit();
   }

   function jumpToPage(){
     var start = 1;
     var end = <%=pages.getTotalPageNum()%>;
     var pageNo = $("currentNo").value;
     if(!isInteger(pageNo)&&pageNo!=0){
       alert("请填写整数");
       $("currentNo").focus();
       return false;
     }
     else if(pageNo > end || pageNo <start){
       alert("页号应在"+start+"-"+end +"之间");
       $("currentNo").focus();
       return false;
     }else{
       gotoPage(pageNo);
     }
   }

  function deleteBookInfo(deleteSeqId){
   var msg ="确定要删除该图书吗？";
   if(!window.confirm(msg)){
   }else{
     var noHiddenId = document.getElementById("noHiddenId").value;
     if(noHiddenId && noHiddenId!="null"){
       window.location.href = contextPath + "/yh/subsys/oa/book/act/YHBookTypeAct/deleteBookInfo.act?HiddenId="+deleteSeqId;  
       return true;
      }
    }
   
  }
  //全选
  function checkAll(field){
	   var allSelect = document.getElementsByName("bookSelect");
	   for(var i = 0; i<allSelect.length; i++){
	      allSelect[i].checked = field.checked;
	   }
  }
  //单选
  function checkOne(one){
    if(!one.checked){   
	     document.all("allbox").checked=false;
	   }
  }
  // 删除所选图书信息
  function deleteCheckBook(){
    var allSelect = document.getElementsByName("bookSelect");
    var deleteStr = "";
    for(var i = 0; i<allSelect.length; i++){
       if(allSelect[i].checked){
         deleteStr += allSelect[i].value +",";
       }
    }  
    if(deleteStr == ""){
       alert("要删除图书信息，请至少选择其中一条");
       return ;
    }
    msg = '确定要删除所选图书信息吗?';
    if(window.confirm(msg)){
       var par = 'deleteStr='+deleteStr;
       var url = contextPath + '/yh/subsys/oa/book/act/YHBookTypeAct/deleteSelectBook.act';
       var json = getJsonRs(url,par);
       if (json.rtState == "0"){//删除后 重新提交查询页面
				 $("form1").submit();
				 return true;
		  } else{
         alert(json.rtMsrg);
      }
      }
   }
 
</script>
</head>

<body class="bodycolor" topmargin="5" >
<%
  if(pages.getTotalRowNum() > 0){%>    

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small" >
  <tr>
    <td class="Big"><img src="<%=imgPath%>/book.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 图书查询结果 </span><br>
    </td>
    <td valign="bottom" align="right">
    <span class="small1">当前为第<b><%=pages.getFirstResult()+1 %></b>至<b><%=pages.getLastResult()%></b>条 (第<%=pages.getCurrentPageIndex()%>页，共<%=pages.getTotalPageNum()%>页，每页最多<%=pages.getPageSize() %>条)</small>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </td>
    </tr>
</table>

<table class="TableList"  width="95%" align="center">
<form method="post" name="from2" id ="from2" action="#">
  <tr class="TableHeader">
      <td nowrap align="center">选择&nbsp;</td>
      <td nowrap align="center">部门&nbsp;</td>
      <td nowrap align="center">书名&nbsp;</td>
      <td nowrap align="center">编号&nbsp;</td>
      <td nowrap align="center">类别&nbsp;</td>
      <td nowrap align="center">作者&nbsp;</td>
      <td nowrap align="center">出版社&nbsp;</td>
      <td nowrap align="center">存放地点&nbsp;</td>
      <td nowrap align="center">借阅范围&nbsp;</td>
      <td nowrap align="center">借阅状态&nbsp;</td>
      <td nowrap align="center">操作&nbsp;</td>
  </tr>
 <%
 	for(int i=0; i<books.size(); i++){%>
 	  <tr class="TableLine1">
 	  <td nowrap align="center"><input type="checkbox" id="bookSelect" name="bookSelect" value="<%=books.get(i).getSeqId() %>" onClick="checkOne(this);">&nbsp;</td>
    <td nowrap align="center"><%=books.get(i).getDeptName() %>&nbsp;</td>
    <td nowrap align="center"><%=books.get(i).getBookName() %>&nbsp;</td>
    <td nowrap align="center"><%=books.get(i).getBookNo() %>&nbsp;</td>
    <td nowrap align="center"><%=books.get(i).getTypeName() %>&nbsp;</td>
    <td nowrap align="center"><%=books.get(i).getAuthor() %>&nbsp;</td>
    <td nowrap align="center"><%=books.get(i).getPubHouse() %>&nbsp;</td>
    <td nowrap align="center"><%=books.get(i).getArea() %>&nbsp;</td>
    <td  align="center" width="40%"><%=books.get(i).getOpenNames() %>&nbsp;</td>
    <td nowrap align="center">
       <%
       	if("1".equalsIgnoreCase(books.get(i).getLend())){
       %>	  
                    已借出
       <% 
       	}else{%>
       	  未借出
       	 <% }
       %>
    </td>
    <td nowrap align="center" width="80">
    	    <% if("1".equalsIgnoreCase(books.get(i).getLend())){
    	     %>
    	    <a href="javascript:void(0);" onclick="borrowBook(<%=books.get(i).getSeqId() %>);">编辑 </a> 
    	    <% }else{%>
    	     <a href="javascript:void(0);" onclick="borrowBook(<%=books.get(i).getSeqId() %>);">编辑 </a>
    	     <a href="javascript:void(0);" onclick="deleteBookInfo(<%=books.get(i).getSeqId() %>);">删除 </a>	
    	    <%
    	   }
    	%>
    </td>
  </tr>
  <input type="hidden" value="<%= books.get(i).getSeqId()%>" id="noHiddenId" name="noHiddenId"/>
  <input type="hidden" value="<%= books.get(i).getBookNo()%>" id="noHidden" name="noHidden"/>
 <%} %> 
 <%
   if(pages.getTotalPageNum() >0){%>
     <tr class="TableControl">
			<td colspan="3" align="left">
				<input type="checkbox" name="allbox" id="allbox" onClick="checkAll(this);"><label for="allbox_for" style="cursor:pointer"><u><b>全选</b></u></label>&nbsp;
        <a href="javascript:deleteCheckBook();" title="删除所选图书"><img src="<%=imgPath %>/delete.gif">删除</a>
	    	 </td>
			 <td colspan="7" align="right">
			   <%if(pages.getCurrentPageIndex() >1){%>
			      <input type="button"  value="首页" class="BigButton"   onclick="gotoPage(1)"> &nbsp;&nbsp;
			   	  <input type="button"  value="上一页" class="BigButton"  onclick="gotoPage(<%=pages.getCurrentPageIndex()-1 %>)"> &nbsp;&nbsp;
			   	<%}%>
			   <%if(pages.getCurrentPageIndex() < pages.getTotalPageNum()){%>
			   	  <input type="button"  value="下一页" class="BigButton"  onclick="gotoPage(<%=pages.getCurrentPageIndex()+1 %>)"> &nbsp;&nbsp;
			   	  <input type="button"  value="末页" class="BigButton"   onclick="gotoPage(<%=pages.getTotalPageNum() %>)"> &nbsp;&nbsp;
			   	<%}%>
			   页数
			   <input type="text" name="currentNo" id="currentNo" value="<%=pages.getCurrentPageIndex()%>" class="SmallInput" size="2"> <input type="button"  value="转到" class="SmallButton" onclick="jumpToPage();" title="转到指定的页面">&nbsp;&nbsp;
			</td>
			<td>&nbsp;</td>
   </tr>
  <%}%> 
  </form>
</table>
<%} else{%>
 <table align="center" width="340" class="MessageBox">
         <tbody>
          <tr>
				    <td class="msg info">
				      <h4 class="title" align="left">提示</h4>
				      <div style="font-size: 12pt;" class="content">没有符合条件的图书！</div>
				    </td>
          </tr>
        </tbody>
      </table>  
<%}%>
<br>
<div align="center">
 <input type="button"  value="返回" class="BigButton" onclick="goBack();return false;">
</div>
<form action="<%=contextPath%>/yh/subsys/oa/book/act/YHBookTypeAct/findBooks.act" method="get" name="form1" id="form1">
  <input type="hidden" name="typeId" value="<%=conditon.getTypeId() %>"/>
  <input type="hidden" name="lend" value="<%=conditon.getLend()%>"/>
  <input type="hidden" name="bookName" value="<%=conditon.getBookName()%>"/>
  <input type="hidden" name="bookNo" value="<%=conditon.getBookNo()%>"/>
  <input type="hidden" name="author" value="<%=conditon.getAuthor()%>"/>
  <input type="hidden" name="isbn" value="<%=conditon.getIsbn()%>"/>
  <input type="hidden" name="pub_house" value="<%=conditon.getPubHouse()%>"/>
  <input type="hidden" name="area" value="<%=conditon.getArea()%>"/>
  <input type="hidden" name="orderflag" value="<%=orderflag %>"/> 
  <input type="hidden" name="currNo" id="currNo" value=""/> 
</form>
</body>
</html>

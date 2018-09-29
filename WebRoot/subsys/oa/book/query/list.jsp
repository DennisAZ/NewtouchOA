<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*"%>
<%@ page  import="yh.subsys.oa.book.data.*"%> 
<%@ page import=" java.net.URLEncoder;" %>
<html>
<head>
<%
	YHPage pages = (YHPage)request.getAttribute("page");
  List<YHBookInfo> books = (List<YHBookInfo>)request.getAttribute("books");
  YHBookInfo conditon = (YHBookInfo)request.getAttribute("conditon");
  String orderflag = (String)request.getAttribute("orderflag");
  String bookName = URLEncoder.encode(conditon.getBookName(),"utf-8");
  String bookNo = URLEncoder.encode(conditon.getBookNo(),"utf-8");
  String author = URLEncoder.encode(conditon.getAuthor(),"utf-8");
  String area = URLEncoder.encode(conditon.getArea(),"utf-8");
  String isbn = URLEncoder.encode(conditon.getIsbn(),"utf-8");
  String pub_house = URLEncoder.encode(conditon.getPubHouse(),"utf-8");
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
<script Language="JavaScript"><!--
   function goBack(){
     window.location.href= contextPath + "/subsys/oa/book/query/bookquery.jsp";
   }

   function borrowBook(bookNo){
    var url  = contextPath + "/subsys/oa/book/query/new.jsp?bookNo="+ toUtf8Uri(bookNo);
    window.open(url);
   }

   function gotoPage(currNo){   
     //$("currNo").value = currNo;
     var typeId = "<%=conditon.getTypeId() %>";
     var lend = "<%=conditon.getLend()%>";
     var bookName = "<%=bookName%>";   
     var bookNo = "<%=bookNo%>";
     var author = "<%=author%>";
     var isbn ="<%=isbn%>";
     var pub_house="<%=pub_house%>";
     var area ="<%=area%>";
     var orderflag = "<%=orderflag %>";
     var currNo = currNo;
     var param = "currNo="+currNo +"&typeId="+typeId +"&lend="+lend+"&bookName="+(bookName) 
                +"&bookNo="+ (bookNo) + "&author="+(author) +"&isbn="+(isbn) 
                +"&pub_house="+(pub_house) + "&area="+(area) + "&currNo=" + currNo;
     var url = contextPath + "/yh/subsys/oa/book/act/YHBookQueryAct/findBooks2.act?";
     //$("form1").submit();
    
     window.location.href = url + param;
   }

   function jumpToPage(){
     var start = 1;
     var end = <%=pages.getTotalPageNum()%>;
     var pageNo = $("currentNo").value;    
     if(!isInteger(pageNo)&&pageNo!=0){
       alert("请填写整数");
       $("currentNo").focus();
       return false;
     }else if(pageNo > end || pageNo <start){
       alert("页号应在"+start+"-"+end +"之间");
       $("currentNo").focus();
       return false;
     }else{
       gotoPage(pageNo);
     }
   }

   function detail(bookId){
    var URL=contextPath + "/yh/subsys/oa/book/act/YHBookQueryAct/detail.act?bookId="+ bookId;
    myleft=(screen.availWidth-500)/2;
    window.open(URL,"read_notify","height=400,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
   }
--></script>
</head>

<body topmargin="5" >
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
  <tr class="TableHeader">
      <td nowrap align="center">部门</td>
      <td nowrap align="center">书名</td>
      <td nowrap align="center">编号</td>
      <td nowrap align="center">类别</td>
      <td nowrap align="center">作者</td>
      <td nowrap align="center">出版社</td>
      <td nowrap align="center">存放地点</td>
      <td nowrap align="center">借阅范围</td>
      <td nowrap align="center">借阅状态</td>
      <td nowrap align="center">操作</td>
  </tr>
 <%
 	for(int i=0; i<books.size(); i++){ 	  
 	%>
 	  <tr class="TableLine1">
    <td nowrap align="center"><%=books.get(i).getDeptName() %> &nbsp;</td>
    <td nowrap align="center"><%=books.get(i).getBookName() %> &nbsp;</td>
    <td nowrap align="center"><%=books.get(i).getBookNo() %> &nbsp;</td>
    <td nowrap align="center"><%=books.get(i).getTypeName() %> &nbsp;</td>
    <td nowrap align="center"><%=books.get(i).getAuthor() %> &nbsp;</td>
    <td nowrap align="center"><%=books.get(i).getPubHouse() %> &nbsp;</td>
    <td nowrap align="center"><%=books.get(i).getArea() %> &nbsp;</td>
    <td  align="center" width="40%"><%=books.get(i).getOpenNames() %> &nbsp;</td>
    <td nowrap align="center">
       <%
       	if("1".equalsIgnoreCase(books.get(i).getLend())){
       		System.out.println(books.get(i).getLend());
       %>	  
                    已借出
       <% 
       	}else{
       		System.out.println(books.get(i).getLend()+"   kkkkkk");
       	%>
       	  未借出
       	 <% }%>
    </td>
    <td nowrap align="center" width="80">
    	<%    	
    	  if(!"1".equalsIgnoreCase(books.get(i).getLend()) ){%>
    	   <a href="javascript:void(0);" onclick="borrowBook('<%= URLEncoder.encode(books.get(i).getBookNo(), "UTF-8")%>');">借阅 </a> 
    	   
    	<%}%>     
      <a href="javascript:void(0);" onclick="detail(<%=books.get(i).getSeqId() %>);">详情 </a>
    </td>
  </tr>
 <%} %> 
 <%
   if(pages.getTotalPageNum() >1){%>
     <tr class="TableControl">
			<td colspan="9" align="right">
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

</body>
</html>

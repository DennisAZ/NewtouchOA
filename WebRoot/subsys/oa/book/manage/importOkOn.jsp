<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.subsys.oa.book.data.*"%>
<%@ page  import="java.util.List"%>
<html>
<head>
<%
  List<YHBookInfo> list =  (List<YHBookInfo>)request.getAttribute("bookinfo");
%>

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
 <script type="text/javascript">
 var flagOpen = true;
  function Back(){
    window.location.href= contextPath + "/subsys/oa/book/manage/import.jsp";
  }

  function borrowBook(bookSeqId){
   //var bookSeqId = $("noHiddenId").value;
   //var url  = contextPath + "/subsys/oa/book/query/new.jsp?bookNo="+ bookSeqId;
   // window.open(url);
   var url = contextPath + "/yh/subsys/oa/book/act/YHBookTypeAct/selectBookTypeId.act?bookSeqId="+ bookSeqId;
   newWindow(url,'820', '500');
   //var url = contextPath + "/yh/subsys/oa/book/act/YHBookTypeAct/selectBookTypeId.act?bookSeqId="+ bookSeqId;
   //window.location.href=url;
  
  }

  /**
   * 打开新窗口  newWindow(URL,'740', '540');
   * @param url
   * @param width
   * @param height
   * @return
   */
  function newWindow(url,width,height){
    var locX=(screen.width-width)/2;
    var locY=(screen.height-height)/2;
    window.open(url, "meeting", 
        "height=" +height + ",width=" + width +",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
        + locY + ", left=" + locX + ", resizable=yes");
  }
 </script>

</head>
<body topmargin="5" class="bodycolor">
<table width="100%" cellspacing="0" cellpadding="3" border="0" class="small">
    <tbody><tr>
      <td class="Big"><img align="absmiddle" src="<%=imgPath %>/book.gif"><span class="big3"> 图书信息导入</span><br>
      </td>
    </tr>
  </tbody></table>
  <table class="TableList" width="100%" align ="center">
  <thead class="TableHeader">
   <tr>
      <td nowrap="" align="center">部门&nbsp;</td>
      <td nowrap="" align="center">书名&nbsp;</td>
      <td nowrap="" align="center">作者&nbsp;</td>      
      <td nowrap="" align="center">ISBN号&nbsp;</td>
      <td nowrap="" align="center">出版社&nbsp;</td>
      <td nowrap="" align="center">信息&nbsp;</td>
      </tr>
  </thead>
    <%  int number = 0;
    if(list.size()>0 && list!=null){
      for(int i=0; i<list.size(); i++){
        if(list.get(i).getSaveInfo().equalsIgnoreCase("保存失败！编号已存在") || list.get(i).getSaveInfo().equalsIgnoreCase("保存失败")){
       
  %>
  
   <tr align="center" style="color:red;">
      <td><%=list.get(i).getDeptName()=="-1"?"":list.get(i).getDeptName() %>&nbsp;</td>
      <td><%=list.get(i).getBookName() %>&nbsp;</td>
      <td><%=list.get(i).getAuthor() %>&nbsp;</td>
      <td><%=list.get(i).getIsbn() %>&nbsp;</td>
      <td><%=list.get(i).getPubHouse() %>&nbsp;</td>
    <td align="left"><%=list.get(i).getSaveInfo() %>&nbsp;</td>
  </tr>
  <%    
        }else{ 
          %>
       <tr align="center">
      <td><%=list.get(i).getDeptName()=="-1"?"":list.get(i).getDeptName() %>&nbsp;</td>
      <td><%=list.get(i).getBookName() %>&nbsp;</td>
      <td><%=list.get(i).getAuthor() %>&nbsp;</td>
      <td><%=list.get(i).getIsbn() %>&nbsp;</td>
      <td><%=list.get(i).getPubHouse() %>&nbsp;</td>
      <td align="left"><%=list.get(i).getSaveInfo() %>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0);" onclick="borrowBook(<%=list.get(i).getSeqId() %>);">编辑 </a></td>
  </tr>
          <% 
          ++ number;
        }
    } 
  }
  %>
  </table>
 <div align="center" style="" id="noData">
	  <table width="300" class="MessageBox">
			  <tbody>
					  <tr>
							 <td class="msg info" id="msgInfo">共 <%=number%> 条数据导入!</td>
					  </tr>
			  </tbody>
	  </table>
	  </div>
  <div align="center">
<input type="button" title="返回" onclick="Back();" class="SmallButton" value="返回">
</div>
</body>
</html>
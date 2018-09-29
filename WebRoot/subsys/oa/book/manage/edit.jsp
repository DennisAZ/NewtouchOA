<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.subsys.oa.book.data.*"%>
<%@ page  import="java.util.List"%>
<html>
<head>
	<%  //图书基本信息查询 book_info
	   List<YHBookInfo> bookinfo = (List<YHBookInfo>)request.getAttribute("bookinfo");
	   YHBookInfo book = bookinfo.get(0);
	   System.out.println(book.getIsbn());
	   String loginName = (String)request.getAttribute("loginName");
	   List<YHBookType> booktype = (List<YHBookType>)request.getAttribute("booktype");
	   
	%>
<title>图书编辑</title>
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
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
function doInit(){
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/selectDeptToAttendance.act?deptId=0";
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userId_priv = rtJson.rtMsrg;
  var userId = userId_priv.split(",")[0];
  var priv = userId_priv.split(",")[1];
  var prcs = rtJson.rtData;
  var selects = document.getElementById("deptId");
  if(priv=='1'){
    var optionAll = document.createElement("option"); 
    optionAll.value = '0'; 
    optionAll.innerHTML = '所有部门'; 
    selects.appendChild(optionAll);
  }
  for(var i=0;i<prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value; 
    option.innerHTML = prc.text; 
    selects.appendChild(option);
  }
  var beginParameters = {
      inputId:'statrTime',
      property:{isHaveTime:false}, //isHaveTime:false 为false没有添加时钟
      bindToBtn:'beginDateImg'
        };
  new Calendar(beginParameters);
  var endParameters = {
      inputId:'endTime',
      property:{isHaveTime:true},
       bindToBtn:'endDateImg'
        };
  new Calendar(endParameters);

  //
  $("deptId").value = <%=book.getDept()%> ;
  $("typeId").value = <%=book.getTypeId()%>;
  
  return userId;
}

function clickBack(){
  if(window.opener){
    window.close();
  }
  else{
	  history.back();
  }
}

function clearDept(){
  document.getElementById("dept").value="";
  document.getElementById("deptDesc").value="";
  
}
function goto(){
  
	window.location.href= contextPath + "/yh/subsys/oa/book/act/YHBookTypeAct/jinruBookType.act";
	/*var typeId = $("typeId").value;
  var lend = $("lend").value; 
  var bookName = $("bookName").value;
  var bookNo = $("bookNo").value; 
  var author = $("author").value;
  var isbn = $("isbn").value;
  var pub_house = $("pubHouse").value;
  var area = $("area").value;
  var parameter ="lend="+lend+"&bookName="+bookName+"&bookNo="+bookNo+"&author="+author+"&isbn="+isbn+"&pub_house="+pub_house+"&area="+area+"&typeId="+typeId;
  alert(typeId+":::"+lend);
	window.location.href = contextPath + "/yh/subsys/oa/book/act/YHBookTypeAct/findBooks.act?parameter="+parameter;
	*/

}
/** 
 * 替换s1为s2 
 */ 
 String.prototype.replaceAll = function(s1,s2){ 
    return this.replace(new RegExp(s1,"gm"),s2); 
 }
 /**
          添加图书
 **/
  function addBook(){
    var seqId = $("seqid").value;
    
    var deptId = $("deptId").value;
    if(deptId.replaceAll(" ","") == "" || deptId == "null"){
      alert("部门不能为空");
      return false;
    }
    var bookName = $("bookName").value; 
    if(bookName.replaceAll(" ","") == "" || bookName == "null"){
      alert("图书名称不能为空");
      return false;
    }
    var bookNo = $("bookNo").value;
    if(bookNo.replaceAll(" ","") == "" || bookNo =="null"){
       alert("图书编号不能为空");
       return false;
      }
    var typeId = $("typeId").value;
    var author = $("author").value;
    var isbn = $("isbn").value;
    var pubHouse = $("pubHouse").value; 
    var statrTime = $("statrTime").value;
    var area = $("area").value;
    var amt = $("amt").value;
    if(amt.replaceAll(" ","") == "" || amt == "null"){
      alert("图书数量不能为空");
      return false;
    }
   if(isNaN(amt)){
    alert("图书数量必须为数字");
    return false;
   }
    var price = $("price").value; 
    if(isNaN(price)){
      alert("价格必须为数字"); 
      return false;
     }
    var brief = $("brief").value;
    //var deptDesc = $("deptDesc").value;
    var dept = $("dept").value;
    if(dept =="" || dept == "null"){
      alert("借阅范围不能为空");
      return false;
   }
    var lend = $("lend").value; 
    var borrPerson = $("borrPerson").value;
    var memo = $("memo").value;
    var attachment = $("attachment").value;
   //var parameter = "deptId="+deptId+"&bookName="+bookName+"&bookNo="+bookNo+"&typeId="+typeId+"&author="+author+"&isbn="+isbn+"&pubHouse="+pubHouse+"&statrTime="+statrTime+"&area="+area+"&amt="+amt+"&price="+price+"&brief="+brief+"&dept="+dept+"&lend="+lend+"&borrPerson="+borrPerson+"&memo="+memo+"&attachment="+attachment+"&seqId="+seqId;
    document.form1.action = contextPath + "/yh/subsys/oa/book/act/YHBookTypeEnterAct/editBookTypeInfo.act?seqId="+seqId;
    document.form1.submit();
    if(window.opener){
      window.close();
    }
    return true; 
   }
</script>

</head>
<body topmargin="5" class="bodycolor" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
  <td class="Big"><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 图书信息编辑 </span>
    </td>
  </tr>
</table>
<table class="TableBlock" width="70%" align="center">
  <form action="#" id="form1" method="post" name="form1" enctype="multipart/form-data">
 <!--  <form enctype="multipart/form-data" action="add.php"  method="post" name="form1" onSubmit="return CheckForm();">
  -->
   <tr>
	    <td nowrap class="TableData">部门： </td>
	    <td nowrap class="TableData">
	       <select name="deptId" id="deptId">
	       </select>
	    </td>
	    
	     <td nowrap class="TableData" rowspan="6" width="150">
	     <%
	      if(!"null".equalsIgnoreCase(book.getAttachmentName())&& book.getAttachmentName()!=null){
	        %>
	        <img src="<%=contextPath %>/yh/core/funcs/office/ntko/act/YHNtkoAct/upload.act?attachmentName=<%=book.getAttachmentName() %>&attachmentId=<%=book.getAttachmentId() %>&module=book" width='100' border=1></img>	           
	       <% 
	      }else{
	         %>
	                <center>暂无封面</center>
	      <%   }
	     %>
     </td>
	    
   </tr>
    
   
   <tr>
    <td nowrap class="TableData" width="120">书名：<font style="color:red">*</font></td>
    <td nowrap class="TableData">
        <input type="text" name="bookName" id="bookName" class="BigInput" size="33" maxlength="100" value="<%=book.getBookName() %>">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData" width="120">编号：<font style="color:red">*</font></td>
    <td nowrap class="TableData">
        <input type="text" name="bookNo" id="bookNo" class="BigInput" size="33" maxlength="100" value="<%=book.getBookNo()%>">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">图书类别：</td>
    <td nowrap class="TableData">
        <select name="typeId" id="typeId">
           <%
     if(booktype!=null && booktype.size()>0){
       for(int i = 0; i < booktype.size(); i++){
   %>
   <option value="<%=booktype.get(i).getSeqId()%>"><%=booktype.get(i).getTypeName()%></option>
   <%
       }
     }
   %>
   </select>
    </td>
   </tr>
    <tr>
    <td nowrap class="TableData" width="120">作者：</td>
    <td nowrap class="TableData">
        <input type="text" name="author" id="author" class="BigInput" size="33" maxlength="100" value="<%=book.getAuthor()%>">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData" width="120">ISBN号：</td>
    <td nowrap class="TableData">
           <input type="text" name="isbn" id="isbn" class="BigInput" size="33" maxlength="100" value="<%=book.getIsbn() %>">&nbsp;
  
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData" width="120">出版社：</td>
    <td nowrap class="TableData" colspan="2">
        <input type="text" name="pubHouse" id="pubHouse" class="BigInput" size="33" maxlength="100" value="<%=book.getPubHouse() %>">&nbsp;
    </td>
   </tr>
   <tr>
		    <td nowrap class="TableData" width="120">出版日期：</td>
		    <td class="TableData" colspan="2">
			    <input type="text" name="statrTime" id="statrTime" value="<%=book.getPubDate() %>" size="20" maxlength="20" class="BigInput">
			    <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
		    </td>
	</tr>
	<tr>
    <td nowrap class="TableData" width="120">存放地点：</td>
    <td nowrap class="TableData" colspan="2">
        <input type="text" name="area" id="area" class="BigInput" size="33" maxlength="100" value="<%=book.getArea() %>">&nbsp
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData" width="120">数量：<font style="color:red">*</font></td>
    <td nowrap class="TableData" colspan="2">
        <input type="text" name="amt" id="amt" class="BigInput" size="25" maxlength="11" value="<%=book.getAmt() %>">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData" width="120">价格：</td>
    <td nowrap class="TableData" colspan="2">
        <input type="text" name="price" id="price" class="BigInput" size="25" maxlength="10" value="<%=book.getPrice() %>">&nbsp;
    </td>
   <tr>
    <td nowrap class="TableData" width="120">内容简介：</td>
    <td nowrap class="TableData" colspan="2">
      	<textarea cols=37 rows=3 name="brief" id="brief" class="BigInput" wrap="yes"><%=book.getBrief() %></textarea>
    </td>
   </tr>
   <tr>
   <td nowrap class="TableData" width="120">借阅范围：<font style="color:red">*</font></td>
   
        <td style="" id="td_dept" class="TableData" colspan="2">
        <input type="hidden" name="dept" id="dept" value="<%=book.getOpen()%>">
        <textarea readonly="" wrap="yes" class="BigStatic" rows="3" id="deptDesc" name="deptDesc" cols="35" ><%=book.getOpenNames() %></textarea>
        <a onclick="javascript:selectDept(['dept','deptDesc']);" class="orgAdd" href="javascript:;">添加</a>
        <a onclick="clearDept();" class="orgClear" href="javascript:;">清空</a>
      </td>
   </tr>
   <tr>
    <td nowrap class="TableData" width="120">借阅状态：</td>
    <td nowrap class="TableData" colspan="2">
       <select name="lend" id="lend">
          <option value="0">未借出 </option>
          <option value="1">已借出 </option>
       </select>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData" width="120">录入人：</td>
    <td nowrap class="TableData" colspan="2">
        <input type="text" name="borrPerson" id="borrPerson" class="BigStatic" size="33" maxlength="100" value="<%=book.getBorrPerson() %>" readonly>&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData" width="120">备注：</td>
    <td nowrap class="TableData" colspan="2">
        <input type="text" name="memo" id="memo" class="BigInput" size="33" maxlength="100" value="<%=book.getMemo() %>">&nbsp;
    </td>
   </tr>
 
   <tr>
    <td nowrap class="TableData" width="120">上传封面：</td>
    <td class="TableData" colspan="2">
        <input type="file" name="attachment" id="attachment" size="40" class="BigInput" title="选择附件文件">
    </td>
   </tr>
   <input type="hidden" name="seqid" id = "seqid" value="<%=book.getSeqId() %>"></input>
   <tr>
    <td nowrap  class="TableControl" colspan="3" align="center">
        <input type="button" value="确定" class="BigButton" title="添加图书" name="button" onclick="javascript:addBook(); return false;">&nbsp;
        <!--  <input type="button" value="返回" class="BigButton" onClick="javascript:goto(); return false;">-->
       <input type="button" onclick="clickBack();" name="back" class="BigButton" value="返回">
        
    </td>
   </tr>
   <tr>
   <td nowrap  class="TableControl" colspan="3" align="center">
   </tr> 
</form>
</table>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件字查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>

<script type="text/javascript">


function doSubmit(){ 

		var query = $('form1').serialize();    
		location.href = "<%=contextPath %>/core/funcs/workflow/flowrun/docword/search.jsp?" + query;

}



</script>
</head>
<body onLoad="">
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="absMiddle" src="<%=imgPath %>/infofind.gif"><span class="big3">文件字查询</span></td>
  </tr>
</table>
<br>	
<form enctype=""  method="post" name="form1" id="form1" >
<table class="TableBlock" width="50%" align="center">
  <tr>
    <td nowrap class="TableContent">名称：</td>
      <td class="TableData">
      <INPUT type="hidden" name="seqId"  id="seqId" class=BigInput size="30" value=""/>
      <INPUT type="text" name="dwName"  id="dwName" class=BigInput size="30" value=""/>
    </td>   
  </tr>
  
   <tr>
    <td nowrap class="TableContent">序号样式：</td>
      <td class="TableData">
        <input type="text" name="indexStyle" id="indexStyle" class="BigInput" size="30" >
    </td>
     
  </tr>
  
  
  <tr align="center" class="TableControl">
    <td colspan=4 nowrap>
       <input  type="button"  onClick="doSubmit()" value="查询" class="BigButton">
          <input  type="reset"   value="重置" class="BigButton">
     </td>
  </tr>
</table>
</form>

</body>
</html>
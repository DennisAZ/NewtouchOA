<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<html>
<%
  String bm = request.getParameter("bm");
  if(bm==null || bm ==""){
     bm ="0";
  }
%>
	<title>选择签收人</title>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=Utf-8">
		  <link rel="stylesheet" href ="<%=cssPath %>/style.css"/>
	    <script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
			<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
			<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
			<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
			<script type="text/Javascript" src="<%=contextPath%>/core/funcs/doc/receive/js/MultiUserSelect.js" ></script>
	  	
	  	<script type="text/javascript">
	  	   function clearValue(){
           $("recipient").value = "";
           $("recipientId").value = "";
		  	 }
		  	 
		  	 function doSubmit(){
           var name = $("recipientId").value;
           var pid = $("recipient").value;
           if(!pid){
             alert("请选择签收人");
             return;
            }
          window.opener.beanchUser(name, pid);
          window.close();
			   }
	  	</script>
	</head>
  <body>
          <center>请选择签收人：</center><br>
          <center>
          <TABLE border="0" width="100" cellspacing="0" cellpadding="3" class="small"> 
	        <tr>
	  			<td nowrap  class="" >签收人：<font style="color:red">*</font></td>
	  			<td nowrap class="TableData">
           <input type="hidden" id="recipient" name="recipient" value="" />
           <input type="text"name="recipientId" id="recipientId" class="SmallStatic"  value="" readonly/>
           <a href="javascript:void(0);" class="orgAdd" onclick="selectSingleUserByDept(['recipient', 'recipientId'],<%=bm %>)" title="添加收件人">添加</a>
           <a href="javascript:void(0);" class="orgClear" onclick="javascript:clearValue();" title="清空收件人">清空</a>
           <font id="recipientMsg" style="color:red"></font>           					  				
	  			</td>
	  		</tr>
	  		<tr align='center'>
	             <td colspan ='2'><input type="button" id="ok" name="ok" class="BigButton" onclick="doSubmit()" value="确定">
	           </tr>
	  	      </TABLE>  	
  	     </center>
  </body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>初始化信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<link rel="stylesheet" href ="<%=contextPath %>/springViews/css/lead.css">
<!-- 封装表单的数据提交 -->
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js"></script>
<script> 

 //开帐请求
   function doSubmitForm() {
   if(!<%=oa.spring.util.OpenAccount.isOpenAccount()%>){
	   var url = "<%=contextPath %>/SpringR/system/openAccount";
	   if(confirmOpenAccount()){
		  $.ajax({
		   type : 'POST',
		   url : url,
		   contentType : "application/json",
		   dataType:'text',
		   success : function(jsonData){   
			  var rtJson = eval("(" + jsonData + ")"); 
			  if(rtJson.rtState == "0"){
			    alert("开帐成功"); 
			    window.location.href = "<%=contextPath %>/yhindex.jsp?type=openAccount";
			  }else{
			    alert("开帐失败"); 
			    return ;
			  }
		   }
		 });
	   }else{
	   	  return false;
	   }
	  }else{
	  	  alert("已经开帐"); 
	      return ;
	  }
   }


//确认开帐
function confirmOpenAccount() {
  if(confirm("已进行开帐，所有初始化数据都不能进行变更，确认要开帐吗？")) {
    return true;
  } else {
    return false;
  }
}


//根据不同类型连接不同的页面
function linkPage(type){
	if(type == 'ck'){
	 window.location.href = '<%=contextPath %>/springViews/erp/initData/indexCK.jsp';
	}else if(type == 'xs'){
	 window.location.href = '<%=contextPath %>/springViews/erp/initData/indexXS.jsp';
	}else if(type == 'cg'){
	 window.location.href = '<%=contextPath %>/springViews/erp/initData/indexCG.jsp';
	}else if(type == 'cw'){
	 window.location.href = '<%=contextPath %>/springViews/erp/initData/indexCW.jsp';
	}else if(type == 'cp'){
	 window.location.href = '<%=contextPath %>/springViews/erp/initData/indexCP.jsp';
	}else{
		return ;
	}
}
</script>
</head>
<body style="background-color: #D1E9E9">

<div id="1">
    		
            <div title="功能模块" id="center">
 <table cellspacing="10px" cellpadding="10px">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;初始化基础</span>
   </td>
 </tr>
            	<tr>
            		<td><input type="button"  style="background-image: url('<%=contextPath %>/springViews/erp/initData/img/cpgl.jpg')" onclick="javascript:linkPage('cp');"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
            		<td><input type="button"  style="background-image: url('<%=contextPath %>/springViews/erp/initData/img/ccgl.jpg')"  onclick="javascript:linkPage('ck');"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
            		<td><input type="button"  style="background-image: url('<%=contextPath %>/springViews/erp/initData/img/xsgl.jpg')" onclick="javascript:linkPage('xs');"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
            		<td><input type="button"  style="background-image: url('<%=contextPath %>/springViews/erp/initData/img/cggl.jpg')" onclick="javascript:linkPage('cg');"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
            	</tr>
				<tr>
				<td><input type="button"  style="background-image: url('<%=contextPath %>/springViews/erp/initData/img/cwgl.jpg')" onclick="javascript:linkPage('cw');"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				</tr>
            </table><br/><br/>
            <center><input type="button" value="确认开帐" class="BigButton" onclick="return doSubmitForm();"></center>
            </div>
       </div>
	   <div id="buttom" style="display:block;width:700px;height:300px;float:left;">
		
			<b>说明:</b>
			<br/>
			该页面模块将引导您进行初始化的数据录入。 <font color="#FF0000">确认开帐后，初始化页面将不复存在，后续业务将按正常的业务工作流方式开展。</font><br/>
	   </div>
</body>
</html>
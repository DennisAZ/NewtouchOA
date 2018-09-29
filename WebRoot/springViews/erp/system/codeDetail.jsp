<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String id = request.getParameter("id");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建现金银行信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
		<link rel="stylesheet" href="<%=cssPath%>/page.css">
		<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css">
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();
</script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/cms/station/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/mobilesms/js/isdatetime.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/dtree.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/javascript">
	function doinit(){
		jQuery('#code_type').append("<option value='CODE1'><%=oa.spring.util.StaticData.CODE1%></option>")
		.append("<option value='CODE2'><%=oa.spring.util.StaticData.CODE2%></option>")
		.append("<option value='CODE3'><%=oa.spring.util.StaticData.CODE3%></option>")
		.append("<option value='CODE4'><%=oa.spring.util.StaticData.CODE4%></option>")
		.append("<option value='CODE5'><%=oa.spring.util.StaticData.CODE5%></option>")
		.append("<option value='CODE6'><%=oa.spring.util.StaticData.CODE6%></option>")
		.append("<option value='CODE7'><%=oa.spring.util.StaticData.CODE7%></option>")
		.append("<option value='CODE8'><%=oa.spring.util.StaticData.CODE8%></option>")
		.append("<option value='CODE9'><%=oa.spring.util.StaticData.CODE9%></option>")
		.append("<option value='CODE10'><%=oa.spring.util.StaticData.CODE10%></option>")
		.append("<option value='CODE11'><%=oa.spring.util.StaticData.CODE11%></option>")
		.append("<option value='CODE12'><%=oa.spring.util.StaticData.CODE12%></option>")
		.append("<option value='CODE13'><%=oa.spring.util.StaticData.CODE13%></option>")
		.append("<option value='CODE14'><%=oa.spring.util.StaticData.CODE14%></option>");
		getCodeMsgById();
	}
	
	function getCodeMsgById(){
		var url = '<%=contextPath%>/SpringR/system/getCodeMsgById?id=<%=id%>';
		jQuery.ajax({
			url:url,
			type:'POST',
			success:function(data){
				var json = eval('('+JSON.stringify(data)+')');
				jQuery('#code_type').val(json.code_type);
				jQuery('#value').val(json.value);
				jQuery('#remark').val(json.remark);
			}
		});
	}
   
   function showOrHide(id){
  if($(id).style.display == 'none'){
    $(id).style.display = '';
  }else{
    $(id).style.display = 'none';
  }  
}
</script>
</head>
<body topmargin="5" onload="doinit()">
<table border="0" width="50%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	编号规则详情
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
<table class="TableBlock" width="50%" align="center">
      <tr>
      <td nowrap class="TableData">编号类型：</td>
       <td class="TableData" colspan="2">
 		<select id="code_type" name="code_type" disabled="disabled">
			</select>
      </td>
     
    </tr>
     <tr>
      <td nowrap class="TableData">表达式的值：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="value" id="value"  class="BigInput" />
 		<a href="javascript:showOrHide('tip');">查看说明</a>
      </td>
    </tr>
    <tr id="tip"  class="TableLine2" style="display:none">
      <td nowrap class="TableContent">自动编号表达式说明：</td>
      <td class="TableData"  colspan=3>表达式中可以使用以下特殊标记：<br>
      <table>
        <tr>
          <td>{Y}：表示年</td>
        </tr>
        <tr>
          <td>{M}：表示月</td>
        </tr>
        <tr>
          <td>{D}：表示日</td>
        </tr>
        <tr>
          <td>{H}：表示时</td>
        </tr>
        <tr>
          <td>{I}：表示分</td>
        </tr>
        <tr>
          <td>{S}：表示秒</td>
        </tr>
        <tr>
          <td>{U}：表示用户姓名</td>
        </tr>
      </table>
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="2">
 		<textarea name="remark" id="remark" class="BigInput" cols="70" readonly></textarea>
      </td>
    </tr>
    
    
  </table>
  <hr/>

<br>
	<div id="listContainer" style="display:none;width:100;">
</div>
   <table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
    <tr align="center" class="TableControl">
      <td colspan=6 nowrap>
       <input type="button" value="返回" class="BigButton" onclick="javascript:window.history.back(-1);">
      </td>
    </tr>
  </table>
</form>
</body>
</html>
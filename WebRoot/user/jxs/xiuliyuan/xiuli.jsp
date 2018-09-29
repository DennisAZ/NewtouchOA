<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修理记录</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
</head>
<script type="text/javascript">

function validate(){
		var resualt=false;
		for(var i=0;i<document.tijiao.name1.length;i++)
		{
			
			if(document.tijiao.name1[i].checked)
			{
			  resualt=true;
			}
		}
		if(!resualt)
		{
			alert("修理没有完成，不能提交！");
			return false;
		}
		resualt=false;
		for(var i=0;i<document.tijiao.name2.length;i++)
		{
			
			if(document.tijiao.name2[i].checked)
			{
			  resualt=true;
			}
		}
		
		if(!resualt)
		{
			alert("修理没有完成，不能提交！");
			return false;
		}
		
	 location.href = "";
}
-->




</script>
<body>
<form name="tijiao" id="tijiao">
<table  width="100%" class="TableList">
    <tr class="TableHeader">
      <td>
      修理项

      </td>
       <td>
          标准值      </td>
      <td>检测值</td>
      <td>修理后检测值</td>
      <td>修理情况</td>
      <td>
      修理是否完毕
      </td>
    </tr>
    <tr class="TableLine2">
      <td>
      最高车速(km/h)：      </td>
      <td>
      250
      </td>
      <td>
      240
      </td>
       <td>
      <input type="text" size=3>
      </td>
      <td>
      <input type="text">
      </td>
       <td>
       <input type="radio" name='name1'>是        <input type="radio" name='name1' value='0'>否       </td>
    </tr>
     <tr class="TableLine1">
      <td>
  油耗
      </td>
      <td>
      8.4-9.0L
      </td>
       <td>
      8.0
      </td>
       <td>
      <input type="text" size=3>
      </td>
      <td>
      <input type="text">
      </td><td>
       <input type="radio" name='name2'>是        <input type="radio" name='name2' vlaue='0'>否       </td>
    </tr>
     <tr class="TableControl" align="right">
      <td  colspan="6">
      <input value="保存" type="button" class="SmallButtonC"/>
      </td>
    </tr>
    </table>
	</form>
</body>
</html>
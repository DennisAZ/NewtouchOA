<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>质检记录</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
</head>
<script type="text/javascript">

function validate(){
    var resualt=false;
    for(var i=0;i<document.tijiao.name1.length;i++)
    {

      if(document.tijiao.name1[i].value=='0')
      { 
        resualt=true;
      }
    }
    if(!resualt)
    {
    	if(confirm("有未合格项，是否重新修理")){
               alert("提交重新修理");
               return false;
               	}
    
    }
    resualt=false;
    for(var i=0;i<document.tijiao.name2.length;i++)
    {
      
      if(document.tijiao.name2[i].value=='0')
      {
          
        resualt=true;
      }
    }
    
    if(!resualt)
    {
    	   if(confirm("有未合格项，是否重新修理")){
               alert("提交重新修理");
               return false;
                 }
  
    }
    resualt=false;
    for(var i=0;i<document.tijiao.name3.length;i++)
    {
    
      if(document.tijiao.name3[i].value=='0')
      {
        resualt=true;
      }
    }
    
    if(!resualt)
    {
    	  if(confirm("有未合格项，是否重新修理")){
              alert("提交重新修理");
              return false;
                }
   
    }
    alert("提交下一步");
}
-->

</script>
<form name="tijiao" id="tijiao">
<body>
<table  width="100%" class="TableList">
    <tr class="TableHeader">
      <td>
      质检项

      </td>
       <td>
          标准值

      </td>

      <td>
     检测值      </td>
      <td>
      是否合格
      </td>
    </tr>
    <tr class="TableLine1">
      <td>
      刹车
      </td>
      <td>
     灵

      </td>
      <td>
      <input value="" type="text">
      </td>
      <td><input type="radio" name="name1">是
          <input type="radio" name="name1" value='0'>否
      </td>
    </tr>
    <tr class="TableLine2">
      <td>
      最高车速(km/h)：

      </td>
      <td>
      250
      </td>
      <td>
       <input value="" type="text">
      </td>
      <td><input type="radio" name="name2">是
          <input type="radio" name="name2" value='0'>否
      </td>
    </tr>
     <tr class="TableLine1">
      <td>
  油耗

      </td>
      <td>
      8.4-9.0L
      </td>
      <td>
       <input value="" type="text">
      </td>
      <td><input type="radio" name="name3">是
          <input type="radio" name="name3" value='0'>否
      </td>
    </tr>
     <tr class="TableControl" align="right">
      <td  colspan="4">
      <input value="保存" type="button" class="SmallButtonC" onClick=""/>
     </td>
    </tr>
    </table>
    </form>
</body>
</html>
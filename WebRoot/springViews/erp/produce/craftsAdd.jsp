<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建生产工艺信息</title>
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

function changeUsing(pro_id){
	 var url = "<%=contextPath %>/SpringR/product/changeUsing?pro_id="+pro_id;
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
		 }
	  });
}

function checkIsUsing(pro_id){
var flag = 0;
	 var url = "<%=contextPath %>/SpringR/product/checkIsUsing?pro_id="+pro_id;
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
		 if(jsonData == "1"){
		 		flag = 1;
			}
		 }
	  });
	return flag;
}
   function doSubmitForm() {
     
	 var crafts_version = jQuery('#crafts_version').val();
	 var is_using="";	
		if(document.getElementById("is_using1").checked){
			is_using=document.getElementById("is_using1").value;
		}else{
			is_using=document.getElementById("is_using2").value;
		} 
	 var proId=jQuery('#proId').val();
	 
	 if(is_using == "1"){
	 	if(checkIsUsing(proId) == 1){
	 		if(!confirm("该产品已有当前使用版本，是否重新设置")){
	 			is_using = "0";
	 		}
	 		else{
	 			changeUsing(proId);
	 		}
	 	}
	 }
	 
	 var remark=jQuery('#remark').val();
	 
	 var reqRemark = jQuery('#reqRemark').val();
	 var param="crafts_version="+encodeURIComponent(crafts_version)+
	 	       "&is_using="+encodeURIComponent(is_using)+
	 	       "&remark="+encodeURIComponent(remark)+
	 	       "&proId="+encodeURIComponent(proId);
     var url="";
     url = "<%=contextPath %>/SpringR/produce/addCrafts?"+param;
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		 if(jsonData == "0"){
		   		alert("添加成功");
		   		window.location.href="craftsManage.jsp";
			}else{
					 alert("添加失败"); 
				 }
			 }
	  });
   }
   
  var nameArray="";
 function chooseProduct(idArray,type){
	    nameArray = idArray;

	 	openDialogResize('selectProduct.jsp',  520, 400);
 	}

  function radionChange(){
 	var isusing=document.getElementsByName("is_using");
 	for(var i=0;i<isusing.length;i++){
 		if(isusing[i].check){
 			isusing.value=isusing[i].value;
 		}
 	}
  	
  }
</script>
</head>
<body topmargin="5"  >
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	生产工艺基础信息
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
<table class="TableBlock" width="60%" align="center">
      <tr>
      <td nowrap class="TableData">工艺版本号：</td>
       <td class="TableData">
 		<input type="text" name="crafts_version" id="crafts_version"  class="BigInput" />
      </td>
     
    </tr>
     <tr>
    	<td nowrap class="TableData">
    	 选择产品
 		
 		</td>
 		 <td class="TableData" >
 		<input type="text"  name="proName" id="proName"  class="BigInput" readOnly >
 		<input type="hidden" id="proId" name="proId">
 		
 		<input type="button" value="选择产品" class="BigButton" onclick="chooseProduct(['products'],0);">
 		
      </td>
    </tr>
    <tr>
    	 <td nowrap class="TableData">是否当前使用：</td>
       <td class="TableData" >
 		是：<input type="radio" name="is_using" id="is_using1"  value="1" class="BigInput"  checked onclick="radionChange()"/>&nbsp;&nbsp;&nbsp;
 		否：<input type="radio" name="is_using"   id="is_using2" value="0" class="BigInput" onclick="radionChange()" />
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" >
 		<textarea name="remark" id="remark" class="BigInput" cols="40"></textarea>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan=6 nowrap>
       <input type="button" value="添加" class="BigButton" onclick="return doSubmitForm();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>
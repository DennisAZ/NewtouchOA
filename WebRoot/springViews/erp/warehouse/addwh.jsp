<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String id = request.getParameter("id");
	String flag = "0";
	if(null != id && !"".equals(id)){
		flag = "1";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增仓库信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">

<!-- 封装表单的数据提交 -->
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">

var flag = '<%=flag%>';
function doInit(){
	if(flag == '1'){
		var url = "<%=contextPath %>/SpringR/warehouse/getWareHouseById?id=<%=id%>";
		 jQuery.ajax({
		   type : 'POST',
		   url : url,
		   success : function(jsonData){   
			 var json = JSON.stringify(jsonData);
	   		 var data = eval("(" + json + ")");
	   		 if(data.length > 0){
	   			 jQuery(data).each(function(i,item){  
	   			  getWHAdmin(item.id);
	   			  jQuery('#name').val(item.name);
	   			  jQuery('#address').val(item.address);
	   			  getWHTypeName(item.type);
	   			  jQuery('#remark').val(item.remark);
 				});
	   		 }
	   		 
		   }
		 });
	}
}

function getWHAdmin(id){
	var names="";
	var ids="";
	var url = "<%=contextPath %>/SpringR/warehouse/getWHAdmin?id="+id;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
					if(names != ""){
						names+=",";
					}
					names+=item.userName;
					if(ids != ""){
						ids+=",";
					}
					ids+=item.seqId;
				});
			}
	   }
	 });
	jQuery('#person_id').val(ids);
	jQuery('#person_name').val(names);
}

function getWHTypeName(type) {
	var rtStr = "";
	var url = "<%=contextPath %>/SpringR/warehouse/getWHTypeName?type="+type;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				var names="";
				jQuery(json).each(function(i,item){
					if(names != ""){
						names+=",";
					}
					names+=item.name;
				});
				rtStr = names;
			}
	   }
	 });
	jQuery('#type_name').val(rtStr);
	jQuery('#type_id').val(type);
}

   function doSubmitForm(formObject) {
<%--	  if($("#unit_name").val() == ""){ --%>
<%--	    alert("计量单位名称不能为空！");--%>
<%--	    $("#unit_name").focus();--%>
<%--	    $("#unit_name").select();--%>
<%--	    return false;--%>
<%--	  }--%>
	 
	 var name = jQuery('#name').val();
	 var address = jQuery('#address').val();
	 var type = jQuery('#type_id').val();
	 var remark = jQuery('#remark').val();
	 var person_id = jQuery('#person_id').val();
	 var para = "person_id="+person_id+"&name="+encodeURIComponent(name)+"&address="+encodeURIComponent(address)+"&type="+type+"&remark="+encodeURIComponent(remark);
     var url="";
	 if(flag == '1'){
    	 url = "<%=contextPath %>/SpringR/warehouse/updateWareHouse?id=<%=id%>&"+para;
     }
	 else{
		 url = "<%=contextPath %>/SpringR/warehouse/addWareHouse?"+para;
	 }
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("添加成功");
		    window.location.href="whManage.jsp";
		  }else if(jsonData == "2"){
		   	alert("更新成功");
		    window.location.href="whManage.jsp";
		  }
		  else{
			   alert("添加失败"); 
		  }
	   }
	 });
   }
   
    function clearData(hiddenId,showId){
  	jQuery('#'+hiddenId).val('');
  	jQuery('#'+showId).val('');
  }
  
  
  var nameArray = null;
   function chooseVaccine(idArray,type){
	    nameArray = idArray;
	 	openDialogResize('chooseType.jsp',  520, 400);
 	}
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	       新增仓库信息
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
<table class="TableBlock" width="55%" align="center">
    <tr>
      <td nowrap class="TableData">仓库名称：</td>
      <td class="TableData" colspan=3>
       <input type="text" name="name" id="name" size="20" class="BigInput" >
      </td>
    </tr>
      <tr>
      <td nowrap class="TableData">仓库地址：</td>
       <td class="TableData" colspan="3">
 		<textarea name="address" id="address" ></textarea>
      </td>
    </tr>
      <tr>
      <td nowrap class="TableData">库存类型：</td>
       <td class="TableData" colspan="3">
       <input type="hidden" name="type_id" id="type_id" value="">
       <textarea cols="40" name="type_name" id="type_name"  cols="40" rows="3" style="overflow-y:auto;" class="SmallStatic" wrap="yes" readonly></textarea>
       <a href="javascript:;" class="orgAdd" onClick="chooseVaccine(['type_id','type_name'],'0');">添加</a>
       <a href="javascript:;" class="orgClear" onClick="clearData('type_id','type_name');">清空</a>
<%--	       <select  id="type" name="type">--%>
<%--	 			<option value="0"></option>--%>
<%--	 			<option value="1">产品库</option>--%>
<%--	 			<option value="2">半成品库</option>--%>
<%--	 			<option value="3">原材料库</option>--%>
<%--	 		</select>--%>
      </td>
    </tr>
     <tr>
      <td nowrap class="TableData">库管：</td>
      <td class="TableData" colspan=3>
      <input type="hidden" name="person_id" id="person_id" value="">
       <textarea cols="40" name="person_name" id="person_name" rows="3" style="overflow-y:auto;" class="SmallStatic" wrap="yes" readonly></textarea>
       <a href="javascript:;" class="orgAdd" onClick="selectUser(['person_id', 'person_name']);">选择</a>
       <a href="javascript:;" class="orgClear" onClick="jQuery('#person_id').val('');jQuery('#person_name').val('');">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="3">
 		<textarea name="remark" id="remark" ></textarea>
      </td>
    </tr>
    
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" value="提交" class="BigButton" onclick="return doSubmitForm(this.form);">
        <input type="button" value="返回" class="BigButton" onclick="javascript:window.history.go(-1)">
      </td>
    </tr>
  </table>
</form>
</body>
</html>
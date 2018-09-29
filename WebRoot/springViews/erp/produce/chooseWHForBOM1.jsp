<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String planId = request.getParameter("planId");
	String type = request.getParameter("type");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>仓库收料</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">

<!-- 封装表单的数据提交 -->
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">jQuery.noConflict();</script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/mobilesms/js/isdatetime.js" ></script>
<script  type="text/javascript"  src="map.js"></script>
<script type="text/javascript">
var planId="<%=planId%>";
var type="<%=type%>";

var dbNumMap = new Map();
var proNumMap = new Map();
function doInit(){
	var url = "<%=contextPath %>/SpringR/produce/getPlanCodeAndInsertBom?planId="+planId+"&type="+type;
	var proIds="";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
					 if(i==0){
 						 jQuery('#bom_id').val(item.bom_id);
					 }
					 var tds = "<tr id='"+i+"'><td align='center' nowrap>"+item.pro_code+"</td>";//产品编号
					  tds += "<td align='center' nowrap>"+item.pro_name+"</td>";//产品名称
					  tds += "<td align='center' nowrap>"+item.pro_type+"</td>";//产品型号
					  tds += "<td align='center' nowrap>"+item.unit_name+"</td>";//计量单位
					  tds += "<td align='center' nowrap>"+item.number+"</td>";//入库数量
					  proNumMap.put(item.pro_id,item.number);
					  tds += "<td align='center' nowrap><span id='add_"+item.pro_id+"'></span></td>";//库存收料情况
					  tds += "<td align='center' nowrap><img style='cursor:pointer' align='absmiddle' title='选择仓库' src='/yh/core/styles/style1/img/notify_new.gif' onclick=\"diogShow('"+item.bom_id+"','"+item.pro_id+"',"+item.number+")\"></td></tr>";
					  jQuery('#pro_table').append(tds);
					  if(proIds!=""){
					  	proIds+=",";
					  }
					  proIds+=item.pro_id;
			    });
			    jQuery("#products").val(proIds);
			}
	   }
	 });
	 
	 //填充产品对应仓库的信息
	 var bom_id = jQuery('#bom_id').val();
	 url = "<%=contextPath %>/SpringR/produce/getBOMDetial1?bom_id="+bom_id;
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
					var key = item.db_id+","+item.pro_id+","+item.wh_id+","+item.batch+","+bom_id;
					dbNumMap.put(key,item.number);
					var tds="<a id='"+key+"'>"+item.wh_name+"("+item.batch+")["+item.number+"]<img  style='cursor:pointer' align='absmiddle' src='/yh/core/styles/style1/img/remove.png' onclick=\"deletes('"+key+"','"+item.pro_id+"','"+item.number+"')\" title='删除'></a>";
					jQuery('#add_'+item.pro_id).append(tds);
					//更新产品的需求数量
					var proNumber = proNumMap.get(item.pro_id);
					var n =parseFloat(proNumber)-parseFloat(item.number);
					proNumMap.removeByKey(item.pro_id);
					proNumMap.put(item.pro_id,n);
			    });
			}
	   }
	 });
}
   function diogShow(bom_id,proId,orderNum){
  		var nums=proNumMap.get(proId);
  		var url="whOutUpdate.jsp?proId="+proId+"&orderNum="+nums+"&bom_id="+bom_id+"&type=2";
 		openDialogResize(url, 1000, 800);
  }
  
 function deletes(id,proId,number){
		var obj = document.getElementById(id);
		obj.parentNode.removeChild(obj);
		dbNumMap.removeByKey(id);//移除该记录
		
		//更新产品的需求数量
		var proNumber = proNumMap.get(proId);
		var n =parseFloat(proNumber)+parseFloat(number);
		proNumMap.removeByKey(proId);
		proNumMap.put(proId,n);
}

   function doSubmitForm(formObject) {
  		 //判断出库的数量是否与需求一致
   		var idsValue = jQuery("#products").val();
   		var ids = idsValue.split(",");
   		for(var j=0;j<ids.length;j++){
   			if(proNumMap.get(ids[j])!=0){
   				alert("库的数量与需求不一致");
   				return false;
   			}
   		}
   
	 var str = "";
	  var keyArr = dbNumMap.keys();
	 for(var i=0;i<keyArr.length;i++){
	 	if(str != "")
	 	{
			str +=";"; 		
	 	}
	 	var value = keyArr[i]+","+dbNumMap.get(keyArr[i]);
	 	str = str + value;	
	   }
	if(str == ""){
		alert("请进行仓库配料");
		return false; 
	}
     var url="<%=contextPath %>/SpringR/produce/updateBOMDetial1";
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   data: "data="+str,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("提交成功");
		    window.close();
		  }
		  else{
			   alert("提交失败"); 
		  }
	   }
	 });
   }
   
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	      仓库收料
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="bom_id" name="bom_id" value="-1">
 <input type="hidden" id="products" name="products" value="-1">
<table class="TableBlock" width="80%" align="center">
      <tr>
      <td nowrap class="TableData" colspan="2">BOM清单：</td>
    </tr>
      <tr>
      <td colspan="2" align="center">
		       <table id="pro_table" align="center" width="100%">
		    	<tr class="TableHeader">
		    	<td align='center' nowrap>产品编号</td>
		    	<td align='center' nowrap>产品名称</td>
		    	<td align='center' nowrap>规格型号</td>
		    	<td align='center' nowrap>计量单位</td>
		    	<td align='center' nowrap>入库数量</td>
		    	<td align='center' nowrap>库存收料[仓库名称/入库数量]</td>
		    	<td align='center' nowrap>操作</td>
		    	</tr>
		    </table>
    </td>
     </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" value="提交" class="BigButton" onclick="return doSubmitForm(this.form);">
        <input type="button" value="关闭" class="BigButton" onclick="javascript:window.close();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>
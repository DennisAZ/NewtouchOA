<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String id = request.getParameter("id");
	String flag = request.getParameter("flag");
	if("".equals(flag)||flag==null){
		flag="-1";
	}
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>查看报损单信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">

<!-- 封装表单的数据提交 -->
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/mobilesms/js/isdatetime.js" ></script>
		<script  type="text/javascript"  src="obejctJs1.js"></script>
<script type="text/javascript">
var arrMainPro=new Array();
var id="<%=id%>";
function doInit(){
	getCusMsg();
	initTime();
}



var arrWhName="";

function getCusMsg(){
var  k=0;
	var url = "<%=contextPath %>/SpringR/warehouse/getLoss?lossId=<%=id%>";

	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
				if(json.length > 0){
				jQuery(json).each(function(i,item){
					var arrPro=new Array();
					var orderNum=0;
				jQuery('#code').val(item.code);
				jQuery('#reason').val(item.reason);
				jQuery('#remark').val(item.remark);
				var url1 = "<%=contextPath %>/SpringR/warehouse/getLossDb?lossId=<%=id%>&proId="+item.proId;
				var arrPro=new Array();
				jQuery.ajax({
					   type : 'POST',
					   url : url1,
					   async:false,
					   success : function(jsonData1){   
							var data1 = JSON.stringify(jsonData1);
							var json1 = eval("(" + data1 + ")");
								if(json1.length > 0){
								jQuery(json1).each(function(j,item1){
					
							var  cfgs={
									db_id:item1.dbId,
									wh_id:item1.whId,
									number:item1.number
									}
						
						    var po=new ProductOut(cfgs);
						    arrPro.push(po);
						    var parm=item.proId+"_"+i
							arrWhName+="<a id='"+parm+"'>"+item1.whName+"["+item1.number+"]<img  style='cursor:pointer' align='absmiddle'  title='删除' src='/yh/core/styles/style1/img/remove.png' onclick=\"deleteDb('"+item.proId+"','"+parm+"',"+j+","+item1.number+")\"></a>";
							orderNum+=parseInt(item1.number);
								
	   					  });
				 	    }
				  	  }
				  });
				  	var  mainCfgs={
							  pro_id :item.proId,//产品id
						      productOut:arrPro,
						      price:item.pro_price
							};
			              var pd=new PD(mainCfgs);
				          arrMainPro.push(pd);
				  					var add="add"+item.proId;
									var tds = "<tr id='"+item.proId+"' ><td align='center' nowrap>"+item.pro_code+"</td>";//产品编号
									tds += "<td align='center' nowrap>"+item.pro_name+"</td>";//产品名称
									tds += "<td align='center' nowrap>"+item.ptName+"</td>";//订单数量
									tds += "<td align='center' nowrap>"+item.unit_name+"</td>";//已发数量
									tds += "<td align='center' nowrap><input type='text' id='"+item.proId+"Number' value='"+orderNum+"'  class='BigInput' size='10' readOnly maxlength='10'></td>";//已发数量
									tds += "<td align='center' nowrap id='"+item.pro_id+"'><span id='"+add+"'></span>"+arrWhName+"</td>";//仓库
									tds += "<td align='center' nowrap><img align='absmiddle' title='选择库存' style='cursor:pointer'  src='/yh/core/styles/style1/img/notify_new.gif' onclick=\"diogShow('"+item.proId+"',"+i+")\"><img  style='cursor:pointer' align='absmiddle' title='删除' src='/yh/core/styles/style1/img/remove.png' onclick=\"deletes('"+item.proId+"',"+i+")\"/></td></tr>";
									
									jQuery('#pro_table').append(tds);
									arrWhName="";
									orderNum="";
			 });
	 	 }
	  	}
	  });
	  
}
  var str="";
																																											
function updateObject(){
	for(var i=0;i<arrMainPro.length;i++){
		for(var j=0;j<arrMainPro[i].productOut.length;j++){
		if (str!= "") {
				str += ";";
		}
		str+=arrMainPro[i].pro_id+","+arrMainPro[i].cache_pro_id+","+arrMainPro[i].price+","+arrMainPro[i].productOut[j].db_id+","+arrMainPro[i].productOut[j].wh_id+","+arrMainPro[i].productOut[j].number+","+arrMainPro[i].productOut[j].sumNum;
		}
	}
}

 
   function doSubmitForm(formObject) {
	  updateObject();
	  var code = jQuery('#code').val();
	  var type = jQuery('#type').val();
	  var reason = jQuery('#reason').val();
	  var remark = jQuery('#remark').val();
	  	  var beginTime = jQuery('#beginTime').val();
	 var para = "code="+code+"&type="+encodeURIComponent(type)+"&remark="+encodeURIComponent(remark)+"&reason="+encodeURIComponent(reason)+"&arrChildStr="+ encodeURIComponent(str)+"&lossId="+id+"&beginTime="+beginTime;
	 var url="<%=contextPath %>/SpringR/warehouse/saleReturnUpdate?"+para;
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("更新成功");
		    window.location.href="dbLossManage.jsp";
		  }
		  else{
			   alert("更新失败"); 
		  }
	   }
	 });
   }


      function chooseProduct(idArray,type){
	    nameArray = idArray;

	 	openDialogResize('selectProduct.jsp',  520, 400);
 	}
 	function diogShow(proId,i){
  	 var url="<%=contextPath%>/springViews/erp/warehouse/returnIn.jsp?proId="+proId+"&k="+i;
 	 var str=openDialogResize(url,  520, 400);
  }
     function deletes(proId,i){
     	arrMainPro.remove(i);
     	jQuery('#'+proId).remove();
     	
     }
     function deleteDb(proId,parm,j,num){
    	 for(var i=0;i<arrMainPro.length;i++){
		 if(proId==arrMainPro[i].pro_id){
		 arrMainPro[i].productOut.remove(j);
		var sum= jQuery('#'+proId+"Number").val();
		jQuery('#'+proId+"Number").val(sum-num);
			
		 }
	  }
	       jQuery('#'+parm).remove();
     }

function initTime(){
  var beginTimePara = {
      inputId:'beginTime',
      property:{isHaveTime:false},
      bindToBtn:'beginTimeImg'
  };
  new Calendar(beginTimePara);
  

  var date = new Date();
  var y = date.getFullYear();
  var m = date.getMonth() + 1;
  m = (m > 9) ? m : '0' + m;
  var d = date.getDate();
  d = (d > 9) ? d : '0' + d;
  $('beginTime').value = y + '-' + m + '-' + d ;
}
  
</script>
</head>

<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	查看报损单明细
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="customerId" name="customerId">
<table class="TableBlock" width="80%" align="center">
	<tr>
	      <td nowrap class="TableData">报损单编号：</td>
	      <td class="TableData" >
	       <input id="code" name="code" type="text" value="" >
	      </td>
	      
	    </tr>
	      <tr>
      <td nowrap class="TableData">报损时间：</td>
      <td class="TableData" >
   		<input type="text" id="beginTime" name="beginTime" size="19" maxlength="19" class="BigInput" value="">
        <img src="<%=imgPath%>/calendar.gif" id="beginTimeImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">报损原因：</td>
      <td class="TableData" >
       <textarea id="reason" name="reason" ></textarea>
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData" colspan="3">
		<textarea id="remark" name="remark" ></textarea>
      </td>
    </tr>
    <tr>
    	 <td class="TableData" colspan="6">
 		<input type="button" value="+添加产品" class="BigButton" onclick="chooseProduct(['products'],0);">
      </td>
    </tr>
      <tr>
      <td colspan="6" align="center">
		       <table id="pro_table" align="center" width="80%">
		    	<tr class="TableHeader">
		    	<td align='center' nowrap>产品编号</td>
		    	<td align='center' nowrap>产品名称</td>
		    	<td align='center' nowrap>规格型号</td>
		    	<td align='center' nowrap>计量单位</td>
		    	<td align='center' nowrap>已经报损数量</td>
		    	<td align='center' nowrap>库存（仓库名称/报损数量）</td>
		    	<td align='center' nowrap>操作</td>
		    	</tr>
		    </table>
    </td>
     </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
              <input type="button" value="更新" class="BigButton" onclick="return doSubmitForm(this.form);">
              <%
              	if("-1".equals(flag)){
              %>
        <input type="button" value="返回" class="BigButton" onclick="javascript:window.history.back(-1);">
        <%
        }else{
        %>
         <input type="button" value="关闭" class="BigButton" onclick="javascript:window.close();">
        <%
        }
        %>
      </td>
    </tr>
  </table>
</form>
</body>
</html>
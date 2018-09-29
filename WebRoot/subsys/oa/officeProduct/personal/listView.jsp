<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*"%>
<%@ page  import="yh.subsys.oa.officeProduct.officeType.data.*"%> 
<%@ page  import="yh.subsys.oa.officeProduct.person.data.*"%> 
<html>
<head>
<%
  List<YHOfficeDepository> officeDep = (List<YHOfficeDepository>)request.getAttribute("findOfficeDeps");
  // List<YHOfficeType> officeTypes = (List<YHOfficeType>)request.getAttribute("officeTypes");
  // List<YHOfficeProducts> officeProducts = (List<YHOfficeProducts>)request.getAttribute("officeProducts");
  
%>

<title>办公用品批量申请</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" type="text/css" href="/inc/js/jquery/page/css/page.css"/>
<link rel="stylesheet" type="text/css" href="/theme/<?=$LOGIN_THEME?>/calendar.css"/>
<style>
.tip {position:absolute;display:none;text-align:center;font-size:9pt;font-weight:bold;z-index:65535;background-color:#DE7293;color:white;padding:5px}
.auto{text-overflow:ellipsis;white-space:nowrap;overflow:hidden;}
</style>
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<link rel="stylesheet" href ="<%=cssPath %>/style.css"/>
<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/notify/js/openWin.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/setting/codeJs/hrCodeJs.js"></script>
<script type="text/javascript">
function extend_depository(no)
{
    var depository = "depository_"+no;
    if(document.getElementById(depository)){
	    if(document.getElementById(depository).style.display=="none")
	       document.getElementById(depository).style.display="";
	    else
	    	 document.getElementById(depository).style.display="none";
    }
}
function extend(no)
{
    var sort = "sort_"+no;
    if(document.getElementById(sort).style.display=="none")
       document.getElementById(sort).style.display="";
    else
    	 document.getElementById(sort).style.display="none";
}

function high_light(no)
{
	 //alert(no);
   var td = "td_"+no;
   var tr = "tr_"+no;
   var check = "check_"+no;
   
   if(document.getElementById(check).checked)
   {
      document.getElementById(td).disabled=false;
      document.getElementById(td).focus();
   }     
   else
   {
      document.getElementById(td).disabled=true; 
   }
}
/** 
 * 替换s1为s2 
 */ 
 String.prototype.replaceAll = function(s1,s2){ 
    return this.replace(new RegExp(s1,"gm"),s2); 
 }
function CheckForm(){
	 var num=1;
	 var transFlags = $("TRANS_FLAG").value.trim();
		if(transFlags == ""){
		  alert("登记类型不能为空！");
			$("TRANS_FLAG").focus();
			$("TRANS_FLAG").select();
		  return false;
	  }
	   var count = document.form1.COUNT.value;
     var cc = 0;
	   for(var i = 0;i<count;i++)
	   {
	      var check ="check_"+(i+1);
	      //alert(check);
	      var td = "td_"+(i+1);
	     // alert(document.getElementById("check_"+(i+1)).checked);
	      //var tdValue = document.getElementById(td).value;
	      if(document.getElementById(check).checked)
	      {
	      	 if(document.getElementById(td).value=="0")
	      	 {
	            alert("申请数量不能为0！");
	            return (false);
	         }  
	      	 if(document.getElementById(td).value.replaceAll(" ","") == "" || document.getElementById(td).value == "null"){
			      alert("申请数量不能为空！");
			      return false;
		       }     
	      	if(isNaN(document.getElementById(td).value)){
	 	       alert("申请数量 必须为数字");
	 	       return false;
	 	      } 
	      	cc++;      
	      }
	   }
	  if (cc == 0) {
        alert("请选择物品！");
        return (false);
	  }
	  document.form1.action = contextPath +"/yh/subsys/oa/officeProduct/person/act/YHPersonalOfficeRecordAct/addOfficeProducts.act";
	  document.form1.submit();
	  return true;
}
//登记类型
function recordTypes(){
	var transFlag = $("TRANS_FLAG").value;
	$("recordType").value = transFlag;
}	
</script>

</head>
<body topmargin="5" class="bodycolor">
<form  action="#"  method="post" name="form1" id="form1" >
<table width="100%" cellspacing="0" cellpadding="3" border="0" class="small">
  <tbody>
  <tr>
    <td class="Big"><img align="absmiddle" src="<%=imgPath %>/notify_open.gif"><span class="big3"> 批量申请 
	登记类型：
         <select class="BigSelect" name="TRANS_FLAG" id="TRANS_FLAG" onchange="recordTypes();">
           <option selected value="">==请选择登记类型==</option>
           <option value="1">领用</option>
           <option value="2">借用</option>
           <option value="3">归还</option>
        </select>
        <input type="hidden" id="recordType" name="recordType">
        </span>
    </td>
  </tr>
</tbody>
</table>

<table align="center" style="border-left: 1px solid rgb(156, 182, 107); border-right: 1px solid rgb(156, 182, 107); border-bottom: 1px solid rgb(156, 182, 107); width: 95%; margin: 0px;">
	<tbody>
		<tr>
			<td>
				<table width="100%" align="center" style="margin: 0px; padding: 0px;" class="TableList">
					 <tbody>
					  <tr class="TableHeader">
					      <td nowrap="" width="20%" align="center">选择</td>
					      <td nowrap="" width="30%" align="center">名称</td>
					      <td nowrap="" width="20%" align="center">当前库存</td>
					      <td nowrap="" width="30%" align="center">申请数量</td>
					  </tr>
					</tbody>
				</table>
 <% int count = 0 ;
    int count1 = 0;
    int count2 = 0;
    if(officeDep !=null && officeDep.size()>0){
    	for(int i =0; i<officeDep.size()&& officeDep.size()>0; i++){
    		//officeDep.get(i).getSeqId();
    		//officeDep.get(i).getDepositoryName();
    		List<YHOfficeType> officeTypes = officeDep.get(i).getOfficeTypes();
    		count ++;
    		
 %>
				 <table width="100%" align="center" style="margin: 0px; padding: 0px; border: 1px none;" class="TableList">
				    <tbody>
				       <tr align="center">
				         <td style="border-bottom: 1px solid rgb(222, 223, 222);" colspan="4"><a onclick="extend_depository(<%=count%>)" link="color:#FFFFFF" href="javascript:;"><%=officeDep.get(i).getDepositoryName()==null?"":officeDep.get(i).getDepositoryName() %></a></td> 
				       </tr>   
				    </tbody>
				 </table>
				   <div style="" id="depository_<%=count%>">
	 			
	  <%
	    if(officeTypes != null){
	    	 for(int j=0; j<officeTypes.size() && officeTypes.size()>0; j++){
	    		 List<YHOfficeProducts> products = officeTypes.get(j).getProducts();
	    		 
	  %>
	
					 <table width="100%" align="center" style="margin: 0px; padding: 0px;" class="TableList">
						       <tbody>
							       <tr style="background-color: rgb(239, 235, 239);">
							         <td nowrap="" width="150" align="center" style="border-bottom: 1px dashed rgb(204, 204, 204); border-top: 1px solid rgb(222, 223, 222);"><a onclick="extend(<%=count1 %>)" href="javascript:;"><%=officeTypes.get(j).getTypeName() %></a></td> 
							         <td nowrap="" align="center" style="border-bottom: 1px dashed rgb(204, 204, 204); border-top: 1px solid rgb(222, 223, 222);"></td>
							         <td nowrap="" align="center" style="border-bottom: 1px dashed rgb(204, 204, 204); border-top: 1px solid rgb(222, 223, 222);"></td>
							         <td nowrap="" style="border-bottom: 1px dashed rgb(231, 227, 231); border-top: 1px solid rgb(222, 223, 222);"></td>
							       </tr>   
						     </tbody>
					  </table>
	
					  <table width="100%" align="center" style="margin: 0px; padding: 0px; border: 1px solid rgb(222, 223, 222);" id="sort_<%=count1 %>" class="TableList">
					      <tbody>
					      		  <%
					     if(products!=null){
					    	 for(int x =0; x<products.size()&&products.size()>0; x++){
					    		String ProStock = String.valueOf(products.get(x).getProStock());
					    		++ count2;
					    		
					  %>
						      <tr id="tr_<%=count2 %>">
						      	 <td nowrap="" width="20%" align="center">
						      	   <input type="checkbox" onclick="high_light(<%=count2 %>)" id="check_<%=count2 %>" name="checkBox" value="<%=count2 %>">
						      	 </td>
						         <td nowrap="" width="30%" align="center" id="proName" ><%=products.get(x).getProName()==null?"":products.get(x).getProName() %></td> 
						         <td nowrap="" width="20%" align="center" id ="proStock"><%=ProStock==null?"":ProStock %></td>
						         <td nowrap="" width="30%" align="center">
						          <input width="50" type="text" name="transQty" id="td_<%=count2 %>" value="" disabled=""> 
						         <!--  
						         <input width="50" type="text" name="COUNT_" id="td_" value="" disabled=""> 
						         <input width="50" type="text" name="curSum" id="curSum" disabled="">--> 
						         </td>
						         
						         <input type="hidden"  name="proNames" id ="proNames" value ="<%=products.get(x).getProName()==null?"":products.get(x).getProName() %>">
						         <input type="hidden"  name="proStocks" id ="proStocks" value ="<%=ProStock==null?"":ProStock %>">
						         <input type="hidden"  name="officeProductId" id="officeProductId" value="<%=products.get(x).getSeqId() %>">
						         <input type="hidden"  name="num" id="num" value="<%=count2 %>">
						         <input type="hidden"  name="PRO_Id_<%=count2 %>" id="PRO_Id_<%=count2 %>">
						      </tr>
						      	   <% }
				      } 
				 	   count1 ++; 
				   %>
					   </tbody>
					  </table>
					
			  <%  }
			  %>
			   <%  	
	    }
	  count++;
	    %>
	    </div>
	    
<%} %>
					    
	 
					<br>
						<div align="center">
							<input type="hidden" name="COUNT" id ="COUNT" value ="<%=count2 %>";>
							<input type="button" class="BigButton" onclick="CheckForm()" value="提交">
						</div>
					</td>
				</tr>
			</tbody>
		</table>
</form>
    <%
    
    }else{ %>
<table width="410" align="center" class="MessageBox">
	<tbody>
	  <tr class="head-no-title">
	      <td class="left"></td>
	      <td class="center">
	      </td>
	      <td class="right"></td>
	   </tr>
	   <tr class="msg">
	      <td class="left"></td>
	      <td class="center info">
	         <div class="msg-content">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;没有可以申请的办公用品。</div>
	      </td>
	      <td class="right"></td>
	   </tr>
	   <tr class="foot">
	      <td class="left"></td>
	      <td class="center"></td>
	      <td class="right"></td>
	   </tr>
	</tbody>
</table>
<%} %>
</body>
</html>
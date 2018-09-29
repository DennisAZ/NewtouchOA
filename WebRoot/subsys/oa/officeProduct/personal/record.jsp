<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="java.util.Map"%>
<%@ page  import="yh.subsys.oa.officeProduct.officeType.data.*"%> 
<html>
<head>
<%
  List<YHOfficeDepository> officeDep = (List<YHOfficeDepository>)request.getAttribute("findOfficeDepS");
  List<Map<String,String>> list = (List<Map<String,String>>)request.getAttribute("listOneOffice");

  String typeId = request.getParameter("TYPE_ID");
  String storeId = request.getParameter("DEPOSITORY"); 
  String proSeqId = request.getParameter("PRO_ID");
 //System.out.println(typeId+"=typeID=="+storeId+"=DEPTiD="+proSeqId);
%>
<title>个人办公用品登记</title>
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
function initSelectOption(element,rtJson){ 
    rtJson.rtData.each(function(e,i){ 
    element.options.add(new Option(e.typeName,e.seqId)); // 是后台集合中的对象
   }); 
 }

// 点击办公用品库 显示办公类型信息
function depositoryOfType(id){
	$("OFFICE_PROTYPE").innerHTML="";
    if (id == "-1") {
      $('OFFICE_PROTYPE').update("<option value=\"-1\">请选择</option>");
      $('PRO_ID').update("<option value=\"\">---请选择---</option>");
      return ;
    }
	var officeType = $('OFFICE_PROTYPE');
	var options = "";
	 var par = "officeId="+id;
	 var url = contextPath+'/yh/subsys/oa/officeProduct/person/act/YHPersonalOfficeRecordAct/getOfficeType.act';
	    var rtJson = getJsonRs(url,par);
	    if(rtJson.rtState == "0"){
	    	 var rtData = rtJson.rtData;
	         var listData = rtData.listData;
	         // alert(rsText);
	         for(var i = 0 ; i < listData.length ;  i ++ ){
	        	 officeType.options.add(new Option(listData[i].typeName, listData[i].seqId));
                  if (i == 0) {
      	        	 depositoryOfProducts(listData[i].seqId);
                  }
			    }
		   }
}
// 点击办公用品类型 显示办公用品信息
function depositoryOfProducts(id){
	 $("PRO_ID").innerHTML="";
	 var proId = $('PRO_ID');
	 var options ="";
	 var par = "officeId="+id;
	 var url = contextPath + '/yh/subsys/oa/officeProduct/person/act/YHPersonalOfficeRecordAct/getOfficeProducts.act';
	 var rtJson = getJsonRs(url,par);
	 if(rtJson.rtState =="0"){
	   var rtData = rtJson.rtData;
	   var listData = rtData.listData;
	   for(var i =0; i<listData.length; i++){
		   proId.options.add(new Option(listData[i].proName, listData[i].seqId));
		  }
	 }
}
//模糊选择
function LoadWindow1()
{
   if(document.getElementById('BLURRED').style.display=="none")
   {
      document.getElementById('BLURRED').style.display="";
      document.getElementById('PRO_ID').disabled="true";
      document.getElementById('PRO_ID').value="";
      document.getElementById('OFFICE_PROTYPE').disabled="true";
      document.getElementById('OFFICE_PROTYPE').value="";
      document.getElementById('OFFICE_DEPOSITORY').disabled="true";
      document.getElementById('OFFICE_DEPOSITORY').value="";
      document.form1.PRO_NAME.focus();
   }
   else
   {
   	  document.getElementById('BLURRED').style.display="none";   	  
      document.getElementById('PRO_ID').disabled="";
      document.getElementById('OFFICE_PROTYPE').disabled="";
      document.getElementById('OFFICE_DEPOSITORY').disabled="";
      document.form1.PRO_NAME.value="";
   }
}
/** 
 * 替换s1为s2 
 */ 
 String.prototype.replaceAll = function(s1,s2){ 
    return this.replace(new RegExp(s1,"gm"),s2); 
 }
 
function CheckForm(){
	var proId = $("PRO_ID").value;//办公用品
	var transQty = $("TRANS_QTY").value;//申请数量
	var officeDepository = $("OFFICE_DEPOSITORY").value;
	var proName = $("PRO_NAME").value;
	//alert(proId +"==" +proName);
	
	if((proId==""||proId=="null") && (proName==""||proName=="null")){
	      alert("办公用品或模糊名称不能为空  "); 
	      return false;
  }
  
 
	if(transQty.replaceAll(" ","") == "" || transQty == "null" || transQty =="0"){
	      alert("申请数量不能为空 或 0"); 
	      return false;
  }
	 if(isNaN(transQty)){
	       alert("申请数量 必须为数字");
	       return false;
	 }
	document.form1.action = contextPath +"/yh/subsys/oa/officeProduct/person/act/YHPersonalOfficeRecordAct/addPersonOfficeRecord.act";
	document.form1.submit();
	return true;  
  
 return false;
}
function checkProName(proName){
   //alert(proName);
	 var par = "proName="+proName;
	 var url = contextPath + '/yh/subsys/oa/officeProduct/person/act/YHPersonalOfficeRecordAct/getOfficeProductName.act';//模糊查询
	 var rtJson = getJsonRs(url,par);
	 
	 if(rtJson && rtJson.rtState =="0"){
	   var rtData = rtJson.rtData;
	   var listData = rtData.listData;
	   if(listData!=null){
		   for(var i =0; i<listData.length; i++){
			   //document.getElementById("PRO_NAME_AREA").style.display="";
			  var seqId = listData[i].seqId;
			   var proName = listData[i].proName;
			   var proStock = listData[i].proStock;
			   $('PRO_NAME_AREA').innerHTML = "<a color ='bulue' id='nameOrStock' style='cursor:pointer' onclick='divHidden();'>" + proName+"/库存"+proStock +"</a>";
			  }
	   }
	 }
}
function divHidden(){
	$('PRO_NAME_AREA').style.display='none';
	var proNameArea  = $('nameOrStock').innerText;
	$('PRO_NAME').value = proNameArea;
}

function seChange(){
	var transFlag = $('TRANS_FLAG').value;
	var temp = ""; 
	switch(transFlag){
	  case '1' : temp="领用";break;
	  case '2' : temp="借用";break;
	  case '3' : temp="归还";break;
	}
	$('transFlagStr').innerHTML = temp;
}
</script>
<body topmargin="5" class="bodycolor">
<table width="50%" cellspacing="0" cellpadding="3" border="0" class="small">
  <tbody><tr>
    <td class="Big"><img align="absmiddle" src="<%=imgPath %>/notify_new.gif"><span class="big3">&nbsp;新建办公用品登记</span>
    </td>
  </tr>
</tbody></table>
<br>
<form enctype="multipart/form-data" action="#"  method="post" name="form1" id="form1" onsubmit="">
<table width="70%" align="center" class="TableBlock">
    <tbody>
    <tr>
      <td nowrap="" class="TableData">登记类型：</td>
      <td class="TableData">
         <select  name="TRANS_FLAG" id="TRANS_FLAG" onchange="seChange()">
           <option selected value="1">领用</option>
           <option value="2">借用</option>
           <option value="3">归还</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap="" class="TableData">办公用品库： </td>
      <td nowrap="" class="TableData">
      <select onchange="depositoryOfType(this.value);" id="OFFICE_DEPOSITORY" name="OFFICE_DEPOSITORY">
				  <%if(list!=null){
					   for(int i =0; i<list.size(); i++){
					  %>
				       <option value="<%=list.get(i).get("deptId")==null?"":list.get(i).get("deptId") %>"><%=list.get(i).get("deptName")==null?"":list.get(i).get("deptName")%></option>
         <% } 
					}
         if(officeDep!=null){ %>
             <option value="-1">请选择</option>
         <%   for(int i=0; i<officeDep.size()&&officeDep.size()>0; i++){ %>
            <option value="<%=officeDep.get(i).getSeqId() %>"><%=officeDep.get(i).getDepositoryName()==null?"":officeDep.get(i).getDepositoryName() %></option>
          <%} 
          }%>
       </select>
    </td>
   </tr>
   <tr>
      <td nowrap="" class="TableData">办公用品类别：</td>
      <td class="TableData">
        <select onchange="depositoryOfProducts(this.value);"  id="OFFICE_PROTYPE" name="OFFICE_PROTYPE">
           <%if(list!=null){
					   for(int i =0; i<list.size(); i++){
					 %>
					  <option value="<%=list.get(i).get("typeId")==null?"":list.get(i).get("typeId")%>"><%=list.get(i).get("typeName")==null?"":list.get(i).get("typeName") %></option>
					  <%}
					   }else{ %>
					    <option value="-1">请选择</option> 
					   <%} %>
       </select>
      </td>
   </tr>
   <tr>
      <td nowrap="" class="TableData">办公用品：</td>
      <td id="OFFICE_PRODUCTS" class="TableData">
      
        <select id="PRO_ID" name="PRO_ID">
           
             <%if(list!=null){
					   for(int i =0; i<list.size(); i++){
					 %>
					  <option value="<%=list.get(i).get("proId")==null?"":list.get(i).get("proId")%>"><%=list.get(i).get("proName")==null?"":list.get(i).get("proName") %></option>
					  <%}
					   }else{ %>
					   <option value="">---请选择---</option>
					   <%} %>
         </select> &nbsp;
         <input type="button" onclick="LoadWindow1()" class="BigButton" value="模糊选择" title="模糊选择" name="SelectPro">
       </td>
      </tr>
     <tr style="display: none;" id="BLURRED">
      <td nowrap="" class="TableData">模糊名称:<font style="color:red">*</font></td>
      <td class="TableData">
        <input type="text" value="" class="BigInput" maxlength="20" size="20" name="PRO_NAME" id="PRO_NAME" onkeyup="checkProName(this.value)">&nbsp;&nbsp;
      <div  id="PRO_NAME_AREA" >
      
      </div>
      </td>
    </tr>
    <tr>
      <td nowrap="" class="TableData"><span id="transFlagStr">领用</span>数量：<font style="color:red">*</font></td>
      <td class="TableData">
        <input type="text" value="" class="BigInput" maxlength="20" size="20" name="TRANS_QTY" id="TRANS_QTY">&nbsp;&nbsp;<div>
      </div></td>
    </tr>
    <tr>
      <td nowrap="" class="TableData">备注：</td>
      <td class="TableData">
        <textarea class="BigInput" rows="5" cols="45" name="REMARK" id="REMARK"></textarea>
      </td>
    </tr>
    <input type="hidden" value="admin" name="TO_ID" id ="TO_ID">
    <input type="hidden" value="" name="PRO_ID_TEXT" id ="PRO_ID_TEXT">
    </tbody>
     <tfoot align="center" class="TableFooter">
      <td nowrap="" colspan="2">
        <input type="button" onclick="CheckForm();" value="确定" name="button"  class="BigButton">
      </td>

  </table>
</form>
</body>
</html>
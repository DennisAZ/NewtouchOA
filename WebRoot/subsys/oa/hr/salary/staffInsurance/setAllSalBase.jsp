<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="yh.subsys.oa.hr.salary.insurancePara.salItem.data.*"%> 
<%@ page  import="yh.subsys.oa.hr.salary.submit.data.*"%> 
<%@ page  import="java.util.List"%>
<%@ page  import="java.util.Map"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<%
  boolean submit = false;
  boolean isComputer = false;
  boolean isreport = false;
  int yesOther = 0;
  yesOther = (Integer)request.getAttribute("yesOtherId");
	String deptId = request.getParameter("deptId");
	if(deptId == null){
	  deptId = "";
	}
	List<YHSalItem> listSalItem = (List<YHSalItem>)request.getAttribute("listSalItem");
	//YHSalPerson person = (YHSalPerson)request.getAttribute("listPerson");
	List<YHSalPerson> listPerson = (List<YHSalPerson>)request.getAttribute("listPerson");
	YHSalPerson  person = listPerson.get(0);
	
	String title = "";
  for(int i=0; i<listSalItem.size(); i++){
    title += "S" + listSalItem.get(i).getSlaitemId() + ",";
  }
  YHHrInsurancePara insurance = null;
  double pensionPPay = 0.00;
  double pensionPPayAdd = 0.00;
  double pensonUPay = 0.00;
  double pensonUPayAdd = 0.00;
  double healthPPay = 0.00;
  double healthPPayAdd = 0.00;
  double healthUPPay =0.00;
  double healthUPPayAdd =0.00;
  double unemploymentPPay =0.00;
  double unemploymentPPayAdd =0.00;
  double unemploymentUPay =0.00;
  double unemploymentUPayAdd =0.00;
  double housingPPay =0.00;
  double housingPPayAdd =0.00;
  double housingUPay =0.00;
  double housingUPayAdd =0.00;
  double injuryUPay=0.00;
  double injuryUPayAdd=0.00;
  double maternityUPay = 0.00;
  double maternityUPayAdd = 0.00;

  List<YHHrInsurancePara> insuranceList = (List<YHHrInsurancePara>)request.getAttribute("hrInsurancePara");
  if(insuranceList!=null && insuranceList.size()>0){
  	   insurance = insuranceList.get(0);
  	   pensionPPay = insurance.getPensionPPay();
  	   pensionPPayAdd = insurance.getPensionPPayAdd();
  	   pensonUPay = insurance.getPensionUPay();
  	   pensonUPayAdd = insurance.getPensionUPayAdd();
  	   healthPPay = insurance.getHealthPPay();
  	   healthPPayAdd = insurance.getHealthPPayAdd();
  	   healthUPPay = insurance.getHealthUPay();
  	   healthUPPayAdd = insurance.getHealthUPayAdd();
  	   unemploymentPPay = insurance.getUnemploymentPPay();
  	   unemploymentPPayAdd = insurance.getUnemploymentPPayAdd();
  	   unemploymentUPay = insurance.getUnemploymentUPay();
  	   unemploymentUPayAdd = insurance.getUnemploymentUPayAdd();
  	   housingPPay =insurance.getHousingPPay();
  	   housingPPayAdd =insurance.getHousingPPayAdd();
  	   housingUPay =insurance.getHousingUPay();
  	   housingUPayAdd =insurance.getHousingUPayAdd();
  	   injuryUPay =insurance.getInjuryUPay();
  	   injuryUPayAdd = insurance.getInjuryUPayAdd();
  	   maternityUPay = insurance.getMaternityUPay();
  	   maternityUPayAdd = insurance.getMaternityUPayAdd();
  	   //yesOther = insurance.getYesOther();
  	}
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/salary/staffInsurance/js/setAllBaseSal.js"></script>
<script type="text/javascript">
var yesOther = "<%=yesOther%>";



function doSubmit(){
	var pars = Form.serialize($('form1')); 
	var url = "<%=contextPath%>/yh/subsys/oa/hr/salary/staffInsurance/act/YHWageBaseSetAct/setPLPersonSubmitInfo.act"; 
	var json = getJsonRs(url,pars); 
	if(json.rtState == '0'){ 
		window.location = "<%=contextPath %>/subsys/oa/hr/salary/staffInsurance/okJsp.jsp";
	//	alert("批量设置成功！"); 
	}else{ 
	  alert("信息修改失败"); 
	}
}


function FormatNumber(srcStr,nAfterDot)
{ 
   var srcStr,nAfterDot; 
   var resultStr,nTen; 
   srcStr = ""+srcStr+""; 
   strLen = srcStr.length; 
   dotPos = srcStr.indexOf("."); 
   if(dotPos == -1)
   { 
      resultStr = srcStr+"."; 
      for(i=0;i<nAfterDot;i++)
      { 
         resultStr = resultStr+"0"; 
      } 
      return resultStr=="NaN.00"?"":resultStr; 
   }else{ 
      if((strLen - dotPos - 1) >= nAfterDot){ 
         nAfter = dotPos + nAfterDot + 1; 
         nTen =1; 
         for(j=0;j<nAfterDot;j++)
         { 
            nTen = nTen*10; 
         } 
         resultStr = Math.round(parseFloat(srcStr)*nTen)/nTen; 
         return resultStr; 
      }else{ 
         resultStr = srcStr; 
         for(i=0;i<(nAfterDot - strLen + dotPos + 1);i++)
         { 
            resultStr = resultStr+"0"; 
         } 
         return resultStr=="NaN.00"?"":resultStr;
      } 
   } 
}
function calculate(){ // alert("ere");
	 //养老
	  var ALL_BASE = parseFloat(document.getElementById('allBase').value);
	  var PENSION_U=ALL_BASE * <%=pensonUPay%>/ 100 +<%=pensonUPayAdd%>;
	  var PENSION_P=ALL_BASE * <%=pensionPPay%>/ 100 + <%=pensionPPayAdd%>;
	  document.getElementById('pensionU').value=FormatNumber(PENSION_U,2);
	  document.getElementById('pensionP').value=FormatNumber(PENSION_P,2);
	  document.getElementById('pensionBase').value = FormatNumber(PENSION_U + PENSION_P,2);
	 //医疗
	  var MEDICAL_U=ALL_BASE * <%=healthUPPay%> / 100 + <%=healthUPPayAdd%>;
	  var MEDICAL_P=ALL_BASE * <%=healthPPay%> / 100 + <%=healthPPayAdd%>;
	  document.getElementById('medicalU').value=FormatNumber(MEDICAL_U,2);
	  document.getElementById('medicalP').value=FormatNumber(MEDICAL_P,2);
	  document.getElementById('medicalBase').value = FormatNumber(MEDICAL_U + MEDICAL_P,2);
	//生育
	  var MATERNITY_U=ALL_BASE * <%=maternityUPay%> / 100 + <%=maternityUPayAdd%>;
	  document.getElementById('fertilityU').value=FormatNumber(MATERNITY_U,2);
	  document.getElementById('fertilityBase').value = FormatNumber(MATERNITY_U,2);	   	

	//失业
	  var UNEMPLOYMENT_U=ALL_BASE * <%=unemploymentUPay%> / 100 +<%=unemploymentUPayAdd%> ;
	  var UNEMPLOYMENT_P=ALL_BASE * <%=unemploymentPPay%> / 100 + <%=unemploymentPPayAdd%>;
	  document.getElementById('unemploymentU').value=FormatNumber(UNEMPLOYMENT_U,2);
	  document.getElementById('unemploymentP').value=FormatNumber(UNEMPLOYMENT_P,2);
	  document.getElementById('unemploymentBase').value = FormatNumber(UNEMPLOYMENT_U + UNEMPLOYMENT_P,2);  

	//工伤
	  var INJURIES_U=ALL_BASE * <%=injuryUPay%> / 100 + <%=injuryUPayAdd%>;
	  document.getElementById('injuriesU').value=FormatNumber(INJURIES_U,2);
	  document.getElementById('injuriesBase').value = FormatNumber(INJURIES_U,2);
	//住房公积金
	  var HOUSING_U=ALL_BASE * <%=housingUPay%> / 100 + <%=housingUPayAdd%>;
	  var HOUSING_P=ALL_BASE * <%=housingPPay%> / 100 + <%=housingPPayAdd%>;
	  document.getElementById('housingU').value=FormatNumber(HOUSING_U,2);
	  document.getElementById('housingP').value=FormatNumber(HOUSING_P,2);
	  document.getElementById('housingBase').value = FormatNumber(HOUSING_U + HOUSING_P,2);   	   
}
function changeRange()
{
   if (document.getElementById("rang_role").style.display=="none")
   {
      document.getElementById("rang_role").style.display="";
      document.getElementById("rang_user").style.display="";
      document.getElementById("href_txt").innerText="隐藏按人员或角色发布";
   }
   else
   {
      document.getElementById("rang_role").style.display="none";
      document.getElementById("rang_user").style.display="none";
      document.getElementById("href_txt").innerText="按人员或角色发布";
   }
}
function gotos(){
	window.location.href= contextPath + "/subsys/oa/hr/salary/staffInsurance/blank.jsp";
	
}

function doInit() {
	
	$$('input').each(function(e, i) {// 循环Html页面中的所有输入框
		if (e.type != 'text') {
	    return;
	  }
		e.onkeyup = function() {//否侧只要输入的值不为 数字  就把当前文本框置为空
		    var data = e.value;
		    if(isNaN(data)){
		    	e.value = '';
	       return;
		   }
	    return;
	  }
  });
}

/*
function doInit() {
	$('pensionBase').onblur = function() {// onblur 鼠标离开文本输入id为'pensionBase'位置触发事件 如果不是  数字就清空,onkeyup鼠标弹起时发生事件
	    var data = $('pensionBase').value;
	    if(isNaN(data)){
	    	$('pensionBase').value = '';
       return false;
	   }
    return false;

  }
}
*/
</script>
</head>
<body topmargin="5" class="bodycolor" onload="doInit()">
<iframe name="temp" style="display:none"></iframe>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 薪酬基数批量设置</span>
    </td>
  </tr>
</table>
<form id="form1" name="form1">
<table align="center" id="cal_table" class="TableBlock" >
  <input type="hidden" id="total" name="total" value="<%=listPerson.size()==0?0:listPerson.size()%>">
  <input type="hidden" id="title" name="title" value="<%=title%>">
  <%  
      if(listPerson!=null && listPerson.size()>0){ 
	     for(int i = 0; i < listPerson.size(); i++){	      
	 %>
	    <input type="hidden" id="userId_<%=i %>" name="userId_<%=i %>" value="<%=listPerson.get(i).getUserId() %>">
  <%}
	 }%>
    <tr class="TableHeader">
      <td nowrap align="center">选择批量设置的人员范围:</td>
      <td nowrap align="center">&nbsp;</td>
    </tr>    	
    <tr>
      <td nowrap class="TableData">按部门设置：<br>&nbsp;<a href="javascript:;" id="href_txt" onclick="changeRange();">按人员或角色发布</a></td>
      <td class="TableData" id="td_dept">
        <input type="hidden" id ="dept" name="dept">
        <textarea cols=38 name="deptDesc" id="deptDesc" rows="2" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectDept(['dept','deptDesc']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('dept').value='';$('deptDesc').value='';");">清空</a>
      </td>
   </tr>
   
   
    <tr id="rang_user" style="display:none;border-right:1px #606275 solid;">
      <td nowrap class="TableData">按人员设置：</td>
      <td class="TableData" id="td_user">
	        <input type="hidden" name="user1" id ="user1">
	        <textarea cols=38 name="userDesc1" id="userDesc1" rows="2" class="BigStatic" wrap="yes" readonly></textarea>
	        <a href="javascript:;" class="orgAdd" onClick="selectUser(['user1', 'userDesc1'],null,null,1);">添加</a>
	        <a href="javascript:;" class="orgClear" onClick="$('user1').value='';$('userDesc1').value='';">清空</a>
        </td>
   </tr>
   
 
   
    <tr id="rang_role" style="display:none;">
      <td nowrap class="TableData">按角色设置：</td>
     <td class="TableData" id="td_role" >
        <input type="hidden" id ="role" name="role">
        <textarea cols=38 name="roleDesc" id="roleDesc" rows="2" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectRole(['role','roleDesc']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('role').value='';$('roleDesc').value='';">清空</a>
        <br>设置范围取部门、人员和角色的并集
      </td>
    </tr>
	<tbody>
	  <%
	   List<String> slist = person.getSlist();
      Map<String,Double> smap = person.getSmap();
	    if(listSalItem != null && listSalItem.size() > 0){
	      //财务上报项
        for(int i = 0; i < listSalItem.size(); i++){  
           if(listSalItem.get(i).getIscomputer().equals("0") && listSalItem.get(i).getIsreport().equals("0")){
             if(submit == false){
               submit = true;
             %>  
     <tr align="center" class="TableHeader">
       <td nowrap="" width="120px"><b>输入项</b></td>
       <td nowrap="" width="230px" align="right"><b>金额</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
     </tr>
          <% }%>
           
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px"><%=listSalItem.get(i).getItemName() %></td>
    <% 
         String s = (String)slist.get(i);
         double value = smap.get(s);
        // System.out.println("shu"+s +"==="+value);
         //String valueStr = YHUtility.getFormatedStr(value,1);
     %>

       <td nowrap="" width="230px" align="left">
         <input type="text" size="20" value="" class="SmallInput" name="<%=s %>" id="<%=s %>"  style="text-align: right;">
       </td>
     </tr>
       <%  }
         }
      }  
      //部门上报项
        for(int i = 0; i < listSalItem.size(); i++){ 
          if(listSalItem.get(i).getIsreport().equals("1")){
            if(isreport == false){
              isreport = true;
            %>  
     <tr align="center" class="TableHeader">
       <td nowrap="" width="120px"><b>上报项</b></td>
       <td nowrap="" width="230px" align="right"><b>金额</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
     </tr>
          <% }%>
           
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px"><%=listSalItem.get(i).getItemName() %></td>
       <% 
        String s = (String)slist.get(i);
        double value = smap.get(s);
        //System.out.println("bumen"+s +"==="+value);
     %>
        <td nowrap="" width="230px" align="left">
         <input type="text" size="20" value="" class="SmallInput" name="<%=s %>" id="<%=s %>"  style="text-align: right;">
       </td>
     </tr>
      <%}
      }
      //保险项
   if(yesOther==1){ %>   
	     <tr align="center" class="TableHeader">
	       <td nowrap="" width="120px"><b>保险项</td>
	       <td nowrap="" width="230px" align="right"><b>金额</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	     </tr>
	     <tr align="center" class="TableLine1">
	       <td nowrap="" width="120px">保险基数</td>
	       <td nowrap="" width="230px" align="left">
	         <input type="text" size="20" value="" class="SmallInput" name="allBase" id="allBase" style="text-align: right;"> 
	         <input type="button" onclick="calculate()" class="BigButton" value="计算">
	       </td>
	     </tr>               
	     <tr align="center" class="TableLine1">
	       <td nowrap="" width="120px">养老保险</td>
	       <td nowrap="" width="230px" align="left">
	         <input type="text" size="20" value="" class="SmallInput" name="pensionBase" id="pensionBase" style="text-align: right;">
	       </td>       
	     </tr>  
	     <tr align="center" class="TableLine1">
	       <td nowrap="" width="120px">单位养老</td>
	       <td nowrap="" width="230px" align="left">
	         <input type="text" size="20" value="" class="SmallInput" name="pensionU" id="pensionU" style="text-align: right;">
	       </td>       
	     </tr> 
	     <tr align="center" class="TableLine1">
	       <td nowrap="" width="120px">个人养老</td>
	       <td nowrap="" width="230px" align="left">
	         <input type="text" size="20" value="" class="SmallInput" name="pensionP" id="pensionP" style="text-align: right;">
	       </td>       
	     </tr>   
	     <tr align="center" class="TableLine1">
	       <td nowrap="" width="120px">医疗保险</td>
	       <td nowrap="" width="230px" align="left">
	         <input type="text" size="20" value="" class="SmallInput" name="medicalBase" id="medicalBase" style="text-align: right;">
	       </td>       
	     </tr>   
	     <tr align="center" class="TableLine1">
	       <td nowrap="" width="120px">单位医疗</td>
	       <td nowrap="" width="230px" align="left">
	         <input type="text" size="20" value="" class="SmallInput" name="medicalU" id="medicalU" style="text-align: right;">
	       </td>       
	     </tr>   
	     <tr align="center" class="TableLine1">
	       <td nowrap="" width="120px">个人医疗</td>
	       <td nowrap="" width="230px" align="left">
	         <input type="text" size="20" value="" class="SmallInput" name="medicalP" id="medicalP" style="text-align: right;">
	       </td>       
	     </tr>   
	     <tr align="center" class="TableLine1">
	       <td nowrap="" width="120px">生育保险</td>
	       <td nowrap="" width="230px" align="left">
	         <input type="text" size="20" value="" class="SmallInput" name="fertilityBase" id="fertilityBase" style="text-align: right;">
	       </td>       
	     </tr>   
	     <tr align="center" class="TableLine1">
	       <td nowrap="" width="120px">单位生育</td>
	       <td nowrap="" width="230px" align="left">
	         <input type="text" size="20" value="" class="SmallInput" name="fertilityU" id="fertilityU" style="text-align: right;">
	       </td>       
	     </tr>   
	     <tr align="center" class="TableLine1">
	       <td nowrap="" width="120px">失业保险</td>
	       <td nowrap="" width="230px" align="left">
	         <input type="text" size="20" value="" class="SmallInput" name="unemploymentBase" id="unemploymentBase" style="text-align: right;">
	       </td>       
	     </tr>   
	     <tr align="center" class="TableLine1">
	       <td nowrap="" width="120px">单位失业</td>
	       <td nowrap="" width="230px" align="left">
	         <input type="text" size="20" value="" class="SmallInput" name="unemploymentU" id="unemploymentU" style="text-align: right;">
	       </td>       
	     </tr>   
	     <tr align="center" class="TableLine1">
	       <td nowrap="" width="120px">个人失业</td>
	       <td nowrap="" width="230px" align="left">
	         <input type="text" size="20" value="" class="SmallInput" name="unemploymentP" id="unemploymentP" style="text-align: right;">
	       </td>       
	     </tr>   
	     <tr align="center" class="TableLine1">
	       <td nowrap="" width="120px">工伤保险</td>
	       <td nowrap="" width="230px" align="left">
	         <input type="text" size="20" value="" class="SmallInput" name="injuriesBase" id="injuriesBase" style="text-align: right;">
	       </td>       
	     </tr>   
	     <tr align="center" class="TableLine1">
	       <td nowrap="" width="120px">单位工伤</td>
	       <td nowrap="" width="230px" align="left">
	         <input type="text" size="20" value="" class="SmallInput" name="injuriesU" id="injuriesU" style="text-align: right;">
	       </td>       
	     </tr>   
	     <tr align="center" class="TableLine1">
	       <td nowrap="" width="120px">住房公积金</td>
	       <td nowrap="" width="230px" align="left">
	         <input type="text" size="20" value="" class="SmallInput" name="housingBase" id="housingBase" style="text-align: right;">
	       </td>       
	     </tr>   
	     <tr align="center" class="TableLine1">
	       <td nowrap="" width="120px">单位住房</td>
	       <td nowrap="" width="230px" align="left">
	         <input type="text" size="20" value="" class="SmallInput" name="housingU" id="housingU" style="text-align: right;">
	       </td>       
	     </tr>   
	     <tr align="center" class="TableLine1">
	       <td nowrap="" width="120px">个人住房</td>
	       <td nowrap="" width="230px" align="left">
	         <input type="text" size="20" value="" class="SmallInput" name="housingP" id="housingP" style="text-align: right;">
	       </td>       
	     </tr>   
  <%} %>
	</tbody>
</table>
<br>
<div align="center">
  <input type="button" value="确 定" class="BigButton" name="button" onclick="doSubmit();">
  <input type="button" value="取消" class="BigButton" onclick="gotos();">
</div>
</form>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
	<%@ page  import="yh.subsys.oa.hr.salary.insurancePara.salItem.data.*"%> 
<%@ page  import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
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
String yesOther = "";
List<YHHrInsurancePara> insuranceList = (List<YHHrInsurancePara>)request.getAttribute("findInsuranceList");
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
   yesOther = insurance.getYesOther();
   //System.out.println(yesOther+"===kkkkkkkk");
}
%>
<title>Insert title here</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
function goto(){
		window.location.href= contextPath + "/yh/subsys/oa/hr/salary/insurancePara/salItem/act/YHSalaryItemAct/getSalaryItemJson.act";
	}
function doSubmit(){
	$("form1").submit();
}
function doInit(){
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

</script>
</head>
<body class="bodycolor" topmargin="5" onload="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/sys_config.gif" align="absmiddle"><span class="big3">保险系数设置</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/salary/insurancePara/salItem/act/YHSalaryItemAct/findInsureBaseCountAct.act" method="post" name="form1" id="form1">
<table class="TableBlock" width="60%" align="center">
    <tr>
    	<td class="TableContent" size="40">显示设置：<input type="checkbox" name="YES_OTHER"  id="YES_OTHER" <%if(yesOther==null||yesOther.equals("")||yesOther.equals("1") ){%> checked <%} %>>
    		<label for="YES_OTHER">是否显示在工资项目中</label></td>
      </tr>
    <tr >
      <td nowrap  class="TableContent">养老保险系数：</td>
    </tr>
    <tr >
      <td nowrap class="TableData" style="padding-left:10px;" >个人支付：
      
      <INPUT type="text" name="PENSION_P_PAY" id="PENSION_P_PAY" class=BigInput size="15" value="<%=pensionPPay %>" style="text-align: right;">%  +
      <INPUT type="text" name="PENSION_P_PAY_ADD" id="PENSION_P_PAY_ADD" class=BigInput size="15" value="<%=pensionPPayAdd%>" style="text-align: right;"></td>
    </tr>
    <tr>
      <td nowrap class="TableData" style="padding-left:10px;">单位支付：
      <INPUT type="text" name="PENSION_U_PAY" id="PENSION_U_PAY" class=BigInput size="15" value="<%=pensonUPay %>" style="text-align: right;">%  +
      <INPUT type="text" name="PENSION_U_PAY_ADD" id="PENSION_U_PAY_ADD" class=BigInput size="15" value="<%=pensonUPayAdd %>" style="text-align: right;">
    </tr>
    <tr>
      <td nowrap  class="TableContent">医疗保险系数：</td>
    </tr>
    <tr>
      <td nowrap class="TableData" style="padding-left:10px;">个人支付：
      <INPUT type="text" name="HEALTH_P_PAY" id="HEALTH_P_PAY" class=BigInput size="15" value="<%=healthPPay %>" style="text-align: right;">%  +
      <INPUT type="text" name="HEALTH_P_PAY_ADD" id="HEALTH_P_PAY_ADD" class=BigInput size="15" value="<%=healthPPayAdd %>" style="text-align: right;">
    </tr>
    <tr>
      <td nowrap class="TableData" style="padding-left:10px;">单位支付：
      <INPUT type="text" name="HEALTH_U_PAY" id="HEALTH_U_PAY" class=BigInput size="15" value="<%=healthUPPay %>" style="text-align: right;">%  +
      <INPUT type="text" name="HEALTH_U_PAY_ADD" id="HEALTH_U_PAY_ADD" class=BigInput size="15" value="<%=healthUPPayAdd %>" style="text-align: right;">
    </tr>
    <tr>
      <td nowrap  class="TableContent">失业保险系数：</td>
    </tr>
    <tr>
      <td nowrap class="TableData" style="padding-left:10px;">个人支付：
      <INPUT type="text" name="UNEMPLOYMENT_P_PAY" id="UNEMPLOYMENT_P_PAY" class=BigInput size="15" value="<%=unemploymentPPay %>" style="text-align: right;">%  +
      <INPUT type="text" name="UNEMPLOYMENT_P_PAY_ADD" id="UNEMPLOYMENT_P_PAY_ADD" class=BigInput size="15" value="<%=unemploymentPPayAdd %>" style="text-align: right;">
    </tr>
    <tr>
      <td nowrap class="TableData" style="padding-left:10px;">单位支付：
      <INPUT type="text" name="UNEMPLOYMENT_U_PAY" id="UNEMPLOYMENT_U_PAY" class=BigInput size="15" value="<%=unemploymentUPay %>" style="text-align: right;">%  +
      <INPUT type="text" name="UNEMPLOYMENT_U_PAY_ADD" id="UNEMPLOYMENT_U_PAY_ADD" class=BigInput size="15" value="<%=unemploymentUPayAdd %>" style="text-align: right;">
    </tr>
    <tr>
      <td nowrap  class="TableContent">住房公积金系数：</td>
    </tr>
    <tr>
      <td nowrap class="TableData" style="padding-left:10px;">个人支付：
      <INPUT type="text" name="HOUSING_P_PAY" id="HOUSING_P_PAY" class=BigInput size="15" value="<%=housingPPay %>" style="text-align: right;">%  +
      <INPUT type="text" name="HOUSING_P_PAY_ADD" id="HOUSING_P_PAY" class=BigInput size="15" value="<%=housingPPayAdd %>" style="text-align: right;">
    </tr>
    <tr>
      <td nowrap class="TableData" style="padding-left:10px;">单位支付：
      <INPUT type="text"name="HOUSING_U_PAY" class=BigInput size="15" value="<%=housingUPay %>" style="text-align: right;">%  +
      <INPUT type="text"name="HOUSING_U_PAY_ADD" class=BigInput size="15" value="<%=housingUPayAdd %>" style="text-align: right;">
    </tr>
     <tr>
      <td nowrap  class="TableContent">工伤保险系数：</td>
    </tr>
    <tr>
      <td nowrap class="TableData" style="padding-left:10px;">单位支付：
      <INPUT type="text" name="INJURY_U_PAY" id="INJURY_U_PAY" class=BigInput size="15" value="<%=injuryUPay %>" style="text-align: right;">%  +
      <INPUT type="text" name="INJURY_U_PAY_ADD" id="INJURY_U_PAY_ADD" class=BigInput size="15" value="<%=injuryUPayAdd %>" style="text-align: right;">
    </tr>
     <tr>
      <td nowrap  class="TableContent">生育保险系数：</td>
    </tr>
    <tr>
      <td nowrap class="TableData" style="padding-left:10px;">单位支付：
      <INPUT type="text" name="MATERNITY_U_PAY" id="MATERNITY_U_PAY" class=BigInput size="15" value="<%=maternityUPay %>" style="text-align: right;">%  +
      <INPUT type="text" name="MATERNITY_U_PAY_ADD" id="MATERNITY_U_PAY_ADD" class=BigInput size="15" value="<%=maternityUPayAdd %>" style="text-align: right;">
    </tr>
    
   <tr align="center" class="TableControl">
      <td colspan=2 nowrap>
      <input type="button" value="保存"  class="BigButton" name="button" onclick="doSubmit();">
        <input type="reset" value="重置" class="BigButton">
      </td>
   </tr>
  </table>
</form>
</body>
</html>
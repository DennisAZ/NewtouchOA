<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="yh.subsys.oa.hr.salary.insurancePara.salItem.data.*"%> 
<%@ page  import="yh.subsys.oa.hr.salary.submit.data.*"%> 
<%@ page  import="java.util.List"%>
<%@ page  import="java.util.Map"%>
<html>
<head>
<%
  boolean submit = false;
  boolean isComputer = false;
  boolean isreport = false;
  int yesOther = 0;
  yesOther = (Integer)request.getAttribute("yesOtherId");
	String userId = request.getParameter("userId");
	if(userId == null){
	  userId = "";
	}
	List<YHSalItem> listSalItem = (List<YHSalItem>)request.getAttribute("listSalItem");
	YHSalPerson person = (YHSalPerson)request.getAttribute("person");
	String title = "";
  for(int i=0; i<listSalItem.size(); i++){
    title += "S" + listSalItem.get(i).getSlaitemId() + ",";
  }
%>
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
<script type="text/javascript">
var yesOther = "<%=yesOther%>";
function doInit(){
	$('userName').innerHTML = staffNameFunc("<%=userId%>");
	$('flowId').value = window.parent.flowId;
	if(yesOther == 1){
	  setDate();
	}
  $$('input').each(function(e, i) {
    if (e.type != 'text') {
      return;
    }
    e.onkeyup = function() {
      var data = e.value;
      if(isNaN(data)){
        e.value = '';
        return;
      }
    return;
    }
  });
}

//日期
function setDate(){
	var date1Parameters = {
	   inputId:'insuranceDate',
	   property:{isHaveTime:false}
	   ,bindToBtn:'date1'
	};
	new Calendar(date1Parameters);
}

/**
 * 单位员工名称
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function staffNameFunc(cellData){
  var url = contextPath + "/yh/subsys/oa/hr/manage/leave/act/YHHrStaffLeaveAct/getUserName.act";
  var rtJson = getJsonRs(url, "userIdStr=" + cellData);
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function doSubmit(){
	var pars = Form.serialize($('form1')); 
	var url = "<%=contextPath%>/yh/subsys/oa/hr/salary/submit/act/YHHrSubmitAct/setSubmitUserInfo.act"; 
	var json = getJsonRs(url,pars); 
	if(json.rtState == "0"){ 
		window.location = "<%=contextPath %>/subsys/oa/hr/salary/submit/run/newRemind2.jsp?userId=<%=userId%>&flowId="+window.parent.flowId;
	}else{ 
	  alert("信息修改失败"); 
	}
}

function onComputer(name){
	calculate(name+'_formula',name,0);
}

function onClean(name){
  $(name).value = "0.00";
}

function onFormula(name){
  alert($(name+'_formulaname').value);
}

function onComputerAll(){
	var titleStr = $('title').value;
	titleStr = titleStr.substring(0,titleStr.length-1);
	titleStr = titleStr.split(",");
	for(var i = 0; i < titleStr.length; i++){
		if($(titleStr[i]+'_formula')){
			calculate(titleStr[i]+'_formula',titleStr[i],0);
		}
	}
}

function calculate(FORMULA,TARGET,SIGN){
  var s1=$(FORMULA).value;
  var re;
  //------所得税计算---------
  if(s1.indexOf("<")!=-1 && s1.indexOf(">")!=-1){
    re=/\<|\>/gi;
    var r=s1.replace(re, "");
    re=/\[|\]/gi;
    r=r.replace(re, "");
    re=/\\$/gi;
    r=r.replace(re, "S");

    for(var i=document.form1.ITEM_COUNT.value; i>0; i--){
      re="S"+i;
      if(($(re).value.trim()=="" || isNaN($(re).value.trim())) && r.indexOf(re)!=-1) {
        if(SIGN==0){
          alert($(re+"_index").innerHTML+"的值尚未填写或值为非数字!");
          return;
        }  
        r=r.replace(re,"0");
      }
      else{
        r=r.replace(re,"("+$(re).value+")");
      }
    }
    cha=eval(r);
    if (cha<=0) {$(TARGET).value=0;}
    if (cha>0&&cha<=500) {$(TARGET).value=(cha*0.05).toFixed(2);}
    if (cha>500&&cha<=2000) {$(TARGET).value=(cha*0.1-25).toFixed(2);}
    if (cha>2000&&cha<=5000) {$(TARGET).value=(cha*0.15-125).toFixed(2);}
    if (cha>5000&&cha<=20000) {$(TARGET).value=(cha*0.2-375).toFixed(2);}
    if (cha>20000&&cha<=40000) {$(TARGET).value=(cha*0.25-1375).toFixed(2);}
    if (cha>40000&&cha<=60000) {$(TARGET).value=(cha*0.30-3375).toFixed(2);}
    if (cha>60000&&cha<=80000) {$(TARGET).value=(cha*0.35-6375).toFixed(2);}
    if (cha>80000&&cha<=100000) {$(TARGET).value=(cha*0.4-10375).toFixed(2);}
    if (cha>100000&&cha>100000) {$(TARGET).value=(cha*0.45-15375).toFixed(2);}
    return;
  }

  re=/\[|\]/gi;
  var r=s1.replace(re, "");
  re=/\\$/gi;
  var r=r.replace(re, "S");

  for(var i=document.form1.ITEM_COUNT.value; i>0; i--){
    re="S"+i;
    if(($(re).value.trim()=="" || isNaN($(re).value.trim())) && r.indexOf(re)!=-1){
      if(SIGN==0){
        alert($(re+"_index").innerHTML+"的值尚未填写或值为非数字!");
        return;
      }
      r=r.replace(re,"0");
    }
    else
      r=r.replace(re,$(re).value);
  }
  $(TARGET).value=eval(r).toFixed(2);
}

function onEditSalar(){
	var url = "<%=contextPath%>/yh/subsys/oa/hr/salary/staffInsurance/act/YHWageBaseSetAct/getSalItemIdUserAct.act?personId=<%=userId%>&userName="+encodeURI($('userName').innerHTML);
  parent.addressmain.location = url;
}
</script>
</head>
<body topmargin="5" class="bodycolor" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/salary.gif" align="middle"><span class="big3"> 工资数据录入（<span class="big3" id="userName" name="userName"></span>）</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<form id="form1" name="form1" method="post" action="">
<table align="center" id="cal_table" class="TableBlock" >
  <input type="hidden" id="userId" name="userId" value="<%=userId %>">
  <input type="hidden" id="flowId" name="flowId" value="">
  <input type="hidden" id="title" name="title" value="<%=title%>">
  <input type="hidden" id="ITEM_COUNT" name="ITEM_COUNT" value="<%=listSalItem.size() %>">
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
       <td nowrap="" width="300px" align="right"><b style="padding-right:55px">金额</b><a href="javascript:onEditSalar();"><b style="padding-right:50px">修改薪酬基数</b></a></td>
     </tr>
          <% }%>
           
     <tr align="center" class="TableLine1">
     <% 
         String s = (String)slist.get(i);
         double value = smap.get(s);
         String valueStr = YHUtility.getFormatedStr(value,1);
     %>
       <td nowrap="" width="120px"><span id="<%=s %>_index" name="<%=s %>_index"><%=listSalItem.get(i).getItemName() %></span></td>
       <td nowrap="" width="300px" align="left"><input type="text" size="20" value="<%=valueStr %>" class="SmallInput" name="<%=s %>" id="<%=s %>" style="text-align: right;"></td>
     </tr>
       <%  }
         }
      }  
	    //计算项

      for(int i = 0; i < listSalItem.size(); i++){ 
        if(listSalItem.get(i).getIscomputer().equals("1") ){
          if(isComputer == false){
            isComputer = true;
          %>  
     <tr align="center" class="TableHeader">
       <td nowrap="" width="120px"><b>计算项</b></td>
       <td nowrap="" width="300px" align="right"><b style="padding-right:80px">金额</b><a href="javascript:onComputerAll();"><b style="padding-right:50px">计算全部</b></a></td>
     </tr>
          <% }%>
           
     <tr align="center" class="TableLine1">
     <% 
        String s = (String)slist.get(i);
        double value = smap.get(s);
        String valueStr = YHUtility.getFormatedStr(value,1);
     %>
       <td nowrap="" width="120px"><span id="<%=s %>_index" name="<%=s %>_index"><%=listSalItem.get(i).getItemName() %></span></td>
       <td nowrap="" width="300px" align="left">
         <input type="text" size="16" value="<%=valueStr %>" class="BigStatic" readonly name="<%=s %>" id="<%=s %>" style="text-align: right;">
         <input type="hidden" id="<%=s %>_formula" name="<%=s %>_formula" value="<%=listSalItem.get(i).getFormula() %>">
         <input type="hidden" id="<%=s %>_formulaname" name="<%=s %>_formulaname" value="<%=listSalItem.get(i).getFormulaname() %>">
         <input type="button" value="计算" onclick="onComputer('<%=s %>')" class="BigButton">&nbsp;
         <input type="button" value="清空" onclick="onClean('<%=s %>')" class="BigButton">&nbsp;
         <a href="javascript:onFormula('<%=s %>');">计算公式</a>
       </td>
     </tr>
      <%}
      }
      //部门上报项

        for(int i = 0; i < listSalItem.size(); i++){ 
          if(listSalItem.get(i).getIscomputer().equals("0") && listSalItem.get(i).getIsreport().equals("1")){
            if(isreport == false){
              isreport = true;
            %>  
     <tr align="center" class="TableHeader">
       <td nowrap="" width="120px"><b>上报项</b></td>
       <td nowrap="" width="300px" align="right"><b style="padding-right:180px">金额</b></td>
     </tr>
          <% }%>
           
     <tr align="center" class="TableLine1">
     <% 
        String s = (String)slist.get(i);
        double value = smap.get(s);
        String valueStr = YHUtility.getFormatedStr(value,1);
     %>
       <td nowrap="" width="120px"><span id="<%=s %>_index" name="<%=s %>_index"><%=listSalItem.get(i).getItemName() %></span></td>
       <td nowrap="" width="300px" align="left"><input type="text" size="16" value="<%=valueStr %>" class="BigStatic" readonly name="<%=s %>" id="<%=s %>" style="text-align: right;"></td>
     </tr>
      <%}
      }
      
      //保险项

        if(yesOther==1){ %>   
     <tr align="center" class="TableHeader">
       <td nowrap="" width="120px"><b>保险项(是否投保<input type="checkbox" id="insuranceOther" name="insuranceOther" <%if(person.getInsuranceOther().equals("1")){%>checked<%} %>>)</b></td>
       <td nowrap="" width="300px" align="right"><b  style="padding-right:180px">金额</b></td>
     </tr>
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">保险基数</td>
       <td nowrap="" width="300px" align="left">
         <input type="text" size=16 value="<%=YHUtility.getFormatedStr(person.getAllBase(),1) %>" class="BigStatic" readonly name="allBase" id="allBase" style="text-align: right;"> 
       </td>
     </tr>               
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">养老保险</td>
       <td nowrap="" width="300px" align="left">
         <input type="text" size="16" value="<%=YHUtility.getFormatedStr(person.getPensionBase(),1) %>" class="BigStatic" readonly name="pensionBase" id="pensionBase" style="text-align: right;">
       </td>       
     </tr>  
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">单位养老</td>
       <td nowrap="" width="300px" align="left">
         <input type="text" size="16" value="<%=YHUtility.getFormatedStr(person.getPensionU(),1) %>" class="BigStatic" readonly name="pensionU" id="pensionU" style="text-align: right;">
       </td>       
     </tr> 
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">个人养老</td>
       <td nowrap="" width="300px" align="left">
         <input type="text" size="16" value="<%=YHUtility.getFormatedStr(person.getPensionP(),1) %>" class="BigStatic" readonly name="pensionP" id="pensionP" style="text-align: right;">
       </td>       
     </tr>   
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">医疗保险</td>
       <td nowrap="" width="300px" align="left">
         <input type="text" size="16" value="<%=YHUtility.getFormatedStr(person.getMedicalBase(),1) %>" class="BigStatic" readonly name="medicalBase" id="medicalBase" style="text-align: right;">
       </td>       
     </tr>   
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">单位医疗</td>
       <td nowrap="" width="300px" align="left">
         <input type="text" size="16" value="<%=YHUtility.getFormatedStr(person.getMedicalU(),1) %>" class="BigStatic" readonly name="medicalU" id="medicalU" style="text-align: right;">
       </td>       
     </tr>   
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">个人医疗</td>
       <td nowrap="" width="300px" align="left">
         <input type="text" size="16" value="<%=YHUtility.getFormatedStr(person.getMedicalP(),1) %>" class="BigStatic" readonly name="medicalP" id="medicalP" style="text-align: right;">
       </td>       
     </tr>   
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">生育保险</td>
       <td nowrap="" width="300px" align="left">
         <input type="text" size="16" value="<%=YHUtility.getFormatedStr(person.getFertilityBase(),1) %>" class="BigStatic" readonly name="fertilityBase" id="fertilityBase" style="text-align: right;">
       </td>       
     </tr>   
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">单位生育</td>
       <td nowrap="" width="300px" align="left">
         <input type="text" size="16" value="<%=YHUtility.getFormatedStr(person.getFertilityU(),1) %>" class="BigStatic" readonly name="fertilityU" id="fertilityU" style="text-align: right;">
       </td>       
     </tr>   
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">失业保险</td>
       <td nowrap="" width="300px" align="left">
         <input type="text" size="16" value="<%=YHUtility.getFormatedStr(person.getUnemploymentBase(),1) %>" class="BigStatic" readonly name="unemploymentBase" id="unemploymentBase" style="text-align: right;">
       </td>       
     </tr>   
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">单位失业</td>
       <td nowrap="" width="300px" align="left">
         <input type="text" size="16" value="<%=YHUtility.getFormatedStr(person.getUnemploymentU(),1) %>" class="BigStatic" readonly name="unemploymentU" id="unemploymentU" style="text-align: right;">
       </td>       
     </tr>   
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">个人失业</td>
       <td nowrap="" width="300px" align="left">
         <input type="text" size="16" value="<%=YHUtility.getFormatedStr(person.getUnemploymentP(),1) %>" class="BigStatic" readonly name="unemploymentP" id="unemploymentP" style="text-align: right;">
       </td>       
     </tr>   
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">工伤保险</td>
       <td nowrap="" width="300px" align="left">
         <input type="text" size="16" value="<%=YHUtility.getFormatedStr(person.getInjuriesBase(),1) %>" class="BigStatic" readonly name="injuriesBase" id="injuriesBase" style="text-align: right;">
       </td>       
     </tr>   
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">单位工伤</td>
       <td nowrap="" width="300px" align="left">
         <input type="text" size="16" value="<%=YHUtility.getFormatedStr(person.getInjuriesU(),1) %>" class="BigStatic" readonly name="injuriesU" id="injuriesU" style="text-align: right;">
       </td>       
     </tr>   
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">住房公积金</td>
       <td nowrap="" width="300px" align="left">
         <input type="text" size="16" value="<%=YHUtility.getFormatedStr(person.getHousingBase(),1) %>" class="BigStatic" readonly name="housingBase" id="housingBase" style="text-align: right;">
       </td>       
     </tr>   
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">单位住房</td>
       <td nowrap="" width="300px" align="left">
         <input type="text" size="16" value="<%=YHUtility.getFormatedStr(person.getHousingU(),1) %>" class="BigStatic" readonly name="housingU" id="housingU" style="text-align: right;">
       </td>       
     </tr>   
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">个人住房</td>
       <td nowrap="" width="300px" align="left">
         <input type="text" size="16" value="<%=YHUtility.getFormatedStr(person.getHousingP(),1) %>" class="BigStatic" readonly name="housingP" id="housingP" style="text-align: right;">
       </td>       
     </tr>   
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">投保日期</td>
       <td nowrap="" width="300px" align="left">
         <input type="text" size="20" value="<%=person.getInsuranceDate() == null ? "" : person.getInsuranceDate()%>" class="SmallInput" name="insuranceDate" id="insuranceDate" style="text-align: right;" readonly>
         <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
       </td>       
     </tr>   
      <%} %>
     <tr align="center" class="TableHeader">
       <td nowrap="" width="120px"><b>备注</b></td>
       <td nowrap="" width="300px" align="right"></td>
     </tr>
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">备注</td>
       <td nowrap="" width="300px" align="left">
         <textarea rows="3" cols="27" id="memo" name="memo" class="BigInput"><%=person.getMemo() == null ? "" : person.getMemo()%></textarea>
       </td>       
     </tr> 
	</tbody>
</table>
<br>
<div align="center">
  <input type="button" value="确 定" class="BigButton" onclick="doSubmit()">
</div>
</form>
</body>
</html>
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
	var url = "<%=contextPath%>/yh/subsys/oa/hr/salary/report/act/YHHrSubmitAct/setSubmitUserInfo.act"; 
	var json = getJsonRs(url,pars); 
	if(json.rtState == "0"){ 
		window.location = "<%=contextPath %>/subsys/oa/hr/salary/report/run/newRemind2.jsp?userId=<%=userId%>&flowId="+window.parent.flowId;
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
    re=/\$/gi;
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
  re=/\$/gi;
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
</script>
</head>
<body topmargin="5" class="bodycolor" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/salary.gif" align="middle"><span class="big3"> 工资数据查阅（<span class="big3" id="userName" name="userName"></span>）</span>&nbsp;&nbsp;
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
	  <tr align="center" class="TableHeader">
      <td nowrap="" width="120px"><b>工资项目</b></td>
      <td nowrap="" width="300px" align="center"><b>金额</b></td>
    </tr>
	  <%
      List<String> slist = person.getSlist();
      Map<String,Double> smap = person.getSmap();
	    if(listSalItem != null && listSalItem.size() > 0){
	      //工资项目
        for(int i = 0; i < listSalItem.size(); i++){  %>
     <tr align="center" class="TableLine1">
     <% 
         String s = (String)slist.get(i);
         double value = smap.get(s);
         String valueStr = YHUtility.getFormatedStr(value,1);
     %>
       <td nowrap width="120px"><span id="<%=s %>_index" name="<%=s %>_index"><%=listSalItem.get(i).getItemName() %></span></td>
       <td nowrap width="300px" align="center"><%=valueStr %></td>
     </tr>
       <%}
      }  %>
     <tr align="center" class="TableLine1">
       <td nowrap width="120px">备注</td>
       <td nowrap width="300px" align="left">
         <%=person.getMemo() == null ? "" : person.getMemo()%>
       </td>       
     </tr> 
	</tbody>
</table>
<br>
<div align="center">
  <input type="button" value="返 回" class="BigButton" onclick="history.go(-1)">
</div>
</form>
</body>
</html>
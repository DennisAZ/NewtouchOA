<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String seqId = request.getParameter("seqId");
if(seqId == null) {
  seqId = "";
}
String pageNo = request.getParameter("pageNo");
if(pageNo == null) {
  pageNo = "";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href = "<%=contextPath %>/core/styles/style1/css/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/rad/CodeUtil/radio.js" ></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var pageNo = "<%=pageNo%>";
var smgr ;
var rmgr ;
function doInit() {
  var url = "<%=contextPath%>/yh/subsys/oa/hr/recruit/filter/act/YHHrRecruitFilterAct/show.act"; 
  if(seqId){
    var rtJson = getJsonRs(url, "seqId="  + seqId); 
    if (rtJson.rtState == "0") {
      bindJson2Cntrl(rtJson.rtData);
      document.getElementById("seqId").value = seqId;
    } else{  
      alert(rtJson.rtMsrg);
    }  
  }
}

function check(el) {
  var flag = true;
  var cntrl = document.getElementById(el);
  if(!cntrl.value) {
	  alert("标记编号不能为空!");
	  cntrl.focus();
	  flag = false;
  }
  if(!isNumber(cntrl.value)){
	  alert("标记编号必须填入数字!");
	  cntrl.focus();
  	flag = false;
  }
  cntrl = document.getElementById("flagDesc");
  if(!cntrl.value) {
  	alert("标记描述不能为空!");
  	cntrl.focus();
  	flag = false;
  }
  return flag;
}

function commitItem() {
//  if(!check()){
//    return;
//  }
  var url = "";
  if(seqId) {
    url = "<%=contextPath%>/yh/subsys/oa/hr/recruit/filter/act/YHHrRecruitFilterAct/updateField.act";
  }else {
    url = "<%=contextPath%>/yh/subsys/oa/hr/recruit/filter/act/YHHrRecruitFilterAct/addField.act";
  }
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  alert(rtJson.rtMsrg);  
}
function goBack() {
  window.location.href = "<%=contextPath %>/subsys/oa/hr/recruit/filter/list.jsp?pageNo=" + pageNo;
}
</script>
</head>
<body onload="doInit()">
<form name="form1" id="form1" method="post">
  <%
    if(seqId.equals("")) {
  %>   
    <h2><img src="<%=contextPath %>/core/styles/imgs/green_plus.gif"></img>添加</h2> 
  <%
    }else {
  %>    
    <h2><img src="<%=contextPath %>/core/styles/imgs/edit.gif"></img>修改</h2>
  <%
    }
  %>
  <input type="hidden" name="seqId" id="seqId" value="" />
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.subsys.oa.hr.recruit.filter.data.YHHrRecruitFilter"/>
  <table cellscpacing="1" cellpadding="3" width="450">
  <tr class="TableLine1">
    <td>createUserId:</td>
    <td>
    <input type="text" id="createUserId" name="createUserId" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>createDeptId:</td>
    <td>
    <input type="text" id="createDeptId" name="createDeptId" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>expertId:</td>
    <td>
    <input type="text" id="expertId" name="expertId" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>employeeName:</td>
    <td>
    <input type="text" id="employeeName" name="employeeName" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>planNo:</td>
    <td>
    <input type="text" id="planNo" name="planNo" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>position:</td>
    <td>
    <input type="text" id="position" name="position" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>employeeMajor:</td>
    <td>
    <input type="text" id="employeeMajor" name="employeeMajor" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>employeePhone:</td>
    <td>
    <input type="text" id="employeePhone" name="employeePhone" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>transactorStep:</td>
    <td>
    <input type="text" id="transactorStep" name="transactorStep" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>stepFlag:</td>
    <td>
    <input type="text" id="stepFlag" name="stepFlag" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>endFlag:</td>
    <td>
    <input type="text" id="endFlag" name="endFlag" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>nextDateTime:</td>
    <td>
    <input type="text" id="nextDateTime" name="nextDateTime" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>nextTransaStep:</td>
    <td>
    <input type="text" id="nextTransaStep" name="nextTransaStep" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>filterMethod1:</td>
    <td>
    <input type="text" id="filterMethod1" name="filterMethod1" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>filterDateTime1:</td>
    <td>
    <input type="text" id="filterDateTime1" name="filterDateTime1" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>firstContent1:</td>
    <td>
    <input type="text" id="firstContent1" name="firstContent1" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>firstView1:</td>
    <td>
    <input type="text" id="firstView1" name="firstView1" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>transactorStep1:</td>
    <td>
    <input type="text" id="transactorStep1" name="transactorStep1" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>passOrNot1:</td>
    <td>
    <input type="text" id="passOrNot1" name="passOrNot1" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>nextTransaStep1:</td>
    <td>
    <input type="text" id="nextTransaStep1" name="nextTransaStep1" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>nextDateTime1:</td>
    <td>
    <input type="text" id="nextDateTime1" name="nextDateTime1" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>filterMethod2:</td>
    <td>
    <input type="text" id="filterMethod2" name="filterMethod2" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>filterDateTime2:</td>
    <td>
    <input type="text" id="filterDateTime2" name="filterDateTime2" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>firstContent2:</td>
    <td>
    <input type="text" id="firstContent2" name="firstContent2" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>firstView2:</td>
    <td>
    <input type="text" id="firstView2" name="firstView2" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>transactorStep2:</td>
    <td>
    <input type="text" id="transactorStep2" name="transactorStep2" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>passOrNot2:</td>
    <td>
    <input type="text" id="passOrNot2" name="passOrNot2" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>nextTransaStep2:</td>
    <td>
    <input type="text" id="nextTransaStep2" name="nextTransaStep2" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>nextDateTime2:</td>
    <td>
    <input type="text" id="nextDateTime2" name="nextDateTime2" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>filterMethod3:</td>
    <td>
    <input type="text" id="filterMethod3" name="filterMethod3" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>filterDateTime3:</td>
    <td>
    <input type="text" id="filterDateTime3" name="filterDateTime3" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>firstContent3:</td>
    <td>
    <input type="text" id="firstContent3" name="firstContent3" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>firstView3:</td>
    <td>
    <input type="text" id="firstView3" name="firstView3" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>transactorStep3:</td>
    <td>
    <input type="text" id="transactorStep3" name="transactorStep3" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>passOrNot3:</td>
    <td>
    <input type="text" id="passOrNot3" name="passOrNot3" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>nextTransaStep3:</td>
    <td>
    <input type="text" id="nextTransaStep3" name="nextTransaStep3" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>nextDateTime3:</td>
    <td>
    <input type="text" id="nextDateTime3" name="nextDateTime3" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>filterMethod4:</td>
    <td>
    <input type="text" id="filterMethod4" name="filterMethod4" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>filterDateTime4:</td>
    <td>
    <input type="text" id="filterDateTime4" name="filterDateTime4" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>firstContent4:</td>
    <td>
    <input type="text" id="firstContent4" name="firstContent4" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>firstView4:</td>
    <td>
    <input type="text" id="firstView4" name="firstView4" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>transactorStep4:</td>
    <td>
    <input type="text" id="transactorStep4" name="transactorStep4" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>passOrNot4:</td>
    <td>
    <input type="text" id="passOrNot4" name="passOrNot4" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
<%
  if(seqId.equals("")) {
%>
  <tr class="TableLine1">
    <td colspan="3" align="center">
      <input type="button" value="提交" class="SmallButton" onclick="commitItem()">
    </td>
  </tr>
<%
  }else {
%>
  <tr class="TableLine1">
    <td colspan="3" align="center">
      <input type="button" value="提交" class="SmallButton" onclick="commitItem()">
      <input type="button" value="返回" class="SmallButton" onclick="goBack()">
    </td>
  </tr>
<%
  }
%>
  </table>
</form>
</body>
</html>
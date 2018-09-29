<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.core.funcs.person.data.YHPerson"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="yh.subsys.oa.asset.data.YHCpCptlInfo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
YHCpCptlInfo cp = (YHCpCptlInfo)request.getAttribute("cp");
%>
<html>
<head>
<title>增加固定资产记录</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<%if(cp!=null){%>
<script Language="JavaScript"> 
function checkForm() {
  if (document.getElementById("CPTL_NAME").value == "") {
    alert("资产名称不能为空！");
    document.getElementById("CPTL_NAME").focus();
    document.getElementById("CPTL_NAME").select();
    return false;
  }
  if (document.getElementById("cpreQty").value == "") {
    alert("数量不能为空！");
    document.getElementById("cpreQty").focus();
    document.getElementById("cpreQty").select();
    return false;
  }
   return true;
}
function sendForm() {
  if (checkForm()) {
   // var deptId = document.getElementById("DEPT_ID").value;
    //var param = encodeURI(deptId);
    var cpRecord = $('form1').serialize() ;
    var url = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpCptlRecordAct/add.act";
    var json = getJsonRs(url,cpRecord);
    if (json.rtState == "1") {
      alert(json.rtMsrg);
    } else {
      alert("添加成功!");
      window.location = contextPath + "/subsys/oa/asset/query/query.jsp";
    }
  }
}

//验证是否合法
function checkValue() {
  var num = /^[0-9]*$/;
  if (!$("cpreQty").value) { 
    alert("不能为空！");
    selectLast($("cpreQty"));
    document.getElementById("cpreQty").select();
    return false;
  }
  if (!num.exec(document.getElementById("cpreQty").value)) { 
    alert("只能为整数！");
    selectLast($("cpreQty")); 
    document.getElementById("cpreQty").select();
    return false;
  }
}
function  ForDight(Dight,How) {  
  var Dight  =  Math.round(Dight*Math.pow(10,How))/Math.pow(10,How);  
  return  Dight;  
} 
function doInit() {
  dept();
  if(document.getElementById("cptlSpec").value.trim() != ""){
    bindDesc([{cntrlId:"cptlSpec", dsDef:"CP_ASSET_TYPE,SEQ_ID,TYPE_NAME"}]);
  }
}
function dept(){
//得到部门
  var deptId = '1';
  var requestUrl = "<%=contextPath%>/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/selectDeptToGift.act";
  var rtJson = getJsonRs(requestUrl);
  if (rtJson.rtState == "1") {
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData;
  var selects = document.getElementById("DEPT_ID");
  for(var i = 0; i < prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value;
    option.innerHTML = prc.text; 
    selects.appendChild(option);
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInit()">
<form name="form1" id="form1">
<input type="hidden" name="dtoClass" value="yh.subsys.oa.asset.data.YHCpCptlRecord">
      <input type="hidden" name="runId" id="RUN_ID" value="0"/>
      <input type="hidden" name="cpreDate" id="CPRE_DATE" value="<%=sf.format(new Date())%>"/>
      <input type="hidden" name="cptlId" id="cptlId" value="<%=cp.getSeqId()%>"/>
      <input type="hidden" name="cpreKeeper " id="CPRE_KEEPER" value="">
      <input type="hidden" name="cpreRecorder" id="CPRE_RECORDER" value="<%=person.getSeqId()%>">
      <input type="hidden" name="cpreUser" id="CPRE_USER" value="<%=person.getSeqId()%>"/>
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif"><span class="big3"> 增加固定资产使用记录</span>
    </td>
  </tr>
</table>
<br>
  <table class="TableBlock" width="500" align="center">
    <tr>
      <td nowrap class="TableData" width="100">记录类型：</td>
      <td class="TableData">
    <input type="hidden" name="cpreFlag" id="CPRE_FLAG" value="1"/>领用单
      </td>
    </tr>
 
    <tr>
      <td nowrap class="TableData" width="100"> 资产名称：<label style="color: red">*</label></td>
      <td class="TableData"> 
      <select name="cptlName" id="CPTL_NAME">
        <option value="<%=cp.getCptlName().replace("'","")%>"><%=cp.getCptlName()%></option>
      </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 编号：</td>
      <td class="TableData"> 
        <input type="text" name="cptlNo" id="CPTL_NO" size="20" maxlength="23" class="BigInput" value="<%=cp.getCptlNo().replace("'","")%>" readonly>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 资产类别：</td>
      <td class="TableData"> 
       <input type="hidden" name="cptlSpec" id="cptlSpec"size="20" maxlength="23" class="BigInput" value="<%=cp.getCptlSpec()%>"  readonly>
       <input type="text" name="cptlSpecDesc" id="cptlSpecDesc"size="20" maxlength="23" class="BigInput" value="" readonly>
      </td>
    </tr>
 
    <tr>
      <td nowrap class="TableData" width="100"> 部门：</td>
      <td class="TableData">
      <select name="deptId" id="DEPT_ID">
      </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 数量：</td>
      <td class="TableData"> 
       <input type="text" name="cpreQty" id="cpreQty" size="15" maxlength="15" class="BigInput" value="1"  onblur="checkValue()" style="text-align:right;" readonly="readonly"> 数字
      </td>
    </tr>
  <tr>
      <td nowrap class="TableData" width="100"> 地点：</td>
      <td class="TableData"> 
        <input type="text" name="cprePlace" id="CPRE_PLACE" size="25" maxlength="20" class="BigInput" value="">
      </td>
    </tr>
  <tr>
      <td nowrap class="TableData" width="100"> 原因：</td>
      <td class="TableData"> 
        <textarea cols="45" name="cpreReason"  id="CPRE_REASON" rows="3" class="BigInput" wrap="yes"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 备注：</td>
      <td class="TableData"> 
        <textarea cols="45" name="cpreMemo" id="CPRE_MEMO" rows="3" class="BigInput" wrap="yes"></textarea>
      </td>
    </tr>      
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="增加" class="BigButton" onclick="sendForm();">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onClick="history.back();">
      </td>
    </tr>
  </table>
 </form>
 <%}else { %>
 <table class="MessageBox" align="center" width="340">
  <tr>
    <td class='msg info'>
    <div class='content' style='font-size: 12pt'>资源信息已删除</div>
    </td>
  </tr>
</table>
<div align="center"> <input type="button" value="返回" class="BigButton" onClick="history.back();">&nbsp;</div>
 <%} %>
</body>
</html>

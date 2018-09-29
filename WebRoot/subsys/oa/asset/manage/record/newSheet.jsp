<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.core.funcs.person.data.YHPerson"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
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
  var num = /^[0-9]*$/;
  if (!$("cpreQty").value) { 
    alert("数量不能为空！");
    selectLast($("cpreQty"));
    document.getElementById("cpreQty").select();
    return false;
  }
  if (!num.exec(document.getElementById("cpreQty").value)) { 
    alert("数量只能为整数！");
    selectLast($("cpreQty"));
    document.getElementById("cpreQty").select();
    return false;
  }
   return true;
}
function sendForm() {
  if (checkForm()) {
    //var deptId = document.getElementById("DEPT_IDDesc").value;
    //var param = encodeURI(deptId);
    var cpRecord = $('form1').serialize() ;
    var url = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpCptlRecordAct/add.act";
    var json = getJsonRs(url,cpRecord);
    if (json.rtState == "1") {
      alert(json.rtMsrg);
    } else {
      alert("添加成功!");
      window.location = contextPath + "/subsys/oa/asset/manage/record.jsp";
    }
  }
}
function selectData(cpreFlag) {
  //window.showModalDialog(url + paras, window, "dialogWidth:300px;dialogHeight:400px");
  var theURL= "<%=contextPath%>/subsys/oa/asset/asset.jsp?cpreFlag=" + cpreFlag;
  var openWidth = 800;
  var openHeight = 450;
  var loc_x = (screen.availWidth - openWidth) / 2;
  var loc_y = (screen.availHeight - openHeight) / 2;
  window.open(encodeURI(theURL),"selectData","height=500,width=800,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=120,left="+ loc_x +",resizable=yes");
}
function doInit() {
  var useFlag = '<%=request.getParameter("single")%>';
  var url = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpCptlRecordAct/nameList.act?useFlag=" + useFlag;
  var json = getJsonRs(url);
  var rtData = json.rtData;
  for(var i = 0; i < rtData.length; i++) {
    var opt = document.createElement("option");
      opt.value = rtData[i].seqId + ",^," + rtData[i].cptlNo + ",^," + rtData[i].cptlSpec;
      opt.text = rtData[i].cptlName;
      var selectObj = $('CPTL_NAME'); 
      selectObj.options.add(opt, selectObj.options ? selectObj.options.length : 0); 
  }
  dept();
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
function checkId(seqId) {
  if (seqId != null) {
    document.getElementById("cptlId").value = seqId.split(",^,")[0];
    document.getElementById("CPTL_NO").value = seqId.split(",^,")[1];
    document.getElementById("CPTL_SPEC").value = seqId.split(",^,")[2];
    if(document.getElementById("CPTL_SPEC").value.trim() != "") {
      bindDesc([{cntrlId:"CPTL_SPEC", dsDef:"CP_ASSET_TYPE,SEQ_ID,TYPE_NAME"}]);
    }
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInit()">
<form name="form1" id="form1">
      <input type="hidden" name="dtoClass" value="yh.subsys.oa.asset.data.YHCpCptlRecord">
      <input type="hidden" name="runId" id="RUN_ID" value="0"/>
      <input type="hidden" name="cpreDate" id="CPRE_DATE" value="<%=sf.format(new Date())%>"/>
      <input type="hidden" name="cptlId" id="cptlId" value="<%=request.getParameter("cptlId")%>"/>
      <input type="hidden" name="cpreKeeper " id="CPRE_KEEPER" value="">
      <input type="hidden" name="cpreRecorder" id="CPRE_RECORDER" value="<%=person.getSeqId() %>">
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
    <input type="hidden" name="cpreFlag" id="CPRE_FLAG" value="<%=request.getParameter("single")%>"/><%=request.getParameter("sheet")%>
      </td>
    </tr>
 
    <tr>
      <td nowrap class="TableData" width="100"> 资产名称：<label style="color: red">*</label></td>
      <td class="TableData"> 
      <select name="cptlName" id="CPTL_NAME" onChange="checkId(this.value)">
        <option value=""></option>
      </select>
<a href="javascript:selectData('<%=request.getParameter("single")%>');" style="cursor: hand">选择数据</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 编号：</td>
      <td class="TableData"> 
        <input type="text" name="cptlNo" id="CPTL_NO" size="20" maxlength="23" class="BigInput" value="" style="text-align:right;" readonly>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 资产类别：</td>
      <td class="TableData"> 
      <input type="hidden" name="cptlSpec" id="CPTL_SPEC" readonly>
       <input type="text" name="cptlSpec" id="CPTL_SPECDesc" size="20" maxlength="23" class="BigInput" value="" style="text-align:right;" readonly>
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
      <td nowrap class="TableData" width="100"> 数量：<label style="color: red">*</label></td>
      <td class="TableData"> 
       <input type="text" name="cpreQty" id="cpreQty" size="12" maxlength="15" class="BigInput" value="1" /> 数字
      </td>
    </tr>
  <tr>
      <td nowrap class="TableData" width="100"> 地点：</td>
      <td class="TableData"> 
        <input type="text" name="cprePlace" id="CPRE_PLACE" size="30" maxlength="30" class="BigInput" value="">
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
      <td colspan="3" nowrap>
        <input type="button" value="增加" class="BigButton" onclick="sendForm();">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton" onclick="">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onClick="history.back();">
      </td>
    </tr>
  </table>
 </form>
</body>
</html>
